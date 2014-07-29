/// <reference path="../app.js" />
/// <reference path="../controllers/ChartCtrl.js" />

'use strict';

module.service('rangeFilterService', function () {
    var _this = this,
        svg = d3.select('#range-filter-chart'),
        chart = svg.select('.chart'),
        line = chart.select('.line'),
        axisX = chart.select('.axisX'),
        axisY = chart.select('.axisY'),
        brush = chart.select('.brush'),
        margin = {
            top: 20,
            bottom: 30,
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
        cachedBrushExtent;

    this.brushFunction = null;

    this.initialize = function(chartData) {
        setSizes.call(this);
        setScaleRanges.call(this);
        setData.call(this, chartData);
        setScaleDomain.call(this);
        setDataGenerationFunctions.call(this);
        draw.call(this);
    };

    this.updateSize = function() {
        setSizes.call(this);
        setScaleRanges.call(this);
        setScaleDomain.call(this);
        setDataGenerationFunctions.call(this);
        draw.call(this);
    };

    this.onChange = function (brushedCallback) {
        var _this = this;

        this.brushFunction.on('brush', function () {
            cachedBrushExtent = _this.brushFunction.extent();
            brushedCallback();
        });
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
        scaleX = scaleX || d3.time.scale();
        scaleY = scaleY || d3.scale.linear();
        scaleX.range([0, chartWidth]);
        scaleY.range([chartHeight, 0]);
    };

    function setData(chartData) {
        data = chartData;
    };

    function setScaleDomain() {
        scaleX.domain([data[0].date, data[data.length - 1].date]);
        scaleY.domain([d3.min(data, function (d) { return d.termOccurrences; }), d3.max(data, function (d) { return d.termOccurrences; })]);
    };

    function setDataGenerationFunctions() {
        this.brushFunction = this.brushFunction || d3.svg.brush();

        lineFunction = d3.svg.line()
            .x(function (d, i) { return scaleX(d.date); })
            .y(function (d, i) { return scaleY(d.termOccurrences); });
        axisXFunction = d3.svg.axis()
            .scale(scaleX)
            .orient('bottom')
            .ticks(4);
        axisYFunction = d3.svg.axis()
            .scale(scaleY)
            .orient('left')
            .ticks(2);
        this.brushFunction.x(scaleX);
    };

    function draw() {
        line.attr('d', lineFunction(data));
        axisX.call(axisXFunction);
        axisY.call(axisYFunction);
        
        if (cachedBrushExtent !== undefined)
            this.brushFunction.extent(cachedBrushExtent);
        brush.call(this.brushFunction)
            .selectAll("rect")
            .attr("height", chartHeight);
    };
});
