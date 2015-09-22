define("tui/searchPanel/view/SearchDatePicker", [
  "dojo",
  "dojo/store/Observable",
  "dojo/dom-attr",
  "dojo/text!tui/searchPanel/view/templates/searchDatepicker.html",
  "tui/searchPanel/store/DateStore",
  "dojo/string",
  "tui/search/nls/Searchi18nable",
  "tui/searchPanel/view/SearchErrorMessaging",
  "tui/widget/datepicker/DatePicker"], function (dojo, Observable, domAttr, searchDatePickerTmpl, DateStore) {

  function splitDestinationQuery(query) {
    return query.split(':')[0];
  }

  dojo.declare("tui.searchPanel.view.SearchDatePicker", [tui.widget.datepicker.DatePicker, tui.search.nls.Searchi18nable, tui.searchPanel.view.SearchErrorMessaging], {

    // ----------------------------------------------------------------------------- properties

    datePattern: null,

    monthsAndYears: null,

    monthAndYearTxt: null,

    seasonLength: 18,

    tmpl: searchDatePickerTmpl,

    dateStore: null,

    skipMonth: true,

    prevAvailableMonth: false,

    prevAvailableLink: false,

    nextAvailableMonth: false,

    nextAvailableLink: false,

    availabilityLinkConnects: null,

    firstDate: null,

    endDate: null,

    calClass: '',

    defaultDaysAhead: 1,

    subscribableMethods: ["onCalendarFocus", "resize", "focusCalendar"],

    // ----------------------------------------------------------------------------- methods

    postCreate: function () {
      var searchDatePicker = this;
      searchDatePicker.seasonLength = searchDatePicker.searchConfig.SEASON_LENGTH;
      searchDatePicker.datePattern = searchDatePicker.searchConfig.DATE_PATTERN;
      searchDatePicker.inherited(arguments);
      searchDatePicker.initSearchMessaging();

      searchDatePicker.firstDate = dojo.clone(searchDatePicker.datePickerShowDate);
      searchDatePicker.endDate = dojo.clone(searchDatePicker.datePickerShowDate);
      searchDatePicker.endDate.setMonth(searchDatePicker.firstDate.getMonth() - 1 + searchDatePicker.seasonLength);
      searchDatePicker.endDate.setDate(parseInt(searchDatePicker.totalDays[searchDatePicker.endDate.getMonth()], 10));

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
        searchDatePicker.setFieldValue(searchDatePicker.searchMessaging.date.placeholder);
        searchDatePicker.onCalendarFocus();
      });

      dojo.subscribe("tui/searchPanel/searchOpening", function (component) {
        if (!searchDatePicker.datepickerDOM) return;
        if (component !== searchDatePicker && searchDatePicker.isShowing(searchDatePicker.datepickerDOM)) {
          searchDatePicker.close();
        }
      });
      searchDatePicker.tagElement(searchDatePicker.domNode, "when");
    },

    resetPlaceHolder: function () {
      var searchDatePicker = this;
      dojo.query(searchDatePicker.domNode).text(searchDatePicker.searchMessaging.date.placeholder);
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
        arrow: true,
        field: "when",
        key: "emptyDate"
      });
    },

    open: function () {
      // summary:
      //		Extends default method to call routeMessage method
      var searchDatePicker = this;
      searchDatePicker.inherited(arguments);
      searchDatePicker.showRouteMessage(searchDatePicker.templateview);
    },

    showRouteMessage: function (templateview) {
      // summary:
      //		Show message if valid route selected and not in "no availability" mode
      var searchDatePicker = this;
      if ((templateview !== "noavailability") && (searchDatePicker.searchPanelModel.to.query().total > 0 ||
          searchDatePicker.searchPanelModel.from.selectedSize > 0)) {
        dojo.query('.route-message').text(searchDatePicker.searchMessaging.date.routeMessage).removeClass('hidden');
      } else {
        dojo.query('.route-message').addClass('hidden');
      }
    },

    redrawDatePicker: function () {
      // summary:
      //		Extends default method to show message if valid route selected
      var searchDatePicker = this;
      searchDatePicker.inherited(arguments);
      searchDatePicker.showRouteMessage(searchDatePicker.templateview);
    },

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

    onCalendarFocus: function (searchApi) {
      // summary:
      //		Extends default method so datepicker will wait for data before proceeding
      var searchDatePicker = this;
      var args = arguments;

      // if method is invoked through publish/subscribe technic checking whether main-search panel invoked or mini-search panel invoked using searchApi argument
      // and if not through publish/subscribe technic then searchApi argument will be null.
      if(searchApi == searchDatePicker.widgetController.searchApi || searchApi == null)
	  {
      dojo.addClass(searchDatePicker.domNode, "loading");
      dojo.when(searchDatePicker.dateStore.requestData(searchDatePicker.searchPanelModel), function () {
        if (searchDatePicker.datepickerDOM) {
          searchDatePicker.redrawDatePicker();
          searchDatePicker.setDateSelectOption();
        }
        dojo.removeClass(searchDatePicker.domNode, "loading");
        searchDatePicker.inherited(args);
      });
	  }
    },

    onBeforeTmplRender: function () {
      // summary:
      //		Extends default method so datepicker will change to next month with available dates on render
      var searchDatePicker = this;
      dojo.query('.available-message', searchDatePicker.datepickerDOM).remove();
      if (searchDatePicker.availabilityLinkConnects && searchDatePicker.availabilityLinkConnects.length > 0) {
        for (var i = 0; i < searchDatePicker.availabilityLinkConnects; i++) {
          dojo.disconnect(searchDatePicker.availabilityLinkConnects[i]);
        }
      }
      if (!searchDatePicker.validDatesTest(searchDatePicker.datePickerShowDate, searchDatePicker.getStoredDates())) {
        if (searchDatePicker.skipMonth) {
          var date = searchDatePicker.findAvailableMonth(searchDatePicker.datePickerShowDate, "forward") ?
              searchDatePicker.findAvailableMonth(searchDatePicker.datePickerShowDate, "forward").split('-') :
              false;
          if (date) {
            searchDatePicker.datePickerShowDate.setMonth(date[0] - 1);
            searchDatePicker.datePickerShowDate.setFullYear(date[1]);
            searchDatePicker.monthAndYearTxt = [searchDatePicker.months[date[0] -
                1], ' ', searchDatePicker.datePickerShowDate.getFullYear()].join('');
            searchDatePicker.setup(searchDatePicker.datePickerShowDate);
          }
        } else {
          searchDatePicker.renderAvailabilityMessage();
        }
      } else {
        if (searchDatePicker.dateSelectOption.length > 0) {
          searchDatePicker.unsubscribeDateSelectOption();
          searchDatePicker.dateSelectOption[0].setSelectedValue([searchDatePicker.datePickerShowDate.getMonth() +
              1, '/', searchDatePicker.datePickerShowDate.getFullYear()].join(''));
          searchDatePicker.subscribeDateSelectOption();
        }
      }

      searchDatePicker.skipMonth = true;
    },

    renderAvailabilityMessage: function () {
      // summary:
      //		Renders message and links if entire month is unavailable
      var searchDatePicker = this;
      var curMonth = dojo.clone(searchDatePicker.datePickerShowDate);

      searchDatePicker.monthAndYearTxt = [searchDatePicker.months[searchDatePicker.datePickerShowDate.getMonth()], ' ', searchDatePicker.datePickerShowDate.getFullYear()].join('');

      var forwardDate = searchDatePicker.findAvailableMonth(curMonth, "forward");
      var reverseDate = searchDatePicker.findAvailableMonth(curMonth);

      // current month to render is first month in season range
      if (curMonth.valueOf() === searchDatePicker.firstDate.valueOf()
          && curMonth.valueOf() !== searchDatePicker.endDate.valueOf()
          && forwardDate) {
        searchDatePicker.prevAvailableMonth = false;
        searchDatePicker.prevAvailableLink = null;
        searchDatePicker.nextAvailableMonth = true;
        searchDatePicker.nextAvailableLink = forwardDate;
      }
      // current month to render is last month in season range
      else if (curMonth.valueOf() !== searchDatePicker.firstDate.valueOf()
          && curMonth.valueOf() === searchDatePicker.endDate.valueOf()
          && reverseDate) {
        searchDatePicker.prevAvailableMonth = true;
        searchDatePicker.prevAvailableLink = reverseDate;
        searchDatePicker.nextAvailableMonth = false;
        searchDatePicker.nextAvailableLink = null;
      }
      // current month is between first and last month in season range
      else {
        if (forwardDate) {
          searchDatePicker.nextAvailableMonth = true;
          searchDatePicker.nextAvailableLink = forwardDate;
        }
        if (reverseDate) {
          searchDatePicker.prevAvailableMonth = true;
          searchDatePicker.prevAvailableLink = reverseDate;
        }
      }

      searchDatePicker.deleteDatePicker();
      searchDatePicker.templateview = "noavailability";
      searchDatePicker.showRouteMessage(searchDatePicker.templateview);
    },

    attachAvailabilityListeners: function () {
      // summary:
      //    attach event listeners for prev/next available month links
      var searchDatePicker = this;
      searchDatePicker.availabilityLinkConnects = [];
      _.forEach(dojo.query('.availability-link', searchDatePicker.datepickerDOM), function (item, index) {
        searchDatePicker.tagElement(item, index === 0 ? "Show Last Date" : "Show Next Date");
        if (!dojo.hasClass(item, 'disabled')) {
          searchDatePicker.availabilityLinkConnects.push(searchDatePicker.connect(item, "onclick", function (event) {
            var month = parseInt(domAttr.get(item, 'data-link').split('-')[0], 10) - 1;
            var year = parseInt(domAttr.get(item, 'data-link').split('-')[1], 10);
            searchDatePicker.datePickerShowDate.setMonth(month);
            searchDatePicker.datePickerShowDate.setFullYear(year);
            searchDatePicker.skipMonth = false;
            searchDatePicker.redrawDatePicker();
          }));
        }
      });
    },

    findAvailableMonth: function (date, direction) {
      // summary:
      //    find next/previous month with available dates
      var searchDatePicker = this;
      direction = direction || "reverse";
      if (direction == "forward") {
        date = dojo.clone(date);
        date.setMonth(date.getMonth() + 1);
        while (date.valueOf() <= searchDatePicker.endDate.valueOf()) {
          if (searchDatePicker.validDatesTest(date, searchDatePicker.getStoredDates())) {
            return [date.getMonth() + 1, '-', date.getFullYear()].join('');
          }
          date.setMonth(date.getMonth() + 1);
        }
      } else {
        date = dojo.clone(date);
        date.setMonth(date.getMonth() - 1);
        while (date.valueOf() >= searchDatePicker.firstDate.valueOf()) {
          if (searchDatePicker.validDatesTest(date, searchDatePicker.getStoredDates())) {
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
      var searchDatePicker = this;
      searchDatePicker.inherited(arguments);
      if (searchDatePicker.templateview === "noavailability") {
        searchDatePicker.attachAvailabilityListeners();
      } else {
        searchDatePicker.eligibleDates(searchDatePicker.getStoredDates());
      }
      if (searchDatePicker.endDate) {
        searchDatePicker.disableUnavailableDates(searchDatePicker.endDate, false);
      }
      searchDatePicker.tagElement(searchDatePicker.datepickerDOM, "datepicker");
    },

    eligibleDates: function (dateStoreData) {
      // summary:
      //   Disables non-eligible dates
      //   params: dateStoreData (Array)
      var searchDatePicker = this;
      var totalDays = searchDatePicker.totalDays[searchDatePicker.datePickerShowDate.getMonth()];
      var month = dojo.clone(searchDatePicker.datePickerShowDate);
      var now = new Date();
      var today = new Date(now.getFullYear(), now.getMonth(), now.getDate());

      for (var i = month.getDate(); i <= totalDays; i++) {
        if (_.indexOf(dateStoreData, searchDatePicker.formatDate(month)) === -1) {
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
    },

    setDateSelectOption: function () {
      // summary:
      // 		override default datepicker method, treat month/year as one item
      var searchDatePicker = this;
      var monthAndYear = [(searchDatePicker.datePickerShowDate.getMonth() +
          1), '/', searchDatePicker.datePickerShowDate.getFullYear()].join('');
      if (searchDatePicker.dateSelectOption[0].getSelectedData().value !== monthAndYear) {
        searchDatePicker.skipMonth = false;
        searchDatePicker.dateSelectOption[0].setSelectedValue(monthAndYear);
      }
    },

    getStoredDates: function () {
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
      return searchDatePicker.dateStore.get(id) ? searchDatePicker.dateStore.get(id).dates : [];
    },

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

  return tui.searchPanel.view.SearchDatePicker;
});
