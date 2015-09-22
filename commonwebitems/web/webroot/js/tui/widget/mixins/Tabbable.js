// #Tabbable
// ##Mixin module
//
// Adds dynamic tab functionality to transition through panes of local content
// This is a mixin class, it must be extended
//
// @borrows tui.widget.mixins.ContentLoader
// @author: Maurice Morgan.

define("tui/widget/mixins/Tabbable", [
  "dojo",
  "dojo/_base/connect",
  "dojo/hash",
  "dojo/io-query",
  "tui/widget/mixins/ContentLoader"], function (dojo, connect, hash, ioQuery) {

  return dojo.declare("tui.widget.mixins.Tabbable", [tui.widget.mixins.ContentLoader], {

    //---------------------------------------------------------------- properties

    /**
     * ###tabSelector
     * CSS selector for tabs
     * @type {String}
     */
    tabSelector: "",

    /**
     * ###tabHeadings
     * Array reference to DOM elements of tab headings.
     * @type {Array}
     */
    tabHeadings: null,

    /**
     * ###tabSelectClassname
     * Classname given to a tab link in an active state.
     * @type {String}
     */
    tabSelectClassname: "active",

    /**
     * ###hashSubscribeChannel
     * An Object which references global hash subscribe function
     */
    hashSubscribeChannel: null,

    /**
     * ###currentTabContent
     * reference to the currently selected tab content.
     */
    currentTabContent: null,

    /**
     * ###hashchange
     * reference to hashchanges
     * @type {Boolean}
     */
    hashchange: false,

    /**
     * ###tabStopEvent
     * property to determine if default browser event should be prevented
     * @type {Boolean}
     */
    tabStopEvent: true,

    /**
     * ###clicked
     * store reference if tab was clicked
     */
    clicked: false,

    // ---------------------------------------------------------------- methods

    /**
     * ###attachTabbableEventListeners()
     * Initialises tabbable behaviour.
     * Adds onClick event to tab headings, and subscribes to the hashchange
     * channel, to respond to browser hash changes.
     */
    attachTabbableEventListeners: function () {
      var tabbable = this,
          activeIndex = 0;

      // hook for widgets to do stuff before tabs are initialised
      // eg render a template of tabs etc...
      tabbable.onBeforeTabInit(tabbable);
      // store reference to the tab headings
      tabbable.tabHeadings = dojo.query(tabbable.domNode).query(tabbable.tabSelector);
      // subscribe to hashchanges
      tabbable.hashSubscribeChannel = connect.subscribe("/dojo/hashchange", tabbable, function (event) {
        // find the tab widget which fired a hash change event, and displays corresponding tab content.
        var hashObj = ioQuery.queryToObject(dojo.hash());
        tabbable.goTabFromHash(hashObj);
      });

      // add onclick event to tab links, also hide all tab content.
      tabbable.tabHeadings.forEach(function (element, index) {
        activeIndex = (dojo.hasClass(element, "active")) ? index : 0;
        element.id = (element.id) ? element.id : [tabbable.domNode.id , "tab" , index].join(".");

        // get the content container's id from the href attribute of the tab header
        var contentId = tabbable.parseAnchorHref(element);

        if (contentId) {
          tabbable.hideTabContent(contentId[1]);
        }

        tabbable.connect(element, "onclick", function (event) {
          tabbable.clicked = true;
          tabbable.handleTabClick(tabbable, event, element);
        });
      });

      // show default tab content.
      tabbable.showTab(tabbable.tabHeadings[activeIndex]);

      // show appropriate tab from browser hash if present
      var hashObj = ioQuery.queryToObject(dojo.hash());
      if (hashObj && hashObj.tcID) {
        tabbable.goTabFromHash(hashObj);
      }

      // hook for widgets to do stuff after tabs are initialised
      tabbable.onAfterTabInit(tabbable);
    },

    /**
     * ###handleTabClick()
     * called by click event listener
     * @param {Object} tabbable reference to widget Class instance
     * @param event
     * @param {Node} element domNode that triggered the event
     */
    handleTabClick: function (tabbable, event, element) {
      // stop default event if configured this way
      if (tabbable.tabStopEvent) {
        dojo.stopEvent(event);
      }
      tabbable.onTabSelect(element);
    },

    /**
     * ###goTabFromHash()
     * Using information stored in given object, we make a decision
     * whether to display new tab content. Depending on if id stored in the hash object
     * is the same as current tab widget id.
     * @param {Object} hashObj
     */
    goTabFromHash: function (hashObj) {
      var tabbable = this;
      if (hashObj.tcID && hashObj.tcID == tabbable.domNode.id) {
        var element = dojo.byId(hashObj.thID);
        tabbable.showTab(element);
      }
    },

    /**
     * ###onTabSelect()
     * Appends information to browser hash to allow tabbing
     * to work via browser back and front buttons.
     * @param {Node} tabHeader
     */
    onTabSelect: function (tabHeader) {
      var tabbable = this;
      if (!tabbable.hashchange) {
        tabbable.showTab(tabHeader);
        return;
      }
      dojo.hash(ioQuery.objectToQuery({
        tcID: tabbable.domNode.id,
        thID: tabHeader.id
      }));
    },

    /**
     * ###showTab()
     * Highlights given tab header, hides current tab content, then displays the new tab content.
     * @param {Node} tabHeader
     */
    showTab: function (tabHeader) {
      var tabbable = this;
      var tabToShow = tabbable.getTabContentFromHref(tabHeader);
      var tabToHide = tabbable.currentTabContent;
      tabbable.beforeShowTab(tabHeader, tabToShow, tabToHide);
      tabbable.hideTabContent(tabbable.currentTabContent);
      tabbable.highlightTabHeading(tabHeader);
      tabbable.displayTabContent(tabToShow);
      tabbable.afterShowTab(tabHeader, tabToShow, tabToHide);
      tabbable.clicked = false;
    },

    /**
     * ###highlightTabHeading()
     * We first remove the class name, from the current active tab header.
     * Then add it to the tab header currently selected.
     * @param {Node} tabHeader
     */
    highlightTabHeading: function (tabHeader) {
      var tabbable = this;
      tabbable.tabHeadings.filter([".", tabbable.tabSelectClassname].join("")).removeClass(tabbable.tabSelectClassname);
      dojo.addClass(tabHeader, tabbable.tabSelectClassname);
    },

    /**
     * ###getTabContentFromHref()
     * Returns tab content, from given tab header. If tab header is not an
     * anchor we will use it as a start point to search for an anchor.
     * @param {Node} tabHeader
     * @returns {Node|null}
     */
    getTabContentFromHref: function (tabHeader) {
      var tabbable = this;
      var contentId = tabbable.parseAnchorHref(tabHeader);
      return (contentId) ? dojo.byId(contentId[1]) : null;
    },

    /**
     * ###displayTabContent()
     * Displays Tab's target content
     * Finds the tab content to display by using information from given tab header href.
     * @param {Node} tabToShow
     */
    displayTabContent: function (tabToShow) {
      var tabbable = this;
      if (tabToShow) {
        tabbable.currentTabContent = tabToShow;
        dojo.style(tabbable.currentTabContent, "display", "block");
      }
    },

    /**
     * ###hideTabContent()
     * Hides Tab's target content
     * @param {Node} tabContent
     */
    hideTabContent: function (tabContent) {
      if (tabContent) {
        dojo.style(tabContent, "display", "none");
      }
    },

    /**
     * ###parseAnchorHref()
     * Get href info from link element.
     * Parses href of given anchor link and return results in an array.
     * @param {Node} tabHeader
     * @returns {null|Array}
     */
    parseAnchorHref: function (tabHeader) {
      var anchorInfo = null;
      if (tabHeader.tagName.toLowerCase() !== 'a') {
        tabHeader = dojo.query(tabHeader).query("a")[0];
      }
      if (tabHeader) {
        anchorInfo = tabHeader.href.match(/#(.+)/);
      }
      return anchorInfo;
    },

    // ---------------------------------------------------------------- contentLoader methods

    /**
     * ###insertContent()
     * Inserts `HTMLContent` content into target DOM element
     * @borrows tui.widget.mixins.ContentLoader.insertContent
     * @param {String} HTMLContent Content that will be inserted into `contentNode`
     * @param {Node} contentNode
     */
    insertContent: function (HTMLContent, contentNode) {
      var tabbable = this;
      tabbable.contentNode = (contentNode || tabbable.currentTabContent);
      tabbable.inherited(arguments);
    },

    // ---------------------------------------------------------------- events

    /**
     * ###onAfterTabInit()
     * Hook fired after tabs have been initialised
     * called by `attachTabbableEventListeners()`
     * @param tabbable
     */
    onAfterTabInit: function (tabbable) {
    },

    /**
     * ###onBeforeTabInit()
     * Hook fired before tabs have been initialised
     * called by `attachTabbableEventListeners()`
     * @param tabbable
     */
    onBeforeTabInit: function (tabbable) {
    },

    /**
     * ###beforeShowTab()
     * Hook fired before tab content is displayed
     * called by `showTab`
     * @param {Node} tabHeader DOM element of the tab that was clicked
     * @param {Node} tabToContent DOM element of tab content that will be shown
     * @param {Node} tabToHide DOM element of tab content that was showing and will be hidden
     */
    beforeShowTab: function (tabHeader, tabToContent, tabToHide) {
    },

    /**
     * ###afterShowTab()
     * Hook fired after tab content is displayed
     * called by `showTab`
     * @param {Node} tabHeader DOM element of the tab that was clicked
     * @param {Node} tabToContent DOM element of tab content that will be shown
     * @param {Node} tabToHide DOM element of tab content that was showing and will be hidden
     */
    afterShowTab: function (tabHeader, tabToContent, tabToHide) {
    }
  });
});