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

package org.openmrs.module.usagestatistics.jmx;

import org.openmrs.api.context.Context;
import org.openmrs.module.usagestatistics.Constants;
import org.openmrs.module.usagestatistics.UsageStatsService;

/**
 * Implementation of the usage statistics JMX bean
 */
public class UsageStatisticsMXBeanImpl implements UsageStatisticsMXBean {
	
	/**
	 * @see org.openmrs.module.usagestatistics.jmx.UsageStatisticsMXBean#getOnlineUsers()
	 */
	@Override
	public int getOnlineUsers() {
		Context.openSession();
		Context.addProxyPrivilege(Constants.PRIV_VIEW_USAGE_STATS);
		UsageStatsService svc = Context.getService(UsageStatsService.class);
		int count = svc.getOnlineUserCount();
		Context.removeProxyPrivilege(Constants.PRIV_VIEW_USAGE_STATS);
		Context.closeSession();
		return count;
	}

	/**
	 * @see org.openmrs.module.usagestatistics.jmx.UsageStatisticsMXBean#getOpenRecords()
	 */
	@Override
	public int getOpenRecords() {
		Context.openSession();
		Context.addProxyPrivilege(Constants.PRIV_VIEW_USAGE_STATS);
		UsageStatsService svc = Context.getService(UsageStatsService.class);
		int count = svc.getOpenRecordsCount();
		Context.removeProxyPrivilege(Constants.PRIV_VIEW_USAGE_STATS);
		Context.closeSession();
		return count;
	}
}
