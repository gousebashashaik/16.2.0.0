
define('tui/widget/popup/cruise/PortOfCallOverlay', [
  'dojo',
  'dojo/text!tui/widget/popup/cruise/templates/PortOfCallOverlayTmpl.html',
  "dojo/query",
  "dojo/dom-style",
  "dojo/dom-class",
  "dojo/dom-geometry",
  "tui/widget/media/cruise/CruiseHeroCarousel",
  'tui/widget/popup/cruise/SlidingTabs',
  'tui/widget/popup/Popup'], function(dojo, overlayTmpl, query, domStyle, domClass, domGeom, HeroCarousel, SlidingTabs) {

  dojo.declare('tui.widget.popup.cruise.PortOfCallOverlay', [tui.widget.popup.Popup, tui.widget.mixins.Templatable], {

    // ----------------------------------------------------------------------------- properties

    tmpl: overlayTmpl,

    jsonData:null,

    cdnDomain: dojoConfig.paths.cdnDomain,

    subscribableMethods: ["open", "close", "resize"],

    modal:true,

    heroWidget: null,

    includeScroll: true,

    displayPagination: false,

    slidingTabWidget:null,

    imageSize: "medium",

    touchSupport: dojo.hasClass(query("html")[0], "touch"),

    // ----------------------------------------------------------------------------- singleton

    open:function (){
    	var portOfCallOverlay = this;
    	if(portOfCallOverlay.jsonData.locationData === undefined ){
    		portOfCallOverlay.jsonData = {"locationData":portOfCallOverlay.jsonData};
    	}
    	portOfCallOverlay.inherited(arguments);
    },

	onOpen: function () {

		var portOfCallOverlay = this
        tabNode = query(".wrapper", portOfCallOverlay.popupDomNode)[0];
		if (portOfCallOverlay.jsonData.locationData.galleryImages.length > 0){
			portOfCallOverlay.heroWidget = new HeroCarousel({
	            jsonData: portOfCallOverlay.jsonData.locationData,
	            imagedefaultSize: portOfCallOverlay.imageSize,
	            useCustomSize: true,
	            height:200,
	            width:519
	        }, query(".slideshow", portOfCallOverlay.popupDomNode)[0]);
			portOfCallOverlay.heroWidget.setImageSize("519", "200");
		}
		//initialise Sliding Tabs
		if(portOfCallOverlay.jsonData.locationData.excursion.length > 0) {
			portOfCallOverlay.slidingTabWidget = new SlidingTabs({
	             jsonData: portOfCallOverlay.jsonData,
	             initialHeroWidget:  portOfCallOverlay.heroWidget,
	             imagedefaultSize: portOfCallOverlay.imageSize
	         }, tabNode);
		}


        setTimeout (function(){
        	domClass.remove(portOfCallOverlay.popupDomNode, "loading");
        }, 1000);

        // re-center popup when it resizes
        portOfCallOverlay.resize();

        var closeLink = query('.close', portOfCallOverlay.popupDomNode)[0];

        dojo.connect(query(".button", portOfCallOverlay.popupDomNode)[0], "click", function(){
			 setTimeout (function(){
				 portOfCallOverlay.close();
		        }, 1000);
		} );
    },

    resize: function(){
    	var overlay = this;
    	overlay.alignOverlay();
    },

    alignOverlay: function(){

    	var overlay = this;

    	var	excursionOverLay = domGeom.position(overlay.popupDomNode, true),
    		excursionDomNodeInfo = domGeom.position(query(".content", overlay.popupDomNode)[0], true),
    		lastX =  (excursionOverLay.w - excursionDomNodeInfo.w) / 2,
    		lastY =  (excursionOverLay.h - excursionDomNodeInfo.h) / 2;
    	if( lastY <= 10 ){
    		domStyle.set(query(".content", overlay.popupDomNode)[0], { top: "10px" });
    	}else{
    		domStyle.set(query(".content", overlay.popupDomNode)[0], { top: lastY + "px" });
    	}

    },

    close: function () {

        var portOfCallOverlay = this;
        portOfCallOverlay.inherited(arguments);
        if(portOfCallOverlay.heroWidget != undefined ){
            portOfCallOverlay.heroWidget.destroyRecursive();
        }
        if(portOfCallOverlay.slidingTabWidget != undefined ){
        	portOfCallOverlay.slidingTabWidget.destroyOnClose();
            portOfCallOverlay.slidingTabWidget.destroyRecursive();
        }
        portOfCallOverlay.deleteScrollerPanel();
        dojo.destroy(portOfCallOverlay.popupDomNode);
        portOfCallOverlay.popupDomNode = null;
        portOfCallOverlay.touchSupport && dojo.window.scrollIntoView(portOfCallOverlay.domNode);
    },

    hideWidget: function () {
        var portOfCallOverlay = this;
        if (portOfCallOverlay.popupDomNode) domStyle.set(portOfCallOverlay.popupDomNode, "display", "none");
    }

  });

  return tui.widget.popup.cruise.PortOfCallOverlay;
});
