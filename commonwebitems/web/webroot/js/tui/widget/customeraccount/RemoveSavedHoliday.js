define ("tui/widget/customeraccount/RemoveSavedHoliday", [
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
													"tui/widget/customeraccount/CommonGetShortListHolidayCount",
													"tui/widget/customeraccount/ErrorHandling"
													
													
							    			  ], function(dojo,  cookie, query, has, validate,check, arrayUtil, domStyle, focusUtil,xhr,fx){

		dojo.declare("tui.widget.customeraccount.RemoveSavedHoliday", [tui.widget._TuiBaseWidget,tui.widget.customeraccount.CommonGetShortListHolidayCount, tui.widget.customeraccount.ErrorHandling], {
	    
		
		targetURL:null,	
		wishlistEntryId:null,
		message:null,
		count:0,
		maxListSize: 3,
		
		postCreate: function() {
			var RemoveSavedHoliday = this;			
			RemoveSavedHoliday.inherited(arguments);
			RemoveSavedHoliday.attachEventListener();
			//RemoveSavedHoliday.getShorListedHolidaysCount();
		},
    	
		removeShortListedHoliday: function (){
			var holiday = this;
			
             /*			
			var res = holiday.handleSessionTimeOut();
			if(!res){
			return false;
			}
			*/
			xhr.get({	//1			
					url: tuiWebrootPath+"/viewAnonymousShortlist/retrieveWishlistCount",				
					// The success handler
					load: function(response) {				
						if(response){ 
						    response = parseInt(response, 10);
							holiday.count = response;
							
							xhr.get({
				
							url: holiday.targetURL+"&count="+holiday.count,				
							
							// The success handler
							load: function(response) {
								
								//var obj = JSON.parse(response);
								if(response == "true"){
									var id = holiday.wishlistEntryId;
									var wipeTarget = dojo.byId(id);							
									fx.wipeOut({ node: wipeTarget }).play();
									if(dojo.byId("shortlistmessage") != undefined ){
									dojo.byId("shortlistmessage").innerHTML = holiday.message;
									}
									holiday.getShorListedHolidaysCount();
								}
							},
							// The error handler
							error: function(errors) { 
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
						holiday.handleBackendError(errors);
					},
					// The complete handler
					handle: function() {
						hasBeenSent = true;
					}
			});
			
			
		},
        
    	attachEventListener: function(){
            var holiday = this;            
            
            dojo.connect(holiday.domNode, "onclick", function(event){                
                dojo.stopEvent(event);                
                holiday.removeShortListedHoliday();
                holiday.removeFromList();
                holiday.setCompareItem();                            
            });
    	},
    	
    	removeFromList: function() {
			var widget = this;
			var compareItemsList = tuiSiteName+"compareItemsList";				
			var getCompareList = JSON.parse(localStorage.getItem(compareItemsList));
			var listLength = widget.computeLength(getCompareList);	
			var currentWishlistEntryId = widget.wishlistEntryId.split("_");
			if(listLength > 0) {
				delete getCompareList[widget.getDeleteIndex(getCompareList, currentWishlistEntryId[1])];				
				localStorage.setItem(compareItemsList, JSON.stringify(getCompareList));
				widget.bindComparePanel();
				widget.bindListLength(--listLength);
			} else {
				return false;
			}
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
		
		getDeleteIndex: function(getCompareList, removeIndex) {
			var key, count = 0;
			for(key in getCompareList) {
			  if(getCompareList.hasOwnProperty(key)) {
			    count++;
			    if(getCompareList[key].Id == removeIndex) {
			    	return key;
			    }
			  }
			}
		},
		
		bindComparePanel: function() {
			var widget = this;
			var compareItemsList = tuiSiteName+"compareItemsList";			
			var myCompareList = JSON.parse(localStorage.getItem(compareItemsList));
			var listLength = widget.computeLength(JSON.parse(localStorage.getItem(compareItemsList)));
			var panelListItem = dojo.query('#compareList li');
			widget.resteComparePanel();
			var key, i = 0;
			for(key in myCompareList) {
				if(myCompareList.hasOwnProperty(key)) {
					dojo.removeClass(panelListItem[i], "empty");
					dojo.setAttr(dojo.query('span.img img', panelListItem[i].domNode)[i], "src", myCompareList[key].Img);
					dojo.setAttr(dojo.query('span.img img', panelListItem[i].domNode)[i], "wishlistid", myCompareList[key].Id);
					dojo.setAttr(dojo.query('span.img input', panelListItem[i].domNode)[i], "value", myCompareList[key].Id);
					i++;
				}
			}
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
			var myId, currentWishlistEntryId = compareNode.wishlistEntryId.split("_");
			for(var i = 0; i < compareActiveList.length; i++){
				myId = dojo.attr(compareActiveList[i], "wishlistid");
				if(myId == currentWishlistEntryId[1]){					
					dojo.removeClass(dojo.query(".radio.active", compareActiveList[i])[0], "active");
					dojo.query(".comparestatus", compareActiveList[i])[0].innerHTML = "Add to compare";
					dojo.setAttr(dojo.query(".tooltip", compareActiveList[i])[0], "style", hide);
					dojo.removeClass(compareActiveList[i], "activated");
				}
			}			
			if(listLength == 0) {
				compareNode.setCompareToolTip(true);
			}				
			compareNode.setMaxTooltip();			
		},
		
		setCompareToolTip: function(mode) {
			var widget = this;
			if(widget.login != 'null') {
				if(dojo.byId('noHolidays') != undefined){
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
		},
		
		setMaxTooltip: function() {
			var widget = this;
			var compareItemsList = tuiSiteName+"compareItemsList";				
			var radioEnabled = dojo.query('.radio_enabled');
			var radioDisabled = dojo.query('.radio_disabled');	
			var hide = { display:"none" };
			var show = { display:"inline" };			
			var jsonList = JSON.parse(localStorage.getItem(compareItemsList));
			var listLength = widget.computeLength(jsonList);	
			if(listLength < widget.maxListSize ? false : true) {		
				widget.enableMaxTooltip(radioEnabled, radioDisabled, hide, show);
			} else {			
				widget.enableMaxTooltip(radioEnabled, radioDisabled, show, hide);
			}
		},
		
		enableMaxTooltip: function(node1, node2, mode1, mode2) {
			var widget = this;
			for(var i = 0; i < node1.length; i++) {
				if(!dojo.hasClass(node1[i], "active")) {
					dojo.setAttr(node1[i], "style", mode1);
					dojo.setAttr(node2[i], "style", mode2);
				}
			}
		}
    	
	});
	
	return tui.widget.customeraccount.RemoveSavedHoliday;
});