define("tui/filterBPanel/view/options/Flight", [
  "dojo/_base/declare",
  "dojo/dom-attr",
  "tui/filterBPanel/view/options/OptionsFilter"], function (declare, domAttr) {

  return declare("tui.filterBPanel.view.options.Flight", [tui.filterBPanel.view.options.OptionsFilter], {

    dataPath: 'flightOptions',

    visibilityKey : 'departureAirportFilter||departureTimeComingBackFilter||departureTimeGoingOutFilter',

    draw: function () {
      var flightOptions = this;
      flightOptions.inherited(arguments);
      flightOptions.defineNumber();
      _.each(dijit.findWidgets(flightOptions.domNode), function (w) {
        w.tag = flightOptions.tagMappingTable.table[flightOptions.declaredClass];
        domAttr.set(w.domNode, 'analytics-id', w.tag);
        domAttr.set(w.domNode, 'analytics-instance', flightOptions.number);
        if(w.model != undefined)
        domAttr.set(w.domNode, 'analytics-text', w.model.name);
      });
    }

  });
});