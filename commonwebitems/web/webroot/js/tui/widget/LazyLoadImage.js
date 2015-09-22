define('tui/widget/LazyLoadImage', [
  'require',
  'dojo/_base/lang',
  'dojo/_base/kernel',
  'dojo/dom-attr',
  'dojo/dom-class',
  'dojo/dom-construct',
  'dojo/dom-geometry',
  'dojo/on',
  'dojo/window',
  'tui/widget/_TuiBaseWidget'
], function(require, lang, kernel, domAttr, domClass, domConstruct, domGeom, on, win) {

  dojo.declare('tui.widget.LazyLoadImage', [tui.widget._TuiBaseWidget], {
    /**
     * @fileOverview
     * Adapted from http://marcellbranco.wordpress.com/2012/10/24/dojo-lazy-loading-image-component/
     *
     * The algorithm used to detect the visibility of an image is super inaccurate:
     *  * It only takes into consideration the upper left corner of images
     *  * It doesn't care of the image dimensions
     *  * It doesn't consider horizontal location of images (i.e. outside the viewport on the right)
     *  * It doesn't work perfectly on images hidden or hidden by an ancestor
     * But it globally works fine because it assumes scrolling is only vertical and
     * from top to bottom.
     *
     * @example <img src='path/to/blank.gif' data-dojo-type='tui.widget.LazyLoadImage' data-dojo-props='source:"IMAGE_SRC"' .... />
     */

    // ----------------------------------------------------------------------------- properties

    /**
     * @param {string} source of image that is to be loaded
     */
    source: '',

    /**
     * @private {string} The 1px transparent GIF file that prevents the browser from loading the image
     */
    _blankGif: require.toUrl('/images/b.gif'),

    /**
     * @private {number}  Any images located from this pixel distance out of the fold will be loaded.
     */
    _sensitivity: 200,

    /**
     * @private
     */
    _tempImgLoadListener: null,

    /**
     * @param {object} element the nearest visible DOM ancestor
     */
    visibleAncestorDomNode: null,

    // ----------------------------------------------------------------------------- methods

    /**
     * Class constructor
     * @constructor
     */
    construct: function() {
      var lazyLoadImage = this;

      // Make sure we're only using <img alt="" /> tags with this component
      if (!(lazyLoadImage.domNode instanceof HTMLImageElement)) {
        console.log('[tui.widget.LazyLoadImage] ' +
          'This component can only be used with <img alt="" /> tags');
        return;
      }

      lazyLoadImage.inherited(arguments);
    },

    // Implementation starts on postCreate
    postCreate: function() {
      var lazyLoadImage = this;

      // Stops if there's no URL to lazy load
      if (typeof lazyLoadImage.source !== 'string' || lazyLoadImage.source === '') {
        console.log('[tui.widget.LazyLoadImage] ' +
          'No URL defined for lazy loading on ' + lazyLoadImage.get('id'));
        return;
      }

      // Reads the current src attribute
      var currentSource = domAttr.get(lazyLoadImage.domNode, 'src');

      // Sets the initial image src to blank.gif if the node doesn't already have one
      if (typeof currentSource !== 'string' || currentSource === '') {
        domAttr.set(lazyLoadImage.domNode, 'src', lazyLoadImage._blankGif);
      }

      // Stops if the current src attribute is the same of lazy load URL
      if (currentSource === lazyLoadImage.source) {
        console.log('[tui.widget.LazyLoadImage] ' +
          'The URL defined for lazy loading is already the src attribute on ' +
          lazyLoadImage.get('id'));
        return;
      }

      // If the image is already in viewport, load it
      if (lazyLoadImage._isInViewport()) {
        // No transitions for image visible on viewport on load.
        lazyLoadImage.loadImage();
        return;
      }

      // Event listeners for loading the image when the node enters the viewport.
      var windowEventListeners;
      var windowEventHandler = _.throttle(function() {
        lazyLoadImage.windowEventHandler(windowEventListeners);
      }, 300);
      windowEventListeners = on(kernel.global, 'scroll,resize', windowEventHandler);
    },

    // Load image and put it on the screen
    loadImage: function() {
      var lazyLoadImage = this;

      // Checks if the image is not loading and it isn't already loaded
      if (!domClass.contains(lazyLoadImage.domNode, 'loading') &&
        domAttr.get(lazyLoadImage.domNode, 'src') !== lazyLoadImage.source) {

        // Adding css class allowing loading decoration
        domClass.add(lazyLoadImage.domNode, 'loading');

        // Wait until the source image has been loaded on tempImg
        lazyLoadImage._tempImgLoadListener = on.once(lazyLoadImage.domNode, 'load', function() {
          lazyLoadImage.tempImgLoadHandler();
        });

        // Start loading
        domAttr.set(lazyLoadImage.domNode, 'src', lazyLoadImage.source);
      }
    },

    // Checks if the node is inside the viewport
    _isInViewport: function() {
      var lazyLoadImage = this;

      var imgPos = domGeom.position(lazyLoadImage.domNode);
      var viewport = win.getBox();

      // Prevent loading hidden elements that have xy = (0, 0).
      if (imgPos.x === 0 && imgPos.y === 0) {
        // See if the nearest visible ancestor has been computed yet.
        if (lazyLoadImage.visibleAncestorDomNode) {
          imgPos = domGeom.position(lazyLoadImage.visibleAncestorDomNode);
        } else {
          // Find the nearest visible ancestor.
          lazyLoadImage.visibleAncestorDomNode = lazyLoadImage.domNode;

          while (lazyLoadImage.visibleAncestorDomNode && imgPos.x === 0 && imgPos.y === 0) {
            lazyLoadImage.visibleAncestorDomNode = lazyLoadImage.visibleAncestorDomNode.parentNode;
            imgPos = domGeom.position(lazyLoadImage.visibleAncestorDomNode);
          }
        }
      }

      return imgPos.y - viewport.h < lazyLoadImage._sensitivity;
    },

    // Window event handler
    windowEventHandler: function(windowEventListeners) {
      var lazyLoadImage = this;

      if (lazyLoadImage._isInViewport()) {
        lazyLoadImage.loadImage();
        windowEventListeners.remove();
      }
    },

    // Called when the image is loaded.
    tempImgLoadHandler: function() {
      var lazyLoadImage = this;

      // Removing loading css class and fades in the <img /> node.
      dojo.replaceClass(lazyLoadImage.domNode, 'loaded', 'loading');
	 // dojo.style(lazyLoadImage.domNode, 'position','relative');
    }
  });

  return tui.widget.LazyLoadImage;
});
