define("tui/searchResults/view/flights/SearchResultNextPreviousCalls", [
	  "dojo",
	  "dojo/has",
	  "dojo/on",
	  'dojo/query',
	  "dojo/io-query",
	  "dojo/parser",
	  "dojo/_base/connect",
	  "tui/search/store/SearchPanelMemory"
	  ], function (dojo, has, on, query, ioQuery, parser, connect, SearchPanelMemory) {
	dojo.declare("tui.searchResults.view.flights.SearchResultNextPreviousCalls", [], {

		/******* Properties *******/

		postCreate:function(){
			var selectedFlightDetails = this;
		},


		  getNextDateFlightsDetails : function(){
			var searchResultCalls = this,startDate,rightArrow,
				savedSearch = searchResultCalls.savedSearch,
				resultlastDate,returnDate,departDate;
				searchResultCalls.calDeptStartDate = dojo.date.add(new Date( searchResultCalls.calDeptStartDate),"day",1);
				searchResultCalls.calDeptEndDate = dojo.date.add(new Date( searchResultCalls.calDeptEndDate),"day",1);
				startDate =  searchResultCalls.calDeptStartDate;
				searchResultCalls.departingObject= searchResultCalls.generateDateObject(startDate);


				if(searchResultCalls.calDeptEndDate.valueOf() > searchResultCalls.seasonEndDate.valueOf() ){
					rightArrow = dojo.query(".next", searchResultCalls.domNode)[0];
	        		dojo.addClass(rightArrow, "arrInactive");
	        		searchResultCalls.calDeptStartDate = dojo.date.add(new Date( searchResultCalls.calDeptStartDate),"day",-1);
					searchResultCalls.calDeptEndDate = dojo.date.add(new Date( searchResultCalls.calDeptEndDate),"day",-1);
	        		return;
				}

				if(savedSearch.returnDate){
					if(dojo.date.difference(searchResultCalls.calRetuEndDate,searchResultCalls.seasonEndDate) === 0){
						searchResultCalls.calRetuStartDate = dojo.date.add(new Date(searchResultCalls.calRetuStartDate),"day",-1);
						searchResultCalls.calRetuEndDate = dojo.date.add(new Date(searchResultCalls.calRetuEndDate),"day",-1);
					}
				}

				resultlastDate = dojo.date.add(new Date(savedSearch.departureDate),"day",searchResultCalls.setNextServiceCallDays);

				if(searchResultCalls.calDeptEndDate.valueOf() > resultlastDate.valueOf()  ){


					//departDate= dojo.date.add(new Date(searchResultCalls.calDeptEndDate),"day",3);

					if(savedSearch.returnDate){
						returnDate = dojo.date.add(new Date(searchResultCalls.calRetuEndDate),"day",1);
					}
					searchResultCalls.callServiceforNextDateFlightsDetails(searchResultCalls.calDeptEndDate,returnDate,"forward");
					searchResultCalls.getPreviousSelectedCellID();
				} else{

				if(savedSearch.returnDate){
					searchResultCalls.templateview="twoWayResult";
					searchResultCalls.calRetuStartDate = dojo.date.add(new Date(searchResultCalls.calRetuStartDate),"day",1);
					searchResultCalls.calRetuEndDate = dojo.date.add(new Date(searchResultCalls.calRetuEndDate),"day",1);

					startDate = searchResultCalls.calRetuStartDate;
					searchResultCalls.returningObject =searchResultCalls.generateDateObject(startDate );
					searchResultCalls.createTwoWayMapObject();
				}else{
					searchResultCalls.templateview="oneWayResult";
					searchResultCalls.createOneWayMapObject();
				}
				searchResultCalls.getPreviousSelectedCellID();
				searchResultCalls.renderSearchResults();
				searchResultCalls.mouseOverCellhiglight();
				}
		  },



		  getPreviousDateFlightsDetails : function(){
			var searchResultCalls = this,startDate,
				savedSearch = searchResultCalls.savedSearch,
				resultStartDate,returnDate,departDate,leftArrow;
				searchResultCalls.calDeptStartDate = dojo.date.add(new Date( searchResultCalls.calDeptStartDate),"day",-1);
				searchResultCalls.calDeptEndDate = dojo.date.add(new Date( searchResultCalls.calDeptEndDate),"day",-1);
				startDate =  searchResultCalls.calDeptStartDate;
				searchResultCalls.departingObject= searchResultCalls.generateDateObject(startDate );

				//previous dates Validation
				if(searchResultCalls.calDeptStartDate.valueOf() <= new Date().valueOf() || searchResultCalls.calDeptStartDate.valueOf() <= searchResultCalls.tracsEndDate.valueOf()){
					leftArrow = dojo.query(".prev", searchResultCalls.domNode)[0];
	        		dojo.addClass(leftArrow, "arrInactive");
	        		searchResultCalls.calDeptStartDate = dojo.date.add(new Date( searchResultCalls.calDeptStartDate),"day",1);
					searchResultCalls.calDeptEndDate = dojo.date.add(new Date( searchResultCalls.calDeptEndDate),"day",1);
	        		return;
				}

				resultStartDate = dojo.date.add(new Date(savedSearch.departureDate),"day",searchResultCalls.setPrevServiceCallDays);

				if(searchResultCalls.calDeptStartDate.valueOf() < resultStartDate.valueOf()){

					//departDate= dojo.date.add(new Date(searchResultCalls.calDeptStartDate),"day",-3);

					if(savedSearch.returnDate){
						returnDate = dojo.date.add(new Date(searchResultCalls.calRetuStartDate),"day",-1);
					}
					searchResultCalls.callServiceforNextDateFlightsDetails(searchResultCalls.calDeptStartDate,returnDate,"backward");
					searchResultCalls.getPreviousSelectedCellID();
				}else{
					if(savedSearch.returnDate){
						searchResultCalls.templateview="twoWayResult";
						searchResultCalls.calRetuStartDate = dojo.date.add(new Date(searchResultCalls.calRetuStartDate),"day",-1);
						searchResultCalls.calRetuEndDate = dojo.date.add(new Date(searchResultCalls.calRetuEndDate),"day",-1);

						startDate = searchResultCalls.calRetuStartDate;
						searchResultCalls.returningObject =searchResultCalls.generateDateObject(startDate );
						searchResultCalls.createTwoWayMapObject();
					}else{
						searchResultCalls.templateview="oneWayResult";
						searchResultCalls.createOneWayMapObject();
					}

				searchResultCalls.getPreviousSelectedCellID();
				searchResultCalls.renderSearchResults();
				searchResultCalls.mouseOverCellhiglight();
				}
		  },


		  callServiceforNextDateFlightsDetails : function(departDate,returnDate,direction){
			  	var searchResultCalls = this,
			  		params,
			  		results,
			  		tab,
			  		selectedAirportId,
			  		airportid=[],
			  		searchType='navigation',
			  		savedSearch = searchResultCalls.savedSearch;
			  		targetUrl = dojoConfig.paths.webRoot+"/ws/searchnav?";


			  		tab = dojo.query(".activeTab .tabNavigation")[0];
			  		if(tab && tab.hasAttribute("data-airportId")){
						selectedAirportId =tab.getAttribute("data-airportId");
		  			}else{
		  				  _.each(searchResultCalls.tabAirports, function (airport) {
				   			airportid.push(airport.id);
				   	      });
		  				selectedAirportId = airportid.join("|");
		  			}


			  		if(savedSearch.from[0].countryCode === "GBR"){
						fromAiports = selectedAirportId;
						toAirports = savedSearch.to[0].id;
					}else if(savedSearch.to[0].countryCode === "GBR"){
						fromAiports = savedSearch.from[0].id
						toAirports = selectedAirportId
					}

			  		params = searchResultCalls.parserJsonData(fromAiports, toAirports, departDate,returnDate, searchType );

			  		dojo.addClass(query('.loadingDivCnt', searchResultCalls.domNode)[0],"loading");
			  		if(!searchResultCalls.savedSearch.returnDate){
			  			dojo.addClass(query('.loadingDivCnt', searchResultCalls.domNode)[0],"oneWayOverlay");
			  		}


			  		results = dojo.xhr("GET", {
			  			url: targetUrl+ ioQuery.objectToQuery(params),
			  			handleAs: "json"
			  		});

			  		 dojo.when(results, function (dateResultsData) {
			  			searchResultCalls.nextPrevServiceItineraryDataProcessing(dateResultsData.itinerary,direction);
			  	      });

		  },

		  nextPrevServiceItineraryDataProcessing : function(resultsItinerarys, direction){
			  	var searchResultCalls = this,
			  		days=0;
			  		searchResultCalls.itinerarys = searchResultCalls.reGenerateItineraryObject(resultsItinerarys, true);

				  	searchResultCalls.itinerarys = _.sortBy(searchResultCalls.itinerarys, function(o) { return o.pricePP; });

				  	if(direction== "forward"){
			  			days =1;

			  			if(searchResultCalls.setNextServiceCallDays == 3 && searchResultCalls.tracsDays > 2){
			  				searchResultCalls.setPrevServiceCallDays = -2;
			  			}
			  			if(searchResultCalls.setNextServiceCallDays == 2  && searchResultCalls.tracsDays > 2){
			  				searchResultCalls.setPrevServiceCallDays = -3;
			  			}

			  			searchResultCalls.setNextServiceCallDays = searchResultCalls.setNextServiceCallDays + 7;

			  			if(searchResultCalls.setNextServiceCallDays == 10 && searchResultCalls.setPrevServiceCallDays == -10){
			  				searchResultCalls.setPrevServiceCallDays = -3;
			  				searchResultCalls.compareCalStartDate = dojo.date.add(new Date(searchResultCalls.compareCalStartDate),"day",7);
			  			}
			  			if(searchResultCalls.setNextServiceCallDays > 10 || searchResultCalls.setPrevServiceCallDays  < -10){
			  				searchResultCalls.compareCalStartDate = dojo.date.add(new Date(searchResultCalls.compareCalStartDate),"day",7);
			  				searchResultCalls.setPrevServiceCallDays = searchResultCalls.setPrevServiceCallDays +7;
			  			}
			  			searchResultCalls.compareCalEndtDate = dojo.date.add(new Date(searchResultCalls.compareCalEndtDate),"day",7);
			  		}
			  		else{
			  			days =-1;

			  			if(searchResultCalls.setPrevServiceCallDays == -3){
			  				searchResultCalls.setNextServiceCallDays = 2;
			  			}if(searchResultCalls.setPrevServiceCallDays == -2){
			  				searchResultCalls.setNextServiceCallDays = 3;
			  			}

			  			searchResultCalls.setPrevServiceCallDays = searchResultCalls.setPrevServiceCallDays - 7;

			  			if(searchResultCalls.setNextServiceCallDays == 10 && searchResultCalls.setPrevServiceCallDays == -10 ){
			  				searchResultCalls.setNextServiceCallDays = 3;
			  				searchResultCalls.compareCalEndtDate = dojo.date.add(new Date(searchResultCalls.compareCalEndtDate),"day",-7);
			  			}
			  			if(searchResultCalls.setPrevServiceCallDays  < -10 || searchResultCalls.setNextServiceCallDays > 10){
			  				searchResultCalls.compareCalEndtDate = dojo.date.add(new Date(searchResultCalls.compareCalEndtDate),"day",-7);
			  				searchResultCalls.setNextServiceCallDays = searchResultCalls.setNextServiceCallDays -7;
			  			}
			  			searchResultCalls.compareCalStartDate = dojo.date.add(new Date(searchResultCalls.compareCalStartDate),"day",-7);
			  		}

			  		searchResultCalls.resultsGridStore = new SearchPanelMemory({data: searchResultCalls.itinerarys});
			  		searchResultCalls.calRetuStartDate = dojo.date.add(new Date(searchResultCalls.calRetuStartDate),"day",days);
					searchResultCalls.calRetuEndDate = dojo.date.add(new Date(searchResultCalls.calRetuEndDate),"day",days);

					startDate = searchResultCalls.calRetuStartDate;
					searchResultCalls.returningObject =searchResultCalls.generateDateObject(startDate );

					if(searchResultCalls.savedSearch.returnDate){
						searchResultCalls.templateview="twoWayResult";
						searchResultCalls.createTwoWayMapObject();
					}else{
						searchResultCalls.templateview="oneWayResult";
						searchResultCalls.createOneWayMapObject();
					}

			  		searchResultCalls.renderSearchResults();
			  		searchResultCalls.mouseOverCellhiglight();
		  }



	});

	return tui.searchResults.view.flights.SearchResultNextPreviousCalls;
});
