define ("tui/filterIGPanel/model/CheckBoxModel", ["dojo", "tui/filterIGPanel/model/FilterItemModel"] , function(dojo, filterItemModel){
	
	/***********************************************************************************/
   	/* tui.filterIGPanel.model.FilterItemModel									   */
   	/***********************************************************************************/
	dojo.declare("tui.filterIGPanel.model.CheckBoxModel", [filterItemModel], {
		
		selected: false
	})
	
	return tui.filterIGPanel.model.CheckBoxModel;
})