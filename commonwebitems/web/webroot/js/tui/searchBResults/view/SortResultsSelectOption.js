define("tui/searchBResults/view/SortResultsSelectOption", [
  "dojo/_base/declare",
  "dojo/query",
  "dojo/aspect",
  "dijit/registry",
  "tui/widget/form/SelectOption"], function (declare, query, aspect, registry) {

  return declare("tui.searchBResults.view.SortResultsSelectOption", [tui.widget.form.SelectOption], {

    // ----------------------------------------------------------------------------- properties

    fireOnChange: true,

    subscribableMethods: ["resize", "hideList", "updateValue"],

    // ----------------------------------------------------------------------------- methods

    postCreate: function () {
      var sortResultsSelectOption = this;
      registry.byId('mediator').registerController(sortResultsSelectOption);
      sortResultsSelectOption.inherited(arguments);
      sortResultsSelectOption.addDropdownEventListener();
      sortResultsSelectOption.tagElement(sortResultsSelectOption.domNode, "SBMain");
      _.each(query('li', sortResultsSelectOption.listElement), function (item) {
        sortResultsSelectOption.tagElement(item, item.innerHTML);
      });
    },

    //this fn is recreated with a closure when value changes.
    generateRequest: function (field) {
      var sortResultsSelectOption = this,
          value = sortResultsSelectOption.getSelectedData().value,
          request = {};
      if (field === 'sortBy') {
        request.searchRequestType = 'Sort';
      }
      request.sortBy = value;
      return request;
    },

    refresh: function () {},

    handleNoResults: function () {},

    addDropdownEventListener: function () {
      var sortResultsSelectOption = this;
      aspect.after(sortResultsSelectOption, "onChange", function (name, oldValue, newValue) {
        if(sortResultsSelectOption.fireOnChange) registry.byId('mediator').fire('sortBy', oldValue, newValue);
      });
    },

    updateValue: function (value) {
      var sortResultsSelectOption = this;
      sortResultsSelectOption.fireOnChange = false;
      sortResultsSelectOption.setSelectedValue(value);
      sortResultsSelectOption.fireOnChange = true;
    }

  });
});