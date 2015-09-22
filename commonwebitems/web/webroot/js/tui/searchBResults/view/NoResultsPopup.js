define("tui/searchBResults/view/NoResultsPopup", ["dojo", "dojo/on", "dojo/_base/connect", "tui/widget/popup/DynamicPopup"], function (dojo, on) {

  dojo.declare("tui.searchBResults.view.NoResultsPopup", [tui.widget.popup.DynamicPopup], {

    widgetId: "filterNoResults",

    modal: true,

    closeSelector: '.hide-modal',

    reloadOnClose: false,

    postCreate: function () {
      var noResultsPopup = this;
      on(dojo.query('.reset', noResultsPopup.popupDomNode), 'click', function () {
        dijit.registry.byId('mediator').fire('clearAll', null, null);
        noResultsPopup.close();
      });
      on(dojo.query('.undo', noResultsPopup.popupDomNode), 'click', function () {
        noResultsPopup.close();
      });
      var reloadLink = dojo.query('.history-reload', noResultsPopup.popupDomNode);
      if (reloadLink.length) {
        on.once(reloadLink, 'click', function () {
          window.location.reload();
        });
      }
      noResultsPopup.inherited(arguments);
    },

    open: function () {
      // summary:
      //      Extends default method
      var noResultsPopup = this;
      window.scrollTo(0, 0);
      noResultsPopup.inherited(arguments);
      // reload page when click fired on modal background (outside popup)
      if (noResultsPopup.reloadOnClose) {
        on.once(noResultsPopup.modalDomNode, "click", function (event) {
          window.location.reload();
        });
      }
    },

    onAfterTmplRender: function () {
      var noResultsPopup = this;

      var links = dojo.query('.options a', noResultsPopup.popupDomNode);
      _.each(links, function (link, i) {
        switch (i) {
          case 0:
            noResultsPopup.tagElement(link, "lastselectUndo");
            break;
          case 1:
            noResultsPopup.tagElement(link, "removemySelection");
            break;
          default:
            break;
        }
      });
    }

  });

  return tui.searchBResults.view.NoResultsPopup;
});