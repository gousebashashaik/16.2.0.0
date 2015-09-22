define ("tui/widget/customeraccount/Captcha", [
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
        
		dojo.declare("tui.widget.customeraccount.Captcha", [tui.widget._TuiBaseWidget], {		
				
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
			textNode.onSubmitEvent();
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
		   var val = dojo.byId("recaptcha_response_field");
		 
			if (val.value == "") {
				return false;
			}  
			return true;
			
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
 			var captcha_box = dojo.byId("recaptcha_response_field");
 			
 			query(captcha_box).on('blur',function(){
 				
 				if(textNode.mandatory == "true"){ 
					var val = captcha_box.value; //three					
					var result = textNode.validator(val);   
					textNode.removeErrorMessage();
					if(!result) {						
						textNode.showErrorMessage();
						return false;
					}
					return true;
				} 				
 			});
        },
        removeErrorMessage: function(){ //five
        	var textNode = this;
            var id = "#"+textNode.id;
			var childs = query(id).children();		
			var field = dojo.query('#recaptcha_response_field', textNode.domNode)[0];
			var fieldId = field.id;
	
        	var id1 = fieldId+"Error_1";
        	var id2 = fieldId+"Error_2";
        	var id3 = fieldId+"Error_3";        	
			 
        	dojo.byId(id1).style.display="none";
        	dojo.byId(id2).style.display="none";
        	dojo.byId(id3).style.display="none";
        	dojo.byId(id3).style.display="inline-block";
        
        	var parent = dojo.byId(textNode.id);
        	var responseField = dojo.byId("recaptcha_response_field").value;
        	var challengeField = dojo.byId("recaptcha_challenge_field").value;
        	
        	//alert(request.getParameter("recaptcha_challenge_field"));
        	if(textNode.postcode=="true"){
        		parent.className = "row c no-label postcode valid" ;
        	}else{
        		parent.className = "row c no-label" ;
        	}
	   	},
    	showErrorMessage:function(){  //six
    		var textNode = this; 
    		
			var id = "#"+textNode.id;
			var childs = query(id).children();	
			var field = dojo.query('#recaptcha_response_field', textNode.domNode)[0];
			var fieldId = field.id;
			
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
        	 
        		dojo.byId(id1).innerHTML = "Please enter Captcha";
	            dojo.byId(id1).style.display="inline-block";
				dojo.byId(id2).style.display="inline-block";
				
		   		var parent = dojo.byId(textNode.id);
		   		if(textNode.postcode=="true"){
	        		parent.className = "row c no-label postcode error" ;
	        	}else{
	        		parent.className = "row c no-label error" ;
	        	}
        	
	   	}
	});
		
	return tui.widget.customeraccount.Captcha;
});