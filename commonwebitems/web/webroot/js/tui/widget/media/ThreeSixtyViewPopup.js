define("tui/widget/media/ThreeSixtyViewPopup", [
  'dojo',
  'dojo/on',
  'dojo/dom-attr',
  'dojo/_base/fx',
  'dojo/_base/array',
  'dojo/text!tui/widget/media/templates/ThreeSixtyViewPopupTmpl.html',
  'tui/widget/popup/Popup',
  'dojo/query',
  'dojo/window',
  'tui/widget/_TuiBaseWidget',
  'dojo/NodeList-traverse' ], function (dojo, on, domAttr, baseFx, array, threeSixtyViewTmpl, popup, query, win) {

  dojo.declare("tui.widget.media.ThreeSixtyViewPopup", [tui.widget.popup.Popup, tui.widget._TuiBaseWidget], {

    //---------------------------------------------- properties

    modal: true,

    tmpl: threeSixtyViewTmpl,

    loadDelay: 5000,

	viewURL:null,

	includeScroll: true,

    //--------------------------------------------------------------methods


    postCreate: function () {
      var ThreeSixtyViewPopup = this;
      ThreeSixtyViewPopup.inherited(arguments);
    },

    onAfterTmplRender: function () {
      var ThreeSixtyViewPopup = this;
      ThreeSixtyViewPopup.inherited(arguments);
      ThreeSixtyViewPopup.addEventListener();
      var timer = setTimeout(function () {
        ThreeSixtyViewPopup.hideLoading();
      }, ThreeSixtyViewPopup.loadDelay);
    },

    hideLoading: function () {
      var ThreeSixtyViewPopup = this;
      dojo.addClass(ThreeSixtyViewPopup.popupDomNode, 'loaded');
      dojo.removeClass(dojo.query('.container', ThreeSixtyViewPopup.popupDomNode)[0], 'loading');
    },

    addEventListener: function () {
      var ThreeSixtyViewPopup = this;
      ThreeSixtyViewPopup.inherited(arguments);
    }
  });

  return tui.widget.media.ThreeSixtyViewPopup;
});