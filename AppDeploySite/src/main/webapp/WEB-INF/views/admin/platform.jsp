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
<title><spring:message code="site.title" /></title>
<script charset="UTF-8" src="/js/AppDeploy.js"></script>
<script charset="UTF-8" src="/js/util/HttpUtils.js"></script>
<script charset="UTF-8" src="/js/pages/admin/Platforms.js"></script>
</head>
<body>
	<h1>Platform management</h1>

	<c:choose>
		<c:when test="${empty platformList}">
			<div>No platforms were registered. Please register some.</div>
		</c:when>
	
		<c:when test="${!empty platformList}">
			<table>
			<tbody>
				<tr>
					<td>Platforms</td>
					<td>Description</td>
					<td>Delete?</td>
				</tr>
			<c:forEach items="${platformList}" var="platform">
				<tr>
					<td><a href="platform/${platform.id}">${platform.name}</a> <c:if test="${!empty platform.adminUrl}">
						<br/><a href="${platform.adminUrl}">Go to developer console</a>
					</c:if></td>
					<td>${platform.description}</td>
					<td><input type="checkbox" name="deleteCheck" value="${platform.id}" /></td>
				</tr>
			</c:forEach>
			</tbody>
			</table>
		</c:when>
	</c:choose>

	<div><a href="platform/add">Add</a></div>
	<div><a href="#" onclick="onClickDelete('<spring:message code='site.admin.ui.platform.no_platform_selected' />'); return false;">Delete selected</a></div>
</body>
</html>