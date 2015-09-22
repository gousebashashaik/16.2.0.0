define('tui/widget/common/CalendarWidget', [
  'dojo',
  'dojo/_base/connect',
  'dojo/dom-style',
  'dojo/dom-class',
  'dojo/dom-construct',
  'dojo/dom-attr',
  'tui/common/TemplateUtil',
  'tui/widget/mobile/Widget'], function(dojo, connect, domStyle, domClass, domConstruct, domAttr, templateUtil) {

  function Month(id, template, container, activeDates, onDateSelect) {


    this.id = function() {
      return id;
    };

    this.dates = function() {
      return _.compact(_.flatten(dateMatrix));
    };

    this.firstDay = function() {
      return new JCalendar(activeDates[0].year(), activeDates[0].month(), 1);
    };

    this.activeDates = function() {
      return activeDates;
    };

    this.parse = function (container) {
        dojo.parser.parse(container);
    };

    this.show = function() {
      domConstruct.place(_dom, container, 'only');
      this.parse(container);
      connect.publish('tui:search/view/calendarLoaded');

      _.each(dojo.query('table > tbody > tr > .avail', container), function(availDate) {
        dojo.connect(availDate, 'click', function() {
          onDateSelect(domAttr.get(availDate, 'data-date-value'));
        });
      });
    };

    this.hide = function() {
      domConstruct.empty(container);
    };

    this.numberOfWeeks = function() {
      return Math.ceil((this.firstDay().numberOfDaysInMonth() + this.firstDay().dayOfWeek()) / 7);
    };

    var dateMatrix = _.matrix(this.numberOfWeeks(), 7, null);

    _.each(activeDates[0].allDatesInTheSameMonth(), function(date) {
      dateMatrix[date.week() - 1][date.dayOfWeek()] = {'value': date.dayOfMonth(), 'active': false, 'date': date.time()};
    });

    _.each(activeDates, function(activeDate) {
      dateMatrix[activeDate.week() - 1][activeDate.dayOfWeek()].active = true;
    });

    var _dom = templateUtil.render(template, {'matrix': dateMatrix, 'monthId': id});



  }

  function monthsFrom(dates, template, container, selectHandler) {
    var groupedByMonth = _.groupBy(dates, function(date) {
      return date.monthName() + ' ' + date.year();
    });

    return _.map(groupedByMonth, function(dates, monthId) {
      return new Month(monthId, template, container, dates, selectHandler);
    });

  }

  dojo.declare('tui.widget.common.CalendarWidget', [tui.widget.mobile.Widget], {

    selectionDom: null,

    months: {},

    show: function() {
      var widget = this;
      domClass.add(widget.domNode, 'show');
    },

    hide: function() {
      var widget = this;
      domClass.remove(widget.domNode, 'show');
    },

    clearMonths: function() {
      var widget = this;
      domConstruct.empty(widget.selectionDom);
    },

    updateSelectableMonths: function(months, savedMonth) {
      var widget = this;

      _.each(months, function(month, index) {
        domConstruct.place(dojo.create('option', {innerHTML: month.id(), 'value': month.id()}), widget.selectionDom);
        if(!savedMonth) {
        index == 0 ? [month.show(), dojo.query('.text', widget.selectionDom.parentNode)[0].innerHTML = month.id()] : null;
        } else {
          savedMonth.show(), dojo.query('.text', widget.selectionDom.parentNode)[0].innerHTML = savedMonth.id()
        }
        var newValue = dojo.query('.text', widget.selectionDom.parentNode)[0].innerHTML;
        dojo.query('select option', widget.domNode).forEach(function(option) {
            if (option.value === newValue) {
              dojo.attr(option, 'selected', 'selected');
            }
          });

      });

      dojo.connect(widget.selectionDom, 'onchange', function() {
        if (months[this.selectedIndex]) {
          _.each(_.without(months, months[this.selectedIndex]), function(month) {
            month.hide();
          });
          months[this.selectedIndex].show();
        }

      });
    },

    update: function(dates, savedDate) {
      var widget = this;
      var monthToParse;

      function dateSelection(d) {
        widget.dateSelected(JCalendar.fromDate(new Date(parseInt(d))));
        widget.hide();
      }

      widget.months = _.sortBy(monthsFrom(dates, widget.monthTemplate, widget.daysContainer, dateSelection), function(month) {
        return month.firstDay().time();
      });

      if(savedDate) {
        var savedDate = new Date(savedDate.nativeCalendar);
        var DateToSet = new JCalendar(savedDate.getFullYear(), savedDate.getMonth(), savedDate.getDate());
        widget.activeDates = [];
        _.each(widget.months, function(month){
          _.map(month.activeDates(), function(date) {
            widget.activeDates.push(date);
          })
          var monthYear = [DateToSet.monthName(), DateToSet.year()].join(' ');
          monthToParse =_.find(widget.months, function(month){
            return month.id() == monthYear;
          });
        })
      }


      widget.clearMonths();
      widget.updateSelectableMonths(widget.months, monthToParse);
      //default date selection
      //widget.dateSelected(_.first(_.first(months).activeDates()));
      return widget;
    },

    postCreate: function() {
      var widget = this;
      widget.daysContainer = dojo.query('.days', widget.domNode)[0];
      widget.selectionDom = dojo.query('.controls > .select > select', widget.domNode)[0];

      dojo.query('.close', widget.domNode).connect('onclick', function(e) {
        dojo.stopEvent(e);
        widget.hide();
      });


      widget.inherited(arguments);
    },

    dateSelected: function(date) {

    }
  });
});
