define("tui/searchResults/view/Tooltips", [
	"dojo",
    "dojo/dom-style",
	"dojo/text!tui/searchResults/view/templates/tooltipTmpl.html",
	"tui/widget/popup/Tooltips"
], function (dojo, domStyle, tooltipTmpl) {

	dojo.declare("tui.searchResults.view.Tooltips", [tui.widget.popup.Tooltips], {

		// ---------------------------------------------------------------- properties

		tmpl: tooltipTmpl,

		floatWhere: "position-top-center",

		text: null,

		title: null,

		width: null,

		listItems: null,

        listClass: null,

        hideWidget: function () {
            var tooltips = this;
            if (tooltips.popupDomNode) domStyle.set(tooltips.popupDomNode, "display", "none");
        }

	});

	return tui.searchResults.view.Tooltips;
});