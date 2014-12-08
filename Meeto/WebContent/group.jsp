<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="groups">
	
	<h3>Group: ${groupBean.group.name}</h3> <br>
	Users:<br>
	<c:forEach items="${groupBean.group.users}" var="value">
			${value.username}<br>
	</c:forEach>
	<br>
	
	<h3>Remove user from this group</h3> 
		<form action="removeUsersFromGroup?IdGroup=${groupBean.group.idgroup}" method="post">
			<select name="UserList" multiple>
				<c:forEach items="${groupBean.group.users}" var="user">
					<option value="${user.id}">${user.username}</option>
				</c:forEach>
			</select>
			<input type="submit" value="Remove Users"><br>
			Ctrl+click to select multiple users<br>
		</form>
		
	<h3>Add users to this group</h3>
		<form action="addUsersToGroup?IdGroup=${groupBean.group.idgroup}" method="post">
			<select name="UserList" multiple>
				<c:forEach items="${userBean.allUsers}" var="user">
					<option value="${user.id}">${user.username}</option>
				</c:forEach>
			</select>
			<input type="submit" value="Add Users"><br>
			Ctrl+click to select multiple users<br>
		</form>
	
</div>

