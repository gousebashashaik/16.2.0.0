define('tui/widget/taggable/PriceAndAvailability', [
  'dojo',
  'tui/widget/mixins/Taggable'
], function(dojo, taggable) {

  dojo.declare('tui.widget.taggable.PriceAndAvailability', [tui.widget._TuiBaseWidget], {

    // ----------------------------------------------------------------------------- methods

    postCreate: function() {
      var priceAndAvailability = this;

      priceAndAvailability.inherited(arguments);
    }
  });

  return tui.widget.taggable.PriceAndAvailability;
});
