define('tui/widget/taggable/AccomodationRoomHighlights', [
  'dojo',
  'tui/widget/mixins/Taggable'
], function(dojo, taggable) {

  dojo.declare('tui.widget.taggable.AccomodationRoomHighlights', [tui.widget._TuiBaseWidget], {

    // ----------------------------------------------------------------------------- methods

    postCreate: function() {
      var accomodationRoomHighlights = this;

      accomodationRoomHighlights.inherited(arguments);

      // Carousel form.
      accomodationRoomHighlights.tagElements(dojo.query('div.panel', accomodationRoomHighlights.domNode), function(DOMElement) {
        var headerElement = dojo.query('h3', DOMElement)[0];
        return headerElement && (headerElement.textContent || headerElement.innerText);
      });
    }
  });

  return tui.widget.taggable.AccomodationRoomHighlights;
});
