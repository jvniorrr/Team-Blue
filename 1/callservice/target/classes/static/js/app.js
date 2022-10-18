// Hanlder for events being emitted
var source = new EventSource('/agents');
var gridElement = $('#dotRows');
const listContainer = document.getElementById('listcontainer');
window.onload=loadGrid;


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
        updateAgent(agent, CSS_Options[agentData.status], agentData.status)
    }

    // create element per agent if not already present
    if (!present) {
        let element = createElement(agentData);
        $('#dotRows').prepend(element);
    }
    

};

function loadGrid(event){
    event.preventDefault();
    for(let i= 0; i<10000; i++){
        const listDiv = document.createElement('div');
        listDiv.classList.add('listitem');
        listDiv.style.backgroundColor ='rgb(105,105,105)';
        listContainer.appendChild(listDiv);
    }
}



/**
 * Method to update a user's attributes and class values when agent already present.
 * @param {jQuery Obj} element jQuery node element.
 * @param {String} newColor Color to update the dot representing a user
 * @param {String} status Status to update the HTML attribute on the dot representing a user.
 */
function updateAgent(element, newColor, status) {
    element.css('color', newColor);
    element.attr('data-user-status', status);
}

function createElement(agentData) {
    const nodeElement = document.createElement("i");
    const element = $(nodeElement);
    element.addClass('fa-solid fa-circle agents px-1');
    element.css('color', CSS_Options[agentData.status])
    
    // attributes to be used when hovering
    element.attr("data-user-id", agentData.id);
    element.attr("data-user-status", agentData.status);

    // temp code below will display this when hovered.
    element.attr("title", `${agentData.id}\n${agentData.status}`);

    return element;
    
}
