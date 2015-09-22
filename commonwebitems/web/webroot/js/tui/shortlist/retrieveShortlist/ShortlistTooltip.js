define('tui/shortlist/retrieveShortlist/ShortlistTooltip', [
  'dojo',
  'tui/widget/popup/Tooltips',
  'tui/search/nls/Searchi18nable'], function(dojo){

  dojo.declare('tui.shortlist.retrieveShortlist.ShortlistTooltip', [tui.widget.popup.Tooltips, tui.search.nls.Searchi18nable], {
  
    text:"",
 
    updateRoomToolTip:function(){
		widget=this;
		widget.initSearchMessaging();
		if(widget.text == "") {
			widget.text = widget.searchMessaging.searchResults.roomsTooltip;
	        widget.text = widget.text.split("\\'").join("'");
		}        
	},
	addPopupEventListener: function () {
		var tooltip = this;
		if (!tooltip.domNode) return;
		tooltip.connect(tooltip.domNode, ["on", tooltip.eventType].join(""), function (e) {
			dojo.stopEvent(e);
			tooltip.open();
			tooltip.tooltipGlobal = tooltip;
		});
		
		tooltip.connect(document.body, ["on", "touchmove"].join(""), function (e) {
			//dojo.stopEvent(e);
			tooltip.tooltipGlobal.close();
		});
		tooltip.connect(tooltip.domNode, ["on", "click"].join(""), function (e) {
			dojo.stopEvent(e);
			tooltip.open();
			tooltip.tooltipGlobal = tooltip;
		});

		if ((tooltip.eventType == "mouseenter") || (tooltip.eventType == "mouseover")) {
			tooltip.connect(tooltip.domNode, "onmouseout", function (e) {
				dojo.stopEvent(e);
				tooltip.close();
			});
		}
	},
  
    postCreate: function(){
      var widget = this;
	  // initialise internationalisation
      widget.updateRoomToolTip();
      widget.inherited(arguments);
    }
  });
});
