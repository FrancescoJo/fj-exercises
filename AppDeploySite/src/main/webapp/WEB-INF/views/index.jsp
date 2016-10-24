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
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="site.title" /></title>
</head>
<body>
	<h1>List of my Apps</h1>
	<c:choose>
		<c:when test="${empty appList}">
			No apps were registered. Please register some.
		</c:when>
		
		<c:when test="${!empty appList}">
			<table>
			<tbody>
			<c:forEach items="${appList}" var="app">
				<tr>
					<td><img src="/static/img/icons/${app.iconName}" /><a href="app/${app.id}">${app.name}</a></td>
				</tr>
			</c:forEach>
			</tbody>
			</table>
		</c:when>
	</c:choose>
</body>
</html>