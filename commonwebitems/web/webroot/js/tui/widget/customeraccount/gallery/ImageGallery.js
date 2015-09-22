define('tui/widget/customeraccount/gallery/ImageGallery', [
  'dojo',
  'dojo/text!tui/widget/customeraccount/gallery/templates/default.html',
  'dojo/dom-style',
  'dojo/query',
  'tui/common/Util',
  'tui/widget/carousel/SlidingContainer',
  'dojo/dom-attr',
  'dojo/dom-class',
  'dojo/_base/event',
  'tui/widget/mixins/Swipable',
  'dojox/dtl',
  'dojox/dtl/Context',
  'dojox/dtl/tag/logic',
  'dijit/_Widget'], function(dojo, tmpl, domStyle, query, util, slidingContainer, domAttr, domClass, event) {


  dojo.declare('tui.widget.customeraccount.gallery.ImageGallery', [tui.widget.mixins.Swipable], {

    imageWidth: null,

    swipeThreshold: -800,

    slideShowInterval: null,

    data: {images: [
      {url: '/holiday/mobile/images/photos/ttd_1.jpg', name: '1 of 5 - Displaying image 1'},
      {url: '/holiday/mobile/images/photos/ttd_2.jpg', name: '2 of 5 - Displaying image 2'},
      {url: '/holiday/mobile/images/photos/ttd_3.jpg', name: '3 of 5 - displaying image 3'},
      {url: '/holiday/mobile/images/photos/ttd_4.jpg', name: '4 of 5 - image 4 '},
      {url: '/holiday/mobile/images/photos/ttd_5.jpg', name: '5 of 5 - image 5'}

    ]},


    getViewPort: function() {
      return this.domNode;
    },

    getViewPortContent: function() {
      var widget = this;
      var viewPortContent = _.first(query('ul', widget.getViewPort()));
      return viewPortContent;
    },

    resize: function() {
      var widget = this;
      var images = query('li > img', widget.getViewPortContent());
      var noOfImages = images.length;
      var viewPortWidth = noOfImages * document.documentElement.clientWidth;
      domStyle.set(widget.getViewPortContent(), 'width', viewPortWidth + 'px');

      query('ul > li > img', widget.domNode).forEach(function(image) {
        var width = document.documentElement.clientWidth;
        var height = document.documentElement.clientHeight;

        domAttr.set(image, 'width', width);
        if (width > height) {
          domAttr.set(image, 'height', height);
        } else {
          domAttr.set(image, 'height', width); //portrait
        }
      });
    },

    numberOfImages: function() {
      var widget = this;
      return query('li > img', widget.getViewPortContent()).length;
    },

    widthOfEachImage: function() {
      var widget = this;
      var images = query('li > img', widget.getViewPortContent());
      return _.first(images).width;
    },

    initSwipable: function() {
      var widget = this;
      widget.slidingContainer = slidingContainer.make(widget.widthOfEachImage(), widget.numberOfImages(),
          widget.getViewPortContent(), widget.windowSlidedListener.bind(widget));
      widget.numberOfPages = widget.numberOfImages();
    },


    showControls: function() {
      var widget = this;
      var imageTitle = query('.image-title', widget.domNode)[0];
      var controls = query('.controls', widget.domNode)[0];
      domStyle.set(imageTitle, 'display', 'block');
      domStyle.set(controls, 'display', 'block');

      window.setTimeout(function() {
        widget.hideControls();
      }, 9000);
    },

    hideControls: function() {
      var widget = this;
      var imageTitle = query('.image-title', widget.domNode)[0];
      var controls = query('.controls', widget.domNode)[0];

      domStyle.set(imageTitle, 'display', 'none');
      domStyle.set(controls, 'display', 'none');

    },

    toggleControls: function() {
      var widget = this;
      var controls = query('.controls', widget.domNode)[0];
      if (domStyle.get(controls, 'display') == 'block') {
        widget.hideControls();
      } else {
        widget.showControls();
      }
    },

    tapped: function() {
      var widget = this;
      widget.toggleControls();
    },

    swiping: function(position) {
      var widget = this;
      widget.slidingContainer.swipe(position);
    },

    swiped: function(direction) {
      var widget = this;
      widget.slidingContainer.slide(direction);
    },

    togglePlayButton: function() {
      var widget = this;
      var playButton = _.first(query('.play-button', widget.domNode));
      domClass.toggle(playButton, 'start');
      domClass.toggle(playButton, 'stop');
    },

    playSlideShow: function() {
      var widget = this;
      widget.togglePlayButton();
      widget.slideShowInterval = window.setInterval(function() {
        widget.swipeLeft();
      }, 2000);
    },

    stopSlideShow: function() {
      var widget = this;
      widget.togglePlayButton();
      window.clearInterval(widget.slideShowInterval);
    },

    isSlideShowInProgress: function() {
      var widget = this;
      var playButton = _.first(query('.play-button', widget.domNode));
      return domClass.contains(playButton, 'stop');
    },

    toggleSlideShow: function() {
      var widget = this;
      if (!widget.isSlideShowInProgress()) {
        widget.playSlideShow();
      } else {
        widget.stopSlideShow();
      }
    },

    swipeLeft: function() {
      var widget = this;
      widget.swiping(widget.swipeThreshold);
      widget.swiped(widget.swipeThreshold);
    },

    swipeRight: function() {
      var widget = this;
      widget.swiping(-1 * widget.swipeThreshold);
      widget.swiped(-1 * widget.swipeThreshold);
    },

    postCreate: function() {
      var widget = this;
      var template = new dojox.dtl.Template(tmpl);
      var context = new dojox.dtl.Context(widget.data);
      var html = template.render(context);
      widget.getViewPort().innerHTML = html;
      widget.resize(); // initial sizing - dynamic based on the view port
      dojo.connect(window, 'onresize', function(e) {
        widget.resize();
        widget.initSwipable();
      });
      dojo.connect(_.first(query('.close', widget.domNode)), 'onclick', function(e) {
        domStyle.set(dojo.byId('gallery'), 'display', 'none');
        event.stop(e);
      });
      dojo.connect(_.first(query('.play-button', widget.domNode)), 'onclick', function(e) {
        widget.toggleSlideShow();
      });
      dojo.connect(_.first(query('.prev', widget.domNode)), 'onclick', function(e) {
        widget.swipeRight();
      });
      dojo.connect(_.first(query('.next', widget.domNode)), 'onclick', function(e) {
        widget.swipeLeft();
      });
      widget.inherited(arguments);
    }

  });
});


