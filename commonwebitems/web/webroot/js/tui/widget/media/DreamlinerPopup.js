define("tui/widget/media/DreamlinerPopup", [
  'dojo',
  'dojo/on',
  'dojo/dom-attr',
  'dojo/_base/fx',
  'dojo/_base/array',
  'dojo/text!tui/widget/media/templates/dreamlinerPopupTmpl.html',
  'tui/widget/popup/Popup',
  'tui/widget/maps/Mappers',
  'dojo/query',
  'dojo/window',
  'tui/widget/_TuiBaseWidget',
  'dojo/NodeList-traverse' ], function (dojo, on, domAttr, baseFx, array, mediaTmpl, popup, mappers, query, win) {

  dojo.declare("tui.widget.media.DreamlinerPopup", [tui.widget.popup.Popup, tui.widget._TuiBaseWidget], {

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

    ItemElementWidth: 96,

    slidesNo: null,

    currentSlide: 0,

    slideAmount: 990,

    start: 0,

    end: 0,

    //----tab2 starts -----------------//

    currentFirst: 0,
    itemContainerFirst: null,
    itemContainerWidthFirst: null,
    ItemElementWidthFirst: 96,
    slidesNoFirst: null,
    currentSlideFirst: 0,
    slideAmountFirst: 990,
    startFirst: 0,
    endFirst: 0,

    //------tab2 ends-----------------//
    //----tab3 starts -----------------//

    currentSecond: 0,
    itemContainerSecond: null,
    itemContainerWidthSecond: null,
    ItemElementWidthSecond: 96,
    slidesNoSecond: null,
    currentSlideSecond: 0,
    slideAmountSecond: 990,
    startSecond: 0,
    endSecond: 0,

    //------tab3 ends-----------------//
    //----tab4 starts -----------------//

    currentThird: 0,
    itemContainerThird: null,
    itemContainerWidthThird: null,
    ItemElementWidthThird: 96,
    slidesNoThird: null,
    currentSlideThird: 0,
    slideAmountThird: 990,
    startThird: 0,
    endThird: 0,

    //------tab4 ends-----------------//
    //----tab5 starts -----------------//

    currentFourth: 0,
    itemContainerFourth: null,
    itemContainerWidthFourth: null,
    ItemElementWidthFourth: 96,
    slidesNoFourth: null,
    currentSlideFourth: 0,
    slideAmountFourth: 990,
    startFourth: 0,
    endFourth: 0,

    //------tab5 ends-----------------//

    //--------------------------------------------------------------methods

    postCreate: function () {
      var mediaPopup = this;
      mediaPopup.inherited(arguments);
      mediaPopup.jsonData = mappers.thumbnailSet(mediaPopup.jsonData);
      console.debug("coming here:" + mediaPopup.jsonData);
    },

    onAfterTmplRender: function () {

      //console.log("coming here 2");
      var mediaPopup = this;

      mediaPopup.inherited(arguments);
      mediaPopup.addEventListener();
      var timer = setTimeout(function () {
        mediaPopup.hideLoading();
      }, 5000);

      mediaPopup.startAutoplay();

      var images = dojo.query('#first .product-list li', mediaPopup.popupDomNode).forEach(function (item, i) {
        domAttr.set(item, "idx", i);
        if (i === 0) {
          dojo.addClass(item, 'active');
          mediaPopup.getcaption(item);
        }
        else {
          dojo.setStyle(item, 'opacity', '0');
          dojo.setStyle(item, 'zIndex', '1');
        }
      });

      var vs = win.getBox();
      if (vs.h < 650) {
        var large = dojo.query('#first .large-carousel', mediaPopup.popupDomNode)[0];
        var largeHeight = dojo.style(large, "height");
        var height = [(largeHeight - ((650 - vs.h) + 50)), 'px'].join("");
        dojo.style(large, {
          "height": height
        });
      }

      //tab2 starts
      var images = dojo.query('#second .product-list1 li', mediaPopup.popupDomNode).forEach(function (item, i) {
        domAttr.set(item, "idx1", i);
        if (i === 0) {
          dojo.addClass(item, 'active1');
          mediaPopup.getcaptionFirst(item);
        }
        else {
          dojo.setStyle(item, 'opacity', '0');
          dojo.setStyle(item, 'zIndex', '1');
        }
      });

      var vs = win.getBox();
      if (vs.h < 650) {
        var large = dojo.query('#second .large-carousel1', mediaPopup.popupDomNode)[0];
        var largeHeight = dojo.style(large, "height");
        var height = [(largeHeight - ((650 - vs.h) + 50)), 'px'].join("");
        dojo.style(large, {
          "height": height
        });
      }
      //tab2 ends
      //tab3 starts
      var images = dojo.query('#third .product-list2 li', mediaPopup.popupDomNode).forEach(function (item, i) {
        domAttr.set(item, "idx2", i);
        if (i === 0) {
          dojo.addClass(item, 'active2');
          mediaPopup.getcaptionSecond(item);
        }
        else {
          dojo.setStyle(item, 'opacity', '0');
          dojo.setStyle(item, 'zIndex', '1');
        }
      });

      var vs = win.getBox();
      if (vs.h < 650) {
        var large = dojo.query('#third .large-carousel2', mediaPopup.popupDomNode)[0];
        var largeHeight = dojo.style(large, "height");
        var height = [(largeHeight - ((650 - vs.h) + 50)), 'px'].join("");
        dojo.style(large, {
          "height": height
        });
      }
      //tab3 ends
      //tab4 starts
      var images = dojo.query('#fourth .product-list3 li', mediaPopup.popupDomNode).forEach(function (item, i) {
        domAttr.set(item, "idx3", i);
        if (i === 0) {
          dojo.addClass(item, 'active3');
          mediaPopup.getcaptionThird(item);
        }
        else {
          dojo.setStyle(item, 'opacity', '0');
          dojo.setStyle(item, 'zIndex', '1');
        }
      });

      var vs = win.getBox();
      if (vs.h < 650) {
        var large = dojo.query('#fourth .large-carousel3', mediaPopup.popupDomNode)[0];
        var largeHeight = dojo.style(large, "height");
        var height = [(largeHeight - ((650 - vs.h) + 50)), 'px'].join("");
        dojo.style(large, {
          "height": height
        });
      }
      //tab4 ends
      //tab5 starts
      var images = dojo.query('#fifth .product-list4 li', mediaPopup.popupDomNode).forEach(function (item, i) {
        domAttr.set(item, "idx4", i);
        if (i === 0) {
          dojo.addClass(item, 'active4');
          mediaPopup.getcaption4(item);
        }
        else {
          dojo.setStyle(item, 'opacity', '0');
          dojo.setStyle(item, 'zIndex', '1');
        }
      });

      var vs = win.getBox();
      if (vs.h < 650) {
        var large = dojo.query('#fifth .large-carousel4', mediaPopup.popupDomNode)[0];
        var largeHeight = dojo.style(large, "height");
        var height = [(largeHeight - ((650 - vs.h) + 50)), 'px'].join("");
        dojo.style(large, {
          "height": height
        });
      }
      //tab5 ends

    },

    hideLoading: function () {
      var mediaPopup = this;
      var container = dojo.query('.container', mediaPopup.popupDomNode)[0];
      var load = dojo.query('.load', mediaPopup.popupDomNode)[0];
      var loadDiv = dojo.query('.bg-load', mediaPopup.popupDomNode)[0];
      dojo.style(container, 'visibility', 'visible');
      dojo.style(load, 'display', 'none');
      dojo.style(loadDiv, 'background', '#111111');
    },

    addEventListener: function () {
      var mediaPopup = this;
      var mediaLink = dojo.query('.view-Images', mediaPopup.domNode)[0];
      var mediaConnect = mediaPopup.connect(mediaLink, 'onclick', function (event) {
        dojo.stopEvent(event);
        if (!mediaPopup.mediaPopup) {
          mediaPopup.open();
        }
        dojo.disconnect(mediaConnect);
      });

      on(mediaPopup.popupDomNode, '#first .next-large:click', function (event) {
        mediaPopup.createNextFade();
      });

      on(mediaPopup.popupDomNode, '#first .prev-large:click', function (event) {
        mediaPopup.createPrevFade();
      });

      //tabs2 starts ---------
      on(mediaPopup.popupDomNode, '#second .next-large1:click', function (event) {
        mediaPopup.createNextFadeFirst();
      });

      on(mediaPopup.popupDomNode, '#second .prev-large1:click', function (event) {
        mediaPopup.createPrevFadeFirst();
      });
      //tabs2 ends -----------
      //tabs3 starts ---------
      on(mediaPopup.popupDomNode, '#third .next-large2:click', function (event) {
        mediaPopup.createNextFadeSecond();
      });

      on(mediaPopup.popupDomNode, '#third .prev-large2:click', function (event) {
        mediaPopup.createPrevFadeSecond();
      });
      //tabs3 ends -----------
      //tabs4 starts ---------
      on(mediaPopup.popupDomNode, '#fourth .next-large3:click', function (event) {
        mediaPopup.createNextFadeThird();
      });

      on(mediaPopup.popupDomNode, '#fourth .prev-large3:click', function (event) {
        mediaPopup.createPrevFadeThird();
      });
      //tabs4 ends -----------
      //tabs5 starts ---------
      on(mediaPopup.popupDomNode, '#fifth .next-large4:click', function (event) {
        mediaPopup.createNextFade4();
      });

      on(mediaPopup.popupDomNode, '#fifth .prev-large4:click', function (event) {
        mediaPopup.createPrevFade4();
      });
      //tabs5 ends -----------

      //adding click event to the tabs
      on(mediaPopup.popupDomNode, '#firsttab:click', function (event) {
        ////console.log('clicked first tab');
        window.scrollTo(0, 0);
        dojo.query('#first', mediaPopup.popupDomNode)[0].style.display = 'block';
        dojo.query('#second', mediaPopup.popupDomNode)[0].style.display = 'none';
        dojo.query('#third', mediaPopup.popupDomNode)[0].style.display = 'none';
        dojo.query('#fourth', mediaPopup.popupDomNode)[0].style.display = 'none';
        dojo.query('#fifth', mediaPopup.popupDomNode)[0].style.display = 'none';

        mediaPopup.tabsClass("firsttab");

      });

      on(mediaPopup.popupDomNode, '#secondtab:click', function (event) {
        ////console.log('clicked second tab');
        window.scrollTo(0, 0);
        dojo.query('#first', mediaPopup.popupDomNode)[0].style.display = 'none';
        dojo.query('#second', mediaPopup.popupDomNode)[0].style.display = 'block';
        dojo.query('#third', mediaPopup.popupDomNode)[0].style.display = 'none';
        dojo.query('#fourth', mediaPopup.popupDomNode)[0].style.display = 'none';
        dojo.query('#fifth', mediaPopup.popupDomNode)[0].style.display = 'none';

        mediaPopup.tabsClass("secondtab");
      });

      on(mediaPopup.popupDomNode, '#thirdtab:click', function (event) {
        ////console.log('clicked third tab');
        window.scrollTo(0, 0);
        dojo.query('#first', mediaPopup.popupDomNode)[0].style.display = 'none';
        dojo.query('#second', mediaPopup.popupDomNode)[0].style.display = 'none';
        dojo.query('#third', mediaPopup.popupDomNode)[0].style.display = 'block';
        dojo.query('#fourth', mediaPopup.popupDomNode)[0].style.display = 'none';
        dojo.query('#fifth', mediaPopup.popupDomNode)[0].style.display = 'none';
        mediaPopup.tabsClass("thirdtab");
      });

      on(mediaPopup.popupDomNode, '#fourthtab:click', function (event) {
        ////console.log('fourth fourth tab');
        window.scrollTo(0, 0);
        dojo.query('#first', mediaPopup.popupDomNode)[0].style.display = 'none';
        dojo.query('#second', mediaPopup.popupDomNode)[0].style.display = 'none';
        dojo.query('#third', mediaPopup.popupDomNode)[0].style.display = 'none';
        dojo.query('#fourth', mediaPopup.popupDomNode)[0].style.display = 'block';
        dojo.query('#fifth', mediaPopup.popupDomNode)[0].style.display = 'none';
        mediaPopup.tabsClass("fourthtab");
      });

      on(mediaPopup.popupDomNode, '#fifthtab:click', function (event) {
        ////console.log('fifth fifth tab');
        window.scrollTo(0, 0);
        dojo.query('#first', mediaPopup.popupDomNode)[0].style.display = 'none';
        dojo.query('#second', mediaPopup.popupDomNode)[0].style.display = 'none';
        dojo.query('#third', mediaPopup.popupDomNode)[0].style.display = 'none';
        dojo.query('#fourth', mediaPopup.popupDomNode)[0].style.display = 'none';
        dojo.query('#fifth', mediaPopup.popupDomNode)[0].style.display = 'block';
        mediaPopup.tabsClass("fifthtab");
      });
    },

    createNextFade: function () {
      var mediaPopup = this;
      var current = dojo.query('.active', mediaPopup.popupDomNode)[0];
      var next = dojo.query('.active', mediaPopup.popupDomNode).next()[0];
      var images = dojo.query('#first ul.product-list li', mediaPopup.popupDomNode);
      if (next) {
        mediaPopup.createFx(current, 1, 0, 1);
        dojo.removeClass(current, 'active');
        mediaPopup.createFx(next, 0.4, 1, 0);
        dojo.addClass(next, 'active');
        mediaPopup.getcaption(next);
        var index = _.indexOf(images, next);
        if (index % 10 === 0 && index === ((mediaPopup.currentSlide + 1) * 10)) {
          mediaPopup.createNextSlide();
        }
      }
      else {
        next = dojo.query('#first ul.product-list li', mediaPopup.popupDomNode)[0];
        mediaPopup.createFx(current, 1, 0, 1);
        dojo.removeClass(current, 'active');
        mediaPopup.createFx(next, 0.4, 1, 0);
        dojo.addClass(next, 'active');
        mediaPopup.getcaption(next);
        for (i = (mediaPopup.slidesNo - 1); i >= 0; i--) {
          mediaPopup.createPrevSlide();
        }
      }
    },

    createPrevFade: function () {
      var mediaPopup = this;
      var images = dojo.query('#first ul.product-list li', mediaPopup.popupDomNode);
      var current = dojo.query('.active', mediaPopup.popupDomNode)[0];
      var prev = dojo.query('.active', mediaPopup.popupDomNode).prev()[0];
      if (prev) {
        mediaPopup.createFx(current, 1, 0, 1);
        dojo.removeClass(current, 'active');
        mediaPopup.createFx(prev, 0.4, 1, 0);
        dojo.addClass(prev, 'active');
        mediaPopup.getcaption(prev);
        var index = _.indexOf(images, prev);
        if ((index + 1) % 10 === 0 && (index + 1) === (mediaPopup.currentSlide * 10)) {
          mediaPopup.createPrevSlide();
        }
      }
      else {
        prev = dojo.query('#first ul.product-list li', mediaPopup.popupDomNode);
        prev = prev[prev.length - 1];
        mediaPopup.createFx(current, 1, 0, 1);
        dojo.removeClass(current, 'active');
        mediaPopup.createFx(prev, 0.4, 1, 0);
        dojo.addClass(prev, 'active');
        mediaPopup.getcaption(prev);
        for (i = (mediaPopup.slidesNo - 1); i >= 0; i--) {
          mediaPopup.createNextSlide();
        }
      }
    },

    getcaption: function (node) {
      var mediaPopup = this;
      var images = dojo.query('#first .large-carousel ul.product-list li', mediaPopup.popupDomNode);

      //console.log("length first tab:"+images.length);
      var index = _.indexOf(images, node);
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
      dojo.setStyle(node, 'zIndex', zindex);
      baseFx.animateProperty({
        node: node,
        duration: 1000,
        properties: {
          opacity: {start: start, end: end}
        }
      }).play();

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

    //tab2 all functions starts ---------------------

    createNextFadeFirst: function () {
      var mediaPopup = this;
      var currentFirst = dojo.query('.active1', mediaPopup.popupDomNode)[0];
      var next1 = dojo.query('.active1', mediaPopup.popupDomNode).next()[0];
      var images1 = dojo.query('#second ul.product-list1 li', mediaPopup.popupDomNode);
      if (next1) {
        mediaPopup.createFx(currentFirst, 1, 0, 1);
        dojo.removeClass(currentFirst, 'active1');
        mediaPopup.createFx(next1, 0.4, 1, 0);
        dojo.addClass(next1, 'active1');
        mediaPopup.getcaptionFirst(next1);
        var index = _.indexOf(images1, next1);
        if (index % 10 === 0 && index === ((mediaPopup.currentSlideFirst + 1) * 10)) {
          mediaPopup.createNextSlideFirst();
        }
      }
      else {
        next1 = dojo.query('#second ul.product-list1 li', mediaPopup.popupDomNode)[0];
        mediaPopup.createFx(currentFirst, 1, 0, 1);
        dojo.removeClass(currentFirst, 'active1');
        mediaPopup.createFx(next1, 0.4, 1, 0);
        dojo.addClass(next1, 'active1');
        mediaPopup.getcaptionFirst(next1);
        for (i = (mediaPopup.slidesNoFirst - 1); i >= 0; i--) {
          mediaPopup.createPrevSlideFirst();
        }
      }
    },

    createPrevFadeFirst: function () {
      var mediaPopup = this;
      var images = dojo.query('#second ul.product-list1 li', mediaPopup.popupDomNode);
      var currentFirst = dojo.query('.active1', mediaPopup.popupDomNode)[0];
      var prev1 = dojo.query('.active1', mediaPopup.popupDomNode).prev()[0];
      if (prev1) {
        mediaPopup.createFx(currentFirst, 1, 0, 1);
        dojo.removeClass(currentFirst, 'active1');
        mediaPopup.createFx(prev1, 0.4, 1, 0);
        dojo.addClass(prev1, 'active1');
        mediaPopup.getcaptionFirst(prev1);
        var index = _.indexOf(images, prev1);
        if ((index + 1) % 10 === 0 && (index + 1) === (mediaPopup.currentSlideFirst * 10)) {
          mediaPopup.createPrevSlideFirst();
        }
      }
      else {
        prev1 = dojo.query('#second ul.product-list1 li', mediaPopup.popupDomNode);
        prev1 = prev1[prev1.length - 1];
        mediaPopup.createFx(currentFirst, 1, 0, 1);
        dojo.removeClass(currentFirst, 'active1');
        mediaPopup.createFx(prev1, 0.4, 1, 0);
        dojo.addClass(prev1, 'active1');
        mediaPopup.getcaptionFirst(prev1);
        for (i = (mediaPopup.slidesNoFirst - 1); i >= 0; i--) {
          mediaPopup.createNextSlideFirst();
        }
      }
    },

    getcaptionFirst: function (node) {
      var mediaPopup = this;
      var images = dojo.query('#second .large-carousel1 ul.product-list1 li', mediaPopup.popupDomNode);
      //console.log("length:"+images.length);
      var index = _.indexOf(images, node);
      var description = domAttr.get(node, 'data-location-name1');
      var page = (index + 1) + ' of ' + (images.length);
      var captionContent = (description) ? [page, description].join(' - ') : page;
      var captionEl = dojo.byId('media-popup-caption1');
      captionEl.innerHTML = captionContent;
      dojo.setStyle(captionEl, 'display', 'block');
    },

    createNextSlideFirst: function () {
      var mediaPopup = this;
      if (mediaPopup.currentSlideFirst === mediaPopup.slidesNoFirst - 1) {
        return;
      }
      else {
        mediaPopup.startFirst = mediaPopup.endFirst;
        mediaPopup.endFirst = mediaPopup.slideAmountFirst * (mediaPopup.currentSlideFirst + 1);
        mediaPopup.animProp(mediaPopup.itemContainerFirst, 1000, {right: {start: mediaPopup.startFirst, end: mediaPopup.endFirst, units: 'px'}}).play();
        mediaPopup.currentSlideFirst++;
      }
    },

    createPrevSlideFirst: function () {
      var mediaPopup = this;
      if (mediaPopup.currentSlideFirst === 0) {
        return;
      }
      else {
        mediaPopup.startFirst = mediaPopup.endFirst;
        mediaPopup.endFirst = mediaPopup.slideAmountFirst * (mediaPopup.currentSlideFirst - 1);
        mediaPopup.animProp(mediaPopup.itemContainerFirst, 1000, {right: {startFirst: mediaPopup.startFirst, end: mediaPopup.endFirst, units: 'px'}}).play();
        mediaPopup.currentSlideFirst--;
      }
    },

    //tab2 all functions ends here ------------------
    //tab3 all functions starts ---------------------

    createNextFadeSecond: function () {

      var mediaPopup = this;
      var currentSecond = dojo.query('.active2', mediaPopup.popupDomNode)[0];
      var next2 = dojo.query('.active2', mediaPopup.popupDomNode).next()[0];
      var images2 = dojo.query('#third ul.product-list2 li', mediaPopup.popupDomNode);

      if (next2) {

        mediaPopup.createFx(currentSecond, 1, 0, 1);

        dojo.removeClass(currentSecond, 'active2');

        mediaPopup.createFx(next2, 0.4, 1, 0);

        dojo.addClass(next2, 'active2');

        mediaPopup.getcaptionSecond(next2);
        var index = _.indexOf(images2, next2);
        if (index % 10 === 0 && index === ((mediaPopup.currentSlideSecond + 1) * 10)) {
          mediaPopup.createNextSlideSecond();
        }
      }
      else {
        next2 = dojo.query('#third ul.product-list2 li', mediaPopup.popupDomNode)[0];
        mediaPopup.createFx(currentSecond, 1, 0, 1);
        dojo.removeClass(currentSecond, 'active2');
        mediaPopup.createFx(next2, 0.4, 1, 0);
        dojo.addClass(next2, 'active2');
        mediaPopup.getcaptionSecond(next2);
        for (i = (mediaPopup.slidesNoSecond - 1); i >= 0; i--) {
          mediaPopup.createPrevSlideSecond();
        }
      }
    },

    createPrevFadeSecond: function () {
      var mediaPopup = this;
      var images = dojo.query('#third ul.product-list2 li', mediaPopup.popupDomNode);
      var currentSecond = dojo.query('.active2', mediaPopup.popupDomNode)[0];
      var prev2 = dojo.query('.active2', mediaPopup.popupDomNode).prev()[0];
      if (prev2) {
        mediaPopup.createFx(currentSecond, 1, 0, 1);
        dojo.removeClass(currentSecond, 'active2');
        mediaPopup.createFx(prev2, 0.4, 1, 0);
        dojo.addClass(prev2, 'active2');
        mediaPopup.getcaptionSecond(prev2);
        var index = _.indexOf(images, prev2);
        if ((index + 1) % 10 === 0 && (index + 1) === (mediaPopup.currentSlideSecond * 10)) {
          mediaPopup.createPrevSlideSecond();
        }
      }
      else {
        prev2 = dojo.query('#third ul.product-list2 li', mediaPopup.popupDomNode);
        prev2 = prev2[prev2.length - 1];
        mediaPopup.createFx(currentSecond, 1, 0, 1);
        dojo.removeClass(currentSecond, 'active2');
        mediaPopup.createFx(prev2, 0.4, 1, 0);
        dojo.addClass(prev2, 'active2');
        mediaPopup.getcaptionSecond(prev2);
        for (i = (mediaPopup.slidesNoSecond - 1); i >= 0; i--) {
          mediaPopup.createNextSlideSecond();
        }
      }
    },

    getcaptionSecond: function (node) {
      var mediaPopup = this;
      //console.log("before");
      var images = dojo.query('#third .large-carousel2 ul.product-list2 li', mediaPopup.popupDomNode);
      //console.log("length:"+images.length);
      var index = _.indexOf(images, node);
      var description = domAttr.get(node, 'data-location-name2');
      var page = (index + 1) + ' of ' + (images.length);
      var captionContent = (description) ? [page, description].join(' - ') : page;
      var captionEl = dojo.byId('media-popup-caption2');
      captionEl.innerHTML = captionContent;
      dojo.setStyle(captionEl, 'display', 'block');
    },

    createNextSlideSecond: function () {
      var mediaPopup = this;
      if (mediaPopup.currentSlideSecond === mediaPopup.slidesNoSecond - 1) {
        return;
      }
      else {
        mediaPopup.startSecond = mediaPopup.endSecond;
        mediaPopup.endSecond = mediaPopup.slideAmountSecond * (mediaPopup.currentSlideSecond + 1);
        mediaPopup.animProp(mediaPopup.itemContainerSecond, 1000, {right: {start: mediaPopup.startSecond, end: mediaPopup.endSecond, units: 'px'}}).play();
        mediaPopup.currentSlideSecond++;
      }
    },

    createPrevSlideSecond: function () {
      var mediaPopup = this;
      if (mediaPopup.currentSlideSecond === 0) {
        return;
      }
      else {
        mediaPopup.startSecond = mediaPopup.endSecond;
        mediaPopup.endSecond = mediaPopup.slideAmountSecond * (mediaPopup.currentSlideSecond - 1);
        mediaPopup.animProp(mediaPopup.itemContainerSecond, 1000, {right: {startSecond: mediaPopup.startSecond, end: mediaPopup.endSecond, units: 'px'}}).play();
        mediaPopup.currentSlideSecond--;
      }
    },

    //tab3 all functions ends here ------------------
    //tab4 all functions starts ---------------------

    createNextFadeThird: function () {
      var mediaPopup = this;
      var currentThird = dojo.query('.active3', mediaPopup.popupDomNode)[0];
      var next3 = dojo.query('.active3', mediaPopup.popupDomNode).next()[0];
      var images3 = dojo.query('#fourth ul.product-list3 li', mediaPopup.popupDomNode);
      if (next3) {
        mediaPopup.createFx(currentThird, 1, 0, 1);
        dojo.removeClass(currentThird, 'active3');
        mediaPopup.createFx(next3, 0.4, 1, 0);
        dojo.addClass(next3, 'active3');
        mediaPopup.getcaptionThird(next3);
        var index = _.indexOf(images3, next3);
        if (index % 10 === 0 && index === ((mediaPopup.currentSlideThird + 1) * 10)) {
          mediaPopup.createNextSlideThird();
        }
      }
      else {
        next3 = dojo.query('#fourth ul.product-list3 li', mediaPopup.popupDomNode)[0];
        mediaPopup.createFx(currentThird, 1, 0, 1);
        dojo.removeClass(currentThird, 'active3');
        mediaPopup.createFx(next3, 0.4, 1, 0);
        dojo.addClass(next3, 'active3');
        mediaPopup.getcaptionThird(next3);
        for (i = (mediaPopup.slidesNoThird - 1); i >= 0; i--) {
          mediaPopup.createPrevSlideThird();
        }
      }
    },

    createPrevFadeThird: function () {
      var mediaPopup = this;
      var images = dojo.query('#fourth ul.product-list3 li', mediaPopup.popupDomNode);
      var currentThird = dojo.query('.active3', mediaPopup.popupDomNode)[0];
      var prev3 = dojo.query('.active3', mediaPopup.popupDomNode).prev()[0];
      if (prev3) {
        mediaPopup.createFx(currentThird, 1, 0, 1);
        dojo.removeClass(currentThird, 'active3');
        mediaPopup.createFx(prev3, 0.4, 1, 0);
        dojo.addClass(prev3, 'active3');
        mediaPopup.getcaptionThird(prev3);
        var index = _.indexOf(images, prev3);
        if ((index + 1) % 10 === 0 && (index + 1) === (mediaPopup.currentSlideThird * 10)) {
          mediaPopup.createPrevSlideThird();
        }
      }
      else {
        prev3 = dojo.query('#fourth ul.product-list3 li', mediaPopup.popupDomNode);
        prev3 = prev3[prev3.length - 1];
        mediaPopup.createFx(currentThird, 1, 0, 1);
        dojo.removeClass(currentThird, 'active3');
        mediaPopup.createFx(prev3, 0.4, 1, 0);
        dojo.addClass(prev3, 'active3');
        mediaPopup.getcaptionThird(prev3);
        for (i = (mediaPopup.slidesNoThird - 1); i >= 0; i--) {
          mediaPopup.createNextSlideThird();
        }
      }
    },

    getcaptionThird: function (node) {
      var mediaPopup = this;
      var images = dojo.query('#fourth .large-carousel3 ul.product-list3 li', mediaPopup.popupDomNode);
      //console.log("length:"+images.length);
      var index = _.indexOf(images, node);
      var description = domAttr.get(node, 'data-location-name3');
      var page = (index + 1) + ' of ' + (images.length);
      var captionContent = (description) ? [page, description].join(' - ') : page;
      var captionEl = dojo.byId('media-popup-caption3');
      captionEl.innerHTML = captionContent;
      dojo.setStyle(captionEl, 'display', 'block');
    },

    createNextSlideThird: function () {
      var mediaPopup = this;
      if (mediaPopup.currentSlideThird === mediaPopup.slidesNoThird - 1) {
        return;
      }
      else {
        mediaPopup.startThird = mediaPopup.endThird;
        mediaPopup.endThird = mediaPopup.slideAmountSecond * (mediaPopup.currentSlideThird + 1);
        mediaPopup.animProp(mediaPopup.itemContainerThird, 1000, {right: {start: mediaPopup.startThird, end: mediaPopup.endThird, units: 'px'}}).play();
        mediaPopup.currentSlideThird++;
      }
    },

    createPrevSlideThird: function () {
      var mediaPopup = this;
      if (mediaPopup.currentSlideThird === 0) {
        return;
      }
      else {
        mediaPopup.startThird = mediaPopup.endThird;
        mediaPopup.endThird = mediaPopup.slideAmountThird * (mediaPopup.currentSlideThird - 1);
        mediaPopup.animProp(mediaPopup.itemContainerThird, 1000, {right: {startThird: mediaPopup.startThird, end: mediaPopup.endThird, units: 'px'}}).play();
        mediaPopup.currentSlideThird--;
      }
    },

    //tab4 all functions ends here ------------------
    //tab5 all functions starts ---------------------

    createNextFade4: function () {
      var mediaPopup = this;
      var currentFourth = dojo.query('.active4', mediaPopup.popupDomNode)[0];
      var next4 = dojo.query('.active4', mediaPopup.popupDomNode).next()[0];
      var images4 = dojo.query('#fifth ul.product-list4 li', mediaPopup.popupDomNode);
      if (next4) {
        mediaPopup.createFx(currentFourth, 1, 0, 1);
        dojo.removeClass(currentFourth, 'active4');
        mediaPopup.createFx(next4, 0.4, 1, 0);
        dojo.addClass(next4, 'active4');
        mediaPopup.getcaption4(next4);
        var index = _.indexOf(images4, next4);
        if (index % 10 === 0 && index === ((mediaPopup.currentSlideFourth + 1) * 10)) {
          mediaPopup.createNextSlide4();
        }
      }
      else {
        next4 = dojo.query('#fifth ul.product-list4 li', mediaPopup.popupDomNode)[0];
        mediaPopup.createFx(currentFourth, 1, 0, 1);
        dojo.removeClass(currentFourth, 'active4');
        mediaPopup.createFx(next4, 0.4, 1, 0);
        dojo.addClass(next4, 'active4');
        mediaPopup.getcaption4(next4);
        for (i = (mediaPopup.slidesNoFourth - 1); i >= 0; i--) {
          mediaPopup.createPrevSlide4();
        }
      }
    },

    createPrevFade4: function () {
      var mediaPopup = this;
      var images = dojo.query('#fifth ul.product-list4 li', mediaPopup.popupDomNode);
      var currentFourth = dojo.query('.active4', mediaPopup.popupDomNode)[0];
      var prev4 = dojo.query('.active4', mediaPopup.popupDomNode).prev()[0];
      if (prev4) {
        mediaPopup.createFx(currentFourth, 1, 0, 1);
        dojo.removeClass(currentFourth, 'active4');
        mediaPopup.createFx(prev4, 0.4, 1, 0);
        dojo.addClass(prev4, 'active4');
        mediaPopup.getcaption4(prev4);
        var index = _.indexOf(images, prev4);
        if ((index + 1) % 10 === 0 && (index + 1) === (mediaPopup.currentSlideFourth * 10)) {
          mediaPopup.createPrevSlide4();
        }
      }
      else {
        prev4 = dojo.query('#fifth ul.product-list4 li', mediaPopup.popupDomNode);
        prev4 = prev4[prev4.length - 1];
        mediaPopup.createFx(currentFourth, 1, 0, 1);
        dojo.removeClass(currentFourth, 'active4');
        mediaPopup.createFx(prev4, 0.4, 1, 0);
        dojo.addClass(prev4, 'active4');
        mediaPopup.getcaption4(prev4);
        for (i = (mediaPopup.slidesNoFourth - 1); i >= 0; i--) {
          mediaPopup.createNextSlide4();
        }
      }
    },

    getcaption4: function (node) {
      var mediaPopup = this;
      var images = dojo.query('#fifth .large-carousel4 ul.product-list4 li', mediaPopup.popupDomNode);
      //console.log("length:"+images.length);
      var index = _.indexOf(images, node);
      var description = domAttr.get(node, 'data-location-name4');
      var page = (index + 1) + ' of ' + (images.length);
      var captionContent = (description) ? [page, description].join(' - ') : page;
      var captionEl = dojo.byId('media-popup-caption4');
      captionEl.innerHTML = captionContent;
      dojo.setStyle(captionEl, 'display', 'block');
    },

    createNextSlide4: function () {
      var mediaPopup = this;
      if (mediaPopup.currentSlideFourth === mediaPopup.slidesNoFourth - 1) {
        return;
      }
      else {
        mediaPopup.startFourth = mediaPopup.endFourth;
        mediaPopup.endFourth = mediaPopup.slideAmountFourth * (mediaPopup.currentSlideFourth + 1);
        mediaPopup.animProp(mediaPopup.itemContainerFourth, 1000, {right: {start: mediaPopup.startFourth, end: mediaPopup.endFourth, units: 'px'}}).play();
        mediaPopup.currentSlideFourth++;
      }
    },

    createPrevSlideThird: function () {
      var mediaPopup = this;
      if (mediaPopup.currentSlideFourth === 0) {
        return;
      }
      else {
        mediaPopup.startFourth = mediaPopup.endFourth;
        mediaPopup.endFourth = mediaPopup.slideAmountFourth * (mediaPopup.currentSlideFourth - 1);
        mediaPopup.animProp(mediaPopup.itemContainerFourth, 1000, {right: {startFourth: mediaPopup.startFourth, end: mediaPopup.endFourth, units: 'px'}}).play();
        mediaPopup.currentSlideFourth--;
      }
    },

    //tab5 all functions ends here ------------------

    stopAutoplay: function () {
      var mediaPopup = this;
      clearInterval(mediaPopup.autoplaytimer);
      mediaPopup.autoplaytimer = null;

    },

    startAutoplay: function () {
    },

    autoplay: function () {
      var mediaPopup = this;
      if (mediaPopup.pause) {
        return;
      }
      mediaPopup.createNextFade();
    },

    onOpen: function () {
      var mediaPopup = this;
      window.scrollTo(0, 0);
      dojo.query('#first', mediaPopup.popupDomNode)[0].style.display = 'block';
      dojo.query('#second', mediaPopup.popupDomNode)[0].style.display = 'none';
      dojo.query('#third', mediaPopup.popupDomNode)[0].style.display = 'none';
      dojo.query('#fourth', mediaPopup.popupDomNode)[0].style.display = 'none';
      dojo.query('#fifth', mediaPopup.popupDomNode)[0].style.display = 'none';
      dojo.publish("tui/widget/media/HeroCarousel/onTemplateLoaded", [true]);
    },

    onClose: function () {
      var mediaPopup = this;
      mediaPopup.stopAutoplay();
      dojo.publish("tui/widget/media/HeroCarousel/onTemplateLoaded", [false]);
    },

    tabsClass: function (current) {

      dojo.removeClass("firsttab", "activetab");
      dojo.removeClass("secondtab", "activetab");
      dojo.removeClass("thirdtab", "activetab");
      dojo.removeClass("fourthtab", "activetab");
      dojo.removeClass("fifthtab", "activetab");

      dojo.addClass(current, "activetab");
    }

  })

  return tui.widget.media.DreamlinerPopup;
});




