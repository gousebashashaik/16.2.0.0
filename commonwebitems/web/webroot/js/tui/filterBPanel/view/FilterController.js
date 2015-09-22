define("tui/filterBPanel/view/FilterController", [
  "dojo",
  "dojo/_base/connect",
  "dojo/on",
  "dojo/query",
  "dojo/dom-class",
  "dojo/dom-style",
  "dojo/dom-attr",
  "tui/filterBPanel/view/sliders/TotalBudget",
  "tui/filterBPanel/view/sliders/PerPersonBudget",
  "tui/filterBPanel/view/sliders/Rating",
  "tui/filterBPanel/view/sliders/TaRating",
  "tui/filterBPanel/view/sliders/Temperature",
  "tui/filterBPanel/view/options/Flight",
  "tui/filterBPanel/view/options/Destination",
  "tui/filterBPanel/view/options/Hotel",
  "tui/filterBPanel/view/options/BoardBasis",
  "tui/filterBPanel/view/options/General",
  "tui/searchBResults/view/ClearFilters",
  "tui/filterBPanel/view/BudgetToggle",
  "tui/widget/expand/FilterExpandable"], function (dojo, connect, on, query, domClass, domStyle, domAttr) {

  dojo.declare("tui.filterBPanel.view.FilterController", [tui.widget.expand.FilterExpandable], {

    itemSelector: ".item-container",

    targetSelector: ".item-toggle",

    itemContentSelector: ".item-content",

    defaultOpen: [],

    selectionSelector: ".selection",

    clearSelector: ".clear-filter",

    closeSelector: ".close",

    model: null,

    filters: [],

    filterContainers: {},

    dataPath: 'searchResult.filterPanel',

    autoTag: false,

    selectSensimarDestinations: false,

    selectCoupleDestinations: false,

    isOpen: false,

    filterCount: 0,

	subscribableMethods: ["resize"],

    postCreate: function () {
      var widget = this;
      widget.inherited(arguments);

      widget.model = dijit.registry.byId('mediator').registerController(widget);

      connect.subscribe("tui/filterBPanel/view/FilterController/applyFilter", function (message) {
        dijit.registry.byId('mediator').fire('applyFilters', message.oldValue, message.newValue);
      });

      connect.subscribe("tui/filterBPanel/view/FilterController/clearFilters", function () {
        dijit.registry.byId('mediator').fire('clearFilters', null, null);
        _.each(query(".item-content-section"), function(filterContent){ // Unhide the filters
            domStyle.set(filterContent,"display","");
        });
      });

      connect.subscribe("tui/filterBPanel/view/FilterController/setFilterSelection", function (filterId) {
          widget.setFilterSelection(filterId);
        });

	  connect.subscribe("tui/filterBPanel/view/FilterController/closeFilterPanel", function () {
    	  itemElements = query(widget.itemSelector, widget.domNode);
    	  widget.closeAllPanel(itemElements);
        });

	  connect.subscribe("tui/filterBPanel/view/filterController/clearAllFilterVisibility", function(){
		  widget.setClearAllVisibility();
	  });

      widget.tagElements(dojo.query(widget.targetSelector, widget.domNode), function (DOMElement) {
    	  if(dojo.query(".title", DOMElement)[0].innerHTML != null) {
        return DOMElement && dojo.query(".title", DOMElement)[0].innerHTML;
    	  }
      });
    },

    generateRequest: function (field) {
      var filterController = this;
      var request = {filters: {}};
      var coupleDestinations = [];
      var sensimarDestinations = [];

      if (field === 'clearFilters' || field === 'clearAll' || field === 'duration' || field === 'clearRooms') {
    	  return request;
      }
      _.each(filterController.filters, function (filter) {
        dojo.mixin(request.filters, filter.generateRequest(field));
      });
      if(dojoConfig.site == "thomson") {
      _.each(request.filters.collections, function(item, index) {
    	  if ((item.name == "COUPLES") && (item.selected == true)) {
    		  _.each(request.filters.collections, function(item, index){
    			  if (item.name == "Sensimar") {
    				  request.filters.collections[index].selected = true;
    	    		 }
    		  });
    		}
    	  if ((item.name == "Sensimar") && (item.selected == true)) {
    		  _.each(request.filters.collections, function(item, index){
    			  if (item.name == "COUPLES") {
    				  request.filters.collections[index].selected = true;
    	    		 }
    		  });
    		}
      });

    }
      if (field === 'applyFilters') {
        request['searchRequestType'] = 'Filter';
      }
      return request;
    },

    handleNoResults: function (field, oldValue, newValue) {
      var controller = this;
      _.each(controller.filters, function (filter) {
        filter.handleNoResults ? filter.handleNoResults(field, oldValue, newValue) : null;
      });

    },

    restore: function (criteria, response) {

    },

    refresh: function (field, oldValue, newValue, response, isCached, sliderPPChanged, sliderTotalChanged, dateSliderChanged, origResponse) {
      var widget = this;

      _.each(query(".item-content-section"), function(filterContent){ // Unhide the filters
          domStyle.set(filterContent,"display","");
      });


      if (field === 'duration' || field === 'rooms') {
        _.each(widget.filters, function (filter) {
          filter.reset ? filter.reset(dojo.getObject(filter.dataPath, false, response)) : null;
        });
      }
      if (field === 'sortBy' && isCached) {
          _.each(widget.filters, function (filter) {
      	  if(filter.code != null && filter.code == 'budgetPP' || filter.code == 'budgetTotal'){
      		  if(sliderPPChanged) {
      			newValue = origResponse.searchRequest.filters.budgetpp;
      			filter.refresh(field, oldValue, newValue, dojo.getObject(filter.dataPath, false, response), isCached, sliderPPChanged, sliderTotalChanged, origResponse);
      		  }
      		  else if(sliderTotalChanged) {
      			  newValue = origResponse.searchRequest.filters.budgettotal;
      			  filter.refresh(field, oldValue, newValue, dojo.getObject(filter.dataPath, false, response), isCached, sliderPPChanged, sliderTotalChanged, origResponse);
      		  }
      	  }
       });
      }
      if (field === 'applyFilters' || field === 'dateSlider') {
        _.each(widget.filters, function (filter) {
    		  filter.refresh(field, oldValue, newValue, dojo.getObject(filter.dataPath, false, response), isCached, sliderPPChanged, sliderTotalChanged, dateSliderChanged, origResponse);
        });
      }
      if (field === 'clearFilters' || field === 'clearAll' || field=== 'clearRooms') {
        _.each(widget.filters, function (filter) {
          filter.clear(dojo.getObject(filter.dataPath, false, response));
        });
		response.repaint = true;
        var itemElements = dojo.query(widget.domNode).parents(".item-container");
        _.each(itemElements, function(item,i) {
           domClass.remove(dojo.query(item)[i], 'selected');
        });
        _.each(query(widget.domNode).children("li"), function(li){
        	if(domClass.contains(li, "open")){
        		connect.publish("tui/searchBResults/view/clearFilters/placeClearAll", li.id);
        	}
        });

      }
      if (response.repaint) {
        _.each(widget.filters, function (filter) {
          if (filter.id === 'budgettotal' || filter.id === 'budgetpp') {
            filter.reset ? filter.reset(dojo.getObject(filter.dataPath, false, response)) : null;
          }
        });
      }
     // widget.setFilterContainerVisibility(response.filterVisibility, response);
      widget.resize();
    },

    submitFilter: function () {},

    registerFilter: function (filter) {
      var widget = this,
      	  filterContainer = null;
      widget.filters.push(filter);
      // Creating an filterContainers object to set the selection mode
      filterContainer = query(filter.domNode).closest("li")[0];
      if(filterContainer && filterContainer.id){
    	  if(!widget.filterContainers[filterContainer.id]){
    		  widget.filterContainers[filterContainer.id] = [];
    	  }
    	  if(_.indexOf(widget.filterContainers[filterContainer.id], filter) === -1){
    		  widget.filterContainers[filterContainer.id].push(filter);
    	  }
      }
      return widget.model ? dojo.getObject(filter.dataPath, false, widget.model) : {};
    },

    setFilterSelection: function(filterId){
    	var widget = this,
    		filterContainer = null,
    		isContainerDefault = true;
    	filterContainer = query("#"+filterId).closest("li")[0];
    	if(filterContainer){
    		isContainerDefault = _.every(widget.filterContainers[filterContainer.id], function(filter){
    			if(!domClass.contains(filter.domNode.parentElement,"close")) {
    				return filter.isDefault === true;
    			}
    			return true;
    		});
    		domClass.remove(filterContainer, "selected");
    		if(!isContainerDefault){
    			domClass.add(filterContainer, "selected");
    		}
    	}
    },

	disableFilterContainer: function(filterId){
    	var widget = this,
		filterContainer = null,
		isSelected = false,
		hasContainerVisibleFilter = true;
	filterContainer = query("#"+filterId).closest("li")[0];
	if(filterContainer){
		hasContainerVisibleFilter = _.every(widget.filterContainers[filterContainer.id], function(filter){
				return filter.isHidden === true;
		});
		isSelected = domClass.contains(filterContainer, "selected");
		domClass.remove(filterContainer, "selected");
		domClass.add(filterContainer, "disabled");
		if(!hasContainerVisibleFilter){
			domClass.remove(filterContainer, "disabled");
			isSelected ? domClass.add(filterContainer, "selected"):"";
		}
	}

    },
	
    // disables/ enables filter
    setFilterContainerVisibility : function(filterVisibilities, serviceResponse){
    	var filterController = this,
    		filterData = null,
    		visibilityKeys;
    	_.each(filterController.filters, function(filter){
    		filterData = dojo.getObject(filter.dataPath, false, serviceResponse);
    		if(filter.visibilityKey){
    			visibilityKeys = filter.visibilityKey.split("||");
    			_.each(visibilityKeys, function(visibilityKey){
    				filter.isHidden = false;
    				if(filter.isDefault && (typeof filterVisibilities[visibilityKey] === "undefined" || filterVisibilities[visibilityKey] === false || !filterData)){
    					filter.isHidden = true;
    				 }
    				filterController.disableFilterContainer(filter.id);
    			});
    		}
    	});
    },

    attachEvents: function () {
      var filterController = this, parent, content, contentElement,
      itemElements = query(filterController.itemSelector);
      filterController.filterCount = itemElements.length;
      on(filterController.domNode, on.selector(filterController.targetSelector, "click"), function (e) {
        parent = query(e.target).parents(filterController.itemSelector)[0];
        if(parent){
	  	  	if(dojo.hasClass(parent,"disabled"))return;
	        content = query(filterController.itemContentSelector, parent)[0];
	        _.each(itemElements, function (item , i) {
	          itemId = domAttr.get(item, "id");
	          parentId = domAttr.get(parent, "id");
	          contentElement = query(filterController.itemContentSelector, item)[0];
	            if(parentId == itemId) {
	              if (domAttr.get(content, 'data-toggle-state') !== 'open') {
	              // open it
	              filterController.toggleOpen(content, parent, 'open');
	              domStyle.set(query('.filter-overlay')[0], 'display', 'block');
	              filterController.setFilterMask();
	              }
	              else {
	                // close it
	                filterController.toggleOpen(content, parent, '');
                  domStyle.set(query('.filter-overlay')[0], 'display', 'none');
	              }
	            }
	            else {
	              // close it
	              filterController.toggleOpen(contentElement, item, '');
	            }

	          });
        }
      });

      on(filterController.domNode, on.selector(filterController.closeSelector, "click"), function (e) {
    	  filterController.closeAllPanel(itemElements);
      });

      on(document.body, "click", function (e) {
        // return when clicked on filters
        if(query(e.target).parents(filterController.itemSelector)[0] != undefined || query(document.activeElement).parents(filterController.itemSelector)[0] != undefined || query(e.target).parents('.item-content')[0] != undefined || domAttr.get(e.target, "class") == "item-content" ||  domClass.contains(e.target, "mask-interactivity") || domClass.contains(e.target, "filter-mask-interactivity") || domClass.contains(e.target, "clear")) return;
        // close if clicked outside
        else {
        	filterController.closeAllPanel(itemElements);
        }
      });
      on(window, 'scroll', function(){
    	  filterController.setFilterMask();
      });
    },

    closeAllPanel : function(itemElements){
    	var filterController = this;
        _.each(itemElements, function (item , i) {
        	var content = null;
            content = query(filterController.itemContentSelector, item)[0];
              filterController.toggleOpen(content, item, '');
              domStyle.set(query('.filter-overlay')[0], 'display', 'none');
          });
    },

    setFilterMask : function(){
    	var filterController = this;
    	var content = dojo.query(".item-content.open" , filterController.domNode)[0];
    		if(content){
	    	    var filterInteractiveMask = dojo.query(".filter-mask-interactivity" , content)[0],
	    	    	footer = dojo.query("#inner-footer")[0].getBoundingClientRect(),
	    	    	filterContent = dojo.query(".item-content-wrap" , content)[0].getBoundingClientRect();
	    	    	maskTop =  footer.top - filterContent.top,
	    	    	maskHeight = filterContent.height - maskTop;

		    	if(maskHeight > 0){
			    	domStyle.set(filterInteractiveMask, "height", maskHeight + "px");
			    	domStyle.set(filterInteractiveMask,"top", maskTop + "px");
		    	}
    		};
    },

    onAfterToggle: function (domNode, state) {
      var widget = this,
          parent = query(domNode).parents(widget.itemSelector)[0],
          action = state === 'open' ? 'add' : 'remove';
      domClass[action](parent, 'open');
      connect.publish("tui/searchBResults/view/clearFilters/reposition", { filterId: parent.id, isOpen : state === 'open', filterCount: widget.filterCount});
    },

    setClearAllVisibility: function() {
    	var widget = this,
    		isClearAllVisible = false;
    	// If any one filter which is visible and not in default state clear All should be visible
    	isClearAllVisible = _.any(widget.filters, function(filter){    		
    		if(!domClass.contains(filter.domNode.parentElement,"close")) {
    			return filter.isDefault === false;
    		}
    		return false;
    	});
    	connect.publish("tui/searchBResults/view/clearFilters/toggleVisibility", isClearAllVisible);
    }

  });

  return tui.filterBPanel.view.FilterController;
});