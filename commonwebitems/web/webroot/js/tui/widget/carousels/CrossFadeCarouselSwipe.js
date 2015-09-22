define ("tui/widget/carousels/CrossFadeCarouselSwipe", ["dojo", "tui/widget/carousels/CarouselSwipe", "tui/widgetFx/CrossFade"], function(dojo){

	dojo.declare("tui.widget.carousels.CrossFadeCarouselSwipe", [tui.widget.carousels.CarouselSwipe], {

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