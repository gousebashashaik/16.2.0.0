define("tui/searchBResults/roomEditor/model/RoomModel", ["dojo", "dojo/Stateful"], function (dojo, stateful) {

	dojo.declare("tui.searchBResults.roomEditor.model.RoomModel", [dojo.Stateful], {

		// ----------------------------------------------------------------------------- properties

		// room id
		id: null,

		// number of adults
		adults: 0,

		// number of seniors
		seniors: 0,

		// number of children
		children: 0,

		// contains array of child ages
		childAges: null,

		// ----------------------------------------------------------------------------- methods

		constructor:function () {
			var roomModel = this;
			roomModel.childAges = [];
		}
	});

	return tui.searchBResults.roomEditor.model.RoomModel;
});