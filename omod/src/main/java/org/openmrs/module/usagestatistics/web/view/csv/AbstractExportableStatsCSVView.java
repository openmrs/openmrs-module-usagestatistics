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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.openmrs.Location;

public abstract class AbstractExportableStatsCSVView extends AbstractCSVView {

	protected static final SimpleDateFormat dfFilename = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * Generates a file name based on the data view parameters in the model
	 * @see AbstractCSVView#getFilename(Map)
	 */
	protected String getFilename(Map<String, Object> model) {
		StringBuffer sb = new StringBuffer();
		
		// Add from and until date fields
		Date from = (Date)model.get("from");
		Date until = (Date)model.get("until");	
		sb.append("_" + dfFilename.format(from));
		sb.append("_" + dfFilename.format(until));	
		
		// Add location if it is defined
		Location location = (Location)model.get("location");
		if (location != null) {
			// Remove whitespace
			String locName = location.getName().replaceAll("\\s+", "_");
			sb.append("_" + locName);
		}
		
		sb.append(".csv");
		return sb.toString();
	}
	
	/**
	 * @see org.openmrs.module.usagestatistics.web.view.csv.AbstractCSVView#writeValues(Map, PrintWriter)
	 */
	@SuppressWarnings("unchecked")
	protected void writeValues(Map<String, Object> model, PrintWriter out) {
		out.println(getHeaderRow());
		
		int[] columns = getStatsColumns();
		
		List<Object[]> stats = (List<Object[]>)model.get("stats");
		for (Object[] row : stats) {
			for (int c = 0; c < columns.length; c++) {
				out.print(row[columns[c]]);
				if (c < columns.length - 1)
					out.print(",");
			}
			out.println();
		}
	}

	protected abstract String getHeaderRow();
	
	protected abstract int[] getStatsColumns();
}
