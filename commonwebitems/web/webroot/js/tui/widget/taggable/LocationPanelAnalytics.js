define('tui/widget/taggable/LocationPanelAnalytics', [
  'dojo',
  'tui/widget/mixins/Taggable'
], function(dojo, taggable) {

  dojo.declare('tui.widget.taggable.LocationPanelAnalytics', [tui.widget._TuiBaseWidget], {

    // ----------------------------------------------------------------------------- methods

    postCreate: function() {
      var locationPanel = this;
      locationPanel.attachTag();
      locationPanel.tagElements(dojo.query('p > a', locationPanel.domNode), "locationSeeMore");  
      locationPanel.inherited(arguments);
    }
  });

  return tui.widget.taggable.LocationPanelAnalytics;
});
