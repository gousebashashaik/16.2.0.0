define("tui/widget/Pills", ["dojo", "tui/widget/_TuiBaseWidget", "tui/widget/mixins/Tabbable"], function(dojo) {

	dojo.declare("tui.widget.Pills", [tui.widget._TuiBaseWidget, tui.widget.mixins.Tabbable], {
		
		// summary:
		//		Class for creating a pills.
		//
		// description:
		//		Class defines the behaviour needed to create a pills widget.
		//
		// @author: Maurice Morgan.

		// Value of current selected pill.
		pillvalue : null,

		// selector for tab headers.
		tabSelector : ".pills li",

		//---------------------------------------------------------------- methods

		postCreate : function() {
			// summary:
			//		Attach event listeners to wigdet elements.
			
			var pills = this;
			pills.attachTabbableEventListeners();
		},

		showTab : function(/*DOM pill*/ selectedPill) {
			// summary:
			//		Display content tab for pill or set value.
			
			var pills = this;
			var link = selectedPill;

			if (link.tagName.toLowerCase() !== 'a') {
				link = dojo.query(link).query("a")[0];
			}

			var value = dojo.attr(link, "data-dojo-value");
			if (!value) {
				return pills.inherited(arguments);
			}
			
			pills.highlightTabHeading(selectedPill)
			pills.setValue(selectedPill, value);
			return null;
		},

		setValue : function(/*DOM pill*/ selectedPill, /*String*/ value) {
			// summary:
			//		Set value for pill.
			
			var pills = this;
			pills.pillvalue = (value && value[1] !== "") ? value : dojo.getAttr(selectedPill, "value");
		}
	})

	return tui.widget.Pills;
})