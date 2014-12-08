<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div>
	<h2> Create Group </h2>

	<form action="createGroup" method="post">
		Name:
		<input name="groupname" type="text"/><br><br>
		
		Select users to add to the group:<br>
		
			<select name="UserList" multiple>
				<c:forEach items="${userBean.allUsers}" var="user">
					<option value="${user.id}">${user.username}</option>
				</c:forEach>
			</select>
			
			Ctrl+click to select multiple users<br><br>
		
		<input type="submit" value="Create">
	</form>
</div>
