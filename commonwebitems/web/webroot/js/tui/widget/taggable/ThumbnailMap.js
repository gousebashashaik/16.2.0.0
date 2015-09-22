define('tui/widget/taggable/ThumbnailMap', [
  'dojo',
  'tui/widget/mixins/Taggable'
], function(dojo, taggable) {

  dojo.declare('tui.widget.taggable.ThumbnailMap', [tui.widget._TuiBaseWidget], {

    // ----------------------------------------------------------------------------- methods

    postCreate: function() {
      var thumbnailMap = this;
      thumbnailMap.attachTag();
      thumbnailMap.tagElements(dojo.query('a', thumbnailMap.domNode), "seePlacesToGo"); 
      thumbnailMap.inherited(arguments);
    }
  });

  return tui.widget.taggable.ThumbnailMap;
});
