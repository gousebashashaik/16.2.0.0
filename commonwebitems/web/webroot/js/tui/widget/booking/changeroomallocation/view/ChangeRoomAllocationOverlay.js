define ("tui/widget/booking/changeroomallocation/view/ChangeRoomAllocationOverlay", [
     "dojo",
     "dojo/_base/fx",
     "tui/widget/popup/DynamicPopup",
     "dojo/dom",
	  "dojo/dom-class",
     "dojo/topic"], function(dojo, fx, dynamicPopup,dom,domclass,topic){

	dojo.declare("tui.widget.booking.changeroomallocation.view.ChangeRoomAllocationOverlay", [tui.widget.popup.DynamicPopup], {

		stopDefaultOnCancel: false,

		// ---------------------------------------------------------------- methods
        postCreate: function(){
            var ChangeRoomAllocationOverlay = this;
            ChangeRoomAllocationOverlay.inherited(arguments);
            ChangeRoomAllocationOverlay.subscribe("tui/widget/search/CookieSearchSave/saveFormData", function(widget){
                if (widget instanceof tui.widget.booking.GetPriceComponent){
                    ChangeRoomAllocationOverlay.animateToLoader();
                }
            })
            topic.subscribe("some/topic/closeOverlay", function(){
    	        console.log("received:");
    	        console.log(this);
    	        ChangeRoomAllocationOverlay.close();
    	    });
        },

        open: function(){
            var ChangeRoomAllocationOverlay = this;
            //window.scrollTo(0,0);
			var priceBreakDown = dom.byId("priceBrk");
           var priceBreakdownSticky = dojo.query(".noAccrodDef",priceBreakDown)[0];
           /*domclass.remove(priceBreakdownSticky,"stick");*/
            //tui.removeAllErrorPopups();
            ChangeRoomAllocationOverlay.inherited(arguments);
        },


        close: function(){
        	var ChangeRoomAllocationOverlay = this;

            topic.publish("tui/widget/ChangeRoomAllocationOverlay/close");
            dojo.publish("tui/widget/booking/ChangeRoomAllocationOverlay/close", [ChangeRoomAllocationOverlay]);
            ChangeRoomAllocationOverlay.inherited(arguments);
        },

        animateToLoader: function(){
            var ChangeRoomAllocationOverlay = this;

            dojo.setStyle(ChangeRoomAllocationOverlay.popupDomNode, {
                "width": [dojo.style(ChangeRoomAllocationOverlay.popupDomNode, "width"), "px"].join(""),
                "height": [dojo.style(ChangeRoomAllocationOverlay.popupDomNode, "height"), "px"].join("")
            });

            var formDom = dojo.query(".check-prices", ChangeRoomAllocationOverlay.popupDomNode)[0];
            dojo.setStyle(formDom, "display", "none");

            var loadingDom = dojo.query(".search-loading")[0];
            var loadingDomWidth = dojo.style(loadingDom, "width") + dojo.style(loadingDom, "paddingRight") + dojo.style(loadingDom, "paddingLeft");
            var loadingDomHeight = dojo.style(loadingDom, "height") + dojo.style(loadingDom, "paddingTop") + dojo.style(loadingDom, "paddingBottom");

            fx.animateProperty({
                node: ChangeRoomAllocationOverlay.popupDomNode,
                properties: {
                    width: loadingDomWidth,
                    height: loadingDomHeight
                },
                onAnimate: function(){
                    ChangeRoomAllocationOverlay.posElement(ChangeRoomAllocationOverlay.popupDomNode);
                },
                onEnd: function(){
					dojo.setStyle(loadingDom, "z-index", "2001");
                    ChangeRoomAllocationOverlay.close();
                }
            }).play();

        }

	})

	return tui.widget.booking.changeroomallocation.view.ChangeRoomAllocationOverlay;
})