define ("tui/widget/TripAdvisorSmoothScroll", ["dojo","dojox/fx/scroll","tui/widget/expand/LinkExpandable"], function(dojo,scroll){
		
	// Class for smooth scrollling of the trip advisor link
	
	dojo.declare("tui.widget.TripAdvisorSmoothScroll", [tui.widget.expand.LinkExpandable], {
		
		//-----------------------------------------------------properties
		
		target: '',
		
		targetId: null,
		
		//-----------------------------------------------------methods
		
		postCreate: function() {
			var smoothScroll = this;
			smoothScroll.targetId = dojo.byId(smoothScroll.target);
			smoothScroll.addEventListener();
			smoothScroll.attachTag();
			smoothScroll.tagElements(dojo.query('.based-on', smoothScroll.domNode),"basedOnXReviews");
		},
		
		addEventListener: function() {
			var smoothScroll = this;
			dojo.connect(dojo.query("a",smoothScroll.domNode)[0], 'onclick', function() {
				dojox.fx.smoothScroll({
		              node: smoothScroll.targetId,
		              win: window,
		              duration: 1000
		         }).play();
		    });
		}
	});
	
	return tui.widget.TripAdvisorSmoothScroll;
});