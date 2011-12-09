<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="View Usage Statistics" otherwise="/login.htm" redirect="/module/usagestatistics/summary.htm" />

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="/WEB-INF/view/admin/maintenance/localHeader.jsp"%>

<%@ include file="template/localInclude.jsp"%>
<%@ include file="template/localHeader.jsp"%>

<b class="boxHeader">
	<spring:message code="usagestatistics.summary.now" />
</b>
<div class="box" style="overflow: hidden">
	<table cellpadding="3" cellspacing="0" width="100%">
		<tr>
			<td>
				<b><spring:message code="usagestatistics.summary.onlineUsers" /></b>
				${onlineUsers}
			</td>
			<td>
				<b><spring:message code="usagestatistics.summary.recordsBeingViewed" /></b>
				${recordsOpen}
			</td>
		</tr>
	</table>
</div>

<br />

<b class="boxHeader">
	<spring:message code="usagestatistics.summary.lastMonth" />
</b>
<div class="box" style="overflow: hidden">
	<table cellpadding="3" cellspacing="0" width="100%">
		<tr>
			<td width="50%"><b><spring:message code="usagestatistics.summary.mostActiveLocations" /></b></td>
			<td width="50%"><b><spring:message code="usagestatistics.summary.mostActiveUsers" /></b></td>
		</tr>
		<tr>
			<td valign="top">
				<table cellpadding="2" cellspacing="0" width="100%">
					<c:forEach items="${locationStats}" var="row" varStatus="rowStatus">
						<tr class="<c:choose><c:when test="${rowStatus.index % 2 == 0}">evenRow</c:when><c:otherwise>oddRow</c:otherwise></c:choose>">
							<td><a href="users.htm?locationId=${row[0].locationId}">${row[0].name}</td>
							<td align="right">${row[1]}</td>
						</tr>
					</c:forEach>
				</table>
			</td>
			<td valign="top">
				<table cellpadding="2" cellspacing="0" width="100%">
					<c:forEach items="${userStats}" var="row" varStatus="rowStatus">
						<tr class="<c:choose><c:when test="${rowStatus.index % 2 == 0}">evenRow</c:when><c:otherwise>oddRow</c:otherwise></c:choose>">
							<td><a href="usages.htm?userId=${row[0]}">${row[1]}</td>
							<td align="right">${row[2]}</td>
						</tr>
					</c:forEach>
				</table>
			</td>
		</tr>
	</table>
</div>

<br />

<b class="boxHeader">
	<spring:message code="usagestatistics.summary.history" />
</b>
<div class="box" style="text-align: center">
	<img src="chart.htm?chart=date&amp;width=700&amp;height=200" width="700" height="200" />
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>