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
	  "tui/search/store/SearchPanelMemory",
	  "tui/searchPanel/model/flights/SearchPanelModel",
	  "tui/searchPanel/model/AirportModel",
	  "tui/searchPanel/model/DestinationModel",
	  "tui/utils/LocalStorage",
	  "dojo/_base/json",
	  "tui/searchResults/view/flights/FlightTabNavigation",
	  "tui/searchResults/view/flights/SelectedFlightDetails",
	  "tui/searchResults/view/flights/SearchResultNextPreviousCalls",
	  "tui/searchResults/view/flights/SearchResultCellActions",
	  "tui/search/nls/Searchi18nable",
	  "tui/mvc/Controller",
	  "tui/widget/mixins/Templatable",
	  "tui/widget/_TuiBaseWidget",
	  "dojo/NodeList-traverse",
	  "tui/searchResults/view/Tooltips",
	  "tui/searchResults/view/PriceToggle",
	  "tui/widget/taggable/Navigation"
	  ], function (dojo, has, on, query, topic, connect, lang, domAttr, ioQuery, domClass, parser, domConstruct,searchResultsGridTmpl,SearchPanelMemory, SearchPanelModel, AirportModel,DestinationModel, localStorage, json , FlightTabNavigation, SelectedFlightDetails, SearchResultNextPreviousCalls, SearchResultCellActions) {

	dojo.declare("tui.searchResults.view.flights.FlightSearchResultController", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable, tui.mvc.Controller, tui.search.nls.Searchi18nable, FlightTabNavigation, SelectedFlightDetails, SearchResultNextPreviousCalls, SearchResultCellActions], {


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
				searchController.inherited(arguments);
				searchController.reGenerateSavedSearchObject();
				searchController.searchPanelModel.prePopulateSearchCriteria(searchController.jsonData.flightSearchCriteria);
				searchController.backtoSearchValidation(searchController.savedSearch.searchCriteriaType);
				searchController.currency = searchController.currency;
				searchController.getTabAiports();
				searchController.itinerarys = searchController.reGenerateItineraryObject(searchController.itinerarys);
				searchController.itinerarys = _.sortBy(searchController.itinerarys, function(o) { return o.pricePP; });
				searchController.resultsGridStore = new SearchPanelMemory({data: searchController.itinerarys});
				searchController.createSelectedDatesCalObject();
				searchController.createCompareCalObject();
				localStorage.removeItem(searchController.savedCompId);
				searchController.renderGridTabNavigation();
				searchController.setActiveTab();
				searchController.selectPreviousTab(searchController.savedSearch.searchCriteriaType);
				searchController.renderSearchResults();
				searchController.getSelectedFlightDetails()
				searchController.attachEvents();
				searchController.tabAttachEvents();
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

		reGenerateItineraryObject : function(itinerarys, flightDetails){
			var searchController = this;

				return _.map(lang.clone(itinerarys), function (itinerary, index) {
						itinerary.showComp = true;

					    if(flightDetails){
					    	departureDt = itinerary.outbound.schedule.departureDate;
					    }else{
					    	departureDt = itinerary.departureDt;
					    }

						itinerary.outDepartureDate = searchController.removeHoursFromDateObject(departureDt);
			    		itinerary.outbound.depDate = new Date(departureDt).toDateString();
			    		itinerary.outbound.depObj = searchController.splitDateString(new Date(departureDt));

					    if (!searchController.savedSearch.oneWay) {

					    	 if(flightDetails){
					    		returnDt = itinerary.inbound.schedule.departureDate;
							 }else{
							    returnDt = itinerary.returnDt;
							    itinerary.inbound=[];
							 }

				    		itinerary.inDepartureDate = searchController.removeHoursFromDateObject(returnDt);
				    		itinerary.inbound.depDate = new Date(returnDt).toDateString();
				    		itinerary.inbound.depObj = searchController.splitDateString(new Date(returnDt));
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
		    	var searchController = this,newDt;
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
			var searchController= this,
				obj={},tempdate,
				dateObj = dt.toDateString();
				obj['dateValue'] = dt.valueOf();
				obj['curDate'] =  dateObj;
				obj['day'] = dateObj.substring(0,3);
				obj['month'] = dateObj.substring(4,7);
				tempdate= dateObj.substring(8,10).replace(/^\s+|\s+$/g, '');
				obj['date'] = (tempdate.length > 1) ? tempdate : "0"+tempdate;
				obj['reqFormat'] = searchController.reqDateFormat(dt);
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

					if(dept.curDate == itinerary.outbound.depDate){
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
				 if(arrayObj[i].outbound.depDate == obj.outbound.depDate){
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

						if(dept.curDate == itinerary.outbound.depDate){

							for(var j = 0; j<retunObj.length; j++ ){
								var retun = retunObj[j];

								if(retun.curDate == itinerary.inbound.depDate ){
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
				 if(arrayObj[i].inbound.depDate === obj.inbound.depDate && arrayObj[i].outbound.depDate == obj.outbound.depDate){
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
			var searchController = this,html,data,selectedDeptDate,selectedRetuDate,tabCnt,
			localSavedSearch = searchController.getPreviousCellobject();
			if(localSavedSearch){
				selectedDeptDate = localSavedSearch.selectedDeptDate
				selectedRetuDate = localSavedSearch.selectedRetuDate
			}
			data = {
					 departureDates		:	searchController.departingObject,
			 		 returnDates 		:	searchController.returningObject,
			 		 currency 			: 	searchController.currency,
			 		 templateview 		: 	searchController.templateview,
			 		 calSelectedDeptDate:	searchController.calSelectedDeptDate.toDateString(),
			 		 componentId 		: 	searchController.pricegridComponentId,
			 		 calSelectedRetuDate:	(searchController.savedSearch.returnDate) ? searchController.calSelectedRetuDate.toDateString() : false,
			 		 selectedDeptDate	:	selectedDeptDate,
			 		 selectedRetuDate 	:	selectedRetuDate
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

		 	searchController.highlightpreviousCell();

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
				 		searchController.getSelectedFlightDetails(this, true);
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
			},



			parserJsonData : function(fromAiports, toAirports, departDate,returnDate, searchType){
				var searchController = this,
			  		params,
			  		infantsAge="",
			  		childAge="",
			  		deptDate="",
			  		retDate="",
			  		savedSearch = searchController.savedSearch;

					deptDate = searchController.reqDateFormat(departDate);

			  		if(returnDate){
			  			retDate = searchController.reqDateFormat(returnDate);
			  		}

			  		params = {
			  			'flyingFrom[]'	: 	fromAiports,
			  			'flyingTo[]'	: 	toAirports,
			  			depDate			: 	deptDate,
			  			returnDate		: 	retDate,
						adults			: 	savedSearch.adultCount,
						children		: 	savedSearch.childCount,
						childAge		: 	(savedSearch.childages) ? savedSearch.childages.toString() : "",
						infants			: 	savedSearch.infantCount,
						infantAge		: 	(savedSearch.infantAges) ? savedSearch.infantAges.toString() : "",
						isOneWay 		: 	JSON.parse(savedSearch.oneWay),
						searchType		:	searchType

				      };

		  		return params;
			},

			reqDateFormat : function(dateObj){
				var searchController = this;
				return dojo.date.locale.format(dateObj, {
			          selector: "date",
			          datePattern: "yyyy-MM-dd"
			        });
			},

			backtoSearchValidation : function(backtoSearch){
				var searchController = this;

				if(backtoSearch && backtoSearch === "backtosearch") {
					searchController.savedSearch.oldDepartureDate = searchController.savedSearch.departureDate
					searchController.savedSearch.departureDate = searchController.setDateFormat(searchController.savedSearch.selDepDate);
					if(!searchController.savedSearch.oneWay){
						searchController.savedSearch.oldReturnDate = searchController.savedSearch.returnDate;
						searchController.savedSearch.returnDate = searchController.setDateFormat(searchController.savedSearch.selArrDate);
					}
				}


			}




	});

	return tui.searchResults.view.flights.FlightSearchResultController;
});
