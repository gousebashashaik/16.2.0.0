define('tui/widget/media/HeroCarouselSwipe', [
  'dojo',
  'dojo/on',
  'dojo/text!tui/widget/media/templates/HeroTmplSwipe.html',
  'tui/widget/media/VideoPopup',
  'dojo/query',
  'dojo/dom-style',
  'dojo/dom-class',
  'dojo/NodeList-traverse',
  'dojo/html',
  'tui/widget/carousels/CrossFadeCarouselSwipe'
], function(dojo, on, heroTmpl, videoPopup, query, domStyle, domClass ) {

    function onImageLoad(img, handler) {
        on.once(img, 'load', handler);
    }

  dojo.declare('tui.widget.media.HeroCarouselSwipe', [tui.widget.carousels.CrossFadeCarouselSwipe], {

    // ----------------------------------------------------------------------------- properties

    autoplaytimer: null,

    autoplaymode: true,

    autoplaygap: 6000,

    tmpl: heroTmpl,

    imageCache: {},

    caption: null,

    captionShow: true,

    displayPagination: false,

    pause: false,

    isHeroCarousel:true,

    // ----------------------------------------------------------------------------- singleton

    postCreate: function() {
      // summary:
      //		Sets up hero carousel with the default values. We first work out which images sizes we need, as the json images
      //		array return all size.
      var heroCarousel = this;

      heroCarousel.setCarouselData();
      heroCarousel.inherited(arguments);
      heroCarousel.setCaptionDomNode();
      heroCarousel.addHeroCarouselEventListner();
//      heroCarousel.startAutoplay();

      var li = dojo.query('li', heroCarousel.domNode)[0];
      heroCarousel.liHeight = dojo.style(li, 'height');
      domStyle.set(li,'float','left');

      var image = dojo.query('img', li)[0];

      heroCarousel.showWidget(image);
      heroCarousel.positionImg(image);
      if (heroCarousel.jsonData.heroImages.length > 1) {
        heroCarousel.displayControls(true);
      }

      heroCarousel.subscribe("tui/widget/media/HeroCarousel/onTemplateLoaded",function(pause){
		if (pause){
			heroCarousel.pause = true;
		}
		else {
			heroCarousel.pause = false;
		}
	});

      var imageIndex = 1;
      while(imageIndex < heroCarousel.jsonData.heroImages.length ) {
    	  heroCarousel.appendScroll(imageIndex, heroCarousel.jsonData.heroImages);
    	  imageIndex++;
      }

      var img = dojo.query('li:last-child img', heroCarousel.domNode)[0];
      var handler = function() {
          setTimeout(function() {
        	  heroCarousel.scroller == null ? heroCarousel.scroller = new IScroll(heroCarousel.getViewPort(), heroCarousel.carouselConfig) : heroCarousel.scroller.refresh();
          }, 1000)
      };
      onImageLoad(img, handler);
     // heroCarousel.tagElement(dojo.query('a.play-video', heroCarousel.domNode)[0]);
    },

    setCarouselData: function () {

    	var heroCarousel = this, imagesize, data;

        // filter correct image size.
        imagesize = (dojo.query(heroCarousel.domNode).parents('.span-two-third').length > 0) ? 'large' : 'xlarge';
        data = heroCarousel.jsonData.heroCarouselMedia || heroCarousel.jsonData.galleryImages;
        heroCarousel.jsonData.heroImages = _.uniq(_.filter(data , function(item) {
           return (item.size === imagesize);
        }), 'code');
    },

    determineListWidth: function() {
        var carousel = this;
        return domStyle.get(_.first(carousel.listItems()), 'width') * carousel.jsonData.heroImages.length;
    },

    positionImg: function(image) {
      var heroCarousel = this;
      var imageHeight = image.height;
      var height = dojo.style(heroCarousel.getTransition().viewport, 'height');
      var top = (imageHeight > height) ? (imageHeight - height) / 2 : 0;
      dojo.style(image, 'top', [-top, 'px'].join(''));
    },

    setCaptionDomNode: function() {
      var heroCarousel = this;
      var html = heroCarousel.getCaptionHtml(0);
      var display = (html !== '') ? 'block' : 'none';
      if (display === 'none') {
        heroCarousel.captionShow = false;
      }
      heroCarousel.caption = dojo.create('p', {
        className: 'caption',
        style: { display: display },
        innerHTML: html
      }, dojo.query(heroCarousel.transition.viewportSelector, heroCarousel.domNode)[0], 'last');
    },

    getCaptionHtml: function(index) {
      var heroCarousel = this;
      var page = (index + 1) + ' of ' + (heroCarousel.getCarouselData().length);
      var description = heroCarousel.getCarouselData()[index].description;
      var html = (description) ? [page, description].join(' - ') : page;
      return html;
    },

    getCarouselData: function() {
      var heroCarousel = this;
      return heroCarousel.jsonData.heroImages;
    },

    addHeroCarouselEventListner: function() {
      var heroCarousel = this;

      heroCarousel.connect(heroCarousel.transition, 'onPage', function(index, element) {
        var html = heroCarousel.getCaptionHtml(index);
        if (html !== '') {
          dojo.html.set(heroCarousel.caption, html);
          heroCarousel.captionShow = true;
          heroCarousel.showCaption();
          return;
        }
        heroCarousel.captionShow = false;
        heroCarousel.hideWidget(heroCarousel.caption);
      });

      heroCarousel.connect(heroCarousel.transition, 'onCrossFadeEnd', function(controlIndex,
                                                                               transition) {
        if (controlIndex === 0) {
          heroCarousel.backPreload(transition);
        } else {
          heroCarousel.forwardPreload(transition);
        }
      });

      heroCarousel.connect(query(".prev",heroCarousel.domNode)[0], 'click', function(index, data,
                                                                                transition) {
    	  heroCarousel.createPrevFade();
      });

      heroCarousel.connect(query(".next",heroCarousel.domNode)[0], 'click', function(index, data,
                                                                                transition) {
    	  heroCarousel.createNextFade();
      });


      heroCarousel.connect(heroCarousel.domNode, 'onmousedown', function(event) {
    	 if(heroCarousel.scroller) {
    		 setTimeout(function() {
    			 heroCarousel.setImageCaption(heroCarousel.scroller.currentPage.pageX);
    		 }, 300);
    	 }
      });

      if(heroCarousel.domNode.addEventListener) {
	      heroCarousel.domNode.addEventListener("touchstart", function(){
	    	  if(heroCarousel.scroller) {
	     		 setTimeout(function() {
	     			 heroCarousel.setImageCaption(heroCarousel.scroller.currentPage.pageX);
	     		 }, 300);
	     	 }
	      }, false);
       }
    },

    showCaption: function() {
      var heroCarousel = this;
      if (heroCarousel.captionShow) {
        heroCarousel.showWidget(heroCarousel.caption);
      }
    },

    displayControls: function(display) {
      var heroCarousel = this;
      var show = (display) ? 'showWidget' : 'hideWidget';
      heroCarousel[show](heroCarousel.transition.controls[0]);
      heroCarousel[show](heroCarousel.transition.controls[1]);
    },

    stopAutoplay: function() {
      var heroCarousel = this;

      clearInterval(heroCarousel.autoplaytimer);
      heroCarousel.autoplaytimer = null;

    },

    startAutoplay: function() {
      var heroCarousel = this;
      if (heroCarousel.autoplaymode &&
        heroCarousel.jsonData.heroImages.length > 1) {
        heroCarousel.autoplaytimer = setInterval(function() {
          heroCarousel.autoplay();
        }, heroCarousel.autoplaygap);
      }
    },

    autoplay: function() {
      var heroCarousel = this;
      if (heroCarousel.pause) {
        return;
      }
      heroCarousel.createNextFade();
    },

    backPreload: function(transition) {
      var heroCarousel = this;
      var start = transition.currentIndex - 1;
      var end = start - transition.itemShow;
      end = Math.max(end, 0);
      for (var i = start; i > end; i--) {
        var data = heroCarousel.jsonData.heroImages[i];
        heroCarousel.preloadImage(data);
      }
    },

    forwardPreload: function(transition) {
      var heroCarousel = this;
      var start = transition.currentIndex + 1;
      var end = start + transition.itemShow;
      end = Math.min(end, heroCarousel.getCarouselData().length);
      for (var i = start; i < end; i++) {
        var data = heroCarousel.jsonData.heroImages[i];
        heroCarousel.preloadImage(data);
      }
    },

    append: function(data, transition) {
      var heroCarousel = this;
      if (!data.domNode) {
        var html = heroCarousel.createTmpl(data);
        var nodeItem = null;
        if (transition.slideItemElements.length > 0) {
          var len = transition.slideItemElements.length - 1;
          nodeItem = dojo.place(html, transition.slideItemElements[len], 'after');
        }
        data.domNode = nodeItem;
        var img = dojo.create('img');
        img.src = data.img.src;
        img.alt = 'Image';
        //data-dojo-props eg: 'useCustomSize: true ,height:563 , width:1000' to restrict image size
        //                     to 1000*563
        if(heroCarousel.useCustomSize){
           heroCarousel.setImageSize(img, heroCarousel.width,heroCarousel.height);
        }
        dojo.place(img, nodeItem);
        heroCarousel.positionImg(img);
        transition.slideItemElements.push(nodeItem);
        domStyle.set(nodeItem, "width", domStyle.get(_.first(heroCarousel.listItems()), 'width') + "px");

      }
    },

    appendScroll: function(data, transition) {
        var heroCarousel = this;
        if (!data.domNode) {
          var html = heroCarousel.createTmpl(data);
          var nodeItem = null;

          if (transition.length > 0) {
              var len = data - 1;
              var list = dojo.query('li', heroCarousel.domNode)[len];
              nodeItem = dojo.place(html, list, 'after');
            }
          data.domNode = nodeItem;
          var img = dojo.create('img');
          img.src = heroCarousel.jsonData.heroImages[data].mainSrc;
          img.alt = 'Image';
          //data-dojo-props eg: 'useCustomSize: true ,height:563 , width:1000' to restrict image size
          //                     to 1000*563
          if(heroCarousel.useCustomSize){
             heroCarousel.setImageSize(img, heroCarousel.width,heroCarousel.height);
          }
          dojo.place(img, nodeItem);

          domStyle.set(nodeItem, "width", domStyle.get(_.first(heroCarousel.listItems()), 'width') + "px");
        }
      },

      createNextFade: function() {
          var heroCarousel = this,
              nextImage = null;
          nextImage = _.first(query("li[idx=" + heroCarousel.scroller.currentPage.pageX + "]", heroCarousel.domNode));
          if(heroCarousel.scroller.currentPage.pageX === (heroCarousel.scroller.pages.length - 1)){
        	  heroCarousel.scroller.goToPage(0, 0);
          } else {
        	  heroCarousel.scroller.next();
          }
          heroCarousel.setImageCaption(heroCarousel.scroller.currentPage.pageX);
      },

      setImageCaption: function(){
    	  var heroCarousel = this,
    	  	  html = heroCarousel.getCaptionHtml(heroCarousel.scroller.currentPage.pageX);
    	  if (html !== '') {
              dojo.html.set(heroCarousel.caption, html);
              heroCarousel.captionShow = true;
              heroCarousel.showCaption();
              return;
            }
          heroCarousel.captionShow = false;
          heroCarousel.hideWidget(heroCarousel.caption);

      },

      createPrevFade: function() {
          var heroCarousel = this,
              prevImage = null;
          prevImage = _.first(query("li[idx=" + heroCarousel.scroller.currentPage.pageX + "]", heroCarousel.domNode));
          if(heroCarousel.scroller.currentPage.pageX === 0){
        	  heroCarousel.scroller.goToPage((heroCarousel.scroller.pages.length - 1), 0);
          } else {
        	  heroCarousel.scroller.prev();
          }
          heroCarousel.setImageCaption(heroCarousel.scroller.currentPage.pageX);
      },

    preloadImage: function(data) {
      var heroCarousel = this;
      if (data.img || data.domNode) {
        return;
      }
      data.img = new Image();
      data.img.src = data.mainSrc;
    },

    // ---------------------------------------------------------------- carousel methods.

    addNewContent: function() {
      // summary:
      //		Method is extended from the carousel class. This is where we preloads the first few
      //		images.
      var heroCarousel = this;
      heroCarousel.transition.currentIndex = heroCarousel.getCarouselData().length;
      heroCarousel.backPreload(heroCarousel.transition);
      heroCarousel.transition.currentIndex = 0;
      heroCarousel.forwardPreload(heroCarousel.transition);
      for (var i = 0; i < heroCarousel.transition.slideItemElements.length; i++) {
        var data = heroCarousel.jsonData.heroImages[i];
        data.domNode = heroCarousel.transition.slideItemElements[i];
      }
    },

    destroyRecursive: function() {
        var heroCarousel = this;
        heroCarousel.stopAutoplay();
        heroCarousel.inherited(arguments);
    },

    setImageSize: function (image, w, h){
          image.width = w;
          image.height = h;
    }
  });

  return tui.widget.media.HeroCarousel;
});
