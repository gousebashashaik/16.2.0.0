define ("tui/widget/customeraccount/removeFromCompare", [
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
        
		dojo.declare("tui.widget.customeraccount.removeFromCompare", [tui.widget._TuiBaseWidget], {		
						
		ImgId: null,
		maxListSize: 3,
		login: null,
			
		postCreate: function() {
			var compareNode = this;
			compareNode.onClickEvent();
			compareNode.inherited(arguments);
		},
		    	
		onClickEvent: function() {			
			var compareNode = this;		
			_.each(dojo.query('.close', compareNode.domNode), function (contentDom, i) {				
				dojo.connect(contentDom, "onclick", function(evt){	
					compareNode.checkCompareList(i);					
				});	
			});
		},
		
		checkCompareList: function(removeIndex) {
			var myCompareNode = this;
			var compareItemsList = tuiSiteName+"compareItemsList";			
			var getCompareList = JSON.parse(localStorage.getItem(compareItemsList));
			var listLength = myCompareNode.computeLength(getCompareList);			
			var panelListItem = dojo.query('#compareList li')[removeIndex];
			var imgItem = dojo.query('span.img img', panelListItem.domNode);
			myCompareNode.ImgId = dojo.attr(imgItem[removeIndex], "wishlistid");
			if(listLength > 0) {
				delete getCompareList[myCompareNode.getDeleteIndex(getCompareList, removeIndex)];
				localStorage.setItem(compareItemsList, JSON.stringify(getCompareList));
				myCompareNode.bindComparePanel(removeIndex);
				myCompareNode.bindListLength(--listLength);
			} else {
				return false;
			}
			return true;
		},
		
		setCompareItem: function(removeIndex) {
			var compareNode = this;
			var compareActiveList = dojo.query(".activated");
			var hide = { display:"none" };
			var imgItem = dojo.query('span.img img', compareActiveList.domNode);
			var myId;
			for(var i = 0; i < compareActiveList.length; i++){
				myId = dojo.attr(compareActiveList[i], "wishlistid");
				if(myId == compareNode.ImgId){					
					dojo.removeClass(dojo.query(".radio.active", compareActiveList[i])[0], "active");
					dojo.query(".comparestatus", compareActiveList[i])[0].innerHTML = "Add to compare";
					dojo.setAttr(dojo.query(".tooltip", compareActiveList[i])[0], "style", hide);
					dojo.removeClass(compareActiveList[i], "activated");
				}
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
		},
		
		getDeleteIndex: function(getCompareList, removeIndex) {
			var key, count = 0;
			removeIndex++;
			for(key in getCompareList) {
			  if(getCompareList.hasOwnProperty(key)) {
			    count++;
			    if(count == removeIndex) {
			    	return key;
			    }
			  }
			}
		},
		
		bindComparePanel: function(removeIndex) {
			var widget = this;
			var compareItemsList = tuiSiteName+"compareItemsList";			
			var myCompareList = JSON.parse(localStorage.getItem(compareItemsList));
			var listLength = widget.computeLength(JSON.parse(localStorage.getItem(compareItemsList)));
			var panelListItem = dojo.query('#compareList li');
			widget.setCompareItem(removeIndex);
			widget.resteComparePanel();
			var key, i = 0;
			for(key in myCompareList) {
				if(myCompareList.hasOwnProperty(key)) {
					dojo.removeClass(panelListItem[i], "empty");
					dojo.setAttr(dojo.query('span.img img', panelListItem[i].domNode)[i], "src", myCompareList[key].Img);
					dojo.setAttr(dojo.query('span.img img', panelListItem[i].domNode)[i], "wishlistid", myCompareList[key].Id);
					dojo.setAttr(dojo.query('span.img input', panelListItem[i].domNode)[i], "value", myCompareList[key].Id);
					dojo.removeClass(dojo.query('a.ensLinkTrack.closee', panelListItem[i].domNode)[i], "hide");			
					dojo.removeClass(dojo.query('span.img img', panelListItem[i].domNode)[i], "hide");
					i++;
				}
			}			
			widget.setCompareItem(removeIndex);
			if(listLength == 0) {
				widget.setCompareToolTip(true);
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
		
		setCompareToolTip: function(mode) {
			var widget = this;
			var compareButtonEmpty = dojo.byId('noHolidays');
			var compareButtonAfter = dojo.byId('addedHoliday');	
			var hide = { display:"none" };
			var show = { display:"block" };
			if(mode) {
				dojo.setAttr(compareButtonEmpty, "style", show);
				dojo.setAttr(compareButtonAfter, "style", hide);				
			} else {
				dojo.setAttr(compareButtonEmpty, "style", hide);
				dojo.setAttr(compareButtonAfter, "style", show);
			}
		},
		
		bindListLength: function(count) {
			var widget = this;
			var hide = { display:"none" };
			var show = { display:"block" };
			if(widget.login) {
				var compareButtonEmpty = widget.login != 'null' ? dojo.byId('compareCount') : dojo.byId('compareCountOff');
				compareButtonEmpty.innerHTML = count; 
			} else {
				dojo.setAttr(dojo.byId('compareXLoggedout2'), "style", show);
				dojo.setAttr(dojo.byId('compareXLoggedout'), "style", hide);
			}
		}
    	            	
	});
	
	return tui.widget.customeraccount.removeFromCompare;
});