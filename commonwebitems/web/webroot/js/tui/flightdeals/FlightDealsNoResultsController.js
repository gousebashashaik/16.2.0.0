define("tui/flightdeals/FlightDealsNoResultsController",[
        "dojo",
        "dojo/_base/declare",
   	  	"tui/flightdeals/model/DealsPanelModel",
   	  	"dojo/ready",
   	  	"tui/mvc/Controller",
   	  	"tui/widget/_TuiBaseWidget",
   	  	"tui/flightdeals/model/DealsPanelModel"
       ],function(dojo,declare,DealsPanelModel,ready){

	declare("tui.flightdeals.FlightDealsNoResultsController", [tui.widget._TuiBaseWidget, tui.mvc.Controller], {

			 postCreate:function(){
				 var FlightDealsNoResultsController = this,dealsSavedSearch;
				 FlightDealsNoResultsController.dealsPanelModel.dealsFilterStatus = false;
				 ready(function(){
					 	if(FlightDealsNoResultsController.jsonData){
			    		FlightDealsNoResultsController.dealsSavedSearch = FlightDealsNoResultsController.dealsPanelModel.getDealsSavedSearch(FlightDealsNoResultsController.jsonData);

			    		FlightDealsNoResultsController.dealsPanelModel.onRetrieveSavedObject(FlightDealsNoResultsController.dealsSavedSearch);
					 	}
				 })
			 }


		});
		return tui.flightdeals.FlightDealsNoResultsController;
	});