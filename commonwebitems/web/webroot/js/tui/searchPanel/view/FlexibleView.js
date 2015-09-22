define("tui/searchPanel/view/FlexibleView", [
	"dojo",
	"dojo/on",
	"tui/widget/_TuiBaseWidget"
], function (dojo, on) {


	dojo.declare("tui.searchPanel.view.FlexibleView", [tui.widget._TuiBaseWidget], {

        checkbox: null,

		// ----------------------------------------------------------------------------- methods

		postCreate: function () {
			var flexibleView = this;
			/*flexibleView.searchPanelModel.watch("flexible", function(name, oldValue, newValue) {
				//var flexibleView = this;
				flexibleView.select(newValue);
			});*/

            flexibleView.checkbox = dojo.query("[type='checkbox']", flexibleView.domNode)[0];

			on(flexibleView.checkbox, "click", function (event) {
				//var answer = dojo.attr(flexibleView.checkbox, "checked");
				flexibleView.setFlexible();
			});

			flexibleView.setFlexible();
			flexibleView.inherited(arguments);
            flexibleView.tagElement(flexibleView.domNode, "flexible");
		},

		/*set: function(name, oldValue, newValue) {
			var flexibleView = this;
			flexibleView.select(newValue);
		},*/

		setFlexible: function () {
			var flexibleView = this;
			var answer = dojo.attr(flexibleView.checkbox, "checked");
			flexibleView.searchPanelModel.set("flexible", answer);
		},

		select: function (name, oldAnswer, answer) {
			var flexibleView = this;
			if (dojo.attr(flexibleView.checkbox, "checked") === answer) {
				return;
			}
			if (answer) {
				dojo.attr(flexibleView.checkbox, "checked", "checked");
			} else {
				dojo.removeAttr(flexibleView.checkbox, "checked");
			}
		}
	});

	return tui.searchPanel.view.FlexibleView;
});
