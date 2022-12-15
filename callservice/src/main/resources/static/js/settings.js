
let options = JSON.parse(localStorage.getItem("options"));

let defaultTeamColor = getComputedStyle(document.documentElement).getPropertyValue("--team-blue-primary")
let agentAvailableColor, agentBusyColor, agentPreviewColor, agentLoggedOutColor, agentAfterColor, tooltipColor;


$(document).ready(() => {
    if (localStorage.getItem("options") != null) {
        let opts = JSON.parse(localStorage.getItem("options"));
        agentAvailableColor = opts.available;
        agentBusyColor = opts.busy;
        agentPreviewColor = opts.preview;
        agentLoggedOutColor = opts.loggedout;
        agentAfterColor = opts.after;
        tooltipColor = opts.tooltip;
    } else { 
        agentAvailableColor = getComputedStyle(document.documentElement).getPropertyValue("--agent-available-bright-color").trim();
        agentBusyColor = getComputedStyle(document.documentElement).getPropertyValue("--agent-busy-bright-color").trim();
        agentPreviewColor = getComputedStyle(document.documentElement).getPropertyValue("--agent-preview-bright-color").trim();
        agentLoggedOutColor = getComputedStyle(document.documentElement).getPropertyValue("--agent-loggedout-color").trim();
        agentAfterColor = getComputedStyle(document.documentElement).getPropertyValue("--agent-after-color").trim();
        tooltipColor = getComputedStyle(document.documentElement).getPropertyValue("--team-blue-primary").trim();
    }

    var cssRuleCode = document.all ? 'rules' : 'cssRules'; //account for IE and FF
    var rules = document.styleSheets[3][cssRuleCode];
    for (let i=0; i<rules.length; i++) {
        let currRule = rules[i].selectorText;
        if (currRule == ".available") {
            rules[i].style.color = agentAvailableColor
        } else if (currRule == ".busy") {
            rules[i].style.color = agentBusyColor;
        } else if (currRule == ".logged-out, .loggedout") {
            rules[i].style.color = agentLoggedOutColor;
        } else if (currRule == ".preview") {
            rules[i].style.color = agentPreviewColor;
        } else if (currRule == ".after, .after-work, .afterwork") {
            rules[i].style.color = agentAfterColor;
        } else if (currRule == ".tooltipType .tooltiptext") {
            rules[i].style.color = tooltipColor;
        }
    }
    
    setupDots();
    setEntityRow();
});



// change the settings for color on when supervisor likes by using local storage
$("#available-color-option").on("input", () => {
    let availableColor = $("#available-color-option").val();
    let opts = JSON.parse(localStorage.getItem("options")) || { "available": agentAvailableColor, "busy": agentBusyColor, "preview": agentPreviewColor, "loggedout": agentLoggedOutColor, "after": agentAfterColor, "tooltip": tooltipColor };

    opts.available = agentAvailableColor = availableColor;
    localStorage.setItem("options", JSON.stringify(opts));
    updateChartStyle();
    setEntityRow();
});
$("#busy-color-option").on("input", () => {
    let busyColor = $("#busy-color-option").val();
    let opts = JSON.parse(localStorage.getItem("options")) || { "available": agentAvailableColor, "busy": agentBusyColor, "preview": agentPreviewColor, "loggedout": agentLoggedOutColor, "after": agentAfterColor, "tooltip": tooltipColor };

    opts.busy = agentBusyColor = busyColor;
    localStorage.setItem("options", JSON.stringify(opts));
    updateChartStyle();
    setEntityRow();
});
$("#preview-color-option").on("input", () => {
    let previewColor = $("#preview-color-option").val();
    let opts = JSON.parse(localStorage.getItem("options")) || { "available": agentAvailableColor, "busy": agentBusyColor, "preview": agentPreviewColor, "loggedout": agentLoggedOutColor, "after": agentAfterColor, "tooltip": tooltipColor };

    opts.preview = agentPreviewColor = previewColor;
    localStorage.setItem("options", JSON.stringify(opts));
    updateChartStyle();
    setEntityRow();
});
$("#after-color-option").on("input", () => {
    let afterColor = $("#after-color-option").val();
    let opts = JSON.parse(localStorage.getItem("options")) || { "available": agentAvailableColor, "busy": agentBusyColor, "preview": agentPreviewColor, "loggedout": agentLoggedOutColor, "after": agentAfterColor, "tooltip": tooltipColor };

    opts.after = agentAfterColor = afterColor;
    localStorage.setItem("options", JSON.stringify(opts));
    updateChartStyle();
    setEntityRow()
});
$("#loggedout-color-option").on("input", () => {
    let loggedoutColor = $("#loggedout-color-option").val();
    let opts = JSON.parse(localStorage.getItem("options")) || { "available": agentAvailableColor, "busy": agentBusyColor, "preview": agentPreviewColor, "loggedout": agentLoggedOutColor, "after": agentAfterColor, "tooltip": tooltipColor };

    opts.loggedout = agentLoggedOutColor = loggedoutColor;
    localStorage.setItem("options", JSON.stringify(opts));
    updateChartStyle();
    setEntityRow();
});
$("#tooltip-color-option").on("input", () => {
    let tooltipColor = $("#tooltip-color-option").val();
    let opts = JSON.parse(localStorage.getItem("options")) || { "available": agentAvailableColor, "busy": agentBusyColor, "preview": agentPreviewColor, "loggedout": agentLoggedOutColor, "after": agentAfterColor, "tooltip": defaultTeamColor };

    opts.tooltip = defaultTeamColor = tooltipColor;
    localStorage.setItem("options", JSON.stringify(opts));
    updateChartStyle();
    setEntityRow();
});


