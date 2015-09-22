define("tui/filterPanel/view/BudgetToggle", [
  "dojo",
  "dojo/dom-class",
  "dojo/_base/xhr",
  "dojo/_base/connect",
  "tui/widget/form/ToggleSwitch"], function (dojo, domClass, xhr, connect) {

  function hide(element) {
    domClass.add(element, 'close');
  }

  function show(element) {
    domClass.remove(element, 'close');
  }

  dojo.declare("tui.filterPanel.view.BudgetToggle", [tui.widget.form.ToggleSwitch], {

    // ----------------------------------------------------------------------------- properties

    stateMap: {
      'pp' : 'on',
      'total' : 'off',
      'off': 'pp',
      'on': 'total'
    },

    leftLabelTag: 'budget-total-toggle',

    rightLabelTag: 'budget-pp-toggle',

    model: null,

    priceView:null,

    subscribableMethods: ["disableSliders", "enableSliders"],

    showSliders: true,

    cookieName: 'filterBudgetToggle',

    // ----------------------------------------------------------------------------- methods

    postCreate: function () {
      var budgetToggle = this;
      budgetToggle.inherited(arguments);

      budgetToggle.model = dijit.registry.byId('mediator').registerController(budgetToggle);

      budgetToggle.budgetContainer = dojo.byId('budgets');
      budgetToggle.totalSlider = dojo.query('.js-budget-total', budgetToggle.budgetContainer)[0];
      budgetToggle.perPersonSlider = dojo.query('.js-budget-pp', budgetToggle.budgetContainer)[0];

      //default hide as per configuration
      budgetToggle.togglePriceView(budgetToggle.priceView);
    },

    onAfterToggle: function(oldValue, newValue) {
      var budgetToggle = this;
      budgetToggle.togglePriceView(newValue);
    },

    togglePriceView: function (view) {
      var budgetToggle = this;
      budgetToggle.priceView = view;
      switch(view) {
        case "total":
          if(budgetToggle.showSliders) {
            budgetToggle.perPersonSlider ? hide(budgetToggle.perPersonSlider) : null;
            budgetToggle.totalSlider ? show(budgetToggle.totalSlider) : null;
          }
          connect.publish("tui:channel=priceToggle", {add : 'total', remove : 'pp'});
        	 budgetToggle.model.searchRequest.priceView = 'total';        	
          break;
        case "pp":
          if(budgetToggle.showSliders) {
            budgetToggle.perPersonSlider ? show(budgetToggle.perPersonSlider) : null;
            budgetToggle.totalSlider ? hide(budgetToggle.totalSlider) : null;
          }
          connect.publish("tui:channel=priceToggle", {remove : 'total', add : 'pp'});
        	 budgetToggle.model.searchRequest.priceView = 'pp';
          break;
      }
    },

    disableSliders: function() {
      var budgetToggle = this;
      budgetToggle.showSliders = false;
      budgetToggle.priceView === "total" ? hide(budgetToggle.totalSlider) : hide(budgetToggle.perPersonSlider);
    },

    enableSliders: function() {
      var budgetToggle = this;
      budgetToggle.showSliders = true;
      budgetToggle.priceView === "total" ? show(budgetToggle.totalSlider) : show(budgetToggle.perPersonSlider);
    },

    refresh: function () {
      var budgetToggle = this;
      budgetToggle.togglePriceView(budgetToggle.priceView);
    },

    handleNoResults: function () {},

    generateRequest: function() {}

  });

  return tui.filterPanel.view.BudgetToggle;
});