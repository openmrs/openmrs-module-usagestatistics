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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 * Controller for the users and roles page
 */
public class ExportableStatsQueryController extends StatsQueryController {
	
	private String pageViewName;
	private View exportView;
	
	/**
	 * @see ParameterizableViewController#handleRequest(HttpServletRequest, HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String, Object> model = buildModel(request, response);
		
		augmentModel(model, request, response);
		
		if (request.getParameter("export") != null)
			return new ModelAndView(exportView, model);
		else	
			return new ModelAndView(pageViewName, model);
	}

	/**
	 * Sets the name of the page view
	 * @param pageViewName the page view name
	 */
	@Required
	public void setPageViewName(String pageViewName) {
		this.pageViewName = pageViewName;
	}
	
	/**
	 * Sets the view for exporting as CSV
	 * @param view the view
	 */
	public void setExportView(View view) {
		exportView = view;
	}
}
