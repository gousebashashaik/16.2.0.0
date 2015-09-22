define ("tui/widget/customeraccount/popup/cookieTransfer", [
													"dojo",											  	
													"dojo/cookie",
													"dojo/query",
													"dojo/has",
													"dojox/validate/web",
													"tui/validate/check",
													"dojo/_base/array",
													"dojo/dom-style",
													"dijit/focus",
													"dojo/_base/xhr",
													"dojo/_base/json",
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
													
													
							    			  ], function(dojo,  cookie, query, has, validate,check, arrayUtil, domStyle, focusUtil, xhr){

		dojo.declare("tui.widget.customeraccount.popup.cookieTransfer", [tui.widget._TuiBaseWidget], {

		transferUrl : tuiWebrootPath+"/manageAccount/customerHome",
		deleteUrl : tuiWebrootPath+"/manageAccount/customerHome",
				
		postCreate: function() {
			var cookieTransfer = this;
			//cookieTransfer.resetForm();	
			cookieTransfer.inherited(arguments);
			cookieTransfer.attachEventListener();	
			
			
		},
		startup:function(){
		    if(dojo.byId("capageloader") != undefined){
			dojo.byId("capageloader").style.display="none";
			}		
		},
    	
		hideloader:function(){
			if(dojo.byId("capageloader") != undefined){
			dojo.byId("capageloader").style.display="none";
			}
		},
		showloader:function(){
			if(dojo.byId("capageloader") != undefined){
			dojo.byId("capageloader").style.display="block";
			}
		},
		submittheForm: function(form) { 
			
			//alert(form.action);
			//form.showloader();
			xhr.post({
				
				url: form.action,
				
				form: dojo.byId(form.id),
				// The success handler
				load: function(response) {
				
					var obj = JSON.parse(response);
					 //alert(response);
					//form.hideloader();
				},
				// The error handler
				error: function(errors) { 
					
					
				},
				// The complete handler
				handle: function() {
					hasBeenSent = true;
				}
			});

		},		
        
        
    	attachEventListener: function(){
            var createAccount = this;
            
            
            
			  
			  
			  /*
			  var link = dojo.query(".manageaccountlink");
			 
			  dojo.connect(link.domNode, "onclick", function(event){
			     dojo.stopEvent(event);
				alert("okkkk");
				 alert(link.innerHTML);
			  });
			  */
			  
    	}
	});
	
	return tui.widget.customeraccount.popup.cookieTransfer;
});