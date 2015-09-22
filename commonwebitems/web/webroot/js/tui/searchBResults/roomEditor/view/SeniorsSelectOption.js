define("tui/searchBResults/roomEditor/view/SeniorsSelectOption", [
  "dojo",
  "dojo/on",
  "tui/searchBResults/roomEditor/view/PartyCompSelectOption"], function (dojo, on) {

  dojo.declare("tui.searchBResults.roomEditor.view.SeniorsSelectOption", [tui.searchBResults.roomEditor.view.PartyCompSelectOption], {

    // ----------------------------------------------------------------------------- properties

    roomModel: null,

    watcher: null,

    // ----------------------------------------------------------------------------- methods

    postCreate: function () {
      // summary:
      //      Sets default values for class properties.
      var seniorsSelectOption = this;
      seniorsSelectOption.inherited(arguments);
      seniorsSelectOption.initConnector();

      seniorsSelectOption.watcher = seniorsSelectOption.roomModel.watch("seniors", function (name, oldValue, newValue) {
        if (parseInt(seniorsSelectOption.getSelectedData().value, 10) !== newValue) {
          seniorsSelectOption.setSelectedValue(newValue);
        }
      });

      // set initial value from model
      seniorsSelectOption.setSelectedValue(seniorsSelectOption.roomModel.get("seniors"));

      seniorsSelectOption.subscribe("tui/searchBResults/roomEditor/view/RoomsEditor/Errors/Party", function (action) {
        action = action ? "addClass" : "removeClass";
        dojo[action](seniorsSelectOption.domNode, "error");
      });
      seniorsSelectOption.tagElement(seniorsSelectOption.domNode, seniorsSelectOption.roomModel.id + "-seniors");
    },

    initConnector: function () {
      // summary:
      //      Connects to change event and sets value to model
      var seniorsSelectOption = this;
      seniorsSelectOption.connect(seniorsSelectOption, "onChange", function (name, oldValue, newValue) {
        seniorsSelectOption.roomModel.set("seniors", parseInt(newValue.value, 10));
      });
    },

    destroyRecursive: function () {
      // summary:
      //      Extends default method to unwatch widget's watchers
      var seniorsSelectOption = this;
      seniorsSelectOption.watcher.unwatch();
      seniorsSelectOption.inherited(arguments);
    }

  });

  return tui.searchBResults.roomEditor.view.SeniorsSelectOption;
});