define("tui/widget/booking/passengers/view/PassengerChildValidationAlertOverlay", [
  "dojo/_base/declare",
  "dojo/dom",
  "dojo/dom-class",
  "dojo/query",
  "tui/widget/popup/DynamicPopup"
], function (declare, dom, domclass, query,dynamicPopup) {
	return declare("tui.widget.booking.passengers.view.PassengerChildValidationAlertOverlay", [tui.widget.popup.DynamicPopup], {

    stopDefaultOnCancel: false,
    refWidget:null,

    open: function () {
      var PassengerChildValidationAlertOverlay = this;
      //window.scrollTo(0, 0);
     
	  var priceBreakDown = dom.byId("priceBrk");
      var priceBreakdownSticky = query(".noAccrodDef",priceBreakDown)[0];
      domclass.remove(priceBreakdownSticky,"stick");
      var nl = query(".modal")[0];
	  PassengerChildValidationAlertOverlay.addEventModalListener();
      PassengerChildValidationAlertOverlay.inherited(arguments);  
      domclass.add(nl,"specialClass");
    },

	addEventModalListener:function(){
			var popup = this;
			if (popup.modalConnect) return;
			popup.modalConnect = popup.connect(popup.modalDomNode, "onclick", function (event) {
			//console.log("hello");
			});
		},

    close: function () {

   	 if (this.refWidget.passengerChildAgeOverlay != null) {
   		 this.refWidget.passengerChildAgeOverlay.destroyRecursive();
   		 this.refWidget.passengerChildAgeOverlay = null;
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