define ("tui/widget/customeraccount/mybookings/bookingValidator", [
													"dojo",
													"dojo/on",
													"dojo/cookie",
													"dojo/query",
													"dojo/has",
													"dojox/validate/web",
													"tui/validate/check",
													"dojo/_base/array",
													"dojo/dom-style",
													"dijit/focus",
													"dojo/dom-construct",
													"dojo/_base/xhr",
													"dojo/text!tui/widget/customeraccount/mybookings/templates/bookingListData.html",
													"dojo/topic",                                
													"tui/widget/_TuiBaseWidget", 
													"dojo/NodeList-traverse",
												
													"dojox/dtl", 
													"dojox/dtl/Context", 
													"dojox/dtl/tag/logic",
													"dijit/registry",
													"tui/dtl/Tmpl",
													"dojo/html",								
													"dojox/validate/us",
													"tui/widget/mixins/Templatable"													
													
							    			  ], function(dojo,on,  cookie, query, has, validate,check, arrayUtil, domStyle, focusUtil, domConstruct,xhr, bookingTmpl){
        
		dojo.declare("tui.widget.customeraccount.mybookings.bookingValidator", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable], {		
		
		errorMessage: null,
		maxlength: null,
		maxLengthMessage: null,
		validateFormat: null,
		promptMessage: null,
		mandatory: null,
		invalidMessage: null,
		
		postCreate: function() {
			var textNode = this;
			textNode.onBlurTextbox();
			textNode.removeErrorMessage();
			textNode.inherited(arguments);
		},
				
    	validator: function(val) {
		    var textNode = this; 
			var valid;
        	   if(textNode.validateFormat == "numeric"){ 
				   valid = /^\d+$/.test(val);
			   }        
			return valid;
        },		
				
        onBlurTextbox:function() {		
			var textNode = this;  
			textNode.removeErrorMessage();      	
			var currentNode, bookValue;
			var bookInputList = [ "book-year", "book-id" ];
			for(var i = 0; i < bookInputList.length; i++) {
				currentNode = dojo.byId(bookInputList[i]);
				query(currentNode).on('blur',function(){
				textNode.removeErrorMessage();
					bookValue = textNode.domNode.value;
					if(bookValue != "") {
						if(bookValue.length <= textNode.maxlength) {
							if(!textNode.validator(bookValue)) {
								textNode.showErrorMessage(textNode.invalidMessage);
								textNode.setSubmitFlag(false);
							} else {
								textNode.setSubmitFlag(true);							
							}
						} else {
							textNode.showErrorMessage(textNode.maxLengthMessage);
						}
					} else {
						textNode.setSubmitFlag(false);
					}
				});		
			}
        },  
		
        removeErrorMessage: function() { 
        	var textNode = this;
			var errorMessage = dojo.query("span.error");
			errorMessage[0].innerHTML = "";
	   	},
		
    	showErrorMessage:function(message) { 
    		var textNode = this; 
			textNode.removeErrorMessage();
			var errorMessage = dojo.query("span.error");
			errorMessage[0].innerHTML = message;			
			textNode.setSubmitFlag(false);        	
	   	},
		
		setSubmitFlag: function(state) {
			var widget = this;
			//var setNode = dojo.query("#" + widget.id);
			//setNode.attr("submitflag", state );
			//dojo.setAttr(setNode[0], "submitflag", state);
			dojo.attr(widget.domNode, "submitflag", state);
		}		
	
	});	
	return tui.widget.customeraccount.mybookings.bookingValidator;
});