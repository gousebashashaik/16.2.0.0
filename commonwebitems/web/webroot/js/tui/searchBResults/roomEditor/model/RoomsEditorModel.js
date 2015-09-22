define("tui/searchBResults/roomEditor/model/RoomsEditorModel", [
  "dojo",
  "dojo/Stateful",
  "dojo/store/Observable",
  "tui/searchBResults/roomEditor/store/RoomsStore",
  "tui/search/model/SearchModel",
  "tui/search/base/PartyCompositionBase"], function (dojo, Stateful, Observable, RoomsStore) {

  dojo.declare("tui.searchBResults.roomEditor.model.RoomsEditorModel", [tui.search.model.SearchModel, tui.search.base.PartyCompositionBase], {

    // ----------------------------------------------------------------------------- properties

    // Defaults for party composition rooms.
    rooms: null,

    valid: false,

    // ----------------------------------------------------------------------------- methods

    constructor: function (props) {
      // summary:
      //		Called on class creation, initialising model data.
      var roomsEditorModel = this;
      dojo.mixin(roomsEditorModel, props);

      roomsEditorModel.searchErrorMessages = new Stateful({
        partyComp: {},
        partyChildAges: {},
        roomOccupancy: {}
      });

      roomsEditorModel.rooms = new Observable(new RoomsStore());
    },

    flattenRooms: function () {
      // summary:
      //		Combines room parties, grouped by age category
      var roomsEditorModel = this;

      // redundant checking for missing/empty properties
      if (!roomsEditorModel.rooms && !_.isArray(roomsEditorModel.childAges)) {
        return;
      }

      // set some variables
      var rooms = dojo.clone(roomsEditorModel.rooms.query()),
          adults = 0, seniors = 0, children = 0, childAges = [];

      // add each room data to the total
      _.forEach(rooms, function (room) {
        adults += room.adults;
        children += room.children;
        seniors += room.seniors;
        childAges.push(room.childAges);

        // validate that room is occupied
        if (room.adults + room.seniors + room.children === 0) {
          roomsEditorModel.searchErrorMessages.set("roomOccupancy", {
            roomOccupancy: roomsEditorModel.searchMessaging[dojoConfig.site].errors.roomOccupancy
          });
        }

        // validate that room doesn't have children with no adults
        if (((room.adults + room.seniors) === 0) && (room.children > 0)) {
          roomsEditorModel.searchErrorMessages.set("roomOccupancy", {
            roomOccupancy: roomsEditorModel.searchMessaging[dojoConfig.site].errors.roomChildOnly
          });
        }

        // validate room child ages, allows searchResults rooms editor to assign error to individual rooms
        if (childAges.length > 0) {
          roomsEditorModel.validateRoomChildAges(room);
        }
      });

      // set total party composition numbers for each age group
      // TODO: find out if this needs to be set to search panel on successful results
      roomsEditorModel.adults = adults;
      roomsEditorModel.seniors = seniors;
      roomsEditorModel.children = children;
      roomsEditorModel.childAges = _.flatten(childAges);
    },

    validateRoomChildAges: function (room) {
      // summary:
      //    Validate childAges groups for rooms editor,
      //    publishes room data in order to determine which room the error is related to
      var roomsEditorModel = this;

      if (_.filter(room.childAges,function (age) {
        return age === -1;
      }).length > 0) {
        dojo.publish("tui/searchB/model/ChildAges/Errors");
        if (_.isEmpty(roomsEditorModel.searchErrorMessages.get("partyChildAges"))) {
          roomsEditorModel.searchErrorMessages.set("partyChildAges", {
            childNoAges: roomsEditorModel.searchMessaging[dojoConfig.site].errors.childNoAges
          });
        }
      }
    }

  });

  return tui.searchBResults.roomEditor.model.RoomsEditorModel;
});