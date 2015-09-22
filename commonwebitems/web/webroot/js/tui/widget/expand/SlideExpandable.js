define("tui/widget/expand/SlideExpandable", [
  'dojo/_base/declare',
  "dojo/on",
  "dojo/query",
  "dojo/_base/Deferred",
  "dojo/dom-class",
  "dojo/dom-construct",
  "dojo/dom-geometry",
  "dojo/dom-style",
  "dojo/dom-attr",
  "tui/utils/DetectFeature",
  "tui/widget/_TuiBaseWidget"
], function (declare, on, query, Deferred, domClass, domConstruct, domGeom, domStyle, domAttr, detectFeature) {

  return declare("tui.widget.expand.SlideExpandable", [tui.widget._TuiBaseWidget], {

    toggleSelector: ".expand-toggle",

    contentSelector: ".expand-content",

    wrapSelector: ".expand-content-wrap",

    parentSelector: '.expand-parent',

    contentNode: null,

    parentNode: null,

    padding: 0,

    /**
     * Options: 'top, bottom, left, right'
     * Adjusts CSS style as specified (to slide up, set to top, slide down, set to bottom, etc...)
     */
    openDirection: 'top',

    slideDistance: 0,

    startDistance: 0,

    subscribableMethods: ['toggleOpen'],

    postCreate: function () {
      var slideExpandable = this;
      slideExpandable.inherited(arguments);

      slideExpandable.initExpandable();
      slideExpandable.attachEvents();
    },

    initExpandable: function () {
      var slideExpandable = this;
      slideExpandable.contentNode = query(slideExpandable.contentSelector, slideExpandable.domNode)[0];
      slideExpandable.parentNode = query(slideExpandable.domNode).parents(slideExpandable.parentSelector)[0];
      slideExpandable.startDistance = domStyle.get(slideExpandable.domNode, slideExpandable.openDirection);
      slideExpandable.slideDistance = slideExpandable.calcSlideDistance();
    },

    attachEvents: function () {
      var slideExpandable = this, action;
      on(slideExpandable.domNode, "click", function (e) {
        action = (domAttr.get(slideExpandable.domNode, 'data-toggle-state') !== 'open') ? 'open' : '';
        slideExpandable.toggleOpen(action);
      });
    },

    toggleOpen: function (state) {
      var slideExpandable = this,
          domNode = slideExpandable.domNode;

      slideExpandable.onBeforeToggle(domNode, state);

      if(state === 'open') {
        domStyle.set(domNode, slideExpandable.openDirection, _.pixels(slideExpandable.slideDistance));
      } else {
        domStyle.set(domNode, slideExpandable.openDirection, '');
      }

      // add/remove class
      domClass[state === 'open' ? 'add' : 'remove'](domNode, 'open');

      // save state
      domAttr.set(domNode, 'data-toggle-state', state);

      slideExpandable.onAfterToggle(domNode, state);
    },

    calcSlideDistance: function () {
      var slideExpandable = this,
          dimSelector = slideExpandable.openDirection === ('top' || 'bottom') ? 'h' : 'w',
          maxMove = domGeom.position(slideExpandable.parentNode)[dimSelector],
          size = domGeom.position(slideExpandable.domNode)[dimSelector] + slideExpandable.padding,
          calc = slideExpandable.openDirection === ('top' || 'left') ? maxMove - size : maxMove + size;

      return _.isPositive(calc) ? (calc) : 0;
    },

    onBeforeToggle: function (domNode, state) {},

    onAfterToggle: function (domNode, state) {}

  });
});