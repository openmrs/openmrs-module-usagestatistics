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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

/**
 * Controller for views that return chart images
 */
public class ChartStatsQueryController extends StatsQueryController {
	
	private Map<String, View> chartViews = new HashMap<String, View>();

	/**
	 * @see StatsQueryController
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String, Object> model = buildModel(request, response);
		
		augmentModel(model, request, response);
		
		String chart = request.getParameter("chart");
			
		return new ModelAndView(chartViews.get(chart), model);
	}

	@Required
	public void setChartViews(Map<String, View> chartViews) {
		this.chartViews = chartViews;
	}
}
