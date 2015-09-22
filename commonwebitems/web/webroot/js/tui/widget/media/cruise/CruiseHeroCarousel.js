define('tui/widget/media/cruise/CruiseHeroCarousel', [
  'dojo', 
  "dojo/_base/declare",
  "dojo/query",
  "dojo/dom-style",
  'tui/widget/media/HeroCarousel'
  ], function(dojo, declare, query, domStyle) {
	
  return declare('tui.widget.media.cruise.CruiseHeroCarousel', [tui.widget.media.HeroCarousel], {
	  // ----------------------------------------------------------------------------- properties
	  	jsonData: null,
	    index:0,
	    imagedefaultSize: "",
	    // ----------------------------------------------------------------------------- singleton
	    setCarouselData: function () {
	    	var crHeroCarousel = this, imagesize, data;
	        // filter correct image size.
	        imagesize = crHeroCarousel.imagedefaultSize;  
	        data =  crHeroCarousel.jsonData.textPaneDatas != null ? crHeroCarousel.jsonData.textPaneDatas[crHeroCarousel.index].galleryImages :  crHeroCarousel.jsonData.galleryImages;  
	        crHeroCarousel.jsonData["galleryImages"] = data;
	        crHeroCarousel.jsonData.heroImages = _.uniq(_.filter(data , function(item) {
	           return (item.size === imagesize);
	        }), 'code');
	    },

	    destroyRecursive: function() {
		   var heroCarousel = this;
		   heroCarousel.stopAutoplay();

		   dojo.disconnect(heroCarousel.onPageHandle);
		   dojo.disconnect(heroCarousel.onCrossFadeEndHandle);
		   dojo.disconnect(heroCarousel.onCrossFadePrevHandle);
		   dojo.disconnect(heroCarousel.onCrossFadeNextHandle);
		   dojo.disconnect(heroCarousel.onmouseoverHandle);
		   dojo.disconnect(heroCarousel.onmouseleaveHandle);

		   //removing all viewed images...
		   _.each(heroCarousel.jsonData.galleryImages, function(node, index){
			  (index > 0) && (node.domNode)? delete node.domNode : null;
		   });

		   heroCarousel.inherited(arguments);
	    }
  });
});
