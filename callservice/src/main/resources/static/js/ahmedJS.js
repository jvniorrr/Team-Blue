// Hanlder for events being emitted
var source = new EventSource('/agents');
var gridElement = $('#dotRows');
var totalAgents = 0;

source.addEventListener('spring', function(event) {

    // can accept a collection / list and iterate through each

    // parse through the event
    let agentData = JSON.parse(event.data);

    let CSS_Options = {
        "available" : "green", // available
        "busy" : "red", // on voice call
        "logged-out" : "black", // logged out
        "after" : "yellow", // after call work
        "preview" : "blue", // on preview task
    }

    // check in all the displayed i elements if the agent is already present
    let present = false;
    let agents = $(".agents");

    // check if the user is present 
    let agent = $(`div.circle[data-userid='${agentData.id}']`);
    if (agent.length) {
        present = true;
        updateUser(agent, CSS_Options[agentData.status]);
    }

    // create element per agent if not already present
    if (!present) {
        totalAgents++;

        // TODO: Possibly, use pure DOM document to modify or use jQuery entirely
        const nodeElemen = document.createElement("div");
        nodeElemen.classList.add("item");
        const innerNode = document.createElement("div");
        innerNode.classList.add("circle")
        $(innerNode).toggleClass(`${CSS_Options[agentData.status]}`);
        $(innerNode).attr("data-userID", agentData.id);
        $(innerNode).attr("data-user-status", agentData.status);
        nodeElemen.prepend(innerNode);

        // create p element that displays current number of agents
        const pElement = document.createElement('p');
        pElement.classList.add('label');
        var userNumber = document.createTextNode(`${totalAgents}`);
        pElement.appendChild(userNumber)
        nodeElemen.append(pElement)

        const element = $(nodeElemen);
        element.addClass('agents-item');
        $('#dotRows').prepend(element);
    }
    

}); 



function updateUser(element, newColor) {
    // check if the element has any present colors if so remove then update
    if (element.hasClass('green')) {
        element.toggleClass('green');
    }
    if (element.hasClass('red')) {
        element.toggleClass('red');
    }
    if (element.hasClass('yellow')) {
        element.toggleClass('yellow');
    }
    if (element.hasClass('black')) {
        element.toggleClass('black');
    }
    if (element.hasClass('blue')) {
        element.toggleClass('blue');
    }

    // update the new color
    element.toggleClass(newColor);
}