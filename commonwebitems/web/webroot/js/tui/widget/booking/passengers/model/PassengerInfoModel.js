
define("tui/widget/booking/passengers/model/PassengerInfoModel",[
	"dojo/_base/declare"
	], function (declare, Stateful) {
		/**
		 * @module tui.widget.booking.passenger.PassengerInfoModel
		 *
		 */
		return declare("tui.widget.booking.passengers.model.PassengerInfoModel", [], {

			title : null,

			gender : null,

			firstName : null,

			lastName : null,

			leadPassenger: null,

			personType : null,

			age : null,

			day : null,

			month : null,

			year : null,

			dob : null,

			country : null,

			constructor: function() {
				this.title= null;
				this.firstName = null;
				this.lastName= null;
				this.leadPassenger = null;
				this.personType = null;
				this.age = null;
				this.day = null;
				this.month= null;
				this.year= null;
				this.dob = null;
				this.country = null;
			},

			setData : function(data) {
				this.title= data.title;
				this.firstName = data.firstName;
				this.lastName= data.lastName;
				this.leadPassenger = data.leadPassenger;
				this.personType = data.personType;
				this.age = data.age;
				this.day = data.day;
				this.month= data.month;
				this.year= data.year;
				this.dob = data.dob;
				this.country = data.country;
			}
		});
});
