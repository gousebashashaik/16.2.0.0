define("tui/widget/booking/hoteloptions/view/SoccerAcademyOverlay", [
  "dojo/_base/declare",
  "tui/widget/popup/DynamicPopup",
  "dojo/dom",
  "dojo/dom-class",
  "dojo/query"], function (declare, dynamicPopup,dom,domclass,query) {

  return declare("tui.widget.booking.hoteloptions.view.SoccerAcademyOverlay", [dynamicPopup], {

    stopDefaultOnCancel: false,

    // ---------------------------------------------------------------- methods
    postCreate: function () {
      var soccerAcademyOverlay = this;
      soccerAcademyOverlay.inherited(arguments);
	  var priceBreakDown = dom.byId("priceBrk");
      var priceBreakdownSticky = query(".noAccrodDef",priceBreakDown)[0];
      domclass.remove(priceBreakdownSticky,"stick");
    },

    open: function () {
      var soccerAcademyOverlay = this;
     // window.scrollTo(0, 0);
      soccerAcademyOverlay.inherited(arguments);
    },

    close: function () {
      var soccerAcademyOverlay = this;
      soccerAcademyOverlay.inherited(arguments);
      dojo.publish("tui/widget/booking/soccerAcademyOverlay/close", [soccerAcademyOverlay]);
    }

  });
});