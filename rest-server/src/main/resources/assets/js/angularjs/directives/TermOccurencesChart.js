/// <reference path="../app.js" />
/// <reference path="../controllers/ChartCtrl.js" />
/// <reference path="~/js/vendor/d3.min.js" />
'use strict';

module.directive('stTermOccurencesChart', function () {
    return {
        restrict: 'E',
        scope: {
            termsOccurences: '=ngModel',
            displayRange: '=',
            linesColors: '='
        },
        link: function link(scope, iElement, iAttrs, controller, transcludeFn) {
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
            var scaleX;
            var scaleY;
            var axisXFunction;
            var axisYFunction;

            scope.isInitialized = false;

            scope.$watch('termsOccurences.length', onDataChange);
            scope.$watch('displayRange', onDisplayRangeChange);

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
                    svg = d3.select('#term-occurences-chart');
                    svgWidth = svg.node().offsetWidth;
                    svgHeight = svg.node().offsetHeight;
                    chartWidth = svgWidth - chartMargin.left - chartMargin.right;
                    chartHeight = svgHeight - chartMargin.top - chartMargin.bottom;
                    linesCanvasWidth = chartWidth - linesCanvasMargin.left - linesCanvasMargin.right;
                    linesCanvasHeight = chartHeight - linesCanvasMargin.top - linesCanvasMargin.bottom;
                    
                    defs = svg.append('defs');
                    clipPath = defs.append('clipPath')
                        .attr('id', 'termOccurencesLinesCanvasRegion');
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

                    scaleX = d3.time.scale()
                        .range([0, linesCanvasWidth]);
                    scaleY = d3.scale.linear()
                        .range([linesCanvasHeight, 0]);

                    scope.isInitialized = true;
                }
            }

            function onDisplayRangeChange() {
                if (scope.isInitialized) {
                    scaleX.domain(scope.displayRange);
                    axisX.call(axisXFunction);
                    axisY.call(axisYFunction);

                    for (var i = 0; i < scope.termsOccurences.length; i++) {
                        var lineFunction = d3.svg.line()
                            .x(function(o, i) { return scaleX(o.date); })
                            .y(function(o, i) { return scaleY(o.count); });
                        linesCanvas.select('.line:nth-child(' + (i + 1) + ')')
                            .attr('d', lineFunction(scope.termsOccurences[i].occurences));
                    }
                }
            }

            function update() {

                var minY = 0;
                var maxY = 0;

                for (var i = 0; i < scope.termsOccurences.length; i++) {
                    var tempMaxY = d3.max(scope.termsOccurences[i].occurences, function (o) { return o.count; });
                    if (tempMaxY > maxY)
                        maxY = tempMaxY;
                }

                var xRange = [scope.termsOccurences[0].occurences[0].date, scope.termsOccurences[0].occurences[scope.termsOccurences[0].occurences.length - 1].date];
                var yRange = [minY, maxY];

                var isDisplayRangeValid = typeof scope.displayRange !== 'undefined' && scope.displayRange !== null && scope.displayRange.length > 0;
                if (isDisplayRangeValid)
                    scaleX.domain(scope.displayRange);
                else 
                    scaleX.domain(xRange);
                scaleY.domain(yRange);

                for (var i = 0; i < scope.termsOccurences.length; i++) {
                    var lineId = generateLineId(scope.termsOccurences[i].name);

                    var lineFunction = d3.svg.line()
                        .x(function (o) { return scaleX(o.date); })
                        .y(function (o) { return scaleY(o.count); });
                    var flatLineFunction = d3.svg.line()
                        .x(function (o) { return scaleX(o.date); })
                        .y(function (o) { return linesCanvasHeight; });


                    var line = d3.select('#' + lineId);
                    var isLineExist = line.empty();
                    if (isLineExist) {
                        line = linesCanvas.append("path")
                            .attr('id', lineId)
                            .attr('class', 'line')
                            .attr('clip-path', 'url(#termOccurencesLinesCanvasRegion)')
                            .attr('style', 'stroke: ' + scope.linesColors[i])
                            .attr('d', flatLineFunction(scope.termsOccurences[i].occurences))
                            .transition()
                            .duration(1000)
                            .attr('d', lineFunction(scope.termsOccurences[i].occurences));
                    } else {
                        line.transition()
                            .duration(1000)
                            .attr('d', lineFunction(scope.termsOccurences[i].occurences));
                    }
                }

                removeObsolateLines(linesCanvas, scope.termsOccurences);

                axisXFunction = d3.svg.axis()
                    .scale(scaleX)
                    .orient('bottom')
                    .ticks(4);
                axisYFunction = d3.svg.axis()
                    .scale(scaleY)
                    .orient('left');

                axisX.transition().duration(1000).call(axisXFunction);
                axisY.transition().duration(1000).call(axisYFunction);
            }

            function generateLineId(term) {
                var termFormatted = term.replace(/\s/g, '_');
                var uniqueLineId = 'termOccurencesLine-' + termFormatted;

                return uniqueLineId;
            }

            function removeObsolateLines(linesCanvas, termsOccurences) {
                var lines = linesCanvas.selectAll('.line');

                lines.each(function () {
                    var line = this;
                    var lineId = line.id;
                    var isTermExist = _.any(termsOccurences, function (o, i) {
                        var term = o.name;
                        var termId = generateLineId(term);

                        return termId === lineId;
                    });

                    if (!isTermExist) {
                        line.remove();
                    }
                });
            }
        },
        template:
            '<svg width="758" height="400">' +
            '</svg>',
        replace: true
    };
});


