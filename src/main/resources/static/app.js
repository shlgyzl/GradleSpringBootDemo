let stompClient = null;
let conferenceId = 1;
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
    let jwt=$("#token").val();
    // let jwt = 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOiJST0xFX0FETUlOLFJPTEVfVVNFUiIsImV4cCI6MTU4NjMzNTY5N30.gAAtzQzq9-ITSj5MOl70-gC8AWQEzsEgAsvBjDmiIZaJIQEHzSyjNvq8nbJ41SQScFjA2TmZhKOE4agQHMJWXg';
    let socket = new SockJS('http://localhost:8080/websocket/tracker?conferenceId=' + conferenceId + '&platform=' + platform + '&access_token=' + jwt);
    stompClient = Stomp.over(socket);
    let headers = {
        Authorization: jwt
    };
    stompClient.connect(headers, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/user/topic/conference/im/' + conferenceId, function (greeting) {
            showGreeting(greeting.body);
        });
        stompClient.subscribe('/user/topic/conference/onoffline/' + conferenceId, function (greeting) {
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
    stompClient.send("/topic/im", {}, "{\"conferenceId\":\"1\",\"type\":\"TEXT\",\"content\":\"hello\",\"participantId\":\"1\"}");
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
