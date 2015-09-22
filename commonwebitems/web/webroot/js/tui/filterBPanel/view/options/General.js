define("tui/filterBPanel/view/options/General", [
  "dojo/_base/declare",
  "dojo/dom-attr",
  "tui/filterBPanel/view/options/OptionsFilter"], function (declare, domAttr) {

  return declare("tui.filterBPanel.view.options.General", [tui.filterBPanel.view.options.OptionsFilter], {

    dataPath: 'generalOptions',
    visibilityKey : 'collectionFilter||bestforFilter',

    draw: function () {
      var generalOptions = this;
      generalOptions.inherited(arguments);
      generalOptions.defineNumber();
      _.each(dijit.findWidgets(generalOptions.domNode), function (w) {
        w.tag = generalOptions.tagMappingTable.table[generalOptions.declaredClass];
        domAttr.set(w.domNode, 'analytics-id', w.tag);
        domAttr.set(w.domNode, 'analytics-instance', generalOptions.number);
        domAttr.set(w.domNode, 'analytics-text', w.model.name);
      });
    }

  });
});