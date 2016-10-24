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
	<h1><img src="/static/img/icons/${app.iconName}" /> ${app.name}</h1>
	<div>
		Target platform:
		<c:forEach items="${app.platformList}" var="platform">
		<span> ${platform.name} </span>
		</c:forEach>
	</div>
	
	<div>
	<c:forEach items="${app.stageList}" var="distStage">
	<c:choose>
		<c:when test="${app.stageId eq distStage.id}">
			<span><em>${distStage.name}</em></span>
		</c:when>
		<c:otherwise>
			<span><a href="/app/${app.id}/${distStage.id}">${distStage.name}</a></span>
		</c:otherwise>
	</c:choose>	
	</c:forEach>
	</div>

	<hr/>
	<table>
	<c:forEach items="${historyMap}" var="historyEntry">
		<tr>
			<td>${historyEntry.key}</td>
			<td>Uploaded on</td>
		</tr>
		<c:forEach items="${historyEntry.value}" var="history">
			<tr>
				<td>
					<div>
						<span><a href="/static/bintray/${history.appId}/${history.stageId}/${history.binaryName}">${history.binaryName}</a></span>
						<span><c:forEach items="${history.tagList}" var="tag">
							${tag}
						</c:forEach>
						</span>
					</div>
					<div>
						${history.description}
					</div>
				</td>
				<td>
					${history.uploadTimeStr}
				</td>
			</tr>
		</c:forEach>
	</c:forEach>
	</table>
	<hr/>
	<jsp:include page="common/pagination.jsp" />
</body>
</html>