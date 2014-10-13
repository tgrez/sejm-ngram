/// <reference path="../app.js" />
/// <reference path="../services/PhrasesService.js" />
/// <reference path="../services/TermOccurencesService.js" />
/// <reference path="../services/RangeFilterService.js" />

'use strict';

module.controller('ChartCtrl', function ($scope, $http, $window, $routeParams, $location, phrasesService, termOccurencesService, rangeFilterService) {
    $scope.phrasesService = {};
    $scope.search = {
        phrasesService: null,
        wasTriggered: false,
        isInProgress: false,
        run: null
    };
    $scope.mostPopularPhrases = {
        phrases: []
    };

    initialize();

    function initialize() {
        $scope.search.phrasesService = phrasesService;
        $scope.search.phrasesService.empty();
        $scope.search.wasTriggered = false;
        $scope.search.isInProgress = false;

        $scope.mostPopularPhrases.phrases = [
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
    }

    $scope.search.run = function () {
        $scope.search.wasTriggered = true;
        $scope.search.isInProgress = true;

        var phrases = phrasesService.getPhrases();

        d3.json("http://sejmotrendy.pl/service/api/ngramfts/" + phrases[0].text,
            function (error, data) {
                $scope.search.isInProgress = false;
                $scope.$apply();
                var chartData = data.partiesNgrams[0].listDates;
                var dateFormat = d3.time.format('%Y-%m-%d');
                chartData.forEach(function (d, i) { d.date = dateFormat.parse(d.date); });

                termOccurencesService.initialize(chartData);
                rangeFilterService.initialize(chartData);
                rangeFilterService.onChange(onRangeFilterChange);
                $location.hash($scope.search.phrasesService.exportToString());
            }
        );
    };
    $scope.onTermOccurencesChartResize = function () {
        termOccurencesService.updateSize();
    }
    $scope.onRangeFilterChartResize = function () {
        rangeFilterService.updateSize();
    }
    /*
    var chartData = [
            { date: '2000-01-01', count: 1 },
            { date: '2000-01-02', count: 3 },
            { date: '2000-01-03', count: 5 },
            { date: '2000-01-04', count: 7 },
            { date: '2000-01-05', count: 5 },
            { date: '2000-01-06', count: 3 },
            { date: '2000-01-07', count: 7 },
            { date: '2000-01-08', count: 0 },
            { date: '2000-01-09', count: 1 },
            { date: '2000-01-10', count: 4 },
            { date: '2000-01-11', count: 3 },
            { date: '2000-01-12', count: 3 },
            { date: '2000-01-13', count: 1 },
            { date: '2000-01-14', count: 3 },
            { date: '2000-01-15', count: 7 },
            { date: '2000-01-16', count: 8 },
            { date: '2000-01-17', count: 8 },
            { date: '2000-01-18', count: 8 },
            { date: '2000-01-19', count: 9 },
            { date: '2000-01-20', count: 4 },
            { date: '2000-01-21', count: 2 },
            { date: '2000-01-22', count: 2 },
            { date: '2000-01-23', count: 1 },
            { date: '2000-01-24', count: 4 },
            { date: '2000-01-25', count: 5 },
            { date: '2000-01-26', count: 5 },
            { date: '2000-01-27', count: 1 },
            { date: '2000-01-28', count: 2 },
            { date: '2000-01-29', count: 5 },
            { date: '2000-01-30', count: 6 },
            { date: '2000-01-31', count: 7 },
            { date: '2000-02-01', count: 2 },
            { date: '2000-02-02', count: 2 },
            { date: '2000-02-03', count: 0 },
            { date: '2000-02-04', count: 0 },
            { date: '2000-02-05', count: 0 },
            { date: '2000-02-06', count: 1 },
            { date: '2000-02-07', count: 1 },
            { date: '2000-02-08', count: 0 },
            { date: '2000-02-09', count: 1 },
            { date: '2000-02-10', count: 4 },
            { date: '2000-02-11', count: 3 },
            { date: '2000-02-12', count: 3 },
            { date: '2000-02-13', count: 1 },
            { date: '2000-02-14', count: 3 },
            { date: '2000-02-15', count: 7 },
            { date: '2000-02-16', count: 8 },
            { date: '2000-02-17', count: 8 },
            { date: '2000-02-18', count: 8 },
            { date: '2000-02-19', count: 9 },
            { date: '2000-02-20', count: 4 },
            { date: '2000-02-21', count: 2 },
            { date: '2000-02-22', count: 2 },
            { date: '2000-02-23', count: 1 },
            { date: '2000-02-24', count: 4 },
            { date: '2000-02-25', count: 5 },
            { date: '2000-02-26', count: 5 },
            { date: '2000-02-27', count: 1 },
            { date: '2000-02-28', count: 2 },
            { date: '2000-03-01', count: 1 },
            { date: '2000-03-02', count: 3 },
            { date: '2000-03-03', count: 5 },
            { date: '2000-03-04', count: 7 },
            { date: '2000-03-05', count: 5 },
            { date: '2000-03-06', count: 3 },
            { date: '2000-03-07', count: 7 },
            { date: '2000-03-08', count: 0 },
            { date: '2000-03-09', count: 1 },
            { date: '2000-03-10', count: 4 },
            { date: '2000-03-11', count: 3 },
            { date: '2000-03-12', count: 3 },
            { date: '2000-03-13', count: 1 },
            { date: '2000-03-14', count: 3 },
            { date: '2000-03-15', count: 7 },
            { date: '2000-03-16', count: 8 },
            { date: '2000-03-17', count: 8 },
            { date: '2000-03-18', count: 8 },
            { date: '2000-03-19', count: 9 },
            { date: '2000-03-20', count: 4 },
            { date: '2000-03-21', count: 2 },
            { date: '2000-03-22', count: 2 },
            { date: '2000-03-23', count: 1 },
            { date: '2000-03-24', count: 4 },
            { date: '2000-03-25', count: 5 },
            { date: '2000-03-26', count: 5 },
            { date: '2000-03-27', count: 1 },
            { date: '2000-03-28', count: 2 },
            { date: '2000-03-29', count: 5 },
            { date: '2000-03-30', count: 6 },
            { date: '2000-04-01', count: 1 },
            { date: '2000-04-02', count: 3 },
            { date: '2000-04-03', count: 5 },
            { date: '2000-04-04', count: 7 },
            { date: '2000-04-05', count: 5 },
            { date: '2000-04-06', count: 3 },
            { date: '2000-04-07', count: 7 },
            { date: '2000-04-08', count: 0 },
            { date: '2000-04-09', count: 1 },
            { date: '2000-04-10', count: 4 },
            { date: '2000-04-11', count: 3 },
            { date: '2000-04-12', count: 3 },
            { date: '2000-04-13', count: 1 },
            { date: '2000-04-14', count: 3 },
            { date: '2000-04-15', count: 7 },
            { date: '2000-04-16', count: 8 },
            { date: '2000-04-17', count: 8 },
            { date: '2000-04-18', count: 8 },
            { date: '2000-04-19', count: 9 },
            { date: '2000-04-20', count: 4 },
            { date: '2000-04-21', count: 2 },
            { date: '2000-04-22', count: 2 },
            { date: '2000-04-23', count: 1 },
            { date: '2000-04-24', count: 4 },
            { date: '2000-04-25', count: 5 },
            { date: '2000-04-26', count: 5 },
            { date: '2000-04-27', count: 1 },
            { date: '2000-04-28', count: 2 },
            { date: '2000-04-29', count: 5 },
            { date: '2000-04-30', count: 6 },
    ];
    chartData.forEach(function (d, i) { d.date = dateFormat.parse(d.date); });
    */

    function onRangeFilterChange() {
        var xRange = rangeFilterService.brushFunction.extent();
        termOccurencesService.updateRange(xRange);
    }

    var phrasesString = $location.hash();
    var isPhrasesStringEmpty = !phrasesString;
    if (!isPhrasesStringEmpty) {
        $scope.search.phrasesService.importFromString(phrasesString);
        $scope.search.run();
    }
});

