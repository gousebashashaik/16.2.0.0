define("tui/searchResults/view/flights/FlightTabNavigation", [
	  "dojo",
	  "dojo/has",
	  "dojo/on",
	  'dojo/query',
	  "dojo/io-query",
	  "tui/search/store/SearchPanelMemory",
	  "tui/utils/LocalStorage",
	  "dojo/text!tui/searchResults/view/flights/templates/gridTabNavigation.html"
	  ], function (dojo, has, on, query, ioQuery , SearchPanelMemory, localStorage, gridTabNavigationTmpl) {


	dojo.declare("tui.searchResults.view.flights.FlightTabNavigation", [], {

		 tabAirports 	: "",

		 tabToAirports 	: "",

		 crossSell 		: false,

		 tabId 			: "",

		/******* Properties *******/

		postCreate:function(){
			var flightTabNavigation = this;
		},


		getTabAiports : function(){
			var flightTabNavigation = this;
				if(flightTabNavigation.jsonData.crossSellAirportData){
					flightTabNavigation.crossSell = true;
					if(flightTabNavigation.jsonData.crossSellAirportData.depAirportData.length > 1){
			   			flightTabNavigation.tabAirports = flightTabNavigation.jsonData.crossSellAirportData.depAirportData;
			   			flightTabNavigation.tabToAirports = flightTabNavigation.jsonData.crossSellAirportData.arrAirportData;
			   		}else if(flightTabNavigation.jsonData.crossSellAirportData.arrAirportData.length > 1){
			   			flightTabNavigation.tabAirports = flightTabNavigation.jsonData.crossSellAirportData.arrAirportData;
			   			flightTabNavigation.tabToAirports = flightTabNavigation.jsonData.crossSellAirportData.depAirportData;
			   		}else{
			   			flightTabNavigation.tabAirports=""
			   		}
				}else{
					if(flightTabNavigation.jsonData.depAirportData.length > 1){
			   			flightTabNavigation.tabAirports = flightTabNavigation.jsonData.depAirportData;
			   			flightTabNavigation.tabToAirports = flightTabNavigation.jsonData.arrAirportData;
			   		}else if(flightTabNavigation.jsonData.arrAirportData.length > 1){
			   			flightTabNavigation.tabAirports = flightTabNavigation.jsonData.arrAirportData;
			   			flightTabNavigation.tabToAirports = flightTabNavigation.jsonData.depAirportData;
			   		}else{
			   			flightTabNavigation.tabAirports=""
			   		}
				}
		},

		selectPreviousTab : function(backtoSearch){
			var flightTabNavigation = this,tabId;
			if(backtoSearch && backtoSearch === "backtosearch"){
				flightTabNavigation.removeActiveTab();
				flightTabNavigation.setActiveTab(flightTabNavigation.savedSearch.tabId+"-tab");
				flightTabNavigation.tabId =	flightTabNavigation.savedSearch.tabId;
			}
		},

		renderGridTabNavigation : function(){
			var flightTabNavigation = this,html,data;
				savedSearch = flightTabNavigation.savedSearch;

				if(savedSearch.from[0].countryCode === "GBR"){
					depratingFrom =   savedSearch.to[0].name + " (" + savedSearch.to[0].id + ")";
					if(savedSearch.oneWay){
						titleinfo = "to";
					}else{
						titleinfo = "from and to";
					}
				}else if(savedSearch.to[0].countryCode === "GBR"){
					depratingFrom =   savedSearch.from[0].name + " (" + savedSearch.from[0].id + ")";
					if(savedSearch.oneWay){
						titleinfo = "from";
					}else{
						titleinfo = "to and from";
					}

				}

				if(flightTabNavigation.crossSell && !flightTabNavigation.savedSearch.tabId){
					flightTabNavigation.tabId = flightTabNavigation.tabAirports[0].id;
				}else {
					flightTabNavigation.tabId =	"ALL";
				}


			data = {
					depratingFrom 		: 	depratingFrom,
					titleinfo			:	titleinfo,
					tabAirports			:	flightTabNavigation.tabAirports,
			 		crossSell			:	flightTabNavigation.crossSell,
			 		oneWay				: 	savedSearch.oneWay
			};

			html = dojo.trim(tui.dtl.Tmpl.createTmpl(data, gridTabNavigationTmpl));
			flightTabNavigation.calendarDom = dojo.place(html, query('#searchResultTabContainer', flightTabNavigation.domNode)[0],"replace");
		},


		tabAttachEvents : function(){
			var flightTabNavigation = this;
				on(flightTabNavigation.domNode, on.selector(".tabNavigationAll", "click"), function (event) {
			 		dojo.stopEvent(event);
			 		flightTabNavigation.getSelectedTabNavigation(this, this.parentNode.id);
			     });

			 	on(flightTabNavigation.domNode, on.selector(".tabNavigation", "click"), function (event) {
			 		dojo.stopEvent(event);
			 		flightTabNavigation.getSelectedTabNavigation(this, this.parentNode.id);
			     });

		},


		getSelectedTabNavigation : function(tab, tabId){
			var flightTabNavigation= this,
				airportid=[],
				fromAiports,
				selectedAirportId,
				deptDate,
				retDate,
				results,
				searchType='tabbed',
				targetUrl = dojoConfig.paths.webRoot+"/ws/tabbedflights?",
				savedSearch = flightTabNavigation.savedSearch;
				backtoSearch = flightTabNavigation.savedSearch.searchCriteriaType;

				if(dojo.hasClass(tab.parentNode, "activeTab")){
					return false;
				}

				if(backtoSearch && backtoSearch === "backtosearch"){
					savedSearch.departureDate = savedSearch.oldDepartureDate;
						if(savedSearch.returnDate){
							savedSearch.returnDate = savedSearch.oldReturnDate;
						}
				}

				flightTabNavigation.tabId = tabId.split("-")[0];

				if(tab.hasAttribute("data-airportId")){
					selectedAirportId =tab.getAttribute("data-airportId");
	  			}else{
	  				  _.each(flightTabNavigation.tabAirports, function (airport) {
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



				params = flightTabNavigation.parserJsonData(fromAiports, toAirports, savedSearch.departureDate, savedSearch.returnDate,  searchType );

				dojo.addClass(query('.loadingDivCnt', flightTabNavigation.domNode)[0],"loading");
		  		if(!savedSearch.returnDate){
		  			dojo.addClass(query('.loadingDivCnt', flightTabNavigation.domNode)[0],"oneWayOverlay");
		  		}

		  		results = dojo.xhr("GET", {
		  			url: targetUrl+ ioQuery.objectToQuery(params),
		  			handleAs: "json"
		  		});

		  		 dojo.when(results, function (dateResultsData) {
		  			flightTabNavigation.showSelectedTabFlightDetails(dateResultsData.itinerary, tabId);
		  	      });

		},

		showSelectedTabFlightDetails :  function(resultsItinerarys, tabId){
			var flightTabNavigation= this;
				flightTabNavigation.itinerarys = flightTabNavigation.reGenerateItineraryObject(resultsItinerarys, true);
				flightTabNavigation.itinerarys = _.sortBy(flightTabNavigation.itinerarys, function(o) { return o.pricePP; });

				flightTabNavigation.resultsGridStore = new SearchPanelMemory({data: flightTabNavigation.itinerarys});

				flightTabNavigation.setNextServiceCallDays = 3;

				flightTabNavigation.setPrevServiceCallDays = -3;

				flightTabNavigation.createSelectedDatesCalObject();

				localStorage.removeItem(flightTabNavigation.savedCompId);

		  		flightTabNavigation.renderSearchResults();

		  		flightTabNavigation.mouseOverCellhiglight();

		  		flightTabNavigation.getSelectedFlightDetails();

		  		flightTabNavigation.removeActiveTab();

		  		flightTabNavigation.setActiveTab(tabId);

		},

		setActiveTab : function(tabId){
			var flightTabNavigation= this, tabCnt;
				tabCnt  = query('.tabs', flightTabNavigation.domNode)[0];
				if(tabId){
					dojo.addClass(dojo.byId(tabId),"activeTab");
				}else if(tabCnt){
						dojo.addClass(dojo.byId(tabCnt.children[0].id),"activeTab");
				}
		},

		removeActiveTab: function () {
		    var flightTabNavigation = this,defaultDisplayTabs;
		      	defaultDisplayTabs = dojo.query(".tabs", flightTabNavigation.domNode)[0];

		      	if(!defaultDisplayTabs) return;

		      	for(var i =0; i < defaultDisplayTabs.children.length; i++){
		      		if(dojo.hasClass(defaultDisplayTabs.children[i], "activeTab")){
		      			dojo.removeClass(defaultDisplayTabs.children[i], "activeTab");
		      		}

		      	}
		    }







	});

	return tui.searchResults.view.flights.FlightTabNavigation;
});

