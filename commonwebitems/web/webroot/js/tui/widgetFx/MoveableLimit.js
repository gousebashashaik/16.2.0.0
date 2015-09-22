define ("tui/widgetFx/MoveableLimit", ["dojo", "dojo/dnd/Moveable"], function(dojo){
	
	dojo.declare("tui.widgetFx.MoveableLimit", [dojo.dnd.Moveable], {
		// summary:
		//		Class for creating a Slider. 
		//
		// description:
		//		This class define the behaviour needed to create a slider. 
		//
		// @author: Maurice Morgan.	
	
		offset: {t: 0, l: 0},
		
		limit: null,
		
		gridStep: false,
		
		host: null,
		
		constructor: function(node, params){
			// summary:
			//		an object, which makes a node moveable
			// node: Node
			//		a node (or node's id) to be moved
			// params: dojo.dnd.__MoveableArgs?
			//		optional parameters
			var moveableLimit = this;
			dojo.safeMixin(moveableLimit, params);
			moveableLimit.inherited(arguments);
		},
		
		onDragDetected: function(/* Event */ e){
			// summary:
			//		called when the drag is detected;
			//		responsible for creation of the mover
			var moveableLimit = this;
			if (!moveableLimit.dd){
				moveableLimit.dd = true;
				moveableLimit.inherited(arguments)
			}
		},
		
		onMoveStop:  function(/* dojo.dnd.Mover */ mover){
				var moveableLimit = this;
				moveableLimit.dd = false;
				moveableLimit.inherited(arguments);
				moveableLimit.host.onMoveStop(mover); 
		},
		
		onMove: function(/* dojo.dnd.Mover */ mover, /* Object */ leftTop, /* Event */ e){
			// summary:
			//		called during every move notification;
			//		should actually move the node; can be overwritten.
			var moveableLimit = this;
			if (moveableLimit.limit.x){
				var limit = moveableLimit.limit;
				leftTop.l = (leftTop.l <= limit.x[0]) ? limit.x[0] : leftTop.l;
				leftTop.l = (leftTop.l > limit.x[1]) ? limit.x[1] : leftTop.l;
				leftTop.t = (moveableLimit.offset.t || 0);
				if (moveableLimit.gridStep){
					leftTop.l = (leftTop.l >= limit.x[1]) ? limit.x[1] : Math.round(leftTop.l / moveableLimit.gridStep) * moveableLimit.gridStep;				
				}
			}
			if (moveableLimit.limit.y){
				var limit = moveableLimit.limit;
				leftTop.t = (leftTop.t <= limit.y[0]) ? limit.y[0] : leftTop.t;
				leftTop.t = (leftTop.t > limit.y[1]) ? limit.y[1] : leftTop.t;
				leftTop.l = (moveableLimit.offset.l || 0);
				if (moveableLimit.gridStep){
					leftTop.t = (leftTop.t >= limit.y[1]) ? limit.y[1] : Math.round(leftTop.t / moveableLimit.gridStep) * moveableLimit.gridStep;
				}
			}
			moveableLimit.inherited(arguments); 
			moveableLimit.host.onMove(moveableLimit, leftTop);
		},
		
		getSet: function(){
			var moveableLimit = this;
			return moveableLimit.step;
		}
	})
	
	return tui.widgetFx.MoveableLimit;
})