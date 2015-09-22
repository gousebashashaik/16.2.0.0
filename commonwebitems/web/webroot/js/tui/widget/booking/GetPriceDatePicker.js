define ("tui/widget/booking/GetPriceDatePicker", ["dojo",
                                                  "dojo/date/locale",
                                                  "dojo/topic",
                                                  "tui/widget/search/SearchDatePicker",
                                                  "dojo/NodeList-traverse",
                                                  "tui/widget/search/CookieSearchSave"], function(dojo){
		
	dojo.declare("tui.widget.booking.GetPriceDatePicker", [tui.widget.search.SearchDatePicker,
	                                                       tui.widget.search.CookieSearchSave], {

    //these dates will be disabled.
    startDate: null,
    
    parentWidget: null,
    
    postCreate: function() {
        var GetPriceDatePicker = this;
        GetPriceDatePicker.inherited(arguments);
        GetPriceDatePicker.parentWidget = GetPriceDatePicker.getParent();

        // if we have a saved search in a cookie, lets use it to retrieve the last searched date.
        var savedsearch = GetPriceDatePicker.getSaveFormData(GetPriceDatePicker.parentWidget.cookieName);

        if (savedsearch) {
            savedsearch = dojo.fromJson(savedsearch);
            var saveddate  = dojo.date.locale.parse(savedsearch.datewhen, {
  			   selector: "date",
  			   datePattern: "d MMMM yyyy"
  		    });
            GetPriceDatePicker.selectedDate = saveddate;
            GetPriceDatePicker.setFieldValue(saveddate); 
         }
        else {
        	GetPriceDatePicker.startDate = new Date(GetPriceDatePicker.startDate);
        	GetPriceDatePicker.selectedDate = GetPriceDatePicker.startDate;
        }
      },

    
	onSelectedDate: function(date){
		var getPriceDatePicker = this;
        getPriceDatePicker.setFormFieldValues(date);
	    dojo.publish("tui/widget/GetPriceDatePicker/onSelectedDate", [date]);
	},

    onAfterTmplRender: function(){
      var getPriceDatePicker = this;
      getPriceDatePicker.inherited(arguments); 
    }

});

	return tui.widget.booking.GetPriceDatePicker;
});