define("tui/widget/expand/cruise/CruiseMapMenuExpandable", [
  "dojo",
  "dojo/_base/declare",
  "dojo/query",
  "dojo/on",
  "dojo/dom-class",
  "dojo/dom-attr",
  "dojo/dom-style",
  "dojo/fx",
  "dojo/mouse",
  "tui/searchResults/view/Tooltips",
  "tui/widget/maps/cruise/CruiseAreasMapScroller",
  "tui/widget/maps/cruise/CruiseBrowseResults",
  "tui/widget/expand/FilterExpandable",
  "tui/search/nls/Searchi18nable",
  'tui/widget/mixins/Templatable'
], function (dojo, declare,  query, on, domClass, domAttr, domStyle, fx, mouse) {

  return declare("tui.widget.expand.cruise.CruiseMapMenuExpandable", [tui.widget.expand.FilterExpandable, tui.search.nls.Searchi18nable, tui.widget.mixins.Templatable], {

      setHeight: 0,

      crMapScroll: null,

      crResults: null,

      vScrollInd: true,

      nodeIndex: '',

      itemSelector: ".item",

      timer: [],

      subscribableMethods: ["expandMapMenu", "collapseMapMenu"],

      maps:[],

  	  postCreate: function(){
		var CRMExpandable = this, urlParams ;
		CRMExpandable.getParent().crMExpandable = CRMExpandable;
		CRMExpandable.inherited(arguments);
	  },

      attachEvents: function () {
          var CRMExpandable = this, parent, content, state, setHeight = 0, setSpeed = false;
          on(CRMExpandable.domNode, on.selector(CRMExpandable.targetSelector, "click"), function (event) {
        	  // fetch the Index of the Node which is in Open state
              _.each(CRMExpandable.expandableItems,function (item, i) {
                  content = query(CRMExpandable.itemContentSelector, item)[0];
                  state = domAttr.get(content,'data-toggle-state');
                  if ( state === 'open') {
                      CRMExpandable.nodeIndex = domAttr.get( content, 'data-index')+"";
                  }
              });
              // If fetched node Index and current target node Index are equal then stop toggling else toggle
              if(CRMExpandable.nodeIndex !== domAttr.get(event.target, 'data-index')+"" )  {
            	  parent = query(event.target).parents(CRMExpandable.itemSelector)[0];
            	  CRMExpandable.invokeToggleEvent(parent);
              } else{
            	  return true;
              }
          });
          if(CRMExpandable.data.destLandingMap) {
        	  var circlesList = query(".map-circle",  CRMExpandable.getParent().cruiseAreaStaticMap.domNode);
        	  CRMExpandable.maps = Array(circlesList.length);
        	  _.each(circlesList, function(obj, ind){
        		  CRMExpandable.maps[dojo.getAttr(obj, "data-index")-1] = obj;
        	  });

          }
          //Mouse enter event
          on(CRMExpandable.domNode, on.selector("span.area-name", mouse.enter), function(e){
        	  CRMExpandable.expandMapMenu(e.target);
          });
          //Mouse leave event
          on(CRMExpandable.domNode, on.selector("span.area-name", mouse.leave), function(e){
        	  CRMExpandable.collapseMapMenu(e.target);
            });
      },

      expandMapMenu: function(target){
    	  var CRMExpandable = this;
    	  var index = parseInt(domAttr.get(target, "data-index"));
    	  if(index > 0 ) {
    		  if(CRMExpandable.data.destLandingMap) {
    			  domClass["add"](CRMExpandable.maps[index-1], "list-"+index+"-selected");
    		  }
        	  //parent = query(target).parents(CRMExpandable.itemSelector)[0];
              //content = query(CRMExpandable.itemContentSelector, parent)[0];
              var arr = CRMExpandable.parentAndContent(target);
              var parent = arr[0];
              var content = arr[1];

              domStyle.set(CRMExpandable.crMapScroll.viewport, "height", "428px");
           	  setHeight = CRMExpandable.calcHeight(content);
              setSpeed = CRMExpandable.calcSpeed(setHeight);
              domStyle.set(content, "maxHeight", _.pixels(setHeight));
    	  }
      },

      collapseMapMenu: function(target){
    	  var CRMExpandable = this;
    	  var index = parseInt(domAttr.get(target, "data-index"));
    	  if(index > 0 ) {
    		  if(CRMExpandable.data.destLandingMap) {
    			  domClass["remove"](CRMExpandable.maps[index-1], "list-"+index+"-selected");
    		  }
        	  //parent = query(target).parents(CRMExpandable.itemSelector)[0];
              //content = query(CRMExpandable.itemContentSelector, parent)[0];
              var arr = CRMExpandable.parentAndContent(target);
              var parent = arr[0];
              var content = arr[1];
              state = domAttr.get(content, 'data-toggle-state');
              if(state !== 'open'){
            	  domStyle.set(content, "maxHeight", _.pixels(0));
              }
    	  }
      },

      parentAndContent:function(target){
          //var CRMExpandable = this;
          var arr = [];

          var parent = _.find(query(target).parents(), function(node){
              //return (node.className == 'item') ;
              return domClass.contains(node,'item');
              //return dojo.hasClass(node, CRMExpandable.itemSelector);
          });
          arr[0] = parent;
          var content = _.find(parent.childNodes, function(node){
              //return (node.className == 'item-content') ;
              return domClass.contains(node,'item-content')
              //dojo.hasClass(node, CRMExpandable.itemContentSelector)

          });
          arr[1] = content;
          return arr;
      },

      invokeToggleEvent: function (parent) {
 		 var CRMExpandable = this, content, state;
 		 content = query(CRMExpandable.itemContentSelector, parent)[0];
 		 state = domAttr.get(content, 'data-toggle-state');
 		 if ( state !== 'open' || state == "" ) {
 			 // open it
 			 CRMExpandable.toggleOpen(content, parent, 'open');
 		 } else {
 			 // close it
 			 CRMExpandable.toggleOpen(content, parent, '');
 		 }
 	},

      onAfterToggle: function (domNode, state) {
    	  var CRMExpandable = this;
    	  if(CRMExpandable.data.destLandingMap) {
    	  	// Index of the current expanded node
    	    var index = CRMExpandable.getParent().cruiseAreaMap.cruiseAreaIndex;
            var iteLinkNode = query("div.itinerary-link",CRMExpandable.getParent().domNode)[0];
            var iteInfoNode = query("div.itinerary-info",CRMExpandable.getParent().domNode )[0];

            on(iteLinkNode, "click", function(e){
               	window.scroll(0,750);
            });

            var parent = query(domNode).parents(CRMExpandable.itemSelector)[0];
            if(index >=0 ) {
            /* flag is true for Itinerary link and false for Itinerary Info node.
             * Accordingly it will set the positions of the nodes.*/
          	  CRMExpandable.playAnimation(iteLinkNode, -27, 825, index, true);
          	  var scrolledTop = CRMExpandable.crMapScroll.viewport.scrollTop;
	          	CRMExpandable.timer.push(setTimeout (function(){
	          		CRMExpandable.playAnimation(iteInfoNode, parent.offsetTop+1-scrolledTop, 201, index, false);
	            }, 1000));
	         	CRMExpandable.timer.push(setTimeout (function(){
	          		 CRMExpandable.playAnimation(iteInfoNode, parent.offsetTop+1-scrolledTop, 0, index, false);
	            }, 3000));
	         	CRMExpandable.timer.push(setTimeout (function(){
	          		 domStyle.set(iteInfoNode, "display", "none")
	            }, 3040));
            }else{
            	 // Default State
	        	 domStyle.set(iteLinkNode, "display","none");
	        	 domStyle.set(iteInfoNode, "display","none");
            }
    	  }
    	  // Show's scroll Indicator if required
    	  dojo.publish("tui/widget/maps/cruise/CruiseAreasMapScroller/afterToggle");
     },

     /**
      * Node to slide, top units, left units, index, flag
      * Slides the node w.r.t top and left units.
      */
	 playAnimation: function (slideNode, tUnits, lUnits, index, flag) {
    	 var CRMExpandable = this;
    	 var liNode = query("li.cruise-area",CRMExpandable.domNode )[index+1];
    	 if(!flag){
    		 domStyle.set(slideNode, slideNode.offsetTop, tUnits);
    	 }
    	 domStyle.set(slideNode, "display", "block")
    	 var left = flag ? "837px": "0px";    // setting the left units for Itinerary Link before the animation
    	 var top = flag ? "0px": _.pixels(tUnits); // setting the top units for Itinerary Info bar before the animation
    	 var slideArgs = {
  		        node: slideNode,  top: tUnits, left: lUnits,
  		        beforeBegin: function(){
					dojo.style(slideNode, { left:left, top:top});
				},
  		        unit: "px"
  		 };
    	 fx.slideTo(slideArgs).play();
	},

    onBeforeToggle: function (domNode, state) {
      var CRMExpandable = this;
      _.each(CRMExpandable.expandableItems, function (item, i) {
          var content = query(CRMExpandable.itemContentSelector, item)[0];
          var state = domAttr.get(content,'data-toggle-state')
          if(state !== null && state === "open"){
              domClass['remove'](item, 'open');
              domClass['remove'](content, 'open');
              domAttr.set(content, 'data-toggle-state', "");
              domStyle.set(content, "maxHeight", _.pixels(CRMExpandable.setHeight));
          }else{
             return true;
          }
      });

      if(CRMExpandable.timer.length > 0 ){
    	  _.each(CRMExpandable.timer, function(timer){
        	  clearInterval(timer);
          });
      }
    }
  });
});