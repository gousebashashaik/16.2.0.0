define("tui/widgetFx/SlideInOut", ["dojo", "dojo/_base/fx", "tui/widgetFx/Transition"], function(dojo, baseFx){
	
	dojo.declare("tui.widgetFx.SlideInOut", [tui.widgetFx.Transition], {
		
		slideTargetSelector: ".slideInOut",
		
		moveAmount: 0,
		
		slideInOutPos: "left",
		
		slideIn: false,
		
		beforeInit: function(){
			var slideInOut = this;
			dojo.style(slideInOut.domNode, slideInOut.slideInOutPos, 0);
			var pos = dojo.position(slideInOut.domNode);
			var property = (slideInOut.slideInOutPos === "left" || 
								slideInOut.slideInOutPos === "right") ? "w" : "h"
			slideInOut.moveAmount = -pos[property];
		},
		
		attachTransitionEventListeners: function(){
			var slideInOut = this;
			dojo.query(slideInOut.slideTargetSelector, slideInOut.domNode).onclick(function(event){
				dojo.stopEvent(event);
				var targetDomNode = this;
				dojo.addClass(targetDomNode, slideInOut.slideInOutPos);
				slideInOut.widgetTransition(slideInOut);
			})
   		},
   		
   		widgetTransition: function(transition){
   			var slideInOut = this;
   			if (slideInOut.animating) return;
   			slideInOut.inherited(arguments);
   			var start = parseInt(dojo.style(slideInOut.domNode, slideInOut.slideInOutPos));
   			var end = (start === 0) ? slideInOut.moveAmount : 0;
   			var properties = {};
   			properties[slideInOut.slideInOutPos] = {
   				start: start, 
   				end: end
   			}
   			baseFx.animateProperty({
				node: slideInOut.domNode,
				properties: properties,
				onEnd: function(){
					slideInOut.onWidgetTransitionEnd(slideInOut);
					slideInOut.slideIn = !(slideInOut.slideIn);
				}
			}).play();
   		},
   		 
   		onSlide: function(){
   			
   		},
   		
   		afterSlide: function(){
   			
   		}
  	})
	
	tui.widgetFx.SlideInOut.LEFT = "left";
	tui.widgetFx.SlideInOut.RIGHT = "right";
	tui.widgetFx.SlideInOut.TOP = "top";
	tui.widgetFx.SlideInOut.BOTTOM = "bottom";
	
	return tui.widgetFx.SlideInOut;
})