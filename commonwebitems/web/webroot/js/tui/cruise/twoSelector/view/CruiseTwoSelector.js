define("tui/cruise/twoSelector/view/CruiseTwoSelector", [
	"dojo/_base/declare",
    'dojo/text!tui/cruise/twoSelector/view/templates/CruiseTwoSelectorTmpl.html',
    'dojo/on',
    'dojo/query',
    "dojo/dom-construct",
    "dojo/dom-style",
    "dojo/dom-class",
    "dojo/dom-attr",
    'tui/widget/mixins/Templatable',
    'tui/widget/_TuiBaseWidget'], function (declare, tmpl, on, query, domConstruct, domStyle, domClass, domAttr) {
	return declare("tui.cruise.twoSelector.view.CruiseTwoSelector", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable], {
        jsonData: null,
	  	tmpl: tmpl,
	  	mainNode : null,
	    componentName: "",
	    shipTabNode: null,
	    itineraryTabNode: null,
	    shipListNode: null,
	    shipNameNode: null,
	    shipPageFlag : false,
	    classNames: {
	    	"TAB_SELECTED" : "tab-selected",
	    	"SHIP_SELECTED" : "ship-selected",
	    	"CHANGE_SHIP" : "Change Ship",
	    	"SINGLE_SHIP": "single-ship",
	    	"MULTI_SHIP" : "multi-ship"
	    },
	    iOS: false,

	    /**
	     * Scenarios:
	     * Case 1(Itinerary Page): Single ship - default selected
	     * Case 2(Itinerary Page): Multiple ships - No ship selected and with choose ship option - select dropdown box
	     * Case 3(Itinerary Page): Multiple ships - Ship selected and with change ship option ( select dropdown box )
	     * Case 4(Ship Page): Single ship - default selected
	     * Case 5(Ship Page): Multiple ships - No ship selected and with choose ship option - select dropdown box
	     * Case 6(Ship Page): Multiple ships - Ship selected and with change ship option ( select dropdown box )
	     *
	     */
        postCreate: function () {
           var crTwoSelector = this, hrefNode, href;
           crTwoSelector.iOS = ( navigator.userAgent.match(/(iPad|iPhone|iPod)/g) ? true : false );
           crTwoSelector.inherited(arguments);
           hrefNode =  query("a.ship-tab-link",crTwoSelector.domNode )[0];
           href = crTwoSelector.jsonData.itineraryShipSelectorUrl !== null ? crTwoSelector.jsonData.itineraryShipSelectorUrl : "javascript:void(0);";
           if(!crTwoSelector.iOS){
        	   hrefNode.href =   href;
           }else{
        	   domAttr.set(hrefNode, "data-dojo-link", href);
           }
           crTwoSelector.mainDivNode = query(".span-half", crTwoSelector.domNode )[1];
           crTwoSelector.shipTabNode = query(".ship-tab", crTwoSelector.domNode )[0];
           crTwoSelector.itineraryTabNode = query(".itinerary-tab", crTwoSelector.domNode )[0];
           crTwoSelector.shipListNode = query(".ship-list", crTwoSelector.domNode )[0];
           crTwoSelector.shipNameNode = query("p", crTwoSelector.mainDivNode )[0];
           //TODO need to refactor
           // Case 6
           if(crTwoSelector.shipPageFlag){
        	   domClass.add( crTwoSelector.shipTabNode , crTwoSelector.classNames.TAB_SELECTED);
      		   domClass.remove( crTwoSelector.itineraryTabNode , crTwoSelector.classNames.TAB_SELECTED);
        	   domClass.add( crTwoSelector.shipListNode , crTwoSelector.classNames.SHIP_SELECTED);
      		   var hrefNode =  query("a.itinerary-tab-link",crTwoSelector.domNode )[0];
	      		 if(!crTwoSelector.iOS){
	      			hrefNode.href = crTwoSelector.jsonData.itinerarySelectorUrl !== null ? crTwoSelector.jsonData.itinerarySelectorUrl : "javascript:void(0);";
	             }else{
	            	 domAttr.set(hrefNode, "data-dojo-link", crTwoSelector.jsonData.itinerarySelectorUrl !== null ? crTwoSelector.jsonData.itinerarySelectorUrl : "javascript:void(0);");
	             }
           }

	       	if(crTwoSelector.jsonData.selectedShipCode === null) {
	      	   if(crTwoSelector.jsonData.shipViewDatas.length > 1) {
	      			// Case 2 and 5 - Multiple  ships, no ship selected scenario
	         	    crTwoSelector.renderShipOptions();
	             } else {
	            	// Case 1 and 4 - Single  ship scenario
	          	    crTwoSelector.singleShipScenario();
	             }
	         } else {
	        	// Case 3 and 6 - Multiple  ships, ship selected scenario
	      	   if(crTwoSelector.jsonData.shipViewDatas.length > 1) {
	      		   _.each(crTwoSelector.jsonData.shipViewDatas, function(shipData){
	      			   if(shipData.shipCode === crTwoSelector.jsonData.selectedShipCode){
	      	        		 crTwoSelector.shipNameNode.innerHTML = shipData.shipName;
	      	        		 query("a .shipText",crTwoSelector.domNode )[0].innerHTML = crTwoSelector.classNames.CHANGE_SHIP;
	      	        		 domClass.add( crTwoSelector.shipTabNode , crTwoSelector.classNames.SHIP_SELECTED);
	      	        		 domClass.add( crTwoSelector.shipListNode , crTwoSelector.classNames.SHIP_SELECTED);
	      	        		  domClass.add( query(".ship-select",crTwoSelector.domNode )[0] , crTwoSelector.classNames.SHIP_SELECTED);
	      			   }
	      		   });
	      		   crTwoSelector.renderShipOptions();
	             } else {
	          		//Single  ship scenario
	         	    crTwoSelector.singleShipScenario(crTwoSelector.shipTabNode);
	             }
	         }

	           on(crTwoSelector.domNode, on.selector(".itinerary-tab-link, .ship-tab-link", "click"), function (e) {
		       		var link = query(e.target).closest("a")[0];
		       		var href = crTwoSelector.iOS ? domAttr.get(link, "data-dojo-link") : link.href ;
		       		if( href.indexOf("javascript") !== 0 ){
		       			domClass.remove(query(".cruise-itinerary-loader", crTwoSelector.domNode)[0], "hide");
		       			if(crTwoSelector.iOS){
		       				setTimeout( function(){location.href = href; }, 300);
		       			}
		       		}
		       	});

	           domClass.add(query(".cruise-itinerary-loader", crTwoSelector.domNode)[0], "hide");

        },

        renderShipOptions: function () {
        	var crTwoSelector = this, html;
        	domStyle.set( query(".ship-select",crTwoSelector.domNode )[0], "display","block");
        	 html = crTwoSelector.renderTmpl(null, {ships:crTwoSelector.jsonData.shipViewDatas});
             domConstruct.place(html, crTwoSelector.shipListNode, "only");
             domClass.add(  crTwoSelector.shipTabNode , crTwoSelector.classNames.MULTI_SHIP);
             domClass.remove(  crTwoSelector.shipTabNode, crTwoSelector.classNames.SINGLE_SHIP);
             crTwoSelector.delegateEvents();
        },

        singleShipScenario: function (node) {
        	var crTwoSelector = this;
     		crTwoSelector.shipNameNode.innerHTML = crTwoSelector.jsonData.shipViewDatas[0].shipName;
            domClass.remove(  crTwoSelector.shipTabNode , crTwoSelector.classNames.MULTI_SHIP);
            domClass.add(  crTwoSelector.shipTabNode , crTwoSelector.classNames.SINGLE_SHIP);
     		domClass.add( node , crTwoSelector.classNames.SHIP_SELECTED);
        },

        delegateEvents: function () {
        	var crTwoSelector = this, parentNode;
        	//closing the drop box options section
        	on(document.body, "click", function (event) {
        		if(event.target.parentNode === query("a.dropbox")[0]){
        			return;
        		}
        		if (domClass.contains( crTwoSelector.shipListNode, "active")){
        			 domClass.remove( crTwoSelector.shipListNode , "active");
        			 domStyle.set(crTwoSelector.shipListNode, "display","none");
        		 }
        	});
        	//opening the drop box options section
        	on(crTwoSelector.domNode, on.selector(".ship-select", "click"), function () {
        		domClass.add( crTwoSelector.shipListNode , "active");
        		domStyle.set(crTwoSelector.shipListNode, "display","block");
        	});
        	on(crTwoSelector.domNode, on.selector("a .ship-type", "click"), function (event) {
        		 domStyle.set(crTwoSelector.shipListNode, "display","none");
        		 var index = domAttr.get(event.target,'data-index');
    			 crTwoSelector.shipNameNode.innerHTML = crTwoSelector.jsonData.shipViewDatas[index-1].shipName;
        		 query("a .shipText",crTwoSelector.domNode )[0].innerHTML = crTwoSelector.classNames.CHANGE_SHIP;
        		 domClass.add( crTwoSelector.shipTabNode , crTwoSelector.classNames.SHIP_SELECTED);
        		 domClass.add( crTwoSelector.shipListNode , crTwoSelector.classNames.SHIP_SELECTED);
        		 domClass.add( query(".ship-select",crTwoSelector.domNode )[0] , crTwoSelector.classNames.SHIP_SELECTED);
        		 // Setting itinerary url related to the selected ship
        		 var hrefNode =  query("a.itinerary-tab-link",crTwoSelector.domNode )[0];
      		   	 hrefNode.href = crTwoSelector.jsonData.shipViewDatas[index-1].itinerarySelectorUrl;
        		// parentNode = event.srcElement ? (event.srcElement.className === "ship-type" ? event.srcElement : event.srcElement.parentNode) : this;
        		// crTwoSelector.shipNameNode.innerHTML = query("h3",parentNode )[0].innerHTML;
        		// query("a .shipText",crTwoSelector.domNode )[0].innerHTML = crTwoSelector.classNames.CHANGE_SHIP;
        		// domClass.add( crTwoSelector.shipTabNode , crTwoSelector.classNames.SHIP_SELECTED);
        	 });
        	 on(crTwoSelector.domNode, on.selector(".shipsTab", "click"), function () {
        		 if (domClass.contains( crTwoSelector.shipTabNode, crTwoSelector.classNames.SHIP_SELECTED)){
        			 domClass.add( crTwoSelector.shipTabNode , crTwoSelector.classNames.TAB_SELECTED);
            		 domClass.remove( crTwoSelector.itineraryTabNode , crTwoSelector.classNames.TAB_SELECTED);
        		 }
        	 });

        	 on(crTwoSelector.domNode, on.selector(".itinerary-tab", "click"), function () {
        		 domClass.remove( crTwoSelector.shipTabNode , crTwoSelector.classNames.TAB_SELECTED);
        		 domClass.add( crTwoSelector.itineraryTabNode , crTwoSelector.classNames.TAB_SELECTED);
        	 });
        }
	});
});


