define("tui/searchResults/roomEditor/view/RoomUnit", [
	"dojo",
	"tui/searchResults/roomEditor/view/PartyCompSelectOption",
	"tui/searchResults/roomEditor/view/AdultsSelectOption",
	"tui/searchResults/roomEditor/view/SeniorsSelectOption",
	"tui/searchResults/roomEditor/view/ChildrenSelectOption",
	"tui/widget/form/SelectOption",
	"tui/widget/_TuiBaseWidget"], function (dojo, SelectOption, AdultsSelectOption, SeniorsSelectOption, ChildrenSelectOption) {

	dojo.declare("tui.searchResults.roomEditor.view.RoomUnit", [tui.widget._TuiBaseWidget], {

		// ----------------------------------------------------------------------------- properties

		roomModel: null,

		roomsEditorModel: null,

		popupNode: null,

		selectOptions: null,

    showSeniors: false,

		// ----------------------------------------------------------------------------- methods

		postCreate: function () {
			// summary:
			//      Sets default values for class properties.
			var roomUnit = this;
			roomUnit.inherited(arguments);
			roomUnit.selectOptions = [];
			roomUnit.initSelectOptions();
		},

		initSelectOptions: function () {
			// summary:
			//      Initialises select option widgets for this view
			var roomUnit = this;
			dojo.parser.parse(roomUnit.domNode);
			roomUnit.selectOptions.push(
				new AdultsSelectOption({
					roomModel: roomUnit.roomModel,
					roomsEditorModel: roomUnit.roomsEditorModel
				}, dojo.query(".adults", roomUnit.domNode)[0]),

				new ChildrenSelectOption({
					roomModel: roomUnit.roomModel,
					roomsEditorModel: roomUnit.roomsEditorModel,
					unitNode: roomUnit.domNode,
					popupNode: roomUnit.popupNode
				}, dojo.query(".children", roomUnit.domNode)[0])
			);
      if(roomUnit.showSeniors) {
        roomUnit.selectOptions.push(new SeniorsSelectOption({
          roomModel: roomUnit.roomModel,
          roomsEditorModel: roomUnit.roomsEditorModel
        }, dojo.query(".seniors", roomUnit.domNode)[0]));
      }
		},

		// todo: extend/override destroyRecursive
		destroyRecursive: function () {
			// summary:
			//      Destroy all widgets/watchers/children of this room widget
			var roomUnit = this;
			_.forEach(roomUnit.selectOptions, function(item){
				item.destroyRecursive();
			});
			roomUnit.inherited(arguments);
		}

	});

	return tui.searchResults.roomEditor.view.RoomUnit;
});