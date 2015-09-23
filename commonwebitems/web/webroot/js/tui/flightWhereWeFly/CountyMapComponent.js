define("tui/flightWhereWeFly/CountyMapComponent", [
	"dojo",
	"dojo/on",
	"dojo/dom",
	'dojo/query',
	'dojo/dom-class',
	'dojo/dom-style'
	], function (dojo, on, dom, query, domClass, domStyle) {

	dojo.declare("tui.flightWhereWeFly.CountyMapComponent",[], {



		postCreate:function(){
			var mapComponent = this,cnt=null;
			mapComponent.inherited(arguments);
		},


		displayAirports: function(obj){
			var mapComponent = this,airport,countryObj,latLng;

			if(query(".destinations")){
				query(".destinations").forEach(function(elm, i){
					if(domClass.contains(elm,"alldestinations")) return;
					domClass.add(elm,"hide");
					dojo.setStyle(dojo.byId("verticalScrollContent"),"top","0px");
					dojo.setStyle(dojo.query(".scrollUp")[0],"display","none");
				});
			}
			if(query("ul#verticalScrollContent li.active")){
				query("ul#verticalScrollContent li.active").forEach(function(elm, i){
					domClass.remove(elm, "active");
				});
			}

			domClass.remove(obj,"hide");
			domClass.add(obj,"active");
			dojo.setStyle(dojo.query(obj.children[1])[0], "display", "block");

			latLng = new google.maps.LatLng(obj.getAttribute("data-latitude"), obj.getAttribute("data-longitude"));
			mapComponent.map.panTo(latLng);
			mapComponent.map.setZoom(5);
		},

		showAirport : function(obj){
			var mapComponent = this,countryObj,latLng;

				query("ul#verticalScrollContent li.active").forEach(function(elm, i){
					domClass.remove(elm, "active");
				});

				domClass.add(obj.parentNode,"active");

				latLng = new google.maps.LatLng(obj.getAttribute("data-latitude"), obj.getAttribute("data-longitude"));
				mapComponent.map.panTo(latLng);
				mapComponent.map.setZoom(10);
		},


		showAllAirports : function(obj){
			var mapComponent = this,continentTab,continent;


				//dojo.setStyle(dojo.query(".destinations .active ul")[0], "display", "none");
				if(query(".destinations.active")[0]){
					domClass.remove(query(".destinations.active")[0],"active");
				}else if(query(".destinations .active")[0]){
					domClass.remove(query(".destinations .active")[0],"active");
				}

				domClass.add(obj,"active");

				query(".destinations").forEach(function(elm, i){
					if(domClass.contains(elm,"alldestinations")) return;
					domClass.remove(elm,"hide");
					dojo.setStyle(dojo.query(elm.children[1])[0], "display", "none");
				});

				mapComponent.continentMapZoom();


		}

	})
	return tui.flightWhereWeFly.CountyMapComponent;
});