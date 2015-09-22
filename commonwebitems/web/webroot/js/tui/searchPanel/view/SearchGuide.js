define("tui/searchPanel/view/SearchGuide", [
	"dojo",
	"dojo/on",
	'dojo/_base/fx',
	'dojo/fx',
	"tui/utils/TuiAnimations",
	"tui/widget/expand/SimpleExpandable",
	"tui/search/nls/Searchi18nable"
], function (dojo, on, baseFx, fx, tuiAnimations) {

	dojo.declare("tui.searchPanel.view.SearchGuide", [tui.widget.expand.SimpleExpandable, tui.search.nls.Searchi18nable], {


		postCreate: function () {
			var searchGuide = this;
			searchGuide.initSearchMessaging();
			searchGuide.inherited(arguments);
		},

		animatePill: function (label, dstElement, callback) {
			var text = dojo.trim(label.textContent || label.innerText);
			var srcPos = dojo.position(label, true);

			var pillElement = dojo.create("div", {
				"innerHTML": "<span>" + text + "</span>",
				"class": "flying-pill",
				"style": {"top": srcPos.y + "px", "left": srcPos.x + "px"}
			}, dojo.body(), "last");

			tuiAnimations.animateTo(pillElement, dstElement, null, null, [baseFx.fadeOut({
				node: pillElement,
				duration: 300,
				onEnd: function (node) {
					dojo.destroy(node);

				}
			})]);
			callback();
		}
	});

	return tui.searchPanel.view.SearchGuide;
}); 

