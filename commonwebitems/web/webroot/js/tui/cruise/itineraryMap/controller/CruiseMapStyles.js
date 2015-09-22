
define("tui/cruise/itineraryMap/controller/CruiseMapStyles", [ "dojo" ],

		function (dojo) {

			var clusterStyles = [
			    	 	  {
				 	        url: dojoConfig.paths.cdnDomain+'/images/' + dojoConfig.site + '/icon-map-cluster.png',
				 	        textColor: '#ffffff ',
				 	        textSize:15,
				 	        height: 60,
				 	        width: 60
				 	      },
				 	      {
				 	        url: dojoConfig.paths.cdnDomain+'/images/' + dojoConfig.site + '/icon-map-cluster.png',
				 	       textColor: '#ffffff ',
				 	        textSize:15,
				 	        height: 60,
				 	        width: 60
				 	      },
				 	      {
				 	        url: dojoConfig.paths.cdnDomain+'/images/' + dojoConfig.site + '/icon-map-cluster.png',
				 	       textColor: '#ffffff ',
				 	        textSize:15,
				 	        height: 60,
				 	        width: 60
				 	      },
				 	      {
				 	        url: dojoConfig.paths.cdnDomain+'/images/' + dojoConfig.site + '/icon-map-cluster.png',
				 	       textColor: '#ffffff ',
				 	        textSize:15,
				 	        height: 60,
				 	        width: 60
				 	      }];

				var mapStyles = [
							 	{
							       "featureType": "water",
							       "elementType": "geometry.fill",
							       "stylers": [
							         { "color": "#b9d7ed" }
							       ]
							     },{
							       "featureType": "landscape.man_made",
							       "stylers": [
							         { "visibility": "off" }
							       ]
							     },{
							       "featureType": "landscape.natural",
							       "stylers": [
							         { "color": "#f5f3f3" }
							       ]
							     },{
							       "featureType": "poi.park",
							       "stylers": [
							         { "visibility": "off" }
							       ]
							     },{
							       "featureType": "road",
							       "stylers": [
							         { "visibility": "off" }
							       ]
							     },{
							       "featureType": "administrative.country",
							       "elementType": "geometry.stroke",
							       "stylers": [
							         { "weight": 0.4 },
							         { "color": "#bababa" }
							       ]
							     },{
							       "featureType": "administrative.country",
							       "elementType": "labels.text.stroke",
							       "stylers": [
							         { "visibility": "off" }
							       ]
							     },{
							       "featureType": "administrative.country",
							       "elementType": "labels.text.fill",
							       "stylers": [
							         { "color": "#999999" }
							       ]
							     },{
							       "featureType": "administrative.province",
							       "stylers": [
							         { "visibility": "off" }
							       ]
							     },{
							       "featureType": "administrative.locality",
							       "elementType": "labels.text.stroke",
							       "stylers": [
							         { "visibility": "off" }
							       ]
							     },{
							       "featureType": "administrative.locality",
							       "elementType": "labels.text.fill",
							       "stylers": [
							         { "color": "#999999" }
							       ]
							     },{
							       "featureType": "water",
							       "elementType": "labels.text.stroke",
							       "stylers": [
							         { "visibility": "off" }
							       ]
							     },{
							       "featureType": "water",
							       "elementType": "labels.text.fill",
							       "stylers": [
							         { "color": "#999999" }
							       ]
							     },{
							       "featureType": "administrative.neighborhood",
							       "stylers": [
							         { "visibility": "off" }
							       ]
							     },{
							       "featureType": "transit",
							       "stylers": [
							         { "visibility": "off" }
							       ]
							     },{
							       "featureType": "administrative.land_parcel",
							       "stylers": [
							         { "visibility": "off" }
							       ]
							     },{
							       "featureType": "administrative.locality",
							       "elementType": "labels.icon",
							       "stylers": [
							         { "color": "#bdbdbd" }
							       ]
							     },{
							       "featureType": "landscape.natural",
							       "elementType": "labels.text.fill",
							       "stylers": [
							         { "color": "#999999" }
							       ]
							     }
							 ];


			var styles = {
					"clusterStyles": clusterStyles,
					"mapStyles": mapStyles
				};

			return styles;
    });


