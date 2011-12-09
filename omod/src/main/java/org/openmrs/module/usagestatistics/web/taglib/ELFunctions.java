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
package org.openmrs.module.usagestatistics.web.taglib;

import java.text.DateFormat;
import java.util.Date;

import org.openmrs.api.context.Context;

public class ELFunctions {
	
	/**
	 * Formats a date in the current user's locale
	 * @param date the date value to format
	 * @return a formatted string representation of the date
	 */
	public static String formatDate(Date date) {
		
		return (date != null) ? Context.getDateFormat().format(date) : "";
	}
	
	/**
	 * Formats a date and time in the current user's locale
	 * @param date the date value to format
	 * @return a formatted string representation of the date and time
	 */
	public static String formatDateTime(Date date) {
		
		return (date != null) ? (
				Context.getDateFormat().format(date) + " " +
				DateFormat.getTimeInstance(DateFormat.LONG, Context.getLocale()).format(date))
				: "";
	}
}
