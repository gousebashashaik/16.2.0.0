define("tui/widget/booking/hoteloptions/view/StageAcademyOverlay", ["dojo",
  "dojo/_base/declare",
  "tui/widget/popup/DynamicPopup",
  "dojo/dom",
  "dojo/dom-class",
  "dojo/query"], function (dojo, declare, dynamicPopup,dom,domclass,query) {

  return declare("tui.widget.booking.hoteloptions.view.StageAcademyOverlay", [dynamicPopup], {

    stopDefaultOnCancel: false,

    // ---------------------------------------------------------------- methods
    postCreate: function () {
      var stageAcademyOverlay = this;
      stageAcademyOverlay.inherited(arguments);
    },

    open: function () {
      var stageAcademyOverlay = this;
     // window.scrollTo(0, 0);
      //tui.removeAllErrorPopups();
      stageAcademyOverlay.inherited(arguments);
	  var priceBreakDown = dom.byId("priceBrk");
      var priceBreakdownSticky = query(".noAccrodDef",priceBreakDown)[0];
      domclass.remove(priceBreakdownSticky,"stick");
    },

    close: function () {
      var stageAcademyOverlay = this;
      stageAcademyOverlay.inherited(arguments);
    }
  });
});