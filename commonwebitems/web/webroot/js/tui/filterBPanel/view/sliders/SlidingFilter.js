define('tui/filterBPanel/view/sliders/SlidingFilter', [
    'dojo',
    'dojo/_base/connect',
    'dojo/dom-style',
    'dojo/dom-class',
    'dojo/dom-attr',
    'dojo/on',
    "dojo/query",
    "dojo/topic",
    'tui/filterBPanel/view/sliders/SlidingSelect',
    'dojox/dtl/_DomTemplated',
    'tui/widget/expand/Expandable'], function (dojo, connect, domStyle, domClass, domAttr, on, query, topic) {

    dojo.declare("tui.filterBPanel.view.sliders.SlidingFilter", [tui.filterBPanel.view.sliders.SlidingSelect], {

        displayMaker: true,

        dataPath: null,

        controller: null,

        selectedValues: [],

        model: null,

        isChanged: false,

        isDefault: true,

        isHidden: false,

        previousSuccessfulSelection: 0,

        postCreate: function () {
            var widget = this;
            widget.inherited(arguments);
            widget.setData();
            // Tagging handles element.
            _.each(widget.handles, function(handle, i){
                widget.tagElement(handle, widget.id + "-slider-" + (i === 0 ? "left" : "right") + "-handle");
            });
            if(widget.model) {
            topic.subscribe("tui:channel=updateSlider/"+widget.model.code , function(model){
            	widget.isChanged = model.changed;
            	widget.isDefault = !model.changed;
            	if(model.changed){
            		topic.publish("tui/filterBPanel/view/FilterController/setFilterSelection", widget.domNode.id);
            	}
             });
			 }
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

        refresh: function (field, oldValue, newValue, data, isCached, sliderPPChanged, sliderTotalChanged, dateSliderChanged, serviceResponse) {
            //update slider with fresh values from server.
            var widget = this,
        	sliderUpdated = false,
        	minMaxRangeEqual = true, // First we assume that min and max range are same
        	filterConatiner = null,
        	lastRequestedValue = null;
                    var pill = dojo.query('ul > li', widget.domNode)[0];
                    var filterItem = dojo.query('.ta-rating', widget.domNode)[0];
                    switch(widget.dataPath){
              		case "rating.filters":
              			var allFilterHidden = false;
              			filterConatiner = _.first(dojo.query(widget.domNode).closest(".item-container"));
              			domStyle.set(widget.domNode.parentElement, 'display', 'inline-block');
              			domClass.remove(filterConatiner, "disabled");
              			widget.isHidden = false;
              			widget.offset = 0;
              			if(isCached && serviceResponse.searchRequest.filters[widget.id].changed){
              				lastRequestedValue = serviceResponse.searchRequest.filters[widget.id];
              				_.each(widget.trackLists(), function(pill, pillIndex) {
              					domClass.remove(pill, 'disabled');
              					if(pillIndex < (lastRequestedValue.minValue - 1) || pillIndex > (lastRequestedValue.maxValue - 1) ) {
              						domClass.add(pill, 'disabled');
              					} else if(!sliderUpdated) {
                      				widget.offset = widget.max - parseInt(lastRequestedValue.maxValue);
              						widget.updateSliderOffset();
              						widget.updateModel(lastRequestedValue);
              						widget.updateSlider((parseInt(lastRequestedValue.min) - 1));
              						sliderUpdated = true;
              					}
              				});
      						widget.isChanged = true;
              			} else {
              				if(!widget.isChanged){
              			 _.each(widget.trackLists(), function(pill, pillIndex) {
              					domClass.remove(pill, 'disabled');
                      			_.each(data, function(ratingData) {
                      				if(widget.id === ratingData.id){
                      					minMaxRangeEqual = false; // If the object exist means min and max are different
                      					if(pillIndex < (ratingData.limit[0] - 1) || pillIndex > (ratingData.limit[1] - 1) ) {
                      						domClass.add(pill, 'disabled');
                      					} else if(!sliderUpdated) {
                      						widget.offset = widget.max - parseInt(ratingData.limit[1]);
                      						widget.allowedMin = parseInt(ratingData.limit[0]);
                      						widget.allowedMax = parseInt(ratingData.limit[1]);
                      						widget.updateSliderOffset();
                      						widget.updateSlider(pillIndex);
                      						sliderUpdated = true;
                      					}
                      					widget.isDefault = true;
                      					topic.publish("tui/filterBPanel/view/FilterController/setFilterSelection", widget.domNode.id);
                                    	topic.publish("tui/filterBPanel/view/filterController/clearAllFilterVisibility");
                      				}
                      		  });
                         });
	              			 if(minMaxRangeEqual){
	              				if(!domClass.contains(filterConatiner, "open")){
	              					domStyle.set(widget.domNode.parentElement, 'display', 'none');
	                  				widget.isHidden = true;
	                  				widget.controller.disableFilterContainer(widget.domNode.id);
	              				} else if(newValue.id !== widget.id){
	              					domStyle.set(widget.domNode.parentElement, 'display', 'none');
	                      			widget.isHidden = true;
	              				 }
	              			 }
              				}
              			}
              			break;
              		default:
              			if(isCached){
              				lastRequestedValue = serviceResponse.searchRequest.filters[widget.code.toLowerCase()];
              				widget.updateModel(lastRequestedValue);
              				widget.redraw(widget.model);
              				if(lastRequestedValue.maxValue !== lastRequestedValue.max){
              					widget.isChanged = true;
              					_.each(widget.trackLists(), function(pill, pillIndex) {
                                    if(domAttr.get(pill, 'data-value') == lastRequestedValue.max || domAttr.get(pill, 'data-value-total') == lastRequestedValue.max) {
                                        widget.updateSlider(pillIndex);
                                   }
              					});
              				}
              			}
              			break;
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
                connect.publish("tui/filterBPanel/view/sliders/SlidingFilter/" + slider.id + "/cleared", {});
                slider.clear();
            }
        },

        getWidgetContainer: function () {
            var widget = this;
            return widget.domNode.parentNode.parentNode;
        },

        clear: function (data) {
            var widget = this;
            domClass.remove(dojo.query(widget.domNode).parents(".item-container")[0], "selected");
                widget.model = _.isArray(data) ? data[0] : widget.model;
                widget.model.values = widget.trackType === 'maxRange' ? [widget.model.range[1]] : [widget.model.range[0]];
                //show the slider temporarily so that offsetwidth can be calcluated
                widget.resetSlider(widget.model);
        },

        onSelect: function(valuePP, index) {
            var widget = this;
            widget.model.values[0] = parseInt(valuePP, 10);
            widget.model.values[1] = parseInt(valuePP, 10);
            if(index != undefined) {
                connect.publish("tui/filterBPanel/view/sliders/SlidingFilter/" + widget.id + "/markerUpdated", [
                    {
                        index: index
                    }
                ]);
            }
            connect.publish("tui/filterBPanel/view/FilterController/applyFilter", [
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
			if(slider.model){
            request [filterId] = {
                code: slider.code || '',
                min: slider.model.values[0],
                max: slider.model.values[1] || slider.model.values[0],
                changed: slider.isChanged,
                maxValue:slider.allowedMax || 0,
                minValue: slider.allowedMin
            };
			}
            return request;
        },

        analyticsData: function () {
            var slider = this;
            return (slider.filterId || slider.id) + '=' + Math.floor(slider.model.values[0]);
        },

        setPreviousSuccessfulSelection : function (value){
        	var slider = this;
        	if(value !== slider.previousSuccessfulSelection){
        		slider.previousSuccessfulSelection = value;
        	}
        },

        updateModel: function(model){
        	var slider = this;
        	slider.model.limit = [];
        	slider.model.range = [];
        	slider.model.values = [];
        	model.maxValue = parseInt(model.maxValue);
        	model.minValue = parseInt(model.minValue);
        	slider.model.maxValue = model.maxValue;
        	slider.model.allowedMin = model.minValue;
        	slider.model.limit.push(parseInt(model.min));
        	slider.model.limit.push(parseInt(model.max));
        	slider.model.range.push(model.minValue);
        	slider.model.range.push(model.maxValue);
        	slider.model.values.push(parseInt(model.min));
        }


    });

    return tui.filterBPanel.view.sliders.SlidingFilter;
});