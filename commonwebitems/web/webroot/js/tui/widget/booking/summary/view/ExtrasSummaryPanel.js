define("tui/widget/booking/summary/view/ExtrasSummaryPanel", [
  "dojo/_base/declare",
  "dojo/query",
  "dojo/dom-class",
  "dojo/text!tui/widget/booking/summary/view/templates/ExtrasSummaryPanel.html",
  "dojo/dom-construct",
  "dojo/parser",
  "tui/widget/mixins/Templatable",
  "dojo/dom-style"
], function (declare, query, domClass, ExtrasSummaryPanel, domConstruct, parser, Templatable,domStyle) {

  return declare("tui.widget.booking.summary.view.ExtrasSummaryPanel", [tui.widget._TuiBaseWidget,Templatable], {
    tmpl: ExtrasSummaryPanel,
    controller: null,

    postCreate: function () {
      this.controller = dijit.registry.byId("controllerWidget");
      this.controller.registerView(this);

      this.renderWidget();
      this.inherited(arguments);
      this.enableAccord(jsonData);

      this.tagElements(query('a.changeExtras'),"changeExtras");
      this.tagElements(query('.summaryAccordSwitch'),"Extras");
    },
  enableAccord : function(jsonData){
	 var accordVar = query(".noExtra")[0];
	 var extras = _.filter(this.jsonData.packageViewData.extraFacilityCategoryViewData, function(category){ return category.extraFacilityGroupCode == "PACKAGE"; });
	 var insData = _.filter(this.jsonData.packageViewData.extraFacilityCategoryViewData, function(category){ return category.extraFacilityGroupCode == "INSURANCE"; });
    if((! _.isEmpty(extras)) || (! _.isEmpty(insData)) ){
    	if(accordVar.style.display !== ""){
    		domStyle.set(accordVar, "display", "block");
    	}
    }else{
    	 domStyle.set(accordVar, "display", "none");
    	 }
    },
    refresh: function (field, response) {
      this.jsonData = response;
     this.enableAccord(response);
      this.renderWidget();

    },

    renderWidget: function () {
    	var widget = this;
    	var html = widget.renderTmpl(widget.tmpl, widget);
      domConstruct.place(html, this.domNode, "only");
      parser.parse(this.domNode);
    }

  });
});