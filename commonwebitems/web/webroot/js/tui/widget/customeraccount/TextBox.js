define ("tui/widget/customeraccount/TextBox", [
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
        
		dojo.declare("tui.widget.customeraccount.TextBox", [tui.widget._TuiBaseWidget], {		
				
		promptMessage: null,
		invalidMessage: null,
		mandatory: null,
		maximumLength: null,
		maxLengthMessage: null,
		submitFlag: false,
		specialCharValidation: false,
		alfaNumeric: null,
		novalidationofSpecialChars: null,
		numeric:null,
		addressField: null,
		
		postCreate: function() {
			var textNode = this;
			textNode.inherited(arguments);	
			//textNode.onBlurEvent();  //one
			textNode.onSubmitEvent();
			//fix for DE15246
			textNode.onBlurTextbox();
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
        	   
        	 /*  if(textNode.numeric == "true"){
				   onlyLetters = /^[0-9]+$/.test(val);
			   }else if(textNode.specialCharValidation == "true"){
				   onlyLetters = /^[a-zA-Z]+([\s-]?[a-zA-Z]*)$/.test(val);
			   }else if(textNode.stringWithHypenSpace == "true"){				   
				   onlyLetters = /^[a-z]+(-[a-z]+( [a-z]+)?||( [a-z]+)(-[a-z]+)?)?$/i.test(val);
			   }
			   else if(textNode.alfaNumeric == "true"){
				   onlyLetters = /^[a-zA-Z0-9]+$/.test(val);
			   }
			   else if(textNode.novalidationofSpecialChars == "true"){
				   return true;
			   } else if(textNode.postcode=="true"){
				   //onlyLetters=/[A-Z]{1,2}[0-9][0-9A-Z]?\s?[0-9][A-Z]{2}/gi.test(val);
				   onlyLetters = /^[a-zA-Z0-9]+$/.test(val);
			   } else if(textNode.addressField=="true"){
				  
				   onlyLetters = /^[a-zA-Z0-9\s-,._\/  ]+$/.test(val);
			   }
			   else{
			   onlyLetters = /^[a-zA-Z]+$/.test(val);
			   }*/
        	   
        	   if(textNode.numeric == "true"){ 
				   onlyLetters = /^[+0-9]+$/.test(val);
			   }else if(textNode.specialCharValidation == "true"){
				   onlyLetters = /^[a-zA-Z]+([\s-]?[a-zA-Z]*)$/.test(val);
			   }else if(textNode.stringWithHypenSpace == "true"){				   
				   onlyLetters = /^[A-Za-z]+(-[A-Za-z]+( [A-Za-z]+)?||( [A-Za-z]+)(-[A-Za-z]+)?)?$/i.test(val);
			   }
			   else if(textNode.alfaNumeric == "true"){
				   onlyLetters = /^[a-zA-Z0-9]+$/.test(val);
			   }
			   else if(textNode.novalidationofSpecialChars == "true"){
				   return true;
			   }else if(textNode.postcode=="true"){
				   //onlyLetters=/[A-Z]{1,2}[0-9][0-9A-Z]?\s?[0-9][A-Z]{2}/gi.test(val);
				   onlyLetters = /^([A-Pa-pR-UWYZr-uwyz0-9][A-Ha-hK-Yk-y0-9][AEHMNPRTUVXYaehmnprtuvxy0-9]?[ABEHMNPRVWXYabehmnprvwxy0-9]?[ \\s]{0,1}[0-9][ABD-HJLN-UW-Zabd-hjln-uw-z]{2}|GIR 0AA)$/.test(val);
			   }else if(textNode.addressField=="true"){				   
				   onlyLetters = /^[a-z0-9A-Z\s #\/.,;:-]+$/.test(val);
			   }else if(textNode.county=="true"){
				   //county field should not accept number modified regex
				   onlyLetters = /^[a-zA-Z\s #\/.,;:-]+$/.test(val);
			   }else if(textNode.telephoneField=="true"){				   
				   onlyLetters = /^[(+?0-9][0-9-?]+[0-9)]$/.test(val);			 
			   }
			   else{
				   onlyLetters = /^[a-zA-Z]+$/.test(val);
			   }
        	   return onlyLetters;
           }     
        },
        onFocus: function() {
           
        },
        //fix for DE15246
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
        //onblur on textbox container removed added to textbox 
        //fix for DE15246
        /*onBlur: function() { //two
            var textNode = this;
			var id = "#"+this.id;
			var childs = query(id).children();
			var field = childs[1];			 
			var fieldId = "#"+childs[1].id;
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
        }, */
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
        	if(textNode.postcode=="true"){
        		parent.className = "row c postcode valid" ;
        	}else{
        		parent.className = "row c" ;
        	}
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
		   		if(textNode.postcode=="true"){
	        		parent.className = "row c postcode error" ;
	        	}else{
	        		parent.className = "row c error" ;
	        	}
        	}
	   	}
	});
	
	return tui.widget.customeraccount.TextBox;
});