define('tui/smerchResults/view/smerchDateAvailabilitySlider', [
  'dojo',
  'dojo/date/locale',
  'dojo/on',
  'dojo/NodeList-data',
  'tui/searchResults/view/DateAvailabilitySlider'
], function(dojo, locale, on) {

  dojo.declare('tui.smerchResults.view.smerchDateAvailabilitySlider', [tui.searchResults.view.DateAvailabilitySlider], {

        // ----------------------------------------------------------------------------- properties
	  smerchStartDate: null,

	  smerchEndDate: null,
       // ----------------------------------------------------------------------------- functions

	  generateRequest: function (field) {
          var dateSlider = this, selection = dateSlider.selection, request;
          if (field === 'clearAll' || field === 'duration' || field === 'rooms' || field === 'clearRooms') {
              request = {
                  'when': locale.format(dateSlider.smerchStartDate, {selector: 'date', datePattern: 'dd-MM-yyyy'}),
                  'until': dateSlider.smerchEndDate,
                  'flexibility': true //default to true as date slider will appear only for flexiblity TRUE
              };
          } else {
              request = {
                  'when': selection.when,
                  'until': (selection.when === selection.until) ? '' : selection.until,
                  'flexibility': (selection.when !== selection.until)
              };
          }
          if (field === 'dateSlider') {
              request.searchRequestType = 'Dateslider';
          }
          return request;
      },

	  initSliderDates: function () {
          var dateAvailabilitySlider = this;

          // temporarily assign required data
          var mediatorData = dijit.registry.byId('mediator').registerController(dateAvailabilitySlider, ['searchResult.availableDates', 'searchRequest']);

          dateAvailabilitySlider.model = mediatorData[0];
          dateAvailabilitySlider.initialModel = dojo.clone(mediatorData[0]);

          var origSearch = mediatorData[1];

          dateAvailabilitySlider.flexibleDays = origSearch.flexibleDays > 0 ? origSearch.flexibleDays : dateAvailabilitySlider.flexibleDays;
          
          var searchRequestStartDate = origSearch.when;
          var monthStartDate = searchRequestStartDate.split('-');
          /*var searchRequestEndDate =dojo.date.locale.format(new Date(monthStartDate[2], monthStartDate[1], 0), {
                         selector: "date",
                         datePattern: "dd-MM-yyyy"
                     });*/
          var searchRequestEndDate = origSearch.until;
          var monthEndDate = 	searchRequestEndDate.split('-');

         dateAvailabilitySlider.offsetStartPos = 0;
         dateAvailabilitySlider.steps = monthEndDate[0] - monthStartDate[0] + 1;
         dateAvailabilitySlider.range = [1, parseInt(dateAvailabilitySlider.steps, 10) + 1];
         dateAvailabilitySlider.sliderDateElements = [];

         // set initial selections based on searchRequestData
         dateAvailabilitySlider.changeSelections(searchRequestStartDate, searchRequestEndDate);	
	    
         dateAvailabilitySlider.smerchStartDate  = dojo.date.locale.parse(searchRequestStartDate, {
	             datePattern: 'dd-MM-yyyy',
	             selector: 'date'
	                 });
	
         dateAvailabilitySlider.searchDate = dateAvailabilitySlider.smerchStartDate;

         dateAvailabilitySlider.smerchEndDate = searchRequestEndDate;

	     if (!dateAvailabilitySlider.searchDate) {
	         _.debug(dateAvailabilitySlider.id + ": can't render date slider without a valid search date", true);
	         return;
	     }

         // Check if today date is included inside the range. Don't show dates in the past.
         var dateRange = new Date();         
         if (dateRange >  searchRequestStartDate) {             
             dateAvailabilitySlider.steps = monthEndDate[0] - dateRange.getDate();
             dateAvailabilitySlider.range = [1, monthEndDate[0] - dateRange.getDate() + 1];
         }

         if (dateAvailabilitySlider.steps > 15) {
                 dojo.addClass(dateAvailabilitySlider.domNode, 'narrow-variant');
             }	

         var tempRange = dateAvailabilitySlider.range[1] - dateAvailabilitySlider.range[0];
         dateAvailabilitySlider.middleOffset = 0;
         
       //calculating the animation duration
         dateAvailabilitySlider.animDuration = 1050 / dateAvailabilitySlider.steps;
      }
    });

    return tui.smerchResults.view.smerchDateAvailabilitySlider;
});
