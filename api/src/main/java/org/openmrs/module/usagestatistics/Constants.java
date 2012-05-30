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

/**
 * Constants used by the usage statistics module
 */
public class Constants {
	// Module properties
	public static final String MODULE_ID = "usagestatistics";
	public static final String MXBEAN_NAME = "UsageStatistics";
	
	// Privileges
	public static final String PRIV_VIEW_USAGE_STATS = "View Usage Statistics";
	public static final String PRIV_MANAGE_USAGE_STATS = "Manage Usage Statistics";
	
	// Global property names
	public static final String PROP_MIN_USAGE_INTERVAL = MODULE_ID + ".minUsageInterval";
	public static final String PROP_IGNORE_SYSTEM_DEVS = MODULE_ID + ".ignoreSystemDevelopers";
	public static final String PROP_LOCATION_ATTRIBUTE_TYPE_ID = MODULE_ID + ".locationAttributeTypeId";
	public static final String PROP_AUTO_DELETE_DAYS = MODULE_ID + ".autoDeleteDays";
	public static final String PROP_REPORT_FREQUENCY = MODULE_ID + ".reportFrequency";
	public static final String PROP_REPORT_RECIPIENTS = MODULE_ID + ".reportRecipients";
	
	// Scheduled task
	public static final String TASK_AGGREGATE_DATA = "Process Usage Statistics Data";
	public static final String TASK_SEND_REPORTS = "Send Usage Statistics Reports";
	
	// Paging options
	public static final int RESULTS_PAGE_SIZE = 25;
	
	// Email properties
	public static final String REPORT_SENDER = "Usage Statistics <no-reply@openmrs.org>";
	public static final String REPORT_SUBJECT = "Usage Statistics Report";
}
