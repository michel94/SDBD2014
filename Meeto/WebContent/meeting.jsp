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
		
	</div>
	<div>
		<form action="createItem?idMeeting=${meetingBean.meeting.id}" method="post">
			<input name="title" type="text" placeholder="Title"/>
			<input name="description" type="text" placeholder="Description"/>
			<br>
			<input type="submit" value="Add Item">
		</form>
	
	</div>
		<h3> Actions </h3>
		
		<c:forEach items="${meetingBean.meeting.actions}" var="act">
			<a href="selectAction?idAction=${act.id}"> ${act.description} </a> ${act.dueTo} 
			<c:if test="${act.done} == 1"> Done </c:if> <c:if test="${act.done} == 0"> Not Done Yet </c:if> 
			<br>
		</c:forEach>
		
		<br>
		
		<form action="createAction?idMeeting=${meetingBean.meeting.id}" method="post">
			<input name="description" type="text" placeholder="Description"/>
			<input name="dueTo" type="text" placeholder="Due To"/>
			<select name="assignedUser">
				<c:forEach items="${userBean.allUsers}" var="user">
					<option value=${user.id}> ${user.username} </option>
				</c:forEach>
			</select>
			<br>
			<input type="submit" value="Add Action">
		</form>
		
		<br>
		
	<div>
		<h3> Users participating in the meeting: </h3>
		<c:forEach items="${meetingBean.usersFromMeeting}" var="user">
			${user.username} <br>
		</c:forEach>
		
		<h4> Invite users to the meeting </h4>
		
		<form action="inviteUsers?idMeeting=${meetingBean.meeting.id}" method="post">
			<c:forEach items="${userBean.allUsers}" var="user">
				<input type="checkbox" name="invUsers" id="${user.id}" /> ${user.username} <br>
			</c:forEach>
			<br>
			<input type="submit" value="Invite Users">
		</form>
		
		<br>
	</div>
	
 </div>