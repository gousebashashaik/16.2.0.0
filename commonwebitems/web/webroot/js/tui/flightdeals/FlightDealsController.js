define("tui/flightdeals/FlightDealsController",[
      "dojo",
      "dojo/ready",
      "dojo/_base/declare",
      "dojo/has",
	  "dojo/on",
	  "dojo/query",
	  "dojo/topic",
	  "dojo/_base/lang",
	  "dojo/dom-attr",
	  "dojo/dom-class",
	  "dojo/parser",
	  "dojo/dom-construct",
	  "dojo/dom",
	  "tui/flightdeals/DealsExpandable",
	  "tui/flightdeals/FlyFromDealsExpandable",
	  "tui/flightdeals/GoToDealsExpandable",
	  "dojo/text!tui/flightdeals/templates/flightDealsSearchPanelTmpl.html",
	  "tui/widget/mixins/Templatable",
	  "tui/widget/_TuiBaseWidget",
	  "tui/search/nls/Searchi18nable",
	  "dijit/registry",
	  "dojo/cookie",
	  "dijit/form/CheckBox",
	  "dijit/form/RadioButton",
	  "tui/widget/form/flights/DealsTitlePane",
	  "tui/mvc/Controller",
	  "dojo/NodeList-traverse",
	  "tui/flightdeals/model/DealsPanelModel"
	  ],function(dojo,ready, declare, has, on, query, topic, lang, domAtrr, domClass, parser, domConstruct,dom,
			  DealsExpandable,FlyFromDealsExpandable,GoToDealsExpandable, flightDealsSearchPanelTmpl, Templatable,
			  _TuiBaseWidget, Searchi18nable,registry,cookie){

	declare("tui.flightdeals.FlightDealscontroller",[Templatable,_TuiBaseWidget,Searchi18nable],{
		/************************* properties ******************************/
		tmpl:flightDealsSearchPanelTmpl,

		dealsFilterStatus : false,


		/*************************** Properties End *****************************/


		/*************************** Methods ************************************/


		constructor: function(){
			var flightDealsController = this;


		},
		postCreate: function(){
			var flightDealsController = this;

				/*if(flightDealsController.jsonData){
					flightDealsController.dealsSavedSearch = flightDealsController.dealsPanelModel.getDealsSavedSearch(flightDealsController.jsonData);

					flightDealsController.dealsPanelModel.onRetrieveSavedObject(flightDealsController.dealsSavedSearch);
				}*/

				flightDealsController.addDisableMode(dom.byId('searchPanelSubtle'));

				flightDealsController.renderDealsSearchPanel();
		},

		//place deals search panel component
		renderDealsSearchPanel: function(){
			var flightDealsController = this;
			var targetNode = query("#dealSearchPanelContainer",flightDealsController.domNode)[0];
			dojo.html.set(targetNode,flightDealsController.tmpl,{
				parseContent: true
			});
			flightDealsController.attachShowDealsEvent();
		},



		attachShowDealsEvent: function(){
			var FlightDealsController = this,modelObj,
				moreFilter=query("#more-filter")[0],
				lessFilter=query(".less-filter-text")[0],
				container = dom.byId("container"),
				searchPanelSubtle = dom.byId("searchPanelSubtle"),
				modelObj = FlightDealsController.dealsPanelModel;
			on(registry.byId("dealsOneWay"),"click",function(){
				var dealsOneWay = this;
				if(dealsOneWay.checked){
					modelObj.hideWidget(dom.byId("results"));
					//modelObj.showWidget(dom.byId("more-filter"));
					//modelObj.hideWidget(dom.byId("less-filter"));
					//query(".filter-options",FlightDealsController.domNode).style("display","none");

				} else {
					//query(".filter-options",FlightDealsController.domNode).style("display","block");
					modelObj.showWidget(dom.byId("results"));
				}
				//FlightDealsController.dealsPanelModel.oneWay =  dealsOneWay.checked;

				//FlightDealsController.dealsPanelModel.updateBugetState();

				if(!FlightDealsController.dealsPanelModel.dealsFilterStatus){
					FlightDealsController.dealsPanelModel.enableDealsButton();
		    	}

			});
			on(moreFilter,"click",function(evt){
				//modelObj.showWidget(dom.byId("results"));
				modelObj.showWidget(dom.byId("less-filter"));
				modelObj.hideWidget(dom.byId("more-filter"));
                if(FlightDealsController.dealsPanelModel.dealsFilterStatus){
                	modelObj.hideWidget(dom.byId("departureTimeFliter"));
                }
                if(dojo.docScroll().y > 700) window.scrollTo(0, 186);
           });
			on(lessFilter,"click",function(evt){
				//modelObj.hideWidget(dom.byId("results"));
				modelObj.showWidget(dom.byId("more-filter"));
				modelObj.hideWidget(dom.byId("less-filter"));
				if(dojo.docScroll().y > 1086) window.scrollTo(0, 186);
            });

			on(dom.byId("container"),"click",function(evt){FlightDealsController.removeDisableforDeals()});
			if(dom.byId("foDealsSearchResults") !== null && dom.byId("foDealsSearchResults") !== undefined){
				on(dom.byId("foDealsSearchResults"),"click",function(evt){FlightDealsController.removeDisableforDeals()});
			}


			on(dom.byId("searchPanelSubtle"),"click",function(){
				if(query(".section-overlay",container).length==0){
					domClass.add(dom.byId("searchPanelSubtle"),"search controller open");
			 		query(".section-overlay",searchPanelSubtle).remove();
			 		domClass.remove(dom.byId("searchPanelSubtle"),"disableAll")
			 		FlightDealsController.addDisableMode(dom.byId("container"));
			 		if(dom.byId("foDealsSearchResults") !== null && dom.byId("foDealsSearchResults") !== undefined){
			 			FlightDealsController.addDisableMode(dom.byId("foDealsSearchResults"));
			 			query("#foDealsSearchResults .section-overlay").addClass("deals-results");
			 		}
				}
			});

			on(dom.byId("dealsbutton"),"click",function(evt){
				if(domClass.contains(this,"disabled")) return;
				FlightDealsController.getDealsResults();
			});


			//Duration filters events
			query("#results .dur-checkbox .dijitCheckBox").on("click",function(){
				if(!FlightDealsController.dealsPanelModel.dealsFilterStatus){
					FlightDealsController.dealsPanelModel.enableDealsButton();
		    	}

			});

			//Departure filter events
			query(".filter-options .flightDepartureTime .dijitCheckBox").on("click",function(evt){

				FlightDealsController.enableApplyBtn();
			});
				FlightDealsController.attachLabelEvents();

		},

		removeDisableforDeals: function(){
			var FlightDealsController = this,
			container = dom.byId("container"),
			searchPanelSubtle = dom.byId("searchPanelSubtle");
			if(query(".section-overlay",searchPanelSubtle).length==0){
				domClass.remove(container,"disableAll");
				query(".section-overlay",container).remove();
				FlightDealsController.addDisableMode(searchPanelSubtle);
				FlightDealsController.resetErrorPopUp();
				if(dom.byId("foDealsSearchResults") !== null && dom.byId("foDealsSearchResults") !== undefined){
		 			query("#foDealsSearchResults .section-overlay").remove();
		 			domClass.remove(dom.byId("foDealsSearchResults"),"disableAll");
		 		}
			}
		},
		startAndEndDuration : function(){
			var FlightDealsController = this;
			var durArray = [];
			FlightDealsController.dealsPanelModel.durationFilter = [];
			query("#results .dur-checkbox .dijitCheckBox.dijitChecked").forEach(function(item){
				_.each(registry.byNode(item).value.split("-"),function(val){
					durArray.push(val);
				});
				FlightDealsController.dealsPanelModel.durationFilter.push(registry.byNode(item).id);
			})

			/*FlightDealsController.dealsPanelModel.retainCriteria.durations = [];
			FlightDealsController.dealsPanelModel.retainCriteria.durations = FlightDealsController.dealsPanelModel.durationFilter;
			dojo.cookie("retainCriteria",dojo.toJson(FlightDealsController.dealsPanelModel.retainCriteria));*/
			if(durArray.length > 0){
				FlightDealsController.dealsPanelModel.startDuration = durArray.min();
				FlightDealsController.dealsPanelModel.endDuration = durArray.max();
			} else {
				FlightDealsController.dealsPanelModel.startDuration = 1;
				FlightDealsController.dealsPanelModel.endDuration = 99;
			}
		},
		attachLabelEvents: function(){
			var FlightDealsController = this,labelNode;
		  	var touchSupport = dojo.hasClass(query("html")[0], "touch");
    		if(!touchSupport){
    			dojo.query(".labelHover",FlightDealsController.domNode).on("mouseover,mouseout",function(evt){

    				labelNode = dojo.query(evt.target).prev('.dijitRadio')[0] || dojo.query(evt.target).prev('.dijitCheckBox')[0];
	    			if(domClass.contains(labelNode,"dijitDisabled")) return;
	    			if(evt.type=="mouseover"){
	    				FlightDealsController.selectOneway(evt,labelNode);
	    			}else{
	    				FlightDealsController.unSelectOneway(evt,labelNode);
	    			}
    			});
    				dojo.query(".dijitRadio",FlightDealsController.domNode).on("mouseover,mouseout",FlightDealsController.hoverOndijit);
    				dojo.query(".dijitCheckBox",FlightDealsController.domNode).on("mouseover,mouseout",FlightDealsController.hoverOndijit);
    		}
		},
		selectOneway:function(evt,labelNode){
	    	dojo.query(evt.target).addClass("per-radiospl");
			dojo.query(evt.target).prev('.dijitRadio').addClass("dijitHover dijitRadioHover");
			if(dojo.hasClass(labelNode,"dijitRadioChecked")){
				dojo.query(evt.target).prev('.dijitRadio').addClass("dijitRadioCheckedHover")
			}
			dojo.query(evt.target).prev('.dijitCheckBox').addClass("dijitHover dijitCheckBoxHover");
	    },
	    unSelectOneway:function(evt,labelNode){
	    	dojo.query(evt.target).removeClass("per-radiospl");
			dojo.query(evt.target).prev('.dijitRadio').removeClass("dijitHover dijitRadioHover");
			dojo.query(evt.target).prev('.dijitCheckBox').removeClass("dijitHover dijitCheckBoxHover");
			if(dojo.hasClass(labelNode,"dijitRadioChecked")){
				dojo.query(evt.target).prev('.dijitRadio').removeClass("dijitRadioCheckedHover")
			}
	    },
		hoverOndijit: function(evt){
	    	if(evt.type=="mouseover"){
    			dojo.query(evt.target).closest("span").children("label").addClass("per-radiospl");
    		} else {
    			dojo.query(evt.target).closest("span").children("label").removeClass("per-radiospl");
    		}
	    },
	    addDisableMode: function(node){
			var element = domConstruct.create("div",{"class":"section-overlay"},node,"first");
			 domClass.add(node,"disableAll")

		},

		resetErrorPopUp : function(){
			var FlightDealsController = this,
			    flyingTo =	dijit.byId("where-to-text"),
			    flyingFrom = dijit.byId("where-from-text"),
				departing = dijit.byId("when"),
				returning = dijit.byId("returnTravel"),
				adults = dijit.byId("totalAdults"),
				child=dijit.byId("totalAdults");;
			if(flyingTo.errorPopup!==null){
			flyingTo.errorPopup.close();
			}
			if(flyingFrom.errorPopup!==null){
			flyingFrom.errorPopup.close();
			}
		    if(departing.errorPopup!==null){
			departing.errorPopup.close();
			}
			if(returning.errorPopup!==null){
			returning.errorPopup.close();
			}
			if(adults.errorPopup!==null){
			adults.errorPopup.close();
			}
			child.searchPanelModel.searchErrorMessages.set("partyChildAges", "");
		},


		getDealsResults : function(){
			var FlightDealsController = this, jsonData;
				FlightDealsController.form = query(".deals-form")[0];

				//FlightDealsController.generateDealsPageTitle();

				FlightDealsController.generateJsonData();

				FlightDealsController.dealsPanelModel.SetDealsSavedSearch();

				FlightDealsController.form.submit();
		},


		generateJsonData : function(){
			var FlightDealsController = this,jsonData;
				FlightDealsController.dealsPanelModel.searchType="ins";
				FlightDealsController.getUserSelection();
				 jsonData = FlightDealsController.dealsPanelModel.createQueryObjForServerCall(true);

				 _.forEach(jsonData, function (value, key) {
				 	FlightDealsController.createInput(key, value);
				});

		},
		// method: "getUserSelection" called on click of the show deals to get all the user selected data  to submit server side.
		getUserSelection : function(){
			var FlightDealsController = this,flyOutObj,flyFromObj,goToObj;
			flyOutObj = registry.byId("dealsExpandable");
			flyFromObj = registry.byId("FlyFromDealsExpandable");
			goToObj = registry.byId("GoToDealsExpandable");

			//Get Flyout Values and assigning to appropriate model variable for the backend references
			if(flyOutObj.selectedData.indexOf("/") != -1){
				FlightDealsController.dealsPanelModel.month = flyOutObj.selectedData.split("/")[0];
				FlightDealsController.dealsPanelModel.year = flyOutObj.selectedData.split("/")[1];
    			FlightDealsController.dealsPanelModel.flexible = flyOutObj.flexibleChecked;
    			FlightDealsController.dealsPanelModel.days = 0;
			}else {
				FlightDealsController.dealsPanelModel.days = flyOutObj.selectedData;

				FlightDealsController.dealsPanelModel.month = 0;
				FlightDealsController.dealsPanelModel.year = 0;
    			FlightDealsController.dealsPanelModel.flexible = false;
			}

			//Get all selected airports
			FlightDealsController.dealsPanelModel.fromAirports = flyFromObj.selectedUkAirports;
			FlightDealsController.dealsPanelModel.toAirports = goToObj.selectedOverseasAirports;

			//Get whethere any uk airport or any overseas airport is selected
			//FlightDealsController.dealsPanelModel.flyFromAllAirports = flyFromObj.flyFromAllAirports;
			//FlightDealsController.dealsPanelModel.goToAllAirports = goToObj.gotoAllAirports;

			//Get oneway status
			FlightDealsController.dealsPanelModel.oneWay =  registry.byId("dealsOneWay").checked;

			//Get durations min and max values : backend params are stardDuration and endDuration
			FlightDealsController.startAndEndDuration();
		},

		 createInput: function (name, value) {
		      //    creates input fields for search form
		    var flightDealsController = this,input;
		      	if(value === null) value = "";

		      	input = dojo.create("input", {"type": "hidden", "name": name, "value": value}, flightDealsController.form);
		 },


		enableApplyBtn: function(){
			 var btn = query("#dealSearchPanelContainer .dealsApplyButton");
			 btn.removeClass("disabled");
			 btn.addClass("cta");
		},
		disableApplyBtn: function(){
			 var btn = query("#dealSearchPanelContainer .dealsApplyButton");
			 btn.addClass("disabled");
			 btn.removeClass("cta");
		}





		/********* Methods End*********/
	});
	return tui.flightdeals.FlightDealscontroller;

})