define ("tui/filterPanel/mixin/FilterPanelItem", ["dojo"] , function(dojo){

	dojo.declare("tui.filterPanel.mixin.FilterPanelItem", null, {
		
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
	
	return tui.filterPanel.mixin.FilterPanelItem;
})