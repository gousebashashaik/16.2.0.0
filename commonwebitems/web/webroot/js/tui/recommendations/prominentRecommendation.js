define("tui/recommendations/prominentRecommendation", [
	"dojo",
	"dojo/dom-attr",
	"dojo/_base/connect",
	"dojo/parser",
	"dojo/number",
	"dojo/cookie",
	"dojo/text!tui/recommendations/view/templates/prominentRecommendation.html",
	"tui/widget/mixins/Templatable"
	], function (dojo, domAttr, connect, parser, number, cookie, tmpl) {

	dojo.declare("tui.recommendations.prominentRecommendation", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable], {

		browseTmpl: tmpl,
		identifierFetchAjaxUrl: '',

		postCreate: function () {
			var rec = this;
			rec.identifierFetchAjaxUrl='/ws/baynoteBrowseFlow';
			rec.inherited(arguments);

			rec.tmplPlaceHolder = dojo.query(".prominent-list", rec.domNode)[0];
			  connect.subscribe("tui/recommendations/prominentRecommendation/getRecommendation", function(message){
				     var recData = JSON.parse(JSON.stringify(message));

				     if(recData.accomodationDatas.length < 3) {
				    	 dojo.setStyle(rec.domNode, {"display": "none"});
				    	 dojo.setStyle(dojo.query(".recsDefaultHomePage"), {"display": "none"});
				    }
				     else{
				     recData.accomodationDatas = recData.accomodationDatas.splice(0,1);
				     rec.jsonData = recData;
	    			 var html = rec.renderTmpl(rec.browseTmpl, rec);
	    			 dojo.place(html, rec.tmplPlaceHolder, 'only');
//	    			 rec.tmplPostRender();
				     }
			  });
		}

	});

	return tui.recommendations.prominentRecommendation;
});







