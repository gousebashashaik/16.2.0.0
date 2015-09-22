define('tui/widget/taggable/INVProductPromoComponent', [
  'dojo',
  'dojo/query',
  'tui/widget/mixins/Taggable',
  'tui/widget/_TuiBaseWidget'
], function(dojo, query, taggable) {

  dojo.declare('tui.widget.taggable.INVProductPromoComponent', [tui.widget._TuiBaseWidget], {

    // ----------------------------------------------------------------------------- methods

    postCreate: function() {
      var invPPComp = this;
      invPPComp.inherited(arguments);
      var input = query('.url', invPPComp.domNode)[0];
      invPPComp.tagElement(input, 'Read More');
    }
  });

  return tui.widget.taggable.INVProductPromoComponent;
});
