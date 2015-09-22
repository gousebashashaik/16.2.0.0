define("tui/searchPanel/store/cruise/DestinationGuideStore", [
    "dojo",
    "dojo/io-query",
    "tui/search/store/SearchPanelMemory",
    "dojo/store/Observable"], function (dojo, ioQuery, Store, Observable) {

    dojo.declare("tui.searchPanel.store.cruise.DestinationGuideStore", [tui.search.store.SearchPanelMemory], {

        // ----------------------------------------------------------------------------- properties

        targetURL: null,

        targetURLForCountries: null,

        products: null,

        // ----------------------------------------------------------------------------- methods

        constructor: function () {
            var destinationGuideStore = this;
            destinationGuideStore.inherited(arguments);
            destinationGuideStore.products = new Observable(new Store());
        },

        requestData: function (id, searchQuery) {
            /**
             * When id is not specified we get the recommended datafeed.
             * If id matches a country id, we get the data of this country.
             */
            var destinationGuideStore = this;
            var targetURL = destinationGuideStore.targetURL;
            var products;

            var params = {
                'from[]': searchQuery.from,
                'when': searchQuery.date,
                'flexible': searchQuery.flexible,
                'duration': searchQuery.duration,
                "addAStay": searchQuery.addAStay
            };

            if (id) {
                targetURL = destinationGuideStore.targetURLForCountries;
                params.to = id;
            }

            var results = dojo.xhr("GET", {
                url: targetURL + "?" + ioQuery.objectToQuery(params),
                handleAs: "json"
            });

            dojo.when(results, function (response) {

                // store response data as normal
                destinationGuideStore.put(response);

                // separate store for products as special case
                if (_.has(response, "suggestions")) {
                    products = _.filter(response.suggestions, function (suggestion) {
                        return (suggestion.id === "HolidayTypes");
                    });
                    if(products.length) {
                        _.forEach(products[0].children, function(product){
                            destinationGuideStore.products.put(product);
                        });
                    }
                }
                //console.log(destinationGuideStore.products.query())
            });

            return results;
        }
    });

    return tui.searchPanel.store.cruise.DestinationGuideStore;
});
