define("tui/widgetFx/CrossFade", ["dojo", "dojo/_base/fx", "tui/widgetFx/paging/TransitionPaging"], function(dojo, baseFx){

	dojo.declare("tui.widgetFx.CrossFade", [tui.widgetFx.paging.TransitionPaging], {
		
		currentIndex: 0,
		
		fadeFx: null,
		
		beforeInit: function(){
			var crossFade = this;
			crossFade.fadeFx = [];
			crossFade.inherited(arguments);
			crossFade.currentIndex = 0;
			dojo.query(crossFade.slideContainer)
				.query(crossFade.itemSelector).style({
					position: "absolute",
					top : 0, left: 0
				})
		},
		
   		previous: function(index, element){
   			// summary:
			//		Creates fade-in and fade-out fx for element which need to 
			//		crossfade. When previous has been selected. 
   			var crossFade = this;
   			var loopEnd = crossFade.getPreviousIndexEnd();
      crossFade.createPrevFx(loopEnd, 1, 0, index, 0);
   			loopEnd = crossFade.getPreviousIndexEnd();
      crossFade.createPrevFx(loopEnd, 0.4, 1, index, 1);
   			crossFade.currentIndex++;
   		}, 
   		
   		next: function(index, element){
   			// summary:
			//		Creates fade-in and fade-out fx for element which need to 
			//		crossfade. When next has been selected. 
   			var crossFade = this;
   			var loopEnd = crossFade.getNextIndexEnd();
      crossFade.createNextFx(loopEnd, 1, 0, index, 0);
   			loopEnd = crossFade.getNextIndexEnd();
      crossFade.createNextFx(loopEnd, 0.4, 1, index, 1);
   			crossFade.currentIndex--;
   		},
   		
   		createPrevFx: function(loopEnd, start, end, index, zindex){
   			var crossFade = this;
   			for(var i = crossFade.currentIndex; i > loopEnd; i--){
   				var ix = (crossFade.jsonData.length === crossFade.currentIndex) ? 0 : i
   				crossFade.onCrossFadePrev(ix, crossFade.jsonData[ix], crossFade);
   				dojo.style(crossFade.jsonData[ix].domNode, "zIndex", zindex);
   				crossFade.fadeFx.push(baseFx.animateProperty({
   					node: crossFade.jsonData[ix].domNode,
   					duration: 1000,
   					properties: {
      					opacity: {start: start, end: end} 
  					},
   					onEnd: function(){
   						if (crossFade.fxCrossFadeEnd(this)){
   							crossFade.onCrossFadeEnd(index, crossFade);
   						}
   					}
   				}))
   				crossFade.currentIndex = (crossFade.currentIndex - 1) * crossFade.itemShow;
   			}
   		},
   		
   		createNextFx: function(loopEnd, start, end, index, zindex){
   			var crossFade = this;
   			for(var i = crossFade.currentIndex; i < loopEnd; i++){
   				crossFade.onCrossFadeNext(i, crossFade.jsonData[i], crossFade);
   				dojo.style(crossFade.jsonData[i].domNode, "zIndex", zindex);
   				crossFade.fadeFx.push(baseFx.animateProperty({
   					node: crossFade.jsonData[i].domNode,
   					duration: 1000,
   					properties: {
      					opacity: {start: start, end: end} 
  					},
   					onEnd: function(){
   						if (crossFade.fxCrossFadeEnd(this)){
   							crossFade.onCrossFadeEnd(index, crossFade);
   						}
   					}
   				}))
   				crossFade.currentIndex = (crossFade.currentIndex + 1)  * crossFade.itemShow;
   			}
   		},
   		
		widgetTransition: function(transition, element){
			// summary:
			//		performs a next or previous crossFade fx on given elements.  
   			var crossFade = this;
   			if (crossFade.animating) return;
   			crossFade.inherited(arguments);
   			var controlsIndex = _.indexOf(crossFade.controls, element);
   			crossFade.onCrossFade(element, controlsIndex, crossFade);
   			(controlsIndex === 0) ? crossFade.previous(controlsIndex, element) : crossFade.next(controlsIndex, element);
   			for(var i = 0; i < crossFade.fadeFx.length; i++){
   				crossFade.fadeFx[i].play();
   			}
            var index = (crossFade.currentIndex / crossFade.itemShow)
            index = (crossFade.jsonData.length == index) ? 0 : index;
   			crossFade.onPage(index, null, crossFade);
   		},
   	   		
   		getNextIndexEnd: function(){
   			// summary:
			//		returns the page end index on next cycle.    
   			var transitionPaging = this;
   			if(transitionPaging.currentIndex === transitionPaging.jsonData.length){
   				transitionPaging.currentIndex = 0;
   			}
   			return Math.min(transitionPaging.itemShow + transitionPaging.currentIndex, transitionPaging.jsonData.length);
   		},
   		
   		getPreviousIndexEnd: function(){
   			// summary:
			//		returns the page end index on previous cycle.  
   			var transitionPaging = this;
   			if(transitionPaging.currentIndex === 0){
   				transitionPaging.currentIndex = transitionPaging.jsonData.length;
   			}
   			return Math.max(transitionPaging.currentIndex - transitionPaging.itemShow, 0);
   		},
   		
   		onCrossFade: function(element, index, crossFade){},
   		
   		onCrossFadeEnd: function(index, crossFade){},
   		
   		onCrossFadeNext: function(index, data, crossFade){},
   		
   		onCrossFadePrev: function(index, data, crossFade){},
   		
   		fxCrossFadeEnd: function(fx){
   			var crossFade = this;
   			var index = _.indexOf(crossFade.fadeFx, fx);
   			crossFade.fadeFx.splice(index, 1);
   			if (crossFade.fadeFx.length === 0){
   				crossFade.onWidgetTransitionEnd(crossFade);
   			};
   			return (crossFade.fadeFx.length === 0);
   		}
	})
	
	return tui.widgetFx.CrossFade;
})
