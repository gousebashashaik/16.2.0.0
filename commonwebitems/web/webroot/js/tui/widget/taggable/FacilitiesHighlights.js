define('tui/widget/taggable/FacilitiesHighlights', [
  'dojo',
  'tui/widget/mixins/Taggable'
], function(dojo, taggable) {

  dojo.declare('tui.widget.taggable.FacilitiesHighlights', [tui.widget._TuiBaseWidget], {

    // ----------------------------------------------------------------------------- methods

    postCreate: function() {
      var facilitiesHighlights = this;

      facilitiesHighlights.inherited(arguments);

      // Carousel form.
      facilitiesHighlights.tagElements(dojo.query('div.panel', facilitiesHighlights.domNode), function(DOMElement) {
        var headerElement = dojo.query('h3', DOMElement)[0];
        return headerElement && (headerElement.textContent || headerElement.innerText);
      });

      // View all link.
      facilitiesHighlights.tagElements(dojo.query('a.view-all', facilitiesHighlights.domNode), 'View all');
    }
  });

  return tui.widget.taggable.FacilitiesHighlights;
});
