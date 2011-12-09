<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="View Usage Statistics" otherwise="/login.htm" redirect="/module/usagestatistics/time.htm"/>

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="/WEB-INF/view/admin/maintenance/localHeader.jsp" %>

<%@ include file="template/localInclude.jsp" %>
<%@ include file="template/localHeader.jsp" %>

<b class="boxHeader">
	<spring:message code="usagestatistics.time.title"/>
</b>
<form method="get" class="box">
	<table cellpadding="2" cellspacing="0" width="100%">
		<tr>
			<td>
				<spring:message code="usagestatistics.query.from" />

				<ustats_tag:dateRange fromField="from" fromValue="${from}" untilField="until" untilValue="${until}" />
				
				<spring:message code="usagestatistics.query.inLocation"/>
				
				<openmrs_tag:locationField formFieldName="locationId" initialValue="${param.locationId}" />
			</td>
			<td align="right">
				<input type="submit" value="<spring:message code="usagestatistics.query.update"/>" />
			</td>
		</tr>
	</table>
</form>

<br/>

<b class="boxHeader">
	<spring:message code="usagestatistics.time.hourOfDay"/>
</b>
<div class="box" style="text-align: center">
	<img src="chart.htm?chart=time&amp;func=hour&amp;from=${ustats:formatDate(from)}&amp;until=${ustats:formatDate(until)}&amp;locationId=${param.locationId}&amp;width=700&amp;height=200" width="700" height="200" />
</div>

<br/>

<b class="boxHeader">
	<spring:message code="usagestatistics.time.dayOfWeek"/>
</b>
<div class="box" style="text-align: center">
	<img src="chart.htm?chart=time&amp;func=dayofweek&amp;from=${ustats:formatDate(from)}&amp;until=${ustats:formatDate(until)}&amp;locationId=${param.locationId}&amp;width=700&amp;height=200" width="700" height="200" />
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>