define("tui/filterPanel/view/options/Flight", [
  "dojo/_base/declare",
  "dojo/dom-attr",
  "tui/filterPanel/view/options/OptionsFilter"], function (declare, domAttr) {

  return declare("tui.filterPanel.view.options.Flight", [tui.filterPanel.view.options.OptionsFilter], {

    dataPath: 'flightOptions',

    draw: function () {
      var flightOptions = this;
      flightOptions.inherited(arguments);
      flightOptions.defineNumber();
      _.each(dijit.findWidgets(flightOptions.domNode), function (w) {
        w.tag = flightOptions.tagMappingTable.table[flightOptions.declaredClass];
        domAttr.set(w.domNode, 'analytics-id', w.tag);
        domAttr.set(w.domNode, 'analytics-instance', flightOptions.number);
        domAttr.set(w.domNode, 'analytics-text', w.model.name);
      });
    }

  });
});