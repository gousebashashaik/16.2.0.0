define ("tui/widget/booking/changeroomallocation/view/deckOverlay", [
   "dojo/_base/declare",
   "tui/widget/popup/DynamicPopup",
   "dojo/dom",
   "dojo/dom-class"
  ], function(declare, dynamicPopup,dom,domClass){

	return declare("tui.widget.booking.changeroomallocation.view.deckOverlay", [dynamicPopup], {

		stopDefaultOnCancel: false,

		open: function(){
			var deckOverlay = this;
			deckOverlay.inherited(arguments);
			/*var priceBreakDown = dom.byId("priceBrk");
	        var priceBreakdownSticky = dojo.query(".noAccrodDef",priceBreakDown)[0];
	        domClass.remove(priceBreakdownSticky,"stick");*/
			
		},

		close: function(){
			var deckOverlay = this;
			
			
			if(deckOverlay.widgetRef != null){
				deckOverlay.widgetRef.destroyRecursive();
				deckOverlay.widgetRef = null;
				
			}
			deckOverlay.inherited(arguments);
			/*var priceBreakDown = dom.byId("priceBrk");
	        var priceBreakdownSticky = dojo.query(".noAccrodDef",priceBreakDown)[0];
	        domClass.remove(priceBreakdownSticky,"stick");*/
			
		}

	});
});