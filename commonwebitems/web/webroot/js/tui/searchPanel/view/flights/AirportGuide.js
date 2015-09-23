define("tui/searchPanel/view/flights/AirportGuide", [
    "dojo",
    "dojo/on",
    "dojo/has",
    "dojo/dom-attr",
    "dojo/query",
    "dojo/dom-style",
    "dojo/text!tui/searchPanel/view/flights/templates/AirportGuideTmpl.html",
    "tui/searchPanel/model/AirportModel",
    "tui/searchPanel/store/AirportGuideStore",
    "tui/searchPanel/view/SearchGuide",
    "dojo/store/Observable",
    "tui/utils/TuiAnimations",
    "tui/search/model/flights/SearchModel",
    "tui/searchPanel/view/flights/AirportListGrouping"],
    function (dojo, on, has, domAttr, query, domStyle, airportGuideTmpl, AirportModel, Toggler, searchGuide ) {

        dojo.declare("tui.searchPanel.view.flights.AirportGuide", [tui.searchPanel.view.SearchGuide, tui.search.model.flights.SearchModel, tui.searchPanel.view.flights.AirportListGrouping], {

            // ----------------------------------------------------------------------------- properties

            tmpl: airportGuideTmpl,

            airportGuideStore: null,

            expandableProp: null,

            targetSelector: null,

            fromLimit: 4,

            guideTimer: 0,

            subscribableMethods: ["openExpandable", "closeExpandable", "openGlobalExpandable"],

            airportList: null,

            // number of columns
            columns:3,

            // number of airports to render per column
            columnLength: 0,

            ukColumnLength: 0,

            overseasColumnLength: 0,

            selectedCountryCode: null,

            /*overseasSwapElement: null,*/

            isErrorOccured: false,

            itemSelected: null,

            tracsEndDate :null,

            // ----------------------------------------------------------------------------- methods

            postCreate: function () {
            	var airportGuide = this;
            	//console.log(airportGuide);
                airportGuide.inherited(arguments);
               // console.log(airportGuide);
                var resultSet = airportGuide.searchPanelModel.from.query();

                resultSet.observe(function (airportModel, remove, add) {
                    // if expandable is not created yet, let's ignore.
                    if (!airportGuide.expandableDom) {
                        return;
                    }
                    // update views
                    airportGuide.updateGuide(airportModel, (add > -1));
                    airportGuide.disableGuideItems();
                    airportGuide.updateGuideCount();
                    airportGuide.updatedOverlyText();
                });



                airportGuide.tagElement(airportGuide.domNode, "Flying-From");

                /* Clear the selectedCountryCode and overseasSwapElement when ever the pill was getting removed from the textbox.
                   Published @ airportMultiFieldList - removeDataItem() function. */
                dojo.subscribe("tui/searchPanel/view/flights/AirportGuide/ClearStorageFields", function() {
                	airportGuide.selectedCountryCode = null;
                	dojo.global.overseasSwapElement = null
                });

                airportGuide.subscribe("tui/searchPanel/view/flights/ArrivalAirportGuide/ArrivalAirportCountry", function(arrivalCountryCode) {
                	airportGuide.itemSelected = arrivalCountryCode;
                });
                dojo.global.overseasSwapElement=null;
            },


            attachEventListeners: function () {},

            openGlobalExpandable: function (controller) {
                var airportGuide = this;
                if (controller === airportGuide.widgetController) {
                    airportGuide.openExpandable();
                }
            },

            attachOpenEvent: function () {
                var airportGuide = this;
                on(airportGuide.domNode, "click", function (event) {
                	if(airportGuide.errorPopup){
                		airportGuide.errorPopup.close();
                	}
                    if (dojo.hasClass(airportGuide.domNode, "loading")) {
                        return;
                    }
                    if (airportGuide.expandableDom === null || !airportGuide.isShowing(airportGuide.expandableDom)) {
                        dojo.publish("tui.searchPanel.view.flights.airportGuide.closeExpandable");
                        setTimeout(function () {
                            airportGuide.openExpandable();
                        }, 350);
                    } else {
                        airportGuide.closeExpandable();
                    }
                });
              /*  on(airportGuide.domNode,"blur",function(evt){
                	 airportGuide.closeExpandable();
                });*/
               /* on(airportGuide.domNode,"focus",function(evt){
                	if (airportGuide.expandableDom === null || !airportGuide.isShowing(airportGuide.expandableDom)) {
                        dojo.publish("tui.searchPanel.view.flights.airportGuide.closeExpandable");
                        setTimeout(function () {
                            airportGuide.openExpandable();
                        }, 350);
                    }
               });*/
            },

           /* cancelBlur: function () {
                var airportGuide = this;
                airportGuide.inherited(arguments);
                airportGuide.domNode.focus();
            },*/

            onAfterTmplRender: function () {
                // summary:
                //		After the expandable dom has been created,
                //		we attach event listeners to the checkboxes and links

            	var airportGuide = this;
           	    airportGuide.inherited(arguments);

                // use dojo "on" and event delegation
                on(airportGuide.expandableDom, "label:click", function (event) {
                    dojo.stopEvent(event);

                    var label, checkbox;
                    if (event.target.tagName.toUpperCase() === "INPUT") {
                        checkbox = event.target;
                        label = event.target.parentElement;
                    } else {
                        checkbox = event.target.children[0];
                        label = event.target;
                    }


                    if (dojo.hasClass(label, "disabled") || dojo.hasClass(label, "manually-disabled")) {
                        return;
                    }

                    if(!airportGuide.multiSelect){
                    	airportGuide.searchPanelModel.from.emptyStore();
                    }

                    // Start the orange pulse on the field.
                    dojo.publish("tui.searchPanel.view.flights.AirportMultiFieldList.pulse", [airportGuide.widgetController]);
                   // airportGuide.selectAirports(checkbox);


                    if(!airportGuide.isErrorOccured) {
                    	airportGuide.updatetoItems(checkbox);
                    }
                    else {
                    	airportGuide.isErrorOccured = false;
                    }
                   // airportGuide.cancelBlur();
                   // airportGuide.closeExpandable(airportGuide.expandableDom);
                    airportGuide.updatedOverlyText();

                    	window.scrollTo(0,0);
                    	if(dijit.byId("where-from").id == "where-from"){
                        	dijit.byId("where-from").searchPanelModel.searchErrorMessages.set("from", {})
        				}
                });

                // add dom event to unselect all link.
                on(airportGuide.expandableDom, ".empty-airport-model:click", function (event) {
                    dojo.stopEvent(event);
                    if (dojo.hasClass(event.target, "inactive")) {
                        return;
                    }
                    airportGuide.clearAll();
                });

                airportGuide.subscribe("tui/searchPanel/searchOpening", function (component) {
                	/*console.log(dojo.byId(component.domNode).id);
                	console.log(dojo.byId(airportGuide.domNode).id);
                    if (component !== airportGuide) {
                    	dojo.publish("tui.searchPanel.view.AirportMultiFieldList.whereFrom", null);
                    	airportGuide.closeExpandable();
                    } */
                });

                var columns = dojo.query(".col", airportGuide.expandableDom);
                _.each(columns, function (column,index) {
                	if(index < 3){
                		airportGuide.tagElement(column, "OverallFlyingFrom");
                	}else if(index > 3){
                		airportGuide.tagElement(column, "OverallFlyingFrom1");
                	}
                });

                airportGuide.tagElement(dojo.query(".close-hide", airportGuide.expandableDom)[0], "Airport Hide");
                airportGuide.tagElement(dojo.query(".empty-airport-model", airportGuide.expandableDom)[0], "Airport Deselect All");
                airportGuide.updatedOverlyText();
            },

            openExpandable: function () {
                // summary:
                //		Extend default expandable behaviour,
                //		only open once airport guide data is available and returned from the server.
                var airportGuide = this,
                	args = arguments;

                dojo.addClass(airportGuide.domNode, "loading");

                // publish opening event for widgets that need to close
                airportGuide.publishMessage("searchPanel/searchOpening");

                dojo.publish("tui.searchPanel.view.flights.AirportMultiFieldList.highlight", [true, airportGuide.widgetController]);

                dojo.when(airportGuide.airportGuideStore.requestData(airportGuide.searchPanelModel.generateQueryObject(), false), function (responseData) {
                    dojo.removeClass(airportGuide.domNode, "loading");

                    airportGuide = airportGuide.airportMap(responseData);

                    airportGuide.multiSelect =airportGuide.searchPanelModel.tracsDate;

                    airportGuide.inherited(args);
                    airportGuide.showTabs();
                    //update the guide count once user opens expandable dom
                    airportGuide.updateGuideCount();
                    //Update the view to conform to the latest datafeed.
                    _.each(airportGuide.airportList, function(item){
                    	airportGuide.updateAirportCheckboxes(item);
            		});
                    _.each(airportGuide.ukairportList, function(item){
                    	airportGuide.updateAirportCheckboxes(item);
            		});

                    // Disable airports selection if saved search has MAX_AIRPORTS_SELECTABLE items selected.
                    airportGuide.disableGuideItems();
                    if (airportGuide.widgetController.searchApi === "getPrice") {
                        dojo.addClass(airportGuide.widgetController.domNode, "open-guide");
                    }
                });
            },

            showTabs : function(){
            	var airportGuide= this,
            		SearchModel = airportGuide.searchPanelModel,
            		toAirport = SearchModel.to.data[0],
            		fromAirport = SearchModel.from.data[0],
            		airportTabs = dojo.byId("airportTabs"),
            		ukairportsContainer =dojo.byId("ukairportsContainer"),
            		overseasContainer = dojo.byId("overseasContainer"),
            		 ukTextContainer=dojo.query(".UKAiportCnt"),
                	 overseasTextContainer=dojo.query(".OverseasairportsCnt");

	         	if(toAirport && toAirport.countryCode === "GBR"){
	         		domStyle.set(airportTabs, "display", "none");
	         		airportGuide.showOverseasContainer(ukairportsContainer, overseasContainer);
	         		airportGuide.showOverseasTextContainer(ukTextContainer,overseasTextContainer);

	         	} else  if(toAirport && toAirport.countryCode !== "GBR"){
	         		domStyle.set(airportTabs, "display", "none");
	         		airportGuide.showUKAirportsContainer(ukairportsContainer, overseasContainer);
	         		airportGuide.showUKAirportsTextContainer(ukTextContainer,overseasTextContainer);

	         	}else{
	         		if(fromAirport){
	         			if(fromAirport.countryCode == "GBR"){
	         				airportGuide.showUKAirportsContainer(ukairportsContainer, overseasContainer);
		         			airportGuide.showUKAirportsTextContainer(ukTextContainer,overseasTextContainer);
	         			}else{
	         				airportGuide.showOverseasContainer(ukairportsContainer, overseasContainer);
		         			airportGuide.showOverseasTextContainer(ukTextContainer,overseasTextContainer);
	         			}
	         		}else{
	         			airportGuide.showUKAirportsContainer(ukairportsContainer, overseasContainer);
	         			airportGuide.showUKAirportsTextContainer(ukTextContainer,overseasTextContainer);
	         		}
	         		domStyle.set(airportTabs, "display", "block");

	         	}
	         	airportGuide.highlightTabs();
	         	airportGuide.setHeight();
            },

            showUKAirportsContainer: function(ukairportsContainer, overseasContainer){
            	domStyle.set(ukairportsContainer, "display", "block");
         		domStyle.set(overseasContainer, "display", "none");
            },
            showOverseasContainer: function(ukairportsContainer, overseasContainer){
            	domStyle.set(ukairportsContainer, "display", "none");
         		domStyle.set(overseasContainer, "display", "block");
            },
            showUKAirportsTextContainer: function(ukTextContainer,overseasTextContainer){
            	ukTextContainer.removeClass('hide').addClass('show');
         		overseasTextContainer.removeClass('show').addClass('hide');
            },
            showOverseasTextContainer: function(ukTextContainer,overseasTextContainer){
            	ukTextContainer.removeClass('show').addClass('hide');
         		overseasTextContainer.removeClass('hide').addClass('show');
            },

            setHeight :  function(){
            	var airportGuide = this;
            	var	wrapper = dojo.query('.wrapper', airportGuide.expandableDom)[0];
            	var height =  _.pixels(dojo.position(wrapper).h);
            		setTimeout(function(){
            			dojo.style(airportGuide.expandableDom, 'maxHeight', height);
            		},100);
            },

            /*closeExpandable: function () {
                var airportGuide = this;
                airportGuide.inherited(arguments);
                dojo.publish("tui.searchPanel.view.AirportMultiFieldList.highlight", [false, airportGuide.widgetController]);

                if (airportGuide.widgetController.searchApi === "getPrice") {
                    dojo.removeClass(airportGuide.widgetController.domNode, "open-guide");
                }
            },*/

            showWidget: function (element) {
                var airportGuide = this;
                if(airportGuide.widgetController.searchApi === "getPrice") {
                    // TODO: fix width being sent, this is arbitrary
                    dojo.publish("tui.searchGetPrice.view.GetPriceModal.resize", 777);
                    // ie7 has issues with getPrice guide
                    // TODO: fix this at some point
                    if (has("ie") === 7) dojo.setStyle(element, {"width": "auto"});
                }
                airportGuide.inherited(arguments);
            },

            hideWidget: function (element) {
                var airportGuide = this;
                if(airportGuide.widgetController.searchApi === "getPrice") {
                    // TODO: fix width being sent, this is arbitrary
                    dojo.publish("tui.searchGetPrice.view.GetPriceModal.resize", 358);
                    // ie7 has issues with getPrice guide
                    // TODO: fix this at some point
                    if (has("ie") === 7) dojo.setStyle(element, {"width": 0});
                }
                /*console.log(arguments);*/
                airportGuide.inherited(arguments);
            },

            place: function (html) {
                // summary:
                //		Override place method from simpleExpandable,
                //		place the airport guide in the main search container.
                var airportGuide = this,
                    target = airportGuide.targetSelector
                        ? dojo.query(airportGuide.targetSelector, airportGuide.widgetController.domNode)[0]
                        : airportGuide.widgetController.domNode;
                return dojo.place(html, target, "last");
            },

            getAirportItemAttributes: function (airport) {
                // summary:
                //		Returns attributes for airport
            	/*alert("getAirportItemAttributes");
            	console.log(domAttr.get(airport, "data-airportmodel-name"));
            	console.log(domAttr.get(airport, "data-airportmodel-id"));
            	console.log(domAttr.get(airport, "data-airportmodel-groups"));*/
                return dojo.mixin(new AirportModel(),{
                    name: domAttr.get(airport, "data-airportmodel-name"),
                    id: domAttr.get(airport, "data-airportmodel-id"),
                    group: domAttr.get(airport, "data-airportmodel-groups").split(","),
                    countryCode: domAttr.get(airport, "data-airportmodel-countryCode")
                });
            },

            selectAirports: function (clickedCheckbox) {
              	 var airportGuide = this;
              	 var airportModel;
                var clickedLabel = dojo.query(clickedCheckbox).parent()[0];

                // Ignore the click if the label is disabled.
                if (dojo.hasClass(clickedLabel, "disabled")) {
                    return;
                }

                // We look to the parent label tag class to see if selected or not.
                var add = !(dojo.hasClass(clickedLabel, "selected"));
                if (add) {
               	 var clickedCheckboxCountryCode = domAttr.get(clickedCheckbox, "data-airportmodel-countryCode");
                    dojo.publish("tui/searchPanel/view/flights/AirportGuide/DepartureAirportCountry", [clickedCheckboxCountryCode]);
                     if(airportGuide.itemSelected !== null && clickedCheckboxCountryCode !== null) {
    	              	 if((airportGuide.itemSelected === "GBR" && clickedCheckboxCountryCode === "GBR")
    	              			 || (airportGuide.itemSelected !== "GBR" && clickedCheckboxCountryCode !== "GBR")) {
    	           	   //Validation to check the user should select only UK to overseas and vice versa.
    	           	   airportGuide.searchPanelModel.searchErrorMessages.set("invalidDepartureandArrivalAirportCombination", {
    	         			 invalidDepartureandArrivalAirportCombination: airportGuide.searchMessaging.errors.invalidFromandToAirportCombination
    	         		 });
    	              		airportGuide.isErrorOccured	= true;
    	           	       return;
    	              }
                   }
                }
                else {
               	 dojo.publish("tui/searchPanel/view/flights/AirportGuide/DepartureAirportCountry", null);
                }

              },

            updatetoItems: function (clickedCheckbox) {
                // summary:
                //		Adds/removes selected airport to/from airport model.
                var airportGuide = this;
                var airportModel;
                var clickedCheckboxId = domAttr.get(clickedCheckbox, "data-airportmodel-id");
                var clickedCheckboxCountryCode = domAttr.get(clickedCheckbox, "data-airportmodel-countryCode");

                //set the selected airport country code to global variable
               /*
                * Chiranjeevi: Commenting this as we don't need validation for airport selection in R2(expectation is if uk airport is selected then again overseas airport is selected it should be swapped to overseas).
                * if(airportGuide.selectedCountryCode !== null) {
                	if(airportGuide.selectedCountryCode === "GBR"
                		&& airportGuide.selectedCountryCode !== clickedCheckboxCountryCode) {
                		airportGuide.searchPanelModel.searchErrorMessages.set("invalidDepartureAirportCombination", {
                			invalidDepartureAirportCombination: airportGuide.searchMessaging.errors.invalidAirportCombination
                	    });
                		 return;
                	}
                	else if(airportGuide.selectedCountryCode !== "GBR"
                		&& clickedCheckboxCountryCode === "GBR") {
                		airportGuide.searchPanelModel.searchErrorMessages.set("invalidDepartureAirportCombination", {
                			invalidDepartureAirportCombination: airportGuide.searchMessaging.errors.invalidAirportCombination
               	    });
                		return;
                	}
                }
                else {
                	airportGuide.selectedCountryCode = clickedCheckboxCountryCode;
                }*/

                //Chiranjeevi: adding from the above commented section of else condition
                if(airportGuide.selectedCountryCode === null) {
                	airportGuide.selectedCountryCode = clickedCheckboxCountryCode;
                }
                var clickedCheckboxId = domAttr.get(clickedCheckbox, "data-airportmodel-id");
                var clickedLabel = dojo.query(clickedCheckbox).parent()[0];
                // Ignore the click if the label is disabled.
                if (dojo.hasClass(clickedLabel, "disabled")) {
                    return;
                }

                // We look to the parent label tag class to see if selected or not.
                var add = !(dojo.hasClass(clickedLabel, "selected"));

                var action = (add) ? "addClass" : "removeClass";
                if (add) {

                    airportModel = airportGuide.getAirportItemAttributes(clickedCheckbox);
                    if (airportModel) {
                    	if(airportGuide.searchPanelModel.from.query().length == 1){
                    		if(airportGuide.searchPanelModel.from.query()[0].countryCode !== "GBR") dojo.global.overseasSwapElement = true;
                    		else dojo.global.overseasSwapElement = null;
                    	} else if(airportGuide.searchPanelModel.from.query().length > 1){
                    		if(airportGuide.searchPanelModel.from.query()[0].countryCode == "GBR") dojo.global.overseasSwapElement = null;
                    	}

                    	//Check if clicked check box is related to overseas airport, then needs to remove already selected one from the the list, if one exists.
                    	if(clickedCheckboxCountryCode !== "GBR") { //Chiranjeevi: Cmmenting this line for the may release as we need to select only one airport
                    		/*airportGuide.overseasSwapElement = clickedCheckboxId;
                    		airportGuide.searchPanelModel.from.remove(this.overseasSwapElement);
                    		dojo.publish("tui/widget/Textbox/removeall");*/
                    		//add the first overseas airport to overseasSwapElement
                    		//dojo.publish("tui/widget/Textbox/removeall");


                    		if(airportGuide.searchPanelModel.from.selectedSize > 0){
	                    		airportGuide.searchPanelModel.from.emptyStore();
	                		}

                    		if(!dojo.global.overseasSwapElement) {
                    			dojo.global.overseasSwapElement = clickedCheckboxId;
                    		}
                    		else {
                    			// remove the existing overseas airport and add the newly selected one to overseasSwapElement
                    			airportGuide.searchPanelModel.from.remove(dojo.global.overseasSwapElement);
                    			dojo.global.overseasSwapElement = clickedCheckboxId;
                    		}
                    	}else if(airportGuide.searchPanelModel.from.selectedSize > 0 && dojo.global.overseasSwapElement){
                    			airportGuide.searchPanelModel.from.emptyStore();
	                    		dojo.global.overseasSwapElement = null;
                    	}

                        airportGuide.animatePill(clickedLabel, dojo.query("#where-from-text", airportGuide.widgetController.domNode)[0], function () {
                            // Execute the action after the animation.
                            airportGuide.searchPanelModel.from.add(airportModel);
                            airportGuide.updatePillText();
                           if((clickedCheckboxCountryCode == "GBR"&& !airportGuide.multiSelect) || clickedCheckboxCountryCode != "GBR"){
                        	   airportGuide.closeExpandable();
                           }
                        });
                    }
                    return;
                }

                // DELETE
                // We are performing a removal from airport guide.
                // if id doesn't exist, we need to delete the airport group.

               //Chiranjeevi: commenting this part for scope changes in R2 as deselection  of airport is not allowed.
               airportModel = airportGuide.searchPanelModel.from.get(clickedCheckboxId);
               if(airportModel.countryCode == "GBR"){
		           if (airportModel) {
		                airportGuide.searchPanelModel.from.remove(clickedCheckboxId);
		                airportGuide.selectedCountryCode = null;
		                dojo.global.overseasSwapElement = null;
		                airportGuide.itemSelected = null;
		            }

		           var clickedCheckboxChildren = domAttr.get(clickedCheckbox, "data-airportmodel-groups");

                // Get the airport group list.
	                if (clickedCheckboxChildren) {
	                    clickedCheckboxChildren = clickedCheckboxChildren.split(",");
	                    dojo.forEach(clickedCheckboxChildren, function (id) {
	                        if (!id) {
	                            return;
	                        }

	                        // Query from to see if groups are present.
	                        var airportGroupModel = airportGuide.searchPanelModel.from.get(id);
	                        if (airportGroupModel) {
	                            airportGuide.searchPanelModel.from.remove(id);


	                            _.each(airportGroupModel.children, function (child) {
	                                if (clickedCheckboxId !== child) {
	                                    // select children of group (except for current item)
	                                    var airportCheckbox = dojo.query("." + child, airportGuide.expandableDom)[0];
	                                    var airportModel = airportGuide.getAirportItemAttributes(airportCheckbox);

	                                    // Making sure we're not adding the airport twice.
	                                    if (!airportGuide.searchPanelModel.from.get(airportModel.id)) {
	                                        airportGuide.searchPanelModel.from.add(airportModel);
	                                    }
	                                }
	                            });
	                        }

	                    });
	                }
            }
                if(airportGuide.searchPanelModel.from.data.length < 1){
                	dojo.query(".value", airportGuide.domNode).style("display","none");
                	dojo.query("#wherefromPlaceholder", airportGuide.domNode).style("display","block");
                }
            },

            updatePillText: function(){

            	var airportGuide = this,
            	data  = airportGuide.searchPanelModel.from.data,
            	pillPlaceHolder = dojo.query("#wherefromPlaceholder", airportGuide.domNode)[0];
        		pill = dojo.query(".value", airportGuide.domNode)[0];
            	pillPlaceHolder.style.display = "none";
            	pill.style.display = "block";
            	if(data.length > 1){
            		pill.innerHTML =  data.length +" "+ " airports";
            	}else {
            		airportModel =  data[0];
            		pill.innerHTML =  airportModel.name + " (" + airportModel.id + ")" ;
            	}
            },

            /*
             * Update selected airport count in airport guide overlay
             */

            updatedOverlyText : function(){
	           	 var airportGuide = this, airportSize=0;
	           	if(airportGuide.searchPanelModel.from.selectedSize === 0){
	           		airportGuide.overseasSwapElement = null;
	           	 }
	           	 if(airportGuide.searchPanelModel.from.data.length > 0 && airportGuide.searchPanelModel.from.data[0].countryCode !== "GBR"){
	           		airportSize = airportGuide.searchPanelModel.from.selectedSize -1;
	           	 }else{
	           		airportSize = airportGuide.searchPanelModel.from.selectedSize;
	           	 }

	           	 if(airportGuide.searchPanelModel.from.selectedSize > 1 || airportGuide.searchPanelModel.from.selectedSize === 0 || airportSize === 0){
	           			dojo.query(".airport-guide-count", airportGuide.expandableDom).text(airportSize +" "+" Airports selected");
	           	 }else{
	           		 	dojo.query(".airport-guide-count", airportGuide.expandableDom).text(airportSize  +" "+" Airport selected");
	           	 }
            },



            updateGuide: function (item, add) {
                // summary:
                //		Updates guide, selects/unselects items to match Search model
                var airportGuide = this;
                var action = (add) ? "addClass" : "removeClass";

                // add/remove "selected" class, select or unselect checkbox.
                if (item.children && item.children.length > 0) {
                    _.each(item.children, function (child, i) {
                        var input = dojo.query("." + child, airportGuide.expandableDom)[0];
                        domAttr.set(input, "checked", add);
                        dojo.query(input).parent()[action]("selected");
                    });
                } else {
                    var input = dojo.query("." + item.id, airportGuide.expandableDom)[0];
                    domAttr.set(input, "checked", add);
                    dojo.query(input).parent()[action]("selected");
                }
               // airportGuide.closeExpandable(airportGuide.expandableDom);

                dojo.removeClass(dojo.byId("where-from"),"border-sel-active");


            },

            updateAirportCheckboxes: function (airportGuideItem) {
                // summary:
                //    Changes item state to enabled/disabled depending on server response
                var airportGuide = this;
                if (!airportGuide.expandableDom) {
                    return;
                }

                var input = dojo.query("." + airportGuideItem.id, airportGuide.expandableDom);
                if (!airportGuideItem.available) {
                    //domAttr.set(input[0], "disabled", true);
                    dojo.addClass(dojo.query(input).parent()[0], "disabled");

                    // For security, we remove the airport from the model if there.
                    var airportGroupModel = airportGuide.searchPanelModel.from.get(airportGuideItem.id);
                    if (airportGroupModel) {
                        airportGuide.searchPanelModel.from.remove(airportGuideItem.id);
                    }
                } else {
                    //domAttr.set(input[0], "disabled", false);
                    dojo.removeClass(dojo.query(input).parent()[0], "disabled");
                }
            },

            disableGuideItems: function () {
                // summary:
                //    Disables guide items when fromLimit is reached.
                var airportGuide = this;
                var items = dojo.query("input[type='checkbox']", airportGuide.expandableDom);
                if (airportGuide.searchPanelModel.from.selectedSize >= airportGuide.fromLimit) {
                    _.each(items, function (item, i) {
                        var label = dojo.query(item).parent()[0];
                        if (!dojo.hasClass(label, "selected")) {
                            dojo.addClass(item, "manually-disabled");
                            dojo.addClass(label, "manually-disabled");
                            domAttr.set(item, "disabled", true);
                        }
                    });
                } else {
                    _.each(items, function (item, i) {
                        var label = dojo.query(item).parent()[0];
                        if (dojo.hasClass(label, "manually-disabled")) {
                            dojo.removeClass(item, "manually-disabled");
                            dojo.removeClass(label, "manually-disabled");
                            domAttr.set(item, "disabled", false);
                        }
                    });
                }
            },

            updateGuideCount: function () {
                // summary:
                //		Updates airport guide count with given value.
                var airportGuide = this;

                //console.dir(dojo.query(".empty-airport-model", airportGuide.expandableDom)[0]);
                // Remove inactive class if we have airports which we can remove.
                // update airport guide selected count.
                dojo.query(".airport-guide-count", airportGuide.expandableDom).text(airportGuide.searchPanelModel.from.selectedSize);

            },

            clearAll: function () {
                // summary:
                //    Empties model when clicking "unselect all" link.
                //    Removes "selected" class from labels.
                var airportGuide = this;
                airportGuide.cancelBlur();
                airportGuide.domNode.focus();
                _.each(airportGuide.searchPanelModel.from.query(), function (airportModel) {
                    airportGuide.searchPanelModel.from.remove(airportModel.id);
                });
                airportGuide.removePill();

                // Remove all checked status of checkboxes.
                dojo.query("label.selected", airportGuide.expandableDom).removeClass("selected");
                var checkboxes = dojo.query("input[type='checkbox']", airportGuide.expandableDom);
                _.each(checkboxes, function (checkbox) {
                    domAttr.set(checkbox, "checked", false);
                });
                airportGuide.updatedOverlyText();
            },

            removePill : function(){
            	var airportGuide = this;
        		dojo.byId("fromPillCnt").innerHTML='';
        		dojo.query("#wherefromValue", airportGuide.domNode).text("");
            	dojo.query("#wherefromPlaceholder", airportGuide.domNode)[0].style.display ="block";
            },
            highlightTabs: function (){
            	var airportGuide= this,
        		SearchModel = airportGuide.searchPanelModel,
        		fromAirport = SearchModel.from.data[0];
            	try {
            		var ukAirportTabContainer = dojo.query("#ukairports"),
                	 overseasAirportTabContainer = dojo.query("#overseasairports");
                	if(fromAirport){
                		if(fromAirport.countryCode === "GBR"){
                			airportGuide.showUKAirportTab(ukAirportTabContainer, overseasAirportTabContainer);
                		} else{
                			airportGuide.showOverseasAirportTab(ukAirportTabContainer, overseasAirportTabContainer);
                		}
                	}else{
                		//by default UK tab is highlighted
                		airportGuide.showUKAirportTab(ukAirportTabContainer, overseasAirportTabContainer);
                	}
            	} catch(err){
            		console.error(err.message)
            	}
            },
            showUKAirportTab: function(ukAirportTabContainer, overseasAirportTabContainer){
            	ukAirportTabContainer.removeClass('ukairports-notselected').addClass('ukairports-selected');
        		overseasAirportTabContainer.removeClass('overseasairports-selected').addClass('overseasairports-notselected');
            },
            showOverseasAirportTab: function(ukAirportTabContainer, overseasAirportTabContainer){
            	ukAirportTabContainer.removeClass('ukairports-selected').addClass('ukairports-notselected');
    			overseasAirportTabContainer.removeClass('overseasairports-notselected').addClass('overseasairports-selected');
            }

        });

        return tui.searchPanel.view.flights.AirportGuide;
    });

