define('tui/widget/mobile/TextRegion', [
  'dojo',
  'dojo/dom-style',
  'dojo/dom-class',
  'dojo/dom-attr',
  'dojo/_base/declare',
  'dojo/Stateful',
  'dojo/query',
  'dojo/dom-geometry',
  'tui/common/Animation',
  'tui/widget/mixins/Expandable',
  'tui/widget/mobile/Widget'
], function(dojo, domStyle, domClass, domAttr, declare, Stateful, query, domGeometry, Animation) {

  dojo.declare('tui.widget.mobile.TextRegion', [tui.widget.mobile.Widget, tui.widget.mixins.Expandable], {

    displayChangeListener: null,

    dynamicHeight: function() {
      var widget = this;
      var height = 0;
      var elements = query('*', widget.domNode);
      elements.forEach(function(element) {
        if (element.nodeName !== 'A') {
          height = height + domGeometry.getMarginBox(element).h;
        }
      });
      return Math.ceil(height);
    },

    expand: function() {
      var widget = this;
      var newHeight = widget.dynamicHeight();

      if (newHeight <= 0) {
        return;
      }

      dojo.setStyle(widget.domNode, 'height', 'auto');
    },

    contract: function() {
      var widget = this;
      var newHeight = domGeometry.getMarginBox(query('h3', widget.domNode)[1]).t;

      if (newHeight <= 0) {
        return;
      }

      domClass.add(widget.domNode, 'toggle-text');
      dojo.setStyle(widget.domNode, 'height', newHeight + 'px');
    },

    onMobile: function() {
      var widget = this;
      widget.contract();
      dojo.addClass(widget.domNode, 'animated');
    },

    onMiniTablet: function() {
      var widget = this;
      widget.expand();
      domAttr.remove(widget.domNode, 'style');
      dojo.removeClass(widget.domNode, 'animated');
    },

    onDesktop: function() {
      var widget = this;
      widget.expand();
      domAttr.remove(widget.domNode, 'style');
      dojo.removeClass(widget.domNode, 'animated');
    },

    postCreate: function() {
      var widget = this;
      widget.inherited(arguments);
    }
  });
});
