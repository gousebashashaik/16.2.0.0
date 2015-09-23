define("tui/searchPanel/view/flights/SubmitButton", [
  "dojo",
  "dojo/on",
  "dojo/has",
  "tui/utils/SessionStorage",
  "tui/search/nls/Searchi18nable",
  "tui/widget/_TuiBaseWidget"], function (dojo, on, has, sessionStore) {

  dojo.declare("tui.searchPanel.view.flights.SubmitButton", [tui.widget._TuiBaseWidget, tui.search.nls.Searchi18nable], {

    // ----------------------------------------------------------------------------- properties

    form: null,

    formSelector: null,

    targetUrl: dojoConfig.paths.webRoot + "/packages",

    // ----------------------------------------------------------------------------- methods

    postCreate: function () {
      var submitButton = this;
      submitButton.inherited(arguments);

      submitButton.initSearchMessaging();

      submitButton.form = dojo.query(submitButton.formSelector, submitButton.getParent.domNode)[0];

      // trigger model submit (validate > publish)
      on(submitButton.domNode, "click", function (event) {

    	  /*TODO: Temporary fix for duplicate search error messages @ search click. */
    	  dojo.global.isFromErrorMessageNew = true;
    	  dojo.global.isToErrorMessageNew = true;
    	  dojo.global.isWhenErrorMessageNew = true;
    	  dojo.global.isReturnErrorMessageNew = true;
    	  dojo.global.isPartyCompositionErrorNew = true;

        submitButton.searchPanelModel.submit();
      });

      // add/remove focus class on focus/blur (usability)
      on(submitButton.domNode, "focus, blur", function (event) {
        switch (event.type) {
          case "focus":
            dojo.addClass(submitButton.domNode, "focus");
            break;
          case "blur":
            dojo.removeClass(submitButton.domNode, "focus");
            break;
        }
      });

      submitButton.subscribe("tui:channel=validSearchModel", function (searchCriteria, searchPanelModel) {
        var submitButton = this;
        var savedState = sessionStore.getItem('searchResultsState');
        if (savedState) {
          sessionStore.removeItem('searchResultsState');
        }
        if (searchPanelModel == submitButton.searchPanelModel) {
          dojo.addClass(submitButton.widgetController.domNode, "searching");
          submitButton.populateSearchForm(searchCriteria);
          submitButton.form.submit();
          // ios hack to close modal before unloading "back" is pressed - only needed for modal search form
          if (has("agent-ios") && submitButton.widgetController.searchApi === "getPrice") {
            dojo.publish("tui.searchGetPrice.view.GetPriceModal.close");
          }
        }

      });

      submitButton.tagElement(submitButton.domNode, submitButton.domNode.innerHTML);
    },

    populateSearchForm: function (searchCriteria) {
      // summary:
      // populates search form with hidden fields containing search criteria
      var submitButton = this;



      flyingFromAirports = [],
      flyingToAirports = [];
      _.forEach(searchCriteria.flyingFrom, function (from) {
    	  flyingFromAirports.push(from.id)
      });
      _.forEach(searchCriteria.flyingTo, function (to) {
    	  flyingToAirports.push(to.id+ ":" +to.name)
      });

      submitButton.createInput("flyingFrom[]", flyingFromAirports.join("|"));
      submitButton.createInput("flyingTo[]", flyingToAirports.join("|"));

      var searchCriteriaCopy = dojo.clone(searchCriteria);
     /* delete searchCriteriaCopy.airports;
      delete searchCriteriaCopy.units;
      */
      _.forEach(searchCriteriaCopy, function (value, key) {
    	  if(key !== "flyingFrom" && key !== "flyingTo"){
    		  submitButton.createInput(key, value);
    	  }
      });

      // send "main search" request type
      submitButton.createInput("searchType", "pricegrid");
      /*submitButton.createInput("sp", "true");*/
      submitButton.createInput("isFlexible", "Y");
     /* if(_.size(searchCriteria.units) > 0)
        submitButton.createInput("multiSelect", _.first(searchCriteria.units)['multiSelect']);
      */
        // Add brandType if in bookflow & getPrice modal
      // TODO: remove redundant checks
      if(submitButton.widgetController.searchApi === 'getPrice' && submitButton.widgetController.jsonData) {
        if(_.has(submitButton.widgetController.jsonData, "packageData")) {
          if(_.has(submitButton.widgetController.jsonData.packageData, "brandType")) {
            submitButton.createInput("brandType", submitButton.widgetController.jsonData.packageData.brandType);
          }
        }
      }
    },

    createInput: function (name, value) {
      // summary:
      //    creates input fields for search form
      var submitButton = this;
      var input = dojo.create("input", {"type": "hidden", "name": name, "value": value}, submitButton.form);
    }

  });

  return tui.searchPanel.view.flights.SubmitButton;
});
