define('tui/widget/taggable/UpsellToHolidayTaggable', [
  'dojo/_base/declare',
  'dojo/query',
  'tui/widget/mixins/Taggable'
], function(declare, query) {

  return declare('tui.widget.taggable.UpsellToHolidayTaggable', [tui.widget._TuiBaseWidget],{
	  
	    postCreate: function () {
	    	var upsellToHoliday = this;
	    	upsellToHoliday.inherited(arguments);
	    	_.each(query("a", upsellToHoliday.domNode), function(anch, ind){
	    		if(anch.href.indexOf("javascript:") == -1 && anch.href.indexOf("#") != 0 ){
	    			upsellToHoliday.tagElement(anch);
	    		}
	    	});
	    	
	    }
  });

});