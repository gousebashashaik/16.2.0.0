define ("tui/widget/customeraccount/EditPassword", [
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
													"dojox/validate/us",
													"tui/widget/customeraccount/ErrorHandling"
													
													
							    			  ], function(dojo,  cookie, query, has, validate,check, arrayUtil, domStyle, focusUtil){

		dojo.declare("tui.widget.customeraccount.EditPassword", [tui.widget._TuiBaseWidget, tui.widget.customeraccount.ErrorHandling], {

		
				
		postCreate: function() {
			var EditPassword = this;			
			EditPassword.inherited(arguments);
			EditPassword.attachEventListener();
		},
    	
    	validate: function() {
          
        },
        
        onFocus: function() {
           
        },

        onBlur: function() {
           
        },
        
		validatePasswordLength: function(field) { 
            var val = field;     
           if(val.length < 6){         	   
        	   return false;
           }
           return true;
        },

        
    	attachEventListener: function(){
            var EditPassword = this;
			
			 var cancel=dojo.query('.cancelit')[0];
            dojo.connect(cancel,"mousedown,keydown",function(e){
            	dojo.stopEvent(e);
            	e.cancelBubble = true;
            	e.stopPropagation();
            	return false;
            });
			
            dojo.connect(EditPassword.domNode, "onsubmit", function(event){
                var form = this;
                dojo.stopEvent(event);
               
				var res = EditPassword.handleSessionTimeOut();
				if(!res){
				return false;
				}
                //on multiple account case
               
				var accountFileds = {
						   trim: ["currentPwd", "newPwd","reenternewPwd"],
						   required: ["currentPwd", "newPwd","reenternewPwd"],
						   constraints: {
							   
							   currentPwd: [EditPassword.validatePasswordLength,false, true],      			  
							   newPwd: [EditPassword.validatePasswordLength,false, true],
							   reenternewPwd: [EditPassword.validatePasswordLength,false, true]
						   },
						   confirm: {
							   newPwd: "reenternewPwd"
						   }
						   
				 } ;
                
                
                var results = validate.check(form, accountFileds);         
            	
            	
            	if(!results.isSuccessful()){
            		
            		var container = form;
            		
            		dojo.query('input', container).forEach(
            		  function(inputElem){            		   
            			  var field = inputElem.id;
                  		  focusUtil.focus(dojo.byId(field));
                  		  dojo.byId(field).blur();
            		  }
            		);
            		dojo.stopEvent(event);
            		return false;
            	}
            	else{
            		form.submit();            		
            	}
                
                
              });
    	}
	});
	
	return tui.widget.customeraccount.EditPassword;
});