define('tui/widget/taggable/AddAStayItinerary', [
  'dojo/_base/declare',
  'dojo/query',
  'tui/widget/mixins/Taggable'
], function(declare, query) {

  return declare('tui.widget.taggable.AddAStayItinerary', [tui.widget._TuiBaseWidget],{
	  
	    postCreate: function () {
	    	var addAStay = this;
	    	addAStay.inherited(arguments);
	    	_.each(query("a", addAStay.domNode), function(anch, ind){
	    		if(anch.href.indexOf("javascript:") == -1 && anch.href.indexOf("#") != 0 ){
	    			addAStay.tagElement(anch);
	    		}
	    	});
	    	
	    }
  });

});