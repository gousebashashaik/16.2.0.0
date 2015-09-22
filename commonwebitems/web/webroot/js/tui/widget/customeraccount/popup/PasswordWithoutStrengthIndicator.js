define ("tui/widget/customeraccount/popup/PasswordWithoutStrengthIndicator", [
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
        
		dojo.declare("tui.widget.customeraccount.popup.PasswordWithoutStrengthIndicator", [tui.widget._TuiBaseWidget], {		
				
		promptMessage: null,
		invalidMessage: null,
		mandatory:null,
		submitFlag: false,
		showHidePopup:function(PopupId, type){
			var root = document.getElementsByTagName( 'html' )[0];
			if(type == "show"){
				dojo.removeClass(PopupId, "hide");
				dojo.addClass(PopupId, "show");
				dojo.addClass(root, "modal-open");
			}
			else{
				dojo.removeClass(PopupId, "show");
				dojo.addClass(PopupId, "hide");
				dojo.removeClass(root, "modal-open");
			}
		},
		postCreate: function() {
			var textNode = this;
			textNode.inherited(arguments);			
			textNode.onSubmitEvent();
			textNode.switchToForgotPassword();
			
		},
		onSubmitEvent: function(){ 
			var textNode = this;
			query("input[type='submit']").on("click", function(e){       		   
				textNode.submitFlag = true;       		
       	    });
		},
		
		switchToForgotPassword: function() {
			var textNode = this;	
			
			dojo.query(".ForgotPasswordLinkFromOverlay").onclick(function(){
				textNode.showHidePopup("wouldyouliketoGo", "hide");
				textNode.showHidePopup("wouldyouliketoGoForgotPassword", "show");
			});
		},
    	validator: function(val) { 
            return validate.isText(val);
        },
        
        onFocus: function() {
           
        },

        onBlur: function() {
            var textNode = this;
            var id = "#"+textNode.id;
			
			var childs = query(id).children();
			var field = childs[1];			 
			var fieldId = childs[1].id;
			
            if(textNode.mandatory == "true"){				
				textNode.removeErrorMessage();  
				var val = field.value;
				if(!val){
						var message = textNode.promptMessage;	
						textNode.showErrorMessage(message);
						textNode.switchToForgotPassword();
				}
				else{
					if(val.length < 6){
						var message = textNode.minimumCharactersMessage;	
						textNode.showErrorMessage(message);
					}
				}
			}
        }, 
        
        
    	
        removeErrorMessage: function(){
        	
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
        	parent.className = "row c pwd valid" ;	
	   	},
    	
    	showErrorMessage:function(message){ 
	   		
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
            
        	 var value = dojo.byId(fieldId).value; //3
        	 if(textNode.submitFlag == true){
        		 value = "test";
        		 textNode.submitFlag = false; //5
        	 }
        	 
             if(value){
		   		dojo.byId(id1).innerHTML = message;

				dojo.byId(id1).style.display="inline-block";
				dojo.byId(id2).style.display="inline-block";
	
		   		var parent = dojo.byId(textNode.id);
		   		parent.className += " error"; 
             }
	   	},
		

 
	   	showTips:function(){
	   		dojo.byId('passwordTips').style.display="inline-block";
	   	},
	   	hideTips:function(){
	   		dojo.byId('passwordTips').style.display="none";
	   	}
    	
	});
	
	return tui.widget.customeraccount.popup.PasswordWithoutStrengthIndicator;
});