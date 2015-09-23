define("tui/flightdeals/FlightDealsResultsController",[
      "dojo",
      "dojo/ready",
      "dojo/_base/declare",
      "dojo/has",
	  "dojo/on",
	  'dojo/query',
	  "dojo/topic",
	  "dojo/_base/lang",
	  "dojo/dom-attr",
	  "dojo/dom-class",
	  "dojo/parser",
	  "dojo/dom-construct",
	  "dojo/dom",
	  "dojo/text!tui/flightdeals/templates/flightDealsSearchResultsTmpl.html",
	  "dojo/text!tui/flightdeals/templates/flightDealsIndividualResultsTmpl.html",
	  "tui/flightdeals/FlightDealsController",
	  "dojox/widget/Standby",
	  "dijit/registry",
	  "tui/searchResults/view/Tooltips",
	  "dojo/date/locale",
	  "tui/search/store/SearchPanelMemory",
	  "tui/flightdeals/FlightDealsPax",
	  "tui/widget/pagination/Pagination",
	  "tui/widget/form/flights/DealsTitlePane",
	  "tui/widget/form/SelectOption",
	  "dojo/NodeList-traverse",
	  "tui/flightdeals/view/sliders/dealsBudgetFilter",
	  "tui/flightdeals/model/DealsPanelModel"
	  ],function(dojo,ready, declare, has, on, query, topic, lang, domAtrr, domClass, parser, domConstruct,dom,
			  flightDealsSearchResultsTmpl,flightDealsIndividualResultsTmpl, FlightDealsController, Standby, registry, Tooltips, locale, SearchPanelMemory, FlightDealsPax){

	declare("tui.flightdeals.FlightDealsResultsController",[FlightDealsController],{
		/************************* properties ******************************/
		tmpl:flightDealsSearchResultsTmpl,

		currency : dojoConfig.currency,

		//jsonData : {"itinerary":[{"offerCount":42,"destinationName":"Cape Varde","destinationCode":"CPV","countryName":"Spain","price":580,"pricePP":290,"discount":20,"discountPP":10,"id":"TCX96303968131212504TCX96313980596212947"},{"offerCount":54,"destinationName":"Cape Varde","destinationCode":"CPV","countryName":"Spain","price":580,"pricePP":200,"discount":20,"discountPP":10,"id":"TCX96303968131212504TCX96313980596212947"},{"offerCount":32,"destinationName":"Cape Varde","destinationCode":"CPV","countryName":"Spain","price":580,"pricePP":390,"discount":20,"discountPP":10,"id":"TCX96303968131212504TCX96313980596212947"},{"offerCount":60,"destinationName":"Cape Varde","destinationCode":"CPV","countryName":"Spain","price":580,"pricePP":400,"discount":20,"discountPP":10,"id":"TCX96303968131212504TCX96313980596212947"},{"offerCount":21,"destinationName":"Cape Varde","destinationCode":"CPV","countryName":"Spain","price":580,"pricePP":240,"discount":20,"discountPP":10,"id":"TCX96303968131212504TCX96313980596212947"},{"offerCount":55,"destinationName":"Cape Varde","destinationCode":"CPV","countryName":"Spain","price":580,"pricePP":270,"discount":20,"discountPP":10,"id":"TCX96303968131212504TCX96313980596212947"},{"offerCount":22,"destinationName":"Cape Varde","destinationCode":"CPV","countryName":"Spain","price":580,"pricePP":210,"discount":20,"discountPP":10,"id":"TCX96303968131212504TCX96313980596212947"},{"offerCount":72,"destinationName":"Cape Varde","destinationCode":"CPV","countryName":"Spain","price":580,"pricePP":450,"discount":20,"discountPP":10,"id":"TCX96303968131212504TCX96313980596212947"},{"offerCount":130,"destinationName":"Cape Varde","destinationCode":"CPV","countryName":"Spain","price":580,"pricePP":290,"discount":20,"discountPP":10,"id":"TCX96303968131212504TCX96313980596212947"},{"offerCount":12,"destinationName":"Cape Varde","destinationCode":"CPV","countryName":"Spain","price":580,"pricePP":230,"discount":20,"discountPP":10,"id":"TCX96303968131212504TCX96313980596212947"}],"totalCount":"248","flyFrom":"Any London","goTo":"Any Destination","duration":"7"},

		jsonData : null,

		lastScrollTop : 0,

		scrlTop : 0,

		scrlDirection: "",

		itineraryCount :0,

		individualResultsStore:null,

		subscribableMethods: ["getSelectedFlightDetails"],

		xhrFlag: true,


		/*************************** Properties End *****************************/


		/*************************** Methods ************************************/


		constructor: function(){
			var FlightDealsResultController = this;

		},
		postCreate: function(){
			var FlightDealsResultController = this,dealsSavedSearch, budgetData;
				FlightDealsResultController.dealsPanelModel.dealsFilterStatus = false;
				query("#dealsSearchResultHeading").style("display","block");

				dojo.style(dojo.query(".filter-options")[0], {"display":"block"});
				//dojo.setStyle(dom.byId("results"), "display", "block");
				//dom.byId("dealsbutton").innerHTML = "UPDATE DEALS";

				FlightDealsResultController.dealsSavedSearch = FlightDealsResultController.dealsPanelModel.getDealsSavedSearch(FlightDealsResultController.jsonData);

				FlightDealsResultController.dealsPanelModel.onRetrieveSavedObject(FlightDealsResultController.dealsSavedSearch);

				FlightDealsResultController.budgetFilterValidation();

				//FlightDealsResultController.mapSavedSearchtoModel(FlightDealsResultController.dealsSavedSearch);

				FlightDealsResultController.individualResultsStore = new SearchPanelMemory();

				FlightDealsResultController.itineraryCount= FlightDealsResultController.jsonData.itineraryCount;

				FlightDealsResultController.renderDealsSearchResults(true);
				budgetData = FlightDealsResultController.getBudgetFilterData();
				FlightDealsResultController.setBudgetFilterData(budgetData);
				FlightDealsResultController.fetchMoreDeals();

				FlightDealsResultController.attachEvents();

				FlightDealsResultController.tagElement(FlightDealsResultController.domNode, "FO_FLIGHT_DEALS_RESULTS");
		},
		budgetFilterValidation : function(){
			var FlightDealsResultController = this;
			if(FlightDealsResultController.jsonData.itinerary.length < 2){
				dojo.byId("budgetFilterCmp").style.visibility="hidden";
				//FlightDealsResultController.dealsPanelModel.updateBugetState();
			}

		},

		//render deals search results
		renderDealsSearchResults: function(enableDep){ /*attachEvent: bolean if false then render the results*/
			if(enableDep == null || enableDep == undefined) enableDep = false;
			var FlightDealsResultController = this,targetNode, data, html;

			if(!FlightDealsResultController.jsonData) return;
				data = {
					currency 	:	FlightDealsResultController.currency,
					itinerarys	:	FlightDealsResultController.jsonData.itinerary,
					oneWay		:	FlightDealsResultController.dealsSavedSearch == null ? "" : FlightDealsResultController.dealsSavedSearch.oneWay

				};

				FlightDealsResultController.dealsPanelModel.startNumber =  FlightDealsResultController.dealsPanelModel.pageSize;
				FlightDealsResultController.dealsPanelModel.endNumber =  FlightDealsResultController.dealsPanelModel.startNumber + FlightDealsResultController.dealsPanelModel.pageSize;


				html = dojo.trim(tui.dtl.Tmpl.createTmpl(data, flightDealsSearchResultsTmpl));

				targetNode = dom.byId("deals-results") || query("#dealSearchResultsContainer",FlightDealsResultController.domNode)[0];
				dojo.html.set(targetNode,html,{
					parseContent: true
				});

				if(dojo.isIE == 8){ //script event is not working for IE8 so adding programmatically.

					var grpItems = query("#dealSearchResultsContainer .dealsGroupItem.dijitTitlePane",FlightDealsResultController.domNode);
					for(var i=0; i<grpItems.length; i++){
						(function(i){
							var totalCount;
							if(registry.byNode(grpItems[i]).dataset !== undefined){
								totalCount = registry.byNode(grpItems[i]).focusNode.dataset.totalCount;
							} else {
								totalCount = registry.byNode(grpItems[i]).focusNode.getAttribute("data-totalCount");
							}
							on(registry.byNode(grpItems[i]),"click",function(e){
								FlightDealsResultController.OnShowEvent(this,totalCount,e);
							});
							console.log(totalCount);
						}(i));
					}
				}
				dojo.removeClass(query('.loadingDivCnt', FlightDealsResultController.domNode)[0],"loading");
				FlightDealsResultController.populateFlightCount();

				FlightDealsResultController.enableFlightDeptFilterValues();

		},

		populateFlightCount : function(){
			var FlightDealsResultController = this,anyLondonCnt,anyDestinationCnt,selectedDuration, savedSearch,displayableText,totalcount ;
			totalcount = FlightDealsResultController.jsonData.totoalcount == null ? 0 : FlightDealsResultController.jsonData.totoalcount;
			displayableText = totalcount == 1 ? "flight" : "flights";
			dojo.byId("flightCount").innerHTML = totalcount + "</span>" + " " + "<span>"+ displayableText + "</span>";
			tui.analytics.page.Results= totalcount;
				//dojo.byId("resultViewPageTitle").innerHTML =  FlightDealsResultController.jsonData.flightSearchCriteria.pageTitle;
		},

		mapSavedSearchtoModel : function(dealsSavedSearch){
			var flightDealsResultController = this;
				dealsSavedSearch.toAirports = (dealsSavedSearch.arrivalAirportCode) ? dealsSavedSearch.arrivalAirportCode : [];
				dealsSavedSearch.fromAirports = (dealsSavedSearch.departureAirportCode) ? dealsSavedSearch.departureAirportCode : [];
		},

		enableFlightDeptFilterValues : function(){
			var FlightDealsResultController = this,outSloats,
				objArry = ['dept1','dept2','dept3','dept4'],
				outSloats = FlightDealsResultController.jsonData.outSlots;

				_.forEach(objArry,function(item){
					dijit.byId(item).setDisabled(true);
				});

				_.forEach(outSloats,function(sloat){
					if(sloat === "A"){
		        		dijit.byId("dept1").setDisabled(false);
		        	}else if(sloat === "B"){
		        		dijit.byId("dept2").setDisabled(false);
		        	}else if(sloat === "C"){
		        		dijit.byId("dept3").setDisabled(false);
		        	}else if(sloat === "D"){
		        		dijit.byId("dept4").setDisabled(false);
		        	}
				});
		},


		reGenerateItineraryObject : function(itinerarys){
			var flightDealsResultController = this;

				return _.map(lang.clone(itinerarys), function (itinerary, index) {
					itinerary.showComp = true;

					if (itinerary.outbound ) {
			    		itinerary.outbound.schedule.depDate	 		= flightDealsResultController.format(new Date(itinerary.outbound.schedule.departureDate),"EEE dd MMM yyy");
			    		itinerary.outbound.schedule.depDateFormat 	= flightDealsResultController.format(new Date(itinerary.outbound.schedule.departureDate),"yyyy-MM-dd");

			    		itinerary.outbound.schedule.arrDate 		= flightDealsResultController.format(new Date(itinerary.outbound.schedule.arrivalDate),"EEE dd MMM yyy");
			    		itinerary.outbound.schedule.arrDateFormat 	= flightDealsResultController.format(new Date(itinerary.outbound.schedule.arrivalDate),"yyyy-MM-dd");
}

				    if (itinerary.inbound) {
			    		itinerary.inbound.schedule.depDate			= flightDealsResultController.format(new Date(itinerary.inbound.schedule.departureDate),"EEE dd MMM yyy");
			    		itinerary.inbound.schedule.depDateFormat 	= flightDealsResultController.format(new Date(itinerary.inbound.schedule.departureDate),"yyyy-MM-dd");

			    		itinerary.inbound.schedule.arrDate 			= flightDealsResultController.format(new Date(itinerary.inbound.schedule.arrivalDate),"EEE dd MMM yyy");
			    		itinerary.inbound.schedule.arrDateFormat 	= flightDealsResultController.format(new Date(itinerary.inbound.schedule.arrivalDate),"yyyy-MM-dd");

				    }
					return itinerary;
				});
		},


	    format: function(date, fmt){
	    	return dojo.date.locale.format(date, {selector:"date", datePattern:fmt });
	    },


		OnShowEvent: function(obj,totalCount,e){
			var FlightDealsResultController = this,countryCode="",targetNode, data, html;
			var targetNode = query(e.target).closest(".dijitTitlePaneTitle")[0] == undefined ? query(e.target) : query(e.target).closest(".dijitTitlePaneTitle")[0];
			if(!dojo.hasClass(targetNode,"dijitTitlePaneTitle")) return false;
			if(obj.open == false) {
				FlightDealsResultController.destroyIndividualRes(obj.containerNode);
				loadingDom = domConstruct.toDom('<div class="loadingDivCnt loading deals"></div>');
				domConstruct.place(loadingDom,obj.containerNode);
				return false;
			}
			if(obj.focusNode.dataset !== undefined){
				countryCode = obj.focusNode.dataset.airport;
			} else {
				countryCode = obj.focusNode.getAttribute("data-airport");
			}
			FlightDealsResultController.dealsPanelModel.searchType = "expanded";
			FlightDealsResultController.dealsPanelModel.destinationCode.push(countryCode);
			FlightDealsResultController.dealsPanelModel.responseType = "";


			var request = FlightDealsResultController.dealsPanelModel.doXhrPost("../ws/expandedCriteria");

			request.then(function(data){
				query(".dealsGroupItem.dijitTitlePane",dom.byId("deals-results")).forEach(function(grpItem){
					if(registry.byNode(grpItem).id != obj.id && registry.byNode(grpItem).open) registry.byNode(grpItem).toggle();

				});
				setTimeout(function(){
					var result=dojo.query(targetNode)[0];
					result.scrollIntoView();
				},300)

				var itinerarys = FlightDealsResultController.reGenerateItineraryObject(data.itinerary);
				if(!data.itinerary) return;
					data = {
						currency 	:	FlightDealsResultController.currency,
						itinerarys	:	itinerarys,
						itineraryCount : totalCount,
						showPagination : true
					};

					 _.each(itinerarys, function (itinerary) {
						 FlightDealsResultController.individualResultsStore.put(itinerary)
					 });


					html = dojo.trim(tui.dtl.Tmpl.createTmpl(data, flightDealsIndividualResultsTmpl));

					obj.set("content",html);
					parser.parse(obj.containerNode);
					//dojo.removeClass(query('.loadingDivCnt', obj.containerNode)[0],"loading");
					FlightDealsResultController.hurryUpToolTip(obj.containerNode);

					if(data.showPagination){

						if(totalCount <= 60) {
							if(totalCount <= 60 && totalCount > 40) {
								dijit.byNode(query(".dojoPage",obj.containerNode)[0]).set("pagesPerSide",2);
							}
							dijit.byNode(query(".dojoPage",obj.containerNode)[0]).set("showPreviousNext",false);

							if(dojo.isIE >= 8) dijit.byNode(query(".dojoPage",obj.containerNode)[0]).render();
						}

						dijit.byNode(query(".dojoPage",obj.containerNode)[0]).on("page",function(evt){
							FlightDealsResultController.PaginationEvent(evt,this,obj.containerNode,countryCode);
						})
					}


			}, function(err){
			    // Do something when the process errors out

			  })

			FlightDealsResultController.dealsPanelModel.destinationCode = [];

		},

		hurryUpToolTip: function(containerNode){
			query(".dealsSeatsCnt .seatslabel",containerNode).on("mouseover,mouseout",function(evt){
				if(evt.type=="mouseover"){
					new Tooltips({
		    			 refId : evt.target,
		    			 text: 'This shows how many of this seat type are available for your flights at the moment.<br/> There may be more of this seat type available for different flight dates and <br/>  combinations. We may also occasionally <br/>put more seats on sale at a later date.',
		    			 floatWhere : 'position-top-center',
		    			 className: "deal-min-avail",
		    			 setPosOffset: function(){
		    				  this.posOffset = {top: -46, left: 0};
		    			 }
		    		 }).placeAt(evt.target).open();
    			}else{
    				//if(dojo.isIE == 8){
    					dojo.query(".deal-min-avail.loaded").forEach(dojo.destroy);
    				//}
    	    		dojo.query(".tooltip.deal-min-avail").forEach(dojo.destroy);
    			}

			})
		},
		PaginationEvent : function(evt,widget,containerNode,countryCode){
			var FlightDealsResultController = this,loadingDom,request;
			loadingDom = domConstruct.toDom('<div class="loadingDivCnt loading"></div>');
			var h = dojo.position(query(".flightresults",containerNode)[0]).h;
			dojo.style(loadingDom,"height",h+"px");
			query(".loadingDivCnt",containerNode).forEach(dojo.destroy)
			domConstruct.place(loadingDom,containerNode);
			FlightDealsResultController.dealsPanelModel.destinationCode.push(countryCode);
			FlightDealsResultController.dealsPanelModel.pageCount = widget.currentResultStart;
			FlightDealsResultController.dealsPanelModel.searchType = "paginate";
			request = FlightDealsResultController.dealsPanelModel.doXhrPost("../ws/expandedCriteria");
			request.then(function(data){
				var itinerary = FlightDealsResultController.reGenerateItineraryObject(data.itinerary);
				if(!data.itinerary) return;
					data = {
						currency 	:	FlightDealsResultController.currency,
						itinerarys	:	itinerary,
						itineraryCount : itinerary.length,
						showPagination : false
					};

					FlightDealsResultController.individualResultsStore = new SearchPanelMemory({data: itinerary});

					html = dojo.trim(tui.dtl.Tmpl.createTmpl(data, flightDealsIndividualResultsTmpl));

					var individualRes = registry.findWidgets(query(".flightresults",containerNode)[0]);
					_.each(individualRes,function(w){
						if(w.declaredClass !=="tui.widget.pagination.Pagination")
						w.destroyRecursive(true);
					});
					domConstruct.empty(query(".flightresults",containerNode)[0]);

					domConstruct.place(domConstruct.toDom(html),query(".flightresults",containerNode)[0]);

					parser.parse(containerNode);

					FlightDealsResultController.hurryUpToolTip(containerNode);

					domConstruct.destroy(query(".loadingDivCnt",containerNode)[0]);


			},function(err){
				//domConstruct.destroy(query(".loadingDivCnt",containerNode)[0]);

			})

			FlightDealsResultController.dealsPanelModel.destinationCode = [];
			FlightDealsResultController.dealsPanelModel.pageCount = "";
		},

		destroyIndividualRes: function(containerNode){
			var individualRes = registry.findWidgets(containerNode);
			_.each(individualRes,function(w){
				w.destroyRecursive(true);
			});
			domConstruct.empty(containerNode);
		},
		attachEvents: function(){
			var FlightDealsResultController = this;
			on(registry.byId("sortByFilter"),"change",function(evt){
				FlightDealsResultController.getSortByResults(this.selectNode.value);
			});

			//click event for apply button
			on(dom.byId("dealsApplybutton"),"click",function(evt){
				if(domClass.contains(this,"disabled")) return;
				FlightDealsResultController.getFilteredResults();
			});

			on(dojo.byId("scrollTop"),"click",function(){
				window.scrollTo(0, 0);
			});
			on(dojo.byId("scrollToTop"),"click",function(){
				window.scrollTo(0, 0);
			});

			on(dojo.byId("clear-filter"),"click",function(){
				if(domClass.contains(this,"disabled")) return;
				FlightDealsResultController.clearFilters();
				dojo.addClass(dojo.query(".clear-filter")[0],"disabled");
			});
		},

		getSortByResults: function(value){
			var FlightDealsResultController = this, jsonData;
			if(!FlightDealsResultController.xhrFlag) return;
			var height = dojo.position(dom.byId("dealSearchResultsContainer")).h;
			if(height > 600){
				query('.loadingDivCnt', FlightDealsResultController.domNode).style("background-position","50% 15%");
			} else {
				query('.loadingDivCnt', FlightDealsResultController.domNode).style("background-position","50% 50%");
			}
			query('.loadingDivCnt', FlightDealsResultController.domNode).style("height",(height+100)+"px");
			dojo.addClass(query('.loadingDivCnt', FlightDealsResultController.domNode)[0],"loading");

			FlightDealsResultController.dealsPanelModel.responseType = "";
			FlightDealsResultController.dealsPanelModel.searchType= value;
			FlightDealsResultController.dealsPanelModel.sortBy = value;
			jsonData =  FlightDealsResultController.dealsPanelModel.createQueryObjForServerCall();
			FlightDealsResultController.dealsPanelModel.SetDealsSavedSearch(jsonData);

			FlightDealsResultController.xhrFlag = false;

			var request = FlightDealsResultController.dealsPanelModel.doXhrPost("../ws/sortby");

			request.then(function(data){
				FlightDealsResultController.jsonData = data;

				var titlePaneWidgets = registry.findWidgets(dom.byId("dealSearchResultsContainer"));

				_.each(titlePaneWidgets,function(w){
					w.destroyRecursive(true);
				});
				dojo.empty(dom.byId("deals-results"));
				FlightDealsResultController.renderDealsSearchResults();
				dojo.removeClass(query('.loadingDivCnt', FlightDealsResultController.domNode)[0],"loading");
				query('.loadingDivCnt', FlightDealsResultController.domNode).style("height","0px");
				FlightDealsResultController.xhrFlag = true;
			}, function(err){
			    // Do something when the process errors out
				dojo.removeClass(query('.loadingDivCnt', FlightDealsResultController.domNode)[0],"loading");
				query('.loadingDivCnt', FlightDealsResultController.domNode).style("height","0px");
				FlightDealsResultController.xhrFlag = true;
			  })
		},
		setApplyFilterToModel: function(){
			var FlightDealsResultController = this;
			var widget = dijit.byId("budgetpp"),
			i = widget.current,
			currentDom = dojo.query("ul > li",widget.domNode)[i];
			dojo.query("ul > li.selected",widget.domNode).nextAll().addClass("disabled")


			if(i<4){
  			  widget.dealsPanelModel.maxPrice = currentDom == undefined ? "" :  currentDom.getAttribute("data-value");
  		  } else {
  			  widget.dealsPanelModel.maxPrice ="";
  		  }

			FlightDealsResultController.dealsPanelModel.outboundSlots = [];
			query(".filter-options .flightDepartureTime .dijitCheckBox.dijitChecked").forEach(function(item){
				FlightDealsResultController.dealsPanelModel.outboundSlots.push(registry.byNode(item).value);
			})

		},
		clearFilters: function(){
			dijit.byId("sortByFilter").setSelectedIndex(0);
			var FlightDealsResultController = this, jsonData;
			FlightDealsResultController.disableApplyBtn();
			var height = dojo.position(dom.byId("dealSearchResultsContainer")).h;
			if(height > 600){
				query('.loadingDivCnt', FlightDealsResultController.domNode).style("background-position","50% 15%");
			} else {
				query('.loadingDivCnt', FlightDealsResultController.domNode).style("background-position","50% 50%");
			}
			query('.loadingDivCnt', FlightDealsResultController.domNode).style("height",(height+100)+"px");
			dojo.addClass(query('.loadingDivCnt', FlightDealsResultController.domNode)[0],"loading");

			//FlightDealsResultController.setApplyFilterToModel();
			FlightDealsResultController.dealsPanelModel.maxPrice ="";
			FlightDealsResultController.dealsPanelModel.outboundSlots = [];



			FlightDealsResultController.dealsPanelModel.responseType = "C";

			FlightDealsResultController.dealsPanelModel.searchType = "filter";

			jsonData =  FlightDealsResultController.dealsPanelModel.createQueryObjForServerCall();
			//FlightDealsResultController.dealsPanelModel.SetDealsSavedSearch(jsonData);

			FlightDealsResultController.xhrFlag = false;

			var request = FlightDealsResultController.dealsPanelModel.doXhrPost("../ws/applyFilter");

			request.then(function(data){

				FlightDealsResultController.jsonData = data;

				var titlePaneWidgets = registry.findWidgets(dom.byId("dealSearchResultsContainer"));

				_.each(titlePaneWidgets,function(w){
					w.destroyRecursive(true);
				});
				FlightDealsResultController.itineraryCount= FlightDealsResultController.jsonData.itineraryCount;

				dojo.empty(dom.byId("deals-results"));
				FlightDealsResultController.renderDealsSearchResults();
				dojo.removeClass(query('.loadingDivCnt', FlightDealsResultController.domNode)[0],"loading");
				query('.loadingDivCnt', FlightDealsResultController.domNode).style("height","0px");
				FlightDealsResultController.xhrFlag = true;
				FlightDealsResultController.resetFilters();
			}, function(err){
			    // Do something when the process errors out
				dojo.removeClass(query('.loadingDivCnt', FlightDealsResultController.domNode)[0],"loading");
				query('.loadingDivCnt', FlightDealsResultController.domNode).style("height","0px");
				FlightDealsResultController.xhrFlag = true;
			  })
			  FlightDealsResultController.dealsPanelModel.responseType = "";

			FlightDealsResultController.dealsPanelModel.searchType = "";
		},
		resetFilters: function(){
			var FlightDealsResultController = this;
			dojo.query("ul > li",dijit.byId("budgetpp").domNode).nextAll().andSelf().removeClass("disabled selected");
			dijit.byId("budgetpp").resetSlider();
			dojo.query("ul > li",dijit.byId("budgetpp").domNode).last().addClass("selected");
			var outSloats = FlightDealsResultController.jsonData.outSlots;
			_.forEach(outSloats,function(sloat){
				if(sloat === "A"){
	        		dijit.byId("dept1").set("checked",false);
	        	}else if(sloat === "B"){
	        		dijit.byId("dept2").set("checked",false);
	        	}else if(sloat === "C"){
	        		dijit.byId("dept3").set("checked",false);
	        	}else if(sloat === "D"){
	        		dijit.byId("dept4").set("checked",false);
	        	}
			})

		},
		getFilteredResults: function(){
			dijit.byId("sortByFilter").setSelectedIndex(0);
			var FlightDealsResultController = this, jsonData;
			FlightDealsResultController.disableApplyBtn();
			var height = dojo.position(dom.byId("dealSearchResultsContainer")).h;
			if(height > 600){
				query('.loadingDivCnt', FlightDealsResultController.domNode).style("background-position","50% 15%");
			} else {
				query('.loadingDivCnt', FlightDealsResultController.domNode).style("background-position","50% 50%");
			}
			query('.loadingDivCnt', FlightDealsResultController.domNode).style("height",(height+100)+"px");
			dojo.addClass(query('.loadingDivCnt', FlightDealsResultController.domNode)[0],"loading");

			FlightDealsResultController.setApplyFilterToModel();

			FlightDealsResultController.dealsPanelModel.responseType = "C";

			FlightDealsResultController.dealsPanelModel.searchType = "filter";

			jsonData =  FlightDealsResultController.dealsPanelModel.createQueryObjForServerCall();
			//FlightDealsResultController.dealsPanelModel.SetDealsSavedSearch(jsonData);

			FlightDealsResultController.xhrFlag = false;

			var request = FlightDealsResultController.dealsPanelModel.doXhrPost("../ws/applyFilter");

			request.then(function(data){

				if(data.sessionTimeerrorflag){
					return;
				}

				FlightDealsResultController.jsonData = data;

				var titlePaneWidgets = registry.findWidgets(dom.byId("dealSearchResultsContainer"));

				_.each(titlePaneWidgets,function(w){
					w.destroyRecursive(true);
				});
				FlightDealsResultController.itineraryCount= FlightDealsResultController.jsonData.itineraryCount;

				dojo.empty(dom.byId("deals-results"));
				FlightDealsResultController.renderDealsSearchResults();
				dojo.removeClass(query('.loadingDivCnt', FlightDealsResultController.domNode)[0],"loading");
				query('.loadingDivCnt', FlightDealsResultController.domNode).style("height","0px");
				FlightDealsResultController.xhrFlag = true;

				dojo.removeClass(dojo.query(".clear-filter")[0],"disabled");

			}, function(err){
			    // Do something when the process errors out
				dojo.removeClass(query('.loadingDivCnt', FlightDealsResultController.domNode)[0],"loading");
				query('.loadingDivCnt', FlightDealsResultController.domNode).style("height","0px");

				var titlePaneWidgets = registry.findWidgets(dom.byId("dealSearchResultsContainer"));

				_.each(titlePaneWidgets,function(w){
					w.destroyRecursive(true);
				});

				dojo.empty(dom.byId("deals-results"));

				dojo.byId("flightCount").innerHTML = 0 + "</span>" + " " + "<span>flights</span>";

				query(".backTotop").style("display","none");

				dojo.removeClass(dojo.query(".clear-filter")[0],"disabled");

				FlightDealsResultController.xhrFlag = true;
			  })
			  FlightDealsResultController.dealsPanelModel.responseType = "";

			  FlightDealsResultController.dealsPanelModel.searchType = "";
		},



		fetchMoreDeals: function(){
			var FlightDealsResultController = this;
			on(window, "scroll", function(){
				var pos = FlightDealsResultController.isElementInViewport(dojo.byId("moreflightDeals"));
				if(FlightDealsResultController.detectScrollDirection.call() === "down"){

					if(FlightDealsResultController.spinner && FlightDealsResultController.spinner.domNode !== null){
						var widget = registry.byNode(FlightDealsResultController.spinner.domNode);
						domConstruct.destroy(widget.domNode);
						widget.destroyRecursive(true);
					}

					if( query("#flightCount").text()==="0 flights" || !FlightDealsResultController.xhrFlag || query(".section-overlay",FlightDealsResultController.domNode).length > 0) return false;
					FlightDealsResultController.spinner = FlightDealsResultController.standBy(dojo.byId("foDealsSearchResults"));

					FlightDealsResultController.dealsPanelModel.searchType = "lazy";
					//set pages

					if(FlightDealsResultController.dealsPanelModel.startNumber >= FlightDealsResultController.itineraryCount){
						return false;
					}

					FlightDealsResultController.spinner.show();
					FlightDealsResultController.onMoreDeals = FlightDealsResultController.dealsPanelModel.doXhrPost("../ws/itinerarylazyload");

					FlightDealsResultController.onMoreDeals.then(function(data){

						FlightDealsResultController.dealsPanelModel.startNumber +=  FlightDealsResultController.dealsPanelModel.pageSize;
						FlightDealsResultController.dealsPanelModel.endNumber +=  FlightDealsResultController.dealsPanelModel.pageSize;

						var itinerary = data.itinerary;
						if(!data.itinerary){
							FlightDealsResultController.spinner.hide();
							return;
						}
							data = {
								currency 	:	FlightDealsResultController.currency,
								itinerarys	:	itinerary,
								oneWay		:	FlightDealsResultController.dealsSavedSearch == null ? "" : FlightDealsResultController.dealsSavedSearch.oneWay
							};

							html = dojo.trim(tui.dtl.Tmpl.createTmpl(data, flightDealsSearchResultsTmpl));

							html = domConstruct.toDom(html);

							parser.parse(html);

							targetNode = query("#deals-results",FlightDealsResultController.domNode)[0];



							if(dojo.isIE == 8){ //script event is not working for IE8 so adding programmatically.

								var grpItems = query(html).children("div.dealsGroupItem");
								for(var i=0; i<grpItems.length; i++){
									(function(i){
										var totalCount;
										if(registry.byNode(grpItems[i]).dataset !== undefined){
											totalCount = registry.byNode(grpItems[i]).focusNode.dataset.totalCount;
										} else {
											totalCount = registry.byNode(grpItems[i]).focusNode.getAttribute("data-totalCount");
										}
										on(registry.byNode(grpItems[i]),"click",function(e){
											FlightDealsResultController.OnShowEvent(this,totalCount,e);
										});
									}(i));
								}
							}
							query(targetNode).append(html);

							FlightDealsResultController.spinner.hide();
					}, function(err){
					    // Do something when the process errors out
						FlightDealsResultController.spinner.hide();
				  });

				}
			});

		},

		isElementInViewport: function(elm){

			/*       	find if element is in viewport		*/

			if(elm){
				var pos = elm.getBoundingClientRect();
			    return (
			        pos.top >= 0 &&
			        pos.left >= 0 &&
			        pos.bottom <= (window.innerHeight || document.documentElement.clientHeight) &&
			        pos.right <= (window.innerWidth || document.documentElement.clientWidth)
			    );
			}
		},

		detectScrollDirection: function(){

			/*       	detect scroll direction		*/

			var FlightDealsResultController = this;
			FlightDealsResultController.scrlTop = dojo._docScroll().y;
	        if (FlightDealsResultController.scrlTop > FlightDealsResultController.lastScrollTop) {
	        	FlightDealsResultController.scrlDirection = "down";
	        } else {
	        	FlightDealsResultController.scrlDirection = "up";
	        }
	        FlightDealsResultController.lastScrollTop = FlightDealsResultController.scrlTop;
	        return  FlightDealsResultController.scrlDirection;
	    },


	    standBy : function(node){

	    	/*			get the spinner			*/

	    	var standby = new Standby({target: node, image:require.toUrl(dojoConfig.paths.webRoot+"/images/loader-maps.gif").toString(),color:"#fff"});
	        document.body.appendChild(standby.domNode);
	    	standby.startup();
	    	return standby;
        },

        getSelectedFlightDetails : function(flightPax){
			var flightDealsResultController = this,itinerary;
				itinerary = flightDealsResultController.individualResultsStore.get(flightPax.itineraryID);

				flightDealsResultController.form = query(".deals-form")[0];
				flightDealsResultController.form.action =dojoConfig.paths.webRoot+"/selectedsearch";

				flightDealsResultController.generatePostObject(flightPax , itinerary);

				flightDealsResultController.form.submit();

		},

		generatePostObject : function(flightPax, itinerary){
			var flightDealsResultController = this,properties;
			 	properties = {
					  	"flyingFrom[]"  :	itinerary.outbound.departureAirport.code,
						"flyingTo[]" 	:	itinerary.outbound.arrivalAirport.code,
						"depDate" 		:	itinerary.outbound.schedule.depDateFormat,
						"returnDate" 	:	(itinerary.inbound) ? itinerary.inbound.schedule.depDateFormat : null,
						"adults" 		:	flightPax.adultSelectCount,
						"children" 		:	flightPax.childSelectCount,
						"childAge" 		:	flightPax.childAges,
						"infants" 		:	flightPax.infantAges.length,
						"infantAge" 	:	flightPax.infantAges,
						"isOneWay" 		:	(itinerary.inbound) ? false : true,
						"selectedFlight": 	itinerary.id,
						"type"			:	"E",
						"duration"		:	itinerary.duration,
						"startDate"		:	itinerary.outbound.schedule.depDateFormat,
			 			"endDate"		: 	(itinerary.inbound) ? itinerary.inbound.schedule.arrDateFormat : ""
				      };

			  	_.forEach(properties, function (value, key) {
			  		flightDealsResultController.createInput(key, value, flightDealsResultController);
			  	});

		},

		createInput: function (name, value, formObject) {
			var flightDealsResultController = this,input;
				if(value === null) value = "";
				input = dojo.create("input", {"type": "hidden", "name": name, "value": value}, formObject.form);
		 },
		 //getting min, max  values of per person price from search results
        getBudgetFilterData: function(){
        	var FlightDealsResultController = this,budgetData,budgetObject;
        		budgetData = FlightDealsResultController.jsonData.pricePerPerson;
        		//Check the cookie value for

        			//converting string value to integer
        			if(budgetData.length >0){
            			budgetData[0] = parseInt(budgetData[0]);
                		budgetData[1] = parseInt(budgetData[1]);
            		}
            		budgetObject=[{"range":budgetData}];

        		FlightDealsResultController.dealsPanelModel.defaultBugetFilterRange = budgetObject;
        		FlightDealsResultController.dealsPanelModel.SetDealsSavedSearch();
        		//dojo.cookie("retainCriteria",dojo.toJson(FlightDealsResultController.dealsSavedSearch));

        		return budgetObject;
        },
        //updating budget filter model with min, max  values of per person price
        setBudgetFilterData: function(budgetData){
        	var FlightDealsResultController = this,budgetFilter;
        		budgetFilter = dijit.byId("budgetpp");
        		budgetFilter.model = budgetData[0];
        		budgetFilter.drawIfData(budgetFilter.model);


        }




		/********* Methods End*********/
	});
	return tui.flightdeals.FlightDealsResultsController;

})