define("tui/flightWhereWeFly/MapComponent", [
	"dojo",
	"dojo/on",
	'dojo/_base/lang',
	'dojo/dom-construct',
	'dojo/dom-class',
	'dojo/dom-style',
	'dojo/dom-attr',
	"dojo/io-query",
	'dojo/_base/window',
	'dojo/parser',
	'dojo/query',
	"dojo/_base/fx",
	'dojo/fx',
	"dijit/registry",
	"tui/utils/TuiAnimations",
	"dojo/dom",
	"dojo/_base/event",
	"tui/searchPanel/view/flights/AirportListGrouping",
	"tui/searchPanel/model/flights/SearchPanelModel",
	"tui/flightWhereWeFly/model/WhereWeFlyPanelModel",
	"tui/searchPanel/model/AirportModel",
	"tui/searchPanel/model/DestinationModel",
	"tui/flightWhereWeFly/CountyMapComponent",
	"tui/flightWhereWeFly/MapNavigationComponent",
	"dojo/text!tui/flightWhereWeFly/templates/ContinentTabNavigationTmpl.html",
	"dojo/text!tui/flightWhereWeFly/templates/AllDestinationsTmpl.html",
	"tui/widget/popup/Tooltips",
	"google/maps/richmarker",
	"dojo/NodeList-traverse",
	'dojo/domReady!'], function (dojo, on, lang, domConstruct, domClass, domStyle, domAttr, ioQuery, win, parser, query, baseFx, fx,registry,
			tuiAnimations, dom, event,AirportListGrouping, searchPanelModel, mapPanelModel, AirportModel , DestinationModel, CountyMapComponent,
			MapNavigationComponent,TabNavigationTmpl,AllDestinationsTmpl,Tooltips) {

	dojo.declare("tui.flightWhereWeFly.MapComponent",[tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable,AirportListGrouping,CountyMapComponent,MapNavigationComponent], {


		map : null,

		markers : [],

		airportsData : null,

		clickInfoWindow :null,

		selectedCounties : null,

		markerIndex: -1,

		allContinents : [],



		postCreate:function(){
			var mapComponent = this,cnt=null;
				mapComponent.inherited(arguments);
				continentList = mapComponent.getContinentDetails(mapComponent.mapData.geoData);
				mapComponent.continentTabRender(continentList);
				mapComponent.allContinents = mapComponent.getAllContinents(mapComponent.mapData.geoData);
				mapComponent.allDestinationsRender(mapComponent.allContinents[0]);
				mapComponent.handleScrollButtons();
				mapComponent.initialize(mapComponent.allContinents[0]);
				mapComponent.continentMapZoom();
				mapComponent.attachEvent();

		},

		continentTabRender : function(continentList){
			var mapComponent = this,html,data;

				data = {
						continentList	: 	continentList
					};

				html = dojo.trim(tui.dtl.Tmpl.createTmpl(data, TabNavigationTmpl));
				dojo.html.set(query('#where-we-fly-tab-comp')[0],html,{});
				query("#allregions").addClass("activeTab");

		},


		allDestinationsRender : function(airportData){
			var mapComponent = this,html,data;

				data = {
						airportData	: 	airportData.countries,
						continentName : airportData.code == "allregions" ? "DESTINATIONS" : airportData.name.toUpperCase()
					};
				html = dojo.trim(tui.dtl.Tmpl.createTmpl(data, AllDestinationsTmpl));
				dojo.html.set(query('#allCountriesCnt')[0],html,{});
				mapComponent.destinationAttachEvent();
		},


		PanControl : function(controlDiv, map) {
			 var mapComponent = this;
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

		createMapObject : function(continent){
			var mapComponent = this,
				continent,
				mapOptions,
				myLatlng = new google.maps.LatLng(continent.latitude, continent.longitude);

				mapOptions = {
		   				center: myLatlng,
		   				zoom: 3,
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
		   		};

				mapComponent.map = new google.maps.Map(document.getElementById("list-of-airports"),mapOptions);
		},

		   initialize : function(continent) {
			   	var mapComponent = this,
			   		panControlDiv,
			   		panControl,
			   		continent;
			   		data = continent.countries;
			   		mapComponent.createMapObject(continent);

			   		mapComponent.map.addListener('zoom_changed', function() {
						   var z = mapComponent.map.getZoom();
						   if(mapComponent.clickInfoWindow){
								mapComponent.clickInfoWindow.close();
							}
						   console.log("zoom ==" +  z);
						   if(z >= 5){
							   mapComponent.reloadMapMarkers(data);
						   }else if(z <= 4){
							   mapComponent.setMapOnAll(null);
							   mapComponent.setMarkers( data);
						   }
					});

				   mapComponent.setMarkers(data);

				   panControlDiv = document.createElement('div');
				   panControl =  mapComponent.PanControl(panControlDiv, mapComponent.map);

				   panControlDiv.index = 1;
				   mapComponent.map.controls[google.maps.ControlPosition.BOTTOM_LEFT].push(panControlDiv);
			},

			continentMapZoom : function(){
				var mapComponent = this,
					continentTab,
					selectedContinent,
					latLng;

				 	continentTab = dojo.query(".continentTabs.activeTab")[0];
					selectedContinent = _.where(mapComponent.allContinents, {"code" : continentTab.id})
					latLng = new google.maps.LatLng(selectedContinent[0].latitude, selectedContinent[0].longitude);
					mapComponent.map.panTo(latLng);
					if(continentTab.id === "allregions"){
						mapComponent.map.setZoom(2);
					}else{
						mapComponent.map.setZoom(3);
					}
			},

			setMarkers  : function(groupCountries) {
				  var mapComponent = this;

				  _.each(groupCountries, function(countries){
					  if(countries.airports.length === 1){
						  mapComponent.singleAirportMarker(countries.airports[0]);
					  }else if(countries.airports.length > 1){
						  mapComponent.multiAirportMarker(countries)
					  }
				  });

			},

			addMarker : function(myLatLng, icon, locations,index){
				var mapComponent = this,marker,clickMsg;
					clickMsg = mapComponent.prepareContent(locations);
					marker = new RichMarker({
					position: myLatLng,
					map: mapComponent.map,
					icon: icon,
					title: locations.name + " (" + locations.code + ")",
					index: index,
					flat: true,
					clickInfo: clickMsg
				});

				mapComponent.setMarkerImages(marker);
				mapComponent.markers.push(marker);
				return marker;
			},
			addDblMarker: function(myLatLng, icon, airportName){
				var mapComponent = this,marker;

					marker = new google.maps.Marker({
					position: myLatLng,
					map: mapComponent.map,
					icon: icon
				});

				mapComponent.markers.push(marker);
				return marker;
			},

	        setMarkerImages: function(marker){
	        	var mapComponent = this;
	                  //stay icon
	              	marker.setContent(
	              			"<div class='my-marker'>" +
	              				"<div  class='map-icon-stay marker-"+marker.index+"' ></div>"+
	              			"</div>"
	              	);
	         },


			singleAirportMarker : function(locations){
				var mapComponent = this, marker ,myLatLng ,icon,conMsg,clickMsg,index;

					index=mapComponent.markerIndex++;

					myLatLng = new google.maps.LatLng(locations.latitude, locations.longitude);

					if(locations.available){
					 	icon = new google.maps.MarkerImage("images/FlightWhere-Ico.png",  new google.maps.Size(34, 48), new google.maps.Point(10, 135));

						marker = mapComponent.addMarker(myLatLng, icon, locations ,index);

						conMsg = locations.name + " (" + locations.code + ")";

						clickMsg = mapComponent.prepareContent(locations);

						mapComponent.attachInfoWindow(conMsg, marker, clickMsg);
					}

			},

			multiAirportMarker : function(locations){
				var mapComponent = this, myLatLng, icon,marker;

					myLatLng = new google.maps.LatLng(locations.latitude, locations.longitude);

					if(locations.available){
					 	icon = new google.maps.MarkerImage("images/FlightWhere-Ico.png",  new google.maps.Size(34, 44),	new google.maps.Point(10, 235));

					 	marker = mapComponent.addDblMarker(myLatLng, icon, locations.name );

					 	mapComponent.mouseOverEvent(marker);
					}
			},


			setMapOnAll : function(map) {
				var mapComponent = this,markers;
					markers = mapComponent.markers;

					  for (var i = 0; i < markers.length; i++) {
					    markers[i].setMap(map);
					  }
			},

			reloadMapMarkers : function(groupCountries){
				var mapComponent = this;

					//delete all markers from map
					mapComponent.setMapOnAll(null);

					  _.each(groupCountries, function(countries){
						  _.each(countries.airports, function(airports){
								mapComponent.singleAirportMarker(airports);
							})
					  })
			},

			mouseOverEvent : function(marker){
				var mapComponent = this
				  	tempIcon=null;

				google.maps.event.addListener(marker, 'mouseover', function() {
					tempIcon = new google.maps.MarkerImage("images/FlightWhere-Ico.png",  new google.maps.Size(34, 44),	new google.maps.Point(10, 286));
					marker.setIcon(tempIcon);

				});

				google.maps.event.addListener(marker, 'mouseout', function() {
					tempIcon = new google.maps.MarkerImage("images/FlightWhere-Ico.png",  new google.maps.Size(34, 44), new google.maps.Point(10, 235));
					marker.setIcon(tempIcon);
				});


			},

			prepareContent : function(airport){
				var mapComponent = this,html;
					html = "<div class='airport-popout' >";
					html += "<div data-airportmodel-id='" + airport.code + "' data-airportmodel-countrycode='" +airport.name+ "' class='airport-name'>" + airport.name + "&nbsp;(" + airport.code + ")</div>";
					html += "<div class='airport-desc'><a href='javascript:void(0);' class='airport-info'>Airport information</a>";
					html += "<div class='add-to airportAddtoSearch' data-airportmodel-id='" + airport.code + "'id='" + airport.code + "'><img src='images/white_arrow_right.gif' />ADD TO SEARCH</div>";
					return html;
			},

			attachInfoWindow : function(con, marker, msg){
				var mapComponent = this,
				  	tempIcon=null,
				  	popupWin=null;

					google.maps.event.addListener(marker, 'mouseover', function(evt) {
						var node = query("div.marker-"+marker.index, mapComponent.domNode.parentNode)[0];

							  if(mapComponent.clickInfoWindow && mapComponent.clickInfoWindow.anchor &&  mapComponent.clickInfoWindow.anchor.title !== marker.title){
								  if(node.id !="") {
									  var widget = registry.byNode(node);
									  widget.open();
								  } else {
									  mapComponent.openTooltip(node,marker);
								  }
								}else if(mapComponent.clickInfoWindow &&  mapComponent.clickInfoWindow.anchor &&  mapComponent.clickInfoWindow.anchor.title === marker.title){
									return false;
								}else{
									 if(node.id !="") {
										  var widget = registry.byNode(node);
										  widget.open();
									} else{
										mapComponent.openTooltip(node,marker);
									}
								}
					});


					google.maps.event.addListener(marker, 'click', function() {
						if(mapComponent.clickInfoWindow){
							mapComponent.clickInfoWindow.close();
						}

						mapComponent.clickInfoWindow = new google.maps.InfoWindow({
							content: msg
						});

						mapComponent.clickInfoWindow.open(mapComponent.map,marker);

						var node = query("div.marker-"+marker.index, mapComponent.domNode.parentNode)[0];
						if(node.id != ""){
							if(registry.byNode(node) !== undefined){
								registry.byNode(node).destroyRecursive(true);
								node.id="";
							}
						}

						google.maps.event.addDomListener(mapComponent.clickInfoWindow, 'domready', function() {
							popupWin = dojo.query(".airport-popout").closest(".gm-style-iw");
							mapComponent.applyStylesToPopup(popupWin, "#fff", "block","0px", "9px");
							dojo.query('.airportAddtoSearch').on("click",function(event) {
								mapComponent.getSelectedAirports(event, this);
						    });
						});
					});
			},

			openTooltip : function(node,marker){
				var mapComponent = this,tip;
				  	tip = new Tooltips({
	 				    text: marker.title,
	 				    floatWhere:'position-top-center',
	 				    className: "map-icon-stay marker-"+marker.index
	 			     }, node);
	 			     tip.open();
			},

			callInfoWin : function(infowindow,marker){
				var mapComponent= this;
					infowindow.open(mapComponent.map,marker);
					popupWin = dojo.query(".airport-name-onhover").closest(".gm-style-iw");
					mapComponent.applyStylesToPopup(popupWin, "#656665", "none", "5px", "30px")
			},

			applyStylesToPopup : function(popupWin, bgColor, display, padding, top){
				var popWinChild = null,
			  		PopWinSubChild1 = null,
			  		PopWinSubChild2 = null,
			  		posTop;

					posTop = parseInt(dojo.trim(dojo.getStyle(popupWin[0].parentNode).top.replace("px",""))) - 37;
					dojo.style(popupWin.prev().children(':nth-child(2)')[0], "display", display);
					dojo.style(popupWin.prev().children(':nth-child(4)')[0], "display", display);
					dojo.style(popupWin[0], "padding", padding);
					dojo.style(popupWin[0], "top", top);
					dojo.style(popupWin[0], "background-color", bgColor);

					popWinChild =popupWin.prev().children(':nth-child(3)');
					PopWinSubChild1=popWinChild.children(':nth-child(1)');
					PopWinSubChild2=popWinChild.children(':nth-child(2)');
					dojo.style(PopWinSubChild1.children(),"background-color",bgColor);
					dojo.style(PopWinSubChild2.children(),"background-color",bgColor);
					dojo.style(popupWin[0].parentNode, "top", posTop+"px");


			},
			getSelectedAirports : function(evt, srcElement){
				var mapComponent = this,
					searchPanelModel,
					airportId= evt.target.id,
					airportResults,
					params,
					airport,
					data,
					targetUrl="ws/validatearrivalairport?";

					searchPanelModel =	mapComponent.searchPanelModel;
					mapPanelModel =	mapComponent.mapPanelModel;

					mapPanelModel.from = searchPanelModel.from.data;
					mapPanelModel.to = airportId;
					mapPanelModel.date = searchPanelModel.date;
					mapPanelModel.returnDate = searchPanelModel.returnDate;


					if(mapPanelModel.from  &&  (mapPanelModel.from.length > 0 && mapPanelModel.from[0].countryCode === "GBR")){
						params = mapPanelModel.parserJsonData();

						airportResults = dojo.xhr("GET", {
  							url: targetUrl+ ioQuery.objectToQuery(params),
  							handleAs: "json"
  						});
						dojo.when(airportResults, function (data) {
							airport = mapComponent.createAirportModelObject(data);
							mapComponent.animatePill(airport, srcElement);

							if(data.available && data.routeAvailable){
								mapComponent.updatedFlyingTo(airport);
							}else{
								mapComponent.resetSearchPanelValue(airport);
							}
				  	      });
					}else{
						mapPanelModel.from = "";
						mapPanelModel.date = "";
						mapPanelModel.returnDate = "";

						params = mapPanelModel.parserJsonData();

						airportResults = dojo.xhr("GET", {
  							url: targetUrl+ ioQuery.objectToQuery(params),
  							handleAs: "json"
  						});
						dojo.when(airportResults, function (data) {
							if(data.available && !data.routeAvailable){
								airport = mapComponent.createAirportModelObject(data);
								mapComponent.animatePill(airport, srcElement);
								mapComponent.resetSearchPanelValue(airport);
							}
				  	      });
						}
			},



			createAirportModelObject : function(airport){
				var mapComponent = this,
					airports = [];

					airports.push(new AirportModel({
			              id: airport.id,
			              name: airport.name,
			              group: airport.group,
			              synonym: airport.synonym,
			              children: airport.children,
			              countryCode :  airport.countryCode,
			              routeAvailable : airport.routeAvailable,
			              available 	: airport.available
				    }));
					return airports;
			},


			resetSearchPanelValue : function(airport){
				var mapComponent = this;

					mapComponent.searchPanelModel.to.emptyStore();
					mapComponent.searchPanelModel.from.emptyStore();
					mapComponent.searchPanelModel.set("date", "");
					mapComponent.searchPanelModel.set("returnDate", "");


					dojo.query("#wherefromValue").style("display","none");
					dojo.query("#wherefromPlaceholder").style("display","block");

					dojo.query("#wheretoValue").style("display","none");
					dojo.query("#wheretoPlaceholder").style("display","block");

					mapComponent.updatedFlyingTo(airport);

			},

			updatedFlyingTo : function(airport){
				var mapComponent = this;
					mapComponent.searchPanelModel.to.emptyStore();
					mapComponent.searchPanelModel.to.setStorageData(airport,AirportModel);

					dojo.query("#wheretoValue").style("display","block");
					dojo.query("#wheretoValue").text(airport[0].name  +"  (" + airport[0].id + ")");
					dojo.query("#wheretoPlaceholder").style("display","none");
					window.scrollTo(0,0);
			},


			animatePill: function (airport, srcElement ) {
				var mapComponent = this,
					text = airport[0].name  +"  (" + airport[0].id + ")",
					srcPos = dojo.position(srcElement, true),
					dstElement = dojo.query("#where-to-text")[0],
					pillElement;

					pillElement = dojo.create("div", {
					"innerHTML": "<span>" + text + "</span>",
					"class": "flying-pill",
					"style": {"top": srcPos.y + "px", "left": srcPos.x + "px"}
					}, dojo.body(), "last");

				tuiAnimations.animateTo(pillElement, dstElement, null, null, [baseFx.fadeOut({
					node: pillElement,
					duration: 300,
					onEnd: function (node) {
						dojo.destroy(node);

					}
				})]);

				mapComponent.clickInfoWindow.close();
			},

			showAvailableAiports : function(mapData){
				var mapComponent = this,
					availableAirports,
					continentTab,
					continent,
					selectedContinent,
					latLng;

					availableAirports = mapComponent.getAirportSwapList(mapData);
					availableAirports = mapComponent.getOverseasairport(availableAirports);

					continent = mapComponent.allContinents[0];

					_.each(continent.countries,function(countries){
						_.each(countries.airports, function(airports){
							_.each(availableAirports, function(availaAiports){
								if(airports.code === availaAiports.id){
									airports.available = availaAiports.available;
								}
							});

						});
					});
					continent.countries = mapComponent.checkCountryAvailable(continent.countries)
					mapComponent.initialize(continent);
					mapComponent.continentMapZoom();

			}

	})

	return tui.flightWhereWeFly.MapComponent;
});