define("tui/widget/booking/yourseats/view/TermsAndConditionsOverlay", [
  "dojo/_base/declare",
  "tui/widget/booking/utils/BookFlowDynamicPopup",
  "dojo/dom",
  "dojo/topic",
  "dojo/dom-class",
  "dojo/query"
], function (declare, dynamicPopup,dom,topic,domclass,query) {

  return declare("tui.widget.booking.yourseats.view.TermsAndConditionsOverlay", [dynamicPopup], {

		    stopDefaultOnCancel: false,
		    closeSelector: ".bookflowClose",
		      open: function () {
		      //window.scrollTo(0, 0);
		      this.inherited(arguments);
			  var priceBreakDown = dom.byId("priceBrk");
		      var priceBreakdownSticky = query(".noAccrodDef",priceBreakDown)[0];
		      domclass.remove(priceBreakdownSticky,"stick");
		    },

		    close: function () {
		      this.inherited(arguments);
		      var priceBreakDown = dom.byId("priceBrk");
		        var priceBreakdownSticky = query(".noAccrodDef",priceBreakDown)[0];
		        domclass.remove(priceBreakdownSticky,"stick");
		    }

  		});
});