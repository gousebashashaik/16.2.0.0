define("tui/filterBPanel/view/options/Hotel", [
  "dojo/_base/declare",
  "dojo/dom-attr",
  "dojo/on",
  "dojo/query",
  "dojo/dom-style",
  "dojo/dom-class",
  "dojo/dom-geometry",
  "dojo/dom-construct",
  "dojo/text!tui/filterBPanel/view/templates/featuresCheckBoxFilters.html",
  "tui/widget/expand/FilterExpandable",
  "tui/filterBPanel/view/options/OptionsFilter"], function (declare, domAttr, on, query, domStyle, domClass, domGeom, domConstruct, tmpl) {

  return declare("tui.filterBPanel.view.options.Hotel", [tui.filterBPanel.view.options.OptionsFilter, tui.widget.expand.FilterExpandable], {

    dataPath: 'accommodationOptions',

    itemSelector: ".sub-item",

    targetSelector: ".sub-item-toggle",

    itemContentSelector: ".sub-item-content",

    wrapSelector: ".sub-item-content-wrap",

    visibilityKey : 'featuresFilter',

    expandableItems: null,

    defaultOpen: [],

    tmpl: tmpl,

    isExpanded: true,

    domRearranged: false,

    draw: function (data) {
      var hotelOptions = this,
      	  filterInterval = null;
      hotelOptions.manipulateData(data);
      hotelOptions.attachEvents();
      hotelOptions.domRearranged = false;
      hotelOptions.inherited(arguments);
      hotelOptions.defineNumber();
      if(domClass.contains(hotelOptions.domNode, "open")){
    	  filterInterval = setInterval(function(){
 			 hotelOptions.manipulateDom();
 			 if(hotelOptions.domRearranged){
 				clearInterval(filterInterval);
 			 }
     	  }, 50);
		 } else {
			 hotelOptions.attachEvents();
	   }
      _.each(dijit.findWidgets(hotelOptions.domNode), function (w) {
        w.tag = hotelOptions.tagMappingTable.table[hotelOptions.declaredClass];
        domAttr.set(w.domNode, 'analytics-id', w.tag);
        domAttr.set(w.domNode, 'analytics-instance', hotelOptions.number);
        domAttr.set(w.domNode, 'analytics-text', w.model.name);
      });
    },

    attachEvents: function(){
    	 var hotelOptions = this,
    	 	 filterInterval =  null;
    	 hotelOptions.inherited(arguments);
    	 on(hotelOptions.domNode.parentElement, "click", function(e){
    		 if(!filterInterval && !domClass.contains(hotelOptions.domNode, "open")){
	    		 filterInterval = setInterval(function(){
	    			 if(domClass.contains(hotelOptions.domNode, "open")){
	    				 hotelOptions.manipulateDom();
	    				 clearInterval(filterInterval);
	    			 }
	    		 }, 50);
    		 }
    	 });
    },

    manipulateDom: function(){
    	var hotelOptions = this,
    		maxHeight = null,
    		filterOptionContainer = null,
    		containerDimension = null,
    		newChild = '<li class="item-content-section"><h3>&nbsp;</h3><ul class="checks"></ul></li>',
    		domClearNode = null,
    		columnBalanced = false,
    		featuresContainer = null,
    		featuresContainerDimension = null,
    		featureChecks = null,
    		lastChild = null,
    		firstChild = null,
    		additionalFeatureContainer = null,
    		additionalFeatureChecks = null,
    		additionalFeatureContainerDimension = null;
    		totalColumns = query(".item-content-section", hotelOptions.domNode).length;
    	filterOptionContainer = query(".three-cols",hotelOptions.domNode)[0];
    	maxHeight = domStyle.getComputedStyle(filterOptionContainer).maxHeight;
    	if(maxHeight && maxHeight.indexOf("px") > -1){
    		maxHeight = parseInt(maxHeight.split("px")[0]);
    	}
    	containerDimension = domGeom.position(filterOptionContainer);
    	if(!hotelOptions.domRearranged && totalColumns && totalColumns < 3 && Math.round(containerDimension.h) === maxHeight){
    		hotelOptions.domRearranged = true;
    		featuresContainer = query(".item-content-section", hotelOptions.domNode)[totalColumns-1];
    		domClearNode = query(".clear",filterOptionContainer)[0];
    		domConstruct.place(newChild, domClearNode, "before");
    		featuresContainerDimension = domGeom.position(featuresContainer);
    		additionalFeatureContainer = query(".item-content-section", hotelOptions.domNode)[totalColumns];
    		additionalFeatureChecks = query("ul.checks",additionalFeatureContainer)[0];
    		while(!columnBalanced && featuresContainerDimension.h > maxHeight){
    			featuresContainerDimension = domGeom.position(featuresContainer);
    			additionalFeatureContainerDimension = domGeom.position(additionalFeatureContainer);
    			if(additionalFeatureContainerDimension.h > maxHeight){
    				while(additionalFeatureContainerDimension.h < featuresContainerDimension.h ){
    					lastChild = query("li:last-child",featuresContainer)[0];
    	    			domConstruct.place(lastChild, additionalFeatureChecks, "first");
    	    			featuresContainerDimension = domGeom.position(featuresContainer);
    	    			additionalFeatureContainerDimension = domGeom.position(additionalFeatureContainer);
    				}
    				featureChecks = query("ul.checks",additionalFeatureContainer)[0];
    				firstChild = query("li:first-child",additionalFeatureChecks)[0];
	    			domConstruct.place(firstChild, featureChecks, "last");
    				columnBalanced = true;
    			} else {
	    			lastChild = query("li:last-child",featuresContainer)[0];
	    			domConstruct.place(lastChild, additionalFeatureChecks, "first");
    			}
    		}
    	}
    },

    manipulateData: function(data) {
      var widget = this;
          //Grouping of Accommodation features filter options into Prioritized and non-prioritized options based on the prioritized feature IDs received
      if(data){
      _.each(data.filters ,function(filter){
        if(filter.id == "features") {
           filter.prioritizedHotelFeatures = _.filter(filter.values, function(feature){
                                                    return _.contains(filter.featurePriorityList, feature.id);
                                                    });
           filter.nonPrioritizedHotelFeatures = _.difference(filter.values, filter.prioritizedHotelFeatures);

           //sorting the HotelFeatures in alphabetic order
           filter.prioritizedHotelFeatures = _.sortBy(filter.prioritizedHotelFeatures, function(feature){ return feature.name; });
           filter.nonPrioritizedHotelFeatures = _.sortBy(filter.nonPrioritizedHotelFeatures, function(feature){ return feature.name; });
          }
      });
    }
    }

  });
});