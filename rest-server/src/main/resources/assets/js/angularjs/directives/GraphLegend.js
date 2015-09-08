/// <reference path="../app.js" />


'use strict';

module
.directive('stGraphLegend', function() {
    return {
       restrict: 'E',
       scope: {
                   partiesNames: '=ngModel',
                   checkboxClicked: '=',
                   graphDrawHelper: '='
       },
       link : function (scope, element, attrs) {
                   scope.$watch('partiesNames.length', onDataChange);

            function onDataChange(){
                console.log(scope.partiesNames[0])
            }
       },
       templateUrl: 'templates/legend.html',
    };
});