
define("tui/cruise/deck/view/DeckInteractiveSVG", [
    'dojo',
    "dojo/_base/declare",
    'dojo/on',
    'dojo/text!tui/cruise/deck/view/templates/DeckInteractiveSVGOverlayTmpl.html',
    "dojo/query",
    "dojo/dom-attr",
    "dojo/dom-style",
    "dojo/mouse",
    "dojo/_base/xhr",
    "dojo/has",
    "dojo/_base/sniff",
    'tui/widget/popup/cruise/FacilityOverlay',
    "tui/cruise/deck/controller/DeckSVGCabinColors",
    'tui/widget/popup/Popup',
    'tui/widget/_TuiBaseWidget',
    'tui/widget/mixins/Templatable'], function (dojo, declare, on, overlayTmpl, query, domAttr, domStyle, mouse, xhr, has, sniff, Overlay, SVGCabinColors) {
	var CABINS_URL = dojoConfig.paths.webRoot + "/deckplans/room";
	return declare("tui.cruise.deck.view.DeckInteractiveSVG", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable], {

        tmpl: overlayTmpl,

        jsonData:null,

        subscribableMethods: ["open", "close", "resize"],

        modal:true,

        popup: null,

        shipCode: null,

        type : null,

        code : null,

        includeScroll: true,

        displayPagination: false,

        //Class level variable to store the cabin/rect nodes
        cabinNodesList : {},

        cabinOptionsList: {},

        //Class level variable to store the text nodes
        textNodesList : {},

        deckNo : "",

        categorysList : {},

        bindEvent: true,

        onClickEvents : [],

        mouseEvents : [],
        // ----------------------------------------------------------------------------- singleton

        postCreate: function () {
            var deckSVG = this;
            if(deckSVG.getParent().getParent() != null){
            	 deckSVG.getParent().getParent().getParent().deckSVGWgts.push(deckSVG);
            }
            deckSVG.inherited(arguments);
            //Cabin Tab - Deck overlay Scenario
            dojo.subscribe("tui/widget/popup/cruise/DeckPopup/deckSVG", function (deckData) {
            	deckSVG.updateTemplate(deckData.response, deckData.deckNo, deckData.bindEvent, deckData.SVGEle,deckData.responseDeckData,deckData.responseDeckDataCabinCategories,deckData.responseDeckDataFacilityTypeMap,deckData.responseDeckDataCabinTypeMap);
            });
        },

        updateTemplate : function (response, deckNo, bindEvent, SVGEle ,responseDeckData,responseDeckDataCabinCategories,responseDeckDataFacilityTypeMap,responseDeckDataCabinTypeMap) {
           var deckSVG = this;
           deckSVG.deckNo = deckNo;
           deckSVG.jsonData = response;

           if(!bindEvent){
        		 deckSVG.bindEvent = bindEvent;
           }
           //Loading SVG to domNode
           deckSVG.updateCabinSVG(response, SVGEle, responseDeckData, responseDeckDataCabinCategories, responseDeckDataCabinTypeMap);
           //on selection of cabin options - update/color SVG
           deckSVG.subscribe("tui/cruise/deck/view/DeckInteractiveSVG/updateCabinColor", function(deckData){
              deckSVG.updateCabinColor(deckData.deckData,  deckData.bindEvent, deckData.cabinOptions, deckData.cabinCategoriesRefAtr,deckData.cabinContentMapRefAtr);
           });

           if(! _.isEmpty(responseDeckDataFacilityTypeMap)){
        	   if( dojo.isIE ){
            		 dojo.isIE > 8 ?  deckSVG.wireFacilities(responseDeckDataFacilityTypeMap) : "";
  	          	}else{
  	          		deckSVG.wireFacilities(responseDeckDataFacilityTypeMap);
  	          	}
           }



        },

        updateCabinSVG: function (response, SVGEle,responseDeckData , responseDeckDataCabinCategories , responseDeckDataCabinTypeMap) {
        	 var deckSVG = this, imageEle,
        	 	SVGNode = query(".svg-container", deckSVG.domNode)[0],
        	 	SVGIENode = query(".svg-container-ie", deckSVG.domNode)[0],
        	 	optionsNode = query(".options-"+deckSVG.deckNo, document.body)[0],
        	 	overlayCabinsNode = query(".overlay-Cabins", document.body)[0];
        	 if(has("ie") <= 8){
        		 domStyle.set(SVGNode, "display", "none");
        		 if(optionsNode){  domStyle.set(optionsNode, "display", "none");  }
        		 if(overlayCabinsNode){  domStyle.set(overlayCabinsNode, "display", "none");  }
        		 domStyle.set(SVGIENode, "display", "block");
        		 var image = query("img", SVGIENode)[0];
        		 if(!image){
        			 imageEle = dojo.create('img');
            		 imageEle.src = responseDeckData.image.mainSrc;//response.deckData.image.mainSrc;
            		 imageEle.alt = 'SVG Image coming soon';
            		 imageEle.height = 265; // default height of the image as provided by source
    	     	     dojo.place(imageEle, SVGIENode, "only");
        		 }
        		 deckSVG.domNode.parentNode.scrollLeft = 130;
        	 } else {
        		 domStyle.set(SVGIENode, "display", "none");
        		 domStyle.set(SVGNode, "display", "block");
        		 if(optionsNode){  domStyle.set(optionsNode, "display", "block");  }
        		 if(overlayCabinsNode){  domStyle.set(overlayCabinsNode, "display", "block");  }
        		 deckSVG.updateSVGNode(response, SVGNode, SVGEle);
        		 // onload coloring
            	 deckSVG.updateCabinColor(response, true,{},responseDeckDataCabinCategories, responseDeckDataCabinTypeMap);
            	 //deckSVG.domNode.parentNode.scrollLeft = 450;
        	 }
        	 dojo.publish("tui/cruise/deck/view/DeckInteractiveSVG/onSvgLoad", deckSVG.domNode);
        },

         wireFacilities: function(response){
          var deckSVG = this;
           _.each(_.pairs(response) , function(facility){
           	var node = query("#"+facility[0], deckSVG.domNode)[0];
           	if( node )
           	{
           	domStyle.set(node, {cursor: "pointer"});
           	deckSVG.onClickEvents.push(on(node, "click", function(){
                   deckSVG.initializeOverlay(query("#"+facility[0], deckSVG.domNode)[0], {
                       code:response[facility[0]].code,
                       type:response[facility[0]].type
                   });
               }));
           }
           });
       },

        updateSVGNode: function (response, SVGNode, SVGEle) {
        	  var deckSVG = this;
        	  SVGNode.innerHTML = SVGEle;
        	  //SVGNode.appendChild(SVGEle);
        },

        updateCabinColor: function (response, bindEvent, options, responseDeckDataCabinCategories, responseDeckDataCabinTypeMap) {
        	var deckSVG = this, groupNode1, cabinNodes = [], textNodes =[], reset = true;

        	//Reset all the instance variables every time when user requested to update cabins color
        	deckSVG.cabinNodesList = {};
	       	deckSVG.cabinOptionsList = {};
	       	deckSVG.textNodesList = {};
	       	deckSVG.categorysList = {};
	       	if(!bindEvent){
	       		deckSVG.bindEvent = bindEvent;
	       	}

	       	//Resetting or removing the existing SVG events before updating the cabin colors
	       	deckSVG.removeEvents();

	       	//Resetting cabin colors
	       	reset =  deckSVG.resetMode(options);

	       	_.each(responseDeckDataCabinCategories , function(category){
	       		 //mapping room id to its specific cabin categories
                 deckSVG.categorysList[category.typeCode] = category.roomsList;
	           	 //mapping usps to its specific cabin categories
	           	 deckSVG.cabinOptionsList[category.typeCode] = [];
	           	 //Mapping
	           	 for( key in category.usps) {
	           		deckSVG.cabinOptionsList[category.typeCode].push(key);
	           	 }
	           	 //adding categories to the node list
	           	 deckSVG.cabinNodesList[category.typeCode] = [];
		         deckSVG.textNodesList[category.typeCode] = [];
            });

	       	cabinNodes = _.filter(query("rect", deckSVG.domNode), function(node){ return node.id.indexOf("Cabin") > -1 ; });
            pathNodes = _.filter(query("path", deckSVG.domNode), function(node){ return node.id.indexOf("Cabin") > -1 ; });
            textNodes = _.filter(query("text", deckSVG.domNode), function(node){ return node.id.indexOf("Cabin") > -1 ; });
            polygonNodes = _.filter(query("polygon", deckSVG.domNode), function(node){ return node.id.indexOf("Cabin") > -1 ; });

            _.each(polygonNodes , function(polygonNode) {
	       	   cabinNodes.push(polygonNode);
	       	});
            _.each(pathNodes , function(pathNode) {
                cabinNodes.push(pathNode);
            });
	       	 /**
	       	  * Updating colors based on cabin id's
	          * "addColor" - boolean - default set to FALSE for each iteration
	          * if  addColor = true  - i.e. this groupNode is already colored
	          * if  addColor = false - i.e. this groupNode is not colored, so color it to white
	          */
	       	var addColor, cbId, currentStyle,flat = [];
            if(options && !reset){
                var list = [];
                _.each(_.filter(_.pairs(options), function(num){ return num[1] === 'checked'; }), function(filter){
                    list.push(responseDeckDataCabinTypeMap[filter[0]]);

                });
                //modified now to apply intersected results between lists.
                flat = _.intersection.apply(null, list);
                //flat = _.flatten(_.intersection(list));
            }
            _.each(cabinNodes , function(groupNode) {
            	 addColor = false;
                 for( key in deckSVG.categorysList) {
            		if(groupNode.id.indexOf("Cabin_") != -1) {
                    	cbId = (groupNode.id).split("_")[1];
                        currentStyle = domAttr.get(groupNode, 'style');
                        if(_.contains(deckSVG.categorysList[key], cbId)) {
                            if(!options){
                        		deckSVG.addColorToCabin(groupNode, key);
                                addColor = true;
                                break;
                        	} else if(reset) {
                        			deckSVG.addColorToCabin(groupNode, key);
                        			addColor = true;
                        			break;
                        	} else {
                                if(_.contains( flat, cbId )) {
                                    deckSVG.addColorToCabin(groupNode, key);
                                    addColor = true;
                                }else {
                                    domStyle.set(groupNode, "fill", SVGCabinColors["cabinColors"]["default"]);
                                }
                    		}
                        }
                    }
            	 }
            	 if(!addColor){
                     domStyle.set(groupNode, "fill", SVGCabinColors["cabinColors"]["default"]);
            	 }
            });




            //For adding mouse events to text nodes,
            var nodes = [];
            for(key in deckSVG.cabinNodesList) {
            	_.each(deckSVG.cabinNodesList[key], function(item){
            		 var txId = (item.id).split("_")[1];
            		nodes.push(item);
            		var textNode = query("#Text_"+txId, deckSVG.domNode)[0];
            		deckSVG.textNodesList[key].push(textNode);
            		if(deckSVG.bindEvent){
            			deckSVG.addEvents(textNode,  deckSVG.textNodesList, response);
                    }
            	});
            }
        },

        /**
         * "flag" - boolean
         * if  flag = true - update the color to all cabins
         * if  flag = false - update cabin colors only to user checked categories.
         */
        resetMode : function(options){
        	var deckSVG = this, flag = true;

        	if(options != null){
	       		for( option in options) {
		   			 if(options[option] === "checked") {
		   				flag = false;
		   				 break;
		   			 }
		       	}
	       	}
        	return flag;
        },

        addColorToCabin: function (groupNode, key){
            var deckSVG = this;
        	deckSVG.cabinNodesList[key].push(groupNode);
            var cabinColor = SVGCabinColors["cabinColors"][key];
            if(cabinColor === '')
               console.log('No color set for '+key);
            domStyle.set(groupNode, "fill", cabinColor);
            domStyle.set(groupNode, "opacity",1);
			domStyle.set(groupNode, "cursor", "pointer");
            if(deckSVG.bindEvent){
            	deckSVG.addEvents(groupNode,  deckSVG.cabinNodesList );
            }
        },

        removeEvents: function(){
        	var deckSVG = this;
        	_.each(deckSVG.onClickEvents, function(event){
	       		event.remove();
	       	});
	       	deckSVG.onClickEvents = [];
	     	_.each(deckSVG.mouseEvents, function(event){
	       		event.remove();
	       	});
	       	deckSVG.mouseEvents = [];
        },

        addEvents : function (groupNode, nodesList, response){
        	var deckSVG = this;
            if(!_.isUndefined(groupNode))
            {
        	   deckSVG.mouseEvents.push(on(groupNode, mouse.enter, function(event){
        		// setting the max opacity, to highlight the cabin nodes
                  deckSVG.mouseEventHandler(groupNode, "0.6", nodesList);
               }));
        	  deckSVG.mouseEvents.push(on(groupNode, mouse.leave, function(event){
            	  // 0.5 is the default opacity provided in SVG - rect tag
                  deckSVG.mouseEventHandler(groupNode, "1", nodesList);
              }));
              deckSVG.onClickEvents.push(on(groupNode, "click", function(){
                 deckSVG.initializeOverlay(groupNode);
              }));
            }
        },

        mouseEventHandler : function (groupNode, opacity, nodesList){
        	var deckSVG = this;
        	 for( key in nodesList) {
        		 if(_.contains(nodesList[key], groupNode) ){
                     _.each(deckSVG.cabinNodesList[key], function(node){
                         domStyle.set(node, "opacity",opacity);
                     });
                 }
        	 }
        },
        initializeOverlay: function(node, facility){
        	var deckSVG = this, xhrReq, type = "", code = "", cbId = (node.id).split("_")[1];
        	if(_.isUndefined(facility)){
                _.each(deckSVG.jsonData.deckData.cabinCategories , function(category){
                    if(_.contains(category.roomsList, cbId)){
                        type =  category.type,
                            code =  category.code
                        return;
                    }
                });
            }
            else{
                type =  facility.type;
                 code =  facility.code;
            }

        	xhrReq = xhr.get({
                url: CABINS_URL+"?shipCode="+deckSVG.shipCode+"&deckNo="+deckSVG.deckNo+"&type="+type+"&code="+code,
                handleAs: "json",
                load: function (response, options) {
                	 deckSVG.handleResults(response);
                },
                error: function (err) {
                	_.debug(err);
            	 	console.log("AJAX Error message: "+error);
                }
              });
        },

        handleResults: function(response){
        	var deckSVG = this;
          	deckSVG.popup = new Overlay({
				componentName:"deckSVG",
				jsonData: response,
				tmpl:deckSVG.tmpl
        	 });
        	 deckSVG.popup.open();
        }
	});
});