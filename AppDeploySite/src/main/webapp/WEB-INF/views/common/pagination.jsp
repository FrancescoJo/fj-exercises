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
<c:if test="${!empty page}">
	<div>
		<c:choose>
			<c:when test="${page.lowerEnabled}">
				<span><a href="?page=${page.low - 1}">&lt;</a></span>
			</c:when>
			<c:otherwise>
				<span>&lt;</span>
			</c:otherwise>
		</c:choose>
		<c:forEach begin="${page.low}" end="${page.high}" var="val">
			<c:choose>
				<c:when test="${page.current eq val}">
					<span><em>${val}</em></span>
				</c:when>
				<c:otherwise>
					<span><a href="?page=${val}">${val}</a></span>
				</c:otherwise>
			</c:choose>
		</c:forEach>
		<c:choose>
			<c:when test="${page.higherEnabled}">
				<span><a href="?page=${page.high + 1}">&gt;</a></span>
			</c:when>
			<c:otherwise>
				<span>&gt;</span>
			</c:otherwise>
		</c:choose>
	</div>
</c:if>
