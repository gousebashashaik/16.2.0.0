define('tui/widget/mixins/Tabbable', ['dojo',
  'dojo/_base/connect',
  'dojo/hash',
  'dojo/io-query',
  'tui/widget/mixins/ContentLoader'],
function(dojo, connect, hash, ioQuery) {

  dojo.declare('tui.widget.mixins.Tabbable', [tui.widget.mixins.ContentLoader], {

    // summary:
    //		Mixin class for Tabs.
    //
    // description:
    //		Mixin class which provides tabbable behaviour.
    //
    // @author: Maurice Morgan.

    // Selector used to retrieve tabs.
    tabSelector: '',

    // Array containing DOM elements of tab headings.
    tabHeadings: null,

    // Classname given a tab link in an active state.
    tabSelectClassname: 'active',

    // An Object which references global hash subscribe function.
    hashSubscribeChannel: null,

    // An Object reference to the current selected tab content.
    currentTabContent: null,

    hashchange: false,

    tabStopEvent: true,

    // ---------------------------------------------------------------- methods

    attachTabbableEventListeners: function() {
      // summary:
      //		Method which attaches events for tabbing behaviour on widget
      //		creation.
      // description:
      //		Adds on click event to tab heading, and subscribe
      // 		to the hashchange channel, for browser hash changes.
      var tabbable = this;
      tabbable.onBeforeTabInit(tabbable);
      tabbable.tabHeadings = dojo.query(tabbable.domNode).query(tabbable.tabSelector);
      tabbable.hashSubscribeChannel = connect.subscribe('/dojo/hashchange', tabbable, function(event) {
        // Find the tab widget which fired a hash change event, and displays corresponding tab content.
        var hashObj = ioQuery.queryToObject(dojo.hash());
        tabbable.goTabFromHash(hashObj);
      });
      // Add onclick event to tab links, also hide all tab content.
      var activeIndex = 0;
      tabbable.tabHeadings.forEach(function(element, index) {
        activeIndex = (dojo.hasClass(element, 'active')) ? index : 0;
        element.id = (element.id) ? element.id : [tabbable.domNode.id, 'tab' , index].join('.');
        var contentId = tabbable.parseAnchorHref(element);

        if (contentId) {
          tabbable.hideTabContent(contentId[1]);
        }
        tabbable.connect(element, 'onclick', function(event) {
          if (tabbable.tabStopEvent) {
            dojo.stopEvent(event);
          }
          tabbable.onTabSelect(element);
        });
      });

      // Show default tab content.
      tabbable.showTab(tabbable.tabHeadings[activeIndex]);
      var hashObj = ioQuery.queryToObject(dojo.hash());
      if (hashObj && hashObj.tcID) {
        tabbable.goTabFromHash(hashObj);
      }
      tabbable.onAfterTabInit(tabbable);
    },

    goTabFromHash: function(/* Object */ hashObj) {
      // summary:
      //		Displays tab content using data in given object.
      // description:
      //		Using information stored in given object, we make a descion
      //		whether to display new tab content.
      //      This depends if id stored in the hash object is the same as current
      //		tab widget id.
      var tabbable = this;
      if (hashObj.tcID && hashObj.tcID == tabbable.domNode.id) {
        var element = dojo.byId(hashObj.thID);
        tabbable.showTab(element);
      }
    },

    onTabSelect: function(/* DOM element */ tabHeader) {
      // summary:
      //		Method which update browser hash.
      // description:
      //		Appends information to browser hash to allow tabing
      //      to work via browser back and front buttons.
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

    showTab: function(/* DOM element */ tabHeader) {
      // summary:
      //		Show tab content.
      // description:
      //		Highlights given tab header, hides current tab content,
      //		then displays the new tab content.
      var tabbable = this;
      var tabToShow = tabbable.getTabContentFromHref(tabHeader);
      var tabToHide = tabbable.currentTabContent;
      tabbable.beforeShowTab(tabHeader, tabToShow, tabToHide);
      tabbable.hideTabContent(tabbable.currentTabContent);
      tabbable.highlightTabHeading(tabHeader);
      tabbable.displayTabContent(tabToShow);
      tabbable.afterShowTab(tabHeader, tabToShow, tabToHide);
    },

    highlightTabHeading: function(/* DOM element */ tabHeader) {
      // summary:
      //		Highlights selected header element, removing previous.
      // description:
      //		We first remove the classname, from the current active tab header.
      //      Then add it to the tab header currently selected.
      var tabbable = this;
      tabbable.tabHeadings.filter(['.', tabbable.tabSelectClassname].join(''))
                    .removeClass(tabbable.tabSelectClassname);
      dojo.addClass(tabHeader, tabbable.tabSelectClassname);
    },

    getTabContentFromHref: function(/* DOM element */ tabHeader) {
      // summary:
      //		get tab content using given HTML element.
      // description:
      // 		Returns tab content, from given tab header. If tab header is
      //		not an anchor we will use it as a start point to search for
      //		an anchor.
      var tabbable = this;
      var contentId = tabbable.parseAnchorHref(tabHeader);
      return (contentId) ? dojo.byId(contentId[1]) : null;
    },

    displayTabContent: function(/* DOM element */ tabToShow) {
      // summary:
      //		Display tab content.
      // description:
      // 		Finds the tab content to display by using information from
      //		given tab headr href.
      var tabbable = this;
      if (tabToShow) {
        tabbable.currentTabContent = tabToShow;
        dojo.style(tabbable.currentTabContent, 'display', 'block');
      }
    },

    hideTabContent: function(/* DOM element */  tabContent) {
      // summary:
      //		Hide tab content.
      // description:
      // 		Hide the given tab content passed.
      var tabbable = this;
      if (tabContent) {
        dojo.style(tabContent, 'display', 'none');
      }
    },

    parseAnchorHref: function(/* DOM element */ tabHeader) {
      // summary:
      //		Get href info from link element.
      // description:
      // 		Parses href of given anchor link and return
      //		results in an array.
      var anchorInfo = null;
      if (tabHeader.tagName.toLowerCase() !== 'a') {
        tabHeader = dojo.query(tabHeader).query('a')[0];
      }
      if (tabHeader) {
        anchorInfo = tabHeader.href.match(/#(.+)/);
      }
      return anchorInfo;
    },

    // ---------------------------------------------------------------- contentLoader methods

    insertContent: function(/*string*/ HTMLContent, /*dom node*/ contentNode) {
      var tabbable = this;
      tabbable.contentNode = (contentNode || tabbable.currentTabContent);
      tabbable.inherited(arguments);
    },

    // ---------------------------------------------------------------- events

    onAfterTabInit: function(tabbable) {
    },

    onBeforeTabInit: function(tabbable) {
    },


    beforeShowTab: function(/*DOM element*/ tabHeader, /*DOM element*/ tabToContent, /*DOM element*/ tabToHide) {
      // summary:
      //		An abstract method, its implementation should
      //		be defined by child class
      //
      // description:
      //		The method is called before tab content is displayed.

    },

    afterShowTab: function(/*DOM element*/ tabHeader, /*DOM element*/ tabToContent, /*DOM element*/ tabToHide) {
      // summary:
      //		An abstract method, its implementation should
      //		be defined by child class
      //
      // description:
      //		The method is called after tab content is displayed.
    }
  });

  return tui.widget.mixins.Tabbable;
});
