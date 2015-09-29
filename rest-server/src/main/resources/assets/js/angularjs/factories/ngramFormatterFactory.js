module.factory('ngramFormatterFactory', function($http) {
  var graphDataFormatterFactory = {};

  graphDataFormatterFactory.formatNgram = function (data) {
    var singleNgram = data.ngram;
    var dateFormat = d3.time.format('%Y-%m-%d');

    var partiesOccurences = [];
    var sumParties = null;

    data.partiesNgrams.forEach(function (ngram, index){
      var chartData = ngram.listDates;
      chartData.forEach(function(data) { data.date = dateFormat.parse(data.date); });

      var partyName = ngram.name;

      if (partyName == $scope.ALL_PARTIES_KEY){
        sumParties = chartData;
      } else {
        partiesOccurences.push({
          partyName: partyName,
          occurences: chartData
        });
      }
    });

    partiesOccurences.unshift({partyName: $scope.ALL_PARTIES_KEY, occurences: sumParties});

    return {
      name: singleNgram,
      partiesOccurences: partiesOccurences
    };
  };

  graphDataFormatterFactory.formatPartiesName = function(partiesOccurence) {
    return {"partyName" : partiesOccurence.partyName, "isVisible" : false }
  };

  return graphDataFormatterFactory;
});
