define("tui/widget/amendncancel/logoutPopup", [
  "dojo",
  "dojo/on",
  "dojo/query",
  "dojo/dom-construct",
  "dojo/parser",
  "dojo/dom-class",
  "dojo/keys",
  "dojo/_base/lang",
  "dojo/_base/fx",
  "dojo/text!tui/widget/amendncancel/templates/logoutPopup.html",
  "tui/widget/popup/Popup"], function (dojo, on, query, domConstruct, parser, domClass, keys, lang, fx, tmpl) {


  dojo.declare("tui.widget.amendncancel.logoutPopup", [tui.widget.popup.Popup], {
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

	targetLink: "../manage/exitBooking",

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

	logout: function() {
		// Please write the function to close the session and redirect to the login page
		// dummy code here;
		domClass.add(query('.logout-popup')[0] , "updating");
	},

	onAfterTmplRender: function () {
          // summary:
          //		Overides onAfterTmplRender to add a close connect event listener for closing popup,
          //		when close link is selected.
          var popup = this;
          popup.inherited(arguments);
          dojo.parser.parse(popup.popupDomNode);
		  var exitBtn = dojo.byId('exitButton');

		  dojo.connect(exitBtn, "onclick", function(evt){
			popup.logout();
		  });

      },
	onOpen: function (popupDomNode, popupBase) {
		var exitBtn = dojo.byId('exitButton');
		dojo.attr(exitBtn, "href", this.targetLink);
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
  return tui.widget.amendncancel.logoutPopup;
});