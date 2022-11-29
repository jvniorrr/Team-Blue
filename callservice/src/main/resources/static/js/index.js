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

    const CSS_Options = {
        available: "green", // available
        busy: "red", // on voice call
        "logged-out": "black", // logged out
        after: "yellow", // after call work
        preview: "blue", // on preview task
    };

    source.addEventListener("updateAgent", EventHandler);
    /**
     * Method to handle the event listener. Awaits events and parses data to be pushed onto frontend.
     * @param {Object} event Event from emitter that handles the incoming events
     */
    function EventHandler(event) {
        // can accept a collection / list and iterate through each

        // parse through the event
        let agentData = JSON.parse(event.data);

        entityStats.html(parseInt(entityStats.html()) + 1);
        if (agentData.status == "available") {
            availableStats.html(parseInt(availableStats.html()) + 1);
        } else if (agentData.status == "busy") {
            busyStats.html(parseInt(busyStats.html()) + 1);
        } else if (agentData.status == "preview") {
            previewStats.html(parseInt(previewStats.html()) + 1);
        } else if (
            agentData.status == "logged-out" ||
            agentData.status == "loggedout"
        ) {
            loggedOutStats.html(parseInt(loggedOutStats.html()) + 1);
        } else if (agentData.status == "after") {
            afterStats.html(parseInt(afterStats.html()) + 1);
        }

        // check in all the displayed i elements if the agent is already present
        let present = false;

        // check if the user is present
        let agent = $(`i[data-user-id='${agentData.id}']`);
        if (agent.length) {
            let child = $(agent.children()[0]);
            present = true;
            updateAgent(agent, agentData, child);
        }

        // create element per agent if not already present
        if (!present) {
            let element = createElement(agentData);
            gridElement.prepend(element);
        }
    }

    /**
     * Method to update a user's attributes and class values when agent already present.
     * @param {jQuery Obj} element jQuery node element.
     * @param {JSON} agentData Object containing information pertaining to the user we are updating
     * @param {jQuery Obj} childElemtn jQuery node element.
     */
    function updateAgent(element, agentData, childElement) {
        // element.css('color', newColor);
        // check if the element has any present colors if so remove then update
        if (element.hasClass("available")) {
            element.toggleClass("available");
        }
        if (element.hasClass("busy")) {
            element.toggleClass("busy");
        }
        if (element.hasClass("loggedout")) {
            element.toggleClass("loggedout");
        }
        if (element.hasClass("preview")) {
            element.toggleClass("preview");
        }
        if (element.hasClass("after")) {
            element.toggleClass("after");
        }

        // update the new color
        element.toggleClass(agentData.status); // TODO: add error handling incase they pass an invalid name
        element.attr("data-user-status", agentData.status);

        childElement.html(`${agentData.name}<br>${agentData.status}`);
    }

    function createElement(agentData) {
        const nodeElement = document.createElement("i");
        const element = $(nodeElement);
        // <span th:attr="class='tooltiptext'" th:utext="${agent.name +  ' <br>' +  agent.status}"></span>
        const spanElement = document.createElement("span");
        spanElement.classList.add("tooltiptext");
        spanElement.innerHTML = `${agentData.name} <br> ${agentData.status}`;

        element.addClass("fa-solid fa-circle agent-dot tooltipType");
        element.addClass(agentData.status);

        // attributes to be used when hovering
        element.attr("data-user-id", agentData.idString);
        element.attr("data-user-status", agentData.status);

        element.append(spanElement);
        // TODO: Add name to title for hover effect
        // temp code below will display this when hovered.
        // element.attr("title", `${agentData.id}\n${agentData.status}`);

        return element;
    }
});
