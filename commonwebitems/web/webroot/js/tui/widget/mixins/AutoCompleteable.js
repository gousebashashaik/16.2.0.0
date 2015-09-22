// # AutoCompleteable
// ## Mixin module
//
// Mixin provides autocomplete functionality for user searches.
// This is a mixin class, it must be extended
//
// @borrows tui.widget.mixins.Listable
// @borrows tui.store.JsonCache
// @author: Maurice Morgan.

define("tui/widget/mixins/AutoCompleteable", [
  "dojo",
  "tui/sniffing/form",
  "dojo/cache",
  "dojo/string",
  "dojo/text!tui/widget/form/autoComplete/template/AutocompleteItemTmpl.html",
  "dojox/dtl",
  "dojox/dtl/Context",
  "dojox/dtl/tag/logic",
  "tui/widget/mixins/Listable",
  "tui/widget/mixins/FloatPosition",
  "tui/store/JsonCache"], function (dojo, has, cache, string, autoTmpl) {

  return dojo.declare("tui.widget.mixins.AutoCompleteable", [tui.widget.mixins.Listable, tui.store.JsonCache], {

    // ---------------------------------------------------------------- autoCompleteable properties

    /**
     * ###charNo
     * Number of characters user will type before request is sent
     * @type {number}
     */
    charNo: 3,

    /**
     * ###duration
     * duration time in milliseconds, for showing list match.
     * @type {Number}
     */
    duration: 500,

    /**
     * ###_autoCompleteTimer
     * internal timer controls display list matches
     */
    _autoCompleteTimer: null,

    /**
     * ###liContentTemplate
     * template used inside list items
     * @type {String}
     */
    liContentTemplate: autoTmpl,

    /**
     * ###highlightFirstOnShow
     * property flag indicating whether to highlight first
     * element when a match has been made.
     * @type {Boolean}
     */
    highlightFirstOnShow: true,

    /**
     * ###searchProperty
     * name of search property
     * @type {String}
     */
    searchProperty: "title",

    /**
     * ###searchQuery
     * internal reference to the current query
     * @type {String}
     */
    searchQuery: null,

    /**
     * ###notSelectedReset
     * specify whether or not field is reset if a selection has not been made
     * @type {Boolean}
     */
    notSelectedReset: true,

    /**
     * ###highlightSelectText
     * specify whether or not any existing text in the input field
     * is highlighted on focus
     * @type {Boolean}
     */
    highlightSelectText: true,

    // ---------------------------------------------------------------- listable properties.

    /**
     * ###maxHeight
     * maximum height beyond which list becomes scrollable
     * @type {Number}
     */
    maxHeight: 276,

    /**
     * ###hideOnResize
     * specify whether list is hidden on browser resize
     * @type {Boolean}
     */
    hideOnResize: false,

    // ---------------------------------------------------------------- jsonCache properties

    /**
     * ###resultsToCache
     * specify whether or not results will be cached
     * @type {Boolean}
     */
    resultsToCache: false,

    // ---------------------------------------------------------------- autoCompleteable methods.

    /**
     * ###setSelectedValue()
     * Sets the selected value from the list
     *
     * fires `setSelectedData` method
     * @param value
     */
    setSelectedValue: function (value) {
      var autoCompleteable = this;
      var dataItem = autoCompleteable._cacheStore.get(value);
      if (dataItem) autoCompleteable.setSelectedData(dataItem);
    },

    /**
     * ###autoCompleteableInit()
     * initialises the autoCompleteable objects, and adds event listeners
     *
     * should be called by extending class
     */
    autoCompleteableInit: function () {
      var autoCompleteable = this;
      autoCompleteable.domNode.autocomplete = "off";
      // initialise list
      autoCompleteable.listableInit();
      // initialise store
      autoCompleteable.setupCacheStore();
      autoCompleteable.clearList();
      // attach events
      autoCompleteable.attachEventListeners();
      autoCompleteable.attachListKeyEvent(autoCompleteable.domNode);
    },

    /**
     * ###attachEventListeners()
     * Attaches event listeners to the body and domNode
     */
    attachEventListeners: function () {
      var autoCompleteable = this;

      // connect to body and exit the widget if not active
      autoCompleteable.connect(document.body, "onclick", function (event) {
        // hides autocomplete list if not active, and list is showing.
        if (document.activeElement === autoCompleteable.domNode || !autoCompleteable.listShowing) return;
        autoCompleteable.hideAutoCompleteList();
        // if autocomplete is attached to a multifield input, publish a blur event to the multifield
        if (autoCompleteable.multiField) {
          dojo.publish(autoCompleteable.multiField.declaredClass + ".onTextboxInputBlur");
        }
      });

      // selects any previously entered text onClick
      autoCompleteable.connect(autoCompleteable.domNode, "onclick", function (event) {
        dojo.publish("tui/widget/mixins/AutoCompleteable/onclick", [autoCompleteable]);
        autoCompleteable.listIndex = -1;
        autoCompleteable.selectText();
      });

      // selects any previously entered text onFocus
      autoCompleteable.connect(autoCompleteable.domNode, "onfocus", function (event) {
        autoCompleteable.selectText();
      });

    },

    /**
     * ###selectText()
     * if highlightSelectText is set to true, selects any previously entered text
     * when the widget is interacted with
     *
     * called by `onClick` and `onFocus` event listeners
     */
    selectText: function () {
      var autoCompleteable = this;

      // allow override through `highlightSelectText` property
      if (!autoCompleteable.highlightSelectText) return;

      if (has("input-attr-placeholder")) {
        autoCompleteable.domNode.select();
        return;
      }

      if (autoCompleteable.domNode.value !==
          dojo.attr(autoCompleteable.domNode, 'placeholder')) {
        autoCompleteable.domNode.select();
      }
    },

    /**
     * ###hideAutoCompleteList()
     * hides the list if showing and calls any exit methods
     *
     * called by `document.body` listener
     */
    hideAutoCompleteList: function () {
      var autoCompleteable = this;
      autoCompleteable.hideList();

      var data = autoCompleteable.getSelectedData();
      if (!data) {
        // if there's no selected data and 'notSelectedReset' is false, return early (leaves typed text in field)
        if (!autoCompleteable.notSelectedReset) return;

        // set the value to the placeholder if available
        autoCompleteable.domNode.value = (!has("input-attr-placeholder") &&
            dojo.attr(autoCompleteable.domNode, 'placeholder')) ?
            dojo.attr(autoCompleteable.domNode, 'placeholder') : "";

      } else if (autoCompleteable.domNode.value !== data.key) {
        if (autoCompleteable.notSelectedReset) {
          // if the current value is new and 'notSelectedReset' is true
          // deselect the entered text
          autoCompleteable.unSelect();
        } else {
          // set the autocomplete value to what was selected
          autoCompleteable.domNode.value = data.key;
        }
      }
    },

    // ---------------------------------------------------------------- listable methods

    /**
     * ### elementListSelection()
     * overrides Listable functionality
     *
     * called by focus events and keypress events
     * @borrows tui.widget.mixins.Listable.elementListSelection
     */
    elementListSelection: function () {
      var autoCompleteable = this;
      if (autoCompleteable.listIndex !== -1) {
        var element = (autoCompleteable.listItems[autoCompleteable.listIndex]);
        if (element) {
          var elems = dojo.query("a", element);
          if (elems.length > 0) {
            elems[0].focus();
          }
        }
        autoCompleteable.setSelectedData();
        autoCompleteable.hideList();
      }
      if (autoCompleteable.domNode.value === '') {
        autoCompleteable.unSelect();
      }
      autoCompleteable.onElementListSelection(autoCompleteable.getSelectedData(), autoCompleteable.listData);
    },

    /**
     * ###highlightItem()
     * called by `onResults`
     * @borrows tui.widget.mixins.Listable.highlightItem
     * @param {Number} keypressed event keycode/charcode
     * @returns {*}
     */
    highlightItem: function (keypressed) {
      var autoCompleteable = this;
      if (autoCompleteable.listShowing) {
        return autoCompleteable.inherited(arguments);
      }
      return false;
    },

    /**
     * ###createListElement()
     * Adds a css class called "autocomplete" to listElement.
     *
     * called by `listableInit`, `onBeforeRender`
     * @borrows tui.widget.mixins.Listable.createListElement
     */
    createListElement: function () {
      var autoCompleteable = this;
      autoCompleteable.inherited(arguments);
      dojo.addClass(autoCompleteable.listElement, "autocomplete");
    },

    // ---------------------------------------------------------------- Listable events

    /**
     * ###onOtherKey()
     * called by tui.widget.mixins.Listable.listkeydown
     * @borrows tui.widget.mixins.Listable.onOtherKey
     * @param {Object} event
     * @param {Node} domNode
     */
    onOtherKey: function (event, domNode) {
      var autoCompleteable = this;

      if (dojo.keys.TAB === (event.charCode || event.keyCode)) {
        autoCompleteable.onEnterKey(event, domNode);
        return;
      }

      // set a timer to fire request after autocomplete's configured `duration`
      autoCompleteable._autoCompleteTimer = setTimeout(function () {
        autoCompleteable.onType(domNode, event);
        clearTimeout(autoCompleteable._autoCompleteTimer);
        autoCompleteable._autoCompleteTimer = null;
      }, autoCompleteable.duration);
    },

    /**
     * ###onEnterKey()
     * called by tui.widget.mixins.Listable.listkeydown
     * @borrows tui.widget.mixins.Listable.onEnterKey
     * @param {Object} event
     * @param {Node} domNode
     */
    onEnterKey: function (event, domNode) {
      var autoCompleteable = this;
      autoCompleteable.inherited(arguments);
      // extend Listable's method. If autcomplete is part of a multiFieldList
      // and validation has passed (no exception)
      // then focus the multiField and blur
      // this is to ensure that blur is executed from the multifield rather than the
      // drop-down list which is not a child
      if (autoCompleteable.multiField && !autoCompleteable.multiField.hasException) {
        autoCompleteable.multiField.onTextboxInputFocus();
        autoCompleteable.multiField.onTextboxInputBlur();
      }
    },

    // ---------------------------------------------------------------- AutoCompleteable events

    /**
     * ###onChange()
     * method is called by watcher attached to
     * JsonCache Stateful
     * @borrows tui.widget.mixins.Listable.onChange
     * @param {String} name name of the field
     * @param {Object|String} oldValue the previous value
     * @param {Object|String} value the 'changed' value
     */
    onChange: function (name, oldValue, value) {
      var autoCompleteable = this;
      autoCompleteable.domNode.value = (value) ? value.key : "";
    },

    /**
     * ###onType()
     * called when the user is typing (within the configured delay)
     * connect using aspect.after (previously used dojo.connect)
     *
     * called by `onOtherKey`
     * @param {Node} element the autocomplete's input field
     * @param event
     */
    onType: function (element, event) {
      var autoCompleteable = this;

      // only fire a query if user has typed at least `charNo` characters
      // else hide the list if showing
      if (element.value.length >= autoCompleteable.charNo) {
        var queryObj = autoCompleteable.createQueryObject(element, event);
        var query = autoCompleteable.createQuery(element, event);
        autoCompleteable.query(queryObj, {
          searchQuery: query
        });
      } else {
        autoCompleteable.hideList();
      }
    },

    /**
     * ###createQueryObject()
     * creates an object containing the query
     * @param {Node} element the autocomplete's input field
     * @param event
     * @returns {{}}
     */
    createQueryObject: function (element, event) {
      var autoCompleteable = this;
      var searchRegExp = new RegExp([element.value].join(""), "i");
      var queryObj = {};
      queryObj[autoCompleteable.searchProperty] = searchRegExp;
      return queryObj;
    },

    /**
     * ###createQuery()
     * creates a query string
     * @param {Node} element the autocomplete's input field
     * @param event
     * @returns {*}
     */
    createQuery: function (element, event) {
      var autoCompleteable = this;
      if (autoCompleteable.searchQuery) {
        var query = ["?", autoCompleteable.searchQuery, "=", element.value].join("");
        return query;
      }
      return null;
    },

    /**
     * ###createTmpl()
     * renders a template using dojox.dtl (django)
     * @param {Object} context object containing data referenced in the template
     * @param tmpl the django template to render
     * @returns rendered html
     */
    createTmpl: function (context, tmpl) {
      var template = new dojox.dtl.Template(tmpl);
      context = new dojox.dtl.Context(context);
      return template.render(context);
    },

    // ---------------------------------------------------------------- listable event.

    /**
     * ###onResults()
     * Override onResults from JsonCache. This method is called every time results
     * are returned from a query.
     *
     * Using the given results we then render the autocomplete list for viewing
     * @borrows tui.store.JsonCache.onResults
     * @param {Array} data objects from query results
     */
    onResults: function (data) {
      var autoCompleteable = this;

      // return early if is in a multifieldlist and is inactive
      if (autoCompleteable.multiField && !autoCompleteable.multiField.isActive()) return;

      // set this data reference to supplied data
      autoCompleteable.listData = data;
      autoCompleteable.renderList();
      autoCompleteable.showList();

      // if no data fire `onNoResults` method
      if (!data || data.length <= 0) {
        autoCompleteable.onNoResults(autoCompleteable.listElementUL);
      } else if (autoCompleteable.highlightFirstOnShow) {
        // if data and `highlightFirstOnShow` fire `highlightItem`
        autoCompleteable.highlightItem(dojo.keys.DOWN_ARROW);
      }
    },

    /**
     * ###destroyRecursive()
     * extend Listable's method to ensure autocomplete list element is also destroyed
     * @borrows tui.widget.mixins.Listable.destroyRecursive
     */
    destroyRecursive: function () {
      var autoCompleteable = this;
      autoCompleteable.clearList();
      dojo.query(autoCompleteable.listElement).remove();
      autoCompleteable.inherited(arguments);
    },

    /**
     * ###onRenderLiContent()
     * extends Listable's method
     * escapes returned `text` and wraps each query match (typed query) in strong tag
     * allows us to indicate to user how we match entry to the query
     * @borrows tui.widget.mixins.Listable.onRenderLiContent
     * @param {String} text match returned from server
     * @param {Object} data complete data object returned from server
     * @returns {String} escaped string with matches wrapped
     */
    onRenderLiContent: function (text, data) {
      var autoCompleteable = this;

      // escape regexp from `http://closure-library.googlecode.com/svn/docs/closure_goog_string_string.js.source.html#line956`
      var typedText = ('' + autoCompleteable.domNode.value).replace(/([-()\[\]{}+?*.$\^|,:#<!\\\/])/g, '\\$1').
          replace(/\x08/g, '\\x08');

      return string.substitute(autoCompleteable.liContentTemplate, {
        autocompletetext: text.replace(new RegExp('(' + typedText + ')', 'i'), '<strong>$1</strong>')
      });
    },

    /**
     * ###onBeforeRender()
     * Called before results are rendered
     * clears the list of current data and
     * renders a new empty list
     *
     * called by tui.widget.mixins.Listable.renderList
     * @borrows tui.widget.mixins.Listable.onBeforeRender
     */
    onBeforeRender: function () {
      var autoCompleteable = this;
      dojo.query(autoCompleteable.listElementUL).orphan();
      dojo.query(autoCompleteable.listElement).orphan();
      autoCompleteable.createListElement();
    },

    /**
     * ###onNoResults()
     * This event method is called when no results are found from autocomplete
     * connect using aspect.after (dojo.connect)
     * no-op
     *
     * @param listElementUL
     * @param data
     */
    onNoResults: function (listElementUL, data) {}
  });
});