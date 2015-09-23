define("tui/search/controller/flights/SearchController", ["dojo",
  "dojo/has",
  "tui/searchPanel/model/AirportModel",
  "tui/searchPanel/model/DestinationModel",
  "tui/searchPanel/model/flights/SearchPanelModel",
  "tui/widget/popup/Tooltips",
  "tui/widget/popup/ErrorPopup",
  "tui/searchPanel/view/flights/AirportMultiFieldList",
  "tui/searchPanel/view/flights/DestinationMultiFieldList",
  "tui/searchPanel/view/flights/ArrivalAirportMultiFieldList",
  "tui/searchPanel/view/flights/AirportGuide",
  "tui/searchPanel/view/flights/DestinationGuide",
  "tui/searchPanel/view/flights/ArrivalAirportGuide",
  "tui/searchPanel/view/FlexibleView",
  "tui/searchPanel/view/flights/AdultsSelectOption",
  "tui/searchPanel/view/flights/SeniorsSelectOption",
  "tui/searchPanel/view/ChildSelectOption",
  "tui/searchPanel/view/flights/SearchDatePicker",
  "tui/searchPanel/view/flights/ArrivalSearchDatePicker",
  "tui/searchPanel/view/SearchSummary",
  "tui/search/nls/Searchi18nable",
  "tui/mvc/Controller",
  "tui/searchPanel/view/flights/SubmitButton",
  "tui/searchPanel/store/flights/AirportMultiFieldStore",
  "tui/searchPanel/store/flights/ArrivalAirportMultiFieldStore",
  "tui/searchPanel/store/AirportGuideStore",
  "tui/searchPanel/store/flights/ArrivalAirportGuideStore",
  "tui/searchPanel/store/DestinationMultiFieldStore",
  "tui/searchPanel/store/DestinationGuideStore",
  "tui/searchPanel/store/DateStore",
  "tui/searchPanel/store/flights/ReturnDateStore"], function (dojo, has, AirportModel, DestinationModel) {

  dojo.declare("tui.search.controller.flights.SearchController", [tui.mvc.Controller, tui.search.nls.Searchi18nable], {

    // ----------------------------------------------------------------------------- properties

    jsonData: null,

    locationData: null,

    pageId: null,

    searchResultsPage: null,

    retrievalScenario: "localStorage",

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
      }
    },

    constructor: function () {
      // summary:
      //		Called on class creation, initialising all widgets views and search model.
      var searchController = this;

      //var mediator = dijit.registry.byId('mediator');
      //mediator ? mediator.registerController(searchController) : null;
      if(searchController.tracsEndDate){
    	  searchController.setDateFormat(searchController.tracsEndDate);
      }


      searchController.searchResultsPage = searchController.pageId === "searchresultspage" || searchController.pageId === "singleaccomsearchresultspage" || searchController.pageId === "flightSearchResultsPage";

      // @TODO: too many scenarios, refactor if possible
      if (searchController.searchResultsPage && _.has(searchController.jsonData, "searchRequest")) {
        // results page
        // only has searchPanel, use searchRequest data
        searchController.retrievalScenario = "searchResults";
        searchController.deferLoading();
      } else if(_.has(searchController.jsonData, "flightSearchCriteria")){
    	  searchController.retrievalScenario = "flightSearchResults";
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

      searchController.attatchEvents();
    },
    attatchEvents: function(){
    	 var searchController = this;
    	 dojo.query("#oneway").on("click", function(evt){
				 // 	event.stop(evt);
				  	searchController.isOnewayChecked(evt);
				});
     },



    deferLoading: function (promise) {
      var searchController = this;

      // subscribe to lazyload channel to ensure all widget components have fully loaded.
      dojo.subscribe("tui:channel=lazyload", function () {

        // prepopulate when the promise has returned (immediate response except for validation case)
        dojo.when(promise, function () {

          // initialise (i18n)
          searchController.initSearchMessaging();
         /* if(searchController.jsonData && searchController.jsonData.flightSearchCriteria){
        	  searchController.retrievalScenario = "flightSearchCriteria"
          }*/

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
            case "location":
              searchController.prePopulate(searchController.locationData);
              break;
            case "flightSearchResults":
            	// no need to populated from here
                break;

            default:
              searchController.searchPanelModel.retrieveSavedObject(searchController.airportGuideStore);
          }

          // after prepopulating
          dojo.publish("tui:channel=searchLocalStorageRetrieved", [searchController.searchPanelModel]);
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

      // push data to model
      searchController.searchPanelModel.onRetrieveSavedObject({
        "flexible": _.has(data, "flexibility") ? data.flexibility : searchController.searchPanelModel.flexible,
        "flexibleDays": _.has(data, "flexibleDays") ? data.flexibleDays : searchController.searchPanelModel.flexibleDays,
        "date": _.has(data, "when") ? searchController.searchPanelModel.formatDate(data.when, "dd-MM-yyyy", "EEE d MMMM yyyy") : searchController.searchPanelModel.date,
       	"returnDate": _.has(data, "retrunTravel") ? searchController.searchPanelModel.formatDate(data.returnTravel, "dd-MM-yyyy", "EEE d MMMM yyyy") : searchController.searchPanelModel.returnDate,
        "adults": _.has(data, "noOfAdults") ? data.noOfAdults : searchController.searchPanelModel.adults,
        "seniors": _.has(data, "noOfSeniors") ? data.noOfSeniors : searchController.searchPanelModel.seniors,
        "children": _.has(data, "noOfChildren") ? data.noOfChildren : searchController.searchPanelModel.children,
        "childAges": _.has(data, "childrenAge") ? data.childrenAge : searchController.searchPanelModel.childAges,
        "to": units,
        "from": airports
      });

    },

    validateSavedData: function (saved) {
      var searchController = this;
      return (saved) ? searchController.airportGuideStore.requestData(searchController.searchPanelModel.generateQueryObject(saved), true) : null;
    },

    onLazyLoad: function () {
      var searchController = this;

      // if url ends in /packages we are on search results page so show summary
      if (searchController.searchResultsPage) {
        searchController.searchPanelModel.set("view", "summary");
      } else {
        searchController.searchPanelModel.set("view", "search");
      }

      setTimeout(function () {
        // remove loading class
        dojo.removeClass(searchController.domNode, "loading");
        // publish update to child-ages, chrome/safari has issues with caching values
        dojo.publish("tui.searchPanel.view.ChildSelectOption.updateChildAgeValues");
      }, 300);
    },

    setDateFormat : function(dateObj){
    	var searchController = this,newDate,
    		parts = dateObj.split('-');
    		newDate = new Date(parts[2],parts[1]-1,parts[0]);
    		searchController.searchPanelModel.tracsEndDate =  newDate;
    		searchController.searchPanelModel.tracsEndDateValidation();

    },
    isOnewayChecked: function(evt){
    	var searchController = this;
    	if(dojo.query(".child-age-label").length != 0 && searchController.searchPanelModel.oneWayOnly === true) {
    		dojo.query(".child-age-label")[0].innerText = "Child ages (on flying date)";
    	}
    	else if(dojo.query(".child-age-label").length != 0) {
    		dojo.query(".child-age-label")[0].innerText = "Child ages (on return date)";
    	}

    }

  });

  return tui.search.controller.flights.SearchController;
});