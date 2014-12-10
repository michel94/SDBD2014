<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="meetings">
	<div> 
		Meetings: <br>
		<c:forEach items="${meetingBean.allMeetings}" var="value">
			<a href="selectMeeting?idMeeting=${value.id}"> ${value.title} </a> <br>
		</c:forEach>
	</div>
	
</div>
