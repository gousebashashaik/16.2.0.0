define("tui/widget/booking/yourseats/view/TermsAndConditionsView", [
  "dojo/_base/declare",
  "dojo/query",
  "dojo/dom",
  "dojo/dom-attr",
  "dojo/dom-construct",
  "dojo/dom-class",
  "dojo/Evented",
  "dojo/topic",
  "dojo/_base/lang",
  "dojo/dom-style",
  "tui/widget/_TuiBaseWidget",
  "dojox/dtl/_Templated",
  "tui/widget/mixins/Templatable",
  "dojo/text!tui/widget/booking/yourseats/view/Templates/TermsAndConditionsView.html",
  "tui/widget/booking/constants/BookflowUrl",
  "dojo/on",
  "dojo/_base/json",
  "tui/widget/form/SelectOption"


], function (declare, query, dom, domAttr, domConstruct, domClass, Evented, topic, lang, domStyle, _TuiBaseWidget,
             dtlTemplate, Templatable, TermsAndConditionsView,BookflowUrl, on, jsonUtil) {

  return declare('tui.widget.booking.yourseats.view.TermsAndConditionsView',
    [_TuiBaseWidget, dtlTemplate, Templatable, Evented], {

      tmpl: TermsAndConditionsView,
      templateString: "",
      widgetsInTemplate: true,


    buildRendering: function () {
      this.templateString = this.renderTmpl(this.tmpl, this);
      delete this._templateCache[this.templateString];
      this.inherited(arguments);
    },

    postCreate: function () {
    	
    }
    
  });
});