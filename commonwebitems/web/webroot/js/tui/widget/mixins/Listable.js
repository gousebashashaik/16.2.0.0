// #Listable
// ##Mixin module
//
// Mixin class used for widgets which need list items. This mixin will also
// give scrollable behaviour, if the list goes over the min height.
// This is a mixin class, it must be extended
//
// @borrows tui.widget.mixins.FloatPosition
// @borrows tui.widget.mixins.Scrollable
// @author: Maurice Morgan.

define("tui/widget/mixins/Listable", [
  "dojo",
  "dojo/has",
  "dojo/on",
  "dojo/NodeList-data",
  "dojo/NodeList-manipulate",
  "tui/widget/mixins/FloatPosition",
  "tui/widget/mixins/Scrollable",
  "dojo/Stateful",
  "dojo/html"], function (dojo, has, on) {

  dojo.declare("tui.widget.mixins.Listable", [tui.widget.mixins.FloatPosition, tui.widget.mixins.Scrollable], {

    // ---------------------------------------------------------------- Listable Properties

    /**
     * ###listData
     * Array which holds the list data.
     *
     * @type {Array}
     */
    listData: null,

    /**
     * ###listConnects
     * Array containing dojo event connectors.
     *
     * @type {Array}
     */
    listConnects: [],

    /**
     * ###listItems
     * Array of list item DOM Nodes.
     *
     * @type {Array}
     */
    listItems: [],

    /**
     * ###listIndex
     * Current index of list.
     *
     * @type {Integer}
     */
    listIndex: -1,

    /**
     * ###disableItemClass
     * CSS class used for disabling list item element.
     *
     * @type {String}
     */
    disableItemClass: "disabled",

    /**
     * ###titleProp
     * Name of property to use for the list-item's title/name
     *
     * @type {String}
     */
    titleProp: "title",

    //
    valueProp: "value",

    /**
     * ###stateful
     * A Stateful object that stores the listData to which
     * watchers can be attached
     */
    stateful: null,

    // Boolean which determines if list is currently being displayed.
    listShowing: false,

    // Property flag for hiding list on browser resize.
    hideOnResize: true,

    // Property which references the list UL DOM element.
    listElementUL: null,

    // Property which references the list DOM element.
    listElement: null,

    // Property which determines if list is scrollable if it reaches maxHeight.
    /**
     * ###scrollable
     * Property which determines if list is scrollable if it reaches maxHeight
     *
     * @type {Boolean}
     */
    scrollable: true,

    // Property which determines the maxHeight of list.
    // This property only comes into consideration if scrollable is set to true.
    maxHeight: null,

    // Property selector for scroller use to create scrollbar.
    scrollerSelector: "",

    // node within which to place list element
    placeNode: document.body,

    // ---------------------------------------------------------------- Listable methods

    /**
     * ###listableInit()
     * Initialises Stateful store and adds watcher
     */
    listableInit: function () {
      // summary:
      //    listableInit
      var listable = this;

      listable.stateful = new dojo.Stateful({
        selectedData: null
      });

      listable.addWatcher(tui.widget.mixins.Listable.SELECTED_DATA,
          function (name, oldValue, value) {
            listable.onChange(name, oldValue, value);
          });

      listable.createListElement();
    },

    /**
     * ###createListElement()
     * Creates `<ul/>` DOM Node, where the list will be inserted
     */
    createListElement: function () {
      // summary:
      //    Creates UL dom node, where the list will be inserted.
      var listable = this;
      listable.listElement = dojo.create("div", null, listable.placeNode);
      listable.listElementUL = dojo.place("<ul></ul>", listable.listElement, 'last');
      dojo.setStyle(listable.listElement, {"display": "none"});
    },

    /**
     * ###clearList()
     * Clears list and resets data
     */
    clearList: function () {
      // summary:
      //    Clear selectOption list and reset data.
      var listable = this;
      // clear the list and data
      for (var i = 0; i < listable.listItems.length; i++) {
        var item = dojo.query(listable.listItems[i]);
        item.removeData();
        item.remove();
      }
      // disconnect any attached event listeners
      listable.disconnectEvents();
      // reset properties
      listable.listIndex = -1;
      listable.listItems = [];
      listable.listConnects = [];
      // delete the scrollable panel if initalised
      listable.deleteScrollerPanel();
      // and empty the `<ul/>` node
      dojo.html.set(listable.listElementUL, "");
    },

    /**
     * ###renderList()
     * Renders the list of data
     */
    renderList: function () {
      var listable = this;
      // first clear the list if present
      listable.clearList();
      // hook for widgets to manipulate complete data set before rendering list
      listable.onBeforeRender(listable, listable.listData);

      // loop over data and create list items
      _.forEach(listable.listData, function (item, index) {
        var li = dojo.create("li", {
          innerHTML: listable.onRenderLiContent(item[listable.titleProp], item),
          dataValue: item.value // custom attribute to hold the value of the item
        });

        if (item.disabled) {
          dojo.addClass(li, listable.disableItemClass);
        }

        // hook for Widgets to manipulate individual list item and data before it is rendered
        listable.onBeforeListItemRender(li, item, listable);

        // attach data to the list item
        dojo.query(li).data("list-data", listable.createListData(item));

        // add it to the list and connect events
        listable.addToList(li);
        listable._connectEvents(li, index);

        // hook for widgets to manipulate list item after it is rendered
        listable.onAfterListItemRender(li, item, listable);
      });

      // hook for widgets to do stuff after complete list is rendered
      listable.onAfterRender(listable, listable.listData);

      // check if scrolling functionality is needed
      listable.isScrollable();
    },

    /**
     * ###isScrollable()
     * If list is scrollable and > maxHeight
     * Render and attach Scrollable behaviour
     */
    isScrollable: function () {
      var listable = this;
      if (!listable.scrollable) {
        return;
      }
      // cache existing display and opacity attributes
      var defaultDisplay = dojo.style(listable.listElement, "display");
      var defaultOpacity = dojo.style(listable.listElement, "opacity");

      // hide it temporarily
      dojo.setStyle(listable.listElement, {
        "opacity": 0,
        "display": "block"
      });

      // calculate the height
      var height = dojo.style(listable.listElement, "height");

      // if it's > maxHeight
      // set the maxHeight height on it
      if (height > listable.maxHeight) {
        dojo.style(listable.listElement, "height", listable.maxHeight + "px");
        dojo.addClass(listable.listElement, "scroller");
        // hook for widgets to do stuff before the scrollPanel is attached
        listable.onBeforeAddScrollerPanel();
        // fire Scrollable method
        listable.addScrollerPanel(listable.listElement);
      }

      // and show the element again
      dojo.setStyle(listable.listElement, {
        "opacity": defaultOpacity,
        "display": defaultDisplay
      });
    },

    /**
     * ###createListData()
     * creates a data object for each item in the list
     *
     * called by `renderList()`
     * @param item
     * @returns {{key: *, value: *, listData: *}}
     */
    createListData: function (item) {
      var listable = this;
      return {
        key: item[listable.titleProp],
        value: item[listable.valueProp],
        listData: item
      };
    },

    /**
     * ###addToList()
     * Adds a list item to the stored array of items
     * and places it in the list's `<ul/>` node
     * @param {Node} li the DOM Node for this list item
     */
    addToList: function (li) {
      var listable = this;
      listable.listItems.push(li);
      dojo.place(li, listable.listElementUL, 'last');
    },

    /**
     * ###onRenderLiContent()
     * hook for widgets to manipulate innerHTML of list item
     * before it is rendered
     *
     * called by `onRenderList()`
     * @param {String} text the text for this list item
     * @param {Object} data the complete data object for the list
     * @returns {*} the manipulated data to be rendered
     */
    onRenderLiContent: function (text, data) {
      return text;
    },

    /**
     * ###disableItem()
     * Mark a list item at the given index as disabled.
     * Adds `disableItemClass` to the list item's DOM Node
     * @param {number} index the array index of the item to disable
     */
    disableItem: function (index) {
      var listable = this;
      if (!listable.listItems) {
        return;
      }
      dojo.addClass(listable.listItems[index], listable.disableItemClass);
    },

    /**
     * ###enableItem()
     * Enable a list item at given index if disabled.
     * Removes `disableItemClass` from the list item's DOM Node
     * @param {number} index the array index of the item to disable
     */
    enableItem: function (index) {
      var listable = this;
      if (!listable.listItems) {
        return;
      }
      dojo.removeClass(listable.listItems[index], listable.disableItemClass);
    },

    /**
     * ###highlightItem()
     * Highlights a list item
     * On hover or when matching keys pressed
     *
     * Called by `onSetHighlight()`, `onUpArrowKey()`, `onDownArrowKey()`
     * @param keypressed
     * @returns {boolean}
     */
    highlightItem: function (keypressed) {
      var listable = this;
      // current index +/- 1 if up/down arrow keys pressed
      var index = (keypressed === dojo.keys.DOWN_ARROW) ? listable.listIndex + 1 : listable.listIndex - 1;
      // if element is disabled skip it
      var newElement = listable.listItems[index];
      if (newElement) {
        if (dojo.hasClass(newElement, listable.disableItemClass)) {
          index = (keypressed === dojo.keys.DOWN_ARROW) ? index + 1 : index - 1;
        }
      }
      // else highlight it
      if (index >= 0 && index < listable.listItems.length) {
        listable.listIndex = index;
        listable.setHighlight();
        return true;
      }
      // we've reached the limit of the list so ignore event
      return false;
    },

    /**
     * ###_connectEvents()
     * Connect events to the list item
     * Called when each list item is rendered in `renderList()`
     * @param {Node} li the list item DOM Node
     * @param {number} index the array index for this item
     * @private
     */
    _connectEvents: function (li, index) {
      var listable = this;

      // hide the list on browser resize
      // or reposition it
      listable.listConnects.push(
          dojo.connect(window, "onresize", function (event) {
            dojo.stopEvent(event);
            if (listable.hideOnResize) {
              listable.hideList();
              return;
            }
            listable.onRepositionList(listable.listElement);
          })
      );

      // highlight on hover
      listable.listConnects.push(
          dojo.connect(li, "onmouseenter", function (event) {
            dojo.stopEvent(event);
            var newElement = listable.listItems[index];
            // DE27482
            /*if (dojo.hasClass(newElement, listable.disableItemClass)) {
              return;
            }*/
            listable.listIndex = index;
            listable.setHighlight();
          })
      );

      // remove highlight on leave
      listable.listConnects.push(
          dojo.connect(li, "onmouseleave", function (event) {
            dojo.stopEvent(event);
            var newElement = listable.listItems[index];
            // DE27482
            /*if (dojo.hasClass(newElement, listable.disableItemClass)) {
              return;
            }*/
            listable.listIndex = index;
            listable.setHighlight();
          })
      );

      // select element's data on click
      listable.listConnects.push(
          dojo.connect(li, "onclick", function (event) {
            dojo.stopEvent(event);
            if (dojo.hasClass(li, listable.disableItemClass)) {
              listable.onDisableSelection();
              return;
            }
            listable.elementListSelection();
          })
      );
      /*listable.listConnects.push(
       dojo.connect(listable.domNode, 'onblur', function(event) {
       dojo.stopEvent(event);
       if (listable.listShowing) {
       listable.elementListSelection();
       }
       })
       );*/
    },

    /**
     * ###elementListSelection()
     * sets the selected list item's data
     */
    elementListSelection: function () {
      var listable = this;
      var element = (listable.listItems[listable.listIndex]);

      if (element) {
        var elements = dojo.query("a", element);
        if (elements.length > 0) {
          elements[0].focus();
        }
      }

      // set the data and hide the list
      listable.setSelectedData();
      listable.hideList();

      // hook for widgets to do stuff after a selection has been made
      listable.onElementListSelection(listable.getSelectedData(), listable.listData);
    },

    /**
     * ###disconnectEvents()
     * Disconnects all events from the list items
     * For example if redrawing list with new data
     */
    disconnectEvents: function () {
      var listable = this;
      for (var i = 0; i < listable.listConnects.length; i++) {
        dojo.disconnect(listable.listConnects[i]);
      }
    },

    /**
     * ###setHighlight()
     * sets a list item to be highlighted. Using the current listIndex.
     */
    setHighlight: function () {
      // summary:
      //    sets a list item to be highlighted. Using the current listIndex.
      var listable = this;
      dojo.query(listable.listElementUL).query(".active").removeClass("active");
      dojo.addClass(listable.listItems[listable.listIndex], "active");
      var data = dojo.query(listable.listItems[listable.listIndex]).data("list-data");
      listable.onHighlightItem(listable.listItems[listable.listIndex], data, listable.listIndex);
    },

    /**
     * ###unSelect()
     * unselects the current selected item in list, and resets to default value.
     */
    unSelect: function () {
      // summary:
      //    unselects the current selected item in list, and resets to default value.
      var listable = this;
      listable.listIndex = -1;
      listable.setSelectedData();
    },

    /**
     * ###setSelectedValue()
     * Sets the list's selected value to the supplied value if valid
     * @param value the value to select
     */
    setSelectedValue: function (value) {
      var listable = this;
      var index = listable.getIndexFromValue(value);
      if (index !== -1) {
        listable.setSelectedIndex(index);
      }
    },

    /**
     * ###setSelectedIndex()
     * given an index for the data array
     * sets the selected value to the data
     * @param index
     */
    setSelectedIndex: function (index) {
      var listable = this;
      listable.listIndex = index;
      listable.setSelectedData();
      listable.setHighlight();
    },

    /**
     * ###getSelectedIndex()
     * Returns the currently selected index
     * @returns {Integer|listIndex|*}
     */
    getSelectedIndex: function () {
      var listable = this;
      return listable.listIndex;
    },

    /**
     * ###getIndexFromValue()
     * Given a value, returns the index of that value if it's in the data array
     * @param value
     * @returns {number}
     */
    getIndexFromValue: function (value) {
      var listable = this;
      var index = -1;
      for (var i = 0; i < listable.listData.length; i++) {
        if (listable.listData[i][listable.valueProp] === [value].join("")) {
          index = i;
          break;
        }
      }
      return index;
    },

    /**
     * ###setSelectedData()
     * sets the selected data
     * @param selectedData
     */
    setSelectedData: function (selectedData) {
      var listable = this;
      if (!selectedData) {
        selectedData = (listable.listIndex > -1) ? dojo.query(listable.listItems[listable.listIndex]).data("list-data")[0] : null;
      } else {
        selectedData = listable.createListData(selectedData);
      }
      listable.stateful.set(tui.widget.mixins.Listable.SELECTED_DATA, selectedData);
    },

    /**
     * ###getSelectedData()
     * returns the currently selected data
     * @returns {*}
     */
    getSelectedData: function () {
      var listable = this;
      return listable.stateful.get(tui.widget.mixins.Listable.SELECTED_DATA);
    },

    /**
     * ###isDataEmpty()
     * check if a selection has been made
     * @returns {boolean}
     */
    isDataEmpty: function () {
      var listable = this;
      return (listable.getSelectedData()) ? false : true;
    },

    /**
     * ###clearHighlight()
     * clear any highlighted list item
     */
    clearHighlight: function () {
      var listable = this;
      listable.listIndex = -1;
      dojo.query(listable.listElementUL).query(".active").removeClass("active");
    },

    /**
     * ###showList()
     * Displays the list
     */
    showList: function () {
      var listable = this;
      listable.onShowList(listable.listElement, listable.listItems, listable.listData);
      listable.onRepositionList(listable.listElement);
      dojo.style(listable.listElement, "display", "block");
      listable.listShowing = true;
    },

    /**
     * ###hideList()
     * Hides the list
     */
    hideList: function () {
      var listable = this;
      listable.onHideList();
      dojo.style(listable.listElement, "display", "none");
      listable.listShowing = false;
    },

    /**
     * ###attachListKeyEvent()
     * Attach keypress events to the list
     * @param {Node} domNode DOM Node to which events will be attached
     */
    attachListKeyEvent: function (domNode) {
      var listable = this;

      dojo.connect(domNode, "onkeypress", function (event) {
        if (listable.domNode.value === "") {
          listable.unSelect();
        }
        listable.listkeydown(event, this);
      });
    },

    /**
     * ###listkeydown()
     * Event method fired by `attachListKeyEvent`
     * Fires specific event methods depending on which key was pressed
     * @param event
     * @param domNode
     */
    listkeydown: function (event, domNode) {
      var listable = this;
      // ampersand and opening bracket are special cases
      var exceptions = ["&", "("];
      if (_.indexOf(exceptions, event.charOrCode) > -1) {
        listable.onOtherKey(event, domNode)
      } else {
        switch (event.keyCode) {
          case dojo.keys.DOWN_ARROW:
            listable.onDownArrowKey(event, domNode);
            break;
          case dojo.keys.UP_ARROW:
            listable.onUpArrowKey(event, domNode);
            break;
          case dojo.keys.ENTER:
            dojo.stopEvent(event);
            listable.onEnterKey(event, domNode);
            break;
          case dojo.keys.ESCAPE:
            dojo.stopEvent(event);
            listable.onEscKey(event, domNode);
            break;
          default:
            listable.onOtherKey(event, domNode);
        }
      }
    },

    /**
     * ###addWatcher()
     * Helper method allows to attach watcher method to a field
     * @param {String} propname string representing field to watch
     * @param {Function} watcherFn function to call when change is triggered
     */
    addWatcher: function (propname, watcherFn) {
      var listable = this;
      listable.stateful.watch(propname, watcherFn);
    },

    // ---------------------------------------------------------------- listable events

    /**
     * ###_getLiPosBottom()
     * Returns bottom position of a list item relative to the list
     * @param li list item for which position will be calculated
     * @returns {number}
     * @private
     */
    _getLiPosBottom: function (li) {
      var listable = this;
      var ly = parseInt(dojo.position(listable.listElement, true).y, 10);
      var y = parseInt(dojo.position(li, true).y, 10);
      var h = parseInt(dojo.position(li, true).h, 10);
      var bottomPos = (y - ly) + h;
      return bottomPos;
    },

    /**
     * ###onDownArrowKey()
     * Event method fired when Down-Arrow Key is pressed
     * @param event
     * @param {Node} domNode DOM Node which the event was fired from
     */
    onDownArrowKey: function (event, domNode) {
      var listable = this;
      dojo.stopEvent(event);
      if (listable.scrollPanels && (listable.listIndex !== -1)) {
        var activeLi = listable.listItems[listable.listIndex];
        var pos = listable._getLiPosBottom(activeLi);
        if (pos >= listable.maxHeight) {
          listable.scrollPanels[0].updateScrollerFromPos(pos);
        }
      }
      listable.highlightItem(dojo.keys.DOWN_ARROW);
    },

    /**
     * ###onUpArrowKey()
     * Event method fired when Up-Arrow Key is pressed
     * @param event
     * @param {Node} domNode DOM Node which the event was fired from
     */
    onUpArrowKey: function (event, domNode) {
      var listable = this;
      dojo.stopEvent(event);
      if (listable.scrollPanels) {
        var activeLi = listable.listItems[listable.listIndex];
        var pos = listable._getLiPosBottom(activeLi);
        if (pos <= 0) {
          listable.scrollPanels[0].updateScrollerFromPos(pos);
        }
      }
      listable.highlightItem(dojo.keys.UP_ARROW);
    },

    /**
     * ###onEnterKey()
     * Event method fired when Enter Key is pressed
     * @param event
     * @param {Node} domNode DOM Node which the event was fired from
     */
    onEnterKey: function (event, domNode) {
      var listable = this;
      var activeLi = listable.listItems[listable.listIndex];
      // again BAU has listable.listShowing check
      if (listable.listShowing) {
        listable.elementListSelection();
      }
    },

    /**
     * ###onEscKey()
     * Event method fired when Esc Key is pressed
     * @param event
     * @param {Node} domNode DOM Node which the event was fired from
     */
    onEscKey: function (event, domNode) {
      var listable = this;
      listable.hideList();
    },

    /**
     * ###destroyRecursive()
     * extends _WidgetBase's method to ensure all rendered elements are destroyed
     * @borrows dijit._WidgetBase.destroyRecursive
     */
    destroyRecursive: function () {
      var listable = this;
      dojo.query(listable.listElement).remove();
      dojo.query(listable.listElementUL).remove();
      listable.clearList();
      listable.inherited(arguments);
    },

    /**
     * ###onRepositionList()
     * Event method fired when list is repositioned
     *
     * Called by `showList()`
     * @param listElement
     */
    onRepositionList: function (listElement) {
      var listable = this;
      listable.posElement(listElement);
    },

    // ---------------------------------------------------------------- no-ops

    /**
     * ###onBeforeListItemRender()
     * Event method fired before each list item is rendered
     *
     * called by `renderList()`
     * @param li
     * @param item
     * @param listable reference to Class instance scope
     */
    onBeforeListItemRender: function (li, item, listable) {
      // summary:
      //    Event which is fire before each list item is rendered.
    },

    /**
     * ###onAfterListItemRender()
     * Event method fired after each list item is rendered
     *
     * called by `renderList()`
     * @param li
     * @param item
     * @param listable reference to Class instance scope
     */
    onAfterListItemRender: function (li, item, listable) {
      // summary:
      //    Event which is fire after each list item is rendered.
    },

    /**
     * ###onDisableSelection()
     * Event method fired when a disabled list item is selected
     */
		onDisableSelection: function () {},

    /**
     * ###onBeforeRender()
     * Event method fired before complete list is rendered
     *
     * called by `renderList()`
     * @param listable reference to Class instance scope
     * @param listData reference to data object for the list
     */
		onBeforeRender: function (listable, listData) {},

    /**
     * ###onAfterRender()
     * Event method fired after complete list is rendered
     *
     * called by `renderList()`
     * @param listable reference to Class instance scope
     * @param listData reference to data object for the list
     */
		onAfterRender: function (listable, listData) {},

    /**
     * ###onOtherKey()
     * Event method fired when a key other than `up`/`down` arrow, `enter`, `esc` is pressed
     * @param event
     * @param {Node} domNode DOM Node which the event was fired from
     */
		onOtherKey: function (event, domNode) {},

    /**
     * ###onElementListSelection
     * Event method fired after list item has been selected
     * @param {Object} selectedData the data that was selected
     * @param {Object} listData reference to complete list data object
     */
		onElementListSelection: function (selectedData, listData) {},

    /**
     * ###onHighlightItem()
     * Event method fired when a list item is highlighted
     * @param {Node} element the DOM Node that was highlighted
     * @param {Object} data reference to complete list data object
     * @param {Integer} index Array index for highlighted item
     */
		onHighlightItem: function (element, data, index) {},

    /**
     * ###onShowList()
     * Event method fired when list is displayed
     * @param {Node} listElement the list's DOM Node
     * @param {Array} listDomItems Array containing references to the list's items
     * @param {Object} listData reference to complete list data object
     */
		onShowList: function (listElement, listDomItems, listData) {},

    onRepositionList: function (listElement) {
      var listable = this;
      listable.posElement(listElement);
    },

		onHideList: function () {},

    /**
     * ###onChange()
     * Event method fired by Listable's Stateful store when a change is made
     * @param {String} name the name of the watched field
     * @param {Object|String} oldvalue the previous selection
     * @param {Object|String} newvalue the new selection
     */
		onChange: function (name, oldvalue, newvalue) {},

		onBeforeAddScrollerPanel: function () {},

    destroyRecursive: function () {
      var listable = this;
      dojo.query(listable.listElement).remove();
      dojo.query(listable.listElementUL).remove();
      listable.clearList();
      listable.inherited(arguments);
    }
  });

  tui.widget.mixins.Listable.SELECTED_DATA = "selectedData";

  return tui.widget.mixins.Listable;
});
