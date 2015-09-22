define('tui/widget/booking/changeroomallocation/view/RoomOptionsButtonToggler', [
  'dojo',
  'dojo/dom',
  'dojo/query',
  'dojo/dom-class',
  'dojo/dom-construct',
  'dojo/topic',
    'dojo/dom-attr',
    'dojo/dom-geometry',
  'tui/widget/booking/changeroomallocation/modal/RoomModel',
  "tui/widget/booking/constants/BookflowUrl",
  "tui/widget/booking/bookflowMsgs/nls/Bookflowi18nable",
  'dojo/window',
    'tui/mvc/Klass',
   'tui/widget/mixins/Templatable',
   'tui/widget/booking/Expandable'
], function(dojo, dom, query, domClass, domConstruct, topic, domAttr, domGeom, roomModel, BookflowUrl, win) {

	dojo.declare('tui.widget.booking.changeroomallocation.view.RoomOptionsButtonToggler', [tui.widget._TuiBaseWidget,tui.widget.booking.Expandable,tui.widget.booking.bookflowMsgs.nls.Bookflowi18nable], {

		changeRoom: null,
		roomRefNum: 3,
                hasMultipleRooms: false,
		postCreate: function() {
			var widget = this;
			var widgetDom = widget.domNode;
			widget.changeRoom ={};
			widget.jsonData = roomModel.alternateRooms ;
			widget.packageTypeRef = roomModel.packageTypeRef;
			//To have transition effect.
			widget.forTransitionEffect();
            dojo.subscribe("tui/widget/booking/displayContent", function(wipe) {
                widget.displayContent(wipe);
			});
            widget.hasMultipleRooms = widget.jsonData.roomOptionsViewData.length > 1 ? true : false;
			widget.displayContent();
            widget.updateRoomDisplay();
			widget.attachShowHideEvents();
            widget.initBookflowMessaging();
            widget.sectionHeadingFunc(widget.jsonData.alternateBoardBasis)

			topic.subscribe("update/sectionHeading", function(response){
	    	       widget.sectionHeadingFunc(response.alternateBoardBasis)
	    	    });

            widget.urlString= this.bookflowMessaging[dojoConfig.site].alternativeRoomurl;
			//Logic to toggle between selected buttons.
			var items = query('.item', widgetDom);
			_.each(items, function(item) {
				var slectedsection = query('.item-content',item)[0];
				var buttons = query('.seatingBtn', item);
				_.each(buttons, function(button) {
					dojo.connect(button, 'click', function(e) {
                        var showAllAlternativeButton = dojo.query(".btnRight", widgetDom)[0];
                        var hideAll = dojo.query(".hideAll", showAllAlternativeButton)[0];
						_.each(items, function(item) {
							var buttons = query('.seatingBtn',item);
							var slectedSection = query('.item-content',item)[0];
							_.each(buttons, function(button) {
								domClass.remove(button,'select-seat');
							});
							domClass.remove(item,'selectedSection');
							domClass.remove(slectedSection,'selectedSection');
						});
                        roomModel.scrollPosition = domGeom.docScroll();
                        roomModel.allRoomsVisible = !(domClass.contains(hideAll, "displayNone"));
						domClass.add(button, 'select-seat');
						domClass.add(item, 'selectedSection');
						domClass.add(slectedsection,'selectedSection');

						var roomsId = dojo.query(".adults-logo",widgetDom)[0];
						var roomId = dojo.attr(roomsId, "id");
						var alternativeRoomid= dojo.attr(button, "id");
						widget.generateRequest(roomId,alternativeRoomid);
					});
				});
			});

			if(widget.bookflowMessaging[dojoConfig.site].brandMapping[widget.packageTypeRef].roomToggleAttachFlag){

				widget.itemDisplayFunc();
			}


		},
		itemDisplayFunc: function(){
			var widget = this;
			var widgetDom = widget.domNode;
			var allItems = dojo.query(".item",widgetDom);

			_.each(allItems,function(item,i){
				var showItem = false;
				curVal = dojo.query(".curVal", item)[0];
                PPPerNight = dojo.query(".PPPerNight", item)[0];
                thresholdAvailability = dojo.query(".limitedAvailability", item)[0];
                totalLabel = dojo.query(".total-label", item)[0];
                textContainer = dojo.query(".text_container", item)[0];



				if(domClass.contains(item, "selected")){
						 showItem = true;
					 }



				 if(i+1 > widget.roomRefNum && !showItem){
						domClass.remove(item,"open");

						domClass.remove(curVal, "displayNone");
                        domClass.remove(PPPerNight, "displayNone");

                        domClass.remove(totalLabel, "displayNone");
						domClass.remove(textContainer,"displayNone");
                        if (thresholdAvailability != null) {
                            domClass.remove(thresholdAvailability, "displayNone");
                        }
						showItem = false;
					}


			})

		},

		attachShowHideEvents: function(){
			var widget = this;
			var widgetDom= widget.domNode;
			var showAllAlternativeButton = dojo.query(".btnRight",widgetDom)[0];
			var showAll = dojo.query(".showAll",showAllAlternativeButton)[0];
			var HideAll = dojo.query(".hideAll",showAllAlternativeButton)[0];
            var roomTypes = dojo.query(".item", widgetDom);
            domClass.add(showAll, "displayNone");
			domClass.add(HideAll,"displayNone");
			//Logic to display the show all button and show less
            if (roomTypes.length > widget.maxRoomsToDisplay) {
                domClass.remove(showAll, "displayNone");
                domClass.add(HideAll, "displayNone");

                if ((widget.hasMultipleRooms || roomModel.roomOrder.length > 1) && roomModel.allRoomsVisible) {
				domClass.add(showAll,"displayNone");
                    domClass.remove(HideAll, "displayNone");
			}
			}
			dojo.connect(showAll, 'click', function(e){
				domClass.remove(HideAll,"displayNone");
				var roomTypes = dojo.query(".item",widgetDom);
				_.each(roomTypes,function(roomtype){

					domClass.remove(roomtype,"displayNone");

					domClass.add(showAll,"displayNone");
				})
			})
			dojo.connect(HideAll, 'click', function(e){
				domClass.remove(showAll,"displayNone");
				domClass.add(HideAll,"displayNone");
				var roomTypes = dojo.query(".alternativeRoom",widgetDom);
				_.each(roomTypes,function(roomtype){
					domClass.add(roomtype,"displayNone");
				})
			})

		},
		forTransitionEffect: function(){
			var widget= this;
			widget.transitionOptions.domNode = widget.domNode;
			widget.transition = widget.addTransition();
			console.log(widget.jsonData);
			// Tagging particular element.
			if(widget.autoTag) {
				widget.tagElements(dojo.query(widget.targetSelector, widget.domNode), 'toggle');
			}
		},

        refresh: function(field, response) {},

		generateRequest : function(roomId,alternativeRoomid) {
			var field = "Rooms";
			var widget= this;
			// Getting the values to be sent as ajax parameters.
			var sellingCode= widget.jsonData.roomOptionsViewData[roomId].listOfRoomViewData[alternativeRoomid].sellingCode;
			var roomCode = widget.jsonData.roomOptionsViewData[roomId].listOfRoomViewData[alternativeRoomid].roomCode;
			var roomIndex = widget.jsonData.roomOptionsViewData[roomId].roomIndex;
            widget.changeRoom = {
                "roomCode": roomCode,
                "sellingCode": sellingCode,
                "roomIndex": roomIndex
            }
            var request = {
                roomSelectionCriteria: dojo.toJson(widget.changeRoom)
            };
			var url = BookflowUrl[widget.urlString] ;
			var controller= dijit.registry.byId("controllerWidget").generateRequest(field,url,request);
			win.scrollIntoView("room_"+roomIndex+"");
			return request;
		},
        displayContent: function(wipe) {
			var widget = this;
			var widgetDom = widget.domNode;
			var items = dojo.query(".open",widgetDom);
            var allItems = dojo.query(".item", widgetDom),
                header = null,
                curVal = null,
                PPPerNight = null,
                thresholdAvailability = null,
			 	totalLabel = null,
			 	textContainer =  null;
          //for displaying the content based on condition(Depends on the transition state).
			if(wipe){
                if (wipe.wipeMode && wipe.target) {
                    header = wipe.target;
                    curVal = dojo.query(".curVal", wipe.target)[0];
                    PPPerNight = dojo.query(".PPPerNight", wipe.target)[0];
                    thresholdAvailability = dojo.query(".limitedAvailability", wipe.target)[0];
                    totalLabel = dojo.query(".total-label", wipe.target)[0];
                    textContainer = dojo.query(".text_container", wipe.target)[0];
                    switch (wipe.wipeMode.toLowerCase()) {
                        case "wipeout":
                            domClass.remove(curVal, "displayNone");
                            domClass.remove(PPPerNight, "displayNone");
                            domClass.remove(totalLabel, "displayNone");
                            if(!query(".selectedSection",wipe.target.parentElement).length){
								domClass.remove(textContainer,"displayNone");
							}
                            if (thresholdAvailability != null) {
                                domClass.remove(thresholdAvailability, "displayNone");
                            }
                            break;
                        case "wipein":
                            domClass.add(curVal, "displayNone");
                            domClass.add(PPPerNight, "displayNone");
                            domClass.add(totalLabel, "displayNone");
                            domClass.add(textContainer,"displayNone");
                            if (thresholdAvailability != null) {
                                domClass.add(thresholdAvailability, "displayNone");
                            }
                            break;
                    }
                }

            } else {
			_.each(allItems,function(item){
                    header = dojo.query(".item-toggle", item)[0];
                    curVal = dojo.query(".curVal", item)[0];
                    PPPerNight = dojo.query(".PPPerNight", item)[0];
                    thresholdAvailability = dojo.query(".limitedAvailability", item)[0];
                    totalLabel = dojo.query(".total-label", item)[0];
                    textContainer = dojo.query(".text_container",item)[0];
				domClass.remove(curVal,"displayNone");
				domClass.remove(PPPerNight,"displayNone");
				domClass.remove(totalLabel,"displayNone");
                    if(!query(".selectedSection",item).length){
						domClass.remove(textContainer,"displayNone");
					}
				if(thresholdAvailability != null){
					domClass.remove(thresholdAvailability,"displayNone");
				}
                });
			_.each(items,function(item){
                    header = dojo.query(".item-toggle", item)[0];
                    curVal = dojo.query(".curVal", item)[0];
                    PPPerNight = dojo.query(".PPPerNight", item)[0];
                    thresholdAvailability = dojo.query(".limitedAvailability", item)[0];
                    totalLabel = dojo.query(".total-label", item)[0];
                    textContainer = dojo.query(".text_container",item)[0];
				domClass.add(curVal,"displayNone");
				domClass.add(PPPerNight,"displayNone");
				domClass.add(totalLabel,"displayNone");
                    domClass.add(textContainer,"displayNone");
				if(thresholdAvailability != null){
					domClass.add(thresholdAvailability,"displayNone");
				}
                });
            }
        },

		sectionHeadingFunc: function(listOfBoardBasis){

		 var sectionHeading = dom.byId("sectionheading");

		 if(!_.isUndefined(this.jsonData.alternateBoardBasis) && !_.isEmpty(this.jsonData.alternateBoardBasis) && this.jsonData.enableBoardBasisComponent){
			domClass.remove(sectionHeading,"displayNone");
			var selectedBB = _.find(listOfBoardBasis, function(item,index){ if(item.selected) return item });
			sectionHeading.innerHTML =  _.isUndefined(this.jsonData.roomOptionsStaticContentViewData.roomContentMap.Room_SelectRooms_CompHeader)? this.bookflowMessaging[dojoConfig.site].roomStaticText: this.jsonData.roomOptionsStaticContentViewData.roomContentMap.Room_SelectRooms_CompHeader+"s for "+selectedBB.boardBasisName;

		 }

		},

        updateRoomDisplay: function() {
            var widget = this;
            var roomIndex = 0;
            var room = null;
            var widgetDom = widget.domNode;
            var roomTypeLength = 0;
            var roomTypes = null;
            roomTypes = dojo.query(".item", widgetDom);
            roomTypeLength = roomTypes.length;
            if (roomTypeLength > widget.maxRoomsToDisplay) {
                for (roomIndex = widget.maxRoomsToDisplay; roomIndex < roomTypeLength; roomIndex++) {
                    room = roomTypes[roomIndex];
                    domClass.add(room, "alternativeRoom displayNone");
                    if (roomModel.allRoomsVisible) {
                        domClass.remove(room, "displayNone");
                    }
                    if (dojo.query(".selectedSection", room).length) {
                        domClass.remove(room, "displayNone");
                        domClass.remove(room, "alternativeRoom");
                        room = roomTypes[widget.maxRoomsToDisplay - 1];
                        domClass.add(room, "alternativeRoom displayNone");
                        if (roomModel.allRoomsVisible) {
                            domClass.remove(room, "displayNone");
                        }
                    }
                }

            }
		}
	});
	return tui.widget.booking.changeroomallocation.view.RoomOptionsButtonToggler;
});