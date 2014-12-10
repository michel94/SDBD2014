<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript">
    var websocket = null;
    var iditem = ${itemBean.item.id};
    var username = "${userBean.user.username}";

    window.onload = function() { // URI = ws://10.16.0.165:8080/WebSocket/ws
        connect('ws://' + window.location.host + '/Meeto/ws');
    }

    function connect(host) { // connect to the host websocket
        if ('WebSocket' in window)
            websocket = new WebSocket(host);
        else if ('MozWebSocket' in window)
            websocket = new MozWebSocket(host);
        else {
            writeToHistory('Get a real browser which supports WebSocket.');
            return;
        }

        websocket.onopen    = onOpen; // set the event listeners below
        websocket.onclose   = onClose;
        websocket.onmessage = onMessage;
        websocket.onerror   = onError;
        
    }

    function onOpen(event) {
        writeToHistory('Connected to ' + window.location.host + '.');
        websocket.send("/s " + iditem);
        document.getElementById('chat-text').onkeydown = function(key) {
            if (key.keyCode == 13)
                doSend(); // call doSend() on enter key
        };
    }
    
    function onClose(event) {
        writeToHistory('WebSocket closed.');
        document.getElementById('chat').onkeydown = null;
    }
    
    function onMessage(message) { // print the received message
    	console.log("message");
        writeToHistory(message.data);
    }
    
    function onError(event) {
        writeToHistory('WebSocket error (' + event.data + ').');
        document.getElementById('chat').onkeydown = null;
    }
    
    function doSend() {
        var message = document.getElementById('chat-text').value;
        if (message != '')
            websocket.send(message); // send the message
        document.getElementById('chat').value = '';
    }
z
    function writeToHistory(text) {
    	var history = $('#chat');
    	history.append(
    			'<li class="left clearfix"> \
                <span class="chat-img pull-left"> \
                    <img src="http://placehold.it/50/55C1E7/fff" alt="User Avatar" class="img-circle" /> \
                </span> \
                <div class="chat-body clearfix"> \
                    <div class="header"> \
                        <strong class="primary-font">'+ username +'</strong>  \
                        <small class="pull-right text-muted"> \
                        	<i class="fa fa-clock-o fa-fw"></i> ' + getCurrentDate() +'\
                   		 </small> \
                    </div> \
                    <p>' + text +
                    '</p> \
                </div> \
            </li> ')
    	$('#chat-text').val('');
    	$('#chat').animate({scrollTop: height});
    }
    $(document).ready(function(){
    	$("#btn-chat").click(function(){
    		doSend();
    	});
    	$("#chat").animate({ scrollTop: $('#chat')[0].scrollHeight}, 1000);
    });
    
    $(document).ready(function() {
	    $('#keydecisions-table').dataTable();
	});
 	
    function getCurrentDate(){
		var d1 = new Date();
		var s1 = d1.getFullYear().toString()+"-"+(d1.getMonth()+1).toString()+"-"+d1.getDate().toString()+" "+d1.getHours().toString()+":"+d1.getMinutes().toString()+":"+d1.getSeconds().toString();
		
		return s1;
	}
</script>

<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">
	        Item: ${itemBean.item.title}
      		<button onclick="location.href='deleteItem?IdItem=${itemBean.item.id}&idMeeting=${itemBean.item.meeting}'" type="button" class="btn btn-danger pull-right">Delete this item</button>
	        
        </h1>
    </div>
    <!-- /.col-lg-12 -->
</div>

<div class="row">
	<div class="col-lg-6">
		<div class="panel panel-default">
	        <div class="panel-heading">
	            Item details
	        </div>
	        <div class="panel-body">
                <form role="form" action="editItem?idItem=${itemBean.item.id}" method="post">
                    <div class="form-group">
                        <label>Title</label>
                        <input name="title" type="text" class="form-control" value="${itemBean.item.title}"/>
                    </div>
                    <div class="form-group">
                        <label>Description</label>
                        <input name="description" type="text" class="form-control" value="${itemBean.item.description}"/>
                    </div>
                    <input type="submit" class="btn btn-primary" value="Edit item" />
				</form>
			</div>
		</div>
	</div>
	
	<div class="col-lg-6">
		<div class="panel panel-default">
	        <div class="panel-heading">
	            Add new KeyDecision
	        </div>
	        <div class="panel-body">
                <form role="form" action="createKeyDecision?idItem=${itemBean.item.id}" method="post">
                    <div class="form-group">
                        <label>Description</label>
                        <input name="description" type="text" class="form-control"/>
                    </div>
                    <input type="submit" class="btn btn-primary" value="Add KeyDecision" />
				</form>
			</div>
		</div>
	</div>
</div>

<div class="row">
	<div class="col-lg-5">
      	<div class="chat-panel panel panel-default">
            <div class="panel-heading">
                <i class="fa fa-comments fa-fw"></i>
                Comments
            </div>
            <!-- /.panel-heading -->
            <div class="panel-body">
                <ul class="chat" id="chat">
                    <c:forEach items="${itemBean.item.comments}" var="com">
	                    <li class="left clearfix">
	                        <span class="chat-img pull-left">
	                            <img src="http://placehold.it/50/55C1E7/fff" alt="User Avatar" class="img-circle" />
	                        </span>
	                        <div class="chat-body clearfix">
	                            <div class="header">
	                                <strong class="primary-font">${com.username}</strong> 
	                                <small class="pull-right text-muted">
	                                    <i class="fa fa-clock-o fa-fw"></i> ${com.datetime}
	                                </small>
	                            </div>
	                            <p>
									${com.text}
	                            </p>
	                        </div>
	                    </li>
                	</c:forEach>
                </ul>
            </div>
            <!-- /.panel-body -->
            <div class="panel-footer">
                <div class="input-group">
                    <input id="chat-text" type="text" class="form-control input-sm" placeholder="Type your comment here..." />
                    <span class="input-group-btn">
                        <button class="btn btn-warning btn-sm" id="btn-chat">
                            Send
                        </button>
                    </span>
                </div>
            </div>
            <!-- /.panel-footer -->
        </div>
        <!-- /.panel .chat-panel -->
   	</div>
   	<div class="col-lg-7">
		<div class="panel panel-default">
	        <div class="panel-heading">
	            KeyDecisions
	        </div>
	        <div class="panel-body">
		        <div class="table-responsive">
			        <table class="table table-striped table-bordered table-hover" id="keydecisions-table">
			             <thead>
			                 <tr>
			                     <th>Description</th>
			                 </tr>
			             </thead>
			             <tbody>
			                 <c:forEach items="${itemBean.item.keydecisions}" var="kd">
			                 <tr>
			                 	<td>${kd.description}</td>
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
	
	
</script>