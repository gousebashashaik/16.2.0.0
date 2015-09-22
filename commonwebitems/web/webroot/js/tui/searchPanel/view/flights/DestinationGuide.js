define('tui/searchPanel/view/flights/DestinationGuide', [
  'dojo',
  'dojo/_base/lang',
  'dojo/query',
  'dojo/on',
  'dojo/dom-attr',
  'dojo/dom-class',
  'dojo/text!tui/searchPanel/view/flights/templates/DestinationGuideTmpl.html',
  'dojo/text!tui/searchPanel/view/templates/DestinationGuideRecommendedDestinationsTmpl.html',
  'dojo/text!tui/searchPanel/view/templates/DestinationGuideCountryDestinationsTmpl.html',
  'dojo/text!tui/searchPanel/view/templates/DestinationGuideInspiredTmpl.html',
  'dojo/text!tui/searchPanel/view/flights/templates/DestinationGuideSelectOptionTmpl.html',
  'tui/searchPanel/model/DestinationModel',
  'tui/searchPanel/store/DestinationGuideStore',
  'tui/searchPanel/view/DestinationGuideSelectOption',
  'tui/searchPanel/view/nls/ListFormatter',
  'tui/widget/expand/SimpleExpandable',
  'dojox/dtl/tag/loader',
  "tui/searchPanel/view/SearchGuide"
], function (dojo, lang, query, on, domAttr, domClass, destGuideTmpl, recDestTmpl, countryDestTmpl, inspireTmpl, selOptionTmpl, DestinationModel, DestinationGuideStore, DestinationGuideSelectOption, ListFormatter) {

  dojo.declare('tui.searchPanel.view.flights.DestinationGuide', [tui.searchPanel.view.SearchGuide], {

    // ----------------------------------------------------------------------------- properties

    suggestions: null,

    countryList: null,

    tmpl: destGuideTmpl,

    recDestTmpl: recDestTmpl,

    inspireTmpl: inspireTmpl,

    destinationGuideStore: null,

    recommendedId: '',

    destinationGuideTitle: '', // Set later, see line 60 below.

    url: dojoConfig.paths.webRoot + '/holiday-destinations.html',

    imageurl: null,

    inspireText: '', // Set later, see line 61 below.

    inspireButton: '', // Set later, see below.

    listFormatter: null,

    listFormats: '',

    guideTimer: 0,

    subscribableMethods: ["openExpandable", "closeExpandable", "cancelBlur", "onDestinationGuideSelectOptionChange"],

    // number of columns
    columns: 3,

    // number of airports to render per column
    columnLength: 0,

    firstRender: true,

    // ----------------------------------------------------------------------------- methods

    postCreate: function () {
      var destinationGuide = this;

      //destinationGuide.initSearchMessaging();
      destinationGuide.inherited(arguments);

      destinationGuide.listFormatter = new ListFormatter();

      destinationGuide.inspireText = destinationGuide.searchMessaging.destinationGuide.inspireMe;
      destinationGuide.inspireButton = destinationGuide.searchMessaging.destinationGuide.button;
      destinationGuide.listFormats = destinationGuide.searchMessaging.listFormats;

      destinationGuide.recommendedId = destinationGuide.id;

      var resultSet = destinationGuide.searchPanelModel.to.query();
      resultSet.observe(function (destinationModel, remove, add) {
        // if expandable is not created yet, let's ignore.
        if (!destinationGuide.expandableDom) {
          return;
        }
        // update views
        destinationGuide.updateGuide(destinationModel, (add > -1));
        (add > -1) ? destinationGuide.toggleMultiSelect(destinationModel.multiSelect) : null;
        if(!destinationGuide.searchPanelModel.to.query().length) {
          destinationGuide.removeMultiSelect();
        }
        destinationGuide.updateGuideCount();
      });

      resultSet = destinationGuide.searchPanelModel.from.query();
      resultSet.observe(function (destinationModel, remove, add) {
        destinationGuide.updateGuideTitle();
      });

      on(document.body, "click", function (event) {
        if (document.activeElement === destinationGuide.domNode) return;
        if (destinationGuide.expandableDom === null || !destinationGuide.isShowing(destinationGuide.expandableDom)) return;
        destinationGuide.closeExpandable();
      });

      destinationGuide.tagElement(destinationGuide.domNode, "destination-list-link");
    },

    removeMultiSelect: function () {
      var destinationGuide = this;
      _.each(destinationGuide.suggestions, function(suggestion){
        _.each(suggestion.children, function(destination){
          if(destination.available) query('[for="recommended-'+destination.id+'"]', destinationGuide.expandableDom).removeClass('disabled');
        });
      });
    },

    toggleMultiSelect: function(multiSelect) {
      var destinationGuide = this;
      var queryStr = 'label input[data-destination-multiselect="'+multiSelect+'"]',
          enableNodes = query(queryStr, destinationGuide.expandableDom);

      // disable all
      query('label', destinationGuide.expandableDom).addClass('disabled');

      _.each(enableNodes, function(node){
        domClass.remove(query(node).parents('label')[0], 'disabled');
      });
    },

    // TODO: why is this here?
    attachEventListeners: function () {},

    onDestinationGuideSelectOptionChange: function (guideData) {
      var destinationGuide = this;
      destinationGuide.cancelBlur();

      dojo.addClass(destinationGuide.expandableDom, "loading");

      query(".default-inspired", destinationGuide.expandableDom).addClass("hidden");
      query(".default-destinations", destinationGuide.expandableDom).addClass("hidden");

      if (guideData.value === "") {
        // Recommended was clicked.
        dojo.removeClass(dojo.byId(destinationGuide.recommendedId + "-inspired"), "hidden");

        // Ask the server data for fresh recommendations.
        dojo.when(destinationGuide.destinationGuideStore.requestData(null, destinationGuide.searchPanelModel.generateQueryObject()), function (response) {
          destinationGuide.suggestions = response.suggestions;
          destinationGuide.countryList = response.destinationlist;
          destinationGuide.columnLength = Math.ceil(_.size(response.suggestions[0].children) / destinationGuide.columns);
          destinationGuide.updateSuggestionsList();
          dojo.removeClass(destinationGuide.expandableDom, "loading");
        });

      } else {
        // A country was clicked.
        var countryInspired = query("#" + guideData.value + "-inspired", destinationGuide.expandableDom);
        var countryDestination = query("#" + guideData.value + "-destinations", destinationGuide.expandableDom);

        if (countryInspired.length) {
          countryInspired.removeClass("hidden");
        }

        // Ask the server data about the selected country.
        dojo.when(destinationGuide.destinationGuideStore.requestData(guideData.value, destinationGuide.searchPanelModel.generateQueryObject()), function (response) {
          var html, inspireText, inspireData, countryData;

          inspireText = lang.replace(destinationGuide.searchMessaging.destinationGuide.inspireText, {country: guideData.key});

          inspireData = {
            id: response.id,
            url: response.url,
            inspireButton: destinationGuide.searchMessaging.destinationGuide.readMore,
            inspireText: inspireText
          };

          countryData = {
            toData: destinationGuide.searchPanelModel.to.data,
            searchMessaging: destinationGuide.searchMessaging
          };

          // Updating the inspired section.
          if (!countryInspired.length) {
            html = dojo.trim(tui.dtl.Tmpl.createTmpl(inspireData, inspireTmpl));
            var inspireElement = dojo.place(html, query("div.default-inspired", destinationGuide.expandableDom)[0], "after");
            destinationGuide.tagElement(query("a", inspireElement)[0], "read more " + guideData.value);
            countryInspired.removeClass("hidden");
          }

          // Updating the country destinations list.
          html = dojo.trim(tui.dtl.Tmpl.createTmpl(dojo.mixin(countryData, response), countryDestTmpl));
          if (!countryDestination.length) {
            dojo.place(html, query("div.default-destinations", destinationGuide.expandableDom)[0], "after");
            countryDestination.removeClass("hidden");
          } else {
            dojo.place(html, query("#" + guideData.value + "-destinations", destinationGuide.expandableDom)[0], "replace");
          }

          destinationGuide.tagElement(query(".country-search label", destinationGuide.expandableDom)[0], "Search all of " + guideData.key);
          dojo.removeClass(destinationGuide.expandableDom, "loading");
        });
      }
    },

    attachOpenEvent: function () {
      var destinationGuide = this;
      on(destinationGuide.domNode, "click", function (event) {
        if (dojo.hasClass(destinationGuide.domNode, "loading")) {
          return;
        }
        if (destinationGuide.expandableDom === null || !destinationGuide.isShowing(destinationGuide.expandableDom)) {
          dojo.publish("tui.searchPanel.view.flights.AirportGuide.closeExpandable");
          setTimeout(function () {
            destinationGuide.openExpandable();
          }, 350);
        } else {
          destinationGuide.closeExpandable();
        }
      });
    },

    cancelBlur: function () {
      var destinationGuide = this;
      if (destinationGuide.isShowing(destinationGuide.expandableDom)) {
        destinationGuide.inherited(arguments);
        destinationGuide.domNode.focus();
      }
    },

    onAfterTmplRender: function () {
      // summary:
      //		After the expandable dom has been created,
      //		we attach event listeners to the checkboxes and links
      var destinationGuide = this;
      destinationGuide.inherited(arguments);

      on(destinationGuide.expandableDom, "label:click", function (event) {
        dojo.stopEvent(event);

        var checkbox, label;
        if (event.target.tagName.toUpperCase() === "INPUT") {
          checkbox = event.target;
          label = event.target.parentElement;
        } else {
          checkbox = event.target.children[0];
          label = event.target;
        }

        if (dojo.hasClass(label, "disabled")) {
          return;
        }

        // Start the orange pulse on the field.
        dojo.publish("tui.searchPanel.view.flights.DestinationMultiFieldList.pulse", [destinationGuide.widgetController]);

        destinationGuide.updateWhereTo(checkbox, label);
        destinationGuide.cancelBlur();
      });

      // add dom event to unselect all link.
      on(destinationGuide.expandableDom, ".empty-destination-model:click", function (event) {
        dojo.stopEvent(event);
        if (dojo.hasClass(event.target, "inactive")) {
          return;
        }
        destinationGuide.clearAll();
      });

      // Set up the country list.
      //destinationGuide.destinationList = new DestinationGuideSelectOption(null, query(".destinationlist-dropdown .destinationlist", destinationGuide.expandableDom)[0]);
      // destinationGuide.destinationGuideStore.query(function)

      if (destinationGuide.pageContext === "destinationLandingPage") {
        dojo.setStyle(dojo.byId("tui_searchPanel_view_flights_DestinationGuide_0-inspired"), "display", "none");
      }

      // Tagging particular element.
      destinationGuide.tagElement(destinationGuide.expandableDom, "Destination List");
      //destinationGuide.tagElement(destinationGuide.destinationList.domNode, "Select a country");
      destinationGuide.tagElement(query(".close-hide", destinationGuide.expandableDom)[0], "Destination Hide");
      destinationGuide.tagElement(query(".empty-destination-model", destinationGuide.expandableDom)[0], "Destination Deselect All");
      destinationGuide.tagElement(query("#tui_searchPanel_view_DestinationGuide_0-inspired a")[0], "Browse Destinations");
      destinationGuide.tagElement(dojo.byId("dg-items"), "country group selections");

      destinationGuide.subscribe("tui/searchPanel/searchOpening", function (component) {
        if (component !== destinationGuide) {
          destinationGuide.closeExpandable();
        }
      });
    },

    openExpandable: function () {
      // summary:
      //		Override the default expandable behaviour,
      //		to only open once destination guide data is
      //		available and returned back from the server.
      var destinationGuide = this;
      var args = arguments;
      dojo.addClass(destinationGuide.domNode, "loading");

      // publish opening event for widgets that need to close
      destinationGuide.publishMessage("searchPanel/searchOpening");

      dojo.publish("tui.searchPanel.view.flights.DestinationMultiFieldList.highlight", [true, destinationGuide.widgetController]);

      dojo.when(destinationGuide.destinationGuideStore.requestData(null, destinationGuide.searchPanelModel.generateQueryObject()), function (response) {
        destinationGuide.suggestions = response.suggestions;
        destinationGuide.countryList = response.destinationlist;
        destinationGuide.columnLength = Math.ceil(_.size(response.suggestions[0].children) / destinationGuide.columns);

        destinationGuide.updateSuggestionsList();
        destinationGuide.inherited(args);
        destinationGuide.updateDestinationList();
        // Set the custom select option to the default value.
        if(!destinationGuide.firstRender) dojo.publish("tui.searchPanel.view.flights.DestinationGuideSelectOption.setSelectedValue", [""]);
        dojo.removeClass(destinationGuide.domNode, "loading");
        destinationGuide.firstRender = false;
      });
    },

    updateDestinationList: function () {
      var destinationGuide = this, html, destListDom, data;
      if(destinationGuide.destinationList) {
        destinationGuide.destinationList.destroyRecursive();
        destinationGuide.destinationList = null;
      }
      data = {
        countryList: destinationGuide.countryList,
        searchMessaging: destinationGuide.searchMessaging
      };

      html = dojo.trim(tui.dtl.Tmpl.createTmpl(data, selOptionTmpl));
      destListDom = dojo.place(html, query('.destinationlist-dropdown', destinationGuide.expandableDom)[0]);

      destinationGuide.destinationList = new DestinationGuideSelectOption(null, query(".destinationlist-dropdown .destinationlist", destinationGuide.expandableDom)[0]);
      destinationGuide.tagElement(destinationGuide.destinationList.domNode, "Country Select");
    },

    closeExpandable: function () {
      var destinationGuide = this;
      destinationGuide.inherited(arguments);
      if (destinationGuide.destinationList) {
        destinationGuide.destinationList.hideList();
      }
      dojo.publish('tui.searchPanel.view.flights.DestinationMultiFieldList.highlight', [false, destinationGuide.widgetController]);
    },

    updateSuggestionsList: function () {
      // summary:
      //      Call the server for a fresh list of suggested destinations.
      //      Rerender the list template inside the element.
      var destinationGuide = this;

      if (dojo.byId(destinationGuide.id + '-destinations')) {
        var html;

        // Updating the recommended destinations list.
        html = dojo.trim(tui.dtl.Tmpl.createTmpl(destinationGuide, recDestTmpl));
        dojo.place(html, query('#' + destinationGuide.id + '-destinations')[0], 'replace');

        // Time to reveal the recommended destinations.
        dojo.removeClass(dojo.byId(destinationGuide.id + '-destinations'), 'hidden');
      }
    },

    place: function (html) {
      // summary:
      //		Override place method from simpleExpandable,
      //		place the destination guide in the main search container.
      var destinationGuide = this;
      var target = destinationGuide.targetSelector ?
          query(destinationGuide.targetSelector, destinationGuide.widgetController.domNode)[0] :
          destinationGuide.widgetController.domNode;
      return dojo.place(html, target, "last");
    },

    getDestinationItemAttributes: function (destination) {
      // summary:
      //		Returns attributes for passed destination
      return new DestinationModel({
        name: domAttr.get(destination, 'data-destination-name'),
        id: domAttr.get(destination, 'data-destination-id'),
        type: domAttr.get(destination, 'data-destination-type'),
        multiSelect:  domAttr.get(destination, 'data-destination-multiselect')
      });
    },

    updateModel: function (destination, action) {
      // summary:
      //		Adds destination to model
      var destinationGuide = this;

      if (_.isArray(destination)) {
        _.forEach(destination, function (hotel) {
          destinationGuide.searchPanelModel.to[action]((action === "remove") ? hotel.id : hotel);
        });
      } else {
        destinationGuide.searchPanelModel.to[action]((action === "remove") ? destination.id : destination);
      }
    },

    updateWhereTo: function (clickedCheckbox, clickedLabel) {
      // summary:
      //		Updates Search model to match guide.
      var destinationGuide = this;
      var destinationModel, productModel = [], productHotels;

      var clickedCheckboxId = domAttr.get(clickedCheckbox, 'data-destination-id');

      // We look to the parent label tag class to see if selected or not.
      var add = !(dojo.hasClass(clickedLabel, 'selected'));
      var action = (add) ? 'addClass' : 'removeClass';

      // Ignore the click if the label is disabled.
      if (dojo.hasClass(clickedLabel, 'disabled')) {
        return;
      }

      // @FIXME: simplify product/item add/remove
      if (add) {

        destinationModel = destinationGuide.searchPanelModel.to.get(clickedCheckboxId);
        if (!destinationModel) {
          destinationModel = destinationGuide.getDestinationItemAttributes(clickedCheckbox);
        }

        destinationGuide.animatePill(clickedLabel, query('.where-to', destinationGuide.widgetController.domNode)[0], function () {
          destinationGuide.updateModel(productModel.length ? productModel : destinationModel, "add");
        });

      } else {
        // We are performing a removal from destination guide.
        if (!destinationGuide.getDestinationItemAttributes(clickedCheckbox).type) {
          // this is a product, remove children
          productModel.length = 0;
          productHotels = destinationGuide.destinationGuideStore.products.query({id: clickedCheckboxId})[0].hotels;
          _.forEach(productHotels, function (hotel) {
            productModel.push(hotel);
          });
        } else {
          destinationModel = destinationGuide.searchPanelModel.to.get(clickedCheckboxId);
        }

        if (productModel.length) {
          destinationGuide.updateModel(productModel, "remove");
        } else {
          if (destinationModel) {
            destinationGuide.updateModel(destinationModel, "remove");
          }
        }
      }

      domAttr.set(clickedCheckbox, 'checked', add);
      dojo[action](clickedLabel, 'selected');

      var clickedCheckboxChildren = domAttr.get(clickedCheckbox, 'data-destination-groups');

      // Get the destination group list.
      if (clickedCheckboxChildren) {
        clickedCheckboxChildren = clickedCheckboxChildren.split(',');
        _.each(clickedCheckboxChildren, function (id) {
          if (!id || id === "") {
            return;
          }

          // Remove all children. Even if the node is selected or not is not relevant here.
          destinationModel = destinationGuide.searchPanelModel.to.get(id);
          if (destinationModel) {
            destinationGuide.searchPanelModel.to.remove(id);
          }

          // Check the children to give visual feedback.
          var input = query('.' + id, destinationGuide.expandableDom);
          domAttr.set(input[0], 'checked', add);
          query(input).parent()[action]('selected');
        });
      }

      var clickedCheckboxParents = domAttr.get(clickedCheckbox, 'data-destination-parents');

      // Deselect all parents if a child gets unselected.
      if (clickedCheckboxParents) {
        clickedCheckboxParents = clickedCheckboxParents.split(',');
        if (!add) {
          // Remove all associated parent destinations.
          _.each(clickedCheckboxParents, function (id) {
            if (!id || id === "") {
              return;
            }

            var input = query('#' + id, destinationGuide.expandableDom);

            // Remove the objects from the model.
            destinationModel = destinationGuide.searchPanelModel.to.get(id);
            if (destinationModel) {
              // Remove the object itself.
              destinationGuide.searchPanelModel.to.remove(id);

              domAttr.set(input[0], 'checked', false);
              query(input).parent().removeClass('selected');

              // Add the children except the current checkbox.
              var children = domAttr.get(input[0], 'data-destination-groups');
              if (children) {
                children = children.split(',');

                _.each(children, function (childrenId) {
                  // If the parent's child is in the checkbox children, we exit.
                  if (!childrenId || childrenId === "" || dojo.indexOf(clickedCheckboxChildren, childrenId) >= 0 ||
                      childrenId === clickedCheckboxId) {
                    return;
                  }

                  var childCheckbox = query('#' + childrenId, destinationGuide.expandableDom);
                  var childLabel = childCheckbox.parent();

                  // Check if the child is disabled, if so return.
                  if (dojo.hasClass(childLabel[0], 'disabled')) {
                    return;
                  }

                  if (!destinationGuide.searchPanelModel.to.get(childrenId)) {
                    destinationModel = destinationGuide.getDestinationItemAttributes(childCheckbox[0]);
                    destinationGuide.searchPanelModel.to.add(destinationModel);
                  }
                });
              }

            } else {
              // Update the selected states of checkboxes, but not the model.
              domAttr.set(input[0], 'checked', add);
              query(input).parent().removeClass('selected');
            }
          });
        }
      }
    },

    updateGuide: function (item, add) {
      // summary:
      //		Updates guide, sets item state to match Search model
      var destinationGuide = this;
      var action = (add) ? 'addClass' : 'removeClass';

      // add/remove 'selected' class, select or unselect checkbox.
      if (item.children && item.children.length > 0) {
        _.each(item.children, function (child) {
          var input = query('.' + child, destinationGuide.expandableDom);
          if (input.length > 0) {
            domAttr.set(input[0], 'checked', true);
            query(input).parent()[action]('selected');
          }
        });
      } else {
        var input = query('.' + item.id, destinationGuide.expandableDom);
        if (input.length > 0) {
          domAttr.set(input[0], 'checked', true);
          query(input).parent()[action]('selected');
        }
      }
    },

    updateGuideTitle: function () {
      var destinationGuide = this;

      var airportModel = destinationGuide.searchPanelModel.from.query();
      var date = destinationGuide.searchPanelModel.date;
      var flexible = destinationGuide.searchPanelModel.flexible;

      airportModel = dojo.map(airportModel, function (obj) {
        return obj.name;
      });

      airportModel = destinationGuide.listFormatter.format(airportModel, destinationGuide.listFormats);

      if (airportModel === '' && date === null) {
        destinationGuide.destinationGuideTitle = destinationGuide.searchMessaging.destinationGuide.title;
      } else if (airportModel !== '' && date === null) {
        destinationGuide.destinationGuideTitle = destinationGuide.searchMessaging.destinationGuide.titleFromAirports;
      } else if (airportModel === '' && date !== null && !flexible) {
        destinationGuide.destinationGuideTitle = destinationGuide.searchMessaging.destinationGuide.titleOnDate;
      } else if (airportModel === '' && date !== null) {
        destinationGuide.destinationGuideTitle = destinationGuide.searchMessaging.destinationGuide.titleOnDateFlexible;
      } else if (airportModel !== '' && date !== null && !flexible) {
        destinationGuide.destinationGuideTitle = destinationGuide.searchMessaging.destinationGuide.titleFromAirportsOnDate;
      } else if (airportModel !== '' && date !== null) {
        destinationGuide.destinationGuideTitle = destinationGuide.searchMessaging.destinationGuide.titleFromAirportsOnDateFlexible;
      }

      destinationGuide.destinationGuideTitle = lang.replace(destinationGuide.destinationGuideTitle, {
        airports: airportModel,
        date: date,
        flexible: destinationGuide.flexibleDays
      });

      query('.destination-guide-title', destinationGuide.expandableDom).text(destinationGuide.destinationGuideTitle);
    },

    updateGuideCount: function () {
      // summary:
      //		Updates destination guide count with given value.
      var destinationGuide = this;
      // Remove inactive class if we have airports which we can remove.
      var emptyDestinationModel = query('.empty-destination-model', destinationGuide.expandableDom)[0];
      if (destinationGuide.searchPanelModel.to.data.length > 0) {
        dojo.removeClass(emptyDestinationModel, 'inactive');
      } else {
        dojo.addClass(emptyDestinationModel, 'inactive');
      }
    },

    clearAll: function () {
      // summary:
      //    Empties model when clicking 'unselect all' link.
      //    Removes 'selected' class from labels.
      var destinationGuide = this;
      destinationGuide.cancelBlur();
      _.each(destinationGuide.searchPanelModel.to.query(), function (destinationModel) {
        destinationGuide.searchPanelModel.to.remove(destinationModel.id);
      });

      // Remove all checked status of checkboxes.
      query('label.selected', destinationGuide.expandableDom).removeClass('selected');
      var checkboxes = query('input[type="checkbox"]', destinationGuide.expandableDom);
      _.each(checkboxes, function (checkbox) {
        domAttr.set(checkbox, 'checked', false);
      });
    }
  });

  return tui.searchPanel.view.flights.DestinationGuide;
});