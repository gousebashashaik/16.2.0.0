define("tui/cruise/itineraryMap/controller/ItineraryMap", [
    'dojo',
    'dojo/text!tui/cruise/itineraryMap/view/templates/portOfCall.html',
    'dojo/text!tui/cruise/itineraryMap/view/templates/AddToStay.html',
    'dojo/on',
    'tui/widget/popup/cruise/PortOfCallOverlay',
    'tui/widget/popup/cruise/FacilityOverlay',
    'tui/widget/mixins/Templatable',
    'tui/widget/_TuiBaseWidget'], function (dojo, tmpl, stayTmpl, on, Overlay, StayOverlay) {
    dojo.declare('tui.cruise.itineraryMap.controller.ItineraryMap', [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable], {

        data: null,

        tmpl: tmpl,
        
        stayTmpl: stayTmpl,
        
        postCreate: function () {
           var itineraryMap = this;
           itineraryMap.inherited(arguments);
            //TODO: Image Coordinate data...
            var map = {};
            map[8] = 'top:264px;left:352px;';
            map[1] = 'top:264px;left:352px;';
            map[2] = 'top:310px;left:526px;';
            map[3] = 'top:149px;left:595px;';
            map[4] = 'top:139px;left:719px;';
            map[5] = 'top:75px;left:660px;';
            map[6] = 'top:73px;left:513px;';
            map[7] = 'top:174px;left:332px;';
            
           _.each(itineraryMap.data.cruiseMapComponentViewData.itineraryLegDatas, function (itineraryData) {
              var itinerary = {"itinerary": itineraryData, "coordinates": map[itineraryData.dayNo]};
              var html = itineraryMap.renderTmpl(null, itinerary);
              dojo.place(html, itineraryMap.domNode , "last");
           });
           
           var itinerary = {"coordinates": "top:310px;left:372px;"};
           var html = itineraryMap.renderTmpl(stayTmpl, itinerary);
           dojo.place(html, itineraryMap.domNode , "last");
           on(itineraryMap.domNode, "span.cr-stay-Icon:click", function(event){
      		  var overlay = new StayOverlay({
      	             jsonData: itineraryMap.data.cruiseMapComponentViewData.addToStay,
      	             componentName: "stayOverlay"
      	       });
      		  overlay.open();
      	  });
    	  on(itineraryMap.domNode, "span.imgDiv:click, a.loction-name:click", function(event){
     		  var seqNo;
     		  var mapData = itineraryMap.data.cruiseMapComponentViewData.itineraryLegDatas;
     		  var index =0;
     		  _.each(mapData, function(item){
     			  var innerHTML = event.srcElement ? event.srcElement.id : event.originalTarget.id;
     			  if(item.locationData.locationName == innerHTML){
     				  seqNo = index;
     			  }
     			 index++;
     		  });
     		  var overlay = new Overlay({
     	             jsonData: itineraryMap.data.cruiseMapComponentViewData.itineraryLegDatas[seqNo]
     	       });
     		  overlay.open();
     	  });
        }
    });
    return tui.cruise.itineraryMap.controller.ItineraryMap;
});


