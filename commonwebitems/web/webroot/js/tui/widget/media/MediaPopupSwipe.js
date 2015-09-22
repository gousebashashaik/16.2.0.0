define("tui/widget/media/MediaPopupSwipe", [
    'dojo',
    'dojo/on',
    'dojo/dom-attr',
    'dojo/_base/fx',
    'dojo/_base/array',
    'dojo/text!tui/widget/media/templates/mediaTmplSwipe.html',
    'tui/widget/popup/Popup',
    'tui/widget/maps/Mappers',
    'dojo/query',
    'dojo/window',
    'dojo/dom-style',
    'dojo/dom-class',
    'tui/common/DomUtil',
    'dojo/has',
    'tui/widget/_TuiBaseWidget',
    'dojo/NodeList-traverse'
], function(dojo, on, domAttr, baseFx, array, mediaTmpl, popup, mappers, query, win, domStyle, domClass, domUtil, has) {

    function onImageLoad(img, handler) {
        on.once(img, 'load', handler);
    }

    function calulateCombinedWidth(xs) {
        var width = 0;
        _.each(xs, function(el) {
            width += domStyle.get(el, 'width')
        });
        return width;
    }


    dojo.declare("tui.widget.media.MediaPopupSwipe", [tui.widget.popup.Popup, tui.widget._TuiBaseWidget], {

        //---------------------------------------------- properties

        jsonData: null,

        modal: true,

        autoplaymode: true,

        autoplaygap: 8000,

        pause: false,

        tmpl: mediaTmpl,

        thumbImages: null,

        mediaPopup: null,

        current: 0,

        thumbViewport: null,

        thumbViewportWidth: null,

        itemContainer: null,

        itemContainerWidth: null,

        itemElementWidth: 96,

        slidesNo: null,

        currentSlide: 0,

        slideAmount: 990,

        slideAmountforIpad: 792,

        start: 0,

        end: 0,

        includeScroll: true,

        dataPath: null,

        loadDelay: 5000,

        scroller: null,

        thumbNailScroller: null,

        isOpen: false,

        noOfImages: 0,

        imageContainerNode: null,

        imageWidth: 0,

        carouselConfig: {
            'scrollX': true,
            'scrollY': false,
            'keyBindings': false,
            'snap': 'li',
            'snapSpeed': 500,
            'snapThreshold': 0.15,
            'momentum': false,
            'tap': 'scrollerTap',
            'zoom': true,
            'eventPassthrough': true,
            'freeScroll': true
        },

        //--------------------------------------------------------------methods

        postCreate: function() {
            var mediaPopup = this;
            mediaPopup.inherited(arguments);
            //console.log(mediaPopup.jsonData[mediaPopup.dataPath])
            mediaPopup.setPopJsonData();
            mediaPopup.jsonData = mappers.thumbnailSet(mediaPopup.jsonData);
            mediaPopup.jsonData.galleryImagesLength = _.size(mediaPopup.jsonData.galleryImages);
        },

        setPopJsonData: function() {
            var mediaPopup = this;
            mediaPopup.jsonData = mediaPopup.dataPath ? mediaPopup.jsonData[mediaPopup.dataPath] : mediaPopup.jsonData;
        },

        onAfterTmplRender: function() {
            var mediaPopup = this;
            mediaPopup.inherited(arguments);
            mediaPopup.addEventListener();
            var timer = setTimeout(function() {
                mediaPopup.hideLoading();
            }, mediaPopup.loadDelay);

            // mediaPopup.startAutoplay();
            mediaPopup.imageContainerNode = _.first(query(".product-list", mediaPopup.popupDomNode));
            var img = _.first(query('img', _.first(mediaPopup.listItems())));
            var thumbImage = _.first(query('img', _.first(mediaPopup.thumbNailListItems())))

            var images = dojo.query('.product-list li', mediaPopup.popupDomNode).forEach(function(item, i) {
                domAttr.set(item, "idx", i);
                if (i === 0) {
                    mediaPopup.getcaption(item);
                  }
            });

            // hardcoding the height of the mediapopup depending on the view port height. ToDo: bring responsive desighn into this component too.

            var vs = win.getBox();
            if (vs.h < 650) {
                var large = dojo.query('.large-carousel', mediaPopup.popupDomNode)[0];
                var largeHeight = dojo.style(large, "height");
                var height = [(largeHeight - ((650 - vs.h) + 50)), 'px'].join("");
                dojo.style(large, {
                    "height": height
                });
            }

            if (mediaPopup.jsonData.galleryImagesLength > 1) {
                var thumb = dojo.query('ul.plist img', mediaPopup.popupDomNode).forEach(function(item, i) {
                    domAttr.set(item, "idx", i);
                });
                // calculating the max number of slides for the thumb Carousel

                mediaPopup.thumbViewport = dojo.query('.thumb-carousel', mediaPopup.popupDomNode)[0];
                mediaPopup.thumbViewportWidth = dojo.style(mediaPopup.thumbViewport, 'width');

                mediaPopup.itemContainer = dojo.query('ul.plist', mediaPopup.popupDomNode)[0];
                mediaPopup.itemContainerWidth = mediaPopup.itemElementWidth * thumb.length;
                mediaPopup.slidesNo = Math.ceil(mediaPopup.itemContainerWidth / mediaPopup.thumbViewportWidth);
            }

            var handler = function() {
                setTimeout(function() {
                	var image = null;
                    mediaPopup.resize();
                    mediaPopup.windowSlidedListener();
                    image = _.first(query("li[idx=" + mediaPopup.scroller.currentPage.pageX + "]", mediaPopup.popupDomNode));
            		mediaPopup.getcaption(image);
                }, 1000)
            };

            var thumbHandler = function() {
                setTimeout(function() {
                    mediaPopup.resizeThumbNail();
                }, 1000)
            };

            onImageLoad(img, handler);
            onImageLoad(thumbImage, thumbHandler);
            domStyle.set(_.first(query(".prev-large",mediaPopup.popupDomNode)),"display", "none");
        },


        refreshList: function() {
            var mediaPopup = this;
            var viewPortWidth = mediaPopup.determineViewPortWidth();
            _.each(query('li', mediaPopup.getViewPortContent()), function(el) {
                domStyle.set(el, 'width', viewPortWidth + 'px');
            });
            var listWidth = mediaPopup.determineListWidth();
            var numberOfWindows = Math.ceil(listWidth / viewPortWidth);
            domStyle.set(mediaPopup.getViewPortContent(), 'width', listWidth + 'px');
            mediaPopup.noOfImages = numberOfWindows;
        },

        thumbNailRefreshList: function() {
            var mediaPopup = this,
            	maxThumbNailImage = 0;
            var thumbNailViewPortWidth = mediaPopup.determineThumbNailViewPortWidth();
            _.each(query('li', mediaPopup.getThumbNailViewPortContent()), function(el) {
                domStyle.set(el, 'width', thumbNailViewPortWidth + 'px');
            });
            // TO-DO need to find better logic for setting width
            maxThumbNailImage = 10;
            if(has("touch")){
            	maxThumbNailImage = 7.5;
            }
            domStyle.set(mediaPopup.getThumbNailViewPortContent(), 'width',  '100%');
            var listWidth = parseInt(domStyle.getComputedStyle(mediaPopup.getThumbNailViewPortContent()).width.split("px")[0]) + (query('li', mediaPopup.getThumbNailViewPortContent()).length - maxThumbNailImage) * (thumbNailViewPortWidth + 1);
            domStyle.set(mediaPopup.getThumbNailViewPortContent(), 'width',  listWidth + 'px');
        },

        resizeThumbNail: function() {
        	var mediaPopup = this;
            mediaPopup.thumbNailRefreshList();
        	mediaPopup.thumbNailScroller == null ? mediaPopup.thumbNailScroller = new IScroll(mediaPopup.getThumbNailViewPort(), mediaPopup.carouselConfig) : mediaPopup.thumbNailScroller.refresh();
        },

        resize: function() {
            var mediaPopup = this;
            mediaPopup.refreshList();
            mediaPopup.scroller == null ? mediaPopup.scroller = new IScroll(mediaPopup.getViewPort(), mediaPopup.carouselConfig) : mediaPopup.scroller.refresh();
        },

        getViewPortContent: function() {
            var gallery = this;
            var viewPortContent = _.first(query('ul', gallery.getViewPort()));
            return viewPortContent;
        },

        getThumbNailViewPortContent: function() {
            var gallery = this;
            var viewPortContent = _.first(query('ul', gallery.getThumbNailViewPort()));
            return viewPortContent;
        },

        listItems: function() {
            return query('li', this.getViewPortContent());
        },

        thumbNailListItems: function() {
            return query('li', this.getThumbNailViewPortContent());
        },

        determineViewPortWidth: function() {
            var mediaPopup = this;
            var clientWidth = _.first(query('li img', this.getViewPortContent())).clientWidth;
            return clientWidth;
        },

        determineThumbNailViewPortWidth: function() {
            var mediaPopup = this;
            var clientWidth = _.first(query('li img', this.getThumbNailViewPortContent())).clientWidth;
            return clientWidth;
        },

        determineListWidth: function() {
            var mediaPopup = this;
            return calulateCombinedWidth(mediaPopup.listItems());
        },

        determineThumbNailListWidth: function() {
        	var mediaPopup = this;
        	return calulateCombinedWidth(mediaPopup.thumbNailListItems());
        },

        initSwipable: function() {
            this.isOpen ? [this.resize(), this.resizeThumbNail()] : '';
        },

        windowSlidedListener: function() {
            var mediaPopup = this;
            domUtil.onWindowResize(mediaPopup.initSwipable.bind(this));
        },

        getViewPort: function() {
            var viewPort = _.first(query('.viewport', this.popupDomNode));
            return viewPort;
        },

        getThumbNailViewPort: function() {
            var viewPort = _.first(query('.thumb-carousel .viewport', this.popupDomNode));
            return viewPort;
        },

        hideLoading: function() {
            var mediaPopup = this;
            dojo.addClass(mediaPopup.popupDomNode, 'loaded');
            dojo.removeClass(dojo.query('.container', mediaPopup.popupDomNode)[0], 'loading');
        },

        addEventListener: function() {
            var mediaPopup = this;

            if (mediaPopup.jsonData.galleryImagesLength > 1) {
                mediaPopup.thumbImages = dojo.query('ul.plist img', mediaPopup.popupDomNode);

                on(mediaPopup.popupDomNode, 'ul.plist img:mouseover', function(event) {
                    mediaPopup.scrollChange(event);
                    var anims = [];
                    mediaPopup.thumbImages.forEach(function (img, i) {
                      if (img !== event.target) {
                        anims.push(mediaPopup.animProp(img, 300, {opacity: 0.5}));
                      }
                    });

                    dojo.fx.combine(anims).play();
                  });

                  on(mediaPopup.popupDomNode, 'ul.plist img:mouseout', function (event) {
                    var anims = [];
                    mediaPopup.thumbImages.forEach(function (img, i) {
                      if (img !== event.target) {
                        anims.push(mediaPopup.animProp(img, 300, {opacity: 1}));
                      }
                    });

                    dojo.fx.combine(anims).play();
                  });

                _.each(mediaPopup.thumbImages, function(thumbImage, i) {
                	if(thumbImage.addEventListener) {
                		thumbImage.addEventListener("touchstart", function(event){
                			mediaPopup.scrollChange(event);
                			var anims = [];
                            mediaPopup.thumbImages.forEach(function (img, i) {
                              if (img !== event.target) {
                                anims.push(mediaPopup.animProp(img, 300, {opacity: 0.5}));
                              } else {
                            	  anims.push(mediaPopup.animProp(img, 300, {opacity: 1}));
                              }
                            });

                            dojo.fx.combine(anims).play();
	                	});
                	}
                });
            }
            if(mediaPopup.popupDomNode.addEventListener) {
            	var images = dojo.query('ul.product-list li', mediaPopup.popupDomNode);
            	_.each(images, function(largeImage){
            		largeImage.addEventListener("touchstart", function(){
    	            	var image = null;
    	            	if(mediaPopup.scroller) {
    	            		setTimeout(function() {
    	            		image = _.first(query("li[idx='" + mediaPopup.scroller.currentPage.pageX + "']", mediaPopup.popupDomNode));
    	            		mediaPopup.getcaption(image);
    	            		domStyle.set(_.first(query(".next-large",mediaPopup.popupDomNode)),"display", "block");
    	            		domStyle.set(_.first(query(".prev-large",mediaPopup.popupDomNode)),"display", "block");
    	    	      		if(parseInt(mediaPopup.scroller.currentPage.pageX) === (mediaPopup.scroller.pages.length - 1)){
    	    	      			domStyle.set(_.first(query(".next-large",mediaPopup.popupDomNode)),"display", "none");
    	    	      		} else if(parseInt(mediaPopup.scroller.currentPage.pageX)  === 0){
    	    	      			domStyle.set(_.first(query(".prev-large",mediaPopup.popupDomNode)),"display", "none");
    	    	    	    }
    	            		}, 300);
    	            	}
    	            	mediaPopup.thumbImages.forEach(function (img, i) {
    	            		var anims = [];
    	            		anims.push(mediaPopup.animProp(img, 300, {opacity: 1}));
    	            		dojo.fx.combine(anims).play();
                          });
    	            }, false);
            	});
            }

            on(mediaPopup.popupDomNode, '.next-large:click', function(event) {
                mediaPopup.createNextFade();
            });

            on(mediaPopup.popupDomNode, '.prev-large:click', function(event) {
                mediaPopup.createPrevFade();
            });

            on(mediaPopup.popupDomNode, '.next:click', function(event) {
                mediaPopup.createNextSlide();
            });

            on(mediaPopup.popupDomNode, '.prev:click', function(event) {
                mediaPopup.createPrevSlide();
            });

            on(mediaPopup.popupDomNode, 'mouseover', function(event) {
                mediaPopup.stopAutoplay();
            });

            on(mediaPopup.popupDomNode, 'mouseout', function(event) {
                mediaPopup.startAutoplay();
            });

            on(mediaPopup.popupDomNode, "mouseup", function(event) {
            	var image = null;
            	if(mediaPopup.scroller) {
            		setTimeout(function() {
            		image = _.first(query("li[idx='" + mediaPopup.scroller.currentPage.pageX + "']", mediaPopup.popupDomNode));
            		mediaPopup.getcaption(image);
            		domStyle.set(_.first(query(".next-large",mediaPopup.popupDomNode)),"display", "block");
            		domStyle.set(_.first(query(".prev-large",mediaPopup.popupDomNode)),"display", "block");
    	      		if(parseInt(mediaPopup.scroller.currentPage.pageX) === (mediaPopup.scroller.pages.length - 1)){
    	      			domStyle.set(_.first(query(".next-large",mediaPopup.popupDomNode)),"display", "none");
    	      		} else if(parseInt(mediaPopup.scroller.currentPage.pageX)  === 0){
    	      			domStyle.set(_.first(query(".prev-large",mediaPopup.popupDomNode)),"display", "none");
    	    	    }
            		}, 300);
            	}
            });
        },

        scrollChange: function(event) {
            var mediaPopup = this,
            	mainImage = null;
            var images = dojo.query('ul.product-list li', mediaPopup.popupDomNode);//Get images contained
            mediaPopup.current = domAttr.get(event.target, 'idx');
            mainImage = _.first(query(".large-carousel li[idx='" + mediaPopup.current + "']", mediaPopup.popupDomNode));
            mediaPopup.scroller.goToPage(mediaPopup.current, 0);
            mediaPopup.getcaption(mainImage);
            domStyle.set(_.first(query(".next-large",mediaPopup.popupDomNode)),"display", "block");
    		domStyle.set(_.first(query(".prev-large",mediaPopup.popupDomNode)),"display", "block");
      		if(parseInt(mediaPopup.scroller.currentPage.pageX) === (mediaPopup.scroller.pages.length - 1)){
      			domStyle.set(_.first(query(".next-large",mediaPopup.popupDomNode)),"display", "none");
      		} else if(parseInt(mediaPopup.scroller.currentPage.pageX)  === 0){
      			domStyle.set(_.first(query(".prev-large",mediaPopup.popupDomNode)),"display", "none");
    	    }
        },

        createNextFade: function() {
            var mediaPopup = this,
                nextImage = null;
            nextImage = _.first(query("li[idx='" + mediaPopup.scroller.currentPage.pageX + "']", mediaPopup.popupDomNode));
            mediaPopup.getcaption(nextImage);
            domStyle.set(_.first(query(".next-large",mediaPopup.popupDomNode)),"display", "block");
            if((mediaPopup.scroller.currentPage.pageX + 1) >= (mediaPopup.scroller.pages.length - 1)){
            	mediaPopup.scroller.next();
            	domStyle.set(_.first(query(".next-large",mediaPopup.popupDomNode)),"display", "none");
            	return ;
            }
            domStyle.set(_.first(query(".prev-large",mediaPopup.popupDomNode)),"display", "block");
            mediaPopup.scroller.next();
            if (mediaPopup.scroller.currentPage.pageX % 10 === 0) {
                mediaPopup.createNextSlide();
            }
        },

        createPrevFade: function() {
            var mediaPopup = this,
                prevImage = null;
            prevImage = _.first(query("li[idx='" + mediaPopup.scroller.currentPage.pageX + "']", mediaPopup.popupDomNode));
            mediaPopup.getcaption(prevImage);
            domStyle.set(_.first(query(".prev-large",mediaPopup.popupDomNode)),"display", "block");
    	    if((mediaPopup.scroller.currentPage.pageX - 1) === 0){
    	    	mediaPopup.scroller.prev();
    	    	domStyle.set(_.first(query(".prev-large",mediaPopup.popupDomNode)),"display", "none");
            	return ;
            }
    	    domStyle.set(_.first(query(".next-large",mediaPopup.popupDomNode)),"display", "block");
            mediaPopup.scroller.prev();
            if ((mediaPopup.scroller.currentPage.pageX + 1) % 10 === 0) {
                mediaPopup.createPrevSlide();
            }
        },

        getcaption: function(node) {
            var mediaPopup = this;
            var images = dojo.query('ul.product-list li', mediaPopup.popupDomNode);
            var index = _.indexOf(images, node);
            var description = domAttr.get(node, 'data-location-name');
            var page = (index + 1) + ' of ' + (images.length);
            var captionContent = (description) ? [page, description].join(' - ') : page;
            var captionEl = dojo.byId('media-popup-caption');
            if (captionEl) {
                captionEl.innerHTML = captionContent;
                dojo.setStyle(captionEl, 'display', 'block');
            }
        },

        createNextSlide: function() {
            var mediaPopup = this,
            	pagesToJump = 0;
            pagesToJump = mediaPopup.thumbNailScroller.currentPage.pageX + 10;
            if(pagesToJump > mediaPopup.thumbNailScroller.pages.length) {
            	pagesToJump = mediaPopup.thumbNailScroller.pages.length - 1;
            }
            mediaPopup.thumbNailScroller.goToPage(pagesToJump , 0);
        },

        createPrevSlide: function() {
            var mediaPopup = this;
            var mediaPopup = this,
        	pagesToJump = 0;
        pagesToJump = mediaPopup.thumbNailScroller.currentPage.pageX - 10;
        if(pagesToJump < 0) {
        	pagesToJump = 0;
        }
        mediaPopup.thumbNailScroller.goToPage(pagesToJump , 0);
        },


        animProp: function(node, duration, properties, delay) {
            var anim = dojo.animateProperty({
                node: node,
                duration: duration,
                properties: properties,
                delay: delay || 0
            });
            return anim;
        },

        stopAutoplay: function() {
            var mediaPopup = this;
            clearInterval(mediaPopup.autoplaytimer);
            mediaPopup.autoplaytimer = null;
        },

        startAutoplay: function() {
            var mediaPopup = this;
            if (mediaPopup.autoplaymode && mediaPopup.jsonData.galleryImagesLength > 1) {
                mediaPopup.autoplaytimer = setInterval(function() {
                    mediaPopup.autoplay();
                }, mediaPopup.autoplaygap);
            }
            mediaPopup.autoplaygap = 6000;
        },

        autoplay: function() {
            var mediaPopup = this;
            if (mediaPopup.pause) {
                return;
            }
            mediaPopup.createNextFade();
        },

        onOpen: function() {
            var mediaPopup = this;
            dojo.publish("tui/widget/media/HeroCarousel/onTemplateLoaded", [true]);
            mediaPopup.isOpen = true;
        },

        onClose: function() {
            var mediaPopup = this;
            mediaPopup.stopAutoplay();
            dojo.publish("tui/widget/media/HeroCarousel/onTemplateLoaded", [false]);
            mediaPopup.isOpen = false;
        }

    });

    return tui.widget.media.MediaPopup;
});