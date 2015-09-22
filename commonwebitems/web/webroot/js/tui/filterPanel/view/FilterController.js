define("tui/filterPanel/view/FilterController", [
  "dojo",
  "dojo/_base/connect",
  "dojo/query",
  "dojo/dom-class",
  "tui/filterPanel/view/calendar/SailingDates",
  "tui/filterPanel/view/sliders/TotalBudget",
  "tui/filterPanel/view/sliders/PerPersonBudget",
  "tui/filterPanel/view/sliders/TotalBudgetTapping",
  "tui/filterPanel/view/sliders/PerPersonBudgetTapping",
  "tui/filterPanel/view/sliders/Rating",
  "tui/filterPanel/view/sliders/TaRating",
  "tui/filterPanel/view/sliders/RatingTapping",
  "tui/filterPanel/view/sliders/TaRatingTapping",
  "tui/filterPanel/view/sliders/Temperature",
  "tui/filterPanel/view/options/Flight",
  "tui/filterPanel/view/options/Destination",
  "tui/filterPanel/view/options/Hotel",
  "tui/filterPanel/view/options/General",
  "tui/searchResults/view/ClearFilters",
  "tui/filterPanel/view/BudgetToggle",
  "tui/widget/expand/FilterExpandable"], function (dojo, connect, query, domClass) {

  dojo.declare("tui.filterPanel.view.FilterController", [tui.widget.expand.FilterExpandable], {

    itemSelector: ".item",

    targetSelector: ".item-toggle",

    itemContentSelector: ".item-content",

    defaultOpen: [0, 1],

    selectionSelector: ".selection",

    clearSelector: ".clear-filter",

    model: null,

    filters: [],

    dataPath: 'searchResult.filterPanel',

    autoTag: false,

    selectCoupleDestinations: false,
    
    selectSensimarDestinations: false,

    subscribableMethods: ["resize"],

    postCreate: function () {
      var widget = this;
      widget.inherited(arguments);

      widget.model = dijit.registry.byId('mediator').registerController(widget);

      connect.subscribe("tui/filterPanel/view/FilterController/applyFilter", function (message) {
        dijit.registry.byId('mediator').fire('applyFilters', message.oldValue, message.newValue);
      });

      connect.subscribe("tui/filterPanel/view/FilterController/clearFilters", function () {
        dijit.registry.byId('mediator').fire('clearFilters', null, null);
      });

      dojo.subscribe("tui/filterPanel/view/FilterController/defaultOpen", function (message) {
    	  widget.openDefault();
      });

      widget.tagElements(dojo.query(widget.targetSelector, widget.domNode), function (DOMElement) {
        return DOMElement && dojo.query(".title", DOMElement)[0].innerHTML;
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
      if(dojoConfig.site == "thomson"){
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
      _.each(request.filters.destinations, function(item, index) {
    	  if(item.parent == "DO_COU") {
    		  coupleDestinations.push(item);
    	  }
    	  if(item.parent == "DO_SMR") {
    		  sensimarDestinations.push(item);
    	  }
      });
      _.each(coupleDestinations, function(item, index){
    		 if(item.selected == true) {
    			 filterController.selectSensimarDestinations = true;
    		 }  else {
    			 filterController.selectSensimarDestinations = false;
    		 }
      });
      _.each(sensimarDestinations, function(item, index){
     		 if(item.selected == true) {
     			filterController.selectCoupleDestinations = true;
     		 }  else {
     			filterController.selectCoupleDestinations = false;
     		 }
      });
	  if(filterController.selectSensimarDestinations == true) {
		  _.each(sensimarDestinations, function(item, index) {
			  sensimarDestinations[index].selected = true; 
		  });
	  }
	  if(filterController.selectCoupleDestinations == true) {
		  _.each(coupleDestinations, function(item, index) {
			  coupleDestinations[index].selected = true; 
		  });
	  }
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

      if (field === 'duration' || field === 'rooms') {
        _.each(widget.filters, function (filter) {
          //Modified to retain filter selection always.
          if(dojoConfig.site === 'cruise'){
             filter.refresh(field, oldValue, newValue, dojo.getObject(filter.dataPath, false, response), isCached, sliderPPChanged, sliderTotalChanged, dateSliderChanged);
          }
          else{
             filter.reset ? filter.reset(dojo.getObject(filter.dataPath, false, response)) : null;
          }
        });
      }
      if (field === 'sortBy' && isCached) {
          _.each(widget.filters, function (filter) {
      	  if(filter.code != null && filter.code == 'budgetPP' || filter.code == 'budgetTotal'){
      		  if(sliderPPChanged) {
      			newValue = origResponse.searchRequest.filters.budgetpp;
      			filter.refresh(field, oldValue, newValue, dojo.getObject(filter.dataPath, false, response), isCached, sliderPPChanged, sliderTotalChanged);
      		  }
      		  else if(sliderTotalChanged) {
      			  newValue = origResponse.searchRequest.filters.budgettotal;
      			  filter.refresh(field, oldValue, newValue, dojo.getObject(filter.dataPath, false, response), isCached, sliderPPChanged, sliderTotalChanged);
      		  }
      	  }
       });
      }
      if (field === 'applyFilters' || field === 'dateSlider') {
        _.each(widget.filters, function (filter) {
    	  if(filter.code != null && filter.code == 'budgetPP' || filter.code == 'budgetTotal'){
    		  if(sliderPPChanged) filter.refresh(field, oldValue, newValue, dojo.getObject(filter.dataPath, false, response), isCached, sliderPPChanged, sliderTotalChanged);
    		  else if(sliderTotalChanged) filter.refresh(field, oldValue, newValue, dojo.getObject(filter.dataPath, false, response), isCached, sliderPPChanged, sliderTotalChanged);
    	  }
    	  else
        	filter.refresh(field, oldValue, newValue, dojo.getObject(filter.dataPath, false, response), isCached, sliderPPChanged, sliderTotalChanged, dateSliderChanged);
        });
      }
      if (field === 'clearFilters' || field === 'clearAll' || field=== 'clearRooms') {
        _.each(widget.filters, function (filter) {
          filter.clear(dojo.getObject(filter.dataPath, false, response));
        });
      }
      if((dojoConfig.site == "firstchoice") || (dojoConfig.site == "thomson")) {
    	  if (response.repaint || (newValue && newValue.id && newValue.id.indexOf("budgetpp") === -1 && newValue.id.indexOf("budgettotal") === -1)) {
    		  _.each(widget.filters, function (filter) {
    			  if (filter.id === 'budgettotal' || filter.id === 'budgetpp') {
    				 filter.reset ? filter.reset(dojo.getObject(filter.dataPath, false, response)) : null;
    			  }
    		  });
    	  }
      }
      else {
    	  if (response.repaint) {
    	        _.each(widget.filters, function (filter) {
    	          if (filter.id === 'budgettotal' || filter.id === 'budgetpp') {
    	            filter.reset ? filter.reset(dojo.getObject(filter.dataPath, false, response)) : null;
    	          }
    	        });
    	  }
      }
      widget.resize();
    },

    submitFilter: function () {},

    registerFilter: function (filter) {
      var widget = this;
      widget.filters.push(filter);
      return widget.model ? dojo.getObject(filter.dataPath, false, widget.model) : {};
    },

    onAfterToggle: function (domNode, state) {
      var widget = this,
          parent = query(domNode).parents(widget.itemSelector)[0],
          action = state === 'open' ? 'add' : 'remove';
      domClass[action](parent, 'open');
    }

  });

  return tui.filterPanel.view.FilterController;
});