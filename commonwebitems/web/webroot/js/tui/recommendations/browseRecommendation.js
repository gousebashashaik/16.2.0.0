define("tui/recommendations/browseRecommendation", [
	"dojo",
	"dojo/dom-attr",
	"dojo/parser",
	"dojo/number",
	"dojo/cookie",
	"dojo/text!tui/recommendations/view/templates/browseRecommendationTmpl.html",
	"tui/recommendations/homeRecommendation",
	"tui/widget/mixins/Templatable"
	], function (dojo, domAttr, parser, number, cookie, tmpl) {

	dojo.declare("tui.recommendations.browseRecommendation", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable, tui.recommendations.homeRecommendation], {

		browseTmpl: tmpl,
		identifierFetchAjaxUrl: '',

		postCreate: function () {
			var rec = this;
			rec.identifierFetchAjaxUrl='/ws/baynoteBrowseFlow';
			rec.inherited(arguments);
			rec.tmplPlaceHolder = dojo.query("UL.plist", rec.domNode)[0];
		},

		tmplPostRender: function(){
			var rec = this, carouselNode = dojo.query(".carousel",rec.domNode)[0];
			domAttr.set(carouselNode, "data-dojo-type", "tui.widget.carousels.Carousel");
			parser.parse({ rootNode: rec.domNode });
		}
	});

	return tui.recommendations.browseRecommendation;
});
