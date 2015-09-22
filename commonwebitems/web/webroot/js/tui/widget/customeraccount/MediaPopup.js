define("tui/widget/customeraccount/MediaPopup", [
  'dojo',
  'dojo/on',
  'dojo/dom-attr',
  'dojo/_base/fx',
  'dojo/_base/array',
  'dojo/text!tui/widget/media/templates/mediaTmpl.html',
  'tui/widget/popup/Popup',
  'tui/widget/maps/Mappers',
  'dojo/query',
  'dojo/window',
  'tui/widget/_TuiBaseWidget',
  'dojo/NodeList-traverse' ], function (dojo, on, domAttr, baseFx, array, mediaTmpl, popup, mappers, query, win) {

  dojo.declare("tui.widget.customeraccount.MediaPopup", [tui.widget.popup.Popup, tui.widget._TuiBaseWidget], {

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

    start: 0,

    end: 0,

    includeScroll: true,

    dataPath: null,

    loadDelay: 5000,

    //--------------------------------------------------------------methods

    postCreate: function () {
      var mediaPopup = this;
      mediaPopup.inherited(arguments);
      //console.log(mediaPopup.jsonData[mediaPopup.dataPath])
      mediaPopup.jsonData = mappers.thumbnailSet(mediaPopup.dataPath ? mediaPopup.jsonData[mediaPopup.dataPath] : mediaPopup.jsonData);
      mediaPopup.jsonData.galleryImagesLength = _.size(mediaPopup.jsonData.galleryImages);
      //console.log(mediaPopup.jsonData.galleryImages)
    },

    onAfterTmplRender: function () {
      var mediaPopup = this;
      mediaPopup.inherited(arguments);
      mediaPopup.addEventListener();
      var timer = setTimeout(function () {
        mediaPopup.hideLoading();
      }, mediaPopup.loadDelay);

      mediaPopup.startAutoplay();

      var images = dojo.query('.product-list li', mediaPopup.popupDomNode).forEach(function (item, i) {
        domAttr.set(item, "idx", i);
        if (i === 0) {
          dojo.addClass(item, 'active');
          mediaPopup.getcaption(item);
          dojo.setStyle(item, 'zIndex', '1');
        }
        else {
          dojo.setStyle(item, 'opacity', '0');
          dojo.setStyle(item, 'zIndex', '0');
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

      if(mediaPopup.jsonData.galleryImagesLength > 1) {
        var thumb = dojo.query('ul.plist img', mediaPopup.popupDomNode).forEach(function (item, i) {
          domAttr.set(item, "idx", i);
        });
        // calculating the max number of slides for the thumb Carousel

        mediaPopup.thumbViewport = dojo.query('.thumb-carousel', mediaPopup.popupDomNode)[0];
        mediaPopup.thumbViewportWidth = dojo.style(mediaPopup.thumbViewport, 'width');

        mediaPopup.itemContainer = dojo.query('ul.plist', mediaPopup.popupDomNode)[0];
        mediaPopup.itemContainerWidth = mediaPopup.itemElementWidth * thumb.length;

        mediaPopup.slidesNo = Math.ceil(mediaPopup.itemContainerWidth / mediaPopup.thumbViewportWidth);
      }

    },

    hideLoading: function () {
      var mediaPopup = this;
      dojo.addClass(mediaPopup.popupDomNode, 'loaded');
      dojo.removeClass(dojo.query('.container', mediaPopup.popupDomNode)[0], 'loading');
    },

    addEventListener: function () {
      var mediaPopup = this;
      /* DE5279, Commented as a fix for DE5279
       * var mediaLink = dojo.query('.view-Images', mediaPopup.domNode)[0];
      var mediaConnect = mediaPopup.connect(mediaLink, 'onclick', function (event) {
        dojo.stopEvent(event);
        if (!mediaPopup.mediaPopup) {
          mediaPopup.open();
        }
        dojo.disconnect(mediaConnect);
      });*/

      if(mediaPopup.jsonData.galleryImagesLength > 1) {
        mediaPopup.thumbImages = dojo.query('ul.plist img', mediaPopup.popupDomNode);

        on(mediaPopup.popupDomNode, 'ul.plist img:mouseover', function (event) {
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
      }
      on(mediaPopup.popupDomNode, '.next-large:click', function (event) {
        mediaPopup.createNextFade();
      });

      on(mediaPopup.popupDomNode, '.prev-large:click', function (event) {
        mediaPopup.createPrevFade();
      });

      on(mediaPopup.popupDomNode, '.next:click', function (event) {
        mediaPopup.createNextSlide();
      });

      on(mediaPopup.popupDomNode, '.prev:click', function (event) {
        mediaPopup.createPrevSlide();
      });

      on(mediaPopup.popupDomNode, 'mouseover', function (event) {
        mediaPopup.stopAutoplay();
      });

      on(mediaPopup.popupDomNode, 'mouseout', function (event) {
        mediaPopup.startAutoplay();
      });
    },

    scrollChange: function (event) {
      var mediaPopup = this;
      var images = dojo.query('ul.product-list li', mediaPopup.popupDomNode);//Get images contained
      var old = dojo.query('.active', mediaPopup.popupDomNode)[0];
      mediaPopup.current = domAttr.get(event.target, 'idx');
      if (old === images[mediaPopup.current]) {
        return;
      }
      mediaPopup.createFx(old, 1, 0, 0);
      dojo.removeClass(old, 'active');
      mediaPopup.createFx(images[mediaPopup.current], 0.4, 1, 1);
      dojo.addClass(images[mediaPopup.current], 'active');
      mediaPopup.getcaption(images[mediaPopup.current]);
    },

    createNextFade: function () {
      var mediaPopup = this;
      var current = dojo.query('.active', mediaPopup.popupDomNode)[0];
      var next = dojo.query('.active', mediaPopup.popupDomNode).next()[0];
      var images = dojo.query('ul.product-list li', mediaPopup.popupDomNode);
      if (next) {
        mediaPopup.createFx(current, 1, 0, 0);
        dojo.removeClass(current, 'active');
        mediaPopup.createFx(next, 0.4, 1, 1);
        dojo.addClass(next, 'active');
        mediaPopup.getcaption(next);
        var index = dojo.indexOf(images, next);
        if (index % 10 === 0 && index === ((mediaPopup.currentSlide + 1) * 10)) {
          mediaPopup.createNextSlide();
        }
      }
      else {
        next = dojo.query('ul.product-list li', mediaPopup.popupDomNode)[0];
        mediaPopup.createFx(current, 1, 0, 0);
        dojo.removeClass(current, 'active');
        mediaPopup.createFx(next, 0.4, 1, 1);
        dojo.addClass(next, 'active');
        mediaPopup.getcaption(next);
        for (i = (mediaPopup.slidesNo - 1); i >= 0; i--) {
          mediaPopup.createPrevSlide();
        }
      }
    },

    createPrevFade: function () {
      var mediaPopup = this;
      var images = dojo.query('ul.product-list li', mediaPopup.popupDomNode);
      var current = dojo.query('.active', mediaPopup.popupDomNode)[0];
      var prev = dojo.query('.active', mediaPopup.popupDomNode).prev()[0];
      if (prev) {
        mediaPopup.createFx(current, 1, 0, 0);
        dojo.removeClass(current, 'active');
        mediaPopup.createFx(prev, 0.4, 1, 1);
        dojo.addClass(prev, 'active');
        mediaPopup.getcaption(prev);
        var index = dojo.indexOf(images, prev);
        if ((index + 1) % 10 === 0 && (index + 1) === (mediaPopup.currentSlide * 10)) {
          mediaPopup.createPrevSlide();
        }
      }
      else {
        prev = dojo.query('ul.product-list li', mediaPopup.popupDomNode);
        prev = prev[prev.length - 1];
        mediaPopup.createFx(current, 1, 0, 0);
        dojo.removeClass(current, 'active');
        mediaPopup.createFx(prev, 0.4, 1, 1);
        dojo.addClass(prev, 'active');
        mediaPopup.getcaption(prev);
        for (i = (mediaPopup.slidesNo - 1); i >= 0; i--) {
          mediaPopup.createNextSlide();
        }
      }
    },

    getcaption: function (node) {
      var mediaPopup = this;
      var images = dojo.query('ul.product-list li', mediaPopup.popupDomNode);
      var index = dojo.indexOf(images, node);
      var description = domAttr.get(node, 'data-location-name');
      var page = (index + 1) + ' of ' + (images.length);
      var captionContent = (description) ? [page, description].join(' - ') : page;
      var captionEl = dojo.byId('media-popup-caption');
      captionEl.innerHTML = captionContent;
      dojo.setStyle(captionEl, 'display', 'block');
    },

    createNextSlide: function () {
      var mediaPopup = this;
      if (mediaPopup.currentSlide === mediaPopup.slidesNo - 1) {
        return;
      }
      else {
        mediaPopup.start = mediaPopup.end;
        mediaPopup.end = mediaPopup.slideAmount * (mediaPopup.currentSlide + 1);
        mediaPopup.animProp(mediaPopup.itemContainer, 1000, {right: {start: mediaPopup.start, end: mediaPopup.end, units: 'px'}}).play();
        mediaPopup.currentSlide++;
      }
    },

    createPrevSlide: function () {
      var mediaPopup = this;
      if (mediaPopup.currentSlide === 0) {
        return;
      }
      else {
        mediaPopup.start = mediaPopup.end;
        mediaPopup.end = mediaPopup.slideAmount * (mediaPopup.currentSlide - 1);
        mediaPopup.animProp(mediaPopup.itemContainer, 1000, {right: {start: mediaPopup.start, end: mediaPopup.end, units: 'px'}}).play();
        mediaPopup.currentSlide--;
      }
    },

    createFx: function (node, start, end, zindex) {
      var mediaPopup = this;
      baseFx.animateProperty({
        node: node,
        duration: 1000,
        properties: {
          opacity: {start: start, end: end}
        }
      }).play();
      dojo.setStyle(node, 'zIndex', zindex);

    },

    animProp: function (node, duration, properties, delay) {
      var anim = dojo.animateProperty({
        node: node,
        duration: duration,
        properties: properties,
        delay: delay || 0
      });
      return anim;
    },

    stopAutoplay: function () {
      var mediaPopup = this;
      clearInterval(mediaPopup.autoplaytimer);
      mediaPopup.autoplaytimer = null;

    },

    startAutoplay: function () {
      var mediaPopup = this;
      if (mediaPopup.autoplaymode && mediaPopup.jsonData.galleryImagesLength > 1) {
        mediaPopup.autoplaytimer = setInterval(function () {
          mediaPopup.autoplay();
        }, mediaPopup.autoplaygap);
      }
      mediaPopup.autoplaygap = 6000;
    },

    autoplay: function () {
      var mediaPopup = this;
      if (mediaPopup.pause) {
        return;
      }
      mediaPopup.createNextFade();
    },

    onOpen: function () {
      dojo.publish("tui/widget/media/HeroCarousel/onTemplateLoaded", [true]);
    },

    onClose: function () {
      var mediaPopup = this;
      mediaPopup.stopAutoplay();
      dojo.publish("tui/widget/media/HeroCarousel/onTemplateLoaded", [false]);
    }

  });

  return tui.widget.customeraccount.MediaPopup;
});