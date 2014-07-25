/// <reference path="../app.js" />

'use strict';

module.controller('ChartCtrl', function ($scope, $http, tagsService, termOccurencesService, rangeFilterService) {
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

    var d3PolishLocale = {
        "decimal": ",",
        "thousands": " ",
        "grouping": [3],
        "currency": ["", "zł"],
        "dateTime": "%e %b %Y %X",
        "date": "%Y-%m-%d",
        "time": "%H:%M:%S",
        "periods": ["", ""], // AM,PM
        "days": ["niedziela", "poniedziałek", "wtorek", "środa", "czwartek", "piątek", "sobota"],
        "shortDays": ["N", "Pn", "Wt", "Śr", "Cz", "Pt", "So"],
        "months": ["styczeń", "luty", "marzec", "kwiecień", "maj", "czerwiec", "lipiec", "sierpień", "wrzesień", "październik", "listopad", "grudzień"],
        "shortMonths": ["sty", "lut", "mar", "kwi", "maj", "cze", "lip", "sie", "wrz", "paź", "lis", "gru"]
    };
    var polishLocale = d3.locale(d3PolishLocale);
    d3.format = polishLocale.numberFormat;
    d3.time.format = polishLocale.timeFormat;

    var dateFormat = polishLocale.timeFormat('%Y-%m-%d');
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
            { date: '2000-02-08', termOccurrences: 0 },
            { date: '2000-02-09', termOccurrences: 1 },
            { date: '2000-02-10', termOccurrences: 4 },
            { date: '2000-02-11', termOccurrences: 3 },
            { date: '2000-02-12', termOccurrences: 3 },
            { date: '2000-02-13', termOccurrences: 1 },
            { date: '2000-02-14', termOccurrences: 3 },
            { date: '2000-02-15', termOccurrences: 7 },
            { date: '2000-02-16', termOccurrences: 8 },
            { date: '2000-02-17', termOccurrences: 8 },
            { date: '2000-02-18', termOccurrences: 8 },
            { date: '2000-02-19', termOccurrences: 9 },
            { date: '2000-02-20', termOccurrences: 4 },
            { date: '2000-02-21', termOccurrences: 2 },
            { date: '2000-02-22', termOccurrences: 2 },
            { date: '2000-02-23', termOccurrences: 1 },
            { date: '2000-02-24', termOccurrences: 4 },
            { date: '2000-02-25', termOccurrences: 5 },
            { date: '2000-02-26', termOccurrences: 5 },
            { date: '2000-02-27', termOccurrences: 1 },
            { date: '2000-02-28', termOccurrences: 2 },
            { date: '2000-03-01', termOccurrences: 1 },
            { date: '2000-03-02', termOccurrences: 3 },
            { date: '2000-03-03', termOccurrences: 5 },
            { date: '2000-03-04', termOccurrences: 7 },
            { date: '2000-03-05', termOccurrences: 5 },
            { date: '2000-03-06', termOccurrences: 3 },
            { date: '2000-03-07', termOccurrences: 7 },
            { date: '2000-03-08', termOccurrences: 0 },
            { date: '2000-03-09', termOccurrences: 1 },
            { date: '2000-03-10', termOccurrences: 4 },
            { date: '2000-03-11', termOccurrences: 3 },
            { date: '2000-03-12', termOccurrences: 3 },
            { date: '2000-03-13', termOccurrences: 1 },
            { date: '2000-03-14', termOccurrences: 3 },
            { date: '2000-03-15', termOccurrences: 7 },
            { date: '2000-03-16', termOccurrences: 8 },
            { date: '2000-03-17', termOccurrences: 8 },
            { date: '2000-03-18', termOccurrences: 8 },
            { date: '2000-03-19', termOccurrences: 9 },
            { date: '2000-03-20', termOccurrences: 4 },
            { date: '2000-03-21', termOccurrences: 2 },
            { date: '2000-03-22', termOccurrences: 2 },
            { date: '2000-03-23', termOccurrences: 1 },
            { date: '2000-03-24', termOccurrences: 4 },
            { date: '2000-03-25', termOccurrences: 5 },
            { date: '2000-03-26', termOccurrences: 5 },
            { date: '2000-03-27', termOccurrences: 1 },
            { date: '2000-03-28', termOccurrences: 2 },
            { date: '2000-03-29', termOccurrences: 5 },
            { date: '2000-03-30', termOccurrences: 6 },
            { date: '2000-04-01', termOccurrences: 1 },
            { date: '2000-04-02', termOccurrences: 3 },
            { date: '2000-04-03', termOccurrences: 5 },
            { date: '2000-04-04', termOccurrences: 7 },
            { date: '2000-04-05', termOccurrences: 5 },
            { date: '2000-04-06', termOccurrences: 3 },
            { date: '2000-04-07', termOccurrences: 7 },
            { date: '2000-04-08', termOccurrences: 0 },
            { date: '2000-04-09', termOccurrences: 1 },
            { date: '2000-04-10', termOccurrences: 4 },
            { date: '2000-04-11', termOccurrences: 3 },
            { date: '2000-04-12', termOccurrences: 3 },
            { date: '2000-04-13', termOccurrences: 1 },
            { date: '2000-04-14', termOccurrences: 3 },
            { date: '2000-04-15', termOccurrences: 7 },
            { date: '2000-04-16', termOccurrences: 8 },
            { date: '2000-04-17', termOccurrences: 8 },
            { date: '2000-04-18', termOccurrences: 8 },
            { date: '2000-04-19', termOccurrences: 9 },
            { date: '2000-04-20', termOccurrences: 4 },
            { date: '2000-04-21', termOccurrences: 2 },
            { date: '2000-04-22', termOccurrences: 2 },
            { date: '2000-04-23', termOccurrences: 1 },
            { date: '2000-04-24', termOccurrences: 4 },
            { date: '2000-04-25', termOccurrences: 5 },
            { date: '2000-04-26', termOccurrences: 5 },
            { date: '2000-04-27', termOccurrences: 1 },
            { date: '2000-04-28', termOccurrences: 2 },
            { date: '2000-04-29', termOccurrences: 5 },
            { date: '2000-04-30', termOccurrences: 6 },
    ];
    chartData.forEach(function (d, i) { d.date = dateFormat.parse(d.date); });

    termOccurencesService.initialize(chartData);
    rangeFilterService.initialize(chartData);
    rangeFilterService.onChange(onRangeFilterChange)

    function onRangeFilterChange() {
        var xRange = rangeFilterService.brushFunction.extent();
        termOccurencesService.updateRange(xRange);
    }
});

