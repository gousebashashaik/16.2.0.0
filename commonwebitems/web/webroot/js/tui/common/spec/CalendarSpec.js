describe('Calendar specification', function() {
  var JCalendar;

  beforeEach(function() {
    JCalendar = dojo.require('tui/common/JCalendar');
  });

  it('Should be loaded', function() {
    expect(JCalendar).not.toBe(null);
  });

  it("Should create today's date", function() {
    var today = new JCalendar();
    var expected = new Date();
    expect(today.dayOfMonth()).toBe(expected.getDate());
    expect(today.month()).toBe(expected.getMonth());
    expect(today.year()).toBe(expected.getFullYear());
  });

  it('Should find if two calendars are of same day', function() {
    var c1 = new JCalendar();
    var c2 = new JCalendar();
    expect(c1.isSameDay(c2)).toBe(true);
  });

  it('Should find the next day', function() {
    var c = new JCalendar();
    expect(c.nextDay().dayOfMonth()).toBe((new Date()).getDate() + 1);
  });

  it('Should find the next month', function() {
    var c = new JCalendar();
    expect(c.nextMonth().month()).toBe((new Date()).getMonth() + 1);
  });

  it('Should Be able to check the day of the week explicitly', function() {
    var sunday = new JCalendar(2013, 5, 2);
    var monday = new JCalendar(2013, 5, 3);
    var tuesday = new JCalendar(2013, 5, 4);
    var wednesday = new JCalendar(2013, 5, 5);
    var thursday = new JCalendar(2013, 5, 6);
    var friday = new JCalendar(2013, 5, 7);
    var saturday = new JCalendar(2013, 5, 8);
    expect(sunday.isSunday()).toBe(true);
    expect(monday.isMonday()).toBe(true);
    expect(tuesday.isTuesday()).toBe(true);
    expect(wednesday.isWednesday()).toBe(true);
    expect(thursday.isThursday()).toBe(true);
    expect(friday.isFriday()).toBe(true);
    expect(saturday.isSaturday()).toBe(true);
  });


  it('Should find the name of the day', function() {
    var sunday = new JCalendar(2013, 5, 2);
    var monday = new JCalendar(2013, 5, 3);
    var tuesday = new JCalendar(2013, 5, 4);
    var wednesday = new JCalendar(2013, 5, 5);
    var thursday = new JCalendar(2013, 5, 6);
    var friday = new JCalendar(2013, 5, 7);
    var saturday = new JCalendar(2013, 5, 8);
    expect(sunday.dayName()).toBe('Sunday');
    expect(monday.dayName()).toBe('Monday');
    expect(tuesday.dayName()).toBe('Tuesday');
    expect(wednesday.dayName()).toBe('Wednesday');
    expect(thursday.dayName()).toBe('Thursday');
    expect(friday.dayName()).toBe('Friday');
    expect(saturday.dayName()).toBe('Saturday');
  });


  it('Should find the name of the month', function() {
    var jan = new JCalendar(2013, 0, 2);
    var feb = new JCalendar(2013, 1, 3);
    var march = new JCalendar(2013, 2, 4);
    var april = new JCalendar(2013, 3, 5);
    var may = new JCalendar(2013, 4, 6);
    var june = new JCalendar(2013, 5, 7);
    var july = new JCalendar(2013, 6, 8);
    expect(jan.monthName()).toBe('January');
    expect(feb.monthName()).toBe('February');
    expect(march.monthName()).toBe('March');
    expect(april.monthName()).toBe('April');
    expect(may.monthName()).toBe('May');
    expect(june.monthName()).toBe('June');
    expect(july.monthName()).toBe('July');
  });

});
