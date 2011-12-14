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
package org.openmrs.module.usagestatistics.web.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.usagestatistics.ActionCriteria;
import org.openmrs.module.usagestatistics.UsageStatisticsService;
import org.openmrs.module.usagestatistics.util.StatsUtils;

/**
 * Controller for the users and roles page
 */
public class StatsLocationsController extends ExportableStatsQueryController {
	
	protected static final Log log = LogFactory.getLog(StatsLocationsController.class);
	
	/**
	 * @see StatsQueryController#augmentModel(Map, HttpServletRequest, HttpServletResponse)
	 */
	@Override
	protected void augmentModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionCriteria usageFilter = StatsUtils.getActionCriteriaParameter(request, "usageFilter", ActionCriteria.ANY);
		
		UsageStatisticsService svc = Context.getService(UsageStatisticsService.class);
		List<Object[]> stats = svc.getLocationsStats(getFromDate(), getUntilInclusiveDate(), usageFilter);	
		
		model.put("usageFilter", usageFilter);
		model.put("stats", stats);
	}
	
}
