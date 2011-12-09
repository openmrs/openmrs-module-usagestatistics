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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.openmrs.module.usagestatistics.util.StatsUtils;

/**
 * Enumeration of different reporting frequencies 
 */
public enum ReportFrequency {
	NONE,
	DAILY, 		// Period will be yesterday
	WEEKLY,		// Period will be last week
	MONTHLY;	// Period will be last month
	
	public boolean dueToday() {
		Calendar calendar = new GregorianCalendar();
		switch (this) {
        case DAILY:
        	return true;
        case WEEKLY:
        	return new GregorianCalendar().get(Calendar.DAY_OF_WEEK) == calendar.getFirstDayOfWeek();
        case MONTHLY:
        	return new GregorianCalendar().get(Calendar.DAY_OF_MONTH) == 1;
		}
		return false;
	}
	
	public Date getLastFullPeriodStart() {
		Calendar cal = new GregorianCalendar();
		switch (this) {
		case DAILY: 
			cal.add(Calendar.DAY_OF_MONTH, -1);
			break;
        case WEEKLY: 
        	cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        	cal.add(Calendar.WEEK_OF_YEAR, -1);
        	break;
        case MONTHLY:
        	cal.set(Calendar.DAY_OF_MONTH, 1);
        	cal.add(Calendar.MONTH, -1);
        	break;
		}
		return StatsUtils.getPreviousMidnight(cal.getTime());
	}
	
	public Date getLastFullPeriodEnd() {
		Calendar cal = new GregorianCalendar();
		switch (this) {
        case WEEKLY:
        	cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        	break;
        case MONTHLY:
        	cal.set(Calendar.DAY_OF_MONTH, 1);
        	break;
		}
		return StatsUtils.getPreviousMidnight(cal.getTime());
	}
	
	/**
	 * Bean-property wrapper for the ordinal method so it can be used in EL
	 */
	public int getOrdinal() {
		return ordinal();
	}
	
	public static ReportFrequency fromOrdinal(int ordinal) {
		switch (ordinal) {
        case 0: return NONE;
        case 1: return DAILY;
        case 2: return WEEKLY;
        case 3: return MONTHLY;
		}
		return null;
	}
}
