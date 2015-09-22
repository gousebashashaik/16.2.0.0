define("tui/cruise/itineraryMap/controller/ItineraryMapController", [
    'dojo',
    "dojo/query",
    "dojo/dom-style",
	"tui/cruise/itineraryMap/controller/CruiseItineraryMap",
	"tui/widget/_TuiBaseWidget"], function (dojo, query, domStyle) {
    dojo.declare('tui.cruise.itineraryMap.controller.ItineraryMapController', [tui.widget._TuiBaseWidget], {

        data: null, 
        
        daysATSea: "",
        
        postCreate: function () {
            // summary:
            //		Call before widget creation.
            // description:
            // 		Intialise given objects to their default states.
            var cruiseMapCont = this;
            cruiseMapCont.inherited(arguments);
            var daysATSea = cruiseMapCont.data.cruiseMapComponentViewData.atSeaDayNos;
            if(daysATSea === ""){
            	domStyle.set(query(".corner-clip",cruiseMapCont.domNode )[0], "display","none");
            	return;
            }
            query(".corner-text",cruiseMapCont.domNode )[0].textContent = "DAY "+daysATSea+": AT SEA";
        }
    
    
    });
    return tui.cruise.itineraryMap.controller.ItineraryMapController;
});