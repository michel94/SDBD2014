<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">
	       Create group
       	</h1>
    </div>
    <!-- /.col-lg-12 -->
</div>

<div class="row">
	<div class="col-lg-5">
		<div class="panel panel-default">
	        <div class="panel-heading">
	            Group details
	        </div>
	        <div class="panel-body">
                <form role="form" action="createGroup" method="post">
                    <div class="form-group">
                        <label>Name</label>
                        <input name="groupname" type="text" class="form-control" />
                    </div>
                    <div class="form-group">
						<label>Assign a user</label>
						<select multiple name="UserList" class="form-control">
							<c:forEach items="${userBean.allUsers}" var="user">
								<option value="${user.id}">${user.username}</option>
							</c:forEach>
						</select>
						<p class="help-block">Ctrl+Mouse click to select multiple users</p>
					</div>
	              	<input type="submit" class="btn btn-primary" value="Create action" />
				</form>
			</div>
		</div>
	</div>
</div>
