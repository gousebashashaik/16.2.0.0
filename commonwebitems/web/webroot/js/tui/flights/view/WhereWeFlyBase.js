define("tui/flights/view/WhereWeFlyBase", [
	"dojo",
	"dojo/on",
	'dojo/_base/lang',
	'dojo/dom-construct',
	'dojo/dom-class',
	'dojo/dom-style',
	'dojo/dom-attr',
	'dojo/_base/window',
	'dojo/parser',
	'dojo/query',
	"dojo/_base/fx",
	"dojo/dom",
	"dojo/_base/event",
	"tui/searchPanel/view/flights/AirportListGrouping",
	"dojo/NodeList-traverse",
	'dojo/domReady!'], function (dojo,on, lang, domConstruct, domClass, domStyle, domAttr, win, parser, query, fx, dom, event,AirportListGrouping) {

	dojo.declare("tui.flights.view.WhereWeFlyBase",[tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable,AirportListGrouping], {


		 latlongs : {
				"allregions":[38.927527, -7.043243],
				"africa":[12.699583, 20.642244],
				"asia":[40.580584664, 82.08984375],
				"caribbean" :[20.920396914, -86.616210938],
				"europe":[54.648412502, 25.268554688],
				"centralamerica":[7.362466866, -75.9375],
				"northamerica":[37.09024, -95.712891]
		},

		map : null,

		markers : [],


		constructor: function(){
			var whereWeFly = this;
			whereWeFly.inherited(arguments);
		},

		postCreate:function(){
			var whereWeFly = this;
			whereWeFly.inherited(arguments);

			dojo.xhrGet({
				  url: "loadMap",
				  handleAs: "json",
				  sync: true,
				  load: function(data, ioargs){
					  dojo.global.airportsData = data;
					  whereWeFly.initialize(data.airportMapData);
					  whereWeFly.handleScrollButtons();
					  query(".scrollButton").on("click", function(evt){
						  	event.stop(evt);
							whereWeFly.scrollContent(evt);
						});

						query("#filterAirports").on("click", function(){
							whereWeFly.filterAirports();
						});
				  },
				  error: function(error, ioargs){

				  }
			});

			//query("#verticalScrollContent > li").connect("onclick", whereWeFly.reloadMap);
			query(".destinations").connect("onclick",whereWeFly.displayAirports);
		},
		handleScrollButtons:function(){
			var contentHeight = 0,
				container = dojo.marginBox(query("#verticalScroll")[0]),
				containerHeight = container.h;//get the height of container
            query("#verticalScrollContent > li").forEach(function(node){
               var currentNode = dojo.marginBox(node);
               contentHeight += currentNode.h;
            });

            if(contentHeight < containerHeight) {
            	domStyle.set(query(".scrollDown")[0], {
            	"display":"none"
            	});
            }
            else{
            	domStyle.set(query(".scrollDown")[0], {
            	"display":"block"
            	});
            }


		},
//*********function to display airports list under a country
		displayAirports: function(evt){
			var whereWeFly = this;
			var targetObj = evt.target || evt.srcElement;
			var parentObj = query(targetObj).parent()[0];
			if(query(".destinations")){
				query(".destinations").forEach(function(elm, i){
					if(domClass.contains(elm,"alldestinations")) return;
					domClass.add(elm,"hide");
				});
			}
			if(query("ul#verticalScrollContent li.active")){
				query("ul#verticalScrollContent li.active").forEach(function(elm, i){
					domClass.remove(elm, "active");
				});
			}
			domClass.remove(query(targetObj).parent()[0],"hide");
			domClass.add(query(targetObj).parent()[0],"active");
			domClass.add(query(targetObj).parent().children().children(),"show");
			dojo.setStyle(dojo.query(".destinations.active ul")[0], "display", "block");

			on(dojo.query(".alldestinations")[0],"click",function(){
				domClass.add(this,"active");
				dojo.setStyle(dojo.query(".destinations.active ul")[0], "display", "none");
				domClass.remove(query(".destinations.active")[0],"active");
				if(query(".destinations")){
					query(".destinations").forEach(function(elm, i){
						if(domClass.contains(elm,"alldestinations")) return;
						domClass.remove(elm,"hide");
					});
				}
			});
		},

		reloadMap: function(evt){

			if(query("ul#verticalScrollContent li.active")){
				query("ul#verticalScrollContent li.active").forEach(function(elm, i){
					domClass.remove(elm, "active");
				});
			}

			if(query("ul#verticalScrollContent li.openToggle"))
				query("ul#verticalScrollContent li.openToggle").forEach(function(elm, i){
					domClass.remove(elm, "openToggle");
				});


			var targetObj = evt.target || evt.srcElement;
			var parentObj = query(targetObj).parent()[0];

			domClass.add(parentObj, "openToggle");domClass.add(parentObj, "active");

			if(domClass.contains(targetObj, "chkOpenToggle")){
				domClass.add(query(targetObj).parent().parent().parent()[0], "active");
				domClass.add(query(targetObj).parent().parent().parent()[0], "openToggle");
			}

			var obj = airportsData.airportMapData;

	        var iataCode = domAttr.get(query(evt.target)[0], "data-airport-code");

	        if(!iataCode){
	        	iataCode = domAttr.get(query("ul li:first-child a",parentObj)[0], "data-airport-code")
	        	map.setZoom(8);
	        }else{
	        	map.setZoom(13);
	        }


	        if(iataCode){
	        	var arr = _.where(obj,  {"airportCode": iataCode});

	            var latitude = arr[0].latitude[0];
	            var longitude = arr[0].longitude[0];

	            var latLng = new google.maps.LatLng(latitude, longitude);
	            map.panTo(latLng);
	        }else return;

		},

		scrollContent: function(evt){
			var contentHeight = 0;
            query("#verticalScrollContent > li").forEach(function(node){
               var currentNode = dojo.marginBox(node);
               contentHeight += currentNode.h;
            });
            // Define and Scroll settings
            var liElm = query("#verticalScrollContent > li"),
                list = dojo.marginBox(liElm[0]),
                container = dojo.marginBox(query("#verticalScroll")[0]),
                containerHeight = container.h,
                slide = 2 * list.h; // Slide length
            // Calculating the top position
            var maxTop = containerHeight - contentHeight-20,
                parentNode = dom.byId("verticalScrollContent"),
                parentNodePos = dojo.marginBox(query("#verticalScrollContent")[0]),
                currentTop = parentNodePos.t,
                calcTop = currentTop - slide,
                newTop = 0;
         // Scroll Down
            if(domClass.contains(evt.currentTarget, "scrollDown")){
	              if(maxTop >= calcTop) {
	            	  newTop = maxTop;
	              }else {
	            	  newTop = currentTop-slide;
	              }
            }else { // Scroll Up
              if((currentTop + slide) >= 0 ){
                newTop = 0;
              }
              else {
                newTop = currentTop+slide;
              }
            }
            /*query(".scrollDown").forEach(function(node){
               if(domClass.contains(node, "reverse")){
                  calcTop = currentTop + slide;
                  // Flag if reverse
                  if(calcTop >= 0) {
                    dojo.query(".scrollDown").removeClass("reverse");
					}
                  else {
                    newTop = currentTop+slide; }
               }
               else {
                  if(maxTop >= calcTop) { newTop = maxTop;
                    dojo.query(".scrollDown").addClass("reverse");
					}
                  else {
                    newTop = currentTop-slide; }
               }
            });*/

            // Animating top property
            fx.animateProperty({node: "verticalScrollContent", duration: 500,properties: {top: { start: currentTop, end: newTop }}}).play();
            if(newTop == 0){//scroll up button is not visible if content top is at 0px
            	domStyle.set(query(".scrollUp")[0], {"display":"none"});
            } else {
            	domStyle.set(query(".scrollUp")[0], {"display":"block"});
            }

            if(newTop <= maxTop) {//scroll down button is not visible if content top reaches max
            	domStyle.set(query(".scrollDown")[0], {"display":"none"});
            }
            else{
            	domStyle.set(query(".scrollDown")[0], {"display":"block"});
            }
		},

		filterAirports: function(){
			var whereWeFly = this;

			var fromAir = query("#flying-from-filter")[0].value;
			 var fromAirValue = "";
			 var continent =  "";
			 var departureDate = "";
			 var arrivalDate = "";
			 var flyingFrom = "";
			 if(fromAir.length > 0)
				if(fromAir.indexOf("(") !== -1) fromAirValue = fromAir.split("(")[1].split(")")[0];
			 flyingFrom = fromAirValue;

			 continent = whereWeFly.returnQueryString("continent");
			 if(query("#ftselectedMonth")[0]){
				 var chkFlag = query("#ftselectedMonth")[0].value;
			 }
			 if(chkFlag === "0"){
				 departureDate = "01-04-2014";
				 arrivalDate  = "30-09-2014";
			 }else if(chkFlag === "1"){
				 departureDate = "01-10-2014"
				 arrivalDate  = "31-03-2015";
			 }

			 var loc = location.href.split("?")[0]
			 var queryString = "?continent="+continent+"&flyingFrom="+flyingFrom+"&departureDate="+departureDate+"&arrivalDate="+arrivalDate;

			 location.href = loc + queryString;
		},

		returnQueryString: function(name){
			  name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
			    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
			        results = regex.exec(location.search);
			    return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
		},

		PanControl : function(controlDiv, map) {
			 var whereWeFly = this;
			  var controlUI = document.createElement('div');
			  controlUI.style.background = 'url(images/FlightWhere-Ico.png) no-repeat -10px -59px';
			  controlUI.style.cursor = 'pointer';
			  controlUI.style.height = '28px';
			  controlUI.style.width = '28px';
			  controlDiv.appendChild(controlUI);

			  google.maps.event.addDomListener(controlUI, 'click', function() {
				//pan map to left
				map.panBy(-100,0);
			  });

		},



		   initialize : function(data) {
			   	var whereWeFly = this,
			   		continent,
			   		myLatlng,
			   		mapOptions,
			   		panControlDiv,
			   		panControl;

			   		continent = whereWeFly.returnQueryString("continent");
			   		myLatlng = new google.maps.LatLng(whereWeFly.latlongs[continent][0], whereWeFly.latlongs[continent][1]);

			   		mapOptions = {
			   				center: myLatlng,
			   				zoom: 3,
			   				mapTypeId: google.maps.MapTypeId.ROADMAP
			   				};

			   		allCountries = whereWeFly.getMapCountryNames(data);

			   		groupCountries = whereWeFly.setGoogleMapAirportMap(data , allCountries);

			   		map = new google.maps.Map(document.getElementById("list-of-airports"),mapOptions);

					map.addListener('zoom_changed', function() {
						   var z = map.getZoom();

						   console.log("zoom ==" +  z);
						   if(z === 4){
							   whereWeFly.reloadMapMarkers(map, data);
						   }else if(z === 2){
							   whereWeFly.setMapOnAll(null);
							   whereWeFly.setMarkers(map, groupCountries);
						   }

					});

				   if(continent === "allregions"){
					   map.setZoom(2);
				   }

				   whereWeFly.setMarkers(map, groupCountries);

				   panControlDiv = document.createElement('div');
				   panControl =  whereWeFly.PanControl(panControlDiv, map);

				   panControlDiv.index = 1;
				   map.controls[google.maps.ControlPosition.BOTTOM_LEFT].push(panControlDiv);
			},

			setMarkers  : function(map, groupCountries) {
				  var whereWeFly = this;

				  _.each(groupCountries, function(countries){
					  if(countries.airports.length === 1){
						  whereWeFly.singleAirportMarker(map ,countries.airports[0]);
					  }else if(countries.airports.length > 1){
						  whereWeFly.multiAirportMarker(map ,countries.airports[0])
					  }
				  });

			},

			addMarker : function(myLatLng, icon, airportName){
				var whereWeFly = this,marker;

					marker = new google.maps.Marker({
					position: myLatLng,
					map: map,
					icon: icon,
					title: airportName
				});

				whereWeFly.markers.push(marker);

				return marker
			},

			singleAirportMarker : function(map, locations){
				var whereWeFly = this, marker ,myLatLng ,icon,conMsg,clickMsg;

					myLatLng = new google.maps.LatLng(locations.latitude, locations.longitude);

				 	icon = new google.maps.MarkerImage("images/FlightWhere-Ico.png",  new google.maps.Size(34, 48), new google.maps.Point(10, 135));

					marker = whereWeFly.addMarker(myLatLng, icon, locations.airportName );

					conMsg = locations.airportName + " (" + locations.airportCode + ")";

					clickMsg = whereWeFly.prepareContent(locations);

					whereWeFly.attachInfoWindow(conMsg, marker, clickMsg);

			},

			multiAirportMarker : function(map, locations){
				var whereWeFly = this, myLatLng, icon,marker;

				 	myLatLng = new google.maps.LatLng(locations.latitude, locations.longitude);

				 	icon = new google.maps.MarkerImage("images/FlightWhere-Ico.png",  new google.maps.Size(34, 45),	new google.maps.Point(10, 235));

				 	marker = whereWeFly.addMarker(myLatLng, icon, locations.airportName );

				 	whereWeFly.mouseOverEvent(marker);
			},


			setMapOnAll : function(map) {
				var whereWeFly = this,markers;
					markers = whereWeFly.markers;

					  for (var i = 0; i < markers.length; i++) {
					    markers[i].setMap(map);
					  }
			},

			reloadMapMarkers : function(map, locations){
				var whereWeFly = this;

					//delete all markers from map
					whereWeFly.setMapOnAll(null);

					for(var i=0; i < locations.length; i++){
						whereWeFly.singleAirportMarker(map,locations[i]);
					}

			},

			mouseOverEvent : function(marker){
				var whereWeFly = this
				  	tempIcon=null;

				google.maps.event.addListener(marker, 'mouseover', function() {
					tempIcon = new google.maps.MarkerImage("images/FlightWhere-Ico.png",  new google.maps.Size(34, 45),	new google.maps.Point(10, 286));
					marker.setIcon(tempIcon);

				});

				google.maps.event.addListener(marker, 'mouseout', function() {
					tempIcon = new google.maps.MarkerImage("images/FlightWhere-Ico.png",  new google.maps.Size(34, 45), new google.maps.Point(10, 235));
					marker.setIcon(tempIcon);
				});


			},

			prepareContent : function(airport){
				var whereWeFly = this,html;
					html = "<div class='airport-popout'>";
					html += "<div data-airportmodel-id='" + airport["airportCode"] + "' data-airportmodel-countrycode='" +airport["country"]+ "' class='airport-name'>" + airport["airportName"] + "&nbsp;(" + airport["airportCode"] + ")</div>";
					html += "<div class='airport-desc'><a href='#' class='airport-info'>Airport information</a>";
					html += "<div class='add-to'><img src='images/white_arrow_right.gif' />ADD TO SEARCH</div>";
					return html;
			},

			attachInfoWindow : function(con, marker, msg){
				  var whereWeFly = this,
				  tempIcon=null;

				var infowindow = new google.maps.InfoWindow({
					content: con
				});

				var clickInfoWindow = new google.maps.InfoWindow({
					content: msg
				});

				google.maps.event.addListener(marker, 'mouseover', function() {
					tempIcon = new google.maps.MarkerImage("images/FlightWhere-Ico.png",  new google.maps.Size(34, 48),
							new google.maps.Point(10, 186));
					marker.setIcon(tempIcon);
					infowindow.open(map,marker);
				});

				google.maps.event.addListener(marker, 'mouseout', function() {
					tempIcon = new google.maps.MarkerImage("images/FlightWhere-Ico.png",  new google.maps.Size(34, 48),
							new google.maps.Point(10, 135));
					marker.setIcon(tempIcon);
				    infowindow.close();
				});

				google.maps.event.addListener(marker, 'click', function() {
					clickInfoWindow.close();
					infowindow.close();
					tempIcon = new google.maps.MarkerImage("images/FlightWhere-Ico.png",  new google.maps.Size(34, 48),
							new google.maps.Point(10, 186));
					marker.setIcon(tempIcon);
					clickInfoWindow.open(map,marker);
				});


			}



	})

	return tui.flights.view.WhereWeFlyBase;
});