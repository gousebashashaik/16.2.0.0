define ("tui/widget/booking/carhire/florida/view/CarHireViewAllOverlay", [
     "dojo",
     "dojo/_base/fx",
     "tui/widget/popup/DynamicPopup",
     "dojo/dom",
	   "dojo/dom-class",
     "dojo/topic",
     "tui/widget/booking/GetPriceComponent"], function(dojo, fx, dynamicPopup, dom, domclass, topic){

	dojo.declare("tui.widget.booking.carhire.florida.view.CarHireViewAllOverlay", [tui.widget.popup.DynamicPopup], {

		stopDefaultOnCancel: false,

		// ---------------------------------------------------------------- methods
        postCreate: function(){
            var CarHireViewAllOverlay = this;
            CarHireViewAllOverlay.inherited(arguments);
            topic.subscribe("tui/booking/carhireclose",function(){
            	console.log("here");
            	CarHireViewAllOverlay.close();	
            })
            
            CarHireViewAllOverlay.subscribe("tui/widget/search/CookieSearchSave/saveFormData", function(CarHireViewAllOverlay){
                if (widget instanceof tui.widget.booking.GetPriceComponent){
                	CarHireViewAllOverlay.animateToLoader();
                }
            })
            
        },

        open: function(){
            var CarHireViewAllOverlay = this;
            window.scrollTo(0,0);
			      var priceBreakDown = dom.byId("priceBrk");
           var priceBreakdownSticky = dojo.query(".noAccrodDef",priceBreakDown)[0];
           domclass.remove(priceBreakdownSticky,"stick");
            //tui.removeAllErrorPopups();
           CarHireViewAllOverlay.inherited(arguments);
        },


        close: function(){
        	var CarHireViewAllOverlay = this;
        	CarHireViewAllOverlay.inherited(arguments);
            dojo.publish("tui/widget/booking/CarHireViewAllOverlay/close", [CarHireViewAllOverlay]);
        },

        animateToLoader: function(){
            var CarHireViewAllOverlay = this;

            dojo.setStyle(CarHireViewAllOverlay.popupDomNode, {
                "width": [dojo.style(CarHireViewAllOverlay.popupDomNode, "width"), "px"].join(""),
                "height": [dojo.style(CarHireViewAllOverlay.popupDomNode, "height"), "px"].join("")
            });

            var formDom = dojo.query(".check-prices", CarHireViewAllOverlay.popupDomNode)[0];
            dojo.setStyle(formDom, "display", "none");

            var loadingDom = dojo.query(".search-loading")[0];
            var loadingDomWidth = dojo.style(loadingDom, "width") + dojo.style(loadingDom, "paddingRight") + dojo.style(loadingDom, "paddingLeft");
            var loadingDomHeight = dojo.style(loadingDom, "height") + dojo.style(loadingDom, "paddingTop") + dojo.style(loadingDom, "paddingBottom");

            fx.animateProperty({
                node: CarHireViewAllOverlay.popupDomNode,
                properties: {
                    width: loadingDomWidth,
                    height: loadingDomHeight
                },
                onAnimate: function(){
                    CarHireViewAllOverlay.posElement(CarHireViewAllOverlay.popupDomNode);
                },
                onEnd: function(){
					dojo.setStyle(loadingDom, "z-index", "2001");
                    CarHireViewAllOverlay.close();
                }
            }).play();

        }

	})

	return tui.widget.booking.carhire.florida.view.CarHireViewAllOverlay;
})