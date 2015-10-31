module.factory('graphDataFormatterFactory', function () {
  var graphDataFormatterFactory = {};

  graphDataFormatterFactory.formatNgram = function (data, allPartiesKey) {
    var singleNgram = data.ngram;
    var dateFormat = d3.time.format('%Y-%m-%d');

    var partiesOccurences = [];
    var sumParties = {}

    data.partiesNgrams.forEach(function (party){
      var chartData = party.listDates;
      chartData = _.filter(chartData, function (dp) { return dp.count > 0 });
      chartData.forEach(function(datapoint) {
          datapoint.date = dateFormat.parse(datapoint.date);
          sumParties[datapoint.date] = sumParties[datapoint.date] || 0 + datapoint.count;
      });

      if (party.name == allPartiesKey){
        sumParties = chartData;
      } else {
        partiesOccurences.push({
          partyName: party.name,
          occurences: chartData
        });
      }
    });

    if (sumParties) {
      var arrayOccurences = [];
      _.mapObject(sumParties, function (count, date) {
          arrayOccurences.push({ 'date':  new Date(date), 'count': sumParties[date] });
      });
      arrayOccurences = _.sortBy(arrayOccurences, 'date');
      partiesOccurences.unshift({partyName: allPartiesKey, occurences: arrayOccurences});
    }

      
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
