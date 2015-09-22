define("tui/widget/expand/cruise/CruiseExpandable", [
  "dojo",
  "dojo/query",
  "dojo/on",
  "dojo/dom-class",
  "dojo/dom-attr",
  "dojo/dom-style",
  "dojo/_base/Deferred",
  "tui/cruise/itineraryMap/service/ItineraryDetail",
  "tui/cruise/itineraryMap/view/ItineraryDetail",
  "tui/widget/expand/FilterExpandable",
  "tui/search/nls/Searchi18nable"
], function (dojo, query, on, domClass, domAttr, domStyle, Deferred, itineraryDetailService, itineraryDetailView) {

  dojo.declare("tui.widget.expand.cruise.CruiseExpandable", [tui.widget.expand.FilterExpandable, tui.search.nls.Searchi18nable], {

      openText:null,

      closeText: null,

      query: 'h3 span',

      context: null,

      setHeight: 0,

      waitUntilPromise: false,

      locationCode: null,

      shipCode: null,

      startup:function(){
          var deckCutaway = this;
          deckCutaway.initSearchMessaging();
          if(deckCutaway.context !=  null) {
              deckCutaway.openText = deckCutaway.searchMessaging[deckCutaway.context].openText;
              deckCutaway.closeText = deckCutaway.searchMessaging[deckCutaway.context].closeText;
          }
      },

      onBeforeToggle: function (domNode, state) {
          var deckCutaway = this;
          _.each(deckCutaway.expandableItems, function (item, i) {
              var content = query(deckCutaway.itemContentSelector, item)[0];
              var state = domAttr.get(content,'data-toggle-state')
               if(state !== null && state === "open"){
                   domClass['remove'](item, 'open');
                   domClass['remove'](content, 'open');
                   //domClass['add'](item.parent, 'clicked');
                   domAttr.set(content, 'data-toggle-state', "");
                   domStyle.set(content, "maxHeight", _.pixels(deckCutaway.setHeight));
                  if(deckCutaway.openText){
				    query(deckCutaway.query, item)[1].innerHTML = deckCutaway.openText;
                  }
               }else{
                  // domClass['remove'](item.parentNode, 'clicked');
                   if(deckCutaway.openText){
				        query(deckCutaway.query, domNode.parentNode)[1].innerHTML = deckCutaway.openText;
                     }
			   }
          });
      },
      attachEvents: function () {
          var filterExpandable = this;
          on(filterExpandable.domNode, on.selector(filterExpandable.targetSelector, "click"), function (e) {
              var parent = query(e.target).parents(filterExpandable.itemSelector)[0];
              var content = query(filterExpandable.itemContentSelector, parent)[0];
              var close = (domAttr.get(content, 'data-toggle-state') === 'open');
              var itmCnt = query("div.item-content", filterExpandable.domNode)[0];

              if(close){
                  filterExpandable.toggleView(e);
              }
              else if( filterExpandable.waitUntilPromise && _.isUndefined(query('.item-list-box', filterExpandable.domNode)[0])){
            	  	if( dojo.getStyle(query('.map-loader', filterExpandable.domNode)[0], "display") == "block" ){ return; }
            	  	domStyle.set(itmCnt, {maxHeight: "100px", height: "100px"});
                     //1. Request new data relevant to Excurions/Facilities
                     var xhrResp = new itineraryDetailService({getParent: function(){ return filterExpandable.domNode } }).fetchData(filterExpandable.shipCode, filterExpandable.locationCode, filterExpandable.atSea);
                     //2. update data within current Expandable
                     xhrResp && Deferred.when(xhrResp, function (response){
                    	domStyle.set(itmCnt, {maxHeight: "0", height: ""});
                        filterExpandable.updateView(response,  e, filterExpandable.atSea);
                     });
              }else{
                  filterExpandable.toggleView(e);
              }
          });
      },

      updateView: function(data, event, atSea){
          var filterExpandable = this;
          Deferred.when(data, function (data) {
              var view = itineraryDetailView(data, filterExpandable.locationName, atSea, filterExpandable.bookMode);
              view.addToView(query('.itinerary-item-list', filterExpandable.domNode)[0], atSea);
              filterExpandable.toggleView(event);
          });
      },

	  onAfterToggle: function (domNode, state) {
        var deckCutaway = this;
		var state = domAttr.get(domNode,'data-toggle-state')
         if(state !== null && state === "open"){
             if(deckCutaway.closeText){
                 query(deckCutaway.query, domNode.parentNode)[1].innerHTML = deckCutaway.closeText;
             }
		}else{
			domStyle.set(domNode, "maxHeight", _.pixels(deckCutaway.setHeight));
		}
		dojo.publish("tui/cruise/deck/controller/DeckSVGController/afterToggle", domNode, state);
      }
  });

  return tui.widget.expand.cruise.CruiseExpandable;
});