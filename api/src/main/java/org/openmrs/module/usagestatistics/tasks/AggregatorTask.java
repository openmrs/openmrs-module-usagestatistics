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
package org.openmrs.module.usagestatistics.tasks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.usagestatistics.Constants;
import org.openmrs.module.usagestatistics.UsageStatsService;

/**
 * Task to aggregate raw data and delete old data
 */
public class AggregatorTask extends SessionTask {

	private static Log log = LogFactory.getLog(AggregatorTask.class);

	/**
	 * Does the actual data aggregation
	 */
	protected void onExecute() {
		// Firstly aggregate raw data not yet aggregated
		UsageStatsService statsSvc = Context.getService(UsageStatsService.class);
		int rowsAdded = statsSvc.aggregateUsages();
		
		// Secondly, delete raw data of a certain age
		AdministrationService adminSvc = Context.getAdministrationService();
		String str = adminSvc.getGlobalProperty(Constants.PROP_AUTO_DELETE_DAYS);
		int autoDeleteDays = Integer.parseInt(str);
		int rowsDeleted = statsSvc.deleteUsages(autoDeleteDays);
		
		log.info("Aggregated data: added " + rowsAdded + " aggregated rows and deleted " + rowsDeleted + " raw rows");
	}
}
