module.factory('dataPointCalcFactory', function( ) {
  var dataPointCalcFactory = {};


  dataPointCalcFactory.getAggFun = function (minDate, maxDate, dayDelta) {

      var aggFun = function (o) {
              return moment(o.date).date(15).toDate();// middle of the month
          }
      aggFun.datemode = "middle_month"
      var window = 200;
     /* if (dayDelta < window){
        aggFun = function (o) { return o.date; }
        aggFun.datemode = "exact_day"
      }

      else */
        if (dayDelta < window * 13){
          aggFun = function (o) {
              return moment(o.date).day(3).toDate(); // get the wednesday of the week
          }
          aggFun.datemode = "wednesday_of_week"
      }
      return aggFun;
  }

  dataPointCalcFactory.getSummaryOfOccurencesForAll = function(dateFrom, dateTo, nrAllWordsPerDates ){
    var date = moment(dateFrom)
    var summary = 0
    while(date <= dateTo){
      var dateFormattedAsKey = moment(date).format("YYYY-MM-DD")
      if(nrAllWordsPerDates[dateFormattedAsKey]){ summary += nrAllWordsPerDates[dateFormattedAsKey]}
      date.add(1, 'days')
    }
    return summary
  }

  return dataPointCalcFactory;
}

);
