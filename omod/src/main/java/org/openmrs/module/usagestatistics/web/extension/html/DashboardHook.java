package org.openmrs.module.usagestatistics.web.extension.html;

import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.Extension;
import org.openmrs.module.usagestatistics.UsageLog;
import org.openmrs.module.usagestatistics.web.filter.RequestProviderFilter;

public class DashboardHook extends Extension {
	/**
	 * @see org.openmrs.module.Extension#getOverrideContent(java.lang.String)
	 */
	@Override
	public String getOverrideContent(String bodyContent) {		
		String patientIdStr = getParameterMap().get("patientId");
		
		if (patientIdStr != null) {
			int patientId = Integer.parseInt(patientIdStr);
			
			String phrase = RequestProviderFilter.getCurrentRequest().getParameter("phrase");
			
			Patient patient = Context.getPatientService().getPatient(patientId);
			if (patient != null)
				UsageLog.logEvent(patient, UsageLog.Type.VIEWED, phrase);
		}
		
		return super.getOverrideContent(bodyContent);
	}

	@Override
	public MEDIA_TYPE getMediaType() {
		return MEDIA_TYPE.html;
	}
}
