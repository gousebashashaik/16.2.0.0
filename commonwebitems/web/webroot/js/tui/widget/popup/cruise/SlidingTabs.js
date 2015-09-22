define("tui/widget/popup/cruise/SlidingTabs", [
	"dojo",
	"dojo/query",
	"dojo/dom-geometry",
	"dojo/dom-style",
	 "tui/widget/media/cruise/CruiseHeroCarousel",
	 "dojo/fx",
	"tui/widget/_TuiBaseWidget",
	"tui/widget/mixins/Tabbable"], function(dojo, query, domGeom, domStyle, HeroCarousel, fx) {

	dojo.declare("tui.widget.popup.cruise.SlidingTabs", [tui.widget._TuiBaseWidget, tui.widget.mixins.Tabbable], {

		// selector for tab headers.
		tabSelector : ".js-tab-trigger",

		heroWidget  : [],

		 // Array containing DOM elements of tab headings.
	    tabHeadings: null,

	    // An Object reference to the current selected tab content.
	    currentTabContent: null,

	    tabStopEvent: true,

	    clicked: false,
	    
	    index: null,
	    
	    imagedefaultSize:"",

		//---------------------------------------------------------------- methods
	    
	    //Need to change the dojo fx slide animation to custom CSS animation. 

		postCreate : function() {
			// summary:
			//		Attach event listeners to widget elements.

			var slidingTabs = this;
			slidingTabs.attachTabbableEventListeners();
		},

		 attachTabbableEventListeners: function () {
		      var slidingTabs = this;
		      slidingTabs.onBeforeTabInit(slidingTabs);
		      slidingTabs.tabHeadings =   dojo.query(slidingTabs.domNode).query(slidingTabs.tabSelector);
		      slidingTabs.tabHeadings.forEach(function (element, index) {
		    	  slidingTabs.activeIndex = (dojo.hasClass(element, "active")) ? index : 0;
		        element.id = (element.id) ? element.id : [slidingTabs.domNode.id , "tab" , index].join(".");
		        var contentId = slidingTabs.parseAnchorHref(element);
		        slidingTabs.connect(element, "onclick", function (event) {
		        	slidingTabs.clicked = true;
		        	slidingTabs.handleTabClick(slidingTabs, event, element);
		        });
		      });
		      // Show default tab content.
		      slidingTabs.tabToShow = slidingTabs.getTabContentFromHref(slidingTabs.tabHeadings[ slidingTabs.activeIndex]);
		      slidingTabs.displayTabContent( slidingTabs.tabToShow);
		    },

		showTab: function (/* DOM element */ tabHeader) {
			var slidingTabs = this;
		      var tabToShow = slidingTabs.getTabContentFromHref(tabHeader);
		      var tabToHide = slidingTabs.currentTabContent;
		      slidingTabs.hideTabContent(slidingTabs.currentTabContent);
		      slidingTabs.displayTabContent(tabToShow);
		      slidingTabs.clicked = false;
		},


		 displayTabContent: function (/* DOM element */ tabToShow) {
			 var slidingTabs = this;
		      if (tabToShow) {
		    	slidingTabs.currentTabContent = tabToShow;
		    	var slideNode = dojo.query("#"+tabToShow.id+"",slidingTabs.domNode )[0];
		    	if( slidingTabs.clicked){
		    	   dojo.style(tabToShow, "display", "block");
		    	   // Index used to set specific excursion data
		    	   // Check if not initialized the carousel atleast once
		    	   slidingTabs.index = tabToShow.id.split("tab")[1];
		    	   var carouselNode = query(".slideshow", tabToShow)[0];
		    	   if(carouselNode != null && carouselNode.id == ""){
		    		   var excursionImages = slidingTabs.jsonData.locationData.excursion[parseInt(slidingTabs.index-2)].galleryImages;
		    		   if(excursionImages.length > 0) {
		    			   slidingTabs.initializeHeroCarousel(tabToShow, carouselNode);
		    		   }
		       		}
		    	}
				//invokes slide animation
		        slidingTabs.slideNodeTo(slideNode, 32, 32);
		        slideNode.id ==='tab1' ? slidingTabs.togglePositionToRelative(slideNode) : null;
					
		    	// sets auto height
		        var height = dojo.position(dojo.query("#"+tabToShow.id+"",slidingTabs.domNode )[0]).h;
			    
		        //dojo.style(slidingTabs.domNode, { height:height+"px"});
		      }
	    },
	    
	    	togglePositionToRelative: function(node){
	    	dojo.style(node, "position", "relative");
	    	dojo.style(node, { top: "0px"});
	    	dojo.style(node, { left: "0px"});
	    	},
			
	    hideTabContent: function (/* DOM element */  tabContent) {
	        var slidingTabs = this;
	        if (tabContent) {
	        	 if( slidingTabs.clicked){
	        		 var slideNode =  dojo.query("#"+tabContent.id+"",slidingTabs.domNode )[0];
	        		 
	        		 if(slideNode.id === "tab1"){
	        			 /* slidingTabs.initialHeroWidget.stopAutoplay();
	        			 if(slidingTabs.heroWidget.length > 0 ){
	        				 slidingTabs.heroWidget.forEach(function(heroWidget, index){
	        					if(index == parseInt(slidingTabs.index-2)){
	        						heroWidget.startAutoplay();
	        					}
	        				 });
	        			 }*/
	        			//invokes slide animation
	        			 slidingTabs.slideNodeTo(slideNode, 32, -650);
	        		 }else{
	        			 /*slidingTabs.initialHeroWidget.startAutoplay();
	        			 if(slidingTabs.heroWidget.length > 0 ){
	        				 slidingTabs.heroWidget.forEach(function(heroWidget){
	        					heroWidget.stopAutoplay();
	        				 });
	        			 }*/
	        			 //invokes slide animation
	        			 slidingTabs.slideNodeTo(slideNode, 32, 650);
	        		 }
		    	  }
				  setTimeout (function(){
					dojo.style(tabContent, "display", "none");
				}, 150);  
	        	 
	        }
	    },
	    
	    slideNodeTo: function (slideNode, tUnits, lUnits) {
	    	 var slidingTabs = this;
	    	 var slideArgs = {
	  		        node: slideNode,  top: tUnits, left: lUnits,
	  		        beforeBegin: function(){
						dojo.style(slideNode, { top: "32px"});
					},
	  		        unit: "px"
	  		 };
	    	 fx.slideTo(slideArgs).play();
		},
	    
	    initializeHeroCarousel: function(tabToShow, carouselNode){
	    	var slidingTabs = this;
	    	 slidingTabs.heroWidget.push(new HeroCarousel({
		            jsonData: slidingTabs.jsonData.locationData.excursion[parseInt(slidingTabs.index-2)],
		            imagedefaultSize: slidingTabs.imagedefaultSize,
		            useCustomSize: true,
		            height:200,
		            width:519
		        }, carouselNode));
	    },
	    
	    destroyOnClose:function(){
	    	var slidingTabs = this;
	    	_.each(slidingTabs.heroWidget, function(heroWidget){
					heroWidget.destroyRecursive();
			});
	    }
	})
	return tui.widget.popup.cruise.SlidingTabs;
});