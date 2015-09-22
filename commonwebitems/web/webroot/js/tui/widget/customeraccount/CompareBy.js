define ("tui/widget/customeraccount/CompareBy", [
													"dojo",											  	
													"dojo/cookie",
													"dojo/query",
													"dojo/has",
													"dojox/validate/web",
													"tui/validate/check",
													"dojo/_base/array",
													"dojo/dom-style",
													"dijit/focus",
													"dojo/text!tui/widget/customeraccount/view/templates/CompareByTmpl.html",
													"dojo/_base/xhr",
													"dojo/fx",
													"dojo/topic",                                               
													"tui/widget/_TuiBaseWidget", 
													"dojo/NodeList-traverse",													
													"dojox/dtl", 
													"dojox/dtl/Context", 
													"dojox/dtl/tag/logic",
													"dijit/registry",
													"tui/dtl/Tmpl",
													"dojo/html",
													"dojox/validate/us",
													"tui/widget/mixins/Templatable"
													
													
							    			  ], function(dojo,  cookie, query, has, validate,check, arrayUtil, domStyle, focusUtil, CompareByTmpl,xhr,fx){

		dojo.declare("tui.widget.customeraccount.CompareBy", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable], {
	
		CommaSeparatedWishlistIds: null,
		tmpl: CompareByTmpl,
		
		holidays: null,
		component:null,
		login:null,
		tuiWebrootPath:tuiWebrootPath,
		
		onAfterTmplRender: function(){
			var compareByComponent = this;					 
		},
		postCreate: function() {
			var compareByComponent = this;
            
            compareByComponent.renderCompareDropDown();	
           	
		},
		
		renderCompareDropDown: function(data){
		
			var FCTHoptionsDisplayFlag = 0;
			var FCoptionsDisplayFlag = 0;
			var THoptionsDisplayFlag = 0;
			
			var compareByComponent = this;	
			if(data){
				newHolidays = data;
			}
			else{				
				newHolidays = (compareByComponent.jsonData);
			}
			
			if(newHolidays){
				var len = newHolidays.length;
				var overlayData = [];
				for(var k = 0; k < len ; k++ ){	
					if(newHolidays[k]["brandType"] == "TH"){						
						THoptionsDisplayFlag = 1;
					}
					if(newHolidays[k]["brandType"] == "FC"){
						FCoptionsDisplayFlag = 1;
						newHolidays[k]["brandTypeDomain"] = "holiday";						
					}
				}	
				newHolidays["loginstring"] = JSON.stringify(compareByComponent.login);
			}	
			FCTHoptionsDisplayFlag = FCoptionsDisplayFlag + THoptionsDisplayFlag;
			var html = compareByComponent.renderTmpl(CompareByTmpl,{tuiWebrootPath:tuiWebrootPath,holidays: newHolidays,FCTHoptionsDisplayFlag:FCTHoptionsDisplayFlag });           

            if (html) {
                dojo.place(html, compareByComponent.domNode, "only");
                dojo.parser.parse(compareByComponent.domNode);
            }
		}
	});
	
	return tui.widget.customeraccount.CompareBy;
});