define("tui/widget/booking/themeParkOverlay", [
    "dojo",
    "dojo/_base/lang",
    "dojo/_base/fx",
    "tui/utils/RequestAnimationFrame",
    "tui/widget/popup/DynamicPopup"], function (dojo, lang, fx) {

    dojo.declare("tui.widget.booking.themeParkOverlay", [tui.widget.popup.DynamicPopup], {

        // ---------------------------------------------------------------- properties

    	stopDefaultOnCancel: false,

        // ---------------------------------------------------------------- methods

        postCreate: function(){
            var stageAcademyOverlay = this;
            stageAcademyOverlay.inherited(arguments);
            stageAcademyOverlay.subscribe("tui/widget/search/CookieSearchSave/saveFormData", function(widget){
                if (widget instanceof tui.widget.booking.GetPriceComponent){
                	stageAcademyOverlay.animateToLoader();
                }
            })
        },

        open: function(){
            var stageAcademyOverlay = this;
            window.scrollTo(0,0);
            //tui.removeAllErrorPopups();
            stageAcademyOverlay.inherited(arguments);
        },

        close: function(){
            var stageAcademyOverlay = this;
            stageAcademyOverlay.inherited(arguments);
            dojo.publish("tui/widget/booking/stageAcademyOverlay/close", [stageAcademyOverlay]);
        },

        animateToLoader: function(){
            var stageAcademyOverlay = this;

            dojo.setStyle(stageAcademyOverlay.popupDomNode, {
                "width": [dojo.style(stageAcademyOverlay.popupDomNode, "width"), "px"].join(""),
                "height": [dojo.style(stageAcademyOverlay.popupDomNode, "height"), "px"].join("")
            });

            var formDom = dojo.query(".check-prices", stageAcademyOverlay.popupDomNode)[0];
            dojo.setStyle(formDom, "display", "none");

            var loadingDom = dojo.query(".search-loading")[0];
            var loadingDomWidth = dojo.style(loadingDom, "width") + dojo.style(loadingDom, "paddingRight") + dojo.style(loadingDom, "paddingLeft");
            var loadingDomHeight = dojo.style(loadingDom, "height") + dojo.style(loadingDom, "paddingTop") + dojo.style(loadingDom, "paddingBottom");

            fx.animateProperty({
                node: stageAcademyOverlay.popupDomNode,
                properties: {
                    width: loadingDomWidth,
                    height: loadingDomHeight
                },
                onAnimate: function(){
                	stageAcademyOverlay.posElement(stageAcademyOverlay.popupDomNode);
                },
                onEnd: function(){
					dojo.setStyle(loadingDom, "z-index", "2001");
					stageAcademyOverlay.close();
                }
            }).play();

        }

	});

    return tui.widget.booking.themeParkOverlay;
});