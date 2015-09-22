define("tui/searchPanel/view/SearchErrorMessaging", [
  "dojo",
  "tui/searchPanel/view/ErrorPopup"], function (dojo, ErrorPopup) {

  dojo.declare("tui.searchPanel.view.SearchErrorMessaging", null, {

    // ----------------------------------------------------------------------------- properties

    errorPopup: null,

    // ----------------------------------------------------------------------------- methods

    validateErrorMessage: function (errorField, params) {

      var searchErrorMessaging = this;
      var newParams = dojo.mixin({
        arrow: false,
        elementRelativeTo: searchErrorMessaging.domNode,
        errorPopupClass: null,
        floatWhere: "position-bottom-center",
        onClose: function () {
          var fromToError = dojo.clone(searchErrorMessaging.searchPanelModel.searchErrorMessages.get(params.field));
          if (fromToError[params.key]) {
            delete fromToError[params.key];
            searchErrorMessaging.searchPanelModel.searchErrorMessages.set(params.field, fromToError);
          }
        }
      }, params);
      if (errorField) {
        if (searchErrorMessaging.errorPopup) {
          searchErrorMessaging.errorPopup.destroyRecursive();
        }
        searchErrorMessaging.errorPopup = new ErrorPopup(newParams, null);
        searchErrorMessaging.errorPopup.open();
        if (searchErrorMessaging.domNode) {
          dojo.addClass(searchErrorMessaging.domNode, "error");
        }
        searchErrorMessaging.onAddError(searchErrorMessaging.errorPopup, errorField, newParams);
        searchErrorMessaging.addTags(params.key);
      } else {
        if (searchErrorMessaging.errorPopup) {
          searchErrorMessaging.errorPopup.close();
        }
        if (searchErrorMessaging.domNode) {
          dojo.removeClass(searchErrorMessaging.domNode, "error");
        }
        searchErrorMessaging.onRemoveError(searchErrorMessaging.errorPopup, errorField, newParams);
      }
    },

    addTags: function (key) {
      var searchErrorMessaging = this, analyticsText;
      switch (key) {
        case "emptyFromTo":
          analyticsText = "Destination and Airport Error";
          break;
        case "emptyDate":
          analyticsText = "Departure Date Error";
          break;
        case "childNoAges":
          analyticsText = "Child Error";
          break;
        case "infantLimit":
          analyticsText = "Child Error";
          break;
        case "childOnly":
          analyticsText = "Child Error";
          break;
        default:
          analyticsText = "Party Comp Error";
          break;
      }
      searchErrorMessaging.tagElement(searchErrorMessaging.errorPopup.popupDomNode, analyticsText);
    },

    onAddError: function (errorPopup, errorField, newParams) {
    },

    onRemoveError: function (errorPopup, errorField, newParams) {
    }

  });

  return tui.searchPanel.view.SearchErrorMessaging;
});