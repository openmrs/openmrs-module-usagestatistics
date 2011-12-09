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
package org.openmrs.module.usagestatistics.web.extension.html;

import org.openmrs.module.usagestatistics.Constants;
import org.openmrs.module.web.extension.PatientDashboardTabExt;

/**
 * Extension class to add a tab to the patient dashboard
 */
public class PatientUsageTabExt extends PatientDashboardTabExt {

	/**
	 * @see org.openmrs.module.web.extension.PatientDashboardTabExt#getPortletUrl()
	 */
	@Override
	public String getPortletUrl() {
		return "usages";
	}

	/**
	 * @see org.openmrs.module.web.extension.PatientDashboardTabExt#getRequiredPrivilege()
	 */
	@Override
	public String getRequiredPrivilege() {
		return Constants.PRIV_VIEW_USAGE_STATS;
	}

	/**
	 * @see org.openmrs.module.web.extension.PatientDashboardTabExt#getTabId()
	 */
	@Override
	public String getTabId() {
		return Constants.MODULE_ID;
	}

	/**
	 * @see org.openmrs.module.web.extension.PatientDashboardTabExt#getTabName()
	 */
	@Override
	public String getTabName() {
		return Constants.MODULE_ID + ".dashboard.tab.name";
	}
}
