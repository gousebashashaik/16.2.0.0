define ("tui/widget/customeraccount/popup/EmailAddress", [
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
													
													
													
							    			  ], function(dojo, on, cookie, query, has, validate,check, arrayUtil, domStyle, focusUtil, domConstruct){
        
		dojo.declare("tui.widget.customeraccount.popup.EmailAddress", [tui.widget._TuiBaseWidget], {		
				
		promptMessage: null,
		invalidMessage: null,
		mandatory: null,
		maximumLength: null,
		maxLengthMessage: null,
		submitFlag: false,
		
		postCreate: function() {
			var email = this;			
			email.inherited(arguments);
			email.onBlurTextbox(); 
			email.onSubmitEvent();
		},
		onSubmitEvent: function(){
			var email = this;
			query("input[type='submit']").on("click", function(e){       		    
				email.submitFlag = true;
       		
       	    });
		},
		onBlurTextbox:function(){
        	var email = this;

			var id = "#"+this.id;
			var childs = query(id).children();
			
			var field = childs[1];			 
			var fieldId = "#"+childs[1].id;
			query(field).on('blur',function(){
				if(email.mandatory == "true"){
					var val = field.value;			
					var result = email.validator(val);        
					email.removeErrorMessage();           	
					if(!result){
						if(!val.length){
							var message = email.promptMessage;	
						}
						else{
							var message = email.invalidMessage;
						}
										email.showErrorMessage(message);
					}else if(result == true){
						if(val.length > email.maximumLength){
							var message = email.maxLengthMessage + " " + email.maximumLength + " characters";
							email.showErrorMessage(message);
						}
										
						if(email.RefId!=null){
							var refVal=dojo.byId(email.RefId).value;
							if(refVal!="" && email.validator(refVal)){
								var matched = email.CheckEmailMatch();
								if(!matched){
									message = email.notMatching;
									email.showErrorMessage(message);
								}else{
									email.removeRefNodeErrorMessage();
								}
							}
						}
					}
				}
			});
        },
    	validator: function(val) {
            return validate.isEmailAddress(val);
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
	
	return tui.widget.customeraccount.popup.EmailAddress;
});