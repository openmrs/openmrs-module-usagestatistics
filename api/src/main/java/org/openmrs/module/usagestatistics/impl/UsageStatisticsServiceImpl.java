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
package org.openmrs.module.usagestatistics.impl;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.api.APIException;
import org.openmrs.api.db.DAOException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.usagestatistics.Usage;
import org.openmrs.module.usagestatistics.ActionCriteria;
import org.openmrs.module.usagestatistics.UsageLog;
import org.openmrs.module.usagestatistics.UsageStatisticsService;
import org.openmrs.module.usagestatistics.db.UsageStatisticsDAO;
import org.openmrs.module.usagestatistics.util.PagingInfo;

/**
 * Implementation of the usage statistics service
 */
public class UsageStatisticsServiceImpl extends BaseOpenmrsService implements UsageStatisticsService {

	protected static final Log log = LogFactory.getLog(UsageStatisticsServiceImpl.class);
	
	protected UsageStatisticsDAO dao;
	
	/**
	 * Sets the database access object for this service
	 * @param dao the database access object
	 */
	public void setUsageStatsDAO(UsageStatisticsDAO dao) {
		this.dao = dao;
	}
	
	/**
	 * @see org.openmrs.api.OpenmrsService#onStartup()
	 */
	@Override
	public void onStartup() {
		// TODO This never gets called.. bug?
		log.info("Starting usage statistics service");
	}

	/**
	 * @see org.openmrs.api.OpenmrsService#onShutdown()
	 */
	@Override
	public void onShutdown() {
		// TODO This never gets called.. bug?
		log.info("Shutting down usage statistics service");
	}

