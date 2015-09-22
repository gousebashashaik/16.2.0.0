define("tui/flights/store/FlightAirportToStore", [
	"dojo",
	"dojo/store/Memory"], function (dojo, memory) {
	dojo.declare("tui.flights.store.FlightAirportToStore", [dojo.store.Memory], {
		
		airportStore: null,
		
		airportCodes: [],
		
		airports: [],
		
		targetURL: "",
		
		constructor: function(params){
			var flightAirportToStore = this;
			
			flightAirportToStore.targetURL = params.targetURL;
			
			var results = flightAirportToStore.requestData();
			
			results.then(function(airportData){
				flightAirportToStore.airportStore = null;
				flightAirportToStore.airportStore = airportData;
			});
			
			flightAirportToStore.renderAirportCodes();
		},
		
		/*fetch airport codes*/
		renderAirportCodes: function(){
			var flightAirportToStore = this;
			flightAirportToStore.airportCodes = _.keys(flightAirportToStore.airportStore);
			flightAirportToStore.fetchAirportNames();
		},
		
		/*fetch airport names*/
		fetchAirportNames: function(){
			var flightAirportToStore = this;
			_.each(flightAirportToStore.airportCodes, function(code){
					_.each(flightAirportToStore.airportStore[code], function(airportInfo){
						flightAirportToStore.airports.push(airportInfo);
					});
			  });
		},
		
		requestData: function(queryObject){
			var flightAirportToStore = this;
			
			var params = {};
			
			var results = dojo.xhr("GET", {
                url: flightAirportToStore.targetURL,
                handleAs: "json",
                sync:true
            });
			
			return results;
			
		}
		
	});

	return tui.flights.store.FlightAirportToStore;
});