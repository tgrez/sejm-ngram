module.factory('apiFactory', ['$http', function($http) {
  var urlBase = 'http://localhost:8080/service/api';
  var dataFactory = {};

  dataFactory.getPhrases = function () {
    return $http.get(urlBase + '/hitcount/top');
  };

  dataFactory.getGraphNgram = function (phrase) {
    return $http.get(urlBase + '/ngramfts/' + phrase);
  };

  return dataFactory;
}]);
