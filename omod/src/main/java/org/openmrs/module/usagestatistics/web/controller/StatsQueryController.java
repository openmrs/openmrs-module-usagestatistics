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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.usagestatistics.Constants;
import org.openmrs.module.usagestatistics.UsageFilter;
import org.openmrs.module.usagestatistics.db.hibernate.HibernateUsageStatsDAO;
import org.openmrs.module.usagestatistics.util.PagingInfo;
import org.openmrs.module.usagestatistics.util.StatsUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Abstract base class for statistical query controllers
 */
public abstract class StatsQueryController extends AbstractController {
	
	protected static final Log log = LogFactory.getLog(HibernateUsageStatsDAO.class);
	
	private Date from, until, untilInclusive;
	private Location location;
	private UsageFilter usageFilter;
	private PagingInfo paging;
	
	protected final Map<String, Object> buildModel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		
		// Default from date is one week ago from last midnight
		Date lastMidnight = StatsUtils.getPreviousMidnight(null);
		Date weekAgo = StatsUtils.addDaysToDate(lastMidnight, -7);
		
		// Get from and until request parameters
		from = StatsUtils.getDateParameter(request, "from", weekAgo);
		until = StatsUtils.getDateParameter(request, "until", lastMidnight);
		
		// Calculate inclusive until date by adding a day
		untilInclusive = StatsUtils.addDaysToDate(until, 1);
		
		usageFilter = StatsUtils.getUsageFilterParameter(request, "usageFilter", UsageFilter.ANY);
		
		// Get paging info
		int offset = ServletRequestUtils.getIntParameter(request, "offset", 0);
		paging = new PagingInfo(offset, Constants.RESULTS_PAGE_SIZE);
		
		model.put("from", from);
		model.put("until", until);
		model.put("untilInclusive", untilInclusive);
		model.put("usageFilter", usageFilter);
		model.put("paging", paging);
		
		// Get location parameter if it exists
		int locationId = ServletRequestUtils.getIntParameter(request, "locationId", 0);
		location = (locationId > 0) ? Context.getLocationService().getLocation(locationId) : null;
		model.put("location", location);
		
		return model;
	}
	
	/**
	 * Subclasses should override this method to add data to the model
	 * @param model the model
	 * @param request the HTTP request
	 * @param response the HTTP response
	 * @throws Exception
	 */
	protected void augmentModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
	}

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
	 * Gets the location
	 * @return the location
	 */
	protected Location getLocation() {
		return location;
	}
	
	/**
	 * Gets the usage filter
	 * @return the usage filter
	 */
	public UsageFilter getUsageFilter() {
		return usageFilter;
	}
	
	/**
	 * Gets the paging information
	 * @return the paging information
	 */
	protected PagingInfo getPagingInfo() {
		return paging;
	}
}
