define ("tui/widget/SmoothScroll", ["dojo","dojox/fx/scroll","tui/widget/expand/LinkExpandable"], function(dojo,scroll){

	// Class for smooth scrolling

	dojo.declare("tui.widget.SmoothScroll", [tui.widget.expand.LinkExpandable], {

		//-----------------------------------------------------properties

		target: '',

		duration: '',

		targetId: null,

		//-----------------------------------------------------methods

		postCreate: function() {
			var smoothScroll = this;
			smoothScroll.targetId = dojo.byId(smoothScroll.target);
			smoothScroll.addEventListener();
			smoothScroll.attachTag();
		},

		addEventListener: function() {
			var smoothScroll = this;
			dojo.connect(dojo.query("a",smoothScroll.domNode)[0], 'onclick', function() {
				dojox.fx.smoothScroll({
		              node: smoothScroll.targetId,
		              win: window,
		              duration: smoothScroll.duration
		         }).play();
		    });
		}
	});

	return tui.widget.SmoothScroll;
});