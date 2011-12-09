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
package org.openmrs.module.usagestatistics.tasks;

import org.openmrs.api.context.Context;

import org.openmrs.scheduler.tasks.AbstractTask;

/**
 * Task to aggregate raw data and delete old data
 */
public abstract class SessionTask extends AbstractTask {

	/**
	 * @see org.openmrs.scheduler.tasks.AbstractTask#execute()
	 */
	@Override
	public void execute() {
		execute(true);
	}
	
	/**
	 * Executes this data aggregation task
	 * @param newSession true to create a new OpenMRS session
	 */
	public void execute(boolean newSession) {
		if (!isExecuting) {
            isExecuting = true;
            
            if (newSession)
            	Context.openSession();
            
			try {
				if (!Context.isAuthenticated())
					authenticate();
				
				onExecute();
				
			} catch (Exception e) {
			} finally {
				if (newSession)
					Context.closeSession();
				isExecuting = false;
			}
		}
	}

	/**
	 * Does the actual task work
	 * @throws Exception 
	 */
	abstract protected void onExecute() throws Exception;
}
