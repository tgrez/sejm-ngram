module.factory('apiFactory', function( $http, apiMockFactory ) {
  var urlBase = '/service/api';
  var USE_REAL = true
  var dataFactory = {};

  dataFactory.getPhrases = function () {
    return $http.get(urlBase + '/hitcount/top');
  };

  dataFactory.getGraphNgram = function (phrase) {
  	if (USE_REAL){
  		    return $http.get(urlBase + '/ngram/' + phrase);
	} else {
  	    return { "then": function (f) { f({ "data": getMockData(phrase) }) } }
  	}
  };

  function getMockData(phrase) {
  	return  apiMockFactory.getMockData(phrase)
  }

  return dataFactory;
});
