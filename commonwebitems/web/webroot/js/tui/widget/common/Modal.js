define('tui/widget/common/Modal', [
  'dojo',
  'dojo/dom-class',
  'dojo/_base/connect',
  'dojo/dom-style',
  'dojo/dom-construct',
  'dojo/dom-attr',
  'tui/widget/mobile/Widget'], function(dojo, domClass) {

  function mobileShow() {
    var widget = this;
    _.each(widget.mobileToggleClasses, function(toggleClass) {
      domClass.add(toggleClass.dom, toggleClass.cssClass);
    });
  }

  function mobileHide() {
    var widget = this;
    _.each(widget.mobileToggleClasses, function(toggleClass) {
      domClass.remove(toggleClass.dom, toggleClass.cssClass);
    });
  }

  function desktopShow() {
    var widget = this;
    _.each(widget.desktopToggleClasses, function(toggleClass) {
      domClass.add(toggleClass.dom, toggleClass.cssClass);
    });
  }

  function desktopHide() {
    var widget = this;
    _.each(widget.desktopToggleClasses, function(toggleClass) {
      domClass.remove(toggleClass.dom, toggleClass.cssClass);
    });
  }

  dojo.declare('tui.widget.common.Modal', [tui.widget.mobile.Widget], {

    isMiniModal:false,

    hideBackground: function() {
      domClass.add(dojo.query('.structure')[0], 'hide');
    },

    showBackground: function() {
      domClass.remove(dojo.query('.structure')[0], 'hide');
    },

    showMiniBackground:function () {
      domClass.add(dojo.query('html')[0], 'show-bg');
    },

    hideMiniBackground:function () {
      domClass.remove(dojo.query('html')[0], 'show-bg');
    },

    isOpen: function() {
      var widget = this;
      return domClass.contains(widget.domNode, 'show');
    },

    onMobile: function() {
      var widget = this;
      widget.show = mobileShow.bind(widget);
      widget.hide = mobileHide.bind(widget);
      if (!widget.isMiniModal) {
      widget.isOpen() ? widget.hideBackground() : widget.showBackground();
      } else {
        widget.isOpen() ? widget.showMiniBackground() : widget.hideMiniBackground();
      }
    },

    onDesktop: function() {
      var widget = this;
      widget.showBackground();
      widget.show = desktopShow.bind(widget);
      widget.hide = desktopHide.bind(widget);
    },

    onMiniTablet: function() {
      var widget = this;
      widget.showBackground();
      widget.show = desktopShow.bind(widget);
      widget.hide = desktopHide.bind(widget);
    },

    postCreate: function() {
      var widget = this;

      var mobileModalToggleClasses = [
        {dom: widget.domNode, cssClass: 'show'},
        {dom: dojo.query('.structure')[0], cssClass: 'hide'},
        {dom: dojo.query('html')[0], cssClass: 'modal-open'}
      ];
      var mobileMiniModalToggleClasses = [
        {dom:widget.domNode, cssClass:'show'},
        {dom:dojo.query('html')[0], cssClass:'modal-open'},
        {dom:dojo.query('html')[0], cssClass:'show-bg'}
      ];

      widget.isMiniModal ? (widget.mobileToggleClasses = mobileMiniModalToggleClasses) :
        (widget.mobileToggleClasses = mobileModalToggleClasses);

      widget.desktopToggleClasses = [
        {dom: widget.domNode, cssClass: 'show'},
        {dom: dojo.query('html')[0], cssClass: 'modal-open'}
      ];

      window.document.addEventListener('orientationchange', function(){
        if (window.orientation === 90 || window.orientation === -90){
          widget.isOpen() ? window.scrollTo(0, 0) : '';
        }
      }, false);

      widget.inherited(arguments);
    },

    show: null,

    hide: null

  });
});
