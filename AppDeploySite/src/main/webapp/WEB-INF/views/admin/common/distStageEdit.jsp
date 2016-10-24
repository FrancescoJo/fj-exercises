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
<form:form id="stageAddForm" method="post" action="">
<table>
<tbody>
	<tr>
		<td><label for="name">Stage name (Max 32 letters)<span class="required">*</span></label></td>
		<td><input id="stageNameInput" type="text" name="name" value="${stage.name}" required /></td>
	</tr>
	<tr>
		<td><label for="description">Stage description</label></td>
		<td><textarea id="stageDescTextArea" name="description" rows="10">${stage.description}</textarea></td>
	</tr>
	<tr>
		<td><label for="preserveCount">Maximum binary perservation count</label></td>
		<td><input id="stagePreserveCountInput" type="number" name="preserveCount" value="${stage.preserveCount}"/></td>
	</tr>
</tbody>
</table>
<input type="submit" />
</form:form>
