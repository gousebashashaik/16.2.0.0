define("tui/smerchOverlayPanel/view/DestinationMultiFieldList", ["dojo",
  "dojo/cookie",
  "dojo/on",
  "dojox/dtl/filter/strings",
  'tui/searchPanel/config/SearchConfig',
  "tui/searchPanel/view/ErrorPopup",
  "tui/searchPanel/model/DestinationModel",
  "tui/searchPanel/view/SearchErrorMessaging",
  "tui/searchPanel/view/SearchMultiFieldList",
  "tui/smerchOverlayPanel/view/DestinationAutoComplete"
], function (dojo, cookie, on, strings, searchConfig, ErrorPopup, DestinationModel) {

  dojo.declare("tui.smerchOverlayPanel.view.DestinationMultiFieldList", [tui.searchPanel.view.SearchMultiFieldList, 
	tui.searchPanel.view.SearchErrorMessaging], {

    // ----------------------------------------------------------------------------- properties

    autocomplete: tui.smerchOverlayPanel.view.DestinationAutoComplete,

    defaultPlaceholderTxt: "Any destination",

    maxHeight: 126,

    scrollable: true,

    helpPopup: null,

    helpPopupCookie: null,

    subscribableMethods: ["pulse", "highlight", "onTextboxInputFocus", "onTextboxInputBlur", "addOnChangeEventListener"],

    blurTimeoutDuration: 120,

    // ----------------------------------------------------------------------------- methods

    postCreate: function () {
      var destinationMultiFieldList = this;

      destinationMultiFieldList.initSearchMessaging();
    //  dojo.query(destinationMultiFieldList.multifieldInfo).text(destinationMultiFieldList.searchMessaging.addMore);
     // destinationMultiFieldList.defaultPlaceholderTxt = destinationMultiFieldList.searchMessaging.to.placeholder;
      //destinationMultiFieldList.itemsSelected = destinationMultiFieldList.searchMessaging.to.itemsSelected;

      destinationMultiFieldList.inherited(arguments);
	  destinationMultiFieldList.addOnChangeEventListener();
      //	destinationMultiFieldList.addWatchers();
	},
	
	addOnChangeEventListener: function() {
		var destinationMultiFieldList = this;

      destinationMultiFieldList.helpPopup = cookie('destinationHelpPopup') ? null : new destinationMultiFieldList.searchErrorPopup({
        errorMessage: destinationMultiFieldList.searchMessaging[dojoConfig.site].to.popupMessage,
        elementRelativeTo: destinationMultiFieldList.domNode,
        errorPopupClass: "info"
      }, null);

      // sets valid route to remove sticky message on keydown
      on(destinationMultiFieldList.textboxInput, "keydown", function (event) {
        // close helpPopup if showing
        if (destinationMultiFieldList.helpPopup) {
          destinationMultiFieldList.helpPopup.close();
        }

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

        // first filter out ignoreKeys we won't ignore
        var ignoreKeys = _.filter(dojo.keys, function (v, key) {
          return _.indexOf(keysToKeep, key) === -1;
        });

        // from remaining ignoreKeys, remove if don't match keyCode
        ignoreKeys = _.filter(ignoreKeys, function (code) {
          return code === event.keyCode
        });

        // if no "ignore ignoreKeys" clear popup
        if (!ignoreKeys.length) {
          destinationMultiFieldList.searchPanelModel.searchErrorMessages.set("to", {});
          destinationMultiFieldList.searchPanelModel.validRoute = true;
        }
      });

      var resultSet = destinationMultiFieldList.searchPanelModel.to.query();
      resultSet.observe(function (destinationModel, remove, add) {
        var action = (add > -1) ? "addNewTextbox" : "removeTextbox";
        destinationMultiFieldList[action]({
          key: destinationModel.name,
          value: destinationModel.id,
          name: destinationMultiFieldList.name
        });
        if(!destinationModel.multiSelect){
        	dojo.query("ul.howLongDD li.active").removeClass("active");
        	if(action == "removeTextbox"){
    			dojo.query(".where-to .multifieldInfo").style("visibility", "visible") ;
        		dojo.query("ul.howLongDD li.lapLandDDLi").style("display", "none") ;
        		dojo.query("ul.howLongDD li:first-child").addClass("active");
        		on.emit(dojo.query("ul.howLongDD li:first-child")[0], "mouseover", {
 					bubbles: true,
 					cancelable: true
 				});
    			on.emit(dojo.query("ul.howLongDD li:first-child")[0], "click", {
 					bubbles: true,
 					cancelable: true
 				});
        	} else {
        		dojo.query(".where-to .multifieldInfo").style("visibility", "hidden") ;
        		dojo.query("ul.howLongDD li.lapLandDDLi").style("display", "block");
        		dojo.query("ul.howLongDD li.lapLandDDLi").addClass("active");
        		on.emit(dojo.query("ul.howLongDD li.lapLandDDLi")[0], "mouseover", {
 					bubbles: true,
 					cancelable: true
 				});
    			on.emit(dojo.query("ul.howLongDD li.lapLandDDLi")[0], "click", {
 					bubbles: true,
 					cancelable: true
 				});
        	}
        }
      });
      destinationMultiFieldList.tagElement(destinationMultiFieldList.domNode, "where-to");
    },

    toggleEmptyAlert: function (name, oldError, newError) {
      var destinationMultiFieldList = this;
      var action = (newError.emptyFromTo) ? "addClass" : "removeClass";
      dojo[action](destinationMultiFieldList.domNode, "error");
      dojo[action](destinationMultiFieldList.placeholder, "error");
    },

    displayToErrorMessage: function (name, oldErrorInfo, newErrorInfo) {
      var destinationMultiFieldList = this;
      // no errors to shows, so close popup if opened.
      if (destinationMultiFieldList.infoPopup) {
        destinationMultiFieldList.infoPopup.close();
        destinationMultiFieldList.infoPopup.destroyRecursive();
        destinationMultiFieldList.infoPopup = null;
      }
            
      if (!newErrorInfo || _.isEmpty(newErrorInfo)) {
        return;
      }

      // create an error popup and display it.
      destinationMultiFieldList.infoPopup = new ErrorPopup({
        tmpl: destinationMultiFieldList.autocomplete.infoPopupTemplate,
        error: destinationMultiFieldList.autocomplete.error,
        elementRelativeTo: destinationMultiFieldList.domNode,
        floatWhere: "position-bottom-center",
        setPosOffset: function (position) {
          var tooltips = this;
          if (position === "position-bottom-center") {
            tooltips.posOffset = {top: 8, left: 0};
          }
        }
      }, null);

      destinationMultiFieldList.infoPopup.open();

      var airportGuide = dojo.query('.airport-guide', destinationMultiFieldList.infoPopup.popupDomNode),
          destGuide = dojo.query('.destination-guide', destinationMultiFieldList.infoPopup.popupDomNode),
          datePicker = dojo.query('.datepicker', destinationMultiFieldList.infoPopup.popupDomNode),
          multiSelect = dojo.query('.multi-select', destinationMultiFieldList.infoPopup.popupDomNode);

      var destGuideText = destinationMultiFieldList.autocomplete.error.code === 'NO_MATCH_FOUND' ? 'See All Destinations NoMatch' : 'See All Destinations';

      destinationMultiFieldList.tagElement(destinationMultiFieldList.infoPopup.popupDomNode, destinationMultiFieldList.autocomplete.error.code);
      destGuide.length ? destinationMultiFieldList.tagElement(destGuide[0], destGuideText) : null;
      airportGuide.length ? destinationMultiFieldList.tagElement(airportGuide[0], 'See Departure Airports For') : null;
      datePicker.length ? destinationMultiFieldList.tagElement(datePicker[0], 'Selected Date No Fly') : null;
      multiSelect.length ? destinationMultiFieldList.tagElement(multiSelect[0], multiSelect[0].innerHTML) : null;

      on(destinationMultiFieldList.infoPopup.popupDomNode, on.selector('.multi-select', "click"), function (event) {
        var destination = null;
        // add entry to whereTo
        _.each(destinationMultiFieldList.autocomplete.error.entry, function (entry) {
          destination = dojo.mixin(new DestinationModel(), entry);
          destinationMultiFieldList.setDataItem({
            key: destination.id,
            value: destination.name,
            listData: destination
          });
        });

        // remove match items from whereTo
        _.each(destinationMultiFieldList.autocomplete.error.matches, function (match) {
          destinationMultiFieldList.searchPanelModel.to.remove(match.id);
        });

        // clear error which will close popup.
        destinationMultiFieldList.searchPanelModel.searchErrorMessages.set("to", {});

      });

      on(destinationMultiFieldList.infoPopup.popupDomNode, on.selector('.airport-guide', "click"), function (event) {
        var destination = null;

        // add entry item to whereTo
        _.each(destinationMultiFieldList.autocomplete.error.entry, function (entry) {
          destination = dojo.mixin(new DestinationModel(), entry);
          destinationMultiFieldList.setDataItem({
            key: destination.id,
            value: destination.name,
            listData: destination
          });
        });

        // remove match items from whereFrom
        _.each(destinationMultiFieldList.autocomplete.error.matches, function (match) {
          destinationMultiFieldList.searchPanelModel.from.remove(match.id);
        });

        // open airport expandable.
        dojo.publish("tui.searchPanel.view.AirportGuide.openExpandable");

        // clear error which will close popup.
        destinationMultiFieldList.searchPanelModel.searchErrorMessages.set("to", {});
      });

      // if invalid route and destination guide clicked, open destination guide
  /*     on(destinationMultiFieldList.infoPopup.popupDomNode, on.selector('.destination-guide', "click"), function (event) {
        // open destination guide
        dojo.publish("tui.searchBPanel.view.DestinationGuide.openExpandable");

        // reset input
        destinationMultiFieldList.resetTextboxInput();

        // clear error which will close popup.
        destinationMultiFieldList.searchPanelModel.searchErrorMessages.set("to", {});
      }); */

      on(destinationMultiFieldList.infoPopup.popupDomNode, on.selector('.datepicker', "click"), function (event) {
        var destination = null;

        // clear date field
        destinationMultiFieldList.searchPanelModel.set("when", null);

        // add entry to whereTo
        _.forEach(destinationMultiFieldList.autocomplete.error.entry, function (entry) {
          destination = dojo.mixin(new DestinationModel(), entry);
          destinationMultiFieldList.setDataItem({
            key: destination.id,
            value: destination.name,
            listData: destination
          });
        });

        // close popup
        destinationMultiFieldList.searchPanelModel.searchErrorMessages.set("to", {});

        // open datepicker
        dojo.publish("tui.smerchOverlayPanel.view.SearchDatePicker.onCalendarFocus", destinationMultiFieldList.widgetController.searchApi);
      });
    },

    /*addWatchers:function () {
     var destinationMultiFieldList = this;
     // adds watchers from error.
     destinationMultiFieldList.searchPanelModel.searchErrorMessages.watch("to", function (name, oldErrorInfo, newErrorInfo) {

     })
     },*/

    onTextboxInputFocus: function (event) {
      var destinationMultiFieldList = this;
      // inherits from tui.widget.form.MultiFieldList
      destinationMultiFieldList.inherited(arguments);

      // publish "close" to Where From field
     // dojo.publish("tui.searchBPanel.view.AirportMultiFieldList.onTextboxInputBlur");

      // delete Empty FromTo error if present
      var fromToError = dojo.clone(destinationMultiFieldList.searchPanelModel.searchErrorMessages.get("fromTo"));
      if (fromToError.emptyFromTo) {
          delete fromToError.emptyFromTo;
          destinationMultiFieldList.searchPanelModel.searchErrorMessages.set("fromTo", fromToError);
      }
      
   // delete no match error & Empty Destination error if present
      destinationMultiFieldList.searchPanelModel.searchErrorMessages.set("to", {});

      // show helpPopup if cookie not set
      if (destinationMultiFieldList.searchPanelModel.to.query().total === 0 && destinationMultiFieldList.helpPopup && !cookie('destinationHelpPopup')) {
        destinationMultiFieldList.helpPopup.open();
        cookie('destinationHelpPopup', true, {expires: 1 });
      }

		// destinationMultiFieldList.displayToErrorMessage();
      // publish opening event for widgets that need to close
      //destinationMultiFieldList.publishMessage("searchBPanel/searchOpening");
    },

    onTextboxInputBlur: function (event) {
      var destinationMultiFieldList = this;
      // inherits from tui.widget.form.MultiFieldList
      destinationMultiFieldList.inherited(arguments);

      // close helpPopup if showing
      if (destinationMultiFieldList.helpPopup) destinationMultiFieldList.helpPopup.close();          
    },

    setDataItem: function (selectData) {
      var destinationMultiFieldList = this;
      if (selectData.value === 'destinationguide') {
        return;
      }
      destinationMultiFieldList.searchPanelModel.to.add(selectData.listData);
    },

    removeDataItem: function (selectData) {
      var destinationMultiFieldList = this;
      destinationMultiFieldList.searchPanelModel.to.remove(selectData.value);
    },   
	
     updatePlaceholder: function () {
      var destinationMultiFieldList = this,
      maxTextWidth = 160,
      text = destinationMultiFieldList.defaultPlaceholderTxt,
      count = destinationMultiFieldList.searchPanelModel.to.summariseCount(),
      label = destinationMultiFieldList.textboxes[0] ? destinationMultiFieldList.textboxes[0].label : destinationMultiFieldList.defaultPlaceholderTxt;
      if (destinationMultiFieldList.getTextWidth(label) > maxTextWidth) {
          label = strings.truncatewords(destinationMultiFieldList.textboxes[0].label, 3);
      } else {
          label = label + "&hellip;";
      }
      if (destinationMultiFieldList.searchPanelModel.to.query().total > 1) {
        
        text = '<span class="placeholder-ellipsis">' + label + '</span>&nbsp;+' + count + ' more';
      }
      dojo.html.set(destinationMultiFieldList.placeholder, text);
    } 

  });

  return tui.smerchOverlayPanel.view.DestinationMultiFieldList;
});
