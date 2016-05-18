module.factory('dataPointCalcFactory', function( ) {
  var dataFactory = {};


  dataFactory.getAggFun = function (minDate, maxDate, dayDelta) {

      var aggFun = function (o) {
              return moment(o.date).date(15).toDate();// middle of the month
          }
      aggFun.datemode = "middle_month"
      var window = 200;
      if (dayDelta < window){
        aggFun = function (o) { return o.date; }
        aggFun.datemode = "exact_day"
      }

      else if (dayDelta < window * 13){
          aggFun = function (o) {
              return moment(o.date).day(3).toDate(); // get the wednesday of the week
          }
          aggFun.datemode = "wednesday_of_week"
      }
      return aggFun;
  }

  dataFactory.testFactory = function () {
    console.log("Hello from test factory")
  };
  return dataFactory;
});
