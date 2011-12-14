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
import org.openmrs.module.usagestatistics.jmx.UsageStatisticsMXBean;
import org.openmrs.module.usagestatistics.jmx.UsageStatisticsMXBeanImpl;
import org.openmrs.module.usagestatistics.tasks.AggregatorTask;
import org.openmrs.module.usagestatistics.tasks.SendReportsTask;
import org.openmrs.module.usagestatistics.util.StatsUtils;
import org.openmrs.scheduler.SchedulerException;
import org.openmrs.scheduler.Task;
import org.openmrs.scheduler.TaskDefinition;

public class UsageStatisticsContext {
	
	protected static final Log log = LogFactory.getLog(UsageStatisticsContext.class);
	
	/**
	 * Registers the aggregation task if it hasn't already been registered
	 */
	public static boolean registerAggregationTask() {
		return registerTask(Constants.TASK_AGGREGATE_DATA, "Deletes or aggregates old usage statistics data", AggregatorTask.class, 60 * 60l);
	}

	/**
	 * Registers the reports task if it hasn't already been registered
	 */
	public static boolean registerSendReportsTask() {
		return registerTask(Constants.TASK_SEND_REPORTS, "Sends usage statistics reports", SendReportsTask.class, 60 * 60 * 24l);		
	}

	/**
	 * Unregisters the aggregation and reports tasks if they exist
	 */
	public static void unregisterTasks() {
	    Context.addProxyPrivilege("Manage Scheduler");
	    try {
	    	unregisterTask(Constants.TASK_AGGREGATE_DATA);
    		unregisterTask(Constants.TASK_SEND_REPORTS);
	    } finally {
	        Context.removeProxyPrivilege("Manage Scheduler");
	    }
	}
	
	/**
	 * Registers the usage statistics MXBean with the JMX module
	 */
	public static void registerMBean() {
		try {
			UsageStatisticsMXBean bean = new UsageStatisticsMXBeanImpl();
			Class<?> c = Context.loadClass("org.openmrs.module.jmx.JMXService");
			Object jmxService = Context.getService(c);
			Method regMethod = jmxService.getClass().getDeclaredMethod("registerMBean", String.class, String.class, Object.class);
			regMethod.invoke(jmxService, Constants.MXBEAN_NAME, null, bean);
			
		} catch (Exception e) {
			log.warn("JMX module not loaded. Unable to register MBean");
		}
	}
	
	/**
	 * Unregisters the usage statistics MXBean with the JMX module
	 */
	public static void unregisterMBean() {
		try {
			Class<?> c = Context.loadClass("org.openmrs.module.jmx.JMXService");
			Object jmxService = Context.getService(c);
			Method unregMethod = jmxService.getClass().getDeclaredMethod("unregisterMBean", String.class, String.class);
			unregMethod.invoke(jmxService, Constants.MXBEAN_NAME, null);
			
		} catch (Exception e) {
			log.warn("JMX module not loaded. Unable to unregister MBean");
		}
	}
	
	/**
	 * Checks if the JMX module is running
	 * @return true if module is running
	 */
	public static boolean isJMXModuleRunning() {
		try {
			Context.loadClass("org.openmrs.module.jmx.JMXService");
		} catch (ClassNotFoundException e) {
			return false;
		}
		return true;
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
	private static boolean registerTask(String name, String description, Class<? extends Task> clazz, long interval) {
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
	private static void unregisterTask(String name) {
		TaskDefinition taskDef = Context.getSchedulerService().getTaskByName(name);
		if (taskDef != null)
			Context.getSchedulerService().deleteTask(taskDef.getId());
	}
}
