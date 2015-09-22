define("tui/widget/booking/passengers/view/PassengerInsAlertOverlay", [
  "dojo/_base/declare",
   "tui/widget/booking/utils/BookFlowDynamicPopup"
], function (declare, dynamicPopup) {
	return declare("tui.widget.booking.passengers.view.PassengerInsAlertOverlay", [dynamicPopup], {

    stopDefaultOnCancel: false,

    open: function () {
      var PassengerDpnOverlay = this;
     // window.scrollTo(0, 0);
      PassengerDpnOverlay.inherited(arguments);
    },

    close: function () {

    }

  });
});