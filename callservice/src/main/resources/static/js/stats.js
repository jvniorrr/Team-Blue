const primaryColor = getComputedStyle(document.documentElement)
    .getPropertyValue("--team-blue-primary")
    .trim();
const defaultColor = getComputedStyle(document.documentElement)
    .getPropertyValue("--color-default")
    .trim();
const labelColor = getComputedStyle(document.documentElement)
    .getPropertyValue("--color-label")
    .trim();

const agentAvailableColor = getComputedStyle(document.documentElement)
    .getPropertyValue("--agent-available-bright-color")
    .trim();
const agentBusyColor = getComputedStyle(document.documentElement)
    .getPropertyValue("--agent-busy-bright-color")
    .trim();
const agentPreviewColor = getComputedStyle(document.documentElement)
    .getPropertyValue("--agent-preview-bright-color")
    .trim();
const agentLoggedOutColor = getComputedStyle(document.documentElement)
    .getPropertyValue("--agent-loggedout-color")
    .trim();
const agentAfterColor = getComputedStyle(document.documentElement)
    .getPropertyValue("--agent-after-color")
    .trim();

let entityStats = parseInt($(".entityStats").html());
let availableStats = parseInt($(".availableStats").html());
let busyStats = parseInt($(".busyStats").html());
let previewStats = parseInt($(".previewStats").html());
let loggedOutStats = parseInt($(".loggedOutStats").html());
let afterStats = parseInt($(".afterStats").html());

// Emitter handler for updates
$(document).ready(() => {
    barChart.render();
    circleChart.render();
    donutChart.render();

    let source = new EventSource("/api/v1/init");

    // listen for API calls on updates
    source.addEventListener("updateAgent", updateHandler);
    source.addEventListener("deleteAgent", deleteHandler);

    
});

function updateHandler(event) {
    let agentData = JSON.parse(event.data);

    if (agentData.status == "available") {
        $(".availableStats").html(++availableStats);
    } else if (agentData.status == "busy") {
        $(".busyStats").html(++busyStats);
    } else if (agentData.status == "preview") {
        $(".previewStats").html(++previewStats);
    } else if (
        agentData.status == "loggedout" ||
        agentData.status == "logged-out"
    ) {
        $(".loggedOutStats").html(++loggedOutStats);
    } else if (agentData.status == "after") {
        $(".afterStats").html(++afterStats);
    }

    $(".entityStats").html(++entityStats);
    updateCharts();

    
}

function deleteHandler(event) {
    let data = JSON.parse(event.data);

    if (data.status == "available") {
        $(".availableStats").html(--availableStats);
    } else if (data.status == "busy") {
        $(".busyStats").html(--busyStats);
    } else if (data.status == "preview") {
        $(".previewStats").html(--previewStats);
    } else if (
        data.status == "loggedout" ||
        data.status == "logged-out"
    ) {
        $(".loggedOutStats").html(--loggedOutStats);
    } else if (data.status == "after") {
        $(".afterStats").html(--afterStats);
    }

    $(".entityStats").html(--entityStats);
    updateCharts();
}

let updateCharts = () => {
    barChart.updateSeries([
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
    ]);

    circleChart.updateSeries([
        (availableStats / entityStats) * 360, (busyStats / entityStats) * 360 , (previewStats / entityStats) * 360, (loggedOutStats / entityStats) * 360, (afterStats / entityStats) * 360,
    ]);
    
    donutChart.updateSeries([
        (availableStats / entityStats), (busyStats / entityStats), (previewStats / entityStats), (loggedOutStats / entityStats), (afterStats / entityStats),
    ]);
}

// Default Chart Options
const defaultOptions = {
    chart: {
        toolbar: {
            show: false,
        },
        selection: {
            enabled: false,
        },
        zoom: {
            enabled: false,
        },
        width: "100%",
        height: 380,
        offsetY: 8,
    },
    stroke: {
        linecap: "round",
    },
    dataLabels: {
        enabled: false,
    },
    legend: {
        show: false,
    },
    states: {
        hover: {
            filter: {
                type: "none",
            },
        },
    },
};

