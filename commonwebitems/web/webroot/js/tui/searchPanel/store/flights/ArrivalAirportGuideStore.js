define("tui/searchPanel/store/flights/ArrivalAirportGuideStore", ["dojo",
    "dojo/io-query",
    "tui/search/store/SearchPanelMemory"], function (dojo, ioQuery) {

    dojo.declare("tui.searchPanel.store.flights.ArrivalAirportGuideStore", [tui.search.store.SearchPanelMemory], {

        // ----------------------------------------------------------------------------- properties

        targetURL: null,

        // ----------------------------------------------------------------------------- methods

        constructor: function () {
            var arrivalAirportGuideStore = this;
            arrivalAirportGuideStore.inherited(arguments);
        },

        requestData: function (/*Object: searchPanelModel.generateQueryObject()*/ queryObject, /*Boolean*/ validationRequest) {
            var arrivalAirportGuideStore = this;

            var params = {
                'from[]': queryObject.from,
                "when": queryObject.date || "",
                "flexible": queryObject.flexible,
                'multiSelect': queryObject.multiSelect
            };

            var results = dojo.xhr("GET", {
                url: arrivalAirportGuideStore.targetURL + "?" + ioQuery.objectToQuery(params),
                handleAs: "json"
            });


            dojo.when(results, function (arrivalAirportGuideResultsData) {
                if (!validationRequest) {
                    // airport guide request, only return airports
                    _.forEach(arrivalAirportGuideResultsData.airports, function (airport) {
                    	arrivalAirportGuideStore.put(airport);
                    });
                } else {
                    // validation request, return everything
                    _.forEach(arrivalAirportGuideResultsData, function (item) {
                        _.forEach(item, function (airport) {
                        	arrivalAirportGuideStore.put(airport);
                        });
                    });
                }
            });

            return results;
        }
    });

    return tui.searchPanel.store.flights.ArrivalAirportGuideStore;
});