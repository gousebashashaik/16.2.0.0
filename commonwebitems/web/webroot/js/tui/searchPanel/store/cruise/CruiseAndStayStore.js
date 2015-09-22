define("tui/searchPanel/store/cruise/CruiseAndStayStore", [
  "dojo",
  "dojo/io-query",
  "tui/search/store/SearchPanelMemory"], function (dojo, ioQuery) {

  function splitDestinationQuery(query) {
    return query.split(':')[0];
  }

  dojo.declare("tui.searchPanel.store.cruise.CruiseAndStayStore", [tui.search.store.SearchPanelMemory], {

    targetURL: null,

    constructor: function () {
      var dateStore = this;
      dateStore.inherited(arguments);
    },

    requestData: function (/*Object: searchPanelModel.generateQueryObject()*/ queryObject, /*Boolean*/ validationRequest) {
      var dateStore = this;

      var params = {
        'to[]': queryObject.to,
        'from[]': queryObject.from,
        "when": queryObject.date,
        "flexible": queryObject.flexible,
        "duration": queryObject.duration
      };

      // ajax request if storeddates is empty or doesn't match model
      var results = dojo.xhr("GET", {
          url: dateStore.targetURL + "?" + ioQuery.objectToQuery(params),
          handleAs: "json"
      });

      dojo.when(results, function (airportGuideResultsData) {
          if (!validationRequest) {
              // airport guide request, only return airports
              _.forEach(airportGuideResultsData.airports, function (airport) {
                  dateStore.put(airport);
              });
          } else {
              // validation request, return everything
              _.forEach(airportGuideResultsData, function (item) {
                  _.forEach(item, function (airport) {
                      dateStore.put(airport);
                  });
              });
          }
      });
      
      return results;
    }
  });

  return tui.searchPanel.store.cruise.CruiseAndStayStore;
});
