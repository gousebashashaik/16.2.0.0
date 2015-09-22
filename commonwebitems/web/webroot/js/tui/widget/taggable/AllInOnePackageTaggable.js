define('tui/widget/taggable/AllInOnePackageTaggable', [
  'dojo/_base/declare',
  'dojo/query',
  'tui/widget/mixins/Taggable'
], function(declare, query) {

  return declare('tui.widget.taggable.AllInOnePackageTaggable', [tui.widget._TuiBaseWidget],{
	  
	    postCreate: function () {
	    	var placesOfInterest = this;
	    	placesOfInterest.inherited(arguments);
	    	_.each(query("a", placesOfInterest.domNode), function(anch, ind){
	    		if(anch.href.indexOf("javascript:") == -1 && anch.href.indexOf("#") != 0 ){
	    			placesOfInterest.tagElement(anch);
	    		}
	    	});
	    	
	    }
  });

});