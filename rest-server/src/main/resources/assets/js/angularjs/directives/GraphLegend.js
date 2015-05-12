/// <reference path="../app.js" />


'use strict';

module
.controller('LegendController', ['$scope', function($scope) {
  $scope.phones = [
    {'name': 'Nexus S',
     'snippet': 'Fast just got faster with Nexus S.'},
    {'name': 'Motorola XOOM™ with Wi-Fi',
     'snippet': 'The Next, Next Generation tablet.'},
    {'name': 'MOTOROLA XOOM™',
     'snippet': 'The Next, Next Generation tablet.'}
  ];
}])
.directive('stGraphLegend', function() {
    return {
       restrict: 'E',
       templateUrl: 'templates/legend.html',
       replace: true
    };
});