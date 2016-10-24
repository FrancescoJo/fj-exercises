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
<script charset="UTF-8" src="/js/pages/admin/App.js"></script>
</head>
<body>
	<h1>App registration</h1>

	<div>No selectable platforms or distribution stages. Please add some first.</div>

	<div>
		<a href="app/add">Add</a>
	</div>
	<div>
		<a href="#" onclick="onClickDelete('<spring:message code='site.admin.ui.app.no_app_selected' />', '<spring:message code='site.admin.ui.no_message_defined' />'); return false;">Delete
			selected</a>
	</div>
</body>
</html>