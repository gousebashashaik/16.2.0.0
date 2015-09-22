define("tui/widget/form/ToggleSwitch", [
  "dojo",
  "dojo/_base/declare",
  "dojo/topic",
  "dojo/on",
  "dojo/cookie",
  "dojo/dom-style",
  "dojo/dom-attr",
  "dojo/dom-class",
  "tui/widget/_TuiBaseWidget"], function (dojo, declare, topic, on, cookie, domStyle, domAttr, domClass) {

  return declare("tui.widget.form.ToggleSwitch", [tui.widget._TuiBaseWidget], {

    // ----------------------------------------------------------------------------- properties

    toggleBtn: null,

    toggleSlide: null,

    toggleLabels: null,

    stateMap: {}, // see ViewModeToggle for example

    leftLabelTag: '',

    rightLabelTag: '',

    cookieName: 'toggleSwitchState',

    // ----------------------------------------------------------------------------- methods

    postCreate: function () {
      // summary:
      //		Sets default values for class properties
      var toggleWidget = this;

      toggleWidget.toggleBtn = dojo.query('.toggle-button', toggleWidget.domNode)[0];
      toggleWidget.toggleSlide = dojo.query('.toggle-slide', toggleWidget.domNode)[0];
      toggleWidget.toggleLabels = dojo.query('.toggle-label', toggleWidget.domNode);

      toggleWidget.inherited(arguments);

      _.each(toggleWidget.toggleLabels, function (label, i) {
        switch (i) {
          case 0:
            toggleWidget.tagElement(label, toggleWidget.leftLabelTag);
            break;
          case 1:
            toggleWidget.tagElement(label, toggleWidget.rightLabelTag);
            break;
          default:
            break;
        }
      });
      toggleWidget.tagElement(toggleWidget.toggleSlide, "Toggle");
      toggleWidget.delegateEvents();

      // restore state if saved
      if(cookie(toggleWidget.cookieName)) {
        var restoreObj = dojo.fromJson(cookie(toggleWidget.cookieName));
        topic.subscribe("tui:channel=lazyload", function(){
          toggleWidget.toggleAll(restoreObj.oldValue, restoreObj.newValue, restoreObj.oldState, restoreObj.newState);
        });
      }
    },

    delegateEvents: function () {
      var toggleWidget = this;
      // listen to button
      on(toggleWidget.toggleSlide, "mousedown", function (e) {
        if (e.target.tagName.toLowerCase() === 'label' || e.target.tagName.toLowerCase() === 'input') return;
        var target = e.target.tagName.toLowerCase() === 'i' || e.target.tagName.toLowerCase() === 'a'
                ? dojo.query(e.target).parents('.toggle-slide')[0]
                : e.target,
            curValue = domAttr.get(target, 'data-value'),
            curState = toggleWidget.stateMap[curValue],
            setValue = toggleWidget.stateMap[curState];

        toggleWidget.toggleAll(curValue, setValue, curState, toggleWidget.stateMap[setValue]);
      });
      // listen to labels
      on(toggleWidget.domNode, ".toggle-label:click", function (e) {
        dojo.stopEvent(e);
        if (domClass.contains(e.target, 'active')) return;
        var setValue = domAttr.get(e.target, 'data-label'),
            curValue = domAttr.get(e.target, 'data-target'),
            curState = toggleWidget.stateMap[curValue];

        toggleWidget.toggleAll(curValue, setValue, curState, toggleWidget.stateMap[setValue]);
      });
    },

    toggleAll: function (oldValue, newValue, oldState, newState) {
      var toggleWidget = this;
      toggleWidget.onBeforeToggle(oldValue, newValue, oldState, newState);
      toggleWidget.toggleValue(newValue);
      toggleWidget.toggleActiveLabel(newValue);
      toggleWidget.toggleSwitchState(oldState, newState);
      toggleWidget.saveState(oldValue, newValue, oldState, newState);
      toggleWidget.onAfterToggle(oldValue, newValue, oldState, newState);
    },

    toggleValue: function (value) {
      var toggleWidget = this;
      domAttr.set(toggleWidget.toggleSlide, 'data-value', value);
    },

    toggleSwitchState: function (oldState, newState) {
      var toggleWidget = this;
      domClass.remove(toggleWidget.toggleBtn, oldState);
      domClass.add(toggleWidget.toggleBtn, newState);
    },

    toggleActiveLabel: function (value) {
      var toggleWidget = this;
      _.each(toggleWidget.toggleLabels, function (label) {
        domClass.remove(label, 'active');
      });
      domClass.add(dojo.query('[data-label="' + value + '"]', toggleWidget.domNode)[0], 'active');
    },

    saveState: function (oldValue, newValue, oldState, newState) {
      // saves state to cookie
      var toggleWidget = this, saveObj = dojo.toJson({
        oldValue: oldValue,
        newValue: newValue,
        oldState: oldState,
        newState: newState
      });
      cookie(toggleWidget.cookieName, saveObj, { expires: 1 });
    },

    // ----------------------------------------------------------------------------- no-ops

    onBeforeToggle: function (oldValue, newValue, oldState, newState) {},

    onAfterToggle: function (oldValue, newValue, oldState, newState) {}

  });
});