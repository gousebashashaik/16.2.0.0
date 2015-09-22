define("tui/searchResults/roomEditor/view/RoomsSummary", [
    "dojo",
    "dojo/on",
    "dojo/text!tui/searchResults/roomEditor/view/templates/roomSummaryTmpl.html",
    "tui/widget/mixins/Templatable",
    "tui/widget/_TuiBaseWidget",
    "tui/search/nls/Searchi18nable"], function (dojo, on, roomSummaryTmpl) {

    dojo.declare("tui.searchResults.roomEditor.view.RoomsSummary", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable, tui.search.nls.Searchi18nable], {

        // ---------------------------------------------------------------- properties

        tmpl: roomSummaryTmpl,

        rooms: null,

        model: null,

        previousRooms: null,

        clearRoomsHandler: null,

        userSummary: null,

        defaultNode: null,

        triggerNode: null,

        subscribableMethods: ["renderSummary", "revert"],

        // ---------------------------------------------------------------- mediator methods

        generateRequest: function (field) {},

        handleNoResults: function (name, oldResults, newResults) {},

        refresh: function (field, oldValue, newValue, response) {
            var roomsSummary = this;
            if (field === "clearAll" || field === "clearRooms") roomsSummary.revert();
        },

        // ---------------------------------------------------------------- rooms summary methods

        postCreate: function () {
            // summary:
            //      Sets default values for class properties.
            var roomsSummary = this;
            roomsSummary.inherited(arguments);
            // TODO: use i18n in template
            roomsSummary.initSearchMessaging();
            roomsSummary.model = dijit.registry.byId('mediator').registerController(roomsSummary, 'searchRequest');
            roomsSummary.defaultNode = dojo.query(".room-editor-default", roomsSummary.domNode)[0];
            roomsSummary.triggerNode = dojo.query(".js-edit-rooms-trigger", roomsSummary.domNode)[0];

            // restore room summary if user has made change (browser back scenario)
            if(_.has(roomsSummary.model, "rooms") && roomsSummary.model['rooms'].length) {
                roomsSummary.renderSummary(_.map(dojo.clone(roomsSummary.model.rooms), function(room){
                    room.id = "room-" + room.id;
                    return room;
                }));
            }

            // UX tagging
            roomsSummary.tagElement(roomsSummary.triggerNode, "Number of Rooms Edit");
        },

        renderSummary: function (rooms) {
            var roomsSummary = this;
            roomsSummary.tidyUp();

            dojo.removeClass(roomsSummary.triggerNode, "hide");
            dojo.addClass(roomsSummary.defaultNode, "hide");

            roomsSummary.userSummary = dojo.place(roomsSummary.renderTmpl(roomSummaryTmpl, {rooms: (dojoConfig.site === 'cruise') ? roomsSummary.cabins(rooms) : rooms}), roomsSummary.domNode, "last");

            roomsSummary.clearRoomsHandler = on(dojo.query(".reset-rooms", roomsSummary.userSummary)[0], "click", function(){
                if(dojoConfig.site === 'cruise') {
                    var roomEditorInst = _.find(dijit.registry.byId('mediator').controllers, function(controller){ return controller.id === 'tui_searchResults_roomEditor_view_RoomsEditor_0'; });
                    if(!_.isUndefined(roomEditorInst)){
							roomEditorInst.roomsChanged = false;
							dijit.registry.byId('mediator').fire('applyFilters', null, null);
							roomsSummary.revert();
						} else {
							var scrollDoc = dojo.isMozilla || dojo.isIE ? document.documentElement : document.body;
							var winscrollTop = scrollDoc.scrollTop;
							dojo.publish("tui.searchResults.roomEditor.view.RoomsPopup.open");
							dojo.publish("tui.searchResults.roomEditor.view.RoomsPopup.close");
							scrollDoc.scrollTop = winscrollTop;
							var roomEditorInst = _.find(dijit.registry.byId('mediator').controllers, function(controller){ return controller.id === 'tui_searchResults_roomEditor_view_RoomsEditor_0'; });
							roomEditorInst.roomsChanged = false;
							dijit.registry.byId('mediator').fire('applyFilters', null, null);
							roomsSummary.revert();
						}
                } else{
                    dijit.registry.byId('mediator').fire('clearRooms', roomsSummary.previousRooms, null);
                }
            });

            roomsSummary.tagElement(dojo.query(".reset-rooms a", roomsSummary.userSummary)[0], "Cheapest Allocation Party");
            dojo.publish("tui.filterPanel.view.FilterController.resize");
        },

        revert: function () {
            var roomsSummary = this;
            // hide summary links
            dojo.addClass(roomsSummary.triggerNode, "hide");
            dojo.removeClass(roomsSummary.defaultNode, "hide");
            roomsSummary.tidyUp();
            dojo.publish("tui.filterPanel.view.FilterController.resize");
        },

        tidyUp: function () {
            var roomsSummary = this;
            // destroy summary and remove listener
            if(roomsSummary.clearRoomsHandler) roomsSummary.clearRoomsHandler.remove();
            if(roomsSummary.userSummary) dojo.destroy(roomsSummary.userSummary);
        },

        cabins: function(rooms){
            return _.map(dojo.clone(rooms), function(room){
                room.id = room.id.replace(/room/i, "cabin");
                return room;
            });
        }
    });

    return tui.searchResults.roomEditor.view.RoomsSummary;
});