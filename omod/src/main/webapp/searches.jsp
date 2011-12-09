<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="View Usage Statistics" otherwise="/login.htm" redirect="/module/usagestatistics/searches.htm"/>

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="/WEB-INF/view/admin/maintenance/localHeader.jsp" %>

<%@ include file="template/localInclude.jsp" %>
<%@ include file="template/localHeader.jsp" %>

<b class="boxHeader">
	<spring:message code="usagestatistics.searches.title"/>
</b>
<form method="get" class="box">

	<table cellpadding="2" cellspacing="0" width="100%">
		<tr>
			<td align="left">
				<spring:message code="usagestatistics.query.from" />

				<ustats_tag:dateRange fromField="from" fromValue="${from}" untilField="until" untilValue="${until}" />
				
				<spring:message code="usagestatistics.query.inLocation"/>
				
				<openmrs_tag:locationField formFieldName="locationId" initialValue="${param.locationId}" />
				
				<spring:message code="usagestatistics.query.with"/>
				
				<ustats_tag:usageFilter formFieldName="usageFilter" initialValue="${usageFilter}" showNoUsage="false" />
			
				<spring:message code="usagestatistics.query.actions"/>
			</td>
			<td align="right">
				<input type="submit" value="<spring:message code="usagestatistics.query.update"/>" />
			</td>
		</tr>
	</table>
</form>
		
<br/>

<b class="boxHeader">
	<spring:message code="usagestatistics.searches.patientsFoundBy"/>
</b>
<div class="box" style="text-align: center">
	<img src="chart.htm?chart=foundBy&amp;from=${ustats:formatDate(from)}&amp;until=${ustats:formatDate(until)}&amp;locationId=${param.locationId}&amp;usageFilter=${usageFilter.ordinal}&amp;width=700&amp;height=200" width="700" height="200" />
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>