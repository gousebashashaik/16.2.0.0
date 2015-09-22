define("tui/widget/booking/passengers/view/PassengerExcursionView", [
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
  "dojo/text!tui/widget/booking/passengers/view/templates/PassengerExcursionView.html",
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
		PassengerExcursionView,PassengerInfoModel,PassengerFormModel, PassengerDetailValidator, on) {

return declare('tui.widget.booking.passengers.view.PassengerExcursionView', [_TuiBaseWidget, dtlTemplate, Templatable,  Evented], {

	tmpl: PassengerExcursionView,
	templateString: "",
	widgetsInTemplate : true,
	validator : null,
	buildRendering: function(){
		this.templateString = this.renderTmpl(this.tmpl, this);
		this.inherited(arguments);
	},

	 postCreate: function() {
		this.validator = new PassengerDetailValidator();

	 	this.inherited(arguments);

	 },
	attachEvents : function(){

	},

	selectValidate : function(selectBox, divBlock){

	},


	displayMessagesForSelect: function(message, focused, selectBox){

		if(message && focused){
			if(selectBox.errorTextBoxNode && selectBox.errorTextBoxNode !== null ){
				domAttr.set(selectBox.id+"_Error", "innerHTML", message);
			}else {
				selectBox.errorTextBoxNode = "<div id= '"+selectBox.id+"_Error' class='error-notation'>"+message+"</div>";
				domConstruct.place(selectBox.errorTextBoxNode, this.selectBlock,"after");
			}
		}else{
			if(selectBox.errorTextBoxNode && selectBox.errorTextBoxNode !== null){
				domConstruct.destroy(selectBox.id+"_Error");
				selectBox.errorTextBoxNode = null;
			}
		}
	}


	});
});