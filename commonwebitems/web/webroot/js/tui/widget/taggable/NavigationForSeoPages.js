define('tui/widget/taggable/NavigationForSeoPages', [
  'dojo',
  'tui/widget/mixins/Taggable'
], function(dojo, taggable) {

  dojo.declare('tui.widget.taggable.NavigationForSeoPages', [tui.widget._TuiBaseWidget], {

    // ----------------------------------------------------------------------------- methods

    postCreate: function() {
      var navigationForSeoPages = this;

      navigationForSeoPages.inherited(arguments);
    }
  });

  return tui.widget.taggable.NavigationForSeoPages;
});
