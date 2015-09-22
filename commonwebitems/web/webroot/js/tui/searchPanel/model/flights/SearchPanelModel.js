define("tui/searchPanel/model/flights/SearchPanelModel", [
  "dojo",
  "dojo/store/Observable",
  "tui/searchPanel/model/AirportModel",
  "tui/searchPanel/model/DestinationModel",
  "tui/searchPanel/store/flights/AirportMultiFieldStore",
  "tui/searchPanel/store/DestinationMultiFieldStore",
  "tui/searchPanel/store/flights/ArrivalAirportMultiFieldStore",
  "tui/utils/LocalStorage",
  "dojo/date/locale",
  "dojo/io-query",
  "tui/search/model/flights/SearchModel",
  "tui/search/base/PartyCompositionBase"], function (dojo, Observable, AirportModel, DestinationModel, AirportMultiFieldStore, DestinationMultiFieldStore, ArrivalAirportMultiFieldStore, localStorage) {

  dojo.declare("tui.searchPanel.model.flights.SearchPanelModel", [tui.search.model.flights.SearchModel, tui.search.base.PartyCompositionBase], {

    // ----------------------------------------------------------------------------- properties
    targetUrl: dojoConfig.paths.webRoot + "/packages",

    // Object containing AirportModels of where to fly from.
    from: null,

    // Object containing DestinationModels of where to fly to.
    to: null,

    // departure date for holiday as a string.
    date: null,

    when : null,

    // arrival date for holiday as a string.
    returnDate: null,


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

    // reference for saved search object
    savedSearch: null,

    //flag for return date filed
    oneWayOnly: false,

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

      // initialising stores and setting initial values
      searchPanelModel.from = new Observable(new AirportMultiFieldStore());
      //TODO
      searchPanelModel.to = new Observable(new ArrivalAirportMultiFieldStore());
      searchPanelModel.adults = searchPanelModel.searchConfig.MIN_ADULTS_NUMBER;
      searchPanelModel.childAges = [];
      searchPanelModel.until = "";
      searchPanelModel.date = "";
      searchPanelModel.returnDate = "";
      searchPanelModel.duration = 0;
      searchPanelModel.page = searchPanelModel.searchConfig.PAGE;
      searchPanelModel.flexibleDays = searchPanelModel.searchConfig.FLEXIBLE_DAYS;
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
          datePattern: destPattern || "yyyy-MM-dd"
        });
      }
      return null;
    },

    onStartValidation: function () {
      // summary:
      //    Extend validation to validate data specific to search panel (from/to/date/childAges)
      var searchPanelModel = this;

      // validate that  from  field is not empty.
      if (searchPanelModel.from.data.length === 0) {
        searchPanelModel.searchErrorMessages.set("fromTo", {
          emptyFromTo: searchPanelModel.searchMessaging.errors.foemptyFromTo
        });
      }

    //code added for empty-to
      if (searchPanelModel.to.data.length === 0) {
    	 // alert(searchPanelModel.searchMessaging.errors.emptyTo);
          searchPanelModel.searchErrorMessages.set("emptyTo", {
            emptyTo: searchPanelModel.searchMessaging.errors.emptyTo
          });
        }

      // validate that a date is set
      if (searchPanelModel.date === "" || !searchPanelModel.parseToDate(searchPanelModel.date)) {
        searchPanelModel.searchErrorMessages.set("when", {
          emptyDate: searchPanelModel.searchMessaging.errors.emptyDate
        });
      }

      if(!searchPanelModel.oneWayOnly) {
    	  if (searchPanelModel.date !== "" && searchPanelModel.returnDate === "") {
              searchPanelModel.searchErrorMessages.set("returnTravel", {
                emptyDate: searchPanelModel.searchMessaging.errors.emptyDate
              });
            }
//validate the from date greater than to date
  		else if(searchPanelModel.date !== "" && searchPanelModel.returnDate !== "" &&  searchPanelModel.parseToDate(searchPanelModel.date) > searchPanelModel.parseToDate(searchPanelModel.returnDate)){
        	  searchPanelModel.searchErrorMessages.set("returnTravel", {
                  emptyDate: searchPanelModel.searchMessaging.errors.emptyDate
                });
          }

      }


      // validate that we don't have children with no age set
      if ((searchPanelModel.adults + searchPanelModel.childAges) && _.filter(searchPanelModel.childAges,function (age) {
        return age === -1;
      }).length > 0) {
        searchPanelModel.searchErrorMessages.set("partyChildAges", {
          childNoAges: searchPanelModel.searchMessaging.errors.childNoAges
        });
      }

      /*searchPanelModel.subscribe("tui/searchPanel/model/searchPanelModel/invalidAirportCombinationError", function () {
    	 // alert("searchPanelModel.subscribe");
    	  airportGuide.searchErrorMessages.set("invalidAirportCombination", {
    			 invalidAirportCombination: airportGuide.searchMessaging.errors.invalidAirportCombination
    	   });
        });*/
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
      var searchPanelModel = this,infants=0, infantsAge = [], returnDate="";

      for(var i= 0; i< searchPanelModel.childAges.length; i++){
    	  if(searchPanelModel.childAges[i] <2){
    		  infants++;
    		  infantsAge[infantsAge.length] = searchPanelModel.childAges[i];
    	  }
      }

      //Return Date validation
      if(!searchPanelModel.oneWayOnly){
    	  returnDate = dojo.date.locale.format(searchPanelModel.parseToDate(searchPanelModel.returnDate), {
              selector: "date",
              datePattern: "yyyy-MM-dd"
            })
      }
      return {

			flyingFrom :searchPanelModel.from.data[0].id,
			flyingTo :searchPanelModel.to.data[0].id,
			depDate: dojo.date.locale.format(searchPanelModel.parseToDate(searchPanelModel.date), {
				  		selector: "date",
				  		datePattern: "yyyy-MM-dd"
			}),
			returnDate: returnDate,
			adults: searchPanelModel.adults,
			children: searchPanelModel.children - infants,
			infants:infants,
			infantAge: infantsAge,
			isOneWay : searchPanelModel.oneWayOnly,
			childAge: (searchPanelModel.childAges.length > 0 ) ? searchPanelModel.childAges : ""


			    /* until: searchPanelModel.until,
			flexibility: searchPanelModel.flexible,
			flexibleDays: searchPanelModel.flexibleDays || searchPanelModel.searchConfig.FLEXIBLE_DAYS,
			noOfSeniors: searchPanelModel.seniors,
			duration: searchPanelModel.duration,
			first: searchPanelModel.page,
			*/




       /* airports: searchPanelModel.from.getStorageData(["id", "name"]),
        units: searchPanelModel.to.getStorageData(["id", "name"])          //, "type", "multiSelect"])*/
      };
    },

    generateQueryObject: function (queryObject) {
      // summary:
      //		generates Query Object for server.
      var searchPanelModel = this, from = [], to = []; multiSelect = '';

      queryObject = queryObject || searchPanelModel.parse();


      _.each(queryObject.from, function (item) {
        from.push(item.id);
      });
      queryObject.from = from;

      _.each(queryObject.to, function (item) {
        to.push(item.id + ':' + item.name);
        multiSelect = (item.multiSelect) ? item.multiSelect : "" ;
      });

     /* _.each(queryObject.to, function (item) {
    	  alert("to new");
          to.push(item.id);
        });
*/
      queryObject.to = to;
      queryObject.date = searchPanelModel.formatDate(queryObject.date, null, "dd-MM-yyyy");
      queryObject.returnDate = searchPanelModel.formatDate(queryObject.returnDate, null, "dd-MM-yyyy");
      queryObject.multiSelect = multiSelect;

      return queryObject;
    },

    parse: function () {
      // summary:
      //		Parses data contained in searchPanelModel, for saved storage.
      //		We will only save required search data.
      var searchPanelModel = this;

      var properties = {
        flexible: null,
        date: null,
        returnDate: null,
        adults: null,
        seniors: null,
        children: null,
        childAges: null,
        oneWayOnly:null
      };

      for (var prop in properties) {
        properties[prop] = searchPanelModel[prop];
      }

      // parsing only the important data we need, from the "from" and "to" fields.
      // to save to storage.
      properties.from = searchPanelModel.from.getStorageData(["id", "name", "children", "group", "countryCode","multiSelect"]);
     // properties.to = searchPanelModel.to.getStorageData(["id", "name", "type", "multiSelect"]);
      properties.to = searchPanelModel.to.getStorageData(["id", "name", "children", "group", "countryCode","multiSelect"]);

      // Store the current timestamp for invalidation logic.
      properties.timestamp = (new Date()).getTime();

      return properties;
    },

    onRetrieveSavedObject: function (savedSearch) {
      var searchPanelModel = this;

      // retrieve all props except from, to, date, timestamp
      for (var prop in savedSearch) {
        if (dojo.indexOf(["from", "to", "date", "when", "returnDate", "timestamp"], prop) === -1) {
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

      if(savedSearch.oneWayOnly){
    	  searchPanelModel.oneWayOnly = savedSearch.oneWayOnly;
    	  dojo.style("returnTravel", {opacity:"0.4", cursor: "not-allowed"});
    	  dojo.query("#oneWayCHK").checked = true;
    	  dojo.query("#oneway .checkbox").addClass("checked");
      }

      // retrieve return date (if not in past and if set)
      if (savedSearch.returnDate && !savedSearch.oneWayOnly) {
        var now = new Date();
        now = new Date(now.getFullYear(), now.getMonth(), now.getDate());
        var savedReturnDate = searchPanelModel.parseToDate(savedSearch.returnDate);
        savedReturnDate = new Date(savedReturnDate.getFullYear(), savedReturnDate.getMonth(), savedReturnDate.getDate());

        if (now.valueOf() < savedReturnDate.valueOf()) {
        	searchPanelModel.set("returnDate", savedSearch.returnDate);
        }
      }

      // retrieve from if available
      if (savedSearch.from.length > 0) {
    	searchPanelModel.from.emptyStore();
        searchPanelModel.from.setStorageData(savedSearch.from, AirportModel);
        var fromObject = savedSearch.from[0];
        dojo.query("#wherefromValue").text(fromObject.name  +"  (" + fromObject.id + ")");
        dojo.query("#wherefromPlaceholder").style("display","none");
      }

      // retrieve to if available
      if (savedSearch.to.length > 0) {
        //searchPanelModel.to.setStorageData(savedSearch.to, DestinationModel);
    	  searchPanelModel.to.emptyStore();
    	  searchPanelModel.to.setStorageData(savedSearch.to, AirportModel);
    	  var toObject = savedSearch.to[0];
    	  dojo.query("#wheretoValue").text(toObject.name     +"  (" + toObject.id + ")");
    	  dojo.query("#wheretoPlaceholder").style("display","none");
      }
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
    },



    prePopulateSearchCriteria : function(savedSearchData){

	      var searchPanelModel = this, airports = [], units = [], deptDate, returnDate,searchToSave,totalChildInfant, data = dojo.clone(savedSearchData);

	      //localStorage.removeItem(searchPanelModel.savedSearch);

	      if (_.has(data, "from")) {
	        // push airports as AirportModel
	    	   airports.push(dojo.mixin(new AirportModel(),{
		            id: data.from.id,
		            name: data.from.name,
		            groups: data.from.group,
		            synonym: data.from.synonym,
		            children: data.from.children,
		            countryCode :  data.from.countryCode
		       }));
	      }

	      if (_.has(data, "to")) {
	    	  units.push(new DestinationModel(data.to));
	      }




	     deptDate =   dojo.date.locale.format(new Date(data.departureDate), {
	          selector: "date",
	          datePattern: "dd MMM yyyy"
	        });
	     if(data.returnDate){
		     returnDate=  dojo.date.locale.format(new Date(data.returnDate), {
		    	 selector: "date",
		    	 datePattern: "dd MMM yyyy"
	            });
	     }

	     totalChildInfant = data.childCount + data.infantCount;
	     data.childages = (data.childages == null) ? [] : data.childages;
		 if(totalChildInfant != data.childages.length){
			 if(data.infantCount>0){
				 data.childCount = data.childCount+data.infantCount;
				 dojo.forEach(data.infantAges,function(item){
					 data.childages.push(item);
				 })

			 }
		 }
	      // push data to model
	      searchPanelModel.onRetrieveSavedObject({
	        "flexible": searchPanelModel.flexible,
	        "date": deptDate,
	       	"returnDate":returnDate,
	        "adults": data.adultCount,
	        "seniors": searchPanelModel.seniors,
	        "children":   (data.childCount == null) ? [] : totalChildInfant,
			"childAges": (data.childages == null) ? [] : data.childages,
			"to": units,
	        "from": airports,
	        "oneWayOnly" :JSON.parse(data.oneWay)
	      });

	      	searchToSave = searchPanelModel.parse();
	      	searchPanelModel.saveObject(searchToSave, searchPanelModel.savedSearch);

    }


  });

  return tui.searchPanel.model.flights.SearchPanelModel;
});