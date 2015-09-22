define("tui/widget/datepicker/flights/DatePicker", [
  "dojo",
  "dojo/text!tui/widget/datepicker/templates/datepicker.html",
  "dijit/registry",
  "dojo/query",
  "dojo/on",
  "dojo/NodeList-traverse",
  "dojo/date/locale",
  "tui/widget/mixins/FloatPosition",
  "tui/widget/_TuiBaseWidget",
  "tui/widget/datepicker/DateSelectOption",
  "tui/widget/mixins/Templatable"], function (dojo, datePickerTmpl, registry, query, on) {

  dojo.declare("tui.widget.datepicker.flights.DatePicker", [tui.widget._TuiBaseWidget, tui.widget.mixins.FloatPosition, tui.widget.mixins.Templatable], {


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

    // datepickerDOM: DOMNode
    // 		Reference to DOM element for date picker popup.
    datepickerDOM: null,

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
    templateview: "datepicker",

    domTagType: null,

    // allows to set date from which date picker will start drawing months
    startDate: null,

    closeSelector: ".closecal",

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
    posOffset: {top: 10, left: -430},

    isPositioned: false,

    // ---------------------------------------------------------------- templatable properties

    // tmpl: String
    //		Datepicker template string.
    tmpl: datePickerTmpl,

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
      var datePicker = this;
      datePicker.dateSelectOption = [];
      datePicker.datePickerConnects = [];
      datePicker.nextMonthdatePickerConnects = [];
      var date = new Date();

      datePicker.startDate = datePicker.startDate ? datePicker.startDate.split('-') : null;
      if(datePicker.startDate == null || datePicker.startDate.length <= 1) {
        	datePicker.now = new Date(date.getFullYear(), date.getMonth(), date.getDate());
        }
        else{
        	datePicker.now = new Date(datePicker.startDate[2], datePicker.startDate[1]-1, datePicker.startDate[0]);
        }
      /*datePicker.now = datePicker.startDate
          ? new Date(datePicker.startDate[2], datePicker.startDate[1], datePicker.startDate[0])
          : new Date(date.getFullYear(), date.getMonth(), date.getDate());*/
      datePicker.inherited(arguments);
    },

    postCreate: function () {
      // summary:
      //		Called on post create to setup dom event listeners, and set default values.
      var datePicker = this;
      if (datePicker.domNode.tagName.toLowerCase() === "input") {
        datePicker.datePickerPlaceholder = datePicker.domNode.placeholder;
        dojo.attr(datePicker.domNode, "readonly", "readonly");
      }

      //dojo.attr(datePicker.domNode, 'tabindex', 0);
      datePicker.setDefaultDate();
      var daysAhead = dojo.clone(datePicker.now);
      daysAhead.setDate(datePicker.now.getDate() + datePicker.defaultDaysAhead);
      datePicker.selectedDate =
          (datePicker.selectedDate && datePicker.selectedDate > datePicker.now) ? datePicker.selectedDate : daysAhead;
      datePicker.setup();
      datePicker.setupnextmonth();
      datePicker.combineMonthsAndYears();
      datePicker.attachEvents();
      datePicker.subscribetotopic();
      datePicker.subscribeMonthBarSelectOption();

      // attaches on focus event to dom node.
      datePicker.connect(document.body, "onclick", function (event) {
        if (document.activeElement === datePicker.domNode) return;
        if (!datePicker.datepickerDOM || !datePicker.isShowing(datePicker.datepickerDOM)) return;
        datePicker.close();
      });

      dojo.subscribe("tui/widget/datepicker/ArrivalDatePicker/changeDepartureDate",function(){
    	  datePicker.onCalendarFocus();
      });

      datePicker.inherited(arguments);
    },

    setDefaultDate: function () {
      // summary:
      //		set the default date for picker. if no default date has been selected
      //		then the NOW date is set.
      var datePicker = this;
      datePicker.selectedDate = dojo.date.locale.parse(datePicker.getFieldValue(), {
        selector: "date",
        datePattern: datePicker.datePattern
      });

      if (datePicker.selectedDate) {
        datePicker.setFieldValue(datePicker.selectedDate);
        return;
      }
    },

    attachEvents: function () {
      // summary:
      //		Method which attaches event listeners to dom element.
      var datePicker = this;

      // attaches on focus event to dom node.
      datePicker.connect(datePicker.domNode, "onfocus", function (event) {
        datePicker.onCalendarFocus();
      });

      // attaches on blur event to dom node.
      datePicker.connect(datePicker.domNode, "onblur", function (event) {
        var datePicker = this;
        dojo.stopEvent(event);
        if (!datePicker.datepickerDOM) {
          return;
        }
        datePicker.datePickerTimer = setTimeout(function () {
          clearTimeout(datePicker.datePickerTimer);
          datePicker.close();
        }, 300);
      });
    },

    onCalendarFocus: function () {
      var datePicker = this;
      if (!datePicker.datepickerDOM) {
        datePicker.renderDatePicker();
      }
      datePicker.domNode.placeholder = "";

      var showDate = dojo.date.locale.parse(datePicker.getFieldValue(), {
        selector: "date",
        datePattern: datePicker.datePattern
      });

      if (showDate) {
        showDate.setDate(1);
        if(datePicker.selectedDateCell !== null && datePicker.selectedDateCell === "secondCalender") {
        	datePicker.datePickerNextMonthShowDate = showDate;
        }
        else if (datePicker.datePickerShowDate.getTime() !== showDate.getTime()) {
          datePicker.datePickerShowDate = showDate;
          datePicker.setDateSelectOption();
        }
      }

      datePicker.open();
    },

    combineMonthsAndYears: function () {
      // summary:
      //    combine months and years for date picker
      //    creates array of objects for months within season length or year range
      var datePicker = this;
      var curMonth = datePicker.month + 1;
      var curYear = datePicker.year;
      var curNextMonth = curMonth + 1;
      var curNextMonthsYear = curYear;
      // datepicker calculations start from current month so set seasonLength default to yearRange * 12 - months elapsed this year
      var seasonLength = datePicker.seasonLength ||
          (datePicker.yearRange * 12) - (datePicker.datePickerShowDate.getMonth() + 1);

      datePicker.monthsAndYears = [];

      // get text value of current month and year for template
      datePicker.monthAndYearTxt = [datePicker.months[curMonth - 1], ' ', datePicker.years[curYear -datePicker.year]].join('');
      datePicker.monthAndYearTxtForNextMonth = [datePicker.months[curNextMonth - 1], ' ', datePicker.years[curNextMonthsYear -
                                                                                                           datePicker.year]].join('');

      // create array of combined months and years
      // TODO: see why BAU has <= seasonLength
      for (var i = 0; i < seasonLength; i++) {
        datePicker.monthsAndYears.push({
          value: [curMonth, '/', curYear].join(''),
          label: [datePicker.months[curMonth - 1], ' ', datePicker.years[curYear - datePicker.year]].join(''),
          nextMonthValue: [curNextMonth, '/', curNextMonthsYear].join(''),
          nextMonthLabel: [datePicker.months[curMonth - 1], ' ', datePicker.years[curYear - datePicker.year]].join('')
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
      var datePicker = this;
      var viewdate = datePicker.selectedDate || dojo.clone(datePicker.now);
      datePicker.datePickerShowDate = newdate || new Date(viewdate.getFullYear(), viewdate.getMonth(), 1);
      datePicker.totalDays = [31, datePicker.isLeapYear(datePicker.datePickerShowDate.getFullYear()) ? 29 :
          28 , 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
      datePicker.day =
          (datePicker.datePickerShowDate.getDay() === 0) ? 7 : datePicker.datePickerShowDate.getDay();
      datePicker.month = datePicker.datePickerShowDate.getMonth();
      datePicker.monthTxt = datePicker.months[datePicker.month];

      //TODO
      datePicker.year = datePicker.datePickerShowDate.getFullYear();
      datePicker.years = [];
      for (var i = 0; i < datePicker.yearRange; i++) {
        datePicker.years[i] = datePicker.now.getFullYear() + i;
      }

      datePicker.predates = dojo.clone(datePicker.dates);
      datePicker.predates.splice((datePicker.day - 1), 31 - (datePicker.day - 1));
      datePicker.monthdayamount = datePicker.totalDays[datePicker.datePickerShowDate.getMonth()];
      datePicker.monthdates = [];

      var newrow = datePicker.day;

      for (var i = 0; i < datePicker.monthdayamount; i++) {
        var monthdates = {
          date: i + 1,
          newrow: false
        };
        if (newrow > 6) {
          newrow = 0;
          monthdates.newrow = true;
        }
        datePicker.monthdates.push(monthdates);
        newrow++;
      }
      datePicker.datePickerShowDate.setDate(datePicker.monthdayamount);

      var endDate = (datePicker.datePickerShowDate.getDay() === 0) ? datePicker.datePickerShowDate.getDay() :
          7 - datePicker.datePickerShowDate.getDay();
      datePicker.enddates = dojo.clone(datePicker.dates);
      datePicker.enddates.splice(endDate, 31 - endDate);
      datePicker.datePickerShowDate.setDate(1);
    },

    setupnextmonth: function (newdate) {
        var datePicker = this;
        var viewdate = datePicker.selectedDate || dojo.clone(datePicker.now);
        var secondMonth = null;
        datePicker.datePickerNextMonthShowDate = null;
        datePicker.monthAndYearLabelForNextMonth = [];
        if(newdate) {
        	newSecondMonth = newdate.getMonth();
        	newSecondMonthsYear = newdate.getFullYear();
        	datePicker.datePickerNextMonthShowDate = new Date(newSecondMonthsYear, newSecondMonth, 1);

        	//Get the year related to that month to show in the template
        	for (var i = 0; i < datePicker.years.length; i++) {
        		if(datePicker.years[i] === newSecondMonthsYear) {
        			datePicker.monthAndYearTxtForNextMonth = [datePicker.months[newSecondMonth], ' ', datePicker.years[i]].join('');
        			break;
        		}
        	}
        }
        else {
        	viewSecondMonth = viewdate.getMonth() + 1;
        	viewSecondMonthsYear = viewdate.getFullYear();
        	datePicker.datePickerNextMonthShowDate = new Date(viewSecondMonthsYear, viewSecondMonth, 1);

        	//Get the year related to that month to show in the template
        	for (var i = 0; i < datePicker.years.length; i++) {
        		if(datePicker.years[i] === viewSecondMonthsYear) {
        			datePicker.monthAndYearTxtForNextMonth = [datePicker.months[viewSecondMonth], ' ', datePicker.years[i]].join('');
        			break;
        		}
        	}
        }

        datePicker.monthAndYearLabelForNextMonth.push(datePicker.monthAndYearTxtForNextMonth);

        if(datePicker.datePickerNextMonthShowDate !== null) {
        	datePicker.nextMonthTotalDays = [31, datePicker.isLeapYear(datePicker.datePickerNextMonthShowDate.getFullYear()) ? 29 :
                28 , 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
            datePicker.day =
                (datePicker.datePickerNextMonthShowDate.getDay() === 0) ? 7 : datePicker.datePickerNextMonthShowDate.getDay();

            datePicker.nextMonth = datePicker.datePickerNextMonthShowDate.getMonth();
            datePicker.nextMonthTxt = datePicker.months[datePicker.nextMonth];

            //TODO
            if(datePicker.datePickerNextMonthShowDate.getMonth() === 0){
            	datePicker.year = datePicker.datePickerNextMonthShowDate.getFullYear()-1;
            }else{
            	datePicker.year = datePicker.datePickerNextMonthShowDate.getFullYear();
            }
            datePicker.years = [];
            for (var i = 0; i < datePicker.yearRange; i++) {
              datePicker.years[i] = datePicker.now.getFullYear() + i;
            }

            datePicker.nextmonthpredates = dojo.clone(datePicker.dates);
            datePicker.nextmonthpredates.splice((datePicker.day - 1), 31 - (datePicker.day - 1));
            datePicker.nextmonthdayamount = datePicker.nextMonthTotalDays[datePicker.datePickerNextMonthShowDate.getMonth()];
            datePicker.nextmonthdates = [];

            var nextmonthnewrow = datePicker.day;
            for (var i = 0; i < datePicker.nextmonthdayamount; i++) {
              var nextmonthdates = {
                date: i + 1,
                nextmonthnewrow: false
              };
              if (nextmonthnewrow > 6) {
              	nextmonthnewrow = 0;
              	nextmonthdates.nextmonthnewrow = true;
              }
              datePicker.nextmonthdates.push(nextmonthdates);
              nextmonthnewrow++;
            }
            datePicker.datePickerNextMonthShowDate.setDate(datePicker.nextmonthdayamount);

            var nextmonthEndDate = (datePicker.datePickerNextMonthShowDate.getDay() === 0) ? datePicker.datePickerNextMonthShowDate.getDay() :
                7 - datePicker.datePickerNextMonthShowDate.getDay();
            datePicker.nextmonthenddates = dojo.clone(datePicker.dates);
            datePicker.nextmonthenddates.splice(nextmonthEndDate, 31 - nextmonthEndDate);
            datePicker.datePickerNextMonthShowDate.setDate(1);
        }
      },

    getSelectedDate: function () {
      var datePicker = this;
      return datePicker.selectedDate;
    },

    isLeapYear: function (year) {
      return ((year % 100 !== 0) && (year % 4 === 0) || (year % 400 === 0));
    },

    disableDatesByIndex: function (index) {
      // summary: Disables datepicker date based on the given index number.
      var datePicker = this;
      var rightArrow = dojo.query(".next", datePicker.datepickerDOM)[0];
      dojo.addClass(rightArrow, "disabled");
      var disableIndex = index || 0;
      for (var i = 0; i < datePicker.daycells.length; i++) {
        if ((i + 1) > disableIndex) {
          datePicker.setClassOnDate(i, "disabled", "firstCalender", false);
        }
      }
    },

    disablePastDatesByIndex: function (index) {
      var datePicker = this;
      var leftArrow = dojo.query(".prev", datePicker.datepickerDOM)[0];
      dojo.addClass(leftArrow, "disabled");
      var disableIndex = index || 0;
      for (var i = 0; i < datePicker.daycells.length; i++) {
        if ((i + 1) < disableIndex) {
          datePicker.setClassOnDate(i, "disabled", "firstCalender", false);
        }
      }
    },

    disableSecondMonthDatesByIndex: function (index) {
      // summary: Disables next month datepicker date based on the given index number.
      var datePicker = this;
      var disableIndex = index || 0;
      for (var i = 0; i < datePicker.nextMonthdaycells.length; i++) {
        if ((i + 1) > disableIndex) {
          datePicker.setClassOnDate(i, "disabled", "secondCalender", false);
        }
      }
    },

    disableSecondMonthPastDatesByIndex: function (index) {
      var datePicker = this;
      var disableIndex = index || 0;
      for (var i = 0; i < datePicker.nextMonthdaycells.length; i++) {
        if ((i + 1) < disableIndex) {
          datePicker.setClassOnDate(i, "disabled", "secondCalender", false);
        }
      }
    },

    disableUnavailableDates: function (date, past) {
      // summary:
      // Determines if datepicker needs to be disable
      // past or dates in the future, from the given date value.
      var datePicker = this;
      //First Month Calendar
      if (datePicker.datePickerShowDate.getMonth() === date.getMonth() &&
          datePicker.datePickerShowDate.getFullYear() === date.getFullYear()) {
        var index = date.getDate();
        if (!past) {
          datePicker.disableDatesByIndex(index);
        } else {
          datePicker.disablePastDatesByIndex(index);
        }
      } else if (!past && datePicker.datePickerShowDate > date) {
        datePicker.disableDatesByIndex();
      } else if (past && datePicker.datePickerShowDate < date) {
        // TODO: using disablePastDatesByIndex() ??
        datePicker.disableDatesByIndex();
      }

      //Second Month Calendar
      if (datePicker.datePickerNextMonthShowDate.getMonth() === date.getMonth() &&
          datePicker.datePickerNextMonthShowDate.getFullYear() === date.getFullYear()) {
        var secondMonthIndex = date.getDate();
        if (!past) {
          datePicker.disableSecondMonthDatesByIndex(secondMonthIndex);
        } else {
          datePicker.disableSecondMonthPastDatesByIndex(secondMonthIndex);
        }
      } else if (!past && datePicker.datePickerNextMonthShowDate > date) {
        datePicker.disableSecondMonthDatesByIndex();
      } else if (past && datePicker.datePickerNextMonthShowDate < date) {
        // TODO: using disablePastDatesByIndex() ??
        datePicker.disableSecondMonthDatesByIndex();
      }
    },

    subscribetotopic: function () {
      var datePicker = this;
      dojo.subscribe("tui/widget/datepicker/DateSelectOption/onclick", function (dateSelectOption, select) {
        if (dojo.query(select).parents(".calendar")[0] === datePicker.datepickerDOM) {
          datePicker.clearDatePickerTimer();
        }
      });

      datePicker.subscribeDateSelectOption();
    },

    subscribeDateSelectOption: function () {
      // summary: subscribe to selectOption and redraw datepicker onchange
      var datePicker = this;
      datePicker.dateSelectOptionChannel = dojo.subscribe("tui/widget/datepicker/DateSelectOption/onchange", function (selectoption, select, newdata) {
        if (dojo.query(select).parents(".calendar")[0] === datePicker.datepickerDOM) {
          var month = parseInt(newdata.value.split('/')[0], 10) - 1;
          var year = parseInt(newdata.value.split('/')[1], 10);
          datePicker.datePickerShowDate.setMonth(month);
          datePicker.datePickerShowDate.setFullYear(year);

          //Season End Date Validation
          datePicker.datePickerShowDate.setDate(parseInt(datePicker.totalDays[datePicker.datePickerShowDate.getMonth()], 10));
          if(datePicker.datePickerShowDate.valueOf() >= datePicker.seasonEndDate.valueOf()){
        	  datePicker.datePickerShowDate.setDate(1);
        	  datePicker.datePickerShowDate.setMonth(datePicker.datePickerShowDate.getMonth()-1);
        	  month = month-1;
          }else{
        	  datePicker.datePickerShowDate.setDate(1);
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

          datePicker.datePickerNextMonthShowDate.setMonth(secondMonth);
          if(secondMonthYear !== null) {
        	  datePicker.datePickerNextMonthShowDate.setFullYear(secondMonthYear);
          }

          // TODO: this is not needed in regular datepicker
          datePicker.skipMonth = false;
          datePicker.skipNextMonth = false;
          datePicker.redrawDatePicker(null);
        }
      });
    },

    subscribeMonthBarSelectOption: function () {
        // summary: subscribe to selectOption and redraw datepicker on select month @ month bar
        var datePicker = this;
        dojo.subscribe("tui/widget/datepicker/MonthBarSelectOption/onchange", function (month, year) {


            dojo.query("div.monthSelector").empty();
            dojo.style(dojo.query(".departuredate-selector")[0], {
            	"display":"block"
            });

            datePicker.datePickerShowDate.setMonth(month);
            datePicker.datePickerShowDate.setFullYear(year);

            //Season End Date Validation
            datePicker.datePickerShowDate.setDate(parseInt(datePicker.totalDays[datePicker.datePickerShowDate.getMonth()], 10));
            if(datePicker.datePickerShowDate.valueOf() >= datePicker.seasonEndDate.valueOf()){
          	  datePicker.datePickerShowDate.setDate(1);
          	  datePicker.datePickerShowDate.setMonth(datePicker.datePickerShowDate.getMonth()-1);
          	  month = month-1;
            }else{
          	  datePicker.datePickerShowDate.setDate(1);
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

            datePicker.datePickerNextMonthShowDate.setMonth(secondMonth);
            if(secondMonthYear !== null) {
          	  datePicker.datePickerNextMonthShowDate.setFullYear(secondMonthYear);
            }
            datePicker.skipMonth = false;
            datePicker.skipNextMonth = false;
            datePicker.redrawDatePicker(null);
        });
      },

    unsubscribeDateSelectOption: function () {
      // summary: unsubscribe from select option
      var datePicker = this;
      datePicker.dateSelectOptionChannel.remove();
    },

    open: function () {
      var datePicker = this;
      dojo.addClass(dojo.byId("when"),"border-sel-active");
      dojo.publish("tui/widget/datepicker/flights/DatePicker/open", [datePicker]);

      !datePicker.isPositioned ? datePicker.posElement(datePicker.datepickerDOM) : null;
      datePicker.isPositioned = true;
      datePicker.showWidget(datePicker.datepickerDOM);
      dojo.addClass(datePicker.domNode, "focus");

      datePicker._resizeConnect = datePicker.connect(window, "onresize", function () {
        datePicker.resize();
      });
    },

    resize: function () {
      var datePicker = this;
      if (datePicker.datepickerDOM) {
        if (datePicker.isShowing(datePicker.datepickerDOM)) datePicker.posElement(datePicker.datepickerDOM);
      }
    },

    close: function () {
      var datePicker = this;
      dojo.removeClass(dojo.byId("when"),"border-sel-active");
      dojo.publish("tui/widget/datepicker/flights/DatePicker/close", [datePicker]);
      datePicker.disconnect(datePicker._resizeConnect);
      datePicker.domNode.placeholder = datePicker.datePickerPlaceholder;
      datePicker.hideWidget(datePicker.dateSelectOption[0].listElement)
      datePicker.hideWidget(datePicker.datepickerDOM);
      dojo.removeClass(datePicker.domNode, "focus")
    },

    setClassOnDate: function (index, classname, selectedmonth, remove) {
      var datePicker = this;
      if (datePicker.daycells.length === 0
    		  && datePicker.nextMonthdaycells.length === 0) return;
      remove = (remove === undefined) ? true : remove;
      if (remove) {
        dojo.query([".", classname].join(""), datePicker.datepickerDOM).removeClass(classname);
      }

      if(selectedmonth === "firstCalender") {
    	  selectedDateCell = selectedmonth;
    	  if(datePicker.daycells[index] !== undefined){
    		  dojo.addClass(datePicker.daycells[index], classname);
    	  }
      }
      else if(selectedmonth === "secondCalender") {
    	  selectedDateCell = selectedmonth;
    	  if(datePicker.nextMonthdaycells[index] !== undefined){
    		  dojo.addClass(datePicker.nextMonthdaycells[index], classname);
    	  }

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
      datePicker.daycells = dojo.query(".datepicker-day", datePicker.datepickerDOM);
      datePicker.nextMonthdaycells = dojo.query(".next-month-datepicker-day", datePicker.datepickerDOM);

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
          /*datePicker.selectedDate.setDate(1);
          datePicker.selectedDate.setMonth(datePicker.month);
          datePicker.selectedDate.setFullYear(datePicker.year);
		  datePicker.selectedDate.setDate(index + 1);*/
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


            //Ex: If the current month is Dec 2014 then prepare second month as Jan 2015.
            if(datePicker.month === 11) {
            	datePicker.selectedDate.setMonth(0);
            	datePicker.selectedDate.setFullYear(datePicker.year + 1);
            }
            else {
            	datePicker.selectedDate.setDate(1);
            	datePicker.selectedDate.setMonth(datePicker.month+1);
                datePicker.selectedDate.setFullYear(datePicker.year);
            }
             datePicker.selectedDate.setDate(index + 1);
             datePicker.setFieldValue(datePicker.selectedDate, datePicker.domNode.id);


            if (datePicker.closeOnSelection) {
              datePicker.close();
            }
            datePicker.onSelectedDate(datePicker.selectedDate);
          }));
        });
    //Next Month

      if (datePicker.templateview === "datepicker") {
        datePicker.connect(datePicker.datepickerDOM, "onclick", function (event) {
          dojo.stopEvent(event);
        });

        var leftArrow = dojo.query(".prev", datePicker.datepickerDOM)[0];
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

        var rightArrow = dojo.query(".next", datePicker.datepickerDOM)[0];
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
      var monthAndYear = [(datePicker.datePickerShowDate.getMonth() +
          1), '/', datePicker.datePickerShowDate.getFullYear()].join('');
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
      var datePicker = this;
      datePicker.templateview = "datepickercells";
      if(renderCalendar == null) {
    	  datePicker.deleteDatePicker();
          datePicker.deleteNextMonthDatePicker();
          datePicker.setup(datePicker.datePickerShowDate);
          datePicker.setupnextmonth(datePicker.datePickerNextMonthShowDate);
      }
      else if(renderCalendar == "first") {
    	  datePicker.deleteDatePicker();
    	  datePicker.setupnextmonth(datePicker.datePickerNextMonthShowDate);
      }
      else if(renderCalendar == "second") {
    	  datePicker.deleteNextMonthDatePicker();
    	  datePicker.setup(datePicker.datePickerShowDate);
      }
      datePicker.renderDatePicker();
    },

    deleteDatePicker: function () {
      var datePicker = this;
      for (var i = 0; i < datePicker.datePickerConnects; i++) {
        dojo.disconnect(datePicker.datePickerConnects[i]);
      }
      if(datePicker.datePickerConnects)
    	  datePicker.datePickerConnects.length = 0;
      if(datePicker.daycells)
    	  datePicker.daycells.length = 0;

      dojo.query(".datepickertable", datePicker.datepickerDOM).remove();
    },

    deleteNextMonthDatePicker: function () {
        var datePicker = this;
        for (var i = 0; i < datePicker.nextMonthdatePickerConnects; i++) {
          dojo.disconnect(datePicker.nextMonthdatePickerConnects[i]);
        }
        if(datePicker.nextMonthdatePickerConnects)
        	datePicker.nextMonthdatePickerConnects.length = 0;
        if(datePicker.nextMonthdaycells)
        	datePicker.nextMonthdaycells.length = 0;

        dojo.query(".second-month-datepickertable", datePicker.datepickerDOM).remove();
      },

    enableArrowBtn: function () {
      var datePicker = this;
      dojo.removeClass(dojo.query(".next", datePicker.datepickerDOM)[0], "disabled");
      dojo.removeClass(dojo.query(".prev", datePicker.datepickerDOM)[0], "disabled");

    },

    onAfterTmplRender: function (datepickerHTML) {
      var datePicker = this;
      if (datePicker.templateview === "datepicker") {
        datePicker.datepickerDOM = dojo.place(dojo.trim(datepickerHTML), document.body);
        dojo.parser.parse(datePicker.datepickerDOM);
        dojo.connect(datePicker.datepickerDOM, "onclick", function (event) {
          dojo.stopEvent(event);
          datePicker.clearDatePickerTimer();
        });
        on(datePicker.datepickerDOM,on.selector(datePicker.closeSelector, "click"), function (event) {
            dojo.stopEvent(event);
             datePicker.close();
        });
      } else {
        dojo.place(dojo.trim(datepickerHTML), datePicker.datepickerDOM);
      }

      datePicker.enableArrowBtn();
      datePicker.addDatePickerEventListener();

      dojo.style(dojo.query(".departuredate-selector")[0], {
	    	"display":"block"
	  });

      if (datePicker.datePickerShowDate.getMonth() === datePicker.now.getMonth() &&
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
      }

      var disableDate = new Date(datePicker.now.getTime());
      disableDate.setDate(disableDate.getDate() + 1);
      datePicker.disableUnavailableDates(disableDate, true);
      if (datePicker.templateview === "datepicker") {
        var customDropDown = dojo.query(".custom-dropdown", datePicker.datepickerDOM);
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

  return tui.widget.datepicker.flights.DatePicker;
});