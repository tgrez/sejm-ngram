
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

          //function for updating tooltip
          scope.onCircleOver = function(event, line, occur){
              d3.select("#tooltip")
                  .style("visibility", "visible")
                  .style("left", (event.layerX + 5) + "px")
                  .style("top", (event.layerY + 5 ) + "px");

              d3.select("#party")
                  .text(line.label);

              d3.select("#dateOccurence")
                  .text("data: " + formatToolTipDate(occur));

              d3.select("#value")
                  .text("ilość wystąpień ngramu: " +   occur.count);
          }

          scope.onCircleLeave = function(){
              d3.select("#tooltip").style("visibility", "hidden")
          }

          var formatToolTipDate = function(occurence){
            var datemode = occurence.datemode
            if (datemode == "exact_day") return occurence.date.toLocaleDateString('pl-PL')
            else if (datemode == "middle_month")  { return moment( occurence.date ).locale("pl").format("MMMM")  + " " + moment(occurence.date).year()}
            else {
              return occurence.grouppedDateFrom.toLocaleDateString('pl-PL') + " -  " + occurence.grouppedDateTo.toLocaleDateString('pl-PL')
            }
          }

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
              scope.toDisplay = calculateLinesToDisplay(scope)
          }

          function updateOnlyYRange(){
            scope.toDisplay = calculateLinesToDisplay(scope)
            scope.graph.yRange  = [0, calculateMaxY(scope.toDisplay) + 1]
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
					scope.$watch('graph.selectedParty', queueUpdate, true);
					scope.$watch(visiblePlots, updateOnlyYRange, true);

        },
        templateUrl: '/templates/chartPane.html',
        replace: true,
        transclude: true
    };
});

function calculateLinesToDisplay(scope){
  return scope.graph.plotLines
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

function calculateMaxY(toDisplay){
   var maxY = 0;
    _.each(toDisplay, function(line){
        var lineMaxY = _.max(line.occurences, function(occurence){
            return occurence.count;
        }).count
        if (lineMaxY > maxY) maxY = lineMaxY
    })
    return maxY
}

function aggregateAndFilter(occurences, minDate, maxDate) {
    var dayDelta = moment(maxDate).diff(moment(minDate), 'days');
    var aggFun = function (o) {
            return moment(o.date).date(15).toDate();// middle of the month
        }
    aggFun.datemode = "middle_month"
    var window = 200;
    if (dayDelta < window){
      aggFun = function (o) { return o.date; }
      aggFun.datemode = "exact_day"
    }

    else if (dayDelta < window * 13){
        aggFun = function (o) {
            return moment(o.date).day(3).toDate(); // get the wednesday of the week
        }
        aggFun.datemode = "wednesday_of_week"
    }

    // We want to include at least one point on each side of the selected range
    var firstInRangeIx = _.findIndex(occurences, function (o) {return o.date >= minDate; });
    if (firstInRangeIx > 0) {
      minDate = occurences[firstInRangeIx-1].date
    }
    var lastInRangeIx = _.findLastIndex(occurences, function (o) {return o.date <= maxDate; });
    if (lastInRangeIx > -1 && lastInRangeIx < occurences.length - 1) {
      maxDate = occurences[lastInRangeIx + 1].date;
    }
    var min = moment(minDate).subtract(dayDelta*0.1, "days");
    var max = moment(maxDate).add(dayDelta*0.1, "days");
    var os = occurences.filter(function(o){
        return o.date >= min  && o.date <= max
    })

    var grouped = _.groupBy(os, aggFun);
    var result = []
    _.each(grouped, function (os, d) {
        var count = os.length;
        var sum = _.reduce(os, function (acc, o) {return acc + o.count;}, 0);
        result.push({
                  date: new Date(d),
                  count: sum/count,
                  datemode: aggFun.datemode,
                  singleDate: (aggFun.datemode == "exact_day"),
                  grouppedDateFrom : calculateDateFrom(new Date(d), aggFun.datemode),
                  grouppedDateTo : calculateDateTo(new Date(d), aggFun.datemode)
                });
    })
    return result;
}

function calculateDateFrom(date, datemode){
  if (datemode == "exact_day") return null
  else if (datemode == "middle_month") return moment(date).startOf("month").toDate();
  else if (datemode == "wednesday_of_week") return moment(date).startOf("week").toDate();
}

function calculateDateTo(date, datemode){
  if (datemode == "exact_day") return null
  else if (datemode == "middle_month") return moment(date).endOf("month").toDate();
  else if (datemode == "wednesday_of_week") return moment(date).endOf("week").toDate();
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
