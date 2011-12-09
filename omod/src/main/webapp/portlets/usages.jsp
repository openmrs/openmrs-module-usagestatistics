<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="../template/localInclude.jsp" %>

<b class="boxHeader">
	<spring:message code="usagestatistics.usages.title"/>
</b>
<form method="get" class="box" name="usagesForm">

	<table cellpadding="2" cellspacing="0" width="100%">
		<tr>
			<td>	
				<spring:message code="usagestatistics.query.from" />

				<ustats_tag:dateRange fromField="from" fromValue="${model.from}" untilField="until" untilValue="${model.until}" />
			
				<spring:message code="usagestatistics.query.byUser"/>

				<table cellpadding="0" cellspacing="0" border="0" style="display: inline; vertical-align: bottom">
					<tr>
						<td>
							<openmrs_tag:userField formFieldName="userId" searchLabelCode="Encounter.provider.find" initialValue="${model.user.userId}" linkUrl="${pageContext.request.contextPath}/admin/users/user.form" />
						</td>
						<td>	
							<input style="width: 16px" type="button" class="smallButton" id="clearUserBtn" onclick="document.usagesForm.userId.value='';document.usagesForm.submit();" value="X" />
						</td>
					</tr>
				</table>
				
				<c:choose>
					<c:when test="${model.showPatients == true}">
						<spring:message code="usagestatistics.query.ofPatient"/>
		
						<table cellpadding="0" cellspacing="0" border="0" style="display: inline; vertical-align: bottom">
							<tr>
								<td>
									<openmrs_tag:patientField formFieldName="patientId" searchLabelCode="Patient.find" initialValue="${model.patient.patientId}" linkUrl="${pageContext.request.contextPath}/admin/patients/patient.form"/>
								</td>
								<td>	
									<input style="width: 16px" type="button" class="smallButton" id="clearUserBtn" onclick="document.usagesForm.patientId.value='';document.usagesForm.submit();" value="X" />
								</td>
							</tr>
						</table>
					</c:when>
					<c:otherwise>
						<input name="patientId" value="${patient.patientId}" type="hidden" />
					</c:otherwise>
				</c:choose>
					
				<spring:message code="usagestatistics.query.with"/>
				
				<ustats_tag:usageFilter formFieldName="usageFilter" initialValue="${model.usageFilter}" showNoUsage="false" />
			
				<spring:message code="usagestatistics.query.actions"/>
			</td>
			<td align="right">
				<input type="submit" value="<spring:message code="usagestatistics.query.update"/>" />
			</td>
		</tr>
	</table>
			
	<table cellpadding="2" cellspacing="0" width="100%">
		<tr>
			<th><spring:message code="usagestatistics.results.when" /></th>
			<th><spring:message code="usagestatistics.results.user" /></th>
			<c:if test="${model.showPatients == true}">
				<th><spring:message code="usagestatistics.results.patient" /></th>
			</c:if>
			<th align="center"><spring:message code="usagestatistics.results.actions" /></th>
			<th><spring:message code="usagestatistics.results.foundBy" /></th>
		</tr>
		
		<c:forEach items="${model.usages}" var="usage" varStatus="rowStatus">
			<tr class="<c:choose><c:when test="${rowStatus.index % 2 == 0}">evenRow</c:when><c:otherwise>oddRow</c:otherwise></c:choose>">
				<td>${ustats:formatDateTime(usage.timestamp)}</td>
				<td><a href="?from=${ustats:formatDate(from)}&amp;until=${ustats:formatDate(until)}&amp;userId=${usage.user.userId}">${usage.user.personName}</a></td>
				<c:if test="${model.showPatients == true}">
					<td><a href="${pageContext.request.contextPath}/patientDashboard.form?patientId=${usage.patient.patientId}">${usage.patient.personName}</a></td>
				</c:if>
				<td align="center">
                	<c:if test="${usage.created}">
						<span title="<spring:message code="usagestatistics.action.created" />"><img src="${pageContext.request.contextPath}/moduleResources/usagestatistics/images/icon_create.png" /></span>
					</c:if>
					<c:forEach items="${usage.encounters}" var="encounter">
						<c:choose>
							<c:when test="${encounter.form != null}">
								<c:set var="legendItem" value="${model.formLegend[encounter.form.formId]}" />
								<c:choose>
									<c:when test="${legendItem != null}">
										<span title="${legendItem[0]}"><img src="${pageContext.request.contextPath}/moduleResources/usagestatistics/images/${legendItem[1]}" /></span>
									</c:when>
									<c:otherwise>
										<span title="<spring:message code="usagestatistics.action.formBasedEncounter" />"><img src="${pageContext.request.contextPath}/moduleResources/usagestatistics/images/icon_form.png" /></span>
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:otherwise>
								<c:set var="legendItem" value="${model.encTypeLegend[encounter.encounterType.encounterTypeId]}" />
								<c:choose>
									<c:when test="${legendItem != null}">
										<span title="${legendItem[0]}"><img src="${pageContext.request.contextPath}/moduleResources/usagestatistics/images/${legendItem[1]}" /></span>
									</c:when>
									<c:otherwise>
										<span title="<spring:message code="usagestatistics.action.formlessEncounter" />"><img src="${pageContext.request.contextPath}/moduleResources/usagestatistics/images/icon_encounter.png" /></span>
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
					</c:forEach>
                    <c:if test="${usage.updated}">
						<span title="<spring:message code="usagestatistics.action.updated" />"><img src="${pageContext.request.contextPath}/moduleResources/usagestatistics/images/icon_update.png" /></span>
					</c:if>
                    <c:if test="${usage.voided}">
						<span title="<spring:message code="usagestatistics.action.voided" />"><img src="${pageContext.request.contextPath}/moduleResources/usagestatistics/images/icon_void.png" /></span>
					</c:if>
                </td>
				<td>
					<spring:message code="usagestatistics.foundBy.${model.foundByLabels[usage.foundBy]}" />
					<c:if test="${usage.query != null}">
						(<i>"${usage.query}"</i>)
					</c:if>
				</td>
			</tr>
		</c:forEach>
		<c:if test="${empty model.usages}">
			<tr>
				<td colspan="${model.showPatients ? 5 : 4}" style="padding: 10px; text-align: center">
					<spring:message code="usagestatistics.noresults"/>
				</td>
			</tr>
		</c:if>
	</table>
	
	<c:if test="${!empty model.usages}">
		<ustats_tag:pager pagingInfo="${model.paging}" />
	</c:if>
	
	<div style="text-align: center">
		<h3><spring:message code="usagestatistics.usages.legend" /></h3>
		<table cellspacing="0" cellpadding="3" border="0" align="center">
			<tr>
				<td style="text-align: left; vertical-align: top">
					<img src="${pageContext.request.contextPath}/moduleResources/usagestatistics/images/icon_create.png" />
					<spring:message code="usagestatistics.action.created" />
					<br />
					<img src="${pageContext.request.contextPath}/moduleResources/usagestatistics/images/icon_encounter.png" />
					<spring:message code="usagestatistics.action.formlessEncounter" />
					<br />
					<img src="${pageContext.request.contextPath}/moduleResources/usagestatistics/images/icon_form.png" />
					<spring:message code="usagestatistics.action.formBasedEncounter" />
					<br />
					<img src="${pageContext.request.contextPath}/moduleResources/usagestatistics/images/icon_update.png" />
					<spring:message code="usagestatistics.action.updated" />
					<br />
					<img src="${pageContext.request.contextPath}/moduleResources/usagestatistics/images/icon_void.png" />
					<spring:message code="usagestatistics.action.voided" />
					<br />
				</td>
				<td>&nbsp;</td>
				<td style="text-align: left; vertical-align: top">
					<c:forEach items="${model.formLegend}" var="item">
						<img src="${pageContext.request.contextPath}/moduleResources/usagestatistics/images/${item.value[1]}" />
						${item.value[0]}
						<br />
					</c:forEach>
				</td>
				<td>&nbsp;</td>
				<td style="text-align: left; vertical-align: top">
					<c:forEach items="${model.encTypeLegend}" var="item">
						<img src="${pageContext.request.contextPath}/moduleResources/usagestatistics/images/${item.value[1]}" />
						${item.value[0]}
						<br />
					</c:forEach>
				</td>
			</tr>
		</table>
	</div>
</form>