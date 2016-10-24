<%-- 
Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
Read LICENCE file in project root for licence terms of this software.

Author: Francesco Jo
Since : 21 - Dec - 2014
--%>
<%@page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@page trimDirectiveWhitespaces="true"%>
<%@page session="false"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<form:form id="platformAddForm" method="post" action="">
<table>
<tbody>
	<tr>
		<td><label for="name">Platform name (Max 32 letters)<span class="required">*</span></label></td>
		<td><input id="platformNameInput" type="text" name="name" value="${platform.name}" required /></td>
	</tr>
	<tr>
		<td><label for="description">Platform description</label></td>
		<td><input id="platformDescInput" type="text" name="description" value="${platform.description}"/></td>
	</tr>
	<tr>
		<td><label for="adminUrl">Platform developer console URL</label></td>
		<td><input id="platformAdminUrlInput" type="text" name="adminUrl" value="${platform.adminUrl}"/></td>
	</tr>
</tbody>
</table>
<input type="submit" />
</form:form>
