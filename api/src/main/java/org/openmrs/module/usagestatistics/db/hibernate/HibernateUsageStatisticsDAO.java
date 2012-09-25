/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.usagestatistics.db.hibernate;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openmrs.EncounterType;
import org.openmrs.Form;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.usagestatistics.Constants;
import org.openmrs.module.usagestatistics.Usage;
import org.openmrs.module.usagestatistics.Options;
import org.openmrs.module.usagestatistics.ActionCriteria;
import org.openmrs.module.usagestatistics.db.UsageStatisticsDAO;
import org.openmrs.module.usagestatistics.util.PagingInfo;
import org.openmrs.module.usagestatistics.util.StatsUtils;

/**
 * Hibernate data access layer
 */
public class HibernateUsageStatisticsDAO implements UsageStatisticsDAO {
	
	protected static final Log log = LogFactory.getLog(HibernateUsageStatisticsDAO.class);
	
	protected SessionFactory sessionFactory;
	protected static final String TABLE_USAGE = Constants.MODULE_ID + "_usage";
	protected static final String TABLE_DAILY = Constants.MODULE_ID + "_daily";
	protected static final String TABLE_ENCOUNTER = Constants.MODULE_ID + "_encounter";
	protected static final SimpleDateFormat dfSQL = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * Set session factory
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory) { 
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.db.UsageStatisticsDAO#saveUsage(Usage)
	 */
	public void saveUsage(Usage event) throws DAOException {
		sessionFactory.getCurrentSession().saveOrUpdate(event);
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.db.UsageStatisticsDAO#getUsages(User, Patient, Date, Date, boolean, PagingInfo)
	 */
	@SuppressWarnings("unchecked")
	public List<Usage> getUsages(User user, Patient patient, Date from, Date until, ActionCriteria filter, PagingInfo paging) throws DAOException {		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT SQL_CALC_FOUND_ROWS {s.*} ");
		sb.append("FROM " + TABLE_USAGE + " s ");
		sb.append("WHERE 1=1 ");
		
		if (user != null)
			sb.append("  AND s.user_id = " + user.getUserId() + " ");
		if (patient != null)
			sb.append("  AND s.patient_id = " + patient.getPatientId() + " ");
		if (from != null)
			sb.append("  AND s.timestamp > '" + dfSQL.format(from) + "' ");
		if (until != null)
			sb.append("  AND s.timestamp < '" + dfSQL.format(until) + "' ");
		
		if (filter == ActionCriteria.CREATED)
			sb.append("  AND s.created = 1 ");
		else if (filter == ActionCriteria.ENCOUNTER)
			sb.append("  AND s.usage_id IN (SELECT usage_id FROM " + TABLE_ENCOUNTER + ") ");
		else if (filter == ActionCriteria.UPDATED)
			sb.append("  AND s.updated = 1 ");
		else if (filter == ActionCriteria.VOIDED)
			sb.append("  AND s.voided = 1 ");
		
		sb.append("ORDER BY s.timestamp DESC ");
		
		sb.append("LIMIT " + paging.getPageOffset() + ", " + paging.getPageSize() + ";");
		
		Session session = sessionFactory.getCurrentSession();
		
		List<Usage> results = sessionFactory.getCurrentSession().createSQLQuery(sb.toString())
			.addEntity("s", Usage.class)
			.list();
		
		int count = ((Number)session.createSQLQuery("SELECT FOUND_ROWS();").uniqueResult()).intValue();
		paging.setResultsTotal(count);
		return results;
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.db.UsageStatisticsDAO#getLastUsage(User, Patient, int)
	 */
	@SuppressWarnings("unchecked")
	public Usage getLastUsage(User user, Patient patient, int maxAgeSecs) throws DAOException {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(Usage.class);
		
		if (user != null)
			query.add(Restrictions.eq("user", user));
		if (patient != null)
			query.add(Restrictions.eq("patient", patient));
		if (maxAgeSecs != 0) {
			Date from = StatsUtils.addSecondsToDate(null, -maxAgeSecs);	
			query.add(Restrictions.ge("timestamp", from));
		}
		
		List<Usage> events = query.addOrder(Order.desc("timestamp")).setMaxResults(1).list();
		return events.size() == 1 ? events.get(0) : null;
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.db.UsageStatisticsDAO#getUsageCount()
	 */
	public int getUsageCount() throws DAOException {
		Session session = sessionFactory.getCurrentSession();
		Number res = (Number)session.createSQLQuery("SELECT COUNT(*) FROM " + TABLE_USAGE + ";")
			.uniqueResult();
		return res.intValue();
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.db.UsageStatisticsDAO#getAggregateCount()
	 */
	public int getAggregateCount() throws DAOException {
		Session session = sessionFactory.getCurrentSession();
		Number res = (Number)session.createSQLQuery("SELECT COUNT(*) FROM " + TABLE_DAILY + ";")
			.uniqueResult();
		return res.intValue();
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.db.UsageStatisticsDAO#getAggregateCount()
	 */
	public int getRecordsAccessedCount(Date from) throws DAOException {
		Session session = sessionFactory.getCurrentSession();
		String sql = "SELECT COUNT(DISTINCT patient_id) FROM " + TABLE_USAGE + " WHERE timestamp >= FROM_UNIXTIME(" + (from.getTime() / 1000) + ");";
		Number res = (Number)session.createSQLQuery(sql).uniqueResult();
		return res.intValue();
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.db.UsageStatisticsDAO#deleteUsages(Date)
	 */
	public int deleteUsages(Date until) throws DAOException {
		String sql = "DELETE FROM " + TABLE_USAGE + " WHERE timestamp < '" + dfSQL.format(until) + "';";
		
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		return query.executeUpdate();
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.db.UsageStatisticsDAO#getLocationsStats(int)
	 */
	public List<Object[]> getLocationsStats(Date from, Date until, ActionCriteria filter) throws DAOException {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT l.location_id, l.name, ");
		sb.append("  COUNT(DISTINCT d.user_id) as `active_users`, ");
		sb.append("  SUM(d.usages) as `usages`, ");
		sb.append("  SUM(d.creates) as `creates`, ");
		sb.append("  SUM(d.encounters) as `encounters`, ");
		sb.append("  SUM(d.updates) as `updates`, ");
		sb.append("  SUM(d.voids) as `voids`, ");
		sb.append("  MAX(d.date) as `last_usage` ");
		sb.append("FROM location l ");
		sb.append("LEFT OUTER JOIN " + TABLE_DAILY + " d ON d.location_id = l.location_id ");
		
		return completeAggregateQuery(sb, from, until, null, "l.location_id", filter, "l.name");
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.db.UsageStatisticsDAO#getRolesStats(Location)
	 */
	public List<Object[]> getRolesStats(Date from, Date until, Location location, ActionCriteria filter) throws DAOException {	
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT r.role, ");
		sb.append("  COUNT(DISTINCT d.user_id) as `active_users`, ");
		sb.append("  SUM(d.usages) as `usages`, ");
		sb.append("  SUM(d.creates) as `creates`, ");
		sb.append("  SUM(d.encounters) as `encounters`, ");
		sb.append("  SUM(d.updates) as `updates`, ");
		sb.append("  SUM(d.voids) as `voids`, ");
		sb.append("  MAX(d.date) as `last_usage` ");
		sb.append("FROM role r ");
		sb.append("LEFT OUTER JOIN user_role ur ON ur.role = r.role ");
		sb.append("LEFT OUTER JOIN " + TABLE_DAILY + " d ON d.user_id = ur.user_id ");
		
		return completeAggregateQuery(sb, from, until, location, "r.role", filter, "r.role");
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.db.UsageStatisticsDAO#getUsersStats(Location, String, int)
	 */
	public List<Object[]> getUsersStats(Date from, Date until, Location location, String role, ActionCriteria filter) throws DAOException {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ");
		sb.append("  u.user_id, ");
		sb.append("  CONCAT(IFNULL(n.given_name,''), ' ', IFNULL(n.middle_name,''), ' ', IFNULL(n.family_name,'')) AS user_name, ");
		sb.append("  SUM(d.usages) as `usages`, ");
		sb.append("  SUM(d.creates) as `creates`, ");
		sb.append("  SUM(d.encounters) as `encounters`, ");
		sb.append("  SUM(d.updates) as `updates`, ");
		sb.append("  SUM(d.voids) as `voids`, ");
		sb.append("  MAX(d.date) as `last_usage` ");
		sb.append("FROM users u ");
		sb.append("INNER JOIN person_name n ON n.person_id = u.person_id ");
		
		if (role != null)
			sb.append("INNER JOIN user_role ur ON ur.user_id = u.user_id AND ur.role = '" + role + "' ");
		
		sb.append("LEFT OUTER JOIN " + TABLE_DAILY + " d ON d.user_id = u.user_id ");
		
		return completeAggregateQuery(sb, from, until, location, "u.user_id", filter, "user_name");
	}
	
	/**
	 * Utility method to complete and execute a query from the daily aggregates table
	 * @param sb the string buffer containing the query so far
	 * @param from the start date (may be null)
	 * @param until the end date (may be null)
	 * @param location the location (if null then all locations)
	 * @param groupBy the column to group by
	 * @param filter the usage actions filter
	 * @param orderBy the column to order by
	 * @return the array of statistics
	 */
	private List<Object[]> completeAggregateQuery(StringBuffer sb, Date from, Date until, Location location, String groupBy, ActionCriteria filter, String orderBy) {
		if (from != null)
			sb.append("  AND d.date >= '" + dfSQL.format(from) + "' ");
		if (until != null)
			sb.append("  AND d.date < '" + dfSQL.format(until) + "' ");
		
		if (location != null)
			sb.append("WHERE d.location_id = " + location.getLocationId() + " ");
		
		sb.append("GROUP BY " + groupBy + " ");
			
		if (filter == ActionCriteria.ANY)
			sb.append("HAVING usages > 0 ");
		else if (filter == ActionCriteria.CREATED)
			sb.append("HAVING creates > 0 ");
		else if (filter == ActionCriteria.ENCOUNTER)
			sb.append("HAVING encounters > 0 ");
		else if (filter == ActionCriteria.UPDATED)
			sb.append("HAVING updates > 0 ");
		else if (filter == ActionCriteria.VOIDED)
			sb.append("HAVING voids > 0 ");
		
		sb.append("ORDER BY " + orderBy + ";");
		
		return executeSQLQuery(sb.toString());
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.db.UsageStatisticsDAO#getMostActiveLocations(Date, int)
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getMostActiveLocations(Date since, int maxResults) throws DAOException {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ");
		sb.append("  {l.*}, ");
		sb.append("  ud.usages as `count` ");
		sb.append("FROM ( ");
		sb.append("  SELECT location_id, SUM(usages) as usages ");
		sb.append("  FROM " + TABLE_DAILY + " ");
		
		if (since != null)
			sb.append("  WHERE `date` > '" + dfSQL.format(since) + "' ");
		
		sb.append("  GROUP BY location_id ");
		sb.append(") ud ");
		sb.append("INNER JOIN location l ON l.location_id = ud.location_id ");
		sb.append("ORDER BY usages DESC ");
		sb.append("LIMIT " + maxResults + ";");
		
		return sessionFactory.getCurrentSession().createSQLQuery(sb.toString())
			.addEntity("l", Location.class)
			.addScalar("count")
			.list();
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.db.UsageStatisticsDAO#getMostActiveUsers(Date, int)
	 */
	public List<Object[]> getMostActiveUsers(Date since, int maxResults) throws DAOException {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ");
		sb.append("  ud.user_id, ");
		sb.append("  CONCAT(n.given_name, ' ', n.middle_name, ' ', n.family_name) AS user_name, ");
		sb.append("  ud.usages ");
		sb.append("FROM ( ");
		sb.append("  SELECT user_id, SUM(usages) as usages ");
		sb.append("  FROM " + TABLE_DAILY + " ");
		
		if (since != null)
			sb.append("  WHERE `date` > '" + dfSQL.format(since) + "' ");
		
		sb.append("  GROUP BY user_id ");
		sb.append(") ud ");
		sb.append("INNER JOIN users u ON ud.user_id = u.user_id ");
		sb.append("INNER JOIN person_name n ON n.person_id = u.person_id ");
		sb.append("ORDER BY usages DESC ");
		sb.append("LIMIT " + maxResults + ";");
		
		return executeSQLQuery(sb.toString());
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.db.UsageStatisticsDAO#getMostCommonForms(Date, int)
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getMostCommonForms(Date since, int maxResults) throws DAOException {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT {f.*}, COUNT(e.form_id) as `count` ");
		sb.append("FROM encounter e ");
		sb.append("INNER JOIN form f ON f.form_id = e.form_id ");
		sb.append("WHERE e.date_created > '" + dfSQL.format(since) + "' ");
		sb.append("GROUP BY e.form_id ORDER BY `count` DESC LIMIT " + maxResults + ";");
		
		return sessionFactory.getCurrentSession().createSQLQuery(sb.toString())
			.addEntity("f", Form.class)
			.addScalar("count")
			.list();
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.db.UsageStatisticsDAO#getMostCommonForms(Date, boolean, int)
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getMostCommonEncounterTypes(Date since, boolean formless, int maxResults) throws DAOException {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT {et.*}, COUNT(e.encounter_id) as `count` ");
		sb.append("FROM encounter e ");
		sb.append("INNER JOIN encounter_type et ON et.encounter_type_id = e.encounter_type ");
		sb.append("WHERE e.date_created > '" + dfSQL.format(since) + "' ");
		
		if (formless)
			sb.append("  AND e.form_id IS NULL ");
		
		sb.append("GROUP BY e.encounter_type ORDER BY `count` DESC LIMIT " + maxResults + ";");
		
		return sessionFactory.getCurrentSession().createSQLQuery(sb.toString())
			.addEntity("et", EncounterType.class)
			.addScalar("count")
			.list();
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.db.UsageStatisticsDAO#getFoundByTotals(Date, Date, Location, ActionCriteria)
	 */
	public int[] getFoundByTotals(Date from, Date until, Location location, ActionCriteria filter) throws DAOException {
		int[] totals = new int[4];
		totals[1] = getFoundByTotal(from, until, location, filter, Usage.FOUNDBY_LINK);
		totals[2] = getFoundByTotal(from, until, location, filter, Usage.FOUNDBY_ID_QUERY);
		totals[3] = getFoundByTotal(from, until, location, filter, Usage.FOUNDBY_NAME_QUERY);
		totals[0] = (totals[1] + totals[2] + totals[3]);
		
		return totals;
	}
	
	/**
	 * Calculates the total usage events with the given "found by" option
	 * @param from the start date
	 * @param until the end date
	 * @param location the location (if null then all locations)
	 * @param filter the types of actions to include
	 * @param foundBy the found by option
	 * @return the total number of events
	 */
	protected int getFoundByTotal(Date from, Date until, Location location, ActionCriteria filter, int foundBy) {
		int locationAttrTypeId = Options.getInstance().getLocationAttributeTypeId();
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT COUNT(*) ");
		sb.append("FROM " + TABLE_USAGE + " e ");
		
		// Check user location
		if (location != null) {
			sb.append("INNER JOIN users us ON e.user_id = us.user_id ");
			sb.append("INNER JOIN person_attribute pa ON (pa.person_id = us.person_id) AND pa.voided = 0 ");
			sb.append("  AND pa.person_attribute_type_id = " + locationAttrTypeId + " AND pa.value = \"" + location.getLocationId() + "\" ");
		}
		
		sb.append("WHERE e.found_by = " + foundBy + " ");
		
		if (filter == ActionCriteria.CREATED)
			sb.append("AND e.created = 1 ");
		else if (filter == ActionCriteria.ENCOUNTER)
			sb.append("AND e.usage_id IN (SELECT usage_id FROM " + TABLE_ENCOUNTER + ") ");
		else if (filter == ActionCriteria.UPDATED)
			sb.append("AND e.updated = 1 ");
		else if (filter == ActionCriteria.VOIDED)
			sb.append("AND e.voided = 1 ");
		
		appendDateRange(sb, "e", from, until);
		
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		return ((BigInteger)query.uniqueResult()).intValue();
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.db.UsageStatisticsDAO#getDateRangeStats(Date, Date, Location)
	 */
	public List<Object[]> getDateRangeStats(Date from, Date until, Location location) throws DAOException {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ");
		sb.append("  `date`, ");
		sb.append("  SUM(usages) as usages, ");
		sb.append("  SUM(encounters) as encounters, ");
		sb.append("  SUM(updates) as updates ");
		sb.append("FROM " + TABLE_DAILY + " ");
		sb.append("WHERE 1=1 ");
		
		if (from != null)
			sb.append("  AND date > '" + dfSQL.format(from) + "' ");
		if (until != null)
			sb.append("  AND date < '" + dfSQL.format(until) + "' ");
		if (location != null)
			sb.append("  AND location_id = " + location.getLocationId() + " ");
		
		sb.append("GROUP BY `date` ");
		sb.append("ORDER BY `date` ASC;");
		
		return executeSQLQuery(sb.toString());
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.db.UsageStatisticsDAO#getTimeBasedTotals(Date, Date, Location, boolean, String)
	 */
	public List<Object[]> getTimeBasedTotals(Date from, Date until, Location location, boolean onlyUpdates, String func) {
		int locationAttrTypeId = Options.getInstance().getLocationAttributeTypeId();
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ");
		sb.append("  " + func + "(e.`timestamp`) as `hour`, ");
		sb.append("  COUNT(*) as `records` ");
		sb.append("FROM " + TABLE_USAGE + " e ");
		
		// Check user location for views
		if (location != null) {
			sb.append("INNER JOIN users us ON e.user_id = us.user_id ");
			sb.append("INNER JOIN person_attribute pa ON (pa.person_id = us.person_id) AND pa.voided = 0 ");
			sb.append("  AND pa.person_attribute_type_id = " + locationAttrTypeId + " AND pa.value = \"" + location.getLocationId() + "\" ");
		}
		
		sb.append("WHERE 1=1 ");
		
		if (onlyUpdates)
			sb.append("AND e.updated = 1 ");
		
		appendDateRange(sb, "e", from, until);
		
		sb.append("GROUP BY " + func + "(e.`timestamp`) ");
		sb.append("ORDER BY " + func + "(e.`timestamp`); ");
		
		return executeSQLQuery(sb.toString());
	}
	
	/**
	 * Utility method to add date range condition to a join or where
	 * @param sb the string buffer to write SQL to
	 * @param alias the table alias
	 * @param from the start date
	 * @param until the end date
	 */
	private void appendDateRange(StringBuffer sb, String alias, Date from, Date until) {
		if (from != null)
			sb.append(" AND " + alias + ".timestamp >= '" + dfSQL.format(from) + "' ");
		if (until != null)		
			sb.append(" AND " + alias + ".timestamp < '" + dfSQL.format(until) + "' ");
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.db.UsageStatisticsDAO#aggregateUsages()
	 */
	public int aggregateUsages() throws DAOException {
		int locationAttrTypeId = Options.getInstance().getLocationAttributeTypeId();
		Session session = sessionFactory.getCurrentSession();
		
		// Get last date in aggregate table
		String sql = "SELECT MAX(date) FROM " + TABLE_DAILY + ";";
		Date lastDate = (Date)session.createSQLQuery(sql).uniqueResult();
		
		// Delete all data from that last date as it may be incomplete
		if (lastDate != null) {
			sql = "DELETE FROM " + TABLE_DAILY + " WHERE `date` = '" + dfSQL.format(lastDate) + "';";	
			session.createSQLQuery(sql).executeUpdate();
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("INSERT INTO " + TABLE_DAILY + " SELECT ");
		sb.append("  DATE(u.timestamp) as `date`, ");
		sb.append("  u.user_id, ");
		sb.append("  pa.value as `location_id`, ");
		sb.append("  COUNT(*) as `usages`, ");
		sb.append("  COUNT(uc.usage_id) as `creates`, ");
		sb.append("  COUNT(e.encounter_id) as `encounters`, ");
		sb.append("  COUNT(uu.usage_id) as `updates`, ");
		sb.append("  COUNT(uv.usage_id) as `voids` ");
		sb.append("FROM " + TABLE_USAGE + " u ");
		sb.append("LEFT OUTER JOIN users us ON u.user_id = us.user_id ");
		sb.append("LEFT OUTER JOIN person_attribute pa ON (pa.person_id = us.person_id) AND pa.voided = 0 AND pa.value != '' ");
		sb.append("  AND pa.person_attribute_type_id = " + locationAttrTypeId + " ");
		
		sb.append("LEFT OUTER JOIN " + TABLE_USAGE + " uc ON uc.usage_id = u.usage_id AND uc.created = 1 ");
		sb.append("LEFT OUTER JOIN " + TABLE_USAGE + " uu ON uu.usage_id = u.usage_id AND uu.updated = 1 ");
		sb.append("LEFT OUTER JOIN " + TABLE_USAGE + " uv ON uv.usage_id = u.usage_id AND uv.voided = 1 ");	
		sb.append("LEFT OUTER JOIN " + TABLE_ENCOUNTER + " e ON e.usage_id = u.usage_id ");
		
		if (lastDate != null)
			sb.append("WHERE DATE(u.timestamp) >= '" + dfSQL.format(lastDate) + "' ");
		
		sb.append("GROUP BY DATE(u.timestamp), u.user_id;");
		
		return executeJDBCUpdate(sb.toString());
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.db.UsageStatisticsDAO#isPatientVoidedInDatabase(org.openmrs.Patient)
	 */
	public boolean isPatientVoidedInDatabase(Patient patient) throws DAOException {
		String sql = "SELECT voided FROM patient WHERE patient_id = " + patient.getPatientId() + ";";
		Number res = (Number)sessionFactory.getCurrentSession().createSQLQuery(sql).uniqueResult();
		return res.intValue() == 1;
	}

	/**
	 * Utility method to execute a native SQL query in the current Hibernate session
	 * @param sql the native SQL query to execute
	 * @return a list of object arrays for each row
	 */
	@SuppressWarnings("unchecked")
	protected List<Object[]> executeSQLQuery(String sql) {	
		Session session = sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql);
		return query.list();
	}
	
	/**
	 * Utility method to execute a native SQL query in JDBC
	 * @param sql the native SQL query to execute
	 * @return a list of object arrays for each row
	 */
	protected int executeJDBCUpdate(String sql) {	
		Connection conn = sessionFactory.getCurrentSession().connection();
		try {
			Statement ps = conn.createStatement();
			return ps.executeUpdate(sql);
		} catch (SQLException ex) {
			throw new DAOException(ex);
		}
	}
}
