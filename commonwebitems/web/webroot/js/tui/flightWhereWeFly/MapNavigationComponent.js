define("tui/flightWhereWeFly/MapNavigationComponent", [
	"dojo",
	"dojo/on",
	"dojo/dom",
	'dojo/query',
	'dojo/dom-class',
	'dojo/dom-style',
	"dojo/_base/fx",
	"dojo/_base/event"
	], function (dojo, on, dom, query, domClass, domStyle, fx,event) {

	dojo.declare("tui.flightWhereWeFly.MapNavigationComponent",[], {



		postCreate:function(){
			var mapComponent = this,cnt=null;
			mapComponent.inherited(arguments);
		},

		attachEvent : function(){
			var mapComponent = this;

			query(".tabNavigation").on("click", function(evt){
			  	event.stop(evt);
				mapComponent.tabNavigation(evt);
			});

			query("#filterAirports").on("click", function(){
				mapComponent.filterAirports();
			});
		},


		destinationAttachEvent : function(){
			var mapComponent = this;

			query(".scrollButton").on("click", function(evt){
			  	event.stop(evt);
				mapComponent.scrollContent(evt);
			});

			query(".destinations").on("click", function(evt){
				if(evt.target.className !== "chkOpenToggle" && !dojo.hasClass(this,"active")){
					mapComponent.displayAirports(this);
					mapComponent.scrollContent(evt);
				}
			});

			query(".chkOpenToggle").on("click", function(evt){
				if(!dojo.hasClass(this,"active")){
					mapComponent.showAirport(this);
				}
			});

			query(".alldestinations").on("click", function(evt){
				if(!dojo.hasClass(this,"active")){
					mapComponent.showAllAirports(this);
					mapComponent.scrollContent(evt);
				}
			});
		},


		handleScrollButtons:function(){
			var contentHeight = 0,
				container = dojo.marginBox(query("#verticalScroll")[0]),
				containerHeight = container.h;//get the height of container
            query("#verticalScrollContent > li").forEach(function(node){
               var currentNode = dojo.marginBox(node);
               contentHeight += currentNode.h;
            });

            if(contentHeight < containerHeight) {
            	domStyle.set(query(".scrollDown")[0], {
            	"display":"none"
            	});
            }
            else{
            	domStyle.set(query(".scrollDown")[0], {
            	"display":"block"
            	});
            }
		},


		scrollContent: function(evt){
			var contentHeight = 0;
            query("#verticalScrollContent > li").forEach(function(node){
               var currentNode = dojo.marginBox(node);
               contentHeight += currentNode.h;
            });
            // Define and Scroll settings
            var liElm = query("#verticalScrollContent > li"),
                list = dojo.marginBox(liElm[0]),
                container = dojo.marginBox(query("#verticalScroll")[0]),
                containerHeight = container.h,
                slide = 2 * list.h; // Slide length
            // Calculating the top position
            var maxTop = containerHeight - contentHeight-20,
                parentNode = dom.byId("verticalScrollContent"),
                parentNodePos = dojo.marginBox(query("#verticalScrollContent")[0]),
                currentTop = parentNodePos.t,
                calcTop = currentTop - slide,
                newTop = 0;
         // Scroll Down
            if(domClass.contains(evt.currentTarget, "scrollDown")){
	              if(maxTop >= calcTop) {
	            	  newTop = maxTop;
	              }else {
	            	  newTop = currentTop-slide;
	              }
            }else if(domClass.contains(evt.currentTarget, "scrollUp")) { // Scroll Up
              if((currentTop + slide) >= 0 ){
                newTop = 0;
              }
              else {
                newTop = currentTop+slide;
              }
            }else{
            	 newTop = 0;
            }

            // Animating top property
            fx.animateProperty({node: "verticalScrollContent", duration: 500,properties: {top: { start: currentTop, end: newTop }}}).play();
            if(newTop == 0){//scroll up button is not visible if content top is at 0px
            	domStyle.set(query(".scrollUp")[0], {"display":"none"});
            } else {
            	domStyle.set(query(".scrollUp")[0], {"display":"block"});
            }

            if(newTop <= maxTop) {//scroll down button is not visible if content top reaches max
            	domStyle.set(query(".scrollDown")[0], {"display":"none"});
            }
            else{
            	domStyle.set(query(".scrollDown")[0], {"display":"block"});
            }
		},


		tabNavigation : function(evt){
			var mapComponent = this,continent;

				if(dojo.hasClass(evt.target.parentNode,"activeTab")) return false;

				continentTabs =  dojo.query(".continentTabs");
				_.each(continentTabs, function(tab){
					if(dojo.hasClass(tab, "activeTab")){
						dojo.removeClass(tab, "activeTab");
					}
				})
				dojo.addClass(evt.target.parentNode,"activeTab");
				domStyle.set(query(".scrollUp")[0], {"display":"none"});
				continent = _.where( mapComponent.allContinents,  {"name": evt.target.getAttribute("data-continent")});
				mapComponent.allDestinationsRender(continent[0]);
				mapComponent.handleScrollButtons();

				mapComponent.continentMapZoom();

		}

	})
	return tui.flightWhereWeFly.MapNavigationComponent;
});