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

import java.io.File;
import java.io.StringWriter;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.openmrs.ImplementationId;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;

import org.openmrs.module.ModuleFactory;
import org.openmrs.notification.Message;
import org.openmrs.notification.MessageException;
import org.openmrs.util.OpenmrsUtil;

/**
 * Task to aggregate raw data and delete old data
 */
public class SendReportsTask extends SessionTask {

	private static Log log = LogFactory.getLog(SendReportsTask.class);

	/**
	 * Does the actual report sending
	 * @throws Exception 
	 */
	protected void onExecute() throws Exception {
		ReportFrequency freq = Options.getInstance().getReportFrequency();
		if (!freq.dueToday()) {
			log.info("No reports due today");
			return;
		}

		Date since = freq.getLastFullPeriodStart();
		Date until = freq.getLastFullPeriodEnd();
		
		String recipients = Options.getInstance().getReportRecipients().trim();
		if (recipients.length() > 0) {
			VelocityEngine velocity = new VelocityEngine();
			velocity.init();
			
			VelocityContext context = new VelocityContext();
			
			UsageStatsService svc = Context.getService(UsageStatsService.class);
			//List<Object[]> roleStats = svc.getRolesStats(null, null, null, UsageFilter.ANY);
			List<Object[]> roleStats = svc.getRolesStats(since, until, null, ActionCriteria.ANY);
			
			AdministrationService adminSvc = Context.getAdministrationService();
			ImplementationId implId = adminSvc.getImplementationId();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			
			context.put("period", (freq == ReportFrequency.DAILY) ? df.format(since) : (df.format(since) + " " + df.format(until)));
			context.put("site", implId != null ? implId.getName() : "UNKNOWN");
	        context.put("roleStats", roleStats);
	        context.put("moduleVersion", ModuleFactory.getModuleById(Constants.MODULE_ID).getVersion());
	        
	        URL templateUrl = getClass().getResource("reportEmail.vm");
	        if (templateUrl == null) {
	        	log.error("Unable to access email template");
				return;
	        }
	       
	        File templateFile = OpenmrsUtil.url2file(templateUrl);
	        String templateString = OpenmrsUtil.getFileAsString(templateFile);
	        StringWriter writer = new StringWriter();
	        velocity.evaluate(context, writer, Constants.REPORT_SUBJECT, templateString);
			
			Message message = new Message();
			message.setRecipients(recipients);
			message.setSender(Constants.REPORT_SENDER);
			message.setContent(writer.toString());
			message.setSubject(Constants.REPORT_SUBJECT);
			message.setAttachment(generateAttachment(roleStats));
			message.setAttachmentContentType("text/csv");
			message.setAttachmentFileName("roles.csv");
			message.setContentType("text/html");
			
			try {	
				Context.getMessageService().sendMessage(message);
			} catch (MessageException e) {
				log.error("Unable to send message", e);
			}
		}
	}
	
	/**
	 * Generates the CSV attachment for the report email
	 * @param since the date from which to start
	 * @param until the date up to
	 * @return
	 */
	private String generateAttachment(List<Object[]> stats) {
		StringBuilder sb = new StringBuilder();

		sb.append("Role,Active Users,All Usages,Creates,Encounters,Updates,Voids,Last usage\n");
		for (Object[] row : stats) {
			for (int c = 0; c < row.length; c++) {
				if (c > 0)
					sb.append(",");
				sb.append(row[c]);
			}
			sb.append("\n");
		}
		
		return sb.toString();
	}
}
