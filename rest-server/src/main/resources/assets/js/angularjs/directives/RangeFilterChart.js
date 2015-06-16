/// <reference path="../app.js" />
/// <reference path="../controllers/ChartCtrl.js" />
/// <reference path="~/js/vendor/d3.min.js" />
'use strict';

module.directive('stRangeFilterChart', function() {
    return {
        restrict: 'E',
        scope: {
            termsOccurences: '=ngModel',
            partiesVisibility: '=',
            partiesNames: '=',
            graphDrawHelper: '=',
            selectedRange: '='
        },
        link: function link(scope, iElement, iAttrs, controller, transcludeFn) {
            var LINE_PREFIX = 'rangeFilterLine';
            var svg;
            var svgWidth;
            var svgHeight;
            var chartWidth;
            var chartHeight;
            var linesCanvasWidth;
            var linesCanvasHeight;
            var defs;
            var clipPath;
            var clipPathRegion;
            var chart;
            var linesCanvas;
            var axisX;
            var axisY;
            var brush;
            var scaleX;
            var scaleY;
            var axisXFunction;
            var axisYFunction;
            var brushFunction;

            scope.isInitialized = false;

            scope.$watch('termsOccurences.length', onDataChange);
            scope.$watchCollection('partiesVisibility', onPartiesVisibilityChange)

            function onPartiesVisibilityChange(){
                scope.graphDrawHelper.setLineVisibility(LINE_PREFIX)
            }

            function onDataChange() {
                var isTermsOccurencesEmpty = typeof scope.termsOccurences === 'undefined' || scope.termsOccurences === null || scope.termsOccurences.length === 0;
                if (!isTermsOccurencesEmpty && !scope.isInitialized) {
                    initialize();
                    update();
                }
                if (scope.isInitialized) {
                    update();
                }

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
                    svg = d3.select('#range-filter-chart');
                    svgWidth = svg.node().offsetWidth;
                    svgHeight = svg.node().offsetHeight;
                    chartWidth = svgWidth - chartMargin.left - chartMargin.right;
                    chartHeight = svgHeight - chartMargin.top - chartMargin.bottom;
                    linesCanvasWidth = chartWidth - linesCanvasMargin.left - linesCanvasMargin.right;
                    linesCanvasHeight = chartHeight - linesCanvasMargin.top - linesCanvasMargin.bottom;

                    defs = svg.append('defs');
                    clipPath = defs.append('clipPath')
                        .attr('id', 'rangeFilterLinesCanvasRegion');
                    clipPathRegion = clipPath.append('rect');
                    clipPathRegion.attr({
                        width: linesCanvasWidth,
                        height: linesCanvasHeight
                    });

                    chart = svg.append('g')
                        .attr({
                            'class': 'chart',
                            'width': chartWidth,
                            'height': chartHeight,
                            'transform': 'translate(' + chartMargin.left + ', ' + chartMargin.top + ')'
                        });

                    linesCanvas = chart.append('g')
                        .attr({
                            'class': 'linesCanvas',
                            'width': linesCanvasWidth,
                            'height': linesCanvasHeight,
                            'transform': 'translate(' + linesCanvasMargin.left + ', ' + linesCanvasMargin.top + ')'
                        });
                    axisX = chart.append('g')
                        .attr({
                            'class': 'axis axisX',
                            'transform': 'translate(' + linesCanvasMargin.left + ', ' + (linesCanvasHeight + linesCanvasMargin.top) + ')'
                        });
                    axisY = chart.append('g')
                        .attr({
                            'class': 'axis axisY',
                            'transform': 'translate(' + linesCanvasMargin.left + ', ' + linesCanvasMargin.top + ')'
                        });
                    brush = linesCanvas.append('g')
                        .attr({
                            'class': 'brush'
                        });


                    scaleX = d3.time.scale()
                        .range([0, linesCanvasWidth]);
                    scaleY = d3.scale.linear()
                        .range([linesCanvasHeight, 0]);

                    brushFunction = d3.svg.brush()
                        .x(scaleX);

                    brush.call(brushFunction)
                        .selectAll("rect")
                        .attr("height", linesCanvasHeight);

                    brushFunction.on('brush', function () {
                        scope.$apply(function () {
                            scope.selectedRange = brushFunction.extent();
                        });
                    });

                    scope.isInitialized = true;
                }
            }

            function update() {
                var minY = 0;
                var maxY = 0;

                var multiLineData = scope.graphDrawHelper.calculateMultiLineData(scope.termsOccurences)

                for (var i = 0; i < multiLineData.length; i++) {
                    var tempMaxY = d3.max(multiLineData[i].occurences, function(o) { return o.count; });
                    if (tempMaxY > maxY)
                        maxY = tempMaxY;
                }

                var xRange = [multiLineData[0].occurences[0].date, multiLineData[0].occurences[multiLineData[0].occurences.length - 1].date];
                var yRange = [minY, maxY];

                scaleX.domain(xRange);
                scaleY.domain(yRange);

                for (var i = 0; i < multiLineData.length; i++) {
                    var lineId = scope.graphDrawHelper.generateLineId(LINE_PREFIX, multiLineData[i].lineName);

                    var lineFunction = d3.svg.line()
                        .x(function(o, i) { return scaleX(o.date); })
                        .y(function(o, i) { return scaleY(o.count); });
                    var flatLineFunction = d3.svg.line()
                        .x(function(o, i) { return scaleX(o.date); })
                        .y(function(o, i) { return linesCanvasHeight; });

                    var line = d3.select('#' + lineId);
                    var isLineExist = line.empty();
                    if (isLineExist) {
                        line = linesCanvas.insert('path', '.brush')
                            .attr('id', lineId)
                            .attr('class', 'line')
                            .attr('clip-path', 'url(#rangeFilterLinesCanvasRegion)')
                            .attr('style', 'stroke: ' + scope.graphDrawHelper.generateLineColorForPartyName(multiLineData[i].lineName))
                            .attr('d', flatLineFunction(multiLineData[i].occurences))
                            .transition()
                            .duration(1000)
                            .attr('d', lineFunction(multiLineData[i].occurences));
                    } else {
                        line.transition()
                            .duration(1000)
                            .attr('d', lineFunction(multiLineData[i].occurences));
                    }
                }
                scope.graphDrawHelper.removeObsolateLines(linesCanvas, multiLineData, LINE_PREFIX);

                axisXFunction = d3.svg.axis()
                    .scale(scaleX)
                    .orient('bottom')
                    .ticks(4);
                axisYFunction = d3.svg.axis()
                    .scale(scaleY)
                    .orient('left')
                    .ticks(2);

                axisX.transition().duration(1000).call(axisXFunction);
                axisY.transition().duration(1000).call(axisYFunction);
            }
        },
        template:
            '<svg width="758" height="150">' +
                '</svg>',
        replace: true
    };
});

