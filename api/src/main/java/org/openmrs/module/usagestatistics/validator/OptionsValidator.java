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
package org.openmrs.module.usagestatistics.validator;

import org.openmrs.module.usagestatistics.Options;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validates a usage statistics configuration
 */
public class OptionsValidator implements Validator {
	
	protected static final int MAX_MINUSAGEINTERVAL = 86400; // 24 hours
	protected static final int MAX_AUTODELETEDAYS = 365;

	/**
	 * @see org.springframework.validation.Validator#supports(Class)
	 */
	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return clazz.equals(Options.class);
	}

	/**
	 * @see org.springframework.validation.Validator#validate(Object, Errors)
	 */
	public void validate(Object obj, Errors errors) {
		Options config = (Options)obj;
		if (config.getMinUsageInterval() < 0 || config.getMinUsageInterval() > MAX_MINUSAGEINTERVAL)
			errors.rejectValue("minUsageInterval", "usagestatistics.error.range",
					new Object[] { 0, MAX_MINUSAGEINTERVAL}, "");
		
		if (config.getAutoDeleteDays() < 0 || config.getAutoDeleteDays() > MAX_AUTODELETEDAYS)
			errors.rejectValue("autoDeleteDays", "usagestatistics.error.range",
					new Object[] { 0, MAX_AUTODELETEDAYS}, "");
	}

}
