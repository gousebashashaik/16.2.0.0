
define('tui/widget/popup/cruise/StayOverlay', [
  'dojo',
  'dojo/text!tui/widget/popup/cruise/templates/StayOverlayTmpl.html',
  "dojo/query",
  "dojo/dom-style",
  "dojo/dom-class",
  "tui/widget/media/cruise/CruiseHeroCarousel",
  'tui/widget/popup/Popup'], function(dojo, overlayTmpl, query, domStyle, domClass, HeroCarousel) {

  dojo.declare('tui.widget.popup.cruise.StayOverlay', [tui.widget.popup.Popup, tui.widget.mixins.Templatable], {

    // ----------------------------------------------------------------------------- properties

    tmpl: overlayTmpl,

    jsonData:null,

    cdnDomain: dojoConfig.paths.cdnDomain,

    subscribableMethods: ["open", "close", "resize"],

    modal:true,

    includeScroll: true,

    displayPagination: false,

    imageSize: "medium",

    // ----------------------------------------------------------------------------- singleton

	onOpen: function () {

		var stayOverlay = this
        //initialise HeroCarousel
		stayOverlay.heroWidget = new HeroCarousel({
            jsonData: stayOverlay.jsonData,
            imagedefaultSize: stayOverlay.imageSize
        }, query(".slideshow", stayOverlay.popupDomNode)[0]);

        setTimeout (function(){
        	domClass.remove(stayOverlay.popupDomNode, "loading");
        }, 1000);
        // re-center popup when it resizes
        stayOverlay.resize();

        var closeLink = query('.close', stayOverlay.popupDomNode)[0];
    },

    close: function () {
        var stayOverlay = this;
        stayOverlay.inherited(arguments);
        stayOverlay.heroWidget.destroyRecursive();
        dojo.destroy(stayOverlay.popupDomNode);
        stayOverlay.popupDomNode = null;
    },

    hideWidget: function () {
        var stayOverlay = this;
        if (stayOverlay.popupDomNode) domStyle.set(stayOverlay.popupDomNode, "display", "none");
    }

  });

  return tui.widget.popup.cruise.StayOverlay;
});
