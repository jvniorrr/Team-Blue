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

    let source = new EventSource("/api/v1/init");

    // listen for API calls on updates
    source.addEventListener("updateAgent", updateHandler);

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
            (availableStats / entityStats) * 100, (busyStats / entityStats) * 100 , (previewStats / entityStats) * 100, (loggedOutStats / entityStats) * 100, (afterStats / entityStats) * 100,
        ]);
    }
});

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
        height: 100,
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

// Bar Chart
let barOptions = {
    ...defaultOptions,
    chart: {
        height: 380,
        // ...defaultOptions.chart,
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
    },
    plotOptions: {
        radialBar: {
            dataLabels: {
                showOn: "hover",
                value: {
                    color: "#fff",
                    formatter: (val) => {
                        return val.substring(0, 2) + "%";
                    }, 
                },

            },
        },
    },
    series: [
        (availableStats / entityStats) * 100, (busyStats / entityStats) * 100 , (previewStats / entityStats) * 100, (loggedOutStats / entityStats) * 100, (afterStats / entityStats) * 100,
    ],
    labels: ["After", "Logged Out", "Preview", "Busy", "Available"],
};



var circleChart = new ApexCharts(document.querySelector("#circle-chart"), circleOptions);
var barChart = new ApexCharts(document.querySelector("#bar-chart"), barOptions);
