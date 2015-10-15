/// <reference path="../app.js" />
/// <reference path="../controllers/ChartCtrl.js" />
/// <reference path="~/js/vendor/d3.min.js" />
'use strict';

module.directive('stTermOccurencesChart', function () {
    return {
        restrict: 'E',
        scope: {
            termsOccurences: '=ngModel',
            partiesNames: '=',
            graphDrawHelper: '=',
            displayRange: '=',
        },
        link: function link(scope, iElement, iAttrs) {
            var chartPane = {};

            var LINE_PREFIX = 'termOccurencesLine'

            scope.$watch('termsOccurences.length', onDataChange);
            scope.$watch('displayRange', onDisplayRangeChange);
            scope.$watch('partiesNames', onPartiesVisibilityChange, true)

            scope.isInitialized = false;

            function initialize() {
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
                var svg = d3.select('#term-occurences-chart');
                var svgWidth = svg.node().offsetWidth;
                var svgHeight = svg.node().offsetHeight;
                var chartWidth = svgWidth - chartMargin.left - chartMargin.right;
                var chartHeight = svgHeight - chartMargin.top - chartMargin.bottom;
                var linesCanvasWidth = chartWidth - linesCanvasMargin.left - linesCanvasMargin.right;
                var linesCanvasHeight = chartPane.linesCanvasHeight =
                    chartHeight - linesCanvasMargin.top - linesCanvasMargin.bottom;

                var defs = svg.append('defs');
                var clipPath = defs.append('clipPath')
                    .attr('id', 'termOccurencesLinesCanvasRegion');
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

                scope.isInitialized = true;
            }

            function onPartiesVisibilityChange(){
                scope.graphDrawHelper.setLineVisibility(LINE_PREFIX)
            }

            function onDataChange() {
                var isTermsOccurencesEmpty = typeof scope.termsOccurences === 'undefined' || scope.termsOccurences === null || scope.termsOccurences.length === 0;
                scope.multiLineData = scope.graphDrawHelper.calculateMultiLineData(scope.termsOccurences)
                if (!isTermsOccurencesEmpty && !scope.isInitialized) {
                        initialize();
                        update();
                }
                if (scope.isInitialized) {
                    update();
                }

            }

            function onDisplayRangeChange() {
                if (scope.isInitialized) {
                    chartPane.scaleX.domain(scope.displayRange);
                    redrawLines(scope.multiLineData, false);
                }
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
                    chartPane.scaleX.domain(scope.displayRange);
                else 
                    chartPane.scaleX.domain(xRange);
                chartPane.scaleY.domain(yRange);

                redrawLines(multiLineData, true);

                var axisXFunction = d3.svg.axis()
                    .scale(chartPane.scaleX)
                    .orient('bottom')
                    .ticks(4);
                var axisYFunction = d3.svg.axis()
                    .scale(chartPane.scaleY)
                    .orient('left');

                chartPane.axisX.transition().duration(1000).call(axisXFunction);
                chartPane.axisY.transition().duration(1000).call(axisYFunction);
            }

            function redrawLines(multiLineData, shouldAnimate) {
                for (var i = 0; i < multiLineData.length; i++) {
                    var lineId = scope.graphDrawHelper.generateLineId(LINE_PREFIX, multiLineData[i].lineName);

                    var lineFunction = d3.svg.line()
                        .x(function (o) { return chartPane.scaleX(o.date); })
                        .y(function (o) { return chartPane.scaleY(o.count); });
                    var flatLineFunction = d3.svg.line()
                        .x(function (o) { return chartPane.scaleX(o.date); })
                        .y(function (o) { return chartPane.linesCanvasHeight; });


                    var line = d3.select('#' + lineId);
                    var isLineExist = line.empty();
                    if (isLineExist) {
                        line = chartPane.linesCanvas.append("path")
                            .attr('id', lineId)
                            .attr('class', 'line')
                            .attr('clip-path', 'url(#termOccurencesLinesCanvasRegion)')
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

                scope.graphDrawHelper.removeObsolateLines(chartPane.linesCanvas, multiLineData, LINE_PREFIX);
            }
        },
        template:
            '<svg width="758" height="400">' +
            '</svg>',
        replace: true
    };
});


