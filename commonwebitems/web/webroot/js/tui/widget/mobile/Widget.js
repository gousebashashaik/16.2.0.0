define('tui/widget/mobile/Widget', [
  'dojo',
  'tui/widget/mobile/Display',
  'dojo/query',
  'dojo/dom-style',
  'dojo/dom-class',
  'dijit/_Widget'], function(dojo, Display) {


  dojo.declare('tui.widget.mobile.Widget', [dijit._Widget], {


    onMobile: function() {
    },

    onDesktop: function() {
    },

    onMiniTablet: function() {
    },

    postCreate: function() {
      var widget = this;
      Display.adapt(widget);
      widget.inherited(arguments);
    }

  });
});


