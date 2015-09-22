define ("tui/widget/customeraccount/CheckPassword", [
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
        
		dojo.declare("tui.widget.customeraccount.CheckPassword", [tui.widget._TuiBaseWidget], {		
				
		promptMessage: null,
		invalidMessage: null,
		notMatching:null,
		RefId:null,
		mandatory:null,
		submitFlag: false,
		postCreate: function() {
			var textNode = this;
			textNode.inherited(arguments);	
            textNode.onPaste();
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
            return validate.isText(val);
        },
        onFocus: function() {
           
        },
        //fix for DE15246
        onBlurTextbox:function(){
        	var textNode = this;
            var id = "#"+textNode.id;			
			var field = dojo.query('.reCheckPwd', textNode.domNode)[0];
			var fieldId = field.id;
			query(field).on('blur',function(){		 
				var val = field.value; 
				var refField = dojo.byId(textNode.RefId);
				if(val.length > 0) {
					textNode.removeErrorMessage(); 
				}
				if(val.length > 0 && val.length < 6) {
					textNode.showErrorMessage("This isn't a valid password - it needs to have at least 6 characters. Re-enter your password.");
				} else if(refField.value.length > 0) {
					if(textNode.mandatory == "true") { 
						if(val == refField.value) {								
							var widget = dojo.byId(textNode.RefId).parentNode.parentNode;
							var id = "#"+widget.id;
							var childs = query(id).children().children();
							var errField = childs[1];			 
							var errfieldId = childs[1].id;
							
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
						if(!val){ 
							var message = textNode.promptMessage;	
							textNode.showErrorMessage(message);
						} else {			
							var message = textNode.checkPasswordMatch();
							if(message != ""){ 
								textNode.showErrorMessage(message);
							} else { 
								textNode.removeErrorMessage();
							}
						}
					}
				} else if(val == '') {			
					var id = "#"+textNode.id;			
					var errField = dojo.query('.reCheckPwd', textNode.domNode)[0];
					var errfieldId = errField.id;
					
					var id1 = errfieldId+"Error_1";
					var id2 = errfieldId+"Error_2";
					var id3 = errfieldId+"Error_3";
					if(dojo.byId(id1) != undefined){
						dojo.byId(id1).style.display="none";
					}
					if(dojo.byId(id2) != undefined){
						dojo.byId(id2).style.display="none";
					}
					if(dojo.byId(id3) != undefined){
						dojo.byId(id3).style.display="none";
					}
					
					var parent=dojo.query('.reCheckPwd').closest(".row");
					dojo.removeClass(parent[0], "error");
				}
			});
        },
        //onblur on textbox container removed added to textbox 
        //fix for DE15246
        /*onBlur: function() {
            var textNode = this;
            var id = "#"+textNode.id;			
			var field = dojo.query('.reCheckPwd', textNode.domNode)[0];
			var fieldId = field.id;
           
			if(textNode.mandatory == "true"){ 		
				textNode.removeErrorMessage();  
				var val = field.value; 
				if(!val){ 
						var message = textNode.promptMessage;	
						textNode.showErrorMessage(message);
				}
				else{			
					var message = textNode.checkPasswordMatch();
					if(message != ""){ 
						textNode.showErrorMessage(message);
					}
					else{ 
						textNode.removeErrorMessage();
					}
				}
			}
			
        }, */
    	onPaste: function(){
		    var textNode = this;
			
			var id = "#"+textNode.id;			
			var field = dojo.query('.reCheckPwd', textNode.domNode);			 
			var fieldId = dojo.attr(field, "id");
			
			var qId = "#"+fieldId;
			query(qId).on("paste", function(e){     		   
				 dojo.stopEvent(e);			
				return false;
       	    });
		},
        removeErrorMessage: function(){
        	var textNode = this; 
			
			var id = "#"+textNode.id;			
			var field = dojo.query('.reCheckPwd', textNode.domNode)[0];
			var fieldId = field.id;
			
        	var id1 = fieldId+"Error_1";
        	var id2 = fieldId+"Error_2";
        	var id3 = fieldId+"Error_3";
			if(dojo.byId(id1) != undefined){
        	dojo.byId(id1).style.display="none";
			}
			if(dojo.byId(id2) != undefined){
        	dojo.byId(id2).style.display="none";
			}
			if(dojo.byId(id3) != undefined){
        	dojo.byId(id3).style.display="none";	
			}
			if(dojo.byId(id3) != undefined){
        	dojo.byId(id3).style.display="inline-block";
			}
        	var parent = dojo.byId(textNode.id);
        	
        	parent.className = "row c pwd valid" ;
	   		 
	   	},
    	showErrorMessage:function(message){ 
    		var textNode = this;
			var id = "#"+textNode.id;			
			var field = dojo.query('.reCheckPwd', textNode.domNode)[0];
			var fieldId = field.id;
			
	   		var id1 = fieldId+"Error_1";
        	var id2 = fieldId+"Error_2";
        	var id3 = fieldId+"Error_3";
			if(dojo.byId(id1) != undefined){
        	dojo.byId(id1).style.display="none";
			}
			if(dojo.byId(id2) != undefined){
        	dojo.byId(id2).style.display="none";
			}
			if(dojo.byId(id3) != undefined){
        	dojo.byId(id3).style.display="none";
			}
        	if(fieldId != ""){
        	var value = dojo.byId(fieldId).value; //3
			}
	       	 if(textNode.submitFlag == true){
	       		 value = "test";
	       		 textNode.submitFlag = false; //5
	       	 }
       	 
            if(value){
				if(dojo.byId(id1) != undefined){
		   		dojo.byId(id1).innerHTML = message;
		   		dojo.byId(id1).style.display="inline-block";
				}
				if(dojo.byId(id2) != undefined){
	        	dojo.byId(id2).style.display="inline-block";
				}
		   		var parent = dojo.byId(textNode.id);
		   		parent.className += " error";
            }
	   	},
    	checkPasswordMatch:function(){ 
			var textNode = this;
			var id = "#"+textNode.id;			
			var field = dojo.query('.reCheckPwd', textNode.domNode)[0];
			var fieldId = field.id;

			var id1 = fieldId+"Error_1";
			var id2 = fieldId+"Error_2";
			var RefId = textNode.RefId;
			var curId = fieldId;
			
			var curField = dojo.byId(curId);
			var RefFiled = dojo.byId(RefId);
			if(dojo.byId(id1) != undefined){
			dojo.byId(id1).style.display="none";
			}
			if(dojo.byId(id2) != undefined){
			dojo.byId(id2).style.display="none";
			}
			if(RefFiled.value != curField.value){
				return textNode.notMatching;
			}
			else{
				return "";
			}
	   	}
	});
	
	return tui.widget.customeraccount.CheckPassword;
});