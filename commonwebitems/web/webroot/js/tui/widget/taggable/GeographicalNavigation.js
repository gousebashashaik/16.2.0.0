define('tui/widget/taggable/GeographicalNavigation', [
  'dojo',
  'tui/widget/mixins/Taggable',
	'tui/widget/_TuiBaseWidget'
], function(dojo, taggable) {

  dojo.declare('tui.widget.taggable.GeographicalNavigation', [tui.widget._TuiBaseWidget], {

    // ----------------------------------------------------------------------------- methods

    postCreate: function() {
      var geographicalNavigation = this;

      geographicalNavigation.inherited(arguments);

      var headerElement = dojo.query('h2', geographicalNavigation.domNode)[0];
      var text = headerElement && (headerElement.textContent || headerElement.innerText);

      geographicalNavigation.tagElements(dojo.query('a', geographicalNavigation.domNode), text);
    }
  });

  return tui.widget.taggable.GeographicalNavigation;
});
