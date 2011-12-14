<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="Manage Usage Statistics" otherwise="/login.htm" redirect="/module/usagestatistics/options.htm"/>

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="/WEB-INF/view/admin/maintenance/localHeader.jsp" %>

<%@ include file="template/localInclude.jsp" %>
<%@ include file="template/localHeader.jsp" %>

<b class="boxHeader">
	<spring:message code="usagestatistics.options.title"/>
</b>
<ustats_form:form commandName="options" cssClass="box" action="options.htm">
	<ustats_form:errors path="*" cssClass="error" />
	<table cellpadding="2" cellspacing="0">
		<tr>
			<td><spring:message code="usagestatistics.options.minUsageInterval"/></td>
			<td>
				<ustats_form:input path="minUsageInterval" size="5" maxlength="5" />
				<spring:message code="usagestatistics.seconds"/>
				<ustats_form:errors path="minUsageInterval" cssClass="error" />
			</td>
		</tr>
		<tr>
			<td><spring:message code="usagestatistics.options.ignoreSystemDevelopers"/></td>
			<td>
				<ustats_form:checkbox path="ignoreSystemDevelopers" />
			</td>
		</tr>
		<tr>
			<td><spring:message code="usagestatistics.options.personAttributeForLocation"/></td>
			<td>
				<ustats_form:select path="locationAttributeTypeId">
				<c:forEach items="${locationAttrs}" var="locAttr">
					<ustats_form:option value="${locAttr.personAttributeTypeId}" label="${locAttr.name}" />
				</c:forEach>
				</ustats_form:select>
			</td>
		</tr>
		<tr>
			<td><spring:message code="usagestatistics.options.autoDeleteRawDataAfter"/></td>
			<td>
				<ustats_form:input path="autoDeleteDays" size="5" maxlength="5" />
				<spring:message code="usagestatistics.options.daysZeroForNever"/>
				<ustats_form:errors path="autoDeleteDays" cssClass="error" />
			</td>
		</tr>
		<tr>
			<td><spring:message code="usagestatistics.options.reportsFrequency"/></td>
			<td>
				<ustats_form:select path="reportFrequency">
					<ustats_form:option value="0" label="None" />
					<ustats_form:option value="1" label="Daily" />
					<ustats_form:option value="2" label="Weekly" />
					<ustats_form:option value="3" label="Monthly" />
				</ustats_form:select>
			</td>
		</tr>
		<tr>
			<td><spring:message code="usagestatistics.options.reportsRecipientAddresses"/></td>
			<td>
				<ustats_form:input path="reportRecipients" size="100" />
				<ustats_form:errors path="reportRecipients" cssClass="error" />
			</td>
		</tr>
		<tr>
			<td><spring:message code="usagestatistics.options.exposeJMXBean"/></td>
			<td>
				<ustats_form:checkbox path="exposeJMXBean" disabled="${not isJMXModuleRunning}" />
				<c:if test="${not isJMXModuleRunning}">
					<span class="error"><spring:message code="usagestatistics.options.jmxNotAvailable"/></span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td><input type="submit" value="<spring:message code="general.save" />" /></td>
		</tr>
	</table>
</ustats_form:form>
<br/>
<b class="boxHeader">
	<spring:message code="usagestatistics.options.status"/>
</b>
<form method="post" class="box">
	<table cellpadding="2" cellspacing="0">
		<tr>
			<td><spring:message code="usagestatistics.options.usageRecords" /></td>
			<td style="font-weight:bold">${usageCount}</td>
		</tr>
		<tr>
			<td><spring:message code="usagestatistics.options.aggregatedRecords" /></td>
			<td style="font-weight:bold">${aggregateCount}</td>
		</tr>
		<tr>
			<td><spring:message code="usagestatistics.options.aggregationTask" /></td>
			<td>
				<c:if test="${aggregationTask.started}">
					<span style="font-weight:bold; color:green"><spring:message code="usagestatistics.options.scheduled" /></span>
				</c:if>
				<c:if test="${!aggregationTask.started}">
					<span style="font-weight:bold; color:red"><spring:message code="usagestatistics.options.stopped" /></span>
				</c:if>	
				<openmrs:hasPrivilege privilege="Manage Scheduler">
					(<a href="${pageContext.request.contextPath}/admin/scheduler/scheduler.form?taskId=${aggregationTask.id}"><spring:message code="general.edit" /></a> | 
					<a href="options.htm?runTask=aggregation"><spring:message code="usagestatistics.options.run" /></a>)
				</openmrs:hasPrivilege>
			</td>
		</tr>
		<tr>
			<td><spring:message code="usagestatistics.options.sendReportsTask" /></td>
			<td>
				<c:if test="${sendReportsTask.started}">
					<span style="font-weight:bold; color:green"><spring:message code="usagestatistics.options.scheduled" /></span>
				</c:if>
				<c:if test="${!sendReportsTask.started}">
					<span style="font-weight:bold; color:red"><spring:message code="usagestatistics.options.stopped" /></span>
				</c:if>
				<openmrs:hasPrivilege privilege="Manage Scheduler">
					(<a href="${pageContext.request.contextPath}/admin/scheduler/scheduler.form?taskId=${sendReportsTask.id}"><spring:message code="general.edit" /></a> | 
					<a href="options.htm?runTask=sendReports"><spring:message code="usagestatistics.options.run" /></a>)
				</openmrs:hasPrivilege>
			</td>	
		</tr>
		<tr>
			<td><spring:message code="usagestatistics.options.reportingPeriod" /></td>
			<td>
				<span style="font-weight:bold"><openmrs:formatDate type="short" date="${options.reportFrequency.lastFullPeriodStart}" /> - 
				<openmrs:formatDate type="short" date="${options.reportFrequency.lastFullPeriodEnd}" /></span>
			</td>	
		</tr>
	</table>
</form>

<%@ include file="/WEB-INF/template/footer.jsp"%>