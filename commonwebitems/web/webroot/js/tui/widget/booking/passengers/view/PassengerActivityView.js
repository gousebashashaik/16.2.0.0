define("tui/widget/booking/passengers/view/PassengerActivityView", [
  "dojo/_base/declare",
  "dojo/query",
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
  "dojo/text!tui/widget/booking/passengers/view/templates/PassengerActivityView.html",
  "tui/widget/booking/passengers/model/PassengerInfoModel",
  "tui/widget/booking/passengers/model/PassengerFormModel",
  "tui/widget/booking/passengers/validator/PassengerDetailValidator",
  "dojo/on",
  "tui/widget/form/ValidationTextBox",
  "dojox/validate",
  "tui/widget/popup/Tooltips",
  "tui/widget/form/SelectOption",
  "dijit/form/CheckBox",
  "dijit/form/TextBox"
], function( declare, query, domAttr, domConstruct, domClass, Evented, topic,lang, domStyle,  _TuiBaseWidget,dtlTemplate, Templatable,
		PassengerActivityView,PassengerInfoModel,PassengerFormModel, PassengerDetailValidator, on) {

return declare('tui.widget.booking.passengers.view.PassengerActivityView', [_TuiBaseWidget, dtlTemplate, Templatable,  Evented], {

	tmpl: PassengerActivityView,
	templateString: "",
	widgetsInTemplate : true,
	validator : null,
	buildRendering: function(){
		this.templateString = this.renderTmpl(this.tmpl, this);
		this.inherited(arguments);
	},

	 postCreate: function() {

	 	this.inherited(arguments);

	 },
	attachEvents : function(){

	},

	selectValidate : function(selectBox, divBlock){

	},


	isValid: function() {



	},

	displayMessagesForSelect: function(message, focused, selectBox){


	}

	});
});