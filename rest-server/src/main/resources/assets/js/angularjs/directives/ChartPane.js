
module.directive('chartPane', function () {
    return {
        restrict: 'E',
        scope: {
            graph: '=',
            currentRange: '='
        },
        controller: function ($scope) {
            this.scope = $scope; // for rangeSelector, which watches scaleX
        },
        link: function link(scope, iElement, iAttrs, controller) {
          // this function gets executed long before the graph shows
          var id = iElement.attr('id');
          scope.idPrefix = id + '-';
          var svg = d3.select('#' + id);
          var chartMargin = {
              top: 10,
              bottom: 10,
              left: 10,
              right: 10
          };
          // chart includes the axes, linesCanvas does not, and it is the area lines are plotted
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
          var linesCanvasHeight = controller.linesCanvasHeight =
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
          controller.scaleX = scope.scaleX;

					function visiblePlots() {
						return scope.graph.plotLines
							           .filter(function (l) {return l.isVisible})
							           .map(function (l) {return l.label});
					}

					scope.toDisplay = [];
					var updateQueued = true;
          function updateAxes() {
						  updateQueued = false;
              // it also updates scales, rangeSelector is watchint scaleX
              scope.scaleX.domain(scope.currentRange);
              scope.scaleY.domain(scope.graph.yRange);

              var axisXFunction = d3.svg.axis()
                  .scale(scope.scaleX)
                  .orient('bottom')
                  .ticks(4);
              var axisYFunction = d3.svg.axis()
                  .scale(scope.scaleY)
                  .orient('left').ticks(linesCanvasHeight/28);

              scope.axisX.call(axisXFunction);

              scope.axisY.call(axisYFunction);
						  scope.toDisplay =
                  scope.graph.plotLines
									.filter(function (l) {return l.isVisible})
									.map(function (plotLine) {

										var lineFunction = d3.svg.line()
												.x(function (o) { return scope.scaleX(o.date); })
												.y(function (o) { return scope.scaleY(o.count); });
										var minDate = scope.scaleX.domain()[0];
										var maxDate = scope.scaleX.domain()[1];
										occurences = aggregateAndFilter(plotLine.occurences, minDate, maxDate);
										return { "occurences": occurences, "label": plotLine.label,
														 "color": plotLine.color,
													   "svgData": lineFunction(occurences)};
							});
          }
					function queueUpdate() {
						updateQueued = true;
						scope.$evalAsync(updateAxes);
					}
					// need to inline watchGroup because it does not accept the third argument
          // that tells it to compare by equality rather than reference
          //scope.$watchGroup(['currentRange', 'graph.yRange', visiblePlots], updateAxes);
					scope.$watch('currentRange', queueUpdate, true);
					scope.$watch('graph.yRange', queueUpdate, true);
					scope.$watch(visiblePlots, queueUpdate, true);
        },
        templateUrl: '/templates/chartPane.html',
        replace: true,
        transclude: true
    };
});

// < 50 days : kazdy punkt
// > 50 dni AND < 350 dnii (rok) : grupujemy po tygodniach
// > rok AND < 4 lata : gupoujemy po miesiacach
// > 4 lata : grupujemy po kwartalach
// > 12 lat : grupujemy po latach

function aggregateAndFilter(occurences, minDate, maxDate) {
    var dayDelta = moment(maxDate).diff(moment(minDate), 'days');
    var aggFun = function (o) { return moment(o.date).month(5).date(0).toDate(); }
    if (dayDelta < 50)
        aggFun = function (o) { return o.date; }
    else if (dayDelta < 350)
        aggFun = function (o) {
            // get the wednesday of the week
            return moment(o.date).day(3).toDate();
        }
    else if (dayDelta < 350 * 4)
        aggfun = function (o) {
            // middle of the month
            return moment(o.date).date(15).toDate();
        }
    else if (dayDelta < 350 * 4 * 3)
        aggFun = function (o) {
            var m = moment(o.date);
            var month = m.quarter()*3 - 1; // middle month of the qtr
            return m.month(month-1).date(15).toDate();
        }
    var os = occurences.filter(function(o){
        return o.date >= minDate  && o.date <= maxDate
    })
    var grouped = _.groupBy(os, aggFun);
    var result = []
    _.each(grouped, function (os, d) {
        var count = os.length;
        var sum = _.reduce(os, function (acc, o) {return acc + o.count;}, 0);
        result.push({date: new Date(d), count: sum/count});
    })
    return result;
}


/* Directive that decorates the parent chart pane with a brush range selector */
module.directive('rangeSelector', function () {
    return {
        restrict: 'E',
        require: '^chartPane',
        scope: {
            currentRange: '=' // two-way binding
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

              // to start off, range is equal to scaleX of the parent chart pane, and the brush is empty
              scope.currentRange = scaleX.domain();
              brush.on('brush', function () {
                  var newRange = brush.extent();
                  if (newRange[0].valueOf() === newRange[1].valueOf()) {
                    newRange = chartPane.scope.scaleX.domain();
                    brush.clear(); // TODO does not seem to work, there is a hair width brush still there
                  }
                  if (newRange !== scope.currentRange) {
                    scope.currentRange = newRange;
                    scope.$apply();
                  }
              });
          }
          function update() {
              var scaleX = chartPane.scope.scaleX;
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
