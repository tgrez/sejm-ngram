/// <reference path="../app.js" />
/// <reference path="../controllers/ChartCtrl.js" />

'use strict';

module.service('rangeFilterChartService', function () {
    var _this = this,
        svg = d3.select('#range-filter-chart'),
        chart = svg.select('.chart'),
        line = chart.select('.line'),
        axisX = chart.select('.axisX'),
        axisY = chart.select('.axisY'),
        brush = chart.select('.brush'),
        margin = {
            top: 40,
            bottom: 40,
            left: 40,
            right: 40
        },
        dateFormat = d3.time.format('%Y-%m-%d'),
        svgWidth,
        svgHeight,
        chartWidth,
        chartHeight,
        scaleX,
        scaleY,
        data,
        lineFunction,
        axisXFunction,
        axisYFunction,
        brushedCallback;

    this.brushFunction = null;

    this.initialize = function(chartData, brushedCallbackFunction) {
        brushedCallback = brushedCallbackFunction;

        setSizes();
        setScaleRanges();
        setData(chartData);
        setScaleDomain();
        setDataGenerationFunctions();
        setDataGenerationFunctions();
        draw();
    };

    this.updateSize = function() {
        setSizes();
        setScaleRanges();
        setData();
        setScaleDomain();
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

    function setScaleDomain() {
        scaleX.domain([data[0].date, data[data.length - 1].date]);
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
        _this.brushFunction = d3.svg.brush()
            .x(scaleX)
            .on('brush', brushedCallback);
    };

    function draw() {
        line.attr('d', lineFunction(data));
        axisX.call(axisXFunction);
        axisY.call(axisYFunction);
        brush.call(_this.brushFunction).selectAll("rect")
      .attr("height", chartHeight);
    };
});
