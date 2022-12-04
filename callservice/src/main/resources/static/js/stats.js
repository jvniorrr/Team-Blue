const base = document.querySelector('#bar');

var options = 
{
    chart: {
        type: 'bar',
        width: '100%',
        height: '100%',
        toolbar: {show: false},
        selection: {enabled: false},
      },
    series: [{
        name: 'Agents',
        data: [
          { x: 'Available',
            y: 125,
            fillColor: '#008276'},
          { x: 'Busy',
            y: 30,
            fillColor: '#d23645'},
          { x: 'Logged-off',
            y: 35,
            fillColor: '#5e5e5e'},
          { x: 'Preview',
            y: 91,
            fillColor: '#1a75c0'},
          { x: 'After-work',
            y: 45,
            fillColor: '#ffd73f'}]
      },
      {
        name: 'Average',
        data: [
          { x: 'Available',
            y: 113,
            fillColor: '#a0a0a0'},
          { x: 'Busy',
            y: 42,
            fillColor: '#a0a0a0'},
          { x: 'Logged-off',
            y: 30,
            fillColor: '#a0a0a0'},
          { x: 'Preview',
            y: 99,
            fillColor: '#a0a0a0'},
          { x: 'After-work',
            y: 23,
            fillColor: '#a0a0a0'}]
      }],
      states: {
        hover: {
          filter: {type: 'none',},
        },
      },
    xaxis: {
        type: 'category',
        labels: {
          floating: true,
          show: true,
        },
        axisBorder: {show: false},
        axisTicks: {show: false},
      },
    yaxis: {show: false},
    legend: {show: false},
    dataLabels : {enabled: false},
    plotOptions: {
        bar: {
            borderRadius: 4.5,
            columnWidth: '20%',
        },
        
    },
};

var chart = new ApexCharts(base, options);
chart.render();