define("tui/searchPanel/view/flights/AdultsSelectOption", [
	"dojo",
	"tui/searchPanel/view/PartyCompSelectOption"], function (dojo) {

	dojo.declare("tui.searchPanel.view.flights.AdultsSelectOption", [tui.searchPanel.view.PartyCompSelectOption], {

		// ----------------------------------------------------------------------------- methods

		postCreate: function () {
			var adultsSelectOption = this;
			adultsSelectOption.inherited(arguments);
			adultsSelectOption.addDropdownEventlistener();
            adultsSelectOption.tagElement(adultsSelectOption.domNode, "adults");
            dojo.global.isPartyCompositionErrorNew=true;
		},

		updateAdultsView: function (name, oldValue, newValue) {
			// stateful watcher method
			var adultsSelectOption = this;
			if (parseInt(adultsSelectOption.getSelectedData().value, 10) !== newValue) {
				adultsSelectOption.setSelectedValue(newValue);
			}
		},

		addDropdownEventlistener: function () {
			var adultsSelectOption = this;
			adultsSelectOption.inherited(arguments);
			adultsSelectOption.connect(adultsSelectOption, "onChange", function (name, oldValue, newValue) {
				adultsSelectOption.searchPanelModel.set("adults", parseInt(newValue.value, 10));
			});
		},

    updatePartyCompError: function(name, oldError, newError){
      var seniorsSelectOption = this;
      if (_.size(newError) > 1) {
        return;
      }
      var key = (_.keys(newError)[0]);
      if(dojo.global.isPartyCompositionErrorNew){
      seniorsSelectOption.validateErrorMessage(newError[key], {
        errorMessage: newError[key],
        errorPopupClass: "error-medium",
        field: "partyComp",
        key: key,
        floatWhere: "position-bottom-left"
      });
  
      // publish error for listeners
      if (_.size(newError) === 1) {
        dojo.publish("tui/searchPanel/view/PartyCompositionView/Errors", [seniorsSelectOption.widgetController, true]);
      }
      //dojo.global.isPartyCompositionErrorNew=false; 
    }
      if (_.size(newError) === 0) {
          dojo.publish("tui/searchPanel/view/PartyCompositionView/Errors", [seniorsSelectOption.widgetController, false]);
        }
    }
	});

	return tui.searchPanel.view.flights.AdultsSelectOption;
});