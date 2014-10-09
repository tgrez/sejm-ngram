/// <reference path="../app.js" />

'use strict';

module.controller('IndexCtrl', function ($scope, $route, $location, $routeParams) {
    $scope.$route = $route;
    $scope.$location = $location;
    $scope.$routeParams = $routeParams;
});
