/// <reference path="../app.js" />
/// <reference path="../services/PhrasesService.js" />
/// <reference path="~/js/vendor/d3.min.js" />

module.controller('ChartCtrl', function ($scope, $http, $window, $routeParams, $location, phrasesService, apiFactory, graphDataFormatterFactory, frequentPhrases) {
  var colors = ['#f06292', '#4dd0e1', '#f5b916', '#9575cd', '#5479c5', '#64b5f6', '#4db690', '#9ec176', '#607d8b', '#ff8a65', '#ff8a65'];
  $scope.search = {
    // phrasesService holds the search phrases before the user pushes the search button
    phrasesService: phrasesService,
    wasTriggered: false,
    isInProgress: false,
    callsInProgressCount: 0,
    run: null
  };

  $scope.ALL_PARTIES_KEY = "all";

  $scope.graph = {
    phrasesOccurences: [],
    partiesNames: [],
    selectedParty: $scope.ALL_PARTIES_KEY,
    plotLines : [],
    xRange: [new Date(1999, 1, 1), new Date(2015, 1, 1)], // dummy initial, I don't want to deal with nulls
    yRange: [0, 1], // dummy initial, I don't want to deal with nulls
    selectedRange: [new Date(1999, 1, 1), new Date(2015, 1, 1)],
    isMultiPhrase: false
  }

	$scope.mostPopularPhrases = frequentPhrases;
    //calling
  apiFactory.getAllWordsPerDate().then(function(response) {
    $scope.nrAllWordsPerDates = response.data
  })

  // TODO: reset selectedParty on new search
  $scope.$watch('graph.selectedParty', function (newValue, oldValue) {
      if (newValue !== oldValue) {
          $scope.graph.plotLines.forEach(function (plotLine) {
              var foundParty = _.find(plotLine.parties, function (party) {return party.partyName == $scope.graph.selectedParty});
              if (foundParty)
                plotLine.occurences = foundParty.occurences;
              else
                plotLine.occurences = [];

          });
      }
  });
  function prepareForDisplay(phraseOccurances) {
    var plotLines = [];
    $scope.graph.partiesNames = [];
    $scope.graph.isMultiPhrase = phraseOccurances.length > 1;
    if (phraseOccurances.length == 1) { //one ngram, many parties
      plotLines = phraseOccurances[0].partiesOccurences.map(function (party, i) {
          return {
              label: party.partyName,
              color: colors[i % colors.length],
              occurences: party.occurences,
              isVisible: false
          }
      });
    } else if (phraseOccurances.length > 1){
        plotLines = phraseOccurances.map(function(phraseObj, i) {
            return {
                label: phraseObj.name,
                color: colors[i % colors.length],
                occurences: _.find(
                    phraseObj.partiesOccurences,
                    function (party) {return party.partyName == $scope.graph.selectedParty}).occurences,
                isVisible: false,
                parties: phraseObj.partiesOccurences
            };
        });
        function collectPartiesNames() {
          var parties = {};
          phraseOccurances.forEach(function (phrase) {
              phrase.partiesOccurences.forEach(function (party) {
                  parties[party.partyName] = true; // simulating adding a value to a Set
              })
          });
          return _.keys(parties);
        }
        $scope.graph.partiesNames = collectPartiesNames();
    }

    var maxY = 0;
    for (var i = 0; i < plotLines.length; i++) {
        var tempMaxY = d3.max(plotLines[i].occurences, function (o) { return o.count; });
        if (tempMaxY > maxY)
            maxY = tempMaxY;
    }
    function calcXRange(plotLines) {
      var minX = d3.min(plotLines, function (plotLine) {
          var frst = plotLine.occurences[0]; return frst ? frst.date : undefined });
      var maxX = d3.max(plotLines, function (plotLine) {
          var last = plotLine.occurences[plotLine.occurences.length-1]; return last ? last.date : undefined });
      return minX ? [minX, maxX] : [new Date(1991,1,1), new Date(2015, 11, 7)];
    }

    $scope.graph.xRange = calcXRange(plotLines);
    $scope.graph.selectedRange = $scope.graph.xRange;
    $scope.graph.yRange = [0, maxY];
    $scope.graph.plotLines = plotLines;
  }

  $scope.search.run = function () {
    $scope.search.wasTriggered = true;

    var phrases = phrasesService.phrases;
    $scope.search.callsInProgressCount += phrases.length;

    phrases.forEach(function (phrase, index) {
      $scope.search.callsInProgressCount--;
      if ($scope.graph.doesPhraseExist(phrase.text)) {
        $scope.search.phrasesService.removePhrase(phrase.text);
      } else {
        apiFactory.getGraphNgram(phrase.text).then(function (response) {
            /* This returns {name:String, partiesOccurences: [{partyName:String, occurances:[{date:String,count:Number}]}]} */
            var chartDataFormatted = graphDataFormatterFactory.formatNgram(response.data, $scope.ALL_PARTIES_KEY);

            $scope.graph.phrasesOccurences.push(chartDataFormatted);
            $scope.search.phrasesService.removePhrase(response.data.ngram);

            if ($scope.search.callsInProgressCount === 0) {
                prepareForDisplay($scope.graph.phrasesOccurences);
            }
        });
      }
    });
  };

  $scope.graph.removePhraseOccurences = function(name) {
    var remainingOccurences = _.filter($scope.graph.phrasesOccurences, function(d, i) { return name !== d.name; });
    $scope.graph.phrasesOccurences = remainingOccurences;
    prepareForDisplay($scope.graph.phrasesOccurences);
  };

  $scope.graph.doesPhraseExist = function(phrase) {
      var result = _.findWhere($scope.graph.phrasesOccurences, {name:phrase});
      console.log (phrase);
      console.log ($scope.graph.phrasesOccurences);
      console.log (result);
      return result !== undefined;
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
