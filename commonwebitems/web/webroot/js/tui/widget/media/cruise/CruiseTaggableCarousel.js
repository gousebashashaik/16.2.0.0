define('tui/widget/media/cruise/CruiseTaggableCarousel', [
  'dojo',
  'tui/widget/carousels/Carousel'
  
], function(dojo, taggable) {

  dojo.declare('tui.widget.media.cruise.CruiseTaggableCarousel', [tui.widget.carousels.Carousel], {

    // ----------------------------------------------------------------------------- methods

		postCreate: function() {
		  var carousel = this;
		  carousel.inherited(arguments);
		},
	
		tagCarousel: function () {
			var carousel = this;
		
			_.each(dojo.query('ul.plist li', carousel.domNode), function(listNode, indx){
				var linkNode = dojo.query('h4 a', listNode);
				if(!linkNode.length) return;
				dojo.setAttr(listNode, "analytics-instance", indx+1);
				carousel.tagElement(listNode, linkNode[0].innerHTML);
			});
			
			
			// Tag next/prev arrows.
			carousel.tagElements(dojo.query('a.prev', carousel.domNode), 'leftNav');
			carousel.tagElements(dojo.query('a.next', carousel.domNode), 'rightNav');
			if (carousel.displayPagination) {
				// Tag paging bullets (must come first).
				var paging = 0;
				carousel.tagElements(dojo.query('ul.paging li', carousel.domNode), function (DOMElement) {
					return 'tab' + ++paging;
				});
			}
		}
  });

  return tui.widget.media.cruise.CruiseTaggableCarousel
  });