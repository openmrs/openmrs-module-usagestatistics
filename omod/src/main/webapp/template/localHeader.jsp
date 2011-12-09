<h2><spring:message code="usagestatistics.title"/></h2>

<ul id="menu">
	<li class="first<c:if test='<%= request.getRequestURI().contains("summary") %>'> active</c:if>">
		<a href="summary.htm"><spring:message code="usagestatistics.menu.summary"/></a>
	</li>
	
	<li <c:if test='<%= request.getRequestURI().contains("time") %>'>class="active"</c:if>>
		<a href="time.htm"><spring:message code="usagestatistics.menu.time"/></a>
	</li>
	
	<li <c:if test='<%= request.getRequestURI().contains("locations") %>'>class="active"</c:if>>
		<a href="locations.htm"><spring:message code="usagestatistics.menu.locations"/></a>
	</li>
	
	<li <c:if test='<%= request.getRequestURI().contains("roles") %>'>class="active"</c:if>>
		<a href="roles.htm"><spring:message code="usagestatistics.menu.roles"/></a>
	</li>
	
	<li <c:if test='<%= request.getRequestURI().contains("users") %>'>class="active"</c:if>>
		<a href="users.htm"><spring:message code="usagestatistics.menu.users"/></a>
	</li>
	
	<li <c:if test='<%= request.getRequestURI().contains("searches") %>'>class="active"</c:if>>
		<a href="searches.htm"><spring:message code="usagestatistics.menu.searches"/></a>
	</li>
	
	<li <c:if test='<%= request.getRequestURI().contains("s/usages") %>'>class="active"</c:if>>
		<a href="usages.htm"><spring:message code="usagestatistics.menu.usages"/></a>
	</li>
<%--	
	<li <c:if test='<%= request.getRequestURI().contains("encounters") %>'>class="active"</c:if>>
		<a href="encounters.htm"><spring:message code="usagestatistics.menu.encounters"/></a>
	</li>
--%>	
	<li <c:if test='<%= request.getRequestURI().contains("/options") %>'>class="active"</c:if>>
		<a href="options.htm"><spring:message code="usagestatistics.menu.options"/></a>
	</li>
</ul>