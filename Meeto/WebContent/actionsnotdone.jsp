<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<h2>Actions not done</h2><br>
	
	<c:forEach items="${actionBean.userActions}" var="act">
		<c:if test="${act.done eq '0'}">
			<a href="selectAction?idAction=${act.id}">${act.description}</a> Status: 
			<c:if test="${act.done eq '1'}">
				Done
			</c:if>
			<c:if test="${act.done eq '0'}">
				Pending
			</c:if>
			<br>
		</c:if>
	</c:forEach>