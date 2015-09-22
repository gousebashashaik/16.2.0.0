define("tui/widget/booking/inflightmeals/view/InflightMealSelection", [
  "dojo/_base/declare",
  "dojo/dom-construct",
  "dojo/parser",
  "dojo/text!tui/widget/booking/inflightmeals/view/templates/InflightMealSelectionTmpl.html",
  "dojo/html",
  "dojo/Evented",
  "tui/widget/_TuiBaseWidget",
  "dojox/dtl/_Templated",
  "tui/widget/mixins/Templatable",
  "tui/widget/expand/Expandable"
  ], function (declare,domConstruct,parser,InflightMealSelectionTmpl, html, Evented, _TuiBaseWidget, dtlTemplate, Templatable, Expandable) {

  return declare("tui.widget.booking.inflightmeals.view.InflightMealSelection",
    [_TuiBaseWidget, dtlTemplate, Templatable, Evented], {

      tmpl: InflightMealSelectionTmpl,
      templateString: "",
      widgetsInTemplate: true,

      buildRendering: function () {
    	 this.siteObj = dojoConfig.site;
        this.templateString = this.renderTmpl(this.tmpl, this);
        delete this._templateCache[this.templateString];
        this.inherited(arguments);
      },

      postCreate: function () {

           this.inherited(arguments);

      }





    });
});