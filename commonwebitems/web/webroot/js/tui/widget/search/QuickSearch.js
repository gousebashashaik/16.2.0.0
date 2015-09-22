define('tui/widget/search/QuickSearch', [
  'dojo',
  'dijit/focus',
  'dojo/text!tui/widget/search/templates/quicksearch/quickSearchTmpl.html',
  'dojo/text!tui/widget/search/templates/quicksearch/resultItemTmpl.html',
  'tui/dtl/Tmpl',
  'tui/widget/form/AutoComplete'
], function(dojo, focus, quickSearchTmpl, resultItemTmpl) {

  dojo.declare('tui.widget.search.QuickSearch', [tui.widget.form.AutoComplete], {

    // summary:
    //     This class defines the behaviour for the quick search component.
    //     Which provides autocomplete searches for accommodation, and locations.
    // @author: Maurice Morgan.

    // ---------------------------------------------------------------- quickSearch methods

    targetURL: dojoConfig.paths.webRoot + '/getAutoSearchResults',

    liContentTemplate: resultItemTmpl,

    highlightFirstOnShow: true,

    searchQuery: 'searchParameter',

    idProperty: 'value',

    descriptionProp: 'type',

    createListElement: function() {
      // summary:
      //      Extends createListElement method, and adds a css class called "autocomplete" to listElement.
      var autoCompleteable = this;
      autoCompleteable.inherited(arguments);
      dojo.addClass(autoCompleteable.listElement, 'qs');
      autoCompleteable.attachTag();
      autoCompleteable.tagElements(dojo.query(autoCompleteable.domNode), "destinationQuickSearch");
    },

    createNoMatchItem: function() {
      var quickSearch = this;
      var context = {templateView: 'quickSearchNoMatch'};
      quickSearch.noMatchItem = quickSearch.createTmpl(context, quickSearchTmpl);
      return quickSearch.noMatchItem;
    },

    onBeforeSetResults: function(dataresults) {
      var parseDataResults = [];
      var parser = function(item, index) {
        parseDataResults.push({
          title: item.name,
          value: item.url,
          type: prop,
          rawData: item
        });
      };
      for (var prop in dataresults) {
        _.forEach(dataresults[prop], parser);
      }
      return parseDataResults;
    },

    onNoResults: function(listElement) {
      var quickSearch = this;
      dojo.html.set(listElement, quickSearch.noMatchItem || quickSearch.createNoMatchItem());
    },

    _connectEvents: function(li, index) {
      var listable = this;

      listable.listConnects.push(
        dojo.connect(window, 'onresize', function(event) {
          dojo.stopEvent(event);
          if (listable.hideOnResize) {
            listable.hideList();
            return;
          }
          listable.onRepositionList(listable.listElement);
        })
      );

      listable.listConnects.push(
        dojo.connect(li, 'onmouseenter', function(event) {
          dojo.stopEvent(event);
          listable.listIndex = index;
          listable.setHighlight();
        })
      );

      listable.listConnects.push(
        dojo.connect(li, 'onmouseleave', function(event) {
          dojo.stopEvent(event);
          listable.listIndex = index;
          listable.setHighlight();
        })
      );

      listable.listConnects.push(
        dojo.connect(li, 'onclick', function(event) {
          dojo.stopEvent(event);
          listable.elementListSelection();
        })
      );

      listable.listConnects.push(
        dojo.connect(listable.domNode, 'onclick', function(event) {
          dojo.stopEvent(event);
          focus.focus(listable.domNode);
        })
      );

      /*listable.listConnects.push(
        dojo.connect(listable.domNode, 'onblur', function(event) {
          dojo.stopEvent(event);
          if (listable.listShowing) {
            listable.elementListSelection();
          }
        })
      );*/
    },

    listkeydown: function(event, domNode) {
      var listable = this;
      switch (event.keyCode) {
        case dojo.keys.DOWN_ARROW:
          listable.onDownArrowKey(event, domNode);
          break;
        case dojo.keys.UP_ARROW:
          listable.onUpArrowKey(event, domNode);
          break;
        case dojo.keys.ENTER:
          dojo.stopEvent(event);
          listable.onEnterKey(event, domNode);
          listable.onElementListSelection(listable.getSelectedData());
          break;
        case dojo.keys.ESCAPE:
          dojo.stopEvent(event);
          listable.onEscKey(event, domNode);
          break;
        case dojo.keys.TAB:
          dojo.stopEvent(event);
          listable.onTabKey(event, domNode);          
          break;
        default:
          listable.onOtherKey(event, domNode);
      }
    },

    onTabKey: function(event, domNode) {
      var listable = this;
      listable.hideList();
      listable.setSelectedData();  
    },

    onElementListSelection: function(selectedData) {
      window.location.href = selectedData.value;
    },

    onRenderLiContent: function(text, data) {
      var quickSearch = this;
      var typedText = quickSearch.domNode.value;
      var contentHTML = dojo.string.substitute(quickSearch.liContentTemplate, {
        autocompletetext: text.replace(new RegExp('(' + typedText + ')', 'i'), '<strong>$1</strong>'),
        description: data[quickSearch.descriptionProp]
      });
      return contentHTML;
    }
  });

  return tui.widget.search.QuickSearch;
});
