define("tui/widget/popup/CSSPopup", [
  "dojo",
  "dojo/on",
  "dojo/query",
  "dojo/text!tui/widget/popup/templates/ModalPopup.html",
  "tui/widget/popup/Popup"], function (dojo, on, query, tmpl) {

  dojo.declare("tui.widget.popup.CSSPopup", [tui.widget.popup.Popup], {

    // ---------------------------------------------------------------- tui.widget.popup.Popup properties

    // DOM eventType for displaying popup.
    eventType: "click",

    // Boolean for enabling and disabling popup modal.
    modal: true,

    content: null,

    contentSelector: '.modal-popup-content',

    tmpl: tmpl,

    contentWidth: 0,

    contentHeight: 0,

    widgetId: null,

    initialLoad: true,

    // ---------------------------------------------------------------- tui.widget.popup.PopupBase methods

    postCreate: function() {
      var popup = this;
      popup.content = "<h1>Testing Popup</h1><hr /><p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Atque earum fuga impedit placeat quod sint sunt suscipit veritatis?</p>";
      popup.inherited(arguments);
    },

    open: function () {
      // summary:
      //		Overides the default open method from popupbase, ensuring that modal DOM container
      //		if configured is displayed on open
      var popup = this;
      if (!popup.popupDomNode) {
        popup.createPopup();
        var content = query(popup.contentSelector, popup.popupDomNode)[0];
        popup.contentWidth = dojo.position(content).w;
        popup.contentHeight = dojo.position(content).h;
      }

      query("html").addClass("modalmode");

      popup.displayModal(true, popup.opacity, function () {
        if (popup.initialLoad) {
          setTimeout(function(){
            popup.displayPopup();
          }, 100);
        } else {
          popup.displayPopup();
        }
      }, popup);

      dojo.publish("tui:channel=modalOpening");
    },

    displayPopup: function () {
      var popup = this;
      dojo.setStyle(popup.popupDomNode, {
        width: popup.contentWidth + 'px',
        height: popup.contentHeight + 'px'
      });
      on.once(popup.popupDomNode, "transitionend, webkitTransitionEnd, mozTransitionEnd, otransitionend, oTransitionEnd", function(){
        dojo.removeClass(popup.popupDomNode, "loading");
      });
      popup.initialLoad = false;
    },

    close: function () {
      // summary:
      //		Overides the default close method from popupbase, ensuring that modal DOM container
      //		if configured is closed on close.
      var popup = this;
      dojo.addClass(popup.popupDomNode, 'closing');
      dojo.setStyle(popup.popupDomNode, {
        width: 0,
        height: 0
      });
      on.once(popup.popupDomNode, "transitionend, webkitTransitionEnd, mozTransitionEnd, otransitionend, oTransitionEnd", function(){
        dojo.removeClass(popup.popupDomNode, 'closing');
        if (popup.modal) {
          query("html").removeClass("modalmode");
          popup.displayModal(false, 0, null, popup);
        }
      });
    },

    onAfterTmplRender: function () {
      // summary:
      //		Overides onAfterTmplRender to add a close connect event listener for closing popup,
      //		when close link is selected.
      var popup = this;
      popup.inherited(arguments);
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

  return tui.widget.popup.CSSPopup;
});