define('tui/widget/common/ClickToolTip', [
  'dojo',
  'dojo/on',
  'dojo/dom-attr',
  'dojo/dom-class',
  'dojo/_base/window',
  'dojo/dom-construct',
  'dojo/dom-style',
  "dijit/registry",
  "dojo/query",
  'dojo/dom-geometry',
  'tui/widget/popup/Tooltips'], function (dojo, on, domAttr, domClass, win, domConstruct, domStyle, registry, query,domGeom){


  dojo.declare('tui.widget.common.ClickToolTip', [tui.widget.popup.Tooltips], {

    position: 'top',

    text: null,

    lists: null,
    
    comment:null,

    offset: null,

    top:20,

    left: 50,


      addPopupEventListener: function () {
          var popupBase = this;
          if (!popupBase.domNode) return;
          popupBase.connect(popupBase.domNode, ["on", popupBase.eventType].join(""), function (e) {
              dojo.stopEvent(e);
              if(!popupBase.popupDomNode) {
                  popupBase.open();
              }
              else {
                  popupBase.isOpen() ? popupBase.close() : popupBase.open();
              }
                // to close the already opened tooltip
              var activePopUp = domGeom.position(popupBase.popupDomNode);
              dojo.forEach(
                  dojo.query(".tooltip"),
                  function(activetip){
                      var tmp = domGeom.position(activetip);
                      if(tmp.h != activePopUp.h  || tmp.w != activePopUp.w || tmp.x != activePopUp.x || tmp.y != activePopUp.y){
                          domStyle.set(activetip, 'display', 'none');
                      }
                  });
          });
          on(dojo.query("body"), 'click', function (e){
              if(popupBase.popupDomNode) {
                  popupBase.close();
              }
          });
		 if(document.addEventListener) {
			document.body.addEventListener('touchstart', function(e) {
			if(popupBase.popupDomNode) {
                  popupBase.close();
              }
		  });
		  }
      },

     isOpen : function() {
         return domStyle.get(this.popupDomNode, "display") === "block" ;
     },

	startup: function(){
    	var popupBase = this;
    	on(dojo.query('.item-toggle'), "click", function(event){
    		popupBase.popupDomNode ? popupBase.close() : null;
    	  });
		  
		  on(dojo.query('.summaryAccordSwitch'), "click", function(event){
    		popupBase.popupDomNode ? popupBase.close() : null;
    	  });

        on(dojo.query('.sub-item-toggle'), "click", function(event){
            popupBase.popupDomNode ? popupBase.close() : null;
        });
		on(dojo.query('a'), "click", function(event){
			popupBase.popupDomNode ? popupBase.close() : null;
		});
		on(dojo.query('.trigger'), "click", function(event){
            popupBase.popupDomNode ? popupBase.close() : null;
        });
    }
  	

  });
});
