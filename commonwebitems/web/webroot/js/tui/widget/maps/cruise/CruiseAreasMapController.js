define("tui/widget/maps/cruise/CruiseAreasMapController", [
    'dojo/_base/declare',
    'dojo/query',
    'dojo/on',
    'dojo/dom-style',
    'dojo/dom-class',
    "tui/widget/maps/cruise/CruiseAreasMap",
    "tui/widget/maps/cruise/CruiseAreasStaticMap",
    "tui/widget/expand/cruise/CruiseMapMenuExpandable",
    'tui/widget/mixins/Templatable',
    'tui/widget/_TuiBaseWidget' ], function (declare, query, on, domStyle, domClass) {
     return declare ("tui.widget.maps.cruise.CruiseAreasMapController", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable], {

         clickNode:null,
         clickAction: "disable",
         data: null,
         cruiseAreaMap: null,
         crMExpandable: null, 
         cruiseAreaStaticMap: null,
         
         postMixInProperties: function () {
             // summary:
             //		Call before widget creation.
             // description:
             // 		Intialise given objects to their default states.
             var mapBase = this;
             mapBase.inherited(arguments);

         },

         postCreate: function () {
            var CMapController = this, subMenuNode, mapNode;
            
            CMapController.inherited(arguments);
            
             CMapController.clickNode = query(".toggle-icon",CMapController.domNode )[0];
             
             subMenuNode = query(".sub-menu",CMapController.domNode )[0];
             mapNode = query(".google-map",CMapController.domNode )[0];
            on( CMapController.clickNode, "click", function(){
                if( CMapController.clickAction == "disable") {
                    CMapController.clickAction =  CMapController.addDisable(subMenuNode,mapNode );
                }else {
                    CMapController.clickAction =  CMapController.addEnable(subMenuNode,mapNode );
                }
            });
        },

        crAreaEventHandler: function(liNode, index){
             var CMapController = this;
             on(liNode, "click", function(event){
                 if(index==0){
                     if(CMapController.data.destLandingMap) {
                    	domStyle.set(query(".google-map",CMapController.domNode )[0], "display","none");
                    	domStyle.set(query(".static-map",CMapController.domNode )[0], "display","block");
					 	domStyle.set(query(".map-icons",CMapController.domNode )[0], "display","block");
					 	domStyle.set(query(".cruiseTopx",CMapController.domNode )[0], "display","block");
					 	CMapController.cruiseAreaMap.cruiseAreaIndex = index-1;
					 }else{
                    	 CMapController.cruiseAreaMap.cruiseAreaIndex = index
                    	 CMapController.initiateGoogleMap();
                     }
					 
                 } else{
                     if(CMapController.data.destLandingMap) {
                    	 domStyle.set(query(".google-map",CMapController.domNode )[0], "display","block");
                    	 domStyle.set(query(".static-map",CMapController.domNode )[0], "display","none");
                    	 domStyle.set(query(".map-icons",CMapController.domNode )[0], "display","none");
                    	 CMapController.cruiseAreaMap.cruiseAreaIndex = index-1;
                    	 domStyle.set(query(".cruiseTopx",CMapController.domNode )[0], "display","none");
                     }else{
                    	 CMapController.cruiseAreaMap.cruiseAreaIndex = index
                     }
                     CMapController.initiateGoogleMap();
                 }
             });
         },
         
         fetchIndex: function (code){
        	 var CMapController = this, index;
        	_.each(CMapController.data.destinationViewDatas, function(item, i){
				 if (code === item.cruiseAreaCode) {
					 index = i
				 }
		    });
        	 return index;
         },
         
         initiateGoogleMap : function (){
        	 var CMapController = this;
        	 CMapController.cruiseAreaMap.setMarkers(CMapController.cruiseAreaMap.fetchLocations());
        	 CMapController.calcMapCenter();
             //CMapController.cruiseAreaMap.map.setZoom(2);
         },
         
        calcMapCenter : function (){
        	 var CMapController = this, center;
        	 center = CMapController.cruiseAreaMap.map.getCenter();
             google.maps.event.trigger(CMapController.cruiseAreaMap.map, "resize");
             CMapController.cruiseAreaMap.map.setCenter(center);
        },
         
         addEnable:function(subMenuNode,mapNode ){
             var CMapController = this, iteInfoNode;
             if(CMapController.data.destLandingMap) {
            	 iteInfoNode = query("div.itinerary-info", CMapController.domNode )[0];
            	 domStyle.set(iteInfoNode, "left","201px");
             }
             domStyle.set(subMenuNode, "display","block");
             domStyle.set(mapNode, "width","799px");
             domStyle.set(mapNode, "float","right");
             domStyle.set(CMapController.clickNode, "left","210px");
             domClass['add'](CMapController.clickNode, 'toggle-enabled');
             domClass['remove'](CMapController.clickNode, 'toggle-disabled');
             CMapController.calcMapCenter();
             return "disable";
         },
         addDisable:function(subMenuNode,mapNode ) {
             var CMapController = this, iteInfoNode;
             domStyle.set(subMenuNode, "display","none");
             domStyle.set(mapNode, "width","1000px");
             if(CMapController.data.destLandingMap) {
            	 iteInfoNode = query("div.itinerary-info", CMapController.domNode )[0];
            	 domStyle.set(iteInfoNode, "left","0px");
             }
             domStyle.set(mapNode, "float","left");
             domStyle.set(CMapController.clickNode, "left","10px");
             domClass['remove'](CMapController.clickNode, 'toggle-enabled');
             domClass['add'](CMapController.clickNode, 'toggle-disabled');
             CMapController.calcMapCenter();
            return "enable";
         }

    });
});