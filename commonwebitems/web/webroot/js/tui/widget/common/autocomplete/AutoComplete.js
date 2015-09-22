define('tui/widget/common/autocomplete/AutoComplete', [
  'dojo',
  'dojo/dom-class',
  'dojo/dom-construct',
  'dojo/_base/lang',
  'dojo/on',
  'tui/widget/common/autocomplete/Results',
  'tui/widget/common/autocomplete/Summary',
  'tui/widget/common/autocomplete/Input',
  'tui/widget/common/autocomplete/Editor',
  'tui/widget/common/autocomplete/AddButton',
  'dojo/_base/window',
  'dojo/Stateful',
  'dojo/_base/declare',
  'dijit/_Widget'
], function(dojo, domClass, domConstruct, lang, on, Results, Summary, Input, Editor, AddButton, win) {

  dojo.declare('tui.widget.mixins.AutoComplete', [], {

    resultsTemplate: null,

    search: function(searchKey) {
      var widget = this;
      return _.when(searchKey.length > 2, function() {
        return widget.entityService.search(searchKey);
      });
    },

    onSelect: function() {
      var widget = this;
      widget.input.clear();
      widget.results.clear();
    },

    onRemove: function(key) {
      var widget = this;
      var entityStore = widget.entityStore;
      entityStore.remove(key);
      widget.summary.update(entityStore.summary());
    },

    onSummaryClick: function() {
      var widget = this;
      widget.editor.show();
    },

    onInputClick: function() {
      var widget = this;
      //widget.summary.hide();
    },

    onInputBlur: function() {
      var widget = this;
      widget.summary.show();
    },

        onResultsDisplay: function () {
            var widget = this;
            domClass.add(widget.domNode.parentNode, 'prevent')
            domClass.add(widget.domNode, 'selectable');
        },

        onResultsHide: function () {
            var widget = this;
            domClass.remove(widget.domNode.parentNode, 'prevent')
            domClass.remove(widget.domNode, 'selectable');
        },

    onKeyUp: function() {
      var widget = this;
      widget.results.clear();
    },

    onInvalidInput: function() {
      var widget = this;
      domClass.add(widget.domNode, 'error');
    },

    onValidInput: function(input) {
      var widget = this;
      domClass.remove(widget.domNode, 'error');
      widget.results.set(widget.search(input));
    },

        onInputReset: function () {
            var widget = this;
            domClass.remove(widget.domNode, 'error');
        },

    onAddButtonClicked: function() {
      var widget = this;
      widget.summary.hide();
      widget.input.focus();
    },

    initialize: function() {
      var widget = this;
      widget.summary = new Summary(dojo.query('.summary', widget.domNode)[0],
          widget.onSummaryClick.bind(widget),
          widget.entityStore);

      widget.input = new Input(dojo.query('input',
          widget.domNode)[0],
          widget.entityName,
          widget.inputPlaceHolder,
          widget.onInvalidInput.bind(widget),
          widget.onValidInput.bind(widget),
          widget.onKeyUp.bind(widget),
          widget.onInputClick.bind(widget),
          widget.onInputBlur.bind(widget),
                widget.onInputReset.bind(widget),
          widget.entityStore);

            widget.editor = new Editor(widget.summaryDom, widget.entityStore);

      widget.addButton = new AddButton(dojo.query('.add-more', widget.domNode)[0], widget.onAddButtonClicked.bind(widget), widget.entityStore);

      widget.resultsContainer = dojo.query('.auto-complete', widget.domNode)[0];
            widget.results = new Results(widget.resultsContainer,
                widget.resultsTemplate,
                widget.onSelect.bind(widget),
                widget.onResultsDisplay.bind(widget),
                widget.onResultsHide.bind(widget),
                widget.entityStore).hide();
      widget.eraser = dojo.query('.erase', widget.domNode)[0];
      return widget;
    },

    clear: function() {
      var widget = this;
      widget.input.clear();
      widget.summary.clear();
      widget.results.clear();
    },

    removeError: function () {
      var widget = this;
      domClass.remove(widget.domNode, 'error');
    },


    postCreate: function() {
      var widget = this;
      widget.initialize();

      on(win.body(), 'click', function(e) {
        var parentClicked = _.reduce([widget.input.dom, widget.resultsContainer], function(parentClicked, element) {
          return parentClicked && e.target != element;
        }, true);
        parentClicked ? widget.results.hide() : null;
      });

      window.document.addEventListener('orientationchange', function (){
        widget.input.blur();
      }, false);

      if (widget.eraser) {
        on(widget.eraser, 'click', function() {
          widget.clear();
        });
      }
    }
  });
});
