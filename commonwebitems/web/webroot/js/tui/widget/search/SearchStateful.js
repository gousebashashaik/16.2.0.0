define ("tui/widget/search/SearchStateful", ["dojo", "dojo/Stateful"], function(dojo){
	
	dojo.declare("tui.widget.search.SearchStateful", null , {
		
		stateful: null,
		 
		initSearchState: function(){ 
			var searchStateful = this;  
		  	searchStateful.stateful = new dojo.Stateful({
				destinationAc: null,
				airportAc: null,
				searchDatePicker: null,
				childenDropdown: null
			});
		},
			
		setDestinationAc: function(autocomplete){
    		var searchStateful = this;
    		searchStateful.stateful.set(tui.widget.search.SearchStateful.DESTINATION_AUTOCOMPLETE, autocomplete);
    	},
    	
    	getDestinationAc: function(){
    		var searchStateful = this;
    		return searchStateful.stateful.get(tui.widget.search.SearchStateful.DESTINATION_AUTOCOMPLETE);
    	},
    	
    	setAirportAc: function(autocomplete){
    		var searchStateful = this;
    		searchStateful.stateful.set(tui.widget.search.SearchStateful.AIRPORT_AUTOCOMPLETE, autocomplete);
    	},
    	
    	getAirportAc: function(){
    		var searchStateful = this;
    		return searchStateful.stateful.get(tui.widget.search.SearchStateful.AIRPORT_AUTOCOMPLETE);
    	},
    	
    	setSearchDatepicker: function(datepicker){
    		var searchStateful = this;
    		searchStateful.stateful.set(tui.widget.search.SearchStateful.SEARCH_DATEPICKER, datepicker);
    	},
    	
    	getSearchDatepicker: function(){
    		var searchStateful = this;
    		return searchStateful.stateful.get(tui.widget.search.SearchStateful.SEARCH_DATEPICKER);
    	},
    	
    	setChildDropdown: function(childdropdown){
    		var searchStateful = this;
    		searchStateful.stateful.set(tui.widget.search.SearchStateful.CHILD_DROPDOWN, childdropdown);
    	},
    	
    	getChildDropdown: function(){
    		var searchStateful = this;
    		return searchStateful.stateful.get(tui.widget.search.SearchStateful.CHILD_DROPDOWN);
    	},
    	
    	addWatcher: function(propname, watcherFn){
			var searchStateful = this;
			searchStateful.stateful.watch(propname, watcherFn);
		}
	})
	
	tui.widget.search.SearchStateful.DESTINATION_AUTOCOMPLETE = "destinationAc";
	tui.widget.search.SearchStateful.AIRPORT_AUTOCOMPLETE = "airportAc";
	tui.widget.search.SearchStateful.SEARCH_DATEPICKER = "searchDatePicker";
	tui.widget.search.SearchStateful.CHILD_DROPDOWN = "childdropdown";
	
	return tui.widget.search.SearchStateful;
})