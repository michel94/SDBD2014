<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="meetings">
	<div> 
		Upcoming Meetings: <br>
		<c:forEach items="${meetingBean.nextMeetings}" var="value">
			<a href="selectMeeting?idMeeting=${value.id}"> ${value.title} </a> <br>
		</c:forEach>
	</div>
	
</div>

