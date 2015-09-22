define('tui/widget/common/Sticky', [
  'dojo',
  'dojo/dom-geometry',
  'dojo/dom-class',
  'dojo/dom-style',
  'dojo/dom-attr',
  'dojo/_base/connect',
  'dojo/dom-construct',
  'tui/widget/mobile/Widget'], function (dojo, geom, domClass, domStyle, domAttr) {


  dojo.declare('tui.widget.common.Sticky', [tui.widget.mobile.Widget], {

    disabled: false,


    reset: function () {
      domAttr.remove(this.scrollableElement(), 'style');
    },


    onMobile: function () {
      this.reset();
      this.disable();
    },

    onDesktop: function () {
      this.enable();
      this.reset();
    },

    onMiniTablet: function () {
      this.enable();
      this.reset();
    },

    onTablet: function () {
      this.enable();
      this.reset();
    },

    disable: function () {
      this.disabled = true;
    },

    enable: function () {
      this.disabled = false;
    },

    isDisabled: function () {
      return this.disabled;
    },

    isEnabled: function () {
      return _.not(this.isDisabled)();
    },

    scrollableElement: _.once(function () {
      return _.first(dojo.query('*', this.domNode));
    }),

    stick: function (scrollLength, stickyRange) {
      domStyle.set(this.scrollableElement(), 'top', _.pixels(scrollLength - _.first(stickyRange)));
    },

    unstick: function () {
      //domClass.remove(this.scrollableElement(), 'sticky');
    },

    stickyRange: function () {
      return [100, 1990];
    },

    shouldStick: function (scrollPosition, stickyRange) {
      return _.isBetween(scrollPosition, stickyRange);
    },

    postCreate: function () {
      var widget = this;
      dojo.connect(window, 'onscroll', function () {
        _.when(widget.isEnabled(), function () {
          widget.shouldStick(geom.docScroll().y, widget.stickyRange()) ? widget.stick(geom.docScroll().y, widget.stickyRange()) : widget.unstick();
        });
      });
      widget.inherited(arguments);
    }

  });
  return tui.widget.common.Sticky;
});
