/// <reference path="../app.js" />
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
        sumPartiesOccurences: null,
        partiesNames: [],
        graphDrawHelper: null,
        getIdFromPartyName: null,
        selectedRange: null,
        partiesVisibility: {},
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

                    var chartDataFormatted = formatSingleNgramResponse(data, phraseName)
                    var partiesNames = _.map(chartDataFormatted.partiesOccurences, function(partiesOccurence){ return partiesOccurence.partyName });

                    $scope.graph.partiesNames = partiesNames;
                    $scope.graph.partiesNames.getId = function(partyName){
                        return $scope.graph.partiesNames.indexOf(partyName)
                    }

                    _.each(partiesNames, function(partyName){
                        $scope.graph.partiesVisibility[partyName] = false
                    });

                    console.log($scope.graph.partiesVisibility);

                    $scope.graph.phrasesOccurences.push(chartDataFormatted);
                    $scope.search.phrasesService.removePhrase(phraseName);
                    $scope.$apply();
                });
        }
    };

    function formatSingleNgramResponse(data, singleNgram){
        var ALL_PARTIES_KEY = "all";
        var dateFormat = d3.time.format('%Y-%m-%d');

        var partiesOccurences = [];
        var sumParties = null;

        for(var i = 0; i < data.partiesNgrams.length; i++){
            var chartData = data.partiesNgrams[i].listDates;
            chartData.forEach(function(d, i) { d.date = dateFormat.parse(d.date); });

            var partyName = data.partiesNgrams[i].name;

            if (partyName == ALL_PARTIES_KEY){
                sumParties = chartData;
            }

            if (partyName != "" && partyName != null && partyName != ALL_PARTIES_KEY){
                 partiesOccurences.push({
                        partyName: data.partiesNgrams[i].name,
                        occurences: chartData
                    })
            }
        }

        partiesOccurences.unshift({partyName: ALL_PARTIES_KEY, occurences: sumParties});

        var chartDataFormatted = {
            name: singleNgram,
            partiesOccurences: partiesOccurences
        };
        return chartDataFormatted;
    }

    $scope.graph.removePhraseOccurences = function(name) {
        var remainingOccurences = _.filter($scope.graph.phrasesOccurences, function(d, i) { return name !== d.name; });
        $scope.graph.phrasesOccurences = remainingOccurences;
    };

    $scope.graph.graphDrawHelper = {
        setLineVisibility: function(line_prefix){
           _.each($scope.graph.partiesVisibility, function(num, key){
                d3.select("#" + $scope.graph.graphDrawHelper.generateLineId(line_prefix, key) )
                  .style('visibility', num ? 'visible' : 'hidden')
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

    $scope.graph.checkboxClicked = function(checked, partyName){
        $scope.graph.partiesVisibility[partyName] = checked;
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

