<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">
	        My Groups
       	</h1>
    </div>
    <!-- /.col-lg-12 -->
</div>

<div class="row">
	<div class="col-lg-12">
    	<div class="table-responsive">
	        <table class="table table-striped table-bordered table-hover" id="groups-table">
	             <thead>
	                 <tr>
	                     <th>Nome</th>
	                 </tr>
	             </thead>
	             <tbody>
	                 <c:forEach items="${groupBean.groupsFromUser}" var="value">
		                 <tr onclick="location.href='selectGroup?idGroup=${value.idgroup}'" style="cursor: pointer;">
		                 	<td>${value.name}</td>
		                 </tr>
					</c:forEach>
		         </tbody>
		     </table>
		 </div>
   	</div>
</div>

<script>
	$(document).ready(function() {
	    $('#groups-table').dataTable();
	});
</script>	