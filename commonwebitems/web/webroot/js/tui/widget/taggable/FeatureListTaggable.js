define('tui/widget/taggable/FeatureListTaggable', [
  'dojo/_base/declare',
  'dojo/query',
  'tui/widget/mixins/Taggable'
], function(declare, query) {

  return declare('tui.widget.taggable.FeatureListTaggable', [tui.widget._TuiBaseWidget],{
	  
	    postCreate: function () {
	    	var featureList = this;
	    	featureList.inherited(arguments);
	    	_.each(query("a", featureList.domNode), function(anch, ind){
	    		if(anch.href.indexOf("javascript:") == -1 && anch.href.indexOf("#") != 0 ){
	    			featureList.tagElement(anch);
	    		}
	    	});
	    	
	    }
  });

});