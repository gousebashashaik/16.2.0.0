define('tui/widget/customeraccount/mybookings/ActiveDate', [
  'dojo',
  'dojo/on',
  'dojo/_base/connect',
  'dojo/dom-style',
  'dojo/dom-class',
  'dojo/dom-attr',
  'tui/widget/mobile/Widget'
], function (dojo, on, connect, domStyle, domClass, domAttr) {

  dojo.declare('tui.widget.customeraccount.mybookings.ActiveDate', [tui.widget._TuiBaseWidget], {

    postCreate: function () {
      var widget = this;
      var criteria = widget.getParent().getCriteria();
      var timeStamp = domAttr.get(widget.domNode, 'data-date-value');

      function toggleHighllight(oldDate, newDate) {
        oldDate === newDate.time().toString() ? domClass.add(widget.domNode, 'active') : domClass.remove(widget.domNode, 'active');
      }

      if (criteria.when !== null) {
        toggleHighllight(timeStamp, criteria.when);
      }
      //wait for the calendar to load to highlight saved selected date
      connect.subscribe('tui:search/view/calendarLoaded', function () {
      criteria.watch('when', function (name, oldValue, newValue) {
          newValue ? toggleHighllight(timeStamp, newValue) : '';
      });
      });

    }
  });
});