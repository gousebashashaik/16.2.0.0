define ("tui/widget/customeraccount/RemoveAllSavedHolidays", [
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
													"dojo/fx",
													
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
													
													
							    			  ], function(dojo,  cookie, query, has, validate,check, arrayUtil, domStyle, focusUtil,xhr,fx){

		dojo.declare("tui.widget.customeraccount.RemoveAllSavedHolidays", [tui.widget._TuiBaseWidget,tui.widget.customeraccount.CommonGetShortListHolidayCount, tui.widget.customeraccount.ErrorHandling], {
	    
		
		targetURL:null,	
		wishlistEntryId:null,
		message:null,
		count:0,
		widgetId: null,
		maxListSize: 3,
		
		postCreate: function() {
			var RemoveAllSavedHolidays = this;				
			RemoveAllSavedHolidays.attachEventListener();	
            RemoveAllSavedHolidays.inherited(arguments);           
		},
    	
		removeShortListedHoliday: function (){
			var holiday = this;	

            /*
			var res = holiday.handleSessionTimeOut();
			if(!res){
			return false;
			}
			*/
					
			xhr.get({				
					url: tuiWebrootPath+"/viewAnonymousShortlist/retrieveWishlistCount",				
					// The success handler
					load: function(response) {				
						if(response){
						
						
						//temp code
						/*
						console.log("1");
									holiday.getShorListedHolidaysCount();
									console.log("2");
									holiday.showHidePopup("DeleteAllHolidaysPopup", "hide");
									console.log("3");
									holiday.handleDisplay();
									console.log("4"); 
						*/
						//temp code
							
						    response = parseInt(response, 10);
							holiday.count = response;
							
							xhr.get({ //1
				
							url: holiday.targetURL+"&count="+holiday.count,				
							
							// The success handler
							load: function(response) {
								
								//var obj = JSON.parse(response);
								if(response == "true"){
									holiday.getShorListedHolidaysCount();
									console.log("2");
									holiday.showHidePopup("DeleteAllHolidaysPopup", "hide");
									console.log("3");
									holiday.handleDisplay();
									console.log("4"); 
								}
							},
							// The error handler
							error: function(errors) {
								console.log("one error");
								holiday.handleBackendError(errors);
								
							},
							// The complete handler
							handle: function() {
								hasBeenSent = true;
							}
						});
						}
					},
					// The error handler
					error: function(errors) { 				
						console.log("one error");
						holiday.handleBackendError(errors);
					},
					// The complete handler
					handle: function() {
						hasBeenSent = true;
					}
			});
            holiday.removeFromList();
			
			
		},
        showHidePopup:function(PopupId, type){
			var root = document.getElementsByTagName( 'html' )[0];
			console.log("one");
			if(type == "show"){
				console.log("two");
				dojo.removeClass(PopupId, "hide");
				console.log("three");
				dojo.addClass(PopupId, "show");
				console.log("four");
				dojo.addClass(root, "modal-open");
				console.log("five"+root.className);
			}
			else{
				dojo.removeClass(PopupId, "show");
				dojo.addClass(PopupId, "hide");
				dojo.removeClass(root, "modal-open");
			}
			
		},
    	attachEventListener: function(){
            var holiday = this;
			
			dojo.query(".remove_all_saved_holidays").onclick(function(){				
				holiday.showHidePopup("DeleteAllHolidaysPopup", "show");
			});
			

			dojo.query(".title .close").onclick(function(){
				holiday.showHidePopup("DeleteAllHolidaysPopup", "hide");				
			});
			if(dojo.byId("DeleteAll_No") != undefined ){
				var DeleteAll_No = dojo.byId("DeleteAll_No");
				dojo.connect(DeleteAll_No, "onclick", function(event){ 
					holiday.showHidePopup("DeleteAllHolidaysPopup", "hide");					
				});
			}
			if(dojo.byId("DeleteAll_Yes") != undefined){
				var DeleteAll_Yes = dojo.byId("DeleteAll_Yes");
				dojo.connect(DeleteAll_Yes, "onclick", function(event){
									
					holiday.removeShortListedHoliday(); 
					
					
				});
			}            
    	},
		handleDisplay: function(){
			var holiday = this;			
			var empTyMsg = '<p id="ca_alert_nudge_message" class="alert generic mid-level">You don\'t have any shortlisted holidays.</p>';
			dojo.destroy("compare-panel");						
			dojo.query(".shortlist-compare").forEach(dojo.destroy);
			dojo.query('.shortlisted-hols').attr('innerHTML',empTyMsg);
			
			
		},
		
		removeFromList: function() {
			var widget = this;
			var compareItemsList = tuiSiteName+"compareItemsList";				
			var getCompareList = JSON.parse(localStorage.getItem(compareItemsList));
			var listLength = widget.computeLength(getCompareList);	
			var currentWishlistEntryId = widget.wishlistEntryId.split("_");
			if(listLength > 0) {
				var key;
				for(key in getCompareList) {
				  if(getCompareList.hasOwnProperty(key)) {
					delete getCompareList[key];			
					localStorage.setItem(compareItemsList, JSON.stringify(getCompareList));
					widget.resteComparePanel();
					widget.bindListLength(--listLength);
				  }
				}	
			} else {
				return false;
			}
			widget.setCompareToolTip(true);
			return true;
		},
		
		computeLength: function(getCompareList) {
			var key, count = 0;
			for(key in getCompareList) {
			  if(getCompareList.hasOwnProperty(key)) {
			    count++;
			  }
			}
			return count;
		},		
				
		resteComparePanel: function() {
			var panelListItem = dojo.query('#compareList li');
			for(var i = 0; i < panelListItem.length; i++) {
				dojo.addClass(panelListItem[i], "empty");
				dojo.setAttr(dojo.query('span.img img', panelListItem[i].domNode)[i], "src", "");
				dojo.setAttr(dojo.query('span.img img', panelListItem[i].domNode)[i], "wishlistid", "");
				dojo.setAttr(dojo.query('span.img input', panelListItem[i].domNode)[i], "value", "");
			}
		},
		
		setCompareItem: function() {
			var compareNode = this;
			var compareItemsList = tuiSiteName+"compareItemsList";
			var listLength = compareNode.computeLength(JSON.parse(localStorage.getItem(compareItemsList)));
			var compareActiveList = dojo.query(".activated");
			var hide = { display:"none" };
			var imgItem = dojo.query('img', compareActiveList.domNode);
			var myId;
			for(var i = 0; i < compareActiveList.length; i++){
				myId = dojo.attr(compareActiveList[i], "wishlistid");
				if(myId == compareNode.wishlistEntryId){					
					dojo.removeClass(dojo.query(".radio.active", compareActiveList[i])[0], "active");
					dojo.query(".comparestatus", compareActiveList[i])[0].innerHTML = "Add to compare";
					dojo.setAttr(dojo.query(".tooltip", compareActiveList[i])[0], "style", hide);
					dojo.removeClass(compareActiveList[i], "activated");
				}
			}			
			if(listLength == 0) {
				compareNode.setCompareToolTip(true);
			}			
		},
		
		setCompareToolTip: function(mode) {
			var widget = this;
			if(widget.login != 'null') {
				var compareButtonEmpty = dojo.byId('noHolidays');
				var compareButtonAfter = dojo.byId('addedHoliday');	
				var compareXLoggedin = dojo.byId('compareXLoggedin');
				var compareXLoggedout = dojo.byId('compareXLoggedout');
				var hide = { display:"none" };
				var show = { display:"block" };
				if(mode) {
					dojo.setAttr(compareButtonEmpty, "style", show);
					dojo.setAttr(compareButtonAfter, "style", hide);				
					dojo.setAttr(compareButtonAfter, "submit", "false");
					dojo.setAttr(compareXLoggedout, "style", show);
					dojo.setAttr(compareXLoggedin, "style", hide);
				} else {
					dojo.setAttr(compareButtonEmpty, "style", hide);
					dojo.setAttr(compareButtonAfter, "style", show);
					dojo.setAttr(compareButtonAfter, "submit", "true");
					dojo.setAttr(compareXLoggedin, "style", show);
					dojo.setAttr(compareXLoggedout, "style", hide);
				}
			}
		},
		
		bindListLength: function(count) {
			var widget = this;
			var show = { display:"block" };
			if(widget.login == 'null') {
				dojo.setAttr(dojo.byId('compareXLoggedout2'), "style", show);
			} else {
				var compareButtonEmpty = widget.login != 'null' ? dojo.byId('compareCount') : dojo.byId('compareCountOff');
				compareButtonEmpty.innerHTML = count; 
			}
		}
				
	});
	
	return tui.widget.customeraccount.RemoveAllSavedHolidays;
});