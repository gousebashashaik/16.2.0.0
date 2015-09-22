define ("tui/widget/booking/alternateflightsOverlay", ["dojo",
                                            "dojo/_base/fx",
                                            "dojo/query",
                                            "dojo/topic",
                                            "dojo/_base/lang",
                                            "tui/widget/popup/DynamicPopup",
                                            "tui/widget/booking/GetPriceComponent"], function(dojo, fx, query, topic, lang, dynamicPopup){

	dojo.declare("tui.widget.booking.alternateflightsOverlay", [tui.widget.popup.DynamicPopup], {

		stopDefaultOnCancel: false,

        
        
        onAfterTmplRender: function () {
            // summary:
            //		Overides onAfterTmplRender to add a close connect event listener for closing popup,
            //		when close link is selected.
            var popup = this;
            popup.inherited(arguments);
            query(popup.closeSelector, popup.popupDomNode).onclick(lang.hitch(this, function (event) {
                if (popup.stopDefaultOnCancel) {
                    dojo.stopEvent(event);
                }
                topic.publish("flightoptions.alternateflightsOverlay.getData");
                popup.close();
            }));
            query(popup.cancelSelector, popup.popupDomNode).onclick(function (event) {
                if (popup.stopDefaultOnCancel) {
                    dojo.stopEvent(event);
                }
                topic.publish("flightoptions.alternateflightsOverlay.getData");
                popup.close();
            });
        },

        addEventModalListener: function () {
            var popup = this;
            if (popup.modalConnect) return;
            popup.modalConnect = popup.connect(popup.modalDomNode, "onclick", lang.hitch(this,function (event) {
                if (popup.stopDefaultOnCancel) {
                    dojo.stopEvent(event);
                }
                topic.publish("flightoptions.alternateflightsOverlay.getData");
                popup.close();
            }));
        },

        // ---------------------------------------------------------------- tui.widget.popup.PopupBase methods


       


        open: function(){
            var alternateflightsOverlay = this;
            window.scrollTo(0,0);
            alternateflightsOverlay.inherited(arguments);
        },

        close: function(){
            var alternateflightsOverlay = this;
            alternateflightsOverlay.inherited(arguments);
            topic.publish("flightoptions.alternateflightsOverlay.close");
        }


	})

	return tui.widget.booking.alternateflightsOverlay;
});