define("tui/widget/media/InventoryCarousel", [
  'dojo',
  'dojo/on',
  'dojo/dom-attr',
  'dojo/_base/fx',
  'dojo/_base/array',
  'dojo/text!tui/widget/media/templates/InventoryCarouselTmpl.html',
  'tui/widget/popup/Popup',
  'tui/widget/maps/Mappers',
  'dojo/query',
  'dojo/window',
  'dojo/dom-construct',
  'tui/widget/_TuiBaseWidget',
  'dojo/NodeList-traverse' ], function (dojo, on, domAttr, baseFx, array, inventoryCarouselTmpl, popup, mappers, query, win, domConstruct) {

  dojo.declare("tui.widget.media.InventoryCarousel", [tui.widget._TuiBaseWidget], {

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

      //newCarousel.startAutoplay();

      var images = dojo.query('.product-list li', newCarousel.domNode).forEach(function (item, i) {
        domAttr.set(item, "idx", i);
        if (i === 0) {
          dojo.addClass(item, 'crsl-active');
          newCarousel.getcaption(item);
          dojo.setStyle(item, 'zIndex', '1');
        }
        else {
          dojo.setStyle(item, 'opacity', '0');
          dojo.setStyle(item, 'zIndex', '0');
        }
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
        var thumb = dojo.query('ul.plist img', newCarousel.domNode).forEach(function (item, i) {
          domAttr.set(item, "idx", i);
        });
        // calculating the max number of slides for the thumb Carousel

        newCarousel.thumbViewport = dojo.query('.thumb-carousel', newCarousel.domNode)[0];
        newCarousel.thumbViewportWidth = dojo.style(newCarousel.thumbViewport, 'width');

        newCarousel.itemContainer = dojo.query('ul.plist', newCarousel.domNode)[0];
        newCarousel.itemContainerWidth = newCarousel.itemElementWidth * thumb.length;

        newCarousel.slidesNo = Math.ceil(newCarousel.itemContainerWidth / newCarousel.thumbViewportWidth);
        
        // hidding the next and prev controls on thumbnail section 
        //if the width of the total thumbnail images is not more than 8
       if(newCarousel.jsonData.noofimages <= 8){
        	dojo.setStyle(dojo.query('.prev',newCarousel.domNode)[0], 'display', 'none');
        	dojo.setStyle(dojo.query('.next',newCarousel.domNode)[0], 'display', 'none');
        }
      }

    },

    hideLoading: function () {
      var newCarousel = this;
      dojo.addClass(newCarousel.domNode, 'loaded');
      dojo.removeClass(dojo.query('.container', newCarousel.domNode)[0], 'loading');
    },

    addEventListener: function () {
      var newCarousel = this;

      /* DE5279, Commented as a fix for DE5279
       * var mediaLink = dojo.query('.view-Images', newCarousel.domNode)[0];
      var mediaConnect = newCarousel.connect(mediaLink, 'onclick', function (event) {
        dojo.stopEvent(event);
        if (!newCarousel.newCarousel) {
          newCarousel.open();
        }
        dojo.disconnect(mediaConnect);
      });*/

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

      /*on(newCarousel.domNode, 'mouseover', function (event) {
        newCarousel.stopAutoplay();
      });

      on(newCarousel.domNode, 'mouseout', function (event) {
        newCarousel.startAutoplay();
      });*/
    },

    scrollChange: function (event) {
      var newCarousel = this;
      var images = dojo.query('ul.product-list li', newCarousel.domNode);//Get images contained
      var old = dojo.query('.crsl-active', newCarousel.domNode)[0];
      newCarousel.current = domAttr.get(event.target, 'idx');
      if (old === images[newCarousel.current]) {
        return;
      }
      newCarousel.createFx(old, 1, 0, 0);
      dojo.removeClass(old, 'crsl-active');
      newCarousel.createFx(images[newCarousel.current], 0.4, 1, 1);
      dojo.addClass(images[newCarousel.current], 'crsl-active');
      newCarousel.getcaption(images[newCarousel.current]);
    },
    
    checkThumbNailVisibilty: function(thumbNailIndex){
    	var newCarousel = this;    	
    	// With the assumption that max 8 images will be displayed on carousel 
    	if(thumbNailIndex > 0 && thumbNailIndex < 7){
    		return true;
    	} else {
    		if(newCarousel.nextSlideHit){
    			if(thumbNailIndex > 0 && thumbNailIndex < 9) {
    				return true;
    			}
    		} else if(thumbNailIndex < 8){
    			return true;
    		}    		
    		return false;
    	}
    },

    createNextFade: function () {
      var newCarousel = this,
      	  current = null,
      	  next = null,
      	  images = [],
      	  imageIndex = 0,
      	  thumbNailNodes = [],
      	  thumbNailImageListNode = null,
      	  thumbNailIndex = 0,
      	  bThumbNailVisible = false;
      current = dojo.query('.crsl-active', newCarousel.domNode)[0];
      next = query('.crsl-active', newCarousel.domNode).next()[0];
      next = next ? next : dojo.query('ul.product-list li', newCarousel.domNode)[0];
      images = query('ul.product-list li', newCarousel.domNode);
      imageIndex = domAttr.get(next, 'idx');
      thumbNailNodes = query('ul.plist li', newCarousel.domNode);
      thumbNailImageListNode = query('ul.plist img[idx="'+ imageIndex +'"]', newCarousel.domNode).parent().parent().parent();
      thumbNailIndex = thumbNailImageListNode.prevAll().length;
      bThumbNailVisible = newCarousel.checkThumbNailVisibilty(thumbNailIndex);           
      while(!bThumbNailVisible){
	  	 newCarousel.createNextSlide();
	  	 thumbNailIndex = thumbNailImageListNode.prevAll().length;
	  	 bThumbNailVisible = newCarousel.checkThumbNailVisibilty(thumbNailIndex);
      }
        newCarousel.createFx(current, 1, 0, 0);
        dojo.removeClass(current, 'crsl-active');
        newCarousel.createFx(next, 0.4, 1, 1);
        dojo.addClass(next, 'crsl-active');
        newCarousel.getcaption(next);              
    },

    createPrevFade: function () {
      var newCarousel = this;
      var images = dojo.query('ul.product-list li', newCarousel.domNode);
      var current = dojo.query('.crsl-active', newCarousel.domNode)[0];
      var prev = dojo.query('.crsl-active', newCarousel.domNode).prev()[0];
      var newCarousel = this,
  	  	  current = null,
  	  	  prev = null,
  	  	  images = [],
  	  	  imageIndex = 0,
  	  	  thumbNailNodes = [],
  	  	  thumbNailImageListNode = null,
  	  	  thumbNailIndex = 0,
  	  	  bThumbNailVisible = false;
      current = dojo.query('.crsl-active', newCarousel.domNode)[0];
      prev = query('.crsl-active', newCarousel.domNode).prev()[0];
      if(typeof prev === "undefined"){
          prev = dojo.query('ul.product-list li', newCarousel.domNode);
          prev = prev[prev.length - 1];
      }
      images = query('ul.product-list li', newCarousel.domNode);
      imageIndex = domAttr.get(prev, 'idx');
      thumbNailNodes = query('ul.plist li', newCarousel.domNode);
      thumbNailImageListNode = query('ul.plist img[idx="'+ imageIndex +'"]', newCarousel.domNode).parent().parent().parent();
      thumbNailIndex = thumbNailImageListNode.prevAll().length;
      bThumbNailVisible = newCarousel.checkThumbNailVisibilty(thumbNailIndex);      
      while(!bThumbNailVisible){
 	  	 newCarousel.createPrevSlide();
 	  	 thumbNailIndex = thumbNailImageListNode.prevAll().length;
 	  	 bThumbNailVisible = newCarousel.checkThumbNailVisibilty(thumbNailIndex);
       }
      newCarousel.createFx(current, 1, 0, 0);
      dojo.removeClass(current, 'crsl-active');
      newCarousel.createFx(prev, 0.4, 1, 1);
      dojo.addClass(prev, 'crsl-active');
      newCarousel.getcaption(prev);
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
    		 if(!newCarousel.prevSlideHit){
    			 domConstruct.place(newCarousel.prevImageToAppend[0],dojo.query("ul.plist",newCarousel.domNode)[0],"last");    			 
    		 }    		 
        }
    	 newCarousel.animationInProgress = false;
    	 newCarousel.prevSlideHit = false;
    },
    
    PrependImage: function(carouselComponent){
  	  var newCarousel = carouselComponent;
      newCarousel.nextImageToPrepend = dojo.query("ul.plist",newCarousel.domNode).children(":last-child");	    	
      if(newCarousel.nextImageToPrepend && newCarousel.nextImageToPrepend.length){
    	  if(!newCarousel.nextSlideHit){
        	  domConstruct.place(newCarousel.nextImageToPrepend[0],dojo.query("ul.plist",newCarousel.domNode)[0],"first");        	  
    	  }  	    	  
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

    createFx: function (node, start, end, zindex) {
      var newCarousel = this;
      baseFx.animateProperty({
        node: node,
        duration: 1000,
        properties: {
          opacity: {start: start, end: end}
        }
      }).play();
      dojo.setStyle(node, 'zIndex', zindex);

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