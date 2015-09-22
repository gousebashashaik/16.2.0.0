define ("tui/widget/booking/dolphinEncounterOverlay", ["dojo",
                                            "dojo/_base/fx",
                                            "tui/widget/popup/DynamicPopup",
                                            "tui/widget/booking/GetPriceComponent"], function(dojo, fx, dynamicPopup){

	dojo.declare("tui.widget.booking.dolphinEncounterOverlay", [tui.widget.popup.DynamicPopup], {

		stopDefaultOnCancel: false,

		// ---------------------------------------------------------------- methods
        postCreate: function(){
            var dolphinEncounterOverlay = this;
            dolphinEncounterOverlay.inherited(arguments);
            dolphinEncounterOverlay.subscribe("tui/widget/search/CookieSearchSave/saveFormData", function(widget){
                if (widget instanceof tui.widget.booking.GetPriceComponent){
                	dolphinEncounterOverlay.animateToLoader();
                }
            })
        },

        open: function(){
            var dolphinEncounterOverlay = this;
            window.scrollTo(0,0);
            //tui.removeAllErrorPopups();
            dolphinEncounterOverlay.inherited(arguments);
        },

        close: function(){
            var dolphinEncounterOverlay = this;
            dolphinEncounterOverlay.inherited(arguments);
            dojo.publish("tui/widget/booking/dolphinEncounterOverlay/close", [dolphinEncounterOverlay]);
        },

        animateToLoader: function(){
            var dolphinEncounterOverlay = this;

            dojo.setStyle(dolphinEncounterOverlay.popupDomNode, {
                "width": [dojo.style(dolphinEncounterOverlay.popupDomNode, "width"), "px"].join(""),
                "height": [dojo.style(dolphinEncounterOverlay.popupDomNode, "height"), "px"].join("")
            });

            var formDom = dojo.query(".check-prices", dolphinEncounterOverlay.popupDomNode)[0];
            dojo.setStyle(formDom, "display", "none");

            var loadingDom = dojo.query(".search-loading")[0];
            var loadingDomWidth = dojo.style(loadingDom, "width") + dojo.style(loadingDom, "paddingRight") + dojo.style(loadingDom, "paddingLeft");
            var loadingDomHeight = dojo.style(loadingDom, "height") + dojo.style(loadingDom, "paddingTop") + dojo.style(loadingDom, "paddingBottom");

            fx.animateProperty({
                node: dolphinEncounterOverlay.popupDomNode,
                properties: {
                    width: loadingDomWidth,
                    height: loadingDomHeight
                },
                onAnimate: function(){
                	dolphinEncounterOverlay.posElement(dolphinEncounterOverlay.popupDomNode);
                },
                onEnd: function(){
					dojo.setStyle(loadingDom, "z-index", "2001");
					dolphinEncounterOverlay.close();
                }
            }).play();

        }

	})

	return tui.widget.booking.dolphinEncounterOverlay;
})