module.factory('apiFactory', function( $http, apiMockFactory ) {
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
  	return  apiMockFactory.getMockData()
  }

  return dataFactory;
});
