<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">
	        Group ${groupBean.group.name}
        </h1>
    </div>
    <!-- /.col-lg-12 -->
</div>

<div class="row">
	<div class="col-lg-7">
		<div class="panel panel-default">
			<div class="panel-heading">
			    Users from this group
			</div>
			<div class="panel-body">
				<div class="table-responsive">
			        <table class="table table-striped table-bordered table-hover" id="group-users">
			             <thead>
			                 <tr>
			                     <th>Username</th>
			                 </tr>
			             </thead>
			             <tbody>
			                 <c:forEach items="${groupBean.group.users}" var="value">
			                 <tr>
			                 	<td>${value.username}</td>
			                 </tr>
							</c:forEach>
				         </tbody>
				     </table>
				 </div>
			 </div>
		 </div>
	 </div>
	<div class="col-lg-5">
		<div class="panel panel-default">
			<div class="panel-heading">
			    Users operations
			</div>
			<div class="panel-body">
				<ul class="nav nav-tabs">
                    <li class="active"><a href="#add-users-tab" data-toggle="tab">Add Users</a>
                    </li>
                    <li><a href="#remove-users-tab" data-toggle="tab">Remove Users</a>
                    </li>
                </ul>
                <div class="tab-content">
            		<div class="tab-pane fade in active" id="add-users-tab">
            			<br/>
					 	<form role="form" action="addUsersToGroup?IdGroup=${groupBean.group.idgroup}" method="post">
							<div class="form-group">
							<label>Select users to invite</label>
								<select multiple name="UserList" class="form-control">
									<c:forEach items="${userBean.allUsers}" var="user">
										<option value="${user.id}">${user.username}</option>
									</c:forEach>
								</select>
								<p class="help-block">Ctrl+Mouse click to select multiple users</p>
							</div>
							<input type="submit" class="btn btn-primary" value="Add users">
						</form>	
					 </div>
					 <div class="tab-pane fade in" id="remove-users-tab">
					 	<br/>
					 	<form role="form" action="removeUsersFromGroup?IdGroup=${groupBean.group.idgroup}" method="post">
							<div class="form-group">
							<label>Select users to remove</label>
								<select multiple name="UserList" class="form-control">
									<c:forEach items="${userBean.allUsers}" var="user">
										<option value="${user.id}">${user.username}</option>
									</c:forEach>
								</select>
								<p class="help-block">Ctrl+Mouse click to select multiple users</p>
							</div>
							<input type="submit" class="btn btn-primary" value="Remove users">
						</form>
					 </div>
				 </div>
			</div>
		</div>	
	</div>					
</div>

<script>
	$(document).ready(function() {
	    $('#group-users').dataTable();
	});
</script>