define ("tui/widget/customeraccount/LoadResendActivationPopup", [
        													"dojo",											  	
        													"dojo/cookie",
        													"dojo/query",
        													"dojo/has",
        													"dojox/validate/web",
        													"tui/validate/check",
        													"dojo/_base/array",
        													"dojo/dom-style",
        													"dijit/focus",
        													"dojo/text!tui/widget/customeraccount/view/templates/ResendActivationPopup.html",
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
        													
        													
        							    			  ], function(dojo,  cookie, query, has, validate,check, arrayUtil, domStyle, focusUtil, ResendActivationPopupTmpl,xhr){

		dojo.declare("tui.widget.customeraccount.LoadResendActivationPopup", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable, tui.widget.customeraccount.ErrorHandling], {
		
		tmpl: ResendActivationPopupTmpl,	
        tuiWebrootPath:tuiWebrootPath,
        uid:null,	
		successUrl : null,
		componentUid:null,
		onAfterTmplRender: function(){
			var resendActivationComponent = this;			 
			dojo.removeClass(resendActivationComponent.domNode, 'updating');			 
		},
		resetTheForm:function(){
			
			if(dojo.byId("manageAccountResendActivationpopupform") != undefined){
				var container = dojo.byId("manageAccountResendActivationpopupform");           
				dojo.query(".EditPasswordForm").attr("class","EditPasswordForm customer-form show");
				dojo.query("#resendActivationSuccess").attr("class","EditPasswordForm customer-form hide");	
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
			var resendActivationComponent = this;	
			
			resendActivationComponent.loadresendActivationForm();			
			resendActivationComponent.attachEventListener();			
			resendActivationComponent.inherited(arguments);
			resendActivationComponent.switchToSignIn();		
		},
		loadresendActivationForm:function(){
			var resendActivationComponent = this;	
			
			dojo.query(".ResendActivationLinkFromOverlay").onclick(function(){
				var successUrl = this.getAttribute("url");
				
				if(dojo.byId("manageAccountResendActivationpopupform") != undefined){
					dojo.byId("manageAccountResendActivationpopupform").setAttribute("successUrl", successUrl);
				}				
				resendActivationComponent.showHidePopup("wouldyouliketoGoResendActivation", "show");
			});
			var cookie = dojo.cookie('customerCookieForLoginPage');
			var keepMecookieChecked = "null";
			if(cookie != undefined || cookie != null || cookie != ""){
				keepMecookieChecked = "checked";
			}
			
			
			var html = resendActivationComponent.renderTmpl(ResendActivationPopupTmpl,{tuiWebrootPath:tuiWebrootPath,uid: resendActivationComponent.uid,componentUid: resendActivationComponent.componentUid});           

            if (html) { 
                dojo.place(html, resendActivationComponent.domNode, "last");
                dojo.parser.parse(resendActivationComponent.domNode);
                
            }
			if(dojo.byId("manageAccountResendActivationpopupform") != undefined){
				resendActivationComponent.successUrl = dojo.byId("manageAccountResendActivationpopupform").getAttribute("successUrl");				
			}
		},
		switchToSignIn: function() {
			var resendActivationComponent = this;	
			
			dojo.query(".SignInFromOverlay").onclick(function(){				
				resendActivationComponent.showHidePopup("wouldyouliketoGoResendActivation", "hide");
				resendActivationComponent.showHidePopup("wouldyouliketoGo", "show");
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
				    //thisForm.handleSessionTimeOut(response);
					var obj = JSON.parse(response);
										
					if(dojo.byId("manageAccountResendActivationpopupform") != undefined){
						form.successUrl = dojo.byId("manageAccountResendActivationpopupform").getAttribute("successUrl");
					}
					if(obj != undefined && obj.email){
						dojo.query(".resendEmailSuccessId").attr("innerHTML",obj.email);
						
						dojo.query(".EditPasswordForm").attr("class","EditPasswordForm customer-form hide");
						dojo.query("#resendActivationSuccess").attr("class","EditPasswordForm customer-form show");
						
						
						return false;
					}
					
					if(obj[0] != undefined){
						/*if(obj[0].code == "PWD"){
							var forgetPasswordErrorLink = '<br /> <a class="ForgotPasswordLinkFromOverlay" url="/holiday/customer-account/login" commonsignin="yes" href="javascript:void(0)"><span>Forgotten your password?</span></a>';
							dojo.byId("resendpopemailError_1").innerHTML = obj[0].description+forgetPasswordErrorLink;	
							dojo.byId("resendpopemailError_1").style.display="inline-block";
							dojo.byId("resendpopemailError_2").style.display="inline-block";
							dojo.byId("resendpopemailError_3").style.display="none";
							var row = dojo.byId("passwordFieldDiv");
							dojo.addClass(row, "error pwd");
							thisForm.switchToForgotPassword();
						}*/
						if(obj[0].code == "SUSPENDED" || obj[0].code == "CLOSED" || obj[0].code == "INACTIVE" ){
							if(tuiSiteName == "firstchoice"){
							dojo.byId("resendpopemailError_1").innerHTML = "This account has been suspended . See our <a href='http://www."+tuiSiteName+".co.uk/contact-us/' class='small-text' target='_blank'>contact us</a> page to get in touch";
							}
							else{
							dojo.byId("resendpopemailError_1").innerHTML = "This account has been suspended . See our <a href='http://www."+tuiSiteName+".co.uk/editorial/legal/contact-us.html' class='small-text' target='_blank'>contact us</a> page to get in touch";
							}
							dojo.byId("resendpopemailError_1").style.display="inline-block";
							dojo.byId("resendpopemailError_2").style.display="inline-block";
							dojo.byId("resendpopemailError_3").style.display="none";
							var row = dojo.byId("activeemailFieldDiv");
							dojo.addClass(row, "error");
						}
						if(obj[0].code == "REGISTERED"){
							dojo.byId("resendpopemailError_1").innerHTML = "Your account hasn't been activated yet <a href='javascript:void(0)' class='ResendActivationLinkFromOverlay'>Resend activation email</a>";
							dojo.byId("resendpopemailError_1").style.display="inline-block";
							dojo.byId("resendpopemailError_2").style.display="inline-block";
							dojo.byId("resendpopemailError_3").style.display="none";
							var row = dojo.byId("activeemailFieldDiv");
							dojo.addClass(row, "error");
						}
						if(obj[0].code == "ACTIVE"){
							
							//prepopulate email field in sign in overlay
							if(dojo.byId("resendpopemail") != undefined){
								if(dojo.byId("popemail") != undefined){
									dojo.byId("popemail").value = dojo.byId("resendpopemail").value;
								}
							}
							dojo.byId("resendpopemailError_1").innerHTML = "this account has been activated already ,  <a href='javascript:void(0)' class='SignInFromOverlay'>click here to sign in</a>";
							dojo.byId("resendpopemailError_1").style.display="inline-block";
							dojo.byId("resendpopemailError_2").style.display="inline-block";
							dojo.byId("resendpopemailError_3").style.display="none";
							var row = dojo.byId("activeemailFieldDiv");
							dojo.addClass(row, "error");
							thisForm.switchToSignIn();
						}
						if(obj[0].code == "EMAIL"){
							dojo.byId("resendpopemailError_1").innerHTML = obj[0].description;
							dojo.byId("resendpopemailError_1").style.display="inline-block";
							dojo.byId("resendpopemailError_2").style.display="inline-block";
							dojo.byId("resendpopemailError_3").style.display="none";
							var row = dojo.byId("activeemailFieldDiv");
							dojo.addClass(row, "error");							
						}
					}
				},
				// The error handler
				error: function(errors) { 
					thisForm.handleBackendError(errors);
					dojo.addClass("resendActivation_btn","loading");
				},
				
				// The complete handler
				handle: function() {
					hasBeenSent = true;
				}
			});
			
			 var thisForm = this;		 
			 dojo.removeClass(thisForm.domNode, 'updating');
			 dojo.removeClass("resendActivation_btn","loading");	
		},
		attachEventListener: function(){
			var resendActivationComponent = this;
            var createresendActivation = dojo.byId("manageAccountResendActivationpopupform");
            dojo.connect("wouldyouliketoGoResendActivationClose", "onclick", function(){ console.log("came in");					
				resendActivationComponent.showHidePopup("wouldyouliketoGoResendActivation", "hide");
			});
			
			var myButton = dojo.byId("wouldyouliketoGoResendActivationClose");				
			 
			dojo.connect(myButton, "onclick", function(evt){
				resendActivationComponent.showHidePopup("wouldyouliketoGoResendActivation", "hide");	
			});


            dojo.connect(createresendActivation, "onsubmit", function(event){
			
				//add loader
				dojo.addClass("resendActivation_btn","loading");
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
					//console.log("not success");        		
            		var container = form;            		
            		dojo.query('input', container).forEach(
            		  function(inputElem){            		   
            			  var field = inputElem.id;
                  		  focusUtil.focus(dojo.byId(field));
                  		  dojo.byId(field).blur();
            		  }
            		);
            		dojo.stopEvent(event);
					dojo.removeClass("resendActivation_btn","loading");
            		return false;
            	}
            	else{
					if(resendActivationComponent.customValidator()){
            		console.log("success");
					resendActivationComponent.submittheForm(createresendActivation);						
					}
            	}               
                
              });
    	},
		startup: function() {		
		},
		customValidator:function(){
			var error = 0;
			if(dojo.byId("resendpopemailError_1").style.display != "none"){
				error = 1;
			}						
			if(error == 1){
			return false;
			}
			return true;			
		}
	});
	
	return tui.widget.customeraccount.LoadResendActivationPopup;
});