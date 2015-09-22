define("tui/searchBResults/roomEditor/view/RoomsPopup", [
	"dojo",
	"dojo/text!tui/searchBResults/roomEditor/view/templates/editRoomsTmpl.html",
	"tui/mvc/Klass",
	"tui/searchBResults/roomEditor/model/RoomsEditorModel",
	"tui/searchBResults/roomEditor/view/RoomsEditor",
	"tui/widget/popup/Popup",
    "tui/searchB/nls/Searchi18nable"], function (dojo, tmpl, Klass) {

	dojo.declare("tui.searchBResults.roomEditor.view.RoomsPopup", [tui.widget.popup.Popup, tui.searchB.nls.Searchi18nable], {

		// ---------------------------------------------------------------- properties

		modal: true,

		tmpl: tmpl,

		roomsEditor: null,

		jsonData: null,

		// ---------------------------------------------------------------- methods

		postCreate: function () {
			// summary:
			//      Sets default values for class properties.
			var roomsPopup = this;
            roomsPopup.initSearchMessaging();
			roomsPopup.inherited(arguments);
            roomsPopup.messages = roomsPopup.searchMessaging[dojoConfig.site];

            roomsPopup.tagElement(roomsPopup.domNode, "Choose number of rooms");
		},

		onAfterTmplRender: function () {
			// summary:
			//      Extends default method
			var roomsPopup = this;
			roomsPopup.inherited(arguments);
			// tagging room pop section
			 var dom =  dojo.query(".holiday-rooms", roomsPopup.popupDomNode)[0];
			 roomsPopup.tagElement(dom, "Number of Rooms Party");
			 var dom =  dojo.query(".button-container", roomsPopup.popupDomNode)[0];
			 roomsPopup.tagElement(dom, "Number of Rooms Search");

			// init rooms editor
			roomsPopup.initRoomsEditor();
		},

		initRoomsEditor: function () {
			// summary:
			//      Initializes new tui.searchResults.view.RoomsEditor
			var roomsPopup = this;

			//set mvc id
			dojo.attr(roomsPopup.popupDomNode, "data-klass-id", "roomsEditorB");
			roomsPopup.roomsEditor = new Klass(null, roomsPopup.popupDomNode).getCreatedInstance();
		},

		resize: function (domNode) {
			// summary:
			//      Extends default method
			var roomsPopup = this;
			domNode = domNode || roomsPopup.popupDomNode;
			roomsPopup.posElement(domNode);
		},

		open: function () {
			// summary:
			//      Extends default method
			var roomsPopup = this;
			window.scrollTo(0,0);
            // publish close events to popups
            dojo.publish("tui.searchBResults.view.InfoPopup.close");
            dojo.publish("tui.searchBPanel.view.ErrorPopup.close");
			roomsPopup.inherited(arguments);
            if(roomsPopup.popupDomNode && dojo.hasClass(roomsPopup.popupDomNode, "no-results")) {
                dojo.removeClass(roomsPopup.popupDomNode, "no-results");
            }
		},

        close: function () {
            var roomsPopup = this;
            roomsPopup.publishMessage("searchBPanel/searchOpening");
            roomsPopup.inherited(arguments);
        }

	});

	return tui.searchBResults.roomEditor.view.RoomsPopup;
});