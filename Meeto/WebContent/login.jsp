<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Meeto - Your Meetings Under Control</title>

    <!-- Bootstrap Core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- MetisMenu CSS -->
    <link href="css/plugins/metisMenu/metisMenu.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="css/sb-admin-2.css" rel="stylesheet">

    <!-- Morris Charts CSS -->
    <link href="css/plugins/morris.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="font-awesome-4.1.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>

    <!-- jQuery -->
    <script src="js/jquery.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="js/bootstrap.min.js"></script>

    <!-- Metis Menu Plugin JavaScript -->
    <script src="js/plugins/metisMenu/metisMenu.min.js"></script>

    <!-- Custom Theme JavaScript -->
    <script src="js/sb-admin-2.js"></script>

	<div class="container">
	    <div class="row">
	        <div class="col-md-4 col-md-offset-4">
	            <div class="login-panel panel panel-default">
	                <div class="panel-heading">
	                    <h3 class="panel-title">Please Sign In <span class="pull-right">Meeto v2</span></h3>
	                </div>
	                <div class="panel-body">
	                    <form role="form" id="login-form" method="post" action="login">
	                        <fieldset>
	                            <div class="form-group">
	                                <input class="form-control" placeholder="Username" name="username" type="text" autofocus>
	                            </div>
	                            <div class="form-group">
	                                <input class="form-control" placeholder="Password" name="password" type="password" value="">
	                            </div>
	                            <!-- Change this to a button or input when using this as a form -->
	                            <input type="submit" class="btn btn-lg btn-primary btn-block" value="Login with a Meeto Account"/>
	                            <a href="login" class="btn btn-lg btn-success btn-block">Login with a Google Account</a>
	                            <input type="submit" class="btn btn-lg btn-primary btn-block" value="Register new Meeto Account" onclick="changeAction()" />
	                        </fieldset>
	                    </form>
	                </div>
	            </div>
	        </div>
	    </div>
	</div>
</body>
</html>

<script>
function changeAction() {
    document.getElementById("login-form").action = "register";
}
</script>