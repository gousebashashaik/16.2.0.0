define("tui/filterPanel/model/FilterPanelModel", ["dojo",
    "tui/filterPanel/model/CheckBoxModel",
    "tui/filterPanel/model/SliderModel",
    "dojo/Stateful"], function (dojo, CheckBoxModel, SliderModel, Stateful) {

    /***********************************************************************************/
    /* tui.filterPanel.model.FilterPanelModel										   */
    /***********************************************************************************/
    dojo.declare("tui.filterPanel.model.FilterPanelModel", [dojo.Stateful], {

        rawData: null,

        defaultPanelOpen: 1,

        filterMap: {},

        constructor: function (rawData, options) {
            var filterPanelModel = this;
            filterPanelModel.rawData = rawData;
            filterPanelModel.createFilterMap();
        },

        createFilterMap: function () {
            var filterPanelModel = this;
            filterPanelModel.filterMap = filterPanelModel.buildFilterMap(filterPanelModel.rawData);
        },

        buildFilterMap: function (rawData) {
            var filterMap = {};
            for (var panel in rawData) {
                if (panel && rawData[panel]) {
                    _.forEach(rawData[panel].filters, function (filter, i) {
                        if (filter.type === tui.filterPanel.model.FilterPanelModel.FILTER_CHECKBOX_TYPE) {
                            filterMap[filter.id] = {};
                            _.forEach(filter.values, function (checkbox) {
                                filterMap[filter.id][checkbox.id] = checkbox;
                            })
                        } else {
                            filterMap[filter.id] = filter;
                        }
                    })
                }
            }
            return filterMap;
        },

        reset: function (rawData, changedFilterPanel) {
            var filterPanelModel = this;
            var filterMap = filterPanelModel.buildFilterMap(rawData);

            _.each(changedFilterPanel.filterMap, function (value, key) {
                if (filterMap[key]) {
                    filterMap[key] = value;
                }
            });

           this.rawData = rawData;
           filterPanelModel.set('filterMap', filterMap);
        }
    })

    tui.filterPanel.model.FilterPanelModel.FILTER_CHECKBOX_TYPE = "checkbox";
    tui.filterPanel.model.FilterPanelModel.FILTER_SLIDER_TYPE = "slider";

    return tui.filterPanel.model.FilterPanelModel;
})