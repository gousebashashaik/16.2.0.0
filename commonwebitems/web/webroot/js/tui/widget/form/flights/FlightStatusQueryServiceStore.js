define([
		"dojo/_base/declare",
		"dojox/data/QueryReadStore",
		"dijit/registry",
		"dojo/dom"
        ],function(declare,QueryReadStore,registry,dom){

		declare("tui.widget.form.flights.FlightStatusQueryServiceStore", QueryReadStore, {
		  fetch:function(request){
			var QueryServiceStore = this;
			  	request.serverQuery=QueryServiceStore.createQueryObject(request);
		    // Call superclasses' fetch
		    return this.inherited(arguments);
		  },
		  createQueryObject: function(request){
			  var param;
			  if(this.id == "flyTo"){
				  param = request.query.arrAirPortInfo.replace(/[*]/gi, '');
			  }else if(this.id == "flyFrom"){
				  param= request.query.depAirPortInfo.replace(/[*]/gi, '');
			  }

				return  query = {
						from: registry.byId("flyFromStat").item == null ? '' : registry.byId("flyFromStat").item.i.depAirpotCode,
						to: registry.byId("flyToStat").item == null ? '' : registry.byId("flyToStat").item.i.arrAirpotCode,
						flightNumber:dom.byId("ftselectedMonth") == null ? '' : dom.byId("ftselectedMonth").value,
						airport: param
				};
			}
		})
		return tui.widget.form.flights.FlightStatusQueryServiceStore;

})