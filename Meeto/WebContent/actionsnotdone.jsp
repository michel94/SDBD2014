<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">
	        My To-do list
       	</h1>
    </div>
    <!-- /.col-lg-12 -->
</div>

<div class="row">
	<div class="col-lg-12">
    	<div class="table-responsive">
	        <table class="table table-striped table-bordered table-hover" id="actions-not-done">
	             <thead>
	                 <tr>
	                     <th>Description</th>
	                     <th>Assigned to</th>
	                     <th>Due to</th>
	                     <th>Status</th>
	                 </tr>
	             </thead>
	             <tbody>
	                 <c:forEach items="${actionBean.userActions}" var="act">
	                 <c:if test="${act.done eq '0'}">
		                 <tr onclick="location.href='selectAction?idAction=${act.id}'" style="cursor: pointer;">
		                 	<td>${act.description}</td>
		                 	<td>${act.assigned_user.username}</td>
		                 	<td>${act.dueTo}</td>
		                 	<td>Pending</td>
		                 </tr>
	                 </c:if>
					</c:forEach>
		         </tbody>
		     </table>
		 </div>
   	</div>
</div>

<script>
	$(document).ready(function() {
	    $('#actions-not-done').dataTable();
	});
</script>