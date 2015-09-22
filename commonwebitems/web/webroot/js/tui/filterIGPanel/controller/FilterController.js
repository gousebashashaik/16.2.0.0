define("tui/filterIGPanel/controller/FilterController",
    ["dojo",
        "dojo/_base/connect",
        "dojo/query",
        "dojo/dom-class",
        "tui/filterIGPanel/view/Rating",
        "tui/filterIGPanel/view/Collection",
        "tui/filterIGPanel/view/TaRating",
        "tui/filterIGPanel/view/Destination",
        "tui/filterIGPanel/view/AccomidationOptions",
        "tui/filterIGPanel/view/BestFor",
        "tui/showIGPackages/view/ClearFilters",
        "tui/widget/expand/FilterExpandable"], function (dojo, connect, query, domClass) {


        /***********************************************************************************/
        /* tui.filterIGPanel.controller.FilterIGPanel											   	   */
        /***********************************************************************************/
        dojo.declare("tui.filterIGPanel.controller.FilterController", [tui.widget.expand.FilterExpandable], {


            itemSelector: ".item",

            targetSelector: ".item-toggle",

            itemContentSelector: ".item-content",

            defaultOpen: [0, 1],

            selectionSelector: ".selection",

            clearSelector: ".clear-filter",

            model: null,

            filters: [],

            stopDefaultOnClick: false,

            jsonData: null,

            //TODO
            dataPath: 'merchandiserResult.filterPanel',

            autoTag: false,

            generateRequest: function (field) {
                var filterController = this;
                var request = {filters: {}};

                if (field === 'clearFilters' || field === 'clearAll') {
                    return request;
                }
                _.each(filterController.filters, function (filter) {
                    dojo.mixin(request.filters, filter.generateRequest(field));
                });
                if (field === 'applyFilters') {
                    request['searchRequestType'] = 'Filter';
                  /*  request['pageLabel'] = filterController.jsonData.merchandiserRequest.pageLabel;*/
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


            refresh: function (field, oldValue, newValue, response) {
                var widget = this;

                if (field === 'duration' || field === 'rooms') {
                    _.each(widget.filters, function (filter) {
                        filter.reset ? filter.reset(dojo.getObject(filter.dataPath, false, response)) : null;
                    });
                }

                if (field === 'applyFilters' || field === 'dateSlider') {
                    _.each(widget.filters, function (filter) {
                        filter.refresh(field, oldValue, newValue, dojo.getObject(filter.dataPath, false, response));
                    });
                }
                if (field === 'clearFilters' || field === 'clearAll') {
                    _.each(widget.filters, function (filter) {
                        filter.clear(dojo.getObject(filter.dataPath, false, response));
                    });
                }
                if( response['repaint']) {
                    _.each(widget.filters, function (filter) {
                        if(filter.id === 'budgettotal' || filter.id === 'budgetpp' )
                            filter.reset ? filter.reset(dojo.getObject(filter.dataPath, false, response)) : null;
                    });
                }
            },


            postCreate: function () {
                var widget = this;
                widget.inherited(arguments);

                widget.model = dijit.registry.byId('mediator').registerController(widget);

                connect.subscribe("tui/filterIGPanel/controller/FilterController/applyFilter", function (message) {
                    dijit.registry.byId('mediator').fire('applyFilters', message.oldValue, message.newValue);
                });

                connect.subscribe("tui/filterIGPanel/controller/FilterController/clearFilters", function () {
                    dijit.registry.byId('mediator').fire('clearFilters', null, null);
                });

                widget.tagElements(query(widget.targetSelector, widget.domNode), function(DOMElement){
                    return DOMElement && query(".title", DOMElement)[0].innerHTML;
                });
            },

            submitFilter: function () {
            },

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

        return tui.filterIGPanel.controller.FilterController;
    })
