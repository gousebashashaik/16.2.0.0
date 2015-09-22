define("tui/widget/media/InventoryCarouselSwipe", [
  'dojo',
  'dojo/on',
  'dojo/dom-attr',
  'dojo/_base/fx',
  'dojo/_base/array',
  'dojo/text!tui/widget/media/templates/InventoryCarouselTmplSwipe.html',
  'tui/widget/popup/Popup',
  'tui/widget/maps/Mappers',
  'dojo/query',
  'dojo/window',
  'dojo/dom-construct',
  'dojo/dom-style',
  'dojo/dom-class',
  'tui/widget/_TuiBaseWidget',
  'dojo/NodeList-traverse' ], function (dojo, on, domAttr, baseFx, array, inventoryCarouselTmpl, popup, mappers, query, win, domConstruct, domStyle, domClass) {

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

  dojo.declare("tui.widget.media.InventoryCarouselSwipe", [tui.widget._TuiBaseWidget], {

    //---------------------------------------------- properties

    jsonData: null,

    modal: true,

    autoplaymode: true,

    autoplaygap: 8000,

    pause: false,

    tmpl: inventoryCarouselTmpl,

    thumbImages: null,

    newCarousel: null,

    current: 0,

    thumbViewport: null,

    thumbViewportWidth: null,

    itemContainer: null,

    itemContainerWidth: null,

    itemElementWidth: 121,

    slidesNo: null,

    currentSlide: 0,

    slideAmount: 131,

    start: 0,

    end: 0,

    includeScroll: true,

    dataPath: null,

    loadDelay: 5000,

    nextImageToPrepend: null,

    prevImageToAppend: null,

    animationInProgress: false,

    nextSlideHit: false,

    prevSlideHit: true,

    noPreviousElement: true,

    maxImageToDisplay: 8,

    scroller: null,

    thumbNailScroller: null,

    isOpen: false,

    noOfImages: 0,

    imageContainerNode: null,

    imageWidth: 0,

    xPosition: 0,

    carouselConfig: {
        'scrollX': true,
        'scrollY': false,
        'keyBindings': false,
        'snap': 'li',
        'snapSpeed': 900,
        'snapThreshold': 0.15,
        'momentum': false,
        'tap': 'scrollerTap',
        'eventPassthrough': true,
        'freeScroll': true
    },

    //--------------------------------------------------------------methods

    postCreate: function () {
      var newCarousel = this;
      newCarousel.inherited(arguments);
      newCarousel.jsonData.galleryImages=newCarousel.jsonData.imageData;
      newCarousel.jsonData = mappers.thumbnailSet(newCarousel.dataPath ? newCarousel.jsonData[newCarousel.dataPath] : newCarousel.jsonData);
      newCarousel.jsonData.noofimages = _.size(newCarousel.jsonData.imageData);
      var mediaNode = domConstruct.toDom(newCarousel.renderTemplate());
      domConstruct.place(mediaNode, newCarousel.domNode);
      newCarousel.attachActions();

    },

    attachActions: function () {
      var newCarousel = this;

      newCarousel.addEventListener();

      var timer = setTimeout(function () {
        newCarousel.hideLoading();
      }, newCarousel.loadDelay);


      dojo.query('ul.product-list li', newCarousel.domNode).forEach(function (item, i) {
        domAttr.set(item, "idx", i);
      });

      var images = dojo.query('.plist li img', newCarousel.domNode).forEach(function (item, i) {
          domAttr.set(item, "idx", i);
        });

      // hardcoding the height of the newCarousel depending on the view port height. ToDo: bring responsive desighn into this component too.

      var vs = win.getBox();
      if (vs.h < 650) {
        var large = dojo.query('.large-carousel', newCarousel.domNode)[0];
        var largeHeight = dojo.style(large, "height");
        var height = [(largeHeight - ((650 - vs.h) + 50)), 'px'].join("");
        dojo.style(large, {
          "height": height
        });
      }

      if(newCarousel.jsonData.noofimages > 1) {
        var thumb = dojo.query('ul.product-list img', newCarousel.domNode).forEach(function (item, i) {
          domAttr.set(item, "idx", i);
          if (i === 0) {
        	  newCarousel.getcaption(item.parentElement);
          }
        });
        // calculating the max number of slides for the thumb Carousel

        newCarousel.thumbViewport = dojo.query('.thumb-carousel', newCarousel.domNode)[0];
        newCarousel.thumbViewportWidth = dojo.style(newCarousel.thumbViewport, 'width');

        newCarousel.itemContainer = dojo.query('ul.plist', newCarousel.domNode)[0];
        newCarousel.itemContainerWidth = newCarousel.itemElementWidth * thumb.length;

        newCarousel.slidesNo = Math.ceil(newCarousel.itemContainerWidth / newCarousel.thumbViewportWidth);

        // hidding the next and prev controls on thumbnail section
        //if the width of the total thumbnail images is not more than 8
       if(newCarousel.jsonData.noofimages <= newCarousel.maxImageToDisplay){
        	dojo.setStyle(dojo.query('.prev',newCarousel.domNode)[0], 'display', 'none');
        	dojo.setStyle(dojo.query('.next',newCarousel.domNode)[0], 'display', 'none');
        }

       var img = _.first(query('img', _.first(newCarousel.listItems())));
       var thumbImage = _.first(query('img', _.first(newCarousel.thumbNailListItems())))

       var handler = function() {
           setTimeout(function() {
           	var image = null;
           		newCarousel.resize();
           }, 1000)
       };

       var thumbHandler = function() {
           setTimeout(function() {
        	 //  newCarousel.generateLargeThumbNailStrip();
        	   newCarousel.resizeThumbNail();
           }, 1000)
       };

       onImageLoad(img, handler);
       onImageLoad(thumbImage, thumbHandler);

       domClass.add(_.first(query(".prev-large .arrow",newCarousel.domNode)),"hide");
      }

    },

    generateLargeThumbNailStrip : function(){
    	var newCarousel = this,
    		thumbNailImages = null,
    		imageStripIndex = 0,
    		totalImagesToBeDisplayed = 0;
    	thumbNailImages  = newCarousel.thumbNailListItems();
    	if(thumbNailImages.length > newCarousel.maxImageToDisplay){
    		imageStripIndex = newCarousel.maxImageToDisplay - 1;
    		while(imageStripIndex){
    			imageStripIndex--;
    			domConstruct.place(_.first(query(".thumb-carousel .plist")).innerHTML, _.first(query(".thumb-carousel .plist")), "last") ;
    		}
    	}
    },

    refreshList: function() {
        var newCarousel = this;
        var viewPortWidth = newCarousel.determineViewPortWidth();
        _.each(query('li', newCarousel.getViewPortContent()), function(el) {
            domStyle.set(el, 'width', viewPortWidth + 'px');
        });
        var listWidth = newCarousel.determineListWidth();
        var numberOfWindows = Math.ceil(listWidth / viewPortWidth);
        domStyle.set(newCarousel.getViewPortContent(), 'width', listWidth + 'px');
        newCarousel.noOfImages = numberOfWindows;
    },

    thumbNailRefreshList: function() {
        var newCarousel = this;
        var thumbNailViewPortWidth = newCarousel.determineThumbNailViewPortWidth();
        _.each(query('li', newCarousel.getThumbNailViewPortContent()), function(el) {
            domStyle.set(el, 'width', '131px');
        });
        // TO-DO need to find better logic for setting width
        domStyle.set(newCarousel.getThumbNailViewPortContent(), 'width',  '100%');
        //quickfix to IE issue
        var widthCalc = parseInt(domStyle.getComputedStyle(newCarousel.getThumbNailViewPortContent()).width.split("px")[0]);
        var ua = window.navigator.userAgent;
        var msie = ua.indexOf("MSIE ");
        if (msie > 0){
          if(parseInt(domStyle.getComputedStyle(newCarousel.getThumbNailViewPortContent()).width.split("px")[0]) == 100){
            widthCalc = 4500;
          }
        }
        var listWidth = widthCalc + (query('li', newCarousel.getThumbNailViewPortContent()).length - newCarousel.maxImageToDisplay) * 131;
        domStyle.set(newCarousel.getThumbNailViewPortContent(), 'width',  listWidth + 'px');
    },

    resizeThumbNail: function() {
    	var newCarousel = this;
        newCarousel.thumbNailRefreshList();
    	newCarousel.thumbNailScroller == null ? newCarousel.thumbNailScroller = new IScroll(newCarousel.getThumbNailViewPort(), newCarousel.carouselConfig) : newCarousel.thumbNailScroller.refresh();

    },

    resize: function() {
        var newCarousel = this;
        newCarousel.refreshList();
        newCarousel.scroller == null ? newCarousel.scroller = new IScroll(newCarousel.getViewPort(), newCarousel.carouselConfig) : newCarousel.scroller.refresh();

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
        var newCarousel = this;
        var clientWidth = _.first(query('li img', this.getViewPortContent())).clientWidth;
        return clientWidth;
    },

    determineThumbNailViewPortWidth: function() {
        var newCarousel = this;
        var clientWidth = _.first(query('li img', this.getThumbNailViewPortContent())).clientWidth;
        return clientWidth;
    },

    determineListWidth: function() {
        var newCarousel = this;
        return calulateCombinedWidth(newCarousel.listItems());
    },

    determineThumbNailListWidth: function() {
    	var newCarousel = this;
    	return calulateCombinedWidth(newCarousel.thumbNailListItems());
    },

    initSwipable: function() {
        this.isOpen ? [this.resize(), this.resizeThumbNail()] : '';
    },

    windowSlidedListener: function() {
        var newCarousel = this;
        domUtil.onWindowResize(newCarousel.initSwipable.bind(this));
    },

    getViewPort: function() {
        var viewPort = _.first(query('.large-carousel .viewport', this.domNode));
        return viewPort;
    },

    getThumbNailViewPort: function() {
        var viewPort = _.first(query('.thumb-carousel .viewport', this.domNode));
        return viewPort;
    },

    hideLoading: function () {
      var newCarousel = this;
      dojo.addClass(newCarousel.domNode, 'loaded');
      dojo.removeClass(dojo.query('.container', newCarousel.domNode)[0], 'loading');
    },

    addEventListener: function () {
      var newCarousel = this,
      	  imageContainer = null;

      if(newCarousel.jsonData.noofimages > 1) {

        newCarousel.thumbImages = dojo.query('ul.plist img', newCarousel.domNode);

        on(newCarousel.domNode, 'ul.plist img:mouseover', function (event) {
            newCarousel.scrollChange(event);
            var anims = [];
            newCarousel.thumbImages.forEach(function (img, i) {
              if (img !== event.target) {
                anims.push(newCarousel.animProp(img, 300, {opacity: 0.5}));
              }
            });
            dojo.fx.combine(anims).play();
          });

        on(newCarousel.domNode, 'ul.plist img:mouseout', function (event) {
            var anims = [];
            newCarousel.thumbImages.forEach(function (img, i) {
              if (img !== event.target) {
                anims.push(newCarousel.animProp(img, 300, {opacity: 1}));
              }
            });

            dojo.fx.combine(anims).play();

          });

        _.each(newCarousel.thumbImages, function(image){

			if(image.addEventListener){
			  image.addEventListener("touchend", function (event) {
                newCarousel.scrollChange(event);
                var anims = [];
                newCarousel.thumbImages.forEach(function (img, i) {
                  if (img !== event.target) {
                    anims.push(newCarousel.animProp(img, 300, {opacity: 0.5}));
                  } else {

                	  anims.push(newCarousel.animProp(img, 300, {opacity: 1}));
                  }
                });
                dojo.fx.combine(anims).play();
              });
			}
			else{
			  image.attachEvent("touchend", function (event) {
                newCarousel.scrollChange(event);
                var anims = [];
                newCarousel.thumbImages.forEach(function (img, i) {
                  if (img !== event.target) {
                    anims.push(newCarousel.animProp(img, 300, {opacity: 0.5}));
                  } else {
                	  anims.push(newCarousel.animProp(img, 300, {opacity: 1}));
                  }
                });
                dojo.fx.combine(anims).play();
              });
			}
        })

      }
      on(newCarousel.domNode, '.next-large:click', function (event) {
        newCarousel.createNextFade();
      });

      on(newCarousel.domNode, '.prev-large:click', function (event) {
        newCarousel.createPrevFade();
      });

      on(newCarousel.domNode, '.next:click', function (event) {
        newCarousel.createNextSlide();
      });

      on(newCarousel.domNode, '.prev:click', function (event) {
        newCarousel.createPrevSlide();
      });

      on(newCarousel.domNode, "mouseup", function(event) {
      	var image = null;
      	if(newCarousel.scroller) {
      		setTimeout(function() {
      		image = _.first(query("ul.product-list li[idx='" + newCarousel.scroller.currentPage.pageX + "']", newCarousel.domNode));
      		newCarousel.getcaption(image);
      		domClass.remove(_.first(query(".next-large .arrow",newCarousel.domNode)),"hide");
      		domClass.remove(_.first(query(".prev-large .arrow",newCarousel.domNode)),"hide");
      		if(parseInt(newCarousel.scroller.currentPage.pageX) === (newCarousel.scroller.pages.length - 1)){
    	    	domClass.add(_.first(query(".next-large .arrow",newCarousel.domNode)),"hide");
      		} else if(parseInt(newCarousel.scroller.currentPage.pageX)  === 0){
    	    	domClass.add(_.first(query(".prev-large .arrow",newCarousel.domNode)),"hide");
    	    }
      		}, 300);
      	}
      });

      if(newCarousel.domNode.addEventListener){
    	  var images = dojo.query('ul.product-list li', newCarousel.domNode);
	      _.each(images, function(largeImage){
	    	  largeImage.addEventListener('touchend', function(e){
		    	  var image = null;
		      	if(newCarousel.scroller) {
		      		setTimeout(function() {
		      		image = _.first(query("ul.product-list li[idx='" + newCarousel.scroller.currentPage.pageX + "']", newCarousel.domNode));
		      		newCarousel.getcaption(image);
		      		domClass.remove(_.first(query(".next-large .arrow",newCarousel.domNode)),"hide");
		      		domClass.remove(_.first(query(".prev-large .arrow",newCarousel.domNode)),"hide");
		      		if(parseInt(newCarousel.scroller.currentPage.pageX) === (newCarousel.scroller.pages.length - 1)){
		    	    	domClass.add(_.first(query(".next-large .arrow",newCarousel.domNode)),"hide");
		      		} else if(parseInt(newCarousel.scroller.currentPage.pageX)  === 0){
		    	    	domClass.add(_.first(query(".prev-large .arrow",newCarousel.domNode)),"hide");
		    	    }
		      		}, 300);
		      	}
		      	 var anims = [];
                newCarousel.thumbImages.forEach(function (img, i) {
                	  anims.push(newCarousel.animProp(img, 300, {opacity: 1}));
                });
                dojo.fx.combine(anims).play();
			} ,false);
	      });

	  }
	  else{
	     newCarousel.domNode.attachEvent('touchend', function(e){
	    	  var image = null;
	      	if(newCarousel.scroller) {
	      		setTimeout(function() {
	      		image = _.first(query("ul.product-list li[idx='" + newCarousel.scroller.currentPage.pageX + "']", newCarousel.domNode));
	      		newCarousel.getcaption(image);
	      		domClass.remove(_.first(query(".next-large .arrow",newCarousel.domNode)),"hide");
	      		domClass.remove(_.first(query(".prev-large .arrow",newCarousel.domNode)),"hide");
	      		if(parseInt(newCarousel.scroller.currentPage.pageX) === (newCarousel.scroller.pages.length - 1)){
	    	    	domClass.add(_.first(query(".next-large .arrow",newCarousel.domNode)),"hide");
	      		} else if(parseInt(newCarousel.scroller.currentPage.pageX)  === 0){
	    	    	domClass.add(_.first(query(".prev-large .arrow",newCarousel.domNode)),"hide");
	    	    }
	      		}, 300);
	      	}
		} ,false);
	  }
    },

    scrollChange: function (event) {
      var newCarousel = this,
  		  mainImage = null;
	  var images = dojo.query('ul.product-list li', newCarousel.domNode);//Get images contained
	  newCarousel.current = domAttr.get(event.target, 'idx');
	  mainImage = _.first(query(".large-carousel li[idx='" + newCarousel.current + "']", newCarousel.domNode));
	  newCarousel.scroller.goToPage(newCarousel.current, 0);
	  newCarousel.getcaption(mainImage);
	  domClass.remove(_.first(query(".next-large .arrow",newCarousel.domNode)),"hide");
	  domClass.remove(_.first(query(".prev-large .arrow",newCarousel.domNode)),"hide");
	  if(parseInt(newCarousel.scroller.currentPage.pageX) === (newCarousel.scroller.pages.length - 1)){
	    	domClass.add(_.first(query(".next-large .arrow",newCarousel.domNode)),"hide");
		} else if(parseInt(newCarousel.scroller.currentPage.pageX)  === 0){
	    	domClass.add(_.first(query(".prev-large .arrow",newCarousel.domNode)),"hide");
	    }
    },

    checkThumbNailVisibilty: function(thumbNailIndex){
    	var newCarousel = this;
    	// With the assumption that max 8 images will be displayed on carousel
    	if(thumbNailIndex > 0 && thumbNailIndex < (newCarousel.maxImageToDisplay - 1)){
    		return true;
    	} else {
    		if(newCarousel.nextSlideHit){
    			if(thumbNailIndex > 0 && thumbNailIndex < 9) {
    				return true;
    			}
    		} else if(thumbNailIndex < newCarousel.maxImageToDisplay){
    			return true;
    		}
    		return false;
    	}
    },

    createNextFade: function () {
        var newCarousel = this,
        nextImage = null;
	    nextImage = _.first(query("li[idx='" + newCarousel.scroller.currentPage.pageX + "']", newCarousel.domNode));
	    newCarousel.getcaption(nextImage);
	    domClass.remove(_.first(query(".next-large .arrow",newCarousel.domNode)),"hide");
	    if((newCarousel.scroller.currentPage.pageX + 1) >= (newCarousel.scroller.pages.length - 1)){
	    	newCarousel.scroller.next();
	    	domClass.add(_.first(query(".next-large .arrow",newCarousel.domNode)),"hide");
	    	return ;
	    }
	    domClass.remove(_.first(query(".prev-large .arrow",newCarousel.domNode)),"hide");
	    newCarousel.scroller.next();
	    if (newCarousel.scroller.currentPage.pageX % 10 === 0) {
	    	newCarousel.createNextSlide();
	    }
    },

    createPrevFade: function () {
        var newCarousel = this,
        	prevImage = null;
	    prevImage = _.first(query("li[idx='" + newCarousel.scroller.currentPage.pageX + "']", newCarousel.domNode));
	    newCarousel.getcaption(prevImage);
	    domClass.remove(_.first(query(".prev-large .arrow",newCarousel.domNode)),"hide");
	    if((newCarousel.scroller.currentPage.pageX - 1) === 0){
	    	newCarousel.scroller.prev();
	    	domClass.add(_.first(query(".prev-large .arrow",newCarousel.domNode)),"hide");
	    	return ;
	    }
	    domClass.remove(_.first(query(".next-large .arrow",newCarousel.domNode)),"hide");
	    newCarousel.scroller.prev();
	    if ((newCarousel.scroller.currentPage.pageX + 1) % 10 === 0) {
	    	newCarousel.createPrevSlide();
	    }
    },

    getcaption: function (node) {
	  var newCarousel = this;
      var overlay_title = dojo.query('.overlay-title',newCarousel.domNode)[0];
      var overlay_desc = dojo.query('.overlay-desc',newCarousel.domNode)[0];
      var title = domAttr.get(node, 'data-image-title');
      var description = domAttr.get(node, 'data-image-desc');
      overlay_title.innerHTML = title;
      overlay_desc.innerHTML = description;
    },

    AppendImage: function(carouselComponent){
    	  var newCarousel = carouselComponent;
    	newCarousel.prevImageToAppend = dojo.query("ul.plist",newCarousel.domNode).children(":first-child");
    	 if(newCarousel.prevImageToAppend && newCarousel.prevImageToAppend.length){
    		 //if(!newCarousel.prevSlideHit){
    			 domConstruct.place(newCarousel.prevImageToAppend[0],dojo.query("ul.plist",newCarousel.domNode)[0],"last");
    		 //}
        }
    	 newCarousel.animationInProgress = false;
    	 newCarousel.prevSlideHit = false;
    },

    PrependImage: function(carouselComponent){
  	  var newCarousel = carouselComponent;
      newCarousel.nextImageToPrepend = dojo.query("ul.plist",newCarousel.domNode).children(":last-child");
      if(newCarousel.nextImageToPrepend && newCarousel.nextImageToPrepend.length){
    	  //if(!newCarousel.nextSlideHit){
        	  domConstruct.place(newCarousel.nextImageToPrepend[0],dojo.query("ul.plist",newCarousel.domNode)[0],"first");
    	  //}
      }
      newCarousel.animationInProgress = false;
      newCarousel.nextSlideHit = false;
    },

    createNextSlide: function () {
      var newCarousel = this;
      if(!newCarousel.animationInProgress){
    	  newCarousel.animationInProgress = true;
    	  newCarousel.nextSlideHit = true;
    	  newCarousel.noPreviousElement = false;
          newCarousel.animProp(newCarousel.itemContainer, 1000, {right: {start: 0, end: newCarousel.slideAmount, units: 'px'}}, 0, newCarousel.AppendImage, newCarousel).play();
      }
    },

    createPrevSlide: function () {
      var newCarousel = this;
      if(!newCarousel.animationInProgress){
    	  newCarousel.animationInProgress = true;
    	  newCarousel.prevSlideHit = true;
    	  if(newCarousel.noPreviousElement) {
    		  newCarousel.PrependImage(newCarousel);
    		  newCarousel.noPreviousElement = false;
    		  newCarousel.animProp(newCarousel.itemContainer, 1000, {right: {start: newCarousel.slideAmount, end: 0 , units: 'px'}}, 0, null, newCarousel).play();
    	  } else {
          newCarousel.animProp(newCarousel.itemContainer, 1000, {right: {start: newCarousel.slideAmount, end: 0 , units: 'px'}}, 0, newCarousel.PrependImage, newCarousel).play();
    	  }
       }
    },

    animProp: function (node, duration, properties, delay, endFunction, carouselComponent) {
      var anim = dojo.animateProperty({
        node: node,
        duration: duration,
        properties: properties,
        delay: delay || 0,
        onEnd: (endFunction && endFunction(carouselComponent)) || null
      });
      return anim;
    },

    stopAutoplay: function () {
      var newCarousel = this;
      clearInterval(newCarousel.autoplaytimer);
      newCarousel.autoplaytimer = null;

    },

    startAutoplay: function () {
      var newCarousel = this;
      if (newCarousel.autoplaymode && newCarousel.jsonData.noofimages > 1) {
        newCarousel.autoplaytimer = setInterval(function () {
          newCarousel.autoplay();
        }, newCarousel.autoplaygap);
      }
      newCarousel.autoplaygap = 6000;
    },

    autoplay: function () {
      var newCarousel = this;
      if (newCarousel.pause) {
        return;
      }
      newCarousel.createNextFade();
    },

    onOpen: function () {
      dojo.publish("tui/widget/media/HeroCarousel/onTemplateLoaded", [true]);
    },

    onClose: function () {
      var newCarousel = this;
      newCarousel.stopAutoplay();
      dojo.publish("tui/widget/media/HeroCarousel/onTemplateLoaded", [false]);
    },

    renderTemplate: function(){
        var newCarousel = this;
        var template  = new dojox.dtl.Template(newCarousel.tmpl);
        var context = new dojox.dtl.Context(newCarousel);
        var html = dojo.trim(template.render(context));
        return html;
    }

  });

  return tui.widget.media.NewCarousel;
});