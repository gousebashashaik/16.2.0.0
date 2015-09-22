define("tui/smerchOverlayPanel/controller/SearchController", ["dojo",
  "dojo/has",
  "dojo/on",
  "dojo/dom-attr",
  "tui/searchPanel/model/AirportModel",
  "tui/searchPanel/model/DestinationModel",
  "tui/searchPanel/model/SearchPanelModel",
  "tui/widget/popup/Tooltips",
  "tui/widget/popup/ErrorPopup",
  "tui/searchPanel/view/AirportMultiFieldList",
  "tui/smerchOverlayPanel/view/DestinationMultiFieldList",
  "tui/searchPanel/view/AirportGuide",
  "tui/searchPanel/view/DestinationGuide",
  "tui/searchPanel/view/HowLongOptions",
  "tui/searchPanel/view/AdultsSelectOption",
  "tui/searchPanel/view/SeniorsSelectOption",
  "tui/searchPanel/view/ChildSelectOption",
  "tui/smerchOverlayPanel/view/SearchDatePicker",
  "tui/searchPanel/view/SearchSummary",
  "tui/search/nls/Searchi18nable",
  "tui/mvc/Controller",
  "tui/searchPanel/view/SubmitButton",
  "tui/searchPanel/store/AirportMultiFieldStore",
  "tui/smerchOverlayPanel/store/AirportGuideStore",
  "tui/searchPanel/store/DestinationMultiFieldStore",
  "tui/searchPanel/store/DestinationGuideStore",
  "tui/smerchOverlayPanel/store/DateStore"], function (dojo, has, on, domAttr, AirportModel, DestinationModel) {

  dojo.declare("tui.smerchOverlayPanel.controller.SearchController", [tui.mvc.Controller, tui.search.nls.Searchi18nable], {

    // ----------------------------------------------------------------------------- properties

    jsonData: null,

    locationData: null,

    pageId: null,

    searchResultsPage: null,
    
    bookflowAccomPage: null,
    
    smerchOverlayPage: null,

    retrievalScenario: "localStorage",
	
	SingleMonthPreDefined : false,
	
	familiesPredefined: false,
	
	durationPredefined: false,

    // ----------------------------------------------------------------------------- methods

    generateRequest: function () {
      return this.searchPanelModel.generatePostObject();
    },

    refresh: function () {
      //handle refresh
    },

    clear: function () {
      //handle clear
    },

    handleNoResults: function () {

    },

    retainIntialSearchRequest: function (searchPanelModel) {
      var request = searchPanelModel.generatePostObject();
      this.generateRequest = function () {
        return request;
      };
    },

    constructor: function () {
      // summary:
      //		Called on class creation, initialising all widgets views and search model.
      var searchController = this;

      //var mediator = dijit.registry.byId('mediator');
      //mediator ? mediator.registerController(searchController) : null;

      searchController.searchResultsPage = searchController.pageId === "searchresultspage" || searchController.pageId === "singleaccomsearchresultspage";
      
      searchController.bookflowAccomPage = searchController.pageId === "bookFlowAccommodationOverviewPage" || searchController.pageId === "bookFlowAccommodationRoomsPage" || searchController.pageId === "bookFlowAccommodationFacilitiesPage" || searchController.pageId === "bookFlowAccommodationLocationPage" || searchController.pageId === "bookFlowAccommodationThingsToDoPage";
      
      searchController.smerchOverlayPage = searchController.pageId === "smerchoverlaypage";

      // @TODO: too many scenarios, refactor if possible
      if (searchController.smerchOverlayPage && _.has(searchController.jsonData, "searchRequestData")) {
          // results page
          // only has searchPanel, use searchRequest data
          searchController.retrievalScenario = "smerch";
          searchController.deferLoading();
        }
      else if (searchController.searchResultsPage && _.has(searchController.jsonData, "searchRequest")) {
        // results page
        // only has searchPanel, use searchRequest data
        searchController.retrievalScenario = "searchResults";
        searchController.deferLoading();
      } else if (!searchController.searchResultsPage && searchController.locationData && _.has(searchController.jsonData, "searchRequestData")) {
        // location page, has searchRequestData (bookflow mode)
        // use searchApi config as search panel/get price have different criteria
        // searchPanel: use searchRequestData
        // getPrice: use searchRequestData and replace "to" with location data
        searchController.retrievalScenario = searchController.searchApi;
        searchController.deferLoading();
      } else if (searchController.locationData && !_.has(searchController.jsonData, "searchRequestData")) {
        // location page, no searchRequestData (browse mode), pre-populate location (both)
        searchController.retrievalScenario = "location";
        searchController.deferLoading();
      } else if (_.has(searchController.jsonData, "searchRequestData") || _.has(searchController.jsonData, "searchRequest")) {
        // who is going page
        searchController.retrievalScenario = "searchPanel";
        searchController.deferLoading();
      } else {
        // else, pre-populate from localStorage (search panel)
        // should only fall on non-location, non-search results pages
        searchController.retrievalScenario = "default";
        searchController.deferLoading(searchController.validateSavedData(searchController.searchPanelModel.getSavedSearch()));
      }

      // subscribe to getPriceModal closing if this is getPrice controller
      // and clear error messages
      if (searchController.searchApi === "getPrice") {
        dojo.subscribe("tui:channel=getPriceClosing", function () {
          _.each(searchController.searchPanelModel.searchErrorMessages, function (message, key) {
            if (typeof message !== "function") {
              searchController.searchPanelModel.searchErrorMessages.set(key, {});
            }
          });
        });
      }
    },

    deferLoading: function (promise) {
      var searchController = this;

      // subscribe to lazyload channel to ensure all widget components have fully loaded.
      dojo.subscribe("tui:channel=lazyload", function () {

        // prepopulate when the promise has returned (immediate response except for validation case)
        dojo.when(promise, function () {

          // initialise (i18n)
          searchController.initSearchMessaging();

          switch (searchController.retrievalScenario) {
            case "searchResults":
              searchController.prePopulate(searchController.jsonData.searchRequest);
              break;
            case "getPrice":
              var data = dojo.clone(searchController.jsonData.searchRequestData);
              delete data.units;
              data = dojo.mixin(data, searchController.locationData);
              searchController.prePopulate(data);
              break;
            case "searchPanel":
              searchController.prePopulate(searchController.jsonData.searchRequestData || searchController.jsonData.searchRequest);
              break;
            case "smerch":
                searchController.prePopulate(searchController.jsonData.searchRequestData);
                break;
							case "searchBPanel":
								var data = dojo.clone(searchController.jsonData.searchRequestData);
								searchController.prePopulate(data);
								break;
            case "location":
              searchController.prePopulate(searchController.locationData);
              break;
            default:
              searchController.searchPanelModel.retrieveSavedObject(searchController.airportGuideStore);
          }

          // after prepopulating
          dojo.publish("tui:channel=searchLocalStorageRetrieved", [searchController.searchPanelModel]);
          // TODO: JOHN, this needs to only happen if we have saved data, or if we have made a searchRequest
          //searchController.retainIntialSearchRequest(searchController.searchPanelModel);
          searchController.searchPanelModel.refresh();
          searchController.onLazyLoad();

        });
      });

    },

    prePopulate: function (data) {
      var searchController = this, airports = [], units = [];

      // @TODO: change model field names to match jsonData (enable automated object generating)

      if (_.has(data, "airports")) {
        // push airports as AirportModel
        _.forEach(data.airports, function (airport) {
          airports.push(new AirportModel({
            id: airport.id,
            name: airport.name,
            groups: airport.group,
            synonym: airport.synonym,
            children: airport.children
          }));
        });
      }

      if (_.has(data, "units")) {
        // push units as DestinationModel
        _.forEach(data.units, function (unit) {
          units.push(new DestinationModel(unit));
        });
      }
      var jsnObj = localStorage["thomson/search/main"] ? JSON.parse(localStorage["thomson/search/main"]) : null;

		//Resets duration with daytrip value, if it the where-to field is multi-select false (Daytrip)
      if(searchController.domNode.id == "get-priceB-modal" || searchController.domNode.id == "get-price-modal"){
    	  if(units && units.length == 1 && units[0].multiSelect == false && dojo.query("#howLong .dropdown .value").text() == "Day Trip"){
    		  _.each(dojo.query("#get-priceB-modal select#get-totalDuration option"), function(opt, ind){
    			  if( dojo.query(opt).text() == "Day Trip" ){
    				  dojo.query(".get-totalDuration_HLDD ul.howLongDD li.lapLandDDLi").style("display", "block");
    	        	  dojo.query(".get-totalDuration_HLDD ul.howLongDD li.lapLandDDLi").addClass("active");
    				  searchController.searchPanelModel.duration = domAttr.get(opt, "value");
    				  if( jsnObj ){
    					  jsnObj.duration = searchController.searchPanelModel.duration;
    				  }
    			  }
    		  });
    	  }
      }
      
      // push data to model
      var EndecaPreDefinedCriteria = {								
				"adults": _.has(data, "noOfAdults") ? data.noOfAdults : searchController.searchPanelModel.adults,
			    "seniors": _.has(data, "noOfSeniors") ? data.noOfSeniors : searchController.searchPanelModel.seniors,
			    "children": _.has(data, "noOfChildren") ? data.noOfChildren : searchController.searchPanelModel.children,
			    "childAges": _.has(data, "childrenAge") ? data.childrenAge : searchController.searchPanelModel.childAges,
				"to": units,
				"from": airports
			};
      
      if(searchController.durationPredefined){
    	  dojo.mixin(EndecaPreDefinedCriteria,{
    		  "duration": _.has(data, "duration") ? data.duration : searchController.searchPanelModel.duration
    	  });
      }
      
      //if famlies is predefined then noOfChildren will be 1 
       if(searchController.familiesPredefined){
    	   dojo.mixin(EndecaPreDefinedCriteria,{
	    	   "children": 1,
	   		   "childAges": []
    	   });
       };
		if(searchController.SingleMonthPreDefined){
			var now = new Date(); 
			var RangeStartDate = data.when.split('-'); 
			RangeStartDate = new Date(RangeStartDate[2],RangeStartDate[1] - 1 ,RangeStartDate[0]);
			var nextDate = new Date(now.getFullYear(), now.getMonth() + 1, now.getDate() + 1);
			if(RangeStartDate.valueOf() < now.valueOf()){
				nextDate = dojo.date.locale.format(nextDate, {
	                selector: "date",
	                datePattern: "dd-MM-yyyy"
	            });
				
			}
			dojo.mixin(EndecaPreDefinedCriteria,{
				"date": _.has(data, "when") ? searchController.searchPanelModel.formatDate(data.when, "dd-MM-yyyy", "EEE d MMMM yyyy") : searchController.searchPanelModel.date,
				"until": _.has(data, "until") ? data.until : searchController.searchPanelModel.until		
			})
		};
		searchController.searchPanelModel.onRetrieveSavedObject(EndecaPreDefinedCriteria);
    },

    validateSavedData: function (saved) {
      var searchController = this;
      return (saved) ? searchController.airportGuideStore.requestData(searchController.searchPanelModel.generateQueryObject(saved), true) : null;
    },

    onLazyLoad: function () {
      var searchController = this;
      var touchElement = document.getElementById(dojoConfig.site);
      var IsNotTouch = !dojo.hasClass(touchElement, "touch");

      // if url ends in /packages we are on search results page so show summary
      // if searchResultsPage on tablet ,we show panel not summary section
      if ((searchController.searchResultsPage && IsNotTouch) || searchController.bookflowAccomPage) {
        searchController.searchPanelModel.set("view", "summary");
      } else {
        searchController.searchPanelModel.set("view", "search");
      }
       

      //setTimeout(function () {
        // remove loading class
        dojo.removeClass(searchController.domNode, "loading");
        // publish update to child-ages, chrome/safari has issues with caching values
        dojo.publish("tui.searchPanel.view.ChildSelectOption.updateChildAgeValues");
      //}, 0);
    }

  });

  return tui.smerchOverlayPanel.controller.SearchController;
});