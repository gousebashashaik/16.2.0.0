define ("tui/widget/customeraccount/popup/TextBox", [
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
													"dojo/topic",                                
													"tui/widget/_TuiBaseWidget", 
													"dojo/NodeList-traverse",
												
													"dojox/dtl", 
													"dojox/dtl/Context", 
													"dojox/dtl/tag/logic",
													"dijit/registry",
													"tui/dtl/Tmpl",
													"dojo/html",								
													"dojox/validate/us"
													
													
													
							    			  ], function(dojo,on,  cookie, query, has, validate,check, arrayUtil, domStyle, focusUtil, domConstruct){
        
		dojo.declare("tui.widget.customeraccount.popup.TextBox", [tui.widget._TuiBaseWidget], {		
				
		promptMessage: null,
		invalidMessage: null,
		mandatory: null,
		maximumLength: null,
		maxLengthMessage: null,
		submitFlag: false,
		specialCharValidation: false,
		
		postCreate: function() {
			var textNode = this;
			textNode.inherited(arguments);	
			textNode.onBlurTextbox();
			textNode.onSubmitEvent();
		},
    	onBlurTextbox:function(){
        	var textNode = this;
 			var id = "#"+this.id;
 			var childs = query(id).children();
 			var field = childs[1];			 
 			var fieldId = "#"+childs[1].id;
 			
 			query(field).on('blur',function(){
 				
 				if(textNode.mandatory == "true"){ 
					var val = field.value; //three
					var result = textNode.validator(val);   
					textNode.removeErrorMessage();
					if(!result){
						if(!val.length){
							var message = textNode.promptMessage;	
						}
						else{
							var message = textNode.invalidMessage;
						}
						textNode.showErrorMessage(message);
					}else if(result == true){
						if(val.length > textNode.maximumLength){
							var message = textNode.maxLengthMessage + " " + textNode.maximumLength + " characters";
							textNode.showErrorMessage(message);
						}
					}
				}
 				
 			});
        },
		onSubmitEvent: function(){
			var textNode = this;
			query("input[type='submit']").on("click", function(e){       		    
				textNode.submitFlag = true;       		
       	    });
		},
    	validator: function(val) { 
		   var textNode = this;
           if(val && validate.isText(val)){ 
        	   
			   if(textNode.specialCharValidation == "true"){
			   onlyLetters = /^[a-zA-Z]+([\s-]?[a-zA-Z]*)$/.test(val);
			   }
			   else{
			   onlyLetters = /^[a-zA-Z]+$/.test(val);
			   }
        	   return onlyLetters;
           }     
        },
        
        onFocus: function() {
           
        },     
    	
        removeErrorMessage: function(){ //five
        	
        	var textNode = this;


            var id = "#"+textNode.id;
			var childs = query(id).children();
			var field = childs[1];			 
			var fieldId = childs[1].id;

	
        	var id1 = fieldId+"Error_1";
        	var id2 = fieldId+"Error_2";
        	var id3 = fieldId+"Error_3";        	
      
			 
        	dojo.byId(id1).style.display="none";
        	dojo.byId(id2).style.display="none";
        	dojo.byId(id3).style.display="none";
        	dojo.byId(id3).style.display="inline-block";
        
        	var parent = dojo.byId(textNode.id);
        	parent.className = "row c" ;	
		
	   	},
    	
    	showErrorMessage:function(message){  //six
	   		
    		var textNode = this; 
			var id = "#"+textNode.id;
			var childs = query(id).children();
			var field = childs[1];			 
			var fieldId = childs[1].id;
			
	   		var id1 = fieldId+"Error_1";
	   		var id2 = fieldId+"Error_2";
	   		var id3 = fieldId+"Error_3";
	   		dojo.byId(id1).style.display="none";
        	dojo.byId(id2).style.display="none";
        	dojo.byId(id3).style.display="none";
            
        	 var value = dojo.byId(fieldId).value;
        	 if(textNode.submitFlag == true){
        		 value = "test";
        		 textNode.submitFlag = false;
        	 }
        	 
             if(value){
		   		dojo.byId(id1).innerHTML = message;

	            dojo.byId(id1).style.display="inline-block";
				dojo.byId(id2).style.display="inline-block";
				
		   		var parent = dojo.byId(textNode.id);
		   		parent.className += " error";
             }
	   	}
	});
	
	return tui.widget.customeraccount.popup.TextBox;
});