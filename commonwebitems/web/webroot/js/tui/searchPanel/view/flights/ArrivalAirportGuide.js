define("tui/searchPanel/view/flights/ArrivalAirportGuide", [
    "dojo",
    "dojo/on",
    "dojo/has",
    "dojo/dom-attr",
    "dojo/query",
    "dojo/dom-style",
    "dojo/text!tui/searchPanel/view/flights/templates/AirportGuideTmpl.html",
    "tui/searchPanel/model/AirportModel",
    "tui/searchPanel/store/flights/ArrivalAirportGuideStore",
    'tui/searchBPanel/view/DestinationGuideSearchBox',
    "tui/searchPanel/view/SearchGuide",
    "dojo/store/Observable",
    "tui/utils/TuiAnimations",
    "tui/search/model/flights/SearchModel"],
    function (dojo, on, has, domAttr, query, domStyle, arrivalAirportGuideTmpl, AirportModel, Toggler, searchGuide ) {

        dojo.declare("tui.searchPanel.view.flights.ArrivalAirportGuide", [tui.searchPanel.view.SearchGuide, tui.search.model.flights.SearchModel], {

            // ----------------------------------------------------------------------------- properties

            tmpl: arrivalAirportGuideTmpl,

            arrivalAirportGuideStore: null,

            expandableProp: null,

            targetSelector: null,

            fromLimit: 6,

            destinationFieldProps: {},

            searchBox : null,

            guideTimer: 0,

            subscribableMethods: ["openExpandable", "closeExpandable", "openGlobalExpandable"],

            airportList: null,

            // number of columns
            columns: 3,

            // number of airports to render per column
            columnLength: 0,

            ukColumnLength: 0,

            overseasColumnLength: 0,

            selectedCountryCode: null,

            overseasSwapElement: null,

            isErrorOccured: false,

            itemSelected: null,

            // ----------------------------------------------------------------------------- methods

            postCreate: function () {
            	var arrivalAirportGuide = this;
                arrivalAirportGuide.inherited(arguments);
                var resultSet = arrivalAirportGuide.searchPanelModel.to.query();

                resultSet.observe(function (airportModel, remove, add) {
                    // if expandable is not created yet, let's ignore.
                    if (!arrivalAirportGuide.expandableDom) {
                        return;
                    }
                    // update views
                    arrivalAirportGuide.updateGuide(airportModel, (add > -1));
                   // arrivalAirportGuide.disableGuideItems();
                    arrivalAirportGuide.updateGuideCount();
                });
                arrivalAirportGuide.tagElement(arrivalAirportGuide.domNode, "Flying-To");

                /* Clear the selectedCountryCode and overseasSwapElement when ever the pill was getting removed from the textbox.
                   Published @ arrivalAirportMultiFieldList - removeDataItem() function. */
                   dojo.subscribe("tui/searchPanel/view/ArrivalAirportGuide/ClearStorageFields", function() {
                   arrivalAirportGuide.selectedCountryCode = null;
                   arrivalAirportGuide.overseasSwapElement = null
                });

                   arrivalAirportGuide.subscribe("tui/searchPanel/view/flights/AirportGuide/DepartureAirportCountry", function(departureCountryCode) {
                   	arrivalAirportGuide.itemSelected = departureCountryCode;
                   });
            },

            attachEventListeners: function () {},

            openGlobalExpandable: function (controller) {
                var arrivalAirportGuide = this;
                if (controller === arrivalAirportGuide.widgetController) {
                    arrivalAirportGuide.openExpandable();
                }
            },

            attachOpenEvent: function () {
                var arrivalAirportGuide = this;
                on(arrivalAirportGuide.domNode, "click", function (event) {
                	if(arrivalAirportGuide.errorPopup){
                		arrivalAirportGuide.errorPopup.close();
                	}
                    if (dojo.hasClass(arrivalAirportGuide.domNode, "loading")) {
                        return;
                    }
                    if (arrivalAirportGuide.expandableDom === null || !arrivalAirportGuide.isShowing(arrivalAirportGuide.expandableDom)) {
                        //dojo.publish("tui.searchPanel.view.DestinationGuide.closeExpandable");
                        setTimeout(function () {
                            arrivalAirportGuide.openExpandable();
                        }, 350);
                    } else {
                        arrivalAirportGuide.closeExpandable();
                    }
                });
                /*on(arrivalAirportGuide.domNode,"blur",function(evt){
                	arrivalAirportGuide.closeExpandable();
               });*/
                /*on(arrivalAirportGuide.domNode,"focus",function(evt){
                	setTimeout(function () {
                        arrivalAirportGuide.openExpandable();
                    }, 350);
               });*/
            },

           /* cancelBlur: function () {
                var arrivalAirportGuide = this;
                arrivalAirportGuide.inherited(arguments);
                arrivalAirportGuide.domNode.focus();
            },*/


            createSearchBox: function () {
            	// summary:
            	//		Create the new search box with magnifier with autocomplete enabled
                  var arrivalAirportGuide = this, html, destListDom, data;
                  arrivalAirportGuide.destinationFieldProps.searchPanelModel = arrivalAirportGuide.searchPanelModel;
                  arrivalAirportGuide.destinationFieldProps.widgetController = arrivalAirportGuide.widgetController;
                  arrivalAirportGuide.searchBox = new tui.searchBPanel.view.DestinationGuideSearchBox(arrivalAirportGuide.destinationFieldProps, query(".destination-search-box", arrivalAirportGuide.expandableDom)[0]);

                },

            onAfterTmplRender: function () {
                // summary:
                //		After the expandable dom has been created,
                //		we attach event listeners to the checkboxes and links

            	var arrivalAirportGuide = this;
           	    arrivalAirportGuide.inherited(arguments);

           	    //arrivalAirportGuide.createSearchBox();
                // use dojo "on" and event delegation
                on(arrivalAirportGuide.expandableDom, "label:click", function (event) {
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
                    dojo.publish("tui.searchPanel.view.flights.ArrivalAirportMultiFieldList.pulse", [arrivalAirportGuide.widgetController]);
                    //arrivalAirportGuide.selectAirports(checkbox);

                    if(!arrivalAirportGuide.isErrorOccured) {
                    	arrivalAirportGuide.updatetoItems(checkbox);
                    }
                    else {
                    	arrivalAirportGuide.isErrorOccured = false;
                    }
                   // arrivalAirportGuide.cancelBlur();

                    arrivalAirportGuide.closeExpandable(arrivalAirportGuide.expandableDom);
                    window.scrollTo(0,0);
                    if(dijit.byId("where-to").id == "where-to"){
                    	dijit.byId("where-to").searchPanelModel.searchErrorMessages.set("to", {})
    				}
                });

                // add dom event to unselect all link.
                on(arrivalAirportGuide.expandableDom, ".empty-airport-model:click", function (event) {
                    dojo.stopEvent(event);
                    if (dojo.hasClass(event.target, "inactive")) {
                        return;
                    }
                    arrivalAirportGuide.clearAll();
                });

                arrivalAirportGuide.subscribe("tui/searchPanel/searchOpening", function (component) {
                	/*console.log(dojo.byId(component.domNode).id);
                	console.log(dojo.byId(arrivalAirportGuide.domNode).id);*/
                    /*if (component !== arrivalAirportGuide) {
                    	dojo.publish("tui.searchPanel.view.AirportMultiFieldList.whereFrom", null);
                    	arrivalAirportGuide.closeExpandable();
                    } */
                });

                var columns = dojo.query(".col", arrivalAirportGuide.expandableDom);
                _.each(columns, function (column, index) {
                	if(index < 3){
                		arrivalAirportGuide.tagElement(column, "OverallFlyingto");
                	}else if(index > 3){
                		arrivalAirportGuide.tagElement(column, "OverallFlyingto1");
                	}
                });

                arrivalAirportGuide.tagElement(dojo.query(".close-hide", arrivalAirportGuide.expandableDom)[0], "Airport Hide");
                arrivalAirportGuide.tagElement(dojo.query(".empty-airport-model", arrivalAirportGuide.expandableDom)[0], "Airport Deselect All");

                /*arrivalAirportGuide.subscribe("tui/searchPanel/searchOpening", function (component) {
                    if (component !== arrivalAirportGuide) {
                    	arrivalAirportGuide.closeExpandable();
                    }
                  });*/

            },

            openExpandable: function () {
                // summary:
                //		Extend default expandable behaviour,
                //		only open once airport guide data is available and returned from the server.
                var arrivalAirportGuide = this;
                var args = arguments;
                dojo.addClass(arrivalAirportGuide.domNode, "loading");


                if(arrivalAirportGuide.searchPanelModel.to.data.length >0){
                	arrivalAirportGuide.overseasSwapElement = arrivalAirportGuide.searchPanelModel.to.data[0].id;
                }

                dojo.publish("tui.searchPanel.view.flights.ArrivalAirportMultiFieldList.highlight", [true, arrivalAirportGuide.widgetController]);

                dojo.when(arrivalAirportGuide.arrivalAirportGuideStore.requestData(arrivalAirportGuide.searchPanelModel.generateQueryObject(), false), function (responseData) {
                    dojo.removeClass(arrivalAirportGuide.domNode, "loading");

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
                    //console.log(airportTempList);


                    arrivalAirportGuide.airportList = airportSwapList;
                    arrivalAirportGuide.ukColumnLength = Math.ceil(_.size(responseData["GBR"]) / arrivalAirportGuide.columns);
                   //arrivalAirportGuide.columnLength = Math.ceil(_.size(arrivalAirportGuide.airportList) / arrivalAirportGuide.columns);
                    arrivalAirportGuide.columnLength = Math.ceil((_.size(arrivalAirportGuide.airportList) - _.size(responseData["GBR"])) / 3);
                   arrivalAirportGuide.overseasColumnLength =  arrivalAirportGuide.columnLength -  arrivalAirportGuide.ukColumnLength;

                    //arrivalAirportGuide.columnLength = Math.ceil((_.size(arrivalAirportGuide.airportList) - _.size(responseData["GBR"])) / arrivalAirportGuide.columns);
                    //arrivalAirportGuide.overseasColumnLength =  arrivalAirportGuide.columnLength -  arrivalAirportGuide.ukColumnLength
                    //arrivalAirportGuide.ukairportList=responseData["GBR"];

                   	var tempUkairportList=[],
                   		tempUkairportList_0=[],
                   		tempUkairportList_1=[],
                   		counrtyGP=['LN','SE','SW','MD','NE','NW','SC','NI'],
                   		counrtyGPNames=['LONDON','SOUTH EAST', 'SOUTH WEST', 'MIDLANDS','NORTH EAST', 'NORTH WEST' , 'SCOTLAND', 'NORTHERN IRELAND'];

                    _.each(arrivalAirportGuide.airportList, function(airport,i){
                    	if(airport.countryCode == "GBR"){
                    		//_.each(airport.group, function(group,j){
                        		if(airport.id === 'LGW'){
                        			arrivalAirportGuide.airportList[i].group[0] ='LN';
                        		}
                        		if(airport.id === 'LTN'){
                        			arrivalAirportGuide.airportList[i].group[0] ='LN';
                        		}
                        		if(airport.id === 'STN'){
                        			arrivalAirportGuide.airportList[i].group[0] ='LN';
                        		}

                        		if(airport.id === 'SEN'){
                        			arrivalAirportGuide.airportList[i].group[0] ='SE';
                    			}
                        		if(airport.id === 'NWI'){
                        			arrivalAirportGuide.airportList[i].group[0] ='SE';
                    			}
                        		if(airport.id === 'SOU'){
                        			arrivalAirportGuide.airportList[i].group[0] ='SE';
                    			}

                        		if(airport.id === 'BOH'){
                        			arrivalAirportGuide.airportList[i].group[0] ='SW';
                    			}
                        		if(airport.id === 'BRS'){
                        			arrivalAirportGuide.airportList[i].group[0] ='SW';
                    			}
                        		if(airport.id === 'CWL'){
                        			arrivalAirportGuide.airportList[i].group[0] ='SW';
                    			}
                        		if(airport.id === 'EXT'){
                        			arrivalAirportGuide.airportList[i].group[0] ='SW';
                    			}

                        		if(airport.id === 'BHX'){
                        			arrivalAirportGuide.airportList[i].group[0] ='MD';
                    			}
                        		if(airport.id === 'EMA'){
                        			arrivalAirportGuide.airportList[i].group[0] ='MD';
                    			}

                        		if(airport.id === 'DSA'){
                        			arrivalAirportGuide.airportList[i].group[0] ='NE';
                    			}
                        		if(airport.id === 'NME'){
                        			arrivalAirportGuide.airportList[i].group[0] ='NE';
                    			}
                        		if(airport.id === 'HUY'){
                        			arrivalAirportGuide.airportList[i].group[0] ='NE';
                    			}
                        		if(airport.id === 'LBA'){
                        			arrivalAirportGuide.airportList[i].group[0] ='NE';
                    			}
                        		if(airport.id === 'NCL'){
                        			arrivalAirportGuide.airportList[i].group[0] ='NE';
                    			}


                        		if(airport.id === 'MAN'){
                        			arrivalAirportGuide.airportList[i].group[0] ='NW';
                    			}

                        		if(airport.id === 'ABZ'){
                        			arrivalAirportGuide.airportList[i].group[0] ='SC';
                    			}
                        		if(airport.id === 'EDI'){
                        			arrivalAirportGuide.airportList[i].group[0] ='SC';
                    			}
                        		if(airport.id === 'GLA'){
                        			arrivalAirportGuide.airportList[i].group[0] ='SC';
                    			}

                        		if(airport.id === 'BFS'){
                        			arrivalAirportGuide.airportList[i].group[0] ='NI';
                    			}

                        //});
                    		// null groups
                    		if(airport.id === 'BHD'){
                    			arrivalAirportGuide.airportList[i].group[0] ='NI';
                			}
                    		if(airport.id === 'LDY'){
                    			arrivalAirportGuide.airportList[i].group[0] ='NI';
                			}
                    		if(airport.id === 'LPL'){
                    			arrivalAirportGuide.airportList[i].group[0] ='NW';
                			}


                    		tempUkairportList.push(airport);
                    	}
                    });

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

                    arrivalAirportGuide.ukairportList = tempUkairportList_0;

                   //Chiranjeevi: Commenting this as we dont need Any London grouping for may release.
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
                   tempUkairportList_0.push(tempJSON);

                   _.each(arrivalAirportGuide.airportList, function(airport){
                   	if(airport.countryCode == "GBR" && airport.group == "SE,LN"){
                   		tempUkairportList_0.push(airport);
                   	}else if(airport.countryCode == "GBR"){
                   		tempUkairportList_1.push(airport);
                   	}
                   });

                   tempUkairportList = tempUkairportList_0.concat(tempUkairportList_1);

                   arrivalAirportGuide.ukairportList = tempUkairportList;
                   */
                    var overseasairportList=[];
                    _.each(arrivalAirportGuide.airportList, function(airport){
                    	if(airport.countryCode !== "GBR"){
                    		airport.nextCol = false;
                    		if(airport.id =="RVN" || airport.id =="MRU"){
                				airport.nextCol = true;
                			}
                    		overseasairportList.push(airport);
                    	}
                    });

                    arrivalAirportGuide.airportList = arrivalAirportGuide.sortByName(overseasairportList);


                arrivalAirportGuide.inherited(args);
                arrivalAirportGuide.showTabs();
                    //Update the view to conform to the latest datafeed.
                    _.each(arrivalAirportGuide.airportList, function(item){
                    	arrivalAirportGuide.updateAirportCheckboxes(item);
            		});
                    _.each(arrivalAirportGuide.ukairportList, function(item){
                    	arrivalAirportGuide.updateAirportCheckboxes(item);
            		});
                    // Disable airports selection if saved search has MAX_AIRPORTS_SELECTABLE items selected.
                   // arrivalAirportGuide.disableGuideItems();

                    var emptyAirportModel = dojo.query(".empty-airport-model", arrivalAirportGuide.expandableDom)[0];
                    if (arrivalAirportGuide.searchPanelModel.to.data.length > 0) {
                    		dojo.removeClass(emptyAirportModel, "inactive");
                    }else{
                    	dojo.addClass(emptyAirportModel, "inactive");
                    }

                    if (arrivalAirportGuide.widgetController.searchApi === "getPrice") {
                        dojo.addClass(arrivalAirportGuide.widgetController.domNode, "open-guide");
                    }


                });
            },


            sortByName :  function(array){
                return _.sortBy(array, "countryName");
            },

            showTabs : function(){
            	var arrivalAirportGuide= this,
            		SearchModel = arrivalAirportGuide.searchPanelModel,
            		fromAirport = SearchModel.from.data[0],
            		toAirport = SearchModel.to.data[0],
            		airportTabs = dojo.byId("arrivalAirportTabs"),
            		ukairportsContainer =dojo.byId("arrivalUkairportsContainer"),
            		overseasContainer = dojo.byId("arrivalOverseasContainer");

	         	if(fromAirport && fromAirport.countryCode === "GBR"){
	         		domStyle.set(airportTabs, "display", "none");
	         		domStyle.set(ukairportsContainer, "display", "none");
	         		domStyle.set(overseasContainer,  "display", "block");

	         	} else  if(fromAirport && fromAirport.countryCode !== "GBR"){
	         		domStyle.set(airportTabs, "display", "none");
	         		domStyle.set(ukairportsContainer, "display", "block");
	         		domStyle.set(overseasContainer, "display", "none");
	         	} else{
	         		if(toAirport && toAirport.countryCode == "GBR"){
	         			domStyle.set(ukairportsContainer, "display", "block");
		         		domStyle.set(overseasContainer, "display", "none");
	         		} else if(toAirport && toAirport.countryCode !== "GBR") {
	         			domStyle.set(ukairportsContainer, "display", "none");
		         		domStyle.set(overseasContainer, "display", "block");
	         		}
	         		domStyle.set(airportTabs, "display", "block");
	         	}

	         	arrivalAirportGuide.setHeight();
            },

            setHeight :  function(){
            	var arrivalAirportGuide = this;
            	var	wrapper = dojo.query('.wrapper', arrivalAirportGuide.expandableDom)[0];
            	var height =  _.pixels(dojo.position(wrapper).h);
            		setTimeout(function(){
            			dojo.style(arrivalAirportGuide.expandableDom, 'maxHeight', height);
            		},100);
            },


            /*closeExpandable: function () {
                var arrivalAirportGuide = this;
                arrivalAirportGuide.inherited(arguments);
                dojo.publish("tui.searchPanel.view.AirportMultiFieldList.highlight", [false, arrivalAirportGuide.widgetController]);

                if (arrivalAirportGuide.widgetController.searchApi === "getPrice") {
                    dojo.removeClass(arrivalAirportGuide.widgetController.domNode, "open-guide");
                }
            },*/

            showWidget: function (element) {
                var arrivalAirportGuide = this;
                if(arrivalAirportGuide.widgetController.searchApi === "getPrice") {
                    // TODO: fix width being sent, this is arbitrary
                    dojo.publish("tui.searchGetPrice.view.GetPriceModal.resize", 777);
                    // ie7 has issues with getPrice guide
                    // TODO: fix this at some point
                    if (has("ie") === 7) dojo.setStyle(element, {"width": "auto"});
                }
                arrivalAirportGuide.inherited(arguments);
            },

            hideWidget: function (element) {
                var arrivalAirportGuide = this;
                if(arrivalAirportGuide.widgetController.searchApi === "getPrice") {
                    // TODO: fix width being sent, this is arbitrary
                    dojo.publish("tui.searchGetPrice.view.GetPriceModal.resize", 358);
                    // ie7 has issues with getPrice guide
                    // TODO: fix this at some point
                    if (has("ie") === 7) dojo.setStyle(element, {"width": 0});
                }
                /*console.log(arguments);*/
                arrivalAirportGuide.inherited(arguments);
            },

            place: function (html) {
                // summary:
                //		Override place method from simpleExpandable,
                //		place the airport guide in the main search container.
                var arrivalAirportGuide = this,
                    target = arrivalAirportGuide.targetSelector
                        ? dojo.query(arrivalAirportGuide.targetSelector, arrivalAirportGuide.widgetController.domNode)[0]
                        : arrivalAirportGuide.widgetController.domNode;
                return dojo.place(html, target, "last");
            },

            getAirportItemAttributes: function (airport) {
                // summary:
                //		Returns attributes for airport
                return dojo.mixin(new AirportModel(),{
                    name: domAttr.get(airport, "data-airportmodel-name"),
                    id: domAttr.get(airport, "data-airportmodel-id"),
                    group: domAttr.get(airport, "data-airportmodel-groups").split(","),
                    countryCode: domAttr.get(airport, "data-airportmodel-countryCode")
                });
            },

            selectAirports: function (clickedCheckbox) {
              	 var arrivalAirportGuide = this;
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
               	 dojo.publish("tui/searchPanel/view/flights/ArrivalAirportGuide/ArrivalAirportCountry", [clickedCheckboxCountryCode]);
                     if(arrivalAirportGuide.itemSelected !== null && clickedCheckboxCountryCode !== null) {
    	              	 if((arrivalAirportGuide.itemSelected === "GBR" && clickedCheckboxCountryCode === "GBR")
    	              			 || (arrivalAirportGuide.itemSelected !== "GBR" && clickedCheckboxCountryCode !== "GBR")) {
    	           	   //Validation to check the user should select only UK to overseas and vice versa.
    	           	    arrivalAirportGuide.searchPanelModel.searchErrorMessages.set("invalidFromAndToFlyingCombination", {
   		          			invalidFromAndToFlyingCombination: arrivalAirportGuide.searchMessaging.errors.invalidFromandToAirportCombination
   		          		  });
    	              		arrivalAirportGuide.isErrorOccured	= true;
    	              		return;
    	              }
                   }
                }
                else {
               	 dojo.publish("tui/searchPanel/view/flights/ArrivalAirportGuide/ArrivalAirportCountry", null);
                }
              },

            updatetoItems: function (clickedCheckbox) {
                // summary:
                //		Adds/removes selected airport to/from airport model.
                var arrivalAirportGuide = this;
                var airportModel;

                var clickedCheckboxId = domAttr.get(clickedCheckbox, "data-airportmodel-id");
                var clickedCheckboxCountryCode = domAttr.get(clickedCheckbox, "data-airportmodel-countryCode");

                //set the selected airport country code to global variable
                /*
                 * Chiranjeevi: Commenting this as we don't need validation for airport selection in R2(expectation is if uk airport is selected then again overseas airport is selected it should be swapped to overseas).
                 *
                 * if(arrivalAirportGuide.selectedCountryCode !== null) {
                	if(arrivalAirportGuide.selectedCountryCode === "GBR"
                		&& arrivalAirportGuide.selectedCountryCode !== clickedCheckboxCountryCode) {
                		arrivalAirportGuide.searchPanelModel.searchErrorMessages.set("invalidArrivalAirportCombination", {
                			invalidArrivalAirportCombination: arrivalAirportGuide.searchMessaging.errors.invalidAirportCombination
                	    });
                		 return;
                	}
                	else if(arrivalAirportGuide.selectedCountryCode !== "GBR"
                		&& clickedCheckboxCountryCode === "GBR") {
                		arrivalAirportGuide.searchPanelModel.searchErrorMessages.set("invalidArrivalAirportCombination", {
                			invalidArrivalAirportCombination: arrivalAirportGuide.searchMessaging.errors.invalidAirportCombination
               	    });
                		return;
                	}
                }
                else {
                	arrivalAirportGuide.selectedCountryCode = clickedCheckboxCountryCode;
                }*/

                //Chiranjeevi: adding from the above commented section of else condition
                if(arrivalAirportGuide.selectedCountryCode !== null) {
                	arrivalAirportGuide.selectedCountryCode = clickedCheckboxCountryCode;
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
                    airportModel = arrivalAirportGuide.getAirportItemAttributes(clickedCheckbox);
                    if (airportModel) {

                    	//Check if clicked check box is related to overseas airport, then needs to remove already selected one from the the list, if one exists.
                    	//if(clickedCheckboxCountryCode !== "GBR") { //Chiranjeevi: Cmmenting this line for the may release as we need to select only one airport
                    		//add the first overseas airport to overseasSwapElement
                    		if(arrivalAirportGuide.searchPanelModel.to.selectedSize > 0){
                    			arrivalAirportGuide.searchPanelModel.to.emptyStore();

                    		}

                    		if(!arrivalAirportGuide.overseasSwapElement) {
                    			arrivalAirportGuide.overseasSwapElement = clickedCheckboxId;
                    		}
                    		else {
                    			// remove the existing overseas airport and add the newly selected one to overseasSwapElement
                    			arrivalAirportGuide.searchPanelModel.to.remove(this.overseasSwapElement);
                    			arrivalAirportGuide.overseasSwapElement = clickedCheckboxId;
                    		}
                    	//}

                        arrivalAirportGuide.animatePill(clickedLabel, dojo.query("#where-to-text", arrivalAirportGuide.widgetController.domNode)[0], function () {
                            // Execute the action after the animation.
                            arrivalAirportGuide.searchPanelModel.to.add(airportModel);
                            arrivalAirportGuide.updatePillText(airportModel);
                        });

                    }
                    return;
                }

                // DELETE
                // We are performing a removal from airport guide.
                // if id doesn't exist, we need to delete the airport group.

                //Chiranjeevi: commenting this part for scope changes in R2 as deselection  of airport is not allowed.

               /*
                * airportModel = arrivalAirportGuide.searchPanelModel.to.get(clickedCheckboxId);
                if (airportModel) {
                    arrivalAirportGuide.searchPanelModel.to.remove(clickedCheckboxId);
                    arrivalAirportGuide.selectedCountryCode = null;
                    arrivalAirportGuide.overseasSwapElement = null;
                    arrivalAirportGuide.itemSelected = null;
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
                        var airportGroupModel = arrivalAirportGuide.searchPanelModel.to.get(id);
                        if (airportGroupModel) {
                            arrivalAirportGuide.searchPanelModel.to.remove(id);

                            _.each(airportGroupModel.children, function (child) {
                                if (clickedCheckboxId !== child) {
                                    // select children of group (except for current item)
                                    var airportCheckbox = dojo.query("." + child, arrivalAirportGuide.expandableDom)[0];
                                    var airportModel = arrivalAirportGuide.getAirportItemAttributes(airportCheckbox);

                                    // Making sure we're not adding the airport twice.
                                    if (!arrivalAirportGuide.searchPanelModel.to.get(airportModel.id)) {
                                        arrivalAirportGuide.searchPanelModel.to.add(airportModel);
                                    }
                                }
                            });
                        }
                    });
                }*/
            },


            updatePillText: function(airportModel){

            	var arrivalAirportGuide = this,
            		pill = dojo.query("#wheretoValue", arrivalAirportGuide.domNode)[0];
            		dojo.query("#wheretoPlaceholder", arrivalAirportGuide.domNode).style("display","none");
            		pill.style.display = "block";
					pill.innerHTML =  airportModel.name + " (" + airportModel.id + ")" ;
            		/*
            		 * pillCnt = dojo.byId("toPillCnt");
            		 * pillCnt.innerHTML="";
            		var pill = dojo.create("div", null, pillCnt);
            		pill.style.display = "block";
            		pill.style.float = "left"; */

            },

            updateGuide: function (item, add) {
                // summary:
                //		Updates guide, selects/unselects items to match Search model
                var arrivalAirportGuide = this;
                var action = (add) ? "addClass" : "removeClass";

                // add/remove "selected" class, select or unselect checkbox.
                if (item.children && item.children.length > 0) {
                    _.each(item.children, function (child, i) {
                        var input = dojo.query("." + child, arrivalAirportGuide.expandableDom)[0];
                        domAttr.set(input, "checked", add);
                        dojo.query(input).parent()[action]("selected");
                    });
                } else {
                    var input = dojo.query("." + item.id, arrivalAirportGuide.expandableDom)[0];
                    domAttr.set(input, "checked", add);
                    dojo.query(input).parent()[action]("selected");
                }
               // arrivalAirportGuide.closeExpandable(arrivalAirportGuide.expandableDom);
                dojo.removeClass(dojo.byId("where-to"),"border-sel-active");
            },

            updateAirportCheckboxes: function (arrivalAirportGuideItem) {
                // summary:
                //    Changes item state to enabled/disabled depending on server response
                var arrivalAirportGuide = this;
                if (!arrivalAirportGuide.expandableDom) {
                    return;
                }

                var input = dojo.query("." + arrivalAirportGuideItem.id, arrivalAirportGuide.expandableDom);
                if (!arrivalAirportGuideItem.available) {
                    //domAttr.set(input[0], "disabled", true);
                    dojo.addClass(dojo.query(input).parent()[0], "disabled");

                    // For security, we remove the airport from the model if there.
                    var airportGroupModel = arrivalAirportGuide.searchPanelModel.to.get(arrivalAirportGuideItem.id);
                    if (airportGroupModel) {
                        arrivalAirportGuide.searchPanelModel.to.remove(arrivalAirportGuideItem.id);
                    }
                } else {
                    //domAttr.set(input[0], "disabled", false);
                    dojo.removeClass(dojo.query(input).parent()[0], "disabled");
                }
            },

            disableGuideItems: function () {
                // summary:
                //    Disables guide items when fromLimit is reached.
                var arrivalAirportGuide = this;
                var items = dojo.query("input[type='checkbox']", arrivalAirportGuide.expandableDom);
                if (arrivalAirportGuide.searchPanelModel.to.selectedSize >= arrivalAirportGuide.fromLimit) {
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
                var arrivalAirportGuide = this;
                //console.dir(dojo.query(".empty-airport-model", arrivalAirportGuide.expandableDom)[0]);
                // Remove inactive class if we have airports which we can remove.
                var emptyAirportModel = dojo.query(".empty-airport-model", arrivalAirportListOverlay.expandableDom)[0];
                if (arrivalAirportGuide.searchPanelModel.to.data.length > 0) {
                    dojo.removeClass(emptyAirportModel, "inactive");
                } else {
                    dojo.addClass(emptyAirportModel, "inactive");
                }
                // update airport guide selected count.
                dojo.query(".airport-guide-count", arrivalAirportGuide.expandableDom).text(arrivalAirportGuide.searchPanelModel.to.selectedSize);

            },

            clearAll: function () {
                // summary:
                //    Empties model when clicking "unselect all" link.
                //    Removes "selected" class from labels.
                var arrivalAirportGuide = this;
                arrivalAirportGuide.cancelBlur();
                arrivalAirportGuide.domNode.focus();
                _.each(arrivalAirportGuide.searchPanelModel.to.query(), function (airportModel) {
                    arrivalAirportGuide.searchPanelModel.to.remove(airportModel.id);
                });
                arrivalAirportGuide.removePill();
                // Remove all checked status of checkboxes.
                dojo.query("label.selected", arrivalAirportGuide.expandableDom).removeClass("selected");
                var checkboxes = dojo.query("input[type='checkbox']", arrivalAirportGuide.expandableDom);
                _.each(checkboxes, function (checkbox) {
                    domAttr.set(checkbox, "checked", false);
                });
            },

            removePill : function(){
            	var arrivalAirportGuide = this;
        		dojo.byId("toPillCnt").innerHTML='';
            	dojo.query("#wheretoPlaceholder", arrivalAirportGuide.domNode)[0].style.display ="block";
            	dojo.query("#wheretoValue", arrivalAirportGuide.domNode).text("");

            },
            highlightTabs: function (){
            	try {
            		var ukAirportTabContainer = dojo.byId("arrivalUkairportsContainer");
                	var overseasAirportTabContainer = dojo.byId("arrivalOverseasContainer");
            		if(overseasAirportTabContainer.style.display == "block" || overseasAirportTabContainer.style.display == ""){
            			dojo.addClass("ukairports-arrival","ukairports-arrival-notselected");
            			dojo.addClass("overseasairports-arrival","overseasairports-arrival-selected");
            			dojo.removeClass("ukairports-arrival","ukairports-arrival-selected");
            			dojo.removeClass("overseasairports-arrival","overseasairports-arrival-notselected");
            		} else {
            			dojo.addClass("ukairports-arrival","ukairports-arrival-selected");
            			dojo.addClass("overseasairports-arrival","overseasairports-arrival-notselected");
            			dojo.removeClass("ukairports-arrival","ukairports-arrival-notselected");
            			dojo.removeClass("overseasairports-arrival","overseasairports-arrival-selected");
            		}
            	} catch(err){
            		console.error(err.message)
            	}
            }

        });

        return tui.searchPanel.view.flights.ArrivalAirportGuide;
    });

