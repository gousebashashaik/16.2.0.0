define('tui/widget/taggable/PlacesToStay', [
  'dojo',
  'tui/widget/mixins/Taggable',
	'tui/widget/_TuiBaseWidget'
], function(dojo, taggable) {

  dojo.declare('tui.widget.taggable.PlacesToStay', [tui.widget._TuiBaseWidget], {

    // ----------------------------------------------------------------------------- methods

    postCreate: function() {
      var placesToStay = this;

      placesToStay.inherited(arguments);

      // Carousel form.
      placesToStay.tagElements(dojo.query('div.panel', placesToStay.domNode), function(DOMElement) {
        var headerElement = dojo.query('h3', DOMElement)[0];
        return headerElement && (headerElement.textContent || headerElement.innerText);
      });

      // Highlights form.
      placesToStay.tagElements(dojo.query('div.places-to-stay', placesToStay.domNode), function(DOMElement) {
        var headerElement = dojo.query('h3', DOMElement)[0];
        return headerElement && (headerElement.textContent || headerElement.innerText);
      });
    }
  });

  return tui.widget.taggable.PlacesToStay;
});
