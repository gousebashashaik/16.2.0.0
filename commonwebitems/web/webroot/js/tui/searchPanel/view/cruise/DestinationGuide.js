define('tui/searchPanel/view/cruise/DestinationGuide', [
  'dojo',
  'dojo/_base/lang',
  'dojo/query',
  'dojo/on',
  'dojo/dom-attr',
  'dojo/dom-class',
  'dojo/text!tui/searchPanel/view/cruise/templates/DestinationGuideTmpl.html',
  'dojo/text!tui/searchPanel/view/cruise/templates/DestinationGuideRegionTmpl.html',
  'dojo/text!tui/searchPanel/view/cruise/templates/DestinationGuideTopCountriesTmpl.html',
  'dojo/text!tui/searchPanel/view/cruise/templates/DestinationGuideItineraryPocTmpl.html',
  'tui/searchPanel/model/DestinationModel',
  'tui/searchPanel/store/cruise/DestinationGuideStore',
  'tui/searchPanel/view/DestinationGuideSelectOption',
  'tui/searchPanel/view/nls/ListFormatter',
  'tui/searchResults/view/Tooltips',
  'tui/widget/expand/SimpleExpandable',
  'tui/searchPanel/view/cruise/DestinationAutoComplete',
  'tui/mvc/Klass',
  'dojox/dtl/tag/loader',
  "tui/searchPanel/view/SearchGuide",
  "tui/searchPanel/view/SearchErrorMessaging"
], function (dojo, lang, query, on, domAttr, domClass, destGuideTmpl, regionDestTmpl, countryDestTmpl, selOptionTmpl, DestinationModel, DestinationGuideStore, DestinationGuideSelectOption, ListFormatter) {

  dojo.declare('tui.searchPanel.view.cruise.DestinationGuide', [tui.searchPanel.view.SearchGuide,tui.searchPanel.view.SearchErrorMessaging], {

    // ----------------------------------------------------------------------------- properties

    suggestions: null,

    countryList: null,

    tmpl: destGuideTmpl,

    regionDestTmpl: regionDestTmpl,

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

    placeholder:null,

    regionsColumnLength: 1,

    countriesColumnLength: 1,

    userSelections: null,

    // ----------------------------------------------------------------------------- methods



    postCreate: function () {
      var destinationGuide = this;

      destinationGuide.initSearchMessaging();
      destinationGuide.inherited(arguments);

      destinationGuide.placeholder = dojo.query(".placehold", destinationGuide.domNode)[0];
      destinationGuide.updatePlaceHolder();

      destinationGuide.listFormatter = new ListFormatter();

      //destinationGuide.inspireText = destinationGuide.searchMessaging.destinationGuide.inspireMe;
      //destinationGuide.inspireButton = destinationGuide.searchMessaging.destinationGuide.button;
      destinationGuide.listFormats = destinationGuide.searchMessaging.listFormats;

      destinationGuide.recommendedId = destinationGuide.id;

      var resultSet = destinationGuide.searchPanelModel.to.query();
      resultSet.observe(function (destinationModel, remove, add) {
        destinationGuide.updatePlaceHolder();

        // if expandable is not created yet, let's ignore.
        if (!destinationGuide.expandableDom) {
          return;
        }
        // update views
        destinationGuide.updateGuide(destinationModel, (add > -1));
        //(add > -1) ? destinationGuide.toggleMultiSelect(destinationModel.multiSelect) : null;
        if(!destinationGuide.searchPanelModel.to.query().length) {
          //destinationGuide.removeMultiSelect();
        }
        //destinationGuide.updateGuideCount();
        dojo.publish("tui:channel=updateResponse", ['to']);
      });

      resultSet = destinationGuide.searchPanelModel.from.query();
      resultSet.observe(function (destinationModel, remove, add) {
        destinationGuide.updateGuideTitle();
      });


      dojo.subscribe("tui:channel=modalOpening", function(){
          destinationGuide.closeExpandable();
      });

      on(document.body, "click", function (event) {
        //TODO: Refactor If conditions.
        if(event.target.className === 'close-list')
        {destinationGuide.closeExpandable();        }
        if ( destinationGuide.expandableDom === null || !destinationGuide.isShowing(destinationGuide.expandableDom) ) return;
        if(destinationGuide.inPredictiveText(destinationGuide.expandableDom, event)) {
            event.target.focus();
            return;
        }
        destinationGuide.closeExpandable();

      });

      destinationGuide.tagElement(destinationGuide.domNode, "where-to");
    },

	placeHolderSupport : function() {
        var textBox = document.createElement("input");
        textBox.type = "text";
        return (typeof textBox.placeholder !== "undefined");
    },

    removeMultiSelect: function () {
      var destinationGuide = this;
      _.each(destinationGuide.suggestions, function(suggestion){
        _.each(suggestion.children, function(destination){
          if(destination.available) query('[for="recommended-'+destination.id+'"]', destinationGuide.expandableDom).removeClass('disabled');
        });
      });
    },

    inPredictiveText: function (element, event) {
        return _.size(query(event.target).closest(".guide"))>0 || dojo.attr(event.target, "data-klass-Id") === 'cruiseAutoComplete';
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
          destinationGuide.regions = response.regions;
          destinationGuide.countries = response.countries;
          destinationGuide.regionsColumnLength = Math.ceil(_.size(response.regions.children) / destinationGuide.columns);
          destinationGuide.countriesColumnLength = Math.ceil(_.size(response.countries.children) / destinationGuide.columns);
          destinationGuide.updateSuggestionsList();
          dojo.removeClass(destinationGuide.expandableDom, "loading");
        });

      }
    },
    //Need to Modify the code- Nagaraj
    showWidget: function (element) {
        var simpleExpandable = this,
            elementToShow = (element || simpleExpandable.domNode),
            wrapper = dojo.query('.wrapper', simpleExpandable.expandableDom)[0],
            setHeight = simpleExpandable.expandableProp === 'expand-vertical',
            //list = dojo.query('.wrapper .list-two', simpleExpandable.expandableDom)[0],
            height = setHeight ? _.pixels(dojo.position(wrapper).h * 2) : 0;

        setTimeout(function () {
            setHeight ? dojo.style(elementToShow, 'maxHeight', height) : null;
            dojo.addClass(elementToShow, "open");
        }, 1);
        on.once(simpleExpandable.expandableDom, "transitionend, webkitTransitionEnd, mozTransitionEnd, otransitionend, oTransitionEnd", function(){
            dojo.addClass(elementToShow, "open-anim-done");
        });
    },

    attachOpenEvent: function () {
      var destinationGuide = this;
      on(destinationGuide.domNode, "click", function (event) {
        if (dojo.hasClass(destinationGuide.domNode, "loading")) {
          return;
        }
        if (destinationGuide.expandableDom === null || !destinationGuide.isShowing(destinationGuide.expandableDom)) {
          dojo.publish("tui.searchPanel.view.cruise.AirportGuide.closeExpandable");
          dojo.publish("tui.searchPanel.view.cruise.CruiseAndStayPicker.closeExpandable");
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
      var textbox = query("#destinationPredictiveText", destinationGuide.expandableDom);
      dojo.parser.parse(destinationGuide.expandableDom);

      /*if (destinationGuide.expandableDom != null){
         on(textbox, "keydown", function (event) {
            //dojo.stopEvent(event);
             console.log('keydown');
         });
      }*/

      on(destinationGuide.expandableDom, "label:click", function (event) {
        dojo.stopEvent(event);

        var checkbox, label;
        if (event.target.tagName.toUpperCase() === "INPUT") {
          checkbox = event.target;
          label = event.target.parentElement;
        } else if(event.target.tagName.toUpperCase() === "DIV") {
            checkbox = event.target.parentElement.children[0];
            label = event.target.parentElement;
        } else {
          checkbox = event.target.children[0];
          label = event.target;
        }

        if (dojo.hasClass(label, "disabled")) {
          return;
        }

        // Start the orange pulse on the field.
        dojo.publish("tui.searchPanel.view.cruise.DestinationMultiFieldList.pulse", [destinationGuide.widgetController]);

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
        dojo.setStyle(dojo.byId("tui_searchPanel_view_DestinationGuide_0-inspired"), "display", "none");
      }

      //Tagging particular element.
      destinationGuide.tagElement(dojo.byId("regions"), "region selections");
      destinationGuide.tagElement(dojo.byId("countries"), "country selections");

      //destinationGuide.tagElement(destinationGuide.expandableDom, "Destination List");
      //destinationGuide.tagElement(destinationGuide.destinationList.domNode, "Select a country");
      /*destinationGuide.tagElement(query(".close-hide", destinationGuide.expandableDom)[0], "Destination Hide");
      destinationGuide.tagElement(query(".empty-destination-model", destinationGuide.expandableDom)[0], "Destination Deselect All");
      destinationGuide.tagElement(query("#tui_searchPanel_view_DestinationGuide_0-inspired a")[0], "Browse Destinations");
      destinationGuide.tagElement(dojo.byId("dg-items"), "country group selections");
      */
      destinationGuide.subscribe("tui/searchPanel/searchOpening", function (component) {
        if (component !== destinationGuide) {
          destinationGuide.closeExpandable();
        }
      });

      if(!destinationGuide.placeHolderSupport()) {
		  var searchBoxDomNode = query(".quick-search input", destinationGuide.expandableDom);
		  if(searchBoxDomNode.length) {
		  var curPlaceholder = domAttr.get(searchBoxDomNode[0],"placeholder");
	        if (curPlaceholder && curPlaceholder.length) {
	        	searchBoxDomNode.val(curPlaceholder);

	        	on(searchBoxDomNode, "focus", function(){
	        		var searchBoxNode = this;
	                if (this.originalTextbox) {
	                    searchBoxNode = this.originalTextbox;
	                    searchBoxNode.dummyTextbox = this;
	                    this.parentNode.replaceChild(this.originalTextbox, this);
	                    searchBoxNode.focus();
	                }
	                if (searchBoxNode.value === curPlaceholder)
	                    searchBoxNode.value = "";
	    		});

	        	on(searchBoxDomNode, "blur", function() {
	        		var searchBoxNode = this;
	        		setTimeout(function(){
	        			if (searchBoxNode.value === "") {
		                    searchBoxNode.value = curPlaceholder;
		                }
	        		}, 100);
	        	});
	        }
		  }
	  }
    },

    openExpandable: function () {
      // summary:
      //		Override the default expandable behaviour,
      //		to only open once destination guide data is
      //		available and returned back from the server.
      var destinationGuide = this;
      var args = arguments;
      dojo.addClass(destinationGuide.domNode, "loading");
      dojo.addClass(destinationGuide.domNode, "active");

      destinationGuide.removeErrors();
      // publish opening event for widgets that need to close
      destinationGuide.publishMessage("searchPanel/searchOpening");

      dojo.publish("tui.searchPanel.view.cruise.DestinationMultiFieldList.highlight", [true, destinationGuide.widgetController]);

      dojo.when(destinationGuide.destinationGuideStore.requestData(null, destinationGuide.searchPanelModel.generateQueryObject()), function (response) {
        destinationGuide.regions = response.data.regions;
        destinationGuide.countries = response.data.countries;
        destinationGuide.regionsColumnLength = Math.ceil(_.size(response.data.regions) / destinationGuide.columns);
        destinationGuide.countriesColumnLength = Math.ceil(_.size(response.data.countries) / destinationGuide.columns);
        if(destinationGuide.firstRender){
           destinationGuide.updatePredictiveSelections(destinationGuide.regions, destinationGuide.countries);
        }
        // Update the view to conform to the latest datafeed.
        destinationGuide.updateSuggestionsList();

        destinationGuide.inherited(args);
        //destinationGuide.updateDestinationList();
        // Set the custom select option to the default value.
        if(!destinationGuide.firstRender) dojo.publish("tui.searchPanel.view.cruise.DestinationGuideSelectOption.setSelectedValue", [""]);
        dojo.removeClass(destinationGuide.domNode, "loading");
        destinationGuide.firstRender = false;
      });
    },

    updatePredictiveSelections: function(regions, countries){
       var destinationGuide = this;
       //query template to see if there exist missing codes...
       var nonExistentSelections = _.filter(destinationGuide.searchPanelModel.to.data, function(num){
            return !(_.contains(_.pluck(regions,'id'), num.id) || _.contains(_.pluck(countries,'id'), num.id));
        });
       destinationGuide.userSelections = nonExistentSelections;
    },

    updateDestinationCheckboxes: function (destinationGuideItem) {
       // summary:
       //    Changes item state to enabled/disabled depending on server response
       var destinationGuide = this;
       if (!destinationGuide.expandableDom) {
           return;
       }

       var input = dojo.query("." + destinationGuideItem.id, destinationGuide.expandableDom);
       if (!destinationGuideItem.available) {
          //domAttr.set(input[0], "disabled", true);
          dojo.addClass(dojo.query(input).parent()[0], "disabled");

          // For security, we remove the airport from the model if there.
          var airportGroupModel = destinationGuide.searchPanelModel.from.get(destinationGuideItem.id);
          if (airportGroupModel) {
             destinationGuide.searchPanelModel.from.remove(destinationGuideItem.id);
          }
          //Removing the item from searchPanelModel.to
          var destinationModel = destinationGuide.searchPanelModel.to.get(destinationGuideItem.id);
          if (destinationModel) {
             destinationGuide.searchPanelModel.to.remove(destinationGuideItem.id);
          }
       } else {
          //domAttr.set(input[0], "disabled", false);
          dojo.removeClass(dojo.query(input).parent()[0], "disabled");
      }
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

      html = dojo.trim(tui.dtl.Tmpl.createTmpl(data, countryDestTmpl));
      destListDom = dojo.place(html, query('.destinationlist-dropdown', destinationGuide.expandableDom)[0]);

        dojo.place(html, query('#' + destinationGuide.id + '-destinations')[0], 'replace');
      //destinationGuide.destinationList = new DestinationGuideSelectOption(null, query(".destinationlist-dropdown .destinationlist", destinationGuide.expandableDom)[0]);
      //destinationGuide.tagElement(destinationGuide.destinationList.domNode, "Country Select");
    },

    closeExpandable: function () {
      var destinationGuide = this;
      destinationGuide.inherited(arguments);
      dojo.removeClass(destinationGuide.domNode, "active");
      if (destinationGuide.destinationList) {
        destinationGuide.destinationList.hideList();
      }
      dojo.publish('tui.searchPanel.view.DestinationMultiFieldList.highlight', [false, destinationGuide.widgetController]);
      dojo.publish("tui.searchPanel.view.DestinationAutoComplete.clearList");
    },

    updateSuggestionsList: function () {
      // summary:
      //      Call the server for a fresh list of suggested destinations.
      //      Rerender the list template inside the element.
      var destinationGuide = this;
      _.each(destinationGuide.countries, function (destination) {
            destinationGuide.updateDestinationCheckboxes(destination);
        });
      _.each(destinationGuide.regions, function (destination) {
            destinationGuide.updateDestinationCheckboxes(destination);
      });
    },

    place: function (html) {
      // summary:
      //		Override place method from simpleExpandable,
      //		place the destination guide in the main search container.
      var destinationGuide = this;
      var target = destinationGuide.targetSelector ?
          query(destinationGuide.targetSelector, destinationGuide.widgetController.domNode)[0] :
          destinationGuide.widgetController.domNode;
          destinationGuide.expandableDom = dojo.place(html, target, "last");
      destinationGuide.updateSuggestionsList();
      return destinationGuide.expandableDom;
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
        console.log(destinationGuide.getDestinationItemAttributes(clickedCheckbox).type);
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
    },
    updatePlaceHolder: function(){
       var destinationGuide = this;
       if (destinationGuide.searchPanelModel.to.data.length > 1){
          destinationGuide.placeholder.innerHTML = destinationGuide.searchPanelModel.to.data.length + " " +destinationGuide.searchMessaging.selections;
       }else if(destinationGuide.searchPanelModel.to.data.length == 1){
          destinationGuide.placeholder.innerHTML = destinationGuide.searchPanelModel.to.data[0].name ;
       }else{
          destinationGuide.placeholder.innerHTML = destinationGuide.searchMessaging.toPlaceholder;
       }
    },
      toggleEmptyAlert: function (name, oldError, newError) {
          var destinationMultiFieldList = this;
          var action = (newError.emptyFromTo) ? "addClass" : "removeClass";
          dojo[action](destinationMultiFieldList.domNode, "error");
          dojo[action](destinationMultiFieldList.placeholder, "error");
      },

      displayToErrorMessage: function (name, oldErrorInfo, newErrorInfo) {
         //TODO: displayError Messages...To

      },

      removeErrors: function(){
         var destinationGuide = this;
         var fromToError = dojo.clone(destinationGuide.searchPanelModel.searchErrorMessages.get("fromTo"));
         if (fromToError.emptyFromTo) {
            delete fromToError.emptyFromTo;
            destinationGuide.searchPanelModel.searchErrorMessages.set("fromTo", fromToError);
         }

         // delete no match error & Empty Destination error if present
         destinationGuide.searchPanelModel.searchErrorMessages.set("to", {});
      }
  });

  return tui.searchPanel.view.cruise.DestinationGuide;
});
