
define("tui/widget/booking/passengers/model/PassengerDetailsModel",[
	"dojo/_base/declare",
	"tui/widget/booking/passengers/model/PassengerFormModel"
	], function (declare, PassengerFormModel) {
		/**
		 * @module tui.widget.booking.passenger.PassengerFormModel
		 * 
		 */
		return  declare("tui.widget.booking.passengers.model.PassengerDetailsModel", [], {
			
			noOfAdults: null,
			
			noOfChildren: null,
			
			noOfInfants: null,
			
			countOfAdultsAndChildren: null,
			
			passengerFormDetails : null,
			
			
			setData : function(data) {
				this.passengerFormDetails = new PassengerFormModel(data.countOfPassengers);
				this.noOfAdults = data.noOfAdults;
				this.noOfChildren = data.noOfChildren;
				this.noOfInfants = data.noOfInfants;
				this.countOfPassengers = data.countOfPassengers;
				this.passengerFormDetails.setPassengerFormDetails(data);
				
				
			}
		});
});
