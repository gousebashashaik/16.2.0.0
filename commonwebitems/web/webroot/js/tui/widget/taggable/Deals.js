define('tui/widget/taggable/Deals', [
  'dojo',
  'dojo/query',
  'tui/widget/mixins/Taggable',
	'tui/widget/_TuiBaseWidget'
], function(dojo, query, taggable) {

  dojo.declare('tui.widget.taggable.Deals', [tui.widget._TuiBaseWidget], {

    // ----------------------------------------------------------------------------- methods

    postCreate: function() {
      var deals = this;

      deals.inherited(arguments);

      deals.tagElements(query('.deal-item', deals.domNode), function(DOMElement) {
        var aElement = query('a', DOMElement)[0];
        return aElement && (aElement.textContent || aElement.innerText);
      });
    }
  });

  return tui.widget.taggable.Deals;
});
