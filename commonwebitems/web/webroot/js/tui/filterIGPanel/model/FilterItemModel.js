define ("tui/filterIGPanel/model/FilterItemModel", ["dojo", "dojo/Stateful"] , function(dojo, stateful){
	
	/***********************************************************************************/
   	/* tui.filterIGPanel.model.FilterItemModel									   */
   	/***********************************************************************************/
	dojo.declare("tui.filterIGPanel.model.FilterItemModel", [stateful], {
		
		id: null,
		
		name: null,
		
		disable: null
	})
	
	return tui.filterIGPanel.model.FilterItemModel;
})