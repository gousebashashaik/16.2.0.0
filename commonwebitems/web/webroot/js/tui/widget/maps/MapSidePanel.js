define("tui/widget/maps/MapSidePanel", ["dojo", "tui/widget/Panel"], function (dojo) {

  dojo.declare("tui.widget.maps.MapSidePanel", [tui.widget.Panel], {

    allowSlideInOut: true,

    mapTab: null,

    //---------------------------------------------------------------- panel properties

    slideable: false,

    slideTargetSelector: ".map-tabs",

    //---------------------------------------------------------------- methods
    getMapTab: function () {
      var mapSidePanel = this;
      return mapSidePanel.mapTab;
    },

    postCreate: function () {
      var mapSidePanel = this;
      mapSidePanel.getParent().mapSidePanel = mapSidePanel;
      mapSidePanel.inherited(arguments);
    },

    addSlideInOutEventListener: function () {
      var mapSidePanel = this;
      if (mapSidePanel.slideable) {
        mapSidePanel.slideInOutTransition = new tui.widgetFx.SlideInOut({
          domNode: mapSidePanel.panelNode,
          slideTargetSelector: mapSidePanel.slideTargetSelector,
          slideInOutPos: mapSidePanel.slideInOutPos,
          widgetTransition: function (event) {
            var transition = this;
            if (mapSidePanel.allowSlideInOut || transition.slideIn) {
              transition.inherited(arguments);
            }
          }
        })
      }
    }
  });

  return tui.widget.maps.MapSidePanel;
});