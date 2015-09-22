	define("tui/widget/amendncancel/summary/PriceBreakSummaryPanel", [
  "dojo/_base/declare",
  "dojo/query",
  "dojo/dom-class",
  "dojo/text!tui/widget/amendncancel/summary/templates/PriceBreakSummaryPanel.html",
  "dojo/dom-construct",
  "dojo/parser",
  "tui/widget/mixins/Templatable",
  "tui/widget/common/ClickToolTip"

], function (declare, query, domClass, PriceBreakSummaryPanel, domConstruct, parser, Templatable) {

  return declare("tui.widget.amendncancel.summary.PriceBreakSummaryPanel", [tui.widget._TuiBaseWidget,Templatable], {

    tmpl: PriceBreakSummaryPanel,
    //controller: null,

    postCreate: function () {
      //this.controller = dijit.registry.byId("controllerWidget");
      //this.controller.registerView(this);
      this.renderWidget();
      this.inherited(arguments);
      //this.tagElements(query('a.changeFlight'),"changeFlights");
      //this.tagElements(query('.summaryAccordSwitch'),"Flights");
    },

    /*refresh: function (field, response) {
      this.jsonData = response;
      this.renderWidget();
    },*/

    renderWidget: function () {
    	var widget = this;
    	var html = widget.renderTmpl(widget.tmpl, widget);
      domConstruct.place(html, this.domNode, "only");
      parser.parse(this.domNode);
    }

  });
});