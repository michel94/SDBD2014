<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="groups">
	<div> 
		Groups: <br>
		<c:forEach items="${groupBean.groupsFromUser}" var="value">
			<a href="selectGroup?idGroup=${value.idgroup}"> ${value.name} </a> <br>
		</c:forEach>
	</div>
	
	
	
</div>

