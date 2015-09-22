define("tui/searchResults/view/SearchResultsComponentThFc", [
  "dojo/_base/declare",
  "dojo/_base/connect",
  "dojo/_base/lang",
  "dojo/parser",
  "dojo/aspect",
  "dojo/topic",
  "dojo/on",
  "dojo/mouse",
  "dojo/query",
  "dojo/dom-style",
  "dojo/dom-attr",
  "dojo/dom-construct",
  "dojo/dom-class",
  "dojo/dom-geometry",
  "dojo/text!tui/searchResults/view/templates/searchResultItemTmpl.html",
  "dojo/text!tui/searchResults/view/templates/noResultsPopupTmpl.html",
  "dojo/text!tui/searchResults/view/templates/ajaxErrorPopupTmpl.html",
  "dojo/text!tui/searchResults/view/templates/pageLoaderTmpl.html",
  "tui/searchResults/view/SearchResultsPaginator",
  "tui/searchResults/view/NoResultsPopup",
  "tui/searchResults/service/RoomGroupingService",
  "tui/utils/SessionStorage",
  "tui/config/TuiConfig",
  "tui/widget/mixins/Templatable",
  "tui/widget/_TuiBaseWidget",
  "tui/search/nls/Searchi18nable"], function (declare, connect, lang, parser, aspect, topic, on, mouse, query,
                                              domStyle, domAttr, domConstruct, domClass, domGeom, resultTmpl, noResultsTmpl,
                                              ajaxErrorTmpl, loaderTmpl, Paginator, Modal, roomGrpSrvc, sessionStorage,TuiConfig) {

  // ----------------------------------------------------------------------------- utility methods

  function brandTypeMap(brandType) {
    var map = {
      'F': 'First Choice',
      'SKY': 'SKYTOURS'
    };
    // map brandtype codes to names
    return (brandType !== 'T') ? map[brandType] : false;
  }

  function taRatingClass(rating) {
    // Add TripAdvisor rating class based on passed rating
    return "rating" + rating * 10;
  }

  function tuiRatingClass(rating) {
    if(rating && rating.match(/\//)){
      return false;
    }
    return rating % 1 === 0.5 ? rating - (rating % 1) + 'plus' : rating;
  }

  function imgPlaceholders(imageUrl) {
    // Replaces imageUrl with placeholder image if empty
    return dojoConfig.paths.cdnDomain + "/images/" + dojoConfig.site + "/default-large.png";
  }

  function formatDates(itinerary) {
    // Loop over itinerary and re-format date strings
    // Format overall departure date
    itinerary.departureDate = _.formatDate(itinerary.departureDate);

    // format outbound dates
    if (!itinerary.outbounds.length || !itinerary.inbounds.length) {
      return itinerary;
    }

    itinerary.outbounds = _.map(itinerary.outbounds, function (outbound) {
      outbound.schedule.departureDate = _.formatDate(outbound.schedule.departureDate);
      outbound.schedule.departureTime = _.formatTime(outbound.schedule.departureTime);
      outbound.schedule.arrivalDate = _.formatDate(outbound.schedule.arrivalDate);
      outbound.schedule.arrivalTime = _.formatTime(outbound.schedule.arrivalTime);
      return outbound;
    });

    // format inbound dates
    itinerary.inbounds = _.map(itinerary.inbounds, function (inbound) {
      inbound.schedule.departureDate = _.formatDate(inbound.schedule.departureDate);
      inbound.schedule.departureTime = _.formatTime(inbound.schedule.departureTime);
      inbound.schedule.arrivalDate = _.formatDate(inbound.schedule.arrivalDate);
      inbound.schedule.arrivalTime = _.formatTime(inbound.schedule.arrivalTime);
      return inbound;
    });

    return itinerary;
  }

  function priceDiffFormat(boardTypes,currency) {
    var symbol, template = '{symbol}{currency}{value}';

    return _.map(boardTypes, function (boardType) {
      if (boardType.priceDiffrence) {
        symbol = boardType.priceDiffrence.charAt(0);
        boardType.priceDiffrence = lang.replace(template, {
          'symbol': symbol,
          'currency': currency,
          'value': boardType.priceDiffrence.replace(symbol, '')
        });
      }
      return boardType;
    });
  }

  return declare("tui.searchResults.view.SearchResultsComponentThFc", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable, tui.search.nls.Searchi18nable], {

    // ----------------------------------------------------------------------------- properties

    holidays: null,

    siteBrand:null,



    holidayNodes: null,

    colNodes: null,

    totalHolidaysAvailable: 0,

    tmpl: resultTmpl,

    paginator: null,

    pageLoaderNode: null,

    noResultsPopup: null,

    ajaxErrorPopup: null,

    currentPage: 1,

    shortlistDisabled: false,

    componentId: null,

    subscribableMethods: ["renderAjaxErrorPopup", "renderNoResultsPopup", "updatePage"],

    defaultGalleryHeight: 345,

    galleryOpenTimer: null,

    analyticsData: null,

    clickedAnchor: null,

    displayDiscount: true,

    currency :  dojoConfig.currency,

    tuiConfig : TuiConfig,


    // ----------------------------------------------------------------------------- holiday rendering methods

    postCreate: function () {
      // initialise search results view
      var resultsView = this,
          mediator = dijit.registry.byId('mediator'),
          model = mediator.registerController(resultsView, 'searchResult');

      // save original holidays

      resultsView.siteName=mediator.originalModel.siteName;
      resultsView.holidays = model.holidays;
      resultsView.totalHolidaysAvailable = model.endecaResultsCount;
      resultsView.siteBrand = model.siteBrand;
      resultsView.shortlistEnabled = model.shortlistEnabled;


      //Scenario where state is restored... Current page needs to be updated for pagination requests...
      if (mediator.fromStore && mediator.currentRequest) {
        resultsView.currentPage = mediator.currentRequest.first;
      }
      // holidayNodes object, used for sorting results into columns (gallery view) or unsorted (list view)
      resultsView.holidayNodes = {
        oddCol: [],
        evenCol: [],
        listCol: []
      };

      // save references to DOM nodes used to display results in columns (gallery view) or rows (list view)
      resultsView.colNodes = {
        listCol: query('.results-full', resultsView.domNode)[0]
      };

      // initialise internationalisation
      resultsView.initSearchMessaging();

      // initialise pagination
      resultsView.initPaginator(mediator);

      // initialise shortlist observer
      resultsView.observeShortlist();

      // add event delegation methods (buttons)
      resultsView.delegateEvents();

      // inherit superclass methods
      resultsView.inherited(arguments);

      resultsView.moredetails=resultsView.searchMessaging[dojoConfig.site].moredetails;
      resultsView.shortlist=resultsView.searchMessaging[dojoConfig.site].shortlist;
      resultsView.currency = currency;

      // cache analytics values
      resultsView.analyticsData = {
        tag: resultsView.tag,
        number: resultsView.number,
        analyticsText: {
          url: "moreDetails",
          shortlist: "savetoShortlist"
        }
      };

      // render initial results
      resultsView.renderSearchResults(resultsView.holidays);
    },

    initPaginator: function (mediator) {
      var resultsView = this;

      // initialise pagination
      resultsView.paginator = new Paginator({area: 1200, attachOnLoad: false}, null);

      aspect.after(resultsView.paginator, "onScroll", function () {
        // detach listener > prevent from re-firing
        resultsView.paginator.detach();
        if (resultsView.hasMoreResults()) {
          mediator.fire('paginate', resultsView.currentPage, _.inc(resultsView.currentPage));
          resultsView.toggleLoaderNode(true);
        }
      });
    },

    updatePage: function(pageNumber) {
      var resultsView = this;
      resultsView.currentPage = pageNumber;
    },

    subscribeToChannels: function () {
      var resultsView = this;

      // toggle price
      topic.subscribe("tui:channel=priceToggle", function (message) {
        domClass.add(resultsView.domNode, message.add);
        domClass.remove(resultsView.domNode, message.remove);
      });

      // remove updating class
      topic.subscribe("tui:channel=lazyload", function () {
        domClass.remove(query(".search-results")[0], 'updating');
      });
    },

    renderSearchResults: function (packages, isPagination) {
      // Render given search results

      if (!packages || !packages.length) {
        _.debug("Error! Holiday package data is missing");
      }

      var resultsView = this, html;

      var holidays = _.map(lang.clone(packages), function (holiday, index) {
        holiday.componentId = resultsView.componentId;
        holiday.itinerary = formatDates(holiday.itinerary);

        holiday.price.discountPP=parseInt(holiday.price.discountPP);
        holiday.price.discount=parseInt(holiday.price.discount);
        //DE13065 fix
        if( holiday.accommodation.featureCodesAndValues.strapline && holiday.accommodation.featureCodesAndValues.strapline.length)
        {
        	holiday.accommodation.featureCodesAndValues.strapline[0] = holiday.accommodation.featureCodesAndValues.strapline[0].replace(/\u0027ll/g, "\\'");
        }
        !holiday.accommodation.imageUrl ? holiday.accommodation.imageUrl = imgPlaceholders(holiday.accommodation.imageUrl) : null;
        if (holiday.accommodation.ratings.tripAdvisorRating) {
          holiday.accommodation.ratings.tripAdvisorRatingKlass = taRatingClass(holiday.accommodation.ratings.tripAdvisorRating);
        }
        	holiday.accommodation.rooms = roomGrpSrvc.groupRooms(holiday.accommodation.rooms, "roomType");

        // map holiday brandType
        holiday.brandType = brandTypeMap(holiday.brandType);

        // map board-type price-diffs
        holiday.alternateBoard = priceDiffFormat(holiday.alternateBoard,  resultsView.currency);

        // due to django limitations have to check deposits here
        holiday.showDeposits = holiday.price.lowDepositExists || holiday.price.depositExists;

        if (!isPagination) {
          holiday.finPos = index + 1;
          holiday.uiPos = index + 1;
        }

        //removing the board basis details from the url if any and the actual board basis seen on the package added to the url at template formation
        var strIndex = holiday.accommodation.url.search("&bb=");
        if(strIndex >= 0){
        	holiday.accommodation.url = holiday.accommodation.url.substring(0,strIndex)+holiday.accommodation.url.substring(strIndex+6);
        }

        // todo: remove after data has been fixed
        holiday.accommodation.ratings.officialRating = tuiRatingClass(holiday.accommodation.ratings.officialRating);
        holiday.accommodation.differentiatedCodeModified = holiday.accommodation.differentiatedCode;

        if(dojoConfig.dualBrandSwitch && resultsView.tuiConfig[dojoConfig.site] && holiday.accommodation.differentiatedCode) {
          var modifiedCode = resultsView.tuiConfig[dojoConfig.site].dualBrandConfig.differentiatedCodeLarge[holiday.accommodation.differentiatedCode.toLowerCase().replace(/\s/g,"")];
          if(modifiedCode){
        	  holiday.accommodation.differentiatedCodeModified = modifiedCode;
          }
        }

        return holiday;
      });

      resultsView.onBeforePlaceHolidays(isPagination);


      // todo: Tempoarary fix for 131382 condition needs to be revisited better to handle it from back-end.

      if((_.isEmpty(resultsView.displayDiscount)) && (dojoConfig.site == "thomson")){
    	  resultsView.displayDiscount=false;
      }

      // todo: refactor
        // render template, include holidays and i8n texts
        html = resultsView.renderTmpl(null, lang.mixin(resultsView.analyticsData, {
          holidays: holidays,
          messages: resultsView.searchMessaging.searchResults,
          moreDetails:resultsView.moredetails,
          shortlist:resultsView.shortlist,
          shortlistEnabled:resultsView.shortlistEnabled,
          siteName: resultsView.siteName,
          currency : resultsView.currency,
displayDiscount: JSON.parse(resultsView.displayDiscount),
          saveButtonDisplay : (resultsView.siteName === 'falcon') ? false : true
        }));
        domConstruct.place(html, resultsView.colNodes.listCol, "last");

      isPagination ? resultsView.destroyResultWidgets(true) : null;
      parser.parse(resultsView.domNode);
      resultsView.onAfterPlaceHolidays(isPagination);
    },

    onBeforePlaceHolidays: function (isPagination) {
      // Runs before holiday packages are placed in the dom
      var resultsView = this;
      if (!isPagination) {
        // destroy all child widgets
        resultsView.destroyResultWidgets();

        // empty container
        _.each(resultsView.colNodes, function (col) {
          domConstruct.empty(col);
        });
      }
    },

    scrollToPosition: function(elementId){
    	var resultsView = this;
    	_.each(query('.search-result-item', resultsView.domNode), function(liElement){
    		var liPackage = dojo.getAttr(liElement,"data-package-id");
    		if(liPackage == elementId){
    			var pos = domGeom.position(liElement);
    			window.scrollTo(pos.x, (pos.y - 25));
    		}
    	});
    },

    onAfterPlaceHolidays: function (isPagination) {
      // Runs after holidays have been rendered, after they are placed in the dom
      var resultsView = this;
      // re-attach pagination listener
      resultsView.paginator.attach();
      resultsView.toggleLoaderNode(false);
      resultsView.refreshShortlistedPackages();
      holidayPackages = query('.search-result-item', resultsView.domNode);
	  		query(".list-view-buttons", resultsView.domNode).removeClass("hide").style("display", "block");
var clickedItem = sessionStorage.getItem("thClickedElement");
      if(clickedItem) {
    	  resultsView.scrollToPosition(clickedItem);
}
    },

    twinCenterHolidaysTagging: function(liItem, view){
    	var resultsView = this;
    	var div = query(".multi-center .details-wrap", liItem);
    	if ( div.length ){
    	  	  domClass.add(div[0], "GVTwinTaggable");
    	      var photoElem = dojo.query(".photo", div[0])[0];
    	      if(photoElem){ resultsView.tagElement(photoElem, "GVTwinPhotos"); }

    	      var videoElem = dojo.query(".video", div[0])[0];
    	      if(videoElem){ resultsView.tagElement(videoElem, "GVTwinVideo");  }
		}
    },

    resetTwinCentGallViewTagInstance: function(){
    	 _.each(query(".results-col-odd .GVTwinTaggable"), function (item, ind) {
       	  var photoElem = dojo.query(".photo", item)[0];
   	      if(photoElem){ domAttr.set(photoElem, "analytics-instance", ind*2+1); }

   	      var videoElem = dojo.query(".video", item)[0];
   	      if(videoElem){  domAttr.set(videoElem, "analytics-instance", ind*2+1);  }
         });
         _.each(query(".results-col-even .GVTwinTaggable"), function (item, ind) {
       	  var photoElem = dojo.query(".photo", item)[0];
   	      if(photoElem){ domAttr.set(photoElem, "analytics-instance", ind*2+2); }

   	      var videoElem = dojo.query(".video", item)[0];
   	      if(videoElem){  domAttr.set(videoElem, "analytics-instance", ind*2+2);  }
         });
    },

    destroyResultWidgets: function (preserveDom) {
      var resultsView = this;
      // destroy all child widgets (tooltips etc...)
      _.each(dijit.findWidgets(resultsView.domNode), function (w) {
        w.destroyRecursive(preserveDom);
      });
    },

    hasMoreResults: function () {
      return this.totalHolidaysAvailable > this.holidays.length;
    },

  // ----------------------------------------------------------------------------- error handling

    renderNoResultsPopup: function () {
      var resultsView = this;
      if (!resultsView.noResultsPopup) {
        domConstruct.place(resultsView.renderTmpl(noResultsTmpl, resultsView.searchMessaging.filterNoResults), document.body, "last");
        resultsView.noResultsPopup = new Modal();
      }
      resultsView.noResultsPopup.open();
    },

    renderAjaxErrorPopup: function () {
      var resultsView = this;
      if (!resultsView.ajaxErrorPopup) {
        domConstruct.place(resultsView.renderTmpl(ajaxErrorTmpl, resultsView.searchMessaging.ajaxError), document.body, "last");
        resultsView.ajaxErrorPopup = new Modal({
          widgetId: 'ajaxError',
          stopDefaultOnCancel: false,
          reloadOnClose: true
        });
      }
      resultsView.ajaxErrorPopup.open();
    },

    // ----------------------------------------------------------------------------- pagination methods

    toggleLoaderNode: function (action) {
      // summary:
      //		Adds page loading indicator to search results dom node
      var resultsView = this, html;
      action = action ? "remove" : "add";
      if (!resultsView.pageLoaderNode) {
        html = resultsView.renderTmpl(loaderTmpl);
        resultsView.pageLoaderNode = domConstruct.place(html, resultsView.domNode, "last");
      }
      domClass[action](resultsView.pageLoaderNode, "hide");
    },

    // ----------------------------------------------------------------------------- result item button methods

    delegateEvents: function () {
      // summary:
      //		delegate result item button events
      var resultsView = this;

      // add finPos and analytics data to img, h4 and cta urls
      on(resultsView.domNode, on.selector(".url", "click"), function (event) {
    	  resultsView.clickedAnchor = dojo.getAttr(event.target, 'data-package-id');
    	  sessionStorage.setItem("thClickedElement", resultsView.clickedAnchor);
        // Save current page state to SessionStorage
        topic.publish('tui/searchResults/Mediator/saveState', []);
      });

      // shortlist button events
      // handle add/remove
      on(resultsView.domNode, on.selector(".shortlist", "click"), function (event) {
        var itemNode = query(event.target).parents(".search-result-item")[0];
        if (resultsView.shortlistDisabled && !resultsView.isShortlisted(itemNode)) return;
        resultsView.updateShortlist(resultsView.isShortlisted(itemNode), domAttr.get(itemNode, "data-package-id"));
      });

      // change text on hover/focus
      on(resultsView.domNode, on.selector(".shortlist", mouse.enter), function (event) {
        var itemNode = query(event.target).parents(".search-result-item")[0];
        if (resultsView.isShortlisted(itemNode)) {
          query(".text", event.target).text("remove");
        }
      });

      // change text on mouseleave/blur
      on(resultsView.domNode, on.selector(".shortlist", mouse.leave), function (event) {
        var itemNode = query(event.target).parents(".search-result-item")[0];
        if (resultsView.isShortlisted(itemNode)) {
        	if(resultsView.siteName=="firstchoice"){
          query(".text", event.target).text("shortlisted");
        	}
        	else
        		query(".text", event.target).text("saved");
        }
      });

   // image data request on click on camera icon
      on(resultsView.domNode, on.selector(".js-img-data-req", "click"), function (event) {

       	 var srcElm = this;
       	// var parDiv = dojo.query(srcElm).parent()[0];
       	 var parLi = dojo.query(srcElm).closest("li.search-result-item")[0];
       	 var packageId = domAttr.get(srcElm, "data-product-id");
       	 var hotelName = domAttr.get(srcElm, "data-accomodation");
       	 var url = dojoConfig.paths.webRoot+"/media/" + hotelName + "-" +packageId;

        	dojo.xhrGet({
       	    // The URL to request
       	    url: url,
       	    handleAs: "json",
               error: function (err) {
                   if (dojoConfig.devDebug) {
                       console.error(err);
                   }
                   mediator.afterFailure();
               },
       	    load: function(result){

       	    	if( typeof result == "object" ){

       	    		dojo.query("div", parLi).removeClass("js-img-data-req");
          	    	 	dojo.query("div.photo", parLi).attr("data-dojo-props", "jsonData:" + JSON.stringify(result.model.jsonObj) );
          	    	 	dojo.query("div.photo", parLi).attr("data-dojo-type", "tui.widget.media.MediaPopup");

          	    	 	if( result.model.jsonObj.galleryVideos && result.model.jsonObj.galleryVideos.length ){

          	    	 		dojo.query("div.video", parLi).attr("data-dojo-props", "videoId:'" + result.model.jsonObj.galleryVideos[0].code + "'");
          	    	 		dojo.query("div.video", parLi).attr("data-dojo-type", "tui.widget.media.VideoPopup");
          	    	 	}
          	    	// _.each(dojo.query(".popup-icons", parLi), function(liObj){
          	    	//	dojo.parser.parse(liObj);
          	    	 //});

           	    	dojo.parser.parse(dojo.query(".popup-icons", parLi)[0]);
           	    	dojo.parser.parse(dojo.query(".popup-icons", parLi)[1]);
           	    	dojo.parser.parse(dojo.query(".popup-icons", parLi)[2]);

           	    	 on.emit(srcElm, "mousedown", {
            	    	    bubbles: true,
            	    	    cancelable: true
            	    	 });
       	    	}
       	    }
       	});
       });
    },

    // ----------------------------------------------------------------------------- shortlist methods

    observeShortlist: function () {
      // Observes shortlist store and updates button state accordingly
      var resultsView = this;
      var resultSet = resultsView.shortlistStore.getObservable().query();

      resultSet.observe(function (holidayPackage, remove, add) {
        // TODO: when we remove hover clone, change this back to find single package only (avoid loop below)
        var holidayNode = _.filter(query(".search-result-item", resultsView.domNode), function (item) {
          return domAttr.get(item, "data-package-id") === holidayPackage.id;
        });

        if (!holidayNode.length) return;

        _.each(holidayNode, function (holidayItem) {
          resultsView.toggleShortlistedStatus((add > -1), holidayItem);
        });
      });

      topic.subscribe("tui:channel=shortlistStoreDisabled", function (isDisabled) {
        resultsView.shortlistDisabled = isDisabled;
        resultsView.disableShortlistButtons();
      });
    },

    disableShortlistButtons: function () {
      var resultsView = this;
      var action = resultsView.shortlistDisabled ? "add" : "remove";
      _.each(query(".search-result-item", resultsView.domNode), function (holidayItem) {
        if (!resultsView.isShortlisted(holidayItem)) {
          domClass[action](query(".shortlist", holidayItem)[0], "disabled");
        }
      });
    },

    isShortlisted: function (node) {
      var resultsView = this;
      return resultsView.shortlistStore.getObservable().query({id: domAttr.get(node, "data-package-id")}).total > 0;
    },

    refreshShortlistedPackages: function () {
      var resultsView = this;
      _.each(query(".search-result-item", resultsView.domNode), function (holidayItem) {
        resultsView.toggleShortlistedStatus(resultsView.isShortlisted(holidayItem), holidayItem);
      });
      resultsView.disableShortlistButtons();
    },

    toggleShortlistedStatus: function (add, holidayItem) {
      var resultsView = this, button, action, text, textNode, productNode;
      // determine actions
      action = add ? "add" : "remove";

     if(resultsView.siteName=="firstchoice"){
      text = add ? "shortlisted" : "shortlist";
     }
     else
    	 text = add ? "saved" : "save";
      // query nodes
      button = query(".shortlist", holidayItem)[0];
      textNode = query(".text", button)[0];
      productNode = query(".product", holidayItem)[0];
      // do the magic
      domClass[action](productNode, "shortlist-saved");
      domClass[action](query(".shortlist", holidayItem)[0], "saved");
      query(".shortlist .text", holidayItem)[0].innerHTML = text;
      if( query(".shortlist", holidayItem)[1]){
    	  domClass[action](query(".shortlist", holidayItem)[1], "saved");
    	  query(".shortlist .text", holidayItem)[1].innerHTML = text;
      }
      //textNode.innerHTML = text;

      //analytics for List view saved holidays
      if( text == "saved" &&  query(".list-viewport ul.plist li.search-result-item").indexOf(holidayItem) != -1 ){
	      resultsView.tagElement(  query(".popup-icons .photo", holidayItem)[0], "LVSphotos" );
	      resultsView.tagElement(  query(".popup-icons .video", holidayItem)[0], "LVSvideo" );
	      var continueBtn = query(".cta-buttons .url", holidayItem)[0];
	      domAttr.remove(continueBtn, "analytics-text");
	      domAttr.remove(button, "analytics-text");
	      resultsView.tagElement(  button, "LVSremove" );
	      resultsView.tagElement(  continueBtn, "LVScontinue" );
      }
    },

    updateShortlist: function (action, packageId) {
      // summary:
      //		Request shortlist store to add/remove package
      var resultsView = this;
      action = !action ? "add" : "remove";
      resultsView.shortlistStore.getObservable().requestData(true, action, packageId);
    },

    // ----------------------------------------------------------------------------- mediator methods

    generateRequest: function (field) {
      var widget = this;
      //todo: convert to page numbers
      if (field === 'paginate') {
        return {
          'first': _.inc(widget.currentPage),
          'searchRequestType': 'paginate'
        };
      }
      return {
        'first': 1
      };
    },

    refresh: function (field, oldValue, newValue, response, isCached) {
      var resultsView = this,
          oldLength = resultsView.holidays.length,
          responseHolidays = lang.clone(response.searchResult.holidays);
      switch (field) {
        case 'paginate' :
          responseHolidays = _.map(responseHolidays, function (holiday) {
            holiday.finPos = ++oldLength;
            holiday.uiPos = holiday.finPos;
            return holiday;
          });
          resultsView.holidays = resultsView.holidays.concat(responseHolidays || []);
          resultsView.currentPage = newValue;
          resultsView.renderSearchResults(responseHolidays, true);
          break;
        default :
          resultsView.currentPage = 1;
          resultsView.totalHolidaysAvailable = response.searchResult.endecaResultsCount;
          resultsView.holidays = responseHolidays;
          resultsView.renderSearchResults(responseHolidays);
          break;
      }
    },

    clear: function () {},

    handleNoResults: function (name) {
      var resultsView = this;
      if (name && (name !== 'duration' && name !== 'rooms')) {
        resultsView.renderNoResultsPopup();
      }
    },

    tagMediaPopup: function(){
    	var resultsView = this;
    	resultsView.tagElements(query('.popup-icons photo', resultsView.domNode), "GVTwinPhotos");
    	resultsView.tagElements(query('.popup-icons photo', resultsView.domNode), "GVTwinVideo");

    }

  });
});
