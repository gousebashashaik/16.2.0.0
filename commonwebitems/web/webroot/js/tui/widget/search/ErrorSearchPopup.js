define ("tui/widget/search/ErrorSearchPopup", ["dojo", 
											   "tui/widget/popup/ErrorPopup"], function(dojo){

	dojo.declare("tui.widget.search.ErrorSearchPopup", [tui.widget.popup.ErrorPopup], {
		setPosOffset: function(position){
			var errorPopup = this;
			switch(position){
				case tui.widget.mixins.FloatPosition.BOTTOM_CENTER:
					errorPopup.posOffset = {top: 15, left: 0};
  				break;
			}
		},

        onOpen: function () {
            var errorPopup = this;
            dojo.addClass(errorPopup.popupDomNode, "iscape");
        }
	})
	return tui.widget.search.ErrorSearchPopup;
})