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
<script charset="UTF-8" src="/js/pages/admin/UploadHistory.js"></script>
</head>
<body>
	<h1>App: ${app.name} (${stage.name})</h1>
	<div>Uploading by <span>curl</span>: </div>
<pre>
$ curl -i \
	-F "authToken=${app.authToken}" \
	-F "version=&lt;YOUR_BUILD_VERSION&gt;" \
	-F "description=&lt;YOUR_BUILD_DESCRIPTION&gt;" \
	-F "tags=&lt;COMMA_SEPARATED_STRING_LIST&gt;" \
	-F "binTray=@&lt;YOUR_BUILD_BINARY_PATH&gt;" \
	${scheme}://${host}<c:if test="${!empty port}">:${port}</c:if>/upload/${app.id}/${stage.id}
</pre>
<div>
	<table>
	<c:forEach items="${historyMap}" var="historyEntry">
		<tr>
			<td colspan="2">${historyEntry.key}</td>
			<td>delete?</td>
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
				<td>
					<input type="checkbox" name="deleteCheck" value="${history.id}" />
				</td>
			</tr>
		</c:forEach>
	</c:forEach>
	</table>
</div>
<a href="#" onclick="onClickDelete('<spring:message code='site.admin.ui.uploadhistory.no_history_selected' />'); return false;">Delete
	selected</a>
	<jsp:include page="../common/pagination.jsp" />
</body>
</html>