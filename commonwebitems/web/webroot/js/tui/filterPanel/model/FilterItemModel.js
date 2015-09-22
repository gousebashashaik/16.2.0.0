define ("tui/filterPanel/model/FilterItemModel", ["dojo", "dojo/Stateful"] , function(dojo, stateful){
	
	/***********************************************************************************/
   	/* tui.filterPanel.model.FilterItemModel									   */
   	/***********************************************************************************/
	dojo.declare("tui.filterPanel.model.FilterItemModel", [stateful], {
		
		id: null,
		
		name: null,
		
		disable: null
	})
	
	return tui.filterPanel.model.FilterItemModel;
})