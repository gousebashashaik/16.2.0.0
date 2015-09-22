define ("tui/widget/customeraccount/TransferCookiePopup", 
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
		 "dojo/text!tui/widget/customeraccount/view/templates/cookieTransPopUp.html",
		 "dojo/text!tui/widget/customeraccount/view/templates/cookieTransPopUpList.html",
		 "dojo/_base/connect",
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
		 "tui/widget/customeraccount/CommonGetShortListHolidayCount",
		 "tui/widget/mixins/Templatable",
		 "tui/widget/customeraccount/ErrorHandling"],
		function(dojo, on, cookie, query, has, validate,check, arrayUtil, domStyle, focusUtil, domConstruct, popHtml, popHtmlList, connect){
		dojo.declare("tui.widget.customeraccount.TransferCookiePopup", [tui.widget._TuiBaseWidget,tui.widget.customeraccount.CommonGetShortListHolidayCount,tui.widget.mixins.Templatable, tui.widget.customeraccount.ErrorHandling], {
			/* Component custom attributes */
			
			slectedCount:0,
			IsLoggedIn:null,
		    constructor: function() {
				// add here anything that will be executed in the widget initialization.
		    	
			},
			manageWindow: function(type){
				var someNode = dojo.query(".modal").attr("class");
				var root = document.getElementsByTagName( 'html' )[0];
				
				if(type === "SHOW"){
				var someNode = dojo.query(".modal").attr("class");
				var root = document.getElementsByTagName( 'html' )[0];
					dojo.addClass(root, "modal-open");
					dojo.removeClass("transCookiePop", "hide");
					dojo.addClass("transCookiePop", "show");	
					
				}
				else if(type === "HIDE"){
					dojo.removeClass(root, "modal-open");	
					dojo.removeClass("transCookiePop", "show");
					dojo.addClass("transCookiePop", "hide");									
				}
				return false;
			},
			postCreate: function() {
				var html = this.renderTmpl(popHtml,{tuiWebrootPath:tuiWebrootPath});
				dojo.place(html, this.domNode, "only");				
				dojo.parser.parse(this.domNode);				
				this.attachCustomEvents();
				
				this.isHaveCookie = "false";
				if(tuiWebrootPath == "/holiday"){
					var isHaveCookie = dojo.cookie('saveHoliday_fc');
				}
				else{
					var isHaveCookie = dojo.cookie('saveHoliday_th');
				}
				if(isHaveCookie != undefined && isHaveCookie != "undefined" && isHaveCookie != "" && isHaveCookie != '""' && this.IsLoggedIn != null && this.IsLoggedIn != "null"){
					this.isHaveCookie = "true";
				}
				//var cookie = dojo.cookie('customerCookieForLoginPage');
				if(this.isHaveCookie == "true"){
					var jsessionId = dojo.cookie('JSESSIONID');	
					
					var sessionCookie = dojo.cookie('sessionCookie');
					if(jsessionId == sessionCookie){
						this.manageWindow("SHOW");
					}
					
				}
			},
					
			attachCustomEvents: function(){
				var self = this;
				dojo.query(".title .close").connect("onclick", function(event){
					dojo.stopEvent(event);
					self.manageWindow("HIDE");
				});
				
				if(dojo.byId("transferDeleteLink") != undefined){
					var myButton = dojo.byId("transferDeleteLink");				
					 
					dojo.connect(myButton, "onclick", function(evt){
						dojo.stopEvent(evt);
						dojo.attr(dojo.byId("transferShortListHoly"), "action", "../viewShortlist/deleteWishlistOfAnonymous"+"?cache="+Math.random());
					
						dojo.xhr.post({
							form: "transferShortListHoly",
							preventCache: true,
							content: { 
								WishListId: self.wishId
							},
							timeout: 3000,
							load: function(data){
								
								if(data == "true" || data == "[]true" || data == "[] true" || data == "[]"){
									self.getShorListedHolidaysCount();
									if(tuiWebrootPath == "/holiday"){										
										dojo.cookie("saveHoliday_fc", "", { expires: -1});
									}
									else{
										dojo.cookie("saveHoliday_th", "", { expires: -1});
									}									
									self.manageWindow("HIDE");
									self.reloadThePage();
								}								
							},
							error: function(errors) {
								console.log(errors);
								self.handleBackendError(errors);
							}
						});	
					});
			    }
				
				if(dojo.byId("transferLink") != undefined){
					var myButton = dojo.byId("transferLink");
					
									
									
					dojo.connect(myButton, "onclick", function(evt){
						dojo.stopEvent(evt);
						dojo.attr(dojo.byId("transferShortListHoly"), "action", "../viewShortlist/transferWishlistToRegistered"+"?cache="+Math.random());
						/*
						var res = self.handleSessionTimeOut();
						if(!res){
						return false;
						}*/
					
						dojo.xhr.post({
							form: "transferShortListHoly",
							preventCache: true,
							content: { 
								WishListId: self.wishId
							},
							timeout: 3000,
							load: function(data){									
									
								var responseData = dojo.fromJson(data);
								console.log(responseData);
								var transferData = responseData.transferSavedHolidays;
								
								if(transferData == "true" || transferData == "[]true" || transferData == "[] true"  || transferData == "[]" || transferData.length == 0 ){									
									self.manageWindow("HIDE");
									if(tuiWebrootPath == "/holiday"){										
										dojo.cookie("saveHoliday_fc", "", { expires: -1});
									}
									else{
										dojo.cookie("saveHoliday_th", "", { expires: -1});
									}	
									self.getShorListedHolidaysCount();
									self.reloadThePage();
									
								}
								else{
								
									connect.publish("transferCookie", data);
									self.manageWindow("HIDE");
								}
								
							},
							error: function(errors) {
								console.log(errors);
								self.handleBackendError(errors);
							}
						});
					});
			
				}
				
			},
			reloadThePage:function(){
				
				var loc = document.location.toString();
				if(loc.indexOf("login") != -1){
					var url = tuiWebrootPath+"/customer-account/login";
					window.location.assign(url);
				}
				else{
					var url = tuiWebrootPath+"/viewShortlist/viewsavedholidaysPage";
					window.location.assign(url);
				}
			},
			startup: function() {			
			}
		});
		return tui.widget.customeraccount.TransferCookiePopup;
});