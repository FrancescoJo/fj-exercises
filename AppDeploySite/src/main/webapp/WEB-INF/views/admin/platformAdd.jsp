<%-- 
Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
Read LICENCE file in project root for licence terms of this software.

Author: Francesco Jo
Since : 21 - Dec - 2014
--%>
<%@page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@page trimDirectiveWhitespaces="true"%>
<%@page session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>App distribution site</title>
<script charset="UTF-8" src="/js/AppDeploy.js"></script>
<script charset="UTF-8" src="/js/util/ObjectUtils.js"></script>
<script charset="UTF-8" src="/js/pages/admin/Platforms.js"></script>
</head>
<body>
	<h1>Add a new platform</h1>

	<div>Select from presets: 
	<c:if test="${!empty platformPresets}">
		<select onfocus="this.oldValue = this.value;" onchange="return onPresetSelected(this, '<spring:message code='site.admin.ui.platform.do_you_want_to_apply_preset' />');">
			<option>
				--- Default presets ---
			</option>
		<c:forEach items="${platformPresets}" var="preset">
			<option
				data-name="${preset.name}"
				data-description="${preset.description}"
				data-adminUrl="${preset.adminUrl}">
				${preset.name}
			</option>
		</c:forEach>
		</select>
	</c:if>
	</div>
	
	<div>Or, input manually:
	<jsp:include page="common/platformEdit.jsp" />
	</div>
</body>
</html>