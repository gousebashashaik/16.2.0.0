define('tui/common/DomUtil', ['dojo',
  'dojo/query',
  'dojo/dom-style'
],
function(dojo, query, domStyle) {

  return {
    isVisible: function(element) {
      return domStyle.get(element, 'display') === 'block';
    },

    onWindowResize: function(callback) {
      dojo.connect(window, 'onresize', _.throttle(callback, 1000));
    }
  };
}
);
