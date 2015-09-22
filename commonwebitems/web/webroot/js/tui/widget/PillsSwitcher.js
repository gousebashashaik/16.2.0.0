define("tui/widget/PillsSwitcher", [
  "dojo/_base/declare",
  "dojo/_base/connect",
  "dojo/cookie",
  "dojo/topic",
  "dojo/dom-class",
  "tui/widget/_TuiBaseWidget"], function (declare, connect, cookie, topic, domClass) {

  return declare("tui.widget.PillsSwitcher", [tui.widget._TuiBaseWidget], {

    listenTo: null,

    postCreate: function () {
      // summary:
      // Attach event listeners to wigdet elements.
      var pillSwitcher = this,
          cookieValue = cookie('villaPill');

      pillSwitcher.inherited(arguments);

      if (cookieValue) {
        pillSwitcher.switchState(cookieValue);
      }

      topic.subscribe('tui:channel=pillStateChange', function(value){
        pillSwitcher.switchState(value);
        console.log('stateChange: '+ value)
      });
    },

    switchState: function(value) {
      var pillSwitcher = this,
          action = (value !== pillSwitcher.listenTo) ? 'add' : 'remove';
      domClass[action](pillSwitcher.domNode, "hide");
    }

  });
});