define('tui/widget/customeraccount/gallery/GalleryPopup', [
  'dojo',
  'dojo/dom-style',
  'dojo/query',
  'dojo/_base/event',
  'dijit/_Widget',
  'tui/thirdparty/klass.min',
  'tui/thirdparty/code.photoswipe-3.0.5.min'
], function(dojo, domStyle, query, event) {

  dojo.declare('tui.widget.customeraccount.gallery.GalleryPopup', [dijit._Widget], {
    postCreate: function() {
      var widget = this;

      dojo.connect(widget.domNode, 'onclick', function(e) {
        window.scrollTo(0, 100);
        var options = {
          loop: false
        };
        var instance = window.Code.PhotoSwipe.attach(window.document.querySelectorAll('#gallery-container a'), options);
        instance.show(0);

        //domStyle.set(dojo.byId('gallery'), 'display', 'block');
        event.stop(e);
      });

      widget.inherited(arguments);     
    }
  });
});
