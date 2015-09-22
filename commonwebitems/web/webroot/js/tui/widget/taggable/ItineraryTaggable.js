define('tui/widget/taggable/ItineraryTaggable', [
  'dojo/_base/declare',
  'dojo/query',
  'tui/widget/mixins/Taggable'
], function(declare, query) {

  return declare('tui.widget.taggable.ItineraryTaggable', [tui.widget._TuiBaseWidget],{
	  
	    postCreate: function () {
	    	var itinerary = this;
	    	itinerary.inherited(arguments);
	    	_.each(query("a", itinerary.domNode), function(anch, ind){
	    		if(anch.href.indexOf("javascript:") == -1 && anch.href.indexOf("#") != 0 ){
	    			itinerary.tagElement(anch);
	    		}
	    	});
	    	
	    }
  });

});