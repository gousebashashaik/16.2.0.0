define("tui/widget/booking/passengers/view/DiscountTextBoxValidation", [
  "dojo",
  "dojo/dom-class",
  "dojo/dom-attr",
  "dojo/dom-style",
  "tui/widget/_TuiBaseWidget",
  "tui/widget/form/ValidationTextBox"
],

    function (dojo,domClass,domAttr,domStyle, _TuiBaseWidget) {
      return dojo.declare('tui.widget.booking.passengers.view.DiscountTextBoxValidation',
          [tui.widget.form.ValidationTextBox], {

        
    	  _onBlur: function(){
    	      if(this.isValid()){
    	        //domAttr.set(this.innerErrorBox,"value", " ");
    	        //domClass.remove(this.innerErrorBox.parentElement, "dijitValidationContainer");
    	        domAttr.set(this.innerErrorBox, "disabled", "disabled");
    	        domClass.add(this.innerErrorBox, "validStateTextBox");
    	      }
    				domStyle.set(this.innerErrorBox, "display", "block");
    				if(!this.isValid() && this.get('value') == ""){
		 this._setBlurValue()
	}	
    	      
    	    }
    })

});