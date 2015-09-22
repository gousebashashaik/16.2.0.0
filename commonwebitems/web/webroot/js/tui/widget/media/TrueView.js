define("tui/widget/media/TrueView", [
	"dojo",
	"dojo/on",
	"dojo/mouse",
	"tui/widget/_TuiBaseWidget"], function(dojo, on, mouse) {
	
		dojo.declare("tui.widget.media.TrueView", [tui.widget._TuiBaseWidget], {

			imgItems: null,

			postCreate: function () {
				var trueView = this;
				trueView.inherited(arguments);
				trueView.initHover();
			},

			initHover: function () {
				var trueView = this;
			
				trueView.imgItems = dojo.query("img", trueView.domNode);
				
				// on hover, fade out all other items
				on(trueView.domNode, on.selector("img", mouse.enter), function(event){
					dojo.stopEvent(event);
					_.forEach(trueView.imgItems, function(img){
						if (img !== event.target) {
							dojo.addClass(img, "fade-out")
						}
					});
				});

				// on leave, fade out all other items
				on(trueView.domNode, on.selector("img", mouse.leave), function(event){
					dojo.stopEvent(event);
					_.forEach(trueView.imgItems, function(img){
						if (img !== event.target) {
							dojo.removeClass(img, "fade-out")
						}
					});
				});
			}

		});

		return tui.widget.media.TrueView;
});