define("tui/searchResults/roomEditor/store/RoomsStore", [
	"dojo",
    "tui/searchResults/roomEditor/model/RoomModel",
	"dojo/store/Memory" ], function (dojo, RoomModel) {

	dojo.declare("tui.searchResults.roomEditor.store.RoomsStore", [dojo.store.Memory], {

		// ----------------------------------------------------------------------------- methods

		put: function (object, options) {
			var roomsStore = this;
			if (!object.isInstanceOf(tui.searchResults.roomEditor.model.RoomModel)) {
				throw "Object needs to be an instance of tui.searchResults.roomEditor.model.RoomModel";
			}
			return roomsStore.inherited(arguments);
		},

        getStorageData: function () {
            var roomsStore = this;
            var rooms = [];
            _.each(roomsStore.query(), function(item){
                rooms.push({
                    id: item.id,
                    noOfAdults: item.adults,
                    noOfSeniors: item.seniors,
                    noOfChildren: item.children,
                    childrenAge: item.childAges
                });
            });
            return rooms;
        }

	});

	return tui.searchResults.roomEditor.store.RoomsStore;
});