
define("tui/widget/booking/passengers/model/PassengerFormModel",[
	"dojo/_base/declare",
	"dojo/Stateful",
	"tui/widget/booking/passengers/model/PassengerInfoModel"
	], function (declare, PassengerInfoModel) {
		/**
		 * @module tui.widget.booking.passenger.PassengerInfoModel
		 *
		 */
		return  declare("tui.widget.booking.passengers.model.PassengerFormModel", [], {

			address1 : null,

			address2 : null,

			houseNum : null,

			town : null,

			county : null,

			postCode: null,

			telephoneNum : null,

			email : null,

			confirmationEmail : null,

			country : null,

			importantInformationChecked : false,

			passengerInfoDetails : [],

			constructor : function(noOfPassengers){
				this.address1 = null;
				this.address2 = null;
				this.houseNum = null;
				this.county = null;
				this.country = null;
				this.town = null;
				this.postcode = null;
				this.telephoneNum = null;
				this.email = null;
				this.confirmationEmail = null;
				this.importantInformationChecked= false;
				this.setEmptyData(noOfPassengers);
			},

			setPassengerFormDetails : function(data){
				if(data.passengerDetailsFormBean !== ""){
					this.address1 = data.address1;
					this.address2 = data.address2;
					this.houseNum = data.houseNum;
					this.county = data.county;
					this.country = data.country;
					this.town = data.town;
					this.postcode = data.postcode;
					this.telephoneNum = data.telephoneNum;
					this.email = data.email;
					this.confirmationEmail = data.confirmationEmai;
					this.importantInformationChecked= data.importantInformationChecked;
					this.passengerInfoDetails = this.setPassengerInfoDetails(data.paxInfoFormBean);
				}


			},

			setPassengerInfoDetails : function(dataArray) {
				for(var index= 0; index < dataArray.length; index++){
					this.passengerInfoDetails[index].setData(dataArray[index]);
				}
			},

			setEmptyData : function(size) {
				 this.passengerInfoDetails =[];
				for(var index= 0; index < size ; index++){
					var passengerInfoDetail = new PassengerInfoModel();
			        this.passengerInfoDetails.push(passengerInfoDetail);
				}
			}


		});

});
