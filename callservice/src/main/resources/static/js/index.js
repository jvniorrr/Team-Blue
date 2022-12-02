// Hanlder for events being emitted
$(document).ready(() => {
    var source = new EventSource("/api/v1/init");
    var gridElement = $("#dots-grid");

    let entityStats = $(".entityStats");
    let availableStats = $(".availableStats");
    let busyStats = $(".busyStats");
    let previewStats = $(".previewStats");
    let loggedOutStats = $(".loggedOutStats");
    let afterStats = $(".afterStats");

    source.addEventListener("updateAgent", EventHandler);
    /**
     * Method to handle the event listener. Awaits events and parses data to be pushed onto frontend.
     * @param {Object} event Event from emitter that handles the incoming events
     */
    function EventHandler(event) {
        // parse through the event
        let agentData = JSON.parse(event.data);

        // check in all the displayed i elements if the agent is already present
        let present = false;

        // check if the user is present
        let agent = $(`i[data-user-id='${agentData.id}']`);
        if (agent.length) {
            present = true;
            updateAgent(agent, agentData);
        } else {
            entityStats.html(parseInt(entityStats.html()) + 1);
            let element = createElement(agentData);
            updateHomeStats(agentData.status);
            gridElement.prepend(element);
        }
    }

    /**
     * Method to update a user's attributes and class values when agent already present.
     * @param {jQuery Obj} element jQuery node element.
     * @param {JSON} agentData Object containing information pertaining to the user we are updating
     * @param {jQuery Obj} childElemtn jQuery node element.
     */
    function updateAgent(element, agentData) {

        // let childElement = $(element.children()[0]);
        // check if the element has any present colors if so remove then update
        if (element.hasClass("available")) {
            element.toggleClass("available", false);
            availableStats.html(parseInt(availableStats.html()) - 1);
        }else if (element.hasClass("busy")) {
            element.toggleClass("busy", false);
            busyStats.html(parseInt(availableStats.html()) - 1);
            
        } else if (element.hasClass("loggedout" || element.hasClass("logged-out"))) {
            element.toggleClass("loggedout", false);
            element.toggleClass("logged-out", false);
            loggedOutStats.html(parseInt(availableStats.html()) - 1);
        } else if (element.hasClass("preview")) {
            element.toggleClass("preview", false);
            previewStats.html(parseInt(availableStats.html()) - 1);

        } else if (element.hasClass("after")) {
            element.toggleClass("after", false);
            afterStats.html(parseInt(availableStats.html()) - 1);
        }

        // update the new color
        element.toggleClass(agentData.status); // TODO: add error handling incase they pass an invalid name
        updateHomeStats(agentData.status);

        $(element.children()[0]).html(`${agentData.name}<br>${agentData.status}`);
    }

    function createElement(agentData) {
        const nodeElement = document.createElement("i");
        const element = $(nodeElement);

        const spanElement = document.createElement("span");
        spanElement.classList.add("tooltiptext");
        spanElement.innerHTML = `${agentData.name} <br>${agentData.status}`;

        element.addClass(`fa-solid fa-circle agent-dot tooltipType ${agentData.status}`);

        // attributes to be used when hovering
        element.attr("data-user-id", agentData.id);

        element.append(spanElement);

        return element;
    }

    function updateHomeStats(status) {
        // update stats
        if (status == "available") {
            availableStats.html(parseInt(availableStats.html()) + 1);
        } else if (status == "busy") {
            busyStats.html(parseInt(busyStats.html()) + 1);
        } else if (status == "preview") {
            previewStats.html(parseInt(previewStats.html()) + 1);
        } else if (
            status == "logged-out" ||
            status == "loggedout"
        ) {
            loggedOutStats.html(parseInt(loggedOutStats.html()) + 1);
        } else if (status == "after") {
            afterStats.html(parseInt(afterStats.html()) + 1);
        }
    }
});
