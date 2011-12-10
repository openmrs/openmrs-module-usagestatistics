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

package org.openmrs.module.usagestatistics;

import java.lang.reflect.Method;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.usagestatistics.jmx.UsageStatisticsMXBean;
import org.openmrs.module.usagestatistics.jmx.UsageStatisticsMXBeanImpl;
import org.openmrs.module.usagestatistics.tasks.AggregatorTask;
import org.openmrs.module.usagestatistics.tasks.SendReportsTask;
import org.openmrs.module.usagestatistics.util.StatsUtils;
import org.openmrs.scheduler.SchedulerException;
import org.openmrs.scheduler.Task;
import org.openmrs.scheduler.TaskDefinition;

/**
 * This class contains the logic that is run every time this module is either
 * started or shutdown
 */
public class ModuleActivator extends BaseModuleActivator {

	private static final Log log = LogFactory.getLog(ModuleActivator.class);

	/**
	 * @see org.openmrs.module.BaseModuleActivator#started()
	 */
	@Override
	public void started() {
		log.info("Starting usage statistics module");
		
		registerMXBean();
		
		log.info("Registered usage statistics management bean");
		
		registerAggregationTask();
		registerSendReportsTask();
		
		log.info("Registered usage statistics tasks");
	}

	/**
	 * @see org.openmrs.module.BaseModuleActivator#stopped()
	 */
	@Override
	public void stopped() {
		log.info("Shutting down usage statistics module");
		
		unregisterMXBean();
		
		log.info("Unregistered usage statistics management bean");

		unregisterTasks();
		
		log.info("Unregistered usage statistics tasks");
	}
	
	/**
	 * Registers the aggregation task if it hasn't already been registered
	 */
	private boolean registerAggregationTask() {
		return registerTask(Constants.TASK_AGGREGATE_DATA, "Deletes or aggregates old usage statistics data", AggregatorTask.class, 60 * 60l);
	}

	/**
	 * Registers the reports task if it hasn't already been registered
	 */
	private boolean registerSendReportsTask() {
		return registerTask(Constants.TASK_SEND_REPORTS, "Sends usage statistics reports", SendReportsTask.class, 60 * 60 * 24l);		
	}

	/**
	 * Unregisters the aggregation and reports tasks if they exist
	 */
	private void unregisterTasks() {
	    Context.addProxyPrivilege("Manage Scheduler");
	    try {
	    	unregisterTask(Constants.TASK_AGGREGATE_DATA);
    		unregisterTask(Constants.TASK_SEND_REPORTS);
	    } finally {
	        Context.removeProxyPrivilege("Manage Scheduler");
	    }
	}

	/**
	 * Register a new OpenMRS task
	 * @param name the name
	 * @param description the description
	 * @param clazz the task class
	 * @param interval the interval in seconds
	 * @return boolean true if successful, else false
	 * @throws SchedulerException if task could not be scheduled
	 */
	private boolean registerTask(String name, String description, Class<? extends Task> clazz, long interval) {
		try {
			Context.addProxyPrivilege("Manage Scheduler");
		
			TaskDefinition taskDef = Context.getSchedulerService().getTaskByName(name);
			if (taskDef == null) {
				taskDef = new TaskDefinition();
				taskDef.setTaskClass(clazz.getCanonicalName());
				taskDef.setStartOnStartup(true);
				taskDef.setRepeatInterval(interval);
				taskDef.setStarted(true);
				taskDef.setStartTime(StatsUtils.getPreviousMidnight(null));
				taskDef.setName(name);
				taskDef.setUuid(UUID.randomUUID().toString()); 
				taskDef.setDescription(description);
				Context.getSchedulerService().scheduleTask(taskDef);
			}
			
		} catch (SchedulerException ex) {
			log.warn("Unable to register task '" + name + "' with scheduler", ex);
			return false;
		} finally {
			Context.removeProxyPrivilege("Manage Scheduler");
		}
		return true;
	}
	
	/**
	 * Unregisters the named task
	 * @param name the task name
	 */
	private void unregisterTask(String name) {
		TaskDefinition taskDef = Context.getSchedulerService().getTaskByName(name);
		if (taskDef != null)
			Context.getSchedulerService().deleteTask(taskDef.getId());
	}
	
	/**
	 * Registers the usage statistics MXBean with the JMX module
	 */
	private void registerMXBean() {
		try {
			UsageStatisticsMXBean bean = new UsageStatisticsMXBeanImpl();
			Class<?> c = Context.loadClass("org.openmrs.module.jmx.JMXService");
			Object jmxService = Context.getService(c);
			Method regMethod = jmxService.getClass().getDeclaredMethod("registerBean", String.class, String.class, Object.class);
			regMethod.invoke(jmxService, null, Constants.MXBEAN_NAME, bean);
			
		} catch (Exception e) {
			log.warn("JMX module not loaded. Unable to register MBean");
		}
	}
	
	/**
	 * Unregisters the usage statistics MXBean with the JMX module
	 */
	private void unregisterMXBean() {
		try {
			Class<?> c = Context.loadClass("org.openmrs.module.jmx.JMXService");
			Object jmxService = Context.getService(c);
			Method unregMethod = jmxService.getClass().getDeclaredMethod("unregisterBean", String.class, String.class);
			unregMethod.invoke(jmxService, null, Constants.MXBEAN_NAME);
			
		} catch (Exception e) {
			log.warn("JMX module not loaded. Unable to unregister MBean");
		}
	}
}
