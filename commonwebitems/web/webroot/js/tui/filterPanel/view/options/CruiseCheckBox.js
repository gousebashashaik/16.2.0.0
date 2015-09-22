define("tui/filterPanel/view/options/CruiseCheckBox", [
  "dojo/_base/declare",
  "dojo/_base/lang",
  "tui/widget/_TuiBaseWidget"], function (declare, lang) {

  return declare("tui.filterPanel.view.options.CruiseCheckBox", [tui.filterPanel.view.options.CheckBox], {

    countTmpl: "[{count}]",

    update: function(model) {
      var checkBox = this;
      if(checkBox.id === model.id){
        model.selected ? checkBox.select() : checkBox.deselect();
        model.disabled ? checkBox.disable() : checkBox.enable();
        model.countShowing ? checkBox.showLabelCounts() : checkBox.hideLabelCounts();
        model.noItineraries ? checkBox.updateCountModel(model) : null;
      }
    },

    updateCount: function () {
      var checkBox = this;
      if (checkBox.count) {
        checkBox.count.innerHTML = lang.replace(checkBox.countTmpl, {
          count: checkBox.model.noItineraries
        });
      }
    },

    updateCountModel: function (model) {
      var checkBox = this;
      checkBox.model.noItineraries = model.noItineraries || checkBox.model.noItineraries;
      checkBox.updateCount();
    }
    
  });
});
