define("tui/widget/amendncancel/summary/ExtrasSummaryPanel", [
  "dojo/_base/declare",
  "dojo/query",
  "dojo/dom-class",
  "dojo/text!tui/widget/amendncancel/summary/templates/ExtrasSummaryPanel.html",
  "dojo/dom-construct",
  "dojo/parser",
  "tui/widget/mixins/Templatable"
], function (declare, query, domClass, ExtrasSummaryPanel, domConstruct, parser, Templatable) {

  return declare("tui.widget.amendncancel.summary.ExtrasSummaryPanel", [tui.widget._TuiBaseWidget,Templatable], {
    tmpl: ExtrasSummaryPanel,
    postCreate: function () {
      this.renderWidget();
      this.inherited(arguments);
    },
    postMixInProperties: function(){
		  this.extraFacilityData = this.jsonData.packageViewData.extraFacilityCategoryViewData;
		  this.staticData = this.jsonData.manageBookingSummaryContentViewData.manageBookingSummaryContentMap;
    },
    renderWidget: function () {
			var widget = this;
			var html = widget.renderTmpl(widget.tmpl, widget);
			domConstruct.place(html, this.domNode, "only");
			parser.parse(this.domNode);
    }

  });
});