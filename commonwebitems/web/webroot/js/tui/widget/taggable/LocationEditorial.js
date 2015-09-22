define('tui/widget/taggable/LocationEditorial', [
  'dojo',
  'tui/widget/mixins/Taggable',
	'tui/widget/_TuiBaseWidget'
], function(dojo, taggable) {

  dojo.declare('tui.widget.taggable.LocationEditorial', [tui.widget._TuiBaseWidget], {

    // ----------------------------------------------------------------------------- methods

    postCreate: function() {
      var breadcrumb = this;

      breadcrumb.inherited(arguments);

      // Accomodation page form.
      breadcrumb.tagElements(dojo.query('li div.product', breadcrumb.domNode), function(DOMElement) {
        var headerElement = dojo.query('h4 a', DOMElement)[0];
        return headerElement && (headerElement.textContent || headerElement.innerText);
      });

      // Location page form.
      breadcrumb.tagElements(dojo.query('div.labeled', breadcrumb.domNode), function(DOMElement) {
        var headerElement = dojo.query('h3', DOMElement)[0];
        return headerElement && (headerElement.textContent || headerElement.innerText);
      });
    }
  });

  return tui.widget.taggable.LocationEditorial;
});
