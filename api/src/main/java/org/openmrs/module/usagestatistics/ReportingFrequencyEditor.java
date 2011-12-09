package org.openmrs.module.usagestatistics;

import java.beans.PropertyEditorSupport;


public class ReportingFrequencyEditor extends PropertyEditorSupport {

	public String getAsText() {
		return ((ReportFrequency)getValue()).ordinal() + "";
	}

	public void setAsText(String text) throws IllegalArgumentException {
		int ordinal = Integer.parseInt(text);
		setValue(ReportFrequency.fromOrdinal(ordinal));
	}
}
