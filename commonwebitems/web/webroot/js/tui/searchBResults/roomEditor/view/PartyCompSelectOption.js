define("tui/searchBResults/roomEditor/view/PartyCompSelectOption", [
  "dojo",
  "dojo/on",
  "tui/widget/form/SelectOption"], function (dojo, on) {

  dojo.declare("tui.searchBResults.roomEditor.view.PartyCompSelectOption", [tui.widget.form.SelectOption], {

    // ----------------------------------------------------------------------------- properties

    roomModel: null,

    roomsEditorModel: null,

    watcher: null,

    // ----------------------------------------------------------------------------- methods

    postCreate: function () {
      // summary:
      //      Sets default values for class properties.
      var partyCompSelectOption = this;
      partyCompSelectOption.inherited(arguments);

      on(partyCompSelectOption.selectDropdown, "click", function () {
        var errorMessageModel = dojo.clone(partyCompSelectOption.roomsEditorModel.searchErrorMessages.get("partyComp"));
        var key = (_.keys(errorMessageModel)[0]);
        delete errorMessageModel[key];
        partyCompSelectOption.roomsEditorModel.searchErrorMessages.set("partyComp", errorMessageModel);
      });

      on(partyCompSelectOption.selectDropdown, "click", function () {
        var errorMessageModel = dojo.clone(partyCompSelectOption.roomsEditorModel.searchErrorMessages.get("partyChildAges"));
        var key = (_.keys(errorMessageModel)[0]);
        delete errorMessageModel[key];
        partyCompSelectOption.roomsEditorModel.searchErrorMessages.set("partyChildAges", errorMessageModel);
      });

      on(partyCompSelectOption.selectDropdown, "click", function () {
        var errorMessageModel = dojo.clone(partyCompSelectOption.roomsEditorModel.searchErrorMessages.get("roomOccupancy"));
        var key = (_.keys(errorMessageModel)[0]);
        delete errorMessageModel[key];
        partyCompSelectOption.roomsEditorModel.searchErrorMessages.set("roomOccupancy", errorMessageModel);
      });

      partyCompSelectOption.subscribe("tui/searchBResults/roomEditor/model/roomNotOccupied", function (action) {
        action = action ? "addClass" : "removeClass";
        if ((partyCompSelectOption.roomModel.adults + partyCompSelectOption.roomModel.seniors + partyCompSelectOption.roomModel.children) === 0) {
          dojo[action](partyCompSelectOption.domNode, "error");
        }
        if (((partyCompSelectOption.roomModel.adults + partyCompSelectOption.roomModel.seniors) === 0) && (partyCompSelectOption.roomModel.children > 0)) {
          dojo[action](partyCompSelectOption.domNode, "error");
        }
      });

      partyCompSelectOption.subscribe("tui/searchBPanel/searchOpening", function (component) {
        partyCompSelectOption.hideList();
      });
    }

  });

  return tui.searchBResults.roomEditor.view.PartyCompSelectOption;
});