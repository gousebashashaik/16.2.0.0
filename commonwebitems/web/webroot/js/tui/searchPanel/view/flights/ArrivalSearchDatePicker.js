define("tui/searchPanel/view/flights/ArrivalSearchDatePicker", [
  "dojo",
  "dojo/store/Observable",
  "dojo/dom-attr",
  "dojo/dom-construct",
  "dojo/text!tui/searchPanel/view/flights/templates/arrivalSearchDatepicker.html",
  "tui/searchPanel/view/flights/ArrivalFlightSearchMonthBar",
  "tui/searchPanel/store/flights/ReturnDateStore",
  "dojo/_base/lang",
  "dojo/on",
  "tui/widget/popup/Tooltips",
  "dojo/Stateful",
  "dojo/string",
  "tui/search/nls/Searchi18nable",
  "tui/searchPanel/view/flights/SearchErrorMessaging",
  "tui/widget/datepicker/ArrivalDatePicker",
  "dojo/NodeList-traverse"], function (dojo, Observable, domAttr, domConstruct, arrivalSearchDatePickerTmpl, arrivalFlightSearchMonthBar, ReturnDateStore, lang, on,tooltips,Stateful) {

  function splitDestinationQuery(query) {
    return query.split(':')[0];
  }

  dojo.declare("tui.searchPanel.view.flights.ArrivalSearchDatePicker", [tui.widget.datepicker.ArrivalDatePicker, tui.search.nls.Searchi18nable, tui.searchPanel.view.flights.SearchErrorMessaging], {

    // ----------------------------------------------------------------------------- properties

    datePattern: null,

    monthsAndYears: null,

    monthAndYearTxt: null,

    monthAndYearTxtForNextMonth: null,

    seasonLength: null,

    tmpl: arrivalSearchDatePickerTmpl,

    returnDateStore: null,

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
      var arrivalSearchDatePicker = this;
      arrivalSearchDatePicker.seasonLength = arrivalSearchDatePicker.seasonLength;
      arrivalSearchDatePicker.datePattern = arrivalSearchDatePicker.searchConfig.DATE_PATTERN;
      arrivalSearchDatePicker.inherited(arguments);
      arrivalSearchDatePicker.initSearchMessaging();

      arrivalSearchDatePicker.firstDate = dojo.clone(arrivalSearchDatePicker.datePickerShowDate);
      arrivalSearchDatePicker.endDate = dojo.clone(arrivalSearchDatePicker.datePickerShowDate);
      arrivalSearchDatePicker.nextMonthFirstDate = dojo.clone(arrivalSearchDatePicker.datePickerNextMonthShowDate);
      arrivalSearchDatePicker.endDate.setMonth(arrivalSearchDatePicker.firstDate.getMonth() - 1 + arrivalSearchDatePicker.seasonLength);
      arrivalSearchDatePicker.endDate.setDate(parseInt(arrivalSearchDatePicker.totalDays[arrivalSearchDatePicker.endDate.getMonth()], 10));
      arrivalSearchDatePicker.seasonDateFormat();

      arrivalSearchDatePicker.resetPlaceHolder();

      arrivalSearchDatePicker.connect(arrivalSearchDatePicker.domNode, "onclick", function () {
        var dateError = dojo.clone(arrivalSearchDatePicker.searchPanelModel.searchErrorMessages.get("when"));
        if (dateError.emptyDate) {
          delete dateError.emptyDate;
          arrivalSearchDatePicker.searchPanelModel.searchErrorMessages.set("when", dateError);
        }
        // publish opening event for widgets that need to close

        arrivalSearchDatePicker.publishMessage("searchPanel/searchOpening");
      });

      dojo.subscribe("tui/searchPanel/view/AutoComplete/datePicker", function () {
        arrivalSearchDatePicker.setFieldValue(arrivalSearchDatePicker.searchMessaging.date.arrplaceholder);
        arrivalSearchDatePicker.onCalendarFocus();
      });

      dojo.subscribe("tui/searchPanel/searchOpening", function (component) {
        if (!arrivalSearchDatePicker.arrivalDatePickerDOM) return;
        if (component !== arrivalSearchDatePicker && arrivalSearchDatePicker.isShowing(arrivalSearchDatePicker.arrivalDatePickerDOM)) {
          arrivalSearchDatePicker.close();
        }
      });
      arrivalSearchDatePicker.tagElement(arrivalSearchDatePicker.domNode, "Returning");
      dojo.global.isReturnErrorMessageNew=true;
      arrivalSearchDatePicker.watchCallback();
    },

    seasonDateFormat : function(){
    	var arrivalSearchDatePicker = this,
    		parts = arrivalSearchDatePicker.seasonEndDate.split('-');
    		arrivalSearchDatePicker.seasonEndDate = new Date(parts[0],parts[1]-1,parts[2]);
    },

    resetPlaceHolder: function () {
      var arrivalSearchDatePicker = this;
      dojo.query(arrivalSearchDatePicker.domNode)[0].innerHTML = "<span style='font-style:italic'>" + arrivalSearchDatePicker.searchMessaging.date.arrplaceholder + "</span>";
    },

    setReturnFormatedDate: function (name, oldvalue, newvalue) {
      // summary:
      //		Watcher method, sets datePicker value based on searchPanelModel
      var arrivalSearchDatePicker = this;
      if (arrivalSearchDatePicker.getFieldValue() === newvalue) return;

      if(!newvalue) {
        arrivalSearchDatePicker.resetPlaceHolder();
        return;
      }

      newvalue = dojo.date.locale.parse(newvalue, {
        selector: "date",
        datePattern: arrivalSearchDatePicker.datePattern
      });

      arrivalSearchDatePicker.selectedDate = newvalue;
      arrivalSearchDatePicker.setFieldValue(newvalue, 'returnTravel');
    },

    displayReturnDateErrorMessage: function (name, oldError, newError) {
      // summary:
      //		Watcher method, displays/removes error message when error messaging model is updated
      var arrivalSearchDatePicker = this;
      if(dojo.global.isReturnErrorMessageNew){
      arrivalSearchDatePicker.validateErrorMessage(newError.emptyDate, {
        errorMessage: newError.emptyDate,
        arrow: true,
        field: "returnTravel",
        key: "emptyDate"
      });
      dojo.global.isReturnErrorMessageNew=false;
      }
    },

    open: function () {
      // summary:
      //		Extends default method to call routeMessage method
      var arrivalSearchDatePicker = this;
      arrivalSearchDatePicker.inherited(arguments);
    },

    /*Instead of this method called super class method directly*/
    /*redrawDatePicker: function () {
      // summary:
      //		Extends default method to show message if valid route selected
      var arrivalSearchDatePicker = this;
      arrivalSearchDatePicker.inherited(arguments);
    },*/

    renderDatePicker: function () {
      // summary:
      //		Extends default method so datepicker will wait for data before rendering
      var arrivalSearchDatePicker = this;
      var args = arguments;
      dojo.when(arrivalSearchDatePicker.returnDateStore.requestData(arrivalSearchDatePicker.searchPanelModel), function () {
        arrivalSearchDatePicker.inherited(args);
      });
    },

    focusCalendar: function (controller) {
      var arrivalSearchDatePicker = this;
      if(controller && arrivalSearchDatePicker.widgetController !== controller) return;
      arrivalSearchDatePicker.domNode.focus();
      arrivalSearchDatePicker.onCalendarFocus();
    },

    onCalendarFocus: function () {
      // summary:
      //		Extends default method so datepicker will wait for data before proceeding
      var arrivalSearchDatePicker = this;
      var args = arguments;

      if(arrivalSearchDatePicker.errorPopup){
    	  arrivalSearchDatePicker.errorPopup.close();
      }

      if(!arrivalSearchDatePicker.searchPanelModel.returnDate){

	   		var monthAndYear, dt, tempDate;
	   		if(arrivalSearchDatePicker.searchPanelModel.date){
	   			dt=	dojo.date.locale.parse(arrivalSearchDatePicker.searchPanelModel.date,{
	   					selector:'date',
	   					datePattern: arrivalSearchDatePicker.datePattern
	   				});

	   			//Season End Date Validation
	   			tempDate = dt.getDate();
	   			dt.setDate(parseInt(arrivalSearchDatePicker.totalDays[dt.getMonth()], 10));
	   			arrivalSearchDatePicker.seasonEndValue = false;
	            if(dt.valueOf() >= arrivalSearchDatePicker.seasonEndDate.valueOf()){
	            	dt.setDate(1);
	            	dt.setMonth(dt.getMonth()-1);
	            	arrivalSearchDatePicker.seasonEndValue = true;
	            }


	   		 arrivalSearchDatePicker.monthAndYearTxt = [arrivalSearchDatePicker.months[dt.getMonth()], ' ', dt.getFullYear()].join('');
	   		 arrivalSearchDatePicker.setup(dt);
	    	  }else{
	    		  dt = new Date()
	    	  }


	   		dt.setDate(1);
	   		arrivalSearchDatePicker.datePickerShowDate = dt;
	   		monthAndYear = [(arrivalSearchDatePicker.datePickerShowDate.getMonth() +1), '/', arrivalSearchDatePicker.datePickerShowDate.getFullYear()].join('');
	   		if(arrivalSearchDatePicker.dateSelectOption[0]){
	   			arrivalSearchDatePicker.dateSelectOption[0].setSelectedValue(monthAndYear);
	   		}
      }

      // validate that a date is set
      if (arrivalSearchDatePicker.searchPanelModel.date === "" || !arrivalSearchDatePicker.searchPanelModel.parseToDate(arrivalSearchDatePicker.searchPanelModel.date)) {
    	  arrivalSearchDatePicker.searchPanelModel.searchErrorMessages.set("when", {
    		  emptyDate: arrivalSearchDatePicker.searchPanelModel.searchMessaging.errors.emptyDate
    	  });
		  if(dijit.byId("when").errorPopup !== null){
			  dijit.byId("when").errorPopup.open();
			  dojo.addClass(dijit.byId("when").domNode,"error");
		  }
          	  return;
      }

      // validate that a oneWay is set
      if (arrivalSearchDatePicker.searchPanelModel.oneWayOnly) {
          	  return;
      }


      dojo.addClass(arrivalSearchDatePicker.domNode, "loading");
      dojo.when(arrivalSearchDatePicker.returnDateStore.requestData(arrivalSearchDatePicker.searchPanelModel), function () {


    	if (!arrivalSearchDatePicker.datepickerDOM) {
    		arrivalSearchDatePicker.renderDatePicker();
        }

        if (arrivalSearchDatePicker.arrivalDatePickerDOM) {

        	arrivalSearchDatePicker.datePickerShowDate.setDate(parseInt(arrivalSearchDatePicker.totalDays[arrivalSearchDatePicker.datePickerShowDate.getMonth()], 10));
            if(arrivalSearchDatePicker.datePickerShowDate.valueOf() >= arrivalSearchDatePicker.seasonEndDate.valueOf()){
            	arrivalSearchDatePicker.datePickerShowDate.setDate(1);
            	arrivalSearchDatePicker.datePickerShowDate.setMonth(arrivalSearchDatePicker.datePickerShowDate.getMonth()-1);
            	arrivalSearchDatePicker.skipMonth= false;
            	arrivalSearchDatePicker.seasonEndValue = true;
            }
            arrivalSearchDatePicker.datePickerShowDate.setDate(1);

          arrivalSearchDatePicker.redrawDatePicker(null);
          arrivalSearchDatePicker.setDateSelectOption();
        }
        dojo.removeClass(arrivalSearchDatePicker.domNode, "loading");
        arrivalSearchDatePicker.inherited(args);
      });
    },

    onBeforeTmplRender: function () {
        // summary:
        //		Extends default method so datepicker will change to next month with available dates on render
        var arrivalSearchDatePicker = this,
        	isFirstMonthDatesAvailable = true,
        	isSecondMonthDatesAvailable = true;


        dojo.query('.first-month-available', arrivalSearchDatePicker.arrivalDatePickerDOM).remove();
        dojo.query('.second-month-available', arrivalSearchDatePicker.arrivalDatePickerDOM).remove();
        if(dojo.query(".mnoth-bar")[0]) domConstruct.destroy(dojo.query(".mnoth-bar")[0]);
        if (arrivalSearchDatePicker.availabilityLinkConnects && arrivalSearchDatePicker.availabilityLinkConnects.length > 0) {
          for (var i = 0; i < arrivalSearchDatePicker.availabilityLinkConnects; i++) {
            dojo.disconnect(arrivalSearchDatePicker.availabilityLinkConnects[i]);
          }
        }

        if (arrivalSearchDatePicker.nextMonthAvailabilityLinkConnects && arrivalSearchDatePicker.nextMonthAvailabilityLinkConnects.length > 0) {
          for (var i = 0; i < arrivalSearchDatePicker.nextMonthAvailabilityLinkConnects; i++) {
            dojo.disconnect(arrivalSearchDatePicker.nextMonthAvailabilityLinkConnects[i]);
          }
        }

        var storedDatesRepo = arrivalSearchDatePicker.getStoredDates(false);

        //First Month
        if (!arrivalSearchDatePicker.validDatesTest(arrivalSearchDatePicker.datePickerShowDate, storedDatesRepo)) {
          if (arrivalSearchDatePicker.skipMonth) {
            var date = arrivalSearchDatePicker.findAvailableMonth(arrivalSearchDatePicker.datePickerShowDate, "forward") ?
                arrivalSearchDatePicker.findAvailableMonth(arrivalSearchDatePicker.datePickerShowDate, "forward").split('-') :
                false;
            if (date) {
              arrivalSearchDatePicker.datePickerShowDate.setMonth(date[0] - 1);
              arrivalSearchDatePicker.datePickerShowDate.setFullYear(date[1]);
              arrivalSearchDatePicker.monthAndYearTxt = [arrivalSearchDatePicker.months[date[0] -
                  1], ' ', arrivalSearchDatePicker.datePickerShowDate.getFullYear()].join('');
              arrivalSearchDatePicker.setup(arrivalSearchDatePicker.datePickerShowDate);
            }
            isFirstMonthDatesAvailable = true;
          }
          else {
            arrivalSearchDatePicker.renderAvailabilityMessage();
            isFirstMonthDatesAvailable = false;
          }
        }
        else {
          if (arrivalSearchDatePicker.dateSelectOption.length > 0) {
            arrivalSearchDatePicker.unsubscribeDateSelectOption();
            arrivalSearchDatePicker.dateSelectOption[0].setSelectedValue([arrivalSearchDatePicker.datePickerShowDate.getMonth() +
                1, '/', arrivalSearchDatePicker.datePickerShowDate.getFullYear()].join(''));
            arrivalSearchDatePicker.subscribeDateSelectOption();
          }
        }

        arrivalSearchDatePicker.skipMonth = true;
        if(arrivalSearchDatePicker.nextMonth <= arrivalSearchDatePicker.month){
        	if(arrivalSearchDatePicker.month != 11){
        		arrivalSearchDatePicker.datePickerNextMonthShowDate.setMonth(arrivalSearchDatePicker.month+1);
        	}else if(arrivalSearchDatePicker.month === 11){
    			arrivalSearchDatePicker.datePickerNextMonthShowDate.setMonth(0);
    			arrivalSearchDatePicker.datePickerNextMonthShowDate.setFullYear(arrivalSearchDatePicker.datePickerShowDate.getFullYear()+1);
        	}


        	arrivalSearchDatePicker.datePickerNextMonthShowDate.setFullYear(arrivalSearchDatePicker.datePickerNextMonthShowDate.getFullYear());
        	arrivalSearchDatePicker.monthAndYearTxtForNextMonth = [arrivalSearchDatePicker.months[arrivalSearchDatePicker.datePickerShowDate.getMonth()+1], ' ', arrivalSearchDatePicker.datePickerNextMonthShowDate.getFullYear()].join('');
        	arrivalSearchDatePicker.setupnextmonth(arrivalSearchDatePicker.datePickerNextMonthShowDate);
        }
        //Second Month
        if (!arrivalSearchDatePicker.validDatesTest(arrivalSearchDatePicker.datePickerNextMonthShowDate, storedDatesRepo)) {
            if (arrivalSearchDatePicker.skipNextMonth) {
              var nextMonthDate = arrivalSearchDatePicker.findAvailableNextMonth(arrivalSearchDatePicker.datePickerNextMonthShowDate, "forward") ?
                  arrivalSearchDatePicker.findAvailableNextMonth(arrivalSearchDatePicker.datePickerNextMonthShowDate, "forward").split('-') :
                  false;
              if (nextMonthDate && (Math.abs(arrivalSearchDatePicker.datePickerNextMonthShowDate.getMonth()-parseInt(nextMonthDate[0]))) <= 1) {
    	          arrivalSearchDatePicker.datePickerNextMonthShowDate.setMonth(nextMonthDate[0]);
    	          arrivalSearchDatePicker.datePickerNextMonthShowDate.setFullYear(nextMonthDate[1]);
    	          arrivalSearchDatePicker.monthAndYearTxtForNextMonth = [arrivalSearchDatePicker.months[nextMonthDate[0] - 1], ' ', arrivalSearchDatePicker.datePickerNextMonthShowDate.getFullYear()].join('');
    	          arrivalSearchDatePicker.setupnextmonth(arrivalSearchDatePicker.datePickerNextMonthShowDate);
    	        }
              isSecondMonthDatesAvailable = true;
            } else {
              arrivalSearchDatePicker.renderNextMonthAvailabilityMessage();
              isSecondMonthDatesAvailable = false;
            }
          }
          arrivalSearchDatePicker.skipNextMonth = true;

          //Render the Month Bar
          if(!isFirstMonthDatesAvailable && !isSecondMonthDatesAvailable ) {
          	arrivalSearchDatePicker.deleteDatePicker();
          	arrivalSearchDatePicker.deleteNextMonthDatePicker();
          	var months;
          	if(arrivalSearchDatePicker.seasonEndValue){
          		months = arrivalSearchDatePicker.datePickerShowDate.getMonth()+1;
          	}else{
          		months = arrivalSearchDatePicker.datePickerShowDate.getMonth();
          	}
            //Month not available message
          	arrivalSearchDatePicker.monthbarmessagestart = arrivalSearchDatePicker.searchMessaging.date.monthbarmessagestart;
          	arrivalSearchDatePicker.unAvailableMonthName = lang.trim(arrivalSearchDatePicker.cal_months_labels[months]) + " "+arrivalSearchDatePicker.datePickerShowDate.getFullYear();
          	arrivalSearchDatePicker.monthbarmessageend = arrivalSearchDatePicker.searchMessaging.date.monthbarmessageend;

            arrivalSearchDatePicker.templateview = "showArrivalMonthBar";
          }

      },


      renderAvailabilityMessage: function () {
          // summary:
          //		Renders message and links if entire month is unavailable
          var arrivalSearchDatePicker = this;
          var curMonth = dojo.clone(arrivalSearchDatePicker.datePickerShowDate);
          arrivalSearchDatePicker.monthAndYearTxt = [arrivalSearchDatePicker.months[arrivalSearchDatePicker.datePickerShowDate.getMonth()], ' ', arrivalSearchDatePicker.datePickerShowDate.getFullYear()].join('');

          var forwardDate = arrivalSearchDatePicker.findAvailableMonth(curMonth, "forward");
          var reverseDate = arrivalSearchDatePicker.findAvailableMonth(curMonth);

          // current month to render is first month in season range
          if (curMonth.valueOf() === arrivalSearchDatePicker.firstDate.valueOf()
              && curMonth.valueOf() !== arrivalSearchDatePicker.endDate.valueOf()
              && forwardDate) {
            arrivalSearchDatePicker.prevAvailableMonth = false;
            arrivalSearchDatePicker.prevAvailableLink = null;
            arrivalSearchDatePicker.nextAvailableMonth = true;
            arrivalSearchDatePicker.nextAvailableLink = forwardDate;
          }
          // current month to render is last month in season range
          else if (curMonth.valueOf() !== arrivalSearchDatePicker.firstDate.valueOf()
              && curMonth.valueOf() === arrivalSearchDatePicker.endDate.valueOf()
              && reverseDate) {
            arrivalSearchDatePicker.prevAvailableMonth = true;
            arrivalSearchDatePicker.prevAvailableLink = reverseDate;
            arrivalSearchDatePicker.nextAvailableMonth = false;
            arrivalSearchDatePicker.nextAvailableLink = null;
          }
          // current month is between first and last month in season range
          else {
            if (forwardDate) {
              arrivalSearchDatePicker.nextAvailableMonth = true;
              arrivalSearchDatePicker.nextAvailableLink = forwardDate;
            }
            if (reverseDate) {
              arrivalSearchDatePicker.prevAvailableMonth = true;
              arrivalSearchDatePicker.prevAvailableLink = reverseDate;
            }
          }

          arrivalSearchDatePicker.deleteDatePicker();
          arrivalSearchDatePicker.templateview = "noavailability";
        },

        renderNextMonthAvailabilityMessage: function () {
            // summary:
            //		Renders message and links if entire month is unavailable
            var arrivalSearchDatePicker = this;
            var curNextMonth = dojo.clone(arrivalSearchDatePicker.datePickerNextMonthShowDate);
            arrivalSearchDatePicker.monthAndYearTxtForNextMonth = [arrivalSearchDatePicker.months[arrivalSearchDatePicker.datePickerNextMonthShowDate.getMonth()], ' ', arrivalSearchDatePicker.datePickerNextMonthShowDate.getFullYear()].join('');

            var forwardDate = arrivalSearchDatePicker.findAvailableNextMonth(curNextMonth, "forward");
            var reverseDate = arrivalSearchDatePicker.findAvailableNextMonth(curNextMonth);

            // current month to render is first month in season range
            if (curNextMonth.valueOf() === arrivalSearchDatePicker.nextMonthFirstDate.valueOf()
                && curNextMonth.valueOf() !== arrivalSearchDatePicker.endDate.valueOf()
                && forwardDate) {
              arrivalSearchDatePicker.prevAvailableMonth = false;
              arrivalSearchDatePicker.prevAvailableLink = null;
              arrivalSearchDatePicker.nextAvailableMonth = true;
              arrivalSearchDatePicker.nextAvailableLink = forwardDate;
            }
            // current month to render is last month in season range
            else if (curNextMonth.valueOf() !== arrivalSearchDatePicker.nextMonthFirstDate.valueOf()
                && curNextMonth.valueOf() === arrivalSearchDatePicker.endDate.valueOf()
                && reverseDate) {
              arrivalSearchDatePicker.prevAvailableMonth = true;
              arrivalSearchDatePicker.prevAvailableLink = reverseDate;
              arrivalSearchDatePicker.nextAvailableMonth = false;
              arrivalSearchDatePicker.nextAvailableLink = null;
            }
            // current month is between first and last month in season range
            else {
              if (forwardDate) {
                arrivalSearchDatePicker.nextAvailableMonth = true;
                arrivalSearchDatePicker.nextAvailableLink = forwardDate;
              }
              if (reverseDate) {
                arrivalSearchDatePicker.prevAvailableMonth = true;
                arrivalSearchDatePicker.prevAvailableLink = reverseDate;
              }
            }

            arrivalSearchDatePicker.deleteNextMonthDatePicker();
            arrivalSearchDatePicker.templateview = "noavailabilityinsecondmonth";

          },

          attachAvailabilityListeners: function () {
              // summary:
              //    attach event listeners for prev/next available month links
              var arrivalSearchDatePicker = this;
              on(arrivalSearchDatePicker.arrivalDatePickerDOM,on.selector(arrivalSearchDatePicker.monthBarOpenOnFirstMonthLinkSelect, "click"), function (event) {
               	  if(dojo.query(".mnoth-bar")[0]) domConstruct.destroy(dojo.query(".mnoth-bar")[0]);
                      arrivalSearchDatePicker.deleteNextMonthDatePicker();
                  	  arrivalSearchDatePicker.deleteDateSelection();
                  	  arrivalSearchDatePicker.templateview = "showArrivalMonthBar";
                  	  //Month not available message
                  	  if(arrivalSearchDatePicker.datePickerShowDate.getMonth() == 11){
                  		arrivalSearchDatePicker.datePickerShowDate.setMonth(0);
                  	  }
                      arrivalSearchDatePicker.monthbarmessagestart = arrivalSearchDatePicker.searchMessaging.date.monthbarmessagestart;
                      arrivalSearchDatePicker.unAvailableMonthName = lang.trim(arrivalSearchDatePicker.cal_months_labels[arrivalSearchDatePicker.datePickerShowDate.getMonth()]) +" "+ arrivalSearchDatePicker.datePickerShowDate.getFullYear();
                      arrivalSearchDatePicker.monthbarmessageend = arrivalSearchDatePicker.searchMessaging.date.monthbarmessageend;

                      arrivalSearchDatePicker.renderDatePicker();
                      arrivalSearchDatePicker.hideWidget(arrivalSearchDatePicker.dateSelectOption[0].listElement)
                      arrivalSearchDatePicker.open();
               });
            },

            attachNextMonthAvailabilityListeners: function () {
              // summary:
              //    attach event listeners for prev/next available month links
              var arrivalSearchDatePicker = this,datePickerMonth;
              on(arrivalSearchDatePicker.arrivalDatePickerDOM,on.selector(arrivalSearchDatePicker.monthBarOpenOnSecondMonthLinkSelect, "click"), function (event) {
           	  if(dojo.query(".mnoth-bar")[0]) domConstruct.destroy(dojo.query(".mnoth-bar")[0]);
                  arrivalSearchDatePicker.deleteDatePicker();
              	  arrivalSearchDatePicker.deleteDateSelection();
              	  arrivalSearchDatePicker.templateview = "showArrivalMonthBar";
              	  //Month not available message
              	  if(arrivalSearchDatePicker.datePickerShowDate.getMonth() == 11){
              		datePickerMonth = 0;
              	  }  else {
              		datePickerMonth = arrivalSearchDatePicker.datePickerShowDate.getMonth()+1;
    	       	  }
                  arrivalSearchDatePicker.monthbarmessagestart = arrivalSearchDatePicker.searchMessaging.date.monthbarmessagestart;
                  arrivalSearchDatePicker.unAvailableMonthName = lang.trim(arrivalSearchDatePicker.cal_months_labels[datePickerMonth]) +" "+ arrivalSearchDatePicker.datePickerShowDate.getFullYear();
                  arrivalSearchDatePicker.monthbarmessageend = arrivalSearchDatePicker.searchMessaging.date.monthbarmessageend;

                  arrivalSearchDatePicker.renderDatePicker();
                  arrivalSearchDatePicker.hideWidget(arrivalSearchDatePicker.dateSelectOption[0].listElement)
                  arrivalSearchDatePicker.open();
              });
            },

            findAvailableMonth: function (date, direction) {
                // summary:
                //    find next/previous month with available dates
                var arrivalSearchDatePicker = this,selectedDepDate;
                var storedDatesRepo = arrivalSearchDatePicker.getStoredDates(false);


                direction = direction || "reverse";
                if (direction == "forward") {
                  date = dojo.clone(date);
                  if(storedDatesRepo.length < 1){
                  		selectedDepDate = new Date(arrivalSearchDatePicker.searchPanelModel.date);
                  			date  = new Date(selectedDepDate);
                  			return [date.getMonth() + 1, '-', date.getFullYear()].join('');

                  }

                  date.setMonth(date.getMonth() + 1);
                  while (date.valueOf() <= arrivalSearchDatePicker.endDate.valueOf()) {
                    if (arrivalSearchDatePicker.validDatesTest(date, storedDatesRepo)) {
                      return [date.getMonth() + 1, '-', date.getFullYear()].join('');
                    }
                    date.setMonth(date.getMonth() + 1);
                  }
                } else {
                  date = dojo.clone(date);
                  date.setMonth(date.getMonth() - 1);
                  while (date.valueOf() >= arrivalSearchDatePicker.firstDate.valueOf()) {
                    if (arrivalSearchDatePicker.validDatesTest(date, storedDatesRepo)) {
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
                var arrivalSearchDatePicker = this;
                var storedDatesRepo = arrivalSearchDatePicker.getStoredDates(false);
                direction = direction || "reverse";
                if (direction == "forward") {
                  date = dojo.clone(date);
                  date.setMonth(date.getMonth() + 1);
                  while (date.valueOf() <= arrivalSearchDatePicker.endDate.valueOf()) {
                    if (arrivalSearchDatePicker.validDatesTest(date, storedDatesRepo)) {
                      return [date.getMonth() + 1, '-', date.getFullYear()].join('');
                    }
                    date.setMonth(date.getMonth() + 1);
                  }
                } else {
                  date = dojo.clone(date);
                  date.setMonth(date.getMonth() - 1);
                  while (date.valueOf() >= arrivalSearchDatePicker.nextMonthFirstDate.valueOf()) {
                    if (arrivalSearchDatePicker.validDatesTest(date, storedDatesRepo)) {
                      return [date.getMonth() + 1, '-', date.getFullYear()].join('');
                    }
                    date.setMonth(date.getMonth() - 1);
                  }
                }
                return false;
              },

    validDatesTest: function (curDate, storedDates) {
      // summary:
      //		Test for valid dates
      var arrivalSearchDatePicker = this;
      for (var i = 0; i < storedDates.length; i++) {
        var string = storedDates[i].split('-');
        if (parseInt(string[1], 10) === curDate.getMonth() + 1 &&
            parseInt(string[2], 10) === curDate.getFullYear()) {
          return true;
        }
      }
      return false;
    },

    secondMonthDateValidate : function(nextMonthDate, storedDates){
    	  var arrivalSearchDatePicker = this;
    	  for (var i = 0; i < storedDates.length; i++) {
    	        var string = storedDates[i].split('-');
    	        if (parseInt(string[1], 10) === nextMonthDate.getMonth() + 1 && parseInt(string[2], 10) === nextMonthDate.getFullYear()) {
    	        	return true;
    	        }
    	      }
    	    return false;
      },



    onAfterTmplRender: function () {
        // summary:
        //		Extends default method to call eligible dates method


    	var arrivalSearchDatePicker = this,nextMonth,rightArrow,leftArrow,fb;
        arrivalSearchDatePicker.inherited(arguments);
        var StoredDates = arrivalSearchDatePicker.getStoredDates(false);

        if (arrivalSearchDatePicker.templateview === "noavailability") {
        		leftArrow = dojo.query(".prev", arrivalSearchDatePicker.arrivalDatePickerDOM)[0];
        		dojo.addClass(leftArrow, "disabled");
        		arrivalSearchDatePicker.secondMonthEligibleDates(arrivalSearchDatePicker.getStoredDates(false));
        		arrivalSearchDatePicker.attachAvailabilityListeners();
        }
        else if (arrivalSearchDatePicker.templateview === "noavailabilityinsecondmonth" ) {
        		rightArrow = dojo.query(".next", arrivalSearchDatePicker.arrivalDatePickerDOM)[0];
        		dojo.addClass(rightArrow, "disabled");
        		arrivalSearchDatePicker.firstMonthEligibleDates(arrivalSearchDatePicker.getStoredDates(false));
        		arrivalSearchDatePicker.attachNextMonthAvailabilityListeners();
        }
        else if (arrivalSearchDatePicker.templateview === "showArrivalMonthBar") {
      	  		arrivalSearchDatePicker.deleteDateSelection();
      	  		arrivalSearchDatePicker.getStoredDates(true);
      	  		dojo.query("div.monthSelector").empty();
      	  		if(dojo.global.arrivalAvailableMonths == undefined){
      	  			arrivalSearchDatePicker.populateMonths();
      	  		}
      	  		fb = new arrivalFlightSearchMonthBar();
      	  		fb.generateMonths(arrivalSearchDatePicker.seasonStartDate,arrivalSearchDatePicker.seasonLength);
      	  		dojo.place(dojo.query(".monthSelector")[0], dojo.query(".mnoth-bar")[0],"last");
        }else {
        	if(arrivalSearchDatePicker.datePickerShowDate.valueOf() === new Date(arrivalSearchDatePicker.searchPanelModel.date).valueOf()){
        		leftArrow = dojo.query(".prev", arrivalSearchDatePicker.arrivalDatePickerDOM)[0];
              	dojo.addClass(leftArrow, "disabled");
        	}
        	arrivalSearchDatePicker.eligibleDates(arrivalSearchDatePicker.getStoredDates(false));

        }
        if (arrivalSearchDatePicker.endDate) {
        	arrivalSearchDatePicker.disableUnavailableDates(arrivalSearchDatePicker.endDate, false);
        }


		if(StoredDates.length < 1 && arrivalSearchDatePicker.templateview !== "showArrivalMonthBar" ){
			arrivalSearchDatePicker.deleteDatePicker();
			arrivalSearchDatePicker.deleteNextMonthDatePicker();
			arrivalSearchDatePicker.deleteDateSelection();
  	  		arrivalSearchDatePicker.getStoredDates(true);

  	  		dojo.query(".noResultsMonthCnt")[0].innerHTML ="";
  	  		dojo.query(".noResultsNextMonthCnt")[0].innerHTML ="";
  	  		 //Month not available message
  	  		arrivalSearchDatePicker.unAvailableMonthName = lang.trim(arrivalSearchDatePicker.cal_months_labels[arrivalSearchDatePicker.datePickerShowDate.getMonth()]) +" "+ arrivalSearchDatePicker.datePickerShowDate.getFullYear();
  	  		 var monthbar = dojo.create("div", {
				"class":"mnoth-bar",
				innerHTML : "<p class='month-not-available-message start'>"+arrivalSearchDatePicker.searchMessaging.date.monthbarmessagestart+"</p><p class='month-not-available-message middle'>"+arrivalSearchDatePicker.unAvailableMonthName + "</p><p class='month-not-available-message end'>"+arrivalSearchDatePicker.searchMessaging.date.monthbarmessageend+"</p>"
			});

  	  		 domConstruct.place(monthbar,document.body,"last");
          	dojo.place(dojo.query(".mnoth-bar")[0], dojo.query(".noResultsMonthCnt")[0],"last");

  	  		fb = new arrivalFlightSearchMonthBar();
  	  		fb.generateMonths(arrivalSearchDatePicker.seasonStartDate,arrivalSearchDatePicker.seasonLength);
  	  		dojo.place(dojo.query(".monthSelector")[0], dojo.query(".mnoth-bar")[0],"last");
  	  		//dojo.parser.parse(arrivalSearchDatePicker.arrivalDatePickerDOM);
		}else if((arrivalSearchDatePicker.templateview === "arrivaldatepicker" || arrivalSearchDatePicker.templateview === "arrivaldatepickercells") && !arrivalSearchDatePicker.secondMonthDateValidate(arrivalSearchDatePicker.datePickerNextMonthShowDate, StoredDates)){

			dojo.query(".noResultsMonthCnt")[0].innerHTML ="";
			dojo.query(".noResultsNextMonthCnt")[0].innerHTML ="";
			arrivalSearchDatePicker.deleteNextMonthDatePicker();
			 if(arrivalSearchDatePicker.datePickerShowDate.getMonth() == 11){
				 nextMonth = 0;
				 arrivalSearchDatePicker.datePickerNextMonthShowDate.setFullYear(arrivalSearchDatePicker.datePickerShowDate.getFullYear()+1);
           	  }  else {
           		nextMonth = arrivalSearchDatePicker.datePickerShowDate.getMonth()+1;
 	       	  }


			/*if(arrivalSearchDatePicker.datePickerShowDate.getMonth() < 11){
				nextMonth = arrivalSearchDatePicker.datePickerShowDate.getMonth()+1;
			} else {
				nextMonth = arrivalSearchDatePicker.datePickerShowDate.getMonth());
			}*/
			arrivalSearchDatePicker.datePickerNextMonthShowDate.setMonth(nextMonth);
			var monthAndYearTxtForNextMonth = [arrivalSearchDatePicker.months[arrivalSearchDatePicker.datePickerNextMonthShowDate.getMonth()], ' ', arrivalSearchDatePicker.datePickerNextMonthShowDate.getFullYear()].join('');
			var secondMonthDiv = dojo.create("div", {
					"class":"second-month-available-message second-cal-msg second-month-available",
					innerHTML : "<p>"+arrivalSearchDatePicker.searchMessaging.date.noAvailability +" "+ monthAndYearTxtForNextMonth+"</p><ul class=available-date-links><li class=left><a href=javascript:void(0); class=second-month-monthbar-link>"+arrivalSearchDatePicker.searchMessaging.date.availability+"</a></li></ul></div>"
				});
      	  	dojo.place(secondMonthDiv, dojo.query(".noResultsNextMonthCnt")[0],"last");
      	  	dojo.parser.parse(dojo.query(".noResultsNextMonthCnt")[0]);
      	  	rightArrow = dojo.query(".next", arrivalSearchDatePicker.arrivalDatePickerDOM)[0];
	        dojo.addClass(rightArrow, "disabled");
	        arrivalSearchDatePicker.attachNextMonthAvailabilityListeners();
      }else{
    	  	dojo.query(".noResultsMonthCnt")[0].innerHTML ="";
    		dojo.query(".noResultsNextMonthCnt")[0].innerHTML ="";
      }

        	arrivalSearchDatePicker.tagElement(arrivalSearchDatePicker.arrivalDatePickerDOM, "OverallReturning");

        	arrivalSearchDatePicker.attachToolTipEvents();

        	arrivalSearchDatePicker.setDateSelectOption();

	    	nextMonth =new Date(arrivalSearchDatePicker.datePickerNextMonthShowDate);
			nextMonth.setMonth(nextMonth.getMonth()+1);
			if(nextMonth.valueOf() > arrivalSearchDatePicker.seasonEndDate.valueOf()){
				rightArrow = dojo.query(".next", arrivalSearchDatePicker.arrivalDatePickerDOM)[0];
		        dojo.addClass(rightArrow, "disabled");
			}

      },
      watchCallback:function(){
    	  var arrivalSearchDatePicker = this;
    	  arrivalSearchDatePicker.watch('year',function(name, oldValue, value){
    		  console.log(name, oldValue, value);
    	  })
      },
      deleteDateSelection: function () {
          var arrivalSearchDatePicker = this;
          dojo.style(dojo.query(".arrivaldate-selector", arrivalSearchDatePicker.arrivalDatePickerDOM)[0], {
          	"display":"none"
          });

      },

      eligibleDates: function (dateStoreData) {
          // summary:
          //   Disables non-eligible dates
          //   params: dateStoreData (Array)
          var arrivalSearchDatePicker = this,selectedDepDate;
          var totalDays = arrivalSearchDatePicker.totalDays[arrivalSearchDatePicker.datePickerShowDate.getMonth()];
          var month = dojo.clone(arrivalSearchDatePicker.datePickerShowDate);

          var secondMonthTotalDays = arrivalSearchDatePicker.nextMonthTotalDays[arrivalSearchDatePicker.datePickerNextMonthShowDate.getMonth()];
          var secondMonth = dojo.clone(arrivalSearchDatePicker.datePickerNextMonthShowDate);

          var now = new Date();
          var today = new Date(now.getFullYear(), now.getMonth(), now.getDate());


          selectedDepDate = new Date(arrivalSearchDatePicker.searchPanelModel.date);
          // First Month
          for (var i = month.getDate(); i <= totalDays; i++) {
            if (dojo.indexOf(dateStoreData, arrivalSearchDatePicker.formatDate(month)) === -1) {
              dojo.addClass(arrivalSearchDatePicker.daycells[month.getDate() - 1], "unavailable");
              dojo.addClass(arrivalSearchDatePicker.daycells[month.getDate() - 1], "disabled");
              // adding past dates class just in case (should not be necessary as unavailable won't be sent)
              if (today.getMonth() === month.getMonth() && today.getFullYear() === month.getFullYear() &&
                  i <= today.getDate()) {
                dojo.addClass(arrivalSearchDatePicker.daycells[month.getDate() - 1], "past");
              }
            }

          //also need to disable dates if it is exceeding the days of 93 from the selected date in departure calendar
            month.setHours(0);// setting time to 00:00 to get accurate days
            month.setMinutes(0);
            if(dojo.date.difference(selectedDepDate, month,"day") > 93) {
            	dojo.addClass(arrivalSearchDatePicker.daycells[month.getDate() - 1], "unavailable");
                dojo.addClass(arrivalSearchDatePicker.daycells[month.getDate() - 1], "disabled");
            }
            month.setDate(month.getDate() + 1);
          }

          // Second Month
          for (var i = secondMonth.getDate(); i <= secondMonthTotalDays; i++) {
              if (dojo.indexOf(dateStoreData, arrivalSearchDatePicker.formatDate(secondMonth)) === -1) {
                dojo.addClass(arrivalSearchDatePicker.nextMonthdaycells[secondMonth.getDate() - 1], "unavailable");
                dojo.addClass(arrivalSearchDatePicker.nextMonthdaycells[secondMonth.getDate() - 1], "disabled");
              }

            //also need to disable dates if it is exceeding the days of 93 from the selected date in departure calendar
              if(dojo.date.difference(selectedDepDate, secondMonth,"day") > 93) {
            	  dojo.addClass(arrivalSearchDatePicker.nextMonthdaycells[secondMonth.getDate() - 1], "unavailable");
                  dojo.addClass(arrivalSearchDatePicker.nextMonthdaycells[secondMonth.getDate() - 1], "disabled");
              }
              secondMonth.setDate(secondMonth.getDate() + 1);

            }
        },

        firstMonthEligibleDates: function (dateStoreData) {
            // summary:
            //   Disables non-eligible first month dates
            //   params: dateStoreData (Array)
            var arrivalSearchDatePicker = this,selectedDepDate;

            var totalDays = arrivalSearchDatePicker.totalDays[arrivalSearchDatePicker.datePickerShowDate.getMonth()];
            var month = dojo.clone(arrivalSearchDatePicker.datePickerShowDate);

            var now = new Date();
            var today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
            selectedDepDate = new Date(arrivalSearchDatePicker.searchPanelModel.date);
            for (var i = month.getDate(); i <= totalDays; i++) {
              if (dojo.indexOf(dateStoreData, arrivalSearchDatePicker.formatDate(month)) === -1) {
                dojo.addClass(arrivalSearchDatePicker.daycells[month.getDate() - 1], "unavailable");
                dojo.addClass(arrivalSearchDatePicker.daycells[month.getDate() - 1], "disabled");
                // adding past dates class just in case (should not be necessary as unavailable won't be sent)
                if (today.getMonth() === month.getMonth() && today.getFullYear() === month.getFullYear() &&
                    i <= today.getDate()) {
                  dojo.addClass(arrivalSearchDatePicker.daycells[month.getDate() - 1], "past");
                }
              }
              month.setDate(month.getDate() + 1);
            //also need to disable dates if it is exceeding the days of 93 from the selected date in departure calendar
	          if(dojo.date.difference(selectedDepDate, month,"day") > 93) {
	          	dojo.addClass(arrivalSearchDatePicker.daycells[month.getDate() - 1], "unavailable");
	              dojo.addClass(arrivalSearchDatePicker.daycells[month.getDate() - 1], "disabled");
	          }
            }
          },

          secondMonthEligibleDates: function (dateStoreData) {
              // summary:
              //   Disables non-eligible second month dates
              //   params: dateStoreData (Array)
              var arrivalSearchDatePicker = this,selectedDepDate;

              var secondMonthTotalDays = arrivalSearchDatePicker.nextMonthTotalDays[arrivalSearchDatePicker.datePickerNextMonthShowDate.getMonth()];
              var secondMonth = dojo.clone(arrivalSearchDatePicker.datePickerNextMonthShowDate);
              selectedDepDate = new Date(arrivalSearchDatePicker.searchPanelModel.date);
              for (var i = secondMonth.getDate(); i <= secondMonthTotalDays; i++) {
                  if (dojo.indexOf(dateStoreData, arrivalSearchDatePicker.formatDate(secondMonth)) === -1) {
                    dojo.addClass(arrivalSearchDatePicker.nextMonthdaycells[secondMonth.getDate() - 1], "unavailable");
                    dojo.addClass(arrivalSearchDatePicker.nextMonthdaycells[secondMonth.getDate() - 1], "disabled");
                  }
                  secondMonth.setDate(secondMonth.getDate() + 1);
                //also need to disable dates if it is exceeding the days of 93 from the selected date in departure calendar
                  if(dojo.date.difference(selectedDepDate, secondMonth,"day") > 93) {
                	  dojo.addClass(arrivalSearchDatePicker.nextMonthdaycells[secondMonth.getDate() - 1], "unavailable");
                      dojo.addClass(arrivalSearchDatePicker.nextMonthdaycells[secondMonth.getDate() - 1], "disabled");
                  }
                }
            },

    formatDate: function (date) {
      // summary:
      //		Formats date object to match JSON format
      var arrivalSearchDatePicker = this;
      return dojo.date.locale.format(date, {
        selector: "date",
        datePattern: "dd-MM-yyyy"
      });
    },

    setFieldValue: function (newdate) {
      // summary:
      // 		Extend default method and write value to model
      var arrivalSearchDatePicker = this;
      arrivalSearchDatePicker.inherited(arguments);
      arrivalSearchDatePicker.searchPanelModel.set("returnDate", arrivalSearchDatePicker.getFieldValue());
    },

    setDateSelectOption: function () {
      // summary:
      // 		override default datepicker method, treat month/year as one item
      var arrivalSearchDatePicker = this;
      var monthAndYear = [(arrivalSearchDatePicker.datePickerShowDate.getMonth() +
          1), '/', arrivalSearchDatePicker.datePickerShowDate.getFullYear()].join('');
      if (arrivalSearchDatePicker.dateSelectOption[0].getSelectedData().value !== monthAndYear) {
        arrivalSearchDatePicker.skipMonth = false;
        arrivalSearchDatePicker.skipNextMonth = false;
        arrivalSearchDatePicker.dateSelectOption[0].setSelectedValue(monthAndYear);
      }
    },

    getStoredDates: function (isMonthBarEnabled) {
        // summary:
        // 		retrieves dates array from store based on current route
        //    returns array of eligible dates
        var arrivalSearchDatePicker = this;
        var searchquery = arrivalSearchDatePicker.searchPanelModel.generateQueryObject();
        var destinations = _.map(dojo.clone(searchquery.to), function(toItem){
          return splitDestinationQuery(toItem);
        });
        var id = [destinations, searchquery.from].join('');
        id = (id === '') ? "all" : id;

        /*Month Bar Functionality*/
        if(isMonthBarEnabled && arrivalSearchDatePicker.returnDateStore.data.length > 0) {
      		  arrivalSearchDatePicker.tempAvailableMonths = arrivalSearchDatePicker.returnDateStore.get(id).dates;
          	  arrivalSearchDatePicker.populateMonths();
        }
        /*Month Bar Functionality*/
        return arrivalSearchDatePicker.returnDateStore.get(id) ? arrivalSearchDatePicker.returnDateStore.get(id).dates : [];
      },

      /*Month Bar Functionality*/
      populateMonths: function(){
          var arrivalSearchDatePicker = this;
      	arrivalSearchDatePicker.finalAvailableMonths = [];
  			for(var i = 0;i < arrivalSearchDatePicker.seasonLength; i++){
  			   var monthAndYear = arrivalSearchDatePicker.getFullMonths(i);
  			   var monandyrinDigit = arrivalSearchDatePicker.getMonthSlicedIndex(i);
  			   var arr = monandyrinDigit.split("@");
  			   var obj = {available: false, month: monthAndYear};
  			   var tmpX = true;
  			   	_.each(arrivalSearchDatePicker.tempAvailableMonths, function(elm){
  			   		if(tmpX){
  				   		if(arr[1] === elm.substring(3,5) && arr[0] === elm.substring(6,10)){
  				   			obj.available = true;
  				   			tmpX = false;
  				   		}
  			   		}
  			   	});
  			   	obj.mon = arr[1]; obj.year = arr[0];
  			   	arrivalSearchDatePicker.finalAvailableMonths.push(obj);
  			}
  			dojo.global.arrivalAvailableMonths = arrivalSearchDatePicker.finalAvailableMonths;
  	  },

     getMonthSlicedIndex:function(i){
  		var arrivalSearchDatePicker = this;
  		var dt = new Date();
  		dt.setDate(1);
  		var mons = (dt).setMonth(dt.getMonth()+i);
  		var mon = lang.trim(arrivalSearchDatePicker.cal_months_labels[arrivalSearchDatePicker.cal_next_date(mons).getMonth()].substring(0,3).toUpperCase());
  		var fullMonth = lang.trim(arrivalSearchDatePicker.cal_months_labels[arrivalSearchDatePicker.cal_next_date(mons).getMonth()].toUpperCase());
  		var monInIndex = Number(new Date(mons).getMonth()) + 1;
  		if(monInIndex < 10) monInIndex = "0" + monInIndex;
  		return  (new Date(mons).getFullYear() + "@" +  monInIndex);
  	},

  	getFullMonths:function(i){
  		var arrivalSearchDatePicker = this;
  		var dt = new Date();
  		dt.setDate(1);
  		var mons = (dt).setMonth(dt.getMonth()+i);
  		var mon = lang.trim(arrivalSearchDatePicker.cal_months_labels[arrivalSearchDatePicker.cal_next_date(mons).getMonth()].substring(0,3).toUpperCase());
  		var fullMonth = lang.trim(arrivalSearchDatePicker.cal_months_labels[arrivalSearchDatePicker.cal_next_date(mons).getMonth()]);
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
    },
    hilightFirstMonthCalendar: function(){
    	 var selectedDateTd;
		 if(dojo.query(".first-month td.selected-from").length > 0){
			 selectedDateTd = dojo.query(".first-month td.selected-from")[0];
		 } else {
			 selectedDateTd = dojo.query(".first-month tr td:first-child")[0];
		 }
		 if(typeof selectedDateTd !== "undefined"){
	    	dojo.query(selectedDateTd).nextAll().some(function(cellItem){
	    		if(dojo.hasClass(cellItem,"selected")) return false;
				 if(!dojo.hasClass(cellItem,"disabled"))
				 dojo.query(cellItem).children("a").toggleClass("cellHover active");
				 else
				 dojo.query(cellItem).children("a").toggleClass("cellHover");
			 });
			 dojo.query(selectedDateTd).parent().nextAll("tr").children("td.arrival-cal-cell").some(function(cellItem){
				 if(dojo.hasClass(cellItem,"selected")) return false;
				 if(!dojo.hasClass(cellItem,"disabled"))
				 dojo.query(cellItem).children("a").toggleClass("cellHover active");
				 else
				 dojo.query(cellItem).children("a").toggleClass("cellHover");
			 });
		 }
    },


    //Attaching tooltip events here onmouseout and onmouseover
    attachToolTipEvents: function(){
    	 var arrivalSearchDatePicker = this;

    	 dojo.query("td.arrival-cal-cell", arrivalSearchDatePicker.arrivalDatePickerDOM).connect("onmouseover",function(evt){
    		 dojo.stopEvent(evt);
    		 if(evt.target.tagName == "A"){
    			 var tdObj = dojo.query(evt.target).closest("td")[0];
    			 if(dojo.hasClass(tdObj, "disabled")){
    				 return;

    			 }
     			if(evt.target.lastElementChild != null){
         			dojo.destroy(evt.target.lastElementChild);
     			}

    			 var actualMonth;
        		 if(dojo.hasClass(dojo.query(evt.target).closest("table")[0],"first-month")) {
        			 actualMonthAndYear = arrivalSearchDatePicker.monthTxt + " " + arrivalSearchDatePicker.datePickerShowDate.getFullYear();
        		 } else {
        			 actualMonthAndYear = arrivalSearchDatePicker.monthAndYearTxtForNextMonth;
        		 }
    			 var actualDate = new Date(evt.target.innerHTML + " " + actualMonthAndYear),
    			 	whenDate = new Date(arrivalSearchDatePicker.searchPanelModel.date),
    			 	diff = dojo.date.difference(whenDate,actualDate,"day"),
    			 	numofDays = diff == 1 ?  diff + " day" : diff+ " days";

	    		 new tooltips({
	    			 refId : evt.target,
	    			 text: numofDays,
	    			 floatWhere : 'position-top-center',
	    			 className: "cal",
	    			 setPosOffset: function(){
	    				  this.posOffset = {top: -46, left: 0};
	    			 }
	    		 }).placeAt(evt.target).open();

	    		 //highlight calendar cells from selected date to hovered date
	    		 dojo.query(".cellHover").removeClass("cellHover");
	    		 if(dojo.hasClass(dojo.query(tdObj).parents("table")[0],"second-month")){

	    			 dojo.query(tdObj).prevAll().some(function(cellItem){
	    				 if(dojo.hasClass(cellItem,"selected-from")) return false;
	    				 if(!dojo.hasClass(cellItem,"disabled"))
	    					 dojo.query(cellItem).children("a").toggleClass("cellHover active");
	    					 else
	    						 dojo.query(cellItem).children("a").toggleClass("cellHover");
	    			 });
	    			 dojo.query(tdObj.parentNode).prevAll("tr").children("td.arrival-cal-cell").some(function(cellItem){
	    				 if(dojo.hasClass(cellItem,"selected-from")) return false;
	    				 if(!dojo.hasClass(cellItem,"disabled"))
	    					 dojo.query(cellItem).children("a").toggleClass("cellHover active");
	    					 else
    						 dojo.query(cellItem).children("a").toggleClass("cellHover");

	    			 });
	    			 if(dojo.query("td.selected-from").length > 0){
	    				 dojo.query("td.selected-from").prevAll().children("a").removeClass("cellHover active");
	    				 dojo.query("td.selected-from").parents('tr').prevAll("tr").children("td.arrival-cal-cell").children("a").removeClass("cellHover active");
	    			 }
	    			 arrivalSearchDatePicker.hilightFirstMonthCalendar();
	    		 } else {
	    				dojo.query(tdObj).prevAll().some(function(cellItem){
	    					if(dojo.hasClass(cellItem,"selected-from")) return false;
	    					if(dojo.hasClass(cellItem,"selected")) return false;
	    					if(!dojo.hasClass(cellItem,"disabled"))
		    					 dojo.query(cellItem).children("a").toggleClass("cellHover active");
		    					 else
	    						 dojo.query(cellItem).children("a").toggleClass("cellHover");
		    			 });
		    			 dojo.query(tdObj.parentNode).prevAll("tr").children("td.arrival-cal-cell").some(function(cellItem){
		    				 if(dojo.hasClass(cellItem,"selected-from"))  return false;
		    				 if(dojo.hasClass(cellItem,"selected")) return false;
		    				 if(!dojo.hasClass(cellItem,"disabled"))
		    					 dojo.query(cellItem).children("a").toggleClass("cellHover active");
		    					 else
	    						 dojo.query(cellItem).children("a").toggleClass("cellHover");
		    			 });
		    			 if(dojo.query("td.selected-from").length > 0){
		    				 dojo.query("td.selected-from").prevAll().children("a").removeClass("cellHover active");
		    				 dojo.query("td.selected-from").parents('tr').prevAll("tr").children("td.arrival-cal-cell").children("a").removeClass("cellHover active");
		    			 }

	    		 }
    	 	}
    	 });
    	 dojo.query("td.arrival-cal-cell", arrivalSearchDatePicker.arrivalDatePickerDOM).connect("onmouseout",function(evt){
    		 dojo.stopEvent(evt);
    		 if(evt.target.tagName == "A"){
				 if(dojo.hasClass(dojo.query(evt.target).closest("td")[0], "disabled")){
					 return;
				 }
				if(dojo.isIE == 8){
					dojo.query(".cal.loaded").forEach(dojo.destroy);
				}
	    		dojo.query(".tooltip.cal").forEach(dojo.destroy);
	    		//unhighlight calendar cells from selected date to hovered date
	    		dojo.query(".cellHover").removeClass("cellHover active");
    		 }
    	 });
    }
  });

  return tui.searchPanel.view.flights.ArrivalSearchDatePicker;
});
