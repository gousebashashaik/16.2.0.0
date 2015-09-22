define("tui/searchPanel/view/flights/SeniorsSelectOption", [
	"dojo",
	"tui/searchPanel/view/PartyCompSelectOption"], function (dojo) {


	dojo.declare("tui.searchPanel.view.flights.SeniorsSelectOption", [tui.searchPanel.view.PartyCompSelectOption], {

		// ----------------------------------------------------------------------------- properties



		// ----------------------------------------------------------------------------- methods

		postCreate: function () {
			var seniorsSelectOption = this;
			seniorsSelectOption.inherited(arguments);
			seniorsSelectOption.addDropdownEventlistener();

            seniorsSelectOption.tagElement(seniorsSelectOption.domNode, "seniors");

			/*seniorsSelectOption.searchPanelModel.watch("seniors", function (name, oldValue, newValue) {
				if (parseInt(seniorsSelectOption.getSelectedData().value, 10) !== newValue) {
					seniorsSelectOption.setSelectedValue(newValue);
				}
			});*/

			/*seniorsSelectOption.searchPanelModel.searchErrorMessages.watch("partyComp", function (name, oldError, newError) {
				if (_.size(newError) > 1) {
					return;
				}
				var key = (_.keys(newError)[0]);
				seniorsSelectOption.validateErrorMessage(newError[key], {
					errorMessage: newError[key],
					errorPopupClass: "error-medium",
					field: "partyComp",
					key: key
				});
				// publish error for listeners
				if (_.size(newError) === 1) {
					dojo.publish("tui/searchPanel/view/PartyCompositionView/Errors", true);
				}
				if (_.size(newError) === 0) {
					dojo.publish("tui/searchPanel/view/PartyCompositionView/Errors", false);
				}
			});*/
            dojo.global.isPartyCompositionErrorNew=true;
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
				key: key
			});
			// publish error for listeners
			if (_.size(newError) === 1) {
				dojo.publish("tui/searchPanel/view/PartyCompositionView/Errors", [seniorsSelectOption.widgetController, true]);
			}
			if (_.size(newError) === 0) {
				dojo.publish("tui/searchPanel/view/PartyCompositionView/Errors", [seniorsSelectOption.widgetController, false]);
			}
			dojo.global.isPartyCompositionErrorNew=false;
			}
		},

		updateSeniorView: function(name, oldValue, newValue){
			var seniorsSelectOption = this;
			if (parseInt(seniorsSelectOption.getSelectedData().value, 10) !== newValue) {
				seniorsSelectOption.setSelectedValue(newValue);
			}
		},

		addDropdownEventlistener: function () {
			var seniorsSelectOption = this;
			seniorsSelectOption.inherited(arguments);

			seniorsSelectOption.connect(seniorsSelectOption, "onChange", function (name, oldValue, newValue) {
				seniorsSelectOption.searchPanelModel.set("seniors", parseInt(newValue.value, 10));
			});
		}
	});

	return tui.searchPanel.view.flights.SeniorsSelectOption;
});
