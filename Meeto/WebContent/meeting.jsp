<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="meeting">
	<h3>Meeting: ${meetingBean.meeting.title}</h3>
	
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
	<h3>Users in this meeting:</h3>
		<c:forEach items="${meetingBean.usersFromMeeting}" var="user">
			${user.username}<br>
		</c:forEach>
	</div>
	
	<div>
		<h3> Items </h3>
		
		<c:forEach items="${meetingBean.meeting.items}" var="it">
			<a href="selectItem?idItem=${it.id}"> ${it.title} </a> ${it.description}<br>
		</c:forEach>
		
		<h3> New Item </h3>
		<form action="createItem?idMeeting=${meetingBean.meeting.id}" method="post">
			<input name="title" type="text" placeholder="Title"/>
			<input name="description" type="text" placeholder="Description"/>
			<br>
			<input type="submit" value="Add Item">
		</form>
	</div>
	
	<div>
		<h3>Actions</h3>
		<c:forEach items="${meetingBean.meeting.actions}" var= "act">
			<a href="selectAction?idAction=${act.id}">${act.description}</a> <!--  Assigned user: ${act.assigned_user.username} Due-date: ${act.dueTo} -->Status: 
			<c:if test="${act.done eq '1'}">
				Done
			</c:if>
			<c:if test="${act.done eq '0'}">
				Pending
			</c:if>
			<br>
		</c:forEach>
		
		<h3> New Action </h3>
		<form action="createAction?IdMeeting=${meetingBean.meeting.id}" method="post">
			<input name="description" type="text" placeholder="Description"/>
			<select name="assigneduser" single>
				<c:forEach items="${userBean.allUsers}" var="user">
					<option value="${user.id}">${user.username}</option>
				</c:forEach>
			</select>
			<input name="dueto" type="text" placeholder="Due-Date"/>
			<br>
			<input type="submit" value="Add Action">
		</form>
	</div>
	<br>
	
	<div>
		<form action="leaveMeeting?IdMeeting=${meetingBean.meeting.id}" method="post">
			<input type="submit" value="Leave meeting">
		</form>
	</div>
	
	<div>
	<h3>Add users to this meeting</h3>
		<form action="addUsersToMeeting?IdMeeting=${meetingBean.meeting.id}" method="post">
			<select name="UserList" multiple>
				<c:forEach items="${userBean.allUsers}" var="user">
					<option value="${user.id}">${user.username}</option>
				</c:forEach>
			</select>
			<input type="submit" value="Invite Users"><br>
			Ctrl+click to select multiple users<br>
		</form>
	</div>
	
	<div>
	<h3>Add group to this meeting</h3>
		<form action="addGroupToMeeting?IdMeeting=${meetingBean.meeting.id}" method="post">
			<select name="idgroup" single>
				<c:forEach items="${groupBean.groupsFromUser}" var="group">
					<option value="${group.idgroup}">${group.name}</option>
				</c:forEach>
			</select>
			<input type="submit" value="Invite group"><br>
		</form>
	</div>
	
 </div>