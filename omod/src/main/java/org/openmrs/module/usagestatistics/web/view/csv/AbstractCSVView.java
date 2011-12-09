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
package org.openmrs.module.usagestatistics.web.view.csv;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.view.AbstractView;

/**
 * Base class for views that render usage data as a comma separated values
 */
public abstract class AbstractCSVView extends AbstractView {
	
	protected static final Log log = LogFactory.getLog(AbstractCSVView.class);
	
	/**
	 * Gets the filename to return to the client
	 * @param model the model
	 * @return the filename
	 */
	protected abstract String getFilename(Map<String, Object> model);
	
	/**
	 * Writes the CSV data to the response stream
	 * @param model the model
	 * @param out the response stream writer
	 */
	protected abstract void writeValues(Map<String, Object> model, PrintWriter out);
	
	/**
	 * @see org.springframework.web.servlet.view.AbstractView
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Respond as a CSV file
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + getFilename(model) + "\"");
		
		// Disable caching
		response.setHeader("Pragma", "No-cache");
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control", "no-cache");
		
		// Write data
		writeValues(model, response.getWriter());
	}
}
