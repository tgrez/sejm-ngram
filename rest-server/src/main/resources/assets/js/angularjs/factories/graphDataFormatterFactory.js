module.factory('graphDataFormatterFactory', function () {
  var graphDataFormatterFactory = {};

  graphDataFormatterFactory.formatNgram = function (data, allPartiesKey) {
    var singleNgram = data.ngram;
    var dateFormat = d3.time.format('%Y-%m-%d');

    var partiesOccurences = [];
    var sumParties = null;

    data.partiesNgrams.forEach(function (ngram, index){
      var chartData = ngram.listDates;
      chartData.forEach(function(data) { data.date = dateFormat.parse(data.date); });

      var partyName = ngram.name;

      if (partyName == allPartiesKey){
        sumParties = chartData;
      } else {
        partiesOccurences.push({
          partyName: partyName,
          occurences: chartData
        });
      }
    });

    partiesOccurences.unshift({partyName: allPartiesKey, occurences: sumParties});

    return {
      name: singleNgram,
      partiesOccurences: partiesOccurences
    };
  };

  graphDataFormatterFactory.formatPartiesName = function (partiesOccurence) {
    return {"partyName" : partiesOccurence.partyName, "isVisible" : false }
  };

  return graphDataFormatterFactory;
});
