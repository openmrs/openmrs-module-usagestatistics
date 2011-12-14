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

import java.awt.Color;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.openmrs.api.context.Context;
import org.openmrs.module.usagestatistics.UsageStatisticsService;
import org.openmrs.module.usagestatistics.util.ContextProvider;

/**
 * View to render found by usage data as a chart image
 */
public class FoundByChartView extends AbstractChartView {
	
	@Override
	protected JFreeChart createChart(Map<String, Object> model, HttpServletRequest request) {	
		UsageStatisticsService svc = Context.getService(UsageStatisticsService.class);
		int[] stats = svc.getFoundByStats(getFromDate(), getUntilInclusiveDate(), getLocation(), getUsageFilter());
		
		String labelLink = ContextProvider.getMessage("usagestatistics.foundBy.directLink");
		String labelId = ContextProvider.getMessage("usagestatistics.foundBy.idSearch");
		String labelName = ContextProvider.getMessage("usagestatistics.foundBy.nameSearch");

		double total = stats[0];
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue(labelLink, 100.0 * stats[1] / total);
        dataset.setValue(labelId, 100.0 * stats[2] / total);
        dataset.setValue(labelName, 100.0 * stats[3] / total);
        
        JFreeChart chart = ChartFactory.createPieChart(null, dataset, false, false, false);

        PiePlot plot = (PiePlot)chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setInteriorGap(0.0);
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({2})"));
        plot.setLabelFont(getFont());
        
        return chart;
	}

	/**
	 * Pie-chart background should be white
	 */
	@Override
	protected Color getBackgroundColor() {
		return Color.WHITE;
	}

	
}
