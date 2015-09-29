module.factory('apiFactory', ['$http', function($http) {
  var urlBase = '/service/api';
  var dataFactory = {};

  dataFactory.getPhrases = function () {
    return $http.get(urlBase + '/hitcount/top');
  };

  dataFactory.getGraphNgram = function (phrase) {
    return $http.get(urlBase + '/ngram/' + phrase);
  };

  return dataFactory;
}]);
