/// <reference path="../app.js" />

'use strict';

module.controller('ChartCtrl', function ($scope, $http, tagsService, termOccurencesChartService, rangeFilterChartService) {
    $scope.wasSearchTriggered = false;
    $scope.isSearchInProgres = false;
    $scope.tagsService = tagsService;

    $scope.mostPopularTags = [
        'aborcja',
        'Tusk',
        'Unia Europejska',
        'deficyt',
        'Rosja',
        'Putin',
        'Platforma Obywatelska',
        'PIS',
        'TVN'
    ];

    $scope.search = function () {
        $scope.wasSearchTriggered = true;
        $scope.isSearchInProgress = true;
    };

    var dateFormat = d3.time.format('%Y-%m-%d');
    var chartData = [
            { date: '2000-01-01', termOccurrences: 1 },
            { date: '2000-01-02', termOccurrences: 3 },
            { date: '2000-01-03', termOccurrences: 5 },
            { date: '2000-01-04', termOccurrences: 7 },
            { date: '2000-01-05', termOccurrences: 5 },
            { date: '2000-01-06', termOccurrences: 3 },
            { date: '2000-01-07', termOccurrences: 7 },
            { date: '2000-01-08', termOccurrences: 0 },
            { date: '2000-01-09', termOccurrences: 1 },
            { date: '2000-01-10', termOccurrences: 4 },
            { date: '2000-01-11', termOccurrences: 3 },
            { date: '2000-01-12', termOccurrences: 3 },
            { date: '2000-01-13', termOccurrences: 1 },
            { date: '2000-01-14', termOccurrences: 3 },
            { date: '2000-01-15', termOccurrences: 7 },
            { date: '2000-01-16', termOccurrences: 8 },
            { date: '2000-01-17', termOccurrences: 8 },
            { date: '2000-01-18', termOccurrences: 8 },
            { date: '2000-01-19', termOccurrences: 9 },
            { date: '2000-01-20', termOccurrences: 4 },
            { date: '2000-01-21', termOccurrences: 2 },
            { date: '2000-01-22', termOccurrences: 2 },
            { date: '2000-01-23', termOccurrences: 1 },
            { date: '2000-01-24', termOccurrences: 4 },
            { date: '2000-01-25', termOccurrences: 5 },
            { date: '2000-01-26', termOccurrences: 5 },
            { date: '2000-01-27', termOccurrences: 1 },
            { date: '2000-01-28', termOccurrences: 2 },
            { date: '2000-01-29', termOccurrences: 5 },
            { date: '2000-01-30', termOccurrences: 6 },
            { date: '2000-01-31', termOccurrences: 7 },
            { date: '2000-02-01', termOccurrences: 2 },
            { date: '2000-02-02', termOccurrences: 2 },
            { date: '2000-02-03', termOccurrences: 0 },
            { date: '2000-02-04', termOccurrences: 0 },
            { date: '2000-02-05', termOccurrences: 0 },
            { date: '2000-02-06', termOccurrences: 1 },
            { date: '2000-02-07', termOccurrences: 1 },
    ];
    chartData.forEach(function (d, i) { d.date = dateFormat.parse(d.date); });

    termOccurencesChartService.initialize(chartData);
    rangeFilterChartService.initialize(chartData, function () {
        var xRange = rangeFilterChartService.brushFunction.extent();
        termOccurencesChartService.updateRange(xRange);
    });
});

