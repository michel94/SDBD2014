<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="meeting">
	Current Meeting: <br>
	
	<form action="editMeeting?idMeeting=${meetingBean.meeting.id}" method="post">
		Title:
		<input name="title" type="text" value="${meetingBean.meeting.title}"/><br>
		Description:
		<input name="description" type="text" value="${meetingBean.meeting.description}" /><br>
		Datetime:
		<input name="datetime" type="text" value="${meetingBean.meeting.datetime}" /><br>
		Location:
		<input name="location" type="text" value="${meetingBean.meeting.location}" /><br>
		
		<input type="submit" value="Edit">
	</form>
	
	<div>
		<h3> Items </h3>
		
		<c:forEach items="${meetingBean.meeting.items}" var="it">
			<a href="selectItem?idItem=${it.id}"> ${it.title} </a> ${it.description}<br>
		</c:forEach>
		
		<h3> New Item </h3>
		
		</div><form action="createItem?idMeeting=${meetingBean.meeting.id}" method="post">
			<input name="title" type="text" placeholder="Title"/>
			<input name="description" type="text" placeholder="Description"/>
			<br>
			<input type="submit" value="Add Item">
		</form>
	
 </div>