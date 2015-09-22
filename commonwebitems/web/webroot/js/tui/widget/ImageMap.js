define("tui/widget/ImageMap", ["dojo", 
							   "tui/widget/_TuiBaseWidget", 
							   "tui/widgetFx/MoveableMapLimit", 
							   "dojo/fx",
							   "dojox/validate/_base"], function(dojo){

	dojo.declare("tui.widget.ImageMap", [tui.widget._TuiBaseWidget], {
		//---------------------------------------------------------------- properties
		
		fitviewport: true,
		
		markers: null,
		
		mapDom: null,
		
		mapBig: null,
		
		mapSmall: null,
		
		smallPos: null,
		
		bigPos: null,
		
		viewport: null,
		
		viewportPos: null,
		
		zoomBtn: null,
		
		zoomEvent: null,
		
		zoomAreaEvent: null,
		
		ratiox: 0,
		
		ratioy: 0,
		
		dragged: false,
		
		zooming: false,
		
		//---------------------------------------------------------------- methods
		getMapDomElements: function(){
			var imageMap = this;
			imageMap.mapDom = dojo.query(".hmap", imageMap.domNode)[0];
			imageMap.mapSmall = dojo.query(".imgSmall", imageMap.domNode)[0];
			imageMap.mapBig = dojo.query(".imgBig", imageMap.domNode)[0];
			imageMap.zoomBtn = dojo.query(".zoom", imageMap.domNode)[0];
			imageMap.viewport = dojo.query(".viewport", imageMap.domNode)[0];
			imageMap.markerlist = dojo.query(".markerlist", imageMap.domNode)[0];
		},
		
		getPositions: function(){
			var imageMap = this;
			imageMap.smallPos = dojo.position(imageMap.mapSmall);
			imageMap.bigPos = dojo.position(imageMap.mapBig);
			imageMap.viewportPos = dojo.position(imageMap.viewport, true);
			
		},
		removeEvents: function(){
			var imageMap = this;
			dojo.disconnect(imageMap.zoomAreaEvent);
			dojo.disconnect(imageMap.zoomEvent);
			imageMap.zoomAreaEvent = null;
			imageMap.zoomEvent = null
			
		},
		addEventZoomListener: function(){
			var imageMap = this;
			if (imageMap.zoomEvent) return;
			imageMap.zoomEvent = dojo.connect(imageMap.zoomBtn, "onclick", function (event){
				dojo.stopEvent(event);
				if (imageMap.dragged === true) return;
				imageMap.removeEvents();
					
				if (imageMap.fitviewport){
					imageMap.zoomIn();
				} else {
					imageMap.moveTo(0,0);
					imageMap.zoomOut();
				}
			});
		},
		
		addEventZoomAreaListener: function(){
			var imageMap = this;
			if (imageMap.zoomAreaEvent) return;
			imageMap.zoomAreaEvent = dojo.connect(imageMap.viewport, "onclick", function(event){
				dojo.stopEvent(event);
				if (imageMap.dragged === true) return;
				imageMap.removeEvents();	
				
				if (!imageMap.fitviewport) {
					imageMap.moveTo(0,0);
					imageMap.zoomOut();
					return;
				}
				var newx = (event.pageX - imageMap.viewportPos.x);
				var newy = (event.pageY  - imageMap.viewportPos.y);
				imageMap.onAreaClick(newx, newy);
				imageMap.zoomIn();
			})
		},
		
		onAreaClick: function(left, top){
			var imageMap = this;
			left = left * imageMap.ratiox;
			top = top * imageMap.ratioy;
			var moveToX = 0;
			var moveToY = 0;
			var x = Math.ceil(imageMap.ratiox);
			var y = Math.ceil(imageMap.ratioy);
				 
			for (var i = 0; i < x; i++){					
				if (dojox.validate.isInRange(left, 
						 {min : i * imageMap.smallPos.w, max : (i + 1) * imageMap.smallPos.w})){	
					moveToX = ((i + 1) === x) ? imageMap.bigPos.w - imageMap.smallPos.w : moveToX = i * imageMap.smallPos.w;
					break;
				}					
			}
			for (var i = 0; i < y; i++){
				if (dojox.validate.isInRange(top, 
						 {min : i * imageMap.smallPos.h, max : (i + 1) * imageMap.smallPos.h})){
					moveToY = ((i + 1) === y) ? imageMap.bigPos.h - imageMap.smallPos.h : moveToY = i * imageMap.smallPos.h;
					break;
				}				
			}
			imageMap.moveTo(-moveToX, -moveToY);	
		},
		
		postCreate: function(){
			var imageMap = this;
			imageMap.markers = [];
			imageMap.getMapDomElements();
			imageMap.getPositions();
			imageMap.jsonData = [{
				left: 1000,
				top: 380,
				name:"maker1"
			},{
				left: 1800,
				top: 200,
				name:"maker2"
			}]
			dojo.style(imageMap.mapDom,{ width: imageMap.bigPos.w + "px", 
										 height: imageMap.bigPos.h + "px" })
			imageMap.ratiox = imageMap.bigPos.w / imageMap.smallPos.w;
			imageMap.ratioy = imageMap.bigPos.h / imageMap.smallPos.h;
			imageMap.addEventZoomListener();
			imageMap.addEventZoomAreaListener();
			new tui.widgetFx.MoveableMapLimit(imageMap.mapDom,{
				viewportNode: imageMap.viewport,
				mapSmall: imageMap.mapSmall,
				onFirstMove: function(){
					imageMap.removeEvents();
					imageMap.dragged = true;
				},
				onMoveStop: function(){
					if (imageMap.dragged === true){
						var timer = setTimeout (function(){
							clearTimeout(timer);
							imageMap.addEventZoomListener();
							imageMap.addEventZoomAreaListener();
							imageMap.dragged = false;
						}, 100)
					}
				}
			})
			imageMap.plotMarkers();
		},
		
		setMarkerPosition: function(marker){
			var imageMap = this;
			var index = _.indexOf(imageMap.markers, marker);
			if (index === -1) return;
			var top = imageMap.jsonData[index].top;
			var left = imageMap.jsonData[index].left;
			top = (imageMap.fitviewport) ?  top / imageMap.ratioy : top;
			left = (imageMap.fitviewport) ? left / imageMap.ratiox : left;
			dojo.style(marker, {
				top: [top, "px"].join(""),
   				left: [left, "px"].join("")
			})
		},
			
		replotMakers: function(){
			var imageMap = this;	
			for(var i = 0; i < imageMap.markers.length; i++){
				var marker = imageMap.markers[i];
				imageMap.setMarkerPosition(marker)
			}
			imageMap.displayMarkers();
		},
		
		plotMarkers: function(){
			var imageMap = this;
			for(var i = 0; i < imageMap.jsonData.length; i++){
				var markerData = imageMap.jsonData[i];
				var marker = dojo.create("a", {
					href:"#",
   					className: "pin",
   					style:{position: "absolute"}  
   				}, imageMap.mapDom);
   				imageMap.markers.push(marker);
   				imageMap.setMarkerPosition(marker);
   				imageMap.addMarkerListener(marker);
   				imageMap.addToMarkerList(imageMap.jsonData[i], marker, i);
			}
		},
		
		addMarkerListener: function(marker){
			var imageMap = this;
			imageMap.connect(marker, "onmouseover", function(event){
				imageMap.removeEvents();
			})
			imageMap.connect(marker, "onmouseout", function(event){
				imageMap.addEventZoomListener();
				imageMap.addEventZoomAreaListener();
			})
		},
		
		addToMarkerList: function(markerData, marker, index){
			var imageMap = this;
			imageMap.markerlist
			var markerItem = dojo.create("li", {
   				innerHTML: ["<a href='#'>", markerData.name ,"</a>"].join("")
   			}, imageMap.markerlist, "last");
   			dojo.query("a", markerItem).connect("onclick", function(event){
   				dojo.query(".selected", imageMap.domNode).removeClass("selected");
   				dojo.addClass(marker, "selected");
   				if (!imageMap.fitviewport){
   					var left = markerData.left / imageMap.ratiox;
   					var top = markerData.top / imageMap.ratioy;
   					imageMap.onAreaClick(parseInt(left), parseInt(top))
   				}
   			})
		},
		
		displayMarkers: function(display){
			var imageMap = this;
			var action = "showWidget";
			if (display === false) action = "hideWidget";
			for(var i = 0; i < imageMap.markers.length; i++){
				var marker = imageMap.markers[i];
				imageMap[action](marker);
			}
		},
		
		zoomIn: function(){
			var imageMap = this;
			imageMap.displayMarkers(false);
			dojo.fx.animateProperty({
				node: imageMap.mapSmall,
				properties: {
      				height: {start:imageMap.smallPos.h, end:imageMap.bigPos.h},
      				width: {start:imageMap.smallPos.w, end:imageMap.bigPos.w},
      				opacity: {start:1, end:0.9}	
  				},
  				onEnd: function(){
  					dojo.style(imageMap.mapSmall, "display", "none");
  					dojo.style(imageMap.mapSmall, {width: imageMap.smallPos.w + "px", height: imageMap.smallPos.h + "px"});
  					dojo.style(imageMap.mapBig, "display", "block");
  					imageMap.fitviewport = false;
					imageMap.addEventZoomListener();
					imageMap.addEventZoomAreaListener();
					imageMap.replotMakers()
  				}}).play();
		},
		
		zoomOut: function(){
			var imageMap = this;
			imageMap.displayMarkers(false);
			dojo.fx.animateProperty({
				node: imageMap.mapBig,
				properties: {
      				height: {start:imageMap.bigPos.h, end:imageMap.smallPos.h},
      				width: {start:imageMap.bigPos.w, end:imageMap.smallPos.w}	
  				},
  				onEnd: function(){
  					dojo.style(imageMap.mapSmall, "display", "block");
					dojo.style(imageMap.mapSmall, "opacity", 1);
  					dojo.style(imageMap.mapBig, {width: imageMap.bigPos.w + "px", height: imageMap.bigPos.h + "px"});
  					dojo.style(imageMap.mapBig, "display", "none");
  					imageMap.fitviewport = true;
					imageMap.addEventZoomListener();
					imageMap.addEventZoomAreaListener();
					imageMap.replotMakers()
  				}}).play();
		},
		
		moveTo: function (left, top){
			var imageMap = this;
			dojo.fx.animateProperty({
				node: imageMap.mapDom,
				properties: {
					top: {start:dojo.style(imageMap.mapDom, "top"), end:top},	
      				left: {start:dojo.style(imageMap.mapDom, "left"), end:left}	
			}}).play();
		}
	})
	
	return tui.widget.ImageMap;
})
