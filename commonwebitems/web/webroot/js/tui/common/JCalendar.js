define('tui/common/JCalendar', [], function (){

  function clone(source){
    return new Date(source.getTime());
  }


  function JCalendarIterator(current){
    this.current = current;
  }

  JCalendarIterator.prototype.next = function (){
    return this.current.nextDay();
  };

  var JCalendar = function (year, month, dayOfMonth, hours, minutes, seconds, milliseconds){
    var date = this.nativeCalendar = new Date();
    date.setDate(1);
    _.isPositive(year) ? date.setFullYear(year) : null;
    _.isPositive(month) ? date.setMonth(month) : null;
    _.isPositive(dayOfMonth) ? date.setDate(dayOfMonth) : null;
    _.isPositive(milliseconds) ? date.setMilliseconds(milliseconds) : date.setMilliseconds(0);
    _.isPositive(seconds) ? date.setSeconds(seconds) : date.setSeconds(0);
    _.isPositive(minutes) ? date.setMinutes(minutes) : date.setMinutes(0);
    _.isPositive(hours) ? date.setHours(hours) : date.setHours(0);
  };


  JCalendar.fromDate = function (date){
    var calendar = new JCalendar();
    calendar.nativeCalendar = clone(date);
    return calendar;
  };

  JCalendar.parse = function (str, format){

  };

  JCalendar.prototype.dayOfMonth = function (){
    return this.nativeCalendar.getDate();
  };

  JCalendar.prototype.dayOfWeek = function (){
    return this.nativeCalendar.getDay();
  };

  JCalendar.prototype.week = function (){
    var firstDay = new Date(this.year(), this.month(), 1).getDay();
    return Math.ceil((this.dayOfMonth() + firstDay) / 7);
  };


  JCalendar.prototype.allDatesInTheSameMonth = function (){
    var calendar = this;
    return _.map(_.range(1, calendar.numberOfDaysInMonth() + 1), function (day){
      return new JCalendar(calendar.year(), calendar.month(), day);
    });
  };

  JCalendar.prototype.dayName = function (){
    return JCalendar.DAYS_NAMES[this.nativeCalendar.getDay()];
  };

  JCalendar.prototype.time = function (){
    return this.nativeCalendar.getTime();
  };

  JCalendar.prototype.month = function (){
    return this.nativeCalendar.getMonth();
  };

  JCalendar.prototype.zeroPaddedMonth = function (){
    var month = _.inc(this.nativeCalendar.getMonth());
    if (month < 10) {
      return '0' + month;
    } else {
      return month;
    }
  };

  JCalendar.prototype.zeroPaddedDayOfMonth = function (){
    var dayOfMonth = this.nativeCalendar.getDate();
    if (dayOfMonth < 10) {
      return '0' + dayOfMonth;
    } else {
      return dayOfMonth;
    }
  };

  JCalendar.prototype.monthName = function (){
    return JCalendar.MONTHS_NAMES[this.nativeCalendar.getMonth()];
  };

  JCalendar.prototype.year = function (){
    return this.nativeCalendar.getFullYear();
  };

  JCalendar.prototype.isSameDay = function (calendar){
    var lhs = this;
    var rhs = calendar;
    return lhs.dayOfMonth() === rhs.dayOfMonth() && lhs.month() === rhs.month() && lhs.year() === rhs.year();
  };

  JCalendar.prototype.isSameMonth = function (calendar){
    var lhs = this;
    var rhs = calendar;
    return lhs.month() === rhs.month() && lhs.year() === rhs.year();
  };

  JCalendar.prototype.numberOfDaysInMonth = function (){
    var calendar = this;
    return new Date(calendar.year(), calendar.month() + 1, 0).getDate();
  };

  JCalendar.prototype.nextDay = function (){
    var date = clone(this.nativeCalendar);
    date.setDate(date.getDate() + 1);
    return JCalendar.fromDate(date);
  };


  JCalendar.prototype.nextMonth = function (){
    var date = clone(this.nativeCalendar);
    date.setMonth(date.getMonth() + 1);
    return JCalendar.fromDate(date);
  };

  JCalendar.prototype.isSunday = function (){
    return this.nativeCalendar.getDay() == JCalendar.DAYS.SUNDAY;
  };

  JCalendar.prototype.isMonday = function (){
    return this.nativeCalendar.getDay() == JCalendar.DAYS.MONDAY;
  };
  JCalendar.prototype.isTuesday = function (){
    return this.nativeCalendar.getDay() == JCalendar.DAYS.TUESDAY;
  };
  JCalendar.prototype.isWednesday = function (){
    return this.nativeCalendar.getDay() == JCalendar.DAYS.WEDNESDAY;
  };
  JCalendar.prototype.isThursday = function (){
    return this.nativeCalendar.getDay() == JCalendar.DAYS.THURSDAY;
  };
  JCalendar.prototype.isFriday = function (){
    return this.nativeCalendar.getDay() == JCalendar.DAYS.FRIDAY;
  };
  JCalendar.prototype.isSaturday = function (){
    return this.nativeCalendar.getDay() == JCalendar.DAYS.SATURDAY;
  };


  JCalendar.prototype.toString = function (){
    //return [_.capitalize(this.dayName()), _.ordinal(this.dayOfMonth()), _.capitalize(this.monthName()), this.year()].join(' ');
    return [_.capitalize(this.dayName().slice(0, 3)), _.interpose([this.dayOfMonth(), _.capitalize(this.monthName().slice(0, 3)), this.year()], ' ')].join(' ');
  };

  JCalendar.DAYS_NAMES = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];

  JCalendar.DAYS = {
    'SUNDAY': 0,
    'MONDAY': 1,
    'TUESDAY': 2,
    'WEDNESDAY': 3,
    'THURSDAY': 4,
    'FRIDAY': 5,
    'SATURDAY': 6
  };

  JCalendar.MONTHS_NAMES = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];


  JCalendar.MONTHS = {
    JANUARY: 0,
    FEBRUARY: 1,
    MARCH: 2,
    APRIL: 3,
    MAY: 4,
    JUNE: 5,
    JULY: 6,
    AUGUST: 7,
    SEPTEMBER: 8,
    OCTOBER: 9,
    NOVEMBER: 10,
    DECEMBER: 11
  };
  return window.JCalendar = JCalendar;
});
