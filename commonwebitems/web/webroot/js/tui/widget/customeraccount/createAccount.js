define ("tui/widget/customeraccount/createAccount", [
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

		dojo.declare("tui.widget.customeraccount.createAccount", [tui.widget._TuiBaseWidget], {

		
				
		postCreate: function() {
			var createAccount = this;			
			createAccount.inherited(arguments);
			createAccount.attachEventListener();
		},
    	
    	validate: function() {
          
        },
        
        onFocus: function() {
           
        },

        onBlur: function() {
           
        },
        
        validateText: function(field) { 
        	 var textNode = this;
			 
             var val = field;
             
            if(val && validate.isText(val)){ 
               onlyLetters = /^[A-Za-z\s-_]+$/.test(val); 
			   //onlyLetters = /^[a-zA-Z]+([\s-]?[a-zA-Z]+)$/.test(val);
        	   return onlyLetters;
            }
            return false;
         },
         
         validateEmailLength: function(field) { 
             var val = field;             
            if(val.length > 64){         	   
         	   return false;
            }
            return true;
         },
		 validatePasswordLength: function(field) { 
            var val = field;             
           if(val.length < 6){         	   
        	   return false;
           }
           return true;
        },
    	attachEventListener: function(){
            var createAccount = this;
            
            
            dojo.connect(createAccount.domNode, "onsubmit", function(event){
                var form = this;
                dojo.stopEvent(event);
                
                var spam = dojo.byId("spambots").value;
                if(spam != ""){
                	return false;
                }
                var accountFileds = {
	             		   trim: ["firstName", "lastName", "email", "reEnterEmail", "pwd", "checkPwd"],
	             		   required: ["firstName", "lastName", "email", "reEnterEmail", "pwd", "checkPwd"],
	             		   constraints: {
	             			   firstName: [createAccount.validateText, false, true],
	             			   lastName: [createAccount.validateText, false, true],	             			  
	             			   email: [createAccount.validateEmailLength, validate.isEmailAddress, false, true],
	             			   reEnterEmail: validate.isText,
	             			   pwd: [createAccount.validatePasswordLength,false, true],
	             			   checkPwd: [createAccount.validatePasswordLength,false, true],             			   
	             			   reEnterEmail: [validate.isEmailAddress, false, true]	             			  
	             		   },
	             		   confirm: {
	             			  reEnterEmail: "email",
	             			  checkPwd: "pwd"
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
	
	return tui.widget.customeraccount.createAccount;
});