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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.BaseModuleActivator;

/**
 * This class contains the logic that is run every time this module is either
 * started or shutdown
 */
public class UsageStatisticsActivator extends BaseModuleActivator {

	private static final Log log = LogFactory.getLog(UsageStatisticsActivator.class);

	/**
	 * @see org.openmrs.module.BaseModuleActivator#started()
	 */
	@Override
	public void started() {
		log.info("Starting usage statistics module");
		
		UsageStatisticsContext.registerAggregationTask();
		UsageStatisticsContext.registerSendReportsTask();
		
		log.info("Registered usage statistics tasks");
	}

	/**
	 * @see org.openmrs.module.BaseModuleActivator#stopped()
	 */
	@Override
	public void stopped() {
		log.info("Shutting down usage statistics module");

		UsageStatisticsContext.unregisterTasks();
		
		log.info("Unregistered usage statistics tasks");
	}
}
