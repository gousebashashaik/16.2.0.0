

define("tui/cruise/itineraryMap/controller/CruiseItineraryMap", [
    'dojo',
    'dojo/text!tui/cruise/itineraryMap/view/templates/portOfCall.html',
    'dojo/text!tui/cruise/itineraryMap/view/templates/AddToStay.html',
    'dojo/on',
    'dojo/query',
    "dojo/parser",
    'tui/widget/popup/cruise/PortOfCallOverlay',
    'tui/widget/popup/cruise/FacilityOverlay',
    "tui/cruise/itineraryMap/controller/CruiseMapStyles",
    "tui/cruise/itineraryMap/service/ItineraryDetail",
    "dojo/_base/Deferred",
    "dojo/_base/sniff",
    "google/maps/richmarker",
    "tui/widget/popup/Tooltips",
    'tui/widget/mixins/Templatable',
    'tui/widget/maps/MapBase'
    ], function (dojo, pocTmpl, stayTmpl, on, query, parser, POCOverlay, StayOverlay, CruiseMapStyles, itineraryDetailService, Deferred) {

    dojo.declare('tui.cruise.itineraryMap.controller.CruiseItineraryMap', [tui.widget.maps.MapBase, tui.widget.mixins.Templatable], {

        latLng: [],

        data: null,

        centerFromMarkers: true,

        controlPosition: google.maps.ControlPosition.TOP_LEFT,

        postMixInProperties: function () {
            // summary:
            //		Call before widget creation.
            // description:
            // 		Intialise given objects to their default states.
            var cruiseMap = this;
            cruiseMap.inherited(arguments);

            cruiseMap.mapnode = cruiseMap.srcNodeRef;

            cruiseMap.markers = [];

            if (cruiseMap.latLng[0] + cruiseMap.latLng[1] === 0) {
                cruiseMap.centerFromMarkers = true;
            }

            cruiseMap.currentPopup = new InfoBubble({
                borderRadius: 0,
                arrowPosition: 10,
                minWidth: 232,
                shadowStyle: 0,
                disableAnimation: true
            });

            cruiseMap.latLngMapCenter = new google.maps.LatLng(cruiseMap.latLng[0], cruiseMap.latLng[0]);
        },

        onAfterInitMap: function () {
            var cruiseMap = this;
            cruiseMap.map.set('styles', CruiseMapStyles["mapStyles"]);
            cruiseMap.setMarkers(cruiseMap.fetchLocations());
            google.maps.event.addListenerOnce(cruiseMap.map, 'idle', function(){
        	    // do something only the first time the map is loaded
        		_.each(query("div.my-marker", cruiseMap.domNode), function(node){
        			parser.parse(node);
        		});
        	});
            google.maps.event.addListener(cruiseMap.map, 'zoom_changed', function(){
            	query(".tooltip").style({display: "none"});
        	});
	      	 var scrollPreventPane =  dojo.place('<div class="scroll-preventer"></div>', cruiseMap.domNode, "last");
	      	 on(scrollPreventPane, "click", function(e){
	      		scrollPreventPane.style.display='none';
	      		var x = event.clientX, y = event.clientY, elementMouseIsOver = document.elementFromPoint(x, y);
	      		if( dojo.isIE &&  dojo.isIE <= 8 ){
	      			elementMouseIsOver.fireEvent("onclick");
	          	}else{
	          		on.emit(elementMouseIsOver, "click", {
		      		    bubbles: true,
		      		    cancelable: true
		      		});
	          	}
	      	 });

	      	 dojo.connect(document, "click", function(e){
	      		if( e.target.style.backgroundImage.indexOf("icon-map-cluster.png") !== -1  ||
	      				e.target.style.backgroundImage.indexOf("map-icon-sprite.png") !== -1
	      				){ return }
	      		var flag = query(e.target).closest(".inspirational").length ||
	      					query(e.target).closest(".poc-overlay").length
	      		scrollPreventPane.style.display=  flag ? 'none' : 'block';
	      	 });
        },

        createMarker: function (latLng, location, image, index, bounds) {
        	var cruiseMap = this;
        	if(!location.featureCodesAndValues.atSea){
	        	var marker = new RichMarker({
		            position: latLng,
		            map: cruiseMap.map,
		            flat: true,
		            title: location.featureCodesAndValues.name,
		            icon: image,
		            index: index
		         });
	            marker.entitydata = location;
	            if (cruiseMap.mapFilter) {
	              marker.entitydata.mapFilter = cruiseMap.mapFilter;
	              marker.entitydata.mapLevel = cruiseMap.mapLevel;
	            }
	            cruiseMap.markers.push(marker);
	            cruiseMap.setMarkerImages(marker);
	            cruiseMap.addMarkerEventListener(marker, location);
	            if (bounds) {
	              bounds.extend(latLng);
	              cruiseMap.map.fitBounds(bounds);
	            }
        	}
        },
         setMarkerImages: function(marker){
            var cruiseMap = this,
            	mapData = cruiseMap.data.cruiseMapComponentViewData.itineraryLegDatas[marker.index];
            /* google.maps.event.addListener(marker, 'mouseover', function() {
	        });
	         google.maps.event.addListener(marker, 'mouseout', function() {
	          });
	        */
            if(mapData.locationData.addToStay){
                //stay icon
            	marker.setContent(/*'<div class="my-marker"> <div class="map-icon-stay"></div> </div>'*/
    			'<div class="my-marker">' +
				'<div  class="map-icon-stay" id="tooltip-'+marker.index +'" data-dojo-type="tui.widget.popup.Tooltips" data-dojo-props="floatWhere:\'position-top-center\', text:\'CRUISE & STAY IN '+mapData.locationData.locationName.toUpperCase()+'\'">'+
				'</div>'+
				'</div>'
	            	);
            }else{

            	if(mapData.dayNo.length > 12){

           		 marker.setContent(
                			'<div class="my-marker">' +
        						'<div class="map-icon-largest"></div> '+
        						'<div  class="map-icon-text largest" id="tooltip-'+marker.index +'" data-dojo-type="tui.widget.popup.Tooltips" data-dojo-props="floatWhere:\'position-top-center\', text:\' '+mapData.locationData.locationName.replace(/\'/g, "\\'").toUpperCase()+'  \'">'+
        						'DAYS<br/>'+mapData.dayNo +'</div>'+
        					'</div>'
                			);
                }if(mapData.dayNo.length >=8 && mapData.dayNo.length <=12){

            		 marker.setContent(
                 			'<div class="my-marker">' +
         						'<div class="map-icon-large"></div> '+
         						'<div  class="map-icon-text large" id="tooltip-'+marker.index +'" data-dojo-type="tui.widget.popup.Tooltips" data-dojo-props="floatWhere:\'position-top-center\', text:\' '+mapData.locationData.locationName.replace(/\'/g, "\\'").toUpperCase()+'  \'">'+
         						'DAYS<br/>'+mapData.dayNo +'</div>'+
         					'</div>'
                 			);
                 }else if(mapData.dayNo.length >=5 && mapData.dayNo.length <=8 ) {

                	 marker.setContent(
                 			'<div class="my-marker">' +
         						'<div class="map-icon-medium"></div> '+
         						'<div  class="map-icon-text medium" id="tooltip-'+marker.index +'" data-dojo-type="tui.widget.popup.Tooltips" data-dojo-props="floatWhere:\'position-top-center\', text:\' '+mapData.locationData.locationName.replace(/\'/g, "\\'").toUpperCase()+'  \'">'+
         						'DAYS<br/>'+mapData.dayNo +'</div>'+
         					'</div>'
                 			);
                 }else if(mapData.dayNo.length >=3 && mapData.dayNo.length <=4){

                	 marker.setContent(
                  			'<div class="my-marker">' +
          						'<div class="map-icon-small"></div> '+
          						'<div  class="map-icon-text small" id="tooltip-'+marker.index +'" data-dojo-type="tui.widget.popup.Tooltips" data-dojo-props="floatWhere:\'position-top-center\', text:\' '+mapData.locationData.locationName.replace(/\'/g, "\\'").toUpperCase()+'  \'">'+
          						'DAYS<br/>'+mapData.dayNo +'</div>'+
          					'</div>'
                  			);
                 }else{

                	 marker.setContent(
                  			'<div class="my-marker">' +
          						'<div class="map-icon-all"></div> '+
          						'<div  class="map-icon-text" id="tooltip-'+marker.index +'" data-dojo-type="tui.widget.popup.Tooltips" data-dojo-props="floatWhere:\'position-top-center\', text:\' '+mapData.locationData.locationName.replace(/\'/g, "\\'").toUpperCase()+'  \'">'+
          						'DAY<br/>'+mapData.dayNo +'</div>'+
          					'</div>'
                  			);
                 }
                // Port of call Icon

            }
            cruiseMap.applyMarkerImages(marker);
        },


        applyMarkerImages: function (marker) {
            // summary:
            //		Apply image to marker.
            // description:
            // 		Set marker rollover state images.
            var cruiseMap = this;
            google.maps.event.addListener(marker, "click", function () {
                cruiseMap.onMarkerClick(marker);
            });
        },

        fetchLocations: function(){
            var cruiseMap = this;
            var locations = [];
            _.each(cruiseMap.data.cruiseMapComponentViewData.itineraryLegDatas, function (itinerary) {
                locations.push({
                    featureCodesAndValues:{
                        latitude: itinerary.locationData.latitude,
                        longitude: itinerary.locationData.longitude,
                        name:itinerary.locationData.locationName,
                        dayNumber: itinerary.dayNo,
                        atSea: itinerary.locationData.atSea
                    }
                });
            });
            return locations;
        },


        onMarkerClick: function (marker) {
            var cruiseMap = this;
            var mapData = cruiseMap.data.cruiseMapComponentViewData.itineraryLegDatas[marker.index];

            var xhrResp = new itineraryDetailService({getParent: function(){ return cruiseMap.getParent().domNode } }).fetchData('', mapData.locationData.addToStay ? mapData.loctionCode : mapData.locationData.locationCode, mapData.locationData.atSea);
            Deferred.when(xhrResp, function (response){

                if(!mapData.locationData.atSea && !mapData.locationData.addToStay){
                    var overlay = new POCOverlay({
                        jsonData: response,
                        bookMode : cruiseMap.bookMode
                    });
                    overlay.open();
                }
                if(!mapData.locationData.atSea && mapData.locationData.addToStay){
                    var overlay = new StayOverlay({
                        jsonData: response,//cruiseMap.data.cruiseMapComponentViewData.addToStay,
                        componentName: "stayOverlay",
                        bookMode : cruiseMap.bookMode
                    });
                    overlay.open();
                }
            });
         }
    });
    return tui.cruise.itineraryMap.controller.CruiseMap;
});