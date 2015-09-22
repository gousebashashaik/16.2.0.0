define("tui/widget/maps/cruise/CruiseAreasMap", [
	"dojo/_base/declare",
	'dojo/query',
    'dojo/dom-style',
    'dojo/on',
    "dojo/dom-attr",
    "dojo/_base/xhr",
    "dijit/registry",
	'tui/widget/popup/cruise/PortOfCallOverlay',
	'tui/widget/popup/cruise/FacilityOverlay',
    "tui/cruise/itineraryMap/controller/CruiseMapStyles",
    "tui/widget/popup/Tooltips",
    "dojo/_base/sniff",
    "google/maps/richmarker",
    'tui/widget/mixins/Templatable',
	'tui/widget/maps/MapBase'
	], function (declare, query, domStyle, on, domAttr, xhr, registry, POCOverlay, StayOverlay, CruiseMapStyles, Tooltips) {
	var excursionUrl = dojoConfig.paths.webRoot + "/excursions";
	return declare("tui.widget.maps.cruise.CruiseAreasMap", [tui.widget.maps.MapBase, tui.widget.mixins.Templatable], {

	    latLng: [],

	    data: null,

	    destLandingMap: true,

	    centerFromMarkers: true,

	    clusterMarkers:true,

	   //need to set from controller
	    cruiseAreaIndex: 0,

	    controlPosition: google.maps.ControlPosition.TOP_LEFT,

	    postMixInProperties: function () {
	        // summary:
	        //		Call before widget creation.
	        // description:
	        // 		Intialise given objects to their default states.
	        var cruiseAreaMap = this;
	        cruiseAreaMap.inherited(arguments);
	        cruiseAreaMap.mapnode = cruiseAreaMap.srcNodeRef;
	    },

	    postCreate: function () {
	        var cruiseAreaMap = this, urlParams, index;
	        cruiseAreaMap.getParent().cruiseAreaMap = cruiseAreaMap;
	        cruiseAreaMap.inherited(arguments);
	        if(!cruiseAreaMap.data.destLandingMap) {
	    	   //on page load - "ALL PORTS OF CALL" ports should be loaded
	    	   // Default code - "000"
	    		cruiseAreaMap.cruiseAreaIndex =  cruiseAreaMap.getParent().fetchIndex("000");
	        	cruiseAreaMap.setMarkers(cruiseAreaMap.fetchLocations());
		        var center = cruiseAreaMap.map.getCenter();
		        google.maps.event.trigger(cruiseAreaMap.map, "resize");
		        cruiseAreaMap.map.setCenter(center);
		        //cruiseAreaMap.map.setZoom(2);
	        }else{
	        	// Initiating Google map for cruise destinations.
			    // Index will never be 1 as static map will be shown in this case
			   (cruiseAreaMap.selectedRegion === '') ? cruiseAreaMap.cruiseAreaIndex = 1 :  cruiseAreaMap.cruiseAreaIndex = cruiseAreaMap.getParent().fetchIndex(cruiseAreaMap.selectedRegion);
	            cruiseAreaMap.getParent().initiateGoogleMap();
	            dojo.destroy(query(".cruise-destination-map-loader")[0]);
	        }
           _.each( query("li.cruise-area",cruiseAreaMap.getParent().domNode ), function(liNode){
        	   index = domAttr.get(liNode,'data-index');
        	   cruiseAreaMap.getParent().crAreaEventHandler(liNode, index);
           });

           urlParams = cruiseAreaMap.selectedRegion;
            /*urlParams = widget.selectedRegion;*/
	        /**
			  * Cruise region preselected - loading google map
			  * Fetch the cruiseAreaIndex using the code(urlParam)
			  * Initiates map and loads preselected cruise region
			  */
	      	 if (urlParams){
	      		 // condition for cruise destination page scenario
	          	 if( cruiseAreaMap.data.destLandingMap) {
	              	 domStyle.set(query(".google-map", cruiseAreaMap.getParent().domNode )[0], "display","block");
	              	 domStyle.set(query(".static-map", cruiseAreaMap.getParent().domNode )[0], "display","none");
	              	 domStyle.set(query(".map-icons", cruiseAreaMap.getParent().domNode )[0], "display","none");
	              	 domStyle.set(query(".cruiseTopx", cruiseAreaMap.getParent().domNode )[0], "display","none");
	              	 cruiseAreaMap.cruiseAreaIndex =  cruiseAreaMap.getParent().fetchIndex(urlParams);
	              	 cruiseAreaMap.getParent().initiateGoogleMap();
	               }
	          }
	      	 var scrollPreventPane =  dojo.place('<div class="scroll-preventer"></div>', cruiseAreaMap.domNode, "last");
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
	       	 	if( dojo.hasClass(e.target, "interactive-map") ){
		      	 	scrollPreventPane.style.display= 'block'; return ;
		      	}
	      		if( e.target.style.backgroundImage.indexOf("icon-map-cluster.png") !== -1  ||
	      				e.target.style.backgroundImage.indexOf("map-icon-sprite.png") !== -1 ||
	      				dojo.hasClass(e.target, "inspirational")
	      				){ return }
	      		var flag = query(e.target).closest(".interactive-map").length ||
	      					query(e.target).closest(".poc-overlay").length ;
	      		scrollPreventPane.style.display=  flag ? 'none' : 'block';
	      	 });
	    },

	    onAfterInitMap: function () {
	        var cruiseAreaMap = this;
	        cruiseAreaMap.map.set('styles', CruiseMapStyles["mapStyles"]);

	        google.maps.event.addListener(cruiseAreaMap.map, 'zoom_changed', function(){
            	query(".tooltip").style({display: "none"});
        	});
	    },

	    createMarker: function (latLng, location, image, index, bounds) {
        	var cruiseAreaMap = this;
        	if(!location.featureCodesAndValues.atSea){
        		 var marker = new RichMarker({
		            position: latLng,
		            map: cruiseAreaMap.map,
		            flat: true,
		            title: location.featureCodesAndValues.name,
		            icon: image,
		            index: index
		         });
	            marker.entitydata = location;
	            marker.index = location.featureCodesAndValues.index;
	            marker.locationName = location.featureCodesAndValues.name;
	            marker.parentLocationName = location.featureCodesAndValues.parentLocationName;
	            if (cruiseAreaMap.mapFilter) {
	              marker.entitydata.mapFilter = cruiseAreaMap.mapFilter;
	              marker.entitydata.mapLevel = cruiseAreaMap.mapLevel;
	            }
	            cruiseAreaMap.markers.push(marker);
	            cruiseAreaMap.setMarkerImages(marker);
	            cruiseAreaMap.addMarkerEventListener(marker, location, index);
	            if (bounds) {
	              bounds.extend(latLng);
	              cruiseAreaMap.map.fitBounds(bounds);
	            }
        	}
        },

        setMarkerImages: function(marker){
          	var cruiseAreaMap = this,
          	mapData = cruiseAreaMap.data.destinationViewDatas[cruiseAreaMap.cruiseAreaIndex].locationDatas[marker.index];
          	if(mapData.addToStay){
                  //stay icon
              	marker.setContent(
              			"<div class='my-marker'>" +
              				"<div  class='map-icon-stay marker-"+marker.index+"' ></div>"+
              			"</div>"
              	);
              }else{
                  // Port of call Icon
              	marker.setContent(
              			"<div class='my-marker'>" +
  							"<div class='map-icon-all ports marker-"+marker.index+"'></div> "+
  						"</div>"
              		);
              }
          	 cruiseAreaMap.applyMarkerImages(marker);
         },

	    applyMarkerImages: function (marker) {
	        // summary:
	        //		Apply image to marker.
	        // description:
	        // 		Set marker rollover state images.
	        var cruiseAreaMap = this;
	        google.maps.event.addListener(marker, "click", function () {
	            cruiseAreaMap.onMarkerClick(marker);
	        })
	        google.maps.event.addListener( marker, 'mouseover', function () {
	            cruiseAreaMap.onMarkerMouseOver(marker);
	        });
	    },

	    createClusterMarkers: function ( ) {
	    	var cruiseAreaMap = this;
	    	cruiseAreaMap.markerClustered = new MarkerClusterer(cruiseAreaMap.map, cruiseAreaMap.markers, {"styles": CruiseMapStyles["clusterStyles"]});
	    },

	    fetchLocations: function() {
	        var cruiseAreaMap = this;
	        var locations = [];
	        var i = 0;
	        _.each(cruiseAreaMap.data.destinationViewDatas[cruiseAreaMap.cruiseAreaIndex].locationDatas, function (itinerary, i) {
        	   locations.push({
	                featureCodesAndValues:{
	                    latitude: itinerary.latitude,
	                    longitude: itinerary.longitude,
	                    name: itinerary.locationName,
	                    atSea: itinerary.atSea,
	                    parentLocationName: itinerary.parentLocationName,
	                    index: i
	                }
	            });
        	   i++
	        });
	        return locations;
	    },

	    onMarkerMouseOver: function (marker) {
        	var cruiseAreaMap = this;
        	mapData = cruiseAreaMap.data.destinationViewDatas[cruiseAreaMap.cruiseAreaIndex].locationDatas[marker.index];
			var node = query("div.marker-"+marker.index, cruiseAreaMap.domNode)[0];
			  if(node.id !="") {
				  var widget = registry.byNode(node);
				  widget.open();
			  }else {
                  var textInTooltip = marker.locationName.toUpperCase();
                  if(_.isString(marker.parentLocationName) && marker.parentLocationName !== '')
                  {
                      textInTooltip = textInTooltip +", "+marker.parentLocationName;
                  }

				  if(mapData.addToStay){
				     var tip = new Tooltips({
    				    text:"CRUISE & STAY IN "+textInTooltip.toUpperCase(),
    				    floatWhere:'position-top-center'
    			     }, node);
    			     tip.open();
				  }else{
				     var tip = new Tooltips({
	    			    text:textInTooltip.toUpperCase(),
	    				floatWhere:'position-top-center'
	    			 }, node);
	    			 tip.open();
				  }
			  }
          },

          onMarkerClick: function (marker) {
  	        var cruiseAreaMap = this, xhrReq;
			domStyle.set(query("div.map-loader", cruiseAreaMap.getParent().domNode)[0], "display","block");
  	        var mapData = cruiseAreaMap.data.destinationViewDatas[cruiseAreaMap.cruiseAreaIndex].locationDatas[marker.index];
			xhrReq = xhr.get({
                  url: excursionUrl+"?locCode="+mapData.locationCode+"&stay="+mapData.addToStay,
                  handleAs: "json",
                  load: function (response) {
                  	cruiseAreaMap.handleResults(response, mapData);
					domStyle.set(query("div.map-loader", cruiseAreaMap.getParent().domNode)[0], "display","none");
					},
                  error: function (err) {
                   _.debug(err);
					console.log("AJAX Error message: "+error);
                  }
                });

  	        },

  	        handleResults:function (response, mapData){
  	        	var cruiseAreaMap = this;
  	        	 if(!mapData.atSea && !mapData.addToStay){

  	 	            var overlay = new POCOverlay({
  	 	                jsonData: response
  	 	            });
  	 	            overlay.open();
  	 	        }
  	 	        if(!mapData.atSea && mapData.addToStay){

  	 	            var overlay = new StayOverlay({
  	 	                jsonData: response,
  	 	                componentName: "stayOverlay"
  	 	                });
  	 	                overlay.open();
  	 	            }
  	        	}
	    });
	});