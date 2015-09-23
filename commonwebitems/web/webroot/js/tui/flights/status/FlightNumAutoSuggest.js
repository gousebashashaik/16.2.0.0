define("tui/flights/status/FlightNumAutoSuggest", [
	"dojo",
	"dojo/text!tui/widget/form/flights/templates/statusAutoCompTmp.html",
	"dojo/on",
	"tui/flights/status/FlightNumAutoComplete",
    "tui/widget/_TuiBaseWidget",
    "dijit/_TemplatedMixin",
    "dojo/NodeList-traverse",
    "dojo/html",
    "tui/widget/mixins/Templatable",
    "dojox/string/BidiComplex",
    "tui/widget/mixins/Scrollable",
    "tui/utils/CancelBlurEventListener"
    ], function (dojo,statusAutoCompTmp,on) {

    dojo.declare("tui.flights.status.FlightNumAutoSuggest", [tui.widget._TuiBaseWidget, dijit._TemplatedMixin, tui.widget.mixins.Scrollable, tui.utils.CancelBlurEventListener], {

        // ---------------------------------------------------------------- MultiFieldList Properties
    	 templateString: statusAutoCompTmp,

        autocomplete: tui.flights.status.FlightNumAutoComplete,

        autocompleteProps: null,

        textboxInput: null,

        defaultPlaceholderTxt: "Type Flight Number. i.e TOM 619",

        hasDownArrow: false,
        // ---------------------------------------------------------------- MultiFieldList methods

        postCreate: function () {
        	var _this = this;
        	_this.autocompleteProps = {};
            // summary:
            //		Method called after widget creation, to initalise defaults values.

        	 _this.createAutocomplete();
        	 _this.attachTextboxlistEventListeners();
        	 _this.updatePlaceholder()
        	 _this.createDownArrow();
        },
        createAutocomplete: function () {
            // summary:
            //		Creates autocomplete for _this.
            var _this = this;
            _this.autocompleteProps.elementRelativeTo = _this.domNode;
            _this.autocomplete = new _this.autocomplete(_this.autocompleteProps, _this.textboxInput);
        },

        attachTextboxlistEventListeners: function () {
            // summary:
            //		Add eventlistener to DOM elements.
            var _this = this;

            on(_this.domNode, "focus, click", function () {
                // clear timeout if present (prevents input blur)
                //if(multiFieldList.blurTimeout) clearTimeout(multiFieldList.blurTimeout);
            	_this.onTextboxInputFocus();
            });
            on(_this.textboxInput, "blur", function (evt) {
                // clear timeout if present (prevents input blur)
                //if(multiFieldList.blurTimeout) clearTimeout(multiFieldList.blurTimeout);
            	_this.onTextboxInputBlur();
            });

            on(document.body, "click", function (evt) {
            	if(dojo.query(evt.target).closest(".autoSuggest").length > 0) return;
            	_this.onTextboxInputBlur();
             });
        },
        onTextboxInputFocus: function(){
        	var _this = this;
        	dojo.style(_this.textboxInput,"display","block");
        	dojo.style(_this.placeholder,"display","none");
        	dojo.style(_this.containerNode,"border-left","5px solid #FCB712");

        	_this.textboxInput.focus();
        },
        onTextboxInputBlur: function(){
        	var _this = this;
        	dojo.style(_this.containerNode,"border-left","0");

        	if(dojo.hasClass(_this.containerNode,"error")) {
        		_this.autocomplete.unSelect();
        		_this.autocomplete.listShowing = false;
        	}

        	if(_this.autocomplete.getSelectedData() || _this.autocomplete.listShowing) return;

        	dojo.style(_this.textboxInput,"display","none");
        	dojo.style(_this.placeholder,"display","block");
        	_this.textboxInput.value = ""
        	dojo.byId("errorpopup").style.display = "none";
    		dojo.query(".focusNode",_this.domNode).removeClass("error");
    		dojo.query("input",_this.domNode).removeClass("error");


        },
        updatePlaceholder: function () {
            var _this = this;
            var text = _this.defaultPlaceholderTxt;
            dojo.html.set(_this.placeholder, text);
        },
        createDownArrow: function(){
        	var _this = this;

        	if(!_this.hasDownArrow){
        		dojo.style(_this.downArrow,"display","none");
        		dojo.style(_this.containerNode,"width","96%");
        	} else {
        		on(_this.downArrow,"click",function(){
        			var flightNum = dijit.byId("flightNumber").displayedValue.replace(" ","");
                	var arrAirPortCode = dijit.byId("flyToStat").getValue();
        			//_this.autocomplete.targetURL =  dojoConfig.paths.webRoot + "/departureAutoSuggestData?arrAirPortCode="+ arrAirPortCode +"&flightNumber="+ flightNum +"&event=departure";
        			if(_this.autocomplete.isShowing()){
        				_this.autocomplete.clearList()
        				_this.autocomplete.query();
        			} else {
        				_this.autocomplete.query();
        			}
        		})
        	}
        },
        reset: function(){
        	var _this = this;
        	_this.autocomplete.unSelect();
        	_this.onTextboxInputBlur();
        },
        getValue: function(){
        	var _this = this;
        	return  _this.autocomplete.getSelectedData() == null ? "" : _this.autocomplete.getSelectedData().value;
        },
        getLabel: function(){
        	var _this = this;
        	return  _this.autocomplete.getSelectedData() == null ? "" : _this.autocomplete.getSelectedData().key;
        }


    });

    return tui.flights.status.FlightNumAutoSuggest;
});