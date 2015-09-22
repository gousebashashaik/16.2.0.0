// # DetectFeature
// ## Utility module
//
// Allows to detect if a particular feature is supported by a browser
// and optionally returns the property name relevant to said browser
// for example to test if a browser supports CSS3 Transitions: `isSupported('transition')`
//
// @author Laurent Mignot

define('tui/utils/DetectFeature', [], function () {

  /**
   * Store reference to browser feature prefixes which
   * will be prepended to feature
   * @type {Array}
   * @private
   */
  var prefixes = ['', 'Webkit', 'Moz', 'ms', 'O'];

  /**
   * Store reference to browser transitionend event names
   * @type {{}}
   * @private
   */
  var transitions = {
        'transition': 'transitionend',
        'WebkitTransition': 'webkitTransitionEnd',
        'MozTransition': 'transitionend',
        'OTransition': 'oTransitionEnd otransitionend'
      };

  /**
   * Retrieves name of browser's transitionend event
   * Allows to bind to valid event rather than a ridiculously long string of possible event names
   * @returns {String|Undefined}
   */
  function getTransitionEndEvent () {
    var ret;
    _.some(transitions, function(evtName, trans){
      ret = evtName;
      return (typeof document.documentElement.style[trans] !== 'undefined');
    });
    return ret;
  }

  /**
   * ###isSupported()
   * Internal method which loops over prefixes and determines if supplied property is available.
   * For example isSupported('transition') in Chrome/FF/IE >8 would return true
   * @param prop
   * @returns {Boolean}
   * @private
   */
  function isSupported (prop) {
    var ret, propName;
    // use some to break loop as soon as positive response is returned
    ret = _.some(prefixes, function (prefix) {
      propName = prefix + (prefix === '' ? prop : _.capitalize(prop));
      return (typeof document.documentElement.style[propName] !== 'undefined');
    });
    return ret;
  }

  return {

    /**
     * ###firstSupportedPropName
     * Loops over prefixes and returns valid prefixed version of supplied
     * property (or unprefixed if the case)
     * @param {String} prop The feature being detected eg 'transition'
     * @returns {String|null}
     */
    firstSupportedPropName: function (prop) {
      var ret, propName;
      ret = _.some(prefixes, function (prefix) {
        propName = prefix + (prefix === '' ? prop : _.capitalize(prop));
        return (typeof document.documentElement.style[propName] !== 'undefined')
            ? propName
            : null;
      });
      return ret ? propName : null;
    },

    /**
     * ###is3dSupported()
     * Helper method for commonly requested properties, in this case browser 3D support
     * @returns {Boolean}
     */
    is3dSupported: function () {
      return isSupported('perspective');
    },

    /**
     * ###isTransformSupported()
     * Test if CSS3 Transforms are supported and returns available property name if true
     * @returns {String|null}
     */
    isTransformSupported: function () {
      var self = this;
      return isSupported('transform') ? self.firstSupportedPropName('transform') : null;
    },

    /**
     * ###isTransitionSupported()
     * Test if CSS3 Transitions are supported and returns available property name if true
     * @returns {String|null|*}
     */
    isTransitionSupported: function () {
      var self = this;
      return isSupported('transition') ? self.firstSupportedPropName('transition') : null;
    },

    transitionEndEvent: function () {
      return getTransitionEndEvent();
    },

    isSupported: function(prop) {
      var self = this;
      return isSupported(prop) ? self.firstSupportedPropName(prop) : null;
    }
  };
});