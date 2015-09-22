define("tui/search/controller/cruise/SearchController", ["dojo",
  "dojo/has",
  "tui/searchPanel/model/AirportModel",
  "tui/searchPanel/model/DestinationModel",
  "dijit/registry",
  "tui/searchPanel/model/cruise/CruiseSearchPanelModel",
  "tui/widget/popup/Tooltips",
  "tui/widget/popup/ErrorPopup",
  "tui/searchPanel/view/AirportMultiFieldList",
  "tui/searchPanel/view/DestinationMultiFieldList",
  "tui/searchPanel/view/AirportGuide",
  "tui/searchPanel/view/DestinationGuide",
  "tui/searchPanel/view/cruise/AirportGuide",
  "tui/searchPanel/view/cruise/DestinationGuide",
  "tui/searchPanel/view/FlexibleView",
  "tui/searchPanel/view/AdultsSelectOption",
  "tui/searchPanel/view/SeniorsSelectOption",
  "tui/searchPanel/view/ChildSelectOption",
  "tui/searchPanel/view/SearchDatePicker",
  "tui/searchPanel/view/SearchSummary",
  "tui/searchPanel/view/cruise/CruiseAndStayPicker",
  "tui/searchPanel/view/cruise/CruiseSubmitButton",
  "tui/searchPanel/view/cruise/CruiseSearchSummary",
  "tui/search/nls/Searchi18nable",
  "tui/mvc/Controller",
  "tui/searchPanel/view/FlexibleView",
  "tui/searchPanel/view/SubmitButton",
  "tui/searchPanel/store/AirportMultiFieldStore",
  "tui/searchPanel/store/cruise/AirportGuideStore",
  "tui/searchPanel/store/DestinationMultiFieldStore",
  "tui/searchPanel/store/cruise/DestinationGuideStore",
  "tui/searchPanel/store/cruise/DateStore",
  "tui/searchPanel/store/cruise/DurationStore",
  "tui/searchPanel/store/cruise/CruiseAndStayStore"], function (dojo, has, AirportModel, DestinationModel, registry) {

  dojo.declare("tui.search.controller.cruise.SearchController", [tui.mvc.Controller, tui.search.nls.Searchi18nable], {

    // ----------------------------------------------------------------------------- properties

    jsonData: null,

    locationData: null,

    pageId: null,

    searchResultsPage: false,

    retrievalScenario: "localStorage",

    bookPageIds: [
       'cruisesearchresultspage','noResults', 'noresultspage', 'allgonepage', 'noHotelsOverlayPage',
       'bookFlowItineraryMainPage',
       'shipBookItineraryOverviewPage','shipBookItineraryEntertainmentPage','shipBookItineraryDiningPage','shipBookItineraryFacilitiesPage','shipBookItineraryCabinsPage','shipBookItineraryDeckPlansPage',
       'hotelsearchresultspage',
       'cruisebookFlowAccommodationOverviewPage','bookFlowAccommodationLocationPage','bookFlowAccommodationRoomsPage','bookFlowAccommodationFacilitiesPage', 'bookFlowAccommodationThingsToDoPage'
    ],
    // ----------------------------------------------------------------------------- methods

    constructor: function () {
       // summary:
       //		Called on class creation, initialising all widgets views and search model.

        var searchController = this;

        //var mediator = dijit.registry.byId('mediator');
        //mediator ? mediator.registerController(searchController) : null;

        //this flag would ensure date is pre populated on all pages after search is performed.
        searchController.searchResultsPage =  _.contains(searchController.bookPageIds, searchController.pageId);
        searchController.searchPanelModel.searchResultsPage = searchController.searchResultsPage;
        searchController.searchPanelModel.location = searchController.location;

        // @TODO: too many scenarios, refactor if possible
        if (searchController.searchResultsPage && _.has(searchController.jsonData, "searchResquest")) {
            // results page
            // only has searchPanel, use searchRequest data
            searchController.retrievalScenario = "searchResults";
            searchController.searchPanelModel.pageId = searchController.pageId;
            searchController.deferLoading();
        } else if (searchController.searchApi === "getPrice") {
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
        } else if ( searchController.jsonData && (_.has(searchController.jsonData, "searchRequestData") || _.has(searchController.jsonData, "searchResquest") || _.has(searchController.jsonData.requestData, "searchResquest") ) ) {
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
                //TODO:Recheck if this can be modified
                _.each(dojo.query('.custom-dropdown', searchController.domNode), function(domNode){
                    registry.byNode(domNode).hideList();

                });
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
        var promise= promise || "";

        // subscribe to lazyload channel to ensure all widget components have fully loaded.
        dojo.subscribe("tui:channel=lazyload", function () {

          // prepopulate when the promise has returned (immediate response except for validation case)
          dojo.when(promise, function () {

            // initialise (i18n)
            searchController.initSearchMessaging();

            switch (searchController.retrievalScenario) {
              case "searchResults":
            	  searchController.searchPanelModel.useDate = searchController.searchResultsPage;
                searchController.prePopulate(searchController.jsonData.searchResquest);
                break;
              case "getPrice":
            	  var data = searchController.locationData ? dojo.clone(searchController.locationData.searchRequest) : {};
                  //delete data.units;
                  data = dojo.mixin(data, searchController.locationData);
                  searchController.prePopulate(data);
                  break;
              case "searchPanel":
                searchController.prePopulate(searchController.jsonData.searchRequestData || searchController.jsonData.searchResquest);
                break;
              case "location":
                searchController.prePopulate(searchController.locationData);
                break;
              default:
                searchController.searchPanelModel.useDate = searchController.searchResultsPage;
                searchController.searchPanelModel.retrieveSavedObject(searchController.airportGuideStore);
            }

            // after prepopulating
          //  dojo.publish("tui:channel=searchLocalStorageRetrieved", [searchController.searchPanelModel]);
            searchController.searchPanelModel.refresh();
            searchController.onLazyLoad();

          });
        });
      },

      prePopulate: function (data) {
          var searchController = this, airports = [], units = [];



          if (_.has(data, "from")) {
              // push airports as AirportModel
              _.forEach(data.from, function (airport) {
                  searchController.searchPanelModel.from.add(new AirportModel({
                      id: airport.id,
                      name: airport.name,
                      type: airport.type,
                      groups: airport.group,
                      synonym: airport.synonym,
                      children: airport.children
                  }));
              });
          }
          if(searchController.searchResultsPage){
             (_.has(data, "when") && searchController.searchResultsPage) ? searchController.searchPanelModel.formatDate(data.when, "dd-MM-yyyy", "EEE d MMMM yyyy") : null;


             if (_.has(data, "to")) {
                 // push data to model
            	 _.forEach(data.to, function (to) {
                     searchController.searchPanelModel.to.add(new DestinationModel({
                         name: to.name,
                         id: to.id,
                         type: to.type,
                         multiSelect: true
                     }));
                 });
             }

             var searchRequsetData = {
            		"flexible": _.has(data, "flexibility") ? data.flexibility : searchController.searchPanelModel.flexible,
   	               "addAStay": _.has(data, "addAStay") ? data.addAStay : searchController.searchPanelModel.addAStay,
   	               "date": _.has(data, "when") ? searchController.searchPanelModel.formatDate(data.when, "dd-MM-yyyy", "dd-MM-yyyy") : searchController.searchPanelModel.date,
   	               "duration": _.has(data, "duration") ? data.duration : searchController.searchPanelModel.duration,
   	               "adults": _.has(data, "noOfAdults") ? data.noOfAdults : searchController.searchPanelModel.adults,
   	               "seniors": _.has(data, "noOfSeniors") ? data.noOfSeniors : searchController.searchPanelModel.seniors,
   	               "children": _.has(data, "noOfChildren") ? data.noOfChildren : searchController.searchPanelModel.children,
   	               "childAges": _.has(data, "childrenAge") ? data.childrenAge : searchController.searchPanelModel.childAges
	   	      };



              for (var prop in searchRequsetData) {
            	  searchController.searchPanelModel.set(prop, searchRequsetData[prop]);
              }

          }
          else{
              // @TODO: change model field names to match jsonData (enable automated object generating)
              if (_.has(data, "to")) {
                  // push data to model
                  var to = _.isArray(data.to) ? data.to[0]: data.to;
                  if(_.isUndefined(_.find(searchController.searchPanelModel.to.data, function(data){
                      return to.id === data.id;
                  }))){
                      searchController.searchPanelModel.to.add(new DestinationModel({
                          name: to.name,
                          id: to.id,
                          type: to.type,
                          multiSelect: true
                      }));
                  }
              }
          }

      },

    onLazyLoad: function () {
        var searchController = this;

        // if url ends in /packages we are on search results page so show summary
        /*if (searchController.searchResultsPage) {
          searchController.searchPanelModel.set("view", "summary");
        } else {
          searchController.searchPanelModel.set("view", "search");
        }*/
        dojo.publish("tui.searchPanel.view.ChildSelectOption.updateChildAgeValues");

      },

    validateSavedData: function (saved) {
       var searchController = this;
       return (saved) ? searchController.airportGuideStore.requestData(searchController.searchPanelModel.generateQueryObject(saved), true) : null;
    },

    generateRequest: function () {
      return this.searchPanelModel.generatePostObject();
    }

  });

  return tui.search.controller.cruise.SearchController;
});