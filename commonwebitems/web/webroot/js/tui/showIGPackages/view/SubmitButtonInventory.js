define("tui/showIGPackages/view/SubmitButtonInventory", [
	"dojo",
	"dojo/on",
    "dojo/has",
    "tui/showIGPackages/Mediator",
	"tui/widget/_TuiBaseWidget"], function (dojo, on, has) {

	dojo.declare("tui.showIGPackages.view.SubmitButtonInventory", [tui.searchPanel.view.SubmitButton], {

		// ----------------------------------------------------------------------------- properties

		form: null,

		formSelector: null,

		 targetUrl: dojoConfig.paths.webRoot + "/packages",
		
		jsonData:null,
		
		mediator:null,
		
		//packagesView: null,

		// ----------------------------------------------------------------------------- methods

		populateSearchForm: function (searchCriteria) {
			// summary:
			// populates search form with hidden fields containing search criteria
			var submitButton = this,
					airports = [],
					units = [];

			
			 var invControllers = submitButton.mediator.controllers;
			
			_.forEach(invControllers, function (invController) {
				if(invController["data-klass-id"] == "showIGPackagesComponent"){
					submitButton.jsonData = invController.holidays; 
				}
			});
			_.forEach(submitButton.jsonData, function (holiday) {
				searchCriteria.units.push(new Object({
					id: holiday.accommodation.code,
					name: holiday.accommodation.name,
					type: holiday.accommodation.accomType
				}));
			});
			
			_.forEach(searchCriteria.units, function (unit) {
				units.push(unit.id + ":" + unit.type)
			});
			
			_.forEach(searchCriteria.airports, function (airport) {
				airports.push(airport.id)
			});
			
			submitButton.createInput("airports[]", airports.join("|"));
			submitButton.createInput("units[]", units.join("|"));

			var searchCriteriaCopy = dojo.clone(searchCriteria);
			delete searchCriteriaCopy.airports;
			delete searchCriteriaCopy.units;

			_.forEach(searchCriteriaCopy, function(value, key){
				submitButton.createInput(key, value);
			});

			// send "main search" request type
			submitButton.createInput("searchRequestType", "ins");
			submitButton.createInput("sp", "true");
		}
	});

	return tui.showIGPackages.view.SubmitButtonInventory;
});
