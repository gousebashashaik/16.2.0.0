define ("tui/widget/customeraccount/mySiteIntegrationTeaser", [
													"dojo",											  	
													"dojo/cookie",
													"dojo/query",
													"dojo/has",
													"dojox/validate/web",
													"tui/validate/check",
													"dojo/_base/array",
													"dojo/dom-style",
													"dijit/focus",
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
													
													
							    			  ], function(dojo,  cookie, query, has, validate,check, arrayUtil, domStyle, focusUtil){

		dojo.declare("tui.widget.customeraccount.mySiteIntegrationTeaser", [tui.widget._TuiBaseWidget], {
	
				
		postCreate: function() {
			var mySiteIntegrationTeaser = this;			
			mySiteIntegrationTeaser.inherited(arguments);
			mySiteIntegrationTeaser.handleComponentDisplay();
		},
    	
    	
        
        
    	handleComponentDisplay: function(){
            var teaser = this;
			if(dojo.query("#caBooking")[0] != undefined){
				if(dojo.query("#account-portal .img-panel.booked-holiday .button")[0] != undefined){
					dojo.query("#caBooking").attr("class", "another-booked show");
					dojo.query("#caNoBooking").attr("class", "another-booked hide");
				}
				else{
					dojo.query("#caBooking").attr("class", "another-booked hide");
					dojo.query("#caNoBooking").attr("class", "another-booked show");
				}
			}
    	}
	});
	
	return tui.widget.customeraccount.mySiteIntegrationTeaser;
});