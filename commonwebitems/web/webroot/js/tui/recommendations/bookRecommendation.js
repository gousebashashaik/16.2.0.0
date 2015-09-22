define("tui/recommendations/bookRecommendation", [
	"dojo",
	"dojo/dom-attr",
	"dojo/parser",
	"dojo/number",
	"dojo/cookie",
	"dojo/date/locale",
	"dojo/text!tui/recommendations/view/templates/bookRecommendationTmpl.html",
	"tui/recommendations/homeRecommendation",
	"tui/mvc/Controller",
	"tui/widget/mixins/Templatable"
	], function (dojo, domAttr, parser, number, cookie, dtLocale, tmpl) {

	dojo.declare("tui.recommendations.bookRecommendation", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable, tui.recommendations.homeRecommendation], {

		browseTmpl: tmpl,
		identifierFetchAjaxUrl: '',

		postCreate: function () {
			var rec = this;
			rec.identifierFetchAjaxUrl='/ws/baynoteBookflow';
			rec.inherited(arguments);
			rec.tmplPlaceHolder = dojo.query("UL.plist", rec.domNode)[0];
		},

		tmplPostRender: function(){
			var rec = this, carouselNode = dojo.query(".carousel",rec.domNode)[0];
			domAttr.set(carouselNode, "data-dojo-type", "tui.widget.carousels.Carousel");
			parser.parse({ rootNode: rec.domNode });
		}

	});

	return tui.recommendations.bookRecommendation;
});
