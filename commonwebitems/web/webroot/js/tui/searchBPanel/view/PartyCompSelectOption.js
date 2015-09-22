define("tui/searchBPanel/view/PartyCompSelectOption", [
	"dojo",
	"dojo/on",
	"tui/searchBPanel/view/SearchErrorMessaging",
	"tui/widget/form/SelectOption"], function (dojo, on) {

	dojo.declare("tui.searchBPanel.view.PartyCompSelectOption", [tui.widget.form.SelectOption, tui.searchBPanel.view.SearchErrorMessaging], {

		// ----------------------------------------------------------------------------- methods

		postCreate: function () {
			var partyCompSelectOption = this;
			partyCompSelectOption.inherited(arguments);
			partyCompSelectOption.addDropdownEventlistener();

			partyCompSelectOption.subscribe("tui/searchBPanel/view/PartyCompositionView/Errors", function (controller, action) {
                if(controller !== partyCompSelectOption.widgetController) return;
				action = action ? "addClass" : "removeClass";
				dojo[action](partyCompSelectOption.domNode, "error");
			});

			partyCompSelectOption.subscribe("tui/searchBPanel/view/ChildAgesView/Errors", function (controller, action) {
                if(controller !== partyCompSelectOption.widgetController) return;
				action = action ? "addClass" : "removeClass";
				dojo[action](partyCompSelectOption.domNode, "error");
			});

			partyCompSelectOption.subscribe("tui/searchBPanel/searchOpening", function (component) {
				if (component !== partyCompSelectOption) {
					partyCompSelectOption.hideList();
				}
			});

			// moved to base class
			/*on(partyCompSelectOption.selectDropdown, "focus", function (event) {
				dojo.addClass(partyCompSelectOption.selectDropdown, 'focus');
			});

			on(partyCompSelectOption.selectDropdown, "blur", function (event) {
				dojo.removeClass(partyCompSelectOption.selectDropdown, 'focus');
			});*/

		},

		addDropdownEventlistener: function () {
			var partyCompSelectOption = this;

			on(partyCompSelectOption.selectDropdown, "click", function () {
				var errorMessageModel = dojo.clone(partyCompSelectOption.searchPanelModel.searchErrorMessages.get("partyComp"));
				var key = (_.keys(errorMessageModel)[0]);
				delete errorMessageModel[key];
				partyCompSelectOption.searchPanelModel.searchErrorMessages.set("partyComp", errorMessageModel);
			});

			on(partyCompSelectOption.selectDropdown, "click", function () {
				var errorMessageModel = dojo.clone(partyCompSelectOption.searchPanelModel.searchErrorMessages.get("partyChildAges"));
				var key = (_.keys(errorMessageModel)[0]);
				delete errorMessageModel[key];
				partyCompSelectOption.searchPanelModel.searchErrorMessages.set("partyChildAges", errorMessageModel);
			});
		}
	});

	return tui.searchBPanel.view.PartyCompSelectOption;
}); 