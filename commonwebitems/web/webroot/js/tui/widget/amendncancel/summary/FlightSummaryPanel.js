define("tui/widget/amendncancel/summary/FlightSummaryPanel", [
  "dojo/_base/declare",
  "dojo/query",
  "dojo/dom-class",
  "dojo/text!tui/widget/amendncancel/summary/templates/FlightsSummaryPanel.html",
  "dojo/dom-construct",
  "dojo/parser",
  "tui/widget/mixins/Templatable",
  "tui/widget/common/ClickToolTip"

], function (declare, query, domClass, FlightsSummaryPanel, domConstruct, parser, Templatable) {

  return declare("tui.widget.amendncancel.summary.FlightSummaryPanel", [tui.widget._TuiBaseWidget,Templatable], {

    tmpl: FlightsSummaryPanel,
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