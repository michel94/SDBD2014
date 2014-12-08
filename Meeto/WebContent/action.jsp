<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<div id="action">
	<br>
	<h2> Action </h2>
	
	<form action="editAction?idAction=${actionBean.idAction}&done=1" method="post">
	Description: <input name="description" type="text" value="${actionBean.action.description}" placeholder="Description"/><br>
	Due Date:<input name="dueto" type="text" value="${actionBean.action.dueTo}" placeholder="Due date"/><br>
	Assigned user: 
	<select name="assigneduser" single>
		<option value="${actionBean.action.assigned_user.id}">${actionBean.action.assigned_user.username}</option>
		<c:forEach items="${userBean.allUsers}" var="user">
			<option value="${user.id}">${user.username}</option>
		</c:forEach>
	</select><br>
	
	Status: <c:if test="${actionBean.action.done eq '1'}">
				Done
			</c:if>
			<c:if test="${actionBean.action.done eq '0'}">
				Pending
			</c:if>
	<br>
	<br>
	<input type="submit" value="Edit Action"> 
	<br>
	</form>
	
	<form action="confirmAction?idAction=${actionBean.idAction}" method="post">
		<input type="submit" value="Confirm Action">
	</form>
	
</div>
</body>
</html>