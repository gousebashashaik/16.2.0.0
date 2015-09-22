define ("tui/widget/customeraccount/SessionTimedOut", [
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

		dojo.declare("tui.widget.customeraccount.SessionTimedOut", [tui.widget._TuiBaseWidget], {
	    SessionTimedOut:null,
		email:null,
		sessionInactiveTime:null,		
		postCreate: function() {
			var SessionTimedOut = this;	
			var localStoragename = tuiSiteName+"_sessionTimedOutEmail";
            if(SessionTimedOut.email != null && SessionTimedOut.email != "null" && SessionTimedOut.email != ""){
				console.log("Set SessionTimedOut Email:"+SessionTimedOut.email);
				localStorage.setItem(localStoragename, SessionTimedOut.email);
			}
			if(SessionTimedOut.timedOut != null && SessionTimedOut.timedOut != "null" && SessionTimedOut.timedOut != ""){
				if(dojo.byId("email") != undefined && (SessionTimedOut.sessionInactiveTime != "null" && SessionTimedOut.sessionInactiveTime != null && SessionTimedOut.sessionInactiveTime != "") ){
					var email = localStorage.getItem(localStoragename);
					if(email != ""){
						console.log("Get SessionTimedOut Email:"+SessionTimedOut.email);
						console.log("Get SessionTimedOut:"+SessionTimedOut.timedOut);
						dojo.byId("email").value = email;
						if(dojo.byId("sessionTimedOut") != undefined ){
							dojo.byId("emailError_3").style.display="inline-block";
						}
						
						if(dojo.byId("sessionTimedOut") != undefined){
							dojo.query("#sessionTimedOut").attr("class", "show alert mid-level");
						}
						
						//don't show logged out message when no cookies are present
						/*
						var cookie = dojo.cookie('customerCookieForLoginPage');				
						if(cookie == undefined || cookie == "undefined"){
							var isLogin = dojo.cookie('JSESSIONID');
							var cookieLogin = dojo.cookie(isLogin);
							if(cookieLogin == undefined || cookieLogin == "undefined"){
								if(dojo.byId("sessionTimedOut") != undefined){
									dojo.query("#sessionTimedOut").attr("class", "hide alert mid-level");
								}
							}
						}
						*/
					}
				}
			}
			if(SessionTimedOut.sessionInactiveTime == "" || SessionTimedOut.sessionInactiveTime == null || SessionTimedOut.sessionInactiveTime == "null" ){
				localStorage.setItem(localStoragename,"");
			}
			SessionTimedOut.inherited(arguments);
		}
	});
	
	return tui.widget.customeraccount.SessionTimedOut;
});