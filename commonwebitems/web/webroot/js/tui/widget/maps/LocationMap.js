define("tui/widget/maps/LocationMap", ["dojo","tui/widget/maps/Mappers",
									   "tui/widget/maps/InspirationMapBase",
									   "tui/utils/ObjectUtils"], function(dojo, mappers){
	
	dojo.declare("tui.widget.maps.LocationMap", [tui.widget.maps.InspirationMapBase], {
		
		// ---------------------------------------------------------------- locationMap properties
		
		mapFilter: false,
		
		showEntitiesTypes: ["locations"],

		// ---------------------------------------------------------------- locationMap methods	
		
		postMixInProperties: function() {
			var locationMap = this;
			locationMap.selectedLocations = [];
			locationMap.inherited(arguments);
			locationMap.parseJsonData();
		},
		
		parseJsonData: function() {
			var locationMap = this;
      locationMap.destinationBreadcrumb = mappers.locationNameMapper(locationMap.jsonData);
      locationMap.selectedLocations = mappers.locationsMapper(locationMap.jsonData, "locations");
		},
		
		postCreate: function(){
			var locationMap = this;
			locationMap.inherited(arguments);
			locationMap.setMarkers(locationMap.selectedLocations);
		}
	})
	
	return tui.widget.maps.LocationMap;
})