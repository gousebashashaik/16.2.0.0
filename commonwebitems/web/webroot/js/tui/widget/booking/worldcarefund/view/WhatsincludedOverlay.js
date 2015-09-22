define("tui/widget/booking/worldcarefund/view/WhatsincludedOverlay", [
  "dojo/_base/declare",
  "dojo/dom",
	 "dojo/dom-class",
  "tui/widget/popup/DynamicPopup"

], function (declare,dom,domclass,dynamicPopup) {

  return declare("tui.widget.booking.worldcarefund.view.WhatsincludedOverlay", [dynamicPopup], {

    stopDefaultOnCancel: false,

    open: function () {
      var whatsincludedOverlay = this;
     // window.scrollTo(0, 0);
      var priceBreakDown = dom.byId("priceBrk");
      var priceBreakdownSticky = dojo.query(".noAccrodDef",priceBreakDown)[0];
      domclass.remove(priceBreakdownSticky,"stick");
      whatsincludedOverlay.inherited(arguments);
    },

    close: function () {
      var whatsincludedOverlay = this;
      whatsincludedOverlay.inherited(arguments);
      dojo.publish("tui/widget/booking/WhatsincludedOverlay/close", [whatsincludedOverlay]);
    }

  });
});