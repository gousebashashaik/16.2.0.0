define('tui/widget/taggable/BackToSearch', [
  'dojo',
  'tui/widget/mixins/Taggable',
	'tui/widget/_TuiBaseWidget'
], function(dojo, taggable) {

  dojo.declare('tui.widget.taggable.BackToSearch', [tui.widget._TuiBaseWidget], {

    // ----------------------------------------------------------------------------- methods

    postCreate: function() {
      var backToSearch = this;

      backToSearch.inherited(arguments);
      
      backToSearch.tagElement(backToSearch.domNode, "backToSearchResults");

    }
  });

  return tui.widget.taggable.BackToSearch;
});
