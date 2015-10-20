
module.directive('chartPane', function () {
    return {
        restrict: 'E',
        scope: {
            graph: '=',
            currentRange: '='
        },
        controller: function ($scope) {
            this.scope = $scope;
        },
        link: function link(scope, iElement, iAttrs, controller) {
          // this function gets executed even before the graph shows
          var id = iElement.attr('id');
          scope.idPrefix = id + '-';
          var svg = d3.select('#' + id);
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
          var svgWidth = iElement.attr('width');
          var svgHeight = iElement.attr('height');
          var chartWidth = svgWidth - chartMargin.left - chartMargin.right;
          var chartHeight = svgHeight - chartMargin.top - chartMargin.bottom;
          var linesCanvasWidth = chartWidth - linesCanvasMargin.left - linesCanvasMargin.right;
          var linesCanvasHeight = controller.linesCanvasHeight = scope.linesCanvasHeight =
              chartHeight - linesCanvasMargin.top - linesCanvasMargin.bottom;
          svg.select('clipPath rect').attr({
              width: linesCanvasWidth,
              height: linesCanvasHeight
          });

          var chart = svg.select('g.chart');
          chart.attr({
                  width: chartWidth,
                  height: chartHeight,
                  transform: 'translate(' + chartMargin.left + ', ' + chartMargin.top + ')'
              });

          var linesCanvas = svg.select('g.linesCanvas');
          linesCanvas.attr({
              width: linesCanvasWidth,
              height: linesCanvasHeight,
              transform: 'translate(' + linesCanvasMargin.left + ', ' + linesCanvasMargin.top + ')'
          });
          scope.axisX = chart.append('g')
              .attr({
                  'class': 'axis axisX',
                  'transform': 'translate(' + linesCanvasMargin.left + ', ' + (linesCanvasHeight + linesCanvasMargin.top) + ')'
              });
          scope.axisY = chart.append('g')
              .attr({
                  'class': 'axis axisY',
                  'transform': 'translate(' + linesCanvasMargin.left + ', ' + linesCanvasMargin.top + ')'
              });

          scope.scaleX = d3.time.scale()
              .range([0, linesCanvasWidth]);
          scope.scaleY = d3.scale.linear()
              .range([linesCanvasHeight, 0]);
          controller.scaleX = scope.scaleX; // TODO can this be shared as scope with range selector?

          function update() {
              scope.scaleX.domain(scope.currentRange ? scope.currentRange : scope.graph.xRange);
              scope.scaleY.domain(scope.graph.yRange);

              var axisXFunction = d3.svg.axis()
                  .scale(scope.scaleX)
                  .orient('bottom')
                  .ticks(4);
              var axisYFunction = d3.svg.axis()
                  .scale(scope.scaleY)
                  .orient('left').ticks(linesCanvasHeight/28);
              if (scope.yAxisTicks !== undefined)
                  axisYFunction.ticks(scope.yAxisTicks);

              scope.axisX.transition().duration(1000).call(axisXFunction);
              scope.axisY.transition().duration(1000).call(axisYFunction);
          }
            //TODO watch xRange and yRange to update scales
          scope.$watch('currentRange', update, true);
          scope.$watch('graph.xRange', update, true);
        },
        templateUrl: '/templates/chartPane.html',
        replace: true,
        transclude: true
    };
});

module.directive('rangeSelector', function () {
    return {
        restrict: 'E',
        require: '^chartPane',
        scope: {
            graph: '=',
            currentRange: '='
        },
        link: function (scope, iElement, iAttrs, chartPane) {
          var initialized = false;
          var brush;
          function initBrushRangeSelector (scaleX) {
              initialized = true;
              brush = d3.svg.brush().x(scaleX);

              // NOTE: if we don't create the brush under the parent, the range is not selectable
              // (The rect object for the brush has no size), I'd like to know the cause.
              // to repro: remove '.parent()' from the line
              var parent = d3.select(iElement.parent()[0]);

              var g = parent.append("g").attr({'class': 'brush'});
              g.call(brush)
                  .selectAll("rect")
                  .attr("height", chartPane.linesCanvasHeight);

              // to start off, range is equal to scaleX, and the brush is empty
              scope.currentRange = scaleX.domain();
              brush.on('brush', function () {
                  var newRange = brush.extent();
                  if (newRange[0].valueOf() === newRange[1].valueOf()) {
                    newRange = scaleX.domain();
                    brush.clear(); // TODO does not seem to work, there is a hair width brush still there
                  }
                  if (newRange !== scope.currentRange) {
                    scope.graph.selectedRange = newRange;
                    scope.$apply();
                  }
              });
          }
          function update() {
              var scaleX = chartPane.scope.scaleX;
              console.log("update scaleX", scaleX, scaleX.range());
              if (scaleX) {
                if (!initialized)
                    initBrushRangeSelector(scaleX);
                // that means a new search was executed
                scope.currentRange = scaleX.range();
                brush.clear(); // TODO untested
                brush.x(scaleX);
              }
          }
          chartPane.scope.$watch('scaleX', update, true);
        },
        replace: true,
        template: "<g></g>" // g is not used, see the NOTE in initBrushRangeSelector
    }
});

module.filter('encodeSvgPath', function () {
    return function(occurences, scaleX, scaleY) {
        var lineFunction = d3.svg.line()
            .x(function (o) { return scaleX(o.date); })
            .y(function (o) { return scaleY(o.count); });
        return lineFunction(occurences);
    };
})
