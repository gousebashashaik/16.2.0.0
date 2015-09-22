define ("tui/filterIGPanel/mixin/FilterPanelItem", ["dojo"] , function(dojo){

	dojo.declare("tui.filterIGPanel.mixin.FilterPanelItem", null, {
		
		filterId: null,

		addSearchResultWatcher: function(filterPanelModel){
			var filterPanelItem = this;
            filterPanelModel.watch('filterMap', function(name, oldValue, newValue){
				filterPanelItem.onValueChange(name, oldValue[filterPanelItem.filterId], newValue[filterPanelItem.filterId])
			})
		},
		
		onValueChange: function(name, newValue, oldValues){
			
		}
	})
	
	return tui.filterIGPanel.mixin.FilterPanelItem;
})