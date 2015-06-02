﻿/// <reference path="../app.js" />
/// <reference path="../services/PhrasesService.js" />
/// <reference path="~/js/vendor/d3.min.js" />
'use strict';

module.controller('ChartCtrl', function ($scope, $http, $window, $routeParams, $location, phrasesService) {
    $scope.search = {
        phrasesService: phrasesService,
        wasTriggered: false,
        isInProgress: false,
        callsInProgressCount: 0,
        run: null
    };
    $scope.graph = {
        phrasesOccurences: [],
        partiesNames: [],
        getIdFromPartyName: null,
        selectedRange: null,
        linesColors: ['#f06292', '#4dd0e1', '#f5b916', '#9575cd', '#5479c5', '#64b5f6', '#4db690', '#9ec176', '#607d8b', '#ff8a65', '#ff8a65'],
        checkboxClicked: null
    }
    $scope.mostPopularPhrases = {
        phrases: [
            'aborcja',
            'recesja',
            'sejm',
            'deficyt',
            'rosja',
            'putin',
            'posel'
        ]
    };

    $scope.search.run = function () {
        $scope.search.wasTriggered = true;

        var phrases = phrasesService.getPhrases();

        for (var i = 0; i < phrases.length; i++) {
            $scope.search.callsInProgressCount++;

//            d3.json("service/api/ngramfts/" + phrases[i].text,
            d3.json("http://localhost:8080/service/api/ngramfts/" + phrases[i].text,
                function (error, data) {
                    $scope.search.callsInProgressCount--;

                    var phraseName = data.ngram;

                    var dateFormat = d3.time.format('%Y-%m-%d');

                    var partiesOccurences = []
                    for(var i = 0; i < data.partiesNgrams.length; i++){
                        var chartData = data.partiesNgrams[i].listDates;
                        chartData.forEach(function(d, i) { d.date = dateFormat.parse(d.date); });
                        partiesOccurences.push({
                            partyName: data.partiesNgrams[i].name,
                            occurences: chartData
                        })
                    }

                    var chartDataFormatted = {
                        name: phraseName,
                        partiesOccurences: partiesOccurences
                    };

                    var partiesNames = _.map(data.partiesNgrams, function(partyNgram){ return partyNgram.name});
                    console.log(partiesNames)

                   $scope.graph.partiesNames = partiesNames;
                   $scope.graph.partiesNames.getId = function(partyName){
                        return $scope.graph.partiesNames.indexOf(partyName)
                    }
                    $scope.graph.phrasesOccurences.push(chartDataFormatted);
                    $scope.search.phrasesService.removePhrase(phraseName);
                    $scope.$apply();
                });
        }
    };
    $scope.graph.removePhraseOccurences = function(name) {
        var remainingOccurences = _.filter($scope.graph.phrasesOccurences, function(d, i) { return name !== d.name; });
        $scope.graph.phrasesOccurences = remainingOccurences;
    };

    $scope.graph.checkboxClicked = function(){
        console.log('CheckboxCLicked!')
    }
    $scope.$watch('search.callsInProgressCount', function (newValue, oldValue) {
        var isNewValueEmpty = typeof newValue === 'undefined' || newValue === null;
        if (!isNewValueEmpty)
            $scope.search.isInProgress = newValue > 0;
    });

    var phrasesString = $location.hash();
    var isPhrasesStringEmpty = !phrasesString;
    if (!isPhrasesStringEmpty) {
        $scope.search.phrasesService.importFromString(phrasesString);
        $scope.search.run();
    }
});

