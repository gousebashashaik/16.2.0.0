define("tui/searchBPanel/view/AirportMultiFieldList", [
  "dojo",
  "dojo/on",
  "dojox/dtl/filter/strings",
  "tui/searchPanel/config/SearchConfig",
  "tui/searchBPanel/view/ErrorPopup",
  "tui/searchBPanel/model/AirportModel",
  "tui/searchBPanel/view/SearchErrorMessaging",
  "tui/searchBPanel/view/SearchMultiFieldList",
  "tui/searchBPanel/view/AirportAutoComplete"
], function (dojo, on, strings, searchConfig, ErrorPopup, AirportModel) {

  dojo.declare("tui.searchBPanel.view.AirportMultiFieldList", [tui.searchBPanel.view.SearchMultiFieldList,
    tui.searchBPanel.view.SearchErrorMessaging], {

    // ----------------------------------------------------------------------------- properties

    autocomplete: tui.searchBPanel.view.AirportAutoComplete,

    defaultPlaceholderTxt: null,

    limitPopupError: null,

    subscribableMethods: ["pulse", "highlight", "onTextboxInputBlur", "onNoException", "onException", "closeLimitPopup"],

    hasException: false,

    // ----------------------------------------------------------------------------- methods

    postCreate: function () {
      var airportMultiFieldList = this;

      tui.searchBPanel.view.AirportMultiFieldList.MAX_AIRPORTS_SELECTABLE = airportMultiFieldList.maxTextboxes;

      airportMultiFieldList.initSearchMessaging();
      airportMultiFieldList.defaultPlaceholderTxt = airportMultiFieldList.searchMessaging.from.placeholder;
      airportMultiFieldList.itemsSelected = airportMultiFieldList.searchMessaging.from.itemsSelected;

      airportMultiFieldList.inherited(arguments);

      dojo.query(airportMultiFieldList.multifieldInfo).text(airportMultiFieldList.searchMessaging.addMore);

      var resultSet = airportMultiFieldList.searchPanelModel.from.query();
      resultSet.observe(function (airportModel, remove, add) {
        var action = (add > -1) ? "addNewTextbox" : "removeTextbox";
        airportMultiFieldList[action]({
          key: (airportModel.children && airportModel.children.length > 0) ? airportModel.name + ' (' + airportModel.children.length + ' ' + airportMultiFieldList.searchMessaging.airports + ')' : airportModel.name,
          value: airportModel.id,
          name: airportMultiFieldList.name
        });
      });

      // sets valid route to remove sticky message on keydown
      on(airportMultiFieldList.textboxInput, "keydown", function (event) {
        var keysToKeep = [
          "BACKSPACE",
          "SPACE",
          "DELETE",
          "NUMPAD_0",
          "NUMPAD_1",
          "NUMPAD_2",
          "NUMPAD_3",
          "NUMPAD_4",
          "NUMPAD_5",
          "NUMPAD_6",
          "NUMPAD_7",
          "NUMPAD_8",
          "NUMPAD_9"
        ];

        // first filter out keys we won't ignore
        var ignoreKeys = _.filter(dojo.keys, function (v, key) {
          return _.indexOf(keysToKeep, key) === -1;
        });
        // from remaining ignoreKeys, remove if don't match keyCode
        ignoreKeys = _.filter(ignoreKeys, function (code) {
          return code === event.keyCode;
        });
        // if no "ignoreKeys" clear popup
        if (!ignoreKeys.length) {
          airportMultiFieldList.searchPanelModel.searchErrorMessages.set("from", {});
          airportMultiFieldList.searchPanelModel.validRoute = true;
        }
        if (airportMultiFieldList.limitPopupError) {
          airportMultiFieldList.limitPopupError.resize();
        }
      });

      airportMultiFieldList.tagElement(airportMultiFieldList.domNode, "where-from");
    },

    displayFromToError: function (name, oldError, newError) {
      // displays 'from, to' validation message if validation error occurs
      var airportMultiFieldList = this;
      airportMultiFieldList.validateErrorMessage(newError.emptyFromTo, {
        errorMessage: newError.emptyFromTo,
        errorPopupClass: "error-jumbo",
        floatWhere: "position-bottom-left",
        field: "fromTo",
        key: "emptyFromTo"
      });
    },

    displayRouteError: function (name, oldError, newError) {
      var airportMultiFieldList = this;
      // no errors to show, so close popup if opened.
      if (airportMultiFieldList.infoPopup) {
        airportMultiFieldList.infoPopup.close();
        airportMultiFieldList.infoPopup.destroyRecursive();
        airportMultiFieldList.infoPopup = null;
      }		
		
      if (!newError || _.isEmpty(newError)) {
        return;
      }

      // create an error popup and display it.
      airportMultiFieldList.infoPopup = new ErrorPopup({
        tmpl: airportMultiFieldList.autocomplete.infoPopupTemplate,
        error: airportMultiFieldList.autocomplete.error,
        elementRelativeTo: airportMultiFieldList.domNode,
        floatWhere: "position-bottom-center",
        setPosOffset: function (position) {
          var tooltips = this;
          if (position === "position-bottom-center") {
            tooltips.posOffset = {top: 8, left: 0};
          }
        }
      }, null);

      airportMultiFieldList.infoPopup.open();

      var airportGuide = dojo.query('.airport-guide', airportMultiFieldList.infoPopup.popupDomNode),
          destGuide = dojo.query('.destination-guide', airportMultiFieldList.infoPopup.popupDomNode),
          datePicker = dojo.query('.datepicker', airportMultiFieldList.infoPopup.popupDomNode);

      var airportGuideText = airportMultiFieldList.autocomplete.error.code === 'NO_MATCH_FOUND' ? 'See Airports From NoMatch' : 'See All Airports From';

      airportMultiFieldList.tagElement(airportMultiFieldList.infoPopup.popupDomNode, airportMultiFieldList.autocomplete.error.code);
      destGuide.length ? airportMultiFieldList.tagElement(destGuide[0], 'See Destinations From') : null;
      airportGuide.length ? airportMultiFieldList.tagElement(airportGuide[0], airportGuideText) : null;
      datePicker.length ? airportMultiFieldList.tagElement(datePicker[0], 'Airport Selected Date No Fly') : null;

      // if invalid route and destination guide clicked, remove whereTo items and open destination guide
      on(airportMultiFieldList.infoPopup.popupDomNode, on.selector('.destination-guide', "click"), function (event) {
        var airportModel = null;

        // add entry item to whereFrom
        _.forEach(airportMultiFieldList.autocomplete.error.entry, function (entry) {
          airportModel = dojo.mixin(new AirportModel(), entry);
          airportMultiFieldList.setDataItem({
            key: airportModel.id,
            value: airportModel.name,
            listData: airportModel
          });
        });

        // remove match items from whereTo
        _.forEach(airportMultiFieldList.autocomplete.error.matches, function (match) {
          airportMultiFieldList.searchPanelModel.to.remove(match.id);
        });

        // open destination guide
        dojo.publish("tui.searchBPanel.view.DestinationGuide.openExpandable");

        // clear error which will close popup.
        airportMultiFieldList.searchPanelModel.searchErrorMessages.set("from", {});
      });

      // if invalid route and airport guide clicked, remove whereFrom items and open airport guide
      on(airportMultiFieldList.infoPopup.popupDomNode, on.selector('.airport-guide', "click"), function (event) {

        // open airport guide
        //dojo.publish("tui.searchPanel.view.AirportGuide.openExpandable");
        dojo.publish("tui.searchBPanel.view.AirportGuide.openGlobalExpandable", [airportMultiFieldList.widgetController]);

        // reset input
        airportMultiFieldList.resetTextboxInput();

        // clear error which will close popup.
        airportMultiFieldList.searchPanelModel.searchErrorMessages.set("from", {});
      });

      // if invalid on date, add item and open datepicker
      on(airportMultiFieldList.infoPopup.popupDomNode, on.selector('.datepicker', "click"), function (event) {
        var airportModel = null;

        // clear date field
        airportMultiFieldList.searchPanelModel.set("when", null);

        // add entry to whereFrom
        _.forEach(airportMultiFieldList.autocomplete.error.entry, function (entry) {
          airportModel = dojo.mixin(new AirportModel(), entry);
          airportMultiFieldList.setDataItem({
            key: airportModel.id,
            value: airportModel.name,
            listData: airportModel
          });
        });

        // close popup
        airportMultiFieldList.searchPanelModel.searchErrorMessages.set("from", {});

        // open datepicker
        dojo.publish("tui.searchBPanel.view.SearchDatePicker.onCalendarFocus");
      });
    },

    setDataItem: function (selectData) {
      // summary:
      //		Set the select data into the store. We ignore setting value if the selected item
      //		was the airport guide, or group/limit exception occurs.
      //      @returns: true if airport guide or no exception has occured
      var airportMultiFieldList = this;
      if (selectData.value === 'airportguide') {
        airportMultiFieldList.unselect();
        return true;
      }

      if (airportMultiFieldList.searchPanelModel.from.isItemInGroup(selectData.listData.id) ||
          airportMultiFieldList.searchPanelModel.from.isChildrenInGroup(selectData.listData.children)) {
        airportMultiFieldList.hasException = true;
        return false;
      }

      var airportLength = airportMultiFieldList.searchPanelModel.from.checkSelectedSize(selectData.listData);
      if (airportLength > airportMultiFieldList.maxTextboxes) {
        airportMultiFieldList.onTextboxLimit();
        airportMultiFieldList.hasException = true;
        return false;
      }

      airportMultiFieldList.hasException = false;
      airportMultiFieldList.searchPanelModel.from.add(selectData.listData);
      return true;
    },

    removeDataItem: function (selectData) {
      var airportMultiFieldList = this;
      airportMultiFieldList.searchPanelModel.from.remove(selectData.value);
    },

    onAddError: function () {
      var airportMultiFieldList = this;
      dojo.addClass(airportMultiFieldList.placeholder, "error");
    },

    onRemoveError: function () {
      var airportMultiFieldList = this;
      dojo.removeClass(airportMultiFieldList.placeholder, "error");
    },

    onTextboxLimit: function () {
      // summary:
      //		When textbox limit occurs, we create an ErrorPop with an appropriate message.
      var airportMultiFieldList = this;
      if (!airportMultiFieldList.limitPopupError) {
        airportMultiFieldList.limitPopupError = new airportMultiFieldList.searchErrorPopup({
          errorMessage: dojo.string.substitute(airportMultiFieldList.searchMessaging.from.maxAirport, {
            size: airportMultiFieldList.maxTextboxes
          }),
          errorPopupClass: "info",
          analyticsText: "Max Airports Error",
          elementRelativeTo: airportMultiFieldList.domNode
        }, null);
      }
      airportMultiFieldList.limitPopupError.open();
      airportMultiFieldList.resetTextboxInput();
    },

    closeLimitPopup: function () {
      var airportMultiFieldList = this;
      if (airportMultiFieldList.limitPopupError) airportMultiFieldList.limitPopupError.close();
      airportMultiFieldList.hasException = false;
    },

    onElementListSelection: function (selectedData, listData, autocomplete) {
      var airportMultiFieldList = this;
      autocomplete.hideList();
      if (!airportMultiFieldList.isValidMatch(selectedData)) {
        airportMultiFieldList.resetTextboxInput();
        airportMultiFieldList.onTextboxInputFocus();
        return;
      }

      if (airportMultiFieldList.setDataItem(selectedData)) {
        airportMultiFieldList.onTextboxInputBlur();
      } else {
        airportMultiFieldList.resetTextboxInput();
        airportMultiFieldList.onTextboxInputFocus();
      }
    },

    onTextboxInputFocus: function (event) {
      var airportMultiFieldList = this;

      // delete Empty FromTo error if present
      var fromToError = dojo.clone(airportMultiFieldList.searchPanelModel.searchErrorMessages.get("fromTo"));
      if (fromToError.emptyFromTo) {
        delete fromToError.emptyFromTo;
        airportMultiFieldList.searchPanelModel.searchErrorMessages.set("fromTo", fromToError);
      }
                 
      // delete no match error & Empty Departure airport error if present
      airportMultiFieldList.searchPanelModel.searchErrorMessages.set("from", {});

      // publish opening event for widgets that need to close
      airportMultiFieldList.publishMessage("searchBPanel/searchOpening");

      // inherits from tui.widget.form.MultiFieldList
      airportMultiFieldList.inherited(arguments);
    },

    onTextboxInputBlur: function (event) {
      var airportMultiFieldList = this;
      // inherits from tui.widget.form.MultiFieldList
      airportMultiFieldList.inherited(arguments);

      // close airport limit popup if showing
      airportMultiFieldList.closeLimitPopup();

      // delete error if not route error 
      var fieldErrors = airportMultiFieldList.searchPanelModel.searchErrorMessages.get("from");
      if(!_.isEmpty(fieldErrors)){
        switch (fieldErrors.errorCode) {
          case "INVALID_ROUTE_ON":
            break;
          case "INVALID_ROUTE_TO":
            break;
          case "INVALID_ROUTE_FROM":
            break;
          default:
            airportMultiFieldList.searchPanelModel.searchErrorMessages.set("from", {});
        }
      }     
    },

    updatePlaceholder: function () {
      var airportMultiFieldList = this,
          count = airportMultiFieldList.searchPanelModel.from.summariseCount(),
          text = airportMultiFieldList.defaultPlaceholderTxt,
          label;

      if (airportMultiFieldList.searchPanelModel.from.selectedSize > 1 && count > 0) {
        label = airportMultiFieldList.textboxes.length ? airportMultiFieldList.textboxes[0].label : airportMultiFieldList.defaultPlaceholderTxt;
        text = '<span class="placeholder-ellipsis">' + label + '</span>&nbsp;+' + count + ' more';
      }
      airportMultiFieldList.placeholder.innerHTML = text;
    },

    onNoException: function () {
      var airportMultiFieldList = this;
      airportMultiFieldList.hasException = false;
    },

    onException: function () {
      var airportMultiFieldList = this;
      airportMultiFieldList.hasException = true;
    }
  });

  tui.searchBPanel.view.AirportMultiFieldList.MAX_AIRPORTS_SELECTABLE = 6;

  return tui.searchBPanel.view.AirportMultiFieldList;
});
