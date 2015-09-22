define ("tui/widget/customeraccount/popup/AccountLogin", [
													"dojo",											  	
													"dojo/cookie",
													"dojo/query",
													"dojo/has",
													"dojox/validate/web",
													"tui/validate/check",
													"dojo/_base/array",
													"dojo/dom-style",
													"dijit/focus",
													"dojo/_base/xhr",
													"dojo/_base/json",
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
													
													
							    			  ], function(dojo,  cookie, query, has, validate,check, arrayUtil, domStyle, focusUtil, xhr){

		dojo.declare("tui.widget.customeraccount.popup.AccountLogin", [tui.widget._TuiBaseWidget], {

		successUrl : null,
				
		postCreate: function() {
			var AccountLogin = this;
			//AccountLogin.resetForm();
			if(dojo.byId("manageAccountsigninpopupform") != undefined){
				AccountLogin.successUrl = dojo.byId("manageAccountsigninpopupform").getAttribute("successUrl");
			}
			AccountLogin.inherited(arguments);
			AccountLogin.attachEventListener();	
			
			
		},
		startup:function(){
		    if(dojo.byId("capageloader") != undefined){
			dojo.byId("capageloader").style.display="none";
			}		
		},
    	resetForm: function(){
    		
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
		hideloader:function(){
			if(dojo.byId("capageloader") != undefined){
			dojo.byId("capageloader").style.display="none";
			}
		},
		showloader:function(){
			if(dojo.byId("capageloader") != undefined){
			dojo.byId("capageloader").style.display="block";
			}
		},
		submittheForm: function(form) { 
			
			//alert(form.action);
			//form.showloader();
			
			 var thisForm = this;
			 dojo.query(".mask-interactivity").style.diplay="block";			 
			 dojo.addClass(thisForm.domNode, 'updating');	
			 
			 
			xhr.post({
				
				url: form.action,
				
				form: dojo.byId(form.id),
				// The success handler
				load: function(response) {
				    var AccountLogin=this;
					var obj = JSON.parse(response);
					//alert(response);
					console.log("one");
					
					if(dojo.byId("manageAccountsigninpopupform") != undefined){
						form.successUrl = dojo.byId("manageAccountsigninpopupform").getAttribute("successUrl");
					}
				
					if(obj != undefined && obj.email){	
						console.log("two");
						console.log(form.successUrl); 
						console.log("three");
						var uid = dojo.byId("puid").value;
						var s_url = form.successUrl;	
						
						window.open(s_url,"_self");
						return false;
					}
					
					
					
					
					if(obj[0] != undefined){ 						
						
						if(obj[0].code == "PWD"){
							dojo.byId("poppwdError_1").innerHTML = obj[0].description;
							dojo.byId("poppwdError_1").style.display="inline-block";
							dojo.byId("poppwdError_2").style.display="inline-block";
							dojo.byId("poppwdError_3").style.display="none";
							var row = dojo.byId("passwordFieldDiv");
							dojo.addClass(row, "error");
						}
						if(obj[0].code == "EMAIL"){
							dojo.byId("popemailError_1").innerHTML = obj[0].description;
							dojo.byId("popemailError_1").style.display="inline-block";
							dojo.byId("popemailError_2").style.display="inline-block";
							dojo.byId("popemailError_3").style.display="none";
							var row = dojo.byId("emailFieldDiv");
							dojo.addClass(row, "error");
							
						}											
						
					}
					
					
					
					if(obj != undefined && obj.email){	
						
						var uid = dojo.byId("puid").value;
						var s_url = AccountLogin.successUrl;	
						
						window.open(s_url,"_self");
						return false;
					}
					//form.hideloader();
				},
				// The error handler
				error: function(errors) { 
					
					
				},
				// The complete handler
				handle: function() {
					hasBeenSent = true;
				}
			});
			
			 var thisForm = this;
			 dojo.query(".mask-interactivity").style.diplay="none";			 
			 dojo.removeClass(thisForm.domNode, 'updating');

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
            var createLogin = this;            
            dojo.connect("wouldyouliketoGoClose", "onclick", function(){ console.log("came in");
				dojo.removeClass("wouldyouliketoGo", "show");
				dojo.addClass("wouldyouliketoGo", "hide");	
			});
			
			var myButton = dojo.byId("wouldyouliketoGoClose");
				
			 
			dojo.connect(myButton, "onclick", function(evt){
				dojo.removeClass("wouldyouliketoGo", "show");
				dojo.addClass("wouldyouliketoGo", "hide");	
			});


            dojo.connect(createLogin.domNode, "onsubmit", function(event){
                var form = this;
                dojo.stopEvent(event);               
                var accountFileds = {
	             		   trim: ["email", "pwd"],
	             		   required: ["email","pwd"],
	             		   constraints: {
	             			   email: [createLogin.validateEmailLength, validate.isEmailAddress, false, true],      			  
	             			   pwd: [createLogin.validatePasswordLength,false, true]
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
            		//form.submit();  
					createLogin.submittheForm(createLogin);						
            	}               
                
              });
    	}
	});
	
	return tui.widget.customeraccount.popup.AccountLogin;
});