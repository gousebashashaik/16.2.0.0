define('tui/widget/taggable/Navigation', [
  'dojo',
  'tui/widget/mixins/Taggable'
], function(dojo, taggable) {

  dojo.declare('tui.widget.taggable.Navigation', [tui.widget._TuiBaseWidget], {

    // ----------------------------------------------------------------------------- methods

    postCreate: function() {
      var navigation = this;

      navigation.inherited(arguments);

      navigation.tagElements(dojo.query('a', navigation.domNode), function(DOMElement) {
        return DOMElement.textContent || DOMElement.innerText;
      });
    }
  });

  return tui.widget.taggable.Navigation;
});
