define ("tui/widget/booking/utils/BookFlowDynamicPopup", ["dojo",
                                            "dojo/_base/fx",
                                            "dojo/dom",
                                            "dojo/dom-class",
                                            "dojo/has",
                                            "tui/widget/popup/DynamicPopup",
                                            "tui/widget/booking/GetPriceComponent"], function(dojo, fx, dom, domclass, has,dynamicPopup){

	dojo.declare("tui.widget.booking.utils.BookFlowDynamicPopup", [tui.widget.popup.DynamicPopup], {

		stopDefaultOnCancel: false,

		// ---------------------------------------------------------------- methods
		 posElement: function (elementRelativeTo, elementToFloat, where) {
	        	
	            var floatPosition = this;
	         
	            if (!floatPosition.floatWhere) return;
	            switch (arguments.length) {
	                case 1:
	                    elementToFloat = elementRelativeTo;
	                    elementRelativeTo = floatPosition.elementRelativeTo || floatPosition.domNode;
	                    break;
	                case 2:
	                    if (typeof arguments[1] === "string") {
	                        where = elementToFloat;
	                        elementToFloat = elementRelativeTo;
	                        elementRelativeTo = floatPosition.elementRelativeTo || floatPosition.domNode;
	                    }
	                    break;
	            }
	           
	            floatPosition.floatWhere = where || floatPosition.floatWhere;
	            var display = dojo.style(elementToFloat, "display");
	            dojo.style(elementToFloat, "display", "block");

	            var position;
	            
	            if(has("ie")){
	            	
	            position = (elementRelativeTo) ;
	            }
	            else{
	            	 //position = (elementRelativeTo) ? dojo.position(elementRelativeTo, true) : null;
					 if(elementRelativeTo != undefined ){
					  try {
						position = dojo.position(elementRelativeTo, true);
						} catch (err) {
							position = {"h":"0","w":"0","x":"0","y":"0"} ;
						}
					}else{
					  position = null;
					}

	            }

	            var subjectPosition = dojo.position(elementToFloat, true);
	            
	            dojo.style(elementToFloat, "display", display);
	            var windowBox = (has("agent-ios")) ? {w: window.innerWidth, h: window.innerHeight} : dojo.window.getBox();

	            floatPosition.onBeforePosition(subjectPosition, position, windowBox, elementToFloat, elementRelativeTo, floatPosition);
	           
	            var scrollTop = 0, scrollLeft = 0;
	           
	            if (floatPosition.includeScroll) {
	                scrollLeft = (window.pageXOffset !== undefined) ? window.pageXOffset :
	                             (document.documentElement || document.body.parentNode || document.body).scrollLeft;
	                scrollTop = (window.pageYOffset !== undefined) ? window.pageYOffset :
	                            (document.documentElement || document.body.parentNode || document.body).scrollTop;
	            }
	            
	            switch (floatPosition.floatWhere) {

	                case "position-top-center":
	                    dojo.setStyle(elementToFloat, {
	                        "top": (position.y - subjectPosition.h) + floatPosition.posOffset.top + "px",
	                        "left": (position.x + position.w / 2) - (subjectPosition.w / 2) + floatPosition.posOffset.left + "px"
	                    });
	                    break;
	                case "position-top-left":
	                    dojo.setStyle(elementToFloat, {
	                        "top": (position.y - subjectPosition.h) + floatPosition.posOffset.top + "px",
	                        "left": position.x + floatPosition.posOffset.left + "px"
	                    });
	                    break;
	                case "position-top-right":
	                    dojo.setStyle(elementToFloat, {
	                        "top": (position.y - subjectPosition.h) + floatPosition.posOffset.top + "px",
	                        "left": (position.x + position.w) + floatPosition.posOffset.left + "px"
	                    });
	                    break;
	                case "position-bottom-left":
	                    dojo.setStyle(elementToFloat, {
	                        "top": position.y + position.h + floatPosition.posOffset.top + "px",
	                        "left": position.x + floatPosition.posOffset.left + "px"
	                    });
	                    break;
	                case "position-bottom-right":
	                    dojo.setStyle(elementToFloat, {
	                        "top": position.y + position.h + floatPosition.posOffset.top + "px",
	                        "left": (position.x + position.w) + floatPosition.posOffset.left + "px"
	                    });
	                    break;
	                case "position-bottom-center":
	                    dojo.setStyle(elementToFloat, {
	                        "top": (position.y + position.h) + floatPosition.posOffset.top + "px",
	                        "left": (position.x + position.w / 2) - (subjectPosition.w / 2) + floatPosition.posOffset.left + "px"
	                    });
	                    break;
	                case "position-center":
	                	
	                    dojo.setStyle(elementToFloat, {
	                        "top": ((windowBox.h / 2) - (subjectPosition.h / 2) + floatPosition.posOffset.top) + scrollTop + "px",
	                        "left": ((windowBox.w / 2) - (subjectPosition.w / 2) + floatPosition.posOffset.left) + scrollLeft + "px"
	                    });
	                    break;
	                case "position-center-top":
	                    dojo.setStyle(elementToFloat, {
	                        "top": ((windowBox.h / 2) - (subjectPosition.h / 2) + floatPosition.posOffset.top) + scrollTop + "px"
	                    });
	                    break;
	                case "position-over":
	                    dojo.setStyle(elementToFloat, {
	                        "top": position.y + "px",
	                        "left": position.x + "px"
	                    });
	                    break;
	                case "position-right-center":
	                    dojo.setStyle(elementToFloat, {
	                        "top": (position.y - (subjectPosition.h / 2) + floatPosition.posOffset.top) + scrollTop + "px",
	                        "left": (position.x + position.w) + floatPosition.posOffset.left + "px"
	                    });
	                    break;
					case "position-right-center-far":
	                    dojo.setStyle(elementToFloat, {
	                        "top": (position.y - (subjectPosition.h / 2) + floatPosition.posOffset.top) + scrollTop + "px",
	                        "left": (position.x + position.w) + floatPosition.posOffset.left + "px"
	                    });
	                    break;
					 case "position-center-insurance":
		                	
		                    dojo.setStyle(elementToFloat, {
		                        "top": ((windowBox.h / 2) - (300) + floatPosition.posOffset.top) + scrollTop + "px",
		                        "left": ((windowBox.w / 2) - (subjectPosition.w / 2) + floatPosition.posOffset.left) + scrollLeft + "px"
		                    });
	            	
	            }

	            floatPosition.onAfterPosition(subjectPosition, position, windowBox, floatPosition);
	        }

	})

	return tui.widget.booking.utils.BookFlowDynamicPopup;
})