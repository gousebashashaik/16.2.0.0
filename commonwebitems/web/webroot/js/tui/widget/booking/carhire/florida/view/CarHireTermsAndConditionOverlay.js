define("tui/widget/booking/carhire/florida/view/CarHireTermsAndConditionOverlay", [
  "dojo/_base/declare",
  "tui/widget/booking/utils/BookFlowDynamicPopup",
  "dojo/dom",
  "dojo/topic",
  "dojo/dom-class",
  "dojo/query"
], function (declare, dynamicPopup,dom,topic,domclass,query) {

  return declare("tui.widget.booking.carhire.florida.view.CarHireTermsAndConditionOverlay", [dynamicPopup], {

		    stopDefaultOnCancel: false,
		    refWidget:null,
		      open: function () {
		      //window.scrollTo(0, 0);
		      this.inherited(arguments);
			  var priceBreakDown = dom.byId("priceBrk");
		      var priceBreakdownSticky = query(".noAccrodDef",priceBreakDown)[0];
		      domclass.remove(priceBreakdownSticky,"stick");
		    },

		    close: function () {
		    	var CarHireTermsAndConditionOverlay = this;
		    	CarHireTermsAndConditionOverlay.inherited(arguments);
	            dojo.publish("tui/widget/booking/CarHireTermsAndConditionOverlay/close", [CarHireTermsAndConditionOverlay]);
	            var priceBreakDown = dom.byId("priceBrk");
			    var priceBreakdownSticky = query(".noAccrodDef",priceBreakDown)[0];
			    domclass.remove(priceBreakdownSticky,"stick");
		     }

  		});
});