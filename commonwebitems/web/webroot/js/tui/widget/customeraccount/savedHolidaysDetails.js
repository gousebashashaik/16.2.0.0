define ("tui/widget/customeraccount/savedHolidaysDetails", [
													"dojo",											  	
													"dojo/cookie",
													"dojo/query",
													"dojo/has",
													"dojox/validate/web",
													"tui/validate/check",
													"dojo/_base/array",
													"dojo/dom-style",
													"dijit/focus",
													"dojo/text!tui/widget/customeraccount/view/templates/searchResultItemTmpl.html",
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
													"tui/widget/customeraccount/AjaxLoader"
													
													
							    			  ], function(dojo,  cookie, query, has, validate,check, arrayUtil, domStyle, focusUtil, searchResultItemTmpl,xhr,fx, ajaxloader){

		dojo.declare("tui.widget.customeraccount.savedHolidaysDetails", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable, tui.widget.customeraccount.AjaxLoader], {
	
		CommaSeparatedWishlistIds: null,
		tmpl: searchResultItemTmpl,
		
		holidays: null,
		component:null,
		login:null,
		tuiWebrootPath:tuiWebrootPath,
		
		onAfterTmplRender: function(){
			var searchResultsComponent = this;					 
		},
		postCreate: function() {
			var searchResultsComponent = this;
            
            searchResultsComponent.renderSavedHolidays();	
			searchResultsComponent.bindLoginCreateAccountLinks();
			
		},
		resetTheForm:function(){
			if(dojo.byId("manageAccountsigninpopupform") != undefined){
				var container = dojo.byId("manageAccountsigninpopupform");           
								
				dojo.query('input', container).forEach(
				  function(inputElem){  console.log(inputElem);           		   
					  var field = inputElem.id; console.log("type::"+inputElem.type);
					  if(inputElem.type != "submit" && inputElem.type != "button"){
					  focusUtil.focus(dojo.byId(field));
					  dojo.query("#"+field).attr("value","");
					  dojo.byId(field).blur();
					  }
				  }
				);
		    }
			if(dojo.byId("CreateAccountpopupform") != undefined){
				var container = dojo.byId("CreateAccountpopupform");           
								
				dojo.query('input', container).forEach(
				  function(inputElem){  console.log(inputElem);           		   
					  var field = inputElem.id; console.log("type::"+inputElem.type);
					  if(inputElem.type != "submit" && inputElem.type != "button"){
					  focusUtil.focus(dojo.byId(field));
					  dojo.query("#"+field).attr("value","");
					  dojo.byId(field).blur();
					  }
				  }
				);
		    }
			if(dojo.byId("manageAccountForgotPasswordpopupform") != undefined){
				var container = dojo.byId("manageAccountForgotPasswordpopupform");           
								
				dojo.query('input', container).forEach(
				  function(inputElem){  console.log(inputElem);           		   
					  var field = inputElem.id; console.log("type::"+inputElem.type);
					  if(inputElem.type != "submit" && inputElem.type != "button"){
					  focusUtil.focus(dojo.byId(field));
					  dojo.query("#"+field).attr("value","");
					  dojo.byId(field).blur();
					  }
				  }
				);
		    }
		},
	    bindLoginCreateAccountLinks:function(){
				var linkComponent = this;
				/*
				dojo.query(".manageaccountlink").onclick(function(){
						dojo.query(".manageaccountlink.ensLinkTrack")[0].click();
						var successUrl = this.getAttribute("url");
						var commonSignIn = this.getAttribute("commonSignIn");
						
						//return false;
						if(dojo.byId("manageAccountsigninpopupform") != undefined){
							dojo.byId("manageAccountsigninpopupform").setAttribute("successUrl", successUrl);
							dojo.byId("manageAccountsigninpopupform").setAttribute("commonSignIn", commonSignIn);
						}				
						linkComponent.showHidePopup("wouldyouliketoGo", "show");
				});
				*/
                dojo.query(".createaccountlink").onclick(function(){
					var successUrl = this.getAttribute("url");
					if(dojo.byId("CreateAccountpopupform") != undefined){
						dojo.byId("CreateAccountpopupform").setAttribute("successUrl", successUrl);					
					}
					linkComponent.showHidePopupCreateAccount("show");
				});
                dojo.query(".CreateAccountLinkFromOverlay").onclick(function(){				
					linkComponent.showHidePopup("wouldyouliketoGo", "hide");
					linkComponent.showHidePopup("GotoCreateAccount", "show");
				});
				console.log("reset in listing page1");
                linkComponent.resetTheForm();	
				console.log("reset in listing page2");
		},
        showHidePopupCreateAccount:function(type){
			var root = document.getElementsByTagName( 'html' )[0];
			if(type == "show"){
				dojo.removeClass("GotoCreateAccount", "hide");
				dojo.addClass("GotoCreateAccount", "show");
				dojo.addClass(root, "modal-open");
			}
			else{
				dojo.removeClass("GotoCreateAccount", "show");
				dojo.addClass("GotoCreateAccount", "hide");
				dojo.removeClass(root, "modal-open");
			}
		},
		showHidePopup:function(PopupId, type){
			var root = document.getElementsByTagName( 'html' )[0];
			if(type == "show"){
				dojo.removeClass(PopupId, "hide");
				dojo.addClass(PopupId, "show");
				dojo.addClass(root, "modal-open");
			}
			else{
				dojo.removeClass(PopupId, "show");
				dojo.addClass(PopupId, "hide");
				dojo.removeClass(root, "modal-open");
			}
		},
		renderSavedHolidays: function(data){
		
			var FCTHoptionsDisplayFlag = 0;
			var FCoptionsDisplayFlag = 0;
			var THoptionsDisplayFlag = 0;
			
			var searchResultsComponent = this;	
			if(data){
				newHolidays = data;
			}
			else{				
				newHolidays = (searchResultsComponent.jsonData);
			}

			var soldOutFlag = 0;
			var datePasssedFlag = 0;
			var priceChangeFlag = 0;
			var CommaSeparatedWishlistIds = "";
			if(newHolidays){
				
				var len = newHolidays.length;
				
				
				var overlayData = [];
				for(var k = 0; k < len ; k++ ){
				    
					var Pricediff = parseInt(newHolidays[k].priceDiff, 10);
					if((Pricediff > 0 || Pricediff < 0) && priceChangeFlag == 0){
						priceChangeFlag = 1;
					}
					var extrasPriceOverLayContent = "";
					if(newHolidays[k].soldoutWishlistEntryId != 0 && newHolidays[k].soldoutWishlistEntryId != null && newHolidays[k].soldoutWishlistEntryId != "null"){
						
						CommaSeparatedWishlistIds = newHolidays[k].soldoutWishlistEntryId+",";
						
						if(soldOutFlag == 0){
							soldOutFlag = 1;
						}
					}
					else if(newHolidays[k].datePassedWishlistEntryId != 0 && newHolidays[k].datePassedWishlistEntryId != null && newHolidays[k].datePassedWishlistEntryId != "null"){
						
						CommaSeparatedWishlistIds = newHolidays[k].datePassedWishlistEntryId+",";
						
						if(datePasssedFlag == 0){
							datePasssedFlag = 1;
						}
					}
					var excurstionLen = 0;
					if(newHolidays[k].ancillariesBreakup != null){
					var excurstionLen = newHolidays[k].ancillariesBreakup.length ;
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
					
					newHolidays[k]["gallery"] = dojo.toJson(newHolidays[k]);
					
					if(newHolidays[k]["brandType"] == "TH"){						
						THoptionsDisplayFlag = 1;
					}
					if(newHolidays[k]["brandType"] == "FC"){
						FCoptionsDisplayFlag = 1;
						newHolidays[k]["brandTypeDomain"] = "holiday";						
					}
					
					if(newHolidays[k]["brandType"] == "TH"){
						newHolidays[k]["brandType"] = "destinations";
						newHolidays[k]["domain"] = "TH";
						newHolidays[k]["brandTypeDomain"] = "destinations";
					}
					if(newHolidays[k]["brandType"] == "FC_TH" || newHolidays[k]["brandType"] == "TH_FC"){
						if(tuiSiteName == "firstchoice"){
						newHolidays[k]["brandTypeDomain"] = "holiday";
                        }	
						if(tuiSiteName == "thomson"){
						newHolidays[k]["brandTypeDomain"] = "destinations";
                        }
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
					
				}
				if(searchResultsComponent.component == null){
					searchResultsComponent.component = "";
				}
				newHolidays["component"] = searchResultsComponent.component;
				newHolidays["login"] = searchResultsComponent.login;
				//newHolidays["login"] = "11";
				
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
				
				newHolidays["componentstring"] = JSON.stringify(searchResultsComponent.component);
				newHolidays["loginstring"] = JSON.stringify(searchResultsComponent.login);
				//newHolidays["loginstring"] = JSON.stringify("22");
				newHolidays["holidaysstring"] = JSON.stringify(searchResultsComponent.jsonData);
				
				if(CommaSeparatedWishlistIds){
					CommaSeparatedWishlistIds = CommaSeparatedWishlistIds.substr(0, CommaSeparatedWishlistIds.length - 1);
				}
			}
					
			FCTHoptionsDisplayFlag = FCoptionsDisplayFlag + THoptionsDisplayFlag;
			
			var html = searchResultsComponent.renderTmpl(searchResultItemTmpl,{tuiWebrootPath:tuiWebrootPath,holidays: newHolidays,CSWishlistIds:CommaSeparatedWishlistIds,soldOut:soldOutFlag,datePassed: datePasssedFlag, priceChangeFlag: priceChangeFlag, FCTHoptionsDisplayFlag:FCTHoptionsDisplayFlag });           

            if (html) {
                dojo.place(html, searchResultsComponent.domNode, "only");
                dojo.parser.parse(searchResultsComponent.domNode);
                
            }
		}
	});
	
	return tui.widget.customeraccount.savedHolidaysDetails;
});