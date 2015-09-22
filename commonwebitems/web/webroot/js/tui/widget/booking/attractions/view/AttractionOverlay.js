define("tui/widget/booking/attractions/view/AttractionOverlay", [
  "dojo/_base/declare",
  "dojo/query",
  "dojo/topic",
  "tui/widget/booking/utils/BookFlowDynamicPopup",
  "dojo/dom",
  "dojo/dom-class",
  "tui/widget/media/HeroCarousel"

], function (declare, query, topic, dynamicPopup,dom,domclass,HeroCarousel) {

  return declare("tui.widget.booking.attractions.view.AttractionOverlay", [dynamicPopup], {

    stopDefaultOnCancel: false,

    postCreate: function () {
      var attractionOverlay = this;
      attractionOverlay.inherited(arguments);
	  var priceBreakDown = dom.byId("priceBrk");
      var priceBreakdownSticky = query(".noAccrodDef",priceBreakDown)[0];
      domclass.remove(priceBreakdownSticky,"stick");
    },

    open: function () {
      var attractionOverlay = this;
      //window.scrollTo(0, 0);
      attractionOverlay.inherited(arguments);
      var priceBreakDown = dom.byId("priceBrk");
      var priceBreakdownSticky = query(".noAccrodDef",priceBreakDown)[0];
      domclass.remove(priceBreakdownSticky,"stick");
    },

    onOpen: function () {
        var attractionOverlay = this,
          heroNode = query(".slideshow", attractionOverlay.popupDomNode)[0];

        // re-center popup when it resizes
        attractionOverlay.timer = setInterval(function(){
        	attractionOverlay.resize();
        }, 30);

        if (attractionOverlay.jsonData.extraContent) {
          // initialise carousel
        	attractionOverlay.heroWidget = new HeroCarousel({
            jsonData: attractionOverlay.jsonData.extraContent
          }, heroNode);



        }

        var viewport =  query(".viewport", heroNode)[0];

        dojo.style(viewport, "width", "440px");

        setTimeout (function(){
          dojo.removeClass(attractionOverlay.popupDomNode, "loading");
        }, 1000);
              var closeLink = dojo.query('.close', attractionOverlay.popupDomNode)[0];

      },

    close: function () {
      var attractionOverlay = this;
      attractionOverlay.inherited(arguments);
    }


  });
});