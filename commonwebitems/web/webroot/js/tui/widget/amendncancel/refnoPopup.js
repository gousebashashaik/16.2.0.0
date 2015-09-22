define("tui/widget/amendncancel/refnoPopup", [
  "dojo",
  "dojo/on",
  "dojo/query",
  "dojo/dom-construct",
  "dojo/parser",
  "dojo/keys",
  "dojo/_base/lang",
  "dojo/_base/fx",
  "dojo/text!tui/widget/amendncancel/templates/acRefnoPopup.html",
  "tui/widget/popup/Popup",
  "tui/widget/form/SelectOption",
  "tui/widget/form/ValidationTextBox"], function (dojo, on, query, domConstruct, parser, keys, lang, fx, tmpl) {

  dojo.declare("tui.widget.amendncancel.refnoPopup", [tui.widget.popup.Popup], {

    // ---------------------------------------------------------------- tui.widget.popup.Popup properties

    // DOM eventType for displaying popup.
    eventType: "click",

    // Boolean for enabling and disabling popup modal.
    modal: true,

    content: null,

    tmpl: tmpl,

    resizeListener: false,

    contentWidth: 0,

    templateString: "",

    contentHeight: 0,
	
	element: null,

    widgetId: null,

    initialLoad: true,
	
	floatWhere: tui.widget.mixins.FloatPosition.CENTER,

    subscribableMethods: ["open", "close", "resize"],

    // ---------------------------------------------------------------- tui.widget.popup.PopupBase methods

    postCreate: function() {
      var popup = this;
      popup.content = "<h1>Popup content from template</h1>";
        popup.inherited(arguments);
    },
	
	onAfterTmplRender: function () {
          // summary:
          //		Overides onAfterTmplRender to add a close connect event listener for closing popup,
          //		when close link is selected.
          var popup = this;
          popup.inherited(arguments);
          dojo.parser.parse(popup.popupDomNode);
		  //dojo.query('.ref-cont')[0].innerHTML = popup.element;
		  // popup.setPosOffset(popup.floatWhere);
      },

      createPopup: function () {
          var popup = this;
          if(!popup.widgetId) {
              popup.renderTmpl();
			  
          } else {
              var preRenderedPopup = dojo.byId(popup.widgetId);
              if(preRenderedPopup) {
                  popup.popupDomNode = dojo.place(preRenderedPopup, document.body, "last");
                  popup.onAfterTmplRender();
              }
          }
      }

  });
  return tui.widget.amendncancel.refnoPopup;
});