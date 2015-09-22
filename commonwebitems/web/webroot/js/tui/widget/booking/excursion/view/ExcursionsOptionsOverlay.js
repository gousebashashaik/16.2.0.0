define("tui/widget/booking/excursion/view/ExcursionsOptionsOverlay", [
  "dojo/_base/declare",
  "tui/widget/booking/utils/BookFlowDynamicPopup",
  "dojo/dom",
  "dojo/dom-class",
  "dojo/query"  ,"tui/widget/media/HeroCarousel"
], function (declare, dynamicPopup,dom,domclass,query, HeroCarousel) {

  return declare("tui.widget.booking.excursion.view.ExcursionsOptionsOverlay", [dynamicPopup], {

		    stopDefaultOnCancel: false,

		    open: function () {
		     // window.scrollTo(0, 0);
		      this.inherited(arguments);
			  var priceBreakDown = dom.byId("priceBrk");
		      var priceBreakdownSticky = query(".noAccrodDef",priceBreakDown)[0];
		      domclass.remove(priceBreakdownSticky,"stick");
		    },

		    onOpen: function () {
		        var excursionsOverlay = this,
		          heroNode = query(".slideshow", excursionsOverlay.popupDomNode)[0];

		        // re-center popup when it resizes
		        excursionsOverlay.timer = setInterval(function(){
		          excursionsOverlay.resize();
		        }, 30);

		        if (excursionsOverlay.jsonData) {
		          // initialise carousel
		          excursionsOverlay.heroWidget = new HeroCarousel({
		            jsonData: excursionsOverlay.jsonData
		          }, heroNode);

		          var viewport =  query(".viewport", heroNode)[0];

		          dojo.style(viewport, "width", "440px");

		        }


		        setTimeout (function(){
		          dojo.removeClass(excursionsOverlay.popupDomNode, "loading");
		        }, 1000);
		              var closeLink = dojo.query('.close', excursionsOverlay.popupDomNode)[0];

		      },

		    close: function () {
		      this.inherited(arguments);
		    }

  		});
});