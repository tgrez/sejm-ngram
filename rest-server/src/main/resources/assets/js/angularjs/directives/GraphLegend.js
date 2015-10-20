/// <reference path="../app.js" />


'use strict';

module
.directive('stGraphLegend', function() {
    return {
       restrict: 'E',
       scope: {
           plotLines: '=',
       },
       link : function (scope, element, attrs) {
           // nothing to do here, do we need this directive? We should probably just inline the template
           return;
       },
       templateUrl: 'templates/legend.html',
    };
});
