define("tui/widget/AttractionsResultPanelModule", [
  "../.",
  "tui/widget/ResultPane"
], function(dojo) {

  dojo.declare("tui.widget.AttractionsResultPanel", [tui.widget.ResultPane], {

    carouselContainerSelector: ".carousels-container",

    scrollpagable: false,

    postCreate: function() {
      var attractionsResultPanel = this;
      dojo.query(attractionsResultPanel.carouselContainerSelector, attractionsResultPanel.domNode)
        .addClass(attractionsResultPanel.loadedSelector);

    }
  });

  dojo.declare("tui.widget.AttractionAccordion", [tui.widget.expand.Expandable], {

		itemSelector: "",
		targetSelector: ".item-toggle",
			
		itemContentSelector:".item-content"

  });

  return tui.widget.AttractionsResultPanelModule;
});
