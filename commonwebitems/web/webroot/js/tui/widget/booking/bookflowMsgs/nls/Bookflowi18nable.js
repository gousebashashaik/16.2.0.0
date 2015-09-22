define("tui/widget/booking/bookflowMsgs/nls/Bookflowi18nable", [
	"dojo",
	"dojo/i18n",
	"dojo/i18n!./Bookflowi18n"
], function (dojo, i18n) {

	dojo.declare("tui.widget.booking.bookflowMsgs.nls.Bookflowi18nable", null, {

		// ----------------------------------------------------------------------------- properties

		bookflowMessaging: null,

		// ----------------------------------------------------------------------------- methods

		initBookflowMessaging: function () {
			// summary:
			//		Initialises localisation
			var bookflowMsging = this; 
			bookflowMsging.bookflowMessaging = i18n.getLocalization("tui.widget.booking.bookflowMsgs", "Bookflowi18n");
		}
	});

	return tui.widget.booking.bookflowMsgs.nls.Bookflowi18nable;
});
