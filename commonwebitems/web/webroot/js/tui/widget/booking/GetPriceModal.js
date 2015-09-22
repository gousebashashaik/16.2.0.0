define ("tui/widget/booking/GetPriceModal", ["dojo",
                                            "dojo/_base/fx",
                                            "tui/widget/popup/DynamicPopup",
                                            "tui/widget/booking/GetPriceComponent"], function(dojo, fx, dynamicPopup){
	
	dojo.declare("tui.widget.booking.GetPriceModal", [tui.widget.popup.DynamicPopup], {

		stopDefaultOnCancel: false,
		
		// ---------------------------------------------------------------- methods
        postCreate: function(){
            var getPriceModal = this;
            getPriceModal.inherited(arguments);
            getPriceModal.subscribe("tui/widget/search/CookieSearchSave/saveFormData", function(widget){
                if (widget instanceof tui.widget.booking.GetPriceComponent){
                    getPriceModal.animateToLoader();
                }
            })
        },

        open: function(){
            var getPriceModal = this;
            window.scrollTo(0,0);
            tui.removeAllErrorPopups();
            getPriceModal.inherited(arguments);
        },

        close: function(){
            var getPriceModal = this;
            getPriceModal.inherited(arguments);
            dojo.publish("tui/widget/booking/GetPriceModal/close", [getPriceModal]);
        },

        animateToLoader: function(){
            var getPriceModal = this;

            dojo.setStyle(getPriceModal.popupDomNode, {
                "width": [dojo.style(getPriceModal.popupDomNode, "width"), "px"].join(""),
                "height": [dojo.style(getPriceModal.popupDomNode, "height"), "px"].join("")
            });

            var formDom = dojo.query(".check-prices", getPriceModal.popupDomNode)[0];
            dojo.setStyle(formDom, "display", "none");

            var loadingDom = dojo.query(".search-loading")[0];
            var loadingDomWidth = dojo.style(loadingDom, "width") + dojo.style(loadingDom, "paddingRight") + dojo.style(loadingDom, "paddingLeft");
            var loadingDomHeight = dojo.style(loadingDom, "height") + dojo.style(loadingDom, "paddingTop") + dojo.style(loadingDom, "paddingBottom");

            fx.animateProperty({
                node: getPriceModal.popupDomNode,
                properties: {
                    width: loadingDomWidth,
                    height: loadingDomHeight
                },
                onAnimate: function(){
                    getPriceModal.posElement(getPriceModal.popupDomNode);
                },
                onEnd: function(){
					dojo.setStyle(loadingDom, "z-index", "2001");
                    getPriceModal.close();
                }
            }).play();

        }

	})

	return tui.widget.booking.GetPriceModal;
})