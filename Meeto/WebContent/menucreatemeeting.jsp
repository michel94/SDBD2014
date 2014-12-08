<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">Create meeting</h1>
    </div>
    <!-- /.col-lg-12 -->
</div>

<div class="row">
	<div class="col-lg-6">
		<div class="panel panel-default">
	        <div class="panel-heading">
	            Meeting details
	        </div>
	        <div class="panel-body">
                <form role="form" action="createMeeting" method="post">
                    <div class="form-group">
                        <label>Title</label>
                        <input name="title" type="text" class="form-control">
                    </div>
                    <div class="form-group">
                        <label>Description</label>
                        <input name="description" type="text" class="form-control">
                    </div>
                    <div class="form-group">
                        <label>Date and time</label>
                        <input name="datetime" type="text" class="form-control">
                    </div>
                    <div class="form-group">
                        <label>Location</label>
                        <input name="location" type="text" class="form-control">
                    </div>
                    <input type="submit" class="btn btn-primary" value="Create" />
				</form>
			</div>
		</div>
	</div>					
</div>	