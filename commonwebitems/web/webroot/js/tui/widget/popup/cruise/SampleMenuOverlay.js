
define('tui/widget/popup/cruise/SampleMenuOverlay', [
  "dojo/_base/declare",
  'dojo/text!tui/widget/popup/cruise/templates/SampleMenuOverlayTmpl.html',
  "dojo/query",
  "dojo/dom-style",
  'tui/widget/popup/Popup'], function(declare, menuTmpl, query, domStyle) {

	return declare('tui.widget.popup.cruise.SampleMenuOverlay', [tui.widget.popup.Popup, tui.widget.mixins.Templatable], {

    // ----------------------------------------------------------------------------- properties

    tmpl: menuTmpl,
    
    jsonData:null,

    subscribableMethods: ["open", "close", "resize"],

    modal:true,

    includeScroll: true,

    timer: null,

    componentName:"",
    
    // ----------------------------------------------------------------------------- singleton
   
    open:function (){
    	var overlay = this;
    	
   		overlay.jsonData.galleryImages = overlay.jsonData.textPaneDatas[overlay.index].sampleMenuUrl;
    	overlay.inherited(arguments);
   },

	onOpen: function () {
		var overlay = this;
		
        setTimeout (function(){
            dojo.removeClass(overlay.popupDomNode, "loading");
        }, 1000);

        // re-center popup when it resizes
        overlay.resize();
		
		var closeLink = dojo.query('.close', overlay.popupDomNode)[0];
    },
    
   
    close: function () {
        var overlay = this;
        overlay.inherited(arguments);
        dojo.destroy(overlay.popupDomNode);
        overlay.popupDomNode = null;
    },

    hideWidget: function () {
        var overlay = this;
        if (overlay.popupDomNode) domStyle.set(overlay.popupDomNode, "display", "none");
    }

  });
});
