define ("tui/widget/customeraccount/resendActivation", [
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

		dojo.declare("tui.widget.customeraccount.resendActivation", [tui.widget._TuiBaseWidget], {

		
				
		postCreate: function() {
			var resendActivation = this;			
			resendActivation.inherited(arguments);
			resendActivation.attachEventListener();
		},
    	
    	validate: function() {
          
        },
        
        onFocus: function() {
           
        },

        onBlur: function() {
           
        },
        validateEmailLength: function(field) { 
            var val = field;             
           if(val.length > 64){         	   
        	   return false;
           }
           return true;
        },
        validateText: function(field) {        	
            var val = field;
            
           if(val && validate.isText(val)){ 
              onlyLetters = /^[A-Za-z\s-_]+$/.test(val);
       	   return onlyLetters;
           }
           return false;
        },
    	attachEventListener: function(){
            var resendActivation = this;
            
            
            dojo.connect(resendActivation.domNode, "onsubmit", function(event){
                var form = this;
                dojo.stopEvent(event);
                var spam = dojo.byId("spambots").value;
                if(spam != ""){
                	return false;
                }
                
                var accountFileds = {
				   trim: ["email"],
				   required: ["email"],
				   constraints: {
					  email: [validate.isEmailAddress, false, true]
				   }
  	            };
                
                var results = validate.check(form, accountFileds);         
            	
            	
            	if(!results.isSuccessful()){
            		
            		var container = form;
            		
            		dojo.query('input', container).forEach(
            		  function(inputElem){            		   
            			  var field = inputElem.id;
                  		  focusUtil.focus(dojo.byId(field));
                  		  dojo.byId(field).blur();
            		  }
            		)
            		dojo.stopEvent(event);
            		return false;
            	}
            	else{
            		form.submit();            		
            	}
                
                
              })
    	}
	});
	
	return tui.widget.customeraccount.resendActivation;
});