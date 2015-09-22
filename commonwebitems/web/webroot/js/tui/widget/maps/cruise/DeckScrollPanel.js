define("tui/widget/maps/cruise/DeckScrollPanel", [
    "dojo",
    "tui/widgetFx/HorizontalMover",
    "tui/widget/ScrollPanel"], function(dojo, horizontalMover){

	dojo.declare("tui.widget.maps.cruise.DeckScrollPanel", [tui.widget.ScrollPanel], {
	
		postCreate: function(){
			var mapScrollPanel = this;
            mapScrollPanel.mover = horizontalMover;
			mapScrollPanel.getParent().mapScrollPanels.push(mapScrollPanel);
			mapScrollPanel.inherited(arguments);
      mapScrollPanel.subscribe("tui/widget/maps/MapTab/afterShowTab", function(){
          mapScrollPanel.update();
      });

      mapScrollPanel.tagAllElements();
    },

    tagAllElements: function() {
      // Called by addEventListeners() method of tui.widget.maps.MapTopx.
      var mapScrollPanel = this;

      mapScrollPanel.tagElements(dojo.query('li', mapScrollPanel.domNode), function(DOMElement) {
        var headerElement = dojo.query('h5', DOMElement)[0];
        return headerElement && (headerElement.textContent || headerElement.innerText);
      });
    }
  });
	
	return tui.widget.maps.cruise.DeckScrollPanel;
});
	