define(["dojo/_base/declare",
        "dijit/form/FilteringSelect",
        "dojo/_base/lang",
        "tui/widget/mixins/Scrollable",
        "dojo/query",
        "dojo/dom-geometry",
        "dojo/dom-style",
        "dojo/NodeList-traverse"
       ],function(declare,FilteringSelect,lang,Scrollable,query,domGeometry,domStyle){
        declare("tui.widget.form.flights.FilteringSelect",[FilteringSelect,Scrollable], {

        	/** Properties Start**/

            required: false, // if this is true, this field is required

            autoComplete: false, //if true selects first option in the dropdown

            doValidation: false, // overriden method for validate, whether todo validation or not

            scrollable:true,

            scrollerSelector:"",

            minKeyCount: 3, // num of keys to start search

            invalidMessage: "", // to show any invalid messages

            missingMessage:"", // to show a message when this field is mandatory

            searchDelay : null, //Delay after user types in

            queryExpr: "*${0}*",

            /** Properties End **/


            constructor: function(){
                console.log("sample module loaded");
            },

            postCreate: function(){
            	 this.inherited(arguments);
            },

            /*
             * _setBlurValue: to set a default value on blur of the text box, default value will be first option of the dropdown
             */

            _setBlurValue: function(){
                return;
            },

            /*
             * _startSearchFromInput: this method will be called oninput of the widget textbox
             */

            _startSearchFromInput: function(){
                if ((dojo.trim(this.focusNode.value.replace(" ","")).length < this.minKeyCount)) {
                    this.closeDropDown();
                    return;
                }
                this.inherited(arguments);
            },

            /*
             * validate: a validation method for filtering select widget.
             */

            validate: function(){
                if(this.doValidation){
                    this.inherited(arguments);
                } else {
                    return;
                }
            },

            /*
             * _startSearch: inherited from FilteringSelect base class, all search starts from here after user inputs the data
             */

            _startSearch: function(){
            	var _this = this;
            	_this.inherited(arguments);

            },

            _showResultList: function(){
            	var _this = this;
            	_this.inherited(arguments);
            	_this.attachScrollPanel();
            },

            /*
             * attachScrollPanel : to attach the scroll panel
             */

            attachScrollPanel: function(){
            	var _this = this;
            	//setTimeout(function(){
            		if(_this._started == undefined && query(".dijitMenuItem",_this.dropDown.domNode).length < 3){
            			return
            		};
        				_this.addScrollerPanel(_this.dropDown.domNode.parentNode)
        				_this.scrollPanels[0].updateScrollerPosition(0);
            			_this.adjustHeight();
            			_this.scrollPanels[0].update();

            	//},570)

            },

            /*
             * adjustHeight: to adjust the height for scroller view when height of the dijitpopup node is < 200
             */

            adjustHeight: function(){
            	var _this = this;
            	var viewPortHeight = domGeometry.position(query(".viewport",_this.dropDown.domNode.parentNode.parentNode)[0]).h;
            	//var dijitPopupHeight = domGeometry.position(query(".dijitComboBoxMenu",_this.dropDown.domNode.parentNode.parentNode)[0]).h;
            	var dijitPopupItemLength = query(".dijitReset.dijitMenuItem",_this.dropDown.domNode.parentNode.parentNode).length;

            	var dijitMenuItemHeight = domGeometry.position(query(".dijitReset.dijitMenuItem",_this.dropDown.domNode.parentNode.parentNode)[0]).h
            	var dijitPopupHeight;
            	if(dijitPopupItemLength > 2 ){
            		dijitPopupHeight = dijitPopupItemLength * dijitMenuItemHeight;
            		dojo.style(_this.dropDown.containerNode,"height",dijitPopupHeight + 'px');
            	} else {
            		dijitPopupHeight = domGeometry.position(query(".dijitComboBoxMenu",_this.dropDown.domNode.parentNode.parentNode)[0]).h;
            	}
            	console.log(viewPortHeight,dijitPopupHeight);
            	if(dijitPopupHeight < viewPortHeight || viewPortHeight < dijitPopupHeight){
            		query(".viewport",_this.dropDown.domNode.parentNode.parentNode).style("height",dijitPopupHeight+"px");
            		if(dijitPopupHeight < 375 && dojo.isIE > 7) query(".track",_this.dropDown.domNode.parentNode.parentNode).style("display","none");
            	} else if(dijitPopupHeight > 200){
            		query(".viewport",_this.dropDown.domNode.parentNode.parentNode).style("height","200px");
            	}
            }


    })
    return tui.widget.form.flights.FilteringSelect;
})