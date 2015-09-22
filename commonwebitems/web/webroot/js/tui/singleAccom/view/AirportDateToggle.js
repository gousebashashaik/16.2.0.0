define("tui/singleAccom/view/AirportDateToggle", [
  "dojo",
  "dojo/on",
  "dojo/dom-style",
  "dojo/dom-attr",
  "dojo/dom-class",
  "tui/widget/form/ToggleSwitch"], function (dojo, on, domStyle, domAttr, domClass) {

  dojo.declare("tui.singleAccom.view.AirportDateToggle", [tui.widget.form.ToggleSwitch], {

    stateMap: {
      'date': 'on',
      'airport': 'off',
      'off': 'date',
      'on': 'airport'
    },

    airport: null,

    date: null,

    model: null,

    currentView: null,

    leftLabelTag: 'singleAccomByAirports',

    rightLabelTag: 'singleAccomByDate',

    cookieName: 'saGroupBy',

    postCreate: function () {
      var widget = this;
      var flightsDom = dojo.query('.flights')[0];
      widget.airport = dojo.query('#byAirportsResults', flightsDom)[0];
      widget.date = dojo.query('#byDatesResults', flightsDom)[0];

      widget.model = dijit.registry.byId('mediator').registerController(widget);

      widget.inherited(arguments);
    },

    onAfterToggle: function(oldValue, newValue) {
      var widget = this;
      domClass.add(widget[oldValue], 'hide');
      domClass.remove(widget[newValue], 'hide');
    },

    refresh: function () {
      var widget = this;
      widget.onAfterToggle(widget.currentView === 'airport' ? 'date' : widget.currentView, widget.currentView);
    },

    handleNoResults: function () {},

    generateRequest: function() {}

  });
  return tui.singleAccom.view.AirportDateToggle;
});