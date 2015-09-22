define('tui/widget/form/ValidationTextBox', [
  "dojo/_base/declare",
  'dojo/query',
  "dojo/on",
  "dojo/dom-class",
  "dojo/_base/lang",
  "dojo/Evented",
  "dojo/dom-attr",
  "dojo/dom-construct",
  "dojo/dom-style",
  "dijit/form/ValidationTextBox",
  "tui/widget/popup/Tooltips"
], function( declare, query, on, domClass, lang, Evented, domAttr, domConstruct, domStyle, ValidationTextBox, Tooltips) {

  return declare('tui.widget.form.ValidationTextBox', [ValidationTextBox], {
    errorTextBoxNode : null,

    postCreate : function(){
      this.inherited(arguments);
      this.innerErrorBox = query(".dijitValidationInner", this.domNode)[0];
      domAttr.set(this.innerErrorBox,"value"," ");
    },

    displayMessage: function(/*String*/ message){
      // summary:
      //		Overridable method to display validation errors/hints.
      //		By default uses a tooltip.
      // tags:
      //		extension
      if(message){
        if(this.errorTextBoxNode !== null){
          domAttr.set(this.id+"_Error", "innerHTML", message);
        }else {
          this.errorTextBoxNode = "<div id= '"+this.id+"_Error' class='error-notation'>"+message+"</div>";
          domConstruct.place(this.errorTextBoxNode, this.domNode,"last");
        }
      }else{
        if(this.errorTextBoxNode !== null){
          domConstruct.destroy(this.id+"_Error");
          this.errorTextBoxNode = null;
        }
      }
    },

    serverValidator : function (message) {
      this._validate = this.validate;
      this._validator = this.validator;
      this.displayMessage(message);
      this.validator  = function () {
        return false;
      }
      this.validate =  function () {
        this.isFocused = true;
        this.state = "Error";
        var isValid = false;
        this.focusNode.setAttribute("aria-invalid", isValid ? "false" : "true");
        this._maskValidSubsetError = false; // we want the error to show up after a blur and refocus
        this.displayMessage(message);
        this.set("message", message);
        return false;
      };
      domClass.add(this.domNode, "dijitTextBoxError dijitValidationTextBoxError dijitError");
      var handle = this.on("focus", lang.hitch (this, function () {
        this.displayMessage("");
        domClass.remove(this.domNode, "dijitTextBoxError dijitValidationTextBoxError dijitError");
        this.validator = this._validator;
        this.validate = this._validate;
        this.validate();
        handle.remove();
      }));

    },

    _onBlur: function(){
      if(this.isValid()){
        //domAttr.set(this.innerErrorBox,"value", " ");
        domClass.remove(this.innerErrorBox.parentElement, "dijitValidationContainer");
        domAttr.set(this.innerErrorBox, "disabled", "disabled");
        domClass.add(this.innerErrorBox, "validStateTextBox");
		var trimedValue=dojo.trim(this.getValue());
        this.setValue(trimedValue);
      }
			domStyle.set(this.innerErrorBox, "display", "block");
      this.inherited(arguments);
    },

    _onFocus: function(/*String*/ by){
      //domAttr.set(this.innerErrorBox,"value"," ");
      domClass.add(this.innerErrorBox.parentElement, "dijitValidationContainer");
      domAttr.set(this.innerErrorBox, "disabled", "disabled");
      domClass.remove(this.innerErrorBox, "validStateTextBox");
      this.inherited(arguments);
    },

	//check this part ***********************************
	reset:function () {
		this._maskValidSubsetError = true;
		this._onFocus();
		this.inherited(arguments);

	}

  });
});