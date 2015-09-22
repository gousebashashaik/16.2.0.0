define("tui/filterPanel/view/sliders/PerPersonBudget", [
  "dojo/_base/declare",
  "dojo/_base/lang",
  "dojo/_base/connect",
  "dojo/topic",
  "tui/filterPanel/view/sliders/SlidingFilter"], function (declare, lang, connect, topic) {

  return declare("tui.filterPanel.view.sliders.PerPersonBudget", [tui.filterPanel.view.sliders.SlidingFilter], {

    dataPath: 'ppbudget.filters',

    valueTmpl:  dojoConfig.currency + "${value}",

    snap: false,

    // TODO: if slider has same min/max don't create

    postCreate: function () {
      var slider = this;
           
      
      slider.inherited(arguments);

      topic.subscribe("tui/filterPanel/view/sliders/SlidingFilter/budgettotal/markerUpdated", function (message) {
        //slider.setSteps([slider.min + (slider.stepSize * (message.value - 1))]);
        slider.updateHandlePositions(message.value);
      });

      topic.subscribe("tui/filterPanel/view/sliders/SlidingFilter/budgettotal/cleared", function () {
        slider.clear();
      });

      	
      /*topic.subscribe("tui:channel=updateSliderPos", function(args){
        if(slider.model.code === args[0]) {
          slider.updateHandlePositions(args[1]);
        }
      });*/
    },

    drawIfData: function (model) {
      var widget = this;
      if (model) {
        // ensure maxValue is updated in any case
        widget.maxValue = model.maxValue;
        // if range is same, don't redraw and disable slider
        if (model.range.length && model.range[0] === model.range[1]) {
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
        var theMax = !_.isUndefined(slider.values) ? Math.ceil(slider.values[0]) : Math.ceil(slider.model.values[0]);
        var min = Math.ceil(slider.model.values[0]);
        var max = Math.ceil(slider.model.values[1]) || theMax;
        if(min>max){
            min = max;
        }
      request [filterId] = {
        code: slider.code || '',
        min: min,//Math.ceil(slider.model.values[0]),
        max: max,//Math.ceil(slider.model.values[1]) || Math.ceil(slider.values[0]),
        changed: slider.isChanged,
        maxValue: slider.maxValue
      };
      return request;
    }
    
    

  });
});