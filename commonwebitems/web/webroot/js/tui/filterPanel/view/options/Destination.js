define("tui/filterPanel/view/options/Destination", [
  "dojo/_base/declare",
  "dojo/dom-attr",
  "tui/filterPanel/view/options/OptionsFilter"], function (declare, domAttr) {

  return declare("tui.filterPanel.view.options.Destination", [tui.filterPanel.view.options.OptionsFilter], {

    dataPath: 'destinationOptions',

    draw: function () {
      var destination = this;
      destination.inherited(arguments);
      destination.defineNumber();
      _.each(dijit.findWidgets(destination.domNode), function (w) {
        w.tag = destination.tagMappingTable.table[destination.declaredClass];
        domAttr.set(w.domNode, 'analytics-id', w.tag);
        domAttr.set(w.domNode, 'analytics-instance', destination.number);
        domAttr.set(w.domNode, 'analytics-text', w.model.name);
      });
    }

  });
});