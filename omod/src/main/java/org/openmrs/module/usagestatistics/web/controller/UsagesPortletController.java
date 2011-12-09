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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.EncounterType;
import org.openmrs.Form;
import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.usagestatistics.Constants;
import org.openmrs.module.usagestatistics.Usage;
import org.openmrs.module.usagestatistics.ActionCriteria;
import org.openmrs.module.usagestatistics.UsageStatsService;
import org.openmrs.module.usagestatistics.util.PagingInfo;
import org.openmrs.module.usagestatistics.util.StatsUtils;
import org.openmrs.web.controller.PortletController;
import org.springframework.web.bind.ServletRequestUtils;

/**
 * Controller for the usage statistics portlet
 */
public class UsagesPortletController extends PortletController {

	/**
	 * @see org.openmrs.web.controller.PortletController
	 */
	@Override
	protected void populateModel(HttpServletRequest request, Map<String, Object> model) {		
		// Default from date is one week ago from last midnight
		Date lastMidnight = StatsUtils.getPreviousMidnight(null);
		Date weekAgo = StatsUtils.addDaysToDate(lastMidnight, -7);
		
		// Get from and until request parameters
		Date from = StatsUtils.getDateParameter(request, "from", weekAgo);
		Date until = StatsUtils.getDateParameter(request, "until", lastMidnight);
		
		// Calculate inclusive until date by adding a day
		Date untilInclusive = StatsUtils.addDaysToDate(until, 1);
		
		// Get paging info
		int offset = ServletRequestUtils.getIntParameter(request, "offset", 0);
		PagingInfo paging = new PagingInfo(offset, Constants.RESULTS_PAGE_SIZE);
		
		ActionCriteria usageFilter = StatsUtils.getUsageFilterParameter(request, "usageFilter", ActionCriteria.ANY);
		
		int userId = ServletRequestUtils.getIntParameter(request, "userId", 0);
		User user = (userId > 0) ? Context.getUserService().getUser(userId) : null;
		
		int patientId = ServletRequestUtils.getIntParameter(request, "patientId", 0);
		Patient patient = (patientId > 0) ? Context.getPatientService().getPatient(patientId) : null;
		
		UsageStatsService svc = Context.getService(UsageStatsService.class);
		List<Usage> usages = svc.getUsages(user, patient, from, untilInclusive, usageFilter, paging);
		
		String[] foundByLabels = { "directLink", "idSearch", "nameSearch" };

		model.put("user", user);
		model.put("patient", patient);
		model.put("from", from);
		model.put("until", until);
		model.put("usageFilter", usageFilter);
		model.put("paging", paging);
		model.put("usages", usages);
		model.put("foundByLabels", foundByLabels);
		
		buildLegends(model);
	}

	/**
	 * Get the 5 most common encounter types and forms in the last week and create maps
	 * of their Ids to their names and suitable icons to be used as a legend
	 * @param model the model to add the maps to
	 */
	private void buildLegends(Map<String, Object> model) {
		Date weekAgo = StatsUtils.addDaysToDate(null, -7);
		
		UsageStatsService svc = Context.getService(UsageStatsService.class);
		List<Object[]> formStats = svc.getMostCommonForms(weekAgo, 5);
		List<Object[]> encTypeStats = svc.getMostCommonEncounterTypes(weekAgo, true, 5);
		
		Map<Integer, Object[]> formLegend = new HashMap<Integer, Object[]>();
		for (int f = 0; f < formStats.size(); f++) {
			Form form = (Form)formStats.get(f)[0];
			String icon = "icon_form_" + (f + 1) + ".png";
			formLegend.put(form.getFormId(), new Object[] {form.getName(), icon});
		}
		
		Map<Integer, Object[]> encTypeLegend = new HashMap<Integer, Object[]>();
		for (int e = 0; e < encTypeStats.size(); e++) {
			EncounterType encType = (EncounterType)encTypeStats.get(e)[0];
			String icon = "icon_encounter_" + (e + 1) + ".png";
			encTypeLegend.put(encType.getEncounterTypeId(), new Object[] {encType.getDescription(), icon});
		}
		
		model.put("formLegend", formLegend);
		model.put("encTypeLegend", encTypeLegend);
	}
}
