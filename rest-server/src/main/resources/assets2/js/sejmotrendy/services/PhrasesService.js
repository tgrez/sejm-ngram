/// <reference path="../app.js" />
/// <reference path="../controllers/ChartCtrl.js" />

'use strict';

module.service('phrasesService', function () {
    this.phrases = [];

    this.addPhrase = function(name) {
        var formatedPhrase = this.preparePhrase.call(this, name);

        if (!this.hasPhrase.call(this, name))
            this.phrases.push(formatedPhrase);
    };
    
    this.preparePhrase = function(phraseName) {
        return { text: phraseName };
    };

    this.hasPhrase = function(phraseName) {
        return _.any(this.phrases, hasIdenticalName);

        function hasIdenticalName(phrase) {
            return phrase.text === phraseName;
        }
    };

    this.getPhrases = function() {
        return this.phrases;
    };

    this.addPhrasesFromString = function(phrasesString) {
        var rawPhrases = phrasesString.split(',');
        var phrases = _.map(rawPhrases, function (rp) { return rp.trim(); });

        _.each(phrases, function (p) {
            this.addPhrase.call(this, p);
        }, this);
    };
})
