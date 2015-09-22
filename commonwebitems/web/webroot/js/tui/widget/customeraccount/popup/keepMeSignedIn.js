define ("tui/widget/customeraccount/popup/keepMeSignedIn", [
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

		dojo.declare("tui.widget.customeraccount.popup.keepMeSignedIn", [tui.widget._TuiBaseWidget], {

		
				
		postCreate: function() {
			var keepMeSignedIn = this;			
			keepMeSignedIn.inherited(arguments);
			keepMeSignedIn.attachEventListener();
		},
    	
    	validate: function() {
          
        },
        
        onFocus: function() {
           
        },

        onBlur: function() {
           
        },
            	
    	attachEventListener: function(){
            var keepMeSignedIn = this;
            
            
            dojo.connect(keepMeSignedIn.domNode, "onclick", function(event){
                
                dojo.stopEvent(event);
				
				var checked = dojo.byId("popupkeepMeSignin").checked;
			    //alert("checked:", checked);
                if(checked){
					dojo.byId("popupkeepMeSignin").checked = false;					
					keepMeSignedIn.domNode.className = "radio inactive loaded";
				}
				else{
					dojo.byId("popupkeepMeSignin").checked = true;
					keepMeSignedIn.domNode.className = "radio active loaded";
				}
              });
    	}
	});
	
	return tui.widget.customeraccount.popup.keepMeSignedIn;
});