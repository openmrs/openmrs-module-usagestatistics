<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglibs/c-rt.tld" %>
<%@ taglib prefix="spring" uri="/WEB-INF/taglibs/spring.tld" %>
<%@ taglib prefix="openmrs" uri="/WEB-INF/taglibs/openmrs.tld" %>

<%@ attribute name="fromField" required="true" %>
<%@ attribute name="untilField" required="true" %>
<%@ attribute name="fromValue" required="false" type="java.util.Date" %>
<%@ attribute name="untilValue" required="false" type="java.util.Date" %>

<openmrs:htmlInclude file="/scripts/calendar/calendar.js" />

<input type="text" id="${fromField}" name="${fromField}" size="10" value="<openmrs:formatDate date="${fromValue}" />" onFocus="showCalendar(this)" />
				
<spring:message code="usagestatistics.dateRange.until" />
				
<input type="text" id="${untilField}" name="${untilField}" size="10" value="<openmrs:formatDate date="${untilValue}" />" onFocus="showCalendar(this)" />
			
