<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">
	        Action
	        <c:if test="${actionBean.action.done eq '0'}">
	        	<button onclick="location.href='confirmAction?idAction=${actionBean.idAction}'" type="button" class="btn disabled btn-success pull-right">Mark this action as Done</button>
        	</c:if>
        	<c:if test="${actionBean.action.done eq '1'}">
	        	<button onclick="location.href='confirmAction?idAction=${actionBean.idAction}'" type="button" class="btn disabled btn-primary pull-right">This actions was already marked as Done</button>
        	</c:if>
        </h1>
    </div>
    <!-- /.col-lg-12 -->
</div>

<div class="row">
	<div class="col-lg-6">
		<div class="panel panel-default">
	        <div class="panel-heading">
	            Action details
	        </div>
	        <div class="panel-body">
                <form role="form" action="editAction?idAction=${actionBean.idAction}&done=1" method="post">
                    <div class="form-group">
                        <label>Description</label>
                        <input name="description" type="text" class="form-control" value="${actionBean.action.description}"/>
                    </div>
                    <div class="form-group">
						<label>Assign a user</label>
						<select name="assigneduser" class="form-control">
							<c:forEach items="${userBean.allUsers}" var="user">
								<c:choose>
									<c:when test="${user.id eq actionBean.action.assigned_user.id}">
										<option selected value="${user.id}">${user.username}</option>
									</c:when>
									<c:otherwise>
										<option value="${user.id}">${user.username}</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</select>
					</div>
					<div class="form-group">
                        <label>Due to</label>
                        <input name="dueto" type="text" class="form-control" value="${actionBean.action.dueTo}"/>
                    </div>
	              	<input type="submit" class="btn btn-primary" value="Edit action" />
				</form>
			</div>
		</div>
	</div>
</div>
