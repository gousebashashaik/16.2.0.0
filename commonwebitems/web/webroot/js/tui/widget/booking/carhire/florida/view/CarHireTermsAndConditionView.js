define("tui/widget/booking/carhire/florida/view/CarHireTermsAndConditionView", [
  "dojo/_base/declare",
  "dojo/query",
  "dojo/dom-attr",
  "dojo/dom-construct",
  "dojo/dom-class",
  "dojo/Evented",
  "dojo/topic",
  "dojo/_base/lang",
  "dojo/dom-style",
  "tui/widget/booking/constants/BookflowUrl",
  "tui/widget/_TuiBaseWidget",
  "dojox/dtl/_Templated",
  "tui/widget/mixins/Templatable",
  "dojo/text!tui/widget/booking/carhire/florida/view/template/CarHireTermsAndConditionView.html",
  "tui/widget/booking/passengers/view/PassengerDpnOverlay",
  "dojo/on",
  "dojo/_base/json",
  "tui/widget/form/SelectOption"


], function (declare, query, domAttr, domConstruct, domClass, Evented, topic, lang, domStyle, BookflowUrl,_TuiBaseWidget, dtlTemplate, Templatable, PassengerDpnView, PassengerDpnOverlay,on, jsonUtil) {

  return declare("tui.widget.booking.carhire.florida.view.CarHireTermsAndConditionView", [_TuiBaseWidget, dtlTemplate, Templatable, Evented], {

      tmpl: PassengerDpnView,
      templateString: "",
	  widgetsInTemplate : true,
	  templateView:false,

    buildRendering: function () {
    	this.templateString = this.renderTmpl(this.tmpl, this);
		delete this._templateCache[this.templateString];
		this.inherited(arguments);
    },

    postCreate: function () {
      this.inherited(arguments);
    }



  });
});