define ("tui/filterPanel/model/CheckBoxModel", ["dojo", "tui/filterPanel/model/FilterItemModel"] , function(dojo, filterItemModel){
	
	/***********************************************************************************/
   	/* tui.filterPanel.model.FilterItemModel									   */
   	/***********************************************************************************/
	dojo.declare("tui.filterPanel.model.CheckBoxModel", [filterItemModel], {
		
		selected: false
	})
	
	return tui.filterPanel.model.CheckBoxModel;
})