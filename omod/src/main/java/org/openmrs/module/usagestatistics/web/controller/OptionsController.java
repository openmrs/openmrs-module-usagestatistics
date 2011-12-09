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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.PersonAttributeType;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.module.usagestatistics.Options;
import org.openmrs.module.usagestatistics.Constants;
import org.openmrs.module.usagestatistics.AggregatorTask;
import org.openmrs.module.usagestatistics.ReportFrequency;
import org.openmrs.module.usagestatistics.ReportingFrequencyEditor;
import org.openmrs.module.usagestatistics.SendReportsTask;
import org.openmrs.module.usagestatistics.SessionTask;
import org.openmrs.module.usagestatistics.UsageStatsService;
import org.openmrs.scheduler.SchedulerService;
import org.openmrs.scheduler.TaskDefinition;
import org.openmrs.web.WebConstants;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Controller for the configuration page
 */
public class OptionsController extends SimpleFormController {

	protected static final Log log = LogFactory.getLog(OptionsController.class);
	
	/**
	 * @see org.springframework.web.servlet.mvc.BaseCommandController#initBinder(javax.servlet.http.HttpServletRequest, org.springframework.web.bind.ServletRequestDataBinder)
	 */
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(ReportFrequency.class, new ReportingFrequencyEditor());
		super.initBinder(request, binder);
	}
	
	/**
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command,
            BindException errors) throws Exception {
		Options options = (Options)command;
		options.save();
		
		// Update reports task
		SchedulerService schedulerSvc = Context.getSchedulerService();
		TaskDefinition sendReportsTaskDef = schedulerSvc.getTaskByName(Constants.TASK_SEND_REPORTS);
		Integer reportsInterval = ServletRequestUtils.getIntParameter(request, "reportsInterval");
		if (reportsInterval != null && sendReportsTaskDef.getRepeatInterval() != reportsInterval.longValue()) {
			sendReportsTaskDef.setRepeatInterval(reportsInterval.longValue());
			sendReportsTaskDef.setStarted(reportsInterval > 0);
			schedulerSvc.rescheduleTask(sendReportsTaskDef);
			log.warn("Rescheduled send reports task with interval: " + reportsInterval);
		}
		
		String msg = getMessageSourceAccessor().getMessage("usagestatistics.options.saveSuccess");
		request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR, msg);
		
		return new ModelAndView(new RedirectView(getSuccessView()));
	}
	
	/**
	 * @see org.springframework.web.servlet.mvc.SimpleFormController
	 */
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		return Options.getInstance();
	}

	/**
	 * @see org.springframework.web.servlet.mvc.SimpleFormController
	 */
	@Override
	protected Map<String, Object> referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {		
		Map<String, Object> model = new HashMap<String, Object>();	
		
		// If user doesn't have manage privilege, then quit now
		if (!Context.hasPrivilege(Constants.PRIV_MANAGE_USAGE_STATS))
			return model;
		
		// If user clicked the "run" link, try to run the specified task
		String runTask = request.getParameter("runTask");
		if (runTask != null) {
			SessionTask task = runTask.equals("aggregation") ? new AggregatorTask() : new SendReportsTask();
			try {
				task.execute(false);
				String msg = getMessageSourceAccessor().getMessage("usagestatistics.options.runTaskSuccess");
				request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR, msg);
			} catch (Exception ex) {
				String msg = getMessageSourceAccessor().getMessage("usagestatistics.options.runTaskFailed");
				request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, msg);
				log.error("Task failed", ex);
			}
		}
		
		UsageStatsService statsSvc = Context.getService(UsageStatsService.class);
		int usageCount = statsSvc.getUsageCount();
		int aggregateCount = statsSvc.getAggregateCount();
		
		// Get list of person attribute types so user can select the one for location
		PersonService personSvc = Context.getPersonService();
		List<PersonAttributeType> locationAttrs = personSvc.getPersonAttributeTypes(null, "org.openmrs.Location", null, null);
		
		// Get status information about the tasks
		SchedulerService schedulerSvc = Context.getSchedulerService();
		TaskDefinition aggregationTaskDef = schedulerSvc.getTaskByName(Constants.TASK_AGGREGATE_DATA);
		TaskDefinition sendReportsTaskDef = schedulerSvc.getTaskByName(Constants.TASK_SEND_REPORTS);
		
		model.put("aggregationTask", aggregationTaskDef);
		model.put("sendReportsTask", sendReportsTaskDef);
		model.put("locationAttrs", locationAttrs);
		model.put("usageCount", usageCount);
		model.put("aggregateCount", aggregateCount);
		
		return model;
	}
}
