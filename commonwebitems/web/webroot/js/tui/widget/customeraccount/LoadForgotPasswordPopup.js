define ("tui/widget/customeraccount/LoadForgotPasswordPopup", [
        													"dojo",											  	
        													"dojo/cookie",
        													"dojo/query",
        													"dojo/has",
        													"dojox/validate/web",
        													"tui/validate/check",
        													"dojo/_base/array",
        													"dojo/dom-style",
        													"dijit/focus",
        													"dojo/text!tui/widget/customeraccount/view/templates/ForgotPasswordPopup.html",
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
        													"dojox/validate/us",
        													"tui/widget/mixins/Templatable",
															"tui/widget/customeraccount/ErrorHandling"	
        													
        													
        							    			  ], function(dojo,  cookie, query, has, validate,check, arrayUtil, domStyle, focusUtil, ForgotPasswordPopupTmpl,xhr){

		dojo.declare("tui.widget.customeraccount.LoadForgotPasswordPopup", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable, tui.widget.customeraccount.ErrorHandling], {
		
		tmpl: ForgotPasswordPopupTmpl,	
        tuiWebrootPath:tuiWebrootPath,
        uid:null,	
		successUrl : null,
		componentUid:null,
		onAfterTmplRender: function(){
			var forgotpasswordComponent = this;			 
			dojo.removeClass(forgotpasswordComponent.domNode, 'updating');			 
		},
		resetTheForm:function(){
			
			if(dojo.byId("manageAccountForgotPasswordpopupform") != undefined){
				var container = dojo.byId("manageAccountForgotPasswordpopupform");           
				dojo.query(".EditPasswordForm").attr("class", "EditPasswordForm customer-form show");
				dojo.query("#forgotpasswordSuccess").attr("class", "EditPasswordForm customer-form hide");	
				for(var i = 0; i < 2; i++){	
				dojo.query('input', container).forEach(
				  function(inputElem){  console.log(inputElem);           		   
					  var field = inputElem.id; console.log("type::"+inputElem.type);
					  if(inputElem.type != "submit" && inputElem.type != "button"){
					  focusUtil.focus(dojo.byId(field));
					  dojo.query("#"+field).attr("value","");
					  dojo.byId(field).blur();
					  }
				  }
				);
				}
		    }
		},
		showHidePopup:function(PopupId, type){
			this.resetTheForm();
			var root = document.getElementsByTagName( 'html' )[0];
			if(type == "show"){
				dojo.removeClass(PopupId, "hide");
				dojo.addClass(PopupId, "show");
				dojo.addClass(root, "modal-open");
			}
			else{
				dojo.removeClass(PopupId, "show");
				dojo.addClass(PopupId, "hide");
				dojo.removeClass(root, "modal-open");
			}
		},
		postCreate: function() {
			var forgotpasswordComponent = this;	
			
			forgotpasswordComponent.loadForgotPasswordForm();			
			forgotpasswordComponent.attachEventListener();			
			forgotpasswordComponent.inherited(arguments);
			forgotpasswordComponent.switchToResendActivation();	
		},
		loadForgotPasswordForm:function(){
			var forgotpasswordComponent = this;	
			
			dojo.query(".ForgotPasswordLinkFromOverlay").onclick(function(){
				var successUrl = this.getAttribute("url");
				if(dojo.byId("manageAccountForgotPasswordpopupform") != undefined){
					dojo.byId("manageAccountForgotPasswordpopupform").setAttribute("successUrl", successUrl);
				}				
				forgotpasswordComponent.showHidePopup("wouldyouliketoGoForgotPassword", "show");
			});
			var cookie = dojo.cookie('customerCookieForLoginPage');
			var keepMecookieChecked = "null";
			if(cookie != undefined || cookie != null || cookie != ""){
				keepMecookieChecked = "checked";
			}
			var html = forgotpasswordComponent.renderTmpl(ForgotPasswordPopupTmpl,{tuiWebrootPath:tuiWebrootPath,keepMecookie: keepMecookieChecked,uid: forgotpasswordComponent.uid,componentUid: forgotpasswordComponent.componentUid});           

            if (html) { 
                dojo.place(html, forgotpasswordComponent.domNode, "only");
                dojo.parser.parse(forgotpasswordComponent.domNode);
                
            }
			if(dojo.byId("manageAccountForgotPasswordpopupform") != undefined){
				forgotpasswordComponent.successUrl = dojo.byId("manageAccountForgotPasswordpopupform").getAttribute("successUrl");				
			}
		},
		
		resendActivationBySystem:function(){
			var forgotpasswordComponent = this;
			
			dojo.query(".ResendActivationLinkFromOverlayBySystem").onclick(function(){				
				//submit the resend form by system				
				forgotpasswordComponent.submittheFormResendActivationBySystem();
				
			});
		},
		
		submittheFormResendActivationBySystem: function() {
			var forgotpasswordComponent = this;
			var form = dojo.byId("manageAccountResendActivationpopupform");
			
			//prepopulate the email id from forgot to resend activation email form
			
			
			if(dojo.byId("forgotpopemail") != undefined){
				if(dojo.byId("resendpopemail") != undefined){
					dojo.byId("resendpopemail").value = dojo.byId("forgotpopemail").value;
				}
			}
			
			xhr.post({
				
				url: form.action+"?cache="+Math.random(),				
				form: dojo.byId(form.id),				
				load: function(response) {				    
					var obj = JSON.parse(response);										
					if(dojo.byId("manageAccountResendActivationpopupform") != undefined){
						form.successUrl = dojo.byId("manageAccountResendActivationpopupform").getAttribute("successUrl");
					}
					if(obj != undefined && obj.email){
						dojo.query(".resendEmailSuccessId").attr("innerHTML",obj.email);						
						
						forgotpasswordComponent.showHidePopup("wouldyouliketoGoForgotPassword", "hide");
						forgotpasswordComponent.showHidePopup("wouldyouliketoGoResendActivation", "show");
						dojo.query(".EditPasswordForm").attr("class","EditPasswordForm customer-form hide");
						dojo.query("#resendActivationSuccess").attr("class","EditPasswordForm customer-form show");
						return false;
					}
				},			
				error: function(errors) { 
					thisForm.handleBackendError(errors);
					dojo.addClass("resendActivation_btn","loading");
				},
				handle: function() {
					hasBeenSent = true;
				}
			});
			
		},
		switchToResendActivation: function() {
			var forgotpasswordComponent = this;	
			
			dojo.query(".ResendActivationLinkFromOverlay").onclick(function(){				
				
				forgotpasswordComponent.showHidePopup("wouldyouliketoGoForgotPassword", "hide");
				forgotpasswordComponent.showHidePopup("wouldyouliketoGoResendActivation", "show");
				
			});
		},
		validateEmailLength: function(field) { 
            var val = field;             
           if(val.length > 64){         	   
        	   return false;
           }
           return true;
        },
		submittheForm: function(form) { 
						
			 var thisForm = this;			 
			 dojo.addClass(thisForm.domNode, 'updating');	
			 
			xhr.post({
				
				url: form.action+"?cache="+Math.random(),				
				form: dojo.byId(form.id),
				// The success handler
				load: function(response) {
				   
					var obj = JSON.parse(response);
										
					if(dojo.byId("manageAccountForgotPasswordpopupform") != undefined){
						form.successUrl = dojo.byId("manageAccountForgotPasswordpopupform").getAttribute("successUrl");
					}
					if(obj != undefined && obj.email){
						dojo.query(".forgotEmailSuccessId").attr("innerHTML",obj.email);						
						dojo.query(".EditPasswordForm").attr("class", "EditPasswordForm customer-form hide");
						dojo.query("#forgotpasswordSuccess").attr("class", "EditPasswordForm customer-form show");
						return false;
					}
					
					if(obj[0] != undefined){	
						if(obj[0].code == "EMAIL"){
							dojo.byId("forgotpopemailError_1").innerHTML = obj[0].description;
							dojo.byId("forgotpopemailError_1").style.display="inline-block";
							dojo.byId("forgotpopemailError_2").style.display="inline-block";
							dojo.byId("forgotpopemailError_3").style.display="none";
							var row = dojo.byId("forgotemailFieldDiv");
							dojo.addClass(row, "error");							
						}
						
						if(obj[0].code == "SUSPENDED" || obj[0].code == "CLOSED"){
							if(tuiSiteName == "firstchoice"){
								dojo.byId("forgotpopemailError_1").innerHTML = "There is a problem with your account. See our <a href='http://www."+tuiSiteName+".co.uk/contact-us/' class='small-text' target='_blank'>contact us</a> page to get in touch";
							}
							else{
								dojo.byId("forgotpopemailError_1").innerHTML = "There is a problem with your account. See our <a href='http://www."+tuiSiteName+".co.uk/editorial/legal/contact-us.html' class='small-text' target='_blank'>contact us</a> page to get in touch";
							}
							
							dojo.byId("forgotpopemailError_1").style.display="inline-block";
							dojo.byId("forgotpopemailError_1").style.display="inline-block";
							dojo.byId("forgotpopemailError_3").style.display="none";
							var row = dojo.byId("forgotemailFieldDiv");
							dojo.addClass(row, "error");
						}
						if(obj[0].code == "REGISTERED" || obj[0].code == "INACTIVE"){
							
							resendEmailLink = "Your account hasn't been activated yet <a href='javascript:void(0)'  class='ResendActivationLinkFromOverlayBySystem'>Resend activation email</a>";						
							
							dojo.byId("forgotpopemailError_1").innerHTML = resendEmailLink;
							dojo.byId("forgotpopemailError_1").style.display="inline-block";
							dojo.byId("forgotpopemailError_1").style.display="inline-block";
							dojo.byId("forgotpopemailError_3").style.display="none";
							var row = dojo.byId("forgotemailFieldDiv");
							dojo.addClass(row, "error");							
							thisForm.resendActivationBySystem();
							
						}
					}
				},
				// The error handler
				error: function(errors) { 
					thisForm.handleBackendError(errors);
					dojo.addClass("forgotPassword_btn","loading");
				},
				// The complete handler
				handle: function() {
					hasBeenSent = true;
				}
			});
			
			 var thisForm = this;		 
			 dojo.removeClass(thisForm.domNode, 'updating');
			 dojo.removeClass("forgotPassword_btn","loading");	
		},
		attachEventListener: function(){
			var forgotpasswordComponent = this;
            var createForgotPassword = dojo.byId("manageAccountForgotPasswordpopupform");
            dojo.connect("wouldyouliketoGoForgotPasswordClose", "onclick", function(){ console.log("came in");					
				forgotpasswordComponent.showHidePopup("wouldyouliketoGoForgotPassword", "hide");
			});
			
			var myButton = dojo.byId("wouldyouliketoGoForgotPasswordClose");				
			 
			dojo.connect(myButton, "onclick", function(evt){
				forgotpasswordComponent.showHidePopup("wouldyouliketoGoForgotPassword", "hide");	
			});


            dojo.connect(createForgotPassword, "onsubmit", function(event){
			
				//add loader
				dojo.addClass("forgotPassword_btn","loading");
                var form = this;
                dojo.stopEvent(event);               
                var accountFileds = {
	             		   trim: ["email"],
	             		   required: ["email"],
	             		   constraints: {
	             			   email: [validate.isEmailAddress, false, true]
	             		   }
	             } ;				
                var results = validate.check(form, accountFileds); 
            	if(!results.isSuccessful()){     
					console.log("not success");        		
            		var container = form;            		
            		dojo.query('input', container).forEach(
            		  function(inputElem){            		   
            			  var field = inputElem.id;
                  		  focusUtil.focus(dojo.byId(field));
                  		  dojo.byId(field).blur();
            		  }
            		);
            		dojo.stopEvent(event);
					dojo.removeClass("forgotPassword_btn","loading");
            		return false;
            	}
            	else{
					if(forgotpasswordComponent.customValidator()){
            		console.log("success");
					forgotpasswordComponent.submittheForm(createForgotPassword);						
					}
            	}               
                
              });
    	},
		startup: function() {		
		},
		customValidator:function(){
			var error = 0;
			if(dojo.byId("forgotpopemailError_1").style.display != "none"){
				error = 1;
			}						
			if(error == 1){
			return false;
			}
			return true;			
		}
	});
	
	return tui.widget.customeraccount.LoadForgotPasswordPopup;
});