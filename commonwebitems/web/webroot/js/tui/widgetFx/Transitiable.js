define("tui/widgetFx/Transitiable", ["dojo", "tui/widgetFx/TransitionFactory"], function(dojo){
	
	dojo.declare("tui.widgetFx.Transitiable", null , {
		
		// Default transition type for widget.
		transitionType: null,
		
		transition: null,
		
		transitionOptions: {},
		
		//---------------------------------------------------------------- methods
		addTransition: function(){
			// summary:
			//		Adds a transition of a given type to the widget.
			//
			// description:
			//		This will add default transition for get given widget. 
			//		i.e By default the ProductWidget will slide its content 
			//		back and forth. 
			var transitiable = this;
			return tui.widgetFx.TransitionFactory.getTransition(transitiable.transitionType, transitiable.transitionOptions);
		}
	})
	
	return tui.widgetFx.Transitiable;
})