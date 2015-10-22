module.factory('graphDataFormatterFactory', function () {
  var graphDataFormatterFactory = {};

  graphDataFormatterFactory.formatNgram = function (data, allPartiesKey) {
    var singleNgram = data.ngram;
    var dateFormat = d3.time.format('%Y-%m-%d');

    var partiesOccurences = [];
    var sumParties = null;

    data.partiesNgrams.forEach(function (party){
      var chartData = party.listDates;
      chartData.forEach(function(datapoint) { datapoint.date = dateFormat.parse(datapoint.date); });

      if (party.name == allPartiesKey){
        sumParties = chartData;
      } else {
        partiesOccurences.push({
          partyName: party.name,
          occurences: chartData
        });
      }
    });

    if (sumParties)
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
