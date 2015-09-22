define('tui/widget/taggable/GSASearch', [
  'dojo',
  'dojo/query',
  'tui/widget/mixins/Taggable',
  'tui/widget/_TuiBaseWidget'
], function(dojo, query, taggable) {

  dojo.declare('tui.widget.taggable.GSASearch', [tui.widget._TuiBaseWidget], {

    // ----------------------------------------------------------------------------- methods

    postCreate: function() {
      var gsaSearch = this;
      gsaSearch.inherited(arguments);
      var input = query('.textfield', gsaSearch.domNode)[0],
          button = query('.search-btn', gsaSearch.domNode)[0];

      gsaSearch.tagElement(input, 'GSA Search Box');
      gsaSearch.tagElement(button, 'GSA Search Button');
    }
  });

  return tui.widget.taggable.GSASearch;
});
