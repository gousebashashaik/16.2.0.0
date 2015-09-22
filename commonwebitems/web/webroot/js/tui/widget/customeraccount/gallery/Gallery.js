define('tui/widget/customeraccount/gallery/Gallery', [
  'dojo',
  'dojo/text!tui/widget/customeraccount/gallery/templates/accommodation_gallery.html',
  "dojo/json",
  'tui/common/TemplateUtil',
  'dojo/dom-construct',
  'dojo/dom-style',
  'dojo/query',
  'dojo/_base/event',
  'dijit/_Widget',
  'tui/thirdparty/klass.min',
  'tui/thirdparty/jquery-1.8.3.min',
  'tui/thirdparty/code.photoswipe-3.0.5'
], function (dojo, galleryTemplate, JSON, templateUtil, domConstruct, domStyle, query, event) {

  var sensitivity = 100;

  dojo.declare('tui.widget.customeraccount.gallery.Gallery', [dijit._Widget], {

    albumId: null,

    images: null,

    galleryId: function () {
      return 'gallery-' + this.albumId;
    },

    galleryContainer: function () {
      return dojo.byId('gallery');
    },

    options: function () {
      return {
        loop: false
      };
    },

    template : function () {
      return galleryTemplate;
    },

    imageSelector : function () {
      return window.document.querySelectorAll('#' + this.galleryId() + ' a');
    },

    buildGallery: _.memoize(function (galleryId, images) {
      var widget = this;
      var html = templateUtil.render(widget.template(), {'id': widget.galleryId(), 'images': images});
      //domConstruct.place(html, widget.galleryContainer(), 'last');
	  dojo.place(html, widget.galleryContainer(), "last");
      //dojo.parser.parse(dojo.byId(galleryId));
      return window.Code.PhotoSwipe.attach(widget.imageSelector(), widget.options());
    }),

    postCreate: function () {
      var widget = this;    
      if (typeof widget.images === 'string') {
        widget.images = JSON.parse(widget.images, true);
      }

      var instance = widget.buildGallery(widget.galleryId(), widget.images);
      dojo.connect(widget.domNode, 'onclick', function (e) {
        event.stop(e);
        widget.xPos = e.pageX;
        widget.yPos = e.pageY;
        instance.show(0);
      });

      instance.addEventHandler(window.Code.PhotoSwipe.EventTypes.onHide, function (e) {
        window.scrollTo(widget.xPos, widget.yPos - sensitivity);
      });

      widget.inherited(arguments);
    }
  });
});
