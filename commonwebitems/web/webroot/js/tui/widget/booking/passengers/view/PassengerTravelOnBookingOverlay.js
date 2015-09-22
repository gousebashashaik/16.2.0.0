define("tui/widget/booking/passengers/view/PassengerTravelOnBookingOverlay", [
  "dojo/_base/declare",
  "tui/widget/booking/utils/BookFlowDynamicPopup",
  "dojo/dom",
  "dojo/topic",
  "dojo/dom-class",
  "dojo/query"
], function (declare, dynamicPopup,dom,topic,domclass,query) {

  return declare("tui.widget.booking.passengers.view.PassengerTravelOnBookingOverlay", [dynamicPopup], {

		    stopDefaultOnCancel: false,
		    refWidget:null,
		      open: function () {
		     // window.scrollTo(0, 0);
		      this.inherited(arguments);
			  var priceBreakDown = dom.byId("priceBrk");
		      var priceBreakdownSticky = query(".noAccrodDef",priceBreakDown)[0];
		      domclass.remove(priceBreakdownSticky,"stick");
		    },

		    close: function () {
		    	
		    	 if (this.passengerTravelOnBookingView != null) {
		    		 this.passengerTravelOnBookingView.destroyRecursive();
		    		 this.passengerTravelOnBookingView = null;
		    		 this.passengerTravelOnBookingViewOverlay.destroyRecursive();
		    		 this.passengerTravelOnBookingViewOverlay = null;
	               }
		      this.inherited(arguments);
		      
		      var priceBreakDown = dom.byId("priceBrk");
		        var priceBreakdownSticky = query(".noAccrodDef",priceBreakDown)[0];
		        domclass.remove(priceBreakdownSticky,"stick");

		    },
		    addEventModalListener: function () {
	            var popup = this;
	            if (popup.modalConnect) return;
	            popup.modalConnect = popup.connect(popup.modalDomNode, "onclick", function (event) {
	                if (popup.stopDefaultOnCancel) {
	                    dojo.stopEvent(event);
	                }
	             //   popup.close();
	            })
		    }

  		});
});