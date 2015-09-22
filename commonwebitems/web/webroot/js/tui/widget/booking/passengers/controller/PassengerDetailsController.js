define("tui/widget/booking/passengers/controller/PassengerDetailsController", [
	"dojo/dom",
    "dojo/on",
    "tui/widget/_TuiBaseWidget",
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/_base/array",
	"dojo/query",
	"dojo/topic",
	"tui/widget/booking/passengers/view/PassengerDetailsComponentPanel",
	"tui/widget/booking/passengers/model/PassengerDetailsModel",
	"dojox/mvc"

    ], function(dom, on, _WidgetBase, declare, lang, arrayUtil, query, topic, PassengerDetailsViewComponent, PassengerDetailsModel){
	/**
	 * @module
	 * @description  Returns a controller
	 */
	return declare("tui.widget.booking.passengers.controller.PassengerDetailsController",[_WidgetBase],  {


		passengerView : null,
		passengerDetailsModel : null,

		postCreate: function () {
			 on(window, 'pageshow', function(e) {
	             if(e.persisted) {
	               // do if page is cached
	           	  window.location.href = "passengerdetails"
	             }
	           });
			this.passengerDetailsModel = new PassengerDetailsModel();
			this.createPassengersView();
			this.inherited(arguments);
		},

		/**
		 * @function
		 * @description creates the company widget.
		 * @Object data
		 *
		 */
		createPassengersView : function() {
			if(this.passengerDetailsFormBean !== ""){
				this.passengerDetailsModel.setData(this.passengerDetailsFormBean);
			}
			var mywidget = dojo.byId("myWidget")
			this.passengerView = new PassengerDetailsViewComponent({
				"jsonData": jsonData,
				"passengermodel": this.passengerDetailsModel.passengerFormDetails
			});
			this.passengerView.setModel(this.model);
			this.passengerView.placeAt(myWidget);
    	}

	});
});