define('tui/widget/taggable/TwoCentreSelectorTaggable', [
  'dojo/_base/declare',
  'dojo/query',
  'tui/widget/mixins/Taggable'
], function(declare, query) {

  return declare('tui.widget.taggable.TwoCentreSelectorTaggable', [tui.widget._TuiBaseWidget],{
	  
	    postCreate: function () {
	    	var twoCentreSelector = this;
	    	twoCentreSelector.inherited(arguments);
	    	_.each(query("a", twoCentreSelector.domNode), function(anch, ind){
	    		if(anch.href.indexOf("javascript:") == -1 && anch.href.indexOf("#") != 0 ){
	    			twoCentreSelector.tagElement(anch);
	    		}
	    	});
	    	
	    }
  });

});