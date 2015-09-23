define("tui/searchResults/view/flights/SelectedFlightDetails", [
	  "dojo",
	  "dojo/has",
	  "dojo/on",
	  'dojo/query',
	  "dojo/io-query",
	  "dojo/parser",
	  "dojo/_base/connect",
	  "tui/search/store/SearchPanelMemory",
	  "dojo/text!tui/searchResults/view/flights/templates/searchResultsFlights.html"
	  ], function (dojo, has, on, query, ioQuery, parser, connect, SearchPanelMemory, searchResultsFlightsTmpl) {


	dojo.declare("tui.searchResults.view.flights.SelectedFlightDetails", [], {

		/******* Properties *******/

		postCreate:function(){
			var selectedFlightDetails = this;
		},

		getPreviousFlightDetails : function(){
			var selectedFlightDetails = this,
  				localSavedSearch,
  				itinerary;
				selectedFlightDetails.selectedDateItinerary =[];
				localSavedSearch = selectedFlightDetails.getPreviousCellobject();

			  	if(localSavedSearch && localSavedSearch.compId){
			  		for(var i=0; i < localSavedSearch.compId.length; i++ ){

			  			itinerary = selectedFlightDetails.resultsGridStore.get(localSavedSearch.compId[i]);

			  			if(itinerary){
			  				selectedFlightDetails.selectedDateItinerary.push(selectedFlightDetails.resultsGridStore.get(localSavedSearch.compId[i]));
			  			}
			  		}
			  		if(itinerary){
			  			selectedFlightDetails.renderFllightsResults(selectedFlightDetails.selectedDateItinerary);
			  		}
			  	}
		},


		getSelectedFlightDetails : function(tdObj, flightSelected){
			var selectedFlightDetails = this,
		  		compId="",
		  		localSavedSearch;
		  		selectedFlightDetails.selectedDateItinerary =[];

		  		if(!tdObj){
		  			tdObj = dojo.query(".srBodySelectedDate",selectedFlightDetails.domNode)[0];

		  			if(!tdObj){
		  				tdObj = dojo.query(".noResultDiv",selectedFlightDetails.domNode)[0];
		  			}
		  		}

		  		/*
		  		 * if arrow select or blank cell select or grid heading select on need to get the flight details
		  		 */

		  		if(dojo.hasClass(tdObj,"arrowstyle") || dojo.hasClass(tdObj,"SRdeptTblHeader") || dojo.hasClass(tdObj,"blank-cell") || dojo.hasClass(tdObj,"SRrettTblHeader") || dojo.hasClass(tdObj,"caption-left") || tdObj.children.length === 0){
			  		return false;
		  		}



			  	for(var i=0; i< tdObj.children.length; i++){
		  			var childObj = tdObj.children[i];
		  			if(childObj.hasAttribute("data-componentId")){
		  				compId =childObj.getAttribute("data-componentId");
		  				selectedFlightDetails.selectedDateItinerary.push(selectedFlightDetails.resultsGridStore.get(compId));
		  			}
		  		}

			  if(selectedFlightDetails.selectedDateItinerary.length >0){
				  selectedFlightDetails.getSelectedDateFlightDeatils(selectedFlightDetails.selectedDateItinerary);
				  if(flightSelected){
				  		if(selectedFlightDetails.savedSearch.returnDate){
				  			window.scrollTo(0, 967);
				  		} else {
				  			window.scrollTo(0, 567);
				  		}
				  }
			  }else{
				  selectedFlightDetails.renderFllightsResults(selectedFlightDetails.selectedDateItinerary);
			  }

		},

		getSelectedDateFlightDeatils : function(selectedDateItinerary){
			var selectedFlightDetails = this,
				savedSearch,
				fromAiports=[],
				toAiports=[],
				from,
				to,
				searchType='selected',
				params,
				targetUrl = dojoConfig.paths.webRoot+"/ws/selectedflights?";
				savedSearch = selectedFlightDetails.savedSearch;

				for(var i=0; i< selectedDateItinerary.length; i++){
					var airport = selectedDateItinerary[i];
						fromAiports.push(airport.outbound.departureAirportData.id);
						toAiports.push(airport.outbound.arrivalAirportData.id);
				}
				departureDate = selectedDateItinerary[0].outDepartureDate;
				returnDate = selectedDateItinerary[0].inDepartureDate;

				fromAiports = _.uniq(fromAiports);
				toAiports = _.uniq(toAiports);

				from = fromAiports.join("|");
		  		to = toAiports.join("|");

				params = selectedFlightDetails.parserJsonData(from, to, departureDate, returnDate, searchType);

				params.tabId = selectedFlightDetails.tabId;

				params.duration = selectedDateItinerary[0].duration;

		  		dojo.addClass(query('#resultinfoCnt', selectedFlightDetails.domNode)[0],"loading");

		  		results = dojo.xhr("GET", {
		  			url: targetUrl+ ioQuery.objectToQuery(params),
		  			handleAs: "json"
		  		});

		  		 dojo.when(results, function (dateResultsData) {
		  			var itinerarys =selectedFlightDetails.reGenerateItineraryObject(dateResultsData.itinerary, true);
		  				itinerarys = _.sortBy(itinerarys, function(o) { return o.pricePP; });
		  				selectedFlightDetails.renderFllightsResults(itinerarys);
		  	      });
	  },


	 renderFllightsResults : function(itinerarys){
		 var selectedFlightDetails = this,html,data;
			data = {
					itineraries 	: itinerarys,
			 		currency 		: selectedFlightDetails.currency,
			 		noResultFlag 	: (itinerarys.length > 0 ) ? false : true,
			 		dreamliner 		: selectedFlightDetails.dreamLiner,
			 		showFlights 	: (itinerarys.length > 1 ) ? true : false,
			 		componentId 	: selectedFlightDetails.individualComponentId,
			 		contextpath		: dojo.config.paths.webRoot

			 };
		 html = dojo.trim(tui.dtl.Tmpl.createTmpl(data, searchResultsFlightsTmpl));

		 selectedFlightDetails.selectedFlightDetails = dojo.place(html, query('#resultinfoCnt', selectedFlightDetails.domNode)[0], "replace");

		 parser.parse(selectedFlightDetails.domNode);

		 dojo.removeClass(query('#resultinfoCnt', selectedFlightDetails.domNode)[0],"loading");

		 if(selectedFlightDetails.selectedDateItinerary.length > 0) {
			 connect.publish("tui:channel=refreshPriceToggle");
		 }

		 selectedFlightDetails.tagElement(selectedFlightDetails.domNode, "individualResults");
	 }


	});

	return tui.searchResults.view.flights.SelectedFlightDetails;
});
