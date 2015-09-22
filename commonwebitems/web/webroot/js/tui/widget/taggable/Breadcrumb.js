define('tui/widget/taggable/Breadcrumb', [
  'dojo',
  'tui/widget/mixins/Taggable',
	'tui/widget/_TuiBaseWidget'
], function(dojo, taggable) {

  dojo.declare('tui.widget.taggable.Breadcrumb', [tui.widget._TuiBaseWidget], {

    // ----------------------------------------------------------------------------- methods

    postCreate: function() {
      var breadcrumb = this;

      breadcrumb.inherited(arguments);

      breadcrumb.tagElements(dojo.query('a', breadcrumb.domNode), function(DOMElement) {
        return DOMElement.textContent || DOMElement.innerText;
      });
    }
  });

  return tui.widget.taggable.Breadcrumb;
});
