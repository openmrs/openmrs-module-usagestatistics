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
import org.openmrs.module.usagestatistics.UsageStatsService;
import org.openmrs.module.usagestatistics.db.UsageStatsDAO;
import org.openmrs.module.usagestatistics.util.PagingInfo;

/**
 * Implementation of the usage statistics service
 */
public class UsageStatsServiceImpl extends BaseOpenmrsService implements UsageStatsService {

	protected static final Log log = LogFactory.getLog(UsageStatsServiceImpl.class);
	
	protected UsageStatsDAO dao;
	
	/**
	 * Sets the database access object for this service
	 * @param dao the database access object
	 */
	public void setUsageStatsDAO(UsageStatsDAO dao) {
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
	 * @see org.openmrs.module.usagestatistics.UsageStatsService#saveUsage(Usage)
	 */
	public void saveUsage(Usage usage) throws APIException {
		dao.saveUsage(usage);
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.UsageStatsService#getUsages(User, Patient, Date, Date, int, PagingInfo)
	 */
	public List<Usage> getUsages(User user, Patient patient, Date from, Date until, ActionCriteria filter, PagingInfo paging) throws APIException {
		return dao.getUsages(user, patient, from, until, filter, paging);
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.UsageStatsService#getLastUsage(User, Patient, int)
	 */
	public Usage getLastUsage(User user, Patient patient, int maxAgeSecs) throws APIException {
		return dao.getLastUsage(user, patient, maxAgeSecs);
	}

	/**
	 * @see org.openmrs.module.usagestatistics.UsageStatsService#getUsageCount()
	 */
	public int getUsageCount() throws APIException {
		return dao.getUsageCount();
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.UsageStatsService#getAggregateCount()
	 */
	public int getAggregateCount() throws APIException {
		return dao.getAggregateCount();
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.UsageStatsService#getRecordsAccessedCount(Date)
	 */
	public int getRecordsAccessedCount(Date from) throws APIException {
		return dao.getRecordsAccessedCount(from);
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.UsageStatsService#deleteUsages(int)
	 */
	public int deleteUsages(int daysOld) throws APIException {
		// Calculate date which is daysOld before today
		Calendar until = new GregorianCalendar();
		until.add(Calendar.DATE, -daysOld);
		
		return dao.deleteUsages(until.getTime());
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.UsageStatsService#getLocationsStats(int)
	 */
	public List<Object[]> getLocationsStats(Date from, Date until, ActionCriteria filter) throws APIException {
		return dao.getLocationsStats(from, until, filter);
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.UsageStatsService#getRolesStats(Location)
	 */
	public List<Object[]> getRolesStats(Date from, Date until, Location location, ActionCriteria filter) throws APIException {
		return dao.getRolesStats(from, until, location, filter);
	}

	/**
	 * @see org.openmrs.module.usagestatistics.UsageStatsService#getUsersStats(int,String,int)
	 */
	public List<Object[]> getUsersStats(Date from, Date until, Location location, String role, ActionCriteria filter) throws APIException {
		return dao.getUsersStats(from, until, location, role, filter);
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.UsageStatsService#getMostActiveLocations(Date, int)
	 */
	public List<Object[]> getMostActiveLocations(Date since, int maxResults) throws APIException {
		return dao.getMostActiveLocations(since, maxResults);
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.db.UsageStatsDAO#getMostActiveUsers(Date, int)
	 */
	public List<Object[]> getMostActiveUsers(Date since, int maxResults) throws APIException {
		return dao.getMostActiveUsers(since, maxResults);
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.db.UsageStatsDAO#getMostCommonForms(Date, int)
	 */
	public List<Object[]> getMostCommonForms(Date since, int maxResults) throws APIException {
		return dao.getMostCommonForms(since, maxResults);
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.db.UsageStatsDAO#getMostCommonEncounterTypes(Date, boolean, int)
	 */
	public List<Object[]> getMostCommonEncounterTypes(Date since, boolean formless, int maxResults) throws APIException {
		return dao.getMostCommonEncounterTypes(since, formless, maxResults);
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.UsageStatsService#getFoundByStats(Date, Date, Location, ActionCriteria)
	 */
	public int[] getFoundByStats(Date from, Date until, Location location, ActionCriteria filter) throws APIException {
		return dao.getFoundByTotals(from, until, location, filter);
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.UsageStatsService#getDateRangeStats(Date, Date, Location)
	 */
	public List<Object[]> getDateRangeStats(Date from, Date until, Location location) throws APIException {
		return dao.getDateRangeStats(from, until, location);
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.UsageStatsService#getHourStats(org.openmrs.User)
	 */
	public int[][] getHourStats(Date from, Date until, Location location) throws APIException {
		return getTimeBasedTotals(from, until, location, "hour", 24);
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.UsageStatsService#getHourStats(org.openmrs.User)
	 */
	public int[][] getDayOfWeekStats(Date from, Date until, Location location) throws APIException {
		return getTimeBasedTotals(from, until, location, "dayofweek", 7);
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.UsageStatsService#aggregateUsages()
	 */
	public int aggregateUsages() throws APIException {
		return dao.aggregateUsages();
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.UsageStatsService#isPatientVoidedInDatabase(org.openmrs.Patient)
	 */
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
	public int[][] getTimeBasedTotals(Date from, Date until, Location location, String func, int categories) {
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
