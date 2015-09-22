define ("tui/widget/booking/HotelOptionsOverlay", ["dojo",
                                            "dojo/_base/fx",
                                            "tui/widget/popup/DynamicPopup",
                                            "tui/widget/booking/GetPriceComponent"], function(dojo, fx, dynamicPopup){

	dojo.declare("tui.widget.booking.HotelOptionsOverlay", [tui.widget.popup.DynamicPopup], {

		stopDefaultOnCancel: false,

		// ---------------------------------------------------------------- methods
        postCreate: function(){
            var HotelOptionsOverlay = this;
            HotelOptionsOverlay.inherited(arguments);
            HotelOptionsOverlay.subscribe("tui/widget/search/CookieSearchSave/saveFormData", function(widget){
                if (widget instanceof tui.widget.booking.GetPriceComponent){
                	HotelOptionsOverlay.animateToLoader();
                }
            })
        },

        open: function(){
            var HotelOptionsOverlay = this;
            window.scrollTo(0,0);
            //tui.removeAllErrorPopups();
            HotelOptionsOverlay.inherited(arguments);
        },

        close: function(){
            var HotelOptionsOverlay = this;
            HotelOptionsOverlay.inherited(arguments);
            dojo.publish("tui/widget/booking/HotelOptionsOverlay/close", [HotelOptionsOverlay]);
        },

        animateToLoader: function(){
            var HotelOptionsOverlay = this;

            dojo.setStyle(HotelOptionsOverlay.popupDomNode, {
                "width": [dojo.style(HotelOptionsOverlay.popupDomNode, "width"), "px"].join(""),
                "height": [dojo.style(HotelOptionsOverlay.popupDomNode, "height"), "px"].join("")
            });

            var formDom = dojo.query(".check-prices", HotelOptionsOverlay.popupDomNode)[0];
            dojo.setStyle(formDom, "display", "none");

            var loadingDom = dojo.query(".search-loading")[0];
            var loadingDomWidth = dojo.style(loadingDom, "width") + dojo.style(loadingDom, "paddingRight") + dojo.style(loadingDom, "paddingLeft");
            var loadingDomHeight = dojo.style(loadingDom, "height") + dojo.style(loadingDom, "paddingTop") + dojo.style(loadingDom, "paddingBottom");

            fx.animateProperty({
                node: HotelOptionsOverlay.popupDomNode,
                properties: {
                    width: loadingDomWidth,
                    height: loadingDomHeight
                },
                onAnimate: function(){
                	HotelOptionsOverlay.posElement(HotelOptionsOverlay.popupDomNode);
                },
                onEnd: function(){
					dojo.setStyle(loadingDom, "z-index", "2001");
					HotelOptionsOverlay.close();
                }
            }).play();

        }

	})

	return tui.widget.booking.HotelOptionsOverlay;
})