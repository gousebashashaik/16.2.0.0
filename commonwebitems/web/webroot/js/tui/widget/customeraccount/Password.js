define ("tui/widget/customeraccount/Password", [
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
													"dojo/NodeList-traverse",
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
        
		dojo.declare("tui.widget.customeraccount.Password", [tui.widget._TuiBaseWidget], {		
				
		promptMessage: null,
		invalidMessage: null,
		mandatory:null,
		submitFlag: false,
		indicatorId: null,
		RefId:null,
		
		postCreate: function() {
			var textNode = this;
			textNode.inherited(arguments);
			textNode.showStrengthIndicator();
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
		
		showStrengthIndicator:function(){
			var textNode = this;
			var id = "#"+textNode.id;
			var childs = query(id).children().children();
			var field = childs[1];			 
			var fieldId = childs[1].id;
			var qId = "#"+fieldId;
			query(qId).on("keyup", function(e){  		   
				if(textNode.mandatory == "true"){				
					textNode.removeErrorMessage();  
					var val = field.value;
					if(val.length == 6 || val.length > 6){						
						var strength = textNode.CheckPasswordStrength(val);
						var len = textNode.ShowPasswordStrength(strength); 
					} else {
						textNode.ShowPasswordStrength("Blank");
					}
					
				}
       	    });
		},
    	validator: function(val) { 
            return validate.isText(val);
        },
        onFocus: function() {
           
        },
        //fix for DE15246
        onBlurTextbox:function(){
        	var textNode = this;
            var id = "#"+textNode.id;
			var childs = query(id).children().children();
			var field = childs[1];			 
			var fieldId = childs[1].id;
			query(field).on('blur',function(){
				if(textNode.mandatory == "true"){
					textNode.removeErrorMessage();  
					var val = field.value;
					if(!val){
							var message = textNode.promptMessage;	
							textNode.showErrorMessage(message);
					} else {
						if(val.length < 6){
							var message = textNode.minimumCharactersMessage;	
							textNode.showErrorMessage(message);
						} else {
						var strength = textNode.CheckPasswordStrength(val);
						var len = textNode.ShowPasswordStrength(strength); 
						}
					}
					
					var curField = dojo.byId(field).value;
					var RefFiled = "";
					if(dojo.byId(textNode.RefId) != undefined){
						RefFiled = dojo.byId(textNode.RefId).value;
					}
					
					if(RefFiled != ''){							
						var id = "#"+textNode.id;							
						if(RefFiled != curField){
							if(curField != "") {
							textNode.showErrorMessage("Your passwords do not match. Please try again");					
							}
						} else {
							var widget = dojo.byId(textNode.RefId).parentNode.parentNode;
							var id = "#"+widget.id;
							var childs = query(id).children().children("input");
							var errField = textNode.RefId;			 
							var errfieldId = textNode.RefId.id;
							
							var id1 = errfieldId + "Error_1";
							var id2 = errfieldId + "Error_2";
							var id3 = errfieldId + "Error_3";
							
							dojo.byId(id1).style.display="none";
							dojo.byId(id2).style.display="none";
							dojo.byId(id3).style.display="none";
														
							dojo.byId(id3).style.display="inline-block";
							var parent = dojo.byId(widget.id);
							parent.className = "row c pwd valid" ;	
						}
					}
				}
			});
        },
        //on blur on textbox container removed added to textbox 
        //fix for DE15246
       /* onBlur: function() {
            var textNode = this;
            var id = "#"+textNode.id;
			var childs = query(id).children().children();
			var field = childs[1];			 
			var fieldId = childs[1].id;
			
            if(textNode.mandatory == "true"){				
				textNode.removeErrorMessage();  
				var val = field.value;
				if(!val){
						var message = textNode.promptMessage;	
						textNode.showErrorMessage(message);
				}
				else{
					if(val.length < 6){
						var message = textNode.minimumCharactersMessage;	
						textNode.showErrorMessage(message);
					}else{
					var strength = textNode.CheckPasswordStrength(val);
					var len = textNode.ShowPasswordStrength(strength); 
					}
				}
			}
        }, */
        removeErrorMessage: function(){
        	var textNode = this; 
			var id = "#"+textNode.id;
			var childs = query(id).children().children();
			var field = childs[1];			 
			var fieldId = childs[1].id;
			
        	var id1 = fieldId+"Error_1";
        	var id2 = fieldId+"Error_2";
        	var id3 = fieldId+"Error_3";
			
        	dojo.byId(id1).style.display="none";
        	dojo.byId(id2).style.display="none";
        	dojo.byId(id3).style.display="none";
        	
        	//var id4 = fieldId+"Error_4";
    		//var id5 = fieldId+"Error_5";
    		//var id6 = fieldId+"Error_6";
			
    		/*dojo.byId(id4).style.display="none";
        	dojo.byId(id5).style.display="none";
        	dojo.byId(id6).style.display="none";*/		
        	
        	dojo.byId(id3).style.display="inline-block";
        	var parent = dojo.byId(textNode.id);
        	parent.className = "row c pwd valid" ;	
	   	},
    	showErrorMessage:function(message){ 
    		var textNode = this;
			var id = "#"+textNode.id;
			var childs = query(id).children().children();
			var field = childs[1];			 
			var fieldId = childs[1].id;
			
			var id1 = fieldId+"Error_1";
        	var id2 = fieldId+"Error_2";
        	var id3 = fieldId+"Error_3";
        	dojo.byId(id1).style.display="none";
        	dojo.byId(id2).style.display="none";
        	dojo.byId(id3).style.display="none";
	   		
	   		//var id4 = fieldId+"Error_4";
    		//var id5 = fieldId+"Error_5";
    		//var id6 = fieldId+"Error_6";
			
    		/*dojo.byId(id4).style.display="none";
        	dojo.byId(id5).style.display="none";
			dojo.byId(id6).style.display="none";*/
        	
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
				parent.className += " error pwd";
            }
	   	},
		CheckPasswordStrength: function(password){
    		var strength = new Array();
    		strength[0] = "Blank";
    		strength[1] = "Weak";
    		strength[2] = "Weak";
    		strength[3] = "Average";
    		strength[4] = "Strong";
    		strength[5] = "Strong";

    		var score = 1;
            
    		var onlySmallLetters = /^[a-z]*$/.test(password); 
    		if(onlySmallLetters){
    			return strength[1];
    		}
    		
    		var onlyBigLetters = /^[A-Z]*$/.test(password); 
    		if(onlyBigLetters){
    			return strength[1];
    		}
    		
    		if (password.length < 1)
    			return strength[0];

    		if (password.length < 4)
    			return strength[1];

    		if (password.length >= 6)
    			score++;
    		if (password.match(/\d+/))
    			score++;
    		if (password.match(/[a-z]/) &&
    			password.match(/[A-Z]/))
    			score++;
    		if (password.match(/.[!,@,#,$,%,^,&,*,?,_,~,-,£,(,)]/))
    			score++;
            if(score > 5){
            	strength[score] = "Strong";
            }
    		return strength[score];
    	},
    	ShowPasswordStrength:function(Strength){ 
    		var textNode = this; 
			var id = "#"+textNode.id;
			var childs = query(id).children().children();
			var field = childs[1];			 
			var fieldId = childs[1].id;
			
    		var id1 = fieldId+"Error_1";
    		var id2 = fieldId+"Error_2";
    		var id3 = fieldId+"Error_3";
    		var id4 = fieldId+"Error_4";
    		var id5 = fieldId+"Error_5";
    		var id6 = fieldId+"Error_6";
			 
			 
			if(id4 != undefined && id5 != undefined && id4 != undefined){
				
				dojo.byId(id1).style.display="none";
				dojo.byId(id2).style.display="none";
				dojo.byId(id3).style.display="none";
				
				dojo.removeClass(textNode.indicatorId, "weak medium strong weakx mediumx strongx");
				 switch(Strength){ 
					case "Weak": 		
						dojo.byId(id3).style.display="inline-block";
						dojo.addClass(textNode.indicatorId, "weak mediumx strongx");
					break;
					case "Average": 
						dojo.byId(id3).style.display="inline-block";
						dojo.addClass(textNode.indicatorId, "weakx medium strongx");
					break;
					case "Strong": 
						dojo.byId(id3).style.display="inline-block";
						dojo.addClass(textNode.indicatorId, "weakx mediumx strong");
					break;
					case "Blank":
						dojo.removeClass(textNode.indicatorId, "weak medium strong weakx mediumx strongx");
					break;
				 }
						
				
				/*var curField = dojo.byId(textNode.id).value;
				var RefFiled = dojo.byId(textNode.RefId).value;
				
				if(curField != ''){	
					
					var id = "#"+textNode.id;	
					
					if(RefFiled != curField){
						textNode.showErrorMessage(textNode.invalidMessage);					
					}
				}*/
				 //TH desktop - Password doesn't match message is still getting displayed when data entered in password and re-enter password fields is same.
		        //fix for DE17265
				
			}
	   	},
	   
	   	showTips:function(){	
			dojo.byId("passwordTips").style.display="inline-block";
	   	},
	   	hideTips:function(){
	   		dojo.byId("passwordTips").style.display="none";
	   	}
    	
	});
	
	return tui.widget.customeraccount.Password;
});