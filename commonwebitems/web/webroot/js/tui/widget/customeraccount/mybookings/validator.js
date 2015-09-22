define ("tui/widget/customeraccount/mybookings/validator", [
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
													
													
													
							    			  ], function(dojo,on,  cookie, query, has, validate,check, arrayUtil, domStyle, focusUtil, domConstruct,xhr){
        
		dojo.declare("tui.widget.customeraccount.mybookings.validator", [tui.widget._TuiBaseWidget], {		
				
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
			textNode.onBlurTextbox();
			textNode.removeErrorMessage();
			textNode.onSubmitEvent();
			textNode.inherited(arguments);	
		},
		onSubmitEvent: function(){
			var textNode = this;
			query("#myBookings").on("click", function(e){
				dojo.stopEvent(e);
				textNode.bindData();	
       	    });
		},
    	validator: function(val) {
		   var textNode = this;        	         	   
        	   if(textNode.numeric == "true"){ 
				   onlyLetters = /^\d+$/.test(val);
			   }               
			return onlyLetters;
        },
        onFocus: function() {
           
        },
        onBlurTextbox:function() {		
			var textNode = this;  
						textNode.removeErrorMessage();      	
			var bookyear = dojo.byId("book-year").value;
			if(bookyear.length == 4) {
				dojo.query("#book-year", textNode.domNode).on('blur',function(){
					if(!textNode.validator(dojo.byId("book-year").value)) {
						textNode.showErrorMessage(textNode.invalidMessage);
					}
				});		
			} else {
				textNode.showErrorMessage(textNode.invalidMessage);
			}			
			dojo.query("#book-id", textNode.domNode).on('blur',function(){
				if(!textNode.validator(dojo.byId("book-id").value)) {
					textNode.showErrorMessage(textNode.invalidMessage);
				}
			});			
        },        
        removeErrorMessage: function(){ //five
        	var textNode = this;
			var errorMessage = dojo.query("span.error", textNode.domNode);
			errorMessage[0].innerHTML = "";
	   	},
    	showErrorMessage:function(message){  //six
    		var textNode = this; 
			var errorMessage = dojo.query("span.error", textNode.domNode);
			errorMessage[0].innerHTML = message;
	   	},		
		bindData: function() {
			var widget = this;
			var sortUrl	= tuiWebrootPath+"/customerBookings/linkmyBookings";
			var form = dojo.byId("getBookings");
			xhr.post({				
				
				url: sortUrl+"?cache="+Math.random(),
				
				preventCache: true,
				
				form: dojo.byId(form.id),
				
				load: function(response) {
					console.log("response:",response);
				},					
				error: function(errors) {
					console.log(errors);
					searchResultsComponent.handleBackendError(errors);
				},					
				handle: function(response) {
					
					console.log("inside handle");
					hasBeenSent = true;
					
					if(response){
						response = dojo.fromJson(response);
						console.log("before2");
						if(dojo.byId("results") != undefined){
							wipeTarget = dojo.byId("results");
							wipeTarget.innerHTML = "";
							wipeTarget.style.display = 'none';							
							searchResultsComponent.renderSavedHolidaysList(response);						
							fx.wipeIn({ node: wipeTarget }).play();
							console.log("after2");
							if(dojo.byId("shortlist") != undefined){				
								searchResultsComponent.hideLoader("shortlistresultsAvailable");
								searchResultsComponent.hideLoader("noLongerAvailableHolidays");
							}
						}
					}
				}
			});
		}
		
	});
	
	return tui.widget.customeraccount.mybookings.validator;
});