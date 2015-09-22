define("tui/searchBResults/roomEditor/view/RoomsEditor", [
    "dojo",
    "dojo/_base/declare",
    "dojo/on",
    "dojo/topic",
    "dojo/text!tui/searchBResults/roomEditor/view/templates/roomOptionsTmpl.html",
    "tui/widget/form/SelectOption",
    "tui/searchBResults/roomEditor/view/RoomUnit",
    "tui/searchBResults/roomEditor/model/RoomModel",
    "tui/widget/mixins/Templatable",
    "tui/widget/_TuiBaseWidget",
    "tui/searchB/nls/Searchi18nable"], function (dojo, declare, on, topic, roomOptionsTmpl, SelectOption, RoomUnit, RoomModel) {

    return declare("tui.searchBResults.roomEditor.view.RoomsEditor", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable, tui.searchB.nls.Searchi18nable], {

        // ---------------------------------------------------------------- properties

        tmpl: roomOptionsTmpl,

        templateView: null,

        numRoomsSelectOption: null,

        rooms: null,

        searchButton: null,

        filterNode: null,

        errorNode: null,

        errorMessages: null,

        //roomSummary: null,

        previousRooms: null,

        originalModel: null,

        roomsChanged: false,

        roomsEditorModel: null,

        showSeniors: false,

        //subscribableMethods: ["loadRoomsState"],

        // ---------------------------------------------------------------- mediator methods

        //@TODO: refactor show/hide summary, undo/reset

        /*loadRoomsState: function (request) {
          var roomsEditor = this;
          roomsEditor.numRoomsSelectOption.setSelectedValue(0);
          roomsEditor.numRoomsSelectOption.setSelectedValue(_.size(request));
          _.each(request, function(room, i){
            roomsEditor.rooms[i].roomModel.set("adults", room.noOfAdults);
            roomsEditor.rooms[i].roomModel.set("seniors", room.noOfSeniors);
            roomsEditor.rooms[i].roomModel.set("children", room.noOfChildren);
            roomsEditor.rooms[i].roomModel.set("childAges", room.childrenAge);
          });

          // publish rooms to room summary view
          topic.publish("tui.searchResults.roomEditor.view.RoomsSummary.renderSummary", [roomsEditor.getFormattedRooms()]);
        },*/

        generateRequest: function (field) {
            var roomsEditor = this;

            // don't send rooms if no change made
            if(!roomsEditor.roomsChanged) return;

            // send empty object if clear selected
            if (field === "clearRooms" || field === "clearAll") {
                return {};
            } else {
                var returnObj = {
                    noOfAdults: roomsEditor.roomsEditorModel.adults,
                    noOfSeniors: roomsEditor.roomsEditorModel.seniors,
                    noOfChildren: roomsEditor.roomsEditorModel.children,
                    childrenAge: roomsEditor.roomsEditorModel.childAges,
                    // backend expects only numbers for room ids
                    rooms: _.map(dojo.clone(roomsEditor.roomsEditorModel.rooms.getStorageData()), function(room){
                        room.id =  parseInt(room.id.replace(/.*-/, ""), 10);
                        return room;
                    })
                };
                // add searchRequest type if room request, else not
                field === 'rooms' ? dojo.mixin(returnObj, { searchRequestType: field }) : null;
                return returnObj;
            }
        },

        refresh: function (field, oldValue, newValue, response) {
            var roomsEditor = this;
            // store a copy of previous rooms to allow undo action
            roomsEditor.previousRooms = roomsEditor.roomsEditorModel.rooms.getStorageData();

            if (field === "rooms" && response.searchResult.endecaResultsCount > 0) {
                dojo.removeClass(roomsEditor.domNode, "updating");
                topic.publish("tui.searchBResults.roomEditor.view.RoomsPopup.close");
                // publish rooms to room summary view
                topic.publish("tui.searchBResults.roomEditor.view.RoomsSummary.renderSummary", roomsEditor.getFormattedRooms());
            }
            if (field === "clearAll" || field === "clearRooms") {
                roomsEditor.reset(response);
            }
        },

        handleNoResults: function (name, oldResults, newResults) {
            var roomsEditor = this;
            if (name === 'rooms') {
                dojo.removeClass(roomsEditor.domNode, "updating");
                dojo.addClass(roomsEditor.domNode, "no-results");
                topic.publish("tui.searchBResults.roomEditor.view.RoomsPopup.resize");
                roomsEditor.undo();
            }
        },

        undo: function () {
            var roomsEditor = this;
            var size = _.size(roomsEditor.previousRooms);
            if (size === 0) {
                // if set to 0, reset back to 1 with original PAX
                roomsEditor.numRoomsSelectOption.setSelectedValue(0);
                roomsEditor.numRoomsSelectOption.setSelectedValue(1);
                roomsEditor.rooms[0].roomModel.set("adults", roomsEditor.originalModel.noOfAdults);
                roomsEditor.rooms[0].roomModel.set("seniors", roomsEditor.originalModel.noOfSeniors);
                roomsEditor.rooms[0].roomModel.set("children", roomsEditor.originalModel.noOfChildren);
                roomsEditor.rooms[0].roomModel.set("childAges", roomsEditor.originalModel.childrenAge);
                // tell room summary to revert to default view
                topic.publish("tui.searchBResults.roomEditor.view.RoomsSummary.revert");
            } else {
                // set back to previous size with previous PAX
                roomsEditor.numRoomsSelectOption.setSelectedValue(0);
                roomsEditor.numRoomsSelectOption.setSelectedValue(size);
                _.each(roomsEditor.previousRooms, function(room, i){
                    roomsEditor.rooms[i].roomModel.set("adults", room.noOfAdults);
                    roomsEditor.rooms[i].roomModel.set("seniors", room.noOfSeniors);
                    roomsEditor.rooms[i].roomModel.set("children", room.noOfChildren);
                    roomsEditor.rooms[i].roomModel.set("childAges", room.childrenAge);
                });
                topic.publish("tui.searchBResults.roomEditor.view.RoomsSummary.renderSummary", [roomsEditor.getFormattedRooms()]);
            }
        },

        reset: function (response) {
            var roomsEditor = this;
            // set selection of rooms back to 1 (first have to reset to 0)
            roomsEditor.numRoomsSelectOption.setSelectedValue(0);
            roomsEditor.numRoomsSelectOption.setSelectedValue(1);
            // set model to response
            roomsEditor.rooms[0].roomModel.set("adults", response.searchRequest.noOfAdults);
            roomsEditor.rooms[0].roomModel.set("seniors", response.searchRequest.noOfSeniors);
            roomsEditor.rooms[0].roomModel.set("children", response.searchRequest.noOfChildren);
            roomsEditor.rooms[0].roomModel.set("childAges", response.searchRequest.childrenAge);
            // set changed back to false
            roomsEditor.roomsChanged = false;
            // close (doh!)
            dojo.removeClass(roomsEditor.domNode, "updating");
            topic.publish("tui.searchBResults.roomEditor.view.RoomsPopup.close");
        },

        getFormattedRooms: function () {
            // returns stored rooms formatted for summary template
            var roomsEditor = this;
            return _.map(dojo.clone(roomsEditor.roomsEditorModel.rooms.getStorageData()), function(room){
                room.id = room.id.replace(/-/, " ");
                return room;
            });
        },

        // ---------------------------------------------------------------- rooms editor methods

        postCreate: function () {
            // summary:
            //      Sets default values for class properties.
            var roomsEditor = this;
            roomsEditor.inherited(arguments);
            roomsEditor.initSearchMessaging();
            roomsEditor.previousRooms = {};

            // register with mediator, and get searchRequest
            roomsEditor.originalModel = dijit.registry.byId('mediator').registerController(roomsEditor, 'searchRequest');

            roomsEditor.rooms = [];
            roomsEditor.errorMessages = [];
            roomsEditor.searchButton = dojo.query("#search-again", roomsEditor.domNode)[0];
            roomsEditor.filterNode = dojo.query(".js-rooms-editor")[0];
            roomsEditor.errorNode = dojo.query(".errors", roomsEditor.domNode)[0];

            // initialise roomsEditorModel observer
            roomsEditor.initObserver();

            // initialise rooms as per searchRequest PAX
            roomsEditor.initRooms(roomsEditor.originalModel);

            // initialise number of rooms selectOption
            roomsEditor.initRoomsSelectOption();

            roomsEditor.delegateEvents();

            // remove errors from error node
            roomsEditor.subscribe("tui/searchBResults/roomEditor/view/RoomsEditor/removeError", function (key) {
                if (key === "childNoAges") {
                    topic.publish("tui/searchB/model/ChildAges/Errors", [
                        {id: 0}
                    ]);
                }
                if (dojo.query("p", roomsEditor.errorNode).length === 0) {
                    dojo.addClass(roomsEditor.errorNode, "invisible");
                    topic.publish("tui.searchBResults.roomEditor.view.RoomsPopup.resize");
                }
            });

            // UX analytics tagging
            roomsEditor.tagElement(roomsEditor.domNode, "choose-rooms-popup");
            roomsEditor.tagElement(roomsEditor.searchButton, "search-again");
            roomsEditor.tagElement(roomsEditor.numRoomsSelectOption.domNode, "number-of-rooms");
        },

        delegateEvents: function () {
            var roomsEditor = this;
            // search button event listener
            on(roomsEditor.searchButton, "click", function (event) {
                dojo.stopEvent(event);
                if (!dojo.hasClass(this, "disabled")) {
                    roomsEditor.roomsEditorModel.flattenRooms();
                    if (roomsEditor.roomsEditorModel.validate()){
                        roomsEditor.submitRequest();
                    }
                }
            });
            // reset filters
            on(roomsEditor.domNode, ".reset:click", function() {
                dojo.addClass(roomsEditor.domNode, "updating");
                dijit.registry.byId('mediator').fire('clearAll', null, null);
            });
            // undo last filter
            on(roomsEditor.domNode, ".undo:click", function() {
                topic.publish("tui.searchBResults.roomEditor.view.RoomsPopup.close");
                dojo.removeClass(roomsEditor.domNode, "no-results");
            });
        },

        submitRequest: function () {
            var roomsEditor = this;
            dojo.addClass(roomsEditor.domNode, "updating");
            roomsEditor.roomsChanged = true;
            dijit.registry.byId('mediator').fire('rooms', roomsEditor.previousRooms, roomsEditor.roomsEditorModel.rooms.getStorageData());
        },

        initObserver: function () {
            // summary:
            //      Initialise observer(s)
            var roomsEditor = this;
            // TODO: refactor this messy code
            var resultSet = roomsEditor.roomsEditorModel.rooms.query();
            resultSet.observe(function (RoomModel, remove, add) {
                if (add === -1) {
                    roomsEditor.removeRoom(RoomModel);
                } else {
                    roomsEditor.addRoom(RoomModel);
                }
                if (roomsEditor.roomsEditorModel.rooms.query().total > 0) {
                    roomsEditor.toggleButton(true);
                }
//                if (roomsEditor.roomsEditorModel.rooms.query().total > 2) {
//                    dojo.addClass(roomsEditor.domNode, "col-3");
//                } else {
//                    dojo.removeClass(roomsEditor.domNode, "col-3");
//                }
                topic.publish("tui.searchBResults.roomEditor.view.RoomsPopup.resize");
            });
        },

        initRooms: function (searchRequest) {
            // summary:
            //      Creates room(s) based on searchPanel party composition
            var roomsEditor = this;
            // restore rooms if user has made change (browser back scenario)
            if(_.has(searchRequest, "rooms") && searchRequest['rooms'].length) {
                _.each(searchRequest.rooms, function(room){
                    roomsEditor.roomsEditorModel.rooms.add(new RoomModel({
                        id: "room-" + room.id,
                        adults: room.noOfAdults,
                        seniors: room.noOfSeniors,
                        children: room.noOfChildren,
                        childAges: room.childrenAge
                    }));
                });
                topic.publish("tui.searchBResults.roomEditor.view.RoomsSummary.renderSummary", roomsEditor.getFormattedRooms());
                return;
            }
            // or just draw one room from search request
            roomsEditor.roomsEditorModel.rooms.add(new RoomModel({
                id: "room-1",
                adults: searchRequest.noOfAdults,
                seniors: searchRequest.noOfSeniors,
                children: searchRequest.noOfChildren,
                childAges: searchRequest.childrenAge
            }));
        },

        initRoomsSelectOption: function () {
            // summary:
            //      Initialises number of rooms select option widget
            var roomsEditor = this;
            roomsEditor.numRoomsSelectOption = new SelectOption({}, dojo.byId("num-rooms"));
            roomsEditor.numRoomsSelectOption.setSelectedValue(roomsEditor.roomsEditorModel.rooms.query().total);

            roomsEditor.connect(roomsEditor.numRoomsSelectOption, "onChange", function (name, oldValue, newValue) {
                oldValue = parseInt(oldValue.value, 10);
                newValue = parseInt(newValue.value, 10);

                if (oldValue === newValue) {
                    return;
                }

                var diff = 0, i;

                if (newValue > oldValue) {
                    diff = newValue - oldValue;
                    for (i = 0; i < diff; i++) {
                        var id = "room-" + (roomsEditor.roomsEditorModel.rooms.query().total + 1);
                        roomsEditor.roomsEditorModel.rooms.add(new RoomModel({
                            id: id
                        }));
                    }
                } else {
                    diff = oldValue - newValue;
                    for (i = oldValue; i > (oldValue - diff); i--) {
                        roomsEditor.roomsEditorModel.rooms.remove("room-" + i);
                    }
                }
            });

            roomsEditor.numRoomsSelectOption.subscribe("tui/searchBPanel/searchOpening", function (component) {
                roomsEditor.numRoomsSelectOption.hideList();
            });
        },

        addRoom: function (RoomModel) {
            // summary:
            //      Adds new tui.searchResults.view.RoomUnit to the view
            var roomsEditor = this, roomId = parseInt(RoomModel.id.replace(/.*-/, ""), 10);

            var roomModel = dojo.mixin(dojo.clone(RoomModel), {
                templateView: "rooms",
                id: roomId,
                showSeniors: roomsEditor.showSeniors,
                messages: roomsEditor.searchMessaging[dojoConfig.site]
            });
            var html = roomsEditor.renderTmpl(roomOptionsTmpl, roomModel);

            dojo.place(html, dojo.query(".holiday-rooms", roomsEditor.domNode)[0], "last");
//            if (roomId % 3 === 0) {
//               dojo.place(roomsEditor.renderTmpl(roomOptionsTmpl, {templateView: "roomSeparator"}), dojo.query(".holiday-rooms", roomsEditor.domNode)[0], "last")
//            }

            roomsEditor.rooms.push(new RoomUnit({
                showSeniors: roomsEditor.showSeniors,
                roomModel: RoomModel,
                roomsEditorModel: roomsEditor.roomsEditorModel,
                popupNode: roomsEditor.domNode
            }, dojo.byId(RoomModel.id)));

            topic.publish("tui.searchBResults.roomEditor.view.RoomsPopup.resize");
        },

        removeRoom: function (RoomModel) {
            // summary:
            //      Removes room unit from the view and destroys any child widgets
            var roomsEditor = this;
            var roomId = parseInt(RoomModel.id.replace(/.*-/, ""), 10) - 1;
//            if ((roomId + 1) % 3 === 0) {
//                dojo.destroy(dojo.query(".clear-it", roomsEditor.domNode)[0]);
//            }

            roomsEditor.rooms[roomId].destroyRecursive();
            roomsEditor.rooms.splice(roomId, 1);
            topic.publish("tui.searchBResults.roomEditor.view.RoomsPopup.resize");
        },

        toggleButton: function (action) {
            // summary:
            //      Enables/disables search button
            var roomsEditor = this;
            action = (action) ? "removeClass" : "addClass";
            dojo[action](roomsEditor.searchButton, "disabled");
        },

        displayErrorMessages: function (name, oldError, newError) {
            // summary:
            //      Displays error messages
            var roomsEditor = this,
                key = _.keys(newError)[0],
                errorExists = _.find(roomsEditor.errorMessages, {"id": key});

            // display one error at a time, return if no new errors
            if (_.size(newError) > 1 || _.size(newError) === 0) return;

            if (!errorExists) {
                // new error, create node
                // TODO: change to use template
                roomsEditor.errorMessages.push(dojo.create("p", {
                    "id": key,
                    "innerHTML": newError[key]
                }, roomsEditor.errorNode));
            }

            // publish to PAX selectOptions > add error class if guilty
            if (name === "partyComp") topic.publish("tui/searchBResults/roomEditor/view/RoomsEditor/Errors/Party", true);
            if (name === "roomOccupancy") topic.publish("tui/searchBResults/roomEditor/model/roomNotOccupied", true);

            // display error node
            dojo.removeClass(roomsEditor.errorNode, "invisible");

            // reposition popup to accommodate error div contracting
            topic.publish("tui.searchBResults.roomEditor.view.RoomsPopup.resize");
        },

        removeErrorMessages: function (name, oldError, newError) {
            // summary:
            //      Removes error messages
            var roomsEditor = this,
                key = _.keys(oldError)[0],
                errorExists = _.find(roomsEditor.errorMessages, {"id": key});

            // ignore if no change to errors
            if (_.size(oldError) === 0) return;

            // ignore if error is the same (validation still failing)
            if (oldError[key] === newError[key]) return;

            // destroy error
            if (errorExists) {
                dojo.destroy(errorExists);
                // remove current error from errorMessages
                roomsEditor.errorMessages = _.filter(roomsEditor.errorMessages, function (item) {
                    return item.id !== key;
                });
            }

            // publish to PAX selectOptions > remove error class if previously guilty
            if (name === "partyComp") topic.publish("tui/searchBResults/roomEditor/view/RoomsEditor/Errors/Party", false);
            if (name === "roomOccupancy") topic.publish("tui/searchBResults/roomEditor/model/roomNotOccupied", false);

            topic.publish("tui/searchBResults/roomEditor/view/RoomsEditor/removeError", [key]);

            // publish to infant selectOptions > remove error class if guilty
            if (key === "infantLimit") topic.publish("tui/searchBResults/roomEditor/view/RoomsEditor/Errors/InfantLimit", false);

            // reposition popup to accommodate error div contracting
            topic.publish("tui.searchBResults.roomEditor.view.RoomsPopup.resize");
        }

    });
});