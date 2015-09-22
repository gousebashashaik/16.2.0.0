define('tui/widget/media/cruise/CruiseMediaPopup', [
  'dojo', 
  "dojo/_base/declare",
  'dojo/text!tui/widget/media/cruise/templates/CruiseMediaTmpl.html',
  "tui/widget/media/MediaPopup"
  ], function(dojo, declare, mediaTmpl) {
	
  return declare('tui.widget.media.cruise.CruiseMediaPopup', [tui.widget.media.MediaPopup], {
	  // ----------------------------------------------------------------------------- properties
	  	jsonData: null,
	    tmpl: mediaTmpl,
	  	index:0,
	    // ----------------------------------------------------------------------------- singleton
	    setPopJsonData: function () {
	    	 var crMediaPopup = this;
	    	 crMediaPopup.jsonData = crMediaPopup.jsonData.textPaneDatas  != null ? crMediaPopup.jsonData.textPaneDatas[crMediaPopup.index]: crMediaPopup.jsonData;
	    }
  });
});
