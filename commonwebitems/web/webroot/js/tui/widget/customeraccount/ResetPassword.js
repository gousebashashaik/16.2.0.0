define ("tui/widget/customeraccount/ResetPassword", [
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

		dojo.declare("tui.widget.customeraccount.ResetPassword", [tui.widget._TuiBaseWidget], {

		
				
		postCreate: function() {
			var ResetPassword = this;			
			ResetPassword.inherited(arguments);
			ResetPassword.attachEventListener();
		},
    	
    	validate: function() {
          
        },
        
        onFocus: function() {
           
        },

        onBlur: function() {
           
        },
        
        validateText: function(field) { 
        	 
        	
             var val = field.value;
             
            if(val && validate.isText(val)){ 
         	   onlyLetters = /^[a-zA-Z]*$/.test(val);
         	   return onlyLetters;
            }
            return false;
         },
		resetTheform: function(){ 
			var cancelbtton = dojo.query(".cancel.reset")[0];
			dojo.connect(cancelbtton,"onclick",function(e){
				var container = dojo.query("#updatePasswordForm ")[0];
				dojo.query('input', container).forEach(
					function(inputElem){            		   
					var field = inputElem.id;
					if(inputElem.type != "submit"){						
						dojo.byId(field).value = "";
						focusUtil.focus(dojo.byId(field));
						dojo.byId(field).blur();
					}
					}
				);
				dojo.query("#pwd-strength").attr("class","pwd-strength");
				dojo.stopEvent(event);
				return false;
			});
		},
    	attachEventListener: function(){
            var ResetPassword = this;
            ResetPassword.resetTheform();
            var submitbtton=dojo.byId("signin_btn");
            dojo.connect(submitbtton,"mousedown,keydown",function(e){
            	dojo.stopEvent(e);
            	e.cancelBubble = true;
            	e.stopPropagation();
            	return false;
            });
			
            dojo.connect(ResetPassword.domNode, "onsubmit", function(event){
                var form = this;
                dojo.stopEvent(event);
                var spam = dojo.byId("spambots").value;
                if(spam != ""){
                	return false;
                }
                var accountFileds = {
	             		   trim: ["password", "checkPwd"],
	             		   required: ["password", "checkPwd"],
	             		   constraints: {
	             			  
	             			   password: validate.isText,
	             			   checkPwd: validate.isText
	             			   
	             		   },
	             		   confirm: {	             			 
	             			  checkPwd: "password"
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
	
	return tui.widget.customeraccount.ResetPassword;
});