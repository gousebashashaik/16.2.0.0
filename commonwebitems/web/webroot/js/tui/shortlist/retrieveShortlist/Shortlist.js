define('tui/shortlist/retrieveShortlist/Shortlist', [
  'dojo',
  'dojo/query',
  'dojo/on',
  'dojo/dom-style',
  'dojo/dom-class',
  'dojo/dom-construct',
  'dojo/dom-attr',
  'dojo/topic',
  'dojo/_base/xhr',
  'dojo/cookie',
  'tui/shortlist/store/ShortlistStore',
  'tui/utils/SessionStorage',
  'tui/shortlist/retrieveShortlist/ViewShortlistHolidaysCarousel',
  'tui/widget/_TuiBaseWidget'], function(dojo, query, on, domStyle, domClass, domConstruct, domAttr, topic, request, cookie, shortlistStore, sessionStore){
	dojo.declare("tui.shortlist.retrieveShortlist.Shortlist", [tui.widget._TuiBaseWidget, tui.shortlist.retrieveShortlist.ViewShortlistHolidaysCarousel, tui.shortlist.store.ShortlistStore], {

	contentSelector: ".content",
	
	itemSelector: ".item",
	
	shortlistCount: 0,
	
    postCreate: function(){
      var widget = this;
      widget.shortlistCounter = dijit.registry.byId('short-list');	
      widget.inherited(arguments);
      widget.addEventListeners();
    },
    
    addEventListeners: function() {
    	var widget = this;
    	on(widget.domNode, on.selector(".item .trigger", "click"), function (event) {
    		var liItem = query(event.target).parents(widget.itemSelector)[0];
    		var contentEle = query(".content",liItem)[0];
    		if(domClass.contains(liItem, "open")) {
    			domClass.remove(liItem,"open");
    			domClass.add(contentEle,"hide");
    		}
    		else {
    			domClass.add(liItem, "open");
    			domClass.remove(contentEle,"hide");
    		}
    	});
    	// image data request on click on camera icon
          on(widget.domNode, on.selector(".js-img-data-req", "click"), function (event) {
           	 var srcElm = this;
           	// var parDiv = dojo.query(srcElm).parent()[0];
           	 var parLi = dojo.query(srcElm).closest("li.search-result-item")[0];
           	 var packageId = domAttr.get(srcElm, "data-product-id");
           	 var hotelName = domAttr.get(srcElm, "data-accomodation");
           	 var url = dojoConfig.paths.webRoot+"/media/" + hotelName + "-" +packageId;
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
           	    		dojo.query("div", parLi).removeClass("js-img-data-req");
              	    	 	dojo.query("div.photo", parLi).attr("data-dojo-props", "jsonData:" + JSON.stringify(result.model.jsonObj) );
              	    	 	dojo.query("div.photo", parLi).attr("data-dojo-type", "tui.widget.media.MediaPopupSwipe");
              	    	 	if( result.model.jsonObj.galleryVideos && result.model.jsonObj.galleryVideos.length ){
              	    	 		dojo.query("div.video", parLi).attr("data-dojo-props", 'videoId:"' + result.model.jsonObj.galleryVideos[0].code + '", videoPlayerId:"' + widget.videoPlayerId + '", videoPlayerKey:"' + widget.videoPlayerKey + '"');
              	    	 		dojo.query("div.video", parLi).attr("data-dojo-type", "tui.widget.media.VideoPopup");
              	    	 	}
               	    	dojo.parser.parse(dojo.query(".popup-icons", parLi)[0]);
               	    	 on.emit(srcElm, "mousedown", {
                	    	    bubbles: true,
                	    	    cancelable: true
                	    	 });
           	    	}
           	    }
           	});
           });
    	on(widget.domNode, on.selector(".remove-unit", "click"), function(event){
          dojo.stopEvent(event);
		  
		  //handle carousel functionality removing a holiday
		  var buttonRemoveId = this.getAttribute("data-remove-id");
		  var removeButtonsIdsArray = dojo.query("#shortlist .remove-unit").attr("data-remove-id");
		  
    	  var packageId = domAttr.get(query(".remove-unit", widget.domNode)[0],"data-package-id");
    	  var liItem = query(event.target).parents("li.search-result-item")[0];
    	  var ulEle = query("ul", widget.domNode)[0];
    	  domStyle.set(liItem, "display", "none");
    	  domConstruct.destroy(liItem);
    	  widget.updateShortlist("remove", domAttr.get(liItem, "data-package-id"));
    	  // update shortlist count on page
    	  --widget.shortlistCount;
    	  widget.updateShortlistCount(widget.shortlistCount);
    	  // display shortlist placeholder item if count less than 3
    	  if(widget.shortlistCount < 3) {
    		  var li = dojo.create("li",{
    			  innerHTML:'<div class="text-container"><p class="placeholder-text"> More shortlisted holidays</p><p class="placeholder-text"> will appear here</p></div>',
    			  "class":"placeholder-item"
    		  }, ulEle, "last");
    	  }
		  
		  //handle carousel functionality removing a holiday
		  dojo.publish("removedHolidayID", [{
				"buttonRemoveId":buttonRemoveId, 
				"removeButtonsIdsArray":removeButtonsIdsArray
		  }]);
        });
    },
    updateShortlist: function (action, packageId) {
        //		Update shortlist store to add/remove package
        var widget = this;
        action = !action ? "add" : "remove";
        widget.getObservable().requestData(true, action, packageId);
      },
      updateShortlistCount: function (count) {
          //		update shortlist count while removing package
          var widget = this;
          var countEle = query(".shortlist-count",widget.domNode)[0];
          if(countEle) countEle.innerHTML = "("+count+")";
        }
  });
});
