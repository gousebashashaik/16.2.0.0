define("tui/searchBPanel/store/DestinationGuideStore", [
    "dojo",
    "dojo/io-query",
    "tui/search/store/SearchPanelMemory",
    "dojo/store/Observable"], function (dojo, ioQuery, Store, Observable) {

    dojo.declare("tui.searchBPanel.store.DestinationGuideStore", [tui.search.store.SearchPanelMemory], {

        // ----------------------------------------------------------------------------- properties

        targetURL: null,

        targetURLForCountries: null,

        targetURLForCollections: null,

        products: null,

        // ----------------------------------------------------------------------------- methods

        constructor: function () {
            var destinationGuideStore = this;
            destinationGuideStore.inherited(arguments);
            destinationGuideStore.products = new Observable(new Store());
            destinationGuideStore.alldestinations = new Observable(new Store());

        },

        requestData: function (id, searchQuery , view) {
            /**
             * When id is not specified we get the recommended datafeed.
             * If id matches a country id, we get the data of this country.
             */
            var destinationGuideStore = this;
			//destinationGuideStore.collectionRendered = false;
            var targetURL = destinationGuideStore.targetURL;
            var products;

            var params = {
                'from[]': searchQuery.from,
                'when': searchQuery.date || '',
                'flexible': searchQuery.flexible,
                'multiSelect': searchQuery.multiSelect
            };

            if (id) {
                targetURL = destinationGuideStore.targetURLForCountries;
                params.key = id;

            }
			if(view == "mostPopular"){
                targetURL = destinationGuideStore.targetURLForMostPopular;
			}
            if(view == "collections"){
                targetURL = destinationGuideStore.targetURLForCollections;
            }
            var id = [searchQuery.from, id, searchQuery.date, view].join('-');
            id = (id === '') ? ["all", view].join('') : id;
            var storedData = destinationGuideStore.get(id);

            // fire ajax request if storeddata is empty or doesn't match model
             var results = storedData || dojo.xhr("GET", {
                url: targetURL + "?" + ioQuery.objectToQuery(params),
                handleAs: "json"
            });

            /*if (storedData) {
				destinationGuideStore.collectionRendered = true;
            }*/
            dojo.when(results, function (data) {
                if (storedData) {
                    return;
                }
                var destinationStoreData = {
                    id: id,
                    destinations: data
                };
                destinationGuideStore.put(destinationStoreData);
            });
            if (storedData) {
                return results.destinations;
            }else{
            return results;
        }
        }
    });

    return tui.searchBPanel.store.DestinationGuideStore;
});
