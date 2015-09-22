define("tui/searchBPanel/view/SearchMultiFieldList", ["dojo",
  "dojo/cookie",
  "dojo/on",
  "tui/searchBPanel/view/ErrorPopup",
  "tui/searchPanel/config/SearchConfig",
  "tui/utils/HasCssProperty",
  "tui/widget/form/MultiFieldList",
  "tui/searchB/nls/Searchi18nable"], function (dojo, cookie, on, SearchErrorPopup, searchConfig, hasCssProp) {

  dojo.declare("tui.searchBPanel.view.SearchMultiFieldList", [tui.widget.form.MultiFieldList,
    tui.searchB.nls.Searchi18nable], {

    itemsSelected: null,

    searchErrorPopup: SearchErrorPopup,

    duration: null,

    postCreate: function () {
      var searchMultiFieldList = this;

      searchMultiFieldList.inherited(arguments);
      searchMultiFieldList.saveInputValue = !searchMultiFieldList.searchPanelModel.validRoute;
      on(searchMultiFieldList.domNode, "animationend, webkitAnimationEnd, oanimationend", function (event) {
        if (dojo.hasClass(searchMultiFieldList.domNode, 'pulse')) {
          dojo.removeClass(searchMultiFieldList.domNode, 'pulse');
        }
      });
    },

    unselect: function () {
      var searchMultiFieldList = this;
      searchMultiFieldList.saveInputValue = !searchMultiFieldList.searchPanelModel.validRoute;
      searchMultiFieldList.inherited(arguments);
    },

    highlight: function (add, controller) {
      var searchMultiFieldList = this;
      if (controller !== searchMultiFieldList.widgetController) {
        return;
      }
      var action = (add) ? "addClass" : "removeClass";
      dojo[action](searchMultiFieldList.domNode, 'highlight');
    },

    pulse: function (controller) {
      // Method which apply a pulsing effect to the field when an item is added or removed.
      var searchMultiFieldList = this;
      if (controller !== searchMultiFieldList.widgetController) {
        return;
      }
      if (hasCssProp.test("animation")) {
        dojo.addClass(searchMultiFieldList.domNode, 'pulse');
      }
    },

    createAutocomplete: function () {
      // summary:
      //		Creates autocomplete for multifieldList.
      var searchMultiFieldList = this;
      searchMultiFieldList.autocompleteProps.searchPanelModel = searchMultiFieldList.searchPanelModel;
      searchMultiFieldList.autocompleteProps.widgetController = searchMultiFieldList.widgetController;
      searchMultiFieldList.autocompleteProps.multiField = searchMultiFieldList;
      searchMultiFieldList.inherited(arguments);
    },

    getTextWidth: function (text) {
      var el = dojo.create("span", {
        innerHTML: text,
        style: {
          fontSize: "12px",
          fontStyle: "italic",
          visibility: "hidden"
        }
      }, document.body, "last");
      var width = dojo.position(el).w;
      dojo.destroy(el);
      return width;
    }
  });

  return tui.searchBPanel.view.SearchMultiFieldList;
});
