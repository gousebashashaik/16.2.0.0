define("tui/searchPanel/model/cruise/CruiseSearchPanelModel", [
  "dojo/_base/declare",
  "dojo/store/Observable",
  "dojo/_base/array",
  "tui/searchPanel/store/AirportMultiFieldStore",
  "tui/searchPanel/store/DestinationMultiFieldStore",
  "tui/searchPanel/model/SearchPanelModel"], function (declare, Observable, array, AirportMultiFieldStore, DestinationMultiFieldStore) {

  return declare("tui.searchPanel.model.cruise.CruiseSearchPanelModel", [tui.searchPanel.model.SearchPanelModel], {

    // ----------------------------------------------------------------------------- properties
    targetUrl: dojoConfig.paths.webRoot + "/packages",

    // Default duration
    addAStay: null,

    useDate: false,

    pageId: "",

    // ----------------------------------------------------------------------------- methods

    initialise: function () {
      var searchPanelModel = this;

      // initialising stores and setting initial values
      searchPanelModel.from = new Observable(new AirportMultiFieldStore());
      searchPanelModel.to = new Observable(new DestinationMultiFieldStore());
      searchPanelModel.adults = searchPanelModel.searchConfig.MIN_ADULTS_NUMBER;
      searchPanelModel.childAges = [];
      searchPanelModel.until = "";
      searchPanelModel.date = "";
      searchPanelModel.duration = '0';
      searchPanelModel.addAStay = '0';
      searchPanelModel.page = searchPanelModel.searchConfig.PAGE;
      searchPanelModel.flexibleDays = searchPanelModel.searchConfig.FLEXIBLE_DAYS;
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


      // validate that a date is set - TODO
      if (searchPanelModel.date === "" || !searchPanelModel.parseToDate(searchPanelModel.date, 'dd-MM-yyyy')) {
        searchPanelModel.searchErrorMessages.set("when", {
          emptyDate: searchPanelModel.searchMessaging[dojoConfig.site].errors.emptyDate
        });
      }

   // validate that a duration is set - TODO
      if (searchPanelModel.duration === 0 ) {
        searchPanelModel.searchErrorMessages.set("duration", {
          emptyDuration: searchPanelModel.searchMessaging[dojoConfig.site].errors.emptyDuration
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

    generatePostObject: function () {
      // summary:
      //		generates Post Object for search panel
      var searchPanelModel = this;

      return {
    	  //TODO  error in parseToDate func. So commenting the below date format. Need to update
        when: searchPanelModel.date, /*dojo.date.locale.format(searchPanelModel.parseToDate(searchPanelModel.date), {
          selector: "date",
          datePattern: "dd-MM-yyyy"
        })*/
        until: searchPanelModel.until,
        flexibility: searchPanelModel.flexible,
        flexibleDays: searchPanelModel.flexibleDays || searchPanelModel.searchConfig.FLEXIBLE_DAYS,
        noOfAdults: searchPanelModel.adults,
        noOfSeniors: searchPanelModel.seniors,
        noOfChildren: searchPanelModel.children,
        childrenAge: searchPanelModel.childAges,
        duration: searchPanelModel.duration,
        addAStay: searchPanelModel.addAStay,
        page: searchPanelModel.page,
        airports: searchPanelModel.from.getStorageData(["id", "name", "type"]),
        units: searchPanelModel.to.getStorageData(["id", "name", "type"])
      };
    },

    generateQueryObject: function (queryObject) {
      // summary:
      //		generates Query Object for server.
      var searchPanelModel = this, from = [], to = [], multiSelect = '';
      queryObject = queryObject || searchPanelModel.parse();

      _.each(queryObject.from, function (item) {
        from.push(item.id + ':' + item.type);
      });
      queryObject.from = from;

      _.each(queryObject.to, function (item) {
        to.push(item.id + ':' + item.type);
        multiSelect = item.multiSelect;
      });

      queryObject.to = to;
      queryObject.date = queryObject.date ; //searchPanelModel.formatDate(queryObject.date);
      queryObject.duration = queryObject.duration;
      queryObject.addAStay = queryObject.addAStay;

      return queryObject;
    },

    parseToDate: function (date, pattern) {
      var searchPanelModel = this;
      pattern = "dd-MM-yyyy";
      // if date is not set return false.
      if (date) {
        return dojo.date.locale.parse(date, {
          datePattern: pattern || searchPanelModel.searchConfig.DATE_PATTERN,
          selector: "date"
        });
      }
      return false;
    },

    parse: function () {
      // summary:
      //		Parses data contained in searchPanelModel, for saved storage.
      //		We will only save required search data.
      var searchPanelModel = this;

      var properties = {
        flexible: null,
        date: null,
        duration:null,
        addAStay:null,
        adults: null,
        seniors: null,
        children: null,
        childAges: null
      };

      for (var prop in properties) {
        properties[prop] = searchPanelModel[prop];
      }

      // parsing only the important data we need, from the "from" and "to" fields.
      // to save to storage.
      properties.from = searchPanelModel.from.getStorageData(["id", "name", "children", "type", "group"]);
      properties.to = searchPanelModel.to.getStorageData(["id", "name", "type"]);

      // Store the current timestamp for invalidation logic.
      properties.timestamp = (new Date()).getTime();

      return properties;
    },

    checkSearchEntries: function(savedSearch, airportStore){
      var searchPanelModel = this;

      //no need to retrieve date anyways..
        if(!searchPanelModel.useDate){
          savedSearch.date = null;
        }

        if(savedSearch.from.length === 0) return savedSearch;

        savedSearch.from = array.filter(savedSearch.from, function(item, i){
              var result = _.where(_.flatten(airportStore.query()), {id: item.id, available: true});
              return (result.total !== 0);
          });
        return savedSearch;
    }
  });
});