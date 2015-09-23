define([
		"dojo/_base/declare",
		"dojox/data/QueryReadStore",
		"dijit/registry",
		"dojo/dom"
        ],function(declare,QueryReadStore,registry,dom){

		declare("tui.widget.form.flights.QueryServiceStore", QueryReadStore, {
		  fetch:function(request){
			var QueryServiceStore = this;
			  	request.serverQuery=QueryServiceStore.createQueryObject(request);
		    // Call superclasses' fetch
		    return this.inherited(arguments);
		  },
		  createQueryObject: function(request){
				return  query = {
						from: registry.byId("timeTableFlyFrom").getValue(),
						to: registry.byId("timeTableFlyTo").getValue(),
						when:dom.byId("ftselectedMonth") == null ? '' : dom.byId("ftselectedMonth").value,
						q: request.query.name
				};
			}
		})
		return tui.widget.form.flights.QueryServiceStore;

})