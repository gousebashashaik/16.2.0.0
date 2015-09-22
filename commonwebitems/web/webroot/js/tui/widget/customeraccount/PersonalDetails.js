define ("tui/widget/customeraccount/PersonalDetails", [
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

		dojo.declare("tui.widget.customeraccount.PersonalDetails", [tui.widget._TuiBaseWidget, tui.widget.customeraccount.ErrorHandling], {

		
				
		postCreate: function() {
			var PersonalDetails = this;			
			PersonalDetails.inherited(arguments);
			PersonalDetails.attachEventListener();
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
		validatePasswordLength: function(field) { 
            var val = field;     
           if(val.length < 6){         	   
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
		validateAddress: function(field) {         	
            var val = field;
           
           if(val && validate.isText(val)){ 
              onlyLetters = /^[a-z0-9A-Z\s #\/.,;:-]+$/.test(val);
			  return onlyLetters;
           }
           return false;
        },
		validatePostcode: function(field) {         	
           var val = field;            
           if(val && validate.isText(val)){ 
              onlyLetters = /^([A-Pa-pR-UWYZr-uwyz0-9][A-Ha-hK-Yk-y0-9][AEHMNPRTUVXYaehmnprtuvxy0-9]?[ABEHMNPRVWXYabehmnprvwxy0-9]?[ \\s]{0,1}[0-9][ABD-HJLN-UW-Zabd-hjln-uw-z]{2}|GIR 0AA)$/.test(val);
			  return onlyLetters;
           }
           return false;
        },
        validateTelePhone: function(field) {         	
           var val = field;            
           if(val && validate.isText(val)){ 
              onlyLetters = /^[(+?0-9][0-9-?]+[0-9)]$/.test(val);		
			  return onlyLetters;
           }
           return false;
        },
		validateCountyState: function(field) {         	
           var val = field;            
           if(val && validate.isText(val)){ 
              onlyLetters = /^[a-zA-Z\s #\/.,;:-]+$/.test(val);		
			  return onlyLetters;
           }
           return false;
        },
        
    	attachEventListener: function(){
            var PersonalDetails = this;
            
           
            dojo.connect(PersonalDetails.domNode, "onsubmit", function(event){ 
                var form = this;
                dojo.stopEvent(event);
				
				var res = PersonalDetails.handleSessionTimeOut();
				if(!res){
				return false;
				}
				
                var spam = dojo.byId("spambots").value
                if(spam != ""){
                	return false;
                }
                //on multiple account case
               
                if(dojo.byId("buildingName").value != ''){
                	var accountFileds = {
                    		
 	             		   trim: ["email","firstName", "lastName","buildingName","postcode"],
 	             		   required: ["email","firstName", "lastName","buildingName","postcode"],
 	             		   constraints: {
 								firstName: [PersonalDetails.validateText, false, true],
 								lastName: [PersonalDetails.validateText, false, true],
 								//fix for DE14449
 								email: [validate.isEmailAddress, false, true],
 								buildingName: [PersonalDetails.validateAddress, false, true],
 								postCode: [PersonalDetails.validatePostcode, false, true],
 								telePhone: [PersonalDetails.validateTelePhone, false, true],
 								countyState: [PersonalDetails.validateCountyState, false, true]	
 	             			  
 	             		   }
 	             } ;
                }  else {
                	var accountFileds = {
                    		
  	             		   trim: ["email","firstName", "lastName"],
  	             		   required: ["email","firstName", "lastName"],
  	             		   constraints: {
  								firstName: [PersonalDetails.validateText, false, true],
  								lastName: [PersonalDetails.validateText, false, true],
  								//fix for DE14449
  								email: [validate.isEmailAddress, false, true],
  								buildingName: [PersonalDetails.validateAddress, false, true],
  								postCode: [PersonalDetails.validatePostcode, false, true],
  								telePhone: [PersonalDetails.validateTelePhone, false, true],
  								countyState: [PersonalDetails.validateCountyState, false, true]	
  	             			  
  	             		   }
  	             } ;
                }
                
               
               
                
                var results = validate.check(form, accountFileds);         
            	
            	
            	if(!results.isSuccessful()){            		
            		var container = form;
            		if(dojo.byId("buildingName").value != ''){
            			var notRequiredFields = ["street","subStreet","city","countyState","telephone","mobile"];
            		} else {
            			var notRequiredFields = ["telephone","mobile","buildingName","street","subStreet","city","countyState","postcode","telephone","mobile"];
            		}
            		
            		dojo.query('input', container).forEach(
            		  function(inputElem){            		   
            			  var field = inputElem.id;
            			  
            			  var present = notRequiredFields.indexOf(field); 
            			  if(present == -1){            			  
            				  focusUtil.focus(dojo.byId(field));
            				//  dojo.byId(field).blur();
            			  }
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
	
	return tui.widget.customeraccount.PersonalDetails;
});