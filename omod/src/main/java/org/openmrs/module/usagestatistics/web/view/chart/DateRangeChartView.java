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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeTableXYDataset;
import org.openmrs.api.context.Context;
import org.openmrs.module.usagestatistics.UsageStatsService;
import org.openmrs.module.usagestatistics.util.ContextProvider;

/**
 * View to render date range usage data as a chart image
 */
public class DateRangeChartView extends AbstractChartView {
	
	@Override
	protected JFreeChart createChart(Map<String, Object> model, HttpServletRequest request) {
		UsageStatsService svc = Context.getService(UsageStatsService.class);
		List<Object[]> stats = svc.getDateRangeStats(null, null, null);
		
		String xAxisLabel = ContextProvider.getMessage("usagestatistics.chart.date");
		String yAxisLabel = ContextProvider.getMessage("usagestatistics.chart.records");
		String seriesUsages = ContextProvider.getMessage("usagestatistics.results.views");
		String seriesEncounters = ContextProvider.getMessage("usagestatistics.results.encounters");
        String seriesUpdates = ContextProvider.getMessage("usagestatistics.results.updates");
        
        // Get minimum date value in returned statistics
        Date minDate = (stats.size() > 0) ? (Date)(stats.get(0)[0]) : getFromDate();
        if (minDate.getTime() > getFromDate().getTime()) // Min date must be at least a week ago
        	minDate = getFromDate();
        // Maximum date defaults to today
        Date maxDate = (getUntilDate() != null) ? getUntilDate() : new Date();
        
        // Build a zeroized dataset of all dates in range       
        TimeTableXYDataset dataset = new TimeTableXYDataset();
        Calendar cal = new GregorianCalendar();
        cal.setTime(minDate);
        while (cal.getTime().getTime() <= maxDate.getTime()) {
        	Date day = cal.getTime();
        	dataset.add(new Day(day), 0, seriesUsages, false);
        	dataset.add(new Day(day), 0, seriesEncounters, false);
        	dataset.add(new Day(day), 0, seriesUpdates, false);  	
        	cal.add(Calendar.DATE, 1);
        }
		
		// Update the dates for which we have statistics
		for (Object[] row : stats) {
			Date date = (Date)row[0];
			int usages = ((Number)row[1]).intValue();
			int encounters = ((Number)row[2]).intValue();
			int updates = ((Number)row[3]).intValue();
        	dataset.add(new Day(date), usages, seriesUsages, false);
        	dataset.add(new Day(date), encounters, seriesEncounters, false);
        	dataset.add(new Day(date), updates, seriesUpdates, false);
        }
		dataset.setDomainIsPointsInTime(true);
		
		JFreeChart chart = ChartFactory.createXYLineChart(null, xAxisLabel, yAxisLabel, dataset, PlotOrientation.VERTICAL, true, false, false);
		DateAxis xAxis = new DateAxis(xAxisLabel);
		
		XYPlot plot = (XYPlot)chart.getPlot();
		plot.setDomainAxis(xAxis);
		
        return chart;
	}
}
