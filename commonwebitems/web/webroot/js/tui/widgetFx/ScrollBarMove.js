define ("tui/widgetFx/ScrollBarMove", ["dojo", "dojo/dnd/Moveable"], function(dojo){
	
	dojo.declare("tui.widgetFx.ScrollBarMove", [dojo.dnd.Moveable], {

		host: null,
		
		constructor: function(node, params){
			var scrollBarMove = this;
			dojo.safeMixin(scrollBarMove, params);
			scrollBarMove.inherited(arguments);
		},

		onMove: function(/* dojo.dnd.Mover */ mover, /* Object */ leftTop, /* Event */ e){
			var scrollBarMove = this;
			scrollBarMove.inherited(arguments);
			scrollBarMove.host.vDrag(leftTop);
		}
	})
	
	return tui.widgetFx.ScrollBarMove;
})