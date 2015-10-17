/// <reference path="../app.js" />
/// <reference path="../controllers/ChartCtrl.js" />
/// <reference path="~/js/vendor/d3.min.js" />
'use strict';

function initChartPane(chartPane) {
    var svg = chartPane.svg;
    var chartMargin = {
        top: 10,
        bottom: 10,
        left: 10,
        right: 10
    };
    var linesCanvasMargin = {
        top: 40,
        bottom: 40,
        left: 40,
        right: 40
    };
    var svgWidth = svg.node().offsetWidth;
    var svgHeight = svg.node().offsetHeight;
    var chartWidth = svgWidth - chartMargin.left - chartMargin.right;
    var chartHeight = svgHeight - chartMargin.top - chartMargin.bottom;
    var linesCanvasWidth = chartWidth - linesCanvasMargin.left - linesCanvasMargin.right;
    var linesCanvasHeight = chartPane.linesCanvasHeight =
        chartHeight - linesCanvasMargin.top - linesCanvasMargin.bottom;

    var defs = svg.append('defs');
    var linesCanvasRegionId = chartPane.linesCanvasRegionId = chartPane.idPrefix + 'lines-canvas-region';
    var clipPath = defs.append('clipPath')
        .attr('id', linesCanvasRegionId);
    var clipPathRegion = clipPath.append('rect');
    clipPathRegion.attr({
        width: linesCanvasWidth,
        height: linesCanvasHeight
    });

    var chart = svg.append('g')
        .attr({
            'class': 'chart',
            'width': chartWidth,
            'height': chartHeight,
            'transform': 'translate(' + chartMargin.left + ', ' + chartMargin.top + ')'
        });

    chartPane.linesCanvas = chart.append('g')
        .attr({
            'class': 'linesCanvas',
            'width': linesCanvasWidth,
            'height': linesCanvasHeight,
            'transform': 'translate(' + linesCanvasMargin.left + ', ' + linesCanvasMargin.top + ')'
        });
    chartPane.axisX = chart.append('g')
        .attr({
            'class': 'axis axisX',
            'transform': 'translate(' + linesCanvasMargin.left + ', ' + (linesCanvasHeight + linesCanvasMargin.top) + ')'
        });
    chartPane.axisY = chart.append('g')
        .attr({
            'class': 'axis axisY',
            'transform': 'translate(' + linesCanvasMargin.left + ', ' + linesCanvasMargin.top + ')'
        });

    chartPane.scaleX = d3.time.scale()
        .range([0, linesCanvasWidth]);
    chartPane.scaleY = d3.scale.linear()
        .range([linesCanvasHeight, 0]);
    
    chartPane.redrawLines = function(multiLineData, shouldAnimate, scope) {
        var that = this;
        for (var i = 0; i < multiLineData.length; i++) {
            var lineId = scope.graphDrawHelper.generateLineId(this.idPrefix, multiLineData[i].lineName);

            var lineFunction = d3.svg.line()
                .x(function (o) { return that.scaleX(o.date); })
                .y(function (o) { return that.scaleY(o.count); });
            var flatLineFunction = d3.svg.line()
                .x(function (o) { return that.scaleX(o.date); })
                .y(function (o) { return that.linesCanvasHeight; });


            var line = d3.select('#' + lineId);
            var isLineExist = line.empty();
            if (isLineExist) {
                line = this.linesCanvas.insert("path",".brush") // place before any brush element, so that brush is not overdrawn
                    .attr('id', lineId)
                    .attr('class', 'line')
                    .attr('clip-path', 'url(#' + this.linesCanvasRegionId +')')
                    .attr('style', 'stroke: ' + scope.graphDrawHelper.generateLineColorForPartyName(multiLineData[i].lineName));
                if (shouldAnimate) {
                    line.attr('d', flatLineFunction(multiLineData[i].occurences))
                      .transition()
                      .duration(1000)
                }
                line.attr('d', lineFunction(multiLineData[i].occurences));
            } else {
                if (shouldAnimate) line.transition().duration(1000);
                line.attr('d', lineFunction(multiLineData[i].occurences));
            }
        }

        scope.graphDrawHelper.removeObsolateLines(this.linesCanvas, multiLineData, this.idPrefix);
    };

    chartPane.initBrushRangeSelector = function(onRangeChange) {
        var brush = this.linesCanvas.append('g')
            .attr({
                'class': 'brush'
            });

        var brushFunction = d3.svg.brush()
            .x(this.scaleX);

        brush.call(brushFunction)
            .selectAll("rect")
            .attr("height", this.linesCanvasHeight);

        brushFunction.on('brush', function () {
            onRangeChange(brushFunction.extent());
        });
    };

    return chartPane;
}

