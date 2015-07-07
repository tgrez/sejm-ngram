'use strict';

var module = angular.module('sejmotrendyApp', ['ngTagsInput', 'ngRoute', 'ngAnimate'])
    .config(function ($routeProvider, $locationProvider) {
        $routeProvider
            .when('/chart', {
                templateUrl: '/templates/chart.html',
                controller: 'ChartCtrl',
                reloadOnSearch: false,
                resolve: {
                    frequentPhrases: ['$http', function($http) {
                        return $http.get("/service/api/hitcount/top").then(function(response){
                           return response.data;
                        });
                    }]
                }
            })
            .when('/chart#:phrasesString', {
                templateUrl: '/templates/chart.html',
                controller: 'ChartCtrl'
            })
            .when('/howitworks', {
                templateUrl: '/templates/howitworks.html'
            })
            .when('/about', {
                templateUrl: '/templates/about.html'
            })
            .when('/support', {
                templateUrl: '/templates/support.html'
            })
            .otherwise({
                redirectTo: '/chart'
            })
    })
    .config(function (tagsInputConfigProvider) {
        tagsInputConfigProvider.setDefaults('tagsInput', {
            placeholder: ''
        });
    });