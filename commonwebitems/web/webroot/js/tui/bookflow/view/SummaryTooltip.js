define("tui/bookflow/view/SummaryTooltip", [
  "dojo",
  "dojo/dom-class",
  "dojo/text!tui/widget/popup/templates/Tooltip.html",
  "tui/widget/popup/Tooltips",
  "dojox/html/entities"], function (dojo, domClass) {

  dojo.declare("tui.bookflow.view.SummaryTooltip", [tui.widget.popup.Tooltips], {

    basisID: null,

    subscribableMethods: ['toggleSelf'],

    // ---------------------------------------------------------------- methods

    toggleSelf: function (value) {
      var tooltips = this;
      if (value === tooltips.basisID && domClass.contains(tooltips.domNode, 'hide')) {
        domClass.remove(tooltips.domNode, 'hide');
      } else {
        domClass.add(tooltips.domNode, 'hide');
      }
    }

  });

  return tui.bookflow.view.SummaryTooltip;
});