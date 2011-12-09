package org.openmrs.module.usagestatistics.advice;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Encounter;
import org.openmrs.module.usagestatistics.UsageLog;
import org.springframework.aop.AfterReturningAdvice;

/**
 * AOP class used to intercept and log calls to EncounterService methods
 */
public class EncounterServiceAdvice implements AfterReturningAdvice {

	protected static final Log log = LogFactory.getLog(EncounterServiceAdvice.class);
	
	/**
	 * @see org.springframework.aop.AfterReturningAdvice#afterReturning(Object, Method, Object[], Object)
	 */
	public void afterReturning(Object returnVal, Method method, Object[] args, Object target) throws Throwable {
		if (method.getName().equals("saveEncounter")
				|| method.getName().equals("createEncounter")
				|| method.getName().equals("updateEncounter")) {
			
			Encounter encounter = (Encounter)args[0];
			
			UsageLog.logEvent(encounter);
		}
	}
}
