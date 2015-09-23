define("tui/searchPanel/view/flights/ArrivalAirportMultiFieldList", [
  "dojo",
  "dojo/on",
  "dojox/dtl/filter/strings",
  "tui/searchPanel/config/SearchConfig",
  "tui/searchPanel/view/ErrorPopup",
  "tui/searchPanel/model/AirportModel",
  "tui/searchPanel/view/flights/SearchErrorMessaging",
  "tui/searchPanel/view/SearchMultiFieldList",
  "tui/searchPanel/view/flights/AirportAutoComplete"
], function (dojo, on, strings, searchConfig, ErrorPopup, AirportModel) {

  dojo.declare("tui.searchPanel.view.flights.ArrivalAirportMultiFieldList", [tui.searchPanel.view.SearchMultiFieldList,
    tui.searchPanel.view.flights.SearchErrorMessaging], {

    // ----------------------------------------------------------------------------- properties

    autocomplete: tui.searchPanel.view.flights.AirportAutoComplete,

    defaultPlaceholderTxt: null,

    limitPopupError: null,

    subscribableMethods: ["pulse", "highlight", "onTextboxInputBlur", "onNoException", "onException", "closeLimitPopup"],

    hasException: false,

    // ----------------------------------------------------------------------------- methods

    postCreate: function () {
      var arrivalAirportMultiFieldList = this;

      tui.searchPanel.view.flights.ArrivalAirportMultiFieldList.MAX_AIRPORTS_SELECTABLE = arrivalAirportMultiFieldList.maxTextboxes;

      arrivalAirportMultiFieldList.initSearchMessaging();
      arrivalAirportMultiFieldList.defaultPlaceholderTxt = arrivalAirportMultiFieldList.searchMessaging.to.foplaceholder;
      arrivalAirportMultiFieldList.itemsSelected = arrivalAirportMultiFieldList.searchMessaging.to.itemsSelected;

      arrivalAirportMultiFieldList.inherited(arguments);

     // dojo.query(arrivalAirportMultiFieldList.multifieldInfo).text(arrivalAirportMultiFieldList.searchMessaging.addMore);


      //building pills for textbox
      var resultSet = arrivalAirportMultiFieldList.searchPanelModel.to.query();

      //Airport pills adding code
     /* resultSet.observe(function (airportModel, remove, add) {
        var action = (add > -1) ? "addNewTextbox" : "removeTextbox";
        arrivalAirportMultiFieldList[action]({
          key: (airportModel.children && airportModel.children.length > 0) ? airportModel.name + ' (' + airportModel.children.length + ' ' + arrivalAirportMultiFieldList.searchMessaging.airports + ')' : airportModel.name,
          value: airportModel.id,
          name: arrivalAirportMultiFieldList.name
        });
      });
*/
      // sets valid route to remove sticky message on keydown
      on(arrivalAirportMultiFieldList.textboxInput, "keydown", function (event) {
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
          arrivalAirportMultiFieldList.searchPanelModel.searchErrorMessages.set("to", {});
          arrivalAirportMultiFieldList.searchPanelModel.validRoute = true;
        }
        if (arrivalAirportMultiFieldList.limitPopupError) {
          arrivalAirportMultiFieldList.limitPopupError.resize();
        }
      });

      arrivalAirportMultiFieldList.tagElement(arrivalAirportMultiFieldList.domNode, "FlyingtoSearch");
    },

  /*  toggleEmptyAlert: function (name, oldError, newError) {
        var arrivalAirportMultiFieldList = this;
        var action = (newError.emptyFromTo) ? "addClass" : "removeClass";
        dojo[action](arrivalAirportMultiFieldList.domNode, "error");
        dojo[action](arrivalAirportMultiFieldList.placeholder, "error");
      },*/


    /*  displayToError: function (name, oldError, newError) {
          // displays ' to' validation message if validation error occurs
          var destinationMultiFieldList = this;
          destinationMultiFieldList.validateErrorMessage(newError.emptyTo, {
            errorMessage: newError.emptyTo,
            arrow: true,
            errorPopupClass: "error-jumbo",
            floatWhere: "position-bottom-center",
            field: "emptyTo",
            key: "emptyTo"
          });
        },*/

   /* displayFromToError: function (name, oldError, newError) {
      // displays 'from, to' validation message if validation error occurs
      var arrivalAirportMultiFieldList = this;
      arrivalAirportMultiFieldList.validateErrorMessage(newError.emptyFromTo, {
        errorMessage: newError.emptyFromTo,
        errorPopupClass: "error-jumbo",
        floatWhere: "position-bottom-left",
        field: "fromTo",
        key: "emptyFromTo"
      });
    },*/

    // displays Invalid Airport Combination Error
    displayInvalidArrivalAirportCombinationError: function (name, oldError, newError) {
      var arrivalAirportMultiFieldList = this;
      arrivalAirportMultiFieldList.validateErrorMessage(newError.invalidArrivalAirportCombination, {
        errorMessage: newError.invalidArrivalAirportCombination,
        arrow:"true",
        errorPopupClass: "error-jumbo",
        floatWhere: "position-bottom-center",
        field: "invalidArrivalAirportCombination",
        key: "invalidArrivalAirportCombination"
      });
    },

    displayInvalidFromAndToFlyingCombinationError: function (name, oldError, newError) {
        var arrivalAirportMultiFieldList = this;
        arrivalAirportMultiFieldList.validateErrorMessage(newError.invalidFromAndToFlyingCombination, {
          errorMessage: newError.invalidFromAndToFlyingCombination,
          arrow:"true",
          errorPopupClass: "error-jumbo",
          floatWhere: "position-bottom-center",
          field: "invalidFromAndToFlyingCombination",
          key: "invalidFromAndToFlyingCombination"
        });
      },

    displayRouteError: function (name, oldError, newError) {
      var arrivalAirportMultiFieldList = this;
      // no errors to show, so close popup if opened.
      if (arrivalAirportMultiFieldList.infoPopup) {
        arrivalAirportMultiFieldList.infoPopup.close();
        arrivalAirportMultiFieldList.infoPopup.destroyRecursive();
        arrivalAirportMultiFieldList.infoPopup = null;
      }

      if (!newError || _.isEmpty(newError)) {
        return;
      }

      // create an error popup and display it.
      arrivalAirportMultiFieldList.infoPopup = new ErrorPopup({
        tmpl: arrivalAirportMultiFieldList.autocomplete.infoPopupTemplate,
        error: arrivalAirportMultiFieldList.autocomplete.error,
        elementRelativeTo: arrivalAirportMultiFieldList.domNode,
        floatWhere: "position-bottom-center",
        setPosOffset: function (position) {
          var tooltips = this;
          if (position === "position-bottom-center") {
            tooltips.posOffset = {top: 8, left: 0};
          }
        }
      }, null);

      arrivalAirportMultiFieldList.infoPopup.open();

      var airportGuide = dojo.query('.airport-guide', arrivalAirportMultiFieldList.infoPopup.popupDomNode),
          destGuide = dojo.query('.destination-guide', arrivalAirportMultiFieldList.infoPopup.popupDomNode),
          datePicker = dojo.query('.datepicker', arrivalAirportMultiFieldList.infoPopup.popupDomNode);

      var airportGuideText = arrivalAirportMultiFieldList.autocomplete.error.code === 'NO_MATCH_FOUND' ? 'See Airports From NoMatch' : 'See All Airports From';

      arrivalAirportMultiFieldList.tagElement(arrivalAirportMultiFieldList.infoPopup.popupDomNode, arrivalAirportMultiFieldList.autocomplete.error.code);
      destGuide.length ? arrivalAirportMultiFieldList.tagElement(destGuide[0], 'See Destinations From') : null;
      airportGuide.length ? arrivalAirportMultiFieldList.tagElement(airportGuide[0], airportGuideText) : null;
      datePicker.length ? arrivalAirportMultiFieldList.tagElement(datePicker[0], 'Airport Selected Date No Fly') : null;

      // if invalid route and destination guide clicked, remove whereTo items and open destination guide
      on(arrivalAirportMultiFieldList.infoPopup.popupDomNode, on.selector('.destination-guide', "click"), function (event) {
        var airportModel = null;

        // add entry item to whereFrom
        dojo.forEach(arrivalAirportMultiFieldList.autocomplete.error.entry, function (entry) {
          airportModel = dojo.mixin(new AirportModel(), entry);
          arrivalAirportMultiFieldList.setDataItem({
            key: airportModel.id,
            value: airportModel.name,
            listData: airportModel
          });
        });

        // remove match items from whereFrom
        dojo.forEach(arrivalAirportMultiFieldList.autocomplete.error.matches, function (match) {
          arrivalAirportMultiFieldList.searchPanelModel.from.remove(match.id);
        });

        // open destination guide
        dojo.publish("tui.searchPanel.view.flights.ArrivalAirportGuide.openExpandable");

        // clear error which will close popup.
        arrivalAirportMultiFieldList.searchPanelModel.searchErrorMessages.set("to", {});
      });

      // if invalid route and airport guide clicked, remove whereFrom items and open airport guide
      on(arrivalAirportMultiFieldList.infoPopup.popupDomNode, on.selector('.airport-guide', "click"), function (event) {

        // open airport guide
        //dojo.publish("tui.searchPanel.view.AirportGuide.openExpandable");
        dojo.publish("tui.searchPanel.view.flights.ArrivalAirportGuide.openGlobalExpandable", [arrivalAirportMultiFieldList.widgetController]);

        // reset input
        arrivalAirportMultiFieldList.resetTextboxInput();

        // clear error which will close popup.
        arrivalAirportMultiFieldList.searchPanelModel.searchErrorMessages.set("to", {});
      });

      // if invalid on date, add item and open datepicker
      on(arrivalAirportMultiFieldList.infoPopup.popupDomNode, on.selector('.datepicker', "click"), function (event) {
        var airportModel = null;

        // clear date field
        arrivalAirportMultiFieldList.searchPanelModel.set("when", null);

        // add entry to whereFrom
        dojo.forEach(arrivalAirportMultiFieldList.autocomplete.error.entry, function (entry) {
          airportModel = dojo.mixin(new AirportModel(), entry);
          arrivalAirportMultiFieldList.setDataItem({
            key: airportModel.id,
            value: airportModel.name,
            listData: airportModel
          });
        });

        // close popup
        arrivalAirportMultiFieldList.searchPanelModel.searchErrorMessages.set("to", {});

        // open datepicker
        dojo.publish("tui.searchPanel.view.flights.SearchDatePicker.onCalendarFocus");
      });
    },

    setDataItem: function (selectData) {
      // summary:
      //		Set the select data into the store. We ignore setting value if the selected item
      //		was the airport guide, or group/limit exception occurs.
      //      @returns: true if airport guide or no exception has occured
      var arrivalAirportMultiFieldList = this;
      if (selectData.value === 'airportguide') {
        arrivalAirportMultiFieldList.unselect();
        return true;
      }

      if (arrivalAirportMultiFieldList.searchPanelModel.to.isItemInGroup(selectData.listData.id) ||
          arrivalAirportMultiFieldList.searchPanelModel.to.isChildrenInGroup(selectData.listData.children)) {
        arrivalAirportMultiFieldList.hasException = true;
        return false;
      }

      var airportLength = arrivalAirportMultiFieldList.searchPanelModel.to.checkSelectedSize(selectData.listData);
      if (airportLength > arrivalAirportMultiFieldList.maxTextboxes) {
        arrivalAirportMultiFieldList.onTextboxLimit();
        arrivalAirportMultiFieldList.hasException = true;
        return false;
      }

      arrivalAirportMultiFieldList.hasException = false;
      arrivalAirportMultiFieldList.searchPanelModel.to.emptyStore();
      arrivalAirportMultiFieldList.searchPanelModel.to.add(selectData.listData);
      dijit.byId("where-to-text").closeExpandable();
      return true;
    },

    removeDataItem: function (selectData) {
      var arrivalAirportMultiFieldList = this;
      arrivalAirportMultiFieldList.searchPanelModel.to.remove(selectData.value);
      dojo.publish("tui/searchPanel/view/flights/ArrivalAirportGuide/ClearStorageFields");
    },

    onAddError: function () {
      var arrivalAirportMultiFieldList = this;
      dojo.addClass(arrivalAirportMultiFieldList.placeholder, "error");
    },

    onRemoveError: function () {
      var arrivalAirportMultiFieldList = this;
      dojo.removeClass(arrivalAirportMultiFieldList.placeholder, "error");
    },

    onTextboxLimit: function () {
      // summary:
      //		When textbox limit occurs, we create an ErrorPop with an appropriate message.
      var arrivalAirportMultiFieldList = this;
      if (!arrivalAirportMultiFieldList.limitPopupError) {
        arrivalAirportMultiFieldList.limitPopupError = new arrivalAirportMultiFieldList.searchErrorPopup({
          errorMessage: dojo.string.substitute(arrivalAirportMultiFieldList.searchMessaging.to.maxAirport, {
            size: arrivalAirportMultiFieldList.maxTextboxes
          }),
          errorPopupClass: "info",
          analyticsText: "Max Airports Error",
          elementRelativeTo: arrivalAirportMultiFieldList.domNode
        }, null);
      }
      arrivalAirportMultiFieldList.limitPopupError.open();
      arrivalAirportMultiFieldList.resetTextboxInput();
    },

    closeLimitPopup: function () {
      var arrivalAirportMultiFieldList = this;
      if (arrivalAirportMultiFieldList.limitPopupError) arrivalAirportMultiFieldList.limitPopupError.close();
      arrivalAirportMultiFieldList.hasException = false;
    },

    onElementListSelection: function (selectedData, listData, autocomplete) {
      var arrivalAirportMultiFieldList = this;
      autocomplete.hideList();
      if (!arrivalAirportMultiFieldList.isValidMatch(selectedData)) {
        arrivalAirportMultiFieldList.resetTextboxInput();
        arrivalAirportMultiFieldList.onTextboxInputFocus();
        return;
      }

      if (arrivalAirportMultiFieldList.setDataItem(selectedData)) {
        arrivalAirportMultiFieldList.onTextboxInputBlur();
      } else {
        arrivalAirportMultiFieldList.resetTextboxInput();
        arrivalAirportMultiFieldList.onTextboxInputFocus();
      }
    },

    onTextboxInputFocus: function (event) {
      var arrivalAirportMultiFieldList = this;
    	  // delete field errors if present
      var fromToError = dojo.clone(arrivalAirportMultiFieldList.searchPanelModel.searchErrorMessages.get("fromTo"));
      if (fromToError.emptyFromTo) {
        delete fromToError.emptyFromTo;
        arrivalAirportMultiFieldList.searchPanelModel.searchErrorMessages.set("fromTo", fromToError);
      }

      dojo.subscribe("tui.searchPanel.view.flights.arrivalAirportMultiFieldList.whereFrom", function () {
    	  //alert("alert called");
    	  //airportMultiFieldList.domNode.focus();
      });

      // delete no match error
      arrivalAirportMultiFieldList.searchPanelModel.searchErrorMessages.set("to", {});

      // publish opening event for widgets that need to close
      arrivalAirportMultiFieldList.publishMessage("searchPanel/searchOpening");

      // inherits from tui.widget.form.MultiFieldList
      arrivalAirportMultiFieldList.inherited(arguments);
    },

    onTextboxInputBlur: function (event) {
      var arrivalAirportMultiFieldList = this;
      // inherits from tui.widget.form.MultiFieldList
      arrivalAirportMultiFieldList.inherited(arguments);

      // close airport limit popup if showing
      arrivalAirportMultiFieldList.closeLimitPopup();

      // delete error if not route error
      var fieldErrors = arrivalAirportMultiFieldList.searchPanelModel.searchErrorMessages.get("to");
      if (!_.isEmpty(fieldErrors)) {
        switch (fieldErrors.errorCode) {
          case "INVALID_ROUTE_ON":
            break;
          case "INVALID_ROUTE_TO":
            break;
          case "INVALID_ROUTE_FROM":
            break;
          default:
            arrivalAirportMultiFieldList.searchPanelModel.searchErrorMessages.set("to", {});
        }
      }
    },

    /*updatePlaceholder: function () {

     var arrivalAirportMultiFieldList = this,
          maxTextWidth = 140,
          count = arrivalAirportMultiFieldList.searchPanelModel.to.summariseCount(),
          text = arrivalAirportMultiFieldList.defaultPlaceholderTxt,
          label = arrivalAirportMultiFieldList.textboxes.length ? arrivalAirportMultiFieldList.textboxes[0].label : arrivalAirportMultiFieldList.defaultPlaceholderTxt;

      if (arrivalAirportMultiFieldList.getTextWidth(label) > maxTextWidth) {
        label = strings.truncatewords(arrivalAirportMultiFieldList.textboxes[0].label, 3);
      } else {
        label = label + "&hellip;";
      }
      if (arrivalAirportMultiFieldList.searchPanelModel.to.selectedSize > 0 && count > 0) {
          text = count + " airports selected";
      }
      dojo.html.set(arrivalAirportMultiFieldList.placeholder, text);
    },*/

    onNoException: function () {
      var arrivalAirportMultiFieldList = this;
      arrivalAirportMultiFieldList.hasException = false;
    },

    onException: function () {
      var arrivalAirportMultiFieldList = this;
      arrivalAirportMultiFieldList.hasException = true;
    }
  });

  tui.searchPanel.view.flights.ArrivalAirportMultiFieldList.MAX_AIRPORTS_SELECTABLE = 6;

  return tui.searchPanel.view.flights.ArrivalAirportMultiFieldList;
});
