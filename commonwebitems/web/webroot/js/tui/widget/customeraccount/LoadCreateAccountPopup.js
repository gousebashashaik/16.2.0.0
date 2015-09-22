define ("tui/widget/customeraccount/LoadCreateAccountPopup", [
        													"dojo",											  	
        													"dojo/cookie",
        													"dojo/query",
        													"dojo/has",
        													"dojox/validate/web",
        													"tui/validate/check",
        													"dojo/_base/array",
        													"dojo/dom-style",
        													"dijit/focus",
        													"dojo/text!tui/widget/customeraccount/view/templates/CreateAccountPopup.html",
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
        													
        													
        							    			  ], function(dojo,  cookie, query, has, validate,check, arrayUtil, domStyle, focusUtil, CreateAccountPopup,xhr){

		dojo.declare("tui.widget.customeraccount.LoadCreateAccountPopup", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable, tui.widget.customeraccount.ErrorHandling], {
		
		tmpl: CreateAccountPopup,	
        tuiWebrootPath:tuiWebrootPath,       	
		successUrl : null,
		componentUid:null,
		
		onAfterTmplRender: function(){
			var CreateAccountComponent = this;
			 dojo.query(".mask-interactivity").style.diplay="none";			 
			 dojo.removeClass(CreateAccountComponent.domNode, 'updating');			 
		},
		postCreate: function() {
			var CreateAccountComponent = this;
			
			CreateAccountComponent.loadCreateAccountForm();			
			CreateAccountComponent.attachEventListener();
			CreateAccountComponent.inherited(arguments);			
		},
		showHidePopup:function(type){ console.log("one");
			this.resetTheForm(); console.log("called reset");
			var root = document.getElementsByTagName( 'html' )[0];
			if(type == "show"){
				dojo.removeClass("GotoCreateAccount", "hide");
				dojo.addClass("GotoCreateAccount", "show");
				dojo.addClass(root, "modal-open");
			}
			else{
				dojo.removeClass("GotoCreateAccount", "show");
				dojo.addClass("GotoCreateAccount", "hide");
				dojo.removeClass(root, "modal-open");
			}
		},
		manageSignInOverlayLinkOnError:function(){
			var CreateAccountComponent = this;	
			console.log("two");
			dojo.query(".manageaccountlink").onclick(function(){
				var successUrl = this.getAttribute("url");
				var commonSignIn = this.getAttribute("commonSignIn");
				
				//return false;
				if(dojo.byId("manageAccountsigninpopupform") != undefined){
					dojo.byId("manageAccountsigninpopupform").setAttribute("successUrl", successUrl);
					dojo.byId("manageAccountsigninpopupform").setAttribute("commonSignIn", commonSignIn);
				}
				CreateAccountComponent.showHidePopup("hide");
				CreateAccountComponent.showHidePopupLoginOnError("wouldyouliketoGo", "show");
			});	
		
		},
		showHidePopupLoginOnError:function(PopupId, type){
		console.log("three");
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
		loadCreateAccountForm:function(){
			var CreateAccountComponent = this;	
			
			dojo.query(".createaccountlink").onclick(function(){
				var successUrl = this.getAttribute("url");
				if(dojo.byId("CreateAccountpopupform") != undefined){
					dojo.byId("CreateAccountpopupform").setAttribute("successUrl", successUrl);					
				}
				CreateAccountComponent.showHidePopup("show");
			});
			var html = CreateAccountComponent.renderTmpl(CreateAccountPopup,{tuiWebrootPath:tuiWebrootPath,componentUid: CreateAccountComponent.componentUid});           

            if (html) { 
                dojo.place(html, CreateAccountComponent.domNode, "only");
                dojo.parser.parse(CreateAccountComponent.domNode);
            }
			if(dojo.byId("CreateAccountpopupform") != undefined){
				CreateAccountComponent.successUrl = dojo.byId("CreateAccountpopupform").getAttribute("successUrl");				
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
		submittheForm: function(form) { 
		     	
			 var thisForm = this;
			 dojo.query(".mask-interactivity").style.diplay="block";			 
			 dojo.addClass(thisForm.domNode, 'updating');	
			 
			xhr.post({
				
				url: form.action+"?cache="+Math.random(),
				form: dojo.byId(form.id),
				// The success handler
				load: function(response) {
				
					//thisForm.handleSessionTimeOut(response);
					console.log("inside load 1");			    
					var obj = JSON.parse(response);
					console.log("inside load 2"+obj);
					console.log("submittheForm:"+obj.email);
					if(dojo.byId("CreateAccountpopupform") != undefined){
						form.successUrl = dojo.byId("CreateAccountpopupform").getAttribute("successUrl");
					}
					console.log("inside load 3"+obj.email);
					if(obj != undefined && obj.email){
						console.log("submittheForm:success2");						
						dojo.query("#CAcreateAccountOverlayForm").attr("class", "content-width hide");
						dojo.query("#CAcreateAccountOverlaySuccess").innerHTML=dojo.query('#accountsucessmsg').innerHTML;
						dojo.query("#CAcreateAccountOverlaySuccess").attr("class", "content-width show");
						var obj = dojo.byId("GotoCreateAccount");
						dojo.query(obj).children(".window").attr("class","window auto-height");						
						return false;
					}
										
                    console.log("code:"+obj[0].code);
					if(obj[0] != undefined){
						var errorFlag = 0;
						if(obj[0].code == "PWD"){
							
							dojo.byId("popuppwdError_1").innerHTML = obj[0].description;
							dojo.byId("popuppwdError_1").style.display="inline-block";
							dojo.byId("popuppwdError_2").style.display="inline-block";
							dojo.byId("popuppwdError_3").style.display="none";
							var row = dojo.byId("passwordDiv");
							dojo.addClass(row, "error c pwd");
							errorFlag = 1;
						}
						if(obj[0].code == "EMAIL"){
							
							dojo.byId("popupemailError_1").innerHTML = obj[0].description;
							dojo.byId("popupemailError_1").style.display="inline-block";
							dojo.byId("popupemailError_2").style.display="inline-block";
							dojo.byId("popupemailError_3").style.display="none";
							var row = dojo.byId("emailDiv");
							dojo.addClass(row, "error c");
							errorFlag = 1;
						}
						if(obj[0].code == "ACCOUNTEXISTWITHEMAILADDRESS"){
						
							//prepopulate email field in sign in overlay
							if(dojo.byId("popupemail") != undefined){
								if(dojo.byId("popemail") != undefined){
									dojo.byId("popemail").value = dojo.byId("popupemail").value;									
								}
							}
							
							var signInErrorLink = ' <a class="manageaccountlink" url="'+tuiWebrootPath+'/customer-account/login" commonsignin="yes" href="javascript:void(0)"><span>Sign in to your account</span></a>';
							dojo.byId("popupemailError_1").innerHTML = obj[0].description+signInErrorLink;
							dojo.byId("popupemailError_1").style.display="inline-block";
							dojo.byId("popupemailError_2").style.display="inline-block";
							dojo.byId("popupemailError_3").style.display="none";
							var row = dojo.byId("emailDiv");
							dojo.addClass(row, "error c");
							
							//dojo.byId("reEnterEmailError_1").innerHTML = obj[0].description;
							dojo.byId("popupreEnterEmailError_1").style.display="none";
							dojo.byId("popupreEnterEmailError_2").style.display="inline-block";
							dojo.byId("popupreEnterEmailError_3").style.display="none";
							var row = dojo.byId("reEnterEmailDiv");
							dojo.addClass(row, "error c");
							thisForm.manageSignInOverlayLinkOnError();
							errorFlag = 1;
						}
						
					}
					thisForm.attachEventListener();
					dojo.removeClass("create-account_btn","loading");
					if(errorFlag == 1){
						return false;
					}
				},
				// The error handler
				error: function(errors) {
				thisForm.handleBackendError(errors);
				dojo.addClass("create-account_btn","loading");
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
		resetTheForm:function(){
			if(dojo.byId("CreateAccountpopupform") != undefined){
			
				dojo.query("#CAcreateAccountOverlayForm").attr("class", "content-width show");						
				dojo.query("#CAcreateAccountOverlaySuccess").attr("class", "content-width hide");
						
				var container = dojo.byId("CreateAccountpopupform");           
								
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
				if(dojo.byId("popuppwd-strength") != undefined){
					dojo.byId("popuppwd-strength").setAttribute("class","pwd-strength");
				}
		    }
		},
		attachEventListener: function(){
			var CreateAccountComponent = this;
            var createAccount = dojo.byId("CreateAccountpopupform");			
            		
			dojo.query('input', createAccount).forEach(
			  function(inputElem){            		   
				  var field = inputElem.id;				  
				  dojo.connect(field, "onBlur", function() {
					if(trim(dojo.byId("popuppwd").value) == trim(dojo.byId("popuppwd").value) ){
					var p1 = dojo.byId("popuppwd");
					var p1 = dojo.byId("popupcheckPwd");
					dojo.byId(p1).blur();
					dojo.byId(p2).blur();
					}
				  });
			  }
			);				
			
            dojo.connect("GotoCreateAccountClose", "onclick", function(){ console.log("came in");
				CreateAccountComponent.showHidePopup("hide");	
			});
			
			var myButton = dojo.byId("GotoCreateAccountClose");
			dojo.connect(myButton, "onclick", function(evt){
				CreateAccountComponent.showHidePopup("hide");
			});


            dojo.connect(createAccount, "onsubmit", function(event){
				
				//show loader
				
				dojo.addClass("create-account_btn","loading"); 
			
                var form = this;
                dojo.stopEvent(event);               
                var accountFileds = {
	             		   trim: ["firstName", "lastName","email","reEnterEmail","pwd","checkPwd"],
	             		   required: ["firstName", "lastName","email","reEnterEmail","pwd","checkPwd"],
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
					dojo.removeClass("create-account_btn","loading");
            		return false;
            	}
            	else{    		
					
					if(CreateAccountComponent.customValidator()){
						CreateAccountComponent.submittheForm(createAccount);
					}					
            	}               
                
              });
    	},
		startup: function() {		
		},
		customValidator:function(){
			var error = 0;
			if(dojo.byId("popupfirstNameError_1").style.display != "none"){
				error = 1;
			}
			else if(dojo.byId("popuplastNameError_1").style.display != "none"){
				error = 1;
			}
			else if(dojo.byId("popupemailError_1").style.display != "none"){
				error = 1;
			}
			else if(dojo.byId("popupreEnterEmailError_1").style.display != "none"){
				error = 1;
			}
			else if(dojo.byId("popuppwdError_1").style.display != "none"){
				error = 1;
			}
			else if(dojo.byId("popupcheckPwdError_1").style.display != "none"){
				error = 1;
			}
			
			var pwdVal = dojo.byId("popuppwd").value;
			var reChkPwdVal = dojo.byId("popupcheckPwd").value;
			
			var pwdVal = pwdVal.trim();
			var reChkPwdVal = reChkPwdVal.trim();
			
			if(pwdVal != reChkPwdVal){
			dojo.byId("popupcheckPwd").blur();
			error = 1;
			}
			if(error == 1){
			return false;
			}
			return true;			
		}
	});
	
	return tui.widget.customeraccount.LoadCreateAccountPopup;
});