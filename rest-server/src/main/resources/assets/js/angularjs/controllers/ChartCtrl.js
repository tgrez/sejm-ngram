﻿/// <reference path="../app.js" />
/// <reference path="../services/PhrasesService.js" />
/// <reference path="~/js/vendor/d3.min.js" />
'use strict';

module.controller('ChartCtrl', function ($scope, $http, $window, $routeParams, $location, phrasesService, apiFactory, graphDataFormatterFactory) {
  var colors = ['#f06292', '#4dd0e1', '#f5b916', '#9575cd', '#5479c5', '#64b5f6', '#4db690', '#9ec176', '#607d8b', '#ff8a65', '#ff8a65'];
  $scope.search = {
    phrasesService: phrasesService,
    wasTriggered: false,
    isInProgress: false,
    callsInProgressCount: 0,
    run: null
  };

  $scope.ALL_PARTIES_KEY = "all";

  $scope.graph = {
    phrasesOccurences: [],
    sumPartiesOccurences: null,
    partiesNames: [],
    graphDrawHelper: null,
    getIdFromPartyName: null,
    selectedRange: null,
    checkboxClicked: null,
    plotLines : [],
    xRange: [new Date(1999, 1, 1), new Date(2015, 1, 1)], // dummy initial, I don't want to deal with nulls
    yRange: [0, 1], // dummy initial, I don't want to deal with nulls
    selectedRange: [new Date(1999, 1, 1), new Date(2015, 1, 1)]
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
    
  function prepareForDisplay(phraseOccurances) {
    var plotLines = [];
    if (phraseOccurances.length == 1) { //one ngram, many parties
      plotLines = phraseOccurances[0].partiesOccurences.map(function (party, i) {
          return {
              label: party.partyName,
              color: colors[i % colors.length],
              occurences: party.occurences,
              isVisible: false
          }
      });
    } else {
      console.log("One search phrase supported only right now");
      return;
    }

    var minY = 0;
    var maxY = 0;
    for (var i = 0; i < plotLines.length; i++) {
        var tempMaxY = d3.max(plotLines[i].occurences, function (o) { return o.count; });
        if (tempMaxY > maxY)
            maxY = tempMaxY;
    }

    // TODO can we rely on the fact that all parties have the same dates?
    $scope.graph.xRange = [plotLines[0].occurences[0].date,
                  plotLines[0].occurences[plotLines[0].occurences.length - 1].date];
    $scope.graph.selectedRange = $scope.graph.xRange;
    $scope.graph.yRange = [minY, maxY];
    $scope.graph.plotLines = plotLines;
  }

  $scope.search.run = function () {
    $scope.search.wasTriggered = true;

    var phrases = phrasesService.phrases;

    phrases.forEach(function (phrase, index) {
      $scope.search.callsInProgressCount++;

      apiFactory.getGraphNgram(phrase.text).then(function (response) {
        $scope.search.callsInProgressCount--;

        /* This returns {name:String, partiesOccurences: [{partyName:String, occurances:[{date:String,count:Number}]}]} */
        var chartDataFormatted = graphDataFormatterFactory.formatNgram(response.data, $scope.ALL_PARTIES_KEY);

        $scope.graph.phrasesOccurences.push(chartDataFormatted);

        // TODO: I don't understand phrasesService
        $scope.search.phrasesService.removePhrase(response.data.ngram);
          
        if ($scope.search.callsInProgressCount === 0) {
            prepareForDisplay($scope.graph.phrasesOccurences);
        }

      });
    });
  };

  $scope.graph.removePhraseOccurences = function(name) {
    var remainingOccurences = _.filter($scope.graph.phrasesOccurences, function(d, i) { return name !== d.name; });
    $scope.graph.phrasesOccurences = remainingOccurences;
  };

  $scope.$watch('search.callsInProgressCount', function (newValue, oldValue) {
    var isNewValueEmpty = typeof newValue === 'undefined' || newValue === null;
    if (!isNewValueEmpty)
      $scope.search.isInProgress = newValue > 0;
  });

  // TODO: what is this doing? supporting linking to phrase searches?
  var phrasesString = $location.hash();
  var isPhrasesStringEmpty = !phrasesString;
  if (!isPhrasesStringEmpty) {
    $scope.search.phrasesService.importFromString(phrasesString);
    $scope.search.run();
  }
});

