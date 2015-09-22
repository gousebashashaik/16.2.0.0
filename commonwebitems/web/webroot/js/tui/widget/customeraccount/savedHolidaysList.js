define ("tui/widget/customeraccount/savedHolidaysList", [
													"dojo",											  	
													"dojo/cookie",
													"dojo/query",
													"dojo/has",
													"dojox/validate/web",
													"tui/validate/check",
													"dojo/_base/array",
													"dojo/dom-style",
													"dijit/focus",
													"dojo/text!tui/widget/customeraccount/view/templates/savedHolidaysListTmpl.html",
													"dojo/_base/xhr",
													"dojo/fx",
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
													"tui/widget/mixins/Templatable",
													"tui/widget/customeraccount/ErrorHandling",
													"tui/widget/customeraccount/CollectSavedHolidaysList",
													"tui/widget/customeraccount/AjaxLoader"
													
													
							    			  ], function(dojo,  cookie, query, has, validate,check, arrayUtil, domStyle, focusUtil, searchResultItemTmpl,xhr,fx,ajaxloader){

		dojo.declare("tui.widget.customeraccount.savedHolidaysList", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable, tui.widget.customeraccount.ErrorHandling, tui.widget.customeraccount.CollectSavedHolidaysList, tui.widget.customeraccount.AjaxLoader], {
	
		CommaSeparatedWishlistIds: null,
		tmpl: searchResultItemTmpl,
		
		holidays: null,
		component:null,
		login:null,
		tuiWebrootPath:tuiWebrootPath,
		
		onAfterTmplRender: function(){
			var searchResultsComponent = this;
			 //dojo.query(".mask-interactivity").style.diplay="none";			 
			 //dojo.removeClass(searchResultsComponent.domNode, 'updating');			 
		},
		showLoader:function(divId){
			if(dojo.byId(divId) != undefined){
				dojo.byId(divId).innerHTML = "";
				dojo.addClass(divId, "updating");
			}
		},
		hideLoader:function(divId){
			if(dojo.byId(divId) != undefined){
				dojo.removeClass(divId, "updating");
			}
		},
		postCreate: function() {
		
			var searchResultsComponent = this;
			
            searchResultsComponent.renderSavedHolidaysList();
			searchResultsComponent.sorting();
			
		},
		sorting:function(){
			var searchResultsComponent = this;
			if(dojo.byId("custom-select-options") != undefined){
				var sortField = dojo.byId("custom-select-options");
				
				dojo.connect(sortField, "onchange", function(event){			
						
					dojo.stopEvent(event);
					console.log(this.value);
					
					if(dojo.byId("caSortBy") != undefined){
						dojo.byId("caSortBy").innerHTML = this.options[this.selectedIndex].text;
					}
					if(dojo.byId("shortlist") != undefined){				
						searchResultsComponent.showLoader("shortlistresultsAvailable");
						searchResultsComponent.showLoader("noLongerAvailableHolidays");
					}
					
					if(searchResultsComponent.login !="null"){
						var sortUrl	= tuiWebrootPath+"/viewShortlist/sortSavedHolidays?sortBy="+this.value;
					}
					else{
						var sortUrl	= tuiWebrootPath+"/viewAnonymousShortlist/sortAnonymousSavedHolidays?sortBy="+this.value;
					}	
					/*
					var res = searchResultsComponent.handleSessionTimeOut();
					if(!res){
					return false;
					}
					*/
					
					
					xhr.post({				
						url: sortUrl,
						
						preventCache: true,
						
						content: {"sortBy": this.value},
						
						load: function(response) {
							
							console.log("inside load");
							if(dojo.byId("shortlist") != undefined){				
								//searchResultsComponent.hideLoader("shortlistresultsAvailable");
								//searchResultsComponent.hideLoader("noLongerAvailableHolidays");
								
							}
						},					
						error: function(errors) {
							console.log(errors);
							searchResultsComponent.handleBackendError(errors);
						},					
						handle: function(response) {
							
							console.log("inside handle");
							hasBeenSent = true;
							
							if(response){
								response = dojo.fromJson(response);
								console.log("before2");
								if(dojo.byId("results") != undefined){
									wipeTarget = dojo.byId("results");
									
													
									searchResultsComponent.renderSavedHolidaysList(response);
									wipeTarget.style.display = 'none';												
									fx.wipeIn({ node: wipeTarget }).play();
									console.log("after2");
									if(dojo.byId("shortlist") != undefined){				
										//searchResultsComponent.hideLoader("shortlistresultsAvailable");
										//searchResultsComponent.hideLoader("noLongerAvailableHolidays");
										
									}
								}
							}
							
						}
					});
					
					
				});
            }
		},
		
		renderGallery: function(holidays) {
		var widget = this;
     // _.each(holidays, function(holiday) {
			var myGallery = {
				image: []
			};
		var imgSmall = new Array();
		var imgLarge = new Array();
		var imgMedium = new Array();
		var imgDesc = new Array();
			var overlapDay = '(+1 day)';
			var accommodationImagesmall = '';
			var accommodationImagemedium = '';
			var accommodationImagelarge = '';
			var accommodationImageDescription = '';
			var gallery = holidays.galleryImages;
			var i = 0;
		
		 _.each(gallery, function(items) {
		 
		switch(items.size)
			{
			case 'small':
			  imgSmall[imgSmall.length] = items.mainSrc;
			  imgDesc[imgSmall.length-1] = items.description;
			  i++;
			  break;
			case 'medium':
			  imgMedium[imgMedium.length] = items.mainSrc;	
			  imgDesc[imgMedium.length-1] = items.description;		  
			  i++;
			  break;
			case 'large':
			  imgLarge[imgLarge.length] = items.mainSrc;
			  imgDesc[imgLarge.length-1] = items.description;				
			  i++;
			  break;
			default:
			  i++;
			  break;
			}
		});
		
        //_.each(gallery, function(items) {
				  var small = '';
				  var large = '';
				  var medium = '';
				  var description = '';
          for(var j = 0; j < imgSmall.length; j++) {
              large = imgLarge[j];
              small = imgSmall[j];
              medium = imgMedium[j];
            if (imgDesc[j] != null) {
              description = imgDesc[j];
				}
				else {
				  description = '';
				}
		  myGallery.image.push({
            'small': small,
            'medium': medium,
            'large': large,
            'description': description
			  });
          }
			  if (i === 0) {
				accommodationImagesmall = small;
				accommodationImagemedium = medium;
				accommodationImagelarge = large;
				accommodationImageDescription = description;
			  }
			  i++;
          

        //});
      

			return myGallery.image;
    },
    
		
		renderSavedHolidaysList: function(data){
			var searchResultsComponent = this;
			
			if(data){
				newHolidays = data;
			}
			else{				
				newHolidays = (searchResultsComponent.jsonData);
			}
			
			newHolidays = JSON.stringify(newHolidays);
			//var newHolidays = searchResultsComponent.special_chars_convert(newHolidays);
			var newHolidays = JSON.parse(newHolidays);
			
			var soldOutFlag = 0;
			var datePasssedFlag = 0;
			var CommaSeparatedWishlistIds = "";
			if(newHolidays){
				
				var len = newHolidays.length;
				
				
				var overlayData = [];
				for(var k = 0; k < len ; k++ ){
					var extrasPriceOverLayContent = "";
					if(newHolidays[k].soldoutWishlistEntryId != 0 && newHolidays[k].soldoutWishlistEntryId != null && newHolidays[k].soldoutWishlistEntryId != "null"){
						
						CommaSeparatedWishlistIds += newHolidays[k].soldoutWishlistEntryId+",";
						
						if(soldOutFlag == 0){
							soldOutFlag = 1;
						}
					}
					else if(newHolidays[k].datePassedWishlistEntryId != 0 && newHolidays[k].datePassedWishlistEntryId != null && newHolidays[k].datePassedWishlistEntryId != "null"){
						
						CommaSeparatedWishlistIds += newHolidays[k].datePassedWishlistEntryId+",";
						
						if(datePasssedFlag == 0){
							datePasssedFlag = 1;
						}
					}
					var excurstionLen = 0;
					if(newHolidays[k].ancillariesBreakup != null){
					var excurstionLen = newHolidays[k].ancillariesBreakup.length ;
					}
					if(newHolidays[k].accomViewData.differentiatedCode){
						newHolidays[k].accomViewData.differentiatedCode = newHolidays[k].accomViewData.differentiatedCode.toLowerCase();
					}
					if(excurstionLen){
					extrasPriceOverLayContent = "<table>";
					for(var t=0; t < excurstionLen; t++ ){
						if(newHolidays[k].ancillariesBreakup[t].count){
						extrasPriceOverLayContent += "<tr><td>"+newHolidays[k].ancillariesBreakup[t].count+" X "+newHolidays[k].ancillariesBreakup[t].ancillaryName+"</td><td>&nbsp;</td><td>&pound;"+newHolidays[k].ancillariesBreakup[t].price+"</td></tr>";
						}
						else{
							extrasPriceOverLayContent += "<tr><td>"+newHolidays[k].ancillariesBreakup[t].ancillaryName+"</td><td>&nbsp;</td><td>&pound;"+newHolidays[k].ancillariesBreakup[t].price+"</td></tr>";
						}
					}
					extrasPriceOverLayContent += "</table>";
					}
					
					newHolidays[k]["overlayContent"] = extrasPriceOverLayContent;
					
					newHolidays[k]["gallery"] = dojo.toJson(searchResultsComponent.renderGallery(newHolidays[k]));
					//newHolidays[k]["gallery"] = dojo.toJson(newHolidays[k]);
					//newHolidays[k]["brandType"] = "TH"; //notesTruncated 
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
							newHolidays[k]["brandType"] = "destinations";
						}
					}
					if(newHolidays[k]["priceDiff"]){
						var num = newHolidays[k]["priceDiff"];
						newHolidays[k]["priceDiff"] = Math.abs(num);
					}
					if(newHolidays[k]["notes"]){
						var str = "";
						var length = 0;
						if(newHolidays[k]["truncate"]){
						length = 10;
						}
						else{
						length = newHolidays[k]["truncate"];
						}
						str = newHolidays[k]["notes"];
						newHolidays[k]["notesTruncated"] = str.substring(0, length);
					}
					//wishlistEntryid null handling
					
					if(newHolidays[k]["wishlistEntryId"] == null){
						if(newHolidays[k]["datePassedWishlistEntryId"]){
							newHolidays[k]["wishlistEntryId"]= newHolidays[k]["datePassedWishlistEntryId"];
						}
						else if(newHolidays[k]["soldoutWishlistEntryId"]){
							newHolidays[k]["wishlistEntryId"]= newHolidays[k]["soldoutWishlistEntryId"];
						}
					}
					
					//Low availability indicator
					
					if(newHolidays[k]["lowAvailabilityIndicator"]){
						if(newHolidays[k]["availableRooms"] > 1){
						newHolidays[k]["availableRoomsText"] = "Only "+newHolidays[k]["availableRooms"]+" rooms left";
						}
						else{
							newHolidays[k]["availableRoomsText"] = "Only "+newHolidays[k]["availableRooms"]+" room left";
						}
					}				
					
				}
				if(searchResultsComponent.component == null){
					searchResultsComponent.component = "";
				}
				newHolidays["availableRoomsToolTipText"] = "We\\'ve almost sold out of the room type that\\'s included in this holiday price. Other room types may be available on the Options page, but the cost of these might be higher. We may occasionally put more rooms on sale at a later date";
				newHolidays["component"] = searchResultsComponent.component;
				newHolidays["login"] = searchResultsComponent.login;
				
				//Adults dropdown pre-selection
				if(searchResultsComponent.component != ""){
					newHolidays["selected0"] = "";
					newHolidays["selected1"] = "";
					newHolidays["selected2"] = "";
					newHolidays["selected3"] = "";
					newHolidays["selected4"] = "";
					newHolidays["selected5"] = "";
					newHolidays["selected6"] = "";
					newHolidays["selected7"] = "";
					newHolidays["selected8"] = "";
					newHolidays["selected9"] = "";
					
					var selectedId = "selected"+searchResultsComponent.component.minNoOfAdult;
					newHolidays[selectedId] = "selected";
				}
				
				
				
				if(CommaSeparatedWishlistIds){
					CommaSeparatedWishlistIds = CommaSeparatedWishlistIds.substr(0, CommaSeparatedWishlistIds.length - 1);
				}
			}
			
			
			
			
			var html = searchResultsComponent.renderTmpl(searchResultItemTmpl,{tuiWebrootPath:tuiWebrootPath,holidays: newHolidays,CSWishlistIds:CommaSeparatedWishlistIds,soldOut:soldOutFlag,datePassed: datePasssedFlag });           

            if (html) {
                dojo.place(html, searchResultsComponent.domNode, "only");
                dojo.parser.parse(searchResultsComponent.domNode);
                if(dojo.byId("compare-panel") != undefined){
				dojo.byId("compare-panel").style.display="block";
				}
				searchResultsComponent.bindWithHolidaysList();
				
            }
			
			
		}
	});
	
	return tui.widget.customeraccount.savedHolidaysList;
});