// home page settings

/**
 * Method to update the entity row information
 */
 function setEntityRow() {
    document.querySelectorAll(".entityInfo-item").forEach( (row) => {
        let jDot = $(row);
        if ((jDot).hasClass("available")) jDot.css("color", agentAvailableColor);
        else if ((jDot).hasClass("busy")) jDot.css("color", agentBusyColor);
        else if ((jDot).hasClass("preview")) jDot.css("color", agentPreviewColor);
        else if ((jDot).hasClass("loggedout") || (jDot).hasClass("logged-out")) jDot.css("color", agentLoggedOutColor);
        else if ((jDot).hasClass("after")) jDot.css("color", `${agentAfterColor} !important`);
    })
}

/**
 * Method to update home page dots referencing the agent dot colors
 */
function setupDots() {
    if (localStorage.getItem("options")) {
        let opts = JSON.parse(localStorage.getItem("options"));
        agentAvailableColor = opts.available;
        agentBusyColor = opts.busy;
        agentPreviewColor = opts.preview;
        agentLoggedOutColor = opts.loggedout;
        agentAfterColor = opts.after;
        tooltipColor = opts.tooltip;
    }

    document.querySelectorAll(".available").forEach( (dot) => {
        let jDot = $(dot);
        dot.style.setProperty("color", agentAvailableColor);
        jDot.css("color", agentAvailableColor);
    });
    document.querySelectorAll(".busy").forEach( (dot) => {
        let jDot = $(dot);
        dot.style.setProperty("color", agentBusyColor);
        jDot.css("color", agentBusyColor);
    });
    document.querySelectorAll(".preview").forEach( (dot) => {
        let jDot = $(dot);
        jDot.css("color", agentPreviewColor);
    });
    document.querySelectorAll(".loggedout, .logged-out").forEach( (dot) => {
        let jDot = $(dot);
        jDot.css("color", agentLoggedOutColor);
    });
    document.querySelectorAll(".after").forEach( (dot) => {
        let jDot = $(dot);
        jDot.css("color", agentAfterColor);
    });

    document.querySelectorAll(".tooltiptext").forEach((el) => {
        $(el).css("color", tooltipColor);
    })

}

/**
 * Method to update styling on the charts
 */
function updateChartStyle() {
    donutChart.updateOptions({
        colors: [ agentAvailableColor, agentBusyColor, agentPreviewColor, agentLoggedOutColor, agentAfterColor ],
    });
    circleChart.updateOptions({
        colors: [ agentAvailableColor, agentBusyColor, agentPreviewColor, agentLoggedOutColor, agentAfterColor ],
    });

    barChart.updateOptions({
        series: [
            // data
            {
                name: "Status",
                data: [
                    {
                        x: "Available",
                        y: availableStats,
                        fillColor: agentAvailableColor,
                    },
                    {
                        x: "Busy",
                        y: busyStats,
                        fillColor: agentBusyColor,
                    },
                    {
                        x: "Preview",
                        y: previewStats,
                        fillColor: agentPreviewColor,
                    },
                    {
                        x: "Logged Out",
                        y: loggedOutStats,
                        fillColor: agentLoggedOutColor,
                    },
                    {
                        x: "After",
                        y: afterStats,
                        fillColor: agentAfterColor,
                    },
                ],
            },
        ],
    })
}