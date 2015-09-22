define("tui/filterPanel/view/options/Hotel", [
  "dojo/_base/declare",
  "dojo/dom-attr",
  "dojo/on",
  "dojo/query",
  "dojo/dom-style",
  "dojo/text!tui/filterPanel/view/templates/featuresCheckBoxFilters.html",
  "tui/widget/expand/FilterExpandable",
  "tui/filterPanel/view/options/OptionsFilter"], function (declare, domAttr, on, query, domStyle, tmpl) {

  return declare("tui.filterPanel.view.options.Hotel", [tui.filterPanel.view.options.OptionsFilter, tui.widget.expand.FilterExpandable], {

    dataPath: 'accommodationOptions',

    itemSelector: ".sub-item",

    targetSelector: ".sub-item-toggle",

    itemContentSelector: ".sub-item-content",
    
    wrapSelector: ".sub-item-content-wrap",
    
    expandableItems: null,

    defaultOpen: [],

    tmpl: tmpl,
    
    isExpanded: true,

    draw: function (data) {
      var hotelOptions = this;
      hotelOptions.manipulateData(data);
      hotelOptions.attachEvents();
      hotelOptions.toggleLink();
      hotelOptions.inherited(arguments);
      hotelOptions.defineNumber();
      _.each(dijit.findWidgets(hotelOptions.domNode), function (w) {
        w.tag = hotelOptions.tagMappingTable.table[hotelOptions.declaredClass];
        domAttr.set(w.domNode, 'analytics-id', w.tag);
        domAttr.set(w.domNode, 'analytics-instance', hotelOptions.number);
        domAttr.set(w.domNode, 'analytics-text', w.model.name);
      });
    },

    manipulateData: function(data) {
      var widget = this;
          //Grouping of Accommodation features filter options into Prioritized and non-prioritized options based on the prioritized feature IDs received
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
    },
    toggleLink:function(){
    	var hotel = this;
    	var isExpanded = false;
    	 on(hotel.domNode, on.selector(hotel.targetSelector, "click"), function (e) {
          dojo.stopEvent(e);
          var expandLink = query(hotel.targetSelector , hotel.domNode)[0];          
          var parent = query(expandLink).parents(hotel.itemSelector)[0];          
          var content = query(expandLink).siblings(hotel.itemContentSelector)[0];  
          if (isExpanded) {
              // open it
              hotel.toggleOpen(content, parent, 'open');
              domStyle.set(content, "display", "none");
              dojo.byId('seeMoreFeatures').innerHTML = "See more features";           
              isExpanded = !isExpanded;
            } 
            else {
              // close it
              hotel.toggleOpen(content, parent, '');
              domStyle.set(content, "display", "block");
              setHeight = dojo.position(query(content).children(hotel.wrapSelector)[0]).h ;
              domHeight =  setHeight + dojo.position(hotel.domNode).h + 64;
              domStyle.set(content, "maxHeight", _.pixels(setHeight));
              domStyle.set(hotel.domNode, "maxHeight", _.pixels(domHeight));
              dojo.byId('seeMoreFeatures').innerHTML = "Hide more features";            
              isExpanded = !isExpanded;
            }  
        });
    }

  });
});