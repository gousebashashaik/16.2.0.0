define('tui/common/animation/Browser', [], function() {

  return {
    is3dSupported: function() {
      return document.body.style.perspective !== null && document.body.style.perspective !== undefined
        && document.body.style.WebkitPerspective !== null && document.body.style.WebkitPerspective !== undefined;
    },

    isTransformSupported: function() {
      return !_.isNull(document.body.style.WebkitTransform) || !_.isNull(document.body.style.MozTransform) || !_.isNull(document.body.style.transformProperty);
    }
  };
});
