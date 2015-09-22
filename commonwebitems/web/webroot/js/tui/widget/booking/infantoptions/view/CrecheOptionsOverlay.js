define("tui/widget/booking/infantoptions/view/CrecheOptionsOverlay", [
  "dojo/_base/declare",
  "dojo/query",
  "dojo/topic",
  "tui/widget/popup/DynamicPopup",
  "dojo/dom",
  "dojo/dom-class"
], function (declare, query, topic, dynamicPopup,dom,domclass) {

  return declare("tui.widget.booking.infantoptions.view.CrecheOptionsOverlay", [dynamicPopup], {

    stopDefaultOnCancel: false,

    postCreate: function () {
      var infantCrecheOptionsOverlay = this;
      infantCrecheOptionsOverlay.inherited(arguments);
	  var priceBreakDown = dom.byId("priceBrk");
      var priceBreakdownSticky = query(".noAccrodDef",priceBreakDown)[0];
      domclass.remove(priceBreakdownSticky,"stick");
    },

    open: function () {
      var infantCrecheOptionsOverlay = this;
     // window.scrollTo(0, 0);
      infantCrecheOptionsOverlay.inherited(arguments);
    },

    close: function () {
      var infantCrecheOptionsOverlay = this;
      infantCrecheOptionsOverlay.inherited(arguments);
    }

  });
});