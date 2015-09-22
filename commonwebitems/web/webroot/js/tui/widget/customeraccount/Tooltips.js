define("tui/widget/customeraccount/Tooltips", ["dojo", 
									 "dojo/text!tui/widget/popup/templates/CaTooltip.html",
                                     "dojo/dom-style",
									 "dojo/query",
									 "tui/widget/customeraccount/PopupBase",
									"dojox/html/entities",
									 "dojo/NodeList-traverse"],
									 function (dojo, tooltipTmpl, domStyle, query) {
    dojo.declare("tui.widget.customeraccount.Tooltips", [tui.widget.customeraccount.PopupBase], {
		
		 // ---------------------------------------------------------------- tui.widget.mixins.Templatable properties
		 								 	
        tmpl: tooltipTmpl,

        zIndex:4000,
		 
		 // ---------------------------------------------------------------- tui.widget.popup.Tooltips properties
		 
        text: "",
        
        loggedOut:null,

	    postMixInProperties: function () {
		    var tooltips = this;
		    if(tooltips.text && !dojo.isString(tooltips.text)){
			    tooltips.text = dojo.trim(dojo.byId(tooltips.text.refId).innerHTML);
		    }
		    tooltips.inherited(arguments);
	    },
		 
		 // ---------------------------------------------------------------- ttui.widget.customeraccount.PopupBase method
					 
		setPosOffset: function(position){
			var tooltips = this;
			switch(position){
				case "position-top-center":
					tooltips.posOffset = {top: -8, left: 0};
				break;
				case "position-bottom-center":
					tooltips.posOffset = {top: 8, left: 0};
  				break;
				case "position-top-right":
					tooltips.posOffset = {top: 50, left: 12};
					break;
				case "position-right-center":
					 tooltips.posOffset = {top: 5, left: -12};
			          break;
				    case "position-right-center-far":
                    tooltips.posOffset = {top: 32, left: 32};
                    break;	
			}
		},

        onAfterTmplRender: function () {
            var tooltips = this;
            tooltips.inherited(arguments);
            domStyle.set(tooltips.popupDomNode, {
                zIndex:tooltips.zIndex
            })
        }
	})
	
	return tui.widget.customeraccount.Tooltips;
})