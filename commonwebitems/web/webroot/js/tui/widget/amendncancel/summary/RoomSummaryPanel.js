define("tui/widget/amendncancel/summary/RoomSummaryPanel", [
  "dojo/_base/declare",
  "dojo/query",
  "dojo/dom-class",
  "dojo/text!tui/widget/amendncancel/summary/templates/RoomSummaryPanel.html",
  "dojo/dom-construct",
  "dojo/parser",
  "tui/widget/mixins/Templatable"
], function (declare, query, domClass, RoomSummaryPanel, domConstruct, parser, Templatable) {
  return declare("tui.widget.amendncancel.summary.RoomSummaryPanel", [tui.widget._TuiBaseWidget,Templatable], {
    tmpl: RoomSummaryPanel,
    //controller : null,

    postCreate: function () {
      //this.controller = dijit.registry.byId("controllerWidget");
      //this.controller.registerView(this);
      this.renderWidget();
      this.inherited(arguments);
      //this.tagElements(query('a.changeRoom'),"changeRoomBoard");
      //this.tagElements(query('.summaryAccordSwitch'),"RoomandBoard");
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