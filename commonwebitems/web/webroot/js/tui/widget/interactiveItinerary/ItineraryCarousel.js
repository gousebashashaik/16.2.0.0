define('tui/widget/interactiveItinerary/ItineraryCarousel', [
  'dojo/_base/declare',
  'dojo/query',
  'dojo/topic',
  'dojo/on',
  'dojo/dom-construct',
  'dojo/dom-class',
  "dojo/dom-attr",
  'dojo/dom-geometry',
  "dojo/mouse",
  'dojo/text!tui/widget/interactiveItinerary/templates/itinControlTmpl.html',
  'tui/widget/carousels/CSSCarousel',
  "dojo/NodeList-dom"
], function (declare, query, topic, on, domConstruct, domClass, domAttr, domGeom, mouse, controlTmpl) {

  return declare('tui.widget.interactiveItinerary.ItineraryCarousel', [tui.widget.carousels.CSSCarousel], {

    controlTmpl: controlTmpl,

    openSlideTmpl: '<div class="js-open-slide open-slide hide"></div>',

    openSlideContainer: null,

    placeControls: null,

    slideHeight: 201,

    slidePadding: 24,

    /**
     * Override base, render controls to different node
     */
    renderControls: function () {
      var carousel = this,
          placeNode = carousel.placeControls ? query(carousel.placeControls, carousel.domNode)[0] : carousel.domNode,
          html = carousel.renderTmpl(carousel.controlTmpl, {
              controls: [{
                controlClass: 'prev',
                controlText: 'previous'
              },{
                controlClass: 'next',
                controlText: 'next'
              }]
          });
      domConstruct.place(html, placeNode, 'last');
    },

    onBeforePage: function () {
    	 var carousel = this;
      // close opened expandables on page
      topic.publish('tui.widget.interactiveItinerary.ItineraryExpandable.toggleOpen', '');
      query("ul.plist li", carousel.domNode).removeClass("hideMe");
  		query("ul.plist li", carousel.domNode).removeClass("showMe");
    },

    /**
     * Thoughts: move content of open slide to "openSlideContainer", position it relative to source, and then expand
     * Probably need to rethink this component though as this whole solution is very problematic
     */
    onAfterInitViewport: function () {
      var carousel = this,
          html = carousel.renderTmpl(carousel.openSlideTmpl);
      carousel.openSlideContainer = domConstruct.place(html, carousel.viewport, 'after');
    },

    /**
     * Attaching event handlers
     */
    onAfterInit: function () {
      var carousel = this;
      carousel.onAfterPage();
      carousel.evtListeners['mouseEnter'] = on(query('.expand-product', carousel.domNode), mouse.enter, function (e){
        var target = domClass.contains(e.target, '.expand-product') ? e.target : query(e.target).parents('.expand-product')[0];
        carousel.handleMouseOver(target);
      });
      
      carousel.evtListeners['mouseLeave'] = on(query('.expand-product', carousel.domNode), mouse.leave, function (e) {
        //var target = query('.expand-product', carousel.openSlideContainer)[0];
    	  var target = domClass.contains(e.target, '.expand-product') ? e.target : query(e.target).parents('.expand-product')[0];
        carousel.handleMouseOut(target);
      });
      
      on(dojo.body(), "click", function (e) {
          //var target = query('.expand-product', carousel.openSlideContainer)[0];
    	  var carousel = this, magnifyObj = query('.magnify.product', carousel.domNode)[0];
    	  if(magnifyObj && domClass.contains(magnifyObj, 'mouseOut') )  {
    		  domClass.remove(magnifyObj, 'mouseOut');
    		  domClass.remove(magnifyObj, 'magnify');
    		  if( domClass.contains(query('.expand', magnifyObj)[0], "open" ) ){
    		  on.emit(query('.expand.open', magnifyObj)[0], "click", {
	   	    	    bubbles: true,
	   	    	    cancelable: true
	   	      });
    		  }
    	  }
        });
      
    },
    
    onAfterPage: function(){
    	var carousel = this;
    	
    	setTimeout(function(){
    		var pageNo = query(".paging li a", carousel.domNode).indexOf(query(".paging li a.active", carousel.domNode)[0]);
        	pageNo = Number(pageNo) + 1;
        	var leftToHide = pageNo*3 - 3, rightToHide = pageNo*3;
        	_.each(query("ul.plist li", carousel.domNode), function(li, ind){
        		if( ind < leftToHide ){
        			domClass.add(li, "hideMe");
        		}else
        		if( ind >= rightToHide ){
        			domClass.add(li, "hideMe");
        		}else
        		{
        			domClass.add(li, "showMe");
        		}
        		
        	});
    		
    	}, 500);
    	
    },

    /**
     * Fires when mouse enters product node
     * @param elem
     */
    handleMouseOver: function (elem) {
      var carousel = this, prodEl;
      var  existingItm = query('.magnify', carousel.domNode)[0];
      
      if(existingItm && elem != existingItm  && domClass.contains(existingItm, 'mouseOut') ) {
		  domClass.remove(existingItm, 'mouseOut');
		  domClass.remove(existingItm, 'magnify');
		  if( domClass.contains(query('.expand', existingItm)[0], "open" ) ){
    		  on.emit(query('.expand.open', existingItm)[0], "click", {
	   	    	    bubbles: true,
	   	    	    cancelable: true
	   	      });
    	  }
	  }
      
      domClass.add(elem, 'magnify');
      domClass.remove(elem, 'mouseOut');
      _.each(carousel.slides, function(slide){
        prodEl = query('.product', slide)[0];
        if(prodEl !== elem) {
          domClass.add(prodEl, 'fade');
        }
      });
    },

    /**
     * Fires when mouse leaves product node
     * @param elem
     */
    handleMouseOut: function (elem) {
      var carousel = this;
      domClass.add(query('.magnify.product', carousel.domNode)[0], 'mouseOut');
      _.each(carousel.slides, function(slide){
        domClass.remove(query('.product', slide)[0], 'fade');
        
      });
    }

  });
});