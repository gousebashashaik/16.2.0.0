define("tui/filterPanel/view/sliders/SlidingFilter", [
    "dojo",
    "dojo/_base/connect",
    "dojo/dom-style",
    "dojo/on",
    "tui/widget/form/sliders/Slider",
    "dojox/dtl/_DomTemplated",
    "tui/widget/expand/Expandable"], function (dojo, connect, domStyle, on) {

    dojo.declare("tui.filterPanel.view.sliders.SlidingFilter", [tui.widget.form.sliders.Slider], {

        displayMaker: true,

        dataPath: null,

        controller: null,

        selectedValues: [],

        model: null,

        isChanged: false,

        onEnd: function (handlePos, value, step) {
            var slider = this;
            if(!slider.isChanged) slider.isChanged = true;
            var oldValue = slider.model.values[handlePos];
            slider.model.values[handlePos] = value;
            var positions = [];
            positions[handlePos] = slider.toPosition(value);
            connect.publish("tui/filterPanel/view/sliders/SlidingFilter/" + slider.id + "/markerUpdated", [
                {
                    value: positions
                }
            ]);

            connect.publish("tui/filterPanel/view/FilterController/applyFilter", [
                {
                    'oldValue': {'id': slider.id, 'value': oldValue, 'handlePos': handlePos},
                    'newValue': {'id': slider.id, 'value': (!slider.snap ? Math.ceil(value) : value), 'handlePos': handlePos}
                }
            ]);
        },

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

        drawSlider: function () {
            var widget = this;
            widget.inherited(arguments);
            for (var i = 0; i < widget.handles.length; i++) {
                widget.updateSelectedValues(i, widget.step[i]);
            }
        },

        refresh: function (field, oldValue, newValue, data, isCached, sliderPPChanged, sliderTotalChanged) {
            //update slider with fresh values from server.
            var widget = this;
            if(isCached) {
            	if(data && data[0] != undefined) {
        			if(field !== 'dateSlider' && (data[0].code === 'budgetPP' && sliderPPChanged) || (data[0].code === 'budgetTotal' && sliderTotalChanged)){
                        //This condition only executes during Back scenario.
                        //This should be moved out from the Filter into Mediator.
                        //Repaints Sliding Filters for Budget
                        widget.isChanged = sliderPPChanged || sliderTotalChanged;
                        var date = '';
                        try{
                            date = dojo.date.locale.parse(newValue.value, {selector: "date", datePattern: 'yyyy-MM-dd'});
                        }catch(err){}

                        if((!_.isUndefined(newValue.checkId)) || _.isDate( date)){
                            var filters = dijit.registry.byId('mediator').savedData.criteria.filters;
                            var value = sliderPPChanged ? filters.budgetpp : filters.budgettotal;
                            data[0].values[0] = value.max;
                            widget.drawIfData(data[0]);
                        }
                        else
        				   data[0].values[0] = newValue.value || newValue.max;
        			}
        			if(newValue.id == data[0].id) {
        				widget.drawIfData(data[0]);
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
            widget.redraw();
        },

        handleNoResults: function (field, oldValue) {
            var slider = this;
            if (!_.has(oldValue, "id")) return;
            if (oldValue.id === slider.id) {
                var positions = [];
                positions[oldValue['handlePos']] = slider.toPosition(oldValue['value']);
                slider.model.values[oldValue['handlePos']] = oldValue['value'];
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
            widget.redraw();
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

    return tui.filterPanel.view.sliders.SlidingFilter;
});