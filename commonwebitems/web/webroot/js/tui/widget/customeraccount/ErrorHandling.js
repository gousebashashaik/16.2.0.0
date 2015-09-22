define("tui/widget/customeraccount/ErrorHandling", 
		["dojo",
		 "dojo/on",
		 "dojo/cookie",
		 "dojo/query",
		 "dojo/has",
		 "dojox/validate/web",
		 "tui/validate/check",
		 "dojo/_base/array",
		 "dojo/dom-style",
		 "dijit/focus",
		 "dojo/dom-construct",
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
		 "dojox/validate/us"],
		function(dojo, on, cookie, query, has, validate,check, arrayUtil, domStyle, focusUtil, domConstruct,xhr){       
		
		dojo.declare("tui.widget.customeraccount.ErrorHandling", [tui.widget._TuiBaseWidget], {
					
		
			postCreate: function() {
				var currentPage = this;
				dojo.addOnLoad( function() {
					var checkSessionForThepages = [
							"login",
							"customerLogin",
							"viewCustomerBookings",
							"customerHome",
							"personalDetails",
							"editPassword",
							"cancelEditPwd",
							"getholidaypreferences",
							"viewCommunicationPreferences"
					];
					
					var loc = document.location.toString();
					var location = loc.substr(loc.lastIndexOf('/') + 1);
					console.log("loc:"+location);
					if(location != undefined){
						if(dojo.indexOf(checkSessionForThepages, location) != -1){
							currentPage.handleSessionTimeOut();
						}
						
					}
				});
			},
			handleException:function(errors){
				
				var response = errors;
				console.log(response);
								
				console.log(11);
				console.log("index:"+response.indexOf("in to your account"));
				if(response.indexOf("in to your account") != -1){		
				    console.log(22);
					var compareItemsList = tuiSiteName+"compareItemsList";
					localStorage.setItem(compareItemsList, "{}");
					window.location.assign(tuiWebrootPath+"/customer-account/login?sessionTimedOut=true")	;	
					console.log(33);		
					dojo.stopEvent(); 
					console.log(44);
					return false;
				}
				console.log(55);
				console.log("index:"+response.indexOf("having some technical problems"));
				if(response.indexOf("having some technical problems") != -1){		
				    console.log(66);			
					window.location.assign(tuiWebrootPath+"/customer-account/technicalErrorPage")	;	
					console.log(77);		
					dojo.stopEvent(); 
					console.log(88);
					return false;
				}
				
			},
			handleSessionTimeOut:function(errors){
				
				if(dojo.byId("popemail") != undefined){
					console.log("called");
					var loginCookie = dojo.cookie('customerCookieForLoginPage');
					var jsessionId = dojo.cookie('JSESSIONID');	
					
					var sessionCookie = dojo.cookie('sessionCookie');
					if(jsessionId == sessionCookie){
						var isLogin = true;
					}
					
					
					
					
					
					if(loginCookie != undefined && loginCookie != "undefined" &&  loginCookie != ""){					
						//signinComponent.sessionInactiveTime = "";
						
						//if(sessionCookie == undefined || sessionCookie == "undefined" ){
							if(jsessionId != sessionCookie){
							console.log("session expired"+this.id);
							dojo.query(".modal").forEach(function(eachModal){ 
								eachModal.setAttribute("class","modal hide");
							});
							var email = dojo.query(".sessionInactiveTimeuid").attr("innerHTML");					
							var field = dojo.byId("popemail");					
							focusUtil.focus(dojo.byId(field));
							field.value=email;					
							dojo.byId(field).blur();
							
							dojo.query("#wouldyouliketoGo").attr("class","modal show");
							var root = document.getElementsByTagName( 'html' )[0];
							dojo.addClass(root, "modal-open");
							return false;
							}
							
						//}	
						
						
						
					}
					else if(sessionCookie != undefined && sessionCookie != "undefined" ){	
						var sessionInactiveTime = dojo.query(".sessionInactiveTime").attr("innerHTML");
						dojo.removeClass(dojo.query(".sessionInactiveTimeMsg")[0], "hide");
						dojo.addClass(dojo.query(".sessionInactiveTimeMsg")[0], "show");
						var msg = "You haven't interacted with your account in the last "+sessionInactiveTime+" minutes so we've signed you out.";
						dojo.query(".sessionInactiveTimeMsg").attr("innerHTML",msg);
						if(jsessionId != sessionCookie){
						var email = dojo.query(".sessionInactiveTimeuid").attr("innerHTML");					
						var field = dojo.byId("popemail");					
						focusUtil.focus(dojo.byId(field));
						field.value=email;					
						dojo.byId(field).blur();
						console.log("session expired"+this.id);
						dojo.query(".modal").forEach(function(eachModal){ console.log(eachModal.id);
							eachModal.setAttribute("class","modal hide");
						});
						
						dojo.query("#wouldyouliketoGo").attr("class","modal show");
						var root = document.getElementsByTagName( 'html' )[0];
						dojo.addClass(root, "modal-open");
						return false;
						}
						
					}				
				}
				return true;
			},
			handleBackendError:function(errors){	
				
				var response = errors.responseText;				
				console.log("index:"+response.indexOf("having some technical problems"));
				if(response.indexOf("having some technical problems") != -1){		
				    console.log(66);			
					window.location.assign(tuiWebrootPath+"/customer-account/technicalErrorPage")	;	
					console.log(77);		
					dojo.stopEvent(); 
					console.log(88);
					return false;
				}
				
			}
			
		});
		return tui.widget.customeraccount.ErrorHandling;
});