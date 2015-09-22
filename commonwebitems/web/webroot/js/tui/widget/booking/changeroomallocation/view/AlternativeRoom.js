define("tui/widget/booking/changeroomallocation/view/AlternativeRoom", [
    "dojo",
    "dojo/query",
    "dojo/dom-class",
    "dojo/dom",
    "dojo/dom-construct",
    "dojo/topic",
    "dojo/text!tui/widget/booking/changeroomallocation/view/Templates/ChangeRoomType.html",
    "tui/widget/booking/changeroomallocation/modal/RoomModel",
    "tui/widget/booking/bookflowMsgs/nls/Bookflowi18nable",
    "tui/widget/mixins/Templatable",
    "tui/widget/expand/Expandable",
    "tui/widget/_TuiBaseWidget",
    "tui/widget/media/MediaPopupSwipe",
    "tui/widget/RoomPlansOverlay"
], function(dojo, query, domClass, dom, domConstruct, topic, changeRoomType, roomModel, Templatable, Expandable, _TuiBaseWidget, mediaPopup) {

    dojo.declare('tui.widget.booking.changeroomallocation.view.AlternativeRoom', [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable,tui.widget.booking.bookflowMsgs.nls.Bookflowi18nable], {

        	tmpl:changeRoomType,

        hasMultipleRooms: false,


            postCreate: function() {

                var widget = this;
                var widgetDom = widget.domNode;
                var controller=null;
            var isSameSession = false;
            var roomOrderFromStorage = null;

                //Registering the view to the controller
                widget.controller= dijit.registry.byId("controllerWidget");
                var testVar= widget.controller.registerView(widget);
                widget.jsonData.allRoomsVisibile = false;

                widget.initBookflowMessaging();
                widget.roomTile= this.bookflowMessaging[dojoConfig.site].roomTitle;

                roomModel.alternateRooms = widget.jsonData;
                roomModel.packageTypeRef = widget.jsonData.packageType;
            widget.hasMultipleRooms = widget.jsonData.roomOptionsViewData.length > 1 ? true : false;

            if(!widget.hasMultipleRooms){
            	_.each(roomModel.alternateRooms.roomOptionsViewData[0].listOfRoomViewData, function(room){
            		roomModel.roomOrder.push(room.roomCode);
            	});
            	if(localStorage){
            		roomOrderFromStorage = localStorage.getItem("roomOrder");
            	}
                if(roomOrderFromStorage){
                	roomOrderFromStorage = roomOrderFromStorage.split("$|$");
                	isSameSession = widget.compareReorderArray(roomModel.roomOrder, roomOrderFromStorage);
                	if(isSameSession){
                		roomModel.roomOrder = [];
    	            	_.each(roomOrderFromStorage, function(roomCode){
    	            		roomModel.roomOrder.push(roomCode);
    	            	});

                	}
                	 localStorage.setItem("roomOrder", roomModel.roomOrder.join("$|$"));
                }

                if(roomModel.roomOrder.length){
                	widget.reorder();
                }
            }
                var nodeAttach = dom.byId("alternative-room-container");
            widget.generateGalleryImages();
            widget.dojoConfig = dojoConfig; // added dojoConfig within the context of template to be rendered
                var html = widget.renderTmpl(widget.tmpl, widget);
                domConstruct.place(html, nodeAttach, 'last');
                var parseNode = dom.byId("temp");
                dojo.parser.parse(parseNode);
                this.inherited(arguments);
                widget.tagElements(dojo.query('.showAll'),"showAvailableRooms");
                var button=dojo.query('.jumbo');
                for(var index=0;index<button.length;index++){
                	var i = parseInt(button[index].id) + 1;
                	widget.tagElement(button[index],"roomSelect"+i);
                }
            },


        // Manipulates jsonData object so that MediaPopUp library can consume galleryImages
        generateGalleryImages: function(){
        	var widget = this;
        	if(widget.jsonData.roomOptionsViewData && widget.jsonData.roomOptionsViewData.length) {
        		_.each(widget.jsonData.roomOptionsViewData, function(roomView){
        			if(roomView.listOfRoomViewData && roomView.listOfRoomViewData.length){
        				_.each(roomView.listOfRoomViewData, function(roomViewData){
        					if(roomViewData && roomViewData.roomImage){
        						roomViewData.galleryImages = roomViewData.roomImage.slice(0);
        						_.each(roomViewData.galleryImages, function(galleryImage){
        							galleryImage.code = galleryImage.code.replace(/(.+)?_.*/gi, "$1");
        							galleryImage.mainSrc = galleryImage.url;
        						});
        						roomViewData.stringifiedView = JSON.stringify(roomViewData);
        					}
        				});
        			}
        		});
        	}
        },

        refresh: function(field, response) {
            var widget = this;
            //Re-painting the Component
            if (field == "changeRoom" || field == "Rooms" || field == "boardBasis") {
            var widgetDom = widget.domNode;
            widget.jsonData = response;
            roomModel.alternateRooms = response;
            var widget = this;
            var widgetDom = widget.domNode;
            widget.jsonData = response;
	                widget.hasMultipleRooms = widget.jsonData.roomOptionsViewData.length > 1 ? true : false;
	                if(!widget.hasMultipleRooms){ // Check whether we have multiple room options
	                	if(roomModel.roomOrder.length){ // Reorder the response
	                		widget.reorder();
	                	} else {
	                      	_.each(roomModel.alternateRooms.roomOptionsViewData[0].listOfRoomViewData, function(room){
		                		roomModel.roomOrder.push(room.roomCode);  // Add rooms in the order in which they are displayed
		                	});
	                	}
	                	 widget.jsonData.allRoomsVisibile = roomModel.allRoomsVisible;
	                }
            var parseNode = dom.byId("temp");
            dojo.destroy(parseNode);
            var nodeAttach = dom.byId("alternative-room-container");
	                widget.generateGalleryImages();
            var html = widget.renderTmpl(widget.tmpl, widget);
            domConstruct.place(html, widget.domNode, 'only');

            dojo.parser.parse(widget.domNode);
	                if(!widget.hasMultipleRooms && field != "boardBasis"){
	                	window.scrollTo(0,roomModel.scrollPosition.y); // Scroll to the selected room position
	                }
            }




        },

        // Reordering response data
        reorder: function(){
        	var widget = this,
        		roomUnorderedList = widget.jsonData.roomOptionsViewData[0].listOfRoomViewData;
        	roomModel.alternateRooms.roomOptionsViewData[0].listOfRoomViewData = [];
        	_.each(roomModel.roomOrder, function(roomCode){
        		var roomList = _.filter(roomUnorderedList, function(room){
        			return room.roomCode === roomCode;
        		});
        		if(roomList && roomList.length){
        			widget.jsonData.roomOptionsViewData[0].listOfRoomViewData.push(roomList[0]);
        		}
        	});
        },
         // Comparing stored array with new array
        compareReorderArray: function(oldArray, newArray){
        	var sortedOldArray = [],
        		sortedNewArray = [];
        	sortedOldArray = oldArray.sort();
        	sortedNewArray = newArray.sort();
        	if(sortedOldArray.join() === sortedNewArray.join()){
        		return true;
        		}
        		return false;
            }
   });
    return tui.widget.booking.changeroomallocation.view.AlternativeRoom;
});