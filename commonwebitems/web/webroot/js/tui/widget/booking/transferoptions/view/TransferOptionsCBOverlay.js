define ("tui/widget/booking/transferoptions/view/TransferOptionsCBOverlay", [
     "dojo",
     "dojo/_base/fx",
     "tui/widget/booking/utils/BookFlowDynamicPopup",
     "dojo/dom",
	   "dojo/dom-class",
     "dojo/topic"], function(dojo, fx, dynamicPopup, dom, domclass, topic){

	dojo.declare("tui.widget.booking.transferoptions.view.TransferOptionsCBOverlay", [dynamicPopup], {

		stopDefaultOnCancel: false,
		modalClick: false,

		// ---------------------------------------------------------------- methods
        postCreate: function(){
            var transferOptionsCBOverlay = this;
            transferOptionsCBOverlay.inherited(arguments);
            
            
            transferOptionsCBOverlay.subscribe("tui/widget/search/CookieSearchSave/saveFormData", function(transferOptionsCBOverlay){
                if (widget instanceof tui.widget.booking.GetPriceComponent){
                	transferOptionsCBOverlay.animateToLoader();
                }
            })
            
        },

        open: function(){
            var transferOptionsCBOverlay = this;
            window.scrollTo(0,0);
			      var priceBreakDown = dom.byId("priceBrk");
           var priceBreakdownSticky = dojo.query(".noAccrodDef",priceBreakDown)[0];
           domclass.remove(priceBreakdownSticky,"stick");
            //tui.removeAllErrorPopups();
           transferOptionsCBOverlay.inherited(arguments);
        },


        close: function(){
        	var transferOptionsCBOverlay = this;
        	transferOptionsCBOverlay.inherited(arguments);
            dojo.publish("tui/widget/booking/transferOptionsCBOverlay/close", [transferOptionsCBOverlay]);
            
            if (transferOptionsCBOverlay.modalClick == true){
            	topic.publish("tui/widget/booking/transferOptionsCBOverlay/close/modalClick");
            	
            }
        },
        
        
        addEventModalListener: function(){
        	var transferOptionsCBOverlay = this;
        	transferOptionsCBOverlay.inherited(arguments);
        	
        	
        	
        	transferOptionsCBOverlay.modalClick =  true;
        }
  
	})

	return tui.widget.booking.transferoptions.view.TransferOptionsCBOverlay;
})