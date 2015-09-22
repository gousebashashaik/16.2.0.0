define("tui/widget/booking/inflightmeals/view/FlightMealsOverlay", ['dojo/query',
  "dojo/_base/declare",
  "tui/widget/booking/utils/BookFlowDynamicPopup",
  "dojo/dom",
  "dojo/dom-class",
  "dojo/topic"
], function (query,declare, BookFlowDynamicPopup,dom,domClass,topic) {

  return declare("tui.widget.booking.inflightmeals.view.FlightMealsOverlay", [BookFlowDynamicPopup], {

    stopDefaultOnCancel: false,

    open: function () {
      var flightMealsOverlay = this;
    //  window.scrollTo(0, 0);
      flightMealsOverlay.inherited(arguments);
      var priceBreakDown = dom.byId("priceBrk");
      var priceBreakdownSticky = query(".noAccrodDef",priceBreakDown)[0];
      domClass.remove(priceBreakdownSticky,"stick");
    },

    close: function () {
      var flightMealsOverlay = this;
      flightMealsOverlay.inherited(arguments);
      topic.publish("tui/widget/booking/FlightMealsOverlay/close", [flightMealsOverlay]);
      var priceBreakDown = dom.byId("priceBrk");
      var priceBreakdownSticky = query(".noAccrodDef",priceBreakDown)[0];
      domClass.remove(priceBreakdownSticky,"stick");
    }

  });
});