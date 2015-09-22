define("tui/searchBPanel/view/ChildAgesView", [
  "dojo",
  "dojo/query",
  "tui/searchBPanel/view/SearchErrorMessaging",
  "tui/widget/_TuiBaseWidget"], function (dojo, query) {

  dojo.declare("tui.searchBPanel.view.ChildAgesView", [tui.widget._TuiBaseWidget, tui.searchBPanel.view.SearchErrorMessaging], {

    watcher: function () {
    },

    // ----------------------------------------------------------------------------- methods

    postCreate: function () {
      var childAgesView = this;
      childAgesView.inherited(arguments);
      // watch model and display error message
      childAgesView.watcher = childAgesView.searchPanelModel.searchErrorMessages.watch("partyChildAges", function (name, oldError, newError) {

        if (_.size(newError) > 1) {
          return;
        }
        var key = (_.keys(newError)[0]);
        childAgesView.validateErrorMessage(newError[key], {
          floatWhere: childAgesView.widgetController.searchApi === 'searchPanel' ? "position-bottom-center" : "position-top-right",
          errorMessage: newError[key],
          field: "partyChildAges",
          key: key,
          elementRelativeTo: _.last(query('.child-age-selector', childAgesView.widgetController.domNode)),
          setPosOffset: function () {
            this.posOffset = childAgesView.widgetController.searchApi === 'searchPanel' ? {top: 12, left: -24} : {top: 32, left: -8};
          }
        });

        // publish error for listeners
        if (_.size(newError) === 1) {
          dojo.publish("tui/searchBPanel/view/ChildAgesView/Errors", [childAgesView.widgetController, true]);
        }
        if (_.size(newError) === 0) {
          dojo.publish("tui/searchBPanel/view/ChildAgesView/Errors", [childAgesView.widgetController, false]);
        }
      });
    }
  });

  return tui.searchBPanel.view.ChildAgesView;
});