define('tui/widget/common/autocomplete/Input', [
  'dojo',
  'dojo/on',
  'dojo/dom-attr',
  'dijit/focus',
  "dojo/dom-class"], function(dojo, on, domAttr, focusUtil, domClass) {

  function Input(dom, entityName, inputPlaceHolder, onError, onValid, keyUp, onClick, onBlur, onReset, entityStore) {
    this.dom = dom;

    entityStore.query().observe(function() {
      entityStore.query().length > 0 ? domAttr.set(dom, 'placeholder', 'Add More ' + entityName) : domAttr.set(dom, 'placeholder', inputPlaceHolder);
    });


    function handleInput(value, validfn, invalidfn) {
    	 !_.isEmpty(value) && /[^a-zA-Z0-9!@#$%^&*()_+\-=\[\]{};':"\\|,.<>?~` ]/.test(value) ? invalidfn() : validfn();
    }

    function onInvalidInput() {
      onError(dom.value);
    }

    function onValidInput() {
      _.when(dom.value.length > 2, function() {
        onValid(dom.value);
      });
    }

    on(dom, 'keyup', function(e) {
      dojo.stopEvent(e);
      keyUp();
      if (dom.value === '') {
    	 var error_temp = "";
    	var finder = dojo.byId('finder').value;
  		if(finder == "holidayfinder")
  			{			
  			error_temp = dojo.query('.row.error-msg')[0] ;
  	    	dojo.byId("holidaySearch").disabled= false;
	        dojo.byId('adults').disabled = false;
	        dojo.byId("children").disabled = false;
  			}
  		else if(finder == "checkprice")
  			{				
  			 	error_temp = dojo.query('#errorGetprice')[0];
  			 	dojo.byId("priceSearch").disabled= false;
                dojo.byId('priceadults').disabled = false;
                dojo.byId('pricechildren').disabled = false;
  			}
    	domClass.remove(error_temp, 'show');
    	
        onReset();
      } else {
      handleInput(dom.value, onValidInput, onInvalidInput);
      }
    });

    on(dom, 'click', function(e) {
      dojo.stopEvent(e);
      onClick();
    });

    on(dom, 'blur', function(e) {
      onBlur();
    });

    return this;
  }

  Input.prototype.clear = function() {
    this.dom.value = '';
  };

  Input.prototype.focus = function() {
    focusUtil.focus(this.dom);
  };

  Input.prototype.blur = function() {
    this.dom.blur();
  };


  return Input;
});