// Bar Chart -- Credit @https://github.com/frontend-joe/es6-charts/tree/main/bar-chart
let barOptions = {
    ...defaultOptions,
    chart: {
        ...defaultOptions.chart,
        type: "bar",
    },
    tooltip: {
        enabled: true,
        fillSeriesColor: true,
        y: {
            formatter: (value) => {
                return `${value}`;
            },
        },
    },
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
    colors: [primaryColor, defaultColor],
    stroke: { show: false },
    grid: {
        borderColor: "rgba(0, 0, 0, 0)",
        padding: { left: 0, right: 0, top: -16, bottom: -8 },
    },
    plotOptions: {
        bar: {
            horizontal: false,
            columnWidth: "15%",
            barHeight: "100%",
            borderRadius: 6,
        },
    },
    yaxis: {
        show: false,
    },
    xaxis: {
        labels: {
            floating: true,
            show: true,
            style: {
                colors: labelColor,
            },
        },
        axisBorder: { show: false },
        axisTicks: { show: false },
        crosshairs: { show: false },
        categories: ["Available", "Busy", "Preview", "Logged Out", "After"],
    },
};


let circleOptions = {
    chart: {
        height: 380,
        type: "radialBar",
        animations: {
            speed: 900,
            animateGradually: {
                enabled: true,
                delay: 150,
            },
            dynamicAnimation: {
                enabled: true,
                speed: 350,
            }
        }
    },
    plotOptions: {
        radialBar: {
            dataLabels: {
                showOn: "always",
                value: {
                    color: "#fff",
                    formatter: (val) => {
                        let val2 = (val/365) * 100;
                        return val2.toString().substring(0, 2) + "%";
                    }, 
                },
            },
            
        },
    },
    // stroke: {
    //     lineCap: "round"
    // },
    series: [
        (availableStats / entityStats) * 360, (busyStats / entityStats) * 360 , (previewStats / entityStats) * 360, (loggedOutStats / entityStats) * 360, (afterStats / entityStats) * 360,
        // (availableStats / entityStats) * 100, (busyStats / entityStats) * 100 , (previewStats / entityStats) * 100,
    ],
    colors: [ agentAvailableColor, agentBusyColor, agentPreviewColor, agentLoggedOutColor, agentAfterColor ],
    labels: ["Available", "Busy", "Preview", "Logged Out", "After"],
};

let donutOptions = {
    series: [
        (availableStats / entityStats), (busyStats / entityStats), (previewStats / entityStats), (loggedOutStats / entityStats), (afterStats / entityStats),
    ],
    colors: [ agentAvailableColor, agentBusyColor, agentPreviewColor, agentLoggedOutColor, agentAfterColor ],
    labels: ["Available", "Busy", "Preview", "Logged Out", "After"],
    dataLabels: {
        enabled: true,
        formatter: (val, opts) => {
            return val.toString().substring(0,2) + "%";
        },
    },
    legend: {
        labels: {
            colors: [ "#fff" ],
            // userSeriesColors: true,
        },
    },
    stroke: {
        show: true,
        curve: "smooth",
        width: 1,
        colors: ["#000"],
    },
    chart: {
        height: 380,
        type: "donut",
        animations: {
            speed: 800,
            animateGradually: {
                enabled: true,
                delay: 150,
            },
            dynamicAnimation: {
                enabled: true,
                speed: 350,
            }
        }
    },
    tooltip: {
        fillSeriesColor: false,
        y: {
            formatter: (val) => {
                return (val * 100).toString().substring(0,5) + "%";
            }
        }
    },
    plotOptions: {
        pie: {
            donut: {
                show: true,
                labels: {
                    value: {
                        show: true,
                        formatter: (val) => {
                            return val.toString().substring(0,4) + "%";
                        }
                    },
                },
            },
            expandOnClick: true,
            
        }
    }
}

/**
 * Function to update the color choices agent wants
 */
function updateSettings() {
    let newSettings = {
        available: "#",
    }
}

var circleChart = new ApexCharts(document.querySelector("#circle-chart"), circleOptions);
var barChart = new ApexCharts(document.querySelector("#bar-chart"), barOptions);
var donutChart = new ApexCharts(document.querySelector("#donut-chart"), donutOptions);
