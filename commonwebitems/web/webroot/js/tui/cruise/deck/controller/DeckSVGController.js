define("tui/cruise/deck/controller/DeckSVGController", [
    'dojo',
    "dojo/_base/declare",
    'dojo/on',
    "dojo/query",
    "dojo/dom-attr",
    "dojo/dom-style",
    "dojo/dom-class",
    "dojo/dom-construct",
    "dojo/_base/xhr",
    "dojo/_base/lang",
    "dojo/_base/sniff",
	"tui/cruise/deck/view/DeckInteractiveSVG",
	"tui/cruise/deck/view/DeckFacilities",
	"tui/cruise/deck/view/DeckLegend",
	"tui/cruise/deck/view/DeckCabinOptions",
    'tui/widget/_TuiBaseWidget',
    'tui/widget/mixins/Templatable' ], function (dojo, declare, on, query, domAttr, domStyle, domClass, domConstruct, xhr, lang) {
	var TARGET_URL = dojoConfig.paths.webRoot + "/deckplans";
	return declare("tui.cruise.deck.controller.DeckSVGController", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable], {

        jsonData:null,

        shipCode: null,

        type : null,

        code : null,

        deckOverlay: false,

        deckSVGWgts : [],

        deckFacilityWgts : [],

        deckLegendWgts : [],

        deckOptionWgts : [],

        // ----------------------------------------------------------------------------- singleton

        postCreate: function () {
            var deckSVGController = this;
            deckSVGController.inherited(arguments);
            deckSVGController.subscribe("tui/cruise/deck/controller/DeckSVGController/afterToggle", function(node, state){
            	var deckNo= domAttr.get(node, 'data-deck-no');
     		    var index= domAttr.get(node, 'data-item-no');
     		   var type = "";
			   var code = "";
     		   if(domClass.contains(node, "open") && !domClass.contains(node, "overlay")){
	            	var xhrReq = xhr.get({
	                    url: TARGET_URL+"?shipCode="+deckSVGController.shipCode+"&deckNo="+deckNo+"&type="+type+"&code="+code,
	                    handleAs: "json",
	                    load: function (response, options) {
	                    	deckSVGController.handleResults(response, deckNo, index);

	                    },
	                    error: function (err) {
	                    	console.log("AJAX Error message: "+error);
	                    }
	                  });
       			}
            });

            dojo.destroy(query(".cruise-deckplan-loader")[0]);
        },

        fetchSVG : function(deckResponse, deckNo, index){
        	var deckSVGController = this, xhrReq, svgUrl;
        	svgUrl = deckResponse.deckData.svgCdnUrl;
        	//For IE8 and 9, XDomainRequest is used for svgs to avoid access denied issue
        	var invocation = dojo.isIE && dojo.isIE <= 9 ?  new window.XDomainRequest() : new XMLHttpRequest();
            if(invocation) {
               invocation.open('GET', svgUrl , true);
               if( dojo.isIE && dojo.isIE <= 9 ){
            	   //XDomainRequest uses onload instead of onreadystatechange
            	   invocation.onload = function(){
            		   deckSVGController.deckSVGWgts[index].updateTemplate(deckResponse, deckNo, true, invocation.responseText,deckResponse.deckData, deckResponse.deckData.cabinCategories, deckResponse.deckData.facilityTypeMap, deckResponse.deckData.cabinTypeMap);
            	   }
               }else{
            	   invocation.onreadystatechange = function(){
            		   if (invocation.readyState==4 && invocation.status==200){
            	              deckSVGController.deckSVGWgts[index].updateTemplate(deckResponse, deckNo, true, invocation.responseText,deckResponse.deckData, deckResponse.deckData.cabinCategories, deckResponse.deckData.facilityTypeMap, deckResponse.deckData.cabinTypeMap);
            	       }
            	   }
               }
               invocation.send();
            }
        },


        handleResults: function (response, deckNo, index) {
        	var deckSVGController = this;
        	if( dojo.isIE ){
        		//If IE8 or less, not sending for svg fetch
       		 	dojo.isIE > 8 ? deckSVGController.fetchSVG(response, deckNo, index) :
       			 			 deckSVGController.deckSVGWgts[index].updateTemplate(response, deckNo, true, "",response.deckData, response.deckData.cabinCategories, response.deckData.facilityTypeMap, response.deckData.cabinTypeMap); ;
	       	}else{
	       		deckSVGController.fetchSVG(response, deckNo, index);
	       	}
        	//updating Deck facilities
        	var  deckData = { "facilities": response.deckData.facilities };
        	deckSVGController.deckFacilityWgts[index].updateTemplate({"deckData":deckData, "deckNo": deckNo, "flag": false});
        	//updating Deck Legends
        	deckSVGController.deckLegendWgts[index].updateTemplate({"deckData":response.deckData.cabinCategories, "flag": true});
        	//updating Deck Cabin Options
        	deckSVGController.deckOptionWgts[index].updateTemplate({"deckData":response.deckData,"cabinTypeData":response.deckData.cabinTypeMap, "bindEvent":true, "deckNo": deckNo});
        	//toggle - resize the item content height accordingly
        	var height = query(".item-content-wrap", deckSVGController.domNode)[index].offsetHeight;
     		domStyle.set(query(".item-content", deckSVGController.domNode)[index], "maxHeight", _.pixels(height));
        }

	});
});