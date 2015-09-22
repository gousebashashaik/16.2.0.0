define("tui/searchResults/view/flights/NoSearchResultsController", [
	  "dojo",
	  "tui/searchPanel/model/flights/SearchPanelModel",
	  "tui/mvc/Controller",
	  "tui/widget/_TuiBaseWidget"
	  ], function (dojo, SearchPanelModel) {

	dojo.declare("tui.searchResults.view.flights.NoSearchResultsController", [tui.widget._TuiBaseWidget, tui.mvc.Controller], {

		 postCreate:function(){
		    	var NoSearchResultsController = this;
		    		if(!NoSearchResultsController.jsonData.flightSearchCriteria) return;
		    		NoSearchResultsController.reGenerateSavedSearchObject();

		    		NoSearchResultsController.searchPanelModel.prePopulateSearchCriteria(NoSearchResultsController.jsonData.flightSearchCriteria);
		    		NoSearchResultsController.onLazyLoad();
		    		NoSearchResultsController.tagElement(NoSearchResultsController.domNode, "nosearchResult");
		    },

		    reGenerateSavedSearchObject :  function(){
				var NoSearchResultsController = this;

				//Date object convert string to int object
				NoSearchResultsController.jsonData.flightSearchCriteria.departureDate = NoSearchResultsController.setDateFormat(NoSearchResultsController.jsonData.flightSearchCriteria.departureDate)
				if(NoSearchResultsController.jsonData.flightSearchCriteria.returnDate){
					NoSearchResultsController.jsonData.flightSearchCriteria.returnDate = NoSearchResultsController.setDateFormat(NoSearchResultsController.jsonData.flightSearchCriteria.returnDate)
				}
				if(NoSearchResultsController.jsonData.depAirportData){
					NoSearchResultsController.jsonData.flightSearchCriteria.from = NoSearchResultsController.jsonData.depAirportData;
		    		NoSearchResultsController.jsonData.flightSearchCriteria.to = NoSearchResultsController.jsonData.arrAirportData;
				}
			},

			setDateFormat : function(dateObj){
		    	var NoSearchResultsController = this,newDate,
		    		parts = dateObj.split('-');
		    		newDate = new Date(parts[0],parts[1]-1,parts[2]);
		    		return newDate;
		    },

		    onLazyLoad: function () {
			      var searchController = this;

			      setTimeout(function () {
			        // publish update to child-ages, chrome/safari has issues with caching values
			        dojo.publish("tui.searchPanel.view.ChildSelectOption.updateChildAgeValues");
			      }, 300);
			    }
	});
	return tui.searchResults.view.flights.NoSearchResultsController;
});