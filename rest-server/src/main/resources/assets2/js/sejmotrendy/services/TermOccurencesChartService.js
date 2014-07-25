/// <reference path="../app.js" />
/// <reference path="../controllers/ChartCtrl.js" />

'use strict';

module.service('termOccurencesChartService', function () {
    var svg = d3.select('#term-occurences-chart'),
        chart = svg.select('.chart'),
        line = chart.select('.line'),
        axisX = chart.select('.axisX'),
        axisY = chart.select('.axisY'),
        margin = {
            top: 40,
            bottom: 40,
            left: 40,
            right: 40
        },
        svgWidth,
        svgHeight,
        chartWidth,
        chartHeight,
        scaleX,
        scaleY,
        data,
        lineFunction,
        axisXFunction,
        axisYFunction;

    this.initialize = function(chartData) {
        setSizes();
        setScaleRanges();
        setData(chartData);
        setScaleDomain();
        setDataGenerationFunctions();
        setDataGenerationFunctions();
        draw();
    };

    this.updateSize = function () {
        setSizes();
        setScaleRanges();
        setData();
        setScaleDomain();
        setDataGenerationFunctions();
        setDataGenerationFunctions();
        draw();
    };

    this.updateRange = function (xRange) {
        setScaleDomain(xRange);
        setDataGenerationFunctions();
        setDataGenerationFunctions();
        draw();
    };
    
    function setSizes() {
        svgWidth = svg.node().offsetWidth;
        svgHeight = svg.node().offsetHeight;
        chartWidth = svgWidth - margin.left - margin.right;
        chartHeight = svgHeight - margin.top - margin.bottom;

        chart.attr({
            'width': chartWidth,
            'height': chartHeight,
            'transform': 'translate(' + margin.left + ', ' + margin.top + ')'
        });
        axisX.attr({
            'transform': 'translate(' + 0 + ', ' + chartHeight + ')'
        });
    };

    function setScaleRanges() {
        scaleX = d3.time.scale()
            .range([0, chartWidth]);
        scaleY = d3.scale.linear()
            .range([chartHeight, 0]);
    };

    function setData(chartData) {
        data = chartData;
    };
    
    function setScaleDomain(xRange) {
        xRange = xRange || [data[0].date, data[data.length - 1].date];
        scaleX.domain(xRange);
        scaleY.domain([d3.min(data, function (d) { return d.termOccurrences; }), d3.max(data, function (d) { return d.termOccurrences; })]);
    };

    function setDataGenerationFunctions() {
        lineFunction = d3.svg.line()
            .x(function (d, i) { return scaleX(d.date); })
            .y(function (d, i) { return scaleY(d.termOccurrences); });
        axisXFunction = d3.svg.axis()
            .scale(scaleX)
            .orient('bottom').ticks(4);
        axisYFunction = d3.svg.axis()
            .scale(scaleY)
            .orient('left');
    };

    function draw() {
        axisX.call(axisXFunction);
        axisY.call(axisYFunction);
        line.attr('d', lineFunction(data));
    };
});
