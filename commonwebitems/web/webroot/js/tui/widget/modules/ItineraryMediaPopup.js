define('tui/widget/modules/ItineraryMediaPopup', [
  'dojo/_base/declare',
  "dojo/dom-attr",
  "dojo/dom-class",
  'dojo/query',
  'dojo/on',
  'dojo/parser',
  "tui/widget/media/VideoPopup",
  "tui/widget/media/MediaPopup"
], function (declare, domAttr, domClass, query, on, parser) {


  return declare('tui.widget.modules.ItineraryMediaPopup', [tui.widget._TuiBaseWidget], {

    postCreate: function () {
    	var ajaxMediaController = this;
    	ajaxMediaController.inherited(arguments);
    	ajaxMediaController.delegateEvents();
    },
    
    delegateEvents: function(){
    	var ajaxMediaController = this;
    	// image data request on click on camera icon
        on(ajaxMediaController.domNode, on.selector(".dataReq", "click"), function (event) {
           
         	 var srcElm = this;
         	 var parNode = ajaxMediaController.domNode;
         	 var packageId = domAttr.get(srcElm, "data-product-id");
         	 var hotelName = domAttr.get(srcElm, "data-accomodation").replace(/[^a-z0-9]/gi,'');
         	 var url = dojoConfig.paths.webRoot + "/media/" + hotelName + "-" +packageId;
         	
         	 if(domClass.contains(srcElm, "disabled")){
        		return;
        	 }
         	 
         	domAttr.remove(query("img", srcElm)[0], "data-dojo-type");
         	
          	dojo.xhrGet({
         	    // The URL to request
         	    url: url,
         	    handleAs: "json",
                 error: function (err) {
                     if (dojoConfig.devDebug) {
                         console.error(err);
                     }
                     mediator.afterFailure();
                 },
         	    load: function(result){
         	    	
         	    	if( typeof result == "object" ){
         	    		
         	    		query("a", parNode).removeClass("dataReq");
         	    		if( result.model.jsonObj.galleryImages && result.model.jsonObj.galleryImages.length ){
         	    			
         	    			query(".gallery", parNode).attr("data-dojo-props", "jsonData:" + JSON.stringify(result.model.jsonObj) );
         	    			query(".gallery", parNode).attr("data-dojo-type", "tui.widget.media.MediaPopup");
         	    		}else{
        	    	 		query(".gallery", parNode).addClass("disabled");
        	    	 	}
        	    	 	
             	    	parser.parse(parNode);
             	    	on.emit(srcElm, "mousedown", {
              	    	    bubbles: true,
              	    	    cancelable: true
              	    	 });

         	    	}
         	    }
         	});
         });
    }
    
    

    });
  });
