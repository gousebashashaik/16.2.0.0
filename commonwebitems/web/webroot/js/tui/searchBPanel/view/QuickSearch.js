define("tui/searchBPanel/view/QuickSearch", [
  "dojo",
  "dojo/text!tui/widget/search/templates/quicksearch/quickSearchTmpl.html",
  "dojo/text!tui/widget/search/templates/quicksearch/resultItemTmpl.html",
  "tui/dtl/Tmpl",
  "tui/widget/form/AutoComplete"
], function(dojo, quickSearchTmpl, resultItemTmpl) {

  // can you pass me the id for results
  dojo.declare("tui.searchBPanel.view.QuickSearch", [tui.widget.form.AutoComplete], {
    // summary:
    //    This class defines the behaviour for the quick search compoment.
    //      Which provides autocomplete searches for accomodation, and locations.
    // @author: Maurice Morgan.

    // ---------------------------------------------------------------- quickSearch methods

    targetURL: dojoConfig.paths.webRoot + "/getAutoSearchResults",

    liContentTemplate: resultItemTmpl,

    highlightFirstOnShow: true,

    searchQuery: "searchParameter",

    idProperty: "value",

    descriptionProp: "type",

    createListElement: function() {
      // summary:
      //      Extends createListElement method, and adds a css class called "autocomplete" to listElement.
      var autoCompleteable = this;
      autoCompleteable.inherited(arguments);
      dojo.addClass(autoCompleteable.listElement, "qs");
      autoCompleteable.attachTag();
      autoCompleteable.tagElements(dojo.query(autoCompleteable.domNode), "destinationQuickSearch");
    },

    createNoMatchItem: function() {
      var quickSearch = this;
      var context = {templateView: "quickSearchNoMatch"};
      quickSearch.noMatchItem = quickSearch.createTmpl(context, quickSearchTmpl);
      return quickSearch.noMatchItem;
    },

    onBeforeSetResults: function(dataresults) {
      var parseDataResults = [];
      for (prop in dataresults) {
        _.forEach(dataresults[prop], function(item, index) {
          parseDataResults.push({
            title:   item.name,
            value:   item.url,
            type:    prop,
            rawData: item
          });
        });
      }
      return parseDataResults;
    },

    onNoResults: function(listElement) {
      var quickSearch = this;
      dojo.html.set(listElement, quickSearch.noMatchItem || quickSearch.createNoMatchItem());
    },
    
    listkeydown: function(event, domNode) {
        var quickSearch = this;
        switch (event.keyCode) {
          case dojo.keys.DOWN_ARROW:
            quickSearch.onDownArrowKey(event, domNode);
            break;
          case dojo.keys.UP_ARROW:
            quickSearch.onUpArrowKey(event, domNode);
            break;
          case dojo.keys.ENTER:
            dojo.stopEvent(event);
            quickSearch.onEnterKey(event, domNode);
            quickSearch.onElementListSelection(quickSearch.getSelectedData());
            break;
          case dojo.keys.ESCAPE:
            dojo.stopEvent(event);
            quickSearch.onEscKey(event, domNode);
            break;
          case dojo.keys.TAB:
            dojo.stopEvent(event);
            quickSearch.onTabKey(event, domNode);
            break;
          default:
            quickSearch.onOtherKey(event, domNode);
        }
      },

      onTabKey: function(event, domNode) {
        var quickSearch = this;
        quickSearch.hideList();
        quickSearch.setSelectedData();
      },

    onElementListSelection: function(selectedData, listData) {
      window.location.href = selectedData.value;
    },

    onRenderLiContent: function(text, data) {
      var quickSearch = this;
      var typedText = quickSearch.domNode.value;
      var contentHTML = dojo.string.substitute(quickSearch.liContentTemplate, {
        autocompletetext: text.replace(new RegExp('(' + typedText + ')', 'i'), '<strong>$1</strong>'),
        description:      data[quickSearch.descriptionProp]
      });
      return contentHTML;
    }
  });

  return tui.searchBPanel.view.QuickSearch;
});
