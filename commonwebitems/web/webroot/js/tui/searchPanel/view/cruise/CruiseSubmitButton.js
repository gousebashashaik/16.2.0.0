define("tui/searchPanel/view/cruise/CruiseSubmitButton", [
	"dojo/_base/declare",
	"dojo/on",
    "dojo/has",
    "dojo/query",
	"tui/searchPanel/view/SubmitButton"], function (declare, on, has, query) {

	return declare("tui.searchPanel.view.cruise.CruiseSubmitButton", [tui.searchPanel.view.SubmitButton], {

		// ----------------------------------------------------------------------------- properties

		 targetUrl: dojoConfig.paths.webRoot + "/packages",
		
		// ----------------------------------------------------------------------------- methods

        populateSearchForm: function (searchCriteria) {
            // summary:
            // populates search form with hidden fields containing search criteria
            var submitButton = this,
                airports = [],
                units = [];

            _.forEach(searchCriteria.airports, function (airport) {
                airports.push(airport.id+ ":" +airport.type);
            });
            _.forEach(searchCriteria.units, function (unit) {
                units.push(unit.id + ":" + unit.type);
            });

            submitButton.createInput("from[]", airports.join("|"));
            submitButton.createInput("to[]", units.join("|"));

            var searchCriteriaCopy = dojo.clone(searchCriteria);
            delete searchCriteriaCopy.airports;
            delete searchCriteriaCopy.units;

            _.forEach(searchCriteriaCopy, function (value, key) {
                submitButton.createInput(key, value);
            });

            // send "main search" request type
            submitButton.createInput("searchRequestType", "ins");
            submitButton.createInput("sp", "true");

            // Add brandType if in bookflow & getPrice modal
            // TODO: remove redundant checks
            if(submitButton.widgetController.searchApi === 'getPrice' && submitButton.widgetController.jsonData) {
                if(_.has(submitButton.widgetController.jsonData, "packageData")) {
                    if(_.has(submitButton.widgetController.jsonData.packageData, "brandType")) {
                        submitButton.createInput("brandType", submitButton.widgetController.jsonData.packageData.brandType);
                    }
                }
            }
        }
	});
});
