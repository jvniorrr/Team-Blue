var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    // $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('http://localhost:8080/stomp-endpoint');
    // var socket = new SockJS('stomp-endpoint');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);

        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', function (greeting) {
            // pass to sep function to handle rendering live
            changeAgent(JSON.parse(greeting.body));
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
}

function changeAgent(message) {
    var options = {
        "available" : "text-success", // available
        "busy" : "text-danger", // on voice call
        "logged-out" : "text-dark", // logged out
        "after" : "text-warning", // after call work
        "preview" : "text-info", // on preview task
    }
    var CSS_Options = {
        "available" : "green", // available
        "busy" : "red", // on voice call
        "logged-out" : "black", // logged out
        "after" : "yellow", // after call work
        "preview" : "blue", // on preview task
    }


    $("#agent-1").css("color", CSS_Options[`${message.status}`]);
}

function showGreeting(message) {

    console.log("hello world")

    var agentRowName = `<td>${message.name}</td>`;
    var agentRowID = `<td>${message.id}</td>`;
    var agentRowStatus = `<td>${message.status}</td>`;
    var agentRowTime = `<td>${new Date().toLocaleTimeString()}</td>`;

    var agentRow = `<tr>${agentRowName}
    ${agentRowID}
    ${agentRowStatus}
    ${agentRowTime}
    `

    $("#greetings").append(agentRow);
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    connect();
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});