define("tui/widget/datepicker/DatePicker", [
  "dojo",
  "dojo/text!tui/widget/datepicker/templates/datepicker.html",
  "dijit/registry",
  "dojo/query",
  "dojo/NodeList-traverse",
  "dojo/date/locale",
  "tui/widget/mixins/FloatPosition",
  "tui/widget/_TuiBaseWidget",
  "tui/widget/datepicker/DateSelectOption",
  "tui/widget/mixins/Templatable"], function (dojo, datePickerTmpl, registry, query) {

  dojo.declare("tui.widget.datepicker.DatePicker", [tui.widget._TuiBaseWidget, tui.widget.mixins.FloatPosition, tui.widget.mixins.Templatable], {


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
    days: ["M", "T", "W", "T", "F", "S", "S"],

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

    // ---------------------------------------------------------------- floatPosition properties

    // posOffset: Object
    //		Offset the top, and left position, of datepicker popup.
    posOffset: {top: 8, left: 0},

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
      datePicker.combineMonthsAndYears();
      datePicker.attachEvents();
      datePicker.subscribetotopic();

      // attaches on focus event to dom node.
      datePicker.connect(document.body, "onclick", function (event) {
        if (document.activeElement === datePicker.domNode) return;
        if (!datePicker.datepickerDOM || !datePicker.isShowing(datePicker.datepickerDOM)) return;
        if(event.target.className === "datepicker") return;

        datePicker.close();
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
        if (datePicker.datePickerShowDate.getTime() !== showDate.getTime()) {
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
      var endDate = datePicker.now.getDate();
      var endMonth = datePicker.now.getMonth();

      // datepicker calculations start from current month so set seasonLength default to yearRange * 12 - months elapsed this year
      var seasonLength = datePicker.seasonLength ||
          (datePicker.yearRange * 12) - (datePicker.datePickerShowDate.getMonth() + 1);

      datePicker.monthsAndYears = [];

      // get text value of current month and year for template
      datePicker.monthAndYearTxt = [datePicker.months[curMonth - 1], ' ', datePicker.years[curYear -
          datePicker.year]].join('');
      if (endMonth === 11 && endDate === 31) {
    	  datePicker.year = datePicker.year - 1;
	  }
      // create array of combined months and years
      // TODO: see why BAU has <= seasonLength
      for (var i = 0; i < seasonLength; i++) {

        datePicker.monthsAndYears.push({
          value: [curMonth, '/', curYear].join(''),
          label: [datePicker.months[curMonth - 1], ' ', datePicker.years[curYear - datePicker.year]].join('')
        });
        if (curMonth % 12 !== 0) {
          curMonth++;
        } else {
          curMonth = 1;
          curYear++;
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
      datePicker.year = datePicker.datePickerShowDate.getFullYear();
      datePicker.monthTxt = datePicker.months[datePicker.month];
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

    getSelectedDate: function () {
      var datePicker = this;
      return datePicker.selectedDate;
    },

    isLeapYear: function (year) {
      return ((year % 100 !== 0) && (year % 4 === 0) || (year % 400 === 0));
    },

    disableDatesByIndex: function (index) {
      // summary:
      // 	 Disables datepicker date based on the given index number.
      var datePicker = this;
      var rightArrow = dojo.query(".next", datePicker.datepickerDOM)[0];
      dojo.addClass(rightArrow, "disabled");
      var disableIndex = index || 0;
      for (var i = 0; i < datePicker.daycells.length; i++) {
        if ((i + 1) > disableIndex) {
          datePicker.setClassOnDate(i, "disabled", false);
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
          datePicker.setClassOnDate(i, "disabled", false);
        }
      }
    },

    disableUnavailableDates: function (date, past) {
      // summary:
      // 		Determines if datepicker needs to be disable
      //		past or dates in the future, from the given date value.
      var datePicker = this;
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
      // summary:
      // 		subscribe to selectOption and redraw datepicker onchange
      var datePicker = this;
      datePicker.dateSelectOptionChannel = dojo.subscribe("tui/widget/datepicker/DateSelectOption/onchange", function (selectoption, select, newdata) {
        if (dojo.query(select).parents(".calendar")[0] === datePicker.datepickerDOM) {
          var month = parseInt(newdata.value.split('/')[0], 10) - 1;
          var year = parseInt(newdata.value.split('/')[1], 10);
          datePicker.datePickerShowDate.setMonth(month);
          datePicker.datePickerShowDate.setFullYear(year);
          // TODO: this is not needed in regular datepicker
          datePicker.skipMonth = false;
          datePicker.redrawDatePicker();
        }
      });
    },

    unsubscribeDateSelectOption: function () {
      // summary:
      // 		unsubscribe from select option
      var datePicker = this;
      datePicker.dateSelectOptionChannel.remove();
    },

    open: function () {
      var datePicker = this;

      dojo.publish("tui/widget/datepicker/DatePicker/open", [datePicker]);
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
      dojo.publish("tui/widget/datepicker/DatePicker/close", [datePicker]);
      datePicker.disconnect(datePicker._resizeConnect);
      datePicker.domNode.placeholder = datePicker.datePickerPlaceholder;
      datePicker.hideWidget(datePicker.datepickerDOM);
      dojo.removeClass(datePicker.domNode, "focus")
    },

    setClassOnDate: function (index, classname, remove) {
      var datePicker = this;
      if (datePicker.daycells.length === 0) return;
      remove = (remove === undefined) ? true : remove;
      if (remove) {
        dojo.query([".", classname].join(""), datePicker.datepickerDOM).removeClass(classname);
      }
      dojo.addClass(datePicker.daycells[index], classname);
    },

    renderDatePicker: function () {
      var datePicker = this;
      datePicker.renderTmpl(datePicker.tmpl);
    },

    getFieldValue: function () {
      // summary:
      //    Get current value of datepicker field.
      var datePicker = this;
      return (datePicker.domNode.tagName.toLowerCase() === 'input') ?
          datePicker.domNode.value : dojo.query(datePicker.domNode).text();
    },

    setFieldValue: function (newdate) {
      // summary:
      // 		Set date picker field with selected value.
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
      dojo.query(datePicker.domNode).text(date);
    },

    addDatePickerEventListener: function () {
      var datePicker = this;
      datePicker.daycells = dojo.query(".datepicker-day", datePicker.datepickerDOM);
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
          datePicker.setClassOnDate(index, "selected");
          datePicker.selectedDate.setDate(index + 1);
          datePicker.selectedDate.setMonth(datePicker.month);
          datePicker.selectedDate.setFullYear(datePicker.year);
          datePicker.setFieldValue(datePicker.selectedDate);

          if (datePicker.closeOnSelection) {
            datePicker.close();
          }
          datePicker.onSelectedDate(datePicker.selectedDate);
        }));
      });

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

    redrawDatePicker: function () {
      var datePicker = this;
      datePicker.templateview = "datepickercells";
      datePicker.deleteDatePicker();
      datePicker.setup(datePicker.datePickerShowDate);
      datePicker.renderDatePicker();
    },

    deleteDatePicker: function () {
      var datePicker = this;
      for (var i = 0; i < datePicker.datePickerConnects; i++) {
        dojo.disconnect(datePicker.datePickerConnects[i]);
      }
      datePicker.datePickerConnects.length = 0;
      datePicker.daycells.length = 0;
      dojo.query(".datepickertable", datePicker.datepickerDOM).remove();
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
      } else {
        dojo.place(dojo.trim(datepickerHTML), datePicker.datepickerDOM);
      }

      datePicker.enableArrowBtn();
      datePicker.addDatePickerEventListener();

      if (datePicker.datePickerShowDate.getMonth() === datePicker.now.getMonth() &&
          datePicker.datePickerShowDate.getFullYear() === datePicker.now.getFullYear()) {
        var index = datePicker.now.getDate();
        datePicker.setClassOnDate(index - 1, "today");
      }

      if (datePicker.selectedDate) {
        if (datePicker.datePickerShowDate.getMonth() === datePicker.selectedDate.getMonth() &&
            datePicker.datePickerShowDate.getFullYear() === datePicker.selectedDate.getFullYear()) {
          var index = datePicker.selectedDate.getDate();
          datePicker.setClassOnDate(index - 1, "selected");
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
    },

    onSelectedDate: function (date) {
    }
  });

  return tui.widget.datepicker.DatePicker;
});