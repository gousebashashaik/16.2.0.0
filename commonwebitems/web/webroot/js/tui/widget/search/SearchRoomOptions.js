define ("tui/widget/search/SearchRoomOptions", ["dojo",
												"dojo/text!tui/widget/Templates/Search/Room.html", 
												"tui/dtl/Tmpl"], 
												function(dojo, roomTmpl, tmpl){
	
	dojo.declare("tui.widget.search.SearchRoomOptions", null, {
		
		rooms: null,
		
		roomSlotNo: 1,
		
		roomTotal: 1,
		
		rooomMax: 5,
		
		paxMax: 9,
		
		roomConnects: null,
		
		roomfields:["numberOfRooms", "search-children", "totalAdultsInRoom", "childrenAges"],

        //addrooms: null,

		initRooms: function(){
		 	var searchRoomOptions = this;
		 	searchRoomOptions.setDefaultRoomValues();
		 	searchRoomOptions.rooms = dojo.query(".rooms .item", searchRoomOptions.domNode);
		 	searchRoomOptions.roomConnects = {};
           // searchRoomOptions.addrooms = dojo.query(".search-add-room", searchRoomOptions.domNode)[0];
		 	searchRoomOptions.attachAddRoomEventListener();
		},
		
		setDefaultRoomValues: function(){
			var searchRoomOptions = this;
			tui.getFormElementByName("numberOfRooms", searchRoomOptions.domNode, 1);
		},

		setRoom: function(searchCookieData){
			var searchRoomOptions = this;
			if (!searchCookieData) return;
			
			tui.setFormElementByName("childrenAges", searchRoomOptions.domNode, searchCookieData["childrenAges"]);
			
			// add rooms
			var numberOfRooms = parseInt(searchCookieData["numberOfRooms"], 10);
		 	tui.setFormElementByName("numberOfRooms", searchRoomOptions.domNode, numberOfRooms);
		 	searchRoomOptions.addRooms(numberOfRooms - 1, searchCookieData);

			//console.log(savedsearch["search-children"]);
			//console.log(savedsearch["totalAdultsInRoom"]);
		 	// refresh children dropdowns
		 	//var fields = tui.setFormElementByName("search-children", searchRoomOptions.domNode, savedsearch["search-children"]);
		 	//console.log(savedsearch["search-children"])
		 	//searchRoomOptions.refreshRoomCustomDropdown(fields);
		 	
		 	// refresh adults dropdowns
		 	//fields = tui.setFormElementByName("totalAdultsInRoom", searchRoomOptions.domNode, savedsearch["totalAdultsInRoom"]);
		 	//searchRoomOptions.refreshRoomCustomDropdown(fields);

           // fields = tui.setFormElementByName("totalSeniorsInRoom", searchRoomOptions.domNode, savedsearch["totalSeniorsInRoom"]);
           // searchRoomOptions.refreshRoomCustomDropdown(fields);
		},
		
		refreshRoomCustomDropdown: function(fields){
			var searchRoomOptions = this;
			var widgetDom, widget;
			_.forEach(fields, function(field, index){
				widgetDom = dojo.query(field).parents(".custom-dropdown")[0];
				if (widgetDom){
					widget = dijit.byId(widgetDom.id);
					widget.renderList();
				}
			})
		},
		
		saveRoomConfiguration: function(){
			var searchRoomOptions = this;
			var widgets = dijit.registry.findWidgets(searchRoomOptions.domNode);
    		var childPopupCount = 1;
    		var answer = true
    		for(var i = 0; i < widgets.length; i++){
    			if (widgets[i] instanceof tui.widget.search.SearchChildPopup){
    				if (!widgets[i].updateAges(childPopupCount)){
    					answer = false;
    					break;
    				};
    				childPopupCount++;
    			}
    		} 
    		return answer;
		},

        isMoreAdultsThanInfant: function(formData){
            var searchRoomOptions = this;
            var numberOfRooms = parseInt(formData.numberOfRooms);
            var answer = true;
            for (var i = 0; i < numberOfRooms; i++){
                var adults = formData.totalAdultsInRoom[i];
                var senior = formData.totalSeniorsInRoom[i];
                adults = (adults == "") ? 0 : parseInt(adults, 10);
                senior = (senior == "") ? 0 : parseInt(senior, 10);

                var infants = formData.totalInfantsInRoom[i];
                infants = (infants == "") ? 0 : parseInt(infants, 10);

                if (infants > (adults + senior)){
                    var room = dojo.query(".rooms .item .rcol", searchRoomOptions.domNode)[i];
                    dojo.attr(room, 'tabindex', -1);
                    var error = new tui.widget.popup.ErrorPopup({
                        elementRelativeTo: room,
                        errorMessage: "For legal reasons, the number of infants needs to be the same as or less than the number of Adults or Senior Citizens. Please try again.",
                        posOffset: {top: 8, left: 0}
                    }, null)
                    error.open();
                    dojo.addClass(error.popupDomNode, "iscape");
                    room.focus()
                    var roomError = dojo.connect(room, "onblur", function(event){
                        dojo.disconnect(roomError);
                        error.close();
                        error.destroyRecursive();
                    })
                    answer = false;
                    break;
                }
            }

            return answer;
        },

		isOnePersonInRoom: function(formData){
			var searchRoomOptions = this;
			var answer = true;
			var numberOfRooms = parseInt(formData.numberOfRooms);
			var fieldsToCheck = ["totalSeniorsInRoom", "totalAdultsInRoom"];
			for (var i = 0; i < numberOfRooms; i++){
				var totalpassangers = 0;
				_.forEach(fieldsToCheck, function(props){
					totalpassangers = (formData[props][i] === "") ? totalpassangers + 0 :
											totalpassangers + parseInt(formData[props][i], 10);
				})

				if (totalpassangers === 0){
					var room = dojo.query(".rooms .item .rcol", searchRoomOptions.domNode)[i];
					dojo.attr(room, 'tabindex', -1);

					var error = new tui.widget.popup.ErrorPopup({
    					elementRelativeTo: room,
						errorMessage: "Please select at least one Adult in room before proceeding.",
						posOffset: {top: 8, left: 0}
    				}, null)

    				var totalBlurError = dojo.connect(room, "onblur", function(event){
    					dojo.stopEvent(event);
                        resetErrorMsg();
    				})

                    var totalClickError = dojo.connect(room, "onclick", function(event){
                        dojo.stopEvent(event);
                        resetErrorMsg();
                    })

                    function resetErrorMsg(){
                        ///
                        dojo.disconnect(totalBlurError);
                        dojo.disconnect(totalClickError);
                        error.close();
                        error.destroyRecursive();
                    }

                    error.open();
                    dojo.addClass(error.popupDomNode, "iscape");
                    room.focus();

                    answer = false;
					break;
				}	
			}
			return answer;	
		},
		
		isOverPaxMax: function(formData){
			var totalpax = 0;
			var passangers = formData.totalChildrenInRoom.concat(formData.totalInfantsInRoom,
                                                                 formData.totalAdultsInRoom,
                                                                 formData.totalSeniorsInRoom);
			_.forEach(passangers, function(passanger, index){
				totalpax = (passanger === "") ? totalpax + 0 : totalpax + parseInt(passanger, 10);
			})
			return (totalpax > 9)
		},
		
		attachAddRoomEventListener: function(){
    		var searchRoomOptions = this;
    		var addroomslink = dojo.query(".search-add-room", searchRoomOptions.domNode)[0];
			dojo.connect(dojo.query("a", addroomslink)[0], "onclick", function(event){
				dojo.stopEvent(event);
                searchRoomOptions.usecookie = false;
				searchRoomOptions.addRoom(addroomslink);
			})		
    	},
    	
    	addRooms: function(numberOfRooms, searchCookieData){
    		var searchRoomOptions = this;
            var addroomslink = dojo.query(".search-add-room", searchRoomOptions.domNode)[0];
            searchRoomOptions.usecookie = true;
            var options;
    		for (var i = 0; i < numberOfRooms; i++){
    			searchRoomOptions.addRoom(addroomslink, searchCookieData);
    		}
    	},
    	
    	addRoom: function(roomLink, searchCookieData){
    		var searchRoomOptions = this;
    		if (searchRoomOptions.roomTotal !== searchRoomOptions.rooomMax){
				// find next rooms slot, and adds room item. 
				var newroom = searchRoomOptions.findNextRoomSlotNo();
			
				
				var html = tmpl.createTmpl(searchRoomOptions, roomTmpl);
				
				var addRoom = null;
				if (newroom == -1)  {
					var lastitems = searchRoomOptions.rooms[searchRoomOptions.rooms.length - 1];
					addRoom = dojo.place(html, lastitems, "after");
					searchRoomOptions.rooms.push(addRoom);
				} else {
					var lastitems = searchRoomOptions.rooms[newroom - 1];
					addRoom = dojo.place(html, lastitems, "after");
					searchRoomOptions.rooms[newroom] = addRoom;
				}
				
				if (searchCookieData){
					tui.setFormElementByName("search-children", searchRoomOptions.domNode, searchCookieData["search-children"]);
					tui.setFormElementByName("totalAdultsInRoom", searchRoomOptions.domNode, searchCookieData["totalAdultsInRoom"]);
		 			tui.setFormElementByName("totalSeniorsInRoom", searchRoomOptions.domNode, searchCookieData["totalSeniorsInRoom"]);
		 		}
				
				// parses from addRoom dom, to create any dojo widgets needed.
				dojo.parser.parse(addRoom);
				searchRoomOptions.numberRooms();
				searchRoomOptions.addEventDeleteRooms(addRoom);
				tui.getFormElementByName("numberOfRooms", searchRoomOptions.domNode).value = searchRoomOptions.roomTotal;
			}
            if (searchRoomOptions.roomTotal === searchRoomOptions.rooomMax){
                searchRoomOptions.hideWidget(roomLink);
            }
    	},
    	
    	findNextRoomSlotNo: function(){
    		var searchRoomOptions = this;
    		var i = _.indexOf(searchRoomOptions.rooms, null);
    		searchRoomOptions.roomSlotNo = (i === -1) ? searchRoomOptions.rooms.length + 1 : i + 1;
    		var filterRooms = searchRoomOptions.rooms.filter(function(item){
    			return (item != null)
    		})
    		searchRoomOptions.roomTotal = filterRooms.length + 1
    		return i;
    	},
    	
    	numberRooms: function(){
    		var searchRoomOptions = this;
    		var roomnames = dojo.query(".rooms .item .rcol", searchRoomOptions.domNode);
    		_.forEach(roomnames, function(roomname, index){
    			dojo.html.set(roomname, ["Room", index + 1].join(" "));
    		})
    	},
    	
    	addEventDeleteRooms: function(roomDom){
    		// summary:
			//		Method which adds an event function for removing rooms. 
			//		This event function attached to all dom element with a class of
			//		".remove" within the given roomDom
    		var searchRoomOptions = this;
    		var deleteRoom = dojo.query(".remove", roomDom)[0];
    		
    		// attach remove event handler
    		var handle = searchRoomOptions.connect(deleteRoom, "onclick", function(event){
                dojo.stopEvent(event);
              //  dojo.publish("tui/widget/search/deleteRoom/close");
                deleteRoom.focus();
    			searchRoomOptions.disconnect(handle);
    			var i = _.indexOf(searchRoomOptions.rooms, roomDom);
    			if (i != -1){
    				searchRoomOptions.rooms[i] = null;
    				searchRoomOptions.roomTotal--;
    				tui.getFormElementByName("numberOfRooms", searchRoomOptions.domNode).value = searchRoomOptions.roomTotal;
    			}
    			var widgets = dijit.registry.findWidgets(roomDom)
    			_.forEach(widgets, function(widget){
    				if (widget instanceof tui.widget.search.SearchChildPopup){
                        widget.close();
    					//widget.deleteCustomSelectWidget();
    					widget.clearAges();
    				}
    				widget.destroyRecursive();
    			})
    			dojo.destroy(roomDom);
    			searchRoomOptions.numberRooms();

                if (searchRoomOptions.roomTotal < searchRoomOptions.rooomMax){
                    var addroomslink = dojo.query(".search-add-room", searchRoomOptions.domNode)[0];
                    searchRoomOptions.showWidget(addroomslink);
                }
    		})
    	}
	})
	
	return tui.widget.search.SearchRoomOptions;
})