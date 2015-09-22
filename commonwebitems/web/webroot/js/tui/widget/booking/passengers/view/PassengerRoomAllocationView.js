define("tui/widget/booking/passengers/view/PassengerRoomAllocationView", [
  "dojo/_base/declare",
  "dojo/query",
  "dojo/dom-attr",
  "dojo/dom-class",
  "dojo/Evented",
  "dojo/topic",
  "dojo/_base/lang",
  "dojo/dom-style",
  "tui/widget/_TuiBaseWidget",
  "dojox/dtl/_Templated",
  "tui/widget/mixins/Templatable",
  "dojo/text!tui/widget/booking/passengers/view/templates/PassengerRoomAllocationView.html",
  "tui/widget/booking/passengers/model/PassengerInfoModel",
  "tui/widget/booking/passengers/model/PassengerFormModel",
  "tui/widget/booking/passengers/validator/PassengerDetailValidator",
  "dojo/dom-construct",
  "dojo/on",
  "tui/widget/form/ValidationTextBox",
  "dojox/validate",
  "tui/widget/popup/Tooltips",
  "tui/widget/form/SelectOption",
  "dijit/form/CheckBox",
  "dijit/form/TextBox"

], function( declare, query, domAttr, domClass, Evented, topic,lang, domStyle, _TuiBaseWidget,dtlTemplate, Templatable,
		PassengerRoomAllocationView,PassengerInfoModel,PassengerFormModel, PassengerDetailValidator, domConstruct, on) {

return declare('tui.widget.booking.passengers.view.PassengerRoomAllocationView', [_TuiBaseWidget, dtlTemplate, Templatable,  Evented], {

	tmpl: PassengerRoomAllocationView,
	templateString: "",
	widgetsInTemplate : true,
	validator : null,
	store : null,
	buildRendering: function(){
		this.templateString = this.renderTmpl(this.tmpl, this);
		this.inherited(arguments);
	},

	postCreate: function() {
		this.validator = new PassengerDetailValidator();
	 	this.inherited(arguments);

	},
	attachEvents : function(){
		onquery()
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