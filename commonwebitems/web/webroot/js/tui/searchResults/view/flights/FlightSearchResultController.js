define("tui/searchResults/view/flights/FlightSearchResultController", [
	  "dojo",
	  "dojo/has",
	  "dojo/on",
	  'dojo/query',
	  "dojo/topic",
	  "dojo/_base/connect",
	  "dojo/_base/lang",
	  "dojo/dom-attr",
	  "dojo/io-query",
	  "dojo/dom-class",
	  "dojo/parser",
	  "dojo/dom-construct",
	  "dojo/text!tui/searchResults/view/flights/templates/searchResultsGrid.html",
	  "dojo/text!tui/searchResults/view/flights/templates/searchResultsFlights.html",
	  "tui/search/store/SearchPanelMemory",
	  "tui/searchPanel/model/flights/SearchPanelModel",
	  "tui/searchPanel/model/AirportModel",
	  "tui/searchPanel/model/DestinationModel",
	  "tui/utils/LocalStorage",
	  "dojo/_base/json",
	  "tui/search/nls/Searchi18nable",
	  "tui/mvc/Controller",
	  "tui/widget/mixins/Templatable",
	  "tui/widget/_TuiBaseWidget",
	  "dojo/NodeList-traverse",
	  "tui/searchResults/view/Tooltips",
	  "tui/searchResults/view/PriceToggle"
	  ], function (dojo, has, on, query, topic, connect, lang, domAttr, ioQuery, domClass, parser, domConstruct,searchResultsGridTmpl,searchResultsFlightsTmpl ,SearchPanelMemory, SearchPanelModel, AirportModel,DestinationModel, localStorage, json) {


	dojo.declare("tui.searchResults.view.flights.FlightSearchResultController", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable, tui.mvc.Controller, tui.search.nls.Searchi18nable], {


		 tmpl: searchResultsGridTmpl,

		 resultsGridStore: null,

		 savedSearch: null,

		 calSelectedDeptDate : null,

		 calSelectedRetuDate : null,

		 calDeptStartDate  : null,

	 	 calDeptEndDate : null,

	 	 calRetuStartDate  : null,

	 	 calRetuEndDate : null,

		 calStartDays : -3,

		 calEndDays : +3,

		 calReturnStartDays : -3,

		 calReturnEndDays : +3,

		 departingObject :[],

		 returningObject : [],

		 seasonEndDate : null,

		 tracsEndDate :null,

		 currency : dojoConfig.currency,

		 selectedDateItinerary : [],

		 savedCompId : "SelectedflightDetails",

		 setNextServiceCallDays : 3,

		 setPrevServiceCallDays : -3,

		 tracsDays : 0,

		 compareCalStartDate :null,

		 compareCalEndtDate :null,

		 pricegridComponentId : "COM_FO_SEARCH_RESULTS_PRICE_GRID",

		 individualComponentId : "COM_FO_SEARCH_RESULTS_INDIVIDUAL",

		 /******* Properties *******/
		constructor: function () {
			var searchController = this;
				searchController.itinerarys =searchController.jsonData.itinerary;
				searchController.dreamLiner = searchController.jsonData.dreamLiner;
				searchController.savedSearch =searchController.jsonData.flightSearchCriteria;
				searchController.tracsEndDate =  new Date(searchController.setDateFormat(searchController.jsonData.tracsEndDate));
				searchController.seasonEndDate =  new Date(searchController.setDateFormat(searchController.jsonData.seasonEndDate));
				//console.log(searchController.resultsGridStore.get(151023151031322.5));
		},

		postCreate:function(){
			var searchController = this;
				searchController.currency = searchController.currency;
				searchController.reGenerateSavedSearchObject();
				searchController.itinerarys = searchController.reGenerateItineraryObject(searchController.itinerarys);
				searchController.itinerarys = _.sortBy(searchController.itinerarys, function(o) { return o.pricePP; });
				searchController.searchPanelModel.prePopulateSearchCriteria(searchController.jsonData.flightSearchCriteria);
				searchController.resultsGridStore = new SearchPanelMemory({data: searchController.itinerarys});
				searchController.createSelectedDatesCalObject();
				searchController.createCompareCalObject();
				localStorage.removeItem(searchController.savedCompId);
				searchController.renderSearchResults();
				searchController.renderFllightsResults();
				searchController.attachEvents();
				searchController.inherited(arguments);
				searchController.onLazyLoad();
				searchController.tagElement(searchController.domNode, "PriceGrid");
				dojo.addOnLoad(function(){
					if(dojo.docScroll().y !== 0){
						window.scrollTo(0,0);
					}
				});

		},

		  onLazyLoad: function () {
		      var searchController = this;

		      setTimeout(function () {
		        // publish update to child-ages, chrome/safari has issues with caching values
		        dojo.publish("tui.searchPanel.view.ChildSelectOption.updateChildAgeValues");
		      }, 300);
		    },

		reGenerateSavedSearchObject :  function(){
			var searchController = this;

			//Date object convert string to int object
			searchController.savedSearch.departureDate = searchController.setDateFormat(searchController.savedSearch.departureDate)
			if(searchController.savedSearch.returnDate){
				searchController.savedSearch.returnDate = searchController.setDateFormat(searchController.savedSearch.returnDate)
			}
			if(searchController.jsonData.depAirportData){
				searchController.jsonData.flightSearchCriteria.from = searchController.jsonData.depAirportData;
				searchController.jsonData.flightSearchCriteria.to = searchController.jsonData.arrAirportData;
			}
		},

		reGenerateItineraryObject : function(itinerarys){
			var searchController = this;

				return _.map(lang.clone(itinerarys), function (itinerary, index) {
					itinerary.showComp = true;

					if (itinerary.outbound ) {
						itinerary.outDepartureDate = searchController.removeHoursFromDateObject(itinerary.outbound.schedule.departureDate);
			    		itinerary.outbound.schedule.depDate = new Date(itinerary.outbound.schedule.departureDate).toDateString();
			    		itinerary.outbound.schedule.depObj = searchController.splitDateString(new Date(itinerary.outbound.schedule.departureDate));

			    		itinerary.outbound.schedule.arrDate = new Date(itinerary.outbound.schedule.arrivalDate).toDateString();
			    		itinerary.outbound.schedule.arrObj = searchController.splitDateString(new Date(itinerary.outbound.schedule.arrivalDate));

					}

				    if (itinerary.inbound) {
				    		itinerary.inDepartureDate = searchController.removeHoursFromDateObject(itinerary.inbound.schedule.departureDate);
				    		itinerary.inbound.schedule.depDate = new Date(itinerary.inbound.schedule.departureDate).toDateString();
				    		itinerary.inbound.schedule.depObj = searchController.splitDateString(new Date(itinerary.inbound.schedule.departureDate));

				    		itinerary.inbound.schedule.arrDate = new Date(itinerary.inbound.schedule.arrivalDate).toDateString();
				    		itinerary.inbound.schedule.arrObj = searchController.splitDateString(new Date(itinerary.inbound.schedule.arrivalDate));

				    }
					return itinerary;
				});
		},

		 setDateFormat : function(dateObj){
		    	var searchController = this,newDate,
		    		parts = dateObj.split('-');
		    		newDate = new Date(parts[0],parts[1]-1,parts[2]);
		    		return newDate;
		    },

		  removeHoursFromDateObject : function(dateObj){
		    	var searchController = this,
		    		newDt = new Date(dateObj);
		    		return  new Date(newDt.setHours(0,0,0,0))
		    },

		createSelectedDatesCalObject : function(){
			var searchController= this,
				savedSearch =searchController.savedSearch;

				searchController.createDeptDate(savedSearch);

				if(savedSearch.returnDate){
					searchController.templateview="twoWayResult";
					searchController.createRetunDate(savedSearch);
					searchController.createTwoWayMapObject();
				}else{
					searchController.templateview="oneWayResult";
					searchController.createOneWayMapObject();
				}
		 },


		createDeptDate : function(savedSearch){
			var searchController= this,
				startDate=null,
				nextDay = dojo.date.add( new Date(),"day",1),
				seasonEndDate = searchController.seasonEndDate,
				days,
				tracsDays,
				endDays;

				//Cal Start Date Validation
				searchController.calSelectedDeptDate = new Date(savedSearch.departureDate);
			 	days = dojo.date.difference(nextDay, searchController.calSelectedDeptDate,"day");
			 	//tracs EndDate Validation
			 	if(searchController.tracsEndDate){
			 		tracsDays = dojo.date.difference(searchController.tracsEndDate,searchController.calSelectedDeptDate)
			 		if(tracsDays < -1 || tracsDays == 1){
			 			tracsDays=0;
			 		}
			 		else if(tracsDays == 2){
			 			tracsDays=1;
			 		} else if(tracsDays == 3){
			 			tracsDays=2;
			 		}
			 	}
			 	searchController.tracsDays = tracsDays;
			 	days +=1;

			 	if(tracsDays <= 2){
			 		searchController.setPrevServiceCallDays = 1;
			 		searchController.setNextServiceCallDays =3;
			 	}

			 	if(days == 0 || tracsDays == 0){
			 		searchController.calStartDays = 0;
			 		searchController.calEndDays = 6;
			 	}else if(days == 1||  tracsDays == 1){
			 		searchController.calStartDays = -1;
			 		searchController.calEndDays = 5;
			 	}else if(days == 2 ||  tracsDays == 2){
			 		searchController.calStartDays = -2;
			 		searchController.calEndDays = 4;
			 	}


			 	//Cal End Date Validation
				endDays = dojo.date.difference(searchController.calSelectedDeptDate, seasonEndDate , "day");
				endDays+1;

				if(endDays == 0){
			 		searchController.calStartDays = -6;
			 		searchController.calEndDays = 0;
			 	}else if(endDays == 1){
			 		searchController.calStartDays = -5;
			 		searchController.calEndDays = 1;
			 	}else if(endDays == 2){
			 		searchController.calStartDays = -4;
			 		searchController.calEndDays = 2;
			 	}

			 	searchController.calDeptStartDate = dojo.date.add(new Date(savedSearch.departureDate),"day",searchController.calStartDays);
			 	searchController.calDeptEndDate = dojo.date.add( new Date(savedSearch.departureDate),"day",searchController.calEndDays);
			 	startDate= searchController.calDeptStartDate;

			 	searchController.departingObject = searchController.generateDateObject(startDate);

		},

		createRetunDate : function(savedSearch){
			var searchController= this,
				startDate=null,
				nextDay = dojo.date.add( new Date(),"day",1),
				seasonEndDate = searchController.seasonEndDate,
				days,
				endDays;

				//Cal Start Date Validation
				searchController.calSelectedRetuDate = new Date(savedSearch.returnDate);
			 	days = dojo.date.difference(nextDay, searchController.calSelectedRetuDate,"day");
			 	days +=1;

			 	if(days == 0){
			 		searchController.calReturnStartDays = 0;
			 		searchController.calReturnEndDays = 6;
			 	}else if(days == 1){
			 		searchController.calReturnStartDays = -1;
			 		searchController.calReturnEndDays = 5;
			 	}else if(days == 2){
			 		searchController.calReturnStartDays = -2;
			 		searchController.calReturnEndDays = 4;
			 	}

			 	//Cal End Date Validation
				endDays = dojo.date.difference(searchController.calSelectedRetuDate, seasonEndDate , "day");
				endDays+1;

				if(endDays == 0){
			 		searchController.calReturnStartDays = -6;
			 		searchController.calReturnEndDays = 0;
			 	}else if(endDays == 1){
			 		searchController.calReturnStartDays = -5;
			 		searchController.calReturnEndDays = 1;
			 	}else if(endDays == 2){
			 		searchController.calReturnStartDays = -4;
			 		searchController.calReturnEndDays = 2;
			 	}


		 		searchController.calRetuStartDate = dojo.date.add(new Date(savedSearch.returnDate),"day",searchController.calReturnStartDays);
			 	searchController.calRetuEndDate = dojo.date.add( new Date(savedSearch.returnDate),"day",searchController.calReturnEndDays);
			 	startDate = searchController.calRetuStartDate;

			 	searchController.returningObject = searchController.generateDateObject(startDate);


		},

		  generateDateObject : function(startDate ){
			  var searchController= this, dateObject =[];
			  for(var i = 0; i < 7;  i++ ){
			 		var dtObj =searchController.splitDateString(startDate);
			 			dateObject.push(dtObj);
						startDate = dojo.date.add(new Date(startDate),"day",1);
				}
			  return dateObject;
		  },

		splitDateString : function(dt){
			var obj={},tempdate,
				dateObj = dt.toDateString();
				obj['dateValue'] = dt.valueOf();
				obj['curDate'] =  dateObj;
				obj['day'] = dateObj.substring(0,3);
				obj['month'] = dateObj.substring(4,7);
				tempdate= dateObj.substring(8,10).replace(/^\s+|\s+$/g, '');
				obj['date'] = (tempdate.length > 1) ? tempdate : "0"+tempdate;
				return obj
		},

		createOneWayMapObject : function(){
			var searchController= this,
				deptObj = searchController.departingObject,
				calSelectedDeptDate =searchController.calSelectedDeptDate,
				itinerarys = searchController.itinerarys;

			for(var i = 0; i < deptObj.length; i++ ){
				var dept  = deptObj[i];

				for(var k = 0; k < itinerarys.length; k++){
					var itinerary = itinerarys[k];

					if(dept.curDate == itinerary.outbound.schedule.depDate){
								if(deptObj[i].itinerary ){
									itinerary = searchController.checkOnewayLowPriceObject(deptObj[i].itinerary ,itinerary);
									deptObj[i].itinerary.push(itinerary);
								}else{
									deptObj[i]['itinerary'] = [];
									deptObj[i]['itinerary'].push(itinerary);
							}
								if(calSelectedDeptDate.toDateString() === dept.curDate){
									searchController.selectedDateItinerary.push(itinerary)
								}
						}
					}
				}
		},



		 checkOnewayLowPriceObject : function(arrayObj , obj){
			 var searchController= this;

			 for(var i =0; i< arrayObj.length; i++ ){
				 if(arrayObj[i].outbound.schedule.depDate == obj.outbound.schedule.depDate){
				 	var price1 = parseInt(arrayObj[i].pricePP);
					var price2 = parseInt(obj.pricePP);

					if(price2 == price1  ){
						obj.showComp=false;
						break;
					}

					if(price2 > price1  ){
						arrayObj[i].lowPrice = true;
						obj.showComp=false;
					}

				 }
			 }




			 return obj;
		 },

		createTwoWayMapObject : function(){
			var searchController= this,
				calSelectedDeptDate =searchController.calSelectedDeptDate,
				calSelectedRetuDate = searchController.calSelectedRetuDate,
				deptObj = searchController.departingObject,
				retunObj = searchController.returningObject,
				itinerarys = searchController.itinerarys;


			for(var i = 0; i < deptObj.length; i++ ){
				var dept  = deptObj[i];
				for(var k = 0; k < itinerarys.length; k++){
					var itinerary = itinerarys[k];

					if(dept.curDate == itinerary.outbound.schedule.depDate){

						for(var j = 0; j<retunObj.length; j++ ){
							var retun = retunObj[j];

							if(retun.curDate == itinerary.inbound.schedule.depDate ){

								if(itinerary.duration < 2){
									itinerary.durationFlag = true;
								}else{
									itinerary.durationFlag = false;
								}
								//Get Selected dates Itinery Object
								if(calSelectedDeptDate.toDateString() === dept.curDate && calSelectedRetuDate.toDateString() === retun.curDate){
									itinerary.selectedDateObj = true;
									searchController.selectedDateItinerary.push(itinerary);
								}
								//Push selected itinery to return date object
								if(retunObj[j].itinerary){
									itinerary = searchController.checkTwowayLowPriceObject(retunObj[j].itinerary ,itinerary);
									retunObj[j].itinerary.push(itinerary);
								}else{
									retunObj[j]['itinerary'] = [];
									retunObj[j]['itinerary'].push(itinerary);
								}

								//Push selected itinery to departing date object
								if(deptObj[i].itinerary ){
									deptObj[i].itinerary.push(itinerary);
								}else{
									deptObj[i]['itinerary'] = [];
									deptObj[i]['itinerary'].push(itinerary);
								}

							}
						}
					}
				}
			}
		 },

		 checkTwowayLowPriceObject : function(arrayObj , obj){
			 var searchController= this;

			 for(var i =0; i< arrayObj.length; i++ ){
				 if(arrayObj[i].inbound.schedule.depDate === obj.inbound.schedule.depDate && arrayObj[i].outbound.schedule.depDate == obj.outbound.schedule.depDate){
				 	var price1 = parseInt(arrayObj[i].pricePP);
					var price2 = parseInt(obj.pricePP);

					if(price2 == price1  ){
						arrayObj[i].showComp = false;
					}

					if(price2 > price1 ){
						obj.showComp = false;
					}
				 }
			 }

			 return obj;
		 },



		renderSearchResults: function(){
			var searchController = this,html,data,selectedDeptDate,selectedRetuDate,
			localSavedSearch = searchController.getPreviousCellobject();
			if(localSavedSearch){
				selectedDeptDate = localSavedSearch.selectedDeptDate
				selectedRetuDate = localSavedSearch.selectedRetuDate
			}

			data = {
					 departureDates:searchController.departingObject,
			 		 returnDates :searchController.returningObject,
			 		 currency : searchController.currency,
			 		 templateview : searchController.templateview,
			 		 depratingFrom : searchController.savedSearch.to.name,
			 		 departingFromCode : searchController.savedSearch.to.id,
			 		 calSelectedDeptDate :searchController.calSelectedDeptDate.toDateString(),
			 		 componentId : searchController.pricegridComponentId,
			 		 calSelectedRetuDate : (searchController.savedSearch.returnDate) ? searchController.calSelectedRetuDate.toDateString() : false,
			 		 selectedDeptDate: selectedDeptDate,
			 		 selectedRetuDate : selectedRetuDate


			};
			html = dojo.trim(tui.dtl.Tmpl.createTmpl(data, searchResultsGridTmpl));
			searchController.calendarDom = dojo.place(html, query('#searchResultContainer', searchController.domNode)[0],"replace");
			dojo.query(".srMultiAirport", searchController.domNode).prev("div.srTdcnt").children(".priceperperson").addClass("multiAirport");
			dojo.query(".srMultiAirport", searchController.domNode).next("div.srTdcnt").children(".priceperperson").addClass("multiAirport");
			searchController.checkNoResultCondition();

			searchController.seasonStartDateValidate();

			searchController.seasonEndDateValidate();

			//Highlight Cells of the grid when exact search criteria is found
			searchController.highlightMiddleCells();
			searchController.highlightpreviousCell();
			dojo.removeClass(query('.loadingDivCnt', searchController.domNode)[0],"loading");
			if(!searchController.savedSearch.returnDate){
	  			dojo.addClass(dojo.byId("searchResult"),"oneWayOverlay");
	  		}

			//tracs EndDate Validation
		 	if(searchController.tracsEndDate){
		 		tracsDays = dojo.date.difference(searchController.tracsEndDate,searchController.calDeptStartDate)
		 		if(tracsDays < -1 || tracsDays == 1){
		 			dojo.query(".prev", searchController.domNode).addClass("arrInactive");
		 		}
		 	}

		 },

		 renderFllightsResults : function(){
			 var searchController = this,html,data;
			data = {
					itineraries : searchController.selectedDateItinerary,
			 		currency : searchController.currency,
			 		noResultFlag : (searchController.selectedDateItinerary.length > 0 ) ? false : true,
			 		dreamliner : searchController.dreamLiner,
			 		showFlights : (searchController.selectedDateItinerary.length > 1 ) ? true : false,
			 		componentId : searchController.individualComponentId

			 };
			 html = dojo.trim(tui.dtl.Tmpl.createTmpl(data, searchResultsFlightsTmpl));

			 searchController.selectedFlightDetails = dojo.place(html, query('#resultinfoCnt', searchController.domNode)[0], "replace");

			 parser.parse(searchController.domNode);

			 if(searchController.selectedDateItinerary.length > 0) {
				 connect.publish("tui:channel=refreshPriceToggle");
			 }

			 searchController.tagElement(searchController.domNode, "individualResults");
		 },


		 seasonStartDateValidate: function(){
			var searchController =this,
				leftArrow = dojo.query(".prev", searchController.domNode)[0];
			 if(searchController.calDeptStartDate.valueOf() < new Date().valueOf()){
	        		dojo.addClass(leftArrow, "arrInactive");
				}else{
					dojo.query(leftArrow).removeClass("arrInactive");
				}
		 },

		 seasonEndDateValidate : function(){
			var searchController =this,
			 	rightArrow = dojo.query(".next", searchController.domNode)[0];
			 if(searchController.calDeptEndDate.valueOf() >= searchController.seasonEndDate.valueOf() ){
	        		dojo.addClass(rightArrow, "arrInactive");
				}else{
					dojo.query(rightArrow).removeClass("arrInactive");
				}
		 },

		 attachEvents: function () {
				var searchController = this;
				 	on(searchController.domNode, "td:click", function (event) {
				 		dojo.stopEvent(event);
						if(dojo.query(event.target).closest(".noResultDiv").length === 1) return false;
				 		searchController.getSelectedFlightDetails(this);
				 		searchController.highlightselectedCell(this);
				     });

				 	on(searchController.domNode, on.selector("#rightArrow", "click"), function (event) {
				 		dojo.stopEvent(event);
				 		searchController.getNextDateFlightsDetails();
				     });


				 	on(searchController.domNode, on.selector("#leftArrow", "click"), function (event) {
				 		dojo.stopEvent(event);
				 		searchController.getPreviousDateFlightsDetails();
				     });

				 	//Attach onmouseover and mouseout effects;
				 	searchController.mouseOverCellhiglight();


		},

		mouseOverCellhiglight : function(){
			 var searchController = this;
			dojo.query(".searchResultTable tr td.flex-grid-cell").on("mouseover,mouseout",function(tdElem){
				var closestTd = dojo.query(tdElem.target).closest("td")[0];
				if(searchController.savedSearch.returnDate){
			 		//Get Selected cells of the top rows


			 		dojo.query(closestTd.parentNode).prevAll().children(':nth-child(' + (closestTd.cellIndex+1) + ')').toggleClass("hover");
			 		//Get Selected cells of the left td's
			 		dojo.query(closestTd).prevAll().toggleClass("hover");

			 		if(dojo.query(closestTd).children(".srTdcnt").length !== 0) {
			 			if(!dojo.hasClass(closestTd,"srDefault") && !dojo.hasClass(closestTd,"srClicked"))
			 			dojo.query(closestTd).toggleClass("srBodySelectedDate");
			 		} else {
			 			dojo.query(closestTd).toggleClass("hover");
			 		}
			 		dojo.query(".SRdeptTblHeader").removeClass("hover");
			 		if(closestTd.parentNode.rowIndex !== 1){
			 			dojo.toggleClass(dojo.query(".searchResultTable tr")[closestTd.parentNode.rowIndex+15].children[0],"hover");
			 		}else {
			 			dojo.toggleClass(dojo.query(".searchResultTable tr")[closestTd.parentNode.rowIndex+8].children[1],"hover");
			 		}
				} else {
					if(dojo.query(closestTd).children(".srTdcnt").length !== 0){
						if(!dojo.hasClass(closestTd,"srDefault") && !dojo.hasClass(closestTd,"srClicked") && !dojo.hasClass(closestTd,"blank-cell")  )
						dojo.query(closestTd).toggleClass("srBodySelectedDate");
					}
				}
		 		});
		},

		  highlightselectedCell : function(tdObj){
			  var searchController = this;
			  if(dojo.hasClass(tdObj,"SRrettTblHeader") || dojo.hasClass(tdObj,"SRdeptTblHeader") || dojo.hasClass(tdObj,"noResultDiv") || dojo.hasClass(tdObj,"searchResultDeptDatesTd") || dojo.hasClass(tdObj,"caption-left")) return;
			  if(tdObj.children.length < 1) return;
			 /* if(tdObj.children.length > 0 && tdObj.children[0].children.length < 1) return;*/
			  if(!dojo.query(".srTdcnt",tdObj) || dojo.hasClass(tdObj, "arrowstyle") || dojo.hasClass(tdObj, "blank-cell")) return;
			  if(dojo.query(".srBodySelectedDate",searchController.domNode).length > 0){
				  dojo.query(".srBodySelectedDate",searchController.domNode).removeClass("srBodySelectedDate srClicked srDefault");
			  }
			  dojo.addClass(tdObj,"srBodySelectedDate srClicked");
			  if(searchController.savedSearch.returnDate){
				  searchController.highlightActiveCells(tdObj);
			  }
		  },
		  highlightActiveCells: function(tdObj){
			var searchController = this;
			dojo.query("#searchResultContainer .active",searchController.domNode).removeClass("active");

			dojo.query(tdObj.parentNode).prevAll().children(':nth-child(' + (tdObj.cellIndex+1) + ')').addClass("active");
			dojo.query(tdObj).prevAll().addClass("active");

			dojo.query(".SRdeptTblHeader").removeClass("active");

			if(tdObj.parentNode.rowIndex !== 1){
				dojo.toggleClass(dojo.query(".searchResultTable tr")[tdObj.parentNode.rowIndex+15].children[0],"active");
			}else {
				dojo.toggleClass(dojo.query(".searchResultTable tr")[tdObj.parentNode.rowIndex+8].children[1],"active");
			}

		  },

		  highlightMiddleCells: function(){
			  var searchController = this,tdObj;
			  if(searchController.savedSearch.returnDate){
				  if(dojo.query(".srBodySelectedDate",searchController.domNode).length === 0 && dojo.query(".noResultDiv",searchController.domNode).length === 0){
					  return;
				  } else {
					  	if(dojo.query(".srBodySelectedDate",searchController.domNode)[0] !== undefined){
						  tdObj = dojo.query(".srBodySelectedDate",searchController.domNode)[0];
				  		} else {
				  			tdObj = dojo.query(".noResultDiv",searchController.domNode)[0];
				  		}
				  }

				  searchController.highlightActiveCells(tdObj);
			  }
		  },
		  highlightpreviousCell : function(){
				var searchController = this,localSavedSearch;

					localSavedSearch = searchController.getPreviousCellobject();
					if(localSavedSearch){
						dojo.query(".srBodySelectedDate",searchController.domNode).removeClass("srBodySelectedDate srDefault");
						if(dojo.query("#"+localSavedSearch.compId).length>0){
							tdObj = dojo.query("#"+localSavedSearch.compId)[0];
							dojo.addClass(tdObj.parentNode,"srBodySelectedDate srDefault");

						}
					}

			  },
		  getSelectedFlightDetails : function(tdObj){
			  	var searchController = this,
			  	compId="";
			  	searchController.selectedDateItinerary =[];
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
		  				searchController.selectedDateItinerary.push(searchController.resultsGridStore.get(compId));
		  			}

		  		}
			  if(searchController.selectedDateItinerary.length >0){
			  		searchController.renderFllightsResults();
			  		if(searchController.savedSearch.returnDate){
			  			window.scrollTo(0, 967);
			  		} else {
			  			window.scrollTo(0, 567);
			  		}
			  }

		  },

		  getNextDateFlightsDetails : function(){
			var searchController = this,startDate,rightArrow,
				savedSearch = searchController.savedSearch,
				resultlastDate,returnDate,departDate;
				searchController.calDeptStartDate = dojo.date.add(new Date( searchController.calDeptStartDate),"day",1);
				searchController.calDeptEndDate = dojo.date.add(new Date( searchController.calDeptEndDate),"day",1);
				startDate =  searchController.calDeptStartDate;
				searchController.departingObject= searchController.generateDateObject(startDate);


				if(searchController.calDeptEndDate.valueOf() > searchController.seasonEndDate.valueOf() ){
					rightArrow = dojo.query(".next", searchController.domNode)[0];
	        		dojo.addClass(rightArrow, "arrInactive");
	        		searchController.calDeptStartDate = dojo.date.add(new Date( searchController.calDeptStartDate),"day",-1);
					searchController.calDeptEndDate = dojo.date.add(new Date( searchController.calDeptEndDate),"day",-1);
	        		return;
				}

				if(savedSearch.returnDate){
					if(dojo.date.difference(searchController.calRetuEndDate,searchController.seasonEndDate) === 0){
						searchController.calRetuStartDate = dojo.date.add(new Date(searchController.calRetuStartDate),"day",-1);
						searchController.calRetuEndDate = dojo.date.add(new Date(searchController.calRetuEndDate),"day",-1);
					}
				}

				resultlastDate = dojo.date.add(new Date(savedSearch.departureDate),"day",searchController.setNextServiceCallDays);

				if(searchController.calDeptEndDate.valueOf() > resultlastDate.valueOf()  ){


					//departDate= dojo.date.add(new Date(searchController.calDeptEndDate),"day",3);

					if(savedSearch.returnDate){
						returnDate = dojo.date.add(new Date(searchController.calRetuEndDate),"day",1);
					}
					searchController.callServiceforNextDateFlightsDetails(searchController.calDeptEndDate,returnDate,"forward");
					searchController.getPreviousSelectedCellID();
				} else{

				if(savedSearch.returnDate){
					searchController.templateview="twoWayResult";
					searchController.calRetuStartDate = dojo.date.add(new Date(searchController.calRetuStartDate),"day",1);
					searchController.calRetuEndDate = dojo.date.add(new Date(searchController.calRetuEndDate),"day",1);

					startDate = searchController.calRetuStartDate;
					searchController.returningObject =searchController.generateDateObject(startDate );
					searchController.createTwoWayMapObject();
				}else{
					searchController.templateview="oneWayResult";
					searchController.createOneWayMapObject();
				}
				searchController.getPreviousSelectedCellID();
				searchController.renderSearchResults();
				searchController.mouseOverCellhiglight();
				}
		  },



		  getPreviousDateFlightsDetails : function(){
			var searchController = this,startDate,
				savedSearch = searchController.savedSearch,
				resultStartDate,returnDate,departDate,leftArrow;
				searchController.calDeptStartDate = dojo.date.add(new Date( searchController.calDeptStartDate),"day",-1);
				searchController.calDeptEndDate = dojo.date.add(new Date( searchController.calDeptEndDate),"day",-1);
				startDate =  searchController.calDeptStartDate;
				searchController.departingObject= searchController.generateDateObject(startDate );

				//previous dates Validation
				if(searchController.calDeptStartDate.valueOf() <= new Date().valueOf() || searchController.calDeptStartDate.valueOf() <= searchController.tracsEndDate.valueOf()){
					leftArrow = dojo.query(".prev", searchController.domNode)[0];
	        		dojo.addClass(leftArrow, "arrInactive");
	        		searchController.calDeptStartDate = dojo.date.add(new Date( searchController.calDeptStartDate),"day",1);
					searchController.calDeptEndDate = dojo.date.add(new Date( searchController.calDeptEndDate),"day",1);
	        		return;
				}

				resultStartDate = dojo.date.add(new Date(savedSearch.departureDate),"day",searchController.setPrevServiceCallDays);

				if(searchController.calDeptStartDate.valueOf() < resultStartDate.valueOf()){

					//departDate= dojo.date.add(new Date(searchController.calDeptStartDate),"day",-3);

					if(savedSearch.returnDate){
						returnDate = dojo.date.add(new Date(searchController.calRetuStartDate),"day",-1);
					}
					searchController.callServiceforNextDateFlightsDetails(searchController.calDeptStartDate,returnDate,"backward");
					searchController.getPreviousSelectedCellID();
				}else{
					if(savedSearch.returnDate){
						searchController.templateview="twoWayResult";
						searchController.calRetuStartDate = dojo.date.add(new Date(searchController.calRetuStartDate),"day",-1);
						searchController.calRetuEndDate = dojo.date.add(new Date(searchController.calRetuEndDate),"day",-1);

						startDate = searchController.calRetuStartDate;
						searchController.returningObject =searchController.generateDateObject(startDate );
						searchController.createTwoWayMapObject();
					}else{
						searchController.templateview="oneWayResult";
						searchController.createOneWayMapObject();
					}

				searchController.getPreviousSelectedCellID();
				searchController.renderSearchResults();
				searchController.mouseOverCellhiglight();
				}
		  },

		  getPreviousSelectedCellID : function(){
			var searchController = this,compId,Itinerary,properties,
			  	tdObj = dojo.query(".srBodySelectedDate",searchController.domNode)[0];
				if(tdObj){
					compId = tdObj.children[0].id;
					Itinerary = searchController.resultsGridStore.get(compId);

					if(searchController.savedSearch.returnDate){
						properties = {
							 	compId: compId,
							 	selectedDeptDate: Itinerary.outbound.schedule.depDate,
						        selectedRetuDate : Itinerary.inbound.schedule.depDate
						      };
					}else{
						properties = {
							 	compId: compId,
							 	selectedDeptDate: Itinerary.outbound.schedule.depDate
						      	};
					}
					 properties.timestamp = (new Date()).getTime();

					searchController.setPreviousCellobject(properties);
				}

		  },

		  checkNoResultCondition: function(){
			  var searchController = this,temptdObj,
			  tdObj = dojo.query(".srBodySelectedDate",searchController.domNode);
			  if(tdObj.length > 1){
				  for(var i = 0;  i < tdObj.length; i++){
					  if(dojo.hasClass(tdObj[i],"mainTd")){
						  dojo.removeClass(tdObj[i], 'srBodySelectedDate');
						  if(tdObj[i].children.length < 1){
							  dojo.addClass(tdObj[i], 'noResultDiv');
						  		tdObj[i].innerHTML = "<div><span>Sorry, no</br>flights available</span><div>";
						  }
					  }else{
						  temptdObj = tdObj[i];
					  }
				  }
			  }else{
				  temptdObj = tdObj[0];
			  }
			  tdObj = temptdObj;
			  if(tdObj && tdObj.children.length < 1){
				  	dojo.removeClass(tdObj, 'srBodySelectedDate');
			  		dojo.addClass(tdObj, 'noResultDiv');
			  		tdObj.innerHTML = "<div><span>Sorry, no</br>flights available</span><div>";
			  }
		  },

		  callServiceforNextDateFlightsDetails : function(departDate,returnDate,direction){
			  	var searchController = this,
			  		params,
			  		results,
			  		dateStoreData,
			  		itinerarys=[],
			  		infantsAge="",
			  		childAge="",
			  		deptDate="",
			  		retDate="",
			  		savedSearch = searchController.savedSearch;
			  		targetUrl = dojoConfig.paths.webRoot+"/ws/searchnav?";



			  		deptDate =   dojo.date.locale.format(departDate, {
			          selector: "date",
			          datePattern: "yyyy-MM-dd"
			        });

			  		if(returnDate){
			  			retDate =   dojo.date.locale.format(returnDate, {
					          selector: "date",
					          datePattern: "yyyy-MM-dd"
					        });
			  		}


			  		dojo.addClass(query('.loadingDivCnt', searchController.domNode)[0],"loading");
			  		if(!searchController.savedSearch.returnDate){
			  			dojo.addClass(query('.loadingDivCnt', searchController.domNode)[0],"oneWayOverlay");
			  		}

			  		params = {
			  			flyingFrom		: 	savedSearch.departureAirportCode[0],
			  			flyingTo		: 	savedSearch.arrivalAirportCode[0],
			  			depDate			: 	deptDate,
			  			returnDate		: 	retDate,
						adults			: 	savedSearch.adultCount,
						children		: 	savedSearch.childCount,
						childAge		: 	(savedSearch.childages) ? savedSearch.childages.toString() : "",
						infants			: 	savedSearch.infantCount,
						infantAge		: 	(savedSearch.infantAges) ? savedSearch.infantAges.toString() : "",
						isOneWay 		: 	JSON.parse(savedSearch.oneWay),
						isPriceGrid		:	'Y'
				      };




			  		results = dojo.xhr("GET", {
			  			url: targetUrl+ ioQuery.objectToQuery(params),
			  			handleAs: "json"
			  		});

			  		 dojo.when(results, function (dateResultsData) {
			  			searchController.nextPrevServiceItineraryDataProcessing(dateResultsData.itinerary,direction);
			  	      });

		  },

		  nextPrevServiceItineraryDataProcessing : function(resultsItinerarys, direction){
			  	var searchController = this,
			  		days=0;
			  		searchController.itinerarys = searchController.reGenerateItineraryObject(resultsItinerarys);

				  	/*for(var i= 0; i< itinerarys.length; i++){
				  			searchController.itinerarys[searchController.itinerarys.length] =  itinerarys[i];
				  		}*/
				  	searchController.itinerarys = _.sortBy(searchController.itinerarys, function(o) { return o.pricePP; });



				  	if(direction== "forward"){
			  			days =1;

			  			if(searchController.setNextServiceCallDays == 3 && searchController.tracsDays > 2){
			  				searchController.setPrevServiceCallDays = -2;
			  			}
			  			if(searchController.setNextServiceCallDays == 2  && searchController.tracsDays > 2){
			  				searchController.setPrevServiceCallDays = -3;
			  			}

			  			searchController.setNextServiceCallDays = searchController.setNextServiceCallDays + 7;

			  			if(searchController.setNextServiceCallDays == 10 && searchController.setPrevServiceCallDays == -10){
			  				searchController.setPrevServiceCallDays = -3;
			  				searchController.compareCalStartDate = dojo.date.add(new Date(searchController.compareCalStartDate),"day",7);
			  			}
			  			if(searchController.setNextServiceCallDays > 10 || searchController.setPrevServiceCallDays  < -10){
			  				searchController.compareCalStartDate = dojo.date.add(new Date(searchController.compareCalStartDate),"day",7);
			  				searchController.setPrevServiceCallDays = searchController.setPrevServiceCallDays +7;
			  			}
			  			searchController.compareCalEndtDate = dojo.date.add(new Date(searchController.compareCalEndtDate),"day",7);
			  		}
			  		else{
			  			days =-1;

			  			if(searchController.setPrevServiceCallDays == -3){
			  				searchController.setNextServiceCallDays = 2;
			  			}if(searchController.setPrevServiceCallDays == -2){
			  				searchController.setNextServiceCallDays = 3;
			  			}

			  			searchController.setPrevServiceCallDays = searchController.setPrevServiceCallDays - 7;

			  			if(searchController.setNextServiceCallDays == 10 && searchController.setPrevServiceCallDays == -10 ){
			  				searchController.setNextServiceCallDays = 3;
			  				searchController.compareCalEndtDate = dojo.date.add(new Date(searchController.compareCalEndtDate),"day",-7);
			  			}
			  			if(searchController.setPrevServiceCallDays  < -10 || searchController.setNextServiceCallDays > 10){
			  				searchController.compareCalEndtDate = dojo.date.add(new Date(searchController.compareCalEndtDate),"day",-7);
			  				searchController.setNextServiceCallDays = searchController.setNextServiceCallDays -7;
			  			}
			  			searchController.compareCalStartDate = dojo.date.add(new Date(searchController.compareCalStartDate),"day",-7);
			  		}


				  	//searchController.removeOldItinerarys();

			  		searchController.resultsGridStore = new SearchPanelMemory({data: searchController.itinerarys});
			  		searchController.calRetuStartDate = dojo.date.add(new Date(searchController.calRetuStartDate),"day",days);
					searchController.calRetuEndDate = dojo.date.add(new Date(searchController.calRetuEndDate),"day",days);

					startDate = searchController.calRetuStartDate;
					searchController.returningObject =searchController.generateDateObject(startDate );

					if(searchController.savedSearch.returnDate){
						searchController.templateview="twoWayResult";
						searchController.createTwoWayMapObject();
					}else{
						searchController.templateview="oneWayResult";
						searchController.createOneWayMapObject();
					}

			  		searchController.renderSearchResults();
			  		searchController.mouseOverCellhiglight();
		  },


		  findItineryStartDate: function(){
			  	var searchController = this, ItinerarysStartDateArray;
			  		ItinerarysDateSortArray = _.sortBy(searchController.itinerarys, function(o) { return o.outbound.schedule.departureDate; });
			  		searchController.itinerarysStartDate = ItinerarysDateSortArray[0].outbound.schedule.departureDate;
			  		searchController.itinerarysEndDate = ItinerarysDateSortArray[ItinerarysDateSortArray.length-1].outbound.schedule.departureDate;


		  },

		  removeOldItinerarys : function(){
			  	var searchController = this,
			  		itinerarys = searchController.itinerarys;
					  for(var i = itinerarys.length-1; i >= 0  ;i--){
						  var itinerary = itinerarys[i];
						  if(searchController.compareCalStartDate.valueOf() > itinerary.outDepartureDate.valueOf()  || itinerary.outDepartureDate.valueOf() > searchController.compareCalEndtDate.valueOf()   ){
							  searchController.itinerarys.splice(i,1);
						  }
					  }
		  },


		  subscribeToChannels: function () {
		      var searchController = this;
		      // toggle price
		      topic.subscribe("tui:channel=priceToggle", function (message) {
		    	  var resultCnt = query('#resultinfoCnt', searchController.domNode)[0]
		        domClass.add(resultCnt, message.add);
		        domClass.remove(resultCnt, message.remove);
		      });
		    },


		    setPreviousCellobject: function(object) {
		    	var searchController=this;
		    		object = dojo.toJson(object);
			    	try {
			    		localStorage.setItem(searchController.savedCompId, object);
			    	} catch (e) {
			    			_.debug("Can't save state in secure mode");
			    	}
		    },

		    getPreviousCellobject: function(){
				var searchController = this,savedCompId;
				try {
					savedCompId = localStorage.getItem(searchController.savedCompId);
					} catch (e) {
						_.debug("Can't restore state in secure mode");
					}
				return (savedCompId) ? dojo.fromJson(savedCompId) : null;
			},

			createCompareCalObject : function(){
				var searchController= this,
					savedSearch = searchController.savedSearch;
					searchController.compareCalStartDate = dojo.date.add(new Date(savedSearch.departureDate),"day",searchController.calStartDays);
					searchController.compareCalEndtDate = dojo.date.add(new Date(savedSearch.departureDate),"day",searchController.calEndDays);
			}


	});

	return tui.searchResults.view.flights.FlightSearchResultController;
});
