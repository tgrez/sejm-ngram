module.factory('apiFactory', ['$http', function($http) {
  var urlBase = 'http://localhost:8080/service/api';
  var USE_REAL = false
  var dataFactory = {};

  dataFactory.getPhrases = function () {
    return $http.get(urlBase + '/hitcount/top');
  };

  dataFactory.getGraphNgram = function (phrase) {
  	if (USE_REAL){
  		    return $http.get(urlBase + '/ngramfts/' + phrase);
	} else {
		return {"then": function(f){ f( {"data": getMockData()} ) }}	}
  };

  function getMockData(){
  	return   	{"ngram":"aborcja",
    "partiesNgrams":[
    	{"name":"all","listDates":[
    		{"date":"1991-11-25","count":10},
    		{"date":"1991-11-26","count":20},
    		{"date":"1991-12-17","count":30},
    		{"date":"1991-12-18","count":40},
    		{"date":"1991-12-21","count":50},
    		{"date":"1991-12-23","count":60},
    		{"date":"1992-01-03","count":70},
    		{"date":"1992-01-04","count":60},
    		{"date":"1992-01-23","count":50},
    		{"date":"1992-01-24","count":40},
    		{"date":"1992-01-25","count":30},
    		{"date":"1992-01-30","count":20},
    		{"date":"1992-01-31","count":10},
    		{"date":"1992-02-01","count":10},
    		{"date":"1992-02-13","count":10},
    		{"date":"1992-02-14","count":0}
    		]
    	},
    	   {"name":"superPartia","listDates":[
    		{"date":"1991-11-25","count":110},
    		{"date":"1991-11-26","count":120},
    		{"date":"1991-12-17","count":130},
    		{"date":"1991-12-18","count":140},
    		{"date":"1991-12-21","count":150},
    		{"date":"1991-12-23","count":160},
    		{"date":"1992-01-03","count":170},
    		{"date":"1992-01-04","count":160},
    		{"date":"1992-01-23","count":150},
    		{"date":"1992-01-24","count":140},
    		{"date":"1992-01-25","count":130},
    		{"date":"1992-01-30","count":120},
    		{"date":"1992-01-31","count":110},
    		{"date":"1992-02-01","count":110},
    		{"date":"1992-02-13","count":110},
    		{"date":"1992-02-14","count":10}
    		]
    	}
    	]
    }
  }

    dataFactory.getMockGraphNgram = function (phrase) {

  };

  return dataFactory;
}]);
