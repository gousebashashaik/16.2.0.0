define('tui/widget/taggable/GalleryViewTwinMediaAnalytics', [
  'dojo',
  "dojo/dom-class",
  'tui/widget/mixins/Taggable',
  "dojo/NodeList-dom"
], function(dojo, domClass, taggable) {

  dojo.declare('tui.widget.taggable.GalleryViewTwinMediaAnalytics', [tui.widget._TuiBaseWidget], {

    // ----------------------------------------------------------------------------- methods

    postCreate: function() {
      var twinMedia = this;
      	  twinMedia.inherited(arguments);
    	  twinMedia.attachTag();
	      var photoElem = dojo.query(".photo", twinMedia.domNode)[0];
	      if(photoElem){ twinMedia.tagElement(photoElem, "GVTwinPhotos"); }  
	      
	      var videoElem = dojo.query(".video", twinMedia.domNode)[0];
	      if(videoElem){ twinMedia.tagElement(videoElem, "GVTwinVideo");  }  
	      
	      var shortlistElem = dojo.query(".gallery-view-buttons .shortlist", twinMedia.domNode)[0];
	      if(shortlistElem){ twinMedia.tagElement(shortlistElem, "GVTwinSaveShortlist");  }  
	      
	      
	      var moreDetElem = dojo.query(".gallery-view-buttons .url", twinMedia.domNode)[0];
	      if(moreDetElem){ twinMedia.tagElement(moreDetElem, "GVTwinMoreDetails");  }  

    }
  });

  return tui.widget.taggable.GalleryViewTwinMediaAnalytics;
});
