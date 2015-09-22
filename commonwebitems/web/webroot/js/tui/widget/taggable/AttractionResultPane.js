define('tui/widget/taggable/AttractionResultPane', [
  'dojo',
  'tui/widget/mixins/Taggable',
	'tui/widget/_TuiBaseWidget'
], function(dojo, taggable) {

  dojo.declare('tui.widget.taggable.AttractionResultPane', [tui.widget._TuiBaseWidget], {

    // ----------------------------------------------------------------------------- methods

    postCreate: function() {
      var attractionResultPane = this;

      attractionResultPane.inherited(arguments);

      // First, we collect all product div elements.
      var attractions = dojo.query('div.product', attractionResultPane.domNode);

      // Then we iterate through these divs.
      _.forEach(attractions, function(attractionElement) {
        // The text is the 2nd anchor textual content.
        var anchorElements = dojo.query('a', attractionElement);
        var headerElement = anchorElements[1];
        var text = headerElement && (headerElement.textContent || headerElement.innerText);

        // Then we tag all anchor tags with the text defined previously.
        attractionResultPane.tagElements(anchorElements, text);
      });
    }
  });

  return tui.widget.taggable.AttractionResultPane;
});
