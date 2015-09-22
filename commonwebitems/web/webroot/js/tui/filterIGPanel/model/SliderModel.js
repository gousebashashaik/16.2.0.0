define ("tui/filterIGPanel/model/SliderModel", ["dojo", "tui/filterIGPanel/model/FilterItemModel"] , function(dojo, filterItemModel){
	
	/***********************************************************************************/
   	/* tui.filterIGPanel.model.FilterItemModel									   */
   	/***********************************************************************************/
	dojo.declare("tui.filterIGPanel.model.SliderModel", [filterItemModel], {
		
		range: null,
		
		step: null,
		
		values: null
	})
	
	return tui.filterIGPanel.model.SliderModel;
})