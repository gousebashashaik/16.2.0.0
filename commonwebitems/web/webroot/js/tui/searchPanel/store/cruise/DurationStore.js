define("tui/searchPanel/store/cruise/DurationStore", [
  "dojo",
  "dojo/io-query",
  "tui/search/store/SearchPanelMemory"], function (dojo, ioQuery) {

  function splitDestinationQuery(query) {
    return query.split(':')[0];
  }

  dojo.declare("tui.searchPanel.store.cruise.DurationStore", [tui.search.store.SearchPanelMemory], {

    targetURL: null,

    constructor: function () {
      var dateStore = this;
      dateStore.inherited(arguments);
    },

    requestData: function (searchPanelModel) {
      var dateStore = this;
      var searchquery = searchPanelModel.generateQueryObject();
      var params = {
        'to[]': searchquery.to,
        'from[]': searchquery.from,
        'when': searchquery.date,
        "addAStay": searchquery.addAStay
      };

      // store with concatenated id of from/to or "all"
      var destinations = _.map(dojo.clone(searchquery.to), function(toItem){
        return splitDestinationQuery(toItem);
      });
      var id = [destinations, searchquery.from].join('');
      id = (id === '') ? "all" : id;
      var storedDates = dateStore.get(id);

      // ajax request if storeddates is empty or doesn't match model
      var results = dojo.xhr("GET", {
        url: dateStore.targetURL + "?" + ioQuery.objectToQuery(params),
        handleAs: "json"
      });

      dojo.when(results, function (dateResultsData) {
        if (storedDates) {
          return;
        }
        var dateStoreData = {
          id: id,
          dates: dateResultsData
        };
        dateStore.put(dateStoreData);
      });

      return results;
    }
  });

  return tui.searchPanel.store.cruise.DurationStore;
});
