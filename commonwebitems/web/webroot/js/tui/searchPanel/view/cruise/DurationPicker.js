define ("tui/searchPanel/view/cruise/DurationPicker", ["dojo",
    "dojo/date/locale",
    "dojo/topic",
    "tui/widget/form/SelectOption",
    "dojo/NodeList-traverse",
    "tui/widget/search/CookieSearchSave",
    "tui/searchPanel/view/SearchErrorMessaging",
    "tui/searchPanel/view/cruise/CommonPicker"], function(dojo){

    dojo.declare("tui.searchPanel.view.cruise.DurationPicker", [tui.searchPanel.view.cruise.CommonPicker, tui.searchPanel.view.SearchErrorMessaging], {

    analyticText: 'duration',

    selectedValue: "",

    prepopulated: false,

    postMixInProperties: function(){
        var durationPicker = this;
        durationPicker.listData = [];
        durationPicker.selectNode = durationPicker.srcNodeRef;
        durationPicker._selindex = durationPicker.selectNode.selectedIndex;
        durationPicker.selectedValue = durationPicker.searchPanelModel.duration;
        durationPicker._buildListDataFromOption();
        durationPicker.inherited(arguments);
        durationPicker.store.targetURL = dojoConfig.paths.webRoot + "/ws/durations";
        dojo.subscribe("tui:channel=lazyload", function (){
        	durationPicker.selectedValue = durationPicker.searchPanelModel.duration;
            durationPicker.populateSavedSearch();
        });
    },
    populateSavedSearch: function(){
        var durationPicker = this;
        if( durationPicker.searchPanelModel.searchResultsPage && durationPicker.selectedValue){
           var index = _.indexOf(_.pluck(durationPicker.listData, 'value'), durationPicker.selectedValue);
           (index>-1) ? durationPicker.setSelectedIndex(index) : '' ;
        }else{
            if(durationPicker.searchPanelModel.location){}
            else{
        	   var savedSearchData = durationPicker.searchPanelModel.getSavedSearch();
        	   _.isNull(savedSearchData) ? console.log('no prepopulation'): durationPicker.setSelectedIndex(_.indexOf(_.pluck(durationPicker.listData, 'value'), durationPicker.searchPanelModel.getSavedSearch().duration));
            }
        }
        durationPicker.prepopulated = true;
    },

/*    buildListData: function(){
    	var durationPicker = this;
    	durationPicker.inherited(arguments);
    	if( durationPicker.selectNode.options[0].value == 0 ){
    		durationPicker.removeOption(0);
    	}
    },*/

    onChange: function (name, oldValue, newvalue) {
       var selectOption = this;
       if( !newvalue ){
    	   return;
       }
       dojo.html.set(selectOption.selectDropdownLabel, newvalue.key);
       for (var i = 0; i < selectOption.selectNode.options.length; i++) {
          if (selectOption.selectNode.options[i].value === newvalue.value) {
             selectOption.selectNode.options[i].selected = true;
          }
       }
       selectOption.searchPanelModel.duration = newvalue.value;
       console.log('Duration-->'+selectOption.prepopulated);
       selectOption.prepopulated ? selectOption.updateDate() : null;
    },

    displayDurationErrorMessage: function (name, oldError, newError) {
        // summary:
        //		Watcher method, displays/removes error message when error messaging model is updated
        var durationPicker = this;
        durationPicker.validateErrorMessage(newError.emptyDuration, {
           errorMessage: newError.emptyDuration,
           arrow: false,
 		   floatWhere: "position-bottom-left",
           field: "duration",
           key: "emptyDuration"
        });
     },

     removeErrors: function(){
         var durationPicker = this;
         var durationError = dojo.clone(durationPicker.searchPanelModel.searchErrorMessages.get("duration"));
         if (durationError.emptyDuration) {
            delete durationError.emptyDuration;
            durationPicker.searchPanelModel.searchErrorMessages.set("duration", durationError);
         }
      },

    updateDate:function(){
       var searchDatePicker = this;
       dojo.publish("tui:channel=updateResponse", ['duration']);
    }

    });
    return tui.searchPanel.view.cruise.DurationPicker;
});