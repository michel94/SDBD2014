<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">
	        Meeting: ${meetingBean.meeting.title} <small>Hosted by ${meetingBean.meeting.leader.username}</small>
	        <button onclick="location.href='leaveMeeting?IdMeeting=${meetingBean.meeting.id}'" type="button" class="btn btn-danger pull-right">Leave meeting</button>
        </h1>
    </div>
    <!-- /.col-lg-12 -->
</div>

<div class="row">
	<div class="col-lg-5">
		<div class="panel panel-default">
	        <div class="panel-heading">
	            Meeting details
	        </div>
	        <div class="panel-body">
                <form role="form" action="editMeeting?idMeeting=${meetingBean.meeting.id}" method="post">
                    <div class="form-group">
                        <label>Title</label>
                        <input name="title" type="text" class="form-control" value="${meetingBean.meeting.title}"/>
                    </div>
                    <div class="form-group">
                        <label>Description</label>
                        <input name="description" type="text" class="form-control" value="${meetingBean.meeting.description}"/>
                    </div>
                    <div class="form-group">
                        <label>Date and time</label>
                        <input name="datetime" type="text" class="form-control" value="${meetingBean.meeting.datetime}"/>
                    </div>
                    <div class="form-group">
                        <label>Location</label>
                        <input name="location" type="text" class="form-control" value="${meetingBean.meeting.location}"/>
                    </div>
                    <input type="submit" class="btn btn-primary" value="Edit" />
				</form>
			</div>
		</div>
	</div>
	
	<div class="col-lg-7">
		<div class="panel panel-default">
			<div class="panel-heading">
			    Meeting Users
			</div>
			<div class="panel-body">
				<ul class="nav nav-tabs">
                    <li class="active"><a href="#invited-users-tab" data-toggle="tab">Users</a>
                    </li>
                    <li><a href="#invite-users-tab" data-toggle="tab">Invite Users</a>
                    </li>
                    <li><a href="#invite-group-tab" data-toggle="tab">Invite Group</a>
                    </li>
                </ul>
                <div class="tab-content">
            		<div class="tab-pane fade in active" id="invited-users-tab">
            			<br/>
					    <div class="table-responsive">
					        <table class="table table-striped table-bordered table-hover" id="invited-users">
					             <thead>
					                 <tr>
					                     <th>Username</th>
					                 </tr>
					             </thead>
					             <tbody>
					                 <c:forEach items="${meetingBean.usersFromMeeting}" var="user">
					                 <tr>
					                 	<td>${user.username}</td>
					                 </tr>
									</c:forEach>
						         </tbody>
						     </table>
						 </div>
						 <!-- /.table-responsive -->
					 </div>
					 <div class="tab-pane fade in" id="invite-users-tab">
					 	<br/>
					 	<form role="form" action="addUsersToMeeting?IdMeeting=${meetingBean.meeting.id}" method="post">
							<div class="form-group">
							<label>Select users to invite</label>
								<select multiple name="UserList" class="form-control">
									<c:forEach items="${userBean.allUsers}" var="user">
										<option value="${user.id}">${user.username}</option>
									</c:forEach>
								</select>
								<p class="help-block">Ctrl+Mouse click to select multiple users</p>
							</div>
							<input type="submit" class="btn btn-primary" value="Invite users">
						</form>	
					 </div>
					 <div class="tab-pane fade in" id="invite-group-tab">
					 	<br/>
					 	<form role="form" action="addGroupToMeeting?IdMeeting=${meetingBean.meeting.id}" method="post">
							<div class="form-group">
							<label>Select one group to invite</label>
								<select name="idgroup" class="form-control">
									<c:forEach items="${groupBean.groupsFromUser}" var="group">
										<option value="${group.idgroup}">${group.name}</option>
									</c:forEach>
								</select>
							</div>
							<input type="submit" class="btn btn-primary" value="Invite group">
						</form>	
					 </div>
				 </div>
			</div>
		</div>	
	</div>					
