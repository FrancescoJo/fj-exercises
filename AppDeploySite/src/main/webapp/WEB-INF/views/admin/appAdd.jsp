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
<script charset="UTF-8" src="/js/util/HttpUtils.js"></script>
<script charset="UTF-8" src="/js/pages/admin/App.js"></script>
</head>
<body>
	<h1>Add a new app</h1>

	<form id="appAddForm" enctype="multipart/form-data" method="post" action="" onSubmit='return checkAppAdd("<spring:message code="error.app.no_checked_dist_conds" />");'>
	<table>
	<tbody>
		<tr>
			<td><label for="icon">App icon</label></td>
			<td><input id="appIconFile" type="file" name="icon" /></td>
		</tr>
		<tr>
			<td><label for="name">App name<span class="required">*</span></label></td>
			<td><input id="appNameInput" type="text" name="name" value="${app.name}" required /></td>
		</tr>
		<tr>
			<td><label for="description">App description</label></td>
			<td><textarea id="appDescTextArea" name="description" rows="10">${app.description}</textarea></td>
		</tr>
		<tr>
			<td><label for="stages">Distribution stages</label></td>
			<td id="stageCheckBoxes">
			<c:forEach items="${stageList}" var="stage" varStatus="loop">
				<div><input id="stageCheckbox" type="checkbox" name="stageCheckBox${stage.id}" data-id="${stage.id}" />${stage.name}</div>
			</c:forEach>
			</td>
		</tr>
		<tr>
			<td><label for="stages">Target platform</label></td>
			<td id="platformCheckboxes">
			<c:forEach items="${platformList}" var="platform" varStatus="loop">
				<div><input id="platformCheckbox" type="radio" name="platformRadio${platform.id}" data-id="${platform.id}" />${platform.name}</div>
			</c:forEach>
			</td>
		</tr>
	</tbody>
	</table>
	<input type="submit" />
	</form>

</body>
</html>