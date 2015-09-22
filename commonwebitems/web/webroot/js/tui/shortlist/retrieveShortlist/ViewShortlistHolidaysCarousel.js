define('tui/shortlist/retrieveShortlist/ViewShortlistHolidaysCarousel', [
  'dojo',
  'dojo/query',
  'dojo/on',
  'dojo/dom-style',
  'dojo/dom-class',
  'dojo/dom-construct',
  'dojo/dom-attr',
  'dojo/topic',
  'tui/widget/_TuiBaseWidget'], function(dojo, query, on, domStyle, domClass, domConstruct, domAttr, topic){
  dojo.declare('tui.shortlist.retrieveShortlist.ViewShortlistHolidaysCarousel', [tui.widget._TuiBaseWidget], {

    postCreate: function(){
      var widget = this;
	  widget.attachNextPrevClickEvents();
      widget.inherited(arguments);
    },
	attachNextPrevClickEvents: function(){
	  var widget = this;
	  clickedNext = 0;
	  clickedPrev = 0;
	  carouselWidgetRef = widget;
	  //Error handling
      if(dojo.query("#shortlist ul.plist li.search-result-item")[0] == undefined || dojo.query("#shortlist .nextButton")[0] == undefined ){
		return;
	  }
	  widget.carouselContainer = dojo.query("#shortlist ul.plist")[0];
      widget.nextButton = dojo.query("#shortlist .nextButton")[0];
      widget.prevButton = dojo.query("#shortlist .prevButton")[0];
      widget.nextButtonSecond = dojo.query("#shortlist .nextButton")[1];
      widget.prevButtonSecond = dojo.query("#shortlist .prevButton")[1];
	  liMargin = parseInt( dojo.query("#shortlist li.search-result-item").style('margin-right')[0],10);
	  if(!liMargin)liMargin=10;
      var noOfHolidays = dojo.query("#shortlist ul.plist li.search-result-item").length;
	  liWidth =  parseInt(dojo.getComputedStyle(dojo.query("#shortlist ul.plist li.search-result-item")[0]).width, 10);
      

      /* Handle swiping */
	  on(widget.carouselContainer,'touchend', function(e){
	        //e.preventDefault();
            //e.stopPropagation();
	  }, false);
	  on(widget.carouselContainer,'touchmove', function(e){
	        //e.preventDefault();
            //e.stopPropagation();
	  }, false);

	  on(widget.carouselContainer,'touchstart', widget.handleSwipeStart, false);
	  on(widget.carouselContainer,'touchmove', widget.handleSwiping, false);

	  var xDown = null;
	  var yDown = null;
	  /* Handle swiping ends here */

	  slidePos = 0;

	  liWidth = liWidth + liMargin;
	  var diff = 180;
	  dojo.addClass(widget.prevButton,"hide");
	  dojo.addClass(widget.prevButtonSecond,"hide");
	  if(noOfHolidays < 4){
		 dojo.addClass( widget.nextButton,"hide");
	  }
	  /* Attach click events to buttons at the bottom */
	  on( widget.nextButtonSecond, "click", function(e){
		  dojo.stopEvent(e);
		  widget.nextButton.click();		 
	  });
	  on( widget.prevButtonSecond, "click", function(e){
		  dojo.stopEvent(e);
		  widget.prevButton.click();		  
	  });
	  /* Attach click events to buttons at the bottom ends here */

	  on( widget.nextButton, "click", function(){
			noOfHolidays = dojo.query("#shortlist ul.plist li.search-result-item").length;
			clickedNext++;
			clickedPrev--;

			slidePos = -liWidth * clickedNext;
			var leftPos = ( noOfHolidays / 2 ) * (-liWidth);

			if( noOfHolidays == 4){
				slidePos = - diff;
				 if(dojoConfig.site == "firstchoice")
				 {
					slidePos = slidePos + 20;
				 }
				widget.handleClickEvents( slidePos, true, false );
			}
			else{
				if(clickedNext == ( noOfHolidays - 4 )){
					 slidePos = slidePos - diff;
					 if(dojoConfig.site == "firstchoice")
					 {
						slidePos = slidePos + 20;
					 } 
					 console.log("slidePos:"+slidePos);
					widget.handleClickEvents( slidePos, true,false);
				}
				else{
					
					widget.handleClickEvents( slidePos,false,false);
				}
			}
	   });
	   on( widget.prevButton,"click", function(){
			noOfHolidays = dojo.query("#shortlist ul.plist li.search-result-item").length;
            clickedNext--;
			clickedPrev++;
			slidePos = slidePos+liWidth;
			if(clickedPrev == 0){
				slidePos = 0;
				widget.handleClickEvents( slidePos, false, true);
			}
			else{
				widget.handleClickEvents( slidePos,false,false);
			}
	   });
		dojo.subscribe("removedHolidayID", function(data){
           setTimeout(function(){		   
				noOfHolidays = dojo.query("#shortlist ul.plist li.search-result-item").length;
				//clickedNext--;
				//clickedPrev++;		
				console.log("noOfHolidays:"+noOfHolidays);
				if(noOfHolidays <= 3 ){
					dojo.addClass( widget.nextButton,"hide");
					dojo.addClass( widget.prevButton,"hide");
					dojo.addClass(widget.nextButtonSecond,"hide");
					dojo.addClass(widget.prevButtonSecond,"hide");
				}
				if(noOfHolidays < 3 ){
					return;
				}
				if(noOfHolidays <= 4 && dojo.hasClass(widget.nextButton,"hide") == false){
					return;
				}
				var removeButtonsIdsArray = data.removeButtonsIdsArray;
				for(var i = 0; i < noOfHolidays; i++){
					dojo.query("#shortlist .remove-unit")[i].setAttribute("data-remove-id",i+1);
				}				
				var lastThreeIds = removeButtonsIdsArray.slice(-3);
				var buttonRemoveId = data.buttonRemoveId;
				
				if(dojo.hasClass(widget.nextButton,"hide") == false){
					if(buttonRemoveId == lastThreeIds[0]){
						//clickedNext--;
						console.log("came in 2");
						//slidePos =0;
						return;
					}
				}
				
				
				console.log("removeButtonsIdsArray: "+removeButtonsIdsArray);
				console.log("lastThreeIds: "+lastThreeIds);
				console.log("buttonRemoveId: "+buttonRemoveId);
				if(lastThreeIds.indexOf(buttonRemoveId) != -1){
					
					if(widget.carouselContainer){
						slidePos = slidePos+liWidth;
						if(slidePos == NaN)slidePos=0;
						console.log("came in pos:"+slidePos);
						if(noOfHolidays == 3){
							clickedNext++;
							clickedPrev--;	
							widget.handleClickEvents( 0,true,true);
						}
						else{
							clickedNext--;
							clickedPrev++;
                            //if(slidePos == -180 || slidePos == -160) //TODO: dynamic values
							if(slidePos < -100 && slidePos > -200 )
							{	
								slidePos = 0;
								clickedNext--;
								widget.handleClickEvents( slidePos,false,true);
								return;
							
							}
							widget.handleClickEvents( slidePos,true,false);
						}			  
					}
				}
		   }, 200);
		});
	},

	handleSwipeStart: function (e) {
        var widget = this;
		xDown = e.touches[0].clientX;
		yDown = e.touches[0].clientY;
    },
	handleSwiping: function (e) {
	    var widget = this;
		if ( ! xDown || ! yDown ) {
			return;
		}

		var xUp = e.touches[0].clientX;
		var yUp = e.touches[0].clientY;

		var xDiff = xDown - xUp;
		var yDiff = yDown - yUp;

		if ( Math.abs( xDiff ) > Math.abs( yDiff ) ) {
			if ( xDiff > 0 ) {
				dojo.stopEvent(e);
				if(dojo.hasClass(carouselWidgetRef.nextButton,"hide") == false){
					carouselWidgetRef.nextButton.click();
				}
			} else {
				dojo.stopEvent(e);
				if(dojo.hasClass(carouselWidgetRef.prevButton,"hide") == false){
					carouselWidgetRef.prevButton.click();
				}
			}
		}
		xDown = null;
		yDown = null;
	},
	handleClickEvents: function(slidePos, hideNextButton, hidePrevButton){

		var widget = this;

		dojo.removeClass(widget.nextButton,"hide");
		dojo.removeClass(widget.prevButton,"hide");
        if(hideNextButton){
        	dojo.addClass(widget.nextButton,"hide");
        }
        if(hidePrevButton){
        	dojo.addClass(widget.prevButton,"hide");
        }
		
		dojo.addClass(widget.nextButtonSecond,"hide");
		dojo.addClass(widget.prevButtonSecond,"hide");
		if(dojo.hasClass(widget.nextButton,"hide") == false){
		  dojo.removeClass(widget.nextButtonSecond,"hide");
		}
		if(dojo.hasClass(widget.prevButton,"hide") == false){
		  dojo.removeClass(widget.prevButtonSecond,"hide");
		}
		if(widget.carouselContainer){
		    if(widget.carouselContainer){
			   dojo.animateProperty({
					node:widget.carouselContainer,
					duration: 600,				
					properties:{marginLeft: slidePos},
					delay: 0
			  }).play();           
			}
		}
		
		
	}
  });
});
