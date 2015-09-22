define ("tui/widget/customeraccount/pageLoader", [
													"dojo",	
													"dojo/query"	
							    			  ], function(dojo){

		dojo.declare("tui.widget.customeraccount.pageLoader", [tui.widget._TuiBaseWidget], {
	
				
		postCreate: function() {
			if(dojo.byId("capageloader") != undefined){
				dojo.byId("capageloader").style.display="block";
			}
		},
		startup: function() {
			if(dojo.byId("capageloader") != undefined){
			dojo.byId("capageloader").style.display="none";
			}
		},      
    	
	});
	
	return tui.widget.customeraccount.pageLoader;
});