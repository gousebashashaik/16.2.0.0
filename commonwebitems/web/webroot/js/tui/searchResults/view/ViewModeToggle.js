define("tui/searchResults/view/ViewModeToggle", [
  "dojo",
  "dojo/dom-class",
  "dojo/_base/connect",
  "tui/widget/form/ToggleSwitch"], function (dojo, domClass, connect) {

  dojo.declare("tui.searchResults.view.ViewModeToggle", [tui.widget.form.ToggleSwitch], {

    // ----------------------------------------------------------------------------- properties

    stateMap: {
      'list' : 'off',
      'gallery' : 'on',
      'off': 'gallery',
      'on': 'list'
    },

    leftLabelTag: 'SRListView',

    rightLabelTag: 'SRGalleryView',

    cookieName: 'srViewMode',

    // ----------------------------------------------------------------------------- methods


    onAfterToggle: function(oldValue, newValue) {
      var resultViewDomNode = dojo.query(".result-view")[0];
      domClass.remove(resultViewDomNode, oldValue);
      domClass.add(resultViewDomNode, newValue);
      // publish rearranging
      connect.publish('tui.searchResults.view.SearchResultsComponent.setView', newValue);
    }

  });

  return tui.searchResults.view.ViewModeToggle;
});