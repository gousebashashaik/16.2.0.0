define("tui/widget/StatefulPills", [
  "dojo",
  "dojo/_base/connect",
  "dojo/hash",
  "dojo/io-query",
  "dojo/cookie",
  "dojo/topic",
  "dojo/dom-class",
  "dojo/dom-attr",
  "dojox/fx/scroll",
  "tui/widget/mixins/ContentLoader",
  "tui/widget/Pills"], function (dojo, connect, hash, ioQuery, cookie, topic, domClass, domAttr) {

  dojo.declare("tui.widget.StatefulPills", [tui.widget.Pills], {

    postCreate: function () {
      // summary:
      // Attach event listeners to wigdet elements.
      var pills = this,
          cookieValue = cookie('villaPill');

      pills.attachTabbableEventListeners();

      if (cookieValue) {
        pills.selectStatefulTab(cookieValue);
      }

      topic.subscribe('tui:channel=pillStateChange', function(value){
        if(!pills.checkActiveTab(value)) {
          pills.selectStatefulTab(value);
        }
      });
    },

    handleTabClick: function (tabbable, event, element) {
      var pills = this,
          value = domAttr.get(element, "data-dojo-value");
      if (pills.tabStopEvent) dojo.stopEvent(event);
      pills.onTabSelect(element);
      topic.publish('tui:channel=pillStateChange', value);
      cookie('villaPill', value, { expires: 365 });
    },

    afterShowTab: function () {
      var pills = this;
      pills.checkPosition();
    },

    selectStatefulTab: function (value) {
      var pills = this,
          selectableTab = _.find(pills.tabHeadings, function(tabHeading) {
            return domAttr.get(tabHeading, "data-dojo-value") === value;
          });
        pills.showTab(selectableTab);
    },

    checkActiveTab: function (value) {
      var pills = this,
          activeTab = _.filter(pills.tabHeadings, function(tab){
            return domClass.contains(tab, 'active');
          })[0];
      return domAttr.get(activeTab, "data-dojo-value") === value;
    },

    checkPosition: function () {
      var pills = this,
          winBox = dojo.window.getBox(),
          elemPos = dojo.position(pills.domNode, false),
          padding = 20;

      if (!pills.clicked) return;

      if(elemPos.y < (0 + padding) || elemPos.y > (winBox.h - padding - 40)) {
        console.log('outside')
      }
    }

  });
  return tui.widget.StatefulPills;
});