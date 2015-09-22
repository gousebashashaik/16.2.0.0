define("tui/searchPanel/view/AirportAutoComplete", ["dojo",
	"tui/searchPanel/model/AirportModel",
	"dojo/text!tui/searchPanel/view/templates/NoResultsAirports.html",
	"tui/search/nls/Searchi18nable",
	"tui/searchPanel/view/SearchAutoComplete"], function (dojo, AirportModel, noResultsTmpl) {

	dojo.declare("tui.searchPanel.view.AirportAutoComplete", [tui.searchPanel.view.SearchAutoComplete,
		tui.search.nls.Searchi18nable], {

		// ----------------------------------------------------------------------------- properties

		//targetURL: '/datafeed/autocomplete.php',
		targetURL: dojoConfig.paths.webRoot + '/ws/airports',

		// data property field for airport title.
		titleProp:'name',

		// data property field for airport code.
		valueProp:'id',

		// searchProperty for key search.
		searchProperty:'searchKey',

		infoPopupTemplate: noResultsTmpl,

		// ----------------------------------------------------------------------------- methods

		postCreate:function () {
			// summary:
			//		Called post widget creation.
			var airportAutocomplete = this;
			airportAutocomplete.inherited(arguments);
			airportAutocomplete.initSearchMessaging();
		},

		onBeforeSetResults:function (dataresults) {
			// summary:
			//		Method parses destination object json, into an
			//		single destination array.
			//var properties = ["Countries", "Destinations", "Hotels", "Products"];
			var airportAutocomplete = this;
			airportAutocomplete.inherited(arguments);

			var data = [];
			dataresults = dataresults.airports;
			if (!dataresults) {
				return data;
			}
			_.forEach(dataresults, function (airportItem, i) {
				data.push(dojo.mixin(new AirportModel(), airportItem));
			});

			return data;
		},

		onResults:function (data) {
			// summary:
			//		Method is overriden from AutoCompleteable, so we can add airport guide option in list.
			var airportAutocomplete = this;
			if (data !== null && data.length > 0) {
				var airportguide = {};
				airportguide[airportAutocomplete.titleProp] = airportAutocomplete.searchMessaging.airportMultiFieldList.showAllAirport;
				airportguide[airportAutocomplete.valueProp] = "airportguide";
				data.push(airportguide);
			}
			airportAutocomplete.inherited(arguments);
		},

		onNoResults:function (listElementUL) {
			// summary:
			//    Extend default method and publish guide link
			var airportAutocomplete = this;
			airportAutocomplete.hideList();
			if (airportAutocomplete.error) {
				airportAutocomplete.searchPanelModel.searchErrorMessages.set("from", {errorCode:airportAutocomplete.error.code})
			}
		},

		onChange:function (name, oldValue, value) {
			// summary:
			//    If airport guide is selected, we have to let all listens to this channel know.
			var airportAutocomplete = this;
			if (value === null) {
				return;
			}
			if (value.listData.id === "airportguide") {
				dojo.publish("tui.searchPanel.view.AirportGuide.openGlobalExpandable", [airportAutocomplete.widgetController]);
				return;
			}
			airportAutocomplete.inherited(arguments);
		},

        onType: function(element, event) {
            var airportAutocomplete = this;
            airportAutocomplete.inherited(arguments);
            if(element.value.length >= airportAutocomplete.charNo) {
                dojo.publish("tui.searchPanel.view.AirportMultiFieldList.closeLimitPopup");
            }
        },

		onBeforeListItemRender:function (li, item) {
			if (item.id === "airportguide") {
				dojo.addClass(li, "guide-link airport-guide");
			}
		},

		renderList:function () {
			var airportAutocomplete = this;
			var string = [];
			airportAutocomplete.clearList();
			airportAutocomplete.onBeforeRender(airportAutocomplete, airportAutocomplete.listData);
			_.forEach(airportAutocomplete.listData, function (item, index) {
				string.push(item[airportAutocomplete.titleProp]);
				// add airport group count
				if (item.children && item.children.length > 0) {
					string.push('&ndash; <em>' + item.children.length + ' ');
					string.push((item.children.length === 1 ? airportAutocomplete.searchMessaging.airport : airportAutocomplete.searchMessaging.airports) + '</em>');
				}
				// add synonym
				if (item.synonym) {
					string.push('(' + item.synonym + ')');
				}
				var li = dojo.create("li", {
					innerHTML:airportAutocomplete.onRenderLiContent(string.join(' '), item)
				});
				string.length = 0;
				airportAutocomplete.onBeforeListItemRender(li, item, airportAutocomplete);
				dojo.query(li).data("list-data", airportAutocomplete.createListData(item));
				airportAutocomplete.addToList(li);
				airportAutocomplete._connectEvents(li, index);
				airportAutocomplete.onAfterListItemRender(li, item, airportAutocomplete);
			});
			airportAutocomplete.onAfterRender(airportAutocomplete, airportAutocomplete.listData);
			airportAutocomplete.isScrollable();
		},

        onEnterKey: function(event, domNode) {
            var airportAutocomplete = this;
            airportAutocomplete.inherited(arguments);

            // hard-coding focus to next search panel element
            // @TODO: fix this properly as this is a temporary hack for go-live
            if(airportAutocomplete.widgetController.searchApi === "searchPanel" && dojo.keys.TAB === (event.charCode || event.keyCode)) {
                dojo.stopEvent(event);
                dojo.publish("tui.searchPanel.view.DestinationMultiFieldList.onTextboxInputFocus");
            }
        },

        onAfterRender: function () {
            var airportAutocomplete = this;
            var node = dojo.query(".guide-link a", airportAutocomplete.listNode)[0];
            if(node) {
                airportAutocomplete.tagElement(node, "See All Airports From");
            }
        }

	});

	return tui.searchPanel.view.AirportAutoComplete;
});
