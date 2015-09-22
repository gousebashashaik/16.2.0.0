define('tui/widget/taggable/SEOLinks', [
  'dojo',
  'tui/widget/mixins/Taggable',
	'tui/widget/_TuiBaseWidget'
], function(dojo, taggable) {

  dojo.declare('tui.widget.taggable.SEOLinks', [tui.widget._TuiBaseWidget], {

    // ----------------------------------------------------------------------------- methods

    postCreate: function() {
      var seoLinks = this;

      seoLinks.inherited(arguments);

      seoLinks.tagElements(dojo.query('a', seoLinks.domNode), function(DOMElement) {
        return DOMElement.textContent || DOMElement.innerText;
      });
    }
  });

  return tui.widget.taggable.SEOLinks;
});
