define("tui/flightdeals/FlightDealsPax",[
        "tui/widget/form/flights/Expandable",
        "tui/widget/form/SelectOption",
        "tui/searchPanel/view/ErrorPopup",
        "dojo",
        "dojo/on",
        "dojo/query",
        "dijit/registry",
        "dojo/dom-construct",
        "dijit/_WidgetBase",
        "dojo/text!tui/flightdeals/templates/flightDealsResultPaxTmpl.html",
        "dojo/parser",
        "tui/search/nls/Searchi18nable",
        "tui/searchPanel/config/SearchConfig",
        "dojox/dtl/Context",
        "dojo/_base/sniff"
        ],function(Expandable,SelectOption,ErrorPopup,dojo,on,query,registry,domConstruct,_WidgetBase,
        		flightDealsResultPaxTmpl,parser,Searchi18nable,SearchConfig){

		dojo.declare("tui.flightdeals.FlightDealsPax",[Expandable,Searchi18nable,SearchConfig],{

			adultsCount :[1,2,3,4,5,6,7,8,9,10],

			childCount:[1,2,3,4,5,6,7,8,9],

			itineraryID : "",

			adultSelectCount :"",

			childSelectCount: "",

			infantAges :[],

			childAges :[],

		//.............................................methods
		postCreate:function(){
			 var flightDealsPax = this;
			 flightDealsPax.inherited(arguments);
			 flightDealsPax.itineraryID = flightDealsPax.itineraryID;
			 flightDealsPax.initSearchMessaging();
			 flightDealsPax.tagElement(flightDealsPax.domNode, "FO_Deals_Results");

		 },
		onAfterTmplRender:function() {
			var flightDealsPax = this;
			flightDealsPax.inherited(arguments);
			flightDealsPax.renderPax();
			flightDealsPax.tagElement(flightDealsPax.expandableDom, "OVERALL_FO_PAX");
			new ErrorPopup();
		},
		onOpenExpandable: function(){
			var flightDealsPax = this;
			var adultReset = dojo.query(".dealsAdultDropDown.custom-dropdown",flightDealsPax.expandableDom);
			var childReset = dojo.query(".dealsTotalChildren.custom-dropdown",flightDealsPax.expandableDom);
			_.forEach(adultReset,function(item){
				if(registry.byNode(item)!=undefined)
				registry.byNode(item).setSelectedValue("2");

			});
			_.forEach(childReset,function(item){
				if(registry.byNode(item)!=undefined)
					registry.byNode(item).setSelectedValue("0");
					query(".flight-pax .clear",flightDealsPax.expandableDom).remove();
			});
		},

		closeExpandable: function(){
	    	var flightDealsPax = this,isDropdwnOpen = false,elem;
	    	var dropDown = dojo.query(".dropdown",flightDealsPax.expandableDom);
	    	//identifying the browser type
	    	if(!dojo.isMozilla){
	    		var evt = event || window.event;
		    	 elem = evt.target || evt.srcElement;
	    	}
	    	else{
	    		elem = window.element;
	    	}
    		if(dropDown!=undefined){
    			_.forEach(dropDown,function(item){
    				if(registry.byNode(item)!=undefined){
    					if(registry.byNode(item).listShowing) isDropdwnOpen = true;
    				}

				});
    		}
    		if(!dojo.hasClass(elem,"close-hide")){
    			if(isDropdwnOpen) return;
    		}

	    	flightDealsPax.inherited(arguments);

    		dojo.query(".dealsPax").remove();
	    },

		renderPax:function(){
			var flightDealsPax = this,html,target;
			data ={
					adultsCount : flightDealsPax.adultsCount,
					childCount : flightDealsPax.childCount
			}

			html = dojo.trim(tui.dtl.Tmpl.createTmpl(data, flightDealsResultPaxTmpl));

			target = dojo.query(".deals-wrapper",flightDealsPax.expandableDom)[0];
			dojo.html.set(target,html,{
				parseContent: true
			});

			flightDealsPax.attachChildSelectEvents();
			flightDealsPax.attachBodyEvent();
		},

		attachChildSelectEvents: function(){
			var flightDealsPax = this,select;
			on(registry.byNode(query(".dealsTotalChildren",flightDealsPax.expandableDom)[0]),"change",function(){
				query('.dealsChildPax .custom-dropdown').forEach(function(dropdown){
					dijit.byNode(dropdown).destroy();
					query(".flight-pax .clear",flightDealsPax.expandableDom).remove();
				});
				if(parseInt(this.selectNode.value) > 0) {
					if(dijit.byId("foDealsSearchPanel").dealsPanelModel.oneWay){
						domConstruct.place("<p class='clear'>CHILD AGES (ON FLYING DATE)</p>",query(".dealsChildPax",flightDealsPax.expandableDom)[0],"before");
					}
					else{
						domConstruct.place("<p class='clear'>CHILD AGES (ON RETURN DATE)</p>",query(".dealsChildPax",flightDealsPax.expandableDom)[0],"before");
					}
				}

				for(var i=0; i<parseInt(this.selectNode.value);i++){
					if(i==2||i==5){
						select = domConstruct.create("select",{'class': 'dropdown childSelect last','data-dojo-type':'tui.widget.form.SelectOption','data-dojo-props':'maxHeight:180'},query(".dealsChildPax",flightDealsPax.expandableDom)[0]);
					}
					else{
						select = domConstruct.create("select",{'class': 'dropdown childSelect','data-dojo-type':'tui.widget.form.SelectOption','data-dojo-props':'maxHeight:180'},query(".dealsChildPax",flightDealsPax.expandableDom)[0]);
					}
					for(var k=0;k<16;k++){
					 if(k==0){
						 var option = domConstruct.create("option",{"value":"-1","label":"-","selected":"selected","innerHTML":"-"},select);
					 }
						var option = domConstruct.create("option",{"value":k,"label":k,"innerHTML":k},select);
					}

				}

				parser.parse(query(".dealsChildPax",flightDealsPax.expandableDom)[0]);

				element = dojo.query(".deals-wrapper",flightDealsPax.expandableDom)[0];
				height = Math.floor(dojo.position(element).h);
				target = dojo.query(".deals-wrapper",flightDealsPax.expandableDom).parent(".deals-guide")[0];
				dojo.style(target,"max-height",height+"px");


	    	});
			on(flightDealsPax.expandableDom,"click",function(evt){
				var elem=evt.target;
				/*if(!dojo.isMozilla){
		    		var evt = event || window.event;
			    	 elem = evt.target || evt.srcElement;
		    	}
		    	else{
		    		elem = window.element;
		    	}*/
				if(dojo.query(elem).closest(".custom-dropdown").length > 0) return;
				var dropDown = dojo.query(".custom-dropdown",flightDealsPax.expandableDom);
				if(dropDown!=undefined){
	    			_.forEach(dropDown,function(item){
	    				if(registry.byNode(item)!=undefined)
	    				registry.byNode(item).hideList();
					});
				}
			});
			query(".button.search-submit",flightDealsPax.expandableDom).on("click",function(){

				if(flightDealsPax.doPaxValidation()){
					 dojo.publish("tui.flightdeals.FlightDealsResultsController.getSelectedFlightDetails", [flightDealsPax]);
				}
			})
		},
		 attachBodyEvent: function(){
		    	var flightDealsPax = this;
		    	on(document.body, "click", function(evt){
		    		dealsGuideDom = dojo.query(evt.target).closest(".deals-guide")[0]
		    		if(dealsGuideDom == undefined && flightDealsPax.domNode != evt.target){
		    			//if(dijit.byId("monthSelect") !== undefined && dijit.byId("monthSelect").listShowing) return
		    			flightDealsPax.closeExpandable();
		    			dojo.query(".dealsPax").remove();
		    			dojo.query(".dropdown",flightDealsPax.expandableDom).removeClass("error");
		    		}
		    	});
		    },

		doPaxValidation: function(){
			var flightDealsPax = this,infantAges=[],childAges=[],  adultSelectCount,childSelectCount,adultNode,childNode;
			adultNode = registry.byNode(query(".dealsAdultDropDown",flightDealsPax.expandableDom)[0]);
			childNode = registry.byNode(query(".dealsTotalChildren",flightDealsPax.expandableDom)[0]);
			//childNodeChild = registry.byNode(query(".childSelect",flightDealsPax.expandableDom)[0]);
			adultSelectCount = parseInt(adultNode.selectNode.value);
			childSelectCount = parseInt(childNode.selectNode.value);

			query(".dealsChildPax .childSelect.custom-dropdown",flightDealsPax.expandableDom).forEach(function(item){
				var childSelectAge = parseInt(registry.byNode(item).selectNode.value);
				if( childSelectAge <= flightDealsPax.INFANT_AGE && childSelectAge != -1){
					infantAges.push(childSelectAge);
					childAges.push(childSelectAge);
				}else {
					childAges.push(childSelectAge);
				}
			});



			if (adultSelectCount === 0 && childSelectCount === 0) {
				flightDealsPax.showErrorPop(flightDealsPax.searchMessaging[dojoConfig.site].errors.onePassenger,adultNode.domNode);
				dojo.query(".dropdown",flightDealsPax.expandableDom).addClass("error");
				return false;
			}

			if(adultSelectCount + childSelectCount > flightDealsPax.MAX_ADULTS_NUMBER ){
				flightDealsPax.showErrorPop(flightDealsPax.searchMessaging[dojoConfig.site].errors.partyLimit + flightDealsPax.searchMessaging[dojoConfig.site].errors.partyLimitHours,childNode.domNode);
				dojo.query(".dropdown",flightDealsPax.expandableDom).addClass("error");

				if(adultSelectCount !== 0 && infantAges.length > adultSelectCount){
					childNodeChild = registry.byNode(query(".childSelect",flightDealsPax.expandableDom)[0]);
					flightDealsPax.showErrorPop(flightDealsPax.searchMessaging[dojoConfig.site].errors.infantLimit,childNodeChild.domNode);
					dojo.query(".dropdown",flightDealsPax.expandableDom).addClass("error");
				}
				else flightDealsPax.SelectedChildValidaiton();
				return false;
			}



			if(adultSelectCount !== 0 && infantAges.length > adultSelectCount){
				childNodeChild = registry.byNode(query(".childSelect",flightDealsPax.expandableDom)[0]);
				flightDealsPax.showErrorPop(flightDealsPax.searchMessaging[dojoConfig.site].errors.infantLimit,childNodeChild.domNode);
				dojo.query(".dropdown",flightDealsPax.expandableDom).addClass("error");
				return false;
			}

			/*flightDealsPax.SelectedChildValidaiton();*/

			if(adultSelectCount === 0 && childSelectCount > 0 ){
				flightDealsPax.showErrorPop(flightDealsPax.searchMessaging[dojoConfig.site].errors.childOnly,adultNode.domNode);
				dojo.query(".dropdown",flightDealsPax.expandableDom).addClass("error");
				if(!flightDealsPax.SelectedChildValidaiton());
				return false;
			}

			if(!flightDealsPax.SelectedChildValidaiton()) return false;

			flightDealsPax.adultSelectCount = adultSelectCount;
			flightDealsPax.childSelectCount = childAges.length - infantAges.length;
			flightDealsPax.childAges = childAges;
			flightDealsPax.infantAges = infantAges;
			return true;

		},


		SelectedChildValidaiton : function (){
			var flightDealsPax = this,flag = true;

			query(".dealsChildPax .childSelect.custom-dropdown",flightDealsPax.expandableDom).forEach(function(item){
				 if(parseInt(registry.byNode(item).selectNode.value) === -1){
					 flag = false;
				 }
			});

			 if(!flag){
				 childNodeChild = registry.byNode(query(".childSelect",flightDealsPax.expandableDom)[0]);
				 if(dijit.byId("foDealsSearchPanel").dealsPanelModel.oneWay){
					 flightDealsPax.showErrorPop(flightDealsPax.searchMessaging[dojoConfig.site].errors.childNoAgesOneWay,childNodeChild.domNode);
					 dojo.query(".dropdown",flightDealsPax.expandableDom).addClass("error");
				 }
				 else{
					 flightDealsPax.showErrorPop(flightDealsPax.searchMessaging[dojoConfig.site].errors.childNoAges,childNodeChild.domNode);
					 dojo.query(".dropdown",flightDealsPax.expandableDom).addClass("error");
				 }
			 }

			return flag;
		},

		showErrorPop: function(errorMessage,elementRelativeTo){
			var flightDealsPax = this;
			var x = new ErrorPopup({
		        arrow: false,
		        elementRelativeTo: elementRelativeTo,
		        errorPopupClass: "dealsPax",
		        floatWhere: "position-bottom-center",
		        errorMessage:errorMessage,
		        onClose:function(){
		        	dojo.query(".dropdown",flightDealsPax.expandableDom).removeClass("error");
		        }
		      });
			x.startup();
			x.open();
			on(dojo.query(".dropdown",flightDealsPax.expandableDom),"click",function(){
				dojo.query(".dropdown",flightDealsPax.expandableDom).removeClass("error");
				dojo.query(".dealsPax").remove();


			});
		}


	});
	return tui.flightdeals.FlightDealsPax;
});