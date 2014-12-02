<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Main</title>
</head>
<body>
Meeto Main

<br>
<c:forEach items="${connectionBean.meetings}" var="meeting">
	<c:out value="${meeting.title}" /><br>
</c:forEach>

<br>
<c:forEach items="${connectionBean.meetings}" var="meeting">
	<h2> <c:out value="${meeting.title}" /><br> </h2>
	<c:forEach items="${meeting.items}" var="item">
		<c:out value="${item.title}" /><br>
	</c:forEach>
</c:forEach>

<br>
<c:forEach items="${connectionBean.userActions}" var="action">
	<c:out value="${action.title}" /><br>
</c:forEach>

<button> ola </button>

</body>
</html>