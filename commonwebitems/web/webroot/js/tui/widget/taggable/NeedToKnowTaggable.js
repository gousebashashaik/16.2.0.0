define('tui/widget/taggable/NeedToKnowTaggable', [
  'dojo/_base/declare',
  'dojo/query',
  'tui/widget/mixins/Taggable'
], function(declare, query) {

  return declare('tui.widget.taggable.NeedToKnowTaggable', [tui.widget._TuiBaseWidget],{
	  
	    postCreate: function () {
	    	var needToKnow = this;
	    	needToKnow.inherited(arguments);
	    	needToKnow.tagElement(needToKnow.domNode);
	    	
	    }
  });

});