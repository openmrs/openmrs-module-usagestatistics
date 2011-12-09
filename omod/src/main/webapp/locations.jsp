<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="View Usage Statistics" otherwise="/login.htm" redirect="/module/usagestatistics/locations.htm"/>

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="/WEB-INF/view/admin/maintenance/localHeader.jsp" %>

<%@ include file="template/localInclude.jsp" %>
<%@ include file="template/localHeader.jsp" %>

<b class="boxHeader">
	<spring:message code="usagestatistics.locations.title"/>
</b>
<form method="get" class="box">

	<table cellpadding="2" cellspacing="0" width="100%">
		<tr>
			<td align="left">
				<spring:message code="usagestatistics.query.from" />

				<ustats_tag:dateRange fromField="from" fromValue="${from}" untilField="until" untilValue="${until}" />
				
				<spring:message code="usagestatistics.query.with" />
				
				<ustats_tag:usageFilter formFieldName="usageFilter" initialValue="${usageFilter}" />
				
				<spring:message code="usagestatistics.query.actions" />
			</td>
			<td align="right">
				<input type="submit" value="<spring:message code="usagestatistics.query.update"/>" />
				<input type="submit" name="export" value="<spring:message code="usagestatistics.query.export"/>" />
			</td>
		</tr>
	</table>
		
	<table cellpadding="2" cellspacing="0" width="100%">
		<tr>
			<th><spring:message code="usagestatistics.results.location"/></th>
			<th align="center"><spring:message code="usagestatistics.results.activeUsers"/></th>
			<th align="center"><spring:message code="usagestatistics.results.allUsages"/></th>
			<th align="center"><spring:message code="usagestatistics.results.creates"/></th>
			<th align="center"><spring:message code="usagestatistics.results.encounters"/></th>
			<th align="center"><spring:message code="usagestatistics.results.updates"/></th>
			<th align="center"><spring:message code="usagestatistics.results.voids"/></th>
			<th align="center"><spring:message code="usagestatistics.results.lastUsage"/></th>
		</tr>
		<c:forEach items="${stats}" var="row" varStatus="rowStatus">
			<tr class="<c:choose><c:when test="${rowStatus.index % 2 == 0}">evenRow</c:when><c:otherwise>oddRow</c:otherwise></c:choose>">
				<td><a href="roles.htm?from=${ustats:formatDate(from)}&amp;until=${ustats:formatDate(until)}&amp;locationId=${row[0]}">${row[1]}</a></td>
				<td align="center">${row[2]}</td>
				<td align="center">${row[3]}</td>
				<td align="center">${row[4]}</td>
				<td align="center">${row[5]}</td>
				<td align="center">${row[6]}</td>
				<td align="center">${row[7]}</td>
				<td align="center">${ustats:formatDate(row[8])}</td>
			</tr>	
		</c:forEach>
		<c:if test="${empty stats}">
			<tr>
				<td colspan="8" style="padding: 10px; text-align: center"><spring:message code="usagestatistics.noresults"/></td>
			</tr>
		</c:if>
	</table>
	
</form>

<%@ include file="/WEB-INF/template/footer.jsp"%>