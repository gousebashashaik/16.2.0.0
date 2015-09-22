define('tui/widget/media/HeroCarousel', [
  'dojo',
  'dojo/text!tui/widget/media/templates/HeroTmpl.html',
  'tui/widget/media/VideoPopup',
  'dojo/query',
  'dojo/NodeList-traverse',
  'dojo/html',
  'tui/widget/carousels/CrossFadeCarousel'
], function(dojo, heroTmpl, videoPopup) {

  dojo.declare('tui.widget.media.HeroCarousel', [tui.widget.carousels.CrossFadeCarousel], {

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
      heroCarousel.startAutoplay();

      var li = dojo.query('li', heroCarousel.domNode)[0];
      heroCarousel.liHeight = dojo.style(li, 'height');

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
      var page = (heroCarousel.transition.currentPage) + ' of ' + (heroCarousel.getCarouselData().length);
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

      heroCarousel.onPageHandle = heroCarousel.connect(heroCarousel.transition, 'onPage', function(index, element) {
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

      heroCarousel.onCrossFadeEndHandle = heroCarousel.connect(heroCarousel.transition, 'onCrossFadeEnd', function(controlIndex,
                                                                               transition) {
        if (controlIndex === 0) {
          heroCarousel.backPreload(transition);
        } else {
          heroCarousel.forwardPreload(transition);
        }
      });

      heroCarousel.onCrossFadePrevHandle = heroCarousel.connect(heroCarousel.transition, 'onCrossFadePrev', function(index, data,
                                                                                transition) {
        heroCarousel.append(data, transition);
      });

      heroCarousel.onCrossFadeNextHandle = heroCarousel.connect(heroCarousel.transition, 'onCrossFadeNext', function(index, data,
                                                                                transition) {
        heroCarousel.append(data, transition);
      });

      heroCarousel.onmouseoverHandle = heroCarousel.connect(heroCarousel.domNode, 'onmouseover', function(event) {
        heroCarousel.stopAutoplay();
      });

      heroCarousel.onmouseleaveHandle = heroCarousel.connect(heroCarousel.domNode, 'onmouseleave', function(event) {
        heroCarousel.startAutoplay();
      });
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
    	if (!window.attachEvent && window.addEventListener) {
    	    //  IE8
    	
      var heroCarousel = this;
      if (heroCarousel.pause) {
        return;
      }
      heroCarousel.transition.widgetTransition(heroCarousel.transition.controls[1]);
    }
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
      }
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
