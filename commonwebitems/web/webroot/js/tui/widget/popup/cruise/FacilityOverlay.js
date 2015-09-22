
define('tui/widget/popup/cruise/FacilityOverlay', [
  'dojo',
  'dojo/text!tui/widget/popup/cruise/templates/FacilityOverlayTmpl.html',
  'dojo/text!tui/widget/popup/cruise/templates/MenuOverlayTmpl.html',
  'dojo/text!tui/widget/popup/cruise/templates/ExcursionOverlayTmpl.html',
  'dojo/text!tui/widget/popup/cruise/templates/StayOverlayTmpl.html',
  "dojo/query",
  "dojo/on",
  "dojo/parser",
  "dojo/dom-geometry",
  "dojo/dom-style",
  "dojo/_base/Deferred",
  "tui/cruise/itineraryMap/service/ItineraryDetail",
  "tui/widget/media/cruise/CruiseHeroCarousel",
  'tui/widget/popup/Popup'], function(dojo, facilityTmpl, menuTmpl, ExcursionOverlayTmpl, StayOverlayTmpl, query, on, parser, domGeom, domStyle, Deferred, itineraryDetailService, HeroCarousel) {

  dojo.declare('tui.widget.popup.cruise.FacilityOverlay', [tui.widget.popup.Popup, tui.widget.mixins.Templatable], {

    // ----------------------------------------------------------------------------- properties

    tmpl: facilityTmpl,

    menuTmpl: menuTmpl,

    ExcursionTmpl: ExcursionOverlayTmpl,

    StayTmpl: StayOverlayTmpl,

    cdnDomain: dojoConfig.paths.cdnDomain,

    displayPagination: false,

    jsonData:null,

    subscribableMethods: ["open", "close", "resize"],

    modal:true,

    includeScroll: true,

    timer: null,

    heroWidget: null,

    imageSize: "medium",

    componentName:"",

    portName:null,

    touchSupport: dojo.hasClass(query("html")[0], "touch"),

    // ----------------------------------------------------------------------------- singleton

      addPopupEventListener: function () {
          var popupBase = this;
          if (!popupBase.domNode) return;
          popupBase.connect(popupBase.domNode, ["on", popupBase.eventType].join(""), function (e) {
              dojo.stopEvent(e);
              if(!_.isUndefined(popupBase.waitUntilPromise) && popupBase.waitUntilPromise){
                  var xhrResp = new itineraryDetailService({widgetNode: popupBase.domNode, getParent: function(){ return dojo.query(this.widgetNode).closest('div')[0]; } }).fetchData('', popupBase.jsonData.locationCode, false);
                  Deferred.when(xhrResp, function (response){
                      popupBase.jsonData = xhrResp.results[0];
                      popupBase.open();
                  });
              }
              else{
                  popupBase.open();
              }

          });

          if ((popupBase.eventType == "mouseenter") || (popupBase.eventType == "mouseover")) {
              popupBase.connect(popupBase.domNode, "onmouseout", function (e) {
                  dojo.stopEvent(e);
                  popupBase.close();
              });
          }
      },

    open:function (){
    	var overlay = this;
    	if( overlay.componentName != "menuOverlay"){
	    	overlay.jsonData.galleryImages = _.uniq(dojo.filter(overlay.jsonData.galleryImages, function(item) {
		        return (item.size === "medium");
		      }), 'code');
    	}

    	switch (overlay.componentName) {
	        case "menuOverlay":
	        	overlay.tmpl = overlay.menuTmpl;
	          break;
	        case "excursionOverlay":
	        	overlay.tmpl = overlay.ExcursionTmpl;
	        	overlay.jsonData["portName"] = overlay.portName;
	          break;
	        case "stayOverlay":
	        	overlay.tmpl = overlay.StayTmpl;
	          break;
	        default://facilites overlay
	        	overlay.tmpl = overlay.tmpl;
    	}
        overlay.jsonData["premierServicesExist"] = !(_.isNull(overlay.jsonData.premiereServices)) ;
    	overlay.inherited(arguments);
   },

	onOpen: function () {

		var overlay = this;
		if(overlay.componentName != "menuOverlay" && (overlay.jsonData.galleryImages.length > 0)){
			//initialise HeroCarousel
			overlay.initializeHeroCarousel();
		}

        setTimeout (function(){
            dojo.removeClass(overlay.popupDomNode, "loading");
        }, 1000);

        // re-center popup when it resizes
        //overlay.alignOverlay();
		if(overlay.componentName === "deckSVG"){
				parser.parse(overlay.popupDomNode);
		}

		overlay.alignOverlay();

		var closeLink = dojo.query('.close', overlay.popupDomNode)[0];

		dojo.connect(query(".button", overlay.popupDomNode)[0], "click", function(){
			 setTimeout (function(){
				 overlay.close();
		        }, 1000);
		} );

		//setTimeout(function(){ alert(query(".excursion", overlay.popupDomNode).length); }, 5000);

		//
		var imgs = query("img", overlay.popupDomNode)[0];
		imgs.onload = imgs ? function () { 	overlay.alignOverlay();  } : function () {}; // dojo.window.scrollIntoView(overlay.popupDomNode);

		// Listen for orientation changes
		window.addEventListener("orientationchange", function() {
			overlay.alignOverlay();
			setTimeout(function(){ overlay.alignOverlay(); }, 500);
		}, false);

		//IPAD popup disappearing issue fix
		setTimeout(function(){
			on.emit(window, "resize", {
				bubbles: true,
				cancelable: true
			});
		}, 350);
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
    		//lastY = (lastY?lastY : 0) + excursionOverLay.y - excursionDomNodeInfo.y + (excursionOverLay.h - excursionDomNodeInfo.h) / 2;
    		lastY =  (excursionOverLay.h - excursionDomNodeInfo.h) / 2;
    	if( lastY <= 10 ){
    		domStyle.set(query(".content", overlay.popupDomNode)[0], { top: "10px" });
    	}else{
    		domStyle.set(query(".content", overlay.popupDomNode)[0], { top: lastY + "px" });
    	}

    	//domStyle.set(query(".exursion-overlay", overlay.popupDomNode)[0], { position: "relative", width: "100%", height: "100%" });
    	//domStyle.set(query(".exursion-overlay", overlay.popupDomNode)[0], { position: "fixed", width: "100%", height: "100%" });
    },

    initializeHeroCarousel: function(){
    	var overlay = this;
    	overlay.heroWidget = new HeroCarousel({
            jsonData: overlay.jsonData,
            imagedefaultSize: overlay.imageSize,
            useCustomSize: true,
            height:200,
            width:519
        }, query(".slideshow", overlay.popupDomNode)[0]);
    },

    close: function () {
        var overlay = this;
        overlay.inherited(arguments);
        if(overlay.heroWidget != undefined ){
        	overlay.heroWidget.destroyRecursive();
        }
        dojo.destroy(overlay.popupDomNode);
        overlay.popupDomNode = null;
        overlay.touchSupport && dojo.window.scrollIntoView(overlay.domNode);
    },

    hideWidget: function () {
        var overlay = this;
        if (overlay.popupDomNode) domStyle.set(overlay.popupDomNode, "display", "none");
    }


  });

  return tui.widget.popup.cruise.FacilityOverlay;
});
