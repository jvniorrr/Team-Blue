// Hanlder for events being emitted
var source = new EventSource('/agents');
var gridElement = $('#dots-grid');

const CSS_Options = {
    "available" : "green", // available
    "busy" : "red", // on voice call
    "logged-out" : "black", // logged out
    "after" : "yellow", // after call work
    "preview" : "blue", // on preview task
}

source.addEventListener('updateAgent', EventHandler); 
/**
 * Method to handle the event listener. Awaits events and parses data to be pushed onto frontend. 
 * @param {Object} event Event from emitter that handles the incoming events
 */
function EventHandler(event) {

    // can accept a collection / list and iterate through each

    // parse through the event
    let agentData = JSON.parse(event.data);

    // check in all the displayed i elements if the agent is already present
    let present = false;

    // check if the user is present 
    let agent = $(`i[data-user-id='${agentData.id}']`);
    if (agent.length) {
        present = true;
        updateAgent(agent, agentData);
    }

    // create element per agent if not already present
    if (!present) {
        let element = createElement(agentData);
        gridElement.prepend(element);
    }
    

};


/**
 * Method to update a user's attributes and class values when agent already present.
 * @param {jQuery Obj} element jQuery node element.
 * @param {JSON} agentData Object containing information pertaining to the user we are updating
 */
function updateAgent(element, agentData) {
    // element.css('color', newColor);
    // check if the element has any present colors if so remove then update
    if (element.hasClass('available')) {
        element.toggleClass('available');
    }
    if (element.hasClass('busy')) {
        element.toggleClass('busy');
    }
    if (element.hasClass('loggedout')) {
        element.toggleClass('loggedout');
    }
    if (element.hasClass('preview')) {
        element.toggleClass('preview');
    }
    if (element.hasClass('after')) {
        element.toggleClass('after');
    }

    // update the new color
    element.toggleClass(agentData.status); // TODO: add error handling incase they pass an invalid name
    element.attr('data-user-status', agentData.status);
}

function createElement(agentData) {
    const nodeElement = document.createElement("i");
    const element = $(nodeElement);
    element.addClass('fa-solid fa-circle fa-xl agent-dot');
    element.addClass(agentData.status)
    
    // attributes to be used when hovering
    element.attr("data-user-id", agentData.id);
    element.attr("data-user-status", agentData.status);

    // TODO: Add name to title for hover effect
    // temp code below will display this when hovered.
    // element.attr("title", `${agentData.id}\n${agentData.status}`);

    return element;
    
}
