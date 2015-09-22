
define([
	"dojo/_base/declare"
	], function (
		declare
	) {
		/**
		 * @module
		 * app.ResponseModel
		 */
		var PassengerDetailsReturnModel = declare("tui.widget.booking.passenger.PassengerDetailsReturnModel", [], {
			
			noOfAdults: null,
			
			noOfChild: null,
			
			noOfInfant: null,
			
			countOfAdultAndChild: null
			
		});

		return PassengerDetailsReturnModel;
});
