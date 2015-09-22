define("tui/widget/booking/passengers/view/PassengerChildAgeOverlay", [
  "dojo/_base/declare",
  "tui/widget/booking/utils/BookFlowDynamicPopup",
  "dojo/dom",
  "dojo/topic",
  "dojo/dom-class",
  "dojo/query"
], function (declare, dynamicPopup,dom,topic,domclass,query) {

  return declare("tui.widget.booking.passengers.view.PassengerChildAgeOverlay", [dynamicPopup], {

		    stopDefaultOnCancel: false,
		    refWidget:null,
		      open: function () {
		      //window.scrollTo(0, 0);
			  this.addEventModalListener();
		      this.inherited(arguments);
			  var priceBreakDown = dom.byId("priceBrk");
		      var priceBreakdownSticky = query(".noAccrodDef",priceBreakDown)[0];
		      domclass.remove(priceBreakdownSticky,"stick");
		    },

			addEventModalListener:function(){
				var popup = this;
				if (popup.modalConnect) return;
				popup.modalConnect = popup.connect(popup.modalDomNode, "onclick", function (event) {
                //console.log("hello");
				});
			},


		    close: function () {

		    	 if (this.refWidget.passengerChildAgeView != null) {
		    		 this.refWidget.passengerChildAgeView.destroyRecursive();
		    		 this.refWidget.passengerChildAgeView = null;
		    		 this.refWidget.passengerChildAgeOverlay.destroyRecursive();
		    		 this.refWidget.passengerChildAgeOverlay = null;
	               }
		      this.inherited(arguments);
		      var priceBreakDown = dom.byId("priceBrk");
		        var priceBreakdownSticky = query(".noAccrodDef",priceBreakDown)[0];
		        domclass.remove(priceBreakdownSticky,"stick");
		    }

  		});
});