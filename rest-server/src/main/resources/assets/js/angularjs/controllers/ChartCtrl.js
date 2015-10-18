/// <reference path="../app.js" />
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
    linesColors: colors,
    checkboxClicked: null,
    plotLines : []
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
    if (phraseOccurances.length == 1) { //one ngram, many parties
      $scope.graph.plotLines = phraseOccurances[0].partiesOccurences.map(function (party, i) {
          return {
              label: party.partyName,
              color: colors[i % colors.length],
              occurances: party.occurances
          }
      });
    } else {
      console.log("One search phrase supported only right now");
    }
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

        // this returns [{partyName:String, isVisible:Bool}]
        var partiesNames = chartDataFormatted.partiesOccurences.map(graphDataFormatterFactory.formatPartiesName);

        // TODO this overwrites whatever parties we had for other phrases
        $scope.graph.partiesNames = partiesNames;

        // TODO this will be made redundant by using ng-repeat
        $scope.graph.partiesNames.getId = function (partyName) {
          return _.findIndex($scope.graph.partiesNames, { partyName: partyName });
        }

        $scope.graph.phrasesOccurences.push(chartDataFormatted);

        // TODO: I don't understand phrasesService
        $scope.search.phrasesService.removePhrase(response.data.ngram);
          
        if ($scope.search.callsInProgressCount === 0) {
            prepareForDisplay($scope.graph.phrasesOccurences);
        }

        // TODO: is this necessary?
        $scope.$apply();
      });
    });
  };

  $scope.graph.removePhraseOccurences = function(name) {
    var remainingOccurences = _.filter($scope.graph.phrasesOccurences, function(d, i) { return name !== d.name; });
    $scope.graph.phrasesOccurences = remainingOccurences;
  };

  $scope.graph.graphDrawHelper = {
    setLineVisibility: function(line_prefix){
      _.each($scope.graph.partiesNames, function(object, key){
        d3.select("#" + $scope.graph.graphDrawHelper.generateLineId(line_prefix, object.partyName) )
        .style('visibility', object.isVisible ? 'visible' : 'hidden')
      })
    },
    generateLineId:     function(prefix, term) {
      return prefix + '-' + $scope.graph.partiesNames.getId(term);
    },
    generateLineColorForPartyName: function(partyName){
      var linesColor = $scope.graph.linesColors;
      var partyId = $scope.graph.partiesNames.getId(partyName);
      return linesColor[ partyId % linesColor.length]
    },
    removeObsolateLines: function (linesCanvas, termsOccurences, line_prefix) {
      var lines = linesCanvas.selectAll('.line');

      lines.each(function () {
        var line = this;
        var lineId = line.id;
        var isTermExist = _.any(termsOccurences, function (o, i) {
          var term = o.lineName;
          var termId = $scope.graph.graphDrawHelper.generateLineId(line_prefix, term);
          return termId === lineId;
        });

        if (!isTermExist) {
          line.remove();
        }
      });
    },
    calculateMultiLineData: function(scopeTermOccurences){
      var multiLineData = []
      if (scopeTermOccurences.length == 1){ //one ngram, many parties
        for (var i = 0; i < scopeTermOccurences[0].partiesOccurences.length; i++) {
          multiLineData.push({
            lineName: scopeTermOccurences[0].partiesOccurences[i].partyName,
            occurences: scopeTermOccurences[0].partiesOccurences[i].occurences
          })
        }
      }
      return multiLineData
    },
    getPartyColorStyle: function(partyName){
      var color = this.generateLineColorForPartyName(partyName);
      var returnS = '{"background-color":' + '"' + color +'"}';
      return returnS;
    }
  }

  // TODO: replace with ng-repeat
  $scope.graph.checkboxClicked = function(checked, partyName){
    var partyIndex = $scope.graph.partiesNames.getId(partyName)
    $scope.graph.partiesNames[partyIndex].isVisible = checked;
  }

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

