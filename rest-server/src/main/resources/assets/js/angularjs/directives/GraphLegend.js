/// <reference path="../app.js" />


'use strict';

module.directive('stGraphLegend', function() {
    return {
        restrict: 'E',

       template:
            '<svg width="200" height="400">' +
                '</svg>',
       replace: true
    };
});