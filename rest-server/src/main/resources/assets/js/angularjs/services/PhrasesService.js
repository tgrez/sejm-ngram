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

  this.removePhrase = function (name) {
    this.phrases = _.without(this.phrases, _.findWhere(this.phrases, { text: name }));
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

  this.importFromString = function(phrasesString) {
    var rawPhrases = phrasesString.replace(/\+/g, ' ').split(',');
    var phrases = _.map(rawPhrases, function (rp) { return rp.trim(); });

    _.each(phrases, function (p) {
      this.addPhrase.call(this, p);
    }, this);
  };

  this.exportToString = function () {
    var rawPhrases = _.map(this.phrases, function (d, i) { return d.text; });
    var phrasesCsv = rawPhrases.join(',').replace(/\s/g, '+');

    return phrasesCsv;
  };

  this.empty = function () {
    this.phrases = [];
  };
})
