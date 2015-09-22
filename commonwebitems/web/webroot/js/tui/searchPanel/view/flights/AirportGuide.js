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
    "tui/search/model/flights/SearchModel"],
    function (dojo, on, has, domAttr, query, domStyle, airportGuideTmpl, AirportModel, Toggler, searchGuide ) {

        dojo.declare("tui.searchPanel.view.flights.AirportGuide", [tui.searchPanel.view.SearchGuide, tui.search.model.flights.SearchModel], {

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
                   // airportGuide.disableGuideItems();
                    airportGuide.updateGuideCount();
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
                    airportGuide.closeExpandable(airportGuide.expandableDom);
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

                    var airportsListKeys = Object.keys(responseData);
                    var  airportTempList= [];
                    var	 airportSwapList = [];
                    _.each(airportsListKeys, function(countryKey){
                    	airportTempList = responseData[countryKey];
                    	var checkCName = "";
                    	_.each(airportTempList, function(airport){

                    		if(!airport.countryName){
                    			airport.countryName ="Finland";
                    		}

                    		if(airport.countryName == checkCName){
                    			airport.cFlag = false;
                    		}else{
                    			airport.cFlag = true;

                    		}
                    		checkCName = airport.countryName;
                    		airportSwapList.push(airport);

                    	});

                     });

                    airportGuide.airportList = airportSwapList;
                    airportGuide.ukColumnLength = Math.ceil(_.size(responseData["GBR"]) / airportGuide.columns);
                    //airportGuide.columnLength = Math.ceil(_.size(airportGuide.airportList) / airportGuide.columns);
                    airportGuide.columnLength = Math.ceil((_.size(airportGuide.airportList) - _.size(responseData["GBR"])) / 3);
                    airportGuide.overseasColumnLength =  airportGuide.columnLength -  airportGuide.ukColumnLength;

                    //airportGuide.columnLength = Math.ceil((_.size(airportGuide.airportList) - _.size(responseData["GBR"])) / airportGuide.columns);
                    //airportGuide.overseasColumnLength =  airportGuide.columnLength -  airportGuide.ukColumnLength
                    //airportGuide.ukairportList=responseData["GBR"];
                    var tempUkairportList=[];
                    var tempUkairportList_0=[];
                    var tempUkairportList_1=[];
                   /* var tempJSON ={
                                "name": "Any London ",
                                "id": "ANY",
                                "available": true,
                                "group": [
                                    "SE",
                                    "LN"
                                ],
                                "children": [],
                                "countryCode": "GBR",
                                "countryName": "United Kingdom",
                                "synonym": ""
                    }
                    tempUkairportList_0.push(tempJSON);*/

                    var counrtyGP=['LN','SE','SW','MD','NE','NW','SC','NI'],
                		counrtyNames=['London Gatwick','London Luton','London Stansted','London Southend','Norwich','Southampton','Bournemouth','Bristol','Cardiff','Exeter',
                	              'Birmingham','East Midlands','Doncaster Sheffield','Humberside','Leeds Bradford','Newcastle','Liverpool John Lennon','Manchester',
                	              'Aberdeen','Edinburgh','Glasgow','Belfast International','Belfast City','City of Derry'],
                	    counrtyGPNames=['LONDON','SOUTH EAST', 'SOUTH WEST', 'MIDLANDS','NORTH EAST', 'NORTH WEST' , 'SCOTLAND', 'NORTHERN IRELAND'];

                    _.each(airportGuide.airportList, function(airport,i){
                    	if(airport.countryCode == "GBR"){
                    		//_.each(airport.group, function(group,j){
                        		if(airport.id === 'LGW'){
                        			airportGuide.airportList[i].group[0] ='LN';
                        		}
                        		if(airport.id === 'LTN'){
                        			airportGuide.airportList[i].group[0] ='LN';
                        		}
                        		if(airport.id === 'STN'){
                        			airportGuide.airportList[i].group[0] ='LN';
                        		}

                        		if(airport.id === 'SEN'){
                        			airportGuide.airportList[i].group[0] ='SE';
                    			}
                        		if(airport.id === 'NWI'){
                        			airportGuide.airportList[i].group[0] ='SE';
                    			}
                        		if(airport.id === 'SOU'){
                        			airportGuide.airportList[i].group[0] ='SE';
                    			}

                        		if(airport.id === 'BOH'){
                        			airportGuide.airportList[i].group[0] ='SW';
                    			}
                        		if(airport.id === 'BRS'){
                        			airportGuide.airportList[i].group[0] ='SW';
                    			}
                        		if(airport.id === 'CWL'){
                        			airportGuide.airportList[i].group[0] ='SW';
                    			}
                        		if(airport.id === 'EXT'){
                        			airportGuide.airportList[i].group[0] ='SW';
                    			}

                        		if(airport.id === 'BHX'){
                        			airportGuide.airportList[i].group[0] ='MD';
                    			}
                        		if(airport.id === 'EMA'){
                        			airportGuide.airportList[i].group[0] ='MD';
                    			}

                        		if(airport.id === 'DSA'){
                        			airportGuide.airportList[i].group[0] ='NE';
                    			}
                        		if(airport.id === 'NME'){
                        			airportGuide.airportList[i].group[0] ='NE';
                    			}
                        		if(airport.id === 'HUY'){
                        			airportGuide.airportList[i].group[0] ='NE';
                    			}
                        		if(airport.id === 'LBA'){
                        			airportGuide.airportList[i].group[0] ='NE';
                    			}
                        		if(airport.id === 'NCL'){
                        			airportGuide.airportList[i].group[0] ='NE';
                    			}


                        		if(airport.id === 'MAN'){
                        			airportGuide.airportList[i].group[0] ='NW';
                    			}

                        		if(airport.id === 'ABZ'){
                        			airportGuide.airportList[i].group[0] ='SC';
                    			}
                        		if(airport.id === 'EDI'){
                        			airportGuide.airportList[i].group[0] ='SC';
                    			}
                        		if(airport.id === 'GLA'){
                        			airportGuide.airportList[i].group[0] ='SC';
                    			}

                        		if(airport.id === 'BFS'){
                        			airportGuide.airportList[i].group[0]  ='NI';
                    			}

                        //});
                    		// null groups
                    		if(airport.id === 'BHD'){
                    			airportGuide.airportList[i].group[0] ='NI';
                			}
                    		if(airport.id === 'LDY'){
                    			airportGuide.airportList[i].group[0] ='NI';
                			}
                    		if(airport.id === 'LPL'){
                    			airportGuide.airportList[i].group[0] ='NW';
                			}


                    		tempUkairportList.push(airport);
                    	}
                    });

                    //tempUkairportList = tempUkairportList_0.concat(tempUkairportList_1);
                    //tempUkairportList = tempUkairportList_1;


                	var checkCName = "";
                    _.each(counrtyGP, function(tempGP, i){
                    	_.each(tempUkairportList, function(airport ,j){
                    		if(airport.group[0] === tempGP){
	                			airport.countryName = counrtyGPNames[i];
	                			airport.nextCol = false;
	                			if(airport.id =="EXT" || airport.id =="MAN"){
                    				airport.nextCol = true;
                    			}
	                			if(airport.countryName == checkCName){
	                    			airport.cFlag = false;
	                    		}else{
	                    			airport.cFlag = true;

	                    		}
	                    		checkCName = airport.countryName;
	                			tempUkairportList_0.push(airport);
	                		}
                    	});
                    })

                    airportGuide.ukairportList = tempUkairportList_0;

                    var overseasairportList=[];
                    _.each(airportGuide.airportList, function(airport){
                    	if(airport.countryCode !== "GBR"){
                    		airport.nextCol = false;
                    		if(airport.id =="RVN" || airport.id =="MRU"){
                				airport.nextCol = true;
                			}
                    		overseasairportList.push(airport);
                    	}
                    });



                airportGuide.airportList = airportGuide.sortByName(overseasairportList);

                    airportGuide.inherited(args);
                    airportGuide.showTabs();
                    //Update the view to conform to the latest datafeed.
                    _.each(airportGuide.airportList, function(item){
                    	airportGuide.updateAirportCheckboxes(item);
            		});
                    _.each(airportGuide.ukairportList, function(item){
                    	airportGuide.updateAirportCheckboxes(item);
            		});

                    // Disable airports selection if saved search has MAX_AIRPORTS_SELECTABLE items selected.
                    //airportGuide.disableGuideItems();
                    var emptyAirportModel = dojo.query(".empty-airport-model" ,airportGuide.expandableDom )[0];
                    if (airportGuide.searchPanelModel.from.data.length > 0) {
                			dojo.removeClass(emptyAirportModel, "inactive");
                    }
                    else{
                    	dojo.addClass(emptyAirportModel, "inactive");
                    }
                    if (airportGuide.widgetController.searchApi === "getPrice") {
                        dojo.addClass(airportGuide.widgetController.domNode, "open-guide");
                    }
                });
            },

            sortByName :  function(array){
                return _.sortBy(array, "countryName");
            },

            showTabs : function(){
            	var airportGuide= this,
            		SearchModel = airportGuide.searchPanelModel,
            		toAirport = SearchModel.to.data[0],
            		fromAirport = SearchModel.from.data[0],
            		airportTabs = dojo.byId("airportTabs"),
            		ukairportsContainer =dojo.byId("ukairportsContainer"),
            		overseasContainer = dojo.byId("overseasContainer");

	         	if(toAirport && toAirport.countryCode === "GBR"){
	         		domStyle.set(airportTabs, "display", "none");
	         		domStyle.set(ukairportsContainer, "display", "none");
	         		domStyle.set(overseasContainer, "display", "block");

	         	} else  if(toAirport && toAirport.countryCode !== "GBR"){
	         		domStyle.set(airportTabs, "display", "none");
	         		domStyle.set(ukairportsContainer, "display", "block");
	         		domStyle.set(overseasContainer, "display", "none");
	         	}else{
	         		if(fromAirport && fromAirport.countryCode == "GBR"){
	         			domStyle.set(ukairportsContainer, "display", "block");
		         		domStyle.set(overseasContainer, "display", "none");
	         		} else if(fromAirport && fromAirport.countryCode !== "GBR"){
	         			domStyle.set(ukairportsContainer, "display", "none");
		         		domStyle.set(overseasContainer, "display", "block");
	         		}
	         		domStyle.set(airportTabs, "display", "block");

	         	}
	         	airportGuide.setHeight();
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

                    	//Check if clicked check box is related to overseas airport, then needs to remove already selected one from the the list, if one exists.
                    	//if(clickedCheckboxCountryCode !== "GBR") { //Chiranjeevi: Cmmenting this line for the may release as we need to select only one airport
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
                    	//}

                        airportGuide.animatePill(clickedLabel, dojo.query("#where-from-text", airportGuide.widgetController.domNode)[0], function () {
                            // Execute the action after the animation.
                            airportGuide.searchPanelModel.from.add(airportModel);
                            airportGuide.updatePillText(airportModel);
                        });
                    }
                    return;
                }

                // DELETE
                // We are performing a removal from airport guide.
                // if id doesn't exist, we need to delete the airport group.

               //Chiranjeevi: commenting this part for scope changes in R2 as deselection  of airport is not allowed.
              /*  airportModel = airportGuide.searchPanelModel.from.get(clickedCheckboxId);
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
                }*/
            },

            updatePillText: function(airportModel){

            	var airportGuide = this,
            	pillPlaceHolder = dojo.query("#wherefromPlaceholder", airportGuide.domNode)[0];
        		pill = dojo.query(".value", airportGuide.domNode)[0];
            	pillPlaceHolder.style.display = "none";
				pill.style.display = "block";
        		pill.innerHTML =  airportModel.name + " (" + airportModel.id + ")" ;
            	/*var airportGuide = this,pill,
            		pillCnt = dojo.byId("fromPillCnt");
            		dojo.query(".placeholder", airportGuide.domNode)[0].style.display ="none";
            		pillCnt.innerHTML="";
            		pill = dojo.create("div", null, pillCnt);
            		pill.style.display = "block";
            		pill.style.float = "left";
            		pill.innerHTML =  airportModel.name + "&nbsp;" +" (" + airportModel.id + ")" ;*/


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
                            //domAttr.set(item, "disabled", true);
                        }
                    });
                } else {
                    _.each(items, function (item, i) {
                        var label = dojo.query(item).parent()[0];
                        if (dojo.hasClass(label, "manually-disabled")) {
                            dojo.removeClass(item, "manually-disabled");
                            dojo.removeClass(label, "manually-disabled");
                            //domAttr.set(item, "disabled", false);
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
                var emptyAirportModel = dojo.query(".empty-airport-model", airportGuide.expandableDom)[0];
                if (airportGuide.searchPanelModel.from.data.length > 0) {
                    dojo.removeClass(emptyAirportModel, "inactive");
                } else {
                    dojo.addClass(emptyAirportModel, "inactive");
                }
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
            },

            removePill : function(){
            	var airportGuide = this;
        		dojo.byId("fromPillCnt").innerHTML='';
        		dojo.query("#wherefromValue", airportGuide.domNode).text("");
            	dojo.query("#wherefromPlaceholder", airportGuide.domNode)[0].style.display ="block";
            },
            highlightTabs: function (){
            	try {
            		var ukAirportTabContainer = dojo.byId("ukairportsContainer");
                	var overseasAirportTabContainer = dojo.byId("overseasContainer");
            		if(ukAirportTabContainer.style.display == "block" || ukAirportTabContainer.style.display == ""){
            			dojo.addClass("ukairports","ukairports-selected");
            			dojo.addClass("overseasairports","overseasairports-notselected");
            			dojo.removeClass("ukairports","ukairports-notselected");
            			dojo.removeClass("overseasairports","overseasairports-selected");
            		} else {
            			dojo.addClass("ukairports","ukairports-notselected");
            			dojo.addClass("overseasairports","overseasairports-selected");
            			dojo.removeClass("ukairports","ukairports-selected");
            			dojo.removeClass("overseasairports","overseasairports-notselected");
            		}
            	} catch(err){
            		console.error(err.message)
            	}
            }

        });

        return tui.searchPanel.view.flights.AirportGuide;
    });

