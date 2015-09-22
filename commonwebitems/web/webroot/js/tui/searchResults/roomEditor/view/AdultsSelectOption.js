define("tui/searchResults/roomEditor/view/AdultsSelectOption", [
  "dojo",
  "dojo/on",
  "tui/searchResults/roomEditor/view/PartyCompSelectOption"], function (dojo, on) {

  dojo.declare("tui.searchResults.roomEditor.view.AdultsSelectOption", [tui.searchResults.roomEditor.view.PartyCompSelectOption], {

    // ----------------------------------------------------------------------------- properties

    roomModel: null,

    watcher: null,

    // ----------------------------------------------------------------------------- methods

    postCreate: function () {
      // summary:
      //      Sets default values for class properties.
      var adultsSelectOption = this;
      adultsSelectOption.inherited(arguments);
      adultsSelectOption.initConnector();

      adultsSelectOption.watcher = adultsSelectOption.roomModel.watch("adults", function (name, oldValue, newValue) {
        if (parseInt(adultsSelectOption.getSelectedData().value, 10) !== newValue) {
          adultsSelectOption.setSelectedValue(newValue);
        }
      });

      // set initial value from model
      adultsSelectOption.setSelectedValue(adultsSelectOption.roomModel.get("adults"));

      adultsSelectOption.subscribe("tui/searchResults/roomEditor/view/RoomsEditor/Errors/Party", function (action) {
        action = action ? "addClass" : "removeClass";
        dojo[action](adultsSelectOption.domNode, "error");
      });
      if( dojoConfig.site == "cruise"){
    	  var tagTxt = "c" + adultsSelectOption.roomModel.id.replace("room-", "") + "Adults";
    	  adultsSelectOption.tagElement(adultsSelectOption.domNode, tagTxt);
      }else{
    	  adultsSelectOption.tagElement(adultsSelectOption.domNode, adultsSelectOption.roomModel.id + "-adults");
      }
    },

    initConnector: function () {
      // summary:
      //      Connects to change event and sets value to model
      var adultsSelectOption = this;
      adultsSelectOption.connect(adultsSelectOption, "onChange", function (name, oldValue, newValue) {
        adultsSelectOption.roomModel.set("adults", parseInt(newValue.value, 10));
      });
    },

    destroyRecursive: function () {
      // summary:
      //      Extends default method to unwatch widget's watchers
      var adultsSelectOption = this;
      adultsSelectOption.watcher.unwatch();
      adultsSelectOption.inherited(arguments);
    }

  });

  return tui.searchResults.roomEditor.view.AdultsSelectOption;
});