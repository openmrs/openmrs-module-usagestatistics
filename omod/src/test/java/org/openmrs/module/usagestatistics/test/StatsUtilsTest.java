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
package org.openmrs.module.usagestatistics.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.module.usagestatistics.util.StatsUtils;

/**
 * Test cases for the utility class StatsUtils
 */
public class StatsUtilsTest {
	
	protected static final SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	
	@Test
	public void addDaysToDate_usingDefaultDate() {
		Date today = new Date();
		Date tomorrow = StatsUtils.addDaysToDate(today, 1);
		Date yesterday = StatsUtils.addDaysToDate(today, -1);
		Assert.assertEquals(today.getTime() + (24 * 60 * 60 * 1000), tomorrow.getTime());
		Assert.assertEquals(today.getTime() - (24 * 60 * 60 * 1000), yesterday.getTime());
	}
	
	@Test
	public void addDaysToDate_usingSpecificDate() throws ParseException {
		Date date1 = df.parse("21-05-1981 15:30");
		Date date2 = df.parse("28-05-1981 15:30");
		Date date3 = df.parse("02-06-1981 15:30");
		Date date4 = StatsUtils.addDaysToDate(date1, 7);
		Date date5 = StatsUtils.addDaysToDate(date2, 5);
		Assert.assertEquals(date2.getTime(), date4.getTime());
		Assert.assertEquals(date3.getTime(), date5.getTime());
	}
	
	@Test
	public void getPreviousMidnight() throws ParseException {	
		Date date1 = df.parse("28-05-1981 15:30");
		Date date2 = df.parse("28-05-1981 00:00");
		Date date3 = StatsUtils.getPreviousMidnight(date1);
		Date date4 = StatsUtils.getPreviousMidnight(date2);
		Assert.assertEquals(date2.getTime(), date3.getTime());
		Assert.assertEquals(date2.getTime(), date4.getTime());
	}
}
