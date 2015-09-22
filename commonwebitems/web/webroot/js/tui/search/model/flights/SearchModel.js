define("tui/search/model/flights/SearchModel", [
	"dojo",
	"dojo/Stateful",
	"tui/utils/LocalStorage",
	"dojo/_base/array",
	"dojo/_base/json",
	"tui/search/nls/Searchi18nable",
	"tui/search/base/SearchValidationBase"], function (dojo, Stateful, localStorage, array) {

	dojo.declare("tui.search.model.flights.SearchModel", [dojo.Stateful, tui.search.nls.Searchi18nable, tui.search.base.SearchValidationBase], {

		// ----------------------------------------------------------------------------- properties

		// Length of time to save object
		//saveLength: false,

		targetUrl: null,

		searchConfig: null,

		// ----------------------------------------------------------------------------- methods

		constructor: function(){
			// summary:
			//		Called on class creation, initialising model data..
			var searchModel = this;
			searchModel.searchErrorMessages = new Stateful({
				from: {},
				to: {},
				fromTo: {},
				emptyTo:{},
				when: {},
				returnTravel: {},
				partyComp: {},
				partyChildAges: {},
				duration: {},
				invalidDepartureAirportCombination:{},
				invalidArrivalAirportCombination:{},
				invalidDepartureandArrivalAirportCombination:{},
				invalidFromAndToFlyingCombination:{}
			});
			searchModel.initSearchMessaging();
		},

		saveObject: function(object, name) {
			object = dojo.toJson(object);
      try {
        localStorage.setItem(name, object);
      } catch (e) {
        _.debug("Can't save state in secure mode");
      }
		},

		getSavedSearch: function(){
			var searchModel = this, savedSearch;
      try {
        savedSearch = localStorage.getItem(searchModel.savedSearch);
      } catch (e) {
        _.debug("Can't restore state in secure mode");
      }
			return (savedSearch) ? dojo.fromJson(savedSearch) : null;
		},

		checkSearchEntries: function(savedSearch, airportStore){
			// summary:
			//		Checks saved airports, with data in given store. To see if search is still valid.
			//    If no airports in search, ignore.
			//    TODO: ideally, should check date against destination as well.
			if(savedSearch.from.length === 0) return savedSearch;

			savedSearch.from = array.filter(savedSearch.from, function(item, i){
				var result = airportStore.query({id: item.id, available: true});
				return (result.total !== 0);
			});

			// no valid airports, so set date to null
			if (savedSearch.from.length === 0){
				savedSearch.date = null;
				savedSearch.returnDate = null;
			}

			return savedSearch;
		},

		retrieveSavedObject: function (airportStore) {
			// summary:
			//		Method which retrieves saved data from local storage if it exists
			//		For IE browser below version 8, we will retrieve the data from IE userData.
			//
			//  return: Object found in local store.
			var searchModel = this;

			var savedSearch = searchModel.getSavedSearch();
			if(!savedSearch) {
				return;
			}

			// Invalidate object data after 30 days
			if (savedSearch.timestamp &&
					(new Date()).getTime() - savedSearch.timestamp > 1000 * 60 * 60 * 24 * searchModel.searchConfig.savedSearchExpiryDays) {
				localStorage.removeItem(searchModel.savedSearch);
			} else {
        // validate saved airports to check they are still valid.
        if (airportStore.data.length !== 0 ){
            savedSearch = searchModel.checkSearchEntries(savedSearch, airportStore);
        }
				searchModel.onRetrieveSavedObject(savedSearch);
			}
		},

		onRetrieveSavedObject: function(savedObject){},

		generateQueryObject: function(queryObject){},

		generatePostObject: function(){},

		parse: function(){}

	});

	return tui.search.model.flights.SearchModel;
});