</div>	

<div class="row">
	<div class="col-lg-5">
		<div class="panel panel-default">
	        <div class="panel-heading">
	            Create item
	        </div>
	        <div class="panel-body">
                <form role="form" action="createItem?idMeeting=${meetingBean.meeting.id}" method="post">
                    <div class="form-group">
                        <label>Title</label>
                        <input name="title" type="text" class="form-control" />
                    </div>
                    <div class="form-group">
                        <label>Description</label>
                        <input name="description" type="text" class="form-control" />
                    </div>
                    <input type="submit" class="btn btn-primary" value="Create item" />
				</form>
			</div>
		</div>
	</div>
	<div class="col-lg-7">
		<div class="panel panel-default">
	        <div class="panel-heading">
	            Meeting items
	        </div>
	        <div class="panel-body">
	        	<div class="table-responsive">
			        <table class="table table-striped table-bordered table-hover" id="meeting-items">
			             <thead>
			                 <tr>
			                     <th>Title</th>
			                     <th>Description</th>
			                 </tr>
			             </thead>
			             <tbody>
			                 <c:forEach items="${meetingBean.meeting.items}" var="it">
			                 <tr onclick="location.href='selectItem?idItem=${it.id}'" style="cursor: pointer;">
			                 	<td>${it.title}</td>
			                 	<td>${it.description}</td>
			                 </tr>
							</c:forEach>
				         </tbody>
				     </table>
				 </div>
	        </div>
       	</div>
   	</div>
</div>

<div class="row">
	<div class="col-lg-5">
		<div class="panel panel-default">
	        <div class="panel-heading">
	            Create action
	        </div>
	        <div class="panel-body">
                <form role="form" action="createAction?IdMeeting=${meetingBean.meeting.id}" method="post">
                    <div class="form-group">
                        <label>Description</label>
                        <input name="description" type="text" class="form-control" />
                    </div>
                    <div class="form-group">
						<label>Assign a user</label>
						<select name="assigneduser" class="form-control">
							<c:forEach items="${userBean.allUsers}" var="user">
								<option value="${user.id}">${user.username}</option>
							</c:forEach>
						</select>
					</div>
					<div class="form-group">
                        <label>Due to</label>
                        <input name="dueto" type="text" class="form-control" />
                    </div>
	              	<input type="submit" class="btn btn-primary" value="Create action" />
				</form>
			</div>
		</div>
	</div>
	<div class="col-lg-7">
		<div class="panel panel-default">
	        <div class="panel-heading">
	            Meeting actions
	        </div>
	        <div class="panel-body">
	        	<div class="table-responsive">
			        <table class="table table-striped table-bordered table-hover" id="meeting-actions">
			             <thead>
			                 <tr>
			                     <th>Description</th>
			                     <th>Assigned to</th>
			                     <th>Due to</th>
			                     <th>Status</th>
			                 </tr>
			             </thead>
			             <tbody>
			                 <c:forEach items="${meetingBean.meeting.actions}" var= "act">
			                 <tr onclick="location.href='selectAction?idAction=${act.id}'" style="cursor: pointer;">
			                 	<td>${act.description}</td>
			                 	<td>${act.assigned_user.username}</td>
			                 	<td>${act.dueTo}</td>
			                 	<td>
			                 		<c:if test="${act.done eq '1'}">
										Done
									</c:if>
									<c:if test="${act.done eq '0'}">
										Pending
									</c:if>
			                 	</td>
			                 </tr>
							</c:forEach>
				         </tbody>
				     </table>
				 </div>
	        </div>
       	</div>
   	</div>
</div>

<script>
	$(document).ready(function() {
	    $('#invited-users').dataTable();
	});
</script>

<script>
	$(document).ready(function() {
	    $('#meeting-items').dataTable();
	});
</script>

<script>
	$(document).ready(function() {
	    $('#meeting-actions').dataTable();
	});
</script>