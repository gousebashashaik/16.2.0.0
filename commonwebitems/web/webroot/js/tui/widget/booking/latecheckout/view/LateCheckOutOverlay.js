define("tui/widget/booking/latecheckout/view/LateCheckOutOverlay", [
  "dojo/_base/declare",
  "dojo/dom",
	 "dojo/dom-class",
  "tui/widget/popup/DynamicPopup"

], function (declare,dom,domclass,dynamicPopup) {

  return declare("tui.widget.booking.latecheckout.view.LateCheckOutOverlay", [dynamicPopup], {

    stopDefaultOnCancel: false,

    open: function () {
      var lateCheckOutOverlay = this;
     // window.scrollTo(0, 0);
      var priceBreakDown = dom.byId("priceBrk");
      var priceBreakdownSticky = dojo.query(".noAccrodDef",priceBreakDown)[0];
      domclass.remove(priceBreakdownSticky,"stick");
      lateCheckOutOverlay.inherited(arguments);
    },

    close: function () {
      var lateCheckOutOverlay = this;
      lateCheckOutOverlay.inherited(arguments);
      dojo.publish("tui/widget/booking/lateCheckOutOverlay/close", [lateCheckOutOverlay]);
    }

  });
});