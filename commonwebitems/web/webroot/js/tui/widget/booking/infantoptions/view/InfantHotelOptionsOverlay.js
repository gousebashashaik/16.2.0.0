define ("tui/widget/booking/infantoptions/view/InfantHotelOptionsOverlay", [
   "dojo/_base/declare",
   "tui/widget/popup/DynamicPopup",
   "dojo/dom",
   "dojo/dom-class"
  ], function(declare, dynamicPopup,dom,domClass){

	return declare("tui.widget.booking.infantoptions.view.InfantHotelOptionsOverlay", [dynamicPopup], {

		stopDefaultOnCancel: false,

		open: function(){
			var infantHotelOptionsOverlay = this;
			infantHotelOptionsOverlay.inherited(arguments);
			var priceBreakDown = dom.byId("priceBrk");
	        var priceBreakdownSticky = dojo.query(".noAccrodDef",priceBreakDown)[0];
	        domClass.remove(priceBreakdownSticky,"stick");
			
		},

		close: function(){
			var infantHotelOptionsOverlay = this;
			infantHotelOptionsOverlay.inherited(arguments);
			var priceBreakDown = dom.byId("priceBrk");
	        var priceBreakdownSticky = dojo.query(".noAccrodDef",priceBreakDown)[0];
	        domClass.remove(priceBreakdownSticky,"stick");
			dojo.publish("tui/widget/booking/infantHotelOptionsOverlay/close", [infantHotelOptionsOverlay]);
		}

	});
});