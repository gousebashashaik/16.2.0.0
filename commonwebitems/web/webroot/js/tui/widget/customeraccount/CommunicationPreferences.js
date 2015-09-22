define ("tui/widget/customeraccount/CommunicationPreferences", [
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

		dojo.declare("tui.widget.customeraccount.CommunicationPreferences", [tui.widget._TuiBaseWidget], {

		
				
		postCreate: function() {
			var CommunicationPreferences = this;			
			CommunicationPreferences.inherited(arguments);
			CommunicationPreferences.attachEventListener();
		},
    	
    	validate: function() {
          
        },
        
        onFocus: function() {
           
        },

        onBlur: function() {
           
        },
            	
    	attachEventListener: function(){
            var CommPrefCheckbox = this;            
            
            dojo.connect(CommPrefCheckbox.domNode, "onclick", function(event){                
                dojo.stopEvent(event);			
                var checked = dojo.hasClass(CommPrefCheckbox.domNode, "active");	
               
                if(checked){
                	CommPrefCheckbox.checked = false;                				
                	CommPrefCheckbox.domNode.className = "radio inactive loaded";
					dojo.byId(CommPrefCheckbox.id).value="false";
					//alert(tailorCheckbox.id);
					
					if(CommPrefCheckbox.id == "byEmailBooking"){
						dojo.removeClass(dojo.query(".booking-information")[0], "hide");
						dojo.addClass(dojo.query(".booking-information")[0], "show");						
					} 
				}
				else{
					CommPrefCheckbox.checked = true;					
					CommPrefCheckbox.domNode.className = "radio active loaded";
					dojo.byId(CommPrefCheckbox.id).value="true";
					//alert(tailorCheckbox.id);
					
					if(CommPrefCheckbox.id == "byEmailBooking"){
						dojo.removeClass(dojo.query(".booking-information")[0], "show");
						dojo.addClass(dojo.query(".booking-information")[0], "hide");						
					}
				}
              });
            
    	}
	});
	
	return tui.widget.customeraccount.CommPrefCheckbox;	
});