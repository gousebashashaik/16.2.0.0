define('tui/widget/taggable/AccommodationDisclaimer', [
  'dojo',
  'tui/widget/mixins/Taggable'
], function(dojo, taggable) {

  dojo.declare('tui.widget.taggable.AccommodationDisclaimer', [tui.widget._TuiBaseWidget], {

    // ----------------------------------------------------------------------------- methods

    postCreate: function() {
      var accommodationDisclaimer = this;

      accommodationDisclaimer.inherited(arguments);
    }
  });

  return tui.widget.taggable.AccommodationDisclaimer;
});
