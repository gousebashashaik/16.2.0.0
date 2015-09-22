define("tui/searchBPanel/view/DestinationScrollPanel", ["dojo", "tui/widget/ScrollPanel"], function(dojo){

	dojo.declare("tui.searchBPanel.view.DestinationScrollPanel", [tui.widget.ScrollPanel], {
	
		postCreate: function(){
			var destScrollPanel = this;
			destScrollPanel.inherited(arguments); 
			destScrollPanel.update();
    },

    onMoveStop: function (mover) {
    
    }
  });
	
	return tui.searchBPanel.view.DestinationScrollPanel;
});
	