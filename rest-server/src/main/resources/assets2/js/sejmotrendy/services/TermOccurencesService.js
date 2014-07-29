/// <reference path="../app.js" />
/// <reference path="../controllers/ChartCtrl.js" />

'use strict';

module.service('termOccurencesService', function () {
    var svg = d3.select('#term-occurences-chart'),
        defs = svg.select('defs'),
        lineTrimRegion = defs.select('#lineTrimRegion'),
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
        setSizes.call(this);
        setScaleRanges.call(this);
        setData.call(this, chartData);
        setScaleDomain.call(this);
        setDataGenerationFunctions.call(this);
        draw.call(this);
    };

    this.updateSize = function () {
        setSizes.call(this);
        setScaleRanges.call(this);
        setDataGenerationFunctions.call(this);
        draw.call(this);
    };

    this.updateRange = function(xRange) {
        setScaleDomain.call(this, xRange);
        setDataGenerationFunctions.call(this);
        draw.call(this);
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
        lineTrimRegion.select('rect').attr({
            'width': chartWidth,
            'height': chartHeight,
        });
    };

    function setScaleRanges() {
        scaleX = scaleX || d3.time.scale();
        scaleY = scaleY || d3.scale.linear();
        scaleX.range([0, chartWidth]);
        scaleY.range([chartHeight, 0]);
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
            .orient('bottom')
            .ticks(4);
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
