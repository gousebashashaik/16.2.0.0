define("tui/filterPanel/view/sliders/PerPersonBudgetTapping", [
  "dojo/_base/declare",
  "dojo/_base/lang",
  "dojo/_base/connect",
  "dojo/topic",
  'dojo/dom-attr',
  'dojo/query',
  "tui/filterPanel/view/sliders/TappingFilter"], function (declare, lang, connect, topic, domAttr, query) {

  return declare("tui.filterPanel.view.sliders.PerPersonBudgetTapping", [tui.filterPanel.view.sliders.TappingFilter], {

    dataPath: 'ppbudget.filters',

    valueTmpl:  dojoConfig.currency + "${value}",

    snap: false,

    // TODO: if slider has same min/max don't create

    postCreate: function () {
      var slider = this;
      slider.inherited(arguments);
      var lists = dojo.query(".filter-container div.filter-slider ul li");
      topic.subscribe("tui/filterPanel/view/sliders/SlidingFilter/budgettotal/markerUpdated", function (message) {
        if(message.index != undefined) {
          var maxVal = domAttr.get(lists[message.index], 'data-value');
          slider.model.values[0] = maxVal;
          slider.model.values[1] = maxVal;
          slider.updateSlider(message.index);
        }
      });

      topic.subscribe("tui/filterPanel/view/sliders/SlidingFilter/budgettotal/cleared", function () {
        slider.clear();
      });
      //Tagging widget with Analytics
      slider.tagElements(dojo.query('div', slider.domNode), function(DOMElement) {
          return "budgetpp-slider-left-handle";
        });
    },

    setMarkers: function(){
      var slider = this, value, valueHolder, marker = null;
      for (var i = 0; i <= slider.steps; i++){
        marker = slider.trackLists()[i];
        valueHolder = query('span', marker)[0];
        if (i === 0){
          value = slider.min;
          valueHolder ? valueHolder.innerHTML = slider.renderValue(value) : '';
          domAttr.set(marker, 'data-value', value);
        }
        else if (i > 0 && i < slider.steps){
          value = slider.min + (i * slider.stepSize);
          value = Math.ceil(value / 10) * 10;
          valueHolder ? valueHolder.innerHTML = slider.renderValue(value) : '';
          domAttr.set(marker, 'data-value', value);
        } else{
          value = slider.max;
          valueHolder ? valueHolder.innerHTML = slider.renderValue(value) : '';
          domAttr.set(marker, 'data-value', value);
        }
        }
    },

    drawIfData: function (model) {
      var widget = this;
      if (model) {
        // if range is same, don't redraw and disable slider
        if (model.range.length && model.range[0] === model.range[1]) {
          // ensure maxValue is updated in any case
          widget.maxValue = model.maxValue;
          //DE29961, explicitly passing model ID & VALUE to the widget
          widget.code = model.code;
          widget.id = model.id;
          topic.publish("tui.filterPanel.view.BudgetToggle.disableSliders");
          return;
        }
        topic.publish("tui.filterPanel.view.BudgetToggle.enableSliders");
        widget.drawSlider(lang.mixin({step: model.values}, model));
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