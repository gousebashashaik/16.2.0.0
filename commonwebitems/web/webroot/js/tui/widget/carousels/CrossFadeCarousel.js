define ("tui/widget/carousels/CrossFadeCarousel", ["dojo", "tui/widget/carousels/Carousel", "tui/widgetFx/CrossFade"], function(dojo){

	dojo.declare("tui.widget.carousels.CrossFadeCarousel", [tui.widget.carousels.Carousel], {
		
		transitionType: "CrossFade",
					
		postCreate: function(){
			var crossFadeCarousel = this;	
			crossFadeCarousel.inherited(arguments);
		},
		
		backPreload: function(transition){},
		
		forwardPreload: function(transition){},
		
		append: function(data, transition){},
				
		addNewContent: function(){}
	})
		
	return tui.widget.carousels.CrossFadeCarousel;
})