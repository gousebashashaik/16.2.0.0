define('tui/widget/customeraccount/mybookings/DepartureDateCalendar', [
  'dojo',
  'dojo/dom-class',
  'dojo/text!tui/widget/customeraccount/mybookings/templates/month.html',
  'dojo/on',
  'tui/widget/customeraccount/mybookings/ActiveDate',
  'tui/widget/common/CalendarWidget'], function (dojo, domClass, template) {


  dojo.declare('tui.widget.customeraccount.mybookings.DepartureDateCalendar', [tui.widget.common.CalendarWidget], {

    monthTemplate: template,


    attachEvents: function () {
      var widget = this;
      dojo.query('.flexible-date > .radio', widget.domNode).connect('onclick', function (e) {
        dojo.stopEvent(e);
        domClass.toggle(e.target, 'active');
        widget.criteria.set('flexible', domClass.contains(e.target, 'active'));
      });

      var monthSelect = dojo.query('.select', widget.domNode)[0];
      widget.monthSelectTarget = dojo.query('.text', monthSelect)[0];
      var monthSelectNative = dojo.query('select', monthSelect)[0];
      dojo.connect(monthSelectNative, 'onchange', function (e) {
        widget.monthSelectTarget.innerHTML = e.target.value;
        dojo.query('select option', widget.domNode).forEach(function(option) {
            var newValue = widget.monthSelectTarget.innerHTML;
        	  if (option.value === newValue) {
        	    dojo.attr(option, 'selected', 'selected');
        	  }
        	});
      });
    },

    postCreate: function () {
      var widget = this;
      widget.criteria = widget.getParent().getCriteria();
      widget.attachEvents();
      widget.getParent().registerCalendarWidget(widget);
      widget.inherited(arguments);
    },

    show: function () {
      var widget = this;
      domClass.add(widget.domNode.parentNode, 'show');
      widget.inherited(arguments);
    },

    hide: function () {
      var widget = this;
      domClass.remove(widget.domNode.parentNode, 'show');
      widget.inherited(arguments);
    },

    dateSelected: function (date) {
      var widget = this;
      widget.criteria.set('when', date);
    },

    getCriteria: _.once(function() {
      var widget = this;
      return widget.getParent().getCriteria();
    })

  });
});
