define("tui/searchPanel/store/flights/ReturnDateStore", [
  "dojo",
  "dojo/io-query",
  "tui/search/store/SearchPanelMemory"], function (dojo, ioQuery) {

  function splitDestinationQuery(query) {
    return query.split(':')[0];
  }

  dojo.declare("tui.searchPanel.store.flights.ReturnDateStore", [tui.search.store.SearchPanelMemory], {

    targetURL: null,

    constructor: function () {
      var returnDateStore = this;
      returnDateStore.inherited(arguments);
    },

    requestData: function (searchPanelModel) {
      var returnDateStore = this;
      var searchquery = searchPanelModel.generateQueryObject();
      var params = {
        'to[]': searchquery.to,
        'from[]': searchquery.from,
        'when': searchquery.date || "",
        'multiSelect': searchquery.multiSelect
      };

      // store with concatenated id of from/to or "all"
      var destinations = _.map(dojo.clone(searchquery.to), function(toItem){
        return splitDestinationQuery(toItem);
      });
      var id = [destinations, searchquery.from].join('');
      id = (id === '') ? "all" : id;
      var storedDates = returnDateStore.get(id);
      var xhrBool = false;
      /*if(storedDates !== undefined){
    	  if(storedDates.timeStamp != params.when) {
    		xhrBool = true;
       	  // 	storedDates.remove(id); 
    		  
    	  }
    	 
      } else if(storedDates == undefined){
    	  xhrBool = true;
      }*/
      if(storedDates !== undefined){ 
    	  if(storedDates.timeStamp != params.when) {
    		  returnDateStore.remove(id);
    	  }
      }
      
      // ajax request if storeddates is empty or doesn't match model
      var results = storedDates || dojo.xhr("GET", {
        url: returnDateStore.targetURL + "?" + ioQuery.objectToQuery(params),
        handleAs: "json"
      });
      
      dojo.when(results, function (dateResultsData) {
       if (storedDates) {
          return;
        }
        var dateStoreData = {
          id: id,
          dates: dateResultsData,
          timeStamp: params.when
        };
        returnDateStore.put(dateStoreData);
      });

      return results;
    }
  });

  return tui.searchPanel.store.flights.ReturnDateStore;
});
