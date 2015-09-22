define ("tui/filterBPanel/model/CheckBoxModel", ["dojo", "tui/filterBPanel/model/FilterItemModel"] , function(dojo, filterItemModel){
	
	/***********************************************************************************/
   	/* tui.filterPanel.model.FilterItemModel									   */
   	/***********************************************************************************/
	dojo.declare("tui.filterBPanel.model.CheckBoxModel", [filterItemModel], {
		
		selected: false
	})
	
	return tui.filterBPanel.model.CheckBoxModel;
})