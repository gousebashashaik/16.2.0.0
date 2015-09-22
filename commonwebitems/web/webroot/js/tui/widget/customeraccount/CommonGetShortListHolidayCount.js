define("tui/widget/customeraccount/CommonGetShortListHolidayCount", 
		["dojo",
		 "dojo/on",
		 "dojo/cookie",
		 "dojo/query",
		 "dojo/has",
		 "dojox/validate/web",
		 "tui/validate/check",
		 "dojo/_base/array",
		 "dojo/dom-style",
		 "dijit/focus",
		 "dojo/dom-construct",
		 "dojo/_base/xhr",
		 "dojo/topic",                                               
		 "tui/widget/_TuiBaseWidget", 
		 "dojo/NodeList-traverse",
		
		 "dojox/dtl", 
		 "dojox/dtl/Context", 
		 "dojox/dtl/tag/logic",
		 "dijit/registry",
		 "tui/dtl/Tmpl",
		 "dojo/html",													
		 "dojox/validate/us"],
		function(dojo, on, cookie, query, has, validate,check, arrayUtil, domStyle, focusUtil, domConstruct,xhr){
        
		dojo.declare("tui.widget.customeraccount.CommonGetShortListHolidayCount", [tui.widget._TuiBaseWidget], {
			/* Component custom attributes */
			postCreate: function() {
				
			},
			getShorListedHolidaysCount: function() {
								
				xhr.get({				
					url: tuiWebrootPath+"/viewAnonymousShortlist/retrieveWishlistCount",	
					preventCache: true,
					// The success handler
					load: function(response) {				
						if(response){
                            var message = "You've got "+response+" shortlisted holidays";
							if(dojo.byId("wishCountSpan") != undefined){
							dojo.byId("wishCountSpan").innerHTML = message;
							}							
							
							dojo.query(".wishCount").innerHTML=""+ response +"";
							
							if(dojo.byId("wishCount") != undefined){
								dojo.byId("wishCount").innerHTML=""+ response +"";
							}
							if(dojo.byId("wishCountOne") != undefined){
								dojo.byId("wishCountOne").innerHTML=""+ response +"";
							}
							if(dojo.byId("wishCountTwo") != undefined){
								dojo.byId("wishCountTwo").innerHTML=""+ response +"";
							}
							if(dojo.byId("wishCountThree") != undefined){
								dojo.byId("wishCountThree").innerHTML=response;
							}
							if(dojo.byId("wishCountFour") != undefined){
								dojo.byId("wishCountFour").innerHTML=response;
							}
							if(dojo.byId("wishCountFive") != undefined){
								dojo.byId("wishCountFive").innerHTML=response;
							}
							if(dojo.byId("wishCountSix") != undefined){
								dojo.byId("wishCountSix").innerHTML=response;
							}
						}
					},
					// The error handler
					error: function(errors) { 				
						
					},
					// The complete handler
					handle: function() {
						hasBeenSent = true;
					}
				});
			}
			
			
		});
		return tui.widget.customeraccount.CommonGetShortListHolidayCount;
});