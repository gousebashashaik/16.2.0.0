define("tui/searchResults/view/SortResultsSelectOption", [
  "dojo/_base/declare",
  "dojo/query",
  "dojo/aspect",
  "dijit/registry",
  "tui/widget/form/SelectOption"], function (declare, query, aspect, registry) {

  return declare("tui.searchResults.view.SortResultsSelectOption", [tui.widget.form.SelectOption], {

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
      //Commented the if condition as sorting is not working in falcon.
      //if (sortResultsSelectOption.listData.length != 7){
	      if (field === 'sortBy') {
	        request.searchRequestType = 'Sort';
	      }
	      request.sortBy = value;
	      return request;
      //}
    },

    refresh: function () {},

    handleNoResults: function () {},

    addDropdownEventListener: function () {
    	var sortResultsSelectOption = this;
        aspect.after(sortResultsSelectOption, "onChange", function (name, oldValue, newValue) {
          if(sortResultsSelectOption.fireOnChange && oldValue !== newValue) registry.byId('mediator').fire('sortBy', oldValue, newValue);
        }, true);
    },

    updateValue: function (value) {
      var sortResultsSelectOption = this;
      sortResultsSelectOption.fireOnChange = false;
      sortResultsSelectOption.setSelectedValue(value);
      sortResultsSelectOption.fireOnChange = true;
    }

  });
});