define("tui/widget/customeraccount/CollectSavedHolidaysList", 
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
		function(dojo, on, cookie, query, has, validate,check, arrayUtil, domStyle, focusUtil, domConstruct){
        
		successUrl:null,
		
		dojo.declare("tui.widget.customeraccount.CollectSavedHolidaysList", [tui.widget._TuiBaseWidget], {
			
			postCreate: function() {
				var compare = this;
				var compareItemsListAll = tuiSiteName+"compareItemsListAll";
				localStorage.setItem(compareItemsListAll, JSON.stringify({}));
				if(dojo.byId("addedHoliday") != undefined ){
					var node = dojo.byId("addedHoliday");		
					compare.connectOnclick(node);
				} 
				if(dojo.byId("compareXLoggedin") != undefined ){
					var nodex = dojo.byId("compareXLoggedin");		
					compare.connectOnclick(nodex);
				}			
				
			},
			bindWithHolidaysList: function() {
				var compare = this;
				var compareItemsListAll = tuiSiteName+"compareItemsListAll";
				localStorage.setItem(compareItemsListAll, JSON.stringify({}));
				
				dojo.query(".comparenowButton1").forEach(function(obj){					
					console.log("compare now"+obj.id);						
					compare.connectOnclick(obj);
				});
			},
			
			connectOnclick: function(node) {
				var widget = this;
				var compareItemsListAll = tuiSiteName+"compareItemsListAll";
				dojo.connect(node, "onclick", function(event){
					var form = this;
					var compareItemsListAll = tuiSiteName+"compareItemsListAll";
					dojo.stopEvent(event);
					var successUrl = widget.successUrl;
					console.log(successUrl);
					localStorage.setItem(compareItemsListAll, JSON.stringify({}));
					
					var childs = query("#shortlistresultsAvailable").children();						
					var len = childs.length;
					
					var jsonList = [];
					
					for(var i=0; i< len; i++){
						var fieldId = childs[i].id;	
													
						if(dojo.byId(fieldId) != undefined ){
							if(dojo.byId(fieldId).style.display != "none"){
								var idPart = fieldId.split("_");
								var ID = idPart[1];												
								var text = "";
								text = dojo.query(".compareHolidayHelpDiv_"+ID).attr("innerHTML")[0];									
								var id = ID;
								var obj = dojo.byId(fieldId);
								var img = dojo.query(".image-container img", obj).attr("src")[0];
								console.log(img);
								jsonList[i] = {"id":id,"text": text,"img":img};
								
							}
						}
					}						
					localStorage.setItem(compareItemsListAll, JSON.stringify(jsonList));
					var url = dojo.query("#submitcomparelistform").attr("action");
					window.location = url;
				});
			}
			
		});
		return tui.widget.customeraccount.CollectSavedHolidaysList;
});