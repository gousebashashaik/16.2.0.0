define("tui/searchBResults/view/HolidayDurationView", ["dojo",
  "dojo/dom-attr",
  "dojo/on",
  "dojo/_base/lang",
  "dojo/keys",
  "dojo/text!tui/searchBResults/view/templates/holidayDurationTmpl.html",
  "dojo/topic",
  "dojo/cookie",
  "tui/searchBResults/view/InfoPopup",
  "tui/searchB/nls/Searchi18nable",
  "tui/widget/mixins/Templatable",
  "tui/widget/_TuiBaseWidget"], function (dojo, domAttr, on, lang, keys, durationTmpl, topic, cookie, Popup) {

  dojo.declare("tui.searchBResults.view.HolidayDurationView", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable, tui.searchB.nls.Searchi18nable], {

    // ----------------------------------------------------------------------------- properties

    customDatesOptionView: null,

    tmpl: durationTmpl,

    templateView: "initialize",

    duration: null,

    maxDuration: 0,

    durationMaxNightsMsg: "",

    selectYourOwnMsg: "",

    moreChoices: null,

    customTab: null,

    errorMessage: null,

    model: null,

    dataPath: "searchResult.durationSelection",

    // info popup displayed on load (if not cancelled)
    infoPopup: null,

    // duration for which to show the info popup
    infoPopupDuration: 10000,

    subscribableMethods: ["displayInfoPopup"],

    // ----------------------------------------------------------------------------- mediator methods

    generateRequest: function (field) {
      var holidayDurationView = this;
      var request = {
        'duration': holidayDurationView.model.activeDuration
      };
      if (field === 'duration') {
        request.searchRequestType = 'Duration';
      }
      return request;
    },

    clearErrors: function () {
      var holidayDurationView = this;
      if (holidayDurationView.errorMessage.length > 0) {
        _.forEach(holidayDurationView.errorMessage, function (item) {
          dojo.destroy(dojo.query(item, holidayDurationView.domNode)[0]);
        });
      }
    },

    getMaxDuration: function () {
      var holidayDurationView = this;
      return holidayDurationView.maxDuration;
    },

    isCustomDuration: function (duration) {
    	this.model.defaultDisplay = this.model.defaultDisplay.splice(0,3);
      return _.indexOf(this.model.defaultDisplay, duration) === -1;
    },

    refresh: function (field, oldDuration, newDuration, response) {
      var holidayDurationView = this;
      if(field === 'clearAll') return;
      this.clearErrors();
      holidayDurationView.model = response;
      if (field === 'duration') {
        if (holidayDurationView.isCustomDuration(newDuration)) {
          if (!holidayDurationView.customTab) {
            holidayDurationView.addCustomTab(newDuration);
          } else {
            holidayDurationView.updateCustomTab(newDuration);
          }
          holidayDurationView.closeMoreOptionsList();
        }
        holidayDurationView.setActiveTab(newDuration);
      } else {
        if (holidayDurationView.isCustomDuration(response.activeDuration)) {
          if (!holidayDurationView.customTab) {
            holidayDurationView.addCustomTab(response.activeDuration);
          } else {
            holidayDurationView.updateCustomTab(response.activeDuration);
          }
          holidayDurationView.closeMoreOptionsList();
        }
        holidayDurationView.setActiveTab(response.activeDuration);
      }
    },

    clear: function (durations) {
      console.log('clearing durations with : ' + durations);
    },

    handleNoResults: function (name, oldDuration, newDuration) {
      var holidayDurationView = this;
      if (name === 'duration') {
        if (holidayDurationView.errorMessage.length > 0) {
          _.forEach(holidayDurationView.errorMessage, function (item) {
            dojo.destroy(dojo.query(item, holidayDurationView.domNode)[0]);
          });
        }
        var html = holidayDurationView.renderTemplate("error", {"message": holidayDurationView.searchMessaging[dojoConfig.site].errors.durationNoResults});
        holidayDurationView.errorMessage.push(dojo.place(html, dojo.query(".customize", holidayDurationView.domNode)[0], "last"));

        if (holidayDurationView.isCustomDuration(newDuration)) {
          if (!holidayDurationView.isCustomDuration(oldDuration) && holidayDurationView.customTab) {
            holidayDurationView.removeCustomTab();
          }
          holidayDurationView.resetCustomDuration();
        } else {
          topic.publish("tui.searchBResults.view.SearchResultsComponent.renderNoResultsPopup");
        }
        //reset to old duration
        holidayDurationView.model.activeDuration = oldDuration;
        holidayDurationView.setActiveTab(oldDuration);
      }
    },

    // ----------------------------------------------------------------------------- holidayDurationView methods

    postCreate: function () {
      // summary:
      //      Sets default values for class properties.

      var holidayDurationView = this;
      holidayDurationView.model = dijit.registry.byId('mediator').registerController(holidayDurationView);

      holidayDurationView.initSearchMessaging();

      // query relevant domNodes for future manipulation
      holidayDurationView.defaultDisplayTabs = dojo.query(".duration-standard", holidayDurationView.domNode);
      holidayDurationView.moreChoicesMenu = dojo.query(".custom-dropdown.dropdown", holidayDurationView.domNode);
      holidayDurationView.moreChoicesOptions = dojo.query(".preset", holidayDurationView.domNode);
      holidayDurationView.customChoice = dojo.query(".customize", holidayDurationView.domNode);
      holidayDurationView.errorMessage = [];

      // i18n string replacements
      holidayDurationView.durationMaxNightsMsg = lang.replace(
          holidayDurationView.searchMessaging[dojoConfig.site].errors.durationMaxNights, {MAX_NIGHTS: holidayDurationView.getMaxDuration()}
      );
      holidayDurationView.selectYourOwnMsg = lang.replace(
          holidayDurationView.searchMessaging.holidayDuration.selectYourOwn, {MAX_NIGHTS: holidayDurationView.getMaxDuration()}
      );

      // attach event listeners
      holidayDurationView.attachDurationViewEventListeners();

      holidayDurationView.inherited(arguments);

      if (_.indexOf(holidayDurationView.model.defaultDisplay, holidayDurationView.model.activeDuration) === -1) {
        holidayDurationView.addCustomTab(holidayDurationView.model.activeDuration);
      } else {
        holidayDurationView.setActiveTab(holidayDurationView.model.activeDuration);
      }

      holidayDurationView.tagElements(dojo.query(".preset", holidayDurationView.domNode), function (DOMElement) {
        return DOMElement && DOMElement.innerHTML;
      });
      holidayDurationView.tagElements(dojo.query(".duration-standard", holidayDurationView.domNode), function (DOMElement) {
        var aElement = DOMElement.children[0];
        return aElement && aElement.innerHTML +" nights";
      });
      if( dojo.query(".search-results .single-accommodation-details .product").length ){
    	  holidayDurationView.tagElements(dojo.query(".custom-dropdown", holidayDurationView.domNode), "SASseeDurations");
      }else{
      holidayDurationView.tagElements(dojo.query(".custom-dropdown", holidayDurationView.domNode), "More Durations");
      }
      holidayDurationView.tagElements(dojo.query("input", holidayDurationView.domNode), "Text Durations");
      holidayDurationView.tagElements(dojo.query("a.button", holidayDurationView.domNode), "Search Durations");

      // show info popup
      dojo.subscribe("tui:channel=lazyload", function () {
        // adding a random string as we want to ignore the cookie settings in the singleaccom view
        holidayDurationView.displayInfoPopup(false);
      });
    },

    displayInfoPopup: function (ignoreCookie) {
      var holidayDurationView = this;
      // don't show if only one duration
      if (holidayDurationView.model['defaultDisplay'].length === 1) return;

      // destroy popup if already created > if duration has changed we need to update display and positioning
      if (holidayDurationView.helpPopup) {
        holidayDurationView.helpPopup.destroyRecursive();
        holidayDurationView.helpPopup = null;
      }

      // on single accom we ignore if cookie has been set and display when link is clicked
      if (ignoreCookie) {
        holidayDurationView.helpPopup = holidayDurationView.createInfoPopup();
      } else {
        holidayDurationView.helpPopup = cookie('durationInfoPopup') ? null : holidayDurationView.createInfoPopup();
      }

      if (holidayDurationView.helpPopup) holidayDurationView.helpPopup.open();
    },

    createInfoPopup: function () {
      var holidayDurationView = this;

      var activeTab = dojo.query(".duration-standard", holidayDurationView.domNode).filter(function (node) {
        return dojo.hasClass(node, "active")
      })[0];
	  //Removing the popup/tootltp
      if(holidayDurationView.searchMessaging.holidayDuration.popupMessage.toLowerCase().indexOf("you can change durations here") != -1
	     && ( dojoConfig.site == "thomson" || dojoConfig.site == "firstchoice" )){
	       return;
	  }
	  else{
      return new Popup({
        floatWhere: "position-top-center",
        heading: lang.replace(
            holidayDurationView.searchMessaging.holidayDuration.popupHeading, {DURATION: holidayDurationView.model.activeDuration}
        ),
        message: holidayDurationView.searchMessaging.holidayDuration.popupMessage,
        removeText: holidayDurationView.searchMessaging.holidayDuration.dontShow,
        elementRelativeTo: activeTab,
        showDuration: holidayDurationView.infoPopupDuration,
        posOffset: {
          left: 0,
          top: -18
        }
      }, null);
	  }
    },

    removeDurations: function () {
      // summary:
      //      Removes all duration options
      var holidayDurationView = this;
      dojo.destroy(dojo.query("ul", holidayDurationView.domNode)[0]);
    },

    attachDurationViewEventListeners: function () {
      // summary:
      //      Adds event listeners to view
      var holidayDurationView = this;

      // standard tabs listener
      on(holidayDurationView.domNode, on.selector(".duration-standard", "click, keyup"), function (event) {
        //dojo.stopEvent(event);
        var currentTab = this;

        if (event.keyCode && event.keyCode !== keys.ENTER) return;

        // set infoPopup cookie on click
        cookie('durationInfoPopup', true, {expires: 1 });

        // close infoPopup if open
        dojo.publish("tui.searchBResults.view.InfoPopup.close");

        // ignore if already active
        if (dojo.hasClass(currentTab, "active")) return;

        holidayDurationView.defaultDisplayTabs.removeClass("active");
        holidayDurationView.setDuration(currentTab);
      });

      on(dojo.body(), "click", function () {
        holidayDurationView.closeMoreOptionsList();
      });

      // more options listener
      on(holidayDurationView.domNode, on.selector(".custom-dropdown", "click, keyup"), function (event) {
        dojo.stopEvent(event);
        if (event.keyCode && event.keyCode !== keys.ENTER) return;

        // close infoPopup if open
        dojo.publish("tui.searchBResults.view.InfoPopup.close");

        // set infoPopup cookie on click
        cookie('durationInfoPopup', true, {expires: 1 });

        holidayDurationView.openMoreOptionsList(this, event);
      });

      // listen to preset options
      on(holidayDurationView.domNode, on.selector(".preset", "click, keyup"), function (event) {
        //dojo.stopEvent(event);
        if (event.keyCode && event.keyCode !== keys.ENTER) {
          return;
        }
        holidayDurationView.setDuration(this);
        holidayDurationView.closeMoreOptionsList();
      });

      // listen to custom option
      on(dojo.query('.customize .button', holidayDurationView.domNode)[0], 'click', function () {
        holidayDurationView.setCustomDuration(holidayDurationView.domNode);
      });

      on(dojo.query('.customize .button', holidayDurationView.domNode)[0], 'keypress', function (event) {
        if (event.keyCode === keys.ENTER || event.keyCode === keys.TAB) {
          //dojo.stopEvent(event);
          holidayDurationView.setCustomDuration(holidayDurationView.domNode);
        }
      });

      on(dojo.query('.customize .custom-duration-textfield', holidayDurationView.domNode)[0], 'keypress', function (event) {
        if (event.keyCode === keys.ENTER) {
          //dojo.stopEvent(event);
          holidayDurationView.setCustomDuration(holidayDurationView.domNode);
        }
      });

    },

    openMoreOptionsList: function (moreOptions, event) {
      var holidayDurationView = this;

      // close list if open and either list element or "more options text" or "arrow" are clicked
      var moreOptionsTargets = [moreOptions];
      _.forEach(dojo.query("span", moreOptions), function (item) {
        moreOptionsTargets.push(item);
      });
      if (dojo.hasClass(moreOptions, "active") && (_.indexOf(moreOptionsTargets, event.target) > -1)) {
        holidayDurationView.closeMoreOptionsList();
        return;
      }

      // open list
      dojo.addClass(moreOptions, "active");

      topic.publish("tui/filterBPanel/view/FilterController/closeFilterPanel");
    },

    closeMoreOptionsList: function () {
      var holidayDurationView = this;
      holidayDurationView.moreChoicesMenu.removeClass("active");
    },

    setDuration: function (tab) {
      var holidayDurationView = this;
      var tabDuration = domAttr.get(tab, "data-dojo-value");
      var oldDuration = holidayDurationView.model.activeDuration;
      var newDuration = parseInt(tabDuration, 10);
      holidayDurationView.model.activeDuration = newDuration;
      dijit.registry.byId('mediator').fire('duration', oldDuration, newDuration);
    },

    resetCustomDuration: function () {
      var holidayDurationView = this;
      var input = dojo.query('input', holidayDurationView.domNode)[0]
      input ? input.value = '' : null;
    },

    showError: function (message) {
      var holidayDurationView = this;
      holidayDurationView.clearErrors();
      var html = holidayDurationView.renderTemplate("error", {"message": message});
      holidayDurationView.errorMessage.push(dojo.place(html, dojo.query(".customize", holidayDurationView.domNode)[0], "last"));
    },

    setCustomDuration: function (node) {
      // summary:
      //      Sets duration of user-input custom duration to searchResultsModel
      var holidayDurationView = this;
      var input = dojo.query("input", node)[0];
      //var button = dojo.query(".button", node)[0];
      var placeholder = domAttr.get(input, "placeholder");

      // todo: move validation to model
      if (!holidayDurationView.testNumber(input.value)) {
        //holidayDurationView.searchResultsModel.searchErrorMessages.set("duration", {durationInputError: holidayDurationView.searchMessaging.errors.durationInputError});
        holidayDurationView.showError(holidayDurationView.searchMessaging[dojoConfig.site].errors.durationInputError);
        return;
      }
      if (parseInt(input.value, 10) > holidayDurationView.getMaxDuration()) {
        //holidayDurationView.searchResultsModel.searchErrorMessages.set("duration", {durationMaxNights: holidayDurationView.durationMaxNightsMsg});
        holidayDurationView.showError(holidayDurationView.durationMaxNightsMsg);
        return;
      }
      /*if (!_.isEmpty(holidayDurationView.searchResultsModel.searchErrorMessages.get("duration"))) {
       holidayDurationView.showError('');
       //holidayDurationView.searchResultsModel.searchErrorMessages.set("duration", {});
       }*/

      if (input.value !== placeholder || input.value !== "") {
        var oldDuration = holidayDurationView.model.activeDuration;
        var newDuration = parseInt(input.value, 10);
        holidayDurationView.model.activeDuration = newDuration;
        dijit.registry.byId('mediator').fire('duration', oldDuration, newDuration);
      }
    },

    setActiveTab: function (value) {
      // summary:
      //      Deactivates all tabs and sets tab with currently selected value to active
      var holidayDurationView = this;
      holidayDurationView.defaultDisplayTabs.removeClass("active");
      _.forEach(holidayDurationView.defaultDisplayTabs, function (tab) {
        if (parseInt(domAttr.get(tab, "data-dojo-value"), 10) === value) {
          dojo.addClass(tab, "active");
        }
      });
    },

    addCustomTab: function (value) {
      // summary:
      //      Adds a custom tab when user selects duration from "more options" menu
      var holidayDurationView = this;
      holidayDurationView.defaultDisplayTabs.removeClass("active");
      var html = holidayDurationView.renderTemplate("newtab", { value: value });
      dojo.place(html, dojo.query(".pill-buttons", holidayDurationView.domNode)[0], "last");
      holidayDurationView.customTab = dojo.byId("duration-custom");
      holidayDurationView.defaultDisplayTabs = dojo.query(".duration-standard", holidayDurationView.domNode);
    },

    updateCustomTab: function (value) {
      // summary:
      //      Updates custom tab with new selected value
      var holidayDurationView = this;
      var a = dojo.query("a", holidayDurationView.customTab)[0];
      domAttr.set(holidayDurationView.customTab, "data-dojo-value", value);
      (value === 1) ? [a.innerHTML = "Day Trip",dojo.addClass(holidayDurationView.customTab,'day-trip')]: [a.innerHTML = value,dojo.removeClass(holidayDurationView.customTab,'day-trip')];
    },

    removeCustomTab: function () {
      // summary:
      //      Removes custom tab
      var holidayDurationView = this;
      dojo.destroy(holidayDurationView.customTab);
      holidayDurationView.customTab = null;
    },

    renderTemplate: function (/*String*/msgType, /*Object?*/data) {
      // summary:
      //      Renders a template
      var holidayDurationView = this;
      holidayDurationView.templateView = msgType;
      var renderData = data ? dojo.mixin(holidayDurationView, data) : holidayDurationView;
      return holidayDurationView.renderTmpl(null, renderData);
    },

    testNumber: function (value) {
      // summary:
      //      Tests if given value is a number
      //      Returns: boolean
      return parseInt(value, 10) === Number(value) && parseInt(value, 10) > 0;
    }

  });

  return tui.searchBResults.view.HolidayDurationView;
});