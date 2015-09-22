define("tui/widget/maps/Retail/RetailMapBase", ["dojo",
  "google/maps/InfoBubble",
  "google/maps/Markerclusterer",
  "tui/widget/_TuiBaseWidget",
  "dojo/NodeList-traverse",
  "tui/widget/maps/MapBase"], function (dojo) {

  dojo.declare("tui.widget.maps.Retail.RetailMapBase", [tui.widget.maps.MapBase], {

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

    // name of website for placeholder images
    siteName: dojoConfig.site,

    // ---------------------------------------------------------------- mapBase methods
    postCreate: function () {
      // summary:
      //		Call after widget creation.
      // description:
      // 		Once map widget as been created, we intialise the google map with given data.
      var mapBase = this;
      mapBase.inherited(arguments);
    },



    applyMarkerImages: function (marker, imagePath) {
      // summary:
      //		Apply image to marker.
      // description:
      // 		Set marker rollover state images.
    	var mapBase=this;
    	
    	if(marker.entitydata.accommodationType==="HOTEL"){
    	if(marker.entitydata.aniteBrand === "T" ){
      var mapBase = this, image = dojoConfig.paths.webRoot + '/images/' + dojoConfig.site + '/icon-map-' + imagePath;
  
      marker.imageon = image + '-on.png';
      marker.imageoff = image + '.png';
      marker.icon = marker.imageoff;
      marker.shadow = dojoConfig.paths.webRoot + '/images/' + dojoConfig.site + '/icon-map-shadow.png';
    	}
    	else
    		{
    		
    		 var mapBase = this, image = dojoConfig.paths.webRoot + '/images/' + dojoConfig.site + '/icon-map-' + imagePath + '-fc';
    		  
    	      marker.imageon = image + '-on.png';
    	      marker.imageoff = image + '.png';
    	      marker.icon = marker.imageoff;
    	      marker.shadow = dojoConfig.paths.webRoot + '/images/' + dojoConfig.site + '/icon-map-shadow.png';
    		
    		}
    	}
    	else
    		{
    		var mapBase = this, image = dojoConfig.paths.webRoot + '/images/' + dojoConfig.site + '/icon-map-' + imagePath;
    		  
    	      marker.imageon = image + '-on.png';
    	      marker.imageoff = image + '.png';
    	      marker.icon = marker.imageoff;
    	      marker.shadow = dojoConfig.paths.webRoot + '/images/' + dojoConfig.site + '/icon-map-shadow.png';
    		
    		
    		}
    	
    	google.maps.event.addListener(marker, "click", function () {
        mapBase.onMarkerClick(marker);
      })
    },
  });

  return tui.widget.maps.Retail.RetailMapBase;
});
