define("tui/searchPanel/model/SearchPanelModel", [
  "dojo",
  "dojo/store/Observable",
  "tui/searchPanel/model/AirportModel",
  "tui/searchPanel/model/DestinationModel",
  "tui/searchPanel/store/AirportMultiFieldStore",
  "tui/searchPanel/store/DestinationMultiFieldStore",
  "dojo/date/locale",
  "dojo/io-query",
  "tui/search/nls/Searchi18nable",
  "tui/search/model/SearchModel",
  "tui/search/base/PartyCompositionBase"], function (dojo, Observable, AirportModel, DestinationModel, AirportMultiFieldStore, DestinationMultiFieldStore) {

  dojo.declare("tui.searchPanel.model.SearchPanelModel", [tui.search.model.SearchModel, tui.search.base.PartyCompositionBase], {

    // ----------------------------------------------------------------------------- properties
    targetUrl: dojoConfig.paths.webRoot + "/packages",

    // Object containing AirportModels of where to fly from.
    from: null,

    // Object containing DestinationModels of where to fly to.
    to: null,

    // departure date for holiday as a string.
    date: null,

    // departure date range > set in results.
    until: null,

    // Flag for date flexibility.
    flexible: true,

    // number of flexible days
    flexibleDays: 0,

    // Default duration
    duration: null,

    // search view (search or summary)
    view: null,

    // search has valid route (boolean)
    validRoute: true,

   // searchType (search or smerch)
    searchType : "search",

    // reference for saved search object
    savedSearch: null,

    // ----------------------------------------------------------------------------- methods

    constructor: function (props) {
      // summary:
      //		Called on class creation, initialising model data.
      var searchPanelModel = this;
      dojo.mixin(searchPanelModel, props);

      searchPanelModel.initialise();
    },

    initialise: function () {
      var searchPanelModel = this;
      var firstDurationDom = dojo.query("select[name='howLong'] option:first-child")[0];

      // initialising stores and setting initial values
      searchPanelModel.from = new Observable(new AirportMultiFieldStore());
      searchPanelModel.to = new Observable(new DestinationMultiFieldStore());
      searchPanelModel.adults = searchPanelModel.searchConfig.MIN_ADULTS_NUMBER;
      searchPanelModel.childAges = [];
      searchPanelModel.until = "";
      searchPanelModel.date = "";
      searchPanelModel.page = searchPanelModel.searchConfig.PAGE;
      searchPanelModel.flexibleDays = searchPanelModel.searchConfig.FLEXIBLE_DAYS;
      searchPanelModel.searchType = searchPanelModel.searchType;
      if(firstDurationDom){
      	searchPanelModel.duration = firstDurationDom.value; //searchPanelModel.searchConfig.DEFAULT_HOWLONG;
      }
    },

    resetStores: function (stores) {
      var searchPanelModel = this;
      if(_.isArray(stores)) {
        _.each(stores, function(store){
          searchPanelModel[store].emptyStore();
        });
      } else {
        searchPanelModel[stores].emptyStore();
      }
    },

    // fields : {'adults' : 0, 'children' : 0}
    resetFields: function(fields) {
      var searchPanelModel = this;
      _.each(fields, function(value, fieldName){
        searchPanelModel.set(fieldName, value);
      });
    },

    parseToDate: function (date, pattern) {
      var searchPanelModel = this;

      // if date is not set return false.
      if (date) {
        return dojo.date.locale.parse(date, {
          datePattern: pattern || searchPanelModel.searchConfig.DATE_PATTERN,
          selector: "date"
        });
      }
      return false;
    },

    formatDate: function (date, srcPattern, destPattern) {
      var searchPanelModel = this;
      var newDate = searchPanelModel.parseToDate(date, srcPattern);
      if (newDate) {
        return dojo.date.locale.format(newDate, {
          selector: "date",
          datePattern: destPattern || "dd-MM-yyyy"
        });
      }
      return null;
    },

    onStartValidation: function () {
      // summary:
      //    Extend validation to validate data specific to search panel (from/to/date/childAges)
      var searchPanelModel = this;

      // validate that both from and to fields are not empty.
      if (searchPanelModel.from.data.length === 0 && searchPanelModel.to.data.length === 0) {
        searchPanelModel.searchErrorMessages.set("fromTo", {
          emptyFromTo: searchPanelModel.searchMessaging[dojoConfig.site].errors.emptyFromTo
        });
      }
		else{
			// validate that only from field is not empty.
			if(searchPanelModel.searchApi === "searchPanel"){
		     if (searchPanelModel.from.data.length === 0) {
				searchPanelModel.searchErrorMessages.set("from", {
					emptyFrom: searchPanelModel.searchMessaging[dojoConfig.site].errors.emptyFrom
				});
			  }
			}
	   }

      // validate that a date is set
      if (searchPanelModel.date === "" || !searchPanelModel.parseToDate(searchPanelModel.date)) {
        searchPanelModel.searchErrorMessages.set("when", {
          emptyDate: searchPanelModel.searchMessaging[dojoConfig.site].errors.emptyDate
        });
      }

      // validate that we don't have children with no age set
      if ((searchPanelModel.adults + searchPanelModel.childAges) && _.filter(searchPanelModel.childAges,function (age) {
        return age === -1;
      }).length > 0) {
        searchPanelModel.searchErrorMessages.set("partyChildAges", {
          childNoAges: searchPanelModel.searchMessaging[dojoConfig.site].errors.childNoAges
        });
      }
    },

    submit: function () {
      // summary:
      //		Submits and saves search model if validation is passed.
      var searchPanelModel = this;

      if (searchPanelModel.validate()) {
        var searchToSave = searchPanelModel.parse();
        searchPanelModel.saveObject(searchToSave, searchPanelModel.savedSearch);
        var searchCriteria = searchPanelModel.generatePostObject();
        dojo.publish("tui:channel=validSearchModel", [searchCriteria, searchPanelModel]);
      }
    },

    generatePostObject: function () {
      // summary:
      //		generates Post Object for search panel
      var searchPanelModel = this;
        if(searchPanelModel.searchType == "smerch") {
          searchPanelModel.flexibleDays = 30;
        }
      return {
        when: dojo.date.locale.format(searchPanelModel.parseToDate(searchPanelModel.date), {
          selector: "date",
          datePattern: "dd-MM-yyyy"
        }),
        until: searchPanelModel.until,
        flexibility: searchPanelModel.flexible,
        flexibleDays: searchPanelModel.flexibleDays || searchPanelModel.searchConfig.FLEXIBLE_DAYS,
        noOfAdults: searchPanelModel.adults,
        noOfSeniors: searchPanelModel.seniors,
        noOfChildren: searchPanelModel.children,
        childrenAge: searchPanelModel.childAges,
        duration: searchPanelModel.duration,
        first: searchPanelModel.page,
        searchType: searchPanelModel.searchType, // to tell whether the search is initiated from search panel or smerch overlay
        airports: searchPanelModel.from.getStorageData(["id", "name", "children", "group"]),
        units: searchPanelModel.to.getStorageData(["id", "name", "type", "multiSelect"])
      };
    },

    generateQueryObject: function (queryObject) {
      // summary:
      //		generates Query Object for server.
      var searchPanelModel = this, from = [], to = [], multiSelect = '';
      queryObject = queryObject || searchPanelModel.parse();

      _.each(queryObject.from, function (item) {
        from.push(item.id);
      });
      queryObject.from = from;

      _.each(queryObject.to, function (item) {
        to.push(item.id + ':' + item.name);
        multiSelect = item.multiSelect;
      });

      queryObject.to = to;
      queryObject.date = searchPanelModel.formatDate(queryObject.date);
      queryObject.multiSelect = multiSelect;

      return queryObject;
    },

    parse: function () {
      // summary:
      //		Parses data contained in searchPanelModel, for saved storage.
      //		We will only save required search data.
      var searchPanelModel = this;
      if(dojo.query("select#howLong").length){
      if( !dojo.query("select#howLong option[value='"+ searchPanelModel.duration +"']").length ){

    	   	var searchKey = dojoConfig.site+"/search/main";
    	    var jsnObj = localStorage[searchKey] ? JSON.parse(localStorage[searchKey]) : null;

			var select = dojo.query("select[name='howLong']")[0];
			if(!jsnObj || jsnObj.duration !=  select.options[select.selectedIndex].value ){

				searchPanelModel.duration = select.options[select.selectedIndex].value;
			}else{
				searchPanelModel.duration = jsnObj.duration;
			}
		}
      }

      var properties = {
        flexible: null,
        date: null,
        until:null,
        adults: null,
        seniors: null,
        children: null,
        childAges: null,
        duration: null
      };

      for (var prop in properties) {
        properties[prop] = searchPanelModel[prop];
      }

      // parsing only the important data we need, from the "from" and "to" fields.
      // to save to storage.
      properties.from = searchPanelModel.from.getStorageData(["id", "name", "children", "group"]);
      properties.to = searchPanelModel.to.getStorageData(["id", "name", "type", "multiSelect"]);

      // Store the current timestamp for invalidation logic.
      properties.timestamp = (new Date()).getTime();

      return properties;
    },

    onRetrieveSavedObject: function (savedSearch) {
      var searchPanelModel = this;

      // retrieve all props except from, to, date, timestamp
      for (var prop in savedSearch) {
        if (_.indexOf(["from", "to", "date", "timestamp"], prop) === -1) {
          searchPanelModel.set(prop, savedSearch[prop]);
        }
      }

      // retrieve date (if not in past and if set)
      if (savedSearch.date) {
        var now = new Date();
        now = new Date(now.getFullYear(), now.getMonth(), now.getDate());
        var savedDate = searchPanelModel.parseToDate(savedSearch.date);
        savedDate = new Date(savedDate.getFullYear(), savedDate.getMonth(), savedDate.getDate());

        if (now.valueOf() < savedDate.valueOf()) {
          searchPanelModel.set("date", savedSearch.date);
        }
      }

      // retrieve from if available
      if (savedSearch.from.length > 0) {
        searchPanelModel.from.setStorageData(savedSearch.from, AirportModel);
      }

      // retrieve to if available
      if (savedSearch.to.length > 0) {
        searchPanelModel.to.setStorageData(savedSearch.to, DestinationModel);
      }

      //console.log(searchPanelModel.childAges)
    },

    refresh: function () {
      // summary:
      //		Reset the model with current stored values.
      var searchPanelModel = this;
      for (var prop in searchPanelModel) {
        if (!dojo.isFunction(searchPanelModel[prop])) {
          searchPanelModel.set(prop, searchPanelModel[prop]);
        }
      }
    }

  });

  return tui.searchPanel.model.SearchPanelModel;
});