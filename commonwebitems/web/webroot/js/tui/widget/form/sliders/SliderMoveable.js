define ("tui/widget/form/sliders/SliderMoveable", ["dojo", "dojo/dnd/Moveable"], function(dojo){
	
	dojo.declare("tui.widget.form.sliders.SliderMoveable", [dojo.dnd.Moveable], {
		
		// summary:
		//		Class for creating a Slider. 
		//
		// description:
		//		This class define the behaviour needed to create a slider. 
		//
		// @author: Maurice Morgan.	
	
		offset: {t: 0, l: 0},

		host: null,
		
		constructor: function(node, params){
			var sliderMoveable = this;
			dojo.safeMixin(sliderMoveable, params);
			sliderMoveable.inherited(arguments);
		},
		
		onDragDetected: function(/* Event */ e){
			// summary:
			//		called when the drag is detected;
			//		responsible for creation of the mover
			var sliderMoveable = this;
			if (!sliderMoveable.dd){
				sliderMoveable.dd = true;
				sliderMoveable.inherited(arguments)
			}
		},
		
		onMoveStop:  function(/* dojo.dnd.Mover */ mover){
			var sliderMoveable = this;
			sliderMoveable.dd = false;
			sliderMoveable.inherited(arguments);
			sliderMoveable.host.onMoveStop(mover); 
		},
		
		onMove: function(mover, leftTop, e){
			// summary:
			//		called during every move notification;
			//		should actually move the node; can be overwritten.
			var sliderMoveable = this;
			switch(sliderMoveable.host.axis){
				case 'x':
  					leftTop.t = sliderMoveable.offset.t || 0;
  				break;
				case 'y':
  					leftTop.l = sliderMoveable.offset.l || 0;
  				break;
			}
			sliderMoveable.inherited(arguments); 
			sliderMoveable.host.onMove(sliderMoveable, leftTop);
		}
	})
	
	return tui.widget.form.sliders.SliderMoveable;
})
