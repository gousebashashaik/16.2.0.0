define ("tui/widgetFx/Transition", ["dojo", "dojo/fx"], function(dojo){
	
	dojo.declare("tui.widgetFx.Transition", null , {
					 				
		domNode: null,
				
		transitionconnects: null,
		
		// boolean which indicates that a transition is currently in progress.
		animating: false,
		
		// ---------------------------------------------------------------- methods
		
		constructor: function(args) {
			var transition = this;
        	dojo.safeMixin(transition, args);
        	transition.transitionconnects = [];
        	transition.beforeInit(transition);
        	transition.attachTransitionEventListeners();
        	transition.afterInit();
    	},
    	
   		beforeInit: function(){
   			
   		},
   		 
		attachTransitionEventListeners: function(){
			
		},
   		
   		afterInit: function(){
   			
   		},  
   		 	
   		widgetTransition: function(transition, element){
   			var transition = this;
   			transition.onWidgetTransition(arguments);	
   		},
   		
   		triggerConnect: function(node, eventType){
   			
   		},
   		
   		onWidgetTransition: function(transition){
 			var transition = this;
   			transition.animating = true;
   			return transition.animating;
   		},
   		
   		onWidgetTransitionEnd: function(transition, itemContent, item, wipe, target){
   			var transition = this;
   			transition.animating = false;
			wipe = wipe ? wipe : "";
			target = target ? target : null;
			dojo.publish("tui/widget/booking/displayContent",[{ "wipeMode" : wipe, "target": target}]);
   			return transition.animating;
   		}
	})
	
	return tui.widgetFx.Transition;
})