define ("tui/widget/parallaxCarousel", ['dojo/_base/fx',
                                        'dojo/fx',
                                        'dojo/dom',
                                        'dojo/fx/easing',
                                        'dojo/window',
                                        'dojo/on',
                                        'dojo/dom-class',
                                        'dojo/domReady!'], function(baseFx, fx, dom, easing, win, on, domClass){

	dojo.declare("tui.widget.parallaxCarousel",  [tui.widget._TuiBaseWidget], {

        currentIndex:1,
	    nextIndex:2,
	    previousIndex:0,
	    currentNode:null,
	    previousNode:null,
	    nextNode:null,
	    imgsToScroll : 1,
	    nextCombAnim: null,
	    previousCombAnim: null,
	    carosalImgPos: {},
	    prevClicked: 'N',
	    nextClicked: 'N',
	    bulletClicked: 'N',
	    duration: 1000,
	    spanedDuration: 1000,
	    easing: 'linear',
	    inOutEasing: 'expoInOut',
	    xPosition: 0,

	    goLeft: function(){
	    	var widget = this;
	    	if( dojo.query(".parallax-carousel.animating").length ){
				widget.prevClicked = 'Y';
				widget.nextClicked = 'N';
				widget.bulletClicked = 'N';
				return;
			}
			widget.imgsToScroll = 1;
			widget.spanedDuration =  widget.duration;

			//Last Clicked is served
	    	widget.prevClicked = 'N';
	    	widget.nextClicked = 'N';
			widget.bulletClicked = 'N';
			widget.previous();
	    },

	    goRight: function() {
	    	var widget = this;
	    	//checking, while animation is progressing, anyone clicked on the "next" link
			if( dojo.query(".parallax-carousel.animating").length ){
				widget.nextClicked = 'Y';
				widget.bulletClicked = 'N';
				widget.prevClicked = 'N';
				return;
			}
			widget.imgsToScroll = 1;
			widget.spanedDuration =  widget.duration;

			//Last Clicked is served
	    	widget.nextClicked = 'N';
	    	widget.bulletClicked = 'N';
			widget.prevClicked = 'N';
			widget.next();
	    },

		postCreate: function() {
			var widget = this,
				imageContainer = null;

			dojo.connect(dojo.byId("goLeft"),"onclick", function(){
				 widget.goLeft();
			});

			dojo.connect(dojo.byId("goRight"),"onclick", function(){
				widget.goRight();
			});

			// Dot's Functionality.
			var bullULClass = "xxy";
			on(dojo.byId("bullUL"), on.selector("li a", "click"), function(){

				var currActBullAnch = dojo.query(".parallax-carousel .ulbullets li a.clicked")[0];
				if(currActBullAnch){
					dojo.removeClass(currActBullAnch, "clicked");
				}

				dojo.addClass(this, "clicked");
				//checking, while animation is progressing, anyone clicked on the "next" link
				if( dojo.query(".parallax-carousel.animating").length ){
					widget.bulletClicked = 'Y';
					widget.prevClicked = 'N';
					widget.nextClicked = 'N';
					return;
				}

				widget.bulletClicked = 'N';
				widget.prevClicked = 'N';
				widget.nextClicked = 'N';
				widget.calculateImgToScroll();
			});

			widget.carosalImgPos.currLiProp = widget.getNodeProp("current");
			widget.carosalImgPos.nextLiProp = widget.getNodeProp("next");
			widget.carosalImgPos.previousLiProp = widget.getNodeProp("previous");
			widget.carosalImgPos.liProp = widget.getNodeProp("leftout");

			widget.currentNode=dojo.query(".homeGalleryCarousel li.current")[0];
			widget.nextNode=dojo.query(".homeGalleryCarousel li.next")[0];
			widget.previousNode=dojo.query(".homeGalleryCarousel li.previous")[0];
			widget.leftout=dojo.query(".homeGalleryCarousel li.leftout")[0];

			// Events for handling image swipe events on iPad
			imageContainer = _.first(dojo.query("ul", widget.domNode));

			imageContainer.addEventListener('touchstart', function(e){
				widget.xPosition =  e.touches[0].pageX;
			} ,false);

			imageContainer.addEventListener('touchend', function(e){
				var touchEndPoint =  e.changedTouches[0].pageX;
				widget.imgsToScroll = 1;
				if(Math.abs(touchEndPoint - widget.xPosition) > 50) {
					 if(touchEndPoint > widget.xPosition) {
						 widget.goLeft();
					 } else {
					     widget.goRight();
					 }
				}
			} ,false);

			imageContainer.addEventListener("mousedown", function(event){
				widget.mouseMoved = false;
			    widget.xPosition = event.clientX;
			    event.preventDefault();
			}, false);
			imageContainer.addEventListener("mousemove", function(){
				widget.mouseMoved = true;
			}, false);
			imageContainer.addEventListener("mouseup", function(event){
				var mouseEndPoint = event.clientX;
				if(widget.mouseMoved){
					if(Math.abs(mouseEndPoint - widget.xPosition) > 50){
						if(mouseEndPoint > widget.xPosition) {
							 widget.goLeft();
						 } else {
						     widget.goRight();
						 }
					}
				}
			}, false);
	    },

	    calculateImgToScroll: function(){

	    	var widget = this;

	    	//Find out clicked bullet index
			var currBullIndx = parseInt(dojo.getAttr(dojo.query(".parallax-carousel .ulbullets li a.clicked")[0], "data-index"));
			var actBullIndx = parseInt(dojo.getAttr(dojo.query(".parallax-carousel .ulbullets li.active a")[0]  , "data-index"));

			//widget.previousCombAnim ? widget.previousCombAnim.stop() : "";
			//widget.nextCombAnim ? widget.nextCombAnim.stop() : "";

			if(actBullIndx < currBullIndx){
				widget.imgsToScroll = currBullIndx - actBullIndx;
				widget.spanedDuration =  parseInt(widget.duration/widget.imgsToScroll);
				widget.next();
			}else if(actBullIndx > currBullIndx){
				widget.imgsToScroll = Math.abs(actBullIndx - currBullIndx);
				widget.spanedDuration =  parseInt(widget.duration/widget.imgsToScroll);
				widget.previous();
			}
	    },

	    getNodeProp:function(cls) {
	    	var prop = {};
	    	//var obj = typeof cls == "object" ? cls : dojo.query("#homeGalleryCarousel li." + cls)[0];
	    	var obj = dojo.query("#homeGalleryCarousel li." + cls)[0];
	    	prop.width = dojo.getStyle(obj, "width");
	    	prop.height = dojo.getStyle(obj, "height");
	    	prop.left = dojo.getStyle(obj, "left");
	    	prop.top = dojo.getStyle(obj, "top");
	    	return prop;
	    },

	    next:function() {
	    	var widget = this;
	    	var duration = widget.spanedDuration; ///widget.imgsToScroll; //How long in milliseconds the animation should take to play. Default 350 miliSec
	    	var es = widget.imgsToScroll == 1 ? easing[widget.inOutEasing] : easing[widget.easing];
	    	var nxtLi = dojo.query(widget.nextNode).next()[0];
        	nxtLi = nxtLi ? nxtLi : dojo.query("#homeGalleryCarousel li")[0];

        	dojo.setStyle(widget.nextNode, "zIndex", "3");
        	dojo.setStyle(widget.currentNode, "zIndex", "2");
        	dojo.setStyle(widget.previousNode, "zIndex", "1");

	    	var nextToCurrent = baseFx.animateProperty({
	            node: widget.nextNode,
	            duration: duration,
	            easing: es,
	            properties: {
	            	width:widget.carosalImgPos.currLiProp.width,
	      		    height:widget.carosalImgPos.currLiProp.height,
	      		    left:widget.carosalImgPos.currLiProp.left,
	      		    top:widget.carosalImgPos.currLiProp.top
	            }
	    	});

	    	var currentToPrevious = baseFx.animateProperty({
	            node: widget.currentNode,
	            duration: duration,
	            easing: es,
	            properties: {
	            	width:widget.carosalImgPos.previousLiProp.width,
	      		    height:widget.carosalImgPos.previousLiProp.height,
	      		    left:widget.carosalImgPos.previousLiProp.left,
	      		    top: widget.carosalImgPos.previousLiProp.top
	            }
	    	});

	    	var previousToRoot = baseFx.animateProperty({
	            node: widget.previousNode,
	            duration: duration,
	            easing: es,
	            properties: {
	            	width:widget.carosalImgPos.liProp.width,
	      		    height:widget.carosalImgPos.liProp.height,
	      		    left:widget.carosalImgPos.liProp.left,
	      		    top: widget.carosalImgPos.liProp.top
	            }

	    	});

	    	var rootToNext = baseFx.animateProperty({
	            node: nxtLi,
	            duration: duration,
	            easing: es,
	            properties: {
	            	width:widget.carosalImgPos.nextLiProp.width,
	      		    height:widget.carosalImgPos.nextLiProp.height,
	      		    left:widget.carosalImgPos.nextLiProp.left,
	      		    top: widget.carosalImgPos.nextLiProp.top
	            }
	    	});

	    	widget.nextCombAnim = fx.combine([nextToCurrent,currentToPrevious, previousToRoot, rootToNext]).play();

	    	//add a class for intimating that the animation is in progress
	    	dojo.addClass(dojo.query(".parallax-carousel")[0],"animating");


	    	dojo.connect(widget.nextCombAnim, "onEnd", function(){

	    		//Removing classes
	    		dojo.removeClass(widget.previousNode, "previous");
	    		dojo.removeClass(widget.currentNode, "current");
	    		dojo.removeClass(widget.nextNode, "next");
	    		dojo.removeClass(widget.leftout, "leftout");

	    		//add classes
            	dojo.addClass(widget.nextNode, "current");
            	dojo.addClass(widget.currentNode, "previous");

            	//interchange objects
            	widget.leftout = widget.previousNode;
            	widget.previousNode = widget.currentNode;
            	widget.currentNode = widget.nextNode;
            	widget.nextNode = nxtLi;

            	//add classes
            	dojo.addClass(widget.leftout, "leftout");
            	dojo.addClass(widget.nextNode, "next");

            	var currImgIndx = parseInt(dojo.getAttr(widget.currentNode, "data-index")) + 1;
            	var prevBullLi = dojo.query(".parallax-carousel .ulbullets li.active")[0];
            	var currBullLi = dojo.query(".parallax-carousel .ulbullets li:nth-child("+ currImgIndx +")")[0];

            	dojo.removeClass(prevBullLi, "active");
            	dojo.addClass(currBullLi, "active");
            	dojo.removeClass(dojo.query(".parallax-carousel")[0],"animating");

            	dojo.setStyle(widget.nextNode, "zIndex", "2");
            	dojo.setStyle(widget.currentNode, "zIndex", "3");
            	dojo.setStyle(widget.previousNode, "zIndex", "1");

            	--widget.imgsToScroll;

            	//If anyone clicked "next" link, while animation in progress, we will reset the "imgsToScroll" to 1
            	// Scenario will work for both animation from 'bullets' and 'next' links
            	if(  widget.nextClicked == "Y" ){
            		widget.nextClicked = "N";
            		widget.imgsToScroll = 1;
            		widget.spanedDuration =  widget.duration;
            	}

            	//while running the animation, bullet was re-pressed, causing resetting of "imgsToScroll"
            	if(  widget.bulletClicked == "Y" ){
            		widget.bulletClicked = "N";
            		widget.calculateImgToScroll();
            		return;
            	}

            	if(widget.imgsToScroll){
            		widget.next();
            	}

	    	});


	    },
	    previous: function() {
	    	var widget = this;
	    	var duration = widget.spanedDuration;
	    	var es = widget.imgsToScroll == 1 ? easing[widget.inOutEasing] : easing[widget.easing];

	    	var prevLi = dojo.query(widget.previousNode).prev()[0];
	    	prevLi = prevLi ? prevLi : dojo.query("#homeGalleryCarousel li:last-child")[0];

	    	dojo.setStyle(widget.nextNode, "zIndex", "1");
        	dojo.setStyle(widget.currentNode, "zIndex", "2");
        	dojo.setStyle(widget.previousNode, "zIndex", "3");

	    	var nextToRoot = baseFx.animateProperty({
	            node: widget.nextNode,
	            duration: duration,
	            easing: es,
	            properties: {
	            	width: widget.carosalImgPos.liProp.width,
	      		    height: widget.carosalImgPos.liProp.height,
	      		    left: widget.carosalImgPos.liProp.left,
	      		    top: widget.carosalImgPos.liProp.top
	            },

	            onEnd: function(){

	              }
	    	});

	    	var currentToNext = baseFx.animateProperty({
	            node: widget.currentNode,
	            duration: duration,
	            easing: es,
	            properties: {
	            	width: widget.carosalImgPos.nextLiProp.width,
	      		    height: widget.carosalImgPos.nextLiProp.height,
	      		    left: widget.carosalImgPos.nextLiProp.left,
	      		    top: widget.carosalImgPos.nextLiProp.top
	            }
	    	});

	    	var previousToCurrent = baseFx.animateProperty({
	            node: widget.previousNode,
	            duration: duration,
	            easing: es,
	            properties: {
	            	width: widget.carosalImgPos.currLiProp.width,
	      		    height: widget.carosalImgPos.currLiProp.height,
	      		    left: widget.carosalImgPos.currLiProp.left,
	      		    top: widget.carosalImgPos.currLiProp.top
	            }
	    	});

	    	var rootToPrevious = baseFx.animateProperty({
	            node: prevLi,
	            duration: duration,
	            easing: es,
	            properties: {
	            	width: widget.carosalImgPos.previousLiProp.width,
	      		    height: widget.carosalImgPos.previousLiProp.height,
	      		    left: widget.carosalImgPos.previousLiProp.left,
	      		    top: widget.carosalImgPos.previousLiProp.top
	            }
	    	});

	    	widget.previousCombAnim = fx.combine([nextToRoot,currentToNext, previousToCurrent, rootToPrevious]).play();

	    	//add a class for intimating that the animation is in progress
	    	dojo.addClass(dojo.query(".parallax-carousel")[0],"animating");

	    	dojo.connect(widget.previousCombAnim, "onEnd", function(){

	    		//Removing classes
	    		dojo.removeClass(widget.previousNode, "previous");
	    		dojo.removeClass(widget.currentNode, "current");
	    		dojo.removeClass(widget.nextNode, "next");
	    		dojo.removeClass(widget.leftout, "leftout");

	    		//add classes
            	dojo.addClass(widget.previousNode, "current");
            	dojo.addClass(widget.currentNode, "next");

            	//interchange objects
            	widget.leftout = widget.nextNode;
            	widget.nextNode = widget.currentNode;
            	widget.currentNode = widget.previousNode;
            	widget.previousNode = prevLi;

            	//add classes
            	dojo.addClass(widget.leftout, "leftout");
            	dojo.addClass(widget.previousNode, "previous");

            	var currImgIndx = parseInt(dojo.getAttr(widget.currentNode, "data-index")) + 1;
            	var prevBullLi = dojo.query(".parallax-carousel .ulbullets li.active")[0];
            	var currBullLi = dojo.query(".parallax-carousel .ulbullets li:nth-child("+ currImgIndx +")")[0];

            	dojo.removeClass(prevBullLi, "active");
            	dojo.addClass(currBullLi, "active");
            	dojo.removeClass(dojo.query(".parallax-carousel")[0],"animating");

            	dojo.setStyle(widget.nextNode, "zIndex", "1");
            	dojo.setStyle(widget.currentNode, "zIndex", "3");
            	dojo.setStyle(widget.previousNode, "zIndex", "2");

            	--widget.imgsToScroll;

            	//If anyone clicked "previous" link, while animation in progress, we will reset the "imgsToScroll" to 1
            	// Scenario will work for both animation from 'bullets' and 'previous' links
            	if(  widget.prevClicked == "Y" ){
            		widget.prevClicked = "N";
            		widget.imgsToScroll = 1;
            		widget.spanedDuration =  widget.duration;
            	}

            	//while running the animation, bullet was re-pressed, causing resetting of "imgsToScroll"
            	if(  widget.bulletClicked == "Y" ){
            		widget.bulletClicked = "N";
            		widget.calculateImgToScroll();
            		return;
            	}


            	if(widget.imgsToScroll){
            		widget.previous();
            	}

	    	});

	    }
	});

	return tui.widget.parallaxCarousel;
});
