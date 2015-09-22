define ("tui/widget/customeraccount/LoadSignInPopup", [
        													"dojo",											  	
        													"dojo/cookie",
        													"dojo/query",
        													"dojo/has",
        													"dojox/validate/web",
        													"tui/validate/check",
        													"dojo/_base/array",
        													"dojo/dom-style",
        													"dijit/focus",
        													"dojo/text!tui/widget/customeraccount/view/templates/SigninPopup.html",
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
															"tui/widget/customeraccount/CommonGetShortListHolidayCount",
															"tui/widget/customeraccount/ErrorHandling"
        													
        													
        							    			  ], function(dojo,  cookie, query, has, validate,check, arrayUtil, domStyle, focusUtil, SigninPopupTmpl,xhr){

		dojo.declare("tui.widget.customeraccount.LoadSignInPopup", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable, tui.widget.customeraccount.CommonGetShortListHolidayCount,tui.widget.customeraccount.ErrorHandling], {
		
		tmpl: SigninPopupTmpl,	
        tuiWebrootPath:tuiWebrootPath,
        uid:null,	
		successUrl : null,
		commonSignIn:null,
		emailFromSignIn:null,
		sessionTimedOut:null,
		sessionInactiveTime:null,
		
		onAfterTmplRender: function(){
			var signinComponent = this;			 
			 dojo.removeClass(signinComponent.domNode, 'updating');			 
		},
		resetTheForm:function(){
			
			/*console.log("SignIn:"+dojo.byId("popemail").value);
			console.log("Forgot:"+dojo.byId("forgotpopemail").value);*/
			
			if(dojo.query(".sessionInactiveTimeMsg")[0] != undefined){
				dojo.addClass(dojo.query(".sessionInactiveTimeMsg")[0], "hide");
				dojo.addClass(dojo.query(".sessionInactiveTimeuid")[0], "hide");
				dojo.byId("accountStatus").setAttribute("class","row no-label hide");
			}
			if(dojo.byId("popemail") != undefined && this.validator(dojo.byId("popemail").value)){
				this.emailFromSignIn = dojo.byId("popemail").value;				
			}
			dojo.query('.modal').forEach(function(container){
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
			});
				
			if(dojo.byId("manageAccountsigninpopupform") != undefined){
				var container = dojo.byId("manageAccountsigninpopupform");           
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
				if(dojo.byId("popupkeepMeSignin") != undefined){
				dojo.query("#popupkeepMeSignin").attr("checked","");
				dojo.query("#popupkeepmesignedinCheckbox").attr("class","radio active loaded");
				}
				
				
				//retain email value from create account
				if(dojo.byId("popupemail") != undefined){
					if(dojo.byId("popemail") != undefined){
						if(dojo.byId("popupemail").value != ""){
							dojo.byId("popemail").value = dojo.byId("popupemail").value;
						}
						
					}
				}
				
				//retain email value from Resend Activation
				/*
				if(dojo.byId("resendpopemail") != undefined){
					if(dojo.byId("popemail") != undefined){
						dojo.byId("popemail").value = dojo.byId("resendpopemail").value;
					}
				}
				*/
				
		    }
			if(dojo.byId("CreateAccountpopupform") != undefined){
				var container = dojo.byId("CreateAccountpopupform");           
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
			if(dojo.byId("manageAccountForgotPasswordpopupform") != undefined){
				var container = dojo.byId("manageAccountForgotPasswordpopupform");           
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
			if(dojo.byId("manageAccountResendActivationpopupform") != undefined){
				var container = dojo.byId("manageAccountResendActivationpopupform");           
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
			if(dojo.byId("forgotpopemail") != undefined){
				if(dojo.byId("popemail") != undefined){
					dojo.byId("forgotpopemail").value = this.emailFromSignIn;
				}
			}
            if(dojo.byId("resendpopemail") != undefined){
				if(dojo.byId("popemail") != undefined){
					dojo.byId("resendpopemail").value = this.emailFromSignIn;
				}
			}
            if(this.uid != undefined && this.uid != "undefined" && this.uid != ""){
            	 dojo.byId("popemail").value = this.uid;	
            }
			return false;
		},
		validator: function(val) {
		   var pattern;
		   return pattern =/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/.test(val);
		  // return pattern = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(val);
		},
		showHidePopup:function(PopupId, type){
			this.resetTheForm(); console.log("called reset 1");
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
			var signinComponent = this;	
			
			signinComponent.loadSigninForm();			
			signinComponent.attachEventListener();
			signinComponent.switchToCreateAccount();
			signinComponent.switchToForgotPassword();
			signinComponent.switchToResendActivation();		
			
			signinComponent.inherited(arguments);
           		
		},
		switchToTransferHolidays: function() {
			var signinComponent = this;	
			console.log("One");			
			signinComponent.showHidePopup("wouldyouliketoGo", "hide");
			console.log("Two");			
			signinComponent.showHidePopup("transCookiePop", "show");
			console.log("Three");			
		},
		switchToCreateAccount: function() {
			var signinComponent = this;	
			
			dojo.query(".CreateAccountLinkFromOverlay").onclick(function(){				
				signinComponent.showHidePopup("wouldyouliketoGo", "hide");
				signinComponent.showHidePopup("GotoCreateAccount", "show");
				return false;
			});
		},
		switchToForgotPassword: function() {
			var signinComponent = this;	
			
			dojo.query(".ForgotPasswordLinkFromOverlay").onclick(function(){				
				signinComponent.showHidePopup("wouldyouliketoGo", "hide");
				signinComponent.showHidePopup("wouldyouliketoGoForgotPassword", "show");
				this.emailFromSignIn = "";
			});
		},
		switchToResendActivation: function() {
			var signinComponent = this;	
			
			dojo.query(".ResendActivationLinkFromOverlay").onclick(function(){				
				signinComponent.showHidePopup("wouldyouliketoGo", "hide");
				signinComponent.showHidePopup("wouldyouliketoGoResendActivation", "show");
			});
		},
		loadSigninForm:function(){
			var signinComponent = this;	
			signinComponent.keepMecookieChecked = "";
			
					
			dojo.query(".manageaccountlink").forEach(function(element){

				dojo.connect(element, "onclick", function(event){
			    dojo.stopEvent(event);
				var currentLink = this;   
			   
				var successUrl = this.getAttribute("url");
				var commonSignIn = this.getAttribute("commonSignIn");
				
				var flag = 0;
				if(commonSignIn == "yes"){
					flag = 1;
				}
				if(dojo.byId("manageAccountsigninpopupform") != undefined){
					dojo.byId("manageAccountsigninpopupform").setAttribute("successUrl", successUrl);
					dojo.byId("manageAccountsigninpopupform").setAttribute("commonSignIn", commonSignIn);
				}
                signinComponent.emailFromSignIn = '';

                if(flag){
					dojo.stopEvent(event);
					signinComponent.showHidePopup("wouldyouliketoGo", "show");						 
					return false;
				}
                var cookie = dojo.cookie('customerCookieForLoginPage');
			
				
                if(cookie != undefined && cookie != "undefined"){
					signinComponent.keepMecookieChecked = "checked";
				}
				
				
				//keepMecookieChecked case
				if(signinComponent.keepMecookieChecked == "checked"){			
					//signinComponent.sessionInactiveTime = "";
					var loginCookie = dojo.cookie('customerCookieForLoginPage');
					var jsessionId = dojo.cookie('JSESSIONID');	
					
					var sessionCookie = dojo.cookie('sessionCookie');
					if(jsessionId == sessionCookie){
						var cookie = true;
					}
				
					var loggedIn = "null";
					if(jsessionId == sessionCookie){
						loggedIn = "loggedIn";
					}
					if(loggedIn == "loggedIn"){					   
						if(successUrl){
							window.location.assign(successUrl);
							return false;
						}
					}
					else{ 
						//handle shorlisted holidays page click on session timed out
						if(dojo.hasClass(currentLink, "commonpage")){
							//var successUrl = tuiWebrootPath+"/viewShortlist/viewsavedholidaysPage";
							if(successUrl){
								window.location.assign(successUrl);
								return false;
							}
							return false;
						}
						signinComponent.sessionTimedOut = "true";
						signinComponent.showHidePopup("wouldyouliketoGo", "show");
						 dojo.stopEvent(event);
						return false;
					}
				}
				else if(signinComponent.keepMecookieChecked == ""){
					var loginCookie = dojo.cookie('customerCookieForLoginPage');
					var jsessionId = dojo.cookie('JSESSIONID');	
					
					var sessionCookie = dojo.cookie('sessionCookie');
					if(jsessionId == sessionCookie){
						var cookie = true;
					}
					var loggedIn = "";
					if(sessionCookie != undefined && sessionCookie != "undefined"){
						loggedIn = "loggedIn";
					}
					if(!loggedIn){
					
						var compareItemsList = tuiSiteName+"compareItemsList";
						localStorage.setItem(compareItemsList, "{}");
						var successUrl = tuiWebrootPath+"/customer-account/login?sessionTimedOut=true";
					}
					
					if(successUrl){					   
						if(successUrl){
								window.location.assign(successUrl);
								return false;
						}
					}
				   
				}
				

			});	
				
			});	
			
			var loginCookie = dojo.cookie('customerCookieForLoginPage');
			
			if(loginCookie != undefined && loginCookie != "undefined" &&  loginCookie != ""){
				signinComponent.keepMecookieChecked = "checked";
				signinComponent.sessionInactiveTime = "";
			}
			else{
				signinComponent.keepMecookieChecked = "";
			}
			
			var html;
			if(signinComponent.keepMecookieChecked == "checked"){			
				//signinComponent.sessionInactiveTime = "";
			}
			if(signinComponent.keepMecookieChecked == undefined){			
				signinComponent.keepMecookieChecked = "";
			}
			//alert("sessionTimedOut:"+ signinComponent.sessionTimedOut+", sessionInactiveTime:"+ signinComponent.sessionInactiveTime+", keepMecookieChecked:"+signinComponent.keepMecookieChecked);
			
			html = signinComponent.renderTmpl(SigninPopupTmpl,{tuiWebrootPath:tuiWebrootPath,keepMecookie: signinComponent.keepMecookieChecked,uid: signinComponent.uid, sessionTimedOut: signinComponent.sessionTimedOut, sessionInactiveTime: signinComponent.sessionInactiveTime});

            if (html) { 
                dojo.place(html, signinComponent.domNode, "only");
                dojo.parser.parse(signinComponent.domNode);
                
            }
			if(dojo.byId("manageAccountsigninpopupform") != undefined){
				signinComponent.successUrl = dojo.byId("manageAccountsigninpopupform").getAttribute("successUrl");
				signinComponent.commonSignIn = dojo.byId("manageAccountsigninpopupform").getAttribute("commonSignIn");
			}
			
			//session timed implementation
			if(dojo.byId("popemail") != undefined){
				var email = dojo.byId("popemail").value;						
				localStorage.setItem('sessionTimedOutEmail', email);				
			}
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
		reloadThePage:function(){
				
			var loc = document.location.toString();
			if(loc.indexOf("login") != -1){
				var url = tuiWebrootPath+"/customer-cccount/login";
				window.location.assign(url);
				return false;
			}
			else{
				var url = tuiWebrootPath+"/viewShortlist/viewsavedholidaysPage";
				window.location.assign(url);
				return false;
			}
		},
		submittheForm: function(form) { 
						
			 var thisForm = this;			 
			 dojo.addClass(thisForm.domNode, 'updating');	
			 
			 //quickfix
			 if(form.popupkeepMeSignin.checked == true){
				form.popupkeepMeSignin.value = "1";
			 }
			 else{
				form.popupkeepMeSignin.value = "0";
			 }			
			xhr.post({
				
				url: form.action+"?cache="+Math.random(),				
				content: { "email": form.popemail.value, "pwd": form.poppwd.value, "keepMeSignin": form.popupkeepMeSignin.value },
				// The success handler
				load: function(response) {
				    var AccountLogin=this;
					//thisForm.handleSessionTimeOut(response);
					var obj = JSON.parse(response);
										
					if(dojo.byId("manageAccountsigninpopupform") != undefined){
						form.successUrl = dojo.byId("manageAccountsigninpopupform").getAttribute("successUrl");
						var commonSignIn = dojo.byId("manageAccountsigninpopupform").getAttribute("commonSignIn");
					}
					if(obj != undefined && obj.email){	
						thisForm.getShorListedHolidaysCount();
						var uid = dojo.byId("puid").value;
						var s_url = form.successUrl;

                      						
						console.log("commonSignIn:"+commonSignIn);
						if(commonSignIn == 'yes'){
							var s_url = document.URL;
							console.log("commonSignIn:"+s_url);							
							
							if(obj.savedHolidayCookie == true || obj.savedHolidayCookie == "true"){
								thisForm.switchToTransferHolidays();
								return false;
							}
							thisForm.reloadThePage();
						}
						else{
							if(s_url == null || s_url == "s_url"){
							var s_url = document.URL;
							window.location.assign(s_url);
							}
							else{
							window.location.assign(s_url);
							}
						}
						return false;
					}
					
					if(obj[0] != undefined){ 						
						
						if(obj[0].code == "PWD"){
							var forgetPasswordErrorLink = '<br /> <a class="ForgotPasswordLinkFromOverlay" url="/holiday/customerAccount/customerAccountLogin" commonsignin="yes" href="javascript:void(0)"><span>Forgotten your password?</span></a>';
							dojo.byId("poppwdError_1").innerHTML = obj[0].description+forgetPasswordErrorLink;	
							dojo.byId("poppwdError_1").style.display="inline-block";
							dojo.byId("poppwdError_2").style.display="inline-block";
							dojo.byId("poppwdError_3").style.display="none";
							var row = dojo.byId("passwordFieldDiv");
							dojo.addClass(row, "error pwd");
							thisForm.switchToForgotPassword();
						}
						if(obj[0].code == "SUSPENDED" || obj[0].code == "CLOSED" || obj[0].code == "INACTIVE" ){
							if(tuiSiteName == "firstchoice"){
							dojo.byId("poppwdError_1").innerHTML = "There is a problem with your account. See our <a href='http://www."+tuiSiteName+".co.uk/contact-us' class='small-text' target='_blank'>contact us</a> page to get in touch";
							}
							else{
							dojo.byId("poppwdError_1").innerHTML = "There is a problem with your account. See our <a href='http://www."+tuiSiteName+".co.uk/editorial/legal/contact-us.html' class='small-text' target='_blank'>contact us</a> page to get in touch";
							}
							dojo.byId("poppwdError_1").style.display="inline-block";
							dojo.byId("poppwdError_2").style.display="inline-block";
							dojo.byId("poppwdError_3").style.display="none";
							var row = dojo.byId("passwordFieldDiv");
							dojo.addClass(row, "error pwd");
						}
						if(obj[0].code == "REGISTERED"){
							
							dojo.byId("accountStatus").innerHTML = "<p class='alert high-level generic row-content'><i class='caret warning'></i>Your account hasn't been activated yet <a href='javascript:void(0)' class='ResendActivationLinkFromOverlay'>Resend activation email</a></p>";
							dojo.byId("accountStatus").setAttribute("class","row no-label show");
							dojo.byId("poppwdError_1").style.display="none";
							dojo.byId("poppwdError_2").style.display="none";
							dojo.byId("poppwdError_3").style.display="none";
							var row = dojo.byId("passwordFieldDiv");
							dojo.addClass(row, "pwd");
							thisForm.switchToResendActivation();
						}
						if(obj[0].code == "EMAIL"){
							dojo.byId("popemailError_1").innerHTML = obj[0].description;
							dojo.byId("popemailError_1").style.display="inline-block";
							dojo.byId("popemailError_2").style.display="inline-block";
							dojo.byId("popemailError_3").style.display="none";
							var row = dojo.byId("emailFieldDiv");
							dojo.addClass(row, "error");
							
						}											
						return false;
					}
					console.log(form.successUrl);
					if(form.successUrl == "undefined" || form.successUrl == undefined || form.successUrl == null){
						window.location.assign(document.location);
							return false;
					}
					
				},
				// The error handler
				error: function(errors) {
					thisForm.handleBackendError(errors);
					dojo.addClass("popsignin_btn","loading");
				},
				// The complete handler
				handle: function() {
					hasBeenSent = true;
				}
			});
			
			 var thisForm = this;		 
			 dojo.removeClass(thisForm.domNode, 'updating');
			 dojo.removeClass("popsignin_btn","loading");	
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
			var signinComponent = this;
			
			
            var createLogin = dojo.byId("manageAccountsigninpopupform");
            dojo.connect("wouldyouliketoGoClose", "onclick", function(){ console.log("came in");					
				signinComponent.showHidePopup("wouldyouliketoGo", "hide"); 
				this.emailFromSignIn = "";
				return false;
			});
			dojo.query(".title .close").connect("onclick", function(){ console.log("called in sign in"); 
				this.emailFromSignIn = "";
				signinComponent.showHidePopup("wouldyouliketoGo", "hide");
				return false;
			});
			var myButton = dojo.byId("wouldyouliketoGoClose");				
			 
			dojo.connect(myButton, "onclick", function(evt){
				signinComponent.showHidePopup("wouldyouliketoGo", "hide");
                return false;				
			});


            dojo.connect(createLogin, "onsubmit", function(event){
			
				//add loader
				dojo.addClass("popsignin_btn","loading");
                var form = this;
                dojo.stopEvent(event);
				//quickfix
				if(dojo.byId("popupkeepMeSignin") != undefined){
					if(dojo.byId("popupkeepMeSignin").value == ""){
					dojo.byId("popupkeepMeSignin").value = "0";
					}
				}
                var accountFileds = {
	             		   trim: ["email", "pwd"],
	             		   required: ["email","pwd"],
	             		   constraints: {
	             			   email: [validate.isEmailAddress, false, true]
	             		   }
	             } ;				
                var results = validate.check(form, accountFileds); 
            	if(!results.isSuccessful()){     console.log("not success");        		
            		var container = form;            		
            		dojo.query('input', container).forEach(
            		  function(inputElem){            		   
            			  var field = inputElem.id;
                  		  focusUtil.focus(dojo.byId(field));
                  		  dojo.byId(field).blur();
            		  }
            		);
            		dojo.stopEvent(event);
					dojo.removeClass("popsignin_btn","loading");
            		return false;
            	}
            	else{
					if(signinComponent.customValidator()){
						console.log("success");
						signinComponent.submittheForm(createLogin);
					}					
            	}               
                
              });
    	},
		startup: function() {		
		},
		customValidator:function(){
			var error = 0;
			if(dojo.byId("popemailError_1").style.display == "inline-block"){
				error = 1;
			}
			else if(dojo.byId("poppwdError_1").style.display == "inline-block"){
				error = 1;
			}			
			if(error == 1){
			return false;
			}
			return true;			
		}
	});
	
	return tui.widget.customeraccount.LoadSignInPopup;
});