'use strict';

var module = angular.module('sejmotrendyApp', ['ngTagsInput', 'ngRoute', 'ngAnimate'])
    .config(function ($routeProvider, $locationProvider) {
        $routeProvider
            .when('/chart', {
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