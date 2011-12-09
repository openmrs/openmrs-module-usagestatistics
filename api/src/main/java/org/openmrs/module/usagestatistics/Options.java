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

import org.openmrs.GlobalProperty;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;

/**
 * Holds the configuration options for this module
 */
public class Options {
	
	protected static Options config;
	
	protected int minUsageInterval;
	protected boolean ignoreSystemDevelopers;
	protected int locationAttributeTypeId;
	protected int autoDeleteDays;
	protected ReportFrequency reportFrequency;
	protected String reportRecipients;
	
	/**
	 * The default constructor
	 */
	protected Options() {
		load(); 
	}
	
	/**
	 * Gets the singleton instance of this class
	 * @return the config instance
	 */
	public static Options getInstance() {
		if (config == null)
			config = new Options();
		return config;
	}
	
	/**
	 * Loads the options from global properties
	 */
	public void load() {
		minUsageInterval = loadIntOption(Constants.PROP_MIN_USAGE_INTERVAL, 300); // 5 minutes
		ignoreSystemDevelopers = loadBooleanOption(Constants.PROP_IGNORE_SYSTEM_DEVS, false);
		locationAttributeTypeId = loadIntOption(Constants.PROP_LOCATION_ATTRIBUTE_TYPE_ID, 7);
		autoDeleteDays = loadIntOption(Constants.PROP_AUTO_DELETE_DAYS, 14); // 2 weeks
		reportFrequency = ReportFrequency.fromOrdinal(loadIntOption(Constants.PROP_REPORT_FREQUENCY, 0));
		reportRecipients = loadStringOption(Constants.PROP_REPORT_RECIPIENTS, "");
	}
	
	/**
	 * Saves the options to global properties
	 */
	public void save() {
		saveOption(Constants.PROP_MIN_USAGE_INTERVAL, minUsageInterval);
		saveOption(Constants.PROP_IGNORE_SYSTEM_DEVS, ignoreSystemDevelopers);
		saveOption(Constants.PROP_LOCATION_ATTRIBUTE_TYPE_ID, locationAttributeTypeId);
		saveOption(Constants.PROP_AUTO_DELETE_DAYS, autoDeleteDays);
		saveOption(Constants.PROP_REPORT_FREQUENCY, reportFrequency.getOrdinal());
		saveOption(Constants.PROP_REPORT_RECIPIENTS, reportRecipients);
	}

	/**
	 * Gets the minimum number of seconds between distinct usage events
	 * @return the minimum usage interval
	 */
	public int getMinUsageInterval() {
		return minUsageInterval;
	}

	/**
	 * Sets the minimum number of seconds between distinct usage events
	 * @param minUsageInterval the minimum usage interval
	 */
	public void setMinUsageInterval(int minUsageInterval) {
		this.minUsageInterval = minUsageInterval;
	}
	
	/**
	 * Gets if usage of users with System Developer role should be ignored
	 * @return the true if they are ignored, else false
	 */
	public boolean isIgnoreSystemDevelopers() {
		return ignoreSystemDevelopers;
	}

	/**
	 * Sets if usage of users with System Developer role should be ignored
	 * @param ignoreSystemDevelopers true if they should be ignored, else false
	 */
	public void setIgnoreSystemDevelopers(boolean ignoreSystemDevelopers) {
		this.ignoreSystemDevelopers = ignoreSystemDevelopers;
	}

	/**
	 * Gets the person attribute type id to be used for user location
	 * @return the person attribute type id
	 */
	public int getLocationAttributeTypeId() {
		return locationAttributeTypeId;
	}

	/**
	 * Sets the person attribute type id to be used for user location
	 * @param locationAttributeTypeId the person attribute type id
	 */
	public void setLocationAttributeTypeId(int locationAttributeTypeId) {
		this.locationAttributeTypeId = locationAttributeTypeId;
	}

	/**
	 * Gets the number of days after which raw data should be automatically deleted
	 * @return the number of days
	 */
	public int getAutoDeleteDays() {
		return autoDeleteDays;
	}

	/**
	 * Sets the number of days after which raw data should be automatically deleted
	 * @param autoDeleteDays the number of days
	 */
	public void setAutoDeleteDays(int autoDeleteDays) {
		this.autoDeleteDays = autoDeleteDays;
	}
	
	/**
	 * Gets the report frequency
	 * @return the frequency
	 */
	public ReportFrequency getReportFrequency() {
		return reportFrequency;
	}
	
	/**
	 * Sets the report frequency
	 * @param reportFrequency the report frequency
	 */
	public void setReportFrequency(ReportFrequency reportFrequency) {
		this.reportFrequency = reportFrequency;
	}
	
	/**
	 * Gets the report recipients
	 * @return the reportRecipients
	 */
	public String getReportRecipients() {
		return reportRecipients;
	}

	/**
	 * Sets the report recipients
	 * @param reportRecipients the reportRecipients
	 */
	public void setReportRecipients(String reportRecipients) {
		this.reportRecipients = reportRecipients;
	}

	/**
	 * Utility method to load a string option from global properties
	 * @param name the name of the global property
	 * @param def the default value if global property is invalid
	 * @return the string value
	 */
	private static String loadStringOption(String name, String def) {
		AdministrationService svc = Context.getAdministrationService();
		String s = svc.getGlobalProperty(name);
		return (s != null) ? s : def;
	}

	/**
	 * Utility method to load an integer option from global properties
	 * @param name the name of the global property
	 * @param def the default value if global property is invalid
	 * @return the integer value
	 */
	private static int loadIntOption(String name, int def) {
		AdministrationService svc = (AdministrationService)Context.getAdministrationService();
		String s = svc.getGlobalProperty(name);
		try {
			return Integer.parseInt(s);
		}
		catch (NumberFormatException ex) {
			return def;
		}
	}
	
	/**
	 * Utility method to load an boolean option from global properties
	 * @param name the name of the global property
	 * @return the boolean value
	 */
	private static boolean loadBooleanOption(String name, boolean def) {
		AdministrationService svc = (AdministrationService)Context.getAdministrationService();
		String s = svc.getGlobalProperty(name);
		try {
			return Boolean.parseBoolean(s);
		}
		catch (NumberFormatException ex) {
			return def;
		}
	}
	
	/**
	 * Utility method to save an option to global properties
	 * @param name the name of the global property
	 * @param value the value of the global property
	 */
	private static void saveOption(String name, Object value) {
		AdministrationService svc = (AdministrationService)Context.getAdministrationService();
		GlobalProperty property = svc.getGlobalPropertyObject(name);
		property.setPropertyValue(String.valueOf(value));
		svc.saveGlobalProperty(property);
	}
}
