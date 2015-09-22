define("tui/searchPanel/view/cruise/AirportGuide", [
    "dojo",
    "dojo/on",
    'dojo/query',
    "dojo/has",
    "dojo/dom-attr",
    "dojo/text!tui/searchPanel/view/cruise/templates/AirportGuideTmpl.html",
    "tui/searchPanel/model/AirportModel",
    "tui/searchPanel/store/cruise/AirportGuideStore",
    "dojo/store/Observable",
    "tui/utils/TuiAnimations",
    "tui/searchPanel/view/SearchGuide",
    "tui/searchPanel/view/SearchErrorMessaging"],
    function (dojo, on, query, has, domAttr, airportGuideTmpl, AirportModel) {

        dojo.declare("tui.searchPanel.view.cruise.AirportGuide", [tui.searchPanel.view.SearchGuide,tui.searchPanel.view.SearchErrorMessaging], {

            // ----------------------------------------------------------------------------- properties

            tmpl: airportGuideTmpl,

            airportGuideStore: null,

            expandableProp: null,

            targetSelector: null,

            fromLimit: 6,

            guideTimer: 0,

            subscribableMethods: ["openExpandable", "closeExpandable", "openGlobalExpandable"],

            airportList: null,

            pocs: null,

            // number of columns
            columns: 5,

            // number of airports to render per column
            columnLength: 0,

            //total child airports
            totalChildAirports: 0,

            parentAirportsDomNode:null,

            placeholder:null,

            // ----------------------------------------------------------------------------- methods

            postCreate: function () {
                var airportGuide = this;

                airportGuide.inherited(arguments);
                airportGuide.placeholder = dojo.query(".placehold", airportGuide.domNode)[0];
                airportGuide.updatePlaceHolder();
                var resultSet = airportGuide.searchPanelModel.from.query();
				airportGuide.airportGuideTitle = airportGuide.searchMessaging[dojoConfig.site].airportGuide.title;
                resultSet.observe(function (airportModel, remove, add) {
                    airportGuide.updatePlaceHolder();
                    // if expandable is not created yet, let's ignore.
                    if (!airportGuide.expandableDom) {
                        return;
                    }
                    // update views
                    airportGuide.updateGuide(airportModel, (add > -1));
                    airportGuide.disableGuideItems();
                    airportGuide.updateParentAirports();
                    dojo.publish("tui:channel=updateResponse", ['from']);
                });

                dojo.subscribe("tui:channel=modalOpening", function(){
                    airportGuide.closeExpandable();
                });

                airportGuide.connect(document.body, "onclick", function (event) {
                    if(event.target.className === 'close-list')
                    {airportGuide.closeExpandable();}
                    if (document.activeElement === airportGuide.domNode) return;
                    if (airportGuide.expandableDom === null || !airportGuide.isShowing(airportGuide.expandableDom) || airportGuide.inGuide(airportGuide.expandableDom, event)) {
                        return;
                    }
                    airportGuide.closeExpandable();
                });

                airportGuide.tagElement(airportGuide.domNode, "where-from");
            },

            inGuide: function (element, event) {
                return _.size(query(event.target).closest(".guide")) > 0;
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
                    if (dojo.hasClass(airportGuide.domNode, "loading")) {
                        return;
                    }
                    if (airportGuide.expandableDom === null || !airportGuide.isShowing(airportGuide.expandableDom)) {
                        setTimeout(function () {
                            airportGuide.openExpandable();
                        }, 350);
                    } else {
                        airportGuide.closeExpandable();
                    }
                });
            },

            cancelBlur: function () {
                var airportGuide = this;
                airportGuide.inherited(arguments);
                airportGuide.domNode.focus();
            },

            onAfterTmplRender: function () {
                // summary:
                //		After the expandable dom has been created,
                //		we attach event listeners to the checkboxes and links
                var airportGuide = this;
                airportGuide.inherited(arguments);
              //Retriving all parent airports
                airportGuide.parentAirportsDomNode = dojo.query(".parent-airport", airportGuide.expandableDom);

                // use dojo "on" and event delegation
                on(airportGuide.expandableDom, "label:click", function (event) {
                    dojo.stopEvent(event);

                    var label, checkbox;
                    var checkChilds, allcheckChilds, disabledcheckChilds = [];
                    if (airportGuide.widgetController.searchApi === "getPrice") {

                        if (event.target.tagName.toUpperCase() === "INPUT") {

                        	checkbox = event.target;
                          	label = event.target.parentElement;
	                        if(dojo.hasClass(label.parentElement,"child-airport")){
	                          checkChilds = '';
	                        }
	                        else {

		                          nextSibling = dojo.query(label.parentElement).next()[0];
		                          if(nextSibling && dojo.hasClass(nextSibling,"child-grp")){

		                             //checkChilds = dojo.query(".child-airport input", nextSibling);
		                              allcheckChilds = dojo.query(".child-airport label input", label.parentElement.parentElement);
		                              disabledcheckChilds = dojo.query(".child-airport label.disabled input", label.parentElement.parentElement);
		                              checkChilds = _.difference(allcheckChilds, disabledcheckChilds);
		                          }
		                          else {
		                             checkChilds = '';
		                          }

	                        }
                        } else {
                          checkbox = event.target.children[0];
                          label = event.target;
                                    //checkChilds = '';
                                    if(dojo.hasClass(label.parentElement,"child-airport")){
                                        checkChilds = '';
                                      }
                                    else {
                                        nextSibling = dojo.query(label.parentElement).next()[0];
                                        if(nextSibling && dojo.hasClass(nextSibling,"child-grp")){
                                           //checkChilds = dojo.query(".child-airport input", nextSibling);
                                                        allcheckChilds = dojo.query(".child-airport label input", label.parentElement.parentElement);
                                            disabledcheckChilds = dojo.query(".child-airport label.disabled input", label.parentElement.parentElement);
                                            checkChilds = _.difference(allcheckChilds, disabledcheckChilds);
                                        }
                                        else {
                                           checkChilds = '';
                                        }

                                      }
                                }
	                }
	                else {
                        if (event.target.tagName.toUpperCase() === "INPUT") {

                          checkbox = event.target;
                          label = event.target.parentElement;
                                            if(dojo.hasClass(label.parentElement,"child-airport")){
                                              checkChilds = '';
                                            }
                                            else {
                                              allcheckChilds = dojo.query(".child-airport label input", label.parentElement.parentElement);
                                              disabledcheckChilds = dojo.query(".child-airport label.disabled input", label.parentElement.parentElement);
                                              checkChilds = _.difference(allcheckChilds, disabledcheckChilds);
                                            }
                        } else {
                          checkbox = event.target.children[0];
                          label = event.target;

                            if(dojo.hasClass(label.parentElement,"child-airport")){

                              checkChilds = '';
                            }
                            else {
                              //checkChilds = dojo.query(".child-airport input", label.parentElement.parentElement);
                                allcheckChilds = dojo.query(".child-airport label input", label.parentElement.parentElement);
                                disabledcheckChilds = dojo.query(".child-airport label.disabled input", label.parentElement.parentElement);
                                checkChilds = _.difference(allcheckChilds, disabledcheckChilds);
                            }
                        }
                    }

                    if (dojo.hasClass(label, "disabled") || dojo.hasClass(label, "manually-disabled")) {
                        return;
                    }

                    // Start the orange pulse on the field.
                    dojo.publish("tui.searchPanel.view.cruise.AirportMultiFieldList.pulse", [airportGuide.widgetController]);

                    airportGuide.updatetoItems(checkbox, checkChilds);
                    airportGuide.cancelBlur();
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
                    if (component !== airportGuide) {
                        airportGuide.closeExpandable();
                    }
                });
                airportGuide.tagElement(airportGuide.expandableDom, "airport selections");
                /*var columns = dojo.query(".col", airportGuide.expandableDom);
                _.each(columns, function (column) {
                	airportGuide.tagElement(column, "airport-selections");
                });*/
                airportGuide.tagElement(dojo.query(".close-list", airportGuide.expandableDom)[0], "airportHide");
                //airportGuide.tagElement(dojo.query(".close-hide", airportGuide.expandableDom)[0], "Airport Hide");
                //airportGuide.tagElement(dojo.query(".empty-airport-model", airportGuide.expandableDom)[0], "Airport Deselect All");
            },

            openExpandable: function () {
                // summary:
                //		Extend default expandable behaviour,
                //		only open once airport guide data is available and returned from the server.
                var airportGuide = this;
                var args = arguments;
                dojo.addClass(airportGuide.domNode, "loading");
                dojo.addClass(airportGuide.domNode, "active");

                airportGuide.removeErrors();
                // publish opening event for widgets that need to close
                airportGuide.publishMessage("searchPanel/searchOpening");

                dojo.publish("tui.searchPanel.view.cruise.AirportMultiFieldList.highlight", [true, airportGuide.widgetController]);
                dojo.when(airportGuide.airportGuideStore.requestData(airportGuide.searchPanelModel.generateQueryObject(), false), function (responseData) {

                    dojo.removeClass(airportGuide.domNode, "loading");
                   // airportGuide.airportList = responseData.data.airports;
                    airportGuide.buildAirportList(responseData.data.airports,responseData.data.airportGroups);
                    airportGuide.pocs = responseData.data.pocs;

                    airportGuide.columnLength = Math.ceil((_.size(airportGuide.airportList)+ airportGuide.totalChildAirports) / airportGuide.columns);
                    airportGuide.inherited(args);

                    // Update the view to conform to the latest datafeed.
                    _.each(responseData.data.airports, function (airport) {
                        airportGuide.updateAirportCheckboxes(airport);
                    });

                  //update parents if all of its enabled children are selected
                    airportGuide.updateParentAirports();

                    // Disable airports selection if saved search has MAX_AIRPORTS_SELECTABLE items selected.
                    airportGuide.disableGuideItems();

                    if (airportGuide.widgetController.searchApi === "getPrice") {
                        dojo.addClass(airportGuide.widgetController.domNode, "open-guide");
                    }
                });
            },

            closeExpandable: function () {
                var airportGuide = this;
                airportGuide.inherited(arguments);
                dojo.removeClass(airportGuide.domNode, "active");
                dojo.publish("tui.searchPanel.view.AirportMultiFieldList.highlight", [false, airportGuide.widgetController]);

                if (airportGuide.widgetController.searchApi === "getPrice") {
                    dojo.removeClass(airportGuide.widgetController.domNode, "open-guide");
                    setTimeout(function(){
                     	 dojo.publish("tui.searchBPanel.view.DropDownSelector", [true]);
                     }, 280);
                }
            },

            showWidget: function (element) {
                var airportGuide = this;
                if(airportGuide.widgetController.searchApi === "getPrice") {
                    // TODO: fix width being sent, this is arbitrary
                    dojo.publish("tui.searchGetPrice.view.GetPriceModal.resize", 675);
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
                    dojo.publish("tui.searchGetPrice.view.GetPriceModal.resize", 299);
                    // ie7 has issues with getPrice guide
                    // TODO: fix this at some point
                    if (has("ie") === 7) dojo.setStyle(element, {"width": 0});
                }
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
                return new AirportModel({
                    name: domAttr.get(airport, "data-airportmodel-name"),
                    id: domAttr.get(airport, "data-airportmodel-id"),
                    group: domAttr.get(airport, "data-airportmodel-groups").split(","),
                    type: domAttr.get(airport, "data-airportmodel-type")
                });
            },

            updatetoItems: function (clickedCheckbox, checkChilds) {
                // summary:
                //		Adds/removes selected airport to/from airport model.
                var airportGuide = this;
                var airportModel;
                          var parentAirport;
                          var clickedCheckboxId = [];
                          var clickedLabel = [];
                          var j=0;
                          if(checkChilds != '') {
                            parentAirport = dojo.query(".parent-airport", clickedCheckbox.parentElement.parentElement.parentElement.parentElement);
                                          _.each(checkChilds, function(object) {
                               clickedCheckboxId[j] = domAttr.get(object, "data-airportmodel-id");
                               clickedLabel[j] = dojo.query(object).parent()[0];
                               j++;
                                });
                          }
                          else {
                            clickedCheckboxId[j] = domAttr.get(clickedCheckbox, "data-airportmodel-id");
                            clickedLabel[j] = dojo.query(clickedCheckbox).parent()[0];
                          }
                // Ignore the click if the label is disabled.
                          if (dojo.hasClass(clickedLabel[0], "disabled")) {
                  return;
                }

                // We look to the parent label tag class to see if selected or not.
                          //var parentAdd = !(dojo.hasClass(clickedLabel, "selected"));
                          var add;
                          if(checkChilds != '') {
                            add = !(dojo.hasClass(parentAirport[0], "selected"));
                          }
                          else {
                            add = !(dojo.hasClass(clickedLabel[j], "selected"));
                          }
                var action = (add) ? "addClass" : "removeClass";

                if (add) {
                             if(checkChilds != '') {
                                dojo.addClass(parentAirport[0], "selected");
                                                           _.each(checkChilds, function(object) {
                                                           if(!dojo.hasClass(object.parentElement, "selected"))
                                                            {
                                   airportModel = airportGuide.getAirportItemAttributes(object);

                  if (airportModel) {
                                       airportGuide.animatePill(parentAirport[0], dojo.query(".where-from", airportGuide.widgetController.domNode)[0], function () {
                     // Execute the action after the animation.
                     airportGuide.searchPanelModel.from.add(airportModel);
                     });
                                   }
                                                            }
                                });
                             }
                             else {
                                   airportModel = airportGuide.getAirportItemAttributes(clickedCheckbox);
                                   if (airportModel) {

                                       airportGuide.animatePill(clickedLabel[0], dojo.query(".where-from", airportGuide.widgetController.domNode)[0], function () {
                      // Execute the action after the animation.
                      airportGuide.searchPanelModel.from.add(airportModel);
                    });
                  }
                             }
                  return;
                }

                // DELETE
                // We are performing a removal from airport guide.
                // if id doesn't exist, we need to delete the airport group.
                          if(checkChilds != '') {
                              dojo.removeClass(parentAirport[0], "selected");
                                          _.each(checkChilds, function(object) {
                              clickedCheckboxId[j] = domAttr.get(object, "data-airportmodel-id");
                               clickedLabel[j] = dojo.query(object).parent()[0];
                               airportModel = airportGuide.searchPanelModel.from.get(clickedCheckboxId[j]);
                if (airportModel) {
                               airportGuide.searchPanelModel.from.remove(clickedCheckboxId[j]);
                            }
                            j++;
                          });

                          }
                          else {
                            airportModel = airportGuide.searchPanelModel.from.get(clickedCheckboxId[j]);
                          }

                if (airportModel) {
                              airportGuide.searchPanelModel.from.remove(clickedCheckboxId[j]);
                }

                var clickedCheckboxChildren = domAttr.get(clickedCheckbox, "data-airportmodel-groups");

                // Get the airport group list.
                if (clickedCheckboxChildren) {
                  clickedCheckboxChildren = clickedCheckboxChildren.split(",");
                              _.forEach(clickedCheckboxChildren, function (id) {
                    if (!id) {
                      return;
                    }

                    // Query from to see if groups are present.
                    var airportGroupModel = airportGuide.searchPanelModel.from.get(id);
                    if (airportGroupModel) {
                      airportGuide.searchPanelModel.from.remove(id);

                      _.each(airportGroupModel.children, function (child) {
                                          if (clickedCheckboxId[j] !== child) {
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
                	// in below query, [1] changed to [0] - Temporary fix - TODO
                	//var input = dojo.query("." + item.id, airportGuide.expandableDom)[1];
                    var input = dojo.query("." + item.id, airportGuide.expandableDom)[0];
                    domAttr.set(input, "checked", add);
                    dojo.query(input).parent()[action]("selected");
                }
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

            updateParentAirports: function () {
                var airportGuide = this;
                _.each(airportGuide.parentAirportsDomNode , function(parentAirportDomNode){
                               var label = parentAirportDomNode;
                               var check = dojo.query("input" ,parentAirportDomNode);
                               var parentUL = parentAirportDomNode.parentElement.parentElement;
                               allChildAirportCount = _.size(dojo.query(".child-airport label", parentUL));
                    disabledChildAirportCount = _.size(dojo.query(".child-airport label.disabled", parentUL));
                    selectedChildAirportCount = _.size(dojo.query(".child-airport label.selected", parentUL));
                    childAirportCount = allChildAirportCount - (disabledChildAirportCount + selectedChildAirportCount);

                    //disabling/enabling the parent airport if all of its child airports can't/can be selected
                               if(childAirportCount > (airportGuide.fromLimit- airportGuide.searchPanelModel.from.selectedSize)){
                                               if (!dojo.hasClass(label, "selected")) {
                            dojo.addClass(check, "manually-disabled");
                            dojo.addClass(label, "manually-disabled");
                        }
                               }
                               else{
                                               if (dojo.hasClass(label, "manually-disabled")) {
                            dojo.removeClass(check, "manually-disabled");
                            dojo.removeClass(label, "manually-disabled");
                        }
                               };

                               //selecting/unselecting the parent airport if all of its child airports are selected/not-selected
                               if(selectedChildAirportCount == (allChildAirportCount-disabledChildAirportCount)){
                          dojo.addClass(label, "selected");
                               }
                               else{
                           dojo.removeClass(label, "selected");
                       };
                });
           },

            clearAll: function () {
                // summary:
                //    Empties model when clicking "unselect all" link.
                //    Removes "selected" class from labels.
                var airportGuide = this;
                airportGuide.cancelBlur();
                _.each(airportGuide.searchPanelModel.from.query(), function (airportModel) {
                    airportGuide.searchPanelModel.from.remove(airportModel.id);
                });

                // Remove all checked status of checkboxes.
                dojo.query("label.selected", airportGuide.expandableDom).removeClass("selected");
                var checkboxes = dojo.query("input[type='checkbox']", airportGuide.expandableDom);
                _.each(checkboxes, function (checkbox) {
                    domAttr.set(checkbox, "checked", false);
                });
            },

            buildAirportList: function(airports,airportGroups){
                var airportGuide = this;
                var anyLondon=[];
                var anyLondonChildren = _.filter(airports, function(airport){
                	airport.group =	airport.group === null ? [] : airport.group;
                	//airport.children =	airport.children ? airport.children : [];
                   return _.contains(airport.group, "LN");
                   });
                if(dojo.config.site !== "falcon"){
                	airportGroups[0].name = "LONDON (ANY)";
	                _.each(anyLondonChildren ,function(airport){
	                   airport.name = airport.name.replace('London ', '');
	                });
	                airportGroups[0].children= anyLondonChildren;
	                anyLondon.push(airportGroups[0]);
                }
                airportGuide.airportList = _.union(anyLondon,_.difference(airports, anyLondonChildren));

                //counting all the child airports
                airportGuide.totalChildAirports = 0;
                _.each(airportGuide.airportList , function(airport){
                   var childLength = airport.children ? airport.children.length : 0;
                   if( childLength > 0){
                      airportGuide.totalChildAirports = airportGuide.totalChildAirports + childLength;
                   }
                });
             } ,

            updatePlaceHolder: function(){
                var airportGuide = this;
                if (airportGuide.searchPanelModel.from.data.length > 1){
                    airportGuide.placeholder.innerHTML = airportGuide.searchPanelModel.from.data.length + " " +airportGuide.searchMessaging.selections;
                }else if(airportGuide.searchPanelModel.from.data.length == 1){
                    airportGuide.placeholder.innerHTML = airportGuide.searchPanelModel.from.data[0].name ;
                }else{
                    airportGuide.placeholder.innerHTML = airportGuide.searchMessaging.fromPlaceholder;
                }
            },

            displayFromToError: function (name, oldError, newError) {
                // displays 'from, to' validation message if validation error occurs
                var airportMultiFieldList = this;
                var action = (newError.emptyFromTo) ? "addClass" : "removeClass";
                dojo[action](airportMultiFieldList.domNode, "error");
                dojo[action](airportMultiFieldList.placeholder, "error");

                airportMultiFieldList.validateErrorMessage(newError.emptyFromTo, {
                    errorMessage: newError.emptyFromTo,
                    errorPopupClass: "error-jumbo",
                    floatWhere: "position-bottom-left",
                    field: "fromTo",
                    key: "emptyFromTo"
                });

            },

            displayRouteError: function (name, oldError, newError) {
                console.log('displayError Messages..displayRouteError..From');
                //TODO: displayError Messages...From
            },

            removeErrors: function(){
                var airportGuide = this;
                var fromToError = dojo.clone(airportGuide.searchPanelModel.searchErrorMessages.get("fromTo"));
                if (fromToError.emptyFromTo) {
                    delete fromToError.emptyFromTo;
                    airportGuide.searchPanelModel.searchErrorMessages.set("fromTo", fromToError);
                }

                // delete no match error & Empty Destination error if present
                airportGuide.searchPanelModel.searchErrorMessages.set("to", {});
            }

        });

        return tui.searchPanel.view.cruise.AirportGuide;
    });
