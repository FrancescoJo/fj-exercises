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
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>App distribution site</title>
</head>
<body>
	<h1>Hello, admin!</h1>
	<div>
		<a href="/">Go to apps page</a>
	</div>
	<div>
		<a href="/admin/platform">Manage platform</a>
	</div>
	<div>
		<a href="/admin/distStage">Manage distribution stage</a>
	</div>
	<div>
		<a href="/admin/app">Manage app</a>
	</div>
	<div>
		<a href="/admin/sys">Manage system</a>
	</div>
</body>
</html>