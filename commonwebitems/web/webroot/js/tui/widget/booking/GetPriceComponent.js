define ("tui/widget/booking/GetPriceComponent", ["dojo",
											  	 "dojo/text!tui/widget/Templates/Search/Room.html",
											  	 "dojo/cookie",
                                                "dojo/query",
                                                "dojo/has",
											  	 "tui/validate/check",
											  	 "dojo/topic",
                                                "tui/widget/search/CookieSearchSave",
											 	 "tui/widget/search/SearchStateful",
											 	 "tui/widget/search/SearchRoomOptions",
											  	 "tui/widget/_TuiBaseWidget", 
											  	 "dojo/NodeList-traverse",
											    "dojox/validate/check",
											 	 "dojox/dtl", 
							    			  "dojox/dtl/Context", 
							    			  "dojox/dtl/tag/logic",
							    			  "dijit/registry",
							    			  "tui/dtl/Tmpl",
							    			  "dojo/html",
							    			  "dojo/dom-form"], function(dojo, roomTmpl, cookie, query, has){

	dojo.declare("tui.widget.booking.GetPriceComponent", [tui.widget._TuiBaseWidget,
                                                          tui.widget.search.CookieSearchSave,
														  tui.widget.search.SearchRoomOptions], {

		cookieName: "gpc/ss",
				
		postCreate: function() {
			var getPriceComponent = this;
			dojo.addClass(getPriceComponent.domNode, "initialising");

            //TODO: add to jsp when iscape release is next available.
            tui.setFormElementByName("searchPageName", getPriceComponent.domNode, "accomSearch");

			getPriceComponent.inherited(arguments);
			getPriceComponent.attachEventListener();
			getPriceComponent.initRooms();

			var savedsearch = getPriceComponent.getSaveFormData(getPriceComponent.cookieName);
            savedsearch = dojo.fromJson(savedsearch);
			if (savedsearch){
				for(searchprop in savedsearch){				
					var index = _.indexOf(getPriceComponent.roomfields, searchprop);
					if (index == -1){
                        if(searchprop === 'datewhen') {
                            savedsearch['datewhen'] = getPriceComponent.checkDate(savedsearch['datewhen']) ? savedsearch['datewhen'] : getPriceComponent.getTomorrow();
                        }
						tui.setFormElementByName(searchprop, getPriceComponent.domNode, savedsearch[searchprop]);
					}
				}
			}

            getPriceComponent.subscribe("tui/lazyload", function(){
                var savedsearch = getPriceComponent.getSaveFormData(getPriceComponent.cookieName);
                savedsearch = dojo.fromJson(savedsearch);

                if (savedsearch){
                    // Reset room from cookie data
                    getPriceComponent.setRoom(savedsearch);
                    var datePickerDom = tui.getFormElementByName("datewhen");
                    var datePicker = dijit.byId(datePickerDom.id);
                    dojo.publish("tui/widget/GetPriceDatePicker/onSelectedDate", [datePicker.selectedDate]);
                }
                dojo.removeClass(getPriceComponent.domNode, "initialising");
            })
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
            var getPriceComponent = this;

            dojo.connect(getPriceComponent.domNode, "onsubmit", function(event){
                var form = this;
                dojo.stopEvent(event);
                if(!getPriceComponent.saveRoomConfiguration()){
                    return;
                }

                var formData = dojo.formToJson(form);

                // check we only have 9 passangers in total.
                var formJson = dojo.fromJson(formData);

                if (getPriceComponent.isOverPaxMax(formJson)){
                    var element = query(".rooms", getPriceComponent.domNode)[0];
                    dojo.attr(element, 'tabindex', -1);
                    var msg = dojo.attr(element, "data-error-paxsize");
                    var error = new tui.widget.popup.ErrorPopup({
                        elementRelativeTo: element,
                        errorMessage: msg,
                        posOffset: {top: -13, left: 0}
                    }, null)
                    error.open();
                    dojo.addClass(error.popupDomNode, "iscape");
                    element.focus();
                    var paxError = dojo.connect(element, "onblur", function(event){
                        dojo.disconnect(paxError);
                        error.close();
                        error.destroyRecursive();
                    })
                    return;
                }

                // check at least one person is in room,
                if (!getPriceComponent.isOnePersonInRoom(formJson)) return;

                // check if more adults than infant in each room.
                if(!getPriceComponent.isMoreAdultsThanInfant(formJson)) return;

                getPriceComponent.saveFormData(formJson);
                
                setTimeout(function(){
                	if (has("agent-ios")){
                		window.onunload = function(){} 
                	}
                	form.submit();	
                }, 3000)
                
            })
    	}
	})
	
	return tui.widget.booking.GetPriceComponent;
})