define("tui/widget/amendncancel/summary/TotalPriceSummaryPanel", [
	  "dojo",
	  "dojo/_base/declare",
	  "dojo/query",
	  "dojo/dom-class",
	  "dojo/on",
	  'dojo/dom',
	  "dojo/_base/lang",
	  "dojo/text!tui/widget/amendncancel/summary/templates/TotalPriceSummaryPanel.html",
	  "dojo/dom-construct",
	  "dojo/_base/xhr",
	  "dojo/dom-attr",
	  "dojo/parser",
	  "dojo/dom-style",
	  "tui/widget/booking/constants/TotalPrice",
	  "tui/widget/common/ClickToolTip"
      ], function (dojo,declare, query, domClass,on,dom,lang,TotalPriceSummaryPanel, domConstruct,xhr,domAttr, parser,domStyle,TotalPrice) {

	return declare("tui.widget.amendncancel.summary.TotalPriceSummaryPanel", [tui.widget._TuiBaseWidget], {

		tmpl: TotalPriceSummaryPanel,
		//controller: null,

		postCreate: function () {
			//this.controller = dijit.registry.byId("controllerWidget");
			//this.controller.registerView(this);
			this.renderWidget();
			this.inherited(arguments);
			domAttr.set(dom.byId('newprice'), "innerHTML", TotalPrice.updatetotalprice(this.jsonData.packageViewData.currencyAppendedRoundUpTotalCost,'poundSize','number','decimal'));

		},

/*		refresh: function (field, response) {
			this.jsonData = response;
			this.renderWidget();
			domAttr.set(dom.byId('newprice'), "innerHTML", TotalPrice.updatetotalprice(this.jsonData.packageViewData.currencyAppendedRoundUpTotalCost,'poundSize','number','decimal'));

		},
*/
		renderWidget: function () {
			var template = new dojox.dtl.Template(this.tmpl),
			context = new dojox.dtl.Context(this),html;
			html = template.render(context);
			domConstruct.place(html, this.domNode, "only");
			parser.parse(this.domNode);
		}

	});
});