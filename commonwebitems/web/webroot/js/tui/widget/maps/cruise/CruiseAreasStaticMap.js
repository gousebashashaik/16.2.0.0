

define("tui/widget/maps/cruise/CruiseAreasStaticMap", [
	'dojo',
	'dojo/text!tui/widget/maps/cruise/template/CruiseAreasStaticMapTmpl.html',
	'dojo/on',
	"dojo/_base/array",
    "dojo/mouse",
    'tui/widget/mixins/Templatable',
	"tui/widget/_TuiBaseWidget"], function (dojo, crStaticMapTmpl, on, array, mouse) {
	dojo.declare("tui.widget.maps.cruise.CruiseAreasStaticMap", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable], {

		data: null,

		pageId: '',

		tmpl: crStaticMapTmpl,

		crStaticDataList:[],

		mainData :[
		            {"crCode": "L04301", "crName":"NORTH AFRICA & MIDDLE EAST", "divClassName":"big-10", "spanClassName": "nor-af-mid-est", "topPosData":"167px", "rightPosData":"17px", "position":"1" },
		            {"crCode": "L34386", "crName":"CANARY ISLANDS & ATLANTIC", "divClassName":"big-10", "spanClassName": "can-isl-the-atln", "topPosData":"175px", "rightPosData":"314px", "position":"3" },
					{"crCode":"L04304", "crName":"CARIBBEAN", "divClassName":"medium-10", "spanClassName": "the-caribben", "topPosData":"246px", "rightPosData":"620px", "position":"2" },
					{"crCode": "L04305", "crName":"CENTRAL AMERICA", "divClassName":"medium-10", "spanClassName": "cen-america", "topPosData":"207px", "rightPosData":"714px", "position":"7" },
		            {"crCode": "L34371", "crName":"BLACK SEA", "divClassName":"small", "spanClassName": "the-black-sea", "topPosData":"98px", "rightPosData":"5px", "position":"5" },
		            {"crCode": "L34374", "crName":"BALTICS", "divClassName":"medium-10", "spanClassName": "the-baltics", "topPosData":"112px", "rightPosData":"139px", "position":"10" },
		            {"crCode": "L34373", "crName":"EASTERN MEDITERRANEAN", "divClassName":"small", "spanClassName": "eastern-med", "topPosData":"128px", "rightPosData":"100px", "position":"6" },
		            {"crCode": "L34381", "crName":"WESTERN MEDITERRANEAN", "divClassName":"medium", "spanClassName": "western-med", "topPosData":"113px", "rightPosData":"172px", "position":"4" },
		            {"crCode": "L34383", "crName":"NORTHERN EUROPE & UK", "divClassName":"medium-10", "spanClassName": "northern-eu", "topPosData":"86px", "rightPosData":"215px", "position":"8" },
		            {"crCode": "L34384", "crName":"FJORDS, ICELAND & THE ARCTIC", "divClassName":"big", "spanClassName": "ice-the-arctic", "topPosData":"2px", "rightPosData":"205px", "position":"9" }
		     ],


		postCreate: function () {
		    var crAreaStaticMap = this;
		    if(crAreaStaticMap.getParent() !== null){
		    	crAreaStaticMap.getParent().cruiseAreaStaticMap = crAreaStaticMap;
		    }
		    crAreaStaticMap.inherited(arguments);
		    crAreaStaticMap.data = crAreaStaticMap.data;
		    if(crAreaStaticMap.data.destLandingMap) {
		    	crAreaStaticMap.getCruiseAreas();
		    	var html = crAreaStaticMap.renderTmpl(crAreaStaticMap.tmpl, {"crStaticDataList": crAreaStaticMap.crStaticDataList, "isHomePage":  crAreaStaticMap.data.homePage });
	    	    if (html) {
	    	       dojo.place(html, crAreaStaticMap.domNode, "last");
	    	    }
		    }

		    if( crAreaStaticMap.pageId != "cruisehomepage" ){
	           //Mouse enter event
	            on(crAreaStaticMap.domNode, on.selector(".map-circle", "mouseover"), function(e){
	                crAreaStaticMap.hoverOnCircles(e);
	            });
	            //Mouse leave event
	            on(crAreaStaticMap.domNode, on.selector(".map-circle", "mouseout"), function(e){
	                crAreaStaticMap.hoverOnCircles(e);
	            });
		    }

		},

        hoverOnCircles: function(e){
           var crAreaStaticMap = this;
           var indx = dojo.getAttr(dojo.query(e.target).closest(".map-circle")[0], "data-index" );
           var regionHover = dojo.query(".sub-menu.filter .area-name[data-index='"+ indx +"']")[0];
           var bgChange = dojo.query(".sub-menu.filter .item.list-"+indx )[0];
           dojo[e.type == "mouseover" ? "addClass" : "removeClass"](bgChange, "hover")
           dojo.publish("tui.widget.expand.cruise.CruiseMapMenuExpandable."+ (e.type == "mouseover" ? "expandMapMenu" : "collapseMapMenu" ), [regionHover]);
        },

		 getCruiseAreas: function( ) {
             var crAreaStaticMap = this, item;
			 _.each(crAreaStaticMap.data.destinationViewDatas, function (data, i) {
				   item = array.filter(crAreaStaticMap.mainData, function(item, j){
				      return item.crCode  === data.cruiseAreaCode;
				    })[0];
				   if(item != undefined){
					   crAreaStaticMap.crStaticDataList.push({
		                     index			: 	item["position"],
		                     countryName	: 	data.countryNames,
		                     url			: 	data.url+'',
		                     cruiseAreaName	: 	item["crName"],
		                     divClassName	:	item["divClassName"],
		                     spanClassName	:	item["spanClassName"],
		                     topPOS			:	item["topPosData"],
		                     rightPOS		:	item["rightPosData"],
		                     crCode			:	item["crCode"]
		            	 });
					   if( crAreaStaticMap.pageId != "cruisehomepage" ){
						   //Reset Index based on mainData list position
						   var cruiseAreaLi = dojo.query(".sub-menu.filter ul li:nth-child("+ (i+2) +")")[0]; //dojo.query(".sub-menu.filter .cruise-area[data-index='"+ (i+1) +"']")[0];
						   dojo.setAttr(cruiseAreaLi, "data-index", item["position"]);
						   dojo.addClass( dojo.query(".item", cruiseAreaLi)[0], "list-" + item["position"]);
						   dojo.setAttr(dojo.query(".area-name", cruiseAreaLi)[0], "data-index", item["position"]);
						   dojo.setAttr(dojo.query(".item-content", cruiseAreaLi)[0], "data-index", item["position"]);
					   }
				   }
			  });
         }
	});
    return tui.widget.maps.cruise.CruiseAreasStaticMap;
});
