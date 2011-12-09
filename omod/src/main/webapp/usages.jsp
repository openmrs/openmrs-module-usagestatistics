<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="View Usage Statistics" otherwise="/login.htm" redirect="/module/usagestatistics/usages.htm"/>

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="/WEB-INF/view/admin/maintenance/localHeader.jsp" %>

<%@ include file="template/localInclude.jsp" %>
<%@ include file="template/localHeader.jsp" %>

<openmrs:portlet url="usages" id="usages" moduleId="usagestatistics" parameters="showPatients=true" />

<%@ include file="/WEB-INF/template/footer.jsp"%>