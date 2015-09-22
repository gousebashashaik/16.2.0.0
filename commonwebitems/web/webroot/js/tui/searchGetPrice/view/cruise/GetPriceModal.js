define("tui/searchGetPrice/view/cruise/GetPriceModal", [
    "dojo",
    "dojo/dom-geometry",
    "dojo/_base/lang",
    "dojo/_base/fx",
    "tui/utils/RequestAnimationFrame",
    "tui/widget/popup/DynamicPopup",
    "tui/searchGetPrice/view/GetPriceModal"], function (dojo, domGeom, lang, fx) {

    dojo.declare("tui.searchGetPrice.view.cruise.GetPriceModal", [tui.searchGetPrice.view.GetPriceModal], {


        // ---------------------------------------------------------------- methods

    	postCreate: function(){
    		var getPriceModal = this;
    		getPriceModal.inherited(arguments);

    		dojo.subscribe("tui.searchGetPrice.view.GetPriceModal.open", function(){
    			getPriceModal.open();
    		});
    		dojo.subscribe("tui.searchGetPrice.view.GetPriceModal.close", function(){
    			getPriceModal.close();
    		});
    		dojo.subscribe("tui.searchGetPrice.view.GetPriceModal.resize", function(w){
    			var childCnt = getPriceModal.searchPanelModel.children;
    			var extraSpace =  w > 600 && (childCnt == 3 ?  90 : childCnt > 3 ? 90 * 2 : 0 )
    			//width adjusted only to airport guide (w>600). Cruise & Stay not required
    			getPriceModal.resize(w + extraSpace);
    			//getPriceModal.alignOverlay(getPriceModal.popupDomNode);
    		});
    		dojo.subscribe("tui.searchGetPrice.view.GetPriceModal.positionPopUp", function(){
    			getPriceModal.positionPopUp();
    		});
    	},

        open: function(sailingDate){
            var getPriceModal = this;
            getPriceModal.inherited(arguments);
            //Update Get Price overlay and Sailing dates accordingly. (only applicable for Cruise)
            (sailingDate) ? getPriceModal.sailingDate = true : getPriceModal.sailingDate = false;
            var flexible = dijit.registry.byId('cruiseGetPriceFlexibleView');
            if(getPriceModal.sailingDate) {
                flexible.checkbox.checked = false;
                flexible.setFlexible();
            }else{
               if(!dojo.attr(flexible.checkbox, "checked")){
                   flexible.checkbox.checked = true;
                   flexible.setFlexible();
               }
            }
        },

        onAfterPosition: function(){
        	var overlay = this;
        	overlay.inherited(arguments);
        	overlay.alignOverlay(overlay.popupDomNode);
        },

        animateWidth: function(w){
        	 var overlay = this;
        	 overlay.inherited(arguments);
        	 fx.animateProperty({
                 node: dojo.query(".cruise-wraper", overlay.popupDomNode)[0],
                 properties: {
                     'minWidth': w
                 },
                 duration:280
             }).play();
        },

        alignOverlay: function(popupDomNode){

        	var overlay = this;
        	var viewStat = popupDomNode.style.display;
        	popupDomNode.style.display = "block";
        	var	excursionOverLay = domGeom.position(popupDomNode, true),
        		excursionDomNodeInfo = domGeom.position(dojo.query(".cruise-wraper", popupDomNode)[0], true),
        		lastX =  (excursionOverLay.w - excursionDomNodeInfo.w) / 2,
        		lastY =  (excursionOverLay.h - excursionDomNodeInfo.h) / 2;
        	//alert("BG Layer" + excursionOverLay.h + "-" + "DomNode Layer" + excursionDomNodeInfo.h);
        	if( lastY <= 10 ){
        		dojo.setStyle(dojo.query(".cruise-wraper", popupDomNode)[0], { top: "10px" }); //, left: lastX + "px"
        	}else{
        		dojo.setStyle(dojo.query(".cruise-wraper", popupDomNode)[0], { top: lastY + "px"}); //, left: lastX + "px"
        	}
        	popupDomNode.style.display = (viewStat == "none" ? "none" : "block");
        }

    });

    return tui.searchGetPrice.view.cruise.GetPriceModal;
});