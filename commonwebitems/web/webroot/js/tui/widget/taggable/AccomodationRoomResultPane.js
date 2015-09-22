define('tui/widget/taggable/AccomodationRoomResultPane', [
  'dojo',
  'tui/widget/mixins/Taggable'
], function(dojo, taggable) {

  dojo.declare('tui.widget.taggable.AccomodationRoomResultPane', [tui.widget._TuiBaseWidget], {

    // ----------------------------------------------------------------------------- methods

    postCreate: function() {
      var accomodationRoomResultPane = this;

      accomodationRoomResultPane.inherited(arguments);

      // Carousel form.
      accomodationRoomResultPane.tagElements(dojo.query('div.panel', accomodationRoomResultPane.domNode), function(DOMElement) {
        var headerElement = dojo.query('h3', DOMElement)[0];
        return headerElement && (headerElement.textContent || headerElement.innerText);
      });
    }
  });

  return tui.widget.taggable.AccomodationRoomResultPane;
});
