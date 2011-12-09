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
package org.openmrs.module.usagestatistics.web.view.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.openmrs.Location;
import org.openmrs.module.usagestatistics.ActionCriteria;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.view.AbstractView;

/**
 * Base class for views that render usage data as a comma separated values
 */
public abstract class AbstractChartView extends AbstractView {
	
	protected static final Log log = LogFactory.getLog(AbstractChartView.class);
	
	private static Font font = new Font("Verdana", Font.PLAIN, 12);
	private static Color bkColor = new Color(240, 240, 250);
	
	private Date from, until, untilInclusive;
	private ActionCriteria usageFilter;
	private Location location;

	/**
	 * @see org.springframework.web.servlet.view.AbstractView
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Respond as a PNG image
		response.setContentType("image/png");

		// Disable caching
		response.setHeader("Pragma", "No-cache");
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control", "no-cache");
		
		int width = ServletRequestUtils.getIntParameter(request, "width", 500);
		int height = ServletRequestUtils.getIntParameter(request, "height", 300);
		
		from = (Date)model.get("from");
		until = (Date)model.get("until");
		untilInclusive = (Date)model.get("untilInclusive");
		usageFilter = (ActionCriteria)model.get("usageFilter");
		location = (Location)model.get("location");
		
		JFreeChart chart = createChart(model, request);
		chart.setBackgroundPaint(Color.WHITE);
		chart.getPlot().setOutlineStroke(new BasicStroke(0));
		chart.getPlot().setOutlinePaint(getBackgroundColor());
		chart.getPlot().setBackgroundPaint(getBackgroundColor());
		
		ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, width, height);
	}
	
	/**
	 * Generates a JFreeChart
	 * @param model the model
	 * @param request the servlet request
	 * @return the chart object
	 */
	protected abstract JFreeChart createChart(Map<String, Object> model, HttpServletRequest request);
	
	/**
	 * Gets the from date
	 * @return the from date
	 */
	protected Date getFromDate() {
		return from;
	}
	
	/**
	 * Gets the until date
	 * @return the until date
	 */
	protected Date getUntilDate() {
		return until;
	}
	
	/**
	 * Gets the inclusive until date (i.e. until + 1 day)
	 * @return the until date
	 */
	protected Date getUntilInclusiveDate() {
		return untilInclusive;
	}
	
	/**
	 * Gets the usage filter
	 * @return the usage filter
	 */
	public ActionCriteria getUsageFilter() {
		return usageFilter;
	}
	
	/**
	 * Gets the location
	 * @return the location
	 */
	protected Location getLocation() {
		return location;
	}
	
	/**
	 * Gets the font for chart rendering
	 */
	protected Font getFont() {
		return font;
	}
	
	/**
	 * Gets the plot background color
	 */
	protected Color getBackgroundColor() {
		return bkColor;
	}
}
