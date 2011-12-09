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
import java.util.HashSet;
import java.util.Set;

import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.User;

/**
 * A record of a user accessing a patient record
 */
public class Usage {
	public static final int FOUNDBY_LINK = 0;
	public static final int FOUNDBY_ID_QUERY = 1;
	public static final int FOUNDBY_NAME_QUERY = 2;
	
	protected Integer usageId;
	protected User user;	
	protected Patient patient;
	protected Date timestamp;
	protected boolean created;
	protected boolean updated;
	protected boolean voided;
	protected int foundBy;
	protected String query;
	protected Set<Encounter> encounters = new HashSet<Encounter>();
	
	/**
	 * Default constructor
	 */	
	public Usage() {
	}
	
	/**
	 * Constructor for a patient record access
	 * @param user the logged in user
	 * @param patient the patient accessed
	 */
	public Usage(User user, Patient patient) {
		this.patient = patient;
		this.user = user;
		this.timestamp = new Date();
	}
	
	/**
	 * Gets the database id
	 * @return the database Id
	 */
	public Integer getUsageId() {
		return usageId;
	}
	
	/**
	 * Sets the database id
	 * @param usageId the database id
	 */
	public void setUsageId(Integer usageId) {
		this.usageId = usageId;
	}
	
	/**
	 * Gets the user
	 * @return the user
	 */
	public User getUser() {
		return user;
	}
	
	/**
	 * Sets the user
	 * @param user the user
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * Gets the patient
	 * @return the patient
	 */
	public Patient getPatient() {
		return patient;
	}
	
	/**
	 * Sets the patient
	 * @param the patient
	 */
	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	/**
	 * Gets the timestamp
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}
	
	/**
	 * Sets the timestamp
	 * @param timestamp
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	/**
	 * Gets if this usage started with the record being created
	 * @return true if record was created
	 */
	public boolean isCreated() {
		return created;
	}

	/**
	 * Sets if this usage started with the record being created
	 * @param created true if record was created
	 */
	public void setCreated(boolean created) {
		this.created = created;
	}
	
	/**
	 * Gets if this usage included an update
	 * @return true if record was updated
	 */
	public boolean isUpdated() {
		return updated;
	}

	/**
	 * Sets if this usage included an update
	 * @param updated true if record was updated
	 */
	public void setUpdated(boolean updated) {
		this.updated = updated;
	}
	
	/**
	 * Gets if this usage included voiding
	 * @return true if record was voided
	 */
	public boolean isVoided() {
		return voided;
	}

	/**
	 * Sets if this usage included voiding
	 * @param voided true if record was voided
	 */
	public void setVoided(boolean voided) {
		this.voided = voided;
	}

	/**
	 * Gets how the record was found
	 * @return the found by constant 
	 */
	public int getFoundBy() {
		return foundBy;
	}

	/**
	 * Sets how the record was found
	 * @param foundBy the found by constant
	 */
	public void setFoundBy(int foundBy) {
		this.foundBy = foundBy;
	}
	
	/**
	 * Gets the query if patient was found by search
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * Sets the query if patient was found by search
	 * @param query the query
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	/**
	 * Gets the encounters
	 * @return the encounters
	 */
	public Set<Encounter> getEncounters() {
		return encounters;
	}

	/**
	 * Sets the encounters
	 * @param encounters the encounters
	 */
	public void setEncounters(Set<Encounter> encounters) {
		this.encounters = encounters;
	}
	
	/**
	 * Adds an encounter
	 * @param encounter the encounter
	 */
	public void addEncounter(Encounter encounter) {
		this.encounters.add(encounter);
	}
}
