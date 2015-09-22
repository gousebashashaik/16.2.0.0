// #MethodSubscribable
// ##Mixin module
//
// Automate subscription to methods available in class. Allows to target specific methods when publishing.
// This mixin is provided to all widgets extending `_TuiBaseWidget`
//
// @author: Maurice Morgan.

define("tui/widget/mixins/MethodSubscribable", [
  "dojo",
  "dojo/topic",
  "tui/Tui"], function (dojo, topic, tui) {

  dojo.declare("tui.widget.mixins.MethodSubscribable", null, {

    /**
     * ###subscribableMethods
     * Array of methods (available in current class) for which subscription channels will be generated (in `_TuiBaseWidget.create()`).
     *
     * `subscribableMethods : ['subscribeMethods']` will subscribe to channel: `tui.widget.mixins.MethodSubscribable.subscribeMethods`
     *
     * @type {array}
     */
    subscribableMethods: null,

    /**
     * ###subscribeMethods()
     * generates subscription channels
     *
     * called by `_TuiBaseWidget.create()`
     */
    subscribeMethods: function () {
      var methodSubscribable = this;
      if (methodSubscribable.subscribableMethods) {
        _.each(methodSubscribable.subscribableMethods, function (methodName) {
          //Channel combines class name and specified methodName
          var subscribeName = methodSubscribable.declaredClass + "." + methodName;

          //Ensure method is available in current class and subscribe
          if (methodSubscribable[methodName]) {
            // TODO: replace with topic.subscribe (find/replace unsubscribe - deprecated, replace with remove)
            if (methodSubscribable.subscribe) {
              methodSubscribable.subscribe(subscribeName, methodSubscribable[methodName]);
            } else {
              dojo.subscribe(subscribeName, methodSubscribable[methodName]);
            }
          }
        });
      }
    }
  });

  return tui.widget.mixins.MethodSubscribable;
});