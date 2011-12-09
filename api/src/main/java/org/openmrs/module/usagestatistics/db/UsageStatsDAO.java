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
package org.openmrs.module.usagestatistics.db;

import java.util.Date;
import java.util.List;

import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.api.APIException;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.usagestatistics.Usage;
import org.openmrs.module.usagestatistics.UsageFilter;
import org.openmrs.module.usagestatistics.util.PagingInfo;

/**
 * Database access object for the usage event table
 */
public interface UsageStatsDAO {
		
	/**
	 * Saves a usage
	 * @param The usage to save
	 * @throws DAOException
	 */
	public void saveUsage(Usage usage) throws DAOException;
	
	/**
	 * Gets raw usage records
	 * @param user the user (may be null)
	 * @param patient the patient (may be null)
	 * @param from the minimum date/time (may be null)
	 * @param from the maximum date/time (may be null))
	 * @param filter the types of actions to include
	 * @param paging the paging info such as offset and page size
	 * @return a list of usages
	 * @throws APIException
	 */
	public List<Usage> getUsages(User user, Patient patient, Date from, Date until, UsageFilter filter, PagingInfo paging) throws DAOException;
	
	/**
	 * Gets the last matching usage
	 * @param user the user (may be null)
	 * @param patient the patient (may be null)
	 * @param maxAgeSecs the maximum age of the returned event in seconds (0 to ignore)
	 * @return the usage event or null if there are no matches
	 * @throws DAOException
	 */
	public Usage getLastUsage(User user, Patient patient, int maxAgeSecs) throws DAOException;
	
	/**
	 * Deletes usages which are older than the given number of days
	 * @param until the timestamp before which usages should be deleted
	 * @return the number of usages that were deleted
	 * @throws APIException
	 */
	public int deleteUsages(Date until) throws DAOException;
	
	/**
	 * Gets usage statistics for locations
	 * @param from the start date
	 * @param until the end date
	 * @param filter the types of actions to include
	 * @return an array of locations and statistics
	 * @throws APIException
	 */
	public List<Object[]> getLocationsStats(Date from, Date until, UsageFilter filter) throws DAOException;
	
	/**
	 * Gets usage statistics for roles
	 * @param from the start date
	 * @param until the end date
	 * @param location the location of users to include (null for all locations)
	 * @param filter
	 * @return an array of roles and statistics
	 * @throws DAOException
	 */
	public List<Object[]> getRolesStats(Date from, Date until, Location location, UsageFilter filter) throws DAOException;
	
	/**
	 * Gets usage statistics for users with a specific role
	 * @param from the start date
	 * @param until the end date
	 * @param location the location of users to include (null for all locations)
	 * @param role the role name (if null then uses all roles)
	 * @param filter
	 * @return an array of user ids, names and statistics
	 * @throws DAOException
	 */
	public List<Object[]> getUsersStats(Date from, Date until, Location location, String role, UsageFilter filter) throws DAOException;
	
	/**
	 * Gets the most active locations based on usage count
	 * @param since the start date
	 * @param maxResults the maximum number of locations to return
	 * @return rows of locationId, location name and usage count
	 * @throws DAOException
	 */
	public List<Object[]> getMostActiveLocations(Date since, int maxResults) throws DAOException;
	
	/**
	 * Gets the most active users based on usage count
	 * @param since the start date
	 * @param maxResults the maximum number of users to return
	 * @return rows of userId, user name and usage count
	 * @throws DAOException
	 */
	public List<Object[]> getMostActiveUsers(Date since, int maxResults) throws DAOException;
	
	/**
	 * Gets the most used forms based on encounter count
	 * @param since the start date
	 * @param maxResults the maximum number of forms to return
	 * @return rows of forms and encounter counts
	 * @throws DAOException
	 */
	public List<Object[]> getMostCommonForms(Date since, int maxResults) throws DAOException;
	
	/**
	 * Gets the most common encounter types
	 * @param since the start date
	 * @param formless if only formless encounters should be considered
	 * @param maxResults the maximum number of forms to return
	 * @return rows of encounter types and counts
	 * @throws DAOException
	 */
	public List<Object[]> getMostCommonEncounterTypes(Date since, boolean formless, int maxResults) throws DAOException;
	
	/**
	 * Gets usage statistics based how the patient record was found
	 * @param from the start date
	 * @param until the end date
	 * @param location the location (if null then all locations)
	 * @param filter the types of actions to include
	 * @return an array of totals
	 * @throws DAOException
	 */
	public int[] getFoundByTotals(Date from, Date until, Location location, UsageFilter filter) throws DAOException;
	
	/**
	 * Gets usage statistics between two dates
	 * @param from the start date
	 * @param until the end date
	 * @param location the location (if null then all locations)
	 * @return rows of date values and statistics
	 * @throws APIException
	 */
	public List<Object[]> getDateRangeStats(Date from, Date until, Location location) throws DAOException;
	
	/**
	 * Calculates time based usage statistics for a specific usage type
	 * @param from the start date
	 * @param until the end date
	 * @param location the location (may be null)
	 * @param onlyUpdates
	 * @param func the function applied to each event timestamp
	 * @return a list of object arrays for each time based category
	 */
	public List<Object[]> getTimeBasedTotals(Date from, Date until, Location location, boolean onlyUpdates, String func) throws DAOException;
	
	/**
	 * Gets the total number of usages
	 * @return the number of rows in the usage table
	 * @throws DAOException
	 */
	public int getUsageCount() throws DAOException;
	
	/**
	 * Gets the total number of daily aggregates
	 * @return the number of rows in the aggregate table
	 * @throws DAOException
	 */
	public int getAggregateCount() throws DAOException;
	
	/**
	 * Aggregates usages per day/user - inserting into the daily table
	 * @return the number of rows added to the aggregate data table
	 */
	public int aggregateUsages() throws DAOException;
	
	/**
	 * Gets whether the given patient is marked as voided in the database
	 * @return true if the patient is voided
	 * @throws DAOException
	 */
	public boolean isPatientVoidedInDatabase(Patient patient) throws DAOException;
}
