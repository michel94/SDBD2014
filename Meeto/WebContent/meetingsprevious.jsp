<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">Previous meetings</h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
 <div class="row">
	 <div class="col-lg-12">
         <div class="table-responsive">
             <table class="table table-striped table-bordered table-hover" id="meetings-prev">
                 <thead>
                     <tr>
                         <th>Title</th>
                         <th>Description</th>
                         <th>Date and Time</th>
                         <th>Location</th>
                         <th>Host</th>
                     </tr>
                 </thead>
                 <tbody>
	                 <c:forEach items="${meetingBean.previousMeetings}" var="value">
	                     <tr onclick="location.href='selectMeeting?idMeeting=${value.id}'" style="cursor: pointer;">
	                     	<td>${value.title}</td>
	                     	<td>${value.description}</td>
	                     	<td>${value.datetime}</td>
	                     	<td>${value.location}</td>
	                     	<td>${value.leader.username}</td>
	                     </tr>
					</c:forEach>
                 </tbody>
             </table>
         </div>
         <!-- /.table-responsive -->	
	 </div>
	 <!-- /.col-lg-12 -->
</div>
<!-- /.row -->

<script>
	$(document).ready(function() {
	    $('#meetings-prev').dataTable();
	});
</script>