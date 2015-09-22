define("tui/filterBPanel/view/options/BoardBasis", [
  "dojo/_base/declare",
  "dojo/dom-attr",
  "dojo/dom-construct",
  "dojo/text!tui/filterBPanel/view/templates/boardBasisFilters.html",
  "dojox/dtl",
  "dojox/dtl/Context",
  "dojox/dtl/tag/logic",
  "tui/filterBPanel/view/options/OptionsFilter"], function (declare, domAttr, domConstruct, tmpl) {

  return declare("tui.filterBPanel.view.options.BoardBasis", [tui.filterBPanel.view.options.OptionsFilter], {

    dataPath: 'generalOptions.filters.0',

    tmpl: tmpl,

    draw: function (data) {
    	 var boardOptions = this;
         boardOptions.inherited(arguments);
         boardOptions.defineNumber();
        _.each(dijit.findWidgets(boardOptions.domNode), function (w) {
           w.tag = boardOptions.tagMappingTable.table[boardOptions.declaredClass];
           domAttr.set(w.domNode, 'analytics-id', w.tag);
           domAttr.set(w.domNode, 'analytics-instance', boardOptions.number);
           domAttr.set(w.domNode, 'analytics-text', w.model.name);
      });
    }

  });
});