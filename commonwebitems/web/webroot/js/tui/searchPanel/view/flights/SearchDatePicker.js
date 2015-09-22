define("tui/searchPanel/view/flights/SearchDatePicker", [
  "dojo",
  "dojo/store/Observable",
  "dojo/dom-attr",
  "dojo/dom-construct",
  "dojo/text!tui/searchPanel/view/flights/templates/searchDatepicker.html",
  "tui/searchPanel/view/flights/FlightSearchMonthBar",
  "tui/searchPanel/store/DateStore",
  "dojo/_base/lang",
  "dojo/on",
  "dojo/string",
  "tui/search/nls/Searchi18nable",
  "tui/searchPanel/view/flights/SearchErrorMessaging",
  "tui/widget/datepicker/flights/DatePicker"], function (dojo, Observable, domAttr, domConstruct, searchDatePickerTmpl, flightSearchMonthBar,  DateStore, lang, on) {

  function splitDestinationQuery(query) {
    return query.split(':')[0];
  }

  dojo.declare("tui.searchPanel.view.flights.SearchDatePicker", [tui.widget.datepicker.flights.DatePicker, tui.search.nls.Searchi18nable, tui.searchPanel.view.flights.SearchErrorMessaging], {

    // ----------------------------------------------------------------------------- properties

    datePattern: null,

    monthsAndYears: null,

    monthAndYearTxt: null,

    monthAndYearTxtForNextMonth: null,

    isFirstMonthNotAvailableByDefault: false,

    isSecondMonthNotAvailableByDefault: false,

    seasonLength: null,

    tmpl: searchDatePickerTmpl,

    dateStore: null,

    skipMonth: true,

    skipNextMonth: true,

    prevAvailableMonth: false,

    prevAvailableLink: false,

    nextAvailableMonth: false,

    nextAvailableLink: false,

    availabilityLinkConnects: null,

    nextMonthAvailabilityLinkConnects: null,

    firstDate: null,

    nextMonthFirstDate: null,

    endDate: null,

    calClass: '',

    defaultDaysAhead: 1,

    subscribableMethods: ["onCalendarFocus", "resize", "focusCalendar"],

    tempAvailableMonths: [],

    finalAvailableMonths: [],

    cal_months_labels : ['January', 'February', 'March', 'April',
	                     'May', 'June', 'July', 'August', 'September',
	                     'October', 'November', 'December'],

    monthbarmessagestart: '',

 	monthbarmessageend: '',

 	unAvailableMonthName: '',

    monthBarOpenOnFirstMonthLinkSelect: ".first-month-monthbar-link",

	monthBarOpenOnSecondMonthLinkSelect: ".second-month-monthbar-link",

    // ----------------------------------------------------------------------------- methods

    postCreate: function () {
      var searchDatePicker = this;
      searchDatePicker.seasonLength = parseInt(searchDatePicker.seasonLength);
      searchDatePicker.datePattern = searchDatePicker.searchConfig.DATE_PATTERN;
      searchDatePicker.inherited(arguments);
      searchDatePicker.initSearchMessaging();

      searchDatePicker.firstDate = dojo.clone(searchDatePicker.datePickerShowDate);
      searchDatePicker.endDate = dojo.clone(searchDatePicker.datePickerShowDate);

      searchDatePicker.nextMonthFirstDate = dojo.clone(searchDatePicker.datePickerNextMonthShowDate);

      searchDatePicker.endDate.setMonth(searchDatePicker.firstDate.getMonth() - 1 + searchDatePicker.seasonLength);
      searchDatePicker.endDate.setDate(parseInt(searchDatePicker.totalDays[searchDatePicker.endDate.getMonth()], 10));
      searchDatePicker.seasonDateFormat();
      searchDatePicker.resetPlaceHolder();

      searchDatePicker.connect(searchDatePicker.domNode, "onclick", function () {
        var dateError = dojo.clone(searchDatePicker.searchPanelModel.searchErrorMessages.get("when"));
        if (dateError.emptyDate) {
          delete dateError.emptyDate;
          searchDatePicker.searchPanelModel.searchErrorMessages.set("when", dateError);
        }
        // publish opening event for widgets that need to close
        searchDatePicker.publishMessage("searchPanel/searchOpening");
      });

      dojo.subscribe("tui/searchPanel/view/AutoComplete/datePicker", function () {
        searchDatePicker.setFieldValue(searchDatePicker.searchMessaging.date.foplaceholder);
        searchDatePicker.onCalendarFocus();
      });

      dojo.subscribe("tui/searchPanel/searchOpening", function (component) {
        if (!searchDatePicker.datepickerDOM) return;
        if (component !== searchDatePicker && searchDatePicker.isShowing(searchDatePicker.datepickerDOM)) {
          searchDatePicker.close();
        }
      });

      searchDatePicker.tagElement(searchDatePicker.domNode, "Dep-Date");
      dojo.global.isWhenErrorMessageNew=true;
    },

    seasonDateFormat : function(){
    	var searchDatePicker = this,
    		parts = searchDatePicker.seasonEndDate.split('-');
    		searchDatePicker.seasonEndDate = new Date(parts[0],parts[1]-1,parts[2]);
    },

    resetPlaceHolder: function () {
      var searchDatePicker = this;
      dojo.query(searchDatePicker.domNode)[0].innerHTML = "<span style='font-style:italic'>" + searchDatePicker.searchMessaging.date.foplaceholder + "</span>";

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
      searchDatePicker.setFieldValue(newvalue, 'when');
    },

    displayDateErrorMessage: function (name, oldError, newError) {
      // summary:
      //		Watcher method, displays/removes error message when error messaging model is updated
      var searchDatePicker = this;
      if(dojo.global.isWhenErrorMessageNew){
      searchDatePicker.validateErrorMessage(newError.emptyDate, {
        errorMessage: newError.emptyDate,
        arrow: true,
        field: "when",
        key: "emptyDate"
      });
      dojo.global.isWhenErrorMessageNew=false;
      }
    },

    open: function () {
      // summary:
      //		Extends default method to call routeMessage method
      var searchDatePicker = this;
      searchDatePicker.inherited(arguments);

    },

    /*Instead of this method called super class method directly*/
    /*redrawDatePicker: function () {
      // summary:
      //		Extends default method to show message if valid route selected
      var searchDatePicker = this;
      searchDatePicker.inherited(arguments);
    },*/

    renderDatePicker: function () {
      // summary:
      //		Extends default method so datepicker will wait for data before rendering
      var searchDatePicker = this;
      var args = arguments;
      dojo.when(searchDatePicker.dateStore.requestData(searchDatePicker.searchPanelModel), function () {
        searchDatePicker.inherited(args);
      });
    },

    focusCalendar: function (controller) {
      var searchDatePicker = this;
      if(controller && searchDatePicker.widgetController !== controller) return;
       searchDatePicker.domNode.focus();
      searchDatePicker.onCalendarFocus();
    },

    onCalendarFocus: function () {
      // summary:
      //		Extends default method so datepicker will wait for data before proceeding
      var searchDatePicker = this;
      var args = arguments;
      if(searchDatePicker.errorPopup){
    	  searchDatePicker.errorPopup.close();
      }
      if(!searchDatePicker.searchPanelModel.date){
    	  var dt = new Date();
    	  dt.setDate(1);
    	  searchDatePicker.datePickerShowDate = dt;
    	  var monthAndYear = [(searchDatePicker.datePickerShowDate.getMonth() +1), '/', searchDatePicker.datePickerShowDate.getFullYear()].join('');
	   		if(searchDatePicker.dateSelectOption[0]){
	   			searchDatePicker.dateSelectOption[0].setSelectedValue(monthAndYear);
	   		}
      }
      dojo.addClass(searchDatePicker.domNode, "loading");
      dojo.when(searchDatePicker.dateStore.requestData(searchDatePicker.searchPanelModel), function () {
        if (searchDatePicker.datepickerDOM) {
          searchDatePicker.redrawDatePicker(null);
          searchDatePicker.setDateSelectOption();
        }
        dojo.removeClass(searchDatePicker.domNode, "loading");
        searchDatePicker.inherited(args);
      });
    },

    onBeforeTmplRender: function () {
        // summary:
        //		Extends default method so datepicker will change to next month with available dates on render
        var searchDatePicker = this;
        var isFirstMonthDatesAvailable = true;
        var isSecondMonthDatesAvailable = true;
        dojo.query('.first-month-available', searchDatePicker.datepickerDOM).remove();
        dojo.query('.second-month-available', searchDatePicker.datepickerDOM).remove();
        if(dojo.query(".mnoth-bar")[0]) domConstruct.destroy(dojo.query(".mnoth-bar")[0]);
        searchDatePicker.isFirstMonthNotAvailableByDefault = false;
        searchDatePicker.isSecondMonthNotAvailableByDefault = false;
        if (searchDatePicker.availabilityLinkConnects && searchDatePicker.availabilityLinkConnects.length > 0) {
          for (var i = 0; i < searchDatePicker.availabilityLinkConnects; i++) {
            dojo.disconnect(searchDatePicker.availabilityLinkConnects[i]);
          }
        }

        if (searchDatePicker.nextMonthAvailabilityLinkConnects && searchDatePicker.nextMonthAvailabilityLinkConnects.length > 0) {
          for (var i = 0; i < searchDatePicker.nextMonthAvailabilityLinkConnects; i++) {
            dojo.disconnect(searchDatePicker.nextMonthAvailabilityLinkConnects[i]);
          }
        }

        var storedDatesRepo = searchDatePicker.getStoredDates(false);
        //First Month
        if (!searchDatePicker.validDatesTest(searchDatePicker.datePickerShowDate, storedDatesRepo)) {
          if (searchDatePicker.skipMonth) {
            var date = searchDatePicker.findAvailableMonth(searchDatePicker.datePickerShowDate, "forward") ?
                searchDatePicker.findAvailableMonth(searchDatePicker.datePickerShowDate, "forward").split('-') :
                false;
          /*  var immediateCurrentMonth = searchDatePicker.datePickerShowDate.getMonth() + 1;
            var immediateCurrentMonthsYear = searchDatePicker.datePickerShowDate.getFullYear();*/
            if (date) {
              searchDatePicker.datePickerShowDate.setMonth(date[0] - 1);
              searchDatePicker.datePickerShowDate.setFullYear(date[1]);
              searchDatePicker.monthAndYearTxt = [searchDatePicker.months[date[0] -
                  1], ' ', searchDatePicker.datePickerShowDate.getFullYear()].join('');
              searchDatePicker.setup(searchDatePicker.datePickerShowDate);
              isFirstMonthDatesAvailable = true;
            }
            else {
            	searchDatePicker.renderCurrentMonthAvailabilityMessageByDefault();
            	isFirstMonthDatesAvailable = false;
            }
          }
          else {
            searchDatePicker.renderAvailabilityMessage();
            isFirstMonthDatesAvailable = false;
          }
        }
        else {
          if (searchDatePicker.dateSelectOption.length > 0) {
            searchDatePicker.unsubscribeDateSelectOption();
            searchDatePicker.dateSelectOption[0].setSelectedValue([searchDatePicker.datePickerShowDate.getMonth() +
                1, '/', searchDatePicker.datePickerShowDate.getFullYear()].join(''));
            searchDatePicker.subscribeDateSelectOption();
            isFirstMonthDatesAvailable = true;
          }
        }

        searchDatePicker.skipMonth = true;
        //Check if second month also is equal to first month, if same increase the nextMont to  +1
        if(dojo.date.compare(searchDatePicker.datePickerNextMonthShowDate,searchDatePicker.datePickerShowDate) <= 0){
        	if(searchDatePicker.datePickerShowDate.getMonth() !== 11){
        		searchDatePicker.datePickerNextMonthShowDate.setMonth(searchDatePicker.datePickerShowDate.getMonth()+1);
        		searchDatePicker.datePickerNextMonthShowDate.setFullYear(searchDatePicker.datePickerShowDate.getFullYear());
        	} else {
        		searchDatePicker.datePickerNextMonthShowDate.setMonth(11);
        		//searchDatePicker.datePickerNextMonthShowDate.setFullYear(searchDatePicker.datePickerNextMonthShowDate.getFullYear()+1);
        		if(!searchDatePicker.findAvailableNextMonth(searchDatePicker.datePickerNextMonthShowDate, "forward")){
        			searchDatePicker.renderNextMonthAvailabilityMessageByDefault();
                    isSecondMonthDatesAvailable = false;
        		}
        		if(searchDatePicker.datePickerNextMonthShowDate.getMonth() === 11){
        			searchDatePicker.datePickerNextMonthShowDate.setMonth(0);
            		searchDatePicker.datePickerNextMonthShowDate.setFullYear(searchDatePicker.datePickerNextMonthShowDate.getFullYear()+1);
            		isSecondMonthDatesAvailable = true;
        		}
        	}

        	searchDatePicker.monthAndYearTxtForNextMonth = [searchDatePicker.months[searchDatePicker.datePickerShowDate.getMonth()+1], ' ', searchDatePicker.datePickerNextMonthShowDate.getFullYear()].join('');
	        searchDatePicker.setupnextmonth(searchDatePicker.datePickerNextMonthShowDate);
        } else {
        //Second Month
        if (!searchDatePicker.validDatesTest(searchDatePicker.datePickerNextMonthShowDate, storedDatesRepo)) {
            if (searchDatePicker.skipNextMonth) {
              var nextMonthDate = searchDatePicker.findAvailableNextMonth(searchDatePicker.datePickerNextMonthShowDate, "forward") ?
                  searchDatePicker.findAvailableNextMonth(searchDatePicker.datePickerNextMonthShowDate, "forward").split('-') :
                  false;
              var immediateNextMonth = searchDatePicker.datePickerNextMonthShowDate.getMonth() + 1;
              var immediateNextMonthsYear = searchDatePicker.datePickerNextMonthShowDate.getFullYear();

              if ((nextMonthDate ||
            		  nextMonthDate[0] === immediateNextMonth.toString())
            		  && nextMonthDate[1] === immediateNextMonthsYear.toString()) {
                  searchDatePicker.datePickerNextMonthShowDate.setMonth(nextMonthDate[0] - 1);
    	          searchDatePicker.datePickerNextMonthShowDate.setFullYear(nextMonthDate[1]);
    	          searchDatePicker.monthAndYearTxtForNextMonth = [searchDatePicker.months[nextMonthDate[0] - 1], ' ', searchDatePicker.datePickerNextMonthShowDate.getFullYear()].join('');
    	          searchDatePicker.setupnextmonth(searchDatePicker.datePickerNextMonthShowDate);
    	          isSecondMonthDatesAvailable = true;
    	        }
              else {
            	  searchDatePicker.renderNextMonthAvailabilityMessageByDefault();
                  isSecondMonthDatesAvailable = false;
              }

            } else {
              searchDatePicker.renderNextMonthAvailabilityMessage();
              isSecondMonthDatesAvailable = false;
            }
          }



          searchDatePicker.skipNextMonth = true;

          //Render the Month Bar
          if(!isFirstMonthDatesAvailable && !isSecondMonthDatesAvailable) {
          	searchDatePicker.deleteDatePicker();
          	searchDatePicker.deleteNextMonthDatePicker();

          	  //Month not available message
              searchDatePicker.monthbarmessagestart = searchDatePicker.searchMessaging.date.monthbarmessagestart;
              searchDatePicker.unAvailableMonthName = lang.trim(searchDatePicker.cal_months_labels[searchDatePicker.datePickerShowDate.getMonth()]) +" "+  searchDatePicker.datePickerShowDate.getFullYear();
              searchDatePicker.monthbarmessageend = searchDatePicker.searchMessaging.date.monthbarmessageend;

              searchDatePicker.templateview = "showMonthBar";
          }
      }
   },


    renderAvailabilityMessage: function () {
      // summary: Renders message and links if entire first month is unavailable
      var searchDatePicker = this;
      var curMonth = dojo.clone(searchDatePicker.datePickerShowDate);
      searchDatePicker.monthAndYearTxt = [searchDatePicker.months[searchDatePicker.datePickerShowDate.getMonth()], ' ', searchDatePicker.datePickerShowDate.getFullYear()].join('');
      searchDatePicker.deleteDatePicker();
      searchDatePicker.templateview = "noavailability";
    },

    renderNextMonthAvailabilityMessage: function () {
	  // summary: Renders message and links if entire second month is unavailable
	    var searchDatePicker = this;
	    var curNextMonth = dojo.clone(searchDatePicker.datePickerNextMonthShowDate);
	    searchDatePicker.monthAndYearTxtForNextMonth = [searchDatePicker.months[searchDatePicker.datePickerNextMonthShowDate.getMonth()], ' ', searchDatePicker.datePickerNextMonthShowDate.getFullYear()].join('');
	    searchDatePicker.deleteNextMonthDatePicker();
	    searchDatePicker.templateview = "noavailabilityinsecondmonth";


	},

	renderCurrentMonthAvailabilityMessageByDefault: function () {
        // summary: Renders message and links if entire first month is unavailable by default
        var searchDatePicker = this;
        var curMonth = dojo.clone(searchDatePicker.datePickerShowDate);
        searchDatePicker.monthAndYearTxt = [searchDatePicker.months[searchDatePicker.datePickerShowDate.getMonth()], ' ', searchDatePicker.datePickerShowDate.getFullYear()].join('');
        searchDatePicker.isFirstMonthNotAvailableByDefault = true;
    },

    renderNextMonthAvailabilityMessageByDefault: function () {
        // summary: Renders message and links if entire second month is unavailable by default
        var searchDatePicker = this;
        var curNextMonth = dojo.clone(searchDatePicker.datePickerNextMonthShowDate);
        searchDatePicker.monthAndYearTxtForNextMonth = [searchDatePicker.months[searchDatePicker.datePickerNextMonthShowDate.getMonth()], ' ', searchDatePicker.datePickerNextMonthShowDate.getFullYear()].join('');
        searchDatePicker.isSecondMonthNotAvailableByDefault = true;
    },

    attachAvailabilityListeners: function () {
          var searchDatePicker = this;
          on(searchDatePicker.datepickerDOM,on.selector(searchDatePicker.monthBarOpenOnFirstMonthLinkSelect, "click"), function (event) {
           	  if(dojo.query(".mnoth-bar")[0]) domConstruct.destroy(dojo.query(".mnoth-bar")[0]);
                  searchDatePicker.deleteNextMonthDatePicker();
              	  searchDatePicker.deleteDateSelection();
              	  searchDatePicker.templateview = "showMonthBar";
              	  //Month not available message
              	  if(searchDatePicker.datePickerShowDate.getMonth() == 11){
              		searchDatePicker.datePickerShowDate.setMonth(0);
              	  }
                  searchDatePicker.monthbarmessagestart = searchDatePicker.searchMessaging.date.monthbarmessagestart;
                  searchDatePicker.unAvailableMonthName = lang.trim(searchDatePicker.cal_months_labels[searchDatePicker.datePickerShowDate.getMonth()]) +" "+  searchDatePicker.datePickerShowDate.getFullYear() ;
                  searchDatePicker.monthbarmessageend = searchDatePicker.searchMessaging.date.monthbarmessageend;

                  searchDatePicker.renderDatePicker();
                  searchDatePicker.hideWidget(searchDatePicker.dateSelectOption[0].listElement)
                  searchDatePicker.open();
           });
      },

      attachNextMonthAvailabilityListeners: function () {
        var searchDatePicker = this,datePicerMonth;
        on(searchDatePicker.datepickerDOM,on.selector(searchDatePicker.monthBarOpenOnSecondMonthLinkSelect, "click"), function (event) {
   	     if(dojo.query(".mnoth-bar")[0]) domConstruct.destroy(dojo.query(".mnoth-bar")[0]);
           searchDatePicker.deleteDatePicker();
      	   searchDatePicker.deleteDateSelection();
      	   searchDatePicker.templateview = "showMonthBar";
      	   //Month not available message
	      	 if(searchDatePicker.datePickerShowDate.getMonth() == 11){
	      		datePicerMonth = 0;
	       	  } else {
	       		datePicerMonth = searchDatePicker.datePickerShowDate.getMonth()+1;
	       	  }
           searchDatePicker.monthbarmessagestart = searchDatePicker.searchMessaging.date.monthbarmessagestart;
           searchDatePicker.unAvailableMonthName = lang.trim(searchDatePicker.cal_months_labels[datePicerMonth]) +" "+ searchDatePicker.datePickerShowDate.getFullYear();
           searchDatePicker.monthbarmessageend = searchDatePicker.searchMessaging.date.monthbarmessageend;

           searchDatePicker.renderDatePicker();
           searchDatePicker.hideWidget(searchDatePicker.dateSelectOption[0].listElement)
           searchDatePicker.open();
         });
      },

    findAvailableMonth: function (date, direction) {
      // summary:
      //    find next/previous month with available dates
      var searchDatePicker = this;
      var storedDatesRepo = searchDatePicker.getStoredDates(false);
      direction = direction || "reverse";
      if (direction == "forward") {
        date = dojo.clone(date);
        date.setMonth(date.getMonth() + 1);
        while (date.valueOf() <= searchDatePicker.endDate.valueOf()) {
          if (searchDatePicker.validDatesTest(date, storedDatesRepo)) {
            return [date.getMonth() + 1, '-', date.getFullYear()].join('');
          }
          date.setMonth(date.getMonth() + 1);
        }
      } else {
        date = dojo.clone(date);
        date.setMonth(date.getMonth() - 1);
        while (date.valueOf() >= searchDatePicker.firstDate.valueOf()) {
          if (searchDatePicker.validDatesTest(date, storedDatesRepo)) {
            return [date.getMonth() + 1, '-', date.getFullYear()].join('');
          }
          date.setMonth(date.getMonth() - 1);
        }
      }
      return false;
    },

    findAvailableNextMonth: function (date, direction) {
      // summary:
      //    find next/previous month with available dates
      var searchDatePicker = this;
      var storedDatesRepo = searchDatePicker.getStoredDates(false);
      direction = direction || "reverse";
      if (direction == "forward") {
        date = dojo.clone(date);
        date.setMonth(date.getMonth() + 1);
        while (date.valueOf() <= searchDatePicker.endDate.valueOf()) {
          if (searchDatePicker.validDatesTest(date, storedDatesRepo)) {
            return [date.getMonth() + 1, '-', date.getFullYear()].join('');
          }
          date.setMonth(date.getMonth() + 1);
        }
      } else {
        date = dojo.clone(date);
        date.setMonth(date.getMonth() - 1);
        while (date.valueOf() >= searchDatePicker.nextMonthFirstDate.valueOf()) {
          if (searchDatePicker.validDatesTest(date, storedDatesRepo)) {
            return [date.getMonth() + 1, '-', date.getFullYear()].join('');
          }
          date.setMonth(date.getMonth() - 1);
        }
      }
      return false;
    },
    findAvailablePrevMonth: function(date){
    	var searchDatePicker = this,
    	storedDatesRepo = searchDatePicker.getStoredDates(false);
    	 date = dojo.clone(date);
         while (date.valueOf() >= searchDatePicker.nextMonthFirstDate.valueOf()) {
           if (searchDatePicker.validDatesTest(date, storedDatesRepo)) {
             return [date.getMonth() + 1, '-', date.getFullYear()].join('');
           }
           date.setMonth(date.getMonth() - 1);
         }
         return false;
    },
    validDatesTest: function (curDate, storedDates) {
      // summary:
      //		Test for valid dates
      var searchDatePicker = this;
      for (var i = 0; i < storedDates.length; i++) {
        var string = storedDates[i].split('-');
        if (parseInt(string[1], 10) === curDate.getMonth() + 1 &&
            parseInt(string[2], 10) === curDate.getFullYear()) {
          return true;
        }
      }
      return false;
    },

    onAfterTmplRender: function () {
      // summary:
      //		Extends default method to call eligible dates method

      var searchDatePicker = this,nextMonth,rightArrow,leftArrow,fb;
      searchDatePicker.inherited(arguments);
      var StoredDates = searchDatePicker.getStoredDates(false);
      if (searchDatePicker.templateview === "noavailability") {
    	  	leftArrow = dojo.query(".prev", searchDatePicker.datepickerDOM)[0];
    	  	dojo.addClass(leftArrow, "disabled");
	    	searchDatePicker.secondMonthEligibleDates(searchDatePicker.getStoredDates(false));
	        searchDatePicker.attachAvailabilityListeners();
      }
      else if (searchDatePicker.templateview === "noavailabilityinsecondmonth") {
    	  	rightArrow = dojo.query(".next", searchDatePicker.datepickerDOM)[0];
    	  	dojo.addClass(rightArrow, "disabled");
    	  	searchDatePicker.firstMonthEligibleDates(searchDatePicker.getStoredDates(false));
    	  	searchDatePicker.attachNextMonthAvailabilityListeners();
      }
      else if (searchDatePicker.templateview === "showMonthBar") {
    	  	searchDatePicker.deleteDateSelection();
    	  	searchDatePicker.getStoredDates(true);
    	  	fb = new flightSearchMonthBar();
    	  	fb.generateMonths(searchDatePicker.seasonStartDate, searchDatePicker.seasonLength);
    	  	dojo.place(dojo.query(".monthSelector")[0], dojo.query(".mnoth-bar")[0],"last");
      }
      else {
      	if(searchDatePicker.isFirstMonthNotAvailableByDefault) {
      		searchDatePicker.secondMonthEligibleDates(searchDatePicker.getStoredDates(false));
      		searchDatePicker.attachAvailabilityListeners();
        	}
      	else if(searchDatePicker.isSecondMonthNotAvailableByDefault) {
      		searchDatePicker.firstMonthEligibleDates(searchDatePicker.getStoredDates(false));
      		searchDatePicker.attachNextMonthAvailabilityListeners();
      	}
      	else {
      		searchDatePicker.eligibleDates(searchDatePicker.getStoredDates(false));
      	}
      }

      if (searchDatePicker.endDate) {
        searchDatePicker.disableUnavailableDates(searchDatePicker.endDate, false);
      }


      searchDatePicker.tagElement(searchDatePicker.datepickerDOM, "OverallDeparting");
      if(searchDatePicker.searchPanelModel.oneWayOnly === true)
    	  {
    	  dojo.byId("oWay").checked = true;
    	  dojo.query(".oneWayChkCnt .checkbox").addClass("checked");
    	  }

      	nextMonth =new Date(searchDatePicker.datePickerNextMonthShowDate);
		nextMonth.setMonth(nextMonth.getMonth()+1);

		var nextAvailMonthandYear = searchDatePicker.findAvailableNextMonth(searchDatePicker.datePickerShowDate,"forward");

		var disableRightArrow = false;
		if(nextAvailMonthandYear){
			nextAvailMonthandYear = new Date(parseInt(nextAvailMonthandYear.split("-")[1]),parseInt(nextAvailMonthandYear.split("-")[0]),searchDatePicker.selectedDate.getDay());
			if(dojo.date.difference(searchDatePicker.datePickerShowDate,nextAvailMonthandYear,"month")>2){
				disableRightArrow = true;
			}
		} else {
			disableRightArrow = true;
		}
		if(nextMonth.valueOf() > searchDatePicker.seasonEndDate.valueOf() || disableRightArrow){
			rightArrow = dojo.query(".next", searchDatePicker.datepickerDOM)[0];
	        dojo.addClass(rightArrow, "disabled");
		}

		var prevAvailMonthandYear = searchDatePicker.findAvailablePrevMonth(searchDatePicker.datePickerShowDate);
		var disableLeftArrow = false;
		if(prevAvailMonthandYear){
			prevAvailMonthandYear = new Date(parseInt(prevAvailMonthandYear.split("-")[1]),parseInt(prevAvailMonthandYear.split("-")[0]),searchDatePicker.selectedDate.getDay());
			if(dojo.date.difference(searchDatePicker.datePickerShowDate,prevAvailMonthandYear,"month")>1){
				disableLeftArrow = true;
			}
		} else {
			disableLeftArrow = true;
		}
		if(disableLeftArrow){
			leftArrow = dojo.query(".prev", searchDatePicker.datepickerDOM)[0];
	        dojo.addClass(leftArrow, "disabled");
		}

    },

    deleteDateSelection: function () {
        var searchDatePicker = this;
        dojo.style(dojo.query(".departuredate-selector", searchDatePicker.datepickerDOM)[0], {
        	"display":"none"
        });
    },

    eligibleDates: function (dateStoreData) {
      // summary:
      //   Disables non-eligible dates
      //   params: dateStoreData (Array)
      var searchDatePicker = this;
      var totalDays = searchDatePicker.totalDays[searchDatePicker.datePickerShowDate.getMonth()];
      var month = dojo.clone(searchDatePicker.datePickerShowDate);

      var secondMonthTotalDays = searchDatePicker.nextMonthTotalDays[searchDatePicker.datePickerNextMonthShowDate.getMonth()];
      var secondMonth = dojo.clone(searchDatePicker.datePickerNextMonthShowDate);

      var now = new Date();
      var today = new Date(now.getFullYear(), now.getMonth(), now.getDate());

      // First Month
      for (var i = month.getDate(); i <= totalDays; i++) {
        if (dojo.indexOf(dateStoreData, searchDatePicker.formatDate(month)) === -1) {
          dojo.addClass(searchDatePicker.daycells[month.getDate() - 1], "unavailable");
          dojo.addClass(searchDatePicker.daycells[month.getDate() - 1], "disabled");
          // adding past dates class just in case (should not be necessary as unavailable won't be sent)
          if (today.getMonth() === month.getMonth() && today.getFullYear() === month.getFullYear() &&
              i <= today.getDate()) {
            dojo.addClass(searchDatePicker.daycells[month.getDate() - 1], "past");
          }
        }
        month.setDate(month.getDate() + 1);
      }

      // Second Month
      for (var i = secondMonth.getDate(); i <= secondMonthTotalDays; i++) {
          if (dojo.indexOf(dateStoreData, searchDatePicker.formatDate(secondMonth)) === -1) {
            dojo.addClass(searchDatePicker.nextMonthdaycells[secondMonth.getDate() - 1], "unavailable");
            dojo.addClass(searchDatePicker.nextMonthdaycells[secondMonth.getDate() - 1], "disabled");
          }
          secondMonth.setDate(secondMonth.getDate() + 1);
        }
    },

    firstMonthEligibleDates: function (dateStoreData) {
        // summary:
        //   Disables non-eligible first month dates
        //   params: dateStoreData (Array)
        var searchDatePicker = this;

        var totalDays = searchDatePicker.totalDays[searchDatePicker.datePickerShowDate.getMonth()];
        var month = dojo.clone(searchDatePicker.datePickerShowDate);

        var now = new Date();
        var today = new Date(now.getFullYear(), now.getMonth(), now.getDate());

        for (var i = month.getDate(); i <= totalDays; i++) {
          if (dojo.indexOf(dateStoreData, searchDatePicker.formatDate(month)) === -1) {
            dojo.addClass(searchDatePicker.daycells[month.getDate() - 1], "unavailable");
            dojo.addClass(searchDatePicker.daycells[month.getDate() - 1], "disabled");
            // adding past dates class just in case (should not be necessary as unavailable won't be sent)
            if (today.getMonth() === month.getMonth() && today.getFullYear() === month.getFullYear() &&
                i <= today.getDate()) {
              dojo.addClass(searchDatePicker.daycells[month.getDate() - 1], "past");
            }
          }
          month.setDate(month.getDate() + 1);
        }
      },

      secondMonthEligibleDates: function (dateStoreData) {
          // summary:
          //   Disables non-eligible second month dates
          //   params: dateStoreData (Array)
          var searchDatePicker = this;

          var secondMonthTotalDays = searchDatePicker.nextMonthTotalDays[searchDatePicker.datePickerNextMonthShowDate.getMonth()];
          var secondMonth = dojo.clone(searchDatePicker.datePickerNextMonthShowDate);

          for (var i = secondMonth.getDate(); i <= secondMonthTotalDays; i++) {
              if (dojo.indexOf(dateStoreData, searchDatePicker.formatDate(secondMonth)) === -1) {
                dojo.addClass(searchDatePicker.nextMonthdaycells[secondMonth.getDate() - 1], "unavailable");
                dojo.addClass(searchDatePicker.nextMonthdaycells[secondMonth.getDate() - 1], "disabled");
              }
              secondMonth.setDate(secondMonth.getDate() + 1);
            }
       },

    formatDate: function (date) {
      // summary:
      //		Formats date object to match JSON format
      var searchDatePicker = this;
      return dojo.date.locale.format(date, {
        selector: "date",
        datePattern: "dd-MM-yyyy"
      });
    },

    setFieldValue: function (newdate) {
      // summary:
      // 		Extend default method and write value to model
      var searchDatePicker = this;
      searchDatePicker.inherited(arguments);
      searchDatePicker.searchPanelModel.set("date", searchDatePicker.getFieldValue());
      searchDatePicker.searchPanelModel.set("returnDate", "");
      dijit.byId("returnTravel").selectedDate = new Date();
      dojo.query("#returnTravel")[0].innerHTML = "<span style='font-style:italic'>" + searchDatePicker.searchMessaging.date.arrplaceholder + "</span>";
    },

    setDateSelectOption: function () {
      // summary:
      // 		override default datepicker method, treat month/year as one item
      var searchDatePicker = this;
      var monthAndYear = [(searchDatePicker.datePickerShowDate.getMonth() +
          1), '/', searchDatePicker.datePickerShowDate.getFullYear()].join('');
      if (searchDatePicker.dateSelectOption[0].getSelectedData().value !== monthAndYear) {
        searchDatePicker.skipMonth = false;
        searchDatePicker.skipNextMonth = false;
        searchDatePicker.dateSelectOption[0].setSelectedValue(monthAndYear);
      }
    },

    getStoredDates: function (isMonthBarEnabled) {
      // summary:
      // 		retrieves dates array from store based on current route
      //    returns array of eligible dates
      var searchDatePicker = this;
      var searchquery = searchDatePicker.searchPanelModel.generateQueryObject();
      var destinations = _.map(dojo.clone(searchquery.to), function(toItem){
        return splitDestinationQuery(toItem);
      });
      var id = [destinations, searchquery.from].join('');
      id = (id === '') ? "all" : id;

      /*Month Bar Functionality*/
      if(isMonthBarEnabled) {
    		  searchDatePicker.tempAvailableMonths = searchDatePicker.dateStore.get(id).dates;
        	  searchDatePicker.populateMonths();
      }
      /*Month Bar Functionality*/
      return searchDatePicker.dateStore.get(id) ? searchDatePicker.dateStore.get(id).dates : [];
    },

    /*Month Bar Functionality*/
    populateMonths: function(){
        var searchDatePicker = this;
        searchDatePicker.finalAvailableMonths = [];
			for(var i = 0;i < searchDatePicker.seasonLength; i++){
			   var monthAndYear = searchDatePicker.getFullMonths(i);
			   var monandyrinDigit = searchDatePicker.getMonthSlicedIndex(i);
			   var arr = monandyrinDigit.split("@");
			   var obj = {available: false, month: monthAndYear};
			   var tmpX = true;
			   	_.each(searchDatePicker.tempAvailableMonths, function(elm){
			   		if(tmpX){
				   		if(arr[1] === elm.substring(3,5) && arr[0] === elm.substring(6,10)){
				   			obj.available = true;
				   			tmpX = false;
				   		}
			   		}
			   	});
			   	obj.mon = arr[1]; obj.year = arr[0];
			   	searchDatePicker.finalAvailableMonths.push(obj);
			}
			dojo.global.availableMonths = searchDatePicker.finalAvailableMonths;
	  },

   getMonthSlicedIndex:function(i){
		var searchDatePicker = this;
		var dt = new Date();
		dt.setDate(1);
		var mons = (dt).setMonth(dt.getMonth()+i);
		var mon = lang.trim(searchDatePicker.cal_months_labels[searchDatePicker.cal_next_date(mons).getMonth()].substring(0,3).toUpperCase());
		var fullMonth = lang.trim(searchDatePicker.cal_months_labels[searchDatePicker.cal_next_date(mons).getMonth()].toUpperCase());
		var monInIndex = Number(new Date(mons).getMonth()) + 1;
		if(monInIndex < 10) monInIndex = "0" + monInIndex;
		return  (new Date(mons).getFullYear() + "@" +  monInIndex);
	},

	getFullMonths:function(i){
		var searchDatePicker = this;
		var dt = new Date();
		dt.setDate(1);
		var mons = (dt).setMonth(dt.getMonth()+i);
		var mon = lang.trim(searchDatePicker.cal_months_labels[searchDatePicker.cal_next_date(mons).getMonth()].substring(0,3).toUpperCase());
		var fullMonth = lang.trim(searchDatePicker.cal_months_labels[searchDatePicker.cal_next_date(mons).getMonth()]);
		return  (fullMonth+" "+new Date(mons).getFullYear());
	},

	cal_next_date: function(dt){
		return new Date(dt);
	},
	/*Month Bar Functionality*/

    monthDiff: function (date1, date2) {
      // summary:
      // 		returns difference in months between 2 date objects
      var months;
      months = (date2.getFullYear() - date1.getFullYear()) * 12;
      months -= date1.getMonth() + 1;
      months += date2.getMonth();
      return months <= 0 ? 0 : months;
    }
  });

  return tui.searchPanel.view.flights.SearchDatePicker;
});
