define ("tui/filterBPanel/model/FilterItemModel", ["dojo", "dojo/Stateful"] , function(dojo, stateful){
	
	/***********************************************************************************/
   	/* tui.filterPanel.model.FilterItemModel									   */
   	/***********************************************************************************/
	dojo.declare("tui.filterBPanel.model.FilterItemModel", [stateful], {
		
		id: null,
		
		name: null,
		
		disable: null
	})
	
	return tui.filterBPanel.model.FilterItemModel;
})