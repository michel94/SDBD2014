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
Lista de meetings:<br>
<c:forEach items="${connectionBean.meetings}" var="mt">
	<c:out value="${mt.title}" /><br>
</c:forEach>

<br>

Test meeting 1:<br>
<c:set value="${connectionBean.meeting.title}" var= "meeting">
	Titulo: <c:out value="${meeting.title}" /><br>
	Descrição: <c:out value="${meeting.description}" /><br>
	Localização: <c:out value="${meeting.location}" /><br>
	Data: <c:out value="${meeting.datetime}" /><br>
</c:set>

<button> ola </button>

</body>
</html>