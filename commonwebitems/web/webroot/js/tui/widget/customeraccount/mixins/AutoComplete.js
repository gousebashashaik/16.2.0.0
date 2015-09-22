define('tui/widget/mixins/AutoComplete', [
  'dojo',
  'dojo/dom-class',
  'dojo/dom-construct',
  'dojo/_base/lang',
  'dojo/on',
  'dojo/Stateful',
  'dojo/_base/declare',
  'dijit/_Widget'], function(dojo, domClass, domConstruct, lang, on) {

  function Results(dom, template, inputElement) {
    this.dom = dom;
    this.template = template;
    this.inputElement = inputElement;
    return this;
  }

  Results.prototype.clear = function() {
    domConstruct.empty(this.dom);
    this.hide();
    return this;
  };

  Results.prototype.hide = function() {
    domClass.add(this.dom, 'hide');
    return this;
  };

  Results.prototype.add = function(result) {
    var results = this;
    result['displayText'] = _.highlightWords(result['name'], result['match']);
    var resultNode = domConstruct.place(lang.replace(results.template, result), results.dom, 'last');
    dojo.connect(resultNode, 'onclick', function() {
      results.inputElement.value = result['name'];
      window.location.href = result['url'];
      results.clear();
    });
  };

  Results.prototype.set = function(response) {
    var results = this;
    _.when(!_.isEmpty(response), function() {
      domClass.remove(results.dom, 'hide');
      _.each(response, results.add.bind(results));
    });
  };

  dojo.declare('tui.widget.mixins.AutoComplete', [], {

    resultsTemplate: null,

    search: function(searchKey) {

    },

    postCreate: function() {
      var widget = this;
      var input = dojo.query('input', widget.domNode)[0];
      var resultsContainer = dojo.query('.auto-complete', widget.domNode)[0];
      var results = new Results(resultsContainer, widget.resultsTemplate, input).hide();
      var eraseContainer = dojo.query('.erase', widget.domNode)[0];


      function handleInput(value, validfn, invalidfn) {
          //!_.isEmpty(value) && /[^a-zA-Z0-9!@#$%^&*()_+\-=\[\]{};':"\\|,.<>?~`/\u00A3\u20AC\xA5\u2022\/\\ ]/.test(value) ? invalidfn() : validfn();
          !_.isEmpty(value) && /[^a-zA-Z0-9!@#$%^&*()_+\-=\[\]{};':"\\|,.<>?~` ]/.test(value) ? invalidfn() : validfn();
      }

      function validInputHandler() {
        domClass.remove(widget.domNode, 'error');
        _.when(input.value.length > 2, function() {
          results.set(widget.search(input.value));
        });
      }

      function invalidInputHandler() {
        domClass.add(widget.domNode, 'error');
      }

      on(widget.domNode, 'keyup', function(e) {
        results.clear();
        handleInput(input.value, validInputHandler, invalidInputHandler);
      });


      on(dojo.query('body')[0], 'click', function(e) {
        var parentClicked = _.reduce([input, resultsContainer], function(parentClicked, element) {
          return parentClicked && e.target != element;
        }, true);
        parentClicked ? results.hide() : null;
      });

      if (eraseContainer) {
            	on(dojo.query('.erase', widget.domNode)[0], 'click', function() {
          input.value = '';
          domClass.remove(widget.domNode, 'error');
          results.clear();
        });
      }
    }

  });
});


