define("tui/widget/booking/hoteloptions/view/SwimAcademyOverlay", [
  "dojo/_base/declare",
  "tui/widget/popup/DynamicPopup",
  "dojo/dom",
  "dojo/dom-class",
  "dojo/query"
], function (declare, dynamicPopup,dom,domclass,query) {

  return declare("tui.widget.booking.hoteloptions.view.SwimAcademyOverlay", [dynamicPopup], {

    stopDefaultOnCancel: false,

    open: function () {
     // window.scrollTo(0, 0);
      this.inherited(arguments);
	  var priceBreakDown = dom.byId("priceBrk");
      var priceBreakdownSticky = query(".noAccrodDef",priceBreakDown)[0];
      domclass.remove(priceBreakdownSticky,"stick");
    },

    close: function () {
      this.inherited(arguments);
    }

  });
});