define("tui/widget/popup/Tooltips", [
  "dojo",
  "dojo/text!tui/widget/popup/templates/Tooltip.html",
  "dojo/dom-style",
"dojo/query",
  "tui/widget/popup/PopupBase",
"dojox/html/entities",
"dojo/NodeList-traverse"], function (dojo, tooltipTmpl, domStyle, query) {
  dojo.declare("tui.widget.popup.Tooltips", [tui.widget.popup.PopupBase], {

    // ---------------------------------------------------------------- tui.widget.mixins.Templatable properties

    tmpl: tooltipTmpl,

    zIndex: 4000,

    className: "",
    // ---------------------------------------------------------------- tui.widget.popup.Tooltips properties

    /**
     * @param text {string} text content for Tooltip
     */
    text: "",

    /**
     * Add ability to reference a domId by passing refId as a parameter to text
     *
     * @example: text: { refId : 'domNodeID' }
     *
     * @extends: tui.widget._TuiBaseWidget
     */
    postMixInProperties: function () {
      var tooltips = this;
      if (tooltips.text && !dojo.isString(tooltips.text)) {
        tooltips.text = dojo.trim(dojo.byId(tooltips.text.refId).innerHTML);
      }
      tooltips.inherited(arguments);
    },

    // ---------------------------------------------------------------- tui.widget.popup.PopupBase method

    setPosOffset: function (position) {
      var tooltips = this;
      switch (position) {
        case "position-top-center":
          tooltips.posOffset = {top: -8, left: 0};
          break;
        case "position-bottom-center":
          tooltips.posOffset = {top: 8, left: 0};
          break;
        case "position-bottom-left":
          tooltips.posOffset = {top: 8, left: -6};
          break;
        case "position-top-right":
          tooltips.posOffset = {top: 50, left: 12};
          break;
	    case "position-left-center-arrow-right":
          tooltips.posOffset = {top:36, left: -12};
          break;
	// TODO: look into this, THIS IS A BASE CLASS
	// NOT THE PLACE TO MAKE COMPONENT-SPECIFIC CHANGES
	// THESE SHOULD BE PASSED AS DATA-DOJO-PROPS !!!
        case "position-right-center":
          tooltips.posOffset = {top: 5, left: -12};
          break;
        case "position-left-center":
          tooltips.posOffset = {top: 30, left: -12};
          break;
        case "position-right-center-far":
          tooltips.posOffset = {top: 32, left: 32};
          break;
        case "position-right-right":
            tooltips.posOffset = {top: 14, left: 13};
            break;
      }
    },

    onAfterTmplRender: function () {
      var tooltips = this;
      tooltips.inherited(arguments);
      domStyle.set(tooltips.popupDomNode, {
        zIndex: tooltips.zIndex
      })
    }
  });

  return tui.widget.popup.Tooltips;
});