define ("tui/widget/Panel", ["dojo",
							 "tui/widget/_TuiBaseWidget", 
							 "tui/widgetFx/SlideInOut", 
							 "tui/widget/mixins/Tabbable"], function(dojo){
	
	dojo.declare("tui.widget.Panel", [tui.widget._TuiBaseWidget, tui.widget.mixins.Tabbable], {
		
		//---------------------------------------------------------------- panel properties
		slideable: false,
		
		slideTargetSelector: "",
		
		slideInOutTransition: null,
		
		slideInOutPos: tui.widgetFx.SlideInOut.LEFT,
		
		tabbable: false,
	
		panelNode: null,
			
		//---------------------------------------------------------------- tabbable properties
		
		tabSelector: ".tabs li",
		
		//---------------------------------------------------------------- methods
		
		postCreate: function(){
			var panel = this;
			panel.panelNode = panel.panelNode || panel.domNode;
			panel.addSlideInOutEventListener();
			if (panel.tabbable){
			//	panel.attachTabbableEventListeners();
			}
		},
		
		addSlideInOutEventListener: function() {
			var panel = this;
			if(panel.slideable) {
				panel.slideInOutTransition = new tui.widgetFx.SlideInOut({
					domNode : panel.panelNode,
					slideTargetSelector : panel.slideTargetSelector,
					slideInOutPos : panel.slideInOutPos
				})
			}
		}

	})
	
	return tui.widget.Panel;
})