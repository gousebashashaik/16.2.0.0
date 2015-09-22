define ("tui/widget/search/SearchComponent", ["dojo",
											  "tui/validate/check",
                                              "dojo/has",
                                              "dojo/cookie",
											  "dojo/topic",
											  "tui/widget/search/CookieSearchSave",
											  "tui/widget/_TuiBaseWidget", 
											  "dojo/NodeList-traverse",
											  "dojox/validate/check",
											  "dojox/dtl", 
							    			  "dojox/dtl/Context", 
							    			  "dojox/dtl/tag/logic",
							    			  "dijit/registry",
							    			  "tui/dtl/Tmpl",
							    			  "dojo/html",
							    			  "tui/widget/search/SearchRoomOptions",
							    			  "dojo/dom-form"], function(dojo, validate, has){
	// summary:
	// 		SearchComponent class manages all the related compoments to search.
	//		This class will manage all the preloading of form fields from either local store or cookie data.
	//		Manage form validation, on form submit, and adding and removing rooms.	
	dojo.declare("tui.widget.search.SearchComponent", [tui.widget._TuiBaseWidget, 
													   tui.widget.search.CookieSearchSave,
													   tui.widget.search.SearchRoomOptions], {
		
		cookieName: "sc/ss",
		postMixInProperties: function(){
            var searchComponent = this;
            searchComponent.inherited(arguments);
            if (has("agent-ios")){
                window.onpageshow = function(evt) {
                    if (evt.persisted) {
                      document.body.style.display = "none";
                      location.reload();
                    }
                };
            }
        },
		postCreate: function() {
			var searchComponent = this;
			dojo.addClass(searchComponent.domNode, "initialising");
			//searchComponent.inherited(arguments);
			searchComponent.attachEventListener();
			searchComponent.initRooms();

			var savedsearch = searchComponent.getSaveFormData(searchComponent.cookieName);
			savedsearch = dojo.fromJson(savedsearch);
			if (savedsearch){
				for(searchprop in savedsearch){
                    if(searchprop === 'datewhen') {
                        savedsearch['datewhen'] = searchComponent.checkDate(savedsearch['datewhen']) ? savedsearch['datewhen'] : searchComponent.getTomorrow();
                    }
					tui.setFormElementByName(searchprop, searchComponent.domNode, savedsearch[searchprop]);
				}
			} else {
				tui.setFormElementByName("search-children", searchComponent.domNode, 0);
				tui.setFormElementByName("totalAdultsInRoom", searchComponent.domNode, 2);
		 		tui.setFormElementByName("totalSeniorsInRoom", searchComponent.domNode, 0);
			}

            searchComponent.subscribe("tui/lazyload", function(){
                var savedsearch = searchComponent.getSaveFormData(searchComponent.cookieName);
                savedsearch = dojo.fromJson(savedsearch);
                if (savedsearch){
                    // Reset room from cookie data
                    searchComponent.setRoom(savedsearch);
                    var datePicker = dijit.byId(tui.getFormElementByName("datewhen").id);
                    dojo.publish("tui/widget/SearchDatePicker/onSelectedDate", [datePicker.selectedDate]);
                }
               dojo.removeClass(searchComponent.domNode, "initialising");
               dojo.addClass(searchComponent.domNode, "loaded");

              // When all components are loaded, we allow form submission.
              dojo.removeAttr(dojo.query('input.button', searchComponent.popupDomNode)[0], "disabled");
            });
    	},

        getTomorrow: function (){
            var today = new Date();
            var tomorrow = new Date(today.getFullYear(), today.getMonth(), today.getDate() + 1);
            return dojo.date.locale.format(tomorrow, {
                datePattern: 'd MMMM yyyy',
                selector: "date"
            });
        },

        checkDate: function(searchDate){
            var now = new Date(),
                savedDate = dojo.date.locale.parse(searchDate, {
                    datePattern: 'd MMMM yyyy',
                    selector: "date"
                });
            now = new Date(now.getFullYear(), now.getMonth(), now.getDate());
            return now.valueOf() < savedDate.valueOf();
        },
    	
    	attachEventListener: function(){
    		// summary:
			//		Adds event listenters to all needed DOM events in the saerch panel.
    		var searchComponent = this;
			searchComponent.addFormEventlistener();
    	},
    	
    	addFormEventlistener: function(){
    		// summary:
			//		Adds an onSubmit event listenters to form, which validates data being submitted.
    		var searchComponent = this;
    		var profile = { required: ["searchfrom", "searchto"] }
    		dojo.connect(searchComponent.domNode, "onsubmit", function(event){
    			var form = this;
    			dojo.stopEvent(event);
    			
    			var formTimer = setTimeout(function(){
    				clearTimeout(formTimer);
    				var validateForm = validate.checkForm(form, profile);
    				if (!validateForm.isSuccessful()){
    					var requiredfields = validateForm.getMissing();
    					_.forEach(requiredfields, function(errorId){
    						var element = tui.getFormElementByName(errorId, searchComponent.domNode);
    						var msg = dojo.attr(element, "data-error-required");
                            var path = "\\search\\templates\\mainsearch\\";
                            var guide = (errorId === "searchfrom") ? [path, "EmptyAirportField.html"] : [path, "EmptyDestinationField.html"];
    						var errorPopup = tui.showDefaultSearchErrorPopup(element, msg, guide.join(""));
                            dojo.query(".guides", errorPopup.popupDomNode).onclick(function(event){
                                _.forEach(profile.required, function(item){
                                    tui.removeErrorPopup(dojo.byId(item));
                                })
                                if (errorId == "searchfrom"){
                                    dojo.publish("tui/widget/search/AirportAutocomplete/airportguide")
                                } else {
                                    dojo.publish("tui/widget/search/DestinationAutocomplete/destinationguide");
                                }
                                tui.removeErrorPopup(item);
                            })
    					})
    				} else {

    					// check room configuration are valid, and add save to form.
    					if(!searchComponent.saveRoomConfiguration()){
    						return;
    					}
    					
    					var formData = dojo.formToJson(form);
    					// check we only have 9 passangers in total.
    					var formJson = dojo.fromJson(formData);
    					
    					if (searchComponent.isOverPaxMax(formJson)){
    						var element = dojo.query(".rooms", searchComponent.domNode)[0];
    						dojo.attr(element, 'tabindex', -1);
    						var msg = dojo.attr(element, "data-error-paxsize");
    						var error = new tui.widget.popup.ErrorPopup({
    							elementRelativeTo: element,
								errorMessage: msg,
								posOffset: {top: -13, left: 0}
    						}, null)
    						error.open();
                            dojo.addClass(error.popupDomNode, "iscape");
    						element.focus()
    						var paxError = dojo.connect(element, "onblur", function(event){
    							dojo.disconnect(paxError);
    							error.close();
    							error.destroyRecursive();
    						})
    						return;
    					} 
    					
    					// check at least one person is in room,
    					if (!searchComponent.isOnePersonInRoom(formJson)) return;

                        // check if more adults than infant in each room.
                        if(!searchComponent.isMoreAdultsThanInfant(formJson)) return;
                        
                        searchComponent.saveFormData(formJson);

                      	setTimeout(function(){
                			form.submit();	
                		}, 2000)
    				}
    			}, 150);
			})
    	}
	})
	
	return tui.widget.search.SearchComponent;
})