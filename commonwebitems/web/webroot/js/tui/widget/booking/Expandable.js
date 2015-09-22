define("tui/widget/booking/Expandable", [
  "dojo",
  "tui/widget/_TuiBaseWidget",
  "tui/widget/booking/Transitiable",
  "tui/widget/booking/WipeTransitions"
], function(dojo) {

  dojo.declare("tui.widget.booking.Expandable", [tui.widget._TuiBaseWidget, tui.widget.booking.Transitiable], {

    transitionType: "WipeInClose",

    transition: null,

    itemSelector: ".item",

    targetSelector: ".item-toggle",

    itemContentSelector: ".item-content",

    closeOnClick: true,

    transitionOptions: {},

    autoTag: true,

    stopDefaultOnClick: true,

    postMixInProperties: function() {
      var expandable = this;
	  expandable.inherited(arguments);
      expandable.transitionOptions = {
        itemSelector:        expandable.itemSelector,
        targetSelector:      expandable.targetSelector,
        itemContentSelector: expandable.itemContentSelector,
        closeOnClick:        expandable.closeOnClick,
        defaultOpen:         expandable.defaultOpen,
        stopDefaultOnClick:  expandable.stopDefaultOnClick
      };
    },

    postCreate: function() {
      var expandable = this;
      expandable.transitionOptions.domNode = expandable.domNode;
      expandable.transition = expandable.addTransition();

      expandable.inherited(arguments);

      // Tagging particular element.
      if(expandable.autoTag) {
          expandable.tagElements(dojo.query(expandable.targetSelector, expandable.domNode), 'toggle');
      }
    }
  });

  return tui.widget.booking.Expandable;
});
