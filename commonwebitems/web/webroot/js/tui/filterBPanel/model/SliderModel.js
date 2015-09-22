define ("tui/filterBPanel/model/SliderModel", ["dojo", "tui/filterBPanel/model/FilterItemModel"] , function(dojo, filterItemModel){
	
	/***********************************************************************************/
   	/* tui.filterPanel.model.FilterItemModel									   */
   	/***********************************************************************************/
	dojo.declare("tui.filterBPanel.model.SliderModel", [filterItemModel], {
		
		range: null,
		
		step: null,
		
		values: null
	})
	
	return tui.filterBPanel.model.SliderModel;
})