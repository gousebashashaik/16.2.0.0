define ("tui/searchBPanel/view/GetPricePopup", [
        "dojo",
        "dojo/_base/fx",
        "dojo/dom",
        "dojo/dom-class",
        "dojo/has",
        "dojo/text!tui/searchBPanel/view/templates/getPricePopup.html",
        "tui/widget/popup/Popup"],
        function(dojo, fx, dom, domclass, has, popTMPL, popup){
		
	dojo.declare("tui.searchBPanel.view.GetPricePopup", [tui.widget.popup.Popup], {
		
		tmpl: popTMPL,		
		
		 subscribableMethods: ["open"],
		
		postCreate: function(){
			var getPricePopUp  = this;
	            getPricePopUp.inherited(arguments);	           
		},
		
		onOpen: function () {
            // summary:
            //		Override default method, fired when popup opens
            var getPricePopUp = this; 
            window.scrollTo(0, 0);
            setTimeout(function(){
            	 window.open(getPricePopUp.params.url);
			}, 2000);
           
            console.log(getPricePopUp.params.url);
        }
		
	});
	 

	
	return tui.searchBPanel.view.GetPricePopup;

});
