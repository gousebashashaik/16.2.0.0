define("tui/smerchOverlayPanel/view/DestinationAutoComplete", [
  "dojo",
  "dojo/on",
  "tui/searchPanel/model/DestinationModel",
  "dojo/text!tui/searchPanel/view/templates/NoResultsDestinations.html",
  "tui/searchPanel/view/SearchAutoComplete",
  "tui/search/nls/Searchi18nable"
], function (dojo, on, DestinationModel, noResultsTmpl) {

  dojo.declare("tui.smerchOverlayPanel.view.DestinationAutoComplete", [tui.searchPanel.view.SearchAutoComplete,
    tui.search.nls.Searchi18nable], {

    // ----------------------------------------------------------------------------- properties

    // target url for destination service.
    targetURL: dojoConfig.paths.webRoot + '/ws/smerchdestinations',

    // data property field for destination name.
    titleProp: 'name',

    // data property field for destination code.
    valueProp: 'id',

    // searchProperty for key search.
    searchProperty: 'searchKey',

    infoPopupTemplate: noResultsTmpl,

    // ----------------------------------------------------------------------------- methods

    postCreate: function () {
      var destinationAutocomplete = this;
      destinationAutocomplete.inherited(arguments);
      destinationAutocomplete.initSearchMessaging();
    },

    createListElement: function () {
      // summary:
      //      Extends createListElement method, and adds a css class called "autocomplete" to listElement.
      var destinationAutocomplete = this;
      destinationAutocomplete.inherited(arguments);
      dojo.addClass(destinationAutocomplete.listElement, "destination-autocomplete");
    },

    onBeforeSetResults: function (dataresults) {
      // summary:
      //		Method parses destination object json, into an
      //		single destination array.
      var destinationAutocomplete = this, data = null, results = dataresults.data;
      destinationAutocomplete.inherited(arguments);

      if(!results) return [];

      _.each(results, function(destData, destProp){
        data = data || [];
        if(destData) {
          // add collection title
          data.push(new DestinationModel({
            'id': 'title',
            'name': destinationAutocomplete.searchMessaging.keys[destProp]
          }));

          _.each(destData, function (item) {
            data.push(dojo.mixin(new DestinationModel(), item));
          });
        }

      });

      return data;
    },

    onResults: function (data) {
      // summary:
      //		Method is overriden from AutoCompleteable, so we can add destination guide option in list.
      var destinationAutocomplete = this;
      if(destinationAutocomplete.widgetController.searchApi === "searchPanel") {
			if (data && data.length > 0) {
				var destinationguide = {};
				destinationguide[destinationAutocomplete.titleProp] = destinationAutocomplete.searchMessaging.destinationMultiFieldList.showAllDestinations;
				destinationguide[destinationAutocomplete.valueProp] = "destinationguide";
				destinationguide[destinationAutocomplete.descriptionProp] = " ";
				data.push(destinationguide);
			}
		}
      destinationAutocomplete.inherited(arguments);
    },

    onNoResults: function (listElementUL) {
      // summary:
      //    Extend default method and publish guide link
      var destinationAutocomplete = this;
      destinationAutocomplete.hideList();
      if (destinationAutocomplete.error) {
        destinationAutocomplete.searchPanelModel.searchErrorMessages.set("to", {errorCode: destinationAutocomplete.error.code});
        if(destinationAutocomplete.widgetController.searchApi === "getPrice") {
        	dojo.query(".error a.destination-guide").style("display", "none");
        	dojo.query(".error p.or").style("display", "none");
        }
      }
    },

    onChange: function (name, oldValue, value) {
      // summary:
      //    If destination guide is selected, we have to let all listens to this channel know.
      var destinationAutocomplete = this;

      if (value === null) {
        return;
      }
      if (value.listData.id === "destinationguide") {
        dojo.publish("tui.searchPanel.view.DestinationGuide.openExpandable");
        return;
      }
      destinationAutocomplete.inherited(arguments);
    },

    onBeforeListItemRender: function (li, item) {
      var destinationAutocomplete = this;
      if (item.id === "destinationguide") {
        dojo.addClass(li, "guide-link destination-guide");
      }
      if (item.id === "title") {
        dojo.addClass(li, "title " + destinationAutocomplete.disableItemClass);
      }
    },

    onDisableSelection: function () {
      dojo.publish('tui.searchPanel.view.DestinationMultiFieldList.onTextboxInputFocus');
    },

    onEnterKey: function (event, domNode) {
      var destinationAutocomplete = this;
      destinationAutocomplete.inherited(arguments);

      // hard-coding focus to next search panel element
      // @TODO: fix this properly as this is a temporary hack for go-live
      if (destinationAutocomplete.widgetController.searchApi === "searchPanel" && dojo.keys.TAB === (event.charCode || event.keyCode)) {
        dojo.stopEvent(event);
        dojo.publish("tui.searchPanel.view.SearchDatePicker.focusCalendar", [destinationAutocomplete.widgetController]);
      }
    },

    onAfterRender: function () {
      var destinationAutocomplete = this;
      var node = dojo.query(".guide-link a", destinationAutocomplete.listNode)[0];
      if (node) {
        destinationAutocomplete.tagElement(node, "See All Destinations");
      }
    }
  });

  return tui.smerchOverlayPanel.view.DestinationAutoComplete;
});
