define("tui/flights/view/WhereWeFlyBase", [
	"dojo",
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
	"dojo/NodeList-traverse",
	'dojo/domReady!'], function (dojo, lang, domConstruct, domClass, domStyle, domAttr, win, parser, query, fx, dom, event) {

	dojo.declare("tui.flights.view.WhereWeFlyBase",[tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable], {
		
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
			
			query("#verticalScrollContent > li").connect("onclick", whereWeFly.reloadMap);
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
            var maxTop = containerHeight - contentHeight - 20,
                parentNode = dom.byId("verticalScrollContent"),
                parentNodePos = dojo.marginBox(query("#verticalScrollContent")[0]),
                currentTop = parentNodePos.t,
                calcTop = currentTop - slide,
                newTop = 0;
            
         // Scroll Down
            if(domClass.contains(evt.currentTarget, "scrollDown")){
              if(maxTop >= calcTop) {
                newTop = maxTop; }
              else {
                newTop = currentTop-slide; }
            }
            
            // Scroll Up
            else {
              if((currentTop + slide) >= 0 ) {
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
            
            if(newTop == 0) domStyle.set(query(".scrollUp")[0], {
            	"display":"none"
            }); 
            	
            else domStyle.set(query(".scrollUp")[0], {
            	"display":"block"
            }); 
              

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
		}
		
	})
	
	return tui.flights.view.WhereWeFlyBase;
});