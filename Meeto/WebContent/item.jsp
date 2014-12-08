<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript">
    var websocket = null;
    var iditem = ${itemBean.item.id};
    var username = "${userBean.user.username}";

    window.onload = function() {
        connect('ws://' + window.location.host + '/Meeto/ws');
        document.getElementById("chat").focus();
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
        document.getElementById('chat').onkeydown = function(key) {
            if (key.keyCode == 13)
                doSend(); // call doSend() on enter key
        };
    }
    
    function onClose(event) {
        writeToHistory('WebSocket closed.');
        document.getElementById('chat').onkeydown = null;
    }
    
    function onMessage(message) { // print the received message
        writeToHistory(message.data);
    }
    
    function onError(event) {
        writeToHistory('WebSocket error (' + event.data + ').');
        document.getElementById('chat').onkeydown = null;
    }
    
    function doSend() {
        var message = document.getElementById('chat').value;
        if (message != '')
            websocket.send(message); // send the message
        document.getElementById('chat').value = '';
    }

    function writeToHistory(text) {
    	var history = document.getElementById('history');
        var line = document.createElement('p');
        line.style.wordWrap = 'break-word';
        line.innerHTML = text;
        history.appendChild(line);
        history.scrollTop = history.scrollHeight;
    }

</script>

<div id="item">
	<br>
	<h2> Item </h2>
	
	</div><form action="editItem?idItem=${itemBean.item.id}" method="post">
		<input name="title" type="text" value="${itemBean.item.title}" placeholder="Title"/>
		<input name="description" type="text" value="${itemBean.item.description}" placeholder="Description"/>
		<br>
		<input type="submit" value="Edit Item">
	</form>
	
	<h3> Comments </h3>
	
	<div>
	    <div id="container"><div id="history"></div></div>
	    <p><input type="text" placeholder="type to chat" id="chat"></p>
	</div>
	
	
</div>