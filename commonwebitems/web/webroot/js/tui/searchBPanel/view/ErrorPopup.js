define("tui/searchBPanel/view/ErrorPopup", [
  "dojo",
  "tui/widget/popup/ErrorPopup",
  "dojo/text!tui/searchBPanel/view/templates/error.html"], function (dojo, ErrorPopup, Tmpl) {

  dojo.declare("tui.searchBPanel.view.ErrorPopup", [tui.widget.popup.ErrorPopup], {

    // ----------------------------------------------------------------------------- properties

    tmpl: Tmpl,

    arrow: true,

    errorPopupClass: null,

    closeSelector: ".close",

    subscribableMethods: ["close"],

    analyticsText: '',

    // ----------------------------------------------------------------------------- methods

    setPosOffset: function (position) {
      var errorPopup = this;
      switch (position) {
        case tui.widget.mixins.FloatPosition.BOTTOM_CENTER:
          errorPopup.posOffset = {
            top: 15,
            left: 0
          };
          break;

        case tui.widget.mixins.FloatPosition.BOTTOM_LEFT:
          errorPopup.posOffset = {
            top: 15,
            left: 0
          };
          break;

        case tui.widget.mixins.FloatPosition.BOTTOM_RIGHT:
          errorPopup.posOffset = {
            top: -15,
            left: -15
          };
          break;
      }
    },

    onAfterTmplRender: function () {
      // summary:
      //		Extends onAfterTmplRender to add a close connect event listener for closing popup,
      //		when close link is selected.
      var errorPopup = this;
      errorPopup.inherited(arguments);
      dojo.query(errorPopup.closeSelector, errorPopup.popupDomNode).onclick(function (event) {
        dojo.stopEvent(event);
        errorPopup.close();
      });
      errorPopup.tagElement(errorPopup.popupDomNode, errorPopup.analyticsText);
    },

    onClose: function (domNode, errorPopup) {
    }

  });

  return tui.searchBPanel.view.ErrorPopup;
});