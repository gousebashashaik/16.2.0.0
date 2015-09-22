define("tui/search/controller/SearchController", ["dojo",
  "dojo/has",
  "dojo/on",
  "dojo/dom-attr",
  "tui/searchPanel/model/AirportModel",
  "tui/searchPanel/model/DestinationModel",
  "tui/searchPanel/model/SearchPanelModel",
  "tui/widget/popup/Tooltips",
  "tui/widget/popup/ErrorPopup",
  "tui/searchPanel/view/AirportMultiFieldList",
  "tui/searchPanel/view/DestinationMultiFieldList",
  "tui/searchPanel/view/AirportGuide",
  "tui/searchPanel/view/DestinationGuide",
  "tui/searchPanel/view/FlexibleView",
  "tui/searchPanel/view/HowLongOptions",
  "tui/searchPanel/view/AdultsSelectOption",
  "tui/searchPanel/view/SeniorsSelectOption",
  "tui/searchPanel/view/ChildSelectOption",
  "tui/searchPanel/view/SearchDatePicker",
  "tui/searchPanel/view/SearchSummary",
  "tui/search/nls/Searchi18nable",
  "tui/mvc/Controller",
  "tui/searchPanel/view/FlexibleView",
  "tui/searchPanel/view/SubmitButton",
  "tui/searchPanel/store/AirportMultiFieldStore",
  "tui/searchPanel/store/AirportGuideStore",
  "tui/searchPanel/store/DestinationMultiFieldStore",
  "tui/searchPanel/store/DestinationGuideStore",
  "tui/searchPanel/store/DateStore"], function (dojo, has, on, domAttr, AirportModel, DestinationModel) {

  dojo.declare("tui.search.controller.SearchController", [tui.mvc.Controller, tui.search.nls.Searchi18nable], {

    // ----------------------------------------------------------------------------- properties

    jsonData: null,

    locationData: null,

    pageId: null,

        registerMediator : true,

    searchResultsPage: null,

    bookflowAccomPage: null,

    retrievalScenario: "localStorage",

    smerchdefaultDuration: null,
    

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

      searchController.searchResultsPage = searchController.pageId === "searchresultspage" || searchController.pageId === "singleaccomsearchresultspage" || searchController.pageId === "newsearchresultspage";

      searchController.bookflowAccomPage = searchController.pageId === "bookFlowAccommodationOverviewPage" || searchController.pageId === "bookFlowAccommodationRoomsPage" || searchController.pageId === "bookFlowAccommodationFacilitiesPage" || searchController.pageId === "bookFlowAccommodationLocationPage" || searchController.pageId === "bookFlowAccommodationThingsToDoPage";

      // @TODO: too many scenarios, refactor if possible
      if (searchController.searchResultsPage && _.has(searchController.jsonData, "searchRequest")) {
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
      var searchKey = dojoConfig.site+"/search/main";
      var jsnObj = localStorage[searchKey] ? JSON.parse(localStorage[searchKey]) : null;

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
      searchController.searchPanelModel.onRetrieveSavedObject({
        "flexible": _.has(data, "flexibility") ? data.flexibility : searchController.searchPanelModel.flexible,
		//we have two searches "smerch search"  & "ordinary search" and each search has its own value for Flexible days at component level which are different 
		//and since common latest criteria is used for both the searches in back end value of one panel is over ridden by the other. 
		//so decided not retain the flexible days value and anyways model will be updated with appropriate component specific value.
        		
        //"flexibleDays": _.has(data, "flexibleDays") ? data.flexibleDays : searchController.searchPanelModel.flexibleDays,
        "date": _.has(data, "when") ? searchController.searchPanelModel.formatDate(data.when, "dd-MM-yyyy", "EEE d MMMM yyyy") : searchController.searchPanelModel.date,
        "duration": _.has(jsnObj, "duration") ? jsnObj.duration : searchController.searchPanelModel.duration,
        		 "adults": _.isEqual(searchController.searchApi,"getPrice") && (location.href.indexOf("\&PassToAccom=true") !== -1 )?searchController.jsonData.searchRequestData.updatedNoOfAdults:_.has(data, "noOfAdults") ? data.noOfAdults : searchController.searchPanelModel.adults,
			        		//_.isEqual(searchController.searchApi,"searchPanel")?2:data.noOfAdults
        "seniors": _.has(data, "noOfSeniors") ? data.noOfSeniors : searchController.searchPanelModel.seniors,
			        "children": _.isEqual(searchController.searchApi,"getPrice") && (location.href.indexOf("\&PassToAccom=true") !== -1 )?searchController.jsonData.searchRequestData.updatedNoOfChildren:_.has(data, "noOfChildren") ? data.noOfChildren : searchController.searchPanelModel.children,
			        		//_.has(data, "noOfChildren") ? data.noOfChildren : searchController.searchPanelModel.children
			        "childAges": _.isEqual(searchController.searchApi,"getPrice")&& (location.href.indexOf("\&PassToAccom=true") !== -1 )?searchController.jsonData.searchRequestData.updatedChildrenAges: _.has(data, "childrenAge") ? data.childrenAge : searchController.searchPanelModel.childAges,
        		//_.has(data, "childrenAge") ? data.childrenAge : searchController.searchPanelModel.childAges
        "to": units,
        "from": airports
      });
      
      //searchType == smerch indicates either smerch results page or smerch no-results page.
	  // updating the duration to the default smerch duration configured 
      
	  if(data.searchType == "smerch" && searchController.smerchdefaultDuration){
		  searchController.searchPanelModel.set("duration",searchController.smerchdefaultDuration);
		}
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
      // US23743 - Removed IsNotTouch from if condition
      if ((searchController.searchResultsPage) || searchController.bookflowAccomPage) {
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

  return tui.search.controller.SearchController;
});