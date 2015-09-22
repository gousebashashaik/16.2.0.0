define ("tui/widgetFx/HorizontalMover", ["dojo", "dojo/dnd/Mover"], function(dojo){
	
	dojo.declare("tui.widgetFx.HorizontalMover", [dojo.dnd.Mover], {
		// summary:
		//		Class for creating a Slider. 
		//
		// description:
		//		This class define the behaviour needed to create a slider. 
		//
		// @author: Maurice Morgan.
		onMouseMove: function(e) {
			var mover = this;
			//dojo.dnd.autoScroll(e);
			var m = this.marginBox;
			mover.host.onMove(mover, {l: m.l + e.pageX, t: 0}, e);
			dojo.stopEvent(e);
		}
	})
	
	return tui.widgetFx.HorizontalMover;
})