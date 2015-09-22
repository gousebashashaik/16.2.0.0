define("tui/widget/expand/subExpandable", [
  "dojo",
  "tui/widget/_TuiBaseWidget",
  "tui/widgetFx/Transitiable",
  "tui/widgetFx/WipeTransitions"
], function(dojo) {

  dojo.declare("tui.widget.expand.subExpandable", [tui.widget._TuiBaseWidget, tui.widgetFx.Transitiable], {

    transitionType: "WipeInClose",

    transition: null,

    itemSelector: ".sub-item",

    targetSelector: ".sub-item-toggle",

    itemContentSelector: ".sub-item-content",

    closeOnClick: true,

    transitionOptions: {},

    autoTag: true,

    stopDefaultOnClick: true,

    postMixInProperties: function() {
      var expandable = this;
	  expandable.inherited(arguments);
      expandable.transitionOptions = {
        transitionType:      expandable.transitionType,
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
      // TODO: fix this, this is a BASE class, should not have specific code in it
      expandable.tagElements(dojo.query('.item > h2 > a', expandable.domNode), "showMoreReviews");
      expandable.tagElements(dojo.query('.link-show-hide', expandable.domNode), "seeReview");


// if(expandable.autoTag) {
     //     expandable.tagElements(dojo.query(expandable.targetSelector, expandable.domNode), 'toggle');
      //}
    }
  });

  return tui.widget.expand.subExpandable;
});
