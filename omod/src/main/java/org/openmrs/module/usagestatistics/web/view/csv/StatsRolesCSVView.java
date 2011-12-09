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

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * View to render role based usage data as a comma separated values
 */
public class StatsRolesCSVView extends AbstractExportableStatsCSVView {
	
	protected static final Log log = LogFactory.getLog(StatsRolesCSVView.class);

	/**
	 * @see org.openmrs.module.usagestatistics.web.view.csv.AbstractCSVView#getFilename()
	 */
	@Override
	protected String getFilename(Map<String, Object> model) {
		return "roles" + super.getFilename(model);
	}

	/**
	 * @see org.openmrs.module.usagestatistics.web.view.csv.AbstractCSVView#getHeaderRow()
	 */
	@Override
	protected String getHeaderRow() {
		return "Role,Active Users,All Usages,Creates,Encounters,Updates,Voids,Last usage";
	}

	/**
	 * @see org.openmrs.module.usagestatistics.web.view.csv.AbstractCSVView#getStatsColumns()
	 */
	@Override
	protected int[] getStatsColumns() {
		return new int[] {0, 1, 2, 3, 4, 5, 6, 7};
	}
}
