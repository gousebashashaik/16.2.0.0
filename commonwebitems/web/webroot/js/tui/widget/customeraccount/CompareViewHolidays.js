define ("tui/widget/customeraccount/CompareViewHolidays", [
        													"dojo",											  	
        													"dojo/cookie",
        													"dojo/query",
        													"dojo/has",
        													"dojox/validate/web",
        													"tui/validate/check",
        													"dojo/_base/array",
        													"dojo/dom-style",
        													"dijit/focus",
        													"dojo/text!tui/widget/customeraccount/view/templates/CompareHolidayTmpl.html",
															"dojo/text!tui/widget/customeraccount/view/templates/AddHolidayTmpl.html",
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
															"tui/widget/customeraccount/ScrollerCustom",
															"tui/widget/customeraccount/ErrorHandling",
															"tui/widget/customeraccount/scrollControls",
															"tui/widget/customeraccount/AjaxLoader"
        													
        							    			  ], function(dojo,  cookie, query, has, validate,check, arrayUtil, domStyle, focusUtil, CompareHolidayTmpl,AddHolidayTmpl, xhr,vScroll,ajaxloader){

		dojo.declare("tui.widget.customeraccount.CompareViewHolidays", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable, tui.widget.customeraccount.ScrollerCustom, tui.widget.customeraccount.ErrorHandling,tui.widget.customeraccount.scrollControls,tui.widget.customeraccount.AjaxLoader], {
		
		tmpl: CompareHolidayTmpl,	
        tuiWebrootPath:tuiWebrootPath,        
		jsonData:null,
		jsonDataSingle:null,
		componentUid:null,
		onAfterTmplRender: function(){
					 
		},
		postCreate: function() {
			var compareViewComponent = this;
			
			compareViewComponent.loadTheCompareView();			
			compareViewComponent.inherited(arguments);
			
		},
		loadTheCompareView:function(){
			var compareViewComponent = this;
			compareViewComponent.loadStart();
			compareViewComponent.handleCALoader("show");
			compareViewComponent.bindLocalData();			
			compareViewComponent.loadHolidayPopup();
			compareViewComponent.bindHolidayPopup();	
			compareViewComponent.attachFilters();
			compareViewComponent.showHideAddHolidayPopup("hide");
			compareViewComponent.syncHeight(3000);
			//compareViewComponent.syncCv();
			compareViewComponent.applyScroller();
			
			window.scrollTo(0,0);
			compareViewComponent.loadStop();
			
			return false;			
			
		},
		bindCompareView:function(){
			var compareViewComponent = this;	
			compareViewComponent.loadStart();	
			compareViewComponent.loadHolidayPopup();
			compareViewComponent.bindHolidayPopup();	
			compareViewComponent.attachFilters();
			compareViewComponent.showHideAddHolidayPopup("hide");
			compareViewComponent.syncHeight(3000);
			//compareViewComponent.syncCv();
			compareViewComponent.applyScroller();
			
			window.scrollTo(0,0);
			compareViewComponent.loadStop();
			return false;
		},
		bindCompareViewSingleHoliday:function(){
			var compareViewComponent = this;
			compareViewComponent.loadStart();	
			compareViewComponent.handleCALoader("show");			
			compareViewComponent.loadHolidayPopup();			
			compareViewComponent.attachFiltersForSingleHoliday();
			compareViewComponent.showHideAddHolidayPopup("hide");
			compareViewComponent.syncHeight(3000);
			//compareViewComponent.syncCv();
			compareViewComponent.applyScroller();
			compareViewComponent.loadStop();
			return false;
		},
		imageSetHeight:function(){
			var maxHt=0;
			dojo.query(".accomImage").forEach(function(ele){
								
					var ht=dojo.position(ele).h;
					ht=parseInt(ht);
					if(ht > maxHt){
						maxHt=ht;
						console.log("before ste",maxHt);
					}
			});
            dojo.query(".accomImage").forEach(function(ele){
				    
					if(maxHt != 0){						
						ele.setAttribute("style","height:"+maxHt+"px");						
					}					
			});	
		},
		syncHeight:function(time){
			var comp=this;
			var catElements=['pinned'];
			var countElements={};
			
			dojo.query(".category-line").forEach(function(element){
				catElements.push(element.className);
			});

			catElements.forEach(function(i){ 
				countElements[i] = (countElements[i]||0)+1;				
			});
			setTimeout(function(){
				Object.keys(countElements).forEach(function(key) {
				var keyclass="."+key.split(" ").join('.');
				var maxHt=0;
				if(keyclass == ".pinned" || keyclass == ".hol"){
					var calcClass= keyclass+".calcHt";
				}
				else{
					var calcClass= keyclass+" .calcHt";
				}
				dojo.query(calcClass).forEach(function(ele){
					var ht=dojo.position(ele).h;
					ht=parseInt(ht);
					if(ht > maxHt){
						maxHt=ht;
						console.log("before ste",maxHt);
					}
				});
				
				dojo.query(calcClass).forEach(function(ele){
					if(maxHt != 0){
						ele.setAttribute("style","height:"+maxHt+"px");
						//console.log("after ste",maxHt);
					}					
				});
				
				
			});	
				comp.imageSetHeight();
			
			},time);
			
			
			
					
		},
		submitHoliday: function(){
			var compareViewComponent = this;
			if(dojo.byId("addHolidaysPopupSelectButton") != undefined){
				var node = dojo.byId("addHolidaysPopupSelectButton");
				dojo.connect(node, "onclick", function(event){					
					dojo.stopEvent(event);					
					console.log("button clicked");
					if(dojo.byId("currentWishListEntryIdHidden") != undefined){
						//add holiday
						if(dojo.byId("currentWishListEntryIdHidden").value == ""){
							var wishlistEntryId = dojo.byId("AddHolidayId").value;
							if(wishlistEntryId){
								compareViewComponent.addToCompareList(wishlistEntryId);
								compareViewComponent.loadTheCompareView();
							}
						}
						else if(dojo.byId("currentWishListEntryIdHidden").value != ""){
							var toWishlistEntryId = dojo.byId("AddHolidayId").value;
							var fromWishlistEntryId = dojo.byId("currentWishListEntryIdHidden").value
							if(toWishlistEntryId){
								compareViewComponent.swapCompareList(fromWishlistEntryId,toWishlistEntryId);
								compareViewComponent.loadTheCompareView();
							}
						}
					
					}
				});
			}
			
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
		swapCompareList:function(fromId, toId){
			var myCompareNode = this;
			var compareItemsList = tuiSiteName+"compareItemsList";
			var myCompareList = {
				Id:[],
				Img:[]
			};
			var jsonList = JSON.parse(localStorage.getItem(compareItemsList));
			var listLength = myCompareNode.computeLength(jsonList);			
			if(listLength <= 3) {
				_.forEach(jsonList, function (item, i) {
                    if(item.Id == fromId){
						jsonList[i].Id = toId;
						jsonList[i].Img = "";
					}					
				});				
				localStorage.setItem(compareItemsList, JSON.stringify(jsonList));	
				return true;
			}
			return false;
		},
		addToCompareList:function(wishlistEntryId){
			var myCompareNode = this;
			var compareItemsList = tuiSiteName+"compareItemsList";
			var myCompareList = {
				Id:[],
				Img:[]
			};
			var jsonList = JSON.parse(localStorage.getItem(compareItemsList));
			var listLength = myCompareNode.computeLength(jsonList);			
			if(listLength < 3) {
				myCompareList.Id = wishlistEntryId;
				myCompareList.Img = "";				
				jsonList[myCompareNode.getCompareListKey(jsonList)] = myCompareList;
				localStorage.setItem(compareItemsList, JSON.stringify(jsonList));	
				return true;
			}
			return false;
		},
		handleCALoader:function(status){			
			dojo.query(".CAloadingResults").attr("class", "CAloadingResults "+status);			
		},
		startup:function(){
			var compareViewComponent = this;
			compareViewComponent.handleCALoader("hide");
		},
		loadHolidayPopup: function(){
			var compareViewComponent = this; console.log("loadHolidayPopup 1");
			var holidaysListOptionsJson = compareViewComponent.getSelectOptions();  console.log("loadHolidayPopup 2");
			var html = compareViewComponent.renderTmpl(AddHolidayTmpl,{tuiWebrootPath:tuiWebrootPath,options:holidaysListOptionsJson,componentUid: compareViewComponent.componentUid}); 
			 console.log("loadHolidayPopup 3");
            if (html) {				
                dojo.place(html, compareViewComponent.domNode, "last");
                //dojo.parser.parse(compareViewComponent.domNode);				
            }			
		},
		getSelectOptions:function(){
			var compareViewComponent = this;
			var compareItemsList = tuiSiteName+"compareItemsList";
			var compareItemsListAll = tuiSiteName+"compareItemsListAll";
			var seletedHolidaysString = "";
            seletedHolidaysString = localStorage.getItem(compareItemsList);
			var seletedHolidaysList = JSON.parse(seletedHolidaysString);			
			var seletedLen = compareViewComponent.computeLength(JSON.parse(seletedHolidaysString));
			
			
			var holidaysListAll = JSON.parse(localStorage.getItem(compareItemsListAll));			
			var listLength = compareViewComponent.computeLength(JSON.parse(localStorage.getItem(compareItemsListAll)));	
			
			var jsonList = [];						
			
			for(var i=0; i< listLength; i++){
				if(holidaysListAll[i] != undefined){
				var text = holidaysListAll[i].text; 
				var id = holidaysListAll[i].id;
				var img = holidaysListAll[i].img;
				if(seletedHolidaysString.indexOf(id) == -1){
				jsonList[i] = {"id":id,"text": text,"img":img};
				}
				}
			}
			return jsonList;
		},
		syncCv:function(){
			dojo.query('.holiday-info>li.empty').style('height','');
			var cvht=[];
			dojo.query('.holiday-info').forEach(function(node){
				var ht=dojo.position(node).h;
				cvht.push(parseInt(ht));
			});
			
			var maxht=Math.max.apply(null, cvht);
			console.log("maxht:",maxht);
			dojo.query('.holiday-info>li.empty').style('height',maxht+"px");		
		},
		attachFilters: function(){
		    var compareViewComponent = this;		
			dojo.query("#compare-view .compare-keyfacts").onclick(function(event){
				console.log("Event:"+event);
				compareViewComponent.enableCompareFilters("compare-keyfacts");
				compareViewComponent.enableFacilitiesTitles("category-title-list keyfacts");
				compareViewComponent.enableFacilities("keyfacts");
				compareViewComponent.syncHeight(0);
				//compareViewComponent.syncCv();
				compareViewComponent.vScrollCompare();
			});
			dojo.query("#compare-view .compare-facilities").onclick(function(){				
				compareViewComponent.enableCompareFilters("compare-facilities");
				compareViewComponent.enableFacilitiesTitles("category-title-list facilities");
				compareViewComponent.enableFacilities("facilities");
				compareViewComponent.syncHeight(0);
				//compareViewComponent.syncCv();
				compareViewComponent.vScrollCompare();
			});
			dojo.query("#compare-view .compare-weather").onclick(function(){				
				compareViewComponent.enableCompareFilters("compare-weather");
				compareViewComponent.enableFacilitiesTitles("category-title-list weather");
				compareViewComponent.enableFacilities("weather");
				compareViewComponent.syncHeight(0);
				//compareViewComponent.syncCv();
				compareViewComponent.vScrollCompare();
			});
			//mobile
			dojo.query("#compare-view .addHolidaysPopupSelectMobile select").onchange(function(){
				
				dojo.query("#compare-view .addHolidaysPopupSelectMobile .text").attr('innerHTML',this.value);				
				
				switch(this.value){
					case "keyfacts":
						compareViewComponent.enableCompareFilters("compare-keyfacts");
						compareViewComponent.enableFacilitiesTitles("category-title-list keyfacts");
						compareViewComponent.enableFacilities("keyfacts");
						compareViewComponent.syncHeight(0);
						//compareViewComponent.syncCv();
						compareViewComponent.vScrollCompare();
					break;
					case "facilities":
						compareViewComponent.enableCompareFilters("compare-facilities");
						compareViewComponent.enableFacilitiesTitles("category-title-list facilities");
						compareViewComponent.enableFacilities("facilities");
						compareViewComponent.syncHeight(0);
						//compareViewComponent.syncCv();
						compareViewComponent.vScrollCompare();
					break;					
					case "weather":
						compareViewComponent.enableCompareFilters("compare-weather");
						compareViewComponent.enableFacilitiesTitles("category-title-list weather");
						compareViewComponent.enableFacilities("weather");
						compareViewComponent.syncHeight(0);
						//compareViewComponent.syncCv();
						compareViewComponent.vScrollCompare();
					break;
				
				}				
			});
			
			compareViewComponent.removeHoliday();
			compareViewComponent.submitHoliday();
		
		},
		attachFiltersForSingleHoliday: function(){
		    var compareViewComponent = this;		
			dojo.query("#compare-view .compare-keyfacts").onclick(function(event){
				console.log("Event:"+event);
				compareViewComponent.enableCompareFilters("compare-keyfacts");
				compareViewComponent.enableFacilitiesTitles("category-title-list keyfacts");
				compareViewComponent.enableFacilities("keyfacts");
			});
			dojo.query("#compare-view .compare-facilities").onclick(function(){				
				compareViewComponent.enableCompareFilters("compare-facilities");
				compareViewComponent.enableFacilitiesTitles("category-title-list facilities");
				compareViewComponent.enableFacilities("facilities");
			});
			dojo.query("#compare-view .compare-weather").onclick(function(){				
				compareViewComponent.enableCompareFilters("compare-weather");
				compareViewComponent.enableFacilitiesTitles("category-title-list weather");
				compareViewComponent.enableFacilities("weather");
			});
			//mobile
			dojo.query("#compare-view .addHolidaysPopupSelectMobile select").onchange(function(){
				
				dojo.query("#compare-view .addHolidaysPopupSelectMobile .text").attr('innerHTML',this.value);				
				
				switch(this.value){
					case "keyfacts":
						compareViewComponent.enableCompareFilters("compare-keyfacts");
						compareViewComponent.enableFacilitiesTitles("category-title-list keyfacts");
						compareViewComponent.enableFacilities("keyfacts");
					break;
					case "facilities":
						compareViewComponent.enableCompareFilters("compare-facilities");
						compareViewComponent.enableFacilitiesTitles("category-title-list facilities");
						compareViewComponent.enableFacilities("facilities");
					break;					
					case "weather":
						compareViewComponent.enableCompareFilters("compare-weather");
						compareViewComponent.enableFacilitiesTitles("category-title-list weather");
						compareViewComponent.enableFacilities("weather");
					break;
				
				}				
			});
		
		},
		bindHolidayPopup: function(){
		    
			var compareViewComponent = this;
			//compareViewComponent.preSelectHoliday();
			dojo.query(".title .close").onclick(function(event){				
				compareViewComponent.showHideAddHolidayPopup("hide");
			});
			
			dojo.query(".empty .button").onclick(function(event){	
				//console.log("popup show");					
				compareViewComponent.showHideAddHolidayPopup("show");
				compareViewComponent.preSelectHoliday();
			});
			dojo.query(".holiday-info .swap").onclick(function(){	
				//console.log("popup show");				
				compareViewComponent.showHideAddHolidayPopup("show");				
				compareViewComponent.preSelectHoliday(this);
			});
					
            
			
		},
		preSelectHoliday: function(obj){
			var compareViewComponent = this;
			var currentHolidayId = "";
			if(obj){
				currentHolidayId = dojo.query(".swapAnchor", obj).attr("id");
				var currentHolidayName = dojo.query(".swapAnchor", obj).attr("name");
				
			}	
					
			query('.addHolidaysPopupSelect').forEach(function(selectDiv) {
				var target = query('span', selectDiv)[0];
				var select = query('select', selectDiv)[0];
				
				console.log("ONE");
				
				select.value = "";
				target.innerHTML = "select holiday";
				if(dojo.byId("currentWishListEntryIdHidden") != undefined){
				dojo.query("#currentWishListEntryIdHidden").attr("value",currentHolidayId);
				}
				if (target && select) {
					console.log("FOUR");
					dojo.connect(select, 'onchange', function(e) { 
						
						target.innerHTML = e.target.options[e.target.selectedIndex].text;
						console.log("FIVE");
					});
				}
				
			});
			
			//TODO: for mobile filter: below code
			query('.addHolidaysPopupSelectMobile').forEach(function(selectDiv) {
				
			});
		},
		showHideAddHolidayPopup:function(type){
			var root = document.getElementsByTagName( 'html' )[0];
			if(type == "show"){
				dojo.removeClass("GotoCompareAddHoliday", "hide");
				dojo.addClass("GotoCompareAddHoliday", "show");
				dojo.addClass(root, "modal-open");
			}
			else{
				dojo.removeClass("GotoCompareAddHoliday", "show");
				dojo.addClass("GotoCompareAddHoliday", "hide");
				dojo.removeClass(root, "modal-open");
			}
		},
		removeHoliday: function(){
			var compareViewComponent = this;
			
			dojo.query(".removeAnchor").onclick(function(event){				
				dojo.stopEvent(event);
				var obj = this;
				var currentHolidayId = dojo.query(obj).attr("id")[0];
				var currentHolidayName = dojo.query(obj).attr("name")[0];
				var expCurrentHolidayId = currentHolidayId.split("_");
				var expCurrentHolidayName = currentHolidayName.split("_");
				
				var currentHolidayId = expCurrentHolidayId[1];
				var currentHolidayName = expCurrentHolidayName[1];
				
				//update localStorage
				compareViewComponent.removeFromList(currentHolidayId);								
				compareViewComponent.loadTheCompareView();
				//compareViewComponent.syncCv();				
			});
			
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
		removeFromList: function(wishlistEntryId) { 
			var widget = this;
			var compareItemsList = tuiSiteName+"compareItemsList";			
			var getCompareList = JSON.parse(localStorage.getItem(compareItemsList));
			var listLength = widget.computeLength(getCompareList);
			console.log(listLength);
			if(listLength > 0) {
				delete getCompareList[widget.getDeleteIndex(getCompareList, wishlistEntryId)];
                			
				localStorage.setItem(compareItemsList, JSON.stringify(getCompareList));
				console.log("removed:"+wishlistEntryId);
			} else {
				return false;
			}
			return true;
		},
		enableCompareFilters: function(currentFilter){	
		
			dojo.query("#compare-view .compare-keyfacts").attr("class","compare-keyfacts activex");
			dojo.query("#compare-view .compare-keyfacts span").attr("class","hide");
			dojo.query("#compare-view .compare-facilities").attr("class","compare-facilities activex");
			dojo.query("#compare-view .compare-facilities span").attr("class","hide");
			dojo.query("#compare-view .compare-weather").attr("class","compare-weather activex");
			dojo.query("#compare-view .compare-weather span").attr("class","hide");
			var activeFilter = "#compare-view ."+currentFilter+"";
			var activeClass = currentFilter+" active";
			dojo.query(activeFilter).attr("class",activeClass);	
			
			var activeFilterr = "#compare-view ."+currentFilter+" span";
			var activeClasss = currentFilter+" show";
			dojo.query(activeFilterr).attr("class",activeClasss);	
			
		},
		enableFacilitiesTitles: function(currentFilter){
			
			dojo.query("#compare-view .category-title-list.keyfacts").attr("class","category-title-list keyfacts activex");
			dojo.query("#compare-view .category-title-list.facilities").attr("class","category-title-list facilities activex");
			dojo.query("#compare-view .category-title-list.weather").attr("class","category-title-list weather activex");
			//console.log("currentFilter:"+currentFilter); //category-title-list keyfacts
			
			var strCurrentFilter = "#compare-view ."+currentFilter.split(" ").join(".");
			//console.log("strCurrentFilter new:",strCurrentFilter);
			var setCurrentFilter = currentFilter+" active"
			dojo.query(strCurrentFilter).attr("class",setCurrentFilter);
			
			
						
		},
		enableFacilities: function(currentFilter){
			
			dojo.query("#compare-view .category-container .keyfacts").attr("class","keyfacts opnex");
			dojo.query("#compare-view .category-container .facilities").attr("class","facilities opnex");
			dojo.query("#compare-view .category-container .weather").attr("class","weather opnex");
			var activeFilter = "#compare-view .category-container ."+currentFilter+"";
			var activeClass = currentFilter+" open";
			dojo.query(activeFilter).attr("class",activeClass);			
		},

		
		bindLocalData: function() {
			var widget = this;
			var compareItemsList = tuiSiteName+"compareItemsList";
			
			var jsonList = JSON.parse(localStorage.getItem(compareItemsList));
			var listLength = widget.computeLength(jsonList);
			var compareholidays = [];
			var key, i = 0;
			
			for(key in jsonList) {
				if(jsonList[key].Id){
					compareholidays[i] = jsonList[key].Id;
					i++;
				}
			}			
			if(compareholidays.length > 0){
				console.log("before submit");
				xhr.get({				
					url: tuiWebrootPath+"/viewShortlist/compareView",						
					preventCache: true,						
					content: {"compareholidays": compareholidays},  
                    start:function(){
                    	console.log("kaka loading");
                    },
					load: function(response) {
						console.log("inside load");
						//widget.handleSessionTimeOut(response);
					},					
					error: function(errors) {
						console.log("inside errors");
						widget.handleBackendError(errors);
						
					},						
					handle: function(response) {
						
						console.log("inside handle");
						hasBeenSent = true;
						if(response == ""){
							response="[]"
						}
						if(response){
							
							
							var newHolidays = dojo.fromJson(response);
							var len = newHolidays.length;
							
							var hotelAndVilla = 0;
							var onlyHotel = 0;
							var onlyVilla = 0;
							
							for(var k = 0; k < len ; k++ ){
							
								newHolidays["availableRoomsToolTipText"] = "We\\'ve almost sold out of the room type that\\'s included in this holiday price. Other room types may be available on the Options page, but the cost of these might be higher. We may occasionally put more rooms on sale at a later date";
							
								if(newHolidays[k].accomViewData.holidayFacilities){
									onlyHotel = onlyHotel + 1;
								}
								if(newHolidays[k].accomViewData.villaFacilities){
									onlyVilla = onlyVilla+ 1;
								}

								if(newHolidays[k]["brandType"] == "TH"){
									newHolidays[k]["brandType"] = "destinations";
									newHolidays[k]["domain"] = "TH";
									newHolidays[k]["brandTypeDomain"] = "destinations";
								}
								else if(newHolidays[k]["brandType"] == "FC"){
									newHolidays[k]["brandType"] = "holiday";
									newHolidays[k]["domain"] = "FC";
									newHolidays[k]["brandTypeDomain"] = "holiday";
								}
								if(newHolidays[k]["brandType"] == "FC_TH" || newHolidays[k]["brandType"] == "TH_FC"){
									if(tuiSiteName == "firstchoice"){
										newHolidays[k]["brandTypeDomain"] = "holiday";
										newHolidays[k]["brandType"] = "holiday";
									}	
									if(tuiSiteName == "thomson"){
										newHolidays[k]["brandTypeDomain"] = "destinations";
										newHolidays[k]["brandType"] = "holiday";
									}
								}								
							}
							if(onlyHotel > 0 && onlyVilla > 0){
								hotelAndVilla = hotelAndVilla + 1;
							}
							
							
							var emptyRangeFlag = "";
							if(len < 3){
								var emptyRangeFlag = "true";
							}
							
							var compareItemsListAll = tuiSiteName+"compareItemsListAll";
							var holidaysListAll = JSON.parse(localStorage.getItem(compareItemsListAll));			
							var listLength = widget.computeLength(JSON.parse(localStorage.getItem(compareItemsListAll)));	
							var lenWithoutNull = 0;
							for(var k = 0; k <= listLength; k++){
								if(holidaysListAll[k] != null){
								lenWithoutNull += 1;
								}
							}
							
							
							
			                if(lenWithoutNull <= len){
							var emptyRangeFlag = "";
							}
							var swapFlag = "";
							if(lenWithoutNull > 3){
							var swapFlag = "true";
							}
							var noOfEmptyHolidays = "";
							if(len < 3){
								var ln = 3 - len;
								for(var k = 1; k <= ln; k++ ){
									noOfEmptyHolidays += "x";
								}
							}
							console.log("listLength"+listLength+", len:"+len+" ,emptyRangeFlag:"+emptyRangeFlag+", swapFlag:"+swapFlag+", noOfEmptyHolidays:"+noOfEmptyHolidays+", hotelAndVilla: "+hotelAndVilla+", onlyHotel: "+onlyHotel+", onlyVilla:"+onlyVilla);
							var html = widget.renderTmpl(CompareHolidayTmpl,{tuiWebrootPath:tuiWebrootPath,holidays: newHolidays, emptyRange: emptyRangeFlag, noOfEmptyHolidays: noOfEmptyHolidays,swapFlag:swapFlag, hotelAndVilla: hotelAndVilla, onlyHotel: onlyHotel, onlyVilla:onlyVilla,componentUid: widget.componentUid}); 
			
							console.log("two");
							if (html) {
								console.log("three");
								dojo.place(html, widget.domNode, "only");
								dojo.parser.parse(widget.domNode);
								console.log("four");
								widget.bindCompareView();
								
								if(noOfEmptyHolidays == "xxx"){
									var backLink = '<a href="'+tuiWebrootPath+'/viewShortlist/viewsavedholidaysPage"><i class="caret link"></i>Back to your Saved Holidays</a>';
									dojo.query(".right-panel.compare-types").attr("innerHTML",backLink);
									dojo.query("#controls").attr("class","hide");			
								}
								dojo.query("#compare-view .category-line.reviews .b.calcHt img").attr("style","width:auto !important");
								
							}		
						}
					}
				});
			}
			else if(compareholidays.length == 0){
				var noOfEmptyHolidays = "xxx";
				var html = widget.renderTmpl(CompareHolidayTmpl,{tuiWebrootPath:tuiWebrootPath,holidays: [], emptyRange: true,noOfEmptyHolidays: noOfEmptyHolidays, swapFlag:true,componentUid: widget.componentUid}); 

				console.log("two");
				if (html) {
					console.log("three");
					dojo.place(html, widget.domNode, "only");
					dojo.parser.parse(widget.domNode);
					console.log("four");
					widget.bindCompareViewSingleHoliday();
					window.scrollTo(0,0);
					
					
				}
                var backLink = '<a href="'+tuiWebrootPath+'/viewShortlist/viewsavedholidaysPage"><i class="caret link"></i>Back to your Saved Holidays</a>';
				dojo.query(".right-panel.compare-types").attr("innerHTML",backLink);
				dojo.query("#controls").attr("class","hide");
				
			}
			
			
			
			//temp code
		    /*
			var response = '[{"tripadvisorData":{"logo":null,"ratingBar":"http://www.tripadvisor.com/img/cdsi/img2/ratings/traveler/4.0-11064-1.gif","averageRating":4,"reviewsCount":433,"userReviewsCount":0,"reviewsUrl":null},"paxViewData":{"noOfAdults":2,"noOfChildren":0,"noOfInfants":0,"noOfSeniors":0,"childAges":[],"paxComposition":"2 adults"},"accomViewData":{"accomCode":"003943","roomViewData":[{"description":"2 Bedroom apartment","roomCode":"VF032","roomType":null,"roomTitle":null,"quantity":1,"noOfAdults":2,"noOfChildren":0,"noOfInfants":0,"totalChildrenCount":0,"childAges":[],"roomAllocationImage":"","price":600,"currencyAppendedPrice":"£600.00","included":true,"sellingCode":null,"packageId":null,"roomImage":null,"minOccupancy":0,"maxOccupancy":0,"roomName":null,"roomFeatures":null,"limitedAvailability":false,"limitedAvailabilityText":null,"selected":false,"perPersonPrice":null,"differencePrice":"","shouldDisplay":false,"defaultRoom":false}],"accomName":"Talayot Apartments","accomImageUrl":"http://newmedia.thomson.co.uk/live/vol/2/87984ef53e86fccc90a80fa93afb502009e235fb/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","destinationName":"Menorca","resortName":"Calan Forcat","rating":"3","boardBasisName":"Self Catering","noOfRooms":0,"featureCodesAndValues":{"usps":["Peaceful setting","6 swimming pools","Well-equipped apartments"]},"productRanges":[{"code":"HPD","name":null,"url":null,"description":null,"supercategories":null,"benefits":null,"usps":null,"imageDataList":null,"mediaData":null,"featureCodesAndValues":{"name":["Thomson Handpicked"],"strapline":["Only from Thomson"]},"productUsps":null,"endecaAccommodationFilterList":null,"picture":null,"pictureUrl":"http://newmedia.thomson.co.uk/live/vol/2/da4c69f151f5a052efebf996168e732cb7c907da/658x370/web/TH_GEN_11_F1835_R_fa46c1cbd619a7a593ea27aaef7c6b54ec04c824.jpg","galleryImages":[]}],"summaryRoomViewData":[{"roomCode":"VF032","description":"2 Bedroom apartment × 1","quantity":1,"price":600,"currencyAppendedPrice":"£600.00","included":true}],"differentiatedProduct":true,"differentiatedType":"Thomson Handpicked","differentiatedCode":"HPD","villaFacilities":null,"sizeInformation":null,"holidayFacilities":["Wi-Fi in public areas","Kids club","Evening entertainment programme"],"averageRainfall":87,"averageTemperature":23,"locationInformation":{"locationInformation":["Set in rockery gardens of cacti and palms in a lively area between the sandy cove of Calan Blanes and the resort centre of Calan Forcat? both 800m away","Bars? restaurants and shops in resort? as well as Calan Forcats small beach and picturesque inlet"]},"hotelRoomsAndFloors":{"noOfFloors":["All blocks have ground floor and first floor only"]}},"flightViewData":{"duration":7,"outboundSectors":[{"arrivalAirport":{"code":"MAH","name":"Menorca"},"departureAirport":{"code":"BHX","name":"Birmingham"},"schedule":{"arrTime":"10:35","depTime":"07:15","arrivalDate":"Wed 8 Oct 2014","departureDate":"Wed 8 Oct 2014","timeOfFlight":"","departureDateInMiliSeconds":"Oct 08, 2014 01:00:00 AM","arrivalDateInMiliSeconds":"Oct 08, 2014 01:00:00 AM","slot":null,"flightOffsetDays":0,"flightOffsetDaysSummary":null},"jnrDuration":"","eqmtDescription":"","carrierCode":""}],"inboundSectors":[{"arrivalAirport":{"code":"BHX","name":"Birmingham"},"departureAirport":{"code":"MAH","name":"Menorca"},"schedule":{"arrTime":"13:20","depTime":"11:50","arrivalDate":"Wed 15 Oct 2014","departureDate":"Wed 15 Oct 2014","timeOfFlight":"","departureDateInMiliSeconds":"Oct 15, 2014 01:00:00 AM","arrivalDateInMiliSeconds":"Oct 15, 2014 01:00:00 AM","slot":null,"flightOffsetDays":0,"flightOffsetDaysSummary":null},"jnrDuration":"","eqmtDescription":"","carrierCode":""}],"highlights":[],"dreamLiner":false},"priceBreakdownViewData":[{"description":"Basic Holiday","price":600,"currencyAppendedPrice":"£600"},{"description":"Options & extras (total)","price":2,"currencyAppendedPrice":"£2"},{"description":"Online Discount","price":140,"currencyAppendedPrice":"- £140"},{"description":"Total Price","price":602,"currencyAppendedPrice":"602"}],"roundUpTotalCost":null,"pricePerPerson":301,"onlineDiscount":140,"currencyAppendedPricePerPerson":"£301 per person","currencyAppendedRoundUpTotalCost":"602","currencyAppendedOnlineDiscount":"Includes £140.0 discount","selected":false,"galleryImages":[{"code":"87984ef53e86fccc90a80fa93afb502009e235fb","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/87984ef53e86fccc90a80fa93afb502009e235fb/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"53adc58c1c8a466996a63eafcd595f6223606af7","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/53adc58c1c8a466996a63eafcd595f6223606af7/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"1-bedroom apartment","mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"038dd5522526be88c3b01eb8f85e41f3f4f639de","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/038dd5522526be88c3b01eb8f85e41f3f4f639de/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"Childrens splash pool","mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"2e6fd794000593871ea442cb17ea8a9ee4c97e97","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/2e6fd794000593871ea442cb17ea8a9ee4c97e97/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"094d5677077d00f9a9f8d98c1839e468b17d5092","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/094d5677077d00f9a9f8d98c1839e468b17d5092/658x370/web/TH_FAM_CLB_09_F111_b8a87748d618a86ccd234474713f20b02b9fce6c.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"061e3f84f2ee093187b77af1644415b225367f66","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/061e3f84f2ee093187b77af1644415b225367f66/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.JPG","description":null,"mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"c8f15afec492101540cfcad046ff69d24a933f0f","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/c8f15afec492101540cfcad046ff69d24a933f0f/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"2-bedroom apartment","mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"974193aca0de05ef0acd0648ee3961733d5aa783","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/974193aca0de05ef0acd0648ee3961733d5aa783/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"Restaurant and pizzeria","mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"0a756faad952191a19b5f99c191581ceec1d3b54","mainSrc":"http://newmedia.thomson.co.uk/live/vol/0/0a756faad952191a19b5f99c191581ceec1d3b54/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCAT.jpg","description":"Calan Forcat Beach, Menorca","mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"e727b87de3c0c144426a8f3267820515597a50d3","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/e727b87de3c0c144426a8f3267820515597a50d3/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"Childrens paddling pool","mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"2bc2df113776d7f07a4a637a58f23e1806c66c03","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/2bc2df113776d7f07a4a637a58f23e1806c66c03/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"Bar","mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"71d9d146452cb21983f3b0198e313e88cdcc948f","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/71d9d146452cb21983f3b0198e313e88cdcc948f/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.JPG","description":null,"mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"286f8f9f35933da97a101220d057073d6ea647e6","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/286f8f9f35933da97a101220d057073d6ea647e6/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"b372cc0a272aad4ebaf136d87ee2fd95dfcd28ef","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/b372cc0a272aad4ebaf136d87ee2fd95dfcd28ef/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"Childrens splash pool","mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"21f52f881d0aa7913e9ec76e46c6ccf22e541eae","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/21f52f881d0aa7913e9ec76e46c6ccf22e541eae/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"aa6555b95328fe3c21087de75aae8881ea117293","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/aa6555b95328fe3c21087de75aae8881ea117293/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"ad3ff4b4628c2aa53be51ce71a8467ca39f5a071","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/ad3ff4b4628c2aa53be51ce71a8467ca39f5a071/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"Childrens splash pool","mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"09b74250360b58217d903c5eae73bafe2c3ffb9f","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/09b74250360b58217d903c5eae73bafe2c3ffb9f/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"1aff3445788b107e9bd2645e4754409e8e0efc72","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/1aff3445788b107e9bd2645e4754409e8e0efc72/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"Childrens splash pool","mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"4e16e13b0aec588c8a86fe3b5b74a81fc94a930b","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/4e16e13b0aec588c8a86fe3b5b74a81fc94a930b/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"2-bedroom apartment","mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"69dc42a4a98978e1b4cad0e5103d7ade3dbd2d3d","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/69dc42a4a98978e1b4cad0e5103d7ade3dbd2d3d/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"9102da59853a169f349bca5f72d993dc91f11f5d","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/9102da59853a169f349bca5f72d993dc91f11f5d/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"960a655664ea21f0185d374a8ad71037423597c1","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/960a655664ea21f0185d374a8ad71037423597c1/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"095af14ee3f539f43059b59624f2aedda5b85a73","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/095af14ee3f539f43059b59624f2aedda5b85a73/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"9ab59260770be0c4a4395ff820649035b5e67e61","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/9ab59260770be0c4a4395ff820649035b5e67e61/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"1-bedroom apartment","mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"9b4f53e11427115fded05277a459e88d4ea96c4b","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/9b4f53e11427115fded05277a459e88d4ea96c4b/658x370/web/TH_FAM_CLB_10_F013_f4a7d2b6af626efa63dc99b8e6e000fff29fa571.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"b9d9dc79f4f853c3a61fcf07963a36f8e3af17d0","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/b9d9dc79f4f853c3a61fcf07963a36f8e3af17d0/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"efcce23b2c2b90b1e80bc4f892bb042686b3fe36","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/efcce23b2c2b90b1e80bc4f892bb042686b3fe36/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"87984ef53e86fccc90a80fa93afb502009e235fb","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/87984ef53e86fccc90a80fa93afb502009e235fb/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"53adc58c1c8a466996a63eafcd595f6223606af7","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/53adc58c1c8a466996a63eafcd595f6223606af7/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"1-bedroom apartment","mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"038dd5522526be88c3b01eb8f85e41f3f4f639de","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/038dd5522526be88c3b01eb8f85e41f3f4f639de/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"Childrens splash pool","mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"2e6fd794000593871ea442cb17ea8a9ee4c97e97","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/2e6fd794000593871ea442cb17ea8a9ee4c97e97/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"094d5677077d00f9a9f8d98c1839e468b17d5092","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/094d5677077d00f9a9f8d98c1839e468b17d5092/232x130/web/TH_FAM_CLB_09_F111_b8a87748d618a86ccd234474713f20b02b9fce6c.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"061e3f84f2ee093187b77af1644415b225367f66","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/061e3f84f2ee093187b77af1644415b225367f66/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.JPG","description":null,"mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"c8f15afec492101540cfcad046ff69d24a933f0f","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/c8f15afec492101540cfcad046ff69d24a933f0f/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"2-bedroom apartment","mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"974193aca0de05ef0acd0648ee3961733d5aa783","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/974193aca0de05ef0acd0648ee3961733d5aa783/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"Restaurant and pizzeria","mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"0a756faad952191a19b5f99c191581ceec1d3b54","mainSrc":"http://newmedia.thomson.co.uk/live/vol/0/0a756faad952191a19b5f99c191581ceec1d3b54/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCAT.jpg","description":"Calan Forcat Beach, Menorca","mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"e727b87de3c0c144426a8f3267820515597a50d3","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/e727b87de3c0c144426a8f3267820515597a50d3/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"Childrens paddling pool","mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"2bc2df113776d7f07a4a637a58f23e1806c66c03","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/2bc2df113776d7f07a4a637a58f23e1806c66c03/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"Bar","mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"71d9d146452cb21983f3b0198e313e88cdcc948f","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/71d9d146452cb21983f3b0198e313e88cdcc948f/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.JPG","description":null,"mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"286f8f9f35933da97a101220d057073d6ea647e6","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/286f8f9f35933da97a101220d057073d6ea647e6/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"b372cc0a272aad4ebaf136d87ee2fd95dfcd28ef","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/b372cc0a272aad4ebaf136d87ee2fd95dfcd28ef/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"Childrens splash pool","mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"21f52f881d0aa7913e9ec76e46c6ccf22e541eae","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/21f52f881d0aa7913e9ec76e46c6ccf22e541eae/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"aa6555b95328fe3c21087de75aae8881ea117293","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/aa6555b95328fe3c21087de75aae8881ea117293/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"ad3ff4b4628c2aa53be51ce71a8467ca39f5a071","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/ad3ff4b4628c2aa53be51ce71a8467ca39f5a071/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"Childrens splash pool","mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"09b74250360b58217d903c5eae73bafe2c3ffb9f","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/09b74250360b58217d903c5eae73bafe2c3ffb9f/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"1aff3445788b107e9bd2645e4754409e8e0efc72","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/1aff3445788b107e9bd2645e4754409e8e0efc72/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"Childrens splash pool","mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"4e16e13b0aec588c8a86fe3b5b74a81fc94a930b","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/4e16e13b0aec588c8a86fe3b5b74a81fc94a930b/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"2-bedroom apartment","mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"69dc42a4a98978e1b4cad0e5103d7ade3dbd2d3d","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/69dc42a4a98978e1b4cad0e5103d7ade3dbd2d3d/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"9102da59853a169f349bca5f72d993dc91f11f5d","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/9102da59853a169f349bca5f72d993dc91f11f5d/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"960a655664ea21f0185d374a8ad71037423597c1","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/960a655664ea21f0185d374a8ad71037423597c1/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"095af14ee3f539f43059b59624f2aedda5b85a73","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/095af14ee3f539f43059b59624f2aedda5b85a73/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"9ab59260770be0c4a4395ff820649035b5e67e61","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/9ab59260770be0c4a4395ff820649035b5e67e61/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"1-bedroom apartment","mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"9b4f53e11427115fded05277a459e88d4ea96c4b","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/9b4f53e11427115fded05277a459e88d4ea96c4b/232x130/web/TH_FAM_CLB_10_F013_f4a7d2b6af626efa63dc99b8e6e000fff29fa571.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"b9d9dc79f4f853c3a61fcf07963a36f8e3af17d0","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/b9d9dc79f4f853c3a61fcf07963a36f8e3af17d0/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"efcce23b2c2b90b1e80bc4f892bb042686b3fe36","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/efcce23b2c2b90b1e80bc4f892bb042686b3fe36/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"87984ef53e86fccc90a80fa93afb502009e235fb","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/87984ef53e86fccc90a80fa93afb502009e235fb/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"53adc58c1c8a466996a63eafcd595f6223606af7","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/53adc58c1c8a466996a63eafcd595f6223606af7/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"1-bedroom apartment","mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"038dd5522526be88c3b01eb8f85e41f3f4f639de","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/038dd5522526be88c3b01eb8f85e41f3f4f639de/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"Childrens splash pool","mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"2e6fd794000593871ea442cb17ea8a9ee4c97e97","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/2e6fd794000593871ea442cb17ea8a9ee4c97e97/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"094d5677077d00f9a9f8d98c1839e468b17d5092","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/094d5677077d00f9a9f8d98c1839e468b17d5092/488x274/web/TH_FAM_CLB_09_F111_b8a87748d618a86ccd234474713f20b02b9fce6c.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"061e3f84f2ee093187b77af1644415b225367f66","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/061e3f84f2ee093187b77af1644415b225367f66/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.JPG","description":null,"mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"c8f15afec492101540cfcad046ff69d24a933f0f","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/c8f15afec492101540cfcad046ff69d24a933f0f/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"2-bedroom apartment","mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"974193aca0de05ef0acd0648ee3961733d5aa783","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/974193aca0de05ef0acd0648ee3961733d5aa783/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"Restaurant and pizzeria","mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"0a756faad952191a19b5f99c191581ceec1d3b54","mainSrc":"http://newmedia.thomson.co.uk/live/vol/0/0a756faad952191a19b5f99c191581ceec1d3b54/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCAT.jpg","description":"Calan Forcat Beach, Menorca","mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"e727b87de3c0c144426a8f3267820515597a50d3","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/e727b87de3c0c144426a8f3267820515597a50d3/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"Childrens paddling pool","mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"2bc2df113776d7f07a4a637a58f23e1806c66c03","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/2bc2df113776d7f07a4a637a58f23e1806c66c03/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"Bar","mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"71d9d146452cb21983f3b0198e313e88cdcc948f","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/71d9d146452cb21983f3b0198e313e88cdcc948f/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.JPG","description":null,"mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"286f8f9f35933da97a101220d057073d6ea647e6","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/286f8f9f35933da97a101220d057073d6ea647e6/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"b372cc0a272aad4ebaf136d87ee2fd95dfcd28ef","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/b372cc0a272aad4ebaf136d87ee2fd95dfcd28ef/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"Childrens splash pool","mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"21f52f881d0aa7913e9ec76e46c6ccf22e541eae","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/21f52f881d0aa7913e9ec76e46c6ccf22e541eae/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"aa6555b95328fe3c21087de75aae8881ea117293","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/aa6555b95328fe3c21087de75aae8881ea117293/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"ad3ff4b4628c2aa53be51ce71a8467ca39f5a071","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/ad3ff4b4628c2aa53be51ce71a8467ca39f5a071/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"Childrens splash pool","mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"09b74250360b58217d903c5eae73bafe2c3ffb9f","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/09b74250360b58217d903c5eae73bafe2c3ffb9f/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"1aff3445788b107e9bd2645e4754409e8e0efc72","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/1aff3445788b107e9bd2645e4754409e8e0efc72/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"Childrens splash pool","mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"4e16e13b0aec588c8a86fe3b5b74a81fc94a930b","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/4e16e13b0aec588c8a86fe3b5b74a81fc94a930b/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"2-bedroom apartment","mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"69dc42a4a98978e1b4cad0e5103d7ade3dbd2d3d","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/69dc42a4a98978e1b4cad0e5103d7ade3dbd2d3d/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"9102da59853a169f349bca5f72d993dc91f11f5d","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/9102da59853a169f349bca5f72d993dc91f11f5d/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"960a655664ea21f0185d374a8ad71037423597c1","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/960a655664ea21f0185d374a8ad71037423597c1/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"095af14ee3f539f43059b59624f2aedda5b85a73","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/095af14ee3f539f43059b59624f2aedda5b85a73/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"9ab59260770be0c4a4395ff820649035b5e67e61","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/9ab59260770be0c4a4395ff820649035b5e67e61/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"1-bedroom apartment","mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"9b4f53e11427115fded05277a459e88d4ea96c4b","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/9b4f53e11427115fded05277a459e88d4ea96c4b/488x274/web/TH_FAM_CLB_10_F013_f4a7d2b6af626efa63dc99b8e6e000fff29fa571.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"b9d9dc79f4f853c3a61fcf07963a36f8e3af17d0","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/b9d9dc79f4f853c3a61fcf07963a36f8e3af17d0/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"efcce23b2c2b90b1e80bc4f892bb042686b3fe36","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/efcce23b2c2b90b1e80bc4f892bb042686b3fe36/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"87984ef53e86fccc90a80fa93afb502009e235fb","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/87984ef53e86fccc90a80fa93afb502009e235fb/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"53adc58c1c8a466996a63eafcd595f6223606af7","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/53adc58c1c8a466996a63eafcd595f6223606af7/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"1-bedroom apartment","mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"038dd5522526be88c3b01eb8f85e41f3f4f639de","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/038dd5522526be88c3b01eb8f85e41f3f4f639de/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"Childrens splash pool","mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"2e6fd794000593871ea442cb17ea8a9ee4c97e97","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/2e6fd794000593871ea442cb17ea8a9ee4c97e97/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"094d5677077d00f9a9f8d98c1839e468b17d5092","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/094d5677077d00f9a9f8d98c1839e468b17d5092/1080x608/web/TH_FAM_CLB_09_F111_b8a87748d618a86ccd234474713f20b02b9fce6c.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"061e3f84f2ee093187b77af1644415b225367f66","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/061e3f84f2ee093187b77af1644415b225367f66/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.JPG","description":null,"mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"c8f15afec492101540cfcad046ff69d24a933f0f","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/c8f15afec492101540cfcad046ff69d24a933f0f/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"2-bedroom apartment","mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"974193aca0de05ef0acd0648ee3961733d5aa783","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/974193aca0de05ef0acd0648ee3961733d5aa783/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"Restaurant and pizzeria","mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"0a756faad952191a19b5f99c191581ceec1d3b54","mainSrc":"http://newmedia.thomson.co.uk/live/vol/0/0a756faad952191a19b5f99c191581ceec1d3b54/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCAT.jpg","description":"Calan Forcat Beach, Menorca","mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"e727b87de3c0c144426a8f3267820515597a50d3","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/e727b87de3c0c144426a8f3267820515597a50d3/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"Childrens paddling pool","mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"2bc2df113776d7f07a4a637a58f23e1806c66c03","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/2bc2df113776d7f07a4a637a58f23e1806c66c03/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"Bar","mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"71d9d146452cb21983f3b0198e313e88cdcc948f","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/71d9d146452cb21983f3b0198e313e88cdcc948f/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.JPG","description":null,"mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"286f8f9f35933da97a101220d057073d6ea647e6","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/286f8f9f35933da97a101220d057073d6ea647e6/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"b372cc0a272aad4ebaf136d87ee2fd95dfcd28ef","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/b372cc0a272aad4ebaf136d87ee2fd95dfcd28ef/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"Childrens splash pool","mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"21f52f881d0aa7913e9ec76e46c6ccf22e541eae","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/21f52f881d0aa7913e9ec76e46c6ccf22e541eae/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"aa6555b95328fe3c21087de75aae8881ea117293","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/aa6555b95328fe3c21087de75aae8881ea117293/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"ad3ff4b4628c2aa53be51ce71a8467ca39f5a071","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/ad3ff4b4628c2aa53be51ce71a8467ca39f5a071/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"Childrens splash pool","mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"09b74250360b58217d903c5eae73bafe2c3ffb9f","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/09b74250360b58217d903c5eae73bafe2c3ffb9f/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"1aff3445788b107e9bd2645e4754409e8e0efc72","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/1aff3445788b107e9bd2645e4754409e8e0efc72/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"Childrens splash pool","mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"4e16e13b0aec588c8a86fe3b5b74a81fc94a930b","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/4e16e13b0aec588c8a86fe3b5b74a81fc94a930b/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"2-bedroom apartment","mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"69dc42a4a98978e1b4cad0e5103d7ade3dbd2d3d","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/69dc42a4a98978e1b4cad0e5103d7ade3dbd2d3d/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"9102da59853a169f349bca5f72d993dc91f11f5d","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/9102da59853a169f349bca5f72d993dc91f11f5d/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"960a655664ea21f0185d374a8ad71037423597c1","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/960a655664ea21f0185d374a8ad71037423597c1/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"095af14ee3f539f43059b59624f2aedda5b85a73","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/095af14ee3f539f43059b59624f2aedda5b85a73/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"9ab59260770be0c4a4395ff820649035b5e67e61","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/9ab59260770be0c4a4395ff820649035b5e67e61/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":"1-bedroom apartment","mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"9b4f53e11427115fded05277a459e88d4ea96c4b","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/9b4f53e11427115fded05277a459e88d4ea96c4b/1080x608/web/TH_FAM_CLB_10_F013_f4a7d2b6af626efa63dc99b8e6e000fff29fa571.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"b9d9dc79f4f853c3a61fcf07963a36f8e3af17d0","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/b9d9dc79f4f853c3a61fcf07963a36f8e3af17d0/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"efcce23b2c2b90b1e80bc4f892bb042686b3fe36","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/efcce23b2c2b90b1e80bc4f892bb042686b3fe36/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMENORCACALANFORCATTALAYOTAPTSCB.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"xlarge","brand":null}],"extrasCount":1,"priceDiff":0,"freeChildPlace":false,"priceDecreased":false,"datePassedWishlistEntryId":null,"wishlistEntryId":"74a10242-dddb-464b-8405-10ecc41d2259","wishlistId":"e337d119-e0f6-4ad0-b849-4290ae03be79","extraFacilityCategoryViewData":[],"passenger":[{"identifier":1,"type":"ADULT","age":30,"passengerCount":1,"selectedMealOption":null,"selectedBaggageOption":{"code":"HLA","weightCode":"15","groupCode":"FLIGHT","description":"HOLD BAGGAGE - 1 BAG","quantity":0,"selectedQuantity":0,"availableQuantity":999,"limitedAvailability":false,"limitedAvailabilityText":null,"free":false,"included":true,"fadingTimer":"Na","price":0,"adultPrice":0,"childPrice":0,"maxChildAge":0,"minAge":0,"maxAge":0,"selected":true,"categoryCode":"BAG","selection":"included","currencyAppendedPrice":"","currencyAppendedPerPersonPrice":"","currencyAppendedPricePerNight":"","maxAllowedPassengers":0,"currencyAppendedPerTaxiPrice":"","currencyAppendedChildPrice":"","currencyAppendedAdultPrice":"","paxType":null,"summaryDescription":"","currencyAppendedTotalPrice":"","maxWeightPerPiece":30,"maxPiecePerPerson":1,"infantWeight":10,"baseWeight":15,"infantBaggageWeightDescription":null,"infantBaggageWeightSelection":"free","totalSelected":false,"passengerExtraFacilityMapping":{"passengers":[]}},"swimOrStageExtraSelected":false,"passengerLabel":null},{"identifier":2,"type":"ADULT","age":30,"passengerCount":2,"selectedMealOption":null,"selectedBaggageOption":{"code":"HLA","weightCode":"15","groupCode":"FLIGHT","description":"HOLD BAGGAGE - 1 BAG","quantity":0,"selectedQuantity":0,"availableQuantity":999,"limitedAvailability":false,"limitedAvailabilityText":null,"free":false,"included":true,"fadingTimer":"Na","price":0,"adultPrice":0,"childPrice":0,"maxChildAge":0,"minAge":0,"maxAge":0,"selected":true,"categoryCode":"BAG","selection":"included","currencyAppendedPrice":"","currencyAppendedPerPersonPrice":"","currencyAppendedPricePerNight":"","maxAllowedPassengers":0,"currencyAppendedPerTaxiPrice":"","currencyAppendedChildPrice":"","currencyAppendedAdultPrice":"","paxType":null,"summaryDescription":"","currencyAppendedTotalPrice":"","maxWeightPerPiece":30,"maxPiecePerPerson":1,"infantWeight":10,"baseWeight":15,"infantBaggageWeightDescription":null,"infantBaggageWeightSelection":"free","totalSelected":false,"passengerExtraFacilityMapping":{"passengers":[]}},"swimOrStageExtraSelected":false,"passengerLabel":null}],"id":"","availableRooms":1,"brandType":"TH","index":0,"earlySalesCommercialPriority":0,"ancillariesBreakup":[{"price":2,"ancillaryName":"World Care Fund - Adult","count":1}],"soldoutWishlistEntryId":null,"deposit":400,"notes":null,"truncate":100,"lowAvailabilityIndicator":false},{"tripadvisorData":{"logo":null,"ratingBar":"http://www.tripadvisor.com/img/cdsi/img2/ratings/traveler/4.5-11064-1.gif","averageRating":4.5,"reviewsCount":1132,"userReviewsCount":0,"reviewsUrl":null},"paxViewData":{"noOfAdults":2,"noOfChildren":0,"noOfInfants":0,"noOfSeniors":0,"childAges":[],"paxComposition":"2 adults"},"accomViewData":{"accomCode":"007652","roomViewData":[{"description":"1 Bedroom apartment","roomCode":"XB032","roomType":null,"roomTitle":null,"quantity":1,"noOfAdults":2,"noOfChildren":0,"noOfInfants":0,"totalChildrenCount":0,"childAges":[],"roomAllocationImage":"","price":672,"currencyAppendedPrice":"£672.00","included":true,"sellingCode":null,"packageId":null,"roomImage":null,"minOccupancy":0,"maxOccupancy":0,"roomName":null,"roomFeatures":null,"limitedAvailability":false,"limitedAvailabilityText":null,"selected":false,"perPersonPrice":null,"differencePrice":"","shouldDisplay":false,"defaultRoom":false}],"accomName":"Protur Aparthotel Bonaire","accomImageUrl":"http://newmedia.thomson.co.uk/live/vol/2/81fe17a55e269dbe3a13db44e8c7aee396160ad5/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","destinationName":"Majorca","resortName":"Cala Bona","rating":"4 plus","boardBasisName":"Self Catering","noOfRooms":0,"featureCodesAndValues":{"usps":["Thomson Family Resort Apr-Oct","Close to the beach","Stylish apartments","Spectacular pool and splash park"]},"productRanges":[{"code":"FAM","name":null,"url":null,"description":null,"supercategories":null,"benefits":null,"usps":null,"imageDataList":null,"mediaData":null,"featureCodesAndValues":{"name":["Thomson Family Resorts"],"strapline":["Created for families, with leading childcare and entertainment"]},"productUsps":null,"endecaAccommodationFilterList":null,"picture":null,"pictureUrl":"http://newmedia.thomson.co.uk/live/vol/2/5e909d4b89c8e88e0f1060b49e194cf1d9e7ce15/658x370/web/TH_GEN_11_F0156_dd408bde09cecdafdded8cc7f5410892d174fe0f.jpg","galleryImages":[]}],"summaryRoomViewData":[{"roomCode":"XB032","description":"1 Bedroom apartment × 1","quantity":1,"price":672,"currencyAppendedPrice":"£672.00","included":true}],"differentiatedProduct":true,"differentiatedType":"Thomson Family Resorts","differentiatedCode":"FAM","villaFacilities":null,"sizeInformation":null,"holidayFacilities":["A la carte dining","Daytime activity programme","Kids club","Evening entertainment programme","Gym"],"averageRainfall":64,"averageTemperature":24,"locationInformation":{"locationInformation":["North eastern end of Cala Bona","Set in spacious grounds","200m to small sand/shingle beach","Local buses will take you the 600m into the resort centre or a little further to the larger resort of Cala Millor"]},"hotelRoomsAndFloors":{"noOfrooms":[],"noOfFloors":[]}},"flightViewData":{"duration":7,"outboundSectors":[{"arrivalAirport":{"code":"PMI","name":"Palma de Mallorca"},"departureAirport":{"code":"LGW","name":"London Gatwick"},"schedule":{"arrTime":"21:10","depTime":"17:55","arrivalDate":"Sat 11 Oct 2014","departureDate":"Sat 11 Oct 2014","timeOfFlight":"","departureDateInMiliSeconds":"Oct 11, 2014 01:00:00 AM","arrivalDateInMiliSeconds":"Oct 11, 2014 01:00:00 AM","slot":null,"flightOffsetDays":0,"flightOffsetDaysSummary":null},"jnrDuration":"","eqmtDescription":"","carrierCode":""}],"inboundSectors":[{"arrivalAirport":{"code":"LGW","name":"London Gatwick"},"departureAirport":{"code":"PMI","name":"Palma de Mallorca"},"schedule":{"arrTime":"23:35","depTime":"22:10","arrivalDate":"Sat 18 Oct 2014","departureDate":"Sat 18 Oct 2014","timeOfFlight":"","departureDateInMiliSeconds":"Oct 18, 2014 01:00:00 AM","arrivalDateInMiliSeconds":"Oct 18, 2014 01:00:00 AM","slot":null,"flightOffsetDays":0,"flightOffsetDaysSummary":null},"jnrDuration":"","eqmtDescription":"","carrierCode":""}],"highlights":[],"dreamLiner":false},"priceBreakdownViewData":[{"description":"Basic Holiday","price":672,"currencyAppendedPrice":"£672"},{"description":"Options & extras (total)","price":2,"currencyAppendedPrice":"£2"},{"description":"Online Discount","price":76,"currencyAppendedPrice":"- £76"},{"description":"Total Price","price":674,"currencyAppendedPrice":"674"}],"roundUpTotalCost":null,"pricePerPerson":337,"onlineDiscount":76,"currencyAppendedPricePerPerson":"£337 per person","currencyAppendedRoundUpTotalCost":"674","currencyAppendedOnlineDiscount":"Includes £76.0 discount","selected":false,"galleryImages":[{"code":"81fe17a55e269dbe3a13db44e8c7aee396160ad5","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/81fe17a55e269dbe3a13db44e8c7aee396160ad5/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"738eb79887e34f088974251a773c3e56cde290ba","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/738eb79887e34f088974251a773c3e56cde290ba/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":"1-bedroom apartment","mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"20131a06781ded662d1497d28715379cdb541c44","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/20131a06781ded662d1497d28715379cdb541c44/658x370/web/MAJ_CBN_0646_fc135d34f4175c89a43f9fd2125ee618c9fa8df8.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"0983593e757b9e6a1e4f0a469ce0b2f8a6ad129a","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/0983593e757b9e6a1e4f0a469ce0b2f8a6ad129a/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.JPG","description":"Buffet restaurant","mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"2c9e325dc390a0c33b9ba0bf5986cfd983033a4a","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/2c9e325dc390a0c33b9ba0bf5986cfd983033a4a/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":"1-bedroom apartment","mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"4054f2de59e3b007eca38c0e5d8b85e6d5729fd7","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/4054f2de59e3b007eca38c0e5d8b85e6d5729fd7/658x370/web/MAJ_CBN_0645_d53bc70688ddda6accff138ea4c7fd056c708e96.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"93541ef2306da1afc151477ae76104996f958e03","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/93541ef2306da1afc151477ae76104996f958e03/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":"Italian restaurant","mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"2f0d66d2f259c47010f2c001b4f3b529db6857e0","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/2f0d66d2f259c47010f2c001b4f3b529db6857e0/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":"Lobby","mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"e243502a1e1a0bf43622734f3f35d6717c577391","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/e243502a1e1a0bf43622734f3f35d6717c577391/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"2a6a89f7a5e1a627bc2cd9f94adf95a71c0d8f15","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/2a6a89f7a5e1a627bc2cd9f94adf95a71c0d8f15/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"091fcdc65da45b21b91fd1cf0232619f260b6751","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/091fcdc65da45b21b91fd1cf0232619f260b6751/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":"Indoor pool","mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"a0a643e0de47e277898e4eea06bf1032e92ce44b","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/a0a643e0de47e277898e4eea06bf1032e92ce44b/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"12b89e9dc7974434d412da9337da26d3dd3a508d","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/12b89e9dc7974434d412da9337da26d3dd3a508d/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"24224f23b347dac3ff26e6196c5a3981c193d23b","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/24224f23b347dac3ff26e6196c5a3981c193d23b/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"b02001cbaaec72a724cf6f2f2c0115e333f5b02d","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/b02001cbaaec72a724cf6f2f2c0115e333f5b02d/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.JPG","description":null,"mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"2e3c3cf80edf843d04b30fb687e3f48562ebbb8f","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/2e3c3cf80edf843d04b30fb687e3f48562ebbb8f/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":"Italian restaurant","mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"f4c6193ad69f8a6400a661177cd7f8a54f0785cc","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/f4c6193ad69f8a6400a661177cd7f8a54f0785cc/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"430817ef8bbe6cca871434bb0c06cab5749d8bf0","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/430817ef8bbe6cca871434bb0c06cab5749d8bf0/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"2b60c0192a573275a35304c256c5bd948a612106","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/2b60c0192a573275a35304c256c5bd948a612106/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"8c2f30abf427dc33c63c62f78d62f34a130127b6","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/8c2f30abf427dc33c63c62f78d62f34a130127b6/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":"Kids club","mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"cd01abafb8991c37f4d9dada8cc25f1e5f16a3de","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/cd01abafb8991c37f4d9dada8cc25f1e5f16a3de/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"6d3d4ae8024ea0962e12d6b3e9c9fbff9032bd1c","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/6d3d4ae8024ea0962e12d6b3e9c9fbff9032bd1c/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"827f87257f10139fd0efe537837bd3d231949875","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/827f87257f10139fd0efe537837bd3d231949875/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":"Kids club","mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"3b60af5ec5dc899c5be084ec96c25c891c8777f5","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/3b60af5ec5dc899c5be084ec96c25c891c8777f5/658x370/web/MAJ_CBN_0647_fa60883130f8088d77a8e7f01a17ee7083a68381.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"c803799d888756982a549782076413af6833ec34","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/c803799d888756982a549782076413af6833ec34/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"d7baf0908dd3812ec405c18d9c86e64b053e1a0c","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/d7baf0908dd3812ec405c18d9c86e64b053e1a0c/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"f1a5bee3b1765fdcda6c84b7f3a51c4a09aeb2fb","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/f1a5bee3b1765fdcda6c84b7f3a51c4a09aeb2fb/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":"1-bedroom apartment","mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"22c06db182c7cd93fcbd157c2f0f41b87c074e20","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/22c06db182c7cd93fcbd157c2f0f41b87c074e20/658x370/web/TH_FAM_CLB_10_F009_9a46bb3adb90c866ce3021960d9c4266b8e99f00.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"8737f95e5201b31ce41c8f9e3327150b775e1406","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/8737f95e5201b31ce41c8f9e3327150b775e1406/658x370/web/TH_FAM_CLB_09_F071_9cb03954a7ac96b95c3f7373128941e7be71777d.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"large","brand":null},{"code":"81fe17a55e269dbe3a13db44e8c7aee396160ad5","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/81fe17a55e269dbe3a13db44e8c7aee396160ad5/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"738eb79887e34f088974251a773c3e56cde290ba","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/738eb79887e34f088974251a773c3e56cde290ba/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":"1-bedroom apartment","mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"20131a06781ded662d1497d28715379cdb541c44","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/20131a06781ded662d1497d28715379cdb541c44/232x130/web/MAJ_CBN_0646_fc135d34f4175c89a43f9fd2125ee618c9fa8df8.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"0983593e757b9e6a1e4f0a469ce0b2f8a6ad129a","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/0983593e757b9e6a1e4f0a469ce0b2f8a6ad129a/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.JPG","description":"Buffet restaurant","mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"2c9e325dc390a0c33b9ba0bf5986cfd983033a4a","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/2c9e325dc390a0c33b9ba0bf5986cfd983033a4a/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":"1-bedroom apartment","mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"4054f2de59e3b007eca38c0e5d8b85e6d5729fd7","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/4054f2de59e3b007eca38c0e5d8b85e6d5729fd7/232x130/web/MAJ_CBN_0645_d53bc70688ddda6accff138ea4c7fd056c708e96.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"93541ef2306da1afc151477ae76104996f958e03","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/93541ef2306da1afc151477ae76104996f958e03/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":"Italian restaurant","mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"2f0d66d2f259c47010f2c001b4f3b529db6857e0","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/2f0d66d2f259c47010f2c001b4f3b529db6857e0/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":"Lobby","mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"e243502a1e1a0bf43622734f3f35d6717c577391","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/e243502a1e1a0bf43622734f3f35d6717c577391/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"2a6a89f7a5e1a627bc2cd9f94adf95a71c0d8f15","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/2a6a89f7a5e1a627bc2cd9f94adf95a71c0d8f15/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"091fcdc65da45b21b91fd1cf0232619f260b6751","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/091fcdc65da45b21b91fd1cf0232619f260b6751/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":"Indoor pool","mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"a0a643e0de47e277898e4eea06bf1032e92ce44b","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/a0a643e0de47e277898e4eea06bf1032e92ce44b/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"12b89e9dc7974434d412da9337da26d3dd3a508d","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/12b89e9dc7974434d412da9337da26d3dd3a508d/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"24224f23b347dac3ff26e6196c5a3981c193d23b","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/24224f23b347dac3ff26e6196c5a3981c193d23b/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"b02001cbaaec72a724cf6f2f2c0115e333f5b02d","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/b02001cbaaec72a724cf6f2f2c0115e333f5b02d/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.JPG","description":null,"mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"2e3c3cf80edf843d04b30fb687e3f48562ebbb8f","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/2e3c3cf80edf843d04b30fb687e3f48562ebbb8f/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":"Italian restaurant","mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"f4c6193ad69f8a6400a661177cd7f8a54f0785cc","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/f4c6193ad69f8a6400a661177cd7f8a54f0785cc/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"430817ef8bbe6cca871434bb0c06cab5749d8bf0","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/430817ef8bbe6cca871434bb0c06cab5749d8bf0/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"2b60c0192a573275a35304c256c5bd948a612106","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/2b60c0192a573275a35304c256c5bd948a612106/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"8c2f30abf427dc33c63c62f78d62f34a130127b6","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/8c2f30abf427dc33c63c62f78d62f34a130127b6/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":"Kids club","mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"cd01abafb8991c37f4d9dada8cc25f1e5f16a3de","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/cd01abafb8991c37f4d9dada8cc25f1e5f16a3de/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"6d3d4ae8024ea0962e12d6b3e9c9fbff9032bd1c","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/6d3d4ae8024ea0962e12d6b3e9c9fbff9032bd1c/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"827f87257f10139fd0efe537837bd3d231949875","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/827f87257f10139fd0efe537837bd3d231949875/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":"Kids club","mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"3b60af5ec5dc899c5be084ec96c25c891c8777f5","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/3b60af5ec5dc899c5be084ec96c25c891c8777f5/232x130/web/MAJ_CBN_0647_fa60883130f8088d77a8e7f01a17ee7083a68381.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"c803799d888756982a549782076413af6833ec34","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/c803799d888756982a549782076413af6833ec34/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"d7baf0908dd3812ec405c18d9c86e64b053e1a0c","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/d7baf0908dd3812ec405c18d9c86e64b053e1a0c/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"f1a5bee3b1765fdcda6c84b7f3a51c4a09aeb2fb","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/f1a5bee3b1765fdcda6c84b7f3a51c4a09aeb2fb/232x130/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":"1-bedroom apartment","mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"22c06db182c7cd93fcbd157c2f0f41b87c074e20","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/22c06db182c7cd93fcbd157c2f0f41b87c074e20/232x130/web/TH_FAM_CLB_10_F009_9a46bb3adb90c866ce3021960d9c4266b8e99f00.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"8737f95e5201b31ce41c8f9e3327150b775e1406","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/8737f95e5201b31ce41c8f9e3327150b775e1406/232x130/web/TH_FAM_CLB_09_F071_9cb03954a7ac96b95c3f7373128941e7be71777d.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"small","brand":null},{"code":"81fe17a55e269dbe3a13db44e8c7aee396160ad5","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/81fe17a55e269dbe3a13db44e8c7aee396160ad5/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"738eb79887e34f088974251a773c3e56cde290ba","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/738eb79887e34f088974251a773c3e56cde290ba/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":"1-bedroom apartment","mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"20131a06781ded662d1497d28715379cdb541c44","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/20131a06781ded662d1497d28715379cdb541c44/488x274/web/MAJ_CBN_0646_fc135d34f4175c89a43f9fd2125ee618c9fa8df8.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"0983593e757b9e6a1e4f0a469ce0b2f8a6ad129a","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/0983593e757b9e6a1e4f0a469ce0b2f8a6ad129a/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.JPG","description":"Buffet restaurant","mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"2c9e325dc390a0c33b9ba0bf5986cfd983033a4a","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/2c9e325dc390a0c33b9ba0bf5986cfd983033a4a/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":"1-bedroom apartment","mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"4054f2de59e3b007eca38c0e5d8b85e6d5729fd7","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/4054f2de59e3b007eca38c0e5d8b85e6d5729fd7/488x274/web/MAJ_CBN_0645_d53bc70688ddda6accff138ea4c7fd056c708e96.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"93541ef2306da1afc151477ae76104996f958e03","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/93541ef2306da1afc151477ae76104996f958e03/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":"Italian restaurant","mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"2f0d66d2f259c47010f2c001b4f3b529db6857e0","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/2f0d66d2f259c47010f2c001b4f3b529db6857e0/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":"Lobby","mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"e243502a1e1a0bf43622734f3f35d6717c577391","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/e243502a1e1a0bf43622734f3f35d6717c577391/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"2a6a89f7a5e1a627bc2cd9f94adf95a71c0d8f15","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/2a6a89f7a5e1a627bc2cd9f94adf95a71c0d8f15/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"091fcdc65da45b21b91fd1cf0232619f260b6751","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/091fcdc65da45b21b91fd1cf0232619f260b6751/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":"Indoor pool","mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"a0a643e0de47e277898e4eea06bf1032e92ce44b","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/a0a643e0de47e277898e4eea06bf1032e92ce44b/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"12b89e9dc7974434d412da9337da26d3dd3a508d","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/12b89e9dc7974434d412da9337da26d3dd3a508d/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"24224f23b347dac3ff26e6196c5a3981c193d23b","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/24224f23b347dac3ff26e6196c5a3981c193d23b/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"b02001cbaaec72a724cf6f2f2c0115e333f5b02d","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/b02001cbaaec72a724cf6f2f2c0115e333f5b02d/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.JPG","description":null,"mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"2e3c3cf80edf843d04b30fb687e3f48562ebbb8f","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/2e3c3cf80edf843d04b30fb687e3f48562ebbb8f/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":"Italian restaurant","mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"f4c6193ad69f8a6400a661177cd7f8a54f0785cc","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/f4c6193ad69f8a6400a661177cd7f8a54f0785cc/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"430817ef8bbe6cca871434bb0c06cab5749d8bf0","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/430817ef8bbe6cca871434bb0c06cab5749d8bf0/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"2b60c0192a573275a35304c256c5bd948a612106","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/2b60c0192a573275a35304c256c5bd948a612106/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"8c2f30abf427dc33c63c62f78d62f34a130127b6","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/8c2f30abf427dc33c63c62f78d62f34a130127b6/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":"Kids club","mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"cd01abafb8991c37f4d9dada8cc25f1e5f16a3de","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/cd01abafb8991c37f4d9dada8cc25f1e5f16a3de/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"6d3d4ae8024ea0962e12d6b3e9c9fbff9032bd1c","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/6d3d4ae8024ea0962e12d6b3e9c9fbff9032bd1c/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"827f87257f10139fd0efe537837bd3d231949875","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/827f87257f10139fd0efe537837bd3d231949875/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":"Kids club","mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"3b60af5ec5dc899c5be084ec96c25c891c8777f5","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/3b60af5ec5dc899c5be084ec96c25c891c8777f5/488x274/web/MAJ_CBN_0647_fa60883130f8088d77a8e7f01a17ee7083a68381.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"c803799d888756982a549782076413af6833ec34","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/c803799d888756982a549782076413af6833ec34/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"d7baf0908dd3812ec405c18d9c86e64b053e1a0c","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/d7baf0908dd3812ec405c18d9c86e64b053e1a0c/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"f1a5bee3b1765fdcda6c84b7f3a51c4a09aeb2fb","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/f1a5bee3b1765fdcda6c84b7f3a51c4a09aeb2fb/488x274/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":"1-bedroom apartment","mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"22c06db182c7cd93fcbd157c2f0f41b87c074e20","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/22c06db182c7cd93fcbd157c2f0f41b87c074e20/488x274/web/TH_FAM_CLB_10_F009_9a46bb3adb90c866ce3021960d9c4266b8e99f00.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"8737f95e5201b31ce41c8f9e3327150b775e1406","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/8737f95e5201b31ce41c8f9e3327150b775e1406/488x274/web/TH_FAM_CLB_09_F071_9cb03954a7ac96b95c3f7373128941e7be71777d.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"medium","brand":null},{"code":"81fe17a55e269dbe3a13db44e8c7aee396160ad5","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/81fe17a55e269dbe3a13db44e8c7aee396160ad5/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"738eb79887e34f088974251a773c3e56cde290ba","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/738eb79887e34f088974251a773c3e56cde290ba/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":"1-bedroom apartment","mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"20131a06781ded662d1497d28715379cdb541c44","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/20131a06781ded662d1497d28715379cdb541c44/1080x608/web/MAJ_CBN_0646_fc135d34f4175c89a43f9fd2125ee618c9fa8df8.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"0983593e757b9e6a1e4f0a469ce0b2f8a6ad129a","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/0983593e757b9e6a1e4f0a469ce0b2f8a6ad129a/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.JPG","description":"Buffet restaurant","mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"2c9e325dc390a0c33b9ba0bf5986cfd983033a4a","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/2c9e325dc390a0c33b9ba0bf5986cfd983033a4a/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":"1-bedroom apartment","mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"4054f2de59e3b007eca38c0e5d8b85e6d5729fd7","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/4054f2de59e3b007eca38c0e5d8b85e6d5729fd7/1080x608/web/MAJ_CBN_0645_d53bc70688ddda6accff138ea4c7fd056c708e96.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"93541ef2306da1afc151477ae76104996f958e03","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/93541ef2306da1afc151477ae76104996f958e03/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":"Italian restaurant","mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"2f0d66d2f259c47010f2c001b4f3b529db6857e0","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/2f0d66d2f259c47010f2c001b4f3b529db6857e0/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":"Lobby","mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"e243502a1e1a0bf43622734f3f35d6717c577391","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/e243502a1e1a0bf43622734f3f35d6717c577391/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"2a6a89f7a5e1a627bc2cd9f94adf95a71c0d8f15","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/2a6a89f7a5e1a627bc2cd9f94adf95a71c0d8f15/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"091fcdc65da45b21b91fd1cf0232619f260b6751","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/091fcdc65da45b21b91fd1cf0232619f260b6751/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":"Indoor pool","mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"a0a643e0de47e277898e4eea06bf1032e92ce44b","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/a0a643e0de47e277898e4eea06bf1032e92ce44b/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"12b89e9dc7974434d412da9337da26d3dd3a508d","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/12b89e9dc7974434d412da9337da26d3dd3a508d/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"24224f23b347dac3ff26e6196c5a3981c193d23b","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/24224f23b347dac3ff26e6196c5a3981c193d23b/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"b02001cbaaec72a724cf6f2f2c0115e333f5b02d","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/b02001cbaaec72a724cf6f2f2c0115e333f5b02d/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.JPG","description":null,"mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"2e3c3cf80edf843d04b30fb687e3f48562ebbb8f","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/2e3c3cf80edf843d04b30fb687e3f48562ebbb8f/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":"Italian restaurant","mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"f4c6193ad69f8a6400a661177cd7f8a54f0785cc","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/f4c6193ad69f8a6400a661177cd7f8a54f0785cc/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"430817ef8bbe6cca871434bb0c06cab5749d8bf0","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/430817ef8bbe6cca871434bb0c06cab5749d8bf0/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"2b60c0192a573275a35304c256c5bd948a612106","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/2b60c0192a573275a35304c256c5bd948a612106/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"8c2f30abf427dc33c63c62f78d62f34a130127b6","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/8c2f30abf427dc33c63c62f78d62f34a130127b6/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":"Kids club","mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"cd01abafb8991c37f4d9dada8cc25f1e5f16a3de","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/cd01abafb8991c37f4d9dada8cc25f1e5f16a3de/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"6d3d4ae8024ea0962e12d6b3e9c9fbff9032bd1c","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/6d3d4ae8024ea0962e12d6b3e9c9fbff9032bd1c/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"827f87257f10139fd0efe537837bd3d231949875","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/827f87257f10139fd0efe537837bd3d231949875/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":"Kids club","mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"3b60af5ec5dc899c5be084ec96c25c891c8777f5","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/3b60af5ec5dc899c5be084ec96c25c891c8777f5/1080x608/web/MAJ_CBN_0647_fa60883130f8088d77a8e7f01a17ee7083a68381.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"c803799d888756982a549782076413af6833ec34","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/c803799d888756982a549782076413af6833ec34/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"d7baf0908dd3812ec405c18d9c86e64b053e1a0c","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/d7baf0908dd3812ec405c18d9c86e64b053e1a0c/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"f1a5bee3b1765fdcda6c84b7f3a51c4a09aeb2fb","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/f1a5bee3b1765fdcda6c84b7f3a51c4a09aeb2fb/1080x608/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCACALABONABONAIRE.jpg","description":"1-bedroom apartment","mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"22c06db182c7cd93fcbd157c2f0f41b87c074e20","mainSrc":"http://newmedia.thomson.co.uk/live/vol/1/22c06db182c7cd93fcbd157c2f0f41b87c074e20/1080x608/web/TH_FAM_CLB_10_F009_9a46bb3adb90c866ce3021960d9c4266b8e99f00.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"xlarge","brand":null},{"code":"8737f95e5201b31ce41c8f9e3327150b775e1406","mainSrc":"http://newmedia.thomson.co.uk/live/vol/2/8737f95e5201b31ce41c8f9e3327150b775e1406/1080x608/web/TH_FAM_CLB_09_F071_9cb03954a7ac96b95c3f7373128941e7be71777d.jpg","description":null,"mime":"image/jpeg","altText":null,"size":"xlarge","brand":null}],"extrasCount":1,"priceDiff":0,"freeChildPlace":false,"priceDecreased":false,"datePassedWishlistEntryId":null,"wishlistEntryId":"a8a9690c-9d18-4511-ab33-9ae8d332a4b0","wishlistId":"e337d119-e0f6-4ad0-b849-4290ae03be79","extraFacilityCategoryViewData":[],"passenger":[{"identifier":1,"type":"ADULT","age":30,"passengerCount":1,"selectedMealOption":null,"selectedBaggageOption":{"code":"HLA","weightCode":"20","groupCode":"FLIGHT","description":"HOLD BAGGAGE - 1 BAG","quantity":0,"selectedQuantity":0,"availableQuantity":999,"limitedAvailability":false,"limitedAvailabilityText":null,"free":false,"included":true,"fadingTimer":"Na","price":0,"adultPrice":0,"childPrice":0,"maxChildAge":0,"minAge":0,"maxAge":0,"selected":true,"categoryCode":"BAG","selection":"included","currencyAppendedPrice":"","currencyAppendedPerPersonPrice":"","currencyAppendedPricePerNight":"","maxAllowedPassengers":0,"currencyAppendedPerTaxiPrice":"","currencyAppendedChildPrice":"","currencyAppendedAdultPrice":"","paxType":null,"summaryDescription":"","currencyAppendedTotalPrice":"","maxWeightPerPiece":30,"maxPiecePerPerson":1,"infantWeight":10,"baseWeight":20,"infantBaggageWeightDescription":null,"infantBaggageWeightSelection":"free","totalSelected":false,"passengerExtraFacilityMapping":{"passengers":[]}},"swimOrStageExtraSelected":false,"passengerLabel":null},{"identifier":2,"type":"ADULT","age":30,"passengerCount":2,"selectedMealOption":null,"selectedBaggageOption":{"code":"HLA","weightCode":"20","groupCode":"FLIGHT","description":"HOLD BAGGAGE - 1 BAG","quantity":0,"selectedQuantity":0,"availableQuantity":999,"limitedAvailability":false,"limitedAvailabilityText":null,"free":false,"included":true,"fadingTimer":"Na","price":0,"adultPrice":0,"childPrice":0,"maxChildAge":0,"minAge":0,"maxAge":0,"selected":true,"categoryCode":"BAG","selection":"included","currencyAppendedPrice":"","currencyAppendedPerPersonPrice":"","currencyAppendedPricePerNight":"","maxAllowedPassengers":0,"currencyAppendedPerTaxiPrice":"","currencyAppendedChildPrice":"","currencyAppendedAdultPrice":"","paxType":null,"summaryDescription":"","currencyAppendedTotalPrice":"","maxWeightPerPiece":30,"maxPiecePerPerson":1,"infantWeight":10,"baseWeight":20,"infantBaggageWeightDescription":null,"infantBaggageWeightSelection":"free","totalSelected":false,"passengerExtraFacilityMapping":{"passengers":[]}},"swimOrStageExtraSelected":false,"passengerLabel":null}],"id":"","availableRooms":1,"brandType":"TH","index":0,"earlySalesCommercialPriority":0,"ancillariesBreakup":[{"price":2,"ancillaryName":"World Care Fund - Adult","count":1}],"soldoutWishlistEntryId":null,"deposit":400,"notes":null,"truncate":100,"lowAvailabilityIndicator":false}]';
				if(response){
					
					
					response = dojo.fromJson(response);
					var len = response.length;
					var emptyRangeFlag ="true";
					var noOfEmptyHolidays = "x";
					var html = widget.renderTmpl(CompareHolidayTmpl,{tuiWebrootPath:tuiWebrootPath,holidays: response, emptyRange: len < 3 ? "true" : "", emptyRange: emptyRangeFlag, noOfEmptyHolidays: noOfEmptyHolidays}); 
	
					console.log("two");
					if (html) {
						console.log("three");
						dojo.place(html, widget.domNode, "only");
						dojo.parser.parse(widget.domNode);
						console.log("four");
						widget.bindCompareView();
					}		
				}
			*/
			//temp code ends
			
		}
	});
	
	return tui.widget.customeraccount.CompareViewHolidays;
});
