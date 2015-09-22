define ("tui/widget/booking/moreAboutOverlay", ["dojo",
                                            "dojo/_base/fx",
                                            "tui/widget/popup/DynamicPopup",
                                            "dojo/dom",
											"dojo/_base/xhr",
                                      	  	"dojo/dom-class",
											"tui/widget/booking/RoomBoard/modal/RoomAndBoard",
											"tui/widget/booking/allInclusive",
                                            "tui/widget/booking/GetPriceComponent"], function(dojo, fx, dynamicPopup,dom,xhr,domclass,RoomAndBoard){

	dojo.declare("tui.widget.booking.moreAboutOverlay", [tui.widget.popup.DynamicPopup], {

		stopDefaultOnCancel: false,

		// ---------------------------------------------------------------- methods
        postCreate: function(){
            var moreAboutOverlay = this;
            moreAboutOverlay.inherited(arguments);
            moreAboutOverlay.subscribe("tui/widget/search/CookieSearchSave/saveFormData", function(widget){
                if (widget instanceof tui.widget.booking.GetPriceComponent){
                	moreAboutOverlay.animateToLoader();
                }
            })
        },

        open: function(){
            var moreAboutOverlay = this;
            //window.scrollTo(0,0);
            var priceBreakDown = dom.byId("priceBrk");
            var priceBreakdownSticky = dojo.query(".noAccrodDef",priceBreakDown)[0];
            domclass.remove(priceBreakdownSticky,"stick");
            //tui.removeAllErrorPopups();

			if(_.isEmpty(RoomAndBoard.sportsActivities)){
			moreAboutOverlay.generateRequest();
			}
			 moreAboutOverlay.inherited(arguments);


        },
		generateRequest: function(){

		 var widget =this;
    	 dojo.addClass(dom.byId("top"), 'updating');
    	 dojo.addClass(dom.byId("main"), 'updating');
    	 dojo.addClass(dom.byId("right"), 'updating');
    	 console.log("ajax call");
    	 console.log(value);

    	 var results = xhr.post({
             url: " /holiday/AllInclusiveBoardComponentController/getOtherFacilities",
             handleAs: "json",
             headers: {Accept: "application/javascript, application/json"},
             error: function (jxhr,err) {
                 if (dojoConfig.devDebug) {
                     console.error(jxhr);
                 }
                 console.log(err.xhr.responseText);
                 widget.afterFailure(err.xhr.responseText);

             }
         });


    	 dojo.when(results,function(response){

    		 console.log("i am here")

    		RoomAndBoard.sportsActivities = response.sportsActivities;
			 RoomAndBoard.foodAndDrinks = response.foodAndDrinks;


			 RoomAndBoard.refWidget.contentRefresh(response);
			 //widget.inherited(arguments);
    		 dojo.removeClass(dom.byId("top"), 'updating');
    		 dojo.removeClass(dom.byId("main"), 'updating');
    		 dojo.removeClass(dom.byId("right"), 'updating');

    	 });

		},
        close: function(){
            var moreAboutOverlay = this;
            moreAboutOverlay.inherited(arguments);
            dojo.publish("tui/widget/booking/moreAboutOverlay/close", [moreAboutOverlay]);
        },

        animateToLoader: function(){
            var moreAboutOverlay = this;

            dojo.setStyle(moreAboutOverlay.popupDomNode, {
                "width": [dojo.style(moreAboutOverlay.popupDomNode, "width"), "px"].join(""),
                "height": [dojo.style(moreAboutOverlay.popupDomNode, "height"), "px"].join("")
            });

            var formDom = dojo.query(".check-prices", moreAboutOverlay.popupDomNode)[0];
            dojo.setStyle(formDom, "display", "none");

            var loadingDom = dojo.query(".search-loading")[0];
            var loadingDomWidth = dojo.style(loadingDom, "width") + dojo.style(loadingDom, "paddingRight") + dojo.style(loadingDom, "paddingLeft");
            var loadingDomHeight = dojo.style(loadingDom, "height") + dojo.style(loadingDom, "paddingTop") + dojo.style(loadingDom, "paddingBottom");

            fx.animateProperty({
                node: moreAboutOverlay.popupDomNode,
                properties: {
                    width: loadingDomWidth,
                    height: loadingDomHeight
                },
                onAnimate: function(){
                	moreAboutOverlay.posElement(moreAboutOverlay.popupDomNode);
                },
                onEnd: function(){
					dojo.setStyle(loadingDom, "z-index", "2001");
					moreAboutOverlay.close();
                }
            }).play();

        }

	})

	return tui.widget.booking.moreAboutOverlay;
})