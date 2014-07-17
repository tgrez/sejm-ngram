/// <reference path="../app.js" />

'use strict';

module.controller('ChartCtrl', function ($scope, $http) {
    $scope.wasSearchTriggered = false;
    $scope.isSearchInProgres = false;
    $scope.tagsWrapper = new TagsWrapper();

    $scope.mostPopularTags = [
        'aborcja',
        'Tusk',
        'Unia Europejska',
        'deficyt',
        'Rosja',
        'Putin',
        'Platforma Obywatelska',
        'PIS',
        'TVN'
    ];

    $scope.search = function () {
        $scope.wasSearchTriggered = true;
        $scope.isSearchInProgress = true;
    }
});

var TagsWrapper = function () {
    this.tags = [];
};

TagsWrapper.prototype.addTag = function (name) {
    var formatedTag = this.prepareTag.call(this, name);

    if (!this.hasTag.call(this, name))
        this.tags.push(formatedTag);
};

TagsWrapper.prototype.prepareTag = function (tagName) {
    return { text: tagName };
};

TagsWrapper.prototype.hasTag = function (tagName) {
    return _.any(this.tags, hasIdenticalName);

    function hasIdenticalName(tag) {
        return tag.text === tagName;
    }
};

TagsWrapper.prototype.getTags = function () {
    return this.tags;
}
