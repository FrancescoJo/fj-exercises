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
	<h1>App: ${app.name}</h1>
	<div>Manage upload history:
	<c:forEach items="${app.stageList}" var="distStage">
	<span><a href="${app.id}/${distStage.id}">${distStage.name}</a></span>
	</c:forEach>
	</div>

	<div>App details:</div>
	<table>
	<tbody>
		<tr>
			<td>App name:</td>
			<td><img src="/static/img/icons/${app.iconName}" /> ${app.name}</td>
		</tr>
		<tr>
			<td>App description:</td>
			<td>${app.description}</td>
		</tr>
		<tr>
			<td>Available platforms:</td>
			<td>
				<c:forEach items="${app.platformList}" var="platform">
				<span>${platform.name}</span>
				</c:forEach>
			</td>
		</tr>
	</tbody>
	</table>
</body>
</html>