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
package org.openmrs.module.usagestatistics.advice;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.usagestatistics.UsageLog;
import org.openmrs.module.usagestatistics.UsageStatisticsService;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

/**
 * AOP class used to intercept and log calls to PatientService methods
 */
public class PatientServiceAdvice implements MethodBeforeAdvice, AfterReturningAdvice {
	
	protected static final Log log = LogFactory.getLog(PatientServiceAdvice.class);
	
	protected UsageLog.Type usageType;
	
	/**
	 * @see org.springframework.aop.MethodBeforeAdvice#before(Method, Object[], Object)
	 */
	public void before(Method method, Object[] args, Object target) throws Throwable {
		if (method.getName().equals("savePatient") || method.getName().equals("updatePatient")) {
			Patient patient = (Patient)args[0];
			usageType = UsageLog.Type.UPDATED;
			
			if (patient.getPatientId() == null)
				usageType = UsageLog.Type.CREATED;
			else if (patient.isVoided()) {
				UsageStatisticsService svc = (UsageStatisticsService)Context.getService(UsageStatisticsService.class);
				// Patient object is voided, but check database record
				if (!svc.isPatientVoidedInDatabase(patient))
					usageType = UsageLog.Type.VOIDED;
			}			
		}
		else if (method.getName().equals("createPatient"))
			usageType = UsageLog.Type.CREATED;
		else if (method.getName().equals("voidPatient"))
			usageType = UsageLog.Type.VOIDED;
	}
	
	/**
	 * @see org.springframework.aop.AfterReturningAdvice#afterReturning(Object, Method, Object[], Object)
	 */
	public void afterReturning(Object returnVal, Method method, Object[] args, Object target) throws Throwable {
		// Intercept patient record updates as calls to savePatient or updatePatient
		if (method.getName().equals("savePatient")
				|| method.getName().equals("updatePatient")
				|| method.getName().equals("createPatient")
				|| method.getName().equals("voidPatient")) {
			Patient patient = (Patient)args[0];

			UsageLog.logEvent(patient, usageType, null);
		}
	}
}
