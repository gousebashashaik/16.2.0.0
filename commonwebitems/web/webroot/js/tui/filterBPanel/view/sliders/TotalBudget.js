define("tui/filterBPanel/view/sliders/TotalBudget", [
  "dojo/_base/declare",
  "dojo/_base/lang",
  "dojo/topic",
  'dojo/dom-attr',
  'dojo/query',
  "tui/filterBPanel/view/sliders/SlidingFilter"], function (declare, lang, topic, domAttr, query) {

  return declare("tui.filterBPanel.view.sliders.TotalBudget", [tui.filterBPanel.view.sliders.SlidingFilter], {

    dataPath: 'totalbudget.filters',

    valueTmpl: dojoConfig.currency+"${value}",

    visibilityKey : 'budgetFilter',

    snap: false,

    // TODO: if slider has same min/max don't create

    postCreate: function () {
      var slider = this;
      slider.inherited(arguments);
      var lists = null;
      topic.subscribe("tui/filterBPanel/view/sliders/SlidingFilter/budgetpp/markerUpdated", function (message) {
    	lists = slider.trackLists();
        if(message.index != undefined && lists && lists.length) {
          var maxVal = domAttr.get(lists[message.index], 'data-value-total');
          slider.model.values[0] = maxVal;
          slider.model.values[1] = maxVal;
          slider.updateSlider(message.index);
        }
      });

      topic.subscribe("tui/filterBPanel/view/sliders/SlidingFilter/budgetpp/cleared", function () {
        slider.clear();
      });

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
    	slider.model.limit.push(model.minValue);
    	slider.model.limit.push(model.maxValue);
    	slider.model.range.push(model.minValue);
    	slider.model.range.push(model.maxValue);
    	slider.model.values.push(parseInt(model.max));
    },

    drawIfData: function (model) {
      var widget = this;
      if (model) {
        // if range is same, don't redraw and disable slider
        widget.setMarkersTotal(model);
        if (model.range.length && model.range[0] === model.range[1]) {
          // ensure maxValue is updated in any case
          widget.maxValue = model.maxValue;
          topic.publish("tui.filterBPanel.view.BudgetToggle.disableSliders");
          return;
        }
        topic.publish("tui.filterBPanel.view.BudgetToggle.enableSliders");
        widget.drawSlider(lang.mixin({step: model.values}, model));
      }
    },

    setMarkersTotal: function(modelTotal) {
      var slider = this, value, valueHolder, marker = null;
      var min = Math.ceil(modelTotal.limit[0]);
      var max = Math.ceil(modelTotal.limit[1]);
      var maxValue = modelTotal.maxValue;
      //slider.step = slider.step || [slider.min, slider.max];
      var range = Math.abs(max - min);
      var steps = 4//slider.steps || slider.max;
      var stepSize = range / (steps);
      var lists = dojo.query(".filter-container div.filter-slider ul li");
      domAttr.set(dojo.query(".filter-container div.filter-slider")[0], 'data-maxValue', maxValue);
      for(var i = 0; i <= steps; i++) {
        marker = lists[i];
        valueHolder = query('span', marker)[0];
        if(i === 0) {
          value = min;
          valueHolder ? valueHolder.innerHTML = slider.renderValue(value) : '';
          domAttr.set(marker, 'data-value-total', value);
        }
        else if(i > 0 && i < steps) {
          value = min + (i * Math.ceil(stepSize));
          value = Math.round(value);
          valueHolder ? valueHolder.innerHTML = slider.renderValue(value) : '';
          domAttr.set(marker, 'data-value-total', value);
        } else {
          value = max;
          valueHolder ? valueHolder.innerHTML = slider.renderValue(value) : '';
          domAttr.set(marker, 'data-value-total', value);
        }
      }
    },
    generateRequest: function (field) {
      var slider = this;
      var request = {};
      var filterId = slider.filterId || slider.id;
      if (field === "duration" || field === "rooms") slider.isChanged = false;
      request [filterId] = {
        code: slider.code || '',
        min: Math.ceil(slider.model.values[0]),
        max: Math.ceil(slider.model.values[1]) || Math.ceil(slider.model.values[0]),
        changed: slider.isChanged,
        maxValue: slider.maxValue,
        minValue: slider.allowedMin
      };
      return request;
    },

    updateState: function(selectedIndex){
    	var slider = this;
    	slider.isDefault = true;
    	slider.isChanged = false;
    	if(selectedIndex < slider.steps) {
  			slider.isDefault = false;
  			slider.isChanged = true;
        }
    	topic.publish("tui/filterBPanel/view/FilterController/setFilterSelection", slider.domNode.id);
        topic.publish("tui/filterBPanel/view/filterController/clearAllFilterVisibility");
    },

    handleNoResults: function (field, oldValue) {
        var slider = this;
        _.each(slider.trackLists(), function(pill, pillIndex) {
            if(domAttr.get(pill, 'data-value-total') == slider.previousSuccessfulSelection) {
            	slider.updateSlider(pillIndex);
           }
		});
    }

  });
});