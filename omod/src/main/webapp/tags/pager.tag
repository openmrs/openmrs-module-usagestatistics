<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="ustats_tag" tagdir="/WEB-INF/tags/module/usagestatistics" %>

<%@ attribute name="pagingInfo" required="true" type="org.openmrs.module.usagestatistics.util.PagingInfo" %>

<div style="background-color: #DDD; padding: 2px; overflow: hidden">
	<div style="float: left">
		<ustats_tag:pagerControls pagingInfo="${pagingInfo}" />
	</div>		
	<div style="float: right; margin: 2px">		
		<ustats_tag:pagerInfo pagingInfo="${pagingInfo}" />
	</div>
</div>