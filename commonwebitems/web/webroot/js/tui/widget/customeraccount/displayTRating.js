define("tui/widget/customeraccount/displayTRating", 
		["dojo",
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
		 "dojox/validate/us"],
		function(dojo, on, cookie, query, has, validate,check, arrayUtil, domStyle, focusUtil, domConstruct,xhr){
		
        
		
		dojo.declare("tui.widget.customeraccount.displayTRating", [tui.widget._TuiBaseWidget], {
			
			trating:null,
		
		    wishlistEntryId:null,

		    brandType:null,
		
			postCreate: function() {
			},
			startup:function(){
				var ratingBar = this;
				
				var ratingChar = "O"; 
				
				var rating = ratingBar.trating;
				if(tuiSiteName == "firstchoice"){
					if(ratingBar.brandType != null && ratingBar.brandType == "destinations") {
						ratingChar = "O" ;					
					}
					else if(ratingBar.brandType != null && ratingBar.brandType == "holiday") {
						ratingChar = "P" ;					
					}
					else if(ratingBar.brandType != null && ratingBar.brandType == "TH_FC") {
						ratingChar = "P" ;					
					}
					else if(ratingBar.brandType != null && ratingBar.brandType == "FC_TH") {
						ratingChar = "P" ;					
					}
				}
				if(rating){
					var exP = rating.split(" ");
					
					var ratingNumber = parseInt(exP[0], 10);
					
					var ratingPlus = exP[1];					
					var ratingString = "";
					
					for(var i=1; i <= ratingNumber; i++){
						ratingString = ratingString + ratingChar;						
					}										
					if(ratingPlus != undefined){
					var plusSpan = '&nbsp;<span>Plus</span>';
					
					ratingString = ratingString + plusSpan;					
					}
					
					ratingBar.attr("innerHTML",ratingString);
				}
			}
			
		});
		return tui.widget.customeraccount.displayTRating;
});