module.directive('stTermOccurencesChart', function () {
    return {
        restrict: 'E',
        scope: {
            termsOccurences: '=ngModel',
            partiesNames: '=',
            graphDrawHelper: '=',
        },
        link: function link(scope, iElement, iAttrs) {
            var mainChart, rangeChart;

            scope.$watch('termsOccurences.length', onDataChange);
            scope.$watch('partiesNames', onPartiesVisibilityChange, true)

            scope.isInitialized = false;
            function initialize() {
                mainChart = initChartPane({
                    svg: d3.select('#term-occurences-chart'),
                    idPrefix: 'term-occurances-chart-'});
                rangeChart = initChartPane({
                    svg: d3.select('#range-filter-chart'),
                    idPrefix: 'range-filter-chart-',
                    yAxisTicks: 2});
                rangeChart.initBrushRangeSelector(function (newRange) {
                    scope.$apply(function () {
                      mainChart.scaleX.domain(newRange);
                      mainChart.redrawLines(scope.multiLineData, false, scope);
                    });
                  });
                scope.isInitialized = true;
            }

            function onPartiesVisibilityChange(chartPane){
                if (scope.isInitialized) {
                    scope.graphDrawHelper.setLineVisibility(mainChart.idPrefix)
                    scope.graphDrawHelper.setLineVisibility(rangeChart.idPrefix)
                }
            }

            function onDataChange() {
                var isTermsOccurencesEmpty = typeof scope.termsOccurences === 'undefined' || scope.termsOccurences === null || scope.termsOccurences.length === 0;
                scope.multiLineData = scope.graphDrawHelper.calculateMultiLineData(scope.termsOccurences)
                if (!isTermsOccurencesEmpty && !scope.isInitialized) {
                     initialize();
                     update();
                }
                if (scope.isInitialized)
                    update();
            }


            function update() {
                var multiLineData = scope.multiLineData
                var minY = 0;
                var maxY = 0;
                for (var i = 0; i < multiLineData.length; i++) {
                    var tempMaxY = d3.max(multiLineData[i].occurences, function (o) { return o.count; });
                    if (tempMaxY > maxY)
                        maxY = tempMaxY;
                }

                var xRange = [multiLineData[0].occurences[0].date, multiLineData[0].occurences[multiLineData[0].occurences.length - 1].date];
                var yRange = [minY, maxY];

                var isDisplayRangeValid = typeof scope.displayRange !== 'undefined' && scope.displayRange !== null && scope.displayRange.length > 0;
                if (isDisplayRangeValid)
                    mainChart.scaleX.domain(scope.displayRange);
                else 
                    mainChart.scaleX.domain(xRange);
                rangeChart.scaleX.domain(xRange);

                [mainChart, rangeChart].forEach(function (chartPane) {
                  chartPane.scaleY.domain(yRange);

                  chartPane.redrawLines(multiLineData, true, scope);

                  var axisXFunction = d3.svg.axis()
                      .scale(chartPane.scaleX)
                      .orient('bottom')
                      .ticks(4);
                  var axisYFunction = d3.svg.axis()
                      .scale(chartPane.scaleY)
                      .orient('left');
                  if (chartPane.yAxisTicks !== undefined)
                      axisYFunction.ticks(chartPane.yAxisTicks);

                  chartPane.axisX.transition().duration(1000).call(axisXFunction);
                  chartPane.axisY.transition().duration(1000).call(axisYFunction);
                })
            }

        },
        template:
            '<div><svg id="term-occurences-chart" width="758" height="400"></svg>'
            +'<svg id="range-filter-chart" width="758" height="150"></svg></div>',
        replace: true
    };
});


