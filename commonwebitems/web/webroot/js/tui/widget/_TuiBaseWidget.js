// # _TuiBaseWidget
// ## Base widget class for Tui widgets
//
// @extends: dijit/_Widget
//
// @borrows tui.widget.mixins.Taggable.attachTag as attachTag
//
// @borrows tui.widget.mixins.MethodSubscribable
//
// @author: Maurice Morgan.

 define("tui/widget/_TuiBaseWidget", [
  "dojo",
  "tui/Tui",
  "dijit/_Widget",
  "tui/widget/mixins/Taggable",
  "tui/widget/mixins/MethodSubscribable"
], function (dojo) {

  dojo.declare("tui.widget._TuiBaseWidget", [dijit._Widget, tui.widget.mixins.Taggable, tui.widget.mixins.MethodSubscribable], {

    /**
     * ###loadedSelector
     * HTML class applied to widget's DOM Node once widget has loaded
     * @type {String}
     */
    loadedSelector: "loaded",

    /**
     * ###jsonData
     * JSON object containing data for widget
     * @type {Object}
     */
    jsonData: null,

    /**
     * ###subscribableMethods
     * Array of class methods that will be subscribed to on widget creation.
     *
     * Allows a class to specify method to execute in target class
     *
     * @type {Array}
     */
    subscribableMethods: null,

    /**
     * ###create()
     * Kicks off the life-cycle of a widget.
     *
     * *Do not override*
     * @param {Object?} params Widget parameters
     * @param {Node} srcNodeRef Widget's domNode
     */
    create: function (params, srcNodeRef) {
      var _tuiBaseWidget = this;
      // Wrap instantiation in a try/catch to avoid errors breaking the page in production
      // In all cases errors are logged
      try {
        // Call superclass functionality
        _tuiBaseWidget.inherited(arguments);
        // Generate method subscription channels
        _tuiBaseWidget.subscribeMethods();
        // Abstract method widgets can use to subscribe to custom channels
        _tuiBaseWidget.subscribeToChannels();
      } catch (err) {
        if (dojoConfig.prodDebug || dojoConfig.devDebug) {
          console.error(err);
          // Throws errors in development
          // add ?/& debug=true to url to enable dev mode
          if (dojoConfig.devDebug) {
            throw err;
          }
        }
      }
    },

    /**
     * ###postMixInProperties()
     * Called after the parameters to the widget have been read-in, but before the widget
     * template is instantiated.
     *
     * Especially useful to set properties that are referenced in the widget's template.
     */
    postMixInProperties: function () {
      var _tuiBaseWidget = this;
      // Call superclass functionality
      _tuiBaseWidget.inherited(arguments);
    },

    /**
     * ###postCreate()
     * Called after the DOM fragment has been created, but not necessarily added to the document.
     *
     * Do not include any operations which rely on node dimensions or placement.
     */
    postCreate: function () {
      var _tuiBaseWidget = this;
      // Call superclass functionality
      _tuiBaseWidget.inherited(arguments);

      // Add a css class to indicate widget has been loaded.
      dojo.addClass(_tuiBaseWidget.domNode, _tuiBaseWidget.loadedSelector);

      // Activate element tagging for analytics.
      _tuiBaseWidget.attachTag();
    },

    /**
     * ###showWidget()
     * Reveals widget domNode unless specified
     * @param {Node?} element DOM Node to show
     */
    showWidget: function (element) {
      var _tuiBaseWidget = this;
      var elementtoshow = (element || _tuiBaseWidget.domNode);
      dojo.setStyle(elementtoshow, "display", "block");
    },

    /**
     * ###isShowing()
     * Determines if widget DOM Node or specified node is visible
     * @param {Node?} element DOM Node to test
     */
    isShowing: function (element) {
      var _tuiBaseWidget = this;
      element = element || _tuiBaseWidget.domNode;
      return (dojo.style(element, "display") === "block");
    },

    /**
     * ###hideWidget()
     * Hides widget DOM Node unless specified
     * @param {Node?} element DOM Node to hide
     */
    hideWidget: function (element) {
      var _tuiBaseWidget = this;
      var elementtoshow = (element || _tuiBaseWidget.domNode);
      dojo.setStyle(elementtoshow, "display", "none");
    },

    /**
     * ###publishMessage()
     * Obsolete, maintained for compatibility
     *
     * Instead, use tui:channel=CHANNELNAME pattern
     * @param {String} action publishes: class on channel "tui/actionName" where action = actionName
     */
    publishMessage: function (action) {
      var _tuiBaseWidget = this;
      dojo.publish("tui/" + action, [_tuiBaseWidget]);
    },

    /**
     * ###subscribeToChannels()
     * Abstract method, used in widgets to manually subscribe to custom publishing channels
     */
    subscribeToChannels: function () {}

  });

  return tui.widget._TuiBaseWidget;
});