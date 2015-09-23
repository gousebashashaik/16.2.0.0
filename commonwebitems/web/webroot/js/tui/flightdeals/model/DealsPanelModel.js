define("tui/flightdeals/model/DealsPanelModel",[
	"dojo/_base/declare",
	"dojo/query",
	"dojo/dom",
	"dojo/Stateful",
	"dojo/store/Memory",
	"dojo/store/Observable",
	"tui/utils/LocalStorage",
	 "tui/flightdeals/model/DealsModel",
	 "dijit/registry",
	 "tui/searchPanel/model/AirportModel",
	 "tui/searchPanel/store/flights/AirportMultiFieldStore",
	 "dojo/cookie"
	],function(declare, query, dom, Stateful, Memory, Observable, localStorage, registry ,AirportModel, AirportMultiFieldStore,cookie ){

	declare("tui.flightdeals.model.DealsPanelModel",[tui.flightdeals.model.DealsModel ],{

		days				: 0,

		oneWay 				: false,

		month				: 0,

		year				: 0,

		flexible			: false,

		fromAirports		: [],

		fromAirportNames	: [],

		fromAirportGpName 	: [],

		fromAirportGpFlag 	: false,

		toAirports			: [],

		toAirportGpName 	: [],

		toAirportNames		: [],

		toAirportGpFlag 	: false,

		startDuration		: 1,

		endDuration			: 99,

		outboundSlots		: [],

		searchType			: "",

		responseType 		: "",

		dealsSavedSearch	: null,

		//destinationCode : For Backend, based on this code they will give individual results
		destinationCode		: [],

		//pageCount : start count for individual results for backend
		pageCount 			: "",

		pageSize			: 15,

		startNumber 		: 0,

		endNumber 			: 15,

		adultCount 			: 1,

		childCount 			: 0,

		childages 			: [],

		dealsFilterStatus	: true,

		sortBy				: "price",

		asynCall			: null,

		durationFilter		: [],

		maxPrice			: "",

		pageTitle 			: null,

		airportName			: [],

		retainCriteria 		: {},

		groupedUKAiports	:	[],

		groupedOverseasAiports : [],

		flyFromAllAirports	: "",

		gotoAllAirports		: "",

		//defaultBugetFilterRange: null,

		//defaultBugetDomIndex: null,

		constructor: function (props) {
			 	var dealsPanelModel = this;
			 		dojo.mixin(dealsPanelModel, props);
			 		//dealsPanelModel.initialise();
		},

		initialise: function () {
			var dealsPanelModel = this;
				dealsPanelModel.fromAirports = new Observable(new AirportMultiFieldStore());
		},

		createQueryObjForServerCall: function(pageSubmit){
	    	var DealsPanelModel = this;
	    	if(pageSubmit){
	    		return properties  ={
	    				'fromAirports'			: 	DealsPanelModel.fromAirports,
	    				'toAirports'			: 	DealsPanelModel.toAirports,
						'month'					:  	DealsPanelModel.month,
						'year'					: 	DealsPanelModel.year,
						'flexible'				: 	DealsPanelModel.flexible,
						'days'					: 	DealsPanelModel.days,
						'startDuration' 		:	DealsPanelModel.startDuration,
						'endDuration'			:	DealsPanelModel.endDuration,
						'outboundSlots' 		: 	"",
						'searchType' 			: 	DealsPanelModel.searchType,
						'oneWay'				: 	DealsPanelModel.oneWay,
						'durationFilter'		:	DealsPanelModel.durationFilter,
						'maxPrice'				: 	""

	    		}
	    	}else{

		    	return dojo.toJson({
					'toAirports'			: 	(DealsPanelModel.toAirports) ? DealsPanelModel.toAirports : [],
					'fromAirports'			: 	(DealsPanelModel.fromAirports) ? DealsPanelModel.fromAirports : [],
					'month'					:  	(DealsPanelModel.month) ? DealsPanelModel.month : "",
					'year'					: 	(DealsPanelModel.year) ? DealsPanelModel.year : "",
					'flexible'				: 	DealsPanelModel.flexible,
					'days'					: 	(DealsPanelModel.days) ? DealsPanelModel.days : "",
					'startDuration' 		: 	(DealsPanelModel.startDuration) ? DealsPanelModel.startDuration : "",
					'endDuration'			:	(DealsPanelModel.endDuration) ? DealsPanelModel.endDuration : "",
					'outboundSlots' 		: 	(DealsPanelModel.outboundSlots) ? DealsPanelModel.outboundSlots : [],
					'searchType' 			: 	(DealsPanelModel.searchType) ? DealsPanelModel.searchType : "",
					'responseType' 			: 	(DealsPanelModel.responseType) ? DealsPanelModel.responseType : "",
					'destinationCode' 		:  	(DealsPanelModel.destinationCode) ? DealsPanelModel.destinationCode : [],
					'pageSize' 				: 	(DealsPanelModel.pageSize) ? DealsPanelModel.pageSize : "",
					'startNumber'			: 	(DealsPanelModel.startNumber) ? DealsPanelModel.startNumber : "",
					'endNumber'				: 	(DealsPanelModel.endNumber) ? DealsPanelModel.endNumber : "",
					'adultCount' 			:	(DealsPanelModel.adultCount) ? DealsPanelModel.adultCount : "",
					'childCount' 			: 	(DealsPanelModel.childCount) ? DealsPanelModel.childCount : "",
					'childages' 			:	(DealsPanelModel.childages) ? DealsPanelModel.childages : [],
					'oneWay'				: 	(DealsPanelModel.oneWay) ? DealsPanelModel.oneWay : false,
					'maxPrice'				: 	(DealsPanelModel.maxPrice) ? DealsPanelModel.maxPrice : "",
					"pageCount"				: 	(DealsPanelModel.pageCount) ? DealsPanelModel.pageCount : 0

				});
	    	}
	    },

	    createQueryObjForLocalStorageCall: function(){
	    	var DealsPanelModel = this;
	    	return dojo.toJson({
				'toAirports'			: 	DealsPanelModel.toAirports,
				'toAirportNames'		: 	DealsPanelModel.toAirportNames,
				'toAirportGpName'		:	DealsPanelModel.toAirportGpName,
				'flyFromAllAirports'	: 	DealsPanelModel.flyFromAllAirports,
				'gotoAllAirports'		: 	DealsPanelModel.gotoAllAirports,
				'month'					:  	DealsPanelModel.month,
				'year'					: 	DealsPanelModel.year,
				'flexible'				: 	DealsPanelModel.flexible,
				'days'					: 	DealsPanelModel.days,
				'fromAirports'			: 	DealsPanelModel.fromAirports,
				'fromAirportNames'		: 	DealsPanelModel.fromAirportNames,
				'fromAirportGpName'		: 	DealsPanelModel.fromAirportGpName,
				'startDuration' 		:	DealsPanelModel.startDuration,
				'endDuration'			:	DealsPanelModel.endDuration,
				'outboundSlots' 		: 	DealsPanelModel.outboundSlots,
				'searchType' 			: 	DealsPanelModel.searchType,
				'responseType' 			: 	DealsPanelModel.responseType,
				'destinationCode' 		: 	DealsPanelModel.destinationCode,
				'pageSize' 				: 	DealsPanelModel.pageSize,
				'startNumber'			: 	DealsPanelModel.startNumber,
				'endNumber'				: 	DealsPanelModel.endNumber,
				'adultCount' 			:	DealsPanelModel.adultCount,
				'childCount' 			: 	DealsPanelModel.childCount,
				'childages' 			:	DealsPanelModel.childages,
				'oneWay'				: 	DealsPanelModel.oneWay,
				'sortBy'				:	DealsPanelModel.sortBy,
				'durationFilter'		:	DealsPanelModel.durationFilter,
				'fromAirportGpFlag' 	: 	DealsPanelModel.fromAirportGpFlag,
				'toAirportGpFlag'		: 	DealsPanelModel.toAirportGpFlag,
				'durationFilter'		:	DealsPanelModel.durationFilter,
				'maxPrice'				:	DealsPanelModel.maxPrice
				//'defaultBugetFilterRange': DealsPanelModel.defaultBugetFilterRange,
				//'defaultBugetDomIndex' : DealsPanelModel.defaultBugetDomIndex
			});
	    },

	    doXhrPost: function(url){
	    	var dealsPanelModel = this;
	    	var jsonData = dealsPanelModel.createQueryObjForServerCall();

	    	if(dealsPanelModel.asynCall && dealsPanelModel.asynCall.ioArgs.xhr.readyState != 4 ){
	    		dealsPanelModel.asynCall.ioArgs.xhr.abort();
	    	}

	    	dealsPanelModel.asynCall = dojo.xhrPost({
				url: url,
				handleAs: "json",
				content: {
					"jsonData":jsonData
				},
				handle: function(data,err){
					//console.log(data,err);
					if(data.sessionTimeerrorflag){
						location.href=data.sessionTimeoutPage+"?sessionTimeerrorflag="+data.sessionTimeerrorflag;
						return;
					}
				}
			});

	    	return dealsPanelModel.asynCall;

	    },

	    SetDealsSavedSearch: function() {
	    	var dealsPanelModel = this;
	    	object = dealsPanelModel.createQueryObjForLocalStorageCall();
		      try {
		    	dojo.cookie("retainCriteria",null,{"expires":-1,"path":"/flight/deals/"}); //Fix for FF to remove duplicates on different paths
		    	dojo.cookie("retainCriteria",null,{"expires":-1,"path":"/flight/deals"}); //Fix for Chrome to remove duplicates on different paths
		    	dojo.cookie("retainCriteria", object,{'path':'/flight/'});
		      } catch (e) {
		        _.debug("Can't save state in secure mode");
		      }
		},

		getDealsSavedSearch: function(json){
			var dealsPanelModel = this;
				//return dojo.cookie("retainCriteria") == undefined ? "" :dojo.fromJson(dojo.cookie("retainCriteria"));
			dealsSavedSearch = json.flightSearchCriteria;

			dealsSavedSearch.fromAirports = (dealsSavedSearch.departureAirportCode) ? dealsSavedSearch.departureAirportCode : [];
			dealsSavedSearch.toAirports = (dealsSavedSearch.arrivalAirportCode) ? dealsSavedSearch.arrivalAirportCode : [];

			dealsSavedSearch.fromAirportNames = (json.departureFlightsInfo) ? json.departureFlightsInfo : [];
			dealsSavedSearch.toAirportNames = (json.arrivalFLightsInfo) ? json.arrivalFLightsInfo : [];

			dealsSavedSearch.flyFromAllAirports  =  (dealsSavedSearch.departureAirportCode) ? "" : "anyUKAirport";
			dealsSavedSearch.gotoAllAirports	=  (dealsSavedSearch.arrivalAirportCode) ? "" : "anyOverseasAirport";


			return dealsSavedSearch;
		},


	    onRetrieveSavedObject: function (dealsSavedSearch) {
	    	var dealsPanelModel = this, outSloats;

	    		if(!dealsSavedSearch) return


	    		// push all value to model object
		        for (var prop in dealsSavedSearch) {
		        	  dealsPanelModel.set(prop, dealsSavedSearch[prop]);
		        }

		        //oneWay Validation
				if(dealsSavedSearch !== null && dealsSavedSearch.oneWay){
					if(!dijit.byId("dealsOneWay")) return false;

					dijit.byId("dealsOneWay").set("checked", dealsSavedSearch.oneWay);
					dealsPanelModel.hideWidget(dom.byId("results"));
					if(dijit.byId("foDealsSearchResults")!==undefined){
						dojo.style(dojo.query(".filter-options")[0], {
	        				"display":"block"
	        			});
					}
					else{
						dojo.style(dojo.query(".filter-options")[0], {
	        				"display":"none"
	        			});
					}
				}

				if(dealsSavedSearch.durationFilter != null){
					_.forEach(dealsSavedSearch.durationFilter,function(item){
						if(dijit.byId(item)){
							if(!dijit.byId(item).disabled) dijit.byId(item).set("checked",true);
						}
					})
				}

				//Fill Fly out object
				if(dealsPanelModel.days != 0) {
					dijit.byId("dealsExpandable").selectedData = dealsPanelModel.days.toString();
				} else {
					dijit.byId("dealsExpandable").selectedData = dealsPanelModel.month + "/" + dealsPanelModel.year;
				}

				//Fly form object
				dijit.byId("FlyFromDealsExpandable").selectedUkAirports = dealsPanelModel.fromAirports;

				//Fly to object
				dijit.byId("GoToDealsExpandable").selectedOverseasAirports = dealsPanelModel.toAirports;
	    },

	   showWidget : function(element){
            dojo.setStyle(element, "display", "block");
       },

       hideWidget : function (element){
             dojo.setStyle(element, "display", "none");

        },

        validateSavedSearch :  function(airportList, savedAirportlist){
        	var dealsPanelModel = this, tempArry=[];
        	if(!savedAirportlist || savedAirportlist.length < 0) return;
        	_.forEach(savedAirportlist , function(airportID){
        		_.forEach(airportList , function(airport){
        			if(airport.id === airportID && airport.available){
        				tempArry.push(airportID);
        			}
        		})
        	});
        	return tempArry;
        },
        updateBugetState: function(){
        	var dealsPanelModel = this;
        	 var criteria = dojo.fromJson(dojo.cookie("retainCriteria"));
        	 if(criteria !==  undefined){
        		 criteria.defaultBugetDomIndex = null;
        		 criteria.maxPrice = null;
            	 dojo.cookie("retainCriteria",dojo.toJson(criteria));
            	 dealsPanelModel.maxPrice = null;

        	 }
        },
		 disableDealsButton: function(){
			 var btn = query("#dealSearchPanelContainer .dealsbutton");
			 btn.addClass("disabled");
			 btn.removeClass("cta");
		 },
		 enableDealsButton: function(){
			 var btn = query("#dealSearchPanelContainer .dealsbutton");
			 btn.removeClass("disabled");
			 btn.addClass("cta");
		 }
	});
	return tui.flightdeals.model.DealsPanelModel;
});