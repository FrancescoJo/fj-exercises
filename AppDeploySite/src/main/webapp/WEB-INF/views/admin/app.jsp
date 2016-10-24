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
<script charset="UTF-8" src="/js/net/AjaxClient.js"></script>
<script charset="UTF-8" src="/js/util/ObjectUtils.js"></script>
<script charset="UTF-8" src="/js/pages/admin/App.js"></script>
</head>
<body>
	<h1>App management</h1>

	<c:choose>
		<c:when test="${empty appList}">
			<div>No apps were registered. Please register some.</div>
		</c:when>

		<c:when test="${!empty appList}">
			<table>
				<tbody>
					<tr>
						<td>App</td>
						<td>Delete?</td>
					</tr>
					<c:forEach items="${appList}" var="app">
						<tr>
							<td><img src="/static/img/icons/${app.iconName}" /><a href="app/${app.id}">${app.name}</a></td>
							<td><input type="checkbox" name="deleteCheck" value="${app.id}" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:when>
	</c:choose>

	<div>
		<a href="app/add">Add</a>
	</div>
	<div>
		<a href="#" onclick="onClickDelete('<spring:message code='site.admin.ui.app.no_app_selected' />', '<spring:message code='site.admin.ui.no_message_defined' />'); return false;">Delete
			selected</a>
	</div>
</body>
</html>