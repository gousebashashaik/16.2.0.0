define("tui/searchBPanel/store/AirportGuideStore", ["dojo",
    "dojo/io-query",
    "tui/search/store/SearchPanelMemory"], function (dojo, ioQuery) {

    dojo.declare("tui.searchBPanel.store.AirportGuideStore", [tui.search.store.SearchPanelMemory], {

        // ----------------------------------------------------------------------------- properties

        targetURL: null,

        // ----------------------------------------------------------------------------- methods

        constructor: function () {
            var airportGuideStore = this;
            airportGuideStore.inherited(arguments);
        },

        requestData: function (/*Object: searchPanelModel.generateQueryObject()*/ queryObject, /*Boolean*/ validationRequest) {
            var airportGuideStore = this;

            var params = {
                "to[]": queryObject.to,
                "when": queryObject.date || "",
                "flexible": queryObject.flexible,
                'multiSelect': queryObject.multiSelect
            };

            var results = dojo.xhr("GET", {
                url: airportGuideStore.targetURL + "?" + ioQuery.objectToQuery(params),
                handleAs: "json"
            });


            dojo.when(results, function (airportGuideResultsData) {
                if (!validationRequest) {
                    // airport guide request, only return airports
                    _.forEach(airportGuideResultsData.airports, function (airport) {
                        airportGuideStore.put(airport);
                    });
                } else {
                    // validation request, return everything
                    _.forEach(airportGuideResultsData, function (item) {
                        _.forEach(item, function (airport) {
                            airportGuideStore.put(airport);
                        });
                    });
                }
            });

            return results;
        }
    });

    return tui.searchBPanel.store.AirportGuideStore;
});