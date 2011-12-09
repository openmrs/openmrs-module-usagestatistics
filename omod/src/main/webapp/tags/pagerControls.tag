<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglibs/c-rt.tld" %>
<%@ taglib prefix="spring" uri="/WEB-INF/taglibs/spring.tld" %>

<%@ attribute name="pagingInfo" required="true" type="org.openmrs.module.usagestatistics.util.PagingInfo" %>

<script type="text/javascript">
function pager_set_offset(offset) {
	var curUrl = location.href;
	if (curUrl.match(/offset=/))
		newUrl = curUrl.replace(/offset=\d*/, "offset=" + offset);
	else if (curUrl.indexOf("?") >= 0)
		newUrl = curUrl + "&offset=" + offset;
	else
		newUrl = curUrl + "?offset=" + offset;
		
	location.href = newUrl;
}
</script>

<input type="button"
	value="&lt;&lt;"
	${pagingInfo.pageOffset == 0 ? 'disabled="disabled"' : ""}
	onclick="pager_set_offset(0)"
	title="<spring:message code="usagestatistics.pager.first" />"
/>

<input type="button"
	value="&lt;"
	${pagingInfo.pageOffset == 0 ? 'disabled="disabled"' : ""}
	onclick="pager_set_offset(${pagingInfo.pageOffset - pagingInfo.pageSize})"
	title="<spring:message code="usagestatistics.pager.previous" />"
/>

<input type="button"
	value="&gt;"
	${pagingInfo.pageOffset ge (pagingInfo.resultsTotal - pagingInfo.pageSize) ? 'disabled="disabled"' : ""}
	onclick="pager_set_offset(${pagingInfo.pageOffset + pagingInfo.pageSize})"
	title="<spring:message code="usagestatistics.pager.next" />"
/>

<input type="button"
	value="&gt;&gt;"
	${pagingInfo.pageOffset ge (pagingInfo.resultsTotal - pagingInfo.pageSize) ? 'disabled="disabled"' : ""}
	onclick="pager_set_offset(${pagingInfo.resultsTotal - pagingInfo.pageSize})"
	title="<spring:message code="usagestatistics.pager.last" />"
/>