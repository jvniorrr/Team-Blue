// Hanlder for events being emitted
var source = new EventSource('/agents');
var gridElement = $('#dotRows');

source.addEventListener('updateAgent', function(event) {

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
    // check if the user is present 
    let agent = $(`i[data-userid='${agentData.id}']`);
    if (agent.length) {
        present = true;
        updateAgent(agent, CSS_Options[agentData.status])
    }

    // create element per agent if not already present
    if (!present) {
        const nodeElement = document.createElement("i");
        const element = $(nodeElement);
        // TODO: More efficiently add classes in one line
        element.addClass('fa-solid');
        element.addClass('fa-circle');
        element.addClass('agents');
        element.addClass('px-1');
        element.css('color', CSS_Options[agentData.status])
        // attributes to be used when hovering
        element.attr("data-userID", agentData.id);
        element.attr("data-user-status", agentData.status);
        element.attr("title", `${agentData.id}\n${agentData.status}`);
        $('#dotRows').prepend(element);
    }
    

}); 

function updateAgent(element, newColor) {
    element.css('color', newColor);
}