define ("tui/widget/search/DurationOption", ["dojo",
                                             "dojo/_base/json",
                                             "tui/widget/form/SelectOption"
                                             ], function(dojo){
   
  dojo.declare("tui.widget.search.DurationOption", [tui.widget.form.SelectOption], {

    postMixInProperties: function(){
      var durationOption = this;
      durationOption.inherited(arguments);
      durationOption.subscribe("tui/widget/SearchDatePicker/onSelectedDate", function (date) {
        var selectedMonth = date.getMonth();
        durationOption.checkDate(selectedMonth);
        if (durationOption.durationType === "1/w") {
          durationOption.setSelectedValue(durationOption.durationType);
          delete durationOption.durationType;
        }
      });
      
      durationOption.selectDurationEventlistener();
    },

    postCreate: function(){
      var durationOption = this;
      durationOption.inherited(arguments);
      durationOption.parentWidget = durationOption.getParent();
      var data = durationOption.parentWidget.getSaveFormData(durationOption.parentWidget.cookieName);
      if(data) {
        durationOption.durationType = dojo.fromJson(data).durationType;
      }
    },

    checkDate: function(month){
      var durationOption = this;
      var dateToCompare = 11; // getMonth returns number of month, starting at 0=January
      var test = durationOption.optionExists("1/w");
      if (month === dateToCompare && !test) {
        var selectedValue = durationOption.getSelectedData();

        durationOption.appendOption("Day trip","1/w",0);
        durationOption.setSelectedValue(selectedValue.value);

      } else if (month !== dateToCompare && test) {
        durationOption.removeOption(0);
      }
    },

    selectDurationEventlistener: function() {
      var durationOption = this;
      durationOption.connect(durationOption, "onChange", function(item, oldValue, newValue){
        dojo.publish("tui/widget/SearchDurationOption/onChange", [newValue]);
      });
    },

    optionExists: function(value) {
      var durationOption = this;
      var queryStr = "[value='"+value+"']";
      var option = dojo.query(queryStr,durationOption.selectNode);
      if(typeof option[0] === "undefined") {
        return false;
      }
      return true;
    }

  });
   
  return tui.widget.search.DurationOption;
});