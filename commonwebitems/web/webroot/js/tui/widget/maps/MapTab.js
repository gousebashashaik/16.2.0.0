define("tui/widget/maps/MapTab", ["dojo", "tui/widget/Tabs"], function (dojo) {

  dojo.declare("tui.widget.maps.MapTab", [tui.widget.Tabs], {

    tabSelector: ".map-tabs li",

    tabStopEvent: true,

    tabSelectClassname: "activate",

    mapScrollPanels: null,

    postMixInProperties: function () {
      var mapTab = this;
      mapTab.inherited(arguments);
    },
    postCreate: function () {
      var mapTab = this;
      mapTab.mapScrollPanels = [];
      mapTab.inherited(arguments);
      mapTab.containerNode = mapTab.domNode;
    },

    getMapScrollPanels: function () {
      var mapTab = this;
      return mapTab.mapScrollPanels;
    },

    onAfterTabInit: function () {
      var mapTab = this;
      var mapSidePanel = mapTab.getParent();
      mapSidePanel.mapTab = mapTab;
    },

    beforeShowTab: function (/*DOM element*/ tabHeader, /*DOM element*/ tabToContent, /*DOM element*/ tabToHide) {
      var mapTab = this;
      var mapSidePanel = mapTab.getParent();
      mapSidePanel.allowSlideInOut = (tabToContent === tabToHide);
    },

    afterShowTab: function (/*DOM element*/ tabHeader, /*DOM element*/ tabToContent, /*DOM element*/ tabToHide) {
      var mapTab = this;
      if (dojo.query(".places-tab", tabHeader).length > 0) {
        dojo.publish("tui/widget/maps/MapTab/afterShowTab");
      }
    }
  });

  return tui.widget.maps.MapTab;
});