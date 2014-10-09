/// <reference path="../app.js" />
/// <reference path="../controllers/ChartCtrl.js" />

'use strict';

module.directive('TermOccurencesChart', function () {
    return {
        restrict: 'E',
        scope: {
            chartData: '=',
            chartExtent: '='
        },
        compile: function compile(tElement, tAttrs, transclude) {

        },
        link: function link(scope, iElement, iAttrs, controller, transcludeFn) {
        }
    };
    


});
