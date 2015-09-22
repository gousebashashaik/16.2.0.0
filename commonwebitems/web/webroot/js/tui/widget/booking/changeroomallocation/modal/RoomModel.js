define("tui/widget/booking/changeroomallocation/modal/RoomModel", ["dojo"], function (dojo) {

		function roomModel () {
			var roomModel = this;
			roomModel.rooms = [];
			roomModel.jsonData={};
			roomModel.alternateRooms={};
			roomModel.roomSellingCode=null;
			roomModel.roomOrder=[];
			roomModel.scrollPosition={};
			roomModel.allRoomsVisible = false;
		}

	return new roomModel();
});      