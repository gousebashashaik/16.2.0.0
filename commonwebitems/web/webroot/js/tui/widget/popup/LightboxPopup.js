define("tui/widget/popup/LightboxPopup", [
    "dojo",
    "dojo/on",
    "dojo/_base/xhr",
    "dojo/dom-attr",
    "dojo/query",
    "dojo/dom-style",
    "dojo/window",
    "dojo/text!tui/widget/popup/templates/BookflowAttractionPopupTmpl.html",
    "tui/widget/media/HeroCarousel",
    "tui/widget/popup/Popup"], function (dojo, on, xhr, domAttr, query, domStyle, win, tmpl, HeroCarousel) {

    dojo.declare("tui.widget.popup.LightboxPopup", [tui.widget.popup.Popup], {

        modal: true,

        includeScroll: true,

        timer: null,

        jsonData: null,

        tmpl: tmpl,

        heroWidget: null,

        pageType: null,

        imageSize: "large",
		
		cdnDomain: dojoConfig.paths.cdnDomain,

        //cachedData: null,

        postCreate: function () {
            var lightboxPopup = this;
            // TODO: cache data to avoid repeat requests
            // TODO: make base "lightbox" class
            //lightboxPopup.cachedData = {};

            on(lightboxPopup.domNode, "a:click", function (event) {
                dojo.stopEvent(event);
                var target = (event.target.tagName.toLowerCase() === "img") ? query(event.target).parents("a")[0] : event.target;
                var href = domAttr.get(target, "href");
                if(lightboxPopup.pageType === "bookflow"){
                    lightboxPopup.requestData(href.replace("attraction", "bookattraction"));
                }else {
                    lightboxPopup.requestData(href);
                }
            });
            lightboxPopup.attachTag();
        },

        close: function () {
            var lightboxPopup = this;
            lightboxPopup.inherited(arguments);
            if (lightboxPopup.heroWidget) lightboxPopup.heroWidget.destroyRecursive();
            lightboxPopup.deleteScrollerPanel();
            dojo.destroy(lightboxPopup.popupDomNode);
            lightboxPopup.popupDomNode = null;
            if (lightboxPopup.timer) clearInterval(lightboxPopup.timer);
        },

        hideWidget: function () {
            var lightboxPopup = this;
            if (lightboxPopup.popupDomNode) domStyle.set(lightboxPopup.popupDomNode, "display", "none");
        },

        onBeforeAddScrollerPanel: function(popupDomNode, lightboxPopup) {
            var contentNode = query(".attraction-lightbox-content", popupDomNode)[0],
                scrollerNode = query(".scroller", popupDomNode)[0],
                winHeight = win.getBox().h,
                contentHeight = (dojo.position(contentNode).h - 24),
                finalHeight = (contentHeight <= (winHeight - 84)) ? contentHeight + 24 : (winHeight - 84),
                scrollerHeight = (finalHeight - dojo.position(query(".title", popupDomNode)[0]).h);

            domStyle.set(popupDomNode, "height", finalHeight + "px");
            domStyle.set(contentNode, "height", finalHeight + "px");
            domStyle.set(scrollerNode, "height", scrollerHeight + "px");

            // add scrollpanel if needed
            lightboxPopup.scrollable = contentHeight > (winHeight - 84);
        },

        onOpen: function () {
            var lightboxPopup = this,
                heroNode = query(".slideshow", lightboxPopup.popupDomNode)[0];

            // re-center popup when it resizes
            lightboxPopup.timer = setInterval(function(){
                lightboxPopup.resize();
            }, 30);

            if (lightboxPopup.jsonData.showCarousel) {
                // initialise carousel
                lightboxPopup.heroWidget = new HeroCarousel({
                    jsonData: lightboxPopup.jsonData.carousel
                }, heroNode);
            }

            setTimeout (function(){
                dojo.removeClass(lightboxPopup.popupDomNode, "loading");
            }, 1000);

            var closeLink = dojo.query('.close', lightboxPopup.popupDomNode)[0];
         	lightboxPopup.tagElement(closeLink, "productDetails");
         	lightboxPopup.tagElement(lightboxPopup.popupDomNode, "Attractions Lightbox");
        },

        requestData: function (url) {
            var lightboxPopup = this;
            xhr.get({
                url:url,
                handleAs: "json",
                error: function(err){
                    if(dojoConfig.devDebug){
                        throw "Ajax Error: " + err;
                    }
                },
                load: function(response, options) {
                    lightboxPopup.handleData(response, options);
                }
            });
        },

        handleData: function(promise, options) {
            var lightboxPopup = this;

            dojo.when(promise, function(response){
                lightboxPopup.jsonData = response;

                if(_.has(response, "editorial")){
                    if(_.has(response.editorial, "editorialContent")){
                        lightboxPopup.jsonData.showEditorial = true;
                    }
                } else {
                    lightboxPopup.jsonData.showEditorial = false;
                }

                if (_.has(response, "carousel")) {
                    // filter out unwanted image sizes
					if(lightboxPopup.jsonData.carousel != null){
                    lightboxPopup.jsonData.carousel.galleryImages = _.filter(lightboxPopup.jsonData.carousel.galleryImages, function(image){
                        return image.size === lightboxPopup.imageSize;
                    });

                    // add blank description if attribute is missing
                    lightboxPopup.jsonData.carousel.galleryImages = _.map(lightboxPopup.jsonData.carousel.galleryImages, function(image){
                        //return dojo.mixin({description: ""}, image);
                        image.description = (!image.description) ? '' : image.description;
                        return image;
                    });
                    lightboxPopup.jsonData.showCarousel = true;
					}else {
					 lightboxPopup.jsonData.showCarousel = false;
					}
                } else {
                    lightboxPopup.jsonData.showCarousel = false;
                }

                if(_.has(response, "thumbnailMap")){
                    if (response.thumbnailMap.featureCodesAndValues.longitude.length && response.thumbnailMap.featureCodesAndValues.latitude.length) {
                        lightboxPopup.jsonData.showMap = true;
                    }
                } else {
                    lightboxPopup.jsonData.showMap = false;
                }

                var usps = [];
                _.each(lightboxPopup.jsonData.usps, function(usp){
                    if(usp) {
                      _.has(usp.featureCodesAndValues, "suitableFor") ? usps.push(usp.featureCodesAndValues['suitableFor'][0]) : null;
                      (_.has(usp.featureCodesAndValues, "duration") && usp.featureCodesAndValues['duration'][0].toLowerCase() !== "ticket only") ? usps.push(usp.featureCodesAndValues['duration'][0]) : null;
                    }
                });
                lightboxPopup.jsonData.usps = usps;
                lightboxPopup.jsonData.showUsps = !_.isEmpty(usps);

                lightboxPopup.open();
            });
        }
    });

    return tui.widget.popup.LightboxPopup;
});