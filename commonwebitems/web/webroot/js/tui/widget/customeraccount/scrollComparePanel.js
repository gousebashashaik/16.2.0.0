define ("tui/widget/customeraccount/scrollComparePanel", [
													"dojo",
													"dojo/dom",
													"dojo/dom-attr",
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
													"tui/widget/customeraccount/UserRepository",
													"dojo/Stateful",
													"dojo/_base/fx",
													"tui/widget/_TuiBaseWidget"													
													
							    			  ], function(dojo, dom, domAttr, on, cookie, query, has, validate,check, arrayUtil, domStyle, focusUtil, domConstruct, userRepo, Stateful,fx){
        
		dojo.declare("tui.widget.customeraccount.scrollComparePanel", [tui.widget._TuiBaseWidget], {		
				
		login: null,
		setDiffer: null,
		postCreate: function() {
			var widget = this;
			window.scrollTo(0,0);
			
			
			window.setTimeout(function(){
				if(dojo.query(".sort-menu")[0] != undefined){
				
				if(dojo.query("#shortlist .create-account")[0] != undefined){
				widget.initScrollAnonymous();
				}
				else{
				widget.initScrollLoggedIn();
				}
				}
			}, 1000);
			
		},
	    initScrollAnonymous:function(){
			
			var widget = this;
			dojo.connect(window, 'resize',function(){
				
				if(dojo.query(".sort-menu")[0] != undefined){
					widget.left = dojo.position(dojo.query("#compare-panel")[0]).x;
					//var iniTTop = (dojo.coords(dojo.query(".sort-menu")[0]).t - dojo.coords(dojo.query(".sort-menu")[0]).h);
					//iniTTop = iniTTop + 10;
					
					var iniTTop = (dojo.coords(dojo.query(".sort-menu")[0]).t);
					
					
					if(iniTTop < 0){
					iniTTop = 0;
					}
					dojo.query("#compare-panel").style({'position':'absolute','top':iniTTop+'px','left':'0'});
				}
			});
			if(dojo.query(".sort-menu")[0] != undefined){
			widget.left = dojo.position(dojo.query("#compare-panel")[0]).x;
			//var iniTTop = (dojo.coords(dojo.query(".sort-menu")[0]).t - dojo.coords(dojo.query(".sort-menu")[0]).h);
					//iniTTop = iniTTop + 10;
					
					var iniTTop = (dojo.coords(dojo.query(".sort-menu")[0]).t);
					
			if(iniTTop < 0){
			iniTTop = 0;
			}
			dojo.query("#compare-panel").style({'position':'absolute','top':iniTTop+'px','left':'0'});
			}
			
			dojo.connect(window, 'onscroll',function(){
			
	  
				var panelHt = (dojo.coords(dojo.query("#compare-panel")[0]).h + 10 );
				var pos = (dojo.coords(dojo.query(".remove_all_saved_holidays")[0]).y - panelHt );
				
				if(pos < 0){ 
					//var bHt = (dojo.coords(dojo.query(".remove_all_saved_holidays")[0]).t - (dojo.coords(dojo.query("#compare-panel")[0]).h + dojo.coords(dojo.query("#compare-panel")[0]).h));
					
					var bHt = dojo.coords(dojo.query(".remove_all_saved_holidays")[0]).t - dojo.coords(dojo.query(".remove_all_saved_holidays")[0]).h;
					bHt = bHt -10;
					
					dojo.query("#compare-panel").style({'position':'absolute','top': bHt+'px','left':'0'});
					
				}
				else if(dojo.coords(dojo.query(".sort-menu")[0]).y < 0){
					
					dojo.query("#compare-panel").style({'position':'fixed','top':'0','left':widget.left+'px'});
								 
				}
				else if(dojo.coords(dojo.query(".sort-menu")[0]).y > 0){
					
					
					dojo.query("#compare-panel").style({'position':'absolute','top':iniTTop+'px','left':'0'});
					
				}
			
			});
		},
		initScrollLoggedIn:function(){
			
			var widget = this;
			
			dojo.connect(window, 'resize',function(){
				window.scrollTo(0,0);
				if(dojo.query(".sort-menu")[0] != undefined){
					widget.left = dojo.position(dojo.query("#compare-panel")[0]).x;						
					var iniTTop = (dojo.coords(dojo.query(".sort-menu")[0]).t + dojo.coords(dojo.query(".sort-menu")[0]).h);					
					if(iniTTop < 0){
					iniTTop = 0;
					}
					dojo.query("#compare-panel").style({'position':'absolute','top':'0px','left':'0'});
				}
			});
			if(dojo.query(".sort-menu")[0] != undefined){
				widget.left = dojo.position(dojo.query("#compare-panel")[0]).x;			
				dojo.query("#compare-panel").style({'position':'absolute','top':'0px','left':'0'});
			}
			
			dojo.connect(window, 'onscroll',function(){
	  
				var panelHt = (dojo.coords(dojo.query("#compare-panel")[0]).h + 10 );
				var pos = (dojo.coords(dojo.query(".remove_all_saved_holidays")[0]).y - panelHt );
				
				if(pos < 0){ 
					
					var bHt = (dojo.coords(dojo.query(".remove_all_saved_holidays")[0]).t + dojo.coords(dojo.query(".remove_all_saved_holidays")[0]).h * 2 - 10) - dojo.coords(dojo.query("#compare-panel")[0]).h;					
					dojo.query("#compare-panel").style({'position':'absolute','top': bHt+'px','left':'0'});					
				}
				else if(dojo.coords(dojo.query(".sort-menu")[0]).y < 0){					
					dojo.query("#compare-panel").style({'position':'fixed','top':'0','left':widget.left+'px'});								 
				}
				else if(dojo.coords(dojo.query(".sort-menu")[0]).y > 0){
					dojo.query("#compare-panel").style({'position':'absolute','top':'0px','left':'0'});					
				}
			
			});
		}
	});
	
	return tui.widget.customeraccount.scrollComparePanel;
});