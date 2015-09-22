define("tui/searchBResults/view/ClearFilters", [
        "dojo",
        "dojo/query",
        "dojo/on",
        "dojo/dom-class",
        "dojo/_base/connect",
        "dojo/dom-construct"], function (dojo, query, on, domClass, connect, domConstruct) {

        dojo.declare("tui.searchBResults.view.ClearFilters", [tui.widget._TuiBaseWidget], {

        	filterToggleQueue: [],

        	filterQueue: [],

        	actualPosition: null,

            constructor:function () {

            },

            postCreate: function () {
                var widget = this;
                widget.inherited(arguments);
                widget.actualPosition = query("#sticky-filter .horizontal-filter-panel")[0];
                on(widget.domNode, "click", function () {
                    connect.publish("tui/filterBPanel/view/FilterController/clearFilters", [{
                        id : 'clear'
                    }]);
                    widget.pushBelowClosedFilterPanel();
                    widget.toggleVisibility(false);
                });
                connect.subscribe("tui/searchBResults/view/clearFilters/reposition", function (filterStatus) {
                		widget.reposition(filterStatus);
                  });
                connect.subscribe("tui/searchBResults/view/clearFilters/toggleVisibility", function(isVisible){
                	widget.toggleVisibility(isVisible);
                });

                connect.subscribe("tui/searchBResults/view/clearFilters/placeClearAll", function(filterId){
                	widget.pushBelowOpenedFilterPanel(filterId);
                });

                widget.tagElement(widget.domNode, "Clear all filters");
            },

            toggleVisibility: function(isVisible){
            	var clearFilter = this,
            		action = isVisible ? "remove": "add",
            	    parentAction = isVisible ? "add" : "remove";
            	domClass[action](clearFilter.domNode,"hide");
            	domClass[parentAction](clearFilter.domNode.parentElement,"visible");
            	if(query(".filter-section")[0]){
				domClass[parentAction](query(".filter-section")[0],"filters-applied");
            	}
            	if(query("#sticky-filter")[0]){
            	domClass[parentAction](query("#sticky-filter")[0],"filters-applied");
            	}

            },

            reposition: function(filterStatus){
            	var clearFilter = this,
            		openFilterIndex = 0;
            	  clearFilter.filterToggleQueue.push(filterStatus.isOpen);
            	  clearFilter.filterQueue.push(filterStatus.filterId);
            	 if(clearFilter.filterToggleQueue.length === filterStatus.filterCount){
            		 openFilterIndex = _.indexOf(clearFilter.filterToggleQueue, true);
            		 if( openFilterIndex > -1){
            			 clearFilter.pushBelowOpenedFilterPanel(clearFilter.filterQueue[openFilterIndex])
            		 } else {
            			 clearFilter.pushBelowClosedFilterPanel();
            		 }
            		 clearFilter.filterToggleQueue = [];
            		 clearFilter.filterQueue = [];
            	 }
            },

            pushBelowOpenedFilterPanel: function(filterId){
            	var clearFilter = this,
            		filterLocation = query("#"+filterId+" .item-content");
            	if(filterLocation && filterLocation.length){
            		domConstruct.place(query(clearFilter.domNode).closest(".clear-shortlist")[0], filterLocation[0], "last");
            	}
            },

            pushBelowClosedFilterPanel: function(){
            	var clearFilter = this;
            		domConstruct.place(query(clearFilter.domNode).closest(".clear-shortlist")[0], clearFilter.actualPosition, "last");
            }

        });

        return tui.searchBResults.view.ClearFilters;
    });