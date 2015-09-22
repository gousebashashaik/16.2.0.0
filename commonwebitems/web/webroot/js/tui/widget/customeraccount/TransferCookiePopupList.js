define ("tui/widget/customeraccount/TransferCookiePopupList", 
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
		function(dojo, on, cookie, query, has, validate,check, arrayUtil, domStyle, focusUtil, domConstruct, popHtmlList, connect){
		dojo.declare("tui.widget.customeraccount.TransferCookiePopupList", [tui.widget._TuiBaseWidget,tui.widget.customeraccount.CommonGetShortListHolidayCount,tui.widget.mixins.Templatable, tui.widget.customeraccount.ErrorHandling], {
			/* Component custom attributes */
			
			slectedCount:0,
			countLimit:0,
		    constructor: function() {
				// add here anything that will be executed in the widget initialization.
		    	
			},
			manageWindow: function(type){
				var someNode = dojo.query(".modal").attr("class");
				var root = document.getElementsByTagName( 'html' )[0];
				
				if(type === "SHOW"){
					dojo.addClass(root, "modal-open");
					dojo.removeClass("transCookiePopList", "hide");
					dojo.addClass("transCookiePopList", "show");	
					
				}
				else if(type === "HIDE"){
					dojo.removeClass(root, "modal-open");	
					dojo.removeClass("transCookiePopList", "show");
					dojo.addClass("transCookiePopList", "hide");									
				}
			},
			postCreate: function() {
				var html = this.renderTmpl(popHtmlList,{tuiWebrootPath:tuiWebrootPath});
				dojo.place(html, this.domNode, "only");				
				dojo.parser.parse(this.domNode);				
				this.attachCustomEvents();
				
			},
			submitTheForm:function(){
				var self = this;
				dojo.query('.caholidays').on('click', function(){
					
					if(self.slectedCount > self.countLimit){
						self.slectedCount = self.countLimit;
					}
					var obj = dojo.query(this).parent(".caholidaysLi");
					
					var thisObjChk = dojo.query(obj).children(".caholidayschk");
					
					var thisObjRadio = dojo.query(obj).children(".radio");
					var checkboxStatus = thisObjChk.attr('checked');
					
					
					
					if(checkboxStatus == "false"){
						self.slectedCount++;
						
						if(self.slectedCount > self.countLimit){
							dojo.query(".maxHolidaysError").attr("innerHTML","* The maximum number of holidays you can shortlist is only " + self.countLimit);
							dojo.query(".maxHolidaysError",self.domNode).attr("class", "maxHolidaysError show error");
							dojo.query(".maxHolidaysError",self.domNode).attr("style", "color:#DF0101");
							return false;
						}
						
						thisObjChk.attr("checked",true);
						var allClasses = "caholidays radio active";
						dojo.query(thisObjRadio).attr("class",allClasses);
					}
					else{
						self.slectedCount--;
						dojo.query(".maxHolidaysError",self.domNode).attr("class", "maxHolidaysError hide error");					
						thisObjChk.attr("checked",false);
						var allClasses = "caholidays radio";
						dojo.query(thisObjRadio).attr("class",allClasses);
					}					
				});
				dojo.query(".title .close").connect("onclick", function(){
					self.manageWindow("HIDE");
				});
				
				dojo.connect(self.domNode, "onsubmit", function(event){
					var form = this;
					dojo.stopEvent(event);
					
					if(self.slectedCount == 0){
						dojo.query(".maxHolidaysError").attr("innerHTML","* Please select the holidays");
						dojo.query(".maxHolidaysError",self.domNode).attr("class", "maxHolidaysError show error");
						dojo.query(".maxHolidaysError",self.domNode).attr("style", "color:#DF0101");
						return false;
					}
					
					dojo.attr(dojo.byId("transferShortListHolyList"), "action", "../viewShortlist/transferSelectedHolidaysToShortlist"+"?cache="+Math.random());
					/*
					var res = self.handleSessionTimeOut();
					if(!res){
					return false;
					}*/
					
					dojo.xhr.post({
						form: "transferShortListHolyList",
						preventCache: true,
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
							return false;
							//self.handleBackendError(errors);
						}
					});	
						
				});
			},			
			attachCustomEvents: function(){
				var self = this;
				dojo.query(".title .close").connect("onclick", function(){
					self.manageWindow("HIDE");
				});
				
				
				    /*
					var res = self.handleSessionTimeOut();
					if(!res){
					return false;
					}
					*/
					//alert("ok1");
					connect.subscribe("transferCookie", function(message){
						console.log(message);
						/*var h = '{"transferSavedHolidays":[{"duration":7,"departureDate":"Tue 2 Dec 2014","departureAirport":"London Gatwick","wishlistEntryId":"d9cbcb68-6162-444e-b005-293c46886886","resortName":"Benidorm","outboundSectors":null,"inboundSectors":null,"accomName":"Hotel Flamingo Oasis"},{"duration":7,"departureDate":"Tue 2 Dec 2014","departureAirport":"London Luton","wishlistEntryId":"909f349f-6156-440e-adb6-c2cffa8e1c74","resortName":"Playa de las Americas","outboundSectors":null,"inboundSectors":null,"accomName":"Hotel Best Tenerife"},{"duration":7,"departureDate":"Tue 2 Dec 2014","departureAirport":"London Luton","wishlistEntryId":"3faab421-6591-4eb1-b1e5-91d2adec04fb","resortName":"Playa de las Americas","outboundSectors":null,"inboundSectors":null,"accomName":"Hotel H10 Conquistador"},{"duration":7,"departureDate":"Tue 2 Dec 2014","departureAirport":"London Luton","wishlistEntryId":"909f349f-6156-440e-adb6-c2cffa8e1c74","resortName":"Playa de las Americas","outboundSectors":null,"inboundSectors":null,"accomName":"Hotel Best Tenerife"},{"duration":7,"departureDate":"Tue 2 Dec 2014","departureAirport":"London Luton","wishlistEntryId":"909f349f-6156-440e-adb6-c2cffa8e1c74","resortName":"Playa de las Americas","outboundSectors":null,"inboundSectors":null,"accomName":"Hotel Best Tenerife"},{"duration":7,"departureDate":"Tue 2 Dec 2014","departureAirport":"London Luton","wishlistEntryId":"909f349f-6156-440e-adb6-c2cffa8e1c74","resortName":"Playa de las Americas","outboundSectors":null,"inboundSectors":null,"accomName":"Hotel Best Tenerife"},{"duration":7,"departureDate":"Tue 2 Dec 2014","departureAirport":"London Luton","wishlistEntryId":"909f349f-6156-440e-adb6-c2cffa8e1c74","resortName":"Playa de las Americas","outboundSectors":null,"inboundSectors":null,"accomName":"Hotel Best Tenerife"},{"duration":7,"departureDate":"Tue 2 Dec 2014","departureAirport":"London Luton","wishlistEntryId":"909f349f-6156-440e-adb6-c2cffa8e1c74","resortName":"Playa de las Americas","outboundSectors":null,"inboundSectors":null,"accomName":"Hotel Best Tenerife"}],"maxShortListLimit":"2"}'	*/
						var responseData = dojo.fromJson(message);
						var holidays = responseData.transferSavedHolidays;
						self.countLimit = responseData.maxShortListLimit;

						if(holidays.length > self.countLimit){
							var html = self.renderTmpl(popHtmlList,{tuiWebrootPath:tuiWebrootPath, holidays:holidays, limit:self.countLimit});

							if (html) { 
								dojo.place(html, self.domNode, "only");
								dojo.parser.parse(self.domNode);									
							}
							self.manageWindow("SHOW");
							self.submitTheForm();
						}
					});	
							
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
		return tui.widget.customeraccount.TransferCookiePopupList;
});