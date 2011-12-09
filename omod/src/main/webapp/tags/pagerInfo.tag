<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglibs/c-rt.tld" %>
<%@ taglib prefix="spring" uri="/WEB-INF/taglibs/spring.tld" %>

<%@ attribute name="pagingInfo" required="true" type="org.openmrs.module.usagestatistics.util.PagingInfo" %>

<c:set var="recordMin" value="${pagingInfo.pageOffset + 1}" />
<c:choose>
	<c:when test="${pagingInfo.pageOffset + pagingInfo.pageSize < pagingInfo.resultsTotal}">
		<c:set var="recordMax" value="${pagingInfo.pageOffset + pagingInfo.pageSize}" />
	</c:when>
	<c:otherwise>
		<c:set var="recordMax" value="${pagingInfo.resultsTotal}" />
	</c:otherwise>
</c:choose>

<spring:message
	code="usagestatistics.pagingInfo.showingResults"
	arguments="${recordMin},${recordMax},${pagingInfo.resultsTotal}"
	text="Showing {0} to {1} of {2}"
/>