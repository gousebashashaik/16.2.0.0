define("tui/widget/maps/MapBase", ["dojo",
  "dojo/on",
  "google/maps/InfoBubble",
  "google/maps/Markerclusterer",
  "tui/widget/_TuiBaseWidget",
  "dojo/NodeList-traverse"], function (dojo, on) {

  dojo.declare("tui.widget.maps.MapBase", [tui.widget._TuiBaseWidget], {

    // summary:
    //        An abstract class for map compoments.
    // description:
    //		  Base class for all map compoment, and encapsulating all the common behaviours.
    //		  This class should always be extended.
    // @author: Maurice Morgan.

    // ---------------------------------------------------------------- mapBase properties

    // <google.maps.Map> map object.
    map: null,

    // <array <number>> holding the Latitude and Longitude of the map center.
    // If both are set to 0. Then we work out the map center from the markers.
    latLng: [0, 0],

    // <google.maps.LatLng> object refering to Latitude and Longitude
    // of the map center.
    latLngMapCenter: null,

    //attribute to decide on the Control position
    controlPosition: google.maps.ControlPosition.TOP_RIGHT,

    // <boolean> flag which determines if map center point are from markers.
    centerFromMarkers: false,

    // <array <google.maps.Marker>> with reference to the map markers.
    markers: null,

    // <InfoBubble> popup window object.
    currentPopup: null,

    // <boolean> flag which determines whether to cluster markers.
    clusterMarkers: false,

    // <MarkerClusterer> markerCluster object.
    markerClustered: null,

    // <number> number indicating the default zoom.
    zoom: 10,

    //<boolean> flag which determines whether to hide pop up on zoom.
    hidePopupOnZoom: false,

    // <google.maps.Marker> reference to the current selected markers.
    currentMarker: null,

    // FC location code (passed from JSP)
    locationCode: null,

    // root, used for placeholder images
    webRoot: dojoConfig.paths.webRoot,

    //cdn path, used for placeholder images
    cdnDomain: dojoConfig.paths.cdnDomain,

    // name of website for placeholder images
    siteName: dojoConfig.site,

    // ---------------------------------------------------------------- mapBase methods

    postMixInProperties: function () {
      // summary:
      //		Call before widget creation.
      // description:
      // 		Intialise given objects to their default states.
      var mapBase = this;

      mapBase.markers = [];

      if (mapBase.latLng[0] + mapBase.latLng[1] === 0) {
        mapBase.centerFromMarkers = true;
      }

      mapBase.currentPopup = new InfoBubble({
        borderRadius: 0,
        arrowPosition: 10,
        minWidth: 232,
        shadowStyle: 0,
        disableAnimation: true
      });

      mapBase.latLngMapCenter = new google.maps.LatLng(mapBase.latLng[0], mapBase.latLng[0]);
    },

    postCreate: function () {
      // summary:
      //		Call after widget creation.
      // description:
      // 		Once map widget as been created, we intialise the google map with given data.
      var mapBase = this;
      mapBase.inherited(arguments);
      mapBase.onBeforeInitMap();
      mapBase.initMap();
      mapBase.onAfterInitMap();
    },

    initMap: function () {
      // summary:
      //		Initalises a new google map object.
      // description:
      //		This methods is called on object creation, and creates
      //		a new google map object, based on given properties.
      var mapBase = this;

      mapBase.map = new google.maps.Map(mapBase.mapnode, {
        center: mapBase.latLngMapCenter,
        zoom: mapBase.zoom,
        zoomControlOptions: {
          position: mapBase.controlPosition
        },
        panControl: false,
        mapTypeControl: false,
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        styles: [
          {
            featureType: "poi.business",
            elementType: "labels",
            stylers: [
              { visibility: "off" }
            ]
          }
        ]
      });
      if ((dojoConfig.site === "thomson") || (dojoConfig.site === "firstchoice")) {
          if (mapBase.jsonData.locationType == "accommodation") {
        	  htmlDOM = document.body.parentNode;
          if (dojo.hasClass(htmlDOM, "touch")) {
        	  mapBase.enableMapControls(false);

           on(mapBase.domNode, "click", function(event){
        	   mapBase.enableMapControls(true);
           });
           on(document.body, "click", function (event) {  // on body click - disable map controls
      			if (event && event.srcElement && dom.isDescendant(event.srcElement,mapBase.mapnode)){
      				return;
      			} 
      			mapBase.enableMapControls(false);
            });
          }
          }
          }
      if (mapBase.hidePopupOnZoom) {
        google.maps.event.addListener(mapBase.map, 'zoom_changed', function () {
          mapBase.currentPopup.close();
        })
      }
    },
 // Enables/ disables map controls based on enable flag
    enableMapControls: function(enable){
    	var mapBase = this;
    	mapBase.map.setOptions({
    		  zoomControl: enable,
    		  scaleControl: enable,
    		  scrollwheel: enable,
    		  disableDoubleClickZoom: !enable,
    		  draggable: enable
			});
    },
    setMarkers: function (/*Array*/ locations, /*Boolean*/ removeAllMarker, /*String*/ type) {
      // summary:
      //		Plots a marker onto the map.
      // description:
      //		Plots a marker onto the map, using given information for location argument.
      //		All marker currently showing maybe removed if removeAllMarker argument is specified.
      var mapBase = this;
      mapBase.onBeforeSetMarker(locations, removeAllMarker, type);
      removeAllMarker = (removeAllMarker === undefined) ? true : removeAllMarker;
      type = (type === undefined) ? "default" : type

      var bounds = (mapBase.centerFromMarkers && locations.length > 1) ? new google.maps.LatLngBounds() : null;
      if (removeAllMarker) {
        mapBase.removeMarkers();
      }
      for (var i = 0; i < locations.length; i++) {
        var image = dojoConfig.paths.cdnDomain + '/images/' + dojoConfig.site + '/icon-map-default.png';
        var location = locations[i];
        var latLng = new google.maps.LatLng(location.featureCodesAndValues.latitude, location.featureCodesAndValues.longitude);
        mapBase.createMarker(latLng, location, image, i, bounds);
      }
      
      if (locations.length == 1) {
        mapBase.map.setCenter(new google.maps.LatLng(locations[0].featureCodesAndValues.latitude,
            locations[0].featureCodesAndValues.longitude));
      }

      if (mapBase.clusterMarkers) {
    	  mapBase.createClusterMarkers();
      }
      mapBase.onAfterSetMarker(mapBase.markers, locations, removeAllMarker, type);
    },
    
    createMarker: function (latLng, location, image, index, bounds) {
    	var mapBase = this;
        var marker = new google.maps.Marker({
          position: latLng,
          map: mapBase.map,
          title: location.featureCodesAndValues.name,
          icon: image,
	            index: index
        });
        marker.entitydata = location;
        if (mapBase.mapFilter) {
          marker.entitydata.mapFilter = mapBase.mapFilter;
          marker.entitydata.mapLevel = mapBase.mapLevel;
        }
        mapBase.markers.push(marker);
        mapBase.setMarkerImages(marker);
        mapBase.addMarkerEventListener(marker, location);
        if (bounds) {
          bounds.extend(latLng);
          mapBase.map.fitBounds(bounds);
        }
    },

    createClusterMarkers: function ( ) {
    	var mapBase = this;
        mapBase.markerClustered = new MarkerClusterer(mapBase.map, mapBase.markers);
    },
    
    setMarkerImages: function (/*google.maps.Marker*/ marker) {
      // summary:
      //		Set marker image path.
      // description:
      // 		Set marker image, based on the marker entity.
      var mapBase = this, imagePath;
      switch (marker.entitydata.type) {
        case "locations":
          imagePath = "default";
          break;
        case "events":
          imagePath = "events";
          break;
        case "sights":
          imagePath = "sights";
          break;
        case "accommodations":
          imagePath = "hotel";
          break;
        case "hotels":
          imagePath = "hotel";
          break;
        case "villas":
          imagePath = "hotel";
          break;
        case "excursions":
          imagePath = "act";
          break;
        default:
          imagePath = "default";
      }
      mapBase.applyMarkerImages(marker, imagePath)
    },

    applyMarkerImages: function (marker, imagePath) {
      // summary:
      //		Apply image to marker.
      // description:
      // 		Set marker rollover state images.
      var mapBase = this, image = dojoConfig.paths.cdnDomain + '/images/' + dojoConfig.site + '/icon-map-' + imagePath;
      marker.imageon = image + '-on.png';
      marker.imageoff = image + '.png';
      marker.icon = marker.imageoff;
      marker.shadow = dojoConfig.paths.cdnDomain + '/images/' + dojoConfig.site + '/icon-map-shadow.png';
      google.maps.event.addListener(marker, "click", function () {
        mapBase.onMarkerClick(marker);
      })
    },

    onMarkerClick: function (marker) {
      var mapBase = this;
      if (mapBase.currentMarker) {
        mapBase.currentMarker.setZIndex(google.maps.Marker.MAX_ZINDEX);
        mapBase.currentMarker.setIcon(mapBase.currentMarker.imageoff);
      }
      marker.setIcon(marker.imageon);
      marker.setZIndex(google.maps.Marker.MAX_ZINDEX + 1);
      mapBase.currentMarker = marker;
    },

    addMarkerEventListener: function (/*google.maps.Marker*/ marker) {
      // summary:
      //		An abstract method for adding marker event listeners
      // description:
      //		Adds events to markers, this method should be overridden by
      //		concrete classes when needed.
    },

    removeMarkers: function () {
      // summary:
      //		Removes all markers from map.
      // description:
      //		Method which removes all markers and markerClusters object from map.
      var mapBase = this;
      if (mapBase.markers) {
        for (i in mapBase.markers) {
          mapBase.markers[i].setMap(null);
        }
        if (mapBase.markerClustered) {
          mapBase.markerClustered.clearMarkers();
        }
      }
      mapBase.markers.length = 0;
      //mapBase.clusterMarkers.length = 0;
    },

    displayMarkers: function (/*String*/ type, /*Boolean*/ display) {
      // summary:
      //		Displays or hides markers, and markerClusters on map.
      // description:
      //		Displays or hides markers, markerClusters depending on given
      //		arguments.
      //		If a marker type is speficed in arguments only that marker type is
      //		displayed or hidden.
      var mapBase = this;
      display = (display === undefined) ? true : display;
      if (mapBase.markers) {
        for (i in mapBase.markers) {
          if (mapBase.markers[i].entitydata.type === type) {
            mapBase.onBeforeDisplayMarkers(mapBase.markers[i], type, display);
            mapBase.markers[i].setVisible(display);
            if (!display) mapBase.markers[i].setIcon(mapBase.markers[i].imageoff);
            if (mapBase.markerClustered) {
              var action = (display) ? "addMarker" : "removeMarker"
              mapBase.markerClustered[action](mapBase.markers[i]);
            }
            mapBase.onAfterDisplayMarkers(mapBase.markers[i], type, display);
          }
        }
      }
    },

    showMarkerInCluster: function (marker, callback) {
      var mapBase = this;
      if (mapBase.markerClustered) {
        mapBase.markerClustered.removeMarker(marker);
        marker.setMap(mapBase.map);
        marker.setVisible(true);
      }
      callback();
    },

    zoomClusteredMarker: function (marker, callback) {
      var mapBase = this;
      callback = callback || function () {
      }
      // if marker is not visible we assume marker is in a cluster.
      // so we zoom to a level where it visible.
      if (mapBase.orignalzoom && !marker.getVisible()) {
        mapBase.map.setZoom(mapBase.orignalzoom);
        delete mapBase.orignalzoom;
      }
      if (!marker.getVisible()) {
        mapBase.orignalzoom = mapBase.map.getZoom();
      }
      var timer = null;

      function zoomIn() {
        clearTimeout(timer);
        if (!marker.getVisible()) {
          google.maps.event.addListenerOnce(mapBase.map, 'zoom_changed', function () {
            timer = setTimeout(zoomIn, 10);
          })
          var zoom = mapBase.map.getZoom();
          zoom++;
          mapBase.map.setZoom(zoom);
        } else {
          callback()
        }
      }

      zoomIn();
    },

    // ---------------------------------------------------------------- mapBase events

    onBeforeSetMarker: function () {
    },

    onAfterSetMarker: function () {
    },

    onBeforeDisplayMarkers: function (marker, type, display) {
    },

    onAfterDisplayMarkers: function (marker, type, display) {
    },

    onBeforeInitMap: function () {
    },

    onAfterInitMap: function () {
    }
  });

  return tui.widget.maps.MapBase;
});
