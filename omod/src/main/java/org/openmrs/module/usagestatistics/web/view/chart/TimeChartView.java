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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.openmrs.api.context.Context;
import org.openmrs.module.usagestatistics.UsageStatsService;
import org.openmrs.module.usagestatistics.util.ContextProvider;
import org.springframework.web.bind.ServletRequestUtils;

/**
 * View to render hour/day usage data as a chart image
 */
public class TimeChartView extends AbstractChartView {

	@Override
	protected JFreeChart createChart(Map<String, Object> model, HttpServletRequest request) {
		// Get the time function
		String func = ServletRequestUtils.getStringParameter(request, "func", "hour");
		
		UsageStatsService svc = Context.getService(UsageStatsService.class);
		
		int[][] timeStats = new int[0][0];
		String xAxisLabel = null;
		String[] categories = null;
		if (func.equals("hour")) {
			timeStats = svc.getHourStats(getFromDate(), getUntilInclusiveDate(), getLocation());
			xAxisLabel = ContextProvider.getMessage("usagestatistics.chart.hour");
		}
		else if (func.equals("dayofweek")) {
			timeStats = svc.getDayOfWeekStats(getFromDate(), getUntilInclusiveDate(), getLocation());
			xAxisLabel = ContextProvider.getMessage("usagestatistics.chart.day");
			categories = ContextProvider.getMessage("usagestatistics.chart.dayNames").split("\\|");
		}

		String yAxisLabel = ContextProvider.getMessage("usagestatistics.chart.records");
		String seriesView = ContextProvider.getMessage("usagestatistics.results.views");
        String seriesUpdates = ContextProvider.getMessage("usagestatistics.results.updates");

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int c = 0; c < timeStats.length; c++) {
        	dataset.addValue(timeStats[c][0], seriesView, (categories != null) ? categories[c] : (c + ""));
        	dataset.addValue(timeStats[c][1], seriesUpdates, (categories != null) ? categories[c] : (c + ""));
        }
        
        JFreeChart chart = ChartFactory.createBarChart(
                null,						// Chart title
                xAxisLabel,					// Domain axis label
                yAxisLabel,					// Range axis label
                dataset,					// Data
                PlotOrientation.VERTICAL,	// Orientation
                true,						// Include legend
                false,						// Tooltips?
                false						// URLs?
        );
           
        return chart;
	}

}
