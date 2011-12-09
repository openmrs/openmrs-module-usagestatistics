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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.util.OpenmrsConstants;

/**
 * Class to handle logging of patient record usage
 */
public class UsageLog {
	
	protected static final Log log = LogFactory.getLog(UsageLog.class);

	/**
	 * The type of a usage event
	 */
	public enum Type {
		VIEWED,
		CREATED,
		UPDATED,
		VOIDED
	}
	
	/**
	 * Logs a usage event
	 * @param patient the patient
	 * @param type the type of usage event
	 * @param query the search query used to find this patient
	 */
	public static void logEvent(Patient patient, Type type, String query) {
		User user = Context.getAuthenticatedUser();
		
		logEvent(user, patient, null, type, query);
	}
	
	/**
	 * Logs a usage event
	 * @param encounter the encounter
	 */
	public static void logEvent(Encounter encounter) {
		// Use encounter creator as infopath forms are submitted by super user
		User user = encounter.getCreator();
		Patient patient = encounter.getPatient();
		
		logEvent(user, patient, encounter, null, null);
	}
	
	/**
	 * Logs a usage event
	 * @param user the user
	 * @param patient the patient
	 * @param encounter the encounter
	 * @param type the type of usage event
	 * @param query the search query used to find this patient
	 */
	private static void logEvent(User user, Patient patient, Encounter encounter, Type type, String query) {
		if (patient == null) {
			log.warn("Attempt to log usage on null patient");
			return;
		}
		
		Options config = Options.getInstance();
		
		// Optionally ignore system developers
		if (user != null && user.hasRole(OpenmrsConstants.SUPERUSER_ROLE) && config.isIgnoreSystemDevelopers())
			return;
		
		UsageStatsService svc = (UsageStatsService)Context.getService(UsageStatsService.class);
		Usage usage = null;
		
		// Check for duplication of a recent usage - update it if exists
		if (user != null && config.getMinUsageInterval() > 0) {
			
			// Get most recent usage with this user, patient and type within the configurable timeframe
			usage = svc.getLastUsage(user, patient, config.getMinUsageInterval());		
			if (usage != null)
				// Update the time of the recent event
				usage.setTimestamp(new Date());
		}
		
		// If not updating a recent event, create a new one
		if (usage == null)
			usage = new Usage(user, patient);
		
		// Add the encounter if it exists
		if (encounter != null) {
			usage.addEncounter(encounter);
		}
		
		// Set created / updated / voided flags
		if (type == Type.CREATED)
			usage.setCreated(true);
		else if (type == Type.UPDATED)
			usage.setUpdated(true);
		else if (type == Type.VOIDED)
			usage.setVoided(true);
		
		// Classify the search phrase if it exists
		if (query != null) {	
			// Classify search phrase
			usage.setFoundBy(classifySearchPhrase(query));
			usage.setQuery(query);
		}
		
		svc.saveUsage(usage);
	}
	
	/**
	 * Classifies a search phrase as a partial ID or name
	 * @param phrase the phrase to classify
	 * @return the classification
	 */
	protected static int classifySearchPhrase(String phrase) {
		if (phrase != null && phrase.length() > 0) {
			// TODO something quicker than a regex?
			if (phrase.matches(".*\\d+.*"))
				return Usage.FOUNDBY_ID_QUERY;
			else
				return Usage.FOUNDBY_NAME_QUERY;
		}
		else
			return Usage.FOUNDBY_LINK;
	}
}
