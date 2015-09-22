define('tui/widget/taggable/LaplandDaytripTaggable', [
  'dojo/_base/declare',
  'dojo/query',
  'tui/widget/mixins/Taggable'
], function(declare, query) {

  return declare('tui.widget.taggable.LaplandDaytripTaggable', [tui.widget._TuiBaseWidget],{
	  
	    postCreate: function () {
	    	var laplandDaytrip = this, anlytcTxt;
	    	
	    	laplandDaytrip.inherited(arguments);
	    	_.each(query(".promo-item", laplandDaytrip.domNode), function(item, ind){
	    		
	    		anlytcTxt = query("h4 a.ensLinkTrack", item)[0].innerHTML.replace(/\s/g, "");
	    		_.each(query("a", item), function(anch, ind){	
	    		
	    		if(anch.href.indexOf("javascript:") == -1 && anch.href.indexOf("#") != 0 ){
	    			if( query("img", anch).length ){
	    				var anlytcTxt1 = "thumbnail" + anlytcTxt;
	    			}else if( anch.innerHTML  == "View Details" ){
	    				var anlytcTxt1 = "button" + anlytcTxt;
	    			}else{ var anlytcTxt1 = null; }
	    			laplandDaytrip.tagElement(anch, anlytcTxt1 );
	    			
	    		}
	    	});
	    });
	    }
  });

});