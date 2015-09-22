define("tui/widget/booking/passengers/view/PassengerDpnOverlay", [
  "dojo/_base/declare",
  "dojo/topic",
  "tui/widget/popup/DynamicPopup"
], function (declare, topic, dynamicPopup) {
	return declare("tui.widget.booking.passengers.view.PassengerDpnOverlay", [dynamicPopup], {

    stopDefaultOnCancel: false,

    open: function () {
    	 
      var PassengerDpnOverlay = this;
      topic.subscribe("some/PassengerDpnOverlay/closeOverlay", function(){ 
    	  PassengerDpnOverlay.close();
	    });
     // window.scrollTo(0, 0);
      PassengerDpnOverlay.inherited(arguments);
    },

    close: function () {
      var PassengerDpnOverlay = this;
      PassengerDpnOverlay.inherited(arguments);
      dojo.publish("tui/widget/booking/passengers/view/PassengerDpnOverlay/close", [PassengerDpnOverlay]);
    }

  });
});