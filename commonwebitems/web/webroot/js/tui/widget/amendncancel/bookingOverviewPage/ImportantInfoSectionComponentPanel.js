define("tui/widget/amendncancel/bookingOverviewPage/ImportantInfoSectionComponentPanel", [
  "dojo/_base/declare",
  "dojo/dom-construct",
  "dojo/parser",
  "dojo/text!tui/widget/amendncancel/bookingOverviewPage/templates/ImportantInfoSectionComponentPanel.html",
  "tui/widget/_TuiBaseWidget",
  "dojox/dtl/_Templated",
  "tui/widget/mixins/Templatable",
  "tui/widget/expand/Expandable"
  ], function (declare,domConstruct,parser,ImportantInfoSectionComponentPanel, _TuiBaseWidget, dtlTemplate, Templatable) {

  return declare("tui.widget.amendncancel.bookingOverviewPage.ImportantInfoSectionComponentPanel",
    [_TuiBaseWidget, dtlTemplate, Templatable], {

      tmpl: ImportantInfoSectionComponentPanel,
      templateString: "",
      widgetsInTemplate: true,

      buildRendering: function () {
        this.templateString = this.renderTmpl(this.tmpl, this);
        delete this._templateCache[this.templateString];
        this.inherited(arguments);
      },

      postMixInProperties: function(){
    	  this.inherited(arguments);
      },

      postCreate: function () {
           this.inherited(arguments);
      }

    });
});