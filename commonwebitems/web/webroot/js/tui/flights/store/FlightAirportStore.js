define("tui/flights/store/FlightAirportStore", [
	"dojo",
	"dojo/store/Memory"], function (dojo, memory) {
	dojo.declare("tui.flights.store.FlightAirportStore", [dojo.store.Memory], {
		
		airportStore: null,
		
		airportCodes: [],
		
		airports: [],
		
		targetURL: "",
		
		constructor: function(params){
			var flightAirportStore = this;
			
			flightAirportStore.targetURL = params.targetURL;
			
			var results = flightAirportStore.requestData();
			
			results.then(function(airportData){
				flightAirportStore.airportStore = null;
				flightAirportStore.airportStore = airportData;
			});
			
			flightAirportStore.renderAirportCodes();
		},
		
		/*fetch airport codes*/
		renderAirportCodes: function(){
			var flightAirportStore = this;
			flightAirportStore.airportCodes = _.keys(flightAirportStore.airportStore);
			flightAirportStore.fetchAirportNames();
		},
		
		/*fetch airport names*/
		fetchAirportNames: function(){
			var flightAirportStore = this;
			_.each(flightAirportStore.airportCodes, function(code){
					_.each(flightAirportStore.airportStore[code], function(airportInfo){
						flightAirportStore.airports.push(airportInfo);
					});
			  });
		},
		
		requestData: function(queryObject){
			var flightAirportStore = this;
			
			var params = {};
			
			var results = dojo.xhr("GET", {
                url: flightAirportStore.targetURL,
                handleAs: "json",
                sync: true
            });
			
			return results;
			
		}
		
	});

	return tui.flights.store.FlightAirportStore;
});