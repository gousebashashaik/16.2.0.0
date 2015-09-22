define("tui/flights/view/FlyingFromSelection", [
		"dojo",		
		"dojo/on",
		"tui/widget/form/SelectOption",
		"tui/widget/_TuiBaseWidget"], function (dojo, on) {
	
		dojo.declare("tui/flights/view/FlyingFromSelection", [tui.widget.form.SelectOption,tui.widget._TuiBaseWidget], {
			
			postCreate: function () {
				var flyingFromSelection = this;
				//var selection = new tui.widget.form.SelectOption();
				flyingFromSelection.inherited(arguments);
				flyingFromSelection.addDropdownEventlistener();
			},
			
			addDropdownEventlistener: function () {
				var partyCompSelectOption = this;

				on(partyCompSelectOption.selectDropdown, "click", function () {
					console.log("click")
				});

				on(partyCompSelectOption.selectDropdown, "click", function () {
					console.log("click")
				});
			}
			
		});
		return tui.flights.view.FlyingFromSelection;
});