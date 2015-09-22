define("tui/widget/datepicker/ArrivalDatePicker", [
  "dojo",
  "dojo/text!tui/widget/datepicker/templates/arrivaldatepicker.html",
  "dijit/registry",
  "dojo/query",
  "dojo/on",
  "dojo/NodeList-traverse",
  "dojo/date/locale",
  "tui/widget/mixins/FloatPosition",
  "tui/widget/_TuiBaseWidget",
  "tui/widget/datepicker/ReturnDateSelectOption",
  "tui/widget/mixins/Templatable"], function (dojo, arrivalDatePickerTmpl, registry, query, on) {

  dojo.declare("tui.widget.datepicker.ArrivalDatePicker", [tui.widget._TuiBaseWidget, tui.widget.mixins.FloatPosition, tui.widget.mixins.Templatable], {


    // summary:
    //		Class which defines the behaviour for datePicker widget.
    //
    // description:
    //		DatePicker extends tui.widget._TuiBaseWidget, and defines the base behaviour for datepickers.
    //		This widget allows dates to be selected via a calendar popup.

    // ---------------------------------------------------------------- datePicker properties

    // months: Array
    //		Months of the year, for datepicker.
    months: ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],

    // days: Array
    //		Days of the week text, for datepicker.
    days: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],

    // totalDays: Number
    //		Total days for month.
    totalDays: null,

    // dates: Array
    // 		Full dates in a month.
    dates: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31],

    // arrivalDatePickerDOM: DOMNode
    // 		Reference to DOM element for date picker popup.
    arrivalDatePickerDOM: null,

    // day: Number
    //		Number for the day of the week for current datepicker date.
    //		i.e Sunday is 0, Monday is 1, and so on.
    day: null,

    // monthTxt: String
    //		Current datepicker month.
    //		i.e January or February
    monthTxt: null,

    // monthsAndYears: Array
    // Contains a list of combined months and years available for datepicker.

    monthsAndYears: null,

    // monthAndYearTxt: String
    //		Current datepicker month and year.
    //		i.e 12 January or 14 February

    monthAndYearTxt: null,

    // month: Number
    //		Current datepicker month, as a number
    //		i.e January is 0, February is 1, and so on.
    month: 0,

    // year: Number
    //		Current datepicker selected year.
    year: null,

 // year: Number
    //		Current datepicker next year,Ex: if current month is Dec 2014, then next year is Jan 2015.
    nextYear : null,
    // years: Array
    //		Contains a list of all years available for datepicker.
    years: null,

    // yearRange: Number
    //		Sets the year range for datepicker.
    yearRange: 10,

    // now: Date
    //	  Today date.
    now: null,

    // selectedDate: Date
    //	 	Selected datepicker date.
    selectedDate: null,

    // defaultDaysAhead: Number
    //	 	Days ahead of current date to show calendar on
    // TODO: set to 1 on BAU?
    defaultDaysAhead: 0,

    datePickerShowDate: null,

    // datePattern: String
    //	    Date display format pattern, on date selection.
    datePattern: "d MMMM yyyy",

    // closeOnSelection: Boolean
    //		Boolean for closing picker when a date is selected.
    closeOnSelection: true,

    // daycells: Array
    //		Contains dom reference to datepicker date cells.
    daycells: null,

    // datePickerConnects: Array
    // 		Contains datePicker event handlers.
    datePickerConnects: null,

    // datePickerPlaceholder: String
    // 		Placeholder text from input field attached to datepicker.
    datePickerPlaceholder: null,

    // templateview: String
    // 		Name of template view which to render from template.
    templateview: "arrivaldatepicker",

    domTagType: null,

    // allows to set date from which date picker will start drawing months
    startDate: null,

    // allows to close  arrival date calender
    closeSelector: ".closecal",

    // allows to close  arrival date calender and open departure date calender as well
    changeDepartureSelector: ".changeDeparture",

    seasonEndValue : false,
    //Next Month
    nextMonthTxt: null,
    nextMonthTotalDays: null,
    datePickerNextMonthShowDate: null,
    nextMonthdaycells: null,
    nextMonthdatePickerConnects: null,
    monthsAndYearsForNextMonth:null,
    monthAndYearTxtForNextMonth:null,
    nextMonthDisplayLabel:null,
    selectedDateCell:null,
    //Next Month

    // ---------------------------------------------------------------- floatPosition properties

    // posOffset: Object
    //		Offset the top, and left position, of datepicker popup.
    posOffset: {top: 10, left: -598},

    isPositioned: false,

    // ---------------------------------------------------------------- templatable properties

    // tmpl: String
    //		Datepicker template string.
    tmpl: arrivalDatePickerTmpl,

    // ---------------------------------------------------------------- datePicker methods

    /*postMixInProperties: function () {
     // summary:
     //		Called before the DatePicker widget is created. Setting values to default states.
     var datePicker = this;
     datePicker.dateSelectOption = [];
     datePicker.datePickerConnects = [];
     var date = new Date();
     datePicker.now = new Date(date.getFullYear(), date.getMonth(), date.getDate());
     datePicker.inherited(arguments);
     },*/

    postMixInProperties: function () {
        // summary:
        // Called before the DatePicker widget is created. Setting values to default states.
        var arrivalDatePicker = this;
        arrivalDatePicker.dateSelectOption = [];
        arrivalDatePicker.datePickerConnects = [];
        arrivalDatePicker.nextMonthdatePickerConnects = [];
        var date = new Date();

        arrivalDatePicker.startDate = arrivalDatePicker.startDate ? arrivalDatePicker.startDate.split('-') : null;
        arrivalDatePicker.now = arrivalDatePicker.startDate
            ? new Date(arrivalDatePicker.startDate[2], arrivalDatePicker.startDate[1], arrivalDatePicker.startDate[0])
            : new Date(date.getFullYear(), date.getMonth(), date.getDate());
            arrivalDatePicker.inherited(arguments);
    },

    postCreate: function () {
        // summary:
        //		Called on post create to setup dom event listeners, and set default values.
        var arrivalDatePicker = this;
        if (arrivalDatePicker.domNode.tagName.toLowerCase() === "input") {
        	arrivalDatePicker.datePickerPlaceholder = arrivalDatePicker.domNode.placeholder;
          dojo.attr(arrivalDatePicker.domNode, "readonly", "readonly");
        }
        //dojo.attr(arrivalDatePicker.domNode, 'tabindex', 0);
        arrivalDatePicker.setDefaultDate();
        var daysAhead = dojo.clone(arrivalDatePicker.now);
        daysAhead.setDate(arrivalDatePicker.now.getDate() + arrivalDatePicker.defaultDaysAhead);
        arrivalDatePicker.selectedDate =
            (arrivalDatePicker.selectedDate && arrivalDatePicker.selectedDate > arrivalDatePicker.now) ? arrivalDatePicker.selectedDate : daysAhead;
        arrivalDatePicker.setup();
        //next month
        arrivalDatePicker.setupnextmonth();
        //next month
        arrivalDatePicker.combineMonthsAndYears();
        arrivalDatePicker.attachEvents();
        arrivalDatePicker.subscribetotopic();
        arrivalDatePicker.subscribeMonthBarSelectOption();

        // attaches on focus event to dom node.
        arrivalDatePicker.connect(document.body, "onclick", function (event) {
          if (document.activeElement === arrivalDatePicker.domNode) return;
          if (!arrivalDatePicker.arrivalDatePickerDOM || !arrivalDatePicker.isShowing(arrivalDatePicker.arrivalDatePickerDOM)) return;
          arrivalDatePicker.close();
        });

        arrivalDatePicker.inherited(arguments);
      },

  setDefaultDate: function () {
      // summary:
      //		set the default date for picker. if no default date has been selected
      //		then the NOW date is set.
      var arrivalDatePicker = this;
      arrivalDatePicker.selectedDate = dojo.date.locale.parse(arrivalDatePicker.getFieldValue(), {
        selector: "date",
        datePattern: arrivalDatePicker.datePattern
      });

      if (arrivalDatePicker.selectedDate) {
    	  arrivalDatePicker.setFieldValue(arrivalDatePicker.selectedDate);
        return;
      }
    },

    attachEvents: function () {
        // summary:
        //		Method which attaches event listeners to dom element.
        var arrivalDatePicker = this;

        // attaches on focus event to dom node.
        arrivalDatePicker.connect(arrivalDatePicker.domNode, "onfocus", function (event) {
          arrivalDatePicker.onCalendarFocus();
        });

        // attaches on blur event to dom node.
        arrivalDatePicker.connect(arrivalDatePicker.domNode, "onblur", function (event) {
          var arrivalDatePicker = this;
          dojo.stopEvent(event);
          if (!arrivalDatePicker.arrivalDatePickerDOM) {
            return;
          }
          arrivalDatePicker.datePickerTimer = setTimeout(function () {
            clearTimeout(arrivalDatePicker.datePickerTimer);
            arrivalDatePicker.close();
          }, 300);
        });
      },

      onCalendarFocus: function () {
          var arrivalDatePicker = this;
          if (!arrivalDatePicker.arrivalDatePickerDOM) {
            arrivalDatePicker.renderDatePicker();
          }
          arrivalDatePicker.domNode.placeholder = "";

          var showDate = dojo.date.locale.parse(arrivalDatePicker.getFieldValue(), {
            selector: "date",
            datePattern: arrivalDatePicker.datePattern
          });

          if (showDate) {
            showDate.setDate(1);

            if(arrivalDatePicker.selectedDateCell !== null && arrivalDatePicker.selectedDateCell === "secondCalender") {
            	arrivalDatePicker.datePickerNextMonthShowDate = showDate;
            }
            else if (arrivalDatePicker.datePickerShowDate.getTime() !== showDate.getTime()) {
              arrivalDatePicker.datePickerShowDate = showDate;
              arrivalDatePicker.setDateSelectOption();
            }
          }

          arrivalDatePicker.open();
        },

    combineMonthsAndYears: function () {
        // summary:
        //    combine months and years for date picker
        //    creates array of objects for months within season length or year range
        var arrivalDatePicker = this;
        var curMonth = arrivalDatePicker.month + 1;
        var curYear = arrivalDatePicker.year;
        var curNextMonth = curMonth + 1;
        var curNextMonthsYear = curYear;
        // arrivalDatePicker calculations start from current month so set seasonLength default to yearRange * 12 - months elapsed this year
        var seasonLength = arrivalDatePicker.seasonLength ||
            (arrivalDatePicker.yearRange * 12) - (arrivalDatePicker.datePickerShowDate.getMonth() + 1);

        arrivalDatePicker.monthsAndYears = [];

        // get text value of current month and year for template
        arrivalDatePicker.monthAndYearTxt = [arrivalDatePicker.months[curMonth - 1], ' ', arrivalDatePicker.years[curYear -arrivalDatePicker.year]].join('');
        arrivalDatePicker.monthAndYearTxtForNextMonth = [arrivalDatePicker.months[curNextMonth - 1], ' ', arrivalDatePicker.years[curNextMonthsYear -
                                                                                                             arrivalDatePicker.year]].join('');

        // create array of combined months and years
        // TODO: see why BAU has <= seasonLength
        for (var i = 0; i < seasonLength; i++) {
          arrivalDatePicker.monthsAndYears.push({
            value: [curMonth, '/', curYear].join(''),
            label: [arrivalDatePicker.months[curMonth - 1], ' ', arrivalDatePicker.years[curYear - arrivalDatePicker.year]].join(''),
            nextMonthValue: [curNextMonth, '/', curNextMonthsYear].join(''),
            nextMonthLabel: [arrivalDatePicker.months[curMonth - 1], ' ', arrivalDatePicker.years[curYear - arrivalDatePicker.year]].join('')
          });

          if (curMonth % 12 !== 0) {
            curMonth++;
            curNextMonth++;
          } else {
            curMonth = 1;
            curNextMonth = 2;
            curYear++;
            curNextMonthsYear++;
          }
        }
      },

      setup: function (newdate) {
          var arrivalDatePicker = this;
          var viewdate = arrivalDatePicker.selectedDate || dojo.clone(arrivalDatePicker.now);
          arrivalDatePicker.datePickerShowDate = newdate || new Date(viewdate.getFullYear(), viewdate.getMonth(), 1);
          arrivalDatePicker.totalDays = [31, arrivalDatePicker.isLeapYear(arrivalDatePicker.datePickerShowDate.getFullYear()) ? 29 :
              28 , 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
          arrivalDatePicker.day =
              (arrivalDatePicker.datePickerShowDate.getDay() === 0) ? 7 : arrivalDatePicker.datePickerShowDate.getDay();
          arrivalDatePicker.month = arrivalDatePicker.datePickerShowDate.getMonth();
          arrivalDatePicker.monthTxt = arrivalDatePicker.months[arrivalDatePicker.month];

          //TODO
          arrivalDatePicker.year = arrivalDatePicker.datePickerShowDate.getFullYear();
          arrivalDatePicker.years = [];
          for (var i = 0; i < arrivalDatePicker.yearRange; i++) {
            arrivalDatePicker.years[i] = arrivalDatePicker.now.getFullYear() + i;
          }

          arrivalDatePicker.predates = dojo.clone(arrivalDatePicker.dates);
          arrivalDatePicker.predates.splice((arrivalDatePicker.day - 1), 31 - (arrivalDatePicker.day - 1));
          arrivalDatePicker.monthdayamount = arrivalDatePicker.totalDays[arrivalDatePicker.datePickerShowDate.getMonth()];
          arrivalDatePicker.monthdates = [];

          var newrow = arrivalDatePicker.day;

          for (var i = 0; i < arrivalDatePicker.monthdayamount; i++) {
            var monthdates = {
              date: i + 1,
              newrow: false
            };
            if (newrow > 6) {
              newrow = 0;
              monthdates.newrow = true;
            }
            arrivalDatePicker.monthdates.push(monthdates);
            newrow++;
          }
          arrivalDatePicker.datePickerShowDate.setDate(arrivalDatePicker.monthdayamount);

          var endDate = (arrivalDatePicker.datePickerShowDate.getDay() === 0) ? arrivalDatePicker.datePickerShowDate.getDay() :
              7 - arrivalDatePicker.datePickerShowDate.getDay();
          arrivalDatePicker.enddates = dojo.clone(arrivalDatePicker.dates);
          arrivalDatePicker.enddates.splice(endDate, 31 - endDate);
          arrivalDatePicker.datePickerShowDate.setDate(1);
        },

      //next month
        setupnextmonth: function (newdate) {
            var arrivalDatePicker = this;
            var viewdate = arrivalDatePicker.selectedDate || dojo.clone(arrivalDatePicker.now);
            var secondMonth = null;
            arrivalDatePicker.datePickerNextMonthShowDate = null;
            arrivalDatePicker.monthAndYearLabelForNextMonth = [];
            if(newdate) {
            	newSecondMonth = newdate.getMonth();
            	newSecondMonthsYear = newdate.getFullYear();
            	arrivalDatePicker.datePickerNextMonthShowDate = new Date(newSecondMonthsYear, newSecondMonth, 1);

            	//Get the year related to that month to show in the template
            	for (var i = 0; i < arrivalDatePicker.years.length; i++) {
            		if(arrivalDatePicker.years[i] === newSecondMonthsYear) {
            			arrivalDatePicker.monthAndYearTxtForNextMonth = [arrivalDatePicker.months[newSecondMonth], ' ', arrivalDatePicker.years[i]].join('');
            			break;
            		}
            	}
            }
            else {
            	viewSecondMonth = viewdate.getMonth() + 1;
            	viewSecondMonthsYear = viewdate.getFullYear();
            	arrivalDatePicker.datePickerNextMonthShowDate = new Date(viewSecondMonthsYear, viewSecondMonth, 1);

            	//Get the year related to that month to show in the template
            	for (var i = 0; i < arrivalDatePicker.years.length; i++) {
            		if(arrivalDatePicker.years[i] === viewSecondMonthsYear) {
            			arrivalDatePicker.monthAndYearTxtForNextMonth = [arrivalDatePicker.months[viewSecondMonth], ' ', arrivalDatePicker.years[i]].join('');
            			break;
            		}
            	}
            }

            arrivalDatePicker.monthAndYearLabelForNextMonth.push(arrivalDatePicker.monthAndYearTxtForNextMonth);

            if(arrivalDatePicker.datePickerNextMonthShowDate !== null) {
            	arrivalDatePicker.nextMonthTotalDays = [31, arrivalDatePicker.isLeapYear(arrivalDatePicker.datePickerNextMonthShowDate.getFullYear()) ? 29 :
                    28 , 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
                arrivalDatePicker.day =
                    (arrivalDatePicker.datePickerNextMonthShowDate.getDay() === 0) ? 7 : arrivalDatePicker.datePickerNextMonthShowDate.getDay();

                arrivalDatePicker.nextMonth = arrivalDatePicker.datePickerNextMonthShowDate.getMonth();
                arrivalDatePicker.nextMonthTxt = arrivalDatePicker.months[arrivalDatePicker.nextMonth];

                //TODO
                	arrivalDatePicker.nextYear = arrivalDatePicker.datePickerNextMonthShowDate.getFullYear();

                arrivalDatePicker.years = [];
                for (var i = 0; i < arrivalDatePicker.yearRange; i++) {
                  arrivalDatePicker.years[i] = arrivalDatePicker.now.getFullYear() + i;
                }

                arrivalDatePicker.nextmonthpredates = dojo.clone(arrivalDatePicker.dates);
                arrivalDatePicker.nextmonthpredates.splice((arrivalDatePicker.day - 1), 31 - (arrivalDatePicker.day - 1));
                arrivalDatePicker.nextmonthdayamount = arrivalDatePicker.nextMonthTotalDays[arrivalDatePicker.datePickerNextMonthShowDate.getMonth()];
                arrivalDatePicker.nextmonthdates = [];

                var nextmonthnewrow = arrivalDatePicker.day;
                for (var i = 0; i < arrivalDatePicker.nextmonthdayamount; i++) {
                  var nextmonthdates = {
                    date: i + 1,
                    nextmonthnewrow: false
                  };
                  if (nextmonthnewrow > 6) {
                  	nextmonthnewrow = 0;
                  	nextmonthdates.nextmonthnewrow = true;
                  }
                  arrivalDatePicker.nextmonthdates.push(nextmonthdates);
                  nextmonthnewrow++;
                }
                arrivalDatePicker.datePickerNextMonthShowDate.setDate(arrivalDatePicker.nextmonthdayamount);

                var nextmonthEndDate = (arrivalDatePicker.datePickerNextMonthShowDate.getDay() === 0) ? arrivalDatePicker.datePickerNextMonthShowDate.getDay() :
                    7 - arrivalDatePicker.datePickerNextMonthShowDate.getDay();
                arrivalDatePicker.nextmonthenddates = dojo.clone(arrivalDatePicker.dates);
                arrivalDatePicker.nextmonthenddates.splice(nextmonthEndDate, 31 - nextmonthEndDate);
                arrivalDatePicker.datePickerNextMonthShowDate.setDate(1);
            }
          },
        //next month

  getSelectedDate: function () {
      var arrivalDatePicker = this;
      return arrivalDatePicker.selectedDate;
    },

    isLeapYear: function (year) {
      return ((year % 100 !== 0) && (year % 4 === 0) || (year % 400 === 0));
    },

    disableDatesByIndex: function (index) {
        // summary: Disables datepicker date based on the given index number.
        var arrivalDatePicker = this;
        var rightArrow = dojo.query(".next", arrivalDatePicker.datepickerDOM)[0];
        dojo.addClass(rightArrow, "disabled");
        var disableIndex = index || 0;
        for (var i = 0; i < arrivalDatePicker.daycells.length; i++) {
          if ((i + 1) > disableIndex) {
            arrivalDatePicker.setClassOnDate(i, "disabled", "firstCalender", false);
          }
        }
      },

      disablePastDatesByIndex: function (index) {
        var arrivalDatePicker = this;
        var leftArrow = dojo.query(".prev", arrivalDatePicker.datepickerDOM)[0];
        dojo.addClass(leftArrow, "disabled");
        var disableIndex = index || 0;
        for (var i = 0; i < arrivalDatePicker.daycells.length; i++) {
          if ((i + 1) < disableIndex) {
            arrivalDatePicker.setClassOnDate(i, "disabled", "firstCalender", false);
          }
        }
      },

      disableSecondMonthDatesByIndex: function (index) {
        // summary: Disables next month arrivalDatePicker date based on the given index number.
        var arrivalDatePicker = this;
        var disableIndex = index || 0;
        for (var i = 0; i < arrivalDatePicker.nextMonthdaycells.length; i++) {
          if ((i + 1) > disableIndex) {
            arrivalDatePicker.setClassOnDate(i, "disabled", "secondCalender", false);
          }
        }
      },

      disableSecondMonthPastDatesByIndex: function (index) {
        var arrivalDatePicker = this;
        var disableIndex = index || 0;
        for (var i = 0; i < arrivalDatePicker.nextMonthdaycells.length; i++) {
          if ((i + 1) < disableIndex) {
            arrivalDatePicker.setClassOnDate(i, "disabled", "secondCalender", false);
          }
        }
      },

      disableUnavailableDates: function (date, past) {
        // summary:
        // Determines if arrivalDatePicker needs to be disable
        // past or dates in the future, from the given date value.
        var arrivalDatePicker = this;
        //First Month Calendar
        if (arrivalDatePicker.datePickerShowDate.getMonth() === date.getMonth() &&
            arrivalDatePicker.datePickerShowDate.getFullYear() === date.getFullYear()) {
          var index = date.getDate();
          if (!past) {
            arrivalDatePicker.disableDatesByIndex(index);
          } else {
            arrivalDatePicker.disablePastDatesByIndex(index);
          }
        } else if (!past && arrivalDatePicker.datePickerShowDate > date) {
          arrivalDatePicker.disableDatesByIndex();
        } else if (past && arrivalDatePicker.datePickerShowDate < date) {
          // TODO: using disablePastDatesByIndex() ??
          arrivalDatePicker.disableDatesByIndex();
        }

        //Second Month Calendar
        if (arrivalDatePicker.datePickerNextMonthShowDate.getMonth() === date.getMonth() &&
            arrivalDatePicker.datePickerNextMonthShowDate.getFullYear() === date.getFullYear()) {
          var secondMonthIndex = date.getDate();
          if (!past) {
            arrivalDatePicker.disableSecondMonthDatesByIndex(secondMonthIndex);
          } else {
            arrivalDatePicker.disableSecondMonthPastDatesByIndex(secondMonthIndex);
          }
        } else if (!past && arrivalDatePicker.datePickerNextMonthShowDate > date) {
          arrivalDatePicker.disableSecondMonthDatesByIndex();
        } else if (past && arrivalDatePicker.datePickerNextMonthShowDate < date) {
          // TODO: using disablePastDatesByIndex() ??
          arrivalDatePicker.disableSecondMonthDatesByIndex();
        }
      },

    subscribetotopic: function () {
      var arrivalDatePicker = this;
      dojo.subscribe("tui/widget/datepicker/ReturnDateSelectOption/onclick", function (dateSelectOption, select) {
        if (dojo.query(select).parents(".calendar")[0] === arrivalDatePicker.arrivalDatePickerDOM) {
          arrivalDatePicker.clearDatePickerTimer();
        }
      });

      arrivalDatePicker.subscribeDateSelectOption();
    },

    subscribeDateSelectOption: function () {
      // summary: subscribe to selectOption and redraw arrivalDatePicker onchange
      var arrivalDatePicker = this;
      arrivalDatePicker.dateSelectOptionChannel = dojo.subscribe("tui/widget/datepicker/ReturnDateSelectOption/onchange", function (selectoption, select, newdata) {
        if (dojo.query(select).parents(".calendar")[0] === arrivalDatePicker.arrivalDatePickerDOM) {
          var month = parseInt(newdata.value.split('/')[0], 10) - 1;
          var year = parseInt(newdata.value.split('/')[1], 10);
          arrivalDatePicker.datePickerShowDate.setMonth(month);
          arrivalDatePicker.datePickerShowDate.setFullYear(year);

        //Season End Date Validation
          arrivalDatePicker.datePickerShowDate.setDate(parseInt(arrivalDatePicker.totalDays[arrivalDatePicker.datePickerShowDate.getMonth()], 10));

          if(arrivalDatePicker.datePickerShowDate.valueOf() >= arrivalDatePicker.seasonEndDate.valueOf()){
        	  arrivalDatePicker.datePickerShowDate.setDate(1);
        	  arrivalDatePicker.datePickerShowDate.setMonth(arrivalDatePicker.datePickerShowDate.getMonth()-1);
        	  arrivalDatePicker.seasonEndValue = true;
        	  month = month-1;
          }else{
        	  arrivalDatePicker.datePickerShowDate.setDate(1);
          }

          var secondMonth = null;
          var secondMonthYear = null;
          if(month === 11) {
        	  secondMonthYear = year + 1;
        	  secondMonth = 0;
          } else {
        	  secondMonthYear = year;
        	  secondMonth = month + 1;
          }

          arrivalDatePicker.datePickerNextMonthShowDate.setMonth(secondMonth);
          if(secondMonthYear !== null) {
        	  arrivalDatePicker.datePickerNextMonthShowDate.setFullYear(secondMonthYear);
          }

          // TODO: this is not needed in regular arrivalDatePicker
          arrivalDatePicker.skipMonth = false;
          arrivalDatePicker.skipNextMonth = false;
          arrivalDatePicker.redrawDatePicker(null);
        }
      });
    },

    subscribeMonthBarSelectOption: function () {
        // summary: subscribe to selectOption and redraw datepicker on select month @ month bar
        var arrivalDatePicker = this;
        dojo.subscribe("tui/widget/datepicker/ArrivalMonthBarSelectOption/onchange", function (month, year) {

            dojo.query("div.monthSelector").empty();
            dojo.style(dojo.query(".arrivaldate-selector")[0], {
            	"display":"block"
            });

            dojo.style(dojo.query(".change-departure")[0], {
              	"display":"inline"
            });

            arrivalDatePicker.datePickerShowDate.setMonth(month);
            arrivalDatePicker.datePickerShowDate.setFullYear(year);

            //Season End Date Validation

            arrivalDatePicker.datePickerShowDate.setDate(parseInt(arrivalDatePicker.totalDays[arrivalDatePicker.datePickerShowDate.getMonth()], 10));
            if(arrivalDatePicker.datePickerShowDate.valueOf() >= arrivalDatePicker.seasonEndDate.valueOf()){
            	arrivalDatePicker.datePickerShowDate.setDate(1);
            	arrivalDatePicker.datePickerShowDate.setMonth(arrivalDatePicker.datePickerShowDate.getMonth()-1);
          	  	month = month-1;
          	  arrivalDatePicker.seasonEndValue = true;
            }else{
            	arrivalDatePicker.datePickerShowDate.setDate(1);
            }


            var secondMonth = null;
            var secondMonthYear = null;
            if(month === 11) {
          	  secondMonthYear = year + 1;
          	  secondMonth = 0;
            } else {
          	  secondMonthYear = year;
          	  secondMonth = month + 1;
            }

            arrivalDatePicker.datePickerNextMonthShowDate.setMonth(secondMonth);
            if(secondMonthYear !== null) {
          	  arrivalDatePicker.datePickerNextMonthShowDate.setFullYear(secondMonthYear);
            }
            arrivalDatePicker.skipMonth = false;
            arrivalDatePicker.skipNextMonth = false;
            arrivalDatePicker.redrawDatePicker(null);
        });
      },

    unsubscribeDateSelectOption: function () {
      // summary: unsubscribe from select option
      var arrivalDatePicker = this;
      arrivalDatePicker.dateSelectOptionChannel.remove();
    },

    open: function () {
      var arrivalDatePicker = this;
      dojo.addClass(dojo.byId("returnTravel"),"border-sel-active");
      dojo.publish("tui/widget/datepicker/DatePicker/open", [arrivalDatePicker]);
      !arrivalDatePicker.isPositioned ? arrivalDatePicker.posElement(arrivalDatePicker.arrivalDatePickerDOM) : null;
      arrivalDatePicker.isPositioned = true;
      arrivalDatePicker.showWidget(arrivalDatePicker.arrivalDatePickerDOM);
      dojo.addClass(arrivalDatePicker.domNode, "focus");

      arrivalDatePicker._resizeConnect = arrivalDatePicker.connect(window, "onresize", function () {
        arrivalDatePicker.resize();
      });
    },

    resize: function () {
      var arrivalDatePicker = this;
      if (arrivalDatePicker.arrivalDatePickerDOM) {
        if (arrivalDatePicker.isShowing(arrivalDatePicker.arrivalDatePickerDOM)) arrivalDatePicker.posElement(arrivalDatePicker.arrivalDatePickerDOM);
      }
    },

    close: function () {
      var arrivalDatePicker = this;
      dojo.removeClass(dojo.byId("returnTravel"),"border-sel-active");
      dojo.publish("tui/widget/datepicker/ArrivalDatePicker/close", [arrivalDatePicker]);
      arrivalDatePicker.disconnect(arrivalDatePicker._resizeConnect);
      arrivalDatePicker.domNode.placeholder = arrivalDatePicker.datePickerPlaceholder;
      arrivalDatePicker.hideWidget(arrivalDatePicker.arrivalDatePickerDOM);
      dojo.removeClass(arrivalDatePicker.domNode, "focus")
      //comented the below code as it is effecting Desktop site
      //dojo.query(".tooltip").forEach(dojo.destroy);	//to close tooltips in devices.
    },

    setClassOnDate: function (index, classname, selectedmonth, remove) {
      var arrivalDatePicker = this;
      if (arrivalDatePicker.daycells.length === 0
    		  && arrivalDatePicker.nextMonthdaycells.length === 0) return;
      remove = (remove === undefined) ? true : remove;
      if (remove) {
        dojo.query([".", classname].join(""), arrivalDatePicker.arrivalDatePickerDOM).removeClass(classname);
      }

      if(selectedmonth === "firstCalender") {
    	  selectedDateCell = selectedmonth;
    	  if(typeof arrivalDatePicker.daycells[index] !== "undefined")
    		  dojo.addClass(arrivalDatePicker.daycells[index], classname);
      }
      else if(selectedmonth === "secondCalender") {
    	  selectedDateCell = selectedmonth;
    	  dojo.addClass(arrivalDatePicker.nextMonthdaycells[index], classname);
      }
    },

    renderDatePicker: function () {
      var datePicker = this;
      datePicker.renderTmpl(datePicker.tmpl);
    },

    getFieldValue: function () {
      // summary: Get current value of datepicker field.
      var datePicker = this;
      return (datePicker.domNode.tagName.toLowerCase() === 'input') ?
          datePicker.domNode.value : dojo.query(datePicker.domNode).text();
    },

    setFieldValue: function (newdate, fieldId) {
      // summary: Set date picker field with selected value.
      var datePicker = this;
      // convert date to required format.
      var date = (typeof newdate === "string") ? newdate : dojo.date.locale.format(newdate, {
        selector: "date",
        datePattern: datePicker.datePattern
      });

      // set value to dom node.
      if (datePicker.domNode.tagName.toLowerCase() === 'input') {
        datePicker.domNode.value = date
        return;
      }
      dojo.query("#" + fieldId).text(date);
    },

  addDatePickerEventListener: function () {
  var datePicker = this;
  datePicker.daycells = dojo.query(".arrival-datepicker-day", datePicker.arrivalDatePickerDOM);
  datePicker.nextMonthdaycells = dojo.query(".arrival-next-month-datepicker-day", datePicker.arrivalDatePickerDOM);

  datePicker.daycells.forEach(function (element, index) {
    datePicker.datePickerConnects.push(dojo.connect(element, "onclick", function (event) {
      dojo.stopEvent(event);
      var cell = this;
      if (dojo.hasClass(cell, "disabled")) {
        datePicker.clearDatePickerTimer();
        return;
      }
      dojo.query("a", element)[0].focus();
      datePicker.selectedDate = datePicker.selectedDate || dojo.clone(datePicker.now);
      //TODO: uncomment and make the Permanent fix.
     // datePicker.setClassOnDate(index, "selected", "firstCalender", true);

      /*datePicker.selectedDate.setDate(index + 1);
      datePicker.selectedDate.setMonth(datePicker.month);
      datePicker.selectedDate.setFullYear(datePicker.year);
      */
      datePicker.selectedDate = new Date(datePicker.year,datePicker.month,index + 1);
      datePicker.setFieldValue(datePicker.selectedDate, datePicker.domNode.id);

      if (datePicker.closeOnSelection) {
        datePicker.close();
      }
      datePicker.onSelectedDate(datePicker.selectedDate);
    }));
  });

//Next Month
  datePicker.nextMonthdaycells.forEach(function (element, index) {
	  datePicker.nextMonthdatePickerConnects.push(dojo.connect(element, "onclick", function (event) {
        dojo.stopEvent(event);
        var cell = this;
        if (dojo.hasClass(cell, "disabled")) {
          datePicker.clearDatePickerTimer();
          return;
        }
        dojo.query("a", element)[0].focus();
        //datePicker.selectedDate = datePicker.selectedDate || dojo.clone(datePicker.now);
        datePicker.setClassOnDate(index, "selected", "secondCalender", true);
       /* datePicker.selectedDate.setDate(index + 1);
        datePicker.selectedDate.setMonth(datePicker.month + 1);
        datePicker.selectedDate.setFullYear(datePicker.nextYear);*/


        if(datePicker.month === 11) {
        	datePicker.month = 0;
        }else {
        	datePicker.month = datePicker.month+1;
        }

        datePicker.selectedDate = new Date(datePicker.nextYear,datePicker.month,index + 1);
        datePicker.setFieldValue(datePicker.selectedDate, datePicker.domNode.id);
       /* //Ex: If the current month is Dec 2014 then prepare second month as Jan 2015.
        if(datePicker.nextMonth === 11) {
        	datePicker.selectedDate.setMonth(0);
        	datePicker.selectedDate.setFullYear(datePicker.year + 1);
        }*/
        if (datePicker.closeOnSelection) {
          datePicker.close();
        }
        datePicker.onSelectedDate(datePicker.selectedDate);
      }));
    });
//Next Month

      if (datePicker.templateview === "arrivaldatepicker") {
        datePicker.connect(datePicker.arrivalDatePickerDOM, "onclick", function (event) {
          dojo.stopEvent(event);
        });

        var leftArrow = dojo.query(".prev", datePicker.arrivalDatePickerDOM)[0];
        datePicker.connect(leftArrow, "onclick", function (event) {
          dojo.stopEvent(event);
          datePicker.clearDatePickerTimer();
          if (dojo.hasClass(leftArrow, "disabled")) {
            return;
          }
          datePicker.datePickerShowDate.setMonth(datePicker.datePickerShowDate.getMonth() - 1);
          datePicker.datePickerNextMonthShowDate.setMonth(datePicker.datePickerNextMonthShowDate.getMonth() - 1);
          datePicker.setDateSelectOption();
        });

        var rightArrow = dojo.query(".next", datePicker.arrivalDatePickerDOM)[0];
        datePicker.connect(rightArrow, "onclick", function (event) {
          dojo.stopEvent(event);
          datePicker.clearDatePickerTimer();
          if (dojo.hasClass(rightArrow, "disabled")) {
            return;
          }
          datePicker.datePickerShowDate.setMonth(datePicker.datePickerShowDate.getMonth() + 1);
          datePicker.datePickerNextMonthShowDate.setMonth(datePicker.datePickerNextMonthShowDate.getMonth() + 1);
          datePicker.setDateSelectOption();
        });
      }
    },

    setDateSelectOption: function () {
      var datePicker = this;
     // console.log(monthAndYear)
      var monthAndYear = [(datePicker.datePickerShowDate.getMonth() +
          1), '/', datePicker.datePickerShowDate.getFullYear()].join('');
     // console.log(monthAndYear)
      if (datePicker.dateSelectOption[0].getSelectedData().value !== monthAndYear) {
        datePicker.dateSelectOption[0].setSelectedValue(monthAndYear);
      }
    },

    clearDatePickerTimer: function () {
      var datePicker = this;
      clearTimeout(datePicker.datePickerTimer);
      datePicker.datePickerTimer = null;
    },

    redrawDatePicker: function (renderCalendar) {
        var arrivalDatePicker = this;
        arrivalDatePicker.templateview = "arrivaldatepickercells";
        if(renderCalendar == null) {
      	  arrivalDatePicker.deleteDatePicker();
            arrivalDatePicker.deleteNextMonthDatePicker();
            arrivalDatePicker.setup(arrivalDatePicker.datePickerShowDate);
            arrivalDatePicker.setupnextmonth(arrivalDatePicker.datePickerNextMonthShowDate);
        }
        else if(renderCalendar == "first") {
      	  arrivalDatePicker.deleteDatePicker();
      	  arrivalDatePicker.setupnextmonth(arrivalDatePicker.datePickerNextMonthShowDate);
        }
        else if(renderCalendar == "second") {
      	  arrivalDatePicker.deleteNextMonthDatePicker();
      	  arrivalDatePicker.setup(arrivalDatePicker.datePickerShowDate);
        }
        arrivalDatePicker.renderDatePicker();
      },

    deleteDatePicker: function () {
        var arrivalDatePicker = this;
        for (var i = 0; i < arrivalDatePicker.datePickerConnects; i++) {
          dojo.disconnect(arrivalDatePicker.datePickerConnects[i]);
        }
        arrivalDatePicker.datePickerConnects.length = 0;
        arrivalDatePicker.daycells.length = 0;

        dojo.query(".datepickertable", arrivalDatePicker.datepickerDOM).remove();
      },

      deleteNextMonthDatePicker: function () {
          var arrivalDatePicker = this;
          for (var i = 0; i < arrivalDatePicker.nextMonthdatePickerConnects; i++) {
            dojo.disconnect(arrivalDatePicker.nextMonthdatePickerConnects[i]);
          }
          arrivalDatePicker.nextMonthdatePickerConnects.length = 0;
          arrivalDatePicker.nextMonthdaycells.length = 0;

          dojo.query(".second-month-datepickertable", arrivalDatePicker.datepickerDOM).remove();
        },

    enableArrowBtn: function () {
      var datePicker = this;
      dojo.removeClass(dojo.query(".next", datePicker.arrivalDatePickerDOM)[0], "disabled");
      dojo.removeClass(dojo.query(".prev", datePicker.arrivalDatePickerDOM)[0], "disabled");
    },

    onAfterTmplRender: function (datepickerHTML) {
      var datePicker = this;
      if (datePicker.templateview === "arrivaldatepicker") {
        datePicker.arrivalDatePickerDOM = dojo.place(dojo.trim(datepickerHTML), document.body);
        dojo.parser.parse(datePicker.arrivalDatePickerDOM);
        if(datePicker.searchPanelModel.returnDate !== ""){
        	datePicker.selectedDate = new Date(datePicker.searchPanelModel.returnDate);
        }


        dojo.connect(datePicker.arrivalDatePickerDOM, "onclick", function (event) {
          dojo.stopEvent(event);
          datePicker.clearDatePickerTimer();
        });
        on(datePicker.arrivalDatePickerDOM,on.selector(datePicker.closeSelector, "click"), function (event) {
            dojo.stopEvent(event);
             datePicker.close();
        });
        on(datePicker.arrivalDatePickerDOM,on.selector(datePicker.changeDepartureSelector, "click"), function (event) {
            dojo.stopEvent(event);
           datePicker.close();
           dojo.publish("tui/widget/datepicker/ArrivalDatePicker/changeDepartureDate");
        });
      } else {
        dojo.place(dojo.trim(datepickerHTML), datePicker.arrivalDatePickerDOM);
      }

      datePicker.enableArrowBtn();
      datePicker.addDatePickerEventListener();

      dojo.style(dojo.query(".arrivaldate-selector")[0], {
        	"display":"block"
      });

      if(datePicker.searchPanelModel.returnDate !== ""){
    	  if(datePicker.selectedDate.getMonth() == datePicker.datePickerShowDate.getMonth() &&
    			 datePicker.selectedDate.getYear() == datePicker.datePickerShowDate.getYear()){
		    	  var index = datePicker.selectedDate.getDate();
		    	  if(dojo.query('.first-month')){
		    		  datePicker.setClassOnDate(index - 1, "selected", "firstCalender");
		    	  }
	      } else if(datePicker.selectedDate.getMonth() == datePicker.datePickerNextMonthShowDate.getMonth() &&
	    		  datePicker.selectedDate.getYear() == datePicker.datePickerNextMonthShowDate.getYear() && datePicker.unAvailableMonthName === ''){
		    	  var index = datePicker.selectedDate.getDate();
		          datePicker.setClassOnDate(index - 1, "selected", "secondCalender");

	      }

      }

      if(datePicker.searchPanelModel.date !== ""){
    	  if(new Date(datePicker.searchPanelModel.date).getMonth() == datePicker.datePickerShowDate.getMonth() &&
    			  new Date(datePicker.searchPanelModel.date).getYear() == datePicker.datePickerShowDate.getYear()){
    		  var index = new Date(datePicker.searchPanelModel.date).getDate();
    		  if(dojo.query('.first-month')){
	    		  datePicker.setClassOnDate(index - 1, "selected-from", "firstCalender");
	    	  }

    	  } else if(new Date(datePicker.searchPanelModel.date).getMonth() == datePicker.datePickerNextMonthShowDate.getMonth() &&
    			  new Date(datePicker.searchPanelModel.date).getYear() == datePicker.datePickerNextMonthShowDate.getYear()){
    		  var index = new Date(datePicker.searchPanelModel.date).getDate();
              datePicker.setClassOnDate(index - 1, "selected-from", "secondCalender");

    	  }


      }


     /* if (datePicker.datePickerShowDate.getMonth() === datePicker.now.getMonth() &&
          datePicker.datePickerShowDate.getFullYear() === datePicker.now.getFullYear()) {
        var index = datePicker.now.getDate();
        datePicker.setClassOnDate(index - 1, "today", "firstCalender");
      }

      if (datePicker.selectedDate) {
        if (datePicker.datePickerShowDate.getMonth() === datePicker.selectedDate.getMonth() &&
            datePicker.datePickerShowDate.getFullYear() === datePicker.selectedDate.getFullYear()) {
          var index = datePicker.selectedDate.getDate();
          datePicker.setClassOnDate(index - 1, "selected", "firstCalender");
        }
        else if (datePicker.datePickerNextMonthShowDate.getMonth() === datePicker.selectedDate.getMonth() &&
                 datePicker.datePickerNextMonthShowDate.getFullYear() === datePicker.selectedDate.getFullYear()) {
            var index = datePicker.selectedDate.getDate();
            datePicker.setClassOnDate(index - 1, "selected", "secondCalender");
        }
      }*/

      var disableDate = new Date(datePicker.now.getTime());
      disableDate.setDate(disableDate.getDate() + 1);
      datePicker.disableUnavailableDates(disableDate, true);
      if (datePicker.templateview === "arrivaldatepicker") {
        var customDropDown = dojo.query(".custom-dropdown", datePicker.arrivalDatePickerDOM);
        customDropDown.forEach(function (element, index) {
          var selectWidget = registry.getEnclosingWidget(element);
          datePicker.dateSelectOption[index] = selectWidget;
        });
      }
      //if previous month is not available disable left arrow.
      var selectedMonthandYearTxt = datePicker.dateSelectOption[0].getSelectedData().value.split("/");
      var selectedFullDate = new Date(selectedMonthandYearTxt[0] + "/" + 1 +"/"+ selectedMonthandYearTxt[1]);
      if(!datePicker.findAvailableMonth(selectedFullDate)){
    	 dojo.addClass(dojo.query(".prev", datePicker.arrivalDatePickerDOM)[0], "disabled");
      }

    },

    onSelectedDate: function (date) {
    }
  });

  return tui.widget.datepicker.ArrivalDatePicker;
});