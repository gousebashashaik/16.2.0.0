define('tui/widget/taggable/TopPlaces', [
  'dojo',
  'tui/widget/mixins/Taggable',
	'tui/widget/_TuiBaseWidget'
], function(dojo, taggable) {

  dojo.declare('tui.widget.taggable.TopPlaces', [tui.widget._TuiBaseWidget], {

    // ----------------------------------------------------------------------------- methods

    postCreate: function() {
      var topPlaces = this;

      topPlaces.inherited(arguments);

      topPlaces.tagElements(dojo.query('h3 a', topPlaces.domNode), function(DOMElement) {
        return DOMElement.textContent || DOMElement.innerText;
      });

      topPlaces.tagElements(dojo.query('.image-container a', topPlaces.domNode), function(DOMElement) {
        var imgElement = DOMElement.children[0];
        return imgElement && imgElement.alt;
      });
    }
  });

  return tui.widget.taggable.TopPlaces;
});
