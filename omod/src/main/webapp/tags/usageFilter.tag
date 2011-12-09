<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglibs/c-rt.tld" %>
<%@ taglib prefix="spring" uri="/WEB-INF/taglibs/spring.tld" %>

<%@ attribute name="formFieldName" required="true" %>
<%@ attribute name="initialValue" type="org.openmrs.module.usagestatistics.ActionCriteria" required="false" %>
<%@ attribute name="showNoUsage" type="java.lang.Boolean" required="false" %>

<select name="${formFieldName}">
	<c:if test="${showNoUsage != false}">
		<option value="0" ${initialValue.ordinal == 0 ? 'selected="selected"' : ""}>&lt;<spring:message code="usagestatistics.usageFilter.anyOrNone"/>&gt;</option>
	</c:if>
	<option value="1" ${initialValue.ordinal == 1 ? 'selected="selected"' : ""}>&lt;<spring:message code="usagestatistics.usageFilter.any"/>&gt;</option>
	<option value="2" ${initialValue.ordinal == 2 ? 'selected="selected"' : ""}><spring:message code="usagestatistics.action.created"/></option>
	<option value="3" ${initialValue.ordinal == 3 ? 'selected="selected"' : ""}><spring:message code="usagestatistics.action.encounter"/></option>
	<option value="4" ${initialValue.ordinal == 4 ? 'selected="selected"' : ""}><spring:message code="usagestatistics.action.updated"/></option>
	<option value="5" ${initialValue.ordinal == 5 ? 'selected="selected"' : ""}><spring:message code="usagestatistics.action.voided"/></option>
</select>
