define ("tui/filterPanel/model/SliderModel", ["dojo", "tui/filterPanel/model/FilterItemModel"] , function(dojo, filterItemModel){
	
	/***********************************************************************************/
   	/* tui.filterPanel.model.FilterItemModel									   */
   	/***********************************************************************************/
	dojo.declare("tui.filterPanel.model.SliderModel", [filterItemModel], {
		
		range: null,
		
		step: null,
		
		values: null
	})
	
	return tui.filterPanel.model.SliderModel;
})