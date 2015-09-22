define ("tui/widget/booking/whatsincludedOverlay", ["dojo",
                                            "dojo/_base/fx",
                                            "tui/widget/popup/DynamicPopup",
                                            "tui/widget/booking/GetPriceComponent"], function(dojo, fx, dynamicPopup){

	dojo.declare("tui.widget.booking.whatsincludedOverlay", [tui.widget.popup.DynamicPopup], {

		stopDefaultOnCancel: false,

		// ---------------------------------------------------------------- methods
        postCreate: function(){
            var whatsincludedOverlay = this;
            whatsincludedOverlay.inherited(arguments);
            whatsincludedOverlay.subscribe("tui/widget/search/CookieSearchSave/saveFormData", function(widget){
                if (widget instanceof tui.widget.booking.GetPriceComponent){
                	whatsincludedOverlay.animateToLoader();
                }
            })
        },

        open: function(){
            var whatsincludedOverlay = this;
            window.scrollTo(0,0);
            //tui.removeAllErrorPopups();
            whatsincludedOverlay.inherited(arguments);
        },

        close: function(){
            var whatsincludedOverlay = this;
            whatsincludedOverlay.inherited(arguments);
            dojo.publish("tui/widget/booking/whatsincludedOverlay/close", [whatsincludedOverlay]);
        },

        animateToLoader: function(){
            var whatsincludedOverlay = this;

            dojo.setStyle(whatsincludedOverlay.popupDomNode, {
                "width": [dojo.style(whatsincludedOverlay.popupDomNode, "width"), "px"].join(""),
                "height": [dojo.style(whatsincludedOverlay.popupDomNode, "height"), "px"].join("")
            });

            var formDom = dojo.query(".check-prices", whatsincludedOverlay.popupDomNode)[0];
            dojo.setStyle(formDom, "display", "none");

            var loadingDom = dojo.query(".search-loading")[0];
            var loadingDomWidth = dojo.style(loadingDom, "width") + dojo.style(loadingDom, "paddingRight") + dojo.style(loadingDom, "paddingLeft");
            var loadingDomHeight = dojo.style(loadingDom, "height") + dojo.style(loadingDom, "paddingTop") + dojo.style(loadingDom, "paddingBottom");

            fx.animateProperty({
                node: whatsincludedOverlay.popupDomNode,
                properties: {
                    width: loadingDomWidth,
                    height: loadingDomHeight
                },
                onAnimate: function(){
                    whatsincludedOverlay.posElement(whatsincludedOverlay.popupDomNode);
                },
                onEnd: function(){
					dojo.setStyle(loadingDom, "z-index", "2001");
                    whatsincludedOverlay.close();
                }
            }).play();

        }

	})

	return tui.widget.booking.whatsincludedOverlay;
})