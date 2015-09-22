define("tui/widget/booking/passengers/view/PassengerTravelInsuranceView", [
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
  "dojo/text!tui/widget/booking/passengers/view/templates/PassengerTravelInsuranceView.html",
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
], function( declare, query, domAttr, domConstruct, domClass, Evented, topic,lang, domStyle, _TuiBaseWidget,dtlTemplate, Templatable,
		PassengerTravelInsuranceView,PassengerInfoModel,PassengerFormModel, PassengerDetailValidator, on) {

return declare('tui.widget.booking.passengers.view.PassengerTravelInsuranceView', [_TuiBaseWidget, dtlTemplate, Templatable,  Evented], {

	tmpl: PassengerTravelInsuranceView,
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

		if(this.genderSelect.selectNode.options[0].selected === true) {
			return false;
		}else if (this.titleSelect.getSelectedData().value === "") {
			return false;
		}else {
			for(var index = 0; index < this._attachPoints.length; index++){
				if(this[this._attachPoints[index]].state && this[this._attachPoints[index]].state === "Error"){
					return false;
				}else if(this[this._attachPoints[index]].state && this[this._attachPoints[index]].state === "Incomplete"){
					domClass.add(this.domNode, "dijitTextBoxError dijitValidationTextBoxError dijitError");
					return false;
				}
			}
		}

		return true;

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