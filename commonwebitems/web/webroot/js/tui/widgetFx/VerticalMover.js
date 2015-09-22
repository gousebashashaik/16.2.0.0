define ("tui/widgetFx/VerticalMover", ["dojo", "dojo/dnd/Mover"], function(dojo){
	
	dojo.declare("tui.widgetFx.VerticalMover", [dojo.dnd.Mover], {
		// summary:
		//		Class for creating a Slider. 
		//
		// description:
		//		This class define the behaviour needed to create a slider. 
		//
		// @author: Maurice Morgan.
		onMouseMove: function(e) {
			var mover = this;
			var m = this.marginBox;
			mover.host.onMove(mover, {l: 0, t: m.t + e.pageY});
		}
	})
	
	return tui.widgetFx.VerticalMover;
})