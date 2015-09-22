define("tui/searchResults/view/SearchResultsComponent", [
  "dojo/_base/declare",
  "dojo/_base/connect",
  "dojo/_base/lang",
  "dojo/_base/xhr",
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
  "tui/search/nls/Searchi18nable"], function (declare, connect, lang, xhr, parser, aspect, topic, on, mouse, query,
                                              domStyle, domAttr, domConstruct, domClass, domGeom, resultTmpl, noResultsTmpl,
                                              ajaxErrorTmpl, loaderTmpl, Paginator, Modal, roomGrpSrvc, sessionStorage, TuiConfig) {

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

  return declare("tui.searchResults.view.SearchResultsComponent", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable, tui.search.nls.Searchi18nable], {

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

    subscribableMethods: ["renderAjaxErrorPopup", "renderNoResultsPopup", "setView", "updatePage"],

    viewMode: null,

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
      resultsView.aniteSwitch = model.aniteSwitch;


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
        oddCol: query('.results-col-odd', resultsView.domNode)[0],
        evenCol: query('.results-col-even', resultsView.domNode)[0],
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

      // watch view-mode for changes
      resultsView.watchView();
      resultsView.setView(resultsView.getView());
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
    	_.each(query('.cta-buttons .cta', resultsView.domNode), function(accomUrl){
    		var continueUrl = dojo.getAttr(accomUrl,"href");
    		 var strIndex = continueUrl.search("&price=");
    		    if(strIndex >= 0){
    		    	continueUrl = continueUrl.substring(0,strIndex)+"&price="+message.add;
    	        }
    		    else {
    		    	continueUrl = continueUrl+"&price="+message.add;
    		    }
    		    dojo.setAttr(accomUrl, "href", continueUrl);
    	});
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
	  console.log(resultsView);
      if (resultsView.getView() === 'list') {
        // render template, include holidays and i8n texts
        html = resultsView.renderTmpl(null, lang.mixin(resultsView.analyticsData, {
          holidays: holidays,
          messages: resultsView.searchMessaging.searchResults,
          moreDetails:resultsView.moredetails,
          shortlist:resultsView.shortlist,
          aniteSwitch:resultsView.aniteSwitch,
          siteName: resultsView.siteName,
          currency : resultsView.currency,
displayDiscount: JSON.parse(resultsView.displayDiscount),
          saveButtonDisplay : (resultsView.siteName === 'falcon') ? false : true
        }));
        domConstruct.place(html, resultsView.colNodes.listCol, "last");
      } else {
        // sort holidays and then render each column
        var htmlOdd, htmlEven, sortedHolidays = {
          oddCol: [],
          evenCol: []
        };
        _.each(holidays, function (holiday) {
          if (parseInt(holiday.uiPos, 10) % 2 !== 0) {
            sortedHolidays.oddCol.push(holiday);
          } else {
            sortedHolidays.evenCol.push(holiday);
          }
        });
        htmlOdd = resultsView.renderTmpl(null, lang.mixin(resultsView.analyticsData, {
          holidays: sortedHolidays.oddCol,
          messages: resultsView.searchMessaging.searchResults,
          displayDiscount: JSON.parse(resultsView.displayDiscount)
        }));
        htmlEven = resultsView.renderTmpl(null, lang.mixin(resultsView.analyticsData, {
          holidays: sortedHolidays.evenCol,
          messages: resultsView.searchMessaging.searchResults,
          displayDiscount: JSON.parse(resultsView.displayDiscount)
        }));
        domConstruct.place(htmlOdd, resultsView.colNodes.oddCol, "last");
		if(htmlEven != ''){
        domConstruct.place(htmlEven, resultsView.colNodes.evenCol, "last");
      }
      }

      isPagination ? resultsView.destroyResultWidgets(true) : null;
      parser.parse(resultsView.domNode);
      resultsView.onAfterPlaceHolidays(isPagination);

      var togglePrice = "";
      dojo.hasClass(resultsView.domNode, "total") ? togglePrice = "total" :  togglePrice = "pp";
      	_.each(query('.cta-buttons .cta', resultsView.domNode), function(accomUrl){
      		var continueUrl = dojo.getAttr(accomUrl,"href");
      		 var strIndex = continueUrl.search("&price=");
      		    if(strIndex >= 0){
      		    	continueUrl = continueUrl.substring(0,strIndex)+"&price="+togglePrice;
      	        }
      		    else {
      		    	continueUrl = continueUrl+"&price="+togglePrice;
      		    }
      		    dojo.setAttr(accomUrl, "href", continueUrl);
      });
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
      var view = resultsView.getView();
      if( view == "gallery" ){
          _.each(holidayPackages, function (item) {
            	resultsView.twinCenterHolidaysTagging(item, "gallery");
            });
          resultsView.resetTwinCentGallViewTagInstance();
  		query(".list-view-buttons", resultsView.domNode).addClass("hide").style("display", "none");
  		query(".gallery-view-buttons", resultsView.domNode).removeClass("hide").style("display", "block");
	  	}else{
	  		query(".list-view-buttons", resultsView.domNode).removeClass("hide").style("display", "block");
	  		query(".gallery-view-buttons", resultsView.domNode).addClass("hide").style("display", "none");
	  	}
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

    	      var shortlistElem = dojo.query(".gallery-view-buttons .shortlist", div[0])[0];
    	      if(shortlistElem){ resultsView.tagElement(shortlistElem, "GVTwinSaveShortlist");  }

    	      var moreDetElem = dojo.query(".gallery-view-buttons .url", div[0])[0];
    	      if(moreDetElem){ resultsView.tagElement(moreDetElem, "GVTwinMoreDetails");  }
		}
    },

    resetTwinCentGallViewTagInstance: function(){
    	 _.each(query(".results-col-odd .GVTwinTaggable"), function (item, ind) {
       	  var photoElem = dojo.query(".photo", item)[0];
   	      if(photoElem){ domAttr.set(photoElem, "analytics-instance", ind*2+1); }

   	      var videoElem = dojo.query(".video", item)[0];
   	      if(videoElem){  domAttr.set(videoElem, "analytics-instance", ind*2+1);  }

   	      var shortlistElem = dojo.query(".gallery-view-buttons .shortlist", item)[0];
   	      if(shortlistElem){  domAttr.set(shortlistElem, "analytics-instance", ind*2+1); }

   	      var moreDetElem = dojo.query(".gallery-view-buttons .url", item)[0];
   	      if(moreDetElem){  domAttr.set(moreDetElem, "analytics-instance", ind*2+1);  }
         });
         _.each(query(".results-col-even .GVTwinTaggable"), function (item, ind) {
       	  var photoElem = dojo.query(".photo", item)[0];
   	      if(photoElem){ domAttr.set(photoElem, "analytics-instance", ind*2+2); }

   	      var videoElem = dojo.query(".video", item)[0];
   	      if(videoElem){  domAttr.set(videoElem, "analytics-instance", ind*2+2);  }

   	      var shortlistElem = dojo.query(".gallery-view-buttons .shortlist", item)[0];
   	      if(shortlistElem){  domAttr.set(shortlistElem, "analytics-instance", ind*2+2); }

   	      var moreDetElem = dojo.query(".gallery-view-buttons .url", item)[0];
   	      if(moreDetElem){  domAttr.set(moreDetElem, "analytics-instance", ind*2+2);  }
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

    getView: function () {
      var resultsView = this;
      return resultsView.get('viewMode');
    },

    setView: function (value) {
      var resultsView = this;
      resultsView.set('viewMode', value);
      resultsView.resetTwinCentGallViewTagInstance();
      resultsView.setGalleryViewImages(false);
    },


    setGalleryViewImages: function( isPagination){
    	 var resultsView = this, view = resultsView.getView();
    	 if(isPagination){
    		 domClass.remove(resultsView.domNode,"js-img-rendered");
    	 }
    	 if( !domClass.contains(resultsView.domNode,"js-img-rendered") && view == "gallery"){
       	  _.each(query(".image-container img", resultsView.domNode), function(imgObj){
       		  //domAttr.set(obj, "data-dojo-type", "tui.widget.LazyLoadImage");
       		  if( !domAttr.has(imgObj, "data-dojo-type" )){
       		  var src = domAttr.get(imgObj, "data-gallery-view-url"); //"http://newmedia.thomson.co.uk/live/vol/2/87e454012d1fd2a2da9ce83ff0870f29af0c1eca/488x274/web/AFRICAEGYPTEGYPT-REDSEASHARMELSHEIKHRES_001544SENSATORISHARMELSHEIKH.jpg"; //
       		  domAttr.set(imgObj, {"data-dojo-type": "tui.widget.LazyLoadImage", "data-dojo-props": "source:'"+ src +"'"});
       		  parser.parse(query(imgObj).parent()[0]);
       		  }
       	  });
       	  domClass.add(resultsView.domNode,"js-img-rendered");
         }
    },

    watchView: function () {
      var resultsView = this;
      resultsView.watch('viewMode', function (name, oldValue, value) {
        if (oldValue !== value) {
          resultsView.sortDom(value);
        }
      });
    },

    sortDom: function (mode) {
      var resultsView = this,
          holidayPackages = query('.search-result-item', resultsView.domNode);

      switch (mode) {
        case "list":
        	query(".list-view-buttons", resultsView.domNode).removeClass("hide").style("display", "block");
    		query(".gallery-view-buttons", resultsView.domNode).addClass("hide").style("display", "none");
          resultsView.holidayNodes.listCol = _.sortBy(holidayPackages, function (item) {
            return parseInt(domAttr.get(item, 'data-package-sort'), 10);
          });
          resultsView.holidayNodes.oddCol.length = 0;
          resultsView.holidayNodes.evenCol.length = 0;
          break;
        case "gallery":
          _.each(holidayPackages, function (item) {
        	resultsView.twinCenterHolidaysTagging(item, "gallery");
        	query(".list-view-buttons", resultsView.domNode).addClass("hide").style("display", "none");
    		query(".gallery-view-buttons", resultsView.domNode).removeClass("hide").style("display", "block");
            if (parseInt(domAttr.get(item, 'data-package-sort'), 10) % 2 !== 0) {
              // is odd
              resultsView.holidayNodes.oddCol.push(item);
            } else {
              resultsView.holidayNodes.evenCol.push(item);
            }
            resultsView.holidayNodes.listCol.length = 0;
          });
          break;
      }
      resultsView.placeSortedDom();
    },

    placeSortedDom: function () {
      var resultsView = this;
      _.each(resultsView.holidayNodes, function (col, colName) {
        _.each(col, function (item) {
          domConstruct.place(item, resultsView.colNodes[colName], "last");
        });
      });
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
     
       	 var Latinise={};
       	Latinise.latin_map={"Á":"A","Ă":"A","Ắ":"A","Ặ":"A","Ằ":"A","Ẳ":"A","Ẵ":"A","Ǎ":"A","Â":"A","Ấ":"A","Ậ":"A","Ầ":"A","Ẩ":"A","Ẫ":"A","Ä":"A","Ǟ":"A","Ȧ":"A","Ǡ":"A","Ạ":"A","Ȁ":"A","À":"A","Ả":"A","Ȃ":"A","Ā":"A","Ą":"A","Å":"A","Ǻ":"A","Ḁ":"A","Ⱥ":"A","Ã":"A","Ꜳ":"AA","Æ":"AE","Ǽ":"AE","Ǣ":"AE","Ꜵ":"AO","Ꜷ":"AU","Ꜹ":"AV","Ꜻ":"AV","Ꜽ":"AY","Ḃ":"B","Ḅ":"B","Ɓ":"B","Ḇ":"B","Ƀ":"B","Ƃ":"B","Ć":"C","Č":"C","Ç":"C","Ḉ":"C","Ĉ":"C","Ċ":"C","Ƈ":"C","Ȼ":"C","Ď":"D","Ḑ":"D","Ḓ":"D","Ḋ":"D","Ḍ":"D","Ɗ":"D","Ḏ":"D","ǲ":"D","ǅ":"D","Đ":"D","Ƌ":"D","Ǳ":"DZ","Ǆ":"DZ","É":"E","Ĕ":"E","Ě":"E","Ȩ":"E","Ḝ":"E","Ê":"E","Ế":"E","Ệ":"E","Ề":"E","Ể":"E","Ễ":"E","Ḙ":"E","Ë":"E","Ė":"E","Ẹ":"E","Ȅ":"E","È":"E","Ẻ":"E","Ȇ":"E","Ē":"E","Ḗ":"E","Ḕ":"E","Ę":"E","Ɇ":"E","Ẽ":"E","Ḛ":"E","Ꝫ":"ET","Ḟ":"F","Ƒ":"F","Ǵ":"G","Ğ":"G","Ǧ":"G","Ģ":"G","Ĝ":"G","Ġ":"G","Ɠ":"G","Ḡ":"G","Ǥ":"G","Ḫ":"H","Ȟ":"H","Ḩ":"H","Ĥ":"H","Ⱨ":"H","Ḧ":"H","Ḣ":"H","Ḥ":"H","Ħ":"H","Í":"I","Ĭ":"I","Ǐ":"I","Î":"I","Ï":"I","Ḯ":"I","İ":"I","Ị":"I","Ȉ":"I","Ì":"I","Ỉ":"I","Ȋ":"I","Ī":"I","Į":"I","Ɨ":"I","Ĩ":"I","Ḭ":"I","Ꝺ":"D","Ꝼ":"F","Ᵹ":"G","Ꞃ":"R","Ꞅ":"S","Ꞇ":"T","Ꝭ":"IS","Ĵ":"J","Ɉ":"J","Ḱ":"K","Ǩ":"K","Ķ":"K","Ⱪ":"K","Ꝃ":"K","Ḳ":"K","Ƙ":"K","Ḵ":"K","Ꝁ":"K","Ꝅ":"K","Ĺ":"L","Ƚ":"L","Ľ":"L","Ļ":"L","Ḽ":"L","Ḷ":"L","Ḹ":"L","Ⱡ":"L","Ꝉ":"L","Ḻ":"L","Ŀ":"L","Ɫ":"L","ǈ":"L","Ł":"L","Ǉ":"LJ","Ḿ":"M","Ṁ":"M","Ṃ":"M","Ɱ":"M","Ń":"N","Ň":"N","Ņ":"N","Ṋ":"N","Ṅ":"N","Ṇ":"N","Ǹ":"N","Ɲ":"N","Ṉ":"N","Ƞ":"N","ǋ":"N","Ñ":"N","Ǌ":"NJ","Ó":"O","Ŏ":"O","Ǒ":"O","Ô":"O","Ố":"O","Ộ":"O","Ồ":"O","Ổ":"O","Ỗ":"O","Ö":"O","Ȫ":"O","Ȯ":"O","Ȱ":"O","Ọ":"O","Ő":"O","Ȍ":"O","Ò":"O","Ỏ":"O","Ơ":"O","Ớ":"O","Ợ":"O","Ờ":"O","Ở":"O","Ỡ":"O","Ȏ":"O","Ꝋ":"O","Ꝍ":"O","Ō":"O","Ṓ":"O","Ṑ":"O","Ɵ":"O","Ǫ":"O","Ǭ":"O","Ø":"O","Ǿ":"O","Õ":"O","Ṍ":"O","Ṏ":"O","Ȭ":"O","Ƣ":"OI","Ꝏ":"OO","Ɛ":"E","Ɔ":"O","Ȣ":"OU","Ṕ":"P","Ṗ":"P","Ꝓ":"P","Ƥ":"P","Ꝕ":"P","Ᵽ":"P","Ꝑ":"P","Ꝙ":"Q","Ꝗ":"Q","Ŕ":"R","Ř":"R","Ŗ":"R","Ṙ":"R","Ṛ":"R","Ṝ":"R","Ȑ":"R","Ȓ":"R","Ṟ":"R","Ɍ":"R","Ɽ":"R","Ꜿ":"C","Ǝ":"E","Ś":"S","Ṥ":"S","Š":"S","Ṧ":"S","Ş":"S","Ŝ":"S","Ș":"S","Ṡ":"S","Ṣ":"S","Ṩ":"S","Ť":"T","Ţ":"T","Ṱ":"T","Ț":"T","Ⱦ":"T","Ṫ":"T","Ṭ":"T","Ƭ":"T","Ṯ":"T","Ʈ":"T","Ŧ":"T","Ɐ":"A","Ꞁ":"L","Ɯ":"M","Ʌ":"V","Ꜩ":"TZ","Ú":"U","Ŭ":"U","Ǔ":"U","Û":"U","Ṷ":"U","Ü":"U","Ǘ":"U","Ǚ":"U","Ǜ":"U","Ǖ":"U","Ṳ":"U","Ụ":"U","Ű":"U","Ȕ":"U","Ù":"U","Ủ":"U","Ư":"U","Ứ":"U","Ự":"U","Ừ":"U","Ử":"U","Ữ":"U","Ȗ":"U","Ū":"U","Ṻ":"U","Ų":"U","Ů":"U","Ũ":"U","Ṹ":"U","Ṵ":"U","Ꝟ":"V","Ṿ":"V","Ʋ":"V","Ṽ":"V","Ꝡ":"VY","Ẃ":"W","Ŵ":"W","Ẅ":"W","Ẇ":"W","Ẉ":"W","Ẁ":"W","Ⱳ":"W","Ẍ":"X","Ẋ":"X","Ý":"Y","Ŷ":"Y","Ÿ":"Y","Ẏ":"Y","Ỵ":"Y","Ỳ":"Y","Ƴ":"Y","Ỷ":"Y","Ỿ":"Y","Ȳ":"Y","Ɏ":"Y","Ỹ":"Y","Ź":"Z","Ž":"Z","Ẑ":"Z","Ⱬ":"Z","Ż":"Z","Ẓ":"Z","Ȥ":"Z","Ẕ":"Z","Ƶ":"Z","Ĳ":"IJ","Œ":"OE","ᴀ":"A","ᴁ":"AE","ʙ":"B","ᴃ":"B","ᴄ":"C","ᴅ":"D","ᴇ":"E","ꜰ":"F","ɢ":"G","ʛ":"G","ʜ":"H","ɪ":"I","ʁ":"R","ᴊ":"J","ᴋ":"K","ʟ":"L","ᴌ":"L","ᴍ":"M","ɴ":"N","ᴏ":"O","ɶ":"OE","ᴐ":"O","ᴕ":"OU","ᴘ":"P","ʀ":"R","ᴎ":"N","ᴙ":"R","ꜱ":"S","ᴛ":"T","ⱻ":"E","ᴚ":"R","ᴜ":"U","ᴠ":"V","ᴡ":"W","ʏ":"Y","ᴢ":"Z","á":"a","ă":"a","ắ":"a","ặ":"a","ằ":"a","ẳ":"a","ẵ":"a","ǎ":"a","â":"a","ấ":"a","ậ":"a","ầ":"a","ẩ":"a","ẫ":"a","ä":"a","ǟ":"a","ȧ":"a","ǡ":"a","ạ":"a","ȁ":"a","à":"a","ả":"a","ȃ":"a","ā":"a","ą":"a","ᶏ":"a","ẚ":"a","å":"a","ǻ":"a","ḁ":"a","ⱥ":"a","ã":"a","ꜳ":"aa","æ":"ae","ǽ":"ae","ǣ":"ae","ꜵ":"ao","ꜷ":"au","ꜹ":"av","ꜻ":"av","ꜽ":"ay","ḃ":"b","ḅ":"b","ɓ":"b","ḇ":"b","ᵬ":"b","ᶀ":"b","ƀ":"b","ƃ":"b","ɵ":"o","ć":"c","č":"c","ç":"c","ḉ":"c","ĉ":"c","ɕ":"c","ċ":"c","ƈ":"c","ȼ":"c","ď":"d","ḑ":"d","ḓ":"d","ȡ":"d","ḋ":"d","ḍ":"d","ɗ":"d","ᶑ":"d","ḏ":"d","ᵭ":"d","ᶁ":"d","đ":"d","ɖ":"d","ƌ":"d","ı":"i","ȷ":"j","ɟ":"j","ʄ":"j","ǳ":"dz","ǆ":"dz","é":"e","ĕ":"e","ě":"e","ȩ":"e","ḝ":"e","ê":"e","ế":"e","ệ":"e","ề":"e","ể":"e","ễ":"e","ḙ":"e","ë":"e","ė":"e","ẹ":"e","ȅ":"e","è":"e","ẻ":"e","ȇ":"e","ē":"e","ḗ":"e","ḕ":"e","ⱸ":"e","ę":"e","ᶒ":"e","ɇ":"e","ẽ":"e","ḛ":"e","ꝫ":"et","ḟ":"f","ƒ":"f","ᵮ":"f","ᶂ":"f","ǵ":"g","ğ":"g","ǧ":"g","ģ":"g","ĝ":"g","ġ":"g","ɠ":"g","ḡ":"g","ᶃ":"g","ǥ":"g","ḫ":"h","ȟ":"h","ḩ":"h","ĥ":"h","ⱨ":"h","ḧ":"h","ḣ":"h","ḥ":"h","ɦ":"h","ẖ":"h","ħ":"h","ƕ":"hv","í":"i","ĭ":"i","ǐ":"i","î":"i","ï":"i","ḯ":"i","ị":"i","ȉ":"i","ì":"i","ỉ":"i","ȋ":"i","ī":"i","į":"i","ᶖ":"i","ɨ":"i","ĩ":"i","ḭ":"i","ꝺ":"d","ꝼ":"f","ᵹ":"g","ꞃ":"r","ꞅ":"s","ꞇ":"t","ꝭ":"is","ǰ":"j","ĵ":"j","ʝ":"j","ɉ":"j","ḱ":"k","ǩ":"k","ķ":"k","ⱪ":"k","ꝃ":"k","ḳ":"k","ƙ":"k","ḵ":"k","ᶄ":"k","ꝁ":"k","ꝅ":"k","ĺ":"l","ƚ":"l","ɬ":"l","ľ":"l","ļ":"l","ḽ":"l","ȴ":"l","ḷ":"l","ḹ":"l","ⱡ":"l","ꝉ":"l","ḻ":"l","ŀ":"l","ɫ":"l","ᶅ":"l","ɭ":"l","ł":"l","ǉ":"lj","ſ":"s","ẜ":"s","ẛ":"s","ẝ":"s","ḿ":"m","ṁ":"m","ṃ":"m","ɱ":"m","ᵯ":"m","ᶆ":"m","ń":"n","ň":"n","ņ":"n","ṋ":"n","ȵ":"n","ṅ":"n","ṇ":"n","ǹ":"n","ɲ":"n","ṉ":"n","ƞ":"n","ᵰ":"n","ᶇ":"n","ɳ":"n","ñ":"n","ǌ":"nj","ó":"o","ŏ":"o","ǒ":"o","ô":"o","ố":"o","ộ":"o","ồ":"o","ổ":"o","ỗ":"o","ö":"o","ȫ":"o","ȯ":"o","ȱ":"o","ọ":"o","ő":"o","ȍ":"o","ò":"o","ỏ":"o","ơ":"o","ớ":"o","ợ":"o","ờ":"o","ở":"o","ỡ":"o","ȏ":"o","ꝋ":"o","ꝍ":"o","ⱺ":"o","ō":"o","ṓ":"o","ṑ":"o","ǫ":"o","ǭ":"o","ø":"o","ǿ":"o","õ":"o","ṍ":"o","ṏ":"o","ȭ":"o","ƣ":"oi","ꝏ":"oo","ɛ":"e","ᶓ":"e","ɔ":"o","ᶗ":"o","ȣ":"ou","ṕ":"p","ṗ":"p","ꝓ":"p","ƥ":"p","ᵱ":"p","ᶈ":"p","ꝕ":"p","ᵽ":"p","ꝑ":"p","ꝙ":"q","ʠ":"q","ɋ":"q","ꝗ":"q","ŕ":"r","ř":"r","ŗ":"r","ṙ":"r","ṛ":"r","ṝ":"r","ȑ":"r","ɾ":"r","ᵳ":"r","ȓ":"r","ṟ":"r","ɼ":"r","ᵲ":"r","ᶉ":"r","ɍ":"r","ɽ":"r","ↄ":"c","ꜿ":"c","ɘ":"e","ɿ":"r","ś":"s","ṥ":"s","š":"s","ṧ":"s","ş":"s","ŝ":"s","ș":"s","ṡ":"s","ṣ":"s","ṩ":"s","ʂ":"s","ᵴ":"s","ᶊ":"s","ȿ":"s","ɡ":"g","ᴑ":"o","ᴓ":"o","ᴝ":"u","ť":"t","ţ":"t","ṱ":"t","ț":"t","ȶ":"t","ẗ":"t","ⱦ":"t","ṫ":"t","ṭ":"t","ƭ":"t","ṯ":"t","ᵵ":"t","ƫ":"t","ʈ":"t","ŧ":"t","ᵺ":"th","ɐ":"a","ᴂ":"ae","ǝ":"e","ᵷ":"g","ɥ":"h","ʮ":"h","ʯ":"h","ᴉ":"i","ʞ":"k","ꞁ":"l","ɯ":"m","ɰ":"m","ᴔ":"oe","ɹ":"r","ɻ":"r","ɺ":"r","ⱹ":"r","ʇ":"t","ʌ":"v","ʍ":"w","ʎ":"y","ꜩ":"tz","ú":"u","ŭ":"u","ǔ":"u","û":"u","ṷ":"u","ü":"u","ǘ":"u","ǚ":"u","ǜ":"u","ǖ":"u","ṳ":"u","ụ":"u","ű":"u","ȕ":"u","ù":"u","ủ":"u","ư":"u","ứ":"u","ự":"u","ừ":"u","ử":"u","ữ":"u","ȗ":"u","ū":"u","ṻ":"u","ų":"u","ᶙ":"u","ů":"u","ũ":"u","ṹ":"u","ṵ":"u","ᵫ":"ue","ꝸ":"um","ⱴ":"v","ꝟ":"v","ṿ":"v","ʋ":"v","ᶌ":"v","ⱱ":"v","ṽ":"v","ꝡ":"vy","ẃ":"w","ŵ":"w","ẅ":"w","ẇ":"w","ẉ":"w","ẁ":"w","ⱳ":"w","ẘ":"w","ẍ":"x","ẋ":"x","ᶍ":"x","ý":"y","ŷ":"y","ÿ":"y","ẏ":"y","ỵ":"y","ỳ":"y","ƴ":"y","ỷ":"y","ỿ":"y","ȳ":"y","ẙ":"y","ɏ":"y","ỹ":"y","ź":"z","ž":"z","ẑ":"z","ʑ":"z","ⱬ":"z","ż":"z","ẓ":"z","ȥ":"z","ẕ":"z","ᵶ":"z","ᶎ":"z","ʐ":"z","ƶ":"z","ɀ":"z","ﬀ":"ff","ﬃ":"ffi","ﬄ":"ffl","ﬁ":"fi","ﬂ":"fl","ĳ":"ij","œ":"oe","ﬆ":"st","ₐ":"a","ₑ":"e","ᵢ":"i","ⱼ":"j","ₒ":"o","ᵣ":"r","ᵤ":"u","ᵥ":"v","ₓ":"x"};
       	String.prototype.latinise=function(){return this.replace(/[^A-Za-z0-9\[\] ]/g,function(a){return Latinise.latin_map[a]||a})};
       	String.prototype.latinize=String.prototype.latinise;
     

       	 
       	 var url = dojoConfig.paths.webRoot+ "/media/" + hotelName.latinize() + "-" +packageId;


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
          	    	 	dojo.query("div.photo", parLi).attr("data-dojo-type", (dojo.config.site == "cruise" || dojo.config.site == "falcon") ? "tui.widget.media.MediaPopup" : "tui.widget.media.MediaPopupSwipe");

          	    	 	if( result.model.jsonObj.galleryVideos && result.model.jsonObj.galleryVideos.length ){

          	    	 		dojo.query("div.video", parLi).attr("data-dojo-props", 'videoId:"' + result.model.jsonObj.galleryVideos[0].code + '", videoPlayerId:"' + resultsView.videoPlayerId + '", videoPlayerKey:"' + resultsView.videoPlayerKey + '"');

          	    	 		dojo.query("div.video", parLi).attr("data-dojo-type", "tui.widget.media.VideoPopup");
          	    	 	}
          	    	// _.each(dojo.query(".popup-icons", parLi), function(liObj){
          	    	//	dojo.parser.parse(liObj);
          	    	 //});

          	    	dojo.query(".popup-icons", parLi)[0] && dojo.parser.parse(dojo.query(".popup-icons", parLi)[0]);
           	    	dojo.query(".popup-icons", parLi)[1] && dojo.parser.parse(dojo.query(".popup-icons", parLi)[1]);
           	    	dojo.query(".popup-icons", parLi)[2] && dojo.parser.parse(dojo.query(".popup-icons", parLi)[2]);

           	    	 on.emit(srcElm, "mousedown", {
            	    	    bubbles: true,
            	    	    cancelable: true
            	    	 });
       	    	}
       	    }
       	});
       });

      // if more-details content doesn't fit, clone target on hover and expand
      on(resultsView.domNode, on.selector(".search-result-item .product", mouse.enter), function (event) {
        if (resultsView.getView() === 'list') return;
        var testNode = query('.more-details', event.target)[0],
            testHeight = domGeom.position(testNode).h;

        if (testHeight > 184) {
          var viewPort = query('.product-viewport', event.target)[0],
              deetWrap = query('.details-wrap', event.target)[0],
              pad = (testHeight - 184) + 16;
            domClass.add(event.target, 'expanded');
            domStyle.set(event.target, 'height', _.pixels(resultsView.defaultGalleryHeight + pad));
            domStyle.set(viewPort, 'height', _.pixels(resultsView.defaultGalleryHeight + pad));
            domStyle.set(deetWrap, 'minHeight', _.pixels((resultsView.defaultGalleryHeight + pad) - 24));
        }
      });

      on(resultsView.domNode, on.selector(".search-result-item .product", mouse.leave), function (event) {
        if (resultsView.getView() === 'list') return;
        if (domAttr.has(event.target, 'style')) {
          var viewPort = query('.product-viewport', event.target)[0],
              deetWrap = query('.details-wrap', event.target)[0];

            domClass.remove(event.target, 'expanded');
            domAttr.remove(event.target, 'style');
            domAttr.remove(viewPort, 'style');
            domAttr.remove(deetWrap, 'style');
        }
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
        	dojo.config.site == "cruise" ? resultsView.totalHolidaysAvailable = response.searchResult.endecaResultsCount : "";
          responseHolidays = _.map(responseHolidays, function (holiday) {
            holiday.finPos = ++oldLength;
            holiday.uiPos = holiday.finPos;
            return holiday;
          });
          resultsView.holidays = resultsView.holidays.concat(responseHolidays || []);
          resultsView.currentPage = newValue;
          resultsView.renderSearchResults(responseHolidays, true);
          resultsView.setGalleryViewImages(true);
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
