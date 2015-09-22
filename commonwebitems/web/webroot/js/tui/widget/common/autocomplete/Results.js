define('tui/widget/common/autocomplete/Results', [
  'dojo',
  'dojo/dom-class',
  'dojo/dom-construct',
  'dojo/_base/lang'], function(dojo, domClass, domConstruct, lang) {

    function Results(dom, template, onSelect, onResultsDisplay, onResultsHide, entityStore) {
    this.dom = dom;
    this.template = template;
    this.onSelect = onSelect;
        this.onResultsDisplay = onResultsDisplay;
        this.onResultsHide = onResultsHide;
    this.entityStore = entityStore;
    return this;
  }

  Results.prototype.clear = function() {
    domConstruct.empty(this.dom);
    this.hide();
    return this;
  };

  Results.prototype.hide = function() {
    domClass.add(this.dom, 'hide');
        this.onResultsHide();
    return this;
  };

  Results.prototype.add = function(result) {
    var results = this;

        result['displayText'] = _.highlightWords(result['name'], result['match']);
        if(result['type'] != null)
        	{
        	  result['ctype'] =result['type'].toLowerCase() ;
        	}
      

    var resultNode = domConstruct.place(lang.replace(results.template, result), results.dom, 'last');
    dojo.connect(resultNode, 'onclick', function(e) {
      dojo.stopEvent(e);
      results.onSelect();
      results.entityStore.put({'id': result['id'], 'name': result['name'], 'type': result['type'], 'multiSelect':result['multiSelect']});
    });
  };

  Results.prototype.set = function(response) {
    var results = this;
    _.when(!_.isEmpty(response), function() {
      domClass.remove(results.dom, 'hide');
            results.onResultsDisplay();
      _.each(response, results.add.bind(results));
    });
  };

  return Results;
});


