define ("tui/widget/search/SearchDatePicker", ["dojo", 
											   "dojo/date/locale",
											   "dojo/topic",
											   "tui/widget/datepicker/DatePickerOld",
											   "dojo/NodeList-traverse",
											   "tui/widget/search/CookieSearchSave"], function(dojo){
		
	dojo.declare("tui.widget.search.SearchDatePicker", [tui.widget.datepicker.DatePicker, 
														tui.widget.search.CookieSearchSave], {
		
		//All dates after this date will be disabled.
		endDate: null,
		
		parentWidget: null,
		
		
		postCreate: function(){
			var searchDatePicker = this;
      searchDatePicker.inherited(arguments);
 			searchDatePicker.parentWidget = searchDatePicker.getParent();
 			
            // if we have a saved search in a cookie, lets use it to retrieve the last searched date.
 			var savedsearch = searchDatePicker.getSaveFormData(searchDatePicker.parentWidget.cookieName);
 			 
 			if (savedsearch){
				savedsearch = dojo.fromJson(savedsearch);
        var saveddate  = dojo.date.locale.parse(savedsearch.datewhen, {
			selector: "date",
			datePattern: "d MMMM yyyy"
		});
        searchDatePicker.selectedDate = saveddate;  
        searchDatePicker.setFieldValue(saveddate); 
       }
      
            if (searchDatePicker.endDate){
 			    searchDatePicker.endDate = dojo.date.locale.parse(searchDatePicker.endDate, {selector: "date", datePattern: "d/M/yy"});
            }
      
 			searchDatePicker.setFormFieldValues(searchDatePicker.getSelectedDate());
		},
		
		// ---------------------------------------------------------------- datepicker events methods
		
		onAfterTmplRender: function(){
			var searchDatePicker = this;
			searchDatePicker.inherited(arguments);
            if (searchDatePicker.endDate){
			    searchDatePicker.disableUnavailableDates(searchDatePicker.endDate, false);
            }
			
		},
		
		onSelectedDate: function(date){
			var searchDatePicker = this;
			searchDatePicker.setFormFieldValues(date);
			dojo.publish("tui/widget/SearchDatePicker/onSelectedDate", [date]);
		},
		
		setFormFieldValues: function(date){
			var searchDatePicker = this;
			var day = tui.getFormElementByName("day", searchDatePicker.parentWidget.domNode);
			var monthYear = tui.getFormElementByName("monthYear", searchDatePicker.parentWidget.domNode);
			day.value = ""; 
			monthYear.value = "";
			if (date){
				day.value = date.getDate();
				monthYear.value = dojo.date.locale.format(date, {selector: "date", datePattern: "MM/yyyy"});
			}
		}
  });

	return tui.widget.search.SearchDatePicker;
});
