define("tui/widget/maps/MapSwitchButton", ["dojo", "tui/widget/SwitchButton", "tui/widget/maps/InspirationMapBase"], function(dojo){

	dojo.declare("tui.widget.maps.MapSwitchButton", [tui.widget.SwitchButton], {
		
		parentMapBaseWidget: null,
		
		postCreate: function(){
			var mapSwitchButton = this;
			var parentWidget = mapSwitchButton
			while(true){
				parentWidget = parentWidget.getParent();
				if (!parentWidget) break;
				if (parentWidget instanceof tui.widget.maps.InspirationMapBase){
					mapSwitchButton.parentMapBaseWidget = parentWidget;
					break;
				}
			}
			mapSwitchButton.inherited(arguments);
		},
		
		onbuttonclick: function(value, state, mapSwitchButton){
      dojo.publish("tui/widget/maps/MapSwitchButton/onbuttonclick", [mapSwitchButton.parentMapBaseWidget, state, value])
		}
	});
	
	return tui.widget.maps.MapSwitchButton;
});