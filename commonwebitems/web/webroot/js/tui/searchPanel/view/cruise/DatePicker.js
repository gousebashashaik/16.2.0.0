define ("tui/searchPanel/view/cruise/DatePicker", ["dojo",
    "dojo/date/locale",
    "dojo/topic",
    "tui/widget/form/SelectOption",
    "dojo/NodeList-traverse",
    "tui/widget/search/CookieSearchSave",
    "tui/searchPanel/view/SearchErrorMessaging",
    "tui/searchPanel/view/cruise/CommonPicker"], function(dojo){

    dojo.declare("tui.searchPanel.view.cruise.DatePicker", [tui.searchPanel.view.cruise.CommonPicker, tui.searchPanel.view.SearchErrorMessaging], {

    analyticText: 'when',

    selectedValue: "",

    prepopulated: false,

    postMixInProperties: function(){
        var selectOption = this;
        selectOption.selectedValue = selectOption.searchPanelModel.date;
        selectOption.inherited(arguments);
        dojo.subscribe("tui:channel=lazyload", function (){
        	selectOption.selectedValue = selectOption.searchPanelModel.date;
            selectOption.populateSavedSearch();
        });
    },
    onChange: function (name, oldValue, newvalue) {
       var selectOption = this;
       dojo.html.set(selectOption.selectDropdownLabel, newvalue.key);
       for (var i = 0; i < selectOption.selectNode.options.length; i++) {
          if (selectOption.selectNode.options[i].value === newvalue.value) {
             selectOption.selectNode.options[i].selected = true;
          }
       }
       selectOption.searchPanelModel.date = newvalue.value;
       selectOption.prepopulated ? selectOption.updateDuration() : null;

    },

    populateSavedSearch: function(){

        var searchDatePicker = this;
        var savedSearchData = searchDatePicker.searchPanelModel.getSavedSearch();
        if( searchDatePicker.selectedValue.split("-").length == 3 &&  searchDatePicker.selectedValue.substring(0, 2) !== "01" ){
        	searchDatePicker.selectedValue = "01" + searchDatePicker.selectedValue.substring(2)
    	}
    	if(searchDatePicker.searchPanelModel.searchResultsPage){
            if(  searchDatePicker.searchPanelModel.useDate  && searchDatePicker.selectedValue && searchDatePicker.listData.length > 1){
            	searchDatePicker.setSelectedIndex(_.indexOf(_.pluck(searchDatePicker.listData, 'value'), searchDatePicker.selectedValue));
            }
        }else if(  searchDatePicker.searchPanelModel.useDate ){
        _.isNull(savedSearchData)? console.log('no prepopulation of date'): searchDatePicker.setSelectedIndex(_.indexOf(_.pluck(searchDatePicker.listData, 'value'), searchDatePicker.searchPanelModel.getSavedSearch().date));
        }
        searchDatePicker.prepopulated = true;
    },

    setFormatedDate: function (name, oldvalue, newvalue) {
       // summary:
       //		Watcher method, sets datePicker value based on searchPanelModel
       var searchDatePicker = this;
       if (searchDatePicker.getFieldValue() === newvalue) return;

       if(!newvalue) {
          searchDatePicker.resetPlaceHolder();
          return;
       }

       newvalue = dojo.date.locale.parse(newvalue, {
          selector: "date",
          datePattern: searchDatePicker.datePattern
       });

       searchDatePicker.selectedDate = newvalue;
       searchDatePicker.setFieldValue(newvalue);
    },

    displayDateErrorMessage: function (name, oldError, newError) {
       // summary:
       //		Watcher method, displays/removes error message when error messaging model is updated
       var searchDatePicker = this;
       searchDatePicker.validateErrorMessage(newError.emptyDate, {
          errorMessage: newError.emptyDate,
          arrow: false,
		  floatWhere: "position-bottom-left",
          field: "when",
          key: "emptyDate"
       });
    },
    removeErrors: function(){
       var searchDatePicker = this;
       var dateError = dojo.clone(searchDatePicker.searchPanelModel.searchErrorMessages.get("when"));
       if (dateError.emptyDate) {
          delete dateError.emptyDate;
          searchDatePicker.searchPanelModel.searchErrorMessages.set("when", dateError);
       }
    },

    updateDuration:function(){
       var searchDatePicker = this;
       dojo.publish("tui:channel=updateResponse", ['date']);
    }

});
return tui.searchPanel.view.cruise.DatePicker;
});