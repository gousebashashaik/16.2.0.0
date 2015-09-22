define("tui/filterPanel/view/sliders/TotalBudgetTapping", [
  "dojo/_base/declare",
  "dojo/_base/lang",
  "dojo/topic",
  'dojo/dom-attr',
  'dojo/query',
  "tui/filterPanel/view/sliders/TappingFilter"], function (declare, lang, topic, domAttr, query) {

  return declare("tui.filterPanel.view.sliders.TotalBudgetTapping", [tui.filterPanel.view.sliders.TappingFilter], {

    dataPath: 'totalbudget.filters',

    valueTmpl: dojoConfig.currency+"${value}",

    snap: false,

    // TODO: if slider has same min/max don't create

    postCreate: function () {
      var slider = this;
      slider.inherited(arguments);
      var lists = dojo.query(".filter-container div.filter-slider ul li");
      topic.subscribe("tui/filterPanel/view/sliders/SlidingFilter/budgetpp/markerUpdated", function (message) {
        //slider.setSteps([slider.min + (slider.stepSize * (message.value - 1) )]);
        if(message.index != undefined) {
          var maxVal = domAttr.get(lists[message.index], 'data-value-total');
          slider.model.values[0] = maxVal;
          slider.model.values[1] = maxVal;
          slider.updateSlider(message.index);
        }
      });

      topic.subscribe("tui/filterPanel/view/sliders/SlidingFilter/budgetpp/cleared", function () {
        slider.clear();
      });
      //Tagging widget with Analytics
      slider.tagElements(dojo.query('div', slider.domNode), function(DOMElement) {
        return "budgettotal-slider-left-handle";
      });
    },

    drawIfData: function (model) {
      var widget = this;
      if (model) {
        // if range is same, don't redraw and disable slider
        widget.setMarkersTotal(model);
        if (model.range.length && model.range[0] === model.range[1]) {
          // ensure maxValue is updated in any case
          widget.maxValue = model.maxValue;
          topic.publish("tui.filterPanel.view.BudgetToggle.disableSliders");
          return;
        }
        topic.publish("tui.filterPanel.view.BudgetToggle.enableSliders");
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
        maxValue: slider.maxValue
      };
      return request;
    }

  });
});