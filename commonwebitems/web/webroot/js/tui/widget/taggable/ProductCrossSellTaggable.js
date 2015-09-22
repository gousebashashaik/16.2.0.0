define('tui/widget/taggable/ProductCrossSellTaggable', [
  'dojo/_base/declare',
  'dojo/query',
  'tui/widget/mixins/Taggable'
], function(declare, query) {

  return declare('tui.widget.taggable.ProductCrossSellTaggable', [tui.widget._TuiBaseWidget],{
	  
	    postCreate: function () {
	    	var crossSell = this;
	    	crossSell.inherited(arguments);
	    	_.each(query("a", crossSell.domNode), function(anch, ind){
	    		if(anch.href.indexOf("javascript:") == -1 && anch.href.indexOf("#") != 0 ){
	    			crossSell.tagElement(anch);
	    		}
	    	});
	    	
	    }
  });

});