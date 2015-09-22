define ("tui/widget/customeraccount/HandleLocalStorage", [
													"dojo",											  	
													"dojo/cookie",
													"dojo/query",
													"dojo/has",
													"dojox/validate/web",
													"tui/validate/check",
													"dojo/_base/array",
													"dojo/dom-style",
													"dijit/focus",
													'dojo/_base/unload',
													"dojo/topic",                                               
													"tui/widget/_TuiBaseWidget", 
													"dojo/NodeList-traverse",
													
													"dojox/dtl", 
													"dojox/dtl/Context", 
													"dojox/dtl/tag/logic",
													"dijit/registry",
													"tui/dtl/Tmpl",
													"dojo/html",
													"dojox/validate/us"
													
													
							    			  ], function(dojo,  cookie, query, has, validate,check, arrayUtil, domStyle, focusUtil, baseUnload){

		dojo.declare("tui.widget.customeraccount.HandleLocalStorage", [tui.widget._TuiBaseWidget], {
	
				
		postCreate: function() {
			var HandleLocalStorage = this;			
			HandleLocalStorage.inherited(arguments);
			//HandleLocalStorage.deleteLocalStorage();			
		},    	
    	
    	deleteLocalStorage: function(){
			var HandleLocalStorage = this;
			baseUnload.addOnUnload(window,function(){
				var storageArray = [
					"thomsoncompareItemsListAll", 
					"thomsoncompareItemsList", 
					"thomson_sessionTimedOutEmail",
					"thomsoncompareItemsListAll", 
					"thomsoncompareItemsList", 
					"thomson_sessionTimedOutEmail",
					"sessionTimedOutEmail"
				];
				
				if(dojo.isArray(storageArray)){
				  var len = storageArray.length;
				  for( var i = 0; i <= len; i++ ){
					localStorage.setItem(storageArray[i], "");
				  }
				}
			});
			
    	}
	});
	
	return tui.widget.customeraccount.HandleLocalStorage;
});