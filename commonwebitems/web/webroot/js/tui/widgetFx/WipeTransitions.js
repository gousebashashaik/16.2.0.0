define("tui/widgetFx/WipeTransitions", ["dojo", "dojo/dom-class", "dojo/_base/fx", "tui/widgetFx/Transition"], function(dojo, domClass, baseFx){
	
	/***********************************************************************************/
   	/* tui.widgetFx.WipeBase														   */
   	/***********************************************************************************/
	dojo.declare("tui.widgetFx.WipeBase", [tui.widgetFx.Transition], {
		
		// summary:
		//		Base class which defines the common behaviour for widget which   
		//		need a wipe-in and wipe-out effect. 
		// @author: Maurice Morgan.
		
		// ---------------------------------------------------------------- wipeBase properties
		
		currentSelected: null,
		
		itemSelector: "",
		
		itemContentSelector: "",
		
		targetSelector: "",
		
		closeOnClick: false,

        stopDefaultOnClick: true,
		
		// ---------------------------------------------------------------- wipeBase methods
		
		attachTransitionEventListeners: function(){
			var wipeBase = this;
    		wipeBase.items = (wipeBase.itemSelector) ? dojo.query(wipeBase.itemSelector, wipeBase.domNode) : dojo.query(wipeBase.domNode);
    		wipeBase.itemContent = dojo.query(wipeBase.itemContentSelector, wipeBase.domNode);
    		wipeBase.items.forEach(function(item, index){
    			var target = dojo.query(wipeBase.targetSelector, item)[0];
    			if (target){
    				var itemContent = wipeBase.itemContent[index];

    				if (_.indexOf(wipeBase.defaultOpen, index + 1) !== -1){
    					wipeBase.onClick(itemContent, item, wipeBase.currentSelected, "wipeIn", target);
    				}
    				wipeBase.transitionconnects.push(dojo.connect(target, "onclick", function(event){
    					if(wipeBase.stopDefaultOnClick) dojo.stopEvent(event);
    					wipeBase.onClick(itemContent, item, wipeBase.currentSelected, "wipeIn", target);
    				}))
    			}
    		})
   		},
     	
   		// ---------------------------------------------------------------- wipeBase events
   			
   		onClick: function(itemContent, item, currentSelected, wipe, target){
			var wipeBase = this;
			wipeBase.widgetTransition(wipeBase, itemContent, item ,wipe, target); 
		}		
   	})
   	
   	
   /***********************************************************************************/
   /* tui.widgetFx.SimpleWipeBase													  */
   /***********************************************************************************/
   dojo.declare("tui.widgetFx.SimpleWipeBase", [tui.widgetFx.WipeBase], {
		widgetTransition: function(transitonRef, itemContent, item, wipe, target){
   			var simpleWipeBase = this;
   			
   			//simpleWipeBase.currentSelected
   			//if (simpleWipeBase.animating) return;
   			
			if ("wipeIn" === wipe) {
				dojo.style(itemContent, "height", 0);
				dojo.addClass(item, "open");
			}
			simpleWipeBase.onWidgetTransition(transitonRef, itemContent, item, wipe, target);
			dojo.fx[wipe]({
				node: itemContent,
				onEnd: function(){
					simpleWipeBase.animating = false;
					if (wipe === 'wipeOut'){
						dojo.removeClass(item, "open");
					} else {
						simpleWipeBase.currentSelected = [itemContent, item];
					}
					simpleWipeBase.onWidgetTransitionEnd(transitonRef, itemContent, item, wipe, target);
				}
			}).play();
   		}		
   	})
   	
   	  
   /***********************************************************************************/
   /* tui.widgetFx.ComplexWipeBase													  */
   /***********************************************************************************/
   dojo.declare("tui.widgetFx.ComplexWipeBase", [tui.widgetFx.WipeBase], {
		widgetTransition: function(transitonRef, itemContent, item, wipe, target, height){
   			var complexWipeBase = this;
			if ("wipeIn" === wipe) {
				dojo.addClass(item, "open");
			}
		
			complexWipeBase.onWidgetTransition(transitonRef, itemContent, item, wipe, target, height);
			baseFx.animateProperty({
				node: itemContent,
				properties: {
      				height: height
  				},
				onEnd: function(){
					complexWipeBase.animating = false;
					if (wipe === 'wipeOut'){
						dojo.removeClass(item, "open");
					} else {
						complexWipeBase.currentSelected = [itemContent, item];
					}
					complexWipeBase.onWidgetTransitionEnd(transitonRef, itemContent, item, wipe, target, height);
				}
			}).play();
   		}		
   	})
   	
   	
   	/***********************************************************************************/
   	/* tui.widgetFx.WipeInClose														   */
   	/***********************************************************************************/
   	dojo.declare("tui.widgetFx.WipeInClose", [tui.widgetFx.SimpleWipeBase], {
		onClick: function(itemContent, item, currentSelected, wipe, target){
			var wipeInClose = this;
			if (wipeInClose.animating) return;
			if ("none" === dojo.style(itemContent, "display") || 
								dojo.style(itemContent, "height") === 0){
				if (currentSelected && this.domNode && !domClass.contains(this.domNode, "roomTypes")){
					if ((arguments[0] !== currentSelected[0])){
    					wipeInClose.widgetTransition(wipeInClose, currentSelected[0], currentSelected[1] , "wipeOut", target); 
    				}
    			} 
    			wipeInClose.inherited(arguments);
			} else {
				if (wipeInClose.closeOnClick){
					arguments[3] = "wipeOut";
					wipeInClose.inherited(arguments);
				}
			}
		}		
   	})
   	
   	
   	/***********************************************************************************/
   	/* tui.widgetFx.WipeInOut														   */
   	/***********************************************************************************/
   	dojo.declare("tui.widgetFx.WipeInOut", [tui.widgetFx.SimpleWipeBase], {
		onClick: function(itemContent, item, currentSelected, wipe, target){
			var wipeInOut = this;
			if (wipeInOut.animating) return;
			arguments[3] = ("none" === dojo.style(itemContent, "display")) ? "wipeIn" : "wipeOut";
			wipeInOut.inherited(arguments); 
		}		
   	})
   	
   	/***********************************************************************************/
   	/* tui.widgetFx.WipeInHeight													   */
   	/***********************************************************************************/
   	dojo.declare("tui.widgetFx.WipeInHeight", [tui.widgetFx.ComplexWipeBase], {
		onClick: function(itemContent, item, currentSelected, wipe, target, height){
			var wipeInHeight = this;
			if (wipeInHeight.animating) return;
			wipeInHeight.widgetTransition(wipeInHeight, itemContent, item ,wipe, target, height); 
		}		
   	})
   	
	return tui.widgetFx.WipeTransitions;
})