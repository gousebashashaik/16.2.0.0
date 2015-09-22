define ("tui/widget/customeraccount/AjaxLoader", [
													"dojo",
													"dojo/on",
													"dojo/cookie",
													"dojo/query",
													"dojo/has",
													"dojo/_base/array",
													"dojo/dom-style",
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
													
													
													
							    			  ], function(dojo, on, cookie, query, has, arrayUtil, domStyle, domConstruct){
        
		dojo.declare("tui.widget.customeraccount.AjaxLoader", [tui.widget._TuiBaseWidget], {	
		loaderHtml:'<div id="loading-results"><img src="../images/big-loader.gif" alt="LOADING..."></div>',
		createLoader:function(){
			var widget=this;
			var loader=dojo.query('#loading-results');
			if(loader.length==0){
				dojo.place(widget.loaderHtml,dojo.query("body")[0],"last");
			}
			dojo.query('#loading-results').addClass("hide");
		},
		loadStart:function(){
			var widget=this;
			setTimeout(function(){			
			window.scrollTo(0,0);
			},500);
			widget.createLoader();
			dojo.query('#loading-results').removeClass("hide");
			dojo.query('#loading-results').addClass("hidex");
			
		},
		loadStop:function(){
			setTimeout(function(){
			dojo.query('#loading-results').removeClass("hidex");
			dojo.query('#loading-results').addClass("hide");
			}, 500);
			
		},
		postCreate: function() {
			
		}
	});
	return tui.widget.customeraccount.AjaxLoader;
});