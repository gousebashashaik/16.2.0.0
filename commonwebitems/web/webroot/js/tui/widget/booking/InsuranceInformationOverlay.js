define ("tui/widget/booking/InsuranceInformationOverlay", ["dojo",
                                            "dojo/_base/fx",
                                            "dojo/dom",
                                            "dojo/dom-class",
                                            "tui/widget/popup/DynamicPopup",
                                            "tui/widget/booking/GetPriceComponent"], function(dojo, fx,dom,domclass, dynamicPopup){

	dojo.declare("tui.widget.booking.InsuranceInformationOverlay", [tui.widget.popup.DynamicPopup], {

		stopDefaultOnCancel: false,

		// ---------------------------------------------------------------- methods
        postCreate: function(){
            var insuranceInformationOverlay = this;
            insuranceInformationOverlay.inherited(arguments);
            insuranceInformationOverlay.subscribe("tui/widget/search/CookieSearchSave/saveFormData", function(widget){
                if (widget instanceof tui.widget.booking.GetPriceComponent){
                	insuranceInformationOverlay.animateToLoader();
                }
            })
        },

        open: function(){
            var insuranceInformationOverlay = this;
            window.scrollTo(0,0);
            //tui.removeAllErrorPopups();
            insuranceInformationOverlay.inherited(arguments);
            var priceBreakDown = dom.byId("priceBrk");
            var priceBreakdownSticky = dojo.query(".noAccrodDef",priceBreakDown)[0];
            domclass.remove(priceBreakdownSticky,"stick");
        },

        close: function(){
            var insuranceInformationOverlay = this;
            insuranceInformationOverlay.inherited(arguments);
            dojo.publish("tui/widget/booking/insuranceInformationOverlay/close", [insuranceInformationOverlay]);
            
            dojo.query(".modal").style({
	    		   "display":"none"
	    		    
	    		});//written specifically for see whats covered background removal; 
        },

        animateToLoader: function(){
            var insuranceInformationOverlay = this;

            dojo.setStyle(insuranceInformationOverlay.popupDomNode, {
                "width": [dojo.style(insuranceInformationOverlay.popupDomNode, "width"), "px"].join(""),
                "height": [dojo.style(insuranceInformationOverlay.popupDomNode, "height"), "px"].join("")
            });

            var formDom = dojo.query(".check-prices", insuranceInformationOverlay.popupDomNode)[0];
            dojo.setStyle(formDom, "display", "none");

            var loadingDom = dojo.query(".search-loading")[0];
            var loadingDomWidth = dojo.style(loadingDom, "width") + dojo.style(loadingDom, "paddingRight") + dojo.style(loadingDom, "paddingLeft");
            var loadingDomHeight = dojo.style(loadingDom, "height") + dojo.style(loadingDom, "paddingTop") + dojo.style(loadingDom, "paddingBottom");

            fx.animateProperty({
                node: insuranceInformationOverlay.popupDomNode,
                properties: {
                    width: loadingDomWidth,
                    height: loadingDomHeight
                },
                onAnimate: function(){
                    insuranceInformationOverlay.posElement(insuranceInformationOverlay.popupDomNode);
                },
                onEnd: function(){
					dojo.setStyle(loadingDom, "z-index", "2001");
                    insuranceInformationOverlay.close();
                }
            }).play();

        }

	})

	return tui.widget.booking.InsuranceInformationOverlay;
})