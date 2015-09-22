define('tui/widget/mobile/ResponsiveImage', [
  'require',
  'dojo/_base/lang',
  'dojo/_base/kernel',
  'dojo/dom-attr',
  'dojo/dom-class',
  'dojo/dom-construct',
  'dojo/dom-geometry',
  'dojo/on',
  'dojo/window'
], function(require, lang, kernel, domAttr, domClass, domConstruct, domGeom, on, win) {

  dojo.declare('tui.widget.mobile.ResponsiveImage', [dijit._Widget], {
    /**
     * Adapted from http://marcellbranco.wordpress.com/2012/10/24/dojo-lazy-loading-image-component/
     *
     * The algorithm used to detect the visibility of an image is super inaccurate:
     *  * It only takes into consideration the upper left corner of images
     *  * It doesn't care of the image dimensions
     *  * It doesn't consider horizontal location of images (i.e. outside the viewport on the right)
     *  * It doesn't work perfectly on images hidden or hidden by an ancestor
     * But it globally works fine because it assumes scrolling is only vertical and
     * from top to bottom.
     */

    // ----------------------------------------------------------------------------- properties

    // The image sources available for the lazy loading
    s: '',
    m: '',
    l: '',

    // The 1px transparent GIF file that prevents the browser from loading the image
    _blankGif: require.toUrl('/destinations/mobile/images/p.gif'),

    // Any images located from this pixel distance out of the fold will be loaded.
    _sensitivity: 200,

    _tempImgLoadListener: null,

    visibleAncestorDomNode: null,

    // ----------------------------------------------------------------------------- methods

    // Class constructor
    construct: function() {
      var responsiveImage = this;

      // Make sure we're only using <img alt="" /> tags with this component
      if (!(responsiveImage.domNode instanceof HTMLImageElement)) {
        console.log('[tui.widget.mobile.ResponsiveImage] ' +
          'This component can only be used with <img alt="" /> tags');
        return;
      }

      responsiveImage.inherited(arguments);
    },

    // Implementation starts on postCreate
    postCreate: function() {
      var responsiveImage = this;

      // Stops if there's no URL to lazy load
      if (typeof responsiveImage.s !== 'string' || responsiveImage.s === '' ||
        typeof responsiveImage.m !== 'string' || responsiveImage.m === '' ||
        typeof responsiveImage.l !== 'string' || responsiveImage.l === '') {
        console.log('[tui.widget.mobile.ResponsiveImage] ' +
          'Missing image source for lazy loading on ' + responsiveImage.get('id'));
        return;
      }

      // Reads the current src attribute
      var currentSource = domAttr.get(responsiveImage.domNode, 'src');

      // Sets the initial image src to blank.gif if the node doesn't already have one
      if (typeof currentSource !== 'string' || currentSource === '') {
        domAttr.set(responsiveImage.domNode, 'src', responsiveImage._blankGif);
      }

      // Stops if the current src attribute is the same of lazy load URL
      if (currentSource === responsiveImage.s ||
        currentSource === responsiveImage.m ||
        currentSource === responsiveImage.l) {
        console.log('[tui.widget.mobile.ResponsiveImage] ' +
          'The image source for lazy loading is already the src attribute on ' +
          responsiveImage.get('id'));
        return;
      }

      // If the image is already in viewport, load it.
      if (true) {
        //if (responsiveImage._isInViewport()) {
        // No transitions for image visible on viewport on load.
        responsiveImage.loadImage();
        return;
      }

      // Event listeners for loading the image when the node enters the viewport.
      var windowEventListeners;
      var windowEventHandler = _.throttle(function() {
        responsiveImage.windowEventHandler(windowEventListeners);
      }, 300);
      windowEventListeners = on(kernel.global, 'scroll,resize', windowEventHandler);
    },

    // Load image and put it on the screen.
    loadImage: function() {
      var responsiveImage = this;

      // Checks if the image is not loading and it isn't already loaded
      if (!domClass.contains(responsiveImage.domNode, 'loading') &&
        domAttr.get(responsiveImage.domNode, 'src') !== responsiveImage.s &&
        domAttr.get(responsiveImage.domNode, 'src') !== responsiveImage.m &&
        domAttr.get(responsiveImage.domNode, 'src') !== responsiveImage.l) {

        // Adding css class allowing loading decoration
        domClass.add(responsiveImage.domNode, 'loading');

        // Wait until the source image has been loaded on tempImg
        responsiveImage._tempImgLoadListener = on.once(responsiveImage.domNode, 'load', function() {
          responsiveImage.tempImgLoadHandler();
        });

        // Start loading
        var width = responsiveImage.domNode.width;

        // Where the magic happens.
        if (width > 1) {
          if (width <= 232) {
            domAttr.set(responsiveImage.domNode, 'src', responsiveImage.s);
          } else if (width <= 488) {
            domAttr.set(responsiveImage.domNode, 'src', responsiveImage.m);
          } else {
            domAttr.set(responsiveImage.domNode, 'src', responsiveImage.l);
          }
        }
      }
    },

    // Checks if the node is inside the viewport
    _isInViewport: function() {
      var responsiveImage = this;

      var imgPos = domGeom.position(responsiveImage.domNode);
      var viewport = win.getBox();

      return imgPos.y - viewport.h < responsiveImage._sensitivity;
    },

    // Window event handler
    windowEventHandler: function(windowEventListeners) {
      var responsiveImage = this;

      if (responsiveImage._isInViewport()) {
        responsiveImage.loadImage();
        windowEventListeners.remove();
      }
    },

    // Called when the image is loaded.
    tempImgLoadHandler: function() {
      var responsiveImage = this;

      // Removing loading css class and fades in the <img /> node.
      dojo.replaceClass(responsiveImage.domNode, 'loaded', 'loading');
    }
  });

  return tui.widget.mobile.ResponsiveImage;
});
