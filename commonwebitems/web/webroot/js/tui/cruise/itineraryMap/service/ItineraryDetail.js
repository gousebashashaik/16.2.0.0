define("tui/cruise/itineraryMap/service/ItineraryDetail", [
    "dojo",
	'dojo/query',
    'dojo/dom-style',
    "dojo/_base/xhr",
    "tui/widget/_TuiBaseWidget"
 ], function (dojo, query, domStyle, xhr) {

    dojo.declare("tui.cruise.itineraryMap.service.ItineraryDetail", [tui.widget._TuiBaseWidget], {

       url: dojoConfig.paths.webRoot + '/ws/overlay',

       fetchData: function(shipCode, locationCode, atSea){
          var itineraryDetail = this;
		  domStyle.set(query("div.map-loader", itineraryDetail.getParent())[0], "display","block");
          var xhrReq = xhr.get({
               url: itineraryDetail.url,
               content: atSea ? {shipCode: shipCode} : {shipCode: shipCode, locationCode: locationCode},
               handleAs: "json",
               load: function (response, options) {
				domStyle.set(query("div.map-loader", itineraryDetail.getParent())[0], "display","none");
               },
               error: function (err) {
                  _.debug(err);
                  //mediator.afterFailure();
               }
          });
           return xhrReq;
       }


    });

    return tui.cruise.itineraryMap.service.ItineraryDetail;
});