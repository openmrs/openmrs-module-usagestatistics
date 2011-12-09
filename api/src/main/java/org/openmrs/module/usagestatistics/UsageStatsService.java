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
package org.openmrs.module.usagestatistics;

import java.util.Date;
import java.util.List;

import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.usagestatistics.Usage;
import org.openmrs.module.usagestatistics.util.PagingInfo;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UsageStatsService extends OpenmrsService {
	
	/**
	 * Saves a usage to the database
	 * @param usage the usage to save
	 * @throws APIException
	 */
	public void saveUsage(Usage usage) throws APIException;
	
	/**
	 * Gets raw usage records
	 * @param user the user (may be null)
	 * @param patient the patient (may be null)
	 * @param from the minimum date/time (may be null)
	 * @param until the maximum date/time (may be null)
	 * @param filter the types of actions to include
	 * @param paging the paging info
	 * @return a list of usage events
	 * @throws APIException
	 */
	@Authorized({Constants.PRIV_VIEW_USAGE_STATS})
	@Transactional(readOnly = true)
	public List<Usage> getUsages(User user, Patient patient, Date from, Date until, ActionCriteria filter, PagingInfo paging) throws APIException;
	
	/**
	 * Gets the last matching usage event
	 * @param user the user (may be null)
	 * @param patient the patient (may be null)
	 * @param maxAgeSecs the maximum age of the returned event in seconds (0 to ignore)
	 * @return the usage event or null if there are no matches
	 * @throws APIException
	 */
	public Usage getLastUsage(User user, Patient patient, int maxAgeSecs) throws APIException;
	
	/**
	 * Gets the total number of usages
	 * @return the number of usage records
	 * @throws APIException
	 */
	@Authorized({Constants.PRIV_VIEW_USAGE_STATS})
	@Transactional(readOnly = true)
	public int getUsageCount() throws APIException;
	
	/**
	 * Gets the total number of aggregates
	 * @return the number of aggregate records
	 * @throws APIException
	 */
	@Authorized({Constants.PRIV_VIEW_USAGE_STATS})
	@Transactional(readOnly = true)
	public int getAggregateCount() throws APIException;
	
	/**
	 * Gets the number of online users
	 * @return the number of online users
	 * @throws APIException
	 */
	@Authorized({Constants.PRIV_VIEW_USAGE_STATS})
	@Transactional(readOnly = true)
	public int getOnlineUserCount() throws APIException;
	
	/**
	 * Gets the total number of different patient records that have been accessed since the given time
	 * @param from the start time
	 * @return the number of patient records
	 * @throws APIException
	 */
	@Authorized({Constants.PRIV_VIEW_USAGE_STATS})
	@Transactional(readOnly = true)
	public int getOpenRecordsCount() throws APIException;
	
	/**
	 * Deletes usages which are older than the given number of days
	 * @param daysOld the number of days
	 * @return the number of usages that were deleted
	 * @throws APIException
	 */
	public int deleteUsages(int daysOld) throws APIException;
	
	/**
	 * Gets usage statistics for locations
	 * @param from the start date
	 * @param until the end date
	 * @param filter the types of actions to include
	 * @return rows of locations and statistics
	 * @throws APIException
	 */
	@Authorized({Constants.PRIV_VIEW_USAGE_STATS})
	@Transactional(readOnly = true)
	public List<Object[]> getLocationsStats(Date from, Date until, ActionCriteria filter) throws APIException;
	
	/**
	 * Gets usage statistics for roles
	 * @param from the start date
	 * @param until the end date
	 * @param location the location of users to include (null for all locations)
	 * @param filter the types of actions to include
	 * @return rows of roles and statistics
	 * @throws APIException
	 */
	@Authorized({Constants.PRIV_VIEW_USAGE_STATS})
	@Transactional(readOnly = true)
	public List<Object[]> getRolesStats(Date from, Date until, Location location, ActionCriteria filter) throws APIException;
	
	/**
	 * Gets per user usage statistics for users with the specified role
	 * @param from the start date
	 * @param until the end date
	 * @param location the location of users to include (null for all locations)
	 * @param role the role of users to include (may be null)
	 * @param filter the types of actions to include
	 * @return rows of users and statistics
	 * @throws APIException
	 */
	@Authorized({Constants.PRIV_VIEW_USAGE_STATS})
	@Transactional(readOnly = true)
	public List<Object[]> getUsersStats(Date from, Date until, Location location, String role, ActionCriteria filter) throws APIException;
	
	/**
	 * Gets the most active locations based on usage count
	 * @param since the start date
	 * @param maxResults the maximum number of locations to return
	 * @return rows of locationId, location name and usage count
	 * @throws APIException
	 */
	@Authorized({Constants.PRIV_VIEW_USAGE_STATS})
	@Transactional(readOnly = true)
	public List<Object[]> getMostActiveLocations(Date since, int maxResults) throws APIException;
	
	/**
	 * Gets the most active users based on usage count
	 * @param since the start date
	 * @param maxResults the maximum number of users to return
	 * @return rows of userId, user name and usage count
	 * @throws APIException
	 */
	@Authorized({Constants.PRIV_VIEW_USAGE_STATS})
	@Transactional(readOnly = true)
	public List<Object[]> getMostActiveUsers(Date since, int maxResults) throws APIException;
	
	/**
	 * Gets the most active users based on usage count
	 * @param since the start date
	 * @param maxResults the maximum number of forms to return
	 * @return rows of forms and encounter counts
	 * @throws APIException
	 */
	@Authorized({Constants.PRIV_VIEW_USAGE_STATS})
	@Transactional(readOnly = true)
	public List<Object[]> getMostCommonForms(Date since, int maxResults) throws APIException;
	
	/**
	 * Gets the most common encounter types
	 * @param since the start date
	 * @param formless if only formless encounters should be considered
	 * @param maxResults the maximum number of forms to return
	 * @return rows of encounter types and counts
	 * @throws APIException
	 */
	@Authorized({Constants.PRIV_VIEW_USAGE_STATS})
	@Transactional(readOnly = true)
	public List<Object[]> getMostCommonEncounterTypes(Date since, boolean formless, int maxResults) throws APIException;
	
	/**
	 * Gets usage statistics based how the patient record was found
	 * @param from the start date
	 * @param until the end date
	 * @param location the location (if null then all locations)
	 * @param filter the types of actions to include
	 * @return an array of totals
	 * @throws DAOException
	 */
	@Authorized({Constants.PRIV_VIEW_USAGE_STATS})
	@Transactional(readOnly = true)
	public int[] getFoundByStats(Date from, Date until, Location location, ActionCriteria filter) throws APIException;
	
	/**
	 * Gets usage statistics between two dates
	 * @param from the start date
	 * @param until the end date
	 * @param location the location (if null then all locations)
	 * @return an array of date values and statistics
	 * @throws APIException
	 */
	@Authorized({Constants.PRIV_VIEW_USAGE_STATS})
	@Transactional(readOnly = true)
	public List<Object[]> getDateRangeStats(Date from, Date until, Location location) throws APIException;
	
	/**
	 * Gets hour of day usage statistics
	 * @param from the start date
	 * @param until the end date
	 * @param location the location (if null then all locations)
	 * @return an array of hour values and statistics
	 * @throws APIException
	 */
	@Authorized({Constants.PRIV_VIEW_USAGE_STATS})
	@Transactional(readOnly = true)
	public int[][] getHourStats(Date from, Date until, Location location) throws APIException;
	
	/**
	 * Gets day of week usage statistics
	 * @param from the start date
	 * @param until the end date
	 * @param location the location (if null then all locations)
	 * @return an array of day (0 = Sunday,1 = Monday,...) values and statistics
	 * @throws APIException
	 */
	@Authorized({Constants.PRIV_VIEW_USAGE_STATS})
	@Transactional(readOnly = true)
	public int[][] getDayOfWeekStats(Date from, Date until, Location location) throws APIException;

	/**
	 * Aggregates usages per day/user - inserting into the daily table
	 * @return the number of rows added to the aggregate data table
	 */
	public int aggregateUsages() throws APIException;
	
	/**
	 * Gets whether the given patient is marked as voided in the database
	 * @return true if the patient is voided
	 * @throws APIException
	 */
	@Transactional(readOnly = true)
	public boolean isPatientVoidedInDatabase(Patient patient) throws APIException;
}
