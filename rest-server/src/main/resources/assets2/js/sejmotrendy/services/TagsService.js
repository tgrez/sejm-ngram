/// <reference path="../app.js" />
/// <reference path="../controllers/ChartCtrl.js" />

'use strict';

module.service('tagsService', function () {
    this.tags = [];

    this.addTag = function (name) {
        var formatedTag = this.prepareTag.call(this, name);

        if (!this.hasTag.call(this, name))
            this.tags.push(formatedTag);
    };

    this.prepareTag = function (tagName) {
        return { text: tagName };
    };

    this.hasTag = function (tagName) {
        return _.any(this.tags, hasIdenticalName);

        function hasIdenticalName(tag) {
            return tag.text === tagName;
        }
    };

    this.getTags = function () {
        return this.tags;
    }
})
