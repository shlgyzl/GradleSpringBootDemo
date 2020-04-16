let stompClient = null;
let platform = 'pc';

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    $("#userinfo").html("");
}

function connect() {
    let jwt = $("#token").val();
    let socket = new SockJS('http://localhost:8080/websocket/tracker?platform=' + platform + '&access_token=' + jwt);
    stompClient = Stomp.over(socket);
    let headers = {
        Authorization: jwt
    };
    // 再发送消息
    stompClient.connect(headers, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        /*stompClient.subscribe('/topic/message', function (greeting) {
            showGreeting(greeting.body);
        });*/
        stompClient.subscribe('/user/topic/update/message', function (greeting) {
            showGreeting(greeting.body);
        });
    }, function (error) {
        console.log(error)
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {
    stompClient.send("/app/update/message", {}, JSON.stringify({"login": "落叶天涯"}));
    //stompClient.send("/app/message", {}, JSON.stringify({"login": "落叶天涯"}));
}

function showGreeting(message) {
    $("#userinfo").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#connect").click(function () {
        connect();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
    $("#send").click(function () {
        sendMessage();
    });
});
