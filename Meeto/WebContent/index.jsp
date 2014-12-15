<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Meeto - Your Meetings Under Control</title>

    <!-- Bootstrap Core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- MetisMenu CSS -->
    <link href="css/plugins/metisMenu/metisMenu.min.css" rel="stylesheet">

    <!-- Timeline CSS -->
    <link href="css/plugins/timeline.css" rel="stylesheet">
    
    <!-- DataTables CSS -->
    <link href="css/plugins/dataTables.bootstrap.css" rel="stylesheet">

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
	
	<script type="text/javascript">
    var websocket2 = null;
	var iduser = "${iduser}"
    window.onload = function() { // URI = ws://10.16.0.165:8080/WebSocket/ws
        connect('ws://' + window.location.host + '/Meeto/wsnot');
    }

    function connect(host) { // connect to the host websocket
        if ('WebSocket' in window)
            websocket2 = new WebSocket(host);
        else if ('MozWebSocket' in window)
            websocket2 = new MozWebSocket(host);
        else {
            writeToHistory('Get a real browser which supports WebSocket.');
            return;
        }

        websocket2.onopen    = onOpen; // set the event listeners below
        websocket2.onclose   = onClose;
        websocket2.onmessage = onMessage;
        websocket2.onerror   = onError;
        
    }

    function onOpen(event) {
        websocket2.send("/s " + iduser);
    }
    
    function onClose(event) {
        writeToHistory('WebSocket closed.');
    }
    
    function onMessage(message) { // print the received message
    	console.log("message");
        writeToHistory(message.data);
    }
    
    function onError(event) {
    }
    
    function writeToHistory(text) {
    	var history = $('#notification');
    	history.append(  '<li class="divider"></li> \
    			<li><div> \
				<strong>Invite</strong> \
				<span class="pull-right text-muted"> \
				<em>'+getCurrentDate()+'</em> \
				</span> \
			</div> \
			<div>'+text+'</div> \
			</li> '
);
    }
    
    $(document).ready(function(){
    });
 
 	
    function getCurrentDate(){
		var d1 = new Date();
		var s1 = d1.getFullYear().toString()+"-"+(d1.getMonth()+1).toString()+"-"+d1.getDate().toString()+" "+d1.getHours().toString()+":"+d1.getMinutes().toString()+":"+d1.getSeconds().toString();
		
		return s1;
	}
	</script>
</head>

<body>


    <!-- jQuery -->
    <script src="js/jquery.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="js/bootstrap.min.js"></script>

    <!-- Metis Menu Plugin JavaScript -->
    <script src="js/plugins/metisMenu/metisMenu.min.js"></script>
    
    <!-- DataTables JavaScript -->
    <script src="js/plugins/dataTables/jquery.dataTables.js"></script>
    <script src="js/plugins/dataTables/dataTables.bootstrap.js"></script>

    <!-- Custom Theme JavaScript -->
    <script src="js/sb-admin-2.js"></script>


    <div id="wrapper">

        <!-- Navigation -->
        <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="index.html">Meeto V2.0</a>
            </div>
            <!-- /.navbar-header -->

            <ul class="nav navbar-top-links navbar-right">
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                        <i class="fa fa-bell fa-fw"></i>  <i class="fa fa-caret-down"></i>
                    </a>
                    <ul class="dropdown-menu dropdown-messages" id="notification">
                        
                    </ul>
                    <!-- /.dropdown-alerts -->
                </li>
                <!-- /.dropdown -->
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                        ${username} <i class="fa fa-caret-down"></i>
                    </a>
                    <ul class="dropdown-menu dropdown-user">
                        <li><a href="index"><i class="fa fa-sign-out fa-fw"></i> Logout</a></li>
                    </ul>
                    <!-- /.dropdown-user -->
                </li>
                <!-- /.dropdown -->
            </ul>
            <!-- /.navbar-top-links -->

            <div class="navbar-default sidebar" role="navigation">
                <div class="sidebar-nav navbar-collapse">
                    <ul class="nav" id="side-menu">
                        <li class="active">
                            <a href="#"><i class="fa fa-briefcase fa-fw"></i> Meetings<span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                <li>
                                    <a href="menumeetingsnext"><i class="fa fa-forward fa-fw"></i> Upcoming meetings</a>
                                </li>
                                <li>
                                    <a href="menumeetingsprevious"><i class="fa fa-backward fa-fw"></i> Previous meetings</a>
                                </li>
                                <li>
                                    <a href="menucreatemeeting"><i class="fa fa-plus fa-fw"></i> Create meeting</a>
                                </li>
                            </ul>
                        </li>
                        <li class="active">
                            <a href="#"><i class="fa fa-tasks fa-fw"></i> ToDo's<span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                <li>
                                    <a href="actionsnotdone"><i class="fa fa-square fa-fw"></i> Not done yet</a>
                                </li>
                                <li>
                                    <a href="actionsdone"><i class="fa fa-check-square fa-fw"></i> Done</a>
                                </li>
                            </ul>
                        </li>
                        <li class="active">
                            <a href="#"><i class="fa fa-group fa-fw"></i> Groups<span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                <li>
                                    <a href="groups"><i class="fa fa-bullseye fa-fw"></i> My Groups</a>
                                </li>
                                <li>
                                    <a href="menucreategroup"><i class="fa fa-plus fa-fw"></i> Create Group</a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </div>
                <!-- /.sidebar-collapse -->
            </div>
            <!-- /.navbar-static-side -->
        </nav>

        <div id="page-wrapper">
            <jsp:include page="${view}.jsp"></jsp:include>
        </div>

    
    </div>
    

</body>

</html>