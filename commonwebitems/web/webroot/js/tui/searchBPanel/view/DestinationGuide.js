define('tui/searchBPanel/view/DestinationGuide', [
  'dojo',
  'dojo/_base/lang',
  'dojo/query',
  'dojo/on',
  'dojo/dom-attr',
  'dojo/dom-style',
  'dojo/dom-class',
    'dojo/NodeList-traverse',
  'dojo/text!tui/searchBPanel/view/templates/DestinationGuideTmpl.html',
    'dojo/text!tui/searchBPanel/view/templates/collectionView.html',
  'dojo/text!tui/searchBPanel/view/templates/DestinationGuideRecommendedDestinationsTmpl.html',
  'dojo/text!tui/searchBPanel/view/templates/DestinationGuideMostPopularTmpl.html',
  'dojo/text!tui/searchBPanel/view/templates/DestinationGuideCountryDestinationsTmpl.html',
  'dojo/text!tui/searchBPanel/view/templates/popupInfoTmpl.html',
  'tui/searchBPanel/model/DestinationModel',
  'tui/searchBPanel/store/DestinationGuideStore',
  'tui/searchBPanel/view/nls/ListFormatter',
    'tui/searchBPanel/view/ErrorPopup',
  'tui/config/TuiConfig',
  'tui/widget/expand/SimpleExpandable',
  'dojox/dtl/tag/loader',
  'tui/searchBPanel/view/SearchGuide'
], function (dojo, lang, query, on, domAttr, domStyle, domClass, traverse, destGuideTmpl, collectionTmpl, recDestTmpl, mostPopularTmpl, countryDestTmpl, popupInfoTmpl, DestinationModel, DestinationGuideStore, ListFormatter, ErrorPopup, TuiConfig) {

  dojo.declare('tui.searchBPanel.view.DestinationGuide', [tui.searchBPanel.view.SearchGuide], {

    // ----------------------------------------------------------------------------- properties


    countryList: null,

    tmpl: destGuideTmpl,

    recDestTmpl: recDestTmpl,

	mostPopularTmpl: mostPopularTmpl,

    collectionTmpl: collectionTmpl,

    popupInfoTmpl: popupInfoTmpl,

    destinationGuideStore: null,

    listFormatter: null,

    listFormats: '',

    subscribableMethods: ["openExpandable", "closeExpandable", "cancelBlur"],

      mostPopularDestinations:null,

      toggleButton : null,

      maxHeight:"700px",
	  
	  delayForSpinner : 0,

	tuiConfig: TuiConfig,

    // ----------------------------------------------------------------------------- methods

    postCreate: function () {
      var destinationGuide = this;

      //destinationGuide.initSearchMessaging();
      destinationGuide.inherited(arguments);
      destinationGuide.findIpadDevice();
      destinationGuide.listFormatter = new ListFormatter();
      destinationGuide.listFormats = destinationGuide.searchMessaging.listFormats;

      var resultSet = destinationGuide.searchPanelModel.to.query();
      resultSet.observe(function (destinationModel, remove, add) {

	    if(destinationGuide.tuiConfig[dojoConfig.site] && dojoConfig.dualBrandSwitch) {
			  labelValue = destinationGuide.tuiConfig[dojoConfig.site].dualBrandConfig.optionLabel[destinationModel.name.toLowerCase().replace(/\s/g,"")];
			  if(labelValue){
				  destinationModel.name = labelValue.toUpperCase();
			  }
	      }
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
      });

      on(document.body, "click", function (event) {
          if(destinationGuide.infoPopup){
              destinationGuide.infoPopup.close();
          }
        if(dojo.query(event.target).parents("div.destination-guide-2015")[0]) return; // return if the click is with in the guide overlay
        if (destinationGuide.expandableDom === null || !destinationGuide.isShowing(destinationGuide.expandableDom)) return;
        destinationGuide.closeExpandable();
      });

        on(destinationGuide.domNode, "click", function (event) {
        	destinationGuide.showDefaultView();
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
      query('label', destinationGuide.expandableDom).addClass('disabled-multiselect');

      _.each(enableNodes, function(node){
        domClass.remove(query(node).parents('label')[0], 'disabled-multiselect');
      });
    },

      toggleEmptyAlert: function (name, oldError, newError) {
          var destinationMultiFieldList = this;
          var action = (newError.emptyFromTo) ? "addClass" : "removeClass";
          dojo[action](destinationMultiFieldList.domNode, "error");
          dojo[action](destinationMultiFieldList.placeholder, "error");
      },

    onCountrySelect: function (destinationCode) {
      var destinationGuide = this;
      destinationGuide.cancelBlur();
      query(".default-destinations", destinationGuide.expandableDom).addClass("hidden");
        var countryDestination = query("#" + destinationCode + "-destinations", destinationGuide.expandableDom);
        // Ask the server data about the selected country.
        dojo.addClass(destinationGuide.expandableDom, "loading");
        domStyle.set(query(".loading-overlay", destinationGuide.expandableDom)[0], "display", "block");

		setTimeout(function(){
        dojo.when(destinationGuide.destinationGuideStore.requestData(destinationCode, destinationGuide.searchPanelModel.generateQueryObject()), function (response) {
          var html, countryData;

          countryData = {
            toData: destinationGuide.searchPanelModel.to.data,
            searchMessaging: destinationGuide.searchMessaging
          };

          // Updating the country destinations list.
          html = dojo.trim(tui.dtl.Tmpl.createTmpl(dojo.mixin(countryData, response), countryDestTmpl));
          if (!countryDestination.length) {
            dojo.place(html, query("div.default-destinations", destinationGuide.expandableDom)[0], "after");
            countryDestination.removeClass("hidden");
          } else {
            dojo.place(html, query("#" + destinationCode + "-destinations", destinationGuide.expandableDom)[0], "replace");
          }
          dojo.removeClass(destinationGuide.expandableDom, "loading");
        });
		},destinationGuide.delayForSpinner);
    },

 // overriding the parent method as events attached in parent method are not required here.
    attachEventListeners: function () {},

    attachOpenEvent: function () {
      var destinationGuide = this;
      on(destinationGuide.domNode, "click", function (event) {
        if (dojo.hasClass(destinationGuide.domNode, "loading")) {
          return;
        }
        if (destinationGuide.expandableDom === null || !destinationGuide.isShowing(destinationGuide.expandableDom)) {
          dojo.publish("tui.searchBPanel.view.AirportGuide.closeExpandable");
          destinationGuide.openExpandable();
          /*setTimeout(function () {
            destinationGuide.openExpandable();
          }, 350);*/
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
        destinationGuide.toggleButton = dojo.query(".toggle-button", destinationGuide.expandableDom)[0];
		destinationGuide.toggleDiv = dojo.query(".toggle-slide", destinationGuide.expandableDom)[0];
        destinationGuide.inherited(arguments);
      on(destinationGuide.expandableDom, ".sub-cat span.checkbox:click", function (event) {
        dojo.stopEvent(event);

        var checkbox, label,spanCheck ;
          if(dojo.hasClass(event.target,"checkbox")){
              checkbox = query("input",event.target)[0];
              label = query("label",event.target)[0];
              spanCheck = event.target;
          }else {
              checkbox = query("input",event.target.parentElement)[0];
              label = query("label",event.target.parentElement)[0];
              spanCheck = event.target.parentElement;
          }

        if (dojo.hasClass(spanCheck, "disabled")) {
          return;
        }

        // Start the orange pulse on the field.
        dojo.publish("tui.searchBPanel.view.DestinationMultiFieldList.pulse", [destinationGuide.widgetController]);

        destinationGuide.updateWhereTo(checkbox, label,spanCheck);
        destinationGuide.cancelBlur();
      });

        //attching the toggle event to toggle button and labels
        on(destinationGuide.expandableDom, ".toggle-slide:click", function (event) {
            destinationGuide.toggleView();
        });
        on(destinationGuide.expandableDom, ".toggle-label.left:click", function (event) {
            destinationGuide.toggleToLeft();
        });
        on(destinationGuide.expandableDom, ".toggle-label.right:click", function (event) {
            destinationGuide.toggleToRight();
        });

        on(destinationGuide.expandableDom, "li.collection:click", function (event) {
            //destinationGuide.toggleView();
            var parentLi = dojo.query(event.target).parents("li.collection")[0];
            var checkbox = query("input",parentLi)[0];
            var label = query("label",parentLi)[0];
            var spanCheck = query("span.checkbox",parentLi)[0];

            if(dojo.hasClass(parentLi,'disabled'))return;

            dojo.hasClass(parentLi,'selected')? dojo.removeClass(parentLi,'selected') : dojo.addClass(parentLi,'selected');

            // Start the orange pulse on the field.
            dojo.publish("tui.searchBPanel.view.DestinationMultiFieldList.pulse", [destinationGuide.widgetController]);

            destinationGuide.updateWhereTo(checkbox, label, spanCheck);
            destinationGuide.cancelBlur();

        });

        on(destinationGuide.expandableDom, ".close-popup:click", function (event) {
            destinationGuide.closeExpandable();
        });

        on(destinationGuide.expandableDom, ".country-hirerchy-done:click", function (event) {
            destinationGuide.showDefaultView();

        });

        on(destinationGuide.expandableDom, ".destination-list li.country a:click", function (event) {
            dojo.stopEvent(event);
            if(!dojo.hasClass(event.target.parentElement,'disabled')){
            destinationGuide.onCountrySelect(domAttr.get(event.target, 'data-destination-id'));
            }
        });

        on(destinationGuide.expandableDom, ".destination-list li.most-popular-destination a:click", function (event) {
            dojo.stopEvent(event);
            // if already selected/disabled don't do any thing
            if(dojo.hasClass(event.target.parentElement,"selected") || dojo.hasClass(event.target.parentElement,"disabled")){
                return;
            }
            destinationGuide.animatePill(event.target, query('.where-to', destinationGuide.widgetController.domNode)[0], function () {
                var allDestinations = destinationGuide.getDestinationItemAttributes(event.target),availableDestinations,unavailableDestinations,
                    destinationsToPopulate = null;
                if(_.isArray(allDestinations)) {
                    availableDestinations = _.filter(allDestinations, function (destination) {
                        return destination.availability == true;
                    });
                    unavailableDestinations = _.difference(allDestinations, availableDestinations);
                    if (unavailableDestinations.length) {
                        destinationGuide.destinationNotAvailableInfo(availableDestinations, unavailableDestinations);
                    }
                    destinationsToPopulate = _.filter(availableDestinations,function(destination){
                        return destinationGuide.searchPanelModel.to.get(destination.id)  == null;
                    });
                }else destinationsToPopulate = allDestinations;

                destinationGuide.updateModel(destinationsToPopulate, "add");
                dojo.addClass(event.target.parentElement,"selected");
            });
        });

      // Tagging particular element.
      destinationGuide.tagElement(destinationGuide.expandableDom, "Destination List");

      destinationGuide.subscribe("tui/searchBPanel/searchOpening", function (component) {
        if (component !== destinationGuide) {
          destinationGuide.closeExpandable();
        }
      });
    },

      destinationNotAvailableInfo: function(availableDestinations,unavailableDestinations ){
          var destinationGuide = this;
          // create an error popup and display it.
          destinationGuide.infoPopup = new ErrorPopup({
              tmpl: destinationGuide.popupInfoTmpl,
              availableDestinations : availableDestinations,
              unavailableDestinations : unavailableDestinations,
              elementRelativeTo: destinationGuide.domNode,
              floatWhere: "position-bottom-center",
              setPosOffset: function (position) {
                  var tooltips = this;
                  if (position === "position-bottom-center") {
                      tooltips.posOffset = {top: -12, left: 40};
                  }
              }
          }, null);

          destinationGuide.infoPopup.open();

      },

      showDefaultView :function(){
          var destinationGuide = this;
          query(".default-destinations", destinationGuide.expandableDom).addClass("hidden");
          query(".collection-list", destinationGuide.expandableDom).addClass("hidden");
          query(".destination-list", destinationGuide.expandableDom).removeClass("hidden");
          if(destinationGuide.toggleButton) dojo.removeClass(destinationGuide.toggleButton, 'on');

      },

      toggleView:function(){
          var destinationGuide = this;
         if(dojo.hasClass(destinationGuide.toggleButton, 'on')){
             //toggling to destinations view
             destinationGuide.toggleToLeft();
         }else{
             //toggling to collection view
             destinationGuide.toggleToRight();
         }
      },

      toggleToLeft: function(){
          var destinationGuide = this;
          query(".default-destinations",destinationGuide.expandableDom).addClass("hidden");
          query(".collection-list",destinationGuide.expandableDom).addClass("hidden");
          query(".destination-list",destinationGuide.expandableDom).removeClass("hidden");
          dojo.removeClass(destinationGuide.toggleButton, 'on');
		  domAttr.set(destinationGuide.toggleDiv, 'analytics-text', "DListCouTog");
      },

      toggleToRight: function(){
          var destinationGuide = this;
          //check for if collections template already rendered. once rendered don't fire the request once again even if data/airport changes as availability check for collections will not be done at hybris as per current implementation
			  if(!destinationGuide.collectionRendered){
	              dojo.addClass(destinationGuide.expandableDom, "loading");
	              domStyle.set(query(".loading-overlay", destinationGuide.expandableDom)[0], "display", "block");
	              setTimeout(function(){
				  dojo.when(destinationGuide.destinationGuideStore.requestData(null, destinationGuide.searchPanelModel.generateQueryObject(),"collections" ), function (response) {
				     if(destinationGuide.tuiConfig[dojoConfig.site] && dojoConfig.dualBrandSwitch) {
					      _.each(response.children, function (item, index) {
					    	  labelValue = destinationGuide.tuiConfig[dojoConfig.site].dualBrandConfig.optionLabel[item.name.toLowerCase().replace(/\s/g,"")];
					    	  if(labelValue){
					    		  response.children[index].name = labelValue.toUpperCase();
					    	  }
					      });
				      }
	                  var collectionData = {toData: destinationGuide.searchPanelModel.to.data, dualBrandSwitch : dojoConfig.dualBrandSwitch};
					  var html = dojo.trim(tui.dtl.Tmpl.createTmpl(dojo.mixin(collectionData, response), collectionTmpl));
	                  dojo.place(html, query('#' + 'dg-items')[0], 'last');
	                  var familyLifeDomNode = query(".collection-list .VIL", destinationGuide.expandableDom);
	                  if(familyLifeDomNode.length){
	                     var parentElement = _.first(familyLifeDomNode).parentElement;
	                	 var inspireText = _.first(query(".inspireTextPara", parentElement));
	                	 domStyle.set(inspireText, "padding-top", "22px");
	                  }
	                  destinationGuide.collectionRendered = true;	                
	                  dojo.removeClass(destinationGuide.expandableDom, "loading");
	              });
				  },destinationGuide.delayForSpinner);
	          }
          query(".default-destinations",destinationGuide.expandableDom).addClass("hidden");
          query(".collection-list",destinationGuide.expandableDom).removeClass("hidden");
          dojo.addClass(destinationGuide.toggleButton, 'on');
		  domAttr.set(destinationGuide.toggleDiv, 'analytics-text', "DListTogColl");
      },
    findIpadDevice: function(){
		var destinationGuide = this;
		destinationGuide.delayForSpinner = 0;
		if(navigator.userAgent.match(/iPad/i) != null && navigator.userAgent.match(/OS 7/i) != null){
			destinationGuide.delayForSpinner = 100; //100 ms : fix for spinner issue on iPad OS 7 device
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

      // publish opening event for widgets that need to close
      destinationGuide.publishMessage("searchBPanel/searchOpening");

      dojo.publish("tui.searchBPanel.view.DestinationMultiFieldList.highlight", [true, destinationGuide.widgetController]);
		destinationGuide.inherited(args);
		dojo.addClass(destinationGuide.expandableDom, "loading");
		dojo.addClass(destinationGuide.expandableDom, "most-popular-loading");
		dojo.addClass(destinationGuide.expandableDom, "country-list-loading");
		dojo.removeClass(destinationGuide.domNode, "loading");
		dojo.addClass(query(".loading-overlay", destinationGuide.expandableDom)[0], "two-third-width");
		domStyle.set(query(".most-popular-loading-overlay", destinationGuide.expandableDom)[0], "display", "block");
		domStyle.set(query(".loading-overlay", destinationGuide.expandableDom)[0], "display", "block");

		setTimeout(function(){
			dojo.when(destinationGuide.destinationGuideStore.requestData(null, destinationGuide.searchPanelModel.generateQueryObject()), function (response) {
          destinationGuide.countryList = response.allContinentHierarchy;
		  var html;		 
		  if(!query(".destination-list .country-list", destinationGuide.expandableDom)[0]){
		  html = dojo.trim(tui.dtl.Tmpl.createTmpl(destinationGuide, recDestTmpl));
		  dojo.place(html, query(".destination-list", destinationGuide.expandableDom)[0], "first");
		  }else{
			  destinationGuide.updateSuggestionsList();
		  }
		  dojo.removeClass(destinationGuide.expandableDom, "country-list-loading");
		  if(!dojo.hasClass(destinationGuide.expandableDom, "most-popular-loading")){
			  dojo.removeClass(destinationGuide.expandableDom, "loading");
		  }
		  domStyle.set(query(".loading-overlay", destinationGuide.expandableDom)[0], "display", "none");
		  dojo.removeClass(query(".loading-overlay", destinationGuide.expandableDom)[0], "two-third-width");
	
        });
	    dojo.when(destinationGuide.destinationGuideStore.requestData(null, destinationGuide.searchPanelModel.generateQueryObject(), "mostPopular"), function (response) {
          destinationGuide.mostPopularDestinations =   destinationGuide.updateAvailabilityAndSelectionCheck(response);
		  var html;		 
		  if(!query(".destination-list .popular", destinationGuide.expandableDom)[0]){
		  html = dojo.trim(tui.dtl.Tmpl.createTmpl(destinationGuide, mostPopularTmpl));
		  dojo.place(html, query(".destination-list .cta-container", destinationGuide.expandableDom)[0], "before");
		  }else{
			  destinationGuide.updateSuggestionsList();
		  }
		  dojo.removeClass(destinationGuide.expandableDom, "most-popular-loading");
		  if(!dojo.hasClass(destinationGuide.expandableDom, "country-list-loading")){
			  dojo.removeClass(destinationGuide.expandableDom, "loading");
		  }
		  domStyle.set(query(".most-popular-loading-overlay", destinationGuide.expandableDom)[0], "display", "none");		  
		});
		},destinationGuide.delayForSpinner);


    },

      updateAvailabilityAndSelectionCheck : function(mostPopularDestinations){
          var destinationGuide = this;

          _.forEach(mostPopularDestinations, function(destinationGroup){
             if( _.isArray(destinationGroup.locationDatas))
              {
                  destinationGroup.disable = true;
                  destinationGroup.select = true;
                  _.forEach(destinationGroup.locationDatas, function (destination) {
                        if(destination.available){
                            destinationGroup.disable = false;
                            return;
                        }
                  });
                  _.forEach(destinationGroup.locationDatas, function (destination) {
                      if(!destinationGuide.searchPanelModel.to.get(destination.id)){
                          destinationGroup.select = false;
                          return;
                      }
                  });

              }

          });
            return mostPopularDestinations;
          },



    closeExpandable: function () {
      var destinationGuide = this;
      destinationGuide.inherited(arguments);
      if (destinationGuide.destinationList) {
        destinationGuide.destinationList.hideList();
      }
      dojo.publish('tui.searchBPanel.view.DestinationMultiFieldList.highlight', [false, destinationGuide.widgetController]);
    },

    updateSuggestionsList: function () {
      // summary:
      //      Call the server for a fresh list of suggested destinations.
      //      Rerender the list template inside the element.
      var destinationGuide = this;

      if (dojo.byId(destinationGuide.id + '-destinations')) {
        var html;

        // Updating the recommended country list.
        html = dojo.trim(tui.dtl.Tmpl.createTmpl(destinationGuide, recDestTmpl));
        dojo.place(html, query('#' + destinationGuide.id + '-destinations .country-list')[0], 'replace');

     // Updating the most popular.
        html = dojo.trim(tui.dtl.Tmpl.createTmpl(destinationGuide, mostPopularTmpl));
        dojo.place(html, query('#' + destinationGuide.id + '-destinations .popular')[0], 'replace');

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
        if(!JSON.parse(domAttr.get(destination, 'data-destination-multiple'))){
          return new DestinationModel({
            name: domAttr.get(destination, 'data-destination-name'),
            id: domAttr.get(destination, 'data-destination-id'),
            type: domAttr.get(destination, 'data-destination-type'),
            multiSelect:  JSON.parse(domAttr.get(destination, 'data-destination-multiselect')),
              fewDaysFlag: JSON.parse(domAttr.get(destination, 'data-destination-fewdays')),
              availability: JSON.parse(domAttr.get(destination, 'data-destination-available'))
          });
        }
          else {
            var names = domAttr.get(destination, 'data-destination-name').split("&"),
            ids = domAttr.get(destination, 'data-destination-id').split("&"),
            types = domAttr.get(destination, 'data-destination-type').split("&"),
            multiSelects = domAttr.get(destination, 'data-destination-multiselect').split("&"),
            fewDaysFlags = domAttr.get(destination, 'data-destination-fewdays').split("&"),
            availabilities = domAttr.get(destination, 'data-destination-available').split("&"),
            destinationAttributes =  _.zip(names,ids,types,multiSelects,fewDaysFlags,availabilities),
            destinationModels = [];
            _.forEach(destinationAttributes, function(destination){
               var destintionmodel =  new DestinationModel({
                    name: destination[0],
                    id: destination[1],
                    type: destination[2],
                    multiSelect:  JSON.parse(destination[3]),
                   fewDaysFlag: JSON.parse(destination[4]),
                   availability: JSON.parse(destination[5])
                });
                destinationModels.push(destintionmodel);
            });
            return destinationModels;

          }
    },

    updateModel: function (destination, action) {
      // summary:
      //		Adds destination to model
      var destinationGuide = this;

      if (_.isArray(destination)) {
        _.forEach(destination, function (item) {
          destinationGuide.searchPanelModel.to[action]((action === "remove") ? item.id : item);
        });
      } else {
        destinationGuide.searchPanelModel.to[action]((action === "remove") ? destination.id : destination);
      }
    },

    updateWhereTo: function (clickedCheckbox, clickedLabel, spanCheck) {
      // summary:
      //		Updates Search model to match guide.
      var destinationGuide = this;
      var destinationModel, productModel = [], productHotels;
        //var spanCheck = query("checkbox-bg", )

      var clickedCheckboxId = domAttr.get(clickedCheckbox, 'data-destination-id');
	  var clickedCheckboxName = domAttr.get(clickedCheckbox, 'data-destination-name');

      // We look to the parent span tag class to see if selected or not.
      var add = !(dojo.hasClass(spanCheck, 'selected'));
      var action = (add) ? 'addClass' : 'removeClass';
	  var liDest = query(".destination-list li.country", destinationGuide.expandableDom);
	  var mostPopularLi = query(".destination-list li.most-popular-destination", destinationGuide.expandableDom);

      // Ignore the click if the destination is disabled.
      if (dojo.hasClass(spanCheck, 'disabled')) {
        return;
      }

      if (add) {
        destinationModel = destinationGuide.searchPanelModel.to.get(clickedCheckboxId);
        if (!destinationModel) {
          destinationModel = destinationGuide.getDestinationItemAttributes(clickedCheckbox);
        }

        destinationGuide.animatePill(clickedLabel, query('.where-to', destinationGuide.widgetController.domNode)[0], function () {
          destinationGuide.updateModel(productModel.length ? productModel : destinationModel, "add");
        });
		//disabling countries when lapland is selected
        if(clickedCheckboxName == 'Lapland') {
			_.each(liDest, function (liEle) {
			  if(query("a",liEle)[0].innerHTML != 'Lapland')
			    dojo.addClass(liEle,"disabled");
			});
			_.each(mostPopularLi, function (liEle) {			
			    dojo.addClass(liEle,"disabled");
			});
		}
      }
	  else {
        // We are performing a removal from destination guide.

		//enabling countries when lapland is selected
		  if(clickedCheckboxName == 'Lapland') {
				_.each(liDest, function (liEle) {				 
				    dojo.removeClass(liEle,"disabled");
				});
				_.each(mostPopularLi, function (liEle) {				
				    dojo.removeClass(liEle,"disabled");
				});
			}

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
        dojo[action](spanCheck, 'selected');

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
          var input = [];
            input = query('.' + id, destinationGuide.expandableDom);
            if(add){
                _.each(input, function (id){
                	if(!dojo.hasClass(query(id).parent()[0],'disabled')){
							query(id).parent()["removeClass"]('selected');
							query(id).parent()["removeClass"]('disabled');
                        	query(id).parent()[action]('selected disabled');
                    }
                });
            }else{
                _.each(input, function (id){
                	if(dojo.hasClass(query(id).parent()[0],'selected')){
							query(id).parent()["removeClass"]('selected');
							query(id).parent()["removeClass"]('disabled');
                        	query(id).parent()[action]('selected disabled');
                    }
                });
            }
        });
      }

      var clickedCheckboxParents = domAttr.get(clickedCheckbox, 'data-destination-parents');

         if(add && clickedCheckboxParents){
            //update its parent to selected state if all of its siblings are selected
            destinationGuide.updateAllParentsStatus(clickedCheckboxParents.split(',') ,clickedCheckbox)
         }

    },

      updateAllParentsStatus : function(parentCheckboxs,childCheckbox){
          var destinationGuide = this;
          var siblingCheckboxs = domAttr.get(childCheckbox, 'data-destination-siblings');
          if (siblingCheckboxs) {
              siblingCheckboxs = siblingCheckboxs.split(',');


              if (destinationGuide.checkSiblingStatus(siblingCheckboxs)) {
                  destinationGuide.removeSiblings(siblingCheckboxs);
                  destinationGuide.updateParent(parentCheckboxs,domAttr.get(childCheckbox, 'data-destination-immediate-parent'));
              }
          }
      },

      checkSiblingStatus : function(siblingCheckboxs){
          var destinationGuide = this;
          var allSliblingsSelected = true;
          _.each(siblingCheckboxs, function (id) {
              if (!id || id === "") {
                  return;
              }
              var input = query('#' + id, destinationGuide.expandableDom)[0];

              if (!dojo.hasClass(input.parentNode, 'selected') && !dojo.hasClass(input.parentNode, 'disabled')) {
                  allSliblingsSelected = false;
                  return;
              }

          })
          return allSliblingsSelected;
      },

      removeSiblings :function(siblingCheckboxs){
          var destinationGuide = this;
          _.each(siblingCheckboxs, function (siblingId) {
              var siblingCheckBox = query('#' + siblingId, destinationGuide.expandableDom),
                  input = query('#' + siblingId, destinationGuide.expandableDom);
              if (destinationGuide.searchPanelModel.to.get(siblingId)) {
                  destinationModel = destinationGuide.getDestinationItemAttributes(siblingCheckBox[0]);
                  destinationGuide.searchPanelModel.to.remove(siblingId);

                  // domAttr.set(input[0], 'checked', remove);
                  query(input).parent().addClass('selected disabled');
              }

          })
      },

      updateParent: function(parentCheckboxs, immediateParentId){
          var destinationGuide = this;
          _.each(parentCheckboxs, function (parentId) {
              //update its only immediate parent
              if(parentId == immediateParentId) {
                  var parentCheckBox = query('#' + parentId, destinationGuide.expandableDom),
                      input = query('#' + parentId, destinationGuide.expandableDom);
                  if (!destinationGuide.searchPanelModel.to.get(parentId)) {
                      destinationModel = destinationGuide.getDestinationItemAttributes(parentCheckBox[0]);
                      destinationGuide.searchPanelModel.to.add(destinationModel);

                      domAttr.set(input[0], 'checked', true);
                      query(input).parent().addClass('selected');

                      //update the parent's immediate parent if present
                      if(domAttr.get(parentId, 'data-destination-immediate-parent')){
                          destinationGuide.updateAllParentsStatus(parentCheckboxs,input[0]);
                      }
                  }
              }
          })
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
        var inputs = query('.' + item.id, destinationGuide.expandableDom);
          _.each(inputs, function(input){
        	  query(input).parent()['removeClass']('selected');
			  query(input).parent()['removeClass']('disabled');
			  if(item.type.toLowerCase() == "productrange"){
				query(input).parents(".collection")['removeClass']('selected');
			  }
                if(dojo.hasClass(input,'multiple')){
                    var select = true;
                    _.each(domAttr.get(input, 'data-destination-id').split("&"),function(id){
                        if(!destinationGuide.searchPanelModel.to.get(id)){
                            select = false;
                            return;
                        }
                    });
                    if(select){
                        domAttr.set(input, 'checked', true);
                        query(input).parent()[action]('selected');
						if(item.type.toLowerCase() == "productrange"){
							query(input).parents('.collection')[action]('selected');
						}
                    }
                }else{
                    domAttr.set(input, 'checked', true);
                    query(input).parent()[action]('selected');
					if(item.type.toLowerCase() == "productrange"){
						query(input).parents('.collection')[action]('selected');
					}
                }
          })
      }
    }
  });

  return tui.searchBPanel.view.DestinationGuide;
});