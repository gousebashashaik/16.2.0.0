define ("tui/widget/customeraccount/NameEmailUpdatemsg", [
        													"dojo",											  	
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
        													
        													
        							    			  ], function(dojo,  cookie, query, has, validate,check, arrayUtil, domStyle, focusUtil,domConstruct){

		dojo.declare("tui.widget.customeraccount.NameEmailUpdatemsg", [tui.widget._TuiBaseWidget], {
		postCreate: function() {
			this.chkLocalstorage();
			this.inherited(arguments);
		},
		updateMsg:'<p class="alert low-level generic top-alert">Your personal details has been updated.</p>',
		errorMsg:'<p class="alert low-level generic top-alert updatemsg">Your personal details not updated.</p>',
		chkLocalstorage:function(){
			var updater = this;
			var update=localStorage.getItem("detailsupdate");
			switch (update)
			{
				case "updated":
					domConstruct.place(updater.updateMsg, updater.id, "after");
					localStorage.removeItem("detailsupdate");
					break;
				case "error":
					domConstruct.place(updater.errorMsg, updater.id, "after");
					localStorage.removeItem("detailsupdate");
					break;
			}
		}
	});
	return tui.widget.customeraccount.NameEmailUpdatemsg;
});