	/**
	 * @see org.openmrs.module.usagestatistics.UsageStatisticsService#saveUsage(Usage)
	 */
	@Override
	public void saveUsage(Usage usage) throws APIException {
		dao.saveUsage(usage);
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.UsageStatisticsService#getUsages(User, Patient, Date, Date, int, PagingInfo)
	 */
	@Override
	public List<Usage> getUsages(User user, Patient patient, Date from, Date until, ActionCriteria filter, PagingInfo paging) throws APIException {
		return dao.getUsages(user, patient, from, until, filter, paging);
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.UsageStatisticsService#getLastUsage(User, Patient, int)
	 */
	@Override
	public Usage getLastUsage(User user, Patient patient, int maxAgeSecs) throws APIException {
		return dao.getLastUsage(user, patient, maxAgeSecs);
	}

	/**
	 * @see org.openmrs.module.usagestatistics.UsageStatisticsService#getUsageCount()
	 */
	@Override
	public int getUsageCount() throws APIException {
		return dao.getUsageCount();
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.UsageStatisticsService#getAggregateCount()
	 */
	@Override
	public int getAggregateCount() throws APIException {
		return dao.getAggregateCount();
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.UsageStatisticsService#getOnlineUserCount()
	 */
	@Override
	public int getOnlineUserCount() throws APIException {
		return UsageLog.getOnlineUserCount();
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.UsageStatisticsService#getRecordsAccessedCount(Date)
	 */
	@Override
	public int getOpenRecordsCount() throws APIException {
		Date tenMinsAgo = new Date(System.currentTimeMillis() - (10 * 60 * 1000));
		return dao.getRecordsAccessedCount(tenMinsAgo);
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.UsageStatisticsService#deleteUsages(int)
	 */
	@Override
	public int deleteUsages(int daysOld) throws APIException {
		// Calculate date which is daysOld before today
		Calendar until = new GregorianCalendar();
		until.add(Calendar.DATE, -daysOld);
		
		return dao.deleteUsages(until.getTime());
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.UsageStatisticsService#getLocationsStats(int)
	 */
	@Override
	public List<Object[]> getLocationsStats(Date from, Date until, ActionCriteria filter) throws APIException {
		return dao.getLocationsStats(from, until, filter);
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.UsageStatisticsService#getRolesStats(Location)
	 */
	@Override
	public List<Object[]> getRolesStats(Date from, Date until, Location location, ActionCriteria filter) throws APIException {
		return dao.getRolesStats(from, until, location, filter);
	}

	/**
	 * @see org.openmrs.module.usagestatistics.UsageStatisticsService#getUsersStats(int,String,int)
	 */
	@Override
	public List<Object[]> getUsersStats(Date from, Date until, Location location, String role, ActionCriteria filter) throws APIException {
		return dao.getUsersStats(from, until, location, role, filter);
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.UsageStatisticsService#getMostActiveLocations(Date, int)
	 */
	@Override
	public List<Object[]> getMostActiveLocations(Date since, int maxResults) throws APIException {
		return dao.getMostActiveLocations(since, maxResults);
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.db.UsageStatisticsDAO#getMostActiveUsers(Date, int)
	 */
	@Override
	public List<Object[]> getMostActiveUsers(Date since, int maxResults) throws APIException {
		return dao.getMostActiveUsers(since, maxResults);
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.db.UsageStatisticsDAO#getMostCommonForms(Date, int)
	 */
	@Override
	public List<Object[]> getMostCommonForms(Date since, int maxResults) throws APIException {
		return dao.getMostCommonForms(since, maxResults);
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.db.UsageStatisticsDAO#getMostCommonEncounterTypes(Date, boolean, int)
	 */
	@Override
	public List<Object[]> getMostCommonEncounterTypes(Date since, boolean formless, int maxResults) throws APIException {
		return dao.getMostCommonEncounterTypes(since, formless, maxResults);
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.UsageStatisticsService#getFoundByStats(Date, Date, Location, ActionCriteria)
	 */
	@Override
	public int[] getFoundByStats(Date from, Date until, Location location, ActionCriteria filter) throws APIException {
		return dao.getFoundByTotals(from, until, location, filter);
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.UsageStatisticsService#getDateRangeStats(Date, Date, Location)
	 */
	@Override
	public List<Object[]> getDateRangeStats(Date from, Date until, Location location) throws APIException {
		return dao.getDateRangeStats(from, until, location);
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.UsageStatisticsService#getHourStats(org.openmrs.User)
	 */
	@Override
	public int[][] getHourStats(Date from, Date until, Location location) throws APIException {
		return getTimeBasedTotals(from, until, location, "hour", 24);
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.UsageStatisticsService#getHourStats(org.openmrs.User)
	 */
	@Override
	public int[][] getDayOfWeekStats(Date from, Date until, Location location) throws APIException {
		return getTimeBasedTotals(from, until, location, "dayofweek", 7);
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.UsageStatisticsService#aggregateUsages()
	 */
	@Override
	public int aggregateUsages() throws APIException {
		return dao.aggregateUsages();
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.UsageStatisticsService#isPatientVoidedInDatabase(org.openmrs.Patient)
	 */
	@Override
	public boolean isPatientVoidedInDatabase(Patient patient) throws APIException {
		return dao.isPatientVoidedInDatabase(patient);
	}
	
	/**
	 * Gets time based usage statistics
	 * @param from the start date
	 * @param until the end date
	 * @param location the location (if null then all locations)
	 * @param func the function name applied to each event timestamp
	 * @param categories the number of categories func will produce (e.g. hour(...) gives 24)
	 * @return an array of hour values and statistics
	 * @throws DAOException
	 */
	private int[][] getTimeBasedTotals(Date from, Date until, Location location, String func, int categories) {
		List<Object[]> all = dao.getTimeBasedTotals(from, until, location, false, func);
		List<Object[]> updates = dao.getTimeBasedTotals(from, until, location, true, func);
		
		// Combine views and updates into single array
		int[][] stats = new int[categories][2];
		
		// Special case because categories aren't zero-based, i.e. Sunday = 1, Monday = 2, ...
		boolean dayOfWeek = func.equalsIgnoreCase("dayofweek"); 
		
		for (Object[] row : all) {
			int category = (Integer)row[0];
			if (dayOfWeek)
				category--;
			stats[category][0] = ((BigInteger)row[1]).intValue();
		}

		for (Object[] row : updates) {
			int category = (Integer)row[0];
			if (dayOfWeek)
				category--;
			stats[category][1] = ((BigInteger)row[1]).intValue();
		}
		
		return stats;
	}
}
