define('tui/filterPanel/view/sliders/TappingFilter', [
    'dojo',
    'dojo/_base/connect',
    'dojo/dom-style',
    'dojo/dom-class',
    'dojo/dom-attr',
    'dojo/on',
    'tui/filterPanel/view/sliders/SlidingSelect',
    'dojox/dtl/_DomTemplated',
    'tui/widget/expand/Expandable'], function (dojo, connect, domStyle, domClass, domAttr, on) {

    dojo.declare("tui.filterPanel.view.sliders.TappingFilter", [tui.filterPanel.view.sliders.SlidingSelect], {

        displayMaker: true,

        dataPath: null,

        controller: null,

        selectedValues: [],

        model: null,

        isChanged: false,

        postCreate: function () {
            var widget = this;
            widget.inherited(arguments);
            widget.setData();
            // Tagging handles element.
            _.each(widget.handles, function(handle, i){
                widget.tagElement(handle, widget.id + "-slider-" + (i === 0 ? "left" : "right") + "-handle");
            });
        },

        setData: function () {
            var widget = this;
            widget.controller = widget.getParent();
            var data = widget.controller.registerFilter(widget);
            widget.model = data[0];
            widget.drawIfData(widget.model);
        },

        drawIfData: function (model) {
            var widget = this;
            if (model) {
                widget.drawSlider(dojo.mixin({step: widget.model.values}, model));
            }
        },

        drawSlider: function (options) {
            var widget = this;
            widget.inherited(arguments);
            options.limit[0] == options.limit[1] ? domClass.add(widget.domNode, 'disabled') : domClass.remove(widget.domNode, 'disabled');
        },

        refresh: function (field, oldValue, newValue, data, isCached, sliderPPChanged, sliderTotalChanged) {
            //update slider with fresh values from server.
            var widget = this;
            if(isCached) {
            	if(data && data[0] != undefined) {
                    if(field !== 'dateSlider' && (data[0].code === 'budgetPP' && sliderPPChanged) || (data[0].code === 'budgetTotal' && sliderTotalChanged) || data.length === 2){
                        var pill = dojo.query('ul > li', widget.domNode)[0];
                        var filterItem = dojo.query('.ta-rating', widget.domNode)[0];
                        _.each(widget.trackLists(), function(pill, i) {
                              if(domClass.contains(pill, 'disabled')) {
                                return;
                              }
                              if(data.length == 2) {
                                if(newValue.id == data[0].id && filterItem == undefined) {
                                    if((domAttr.get(pill, 'data-value') == newValue.value)) {
                                        widget.updateSlider(i);
                                        widget.onSelect(newValue.value, i);
                                      }
                                  }
                                  if(newValue.id == data[1].id && filterItem != undefined) {
                                    if((domAttr.get(pill, 'data-value') == newValue.value)) {
                                        widget.updateSlider(i);
                                        widget.onSelect(newValue.value, i);
                                      }
                                  }
                              }
                              else {
                                if(newValue.id == data[0].id) {
                                    if((domAttr.get(pill, 'data-value') == newValue.value) || (domAttr.get(pill, 'data-value-total') == newValue.value)) {
                                        widget.updateSlider(i);
                                        widget.onSelect(newValue.value, i);
                                      }
                                  }
                              }
                          });
        			}
        		}
            }
        },

        redraw: function () {
            var widget = this;
            var itemContainer = widget.getWidgetContainer();
            if (domStyle.get(itemContainer, 'display') === 'none') {
                domStyle.set(itemContainer, 'display', 'block');
                widget.drawIfData(widget.model);
                domStyle.set(itemContainer, 'display', 'none');
            } else {
                widget.drawIfData(widget.model);
            }
        },

        reset: function (data) {
            var widget = this;
            widget.isChanged = false;
            if(!data.length) {
              _.debug('Warning! No data returned for ' + widget.declaredClass);
            }
            widget.model = data.length ? data[0] : widget.model;
                widget.redraw(widget.model);
                widget.resetSlider(widget.model);
        },

        handleNoResults: function (field, oldValue) {
            var slider = this;
            if (!_.has(oldValue, "id")) return;
            if (oldValue.id === slider.id) {
                var positions = [];
                connect.publish("tui/filterPanel/view/sliders/SlidingFilter/" + slider.id + "/cleared", {});
                slider.clear();
            }
        },

        getWidgetContainer: function () {
            var widget = this;
            return widget.domNode.parentNode.parentNode;
        },

        clear: function (data) {
            var widget = this;
            widget.model = _.isArray(data) ? data[0] : widget.model;
            widget.model.values = widget.trackType === 'maxRange' ? [widget.model.range[1]] : [widget.model.range[0]];
            //show the slider temporarily so that offsetwidth can be calcluated
                widget.resetSlider(widget.model);
        },

        onSelect: function(valuePP, index) {
            var widget = this;
            if(!widget.isChanged) widget.isChanged = true;
            widget.model.values[0] = parseInt(valuePP, 10);
            widget.model.values[1] = parseInt(valuePP, 10);
            if(index != undefined) {
                connect.publish("tui/filterPanel/view/sliders/SlidingFilter/" + widget.id + "/markerUpdated", [
                    {
                        index: index
                    }
                ]);
            }
            connect.publish("tui/filterPanel/view/FilterController/applyFilter", [
                {
                   'oldValue': {'id': widget.id, 'value': parseInt(valuePP, 10)},
                   'newValue': {'id': widget.id, 'value': parseInt(valuePP, 10)}
                }
            ]);
        },

        generateRequest: function (field) {
            var slider = this;
            var request = {};
            var filterId = slider.filterId || slider.id;
            if(field === "duration" || field === "rooms") slider.isChanged = false;
            request [filterId] = {
                code: slider.code || '',
                min: slider.model.values[0],
                max: slider.model.values[1] || slider.model.values[0],
                changed: slider.isChanged,
                maxValue:slider.maxValue || 0
            };
            return request;
        },

        analyticsData: function () {
            var slider = this;
            return (slider.filterId || slider.id) + '=' + Math.floor(slider.model.values[0]);
        }

    });

    return tui.filterPanel.view.sliders.TappingFilter;
});