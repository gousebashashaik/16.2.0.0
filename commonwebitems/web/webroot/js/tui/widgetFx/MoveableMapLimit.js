define ("tui/widgetFx/MoveableMapLimit", ["dojo", "dojo/dnd/Moveable"], function(dojo){
	
	dojo.declare("tui.widgetFx.MoveableMapLimit", [dojo.dnd.Moveable], {

		constructor: function(node, params){
			var moveableLimit = this;
			dojo.safeMixin(moveableLimit, params);
			moveableLimit.inherited(arguments);
			var position = dojo.position(moveableLimit.node);
			var viewportPosition = dojo.position(moveableLimit.viewportNode)
			
			moveableLimit.maxl =  viewportPosition.w - position.w;
			moveableLimit.maxt =  viewportPosition.h - position.h;
		},
	
		onMove: function(/* dojo.dnd.Mover */ mover, /* Object */ leftTop, /* Event */ e){
			var moveableLimit = this;
			if ("block" != dojo.style(moveableLimit.mapSmall, "display")){
				if (leftTop.l > 0) leftTop.l = 0;
				if (leftTop.t > 0) leftTop.t = 0;
				if (leftTop.l < moveableLimit.maxl) leftTop.l = moveableLimit.maxl;
				if (leftTop.t < moveableLimit.maxt) leftTop.t = moveableLimit.maxt;
			} else {
				leftTop.t = 0;
				leftTop.l = 0;
			}
			moveableLimit.inherited(arguments);
		}
	})
	
	return tui.widgetFx.MoveableMapLimit;
})