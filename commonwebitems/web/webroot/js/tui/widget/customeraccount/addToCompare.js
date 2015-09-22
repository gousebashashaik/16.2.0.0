define ("tui/widget/customeraccount/addToCompare", [
													"dojo",
													"dojo/dom",
													"dojo/dom-attr",
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
													"tui/widget/customeraccount/UserRepository",
													"dojo/Stateful",
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
													
							    			  ], function(dojo, dom, domAttr, on, cookie, query, has, validate,check, arrayUtil, domStyle, focusUtil, domConstruct, userRepo, Stateful){
        
		dojo.declare("tui.widget.customeraccount.addToCompare", [tui.widget._TuiBaseWidget], {		
				
		wishlistEntryId: null,
		imageUrl: null,
		removeMode: null,
		itemNo: null,
		login: null,
		maxListSize: 3,
				
		postCreate: function() {
			var compareNode = this;
			compareNode.onClickEvent();
			compareNode.initCompareList();
			compareNode.inherited(arguments);
		},
		
		initCompareList: function() {
			var widget = this;
			var compareItemsList = tuiSiteName+"compareItemsList";
			if(typeof(Storage)!=="undefined") {
				var compareListItems = localStorage.getItem(tuiSiteName+"compareItemsList");
				if( compareListItems != "" && compareListItems != "{}" && compareListItems != "[]" && compareListItems != null) {		
					widget.setAvailableCompareItem();
					widget.bindComparePanel();	
					widget.bindListLength(widget.computeLength(JSON.parse(localStorage.getItem(compareItemsList))));
				} else {
					var myCompareList = { };
					localStorage.setItem(compareItemsList, JSON.stringify(myCompareList));
					widget.setCompareToolTip(true);
				}
			}
		},
    	
		onClickEvent: function() {
			var compareNode = this;
			var clickNode = dojo.query(".radio.radio_enabled", compareNode.domNode);
			clickNode.onclick(function(e){
				if(compareNode.login != 'null') {
					if(!dojo.hasClass(clickNode[0], "active")) {
						if(compareNode.addToCompareList()) {
							compareNode.setCompareItem(true);
						}
					} else {
						compareNode.removeFromList();
						compareNode.setCompareItem(false);
					}
				}	
			});
		},
		
		addToCompareList: function() {
			var myCompareNode = this;
			var compareItemsList = tuiSiteName+"compareItemsList";
			var myCompareList = {
				Id:[],
				Img:[]
			};
			var jsonList = JSON.parse(localStorage.getItem(compareItemsList));
			var listLength = myCompareNode.computeLength(jsonList);	
			if(listLength < myCompareNode.maxListSize) {
				if(myCompareNode.checkExistingItem(jsonList)) {
					myCompareList.Id = myCompareNode.wishlistEntryId;
					myCompareList.Img = myCompareNode.imageUrl;
					jsonList[myCompareNode.getCompareListKey(jsonList)] = myCompareList;
					localStorage.setItem(compareItemsList, JSON.stringify(jsonList));
					myCompareNode.bindComparePanel();
					myCompareNode.setCompareToolTip(false);
					myCompareNode.bindListLength(++listLength);
				}
			} else {
				alert("You have reached the maximum limit and You need to delete a holiday from compare panel to add another one.");
				return false;
			}
			return true;
		},
		
		checkExistingItem: function(jsonList){
			var widget = this;
			for(key in jsonList) {
				if(jsonList[key].Id == widget.wishlistEntryId) {
				   return false;
				}
			}			
			return true;
		},
		
		setCompareItem: function(mode) {
			var compareNode = this;
			var compareItemsList = tuiSiteName+"compareItemsList";
			var listLength = compareNode.computeLength(JSON.parse(localStorage.getItem(compareItemsList)));
			if(mode) {
				var clickNode = dojo.query(".radio.radio_enabled", compareNode.domNode);
				var compareNodeSet = dojo.query(".radio.radio_enabled", compareNode.domNode).closest(".compare");			
				var textNode = dojo.query(".comparestatus", compareNode.domNode);
				var tooltipNode = dojo.query("a.tooltip", compareNode.domNode);
				compareNodeSet.addClass("activated");
				clickNode.addClass("active");
				textNode[0].innerHTML = "Added -";
				tooltipNode.style("display", "inline");
				if(compareNode.login == "null"){
					tooltipNode.attr("class", "tooltip disabled" );
				}
			} else {
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
			}
			if(listLength == 0) {
				compareNode.setCompareToolTip(true);
			}				
			compareNode.setMaxTooltip();			
		},
		
		setAvailableCompareItem: function() {
			var compareNode = this;
			var compareItemsList = tuiSiteName+"compareItemsList";
			var jsonList = JSON.parse(localStorage.getItem(compareItemsList));
			var listLength = compareNode.computeLength(JSON.parse(localStorage.getItem(compareItemsList)));
			var clickNode = dojo.query(".radio_enabled");
			var compareNodeSet = dojo.query(".radio.radio_enabled", compareNode.domNode).closest(".compare");
			var compareActiveList = dojo.query(".compare");
			var myID, key, textNode, tooltipNode, currentCompareNode;
			for(key in jsonList){
				for(var i = 0; i < clickNode.length; i++) {
					myID = dojo.attr(compareActiveList[i], "wishlistid");
					if(myID == jsonList[key].Id){
						currentCompareNode = compareActiveList[i];
						textNode = dojo.query(".comparestatus", currentCompareNode);
						tooltipNode = dojo.query("a.tooltip", currentCompareNode);
						
						dojo.addClass(currentCompareNode, "activated");
						dojo.addClass(clickNode[i], "active");
						textNode[0].innerHTML = "Added -";
						tooltipNode.style("display", "inline");
						if(compareNode.login == "null"){
							tooltipNode.attr("class", "tooltip disabled" );
						}
						break;						
					} 
				}
			}	
			if(listLength > 0) {
			compareNode.setCompareToolTip(false);
			}
			compareNode.setMaxTooltip();			
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
		
		getCompareListKey: function(getCompareList) {
			var key, count = 0;
			for(key in getCompareList) {
				if(getCompareList.hasOwnProperty(key)) {
				    count = key;
				}
			}			
			return ++count;
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
					var imgSrc ="";
					imgSrc = dojo.query(".savedHolidayImg_"+myCompareList[key].Id).attr("src");
					dojo.query('span.img img', panelListItem[i].domNode)[i].src=imgSrc;
					dojo.setAttr(dojo.query('span.img img', panelListItem[i].domNode)[i], "wishlistid", myCompareList[key].Id);
					dojo.setAttr(dojo.query('span.img input', panelListItem[i].domNode)[i], "value", myCompareList[key].Id);
					dojo.removeClass(dojo.query('a.ensLinkTrack.closee', panelListItem[i].domNode)[i], "hide");			
					dojo.removeClass(dojo.query('span.img img', panelListItem[i].domNode)[i], "hide");
					i++;
				}
			}
		},
		
		resteComparePanel: function() {
			var panelListItem = dojo.query('#compareList li');
			for(var i = 0; i < panelListItem.length; i++) {
				dojo.addClass(panelListItem[i], "empty");
				dojo.addClass(dojo.query('a.ensLinkTrack.closee', panelListItem[i].domNode)[i], "hide");
				dojo.addClass(dojo.query('span.img img', panelListItem[i].domNode)[i], "hide");
				dojo.setAttr(dojo.query('span.img img', panelListItem[i].domNode)[i], "src", "");
				dojo.setAttr(dojo.query('span.img img', panelListItem[i].domNode)[i], "wishlistid", "");
				dojo.setAttr(dojo.query('span.img input', panelListItem[i].domNode)[i], "value", "");
			}
		},
		
		removeFromList: function() {
			var widget = this;
			var compareItemsList = tuiSiteName+"compareItemsList";				
			var getCompareList = JSON.parse(localStorage.getItem(compareItemsList));
			var listLength = widget.computeLength(getCompareList);	
			if(listLength > 0) {
				delete getCompareList[widget.getDeleteIndex(getCompareList, widget.wishlistEntryId)];				
				localStorage.setItem(compareItemsList, JSON.stringify(getCompareList));
				widget.bindComparePanel();
				widget.bindListLength(--listLength);
			} else {
				return false;
			}
			return true;
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
		
		computeLength: function(getCompareList) {
			var key, count = 0;
			for(key in getCompareList) {
			  if(getCompareList.hasOwnProperty(key)) {
			    count++;
			  }
			}
			return count;
		},
		
		setCompareToolTip: function(mode) {
		
			var widget = this;
			if(widget.login != 'null') {
				var compareButtonEmpty = dojo.byId('noHolidays');
				var compareButtonAfter = dojo.byId('addedHoliday');	
				//var compareXLoggedin = dojo.byId('compareXLoggedin');
				//var compareXLoggedout = dojo.byId('compareXLoggedout');
				var hide = { display:"none" };
				var show = { display:"block" };
				if(mode) {
					dojo.setAttr(compareButtonEmpty, "style", show);
					dojo.setAttr(compareButtonAfter, "style", hide);				
					dojo.setAttr(compareButtonAfter, "submit", "false");
					if(dojo.byId('compareXLoggedout') != undefined){
					var compareXLoggedout = dojo.byId('compareXLoggedout');
					dojo.setAttr(compareXLoggedout, "style", show);
					}
					if(dojo.byId('compareXLoggedin') != undefined){
					var compareXLoggedin = dojo.byId('compareXLoggedin');
					dojo.setAttr(compareXLoggedin, "style", hide);
					}
				} else {
					dojo.setAttr(compareButtonEmpty, "style", hide);
					dojo.setAttr(compareButtonAfter, "style", show);
					dojo.setAttr(compareButtonAfter, "submit", "true");
					if(dojo.byId('compareXLoggedin') != undefined){
					var compareXLoggedin = dojo.byId('compareXLoggedin');
					dojo.setAttr(compareXLoggedin, "style", show);
					}
					if(dojo.byId('compareXLoggedout') != undefined){
					var compareXLoggedout = dojo.byId('compareXLoggedout');
					dojo.setAttr(compareXLoggedout, "style", hide);
					}
				}
			}
		},
		
		bindListLength: function(count) {
			var widget = this;
			var show = { display:"block" };
			if(widget.login != 'null') {
				var compareButtonEmpty = dojo.byId('compareCount');
				compareButtonEmpty.innerHTML = count; 
			} else {
				dojo.setAttr(dojo.byId('compareXLoggedout2'), "style", show);
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
	
	return tui.widget.customeraccount.addToCompare;
});